package edu.rutmiit.demo.auctioncontract.graphql.types;

import java.math.BigDecimal;

/**
 * Входной тип для размещения ставки.
 * Соответствует input PlaceBidInput в GraphQL-схеме.
 */
public record PlaceBidInputGql(
        String bidderId,
        BigDecimal amount
) {}