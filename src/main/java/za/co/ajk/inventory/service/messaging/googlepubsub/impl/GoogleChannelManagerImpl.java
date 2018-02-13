package za.co.ajk.inventory.service.messaging.googlepubsub.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gcp.pubsub.core.PubSubTemplate;
import org.springframework.cloud.gcp.pubsub.support.GcpHeaders;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.gcp.pubsub.AckMode;
import org.springframework.integration.gcp.pubsub.inbound.PubSubInboundChannelAdapter;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.ChannelInterceptorAdapter;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.cloud.pubsub.v1.AckReplyConsumer;
import com.google.protobuf.ByteString;
import com.google.pubsub.v1.PubsubMessage;
import za.co.ajk.inventory.config.PubSubMessagingProperties;
import za.co.ajk.inventory.enums.PubSubMessageType;
import za.co.ajk.inventory.service.messaging.IMMessageProcessor;
import za.co.ajk.inventory.service.messaging.InterModulePubSubMessage;
import za.co.ajk.inventory.service.messaging.googlepubsub.GoogleChannelManager;


@Component
@Configuration
public class GoogleChannelManagerImpl implements GoogleChannelManager {
    
    private final Logger log = LoggerFactory.getLogger(getClass());
    
    private ObjectMapper objectMapper;
    
    @Value("${spring.application.name}")
    private String applicationModuleName;
    
    @Autowired
    private ChannelInterceptorAdapter channelInterceptorAdapter;
    
    @Autowired
    private IMMessageProcessor imMessageProcessor;
    
    private PubSubTemplate pubSubTemplate;
    private PubSubMessagingProperties pubSubMessagingProperties;
    
    public GoogleChannelManagerImpl(PubSubMessagingProperties pubSubMessagingProperties,
                                    PubSubTemplate pubSubTemplate,
                                    ObjectMapper objectMapper) {
        this.pubSubMessagingProperties = pubSubMessagingProperties;
        this.pubSubTemplate = pubSubTemplate;
        this.objectMapper = objectMapper;
        this.objectMapper.findAndRegisterModules();
    }
    
    /**
     * Utility method that will take a InterModulePubSubMessage object, add it to a PubSubMessage and route it to the required topics.
     *
     * @param interModulePubSubMessage
     */
    @Override
    public void pubSubMessageSender(InterModulePubSubMessage interModulePubSubMessage) {
        
        try {
            ByteString data = ByteString.copyFromUtf8(objectMapper.writeValueAsString(interModulePubSubMessage));
            PubsubMessage mes = PubsubMessage.newBuilder()
                .putAttributes("PubSubMessageType",
                    interModulePubSubMessage.getPubSubMessageType().getMessageTypeCode())
                .setData(data)
                .build();
            
            List<String> targetTopicsList = getTargetTopicNames(interModulePubSubMessage.getPubSubMessageType());
            
            targetTopicsList.forEach(topic -> {
                
                ListenableFuture<String> event = pubSubTemplate.publish(topic, mes);
                try {
                    String id = event.get(5000l, TimeUnit.MILLISECONDS);
                    log.info("Message ID : " + id);
                    
                } catch (InterruptedException | TimeoutException | ExecutionException ie) {
                    log.error("Error retrieving messageId for message submitted", ie);
                }
                
            });
            
        } catch (JsonProcessingException jpe) {
            log.error("Error submitting message to GooglePubSub", jpe);
        }
        
    }
    
    @Bean
    public PubSubInboundChannelAdapter messageChannelAdapterGenericSub(
        @Qualifier("pubsubInputChannel") MessageChannel inputChannel) {
        try {
            PubSubInboundChannelAdapter adapter =
                new PubSubInboundChannelAdapter(pubSubTemplate, "InventoryGenericSub");
            adapter
                .setOutputChannel(
                    inputChannel); // looks like the channel to ack on (thus the input channel - confusing!)
            adapter.setAckMode(AckMode.MANUAL);
            return adapter;
        } catch (Exception ex) {
            ex.printStackTrace();
            System.exit(-1);
            return null;
        }
    }
    
    @Bean
    public PubSubInboundChannelAdapter messageChannelAdapterBillingSub(
        @Qualifier("pubsubInputChannel")
            MessageChannel inputChannel) {
        try {
            PubSubInboundChannelAdapter adapter =
                new PubSubInboundChannelAdapter(pubSubTemplate, "InventoryTopicSub");
            adapter
                .setOutputChannel(
                    inputChannel); // looks like the channel to ack on (thus the input channel - confusing!)
            adapter.setAckMode(AckMode.MANUAL);
            return adapter;
        } catch (Exception ex) {
            ex.printStackTrace();
            System.exit(-1);
            return null;
        }
    }
    
    @Bean
    @Override
    public MessageChannel pubsubInputChannel() {
        DirectChannel dc = new DirectChannel();
        List<ChannelInterceptor> interceptors = new ArrayList<>();
        interceptors.add(channelInterceptorAdapter);
        dc.setInterceptors(interceptors);
        return dc;
    }
    
    @Bean
    @Override
    @ServiceActivator(inputChannel = "pubsubInputChannel")
    public MessageHandler messageReceiver() {
        return (Message<?> message) -> {
            
            imMessageProcessor.processMessageReceived(message);
            
            AckReplyConsumer consumer =
                (AckReplyConsumer) message.getHeaders().get(GcpHeaders.ACKNOWLEDGEMENT);
            consumer.ack();
            
        };
    }
    
    /**
     * Retrieve the list of topics the message must be send to.
     *
     * @param pubSubMessageType
     * @return List<String> A list of topic names. If none found then the list will be empty.
     */
    private List<String> getTargetTopicNames(PubSubMessageType pubSubMessageType) {
        
        List<PubSubMessagingProperties.Modules> modules
            = pubSubMessagingProperties.getModules().stream().filter(module ->
            (module.getApplicationModuleName().equals(applicationModuleName)
                && module.getPubSubMessageType().equalsIgnoreCase(pubSubMessageType.getMessageTypeCode())))
            .collect(Collectors.toList());
        
        if (!modules.isEmpty()) {
            return modules.get(0).getTopicsList();
        } else {
            return new ArrayList<>();
        }
        
    }
}
