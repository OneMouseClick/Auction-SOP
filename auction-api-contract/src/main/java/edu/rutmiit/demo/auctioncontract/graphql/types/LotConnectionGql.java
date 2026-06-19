package edu.rutmiit.demo.auctioncontract.graphql.types;

import edu.rutmiit.demo.auctioncontract.dto.LotResponse;
import java.util.List;

/**
 * Тип-обёртка для постраничного ответа со списком лотов.
 * Соответствует типу LotConnection в GraphQL-схеме.
 */
public record LotConnectionGql(
        List<LotResponse> content,
        PageInfoGql pageInfo,
        int totalElements
) {}