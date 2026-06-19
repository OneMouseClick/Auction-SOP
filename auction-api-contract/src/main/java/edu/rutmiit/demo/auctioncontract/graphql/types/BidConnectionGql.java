package edu.rutmiit.demo.auctioncontract.graphql.types;

import edu.rutmiit.demo.auctioncontract.dto.BidResponse;
import java.util.List;

/**
 * Тип-обёртка для постраничного ответа со списком ставок.
 * Соответствует типу BidConnection в GraphQL-схеме.
 */
public record BidConnectionGql(
        List<BidResponse> content,
        PageInfoGql pageInfo,
        int totalElements
) {}