package in.clear.http;

import in.clear.http.model.Vendor;
import in.clear.http.service.VendorService;
import in.clear.http.util.DataGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.util.List;

@Slf4j
@Component
public class DataGeneratorAppRunner implements CommandLineRunner {

    private static final int TOTAL_VENDORS = 5;
    private static final boolean GENERATE_DATA = false; // Set to true to generate data on startup

    @Autowired
    private VendorService vendorService;

    @Override
    public void run(String... args) {
        if (GENERATE_DATA) {
            List<Vendor> vendors = DataGenerator.generateVendorData(TOTAL_VENDORS);
            vendorService.saveVendors(vendors);
            System.out.println("Generated and saved vendor records." + TOTAL_VENDORS);
        } else {
            System.out.println("Data generation is disabled. Set GENERATE_DATA to true to enable data generation.");
        }
    }
}
