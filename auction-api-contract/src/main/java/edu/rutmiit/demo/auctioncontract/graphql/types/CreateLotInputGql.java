package edu.rutmiit.demo.auctioncontract.graphql.types;

import java.math.BigDecimal;

/**
 * Входной тип для создания лота.
 * Соответствует input CreateLotInput в GraphQL-схеме.
 */
public record CreateLotInputGql(
        String sellerId,
        String title,
        String description,
        BigDecimal startingPrice,
        BigDecimal bidStep,
        Integer durationHours
) {}