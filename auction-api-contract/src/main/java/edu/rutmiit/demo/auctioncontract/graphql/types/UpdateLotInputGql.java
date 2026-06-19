package edu.rutmiit.demo.auctioncontract.graphql.types;

import java.math.BigDecimal;

/**
 * Входной тип для полного обновления лота.
 * Соответствует input UpdateLotInput в GraphQL-схеме.
 */
public record UpdateLotInputGql(
        String title,
        String description,
        BigDecimal startingPrice,
        BigDecimal bidStep
) {}