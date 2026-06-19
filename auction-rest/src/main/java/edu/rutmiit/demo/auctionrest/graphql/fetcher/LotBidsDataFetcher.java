package edu.rutmiit.demo.auctionrest.graphql.fetcher;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsData;
import com.netflix.graphql.dgs.DgsDataFetchingEnvironment;
import com.netflix.graphql.dgs.InputArgument;
import edu.rutmiit.demo.auctioncontract.dto.BidResponse;
import edu.rutmiit.demo.auctioncontract.dto.LotResponse;
import edu.rutmiit.demo.auctioncontract.dto.PagedResponse;
import edu.rutmiit.demo.auctioncontract.graphql.types.BidConnectionGql;
import edu.rutmiit.demo.auctioncontract.graphql.types.PageInfoGql;
import edu.rutmiit.demo.auctionrest.service.BidService;

@DgsComponent
public class LotBidsDataFetcher {

    private final BidService bidService;

    public LotBidsDataFetcher(BidService bidService) {
        this.bidService = bidService;
    }

    @DgsData(parentType = "Lot", field = "bids")
    public BidConnectionGql bids(
            DgsDataFetchingEnvironment dfe,
            @InputArgument Integer page,
            @InputArgument Integer size) {

        LotResponse lot = dfe.getSource();
        int pageNum = page != null ? page : 0;
        int pageSize = size != null ? size : 20;

        PagedResponse<BidResponse> paged = bidService.findBidsForLot(lot.getId(), pageNum, pageSize);

        return new BidConnectionGql(
                paged.content(),
                new PageInfoGql(paged.pageNumber(), paged.pageSize(), paged.totalPages(), paged.last()),
                (int) paged.totalElements());
    }
}