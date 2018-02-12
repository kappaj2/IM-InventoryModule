package za.co.ajk.inventory.cucumber.stepdefs;

import za.co.ajk.inventory.InventoryModuleApp;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.ResultActions;

import org.springframework.boot.test.context.SpringBootTest;

@WebAppConfiguration
@SpringBootTest
@ContextConfiguration(classes = InventoryModuleApp.class)
public abstract class StepDefs {

    protected ResultActions actions;

}
