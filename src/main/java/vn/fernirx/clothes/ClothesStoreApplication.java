package vn.fernirx.clothes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class ClothesStoreApplication {

    public static void main(String[] args) {
        SpringApplication.run(ClothesStoreApplication.class, args);
    }

}
