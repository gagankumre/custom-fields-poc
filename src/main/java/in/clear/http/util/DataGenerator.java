package in.clear.http.util;

import com.github.javafaker.Faker;
import in.clear.http.model.VendorContact;
import in.clear.http.model.customField.Address;
import in.clear.http.model.customField.BankDetail;
import in.clear.http.model.customField.VendorCustomFields;
import in.clear.http.model.Vendor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class DataGenerator {
    private static final int TOTAL_WORKSPACES = 5;
    private static final String[] WORKSPACE_IDS = {"workspace-1", "workspace-2", "workspace-3", "workspace-4", "workspace-5"};

    // Common email domains
    private static final String[] EMAIL_DOMAINS = {"example.com", "test.com", "sample.com"};

    // Using a Set to ensure uniqueness while generating common values
    private static final Set<String> commonNames = new HashSet<>();
    private static final Set<String> commonAccountNumbers = new HashSet<>();

    private static final Faker faker = new Faker();
    private static final Random random = new Random();

    public static List<Vendor> generateVendorData(int count) {
        List<Vendor> vendors = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            Vendor vendor = new Vendor();
            vendor.setWorkspaceId(WORKSPACE_IDS[random.nextInt(TOTAL_WORKSPACES)]);
            vendor.setErpId(faker.idNumber().valid());
            vendor.setCustomField(generateCustomFields());
            vendor.setContacts(generateContacts());

            vendors.add(vendor);
        }
        System.out.println("commonNames: " + commonNames);
        System.out.println("commonAccountNumbers: " + commonAccountNumbers);
        return vendors;
    }

    private static List<VendorContact> generateContacts() {
        List<VendorContact> contacts = new ArrayList<>();
        for (int i = 0; i < random.nextInt(5); i++) {
            VendorContact contact = new VendorContact();
            contact.setName(faker.name().fullName());
            contact.setEmail(faker.internet().emailAddress().split("@")[0] + "@" + EMAIL_DOMAINS[random.nextInt(EMAIL_DOMAINS.length)]);
            contact.setPhoneNumber(faker.phoneNumber().phoneNumber());
            contact.setDesignation(faker.company().profession());
            contact.setDepartment(faker.company().profession());
            contacts.add(contact);
        }
        return contacts;
    }

    private static VendorCustomFields generateCustomFields() {
        VendorCustomFields customFields = new VendorCustomFields();

        // Create or retrieve common names
        String name;
        if (commonNames.size() < 100) {  // Limit the common names to a maximum of 100
            name = faker.company().name();
            commonNames.add(name);
        } else {
            name = commonNames.stream().skip(random.nextInt(commonNames.size())).findFirst().orElse(faker.company().name());
        }
        customFields.setName(name);

        customFields.setVendorCode(faker.company().industry());
        customFields.setPaymentTerms(faker.finance().creditCard());
        customFields.setReconAccount(faker.number().digits(10));

        // Generate account numbers to ensure some commonality
        String accountNumber;
        if (commonAccountNumbers.size() < 100) {  // Limit common account numbers to a maximum of 100
            accountNumber = faker.number().digits(10);
            commonAccountNumbers.add(accountNumber);
        } else {
            accountNumber = commonAccountNumbers.stream().skip(random.nextInt(commonAccountNumbers.size())).findFirst().orElse(faker.number().digits(10));
        }
        customFields.setReconAccount(accountNumber);

        // Generate Address
        Address address = new Address();
        address.setAddressLine1(faker.address().streetAddress());
        address.setCity(faker.address().city());
        address.setEmail(faker.internet().emailAddress().split("@")[0] + "@" + EMAIL_DOMAINS[random.nextInt(EMAIL_DOMAINS.length)]);
        address.setPhoneNumber1(faker.phoneNumber().phoneNumber());

        customFields.setAddress(address);

        // Generate Bank Details
        BankDetail bankDetail = new BankDetail();
        bankDetail.setAccountType(faker.number().digits(10));
        bankDetail.setAccountNumber(accountNumber); // Use the common account number
        bankDetail.setBankName(faker.company().name());

        customFields.setBankDetail(bankDetail);

        return customFields;
    }
}

