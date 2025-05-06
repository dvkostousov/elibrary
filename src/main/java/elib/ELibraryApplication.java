package elib;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ELibraryApplication {

	public static void main(String[] args) {
		String javaVersion = System.getProperty("java.version");
        String javaVendor = System.getProperty("java.vendor");
        String javaVendorUrl = System.getProperty("java.vendor.url");

        System.out.println("Java Version: " + javaVersion);
        System.out.println("Java Vendor: " + javaVendor);
        System.out.println("Java Vendor URL: " + javaVendorUrl);
		SpringApplication.run(ELibraryApplication.class, args);
	}

}
