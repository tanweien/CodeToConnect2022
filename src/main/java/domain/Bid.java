package domain;

public class Bid {
    private final double bidPrice;
    private final int bidSize;


    public Bid(double bidPrice, int bidSize) {
        this.bidPrice = bidPrice;
        this.bidSize = bidSize;
    }

    public double getBidPrice() {
        return bidPrice;
    }

    public int getBidSize() {
        return bidSize;
    }

    @Override
    public String toString() {
        return bidPrice + " " + bidSize;
    }

}
