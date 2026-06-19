package edu.rutmiit.demo.auctioncontract.graphql.types;

import java.math.BigDecimal;

/**
 * Входной тип для частичного обновления лота.
 * Соответствует input PatchLotInput в GraphQL-схеме.
 */
public record PatchLotInputGql(
        String title,
        String description,
        BigDecimal bidStep
) {}