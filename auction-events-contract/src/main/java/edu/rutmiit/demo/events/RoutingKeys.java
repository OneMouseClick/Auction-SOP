package edu.rutmiit.demo.events;

public final class RoutingKeys {
    private RoutingKeys() {}

    public static final String EXCHANGE = "auction.events";

    public static final String LOT_CREATED = "lot.created";
    public static final String BID_PLACED = "bid.placed";
    public static final String LOT_EXPIRED = "lot.expired";
    public static final String LOT_SOLD = "lot.sold";

    public static final String ALL_LOT_EVENTS = "lot.*";
    public static final String ALL_BID_EVENTS = "bid.*";
    public static final String ALL_EVENTS = "#";
}
