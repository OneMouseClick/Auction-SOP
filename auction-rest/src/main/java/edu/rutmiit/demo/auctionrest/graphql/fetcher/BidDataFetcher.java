package edu.rutmiit.demo.auctionrest.graphql.fetcher;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;
import edu.rutmiit.demo.auctioncontract.dto.*;
import edu.rutmiit.demo.auctioncontract.graphql.types.PlaceBidInputGql;
import edu.rutmiit.demo.auctionrest.service.BidService;

@DgsComponent
public class BidDataFetcher {

    private final BidService bidService;

    public BidDataFetcher(BidService bidService) {
        this.bidService = bidService;
    }

    @DgsQuery
    public BidResponse bid(@InputArgument String id) {
        return bidService.findBidById(Long.parseLong(id));
    }

    @DgsMutation
    public BidResponse placeBid(@InputArgument String lotId, @InputArgument PlaceBidInputGql input) {
        BidPlaceRequest request = new BidPlaceRequest(
                Long.parseLong(input.bidderId()),
                input.amount()
        );
        return bidService.placeBid(Long.parseLong(lotId), request);
    }
}