package edu.rutmiit.demo.auctioncontract.graphql.types;

import edu.rutmiit.demo.auctioncontract.dto.LotStatus;

/**
 * Входной тип для фильтрации лотов.
 * Соответствует input LotFilter в GraphQL-схеме.
 */
public record LotFilterGql(
        LotStatus status,
        String sellerId
) {}