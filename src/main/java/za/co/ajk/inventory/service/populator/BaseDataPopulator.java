package za.co.ajk.inventory.service.populator;

import java.time.Instant;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import za.co.ajk.inventory.domain.Company;
import za.co.ajk.inventory.domain.Country;
import za.co.ajk.inventory.domain.Equipment;
import za.co.ajk.inventory.domain.Region;
import za.co.ajk.inventory.repository.CompanyRepository;
import za.co.ajk.inventory.repository.CountryRepository;
import za.co.ajk.inventory.repository.EquipmentRepository;
import za.co.ajk.inventory.repository.RegionRepository;
import za.co.ajk.inventory.service.CompanyService;
import za.co.ajk.inventory.service.CountryService;
import za.co.ajk.inventory.service.EquipmentService;
import za.co.ajk.inventory.service.RegionService;
import za.co.ajk.inventory.service.dto.CompanyDTO;
import za.co.ajk.inventory.service.dto.CountryDTO;
import za.co.ajk.inventory.service.dto.EquipmentDTO;
import za.co.ajk.inventory.service.dto.RegionDTO;


/**
 * Class to load basic data for unit testing and QA.
 */
@Profile("dev")
@Service
public class BaseDataPopulator implements CommandLineRunner {
    
    @Autowired
    private CountryRepository countryRepository;
    @Autowired
    private CountryService countryService;
    
    @Autowired
    private RegionRepository regionRepository;
    @Autowired
    private RegionService regionService;
    
    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private CompanyService companyService;
    
    @Autowired
    private EquipmentRepository equipmentRepository;
    @Autowired
    private EquipmentService equipmentService;
    
    private Country southAfrica = new Country().countryCode("ZA").countryName("South Africa").regions(null);
    private Country america = new Country().countryCode("US").countryName("United States of America").regions(null);
    private Country newZealand = new Country().countryCode("NZ").countryName("New Zealand").regions(null);
    
    private Region gauteng = new Region().regionCode("REG1").regionName("Gauteng").country(southAfrica);
    private Region kzn = new Region().regionCode("REG2").regionName("Kwazulu Natal").country(southAfrica);
    private Region westernCape = new Region().regionCode("REG3").regionName("Western Cape").country(southAfrica);
    
    private Company ca1_1 = new Company().name("Company One").branchCode("BC1").region(westernCape);
    private Company ca1_2 = new Company().name("Company One").branchCode("BC2").region(westernCape);
    private Company ca1_3 = new Company().name("Company One").branchCode("BC3").region(westernCape);
    
    private Company ca2 = new Company().name("Company Two").region(gauteng);
    private Company ca3 = new Company().name("Company Three").region(gauteng);
    private Company ca4 = new Company().name("Company Four").region(kzn);
    private Company ca5 = new Company().name("Company Five").region(kzn);
    private Company ca6 = new Company().name("Company Six");
    private Company ca7 = new Company().name("Company Seven");
    
    @Override
    public void run(String... args) throws Exception {
        loadBaseCountries();
        loadBaseRegions();
        loadBaseCompanies();
        loadBaseEquipment();
    }
    
    private void loadBaseCountries() {
        List<CountryDTO> countryList = countryService.findAll();
        if (countryList.size() == 0) {
            countryRepository.save(southAfrica);
            countryRepository.save(america);
            countryRepository.save(newZealand);
        }
        countryRepository.findAll().forEach(System.out::println);
    }
    
    private void loadBaseRegions() {
        List<RegionDTO> regionList = regionService.findAll();
        if (regionList.size() == 0) {
            regionRepository.save(gauteng);
            regionRepository.save(kzn);
            regionRepository.save(westernCape);
        }
    }
    
    private void loadBaseCompanies() {
        List<CompanyDTO> companyList = companyService.findAll();
        if (companyList.size() == 0) {
            
            //  Load a company with branch codes for the same company
            companyRepository.save(ca1_1);
            companyRepository.save(ca1_2);
            companyRepository.save(ca1_3);
            
            //  Load companies without branch codes.
            companyRepository.save(ca2);
            companyRepository.save(ca3);
            companyRepository.save(ca4);
            companyRepository.save(ca5);
            companyRepository.save(ca6);
            companyRepository.save(ca7);
            
        }
        companyRepository.findAll().forEach(System.out::println);
    }
    
    private void loadBaseEquipment() {
        List<EquipmentDTO> equipmentList = equipmentService.findAll();
    
        Equipment eq1 = new Equipment();
        eq1.setEquipmentId(1);
        eq1.setEquipmentName("EquipmentName1");
        eq1.setAddedBy("Andre");
        eq1.setCompany(ca1_1);
        eq1.setCurrentlyAvailable(true);
        eq1.setDateAddedToStock(Instant.now());
        eq1.setEquipmentBarcode("Barcode111");
        eq1.setEquipmentDescription("Test equipment 1");
        eq1.setEquipmentSerialNumber("1234567890");
        eq1.setEquipmentGroup("EQGroup 1");
    
        Equipment eq2 = new Equipment();
        eq2.setEquipmentId(2);
        eq2.setEquipmentName("EquipmentName2");
        eq2.setAddedBy("Andre");
        eq2.setCompany(ca1_1);
        eq2.setCurrentlyAvailable(true);
        eq2.setDateAddedToStock(Instant.now());
        eq2.setEquipmentBarcode("Barcode112");
        eq2.setEquipmentDescription("Test equipment 2");
        eq2.setEquipmentSerialNumber("1234567892");
        eq2.setEquipmentGroup("EQGroup 1");
    
        Equipment eq3 = new Equipment();
        eq3.setEquipmentId(3);
        eq3.setEquipmentName("EquipmentName3");
        eq3.setAddedBy("Andre");
        eq3.setCompany(ca1_1);
        eq3.setCurrentlyAvailable(true);
        eq3.setDateAddedToStock(Instant.now());
        eq3.setEquipmentBarcode("Barcode113");
        eq3.setEquipmentDescription("Test equipment 3");
        eq3.setEquipmentSerialNumber("1234567893");
        eq3.setEquipmentGroup("EQGroup 1");
    
        Equipment eq4 = new Equipment();
        eq4.setEquipmentId(4);
        eq4.setEquipmentName("EquipmentName4");
        eq4.setAddedBy("Andre");
        eq4.setCompany(ca1_2);
        eq4.setCurrentlyAvailable(true);
        eq4.setDateAddedToStock(Instant.now());
        eq4.setEquipmentBarcode("Barcode114");
        eq4.setEquipmentDescription("Test equipment 4");
        eq4.setEquipmentSerialNumber("1234567894");
        eq4.setEquipmentGroup("EQGroup 4");
        
        equipmentRepository.save(eq1);
        equipmentRepository.save(eq2);
        equipmentRepository.save(eq3);
        equipmentRepository.save(eq4);
        
        equipmentRepository.findAll().stream().forEach(System.out::print);
    }
}
