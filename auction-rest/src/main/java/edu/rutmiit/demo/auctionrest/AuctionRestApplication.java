package edu.rutmiit.demo.auctionrest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.hateoas.config.EnableHypermediaSupport;

@SpringBootApplication(
        scanBasePackages = {
            "edu.rutmiit.demo.auctionrest",
            "edu.rutmiit.demo.auctioncontract"
        }
)
@EnableHypermediaSupport(type = EnableHypermediaSupport.HypermediaType.HAL)
public class AuctionRestApplication {
    public static void main(String[] args) {
        SpringApplication.run(AuctionRestApplication.class, args);
    }
}