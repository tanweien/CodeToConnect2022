package domain;

import java.util.List;

public class Quote extends MarketData {
    private final List<Bid> bidList;
    private final List<Ask> askList;

    public Quote(int timestamp, List<Bid> bidList, List<Ask> askList) {
        super("Q", timestamp);
        this.bidList = bidList;
        this.askList = askList;
    }

    public List<Bid> getBidList() {
        return bidList;
    }

    public List<Ask> getAskList() {
        return askList;
    }

    public Ask getBestAsk() {
        if (this.askList.size() > 0) {
            return this.askList.get(0);
        } else {
            return null;
        }
    }

    @Override
    public String toString() {
        return this.getTimestamp() + ", " + this.bidList.toString() + ", " + this.askList.toString();
    }

}
