package edu.rutmiit.demo.auctionrest.config;

import edu.rutmiit.demo.auctioncontract.dto.BidResponse;
import edu.rutmiit.demo.auctioncontract.dto.LotResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.HateoasPageableHandlerMethodArgumentResolver;
import org.springframework.data.web.PagedResourcesAssembler;

@Configuration
public class HateoasConfig {

    @Bean
    public PagedResourcesAssembler<LotResponse> lotPagedResourcesAssembler() {
        return new PagedResourcesAssembler<>(
                new HateoasPageableHandlerMethodArgumentResolver(),
                null
        );
    }

    @Bean
    public PagedResourcesAssembler<BidResponse> bidPagedResourcesAssembler() {
        return new PagedResourcesAssembler<>(
                new HateoasPageableHandlerMethodArgumentResolver(),
                null
        );
    }
}