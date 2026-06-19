package edu.rutmiit.demo.auctionrest.graphql.fetcher;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;
import edu.rutmiit.demo.auctioncontract.dto.*;
import edu.rutmiit.demo.auctioncontract.graphql.types.*;
import edu.rutmiit.demo.auctionrest.service.LotService;

@DgsComponent
public class LotDataFetcher {

    private final LotService lotService;

    public LotDataFetcher(LotService lotService) {
        this.lotService = lotService;
    }

    @DgsQuery
    public LotResponse lot(@InputArgument String id) {
        return lotService.findLotById(Long.parseLong(id));
    }

    @DgsQuery
    public LotConnectionGql lots(
            @InputArgument LotFilterGql filter,
            @InputArgument Integer page,
            @InputArgument Integer size) {

        int pageNum = page != null ? page : 0;
        int pageSize = size != null ? size : 20;

        LotStatus status = null;
        Long sellerId = null;

        if (filter != null) {
            status = filter.status();
            sellerId = filter.sellerId() != null ? Long.parseLong(filter.sellerId()) : null;
        }

        PagedResponse<LotResponse> paged = lotService.findAllLots(status, sellerId, pageNum, pageSize);

        return new LotConnectionGql(
                paged.content(),
                new PageInfoGql(paged.pageNumber(), paged.pageSize(), paged.totalPages(), paged.last()),
                (int) paged.totalElements());
    }

    @DgsMutation
    public LotResponse createLot(@InputArgument CreateLotInputGql input) {
        LotCreateRequest request = new LotCreateRequest(
                Long.parseLong(input.sellerId()),
                input.title(),
                input.description(),
                input.startingPrice(),
                input.bidStep(),
                input.durationHours()
        );
        return lotService.createLot(request);
    }

    @DgsMutation
    public LotResponse updateLot(@InputArgument String id, @InputArgument UpdateLotInputGql input) {
        LotUpdateRequest request = new LotUpdateRequest(
                input.title(),
                input.description(),
                input.startingPrice(),
                input.bidStep()
        );
        return lotService.updateLot(Long.parseLong(id), request);
    }

    @DgsMutation
    public LotResponse patchLot(@InputArgument String id, @InputArgument PatchLotInputGql input) {
        PatchLotRequest request = new PatchLotRequest(
                input.title(),
                input.description(),
                input.bidStep()
        );
        return lotService.patchLot(Long.parseLong(id), request);
    }

    @DgsMutation
    public boolean deleteLot(@InputArgument String id) {
        lotService.deleteLot(Long.parseLong(id));
        return true;
    }
}