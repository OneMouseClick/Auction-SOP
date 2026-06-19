package edu.rutmiit.demo.auctioncontract.graphql.types;

/**
 * Метаданные страницы для пагинации.
 * Соответствует типу PageInfo в GraphQL-схеме.
 */
public record PageInfoGql(
        int pageNumber,
        int pageSize,
        int totalPages,
        boolean last
) {}