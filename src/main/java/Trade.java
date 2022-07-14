public class Trade extends MarketData {
    private final double tradePrice;
    private final int tradeVolume;

    protected Trade(int timestamp, double tradePrice, int tradeVolume) {
        super("T", timestamp);
        this.tradePrice = tradePrice;
        this.tradeVolume = tradeVolume;
    }

    public int getTradeVolume() {
        return this.tradeVolume;
    }

    @Override
    public String toString() {
        return this.getTimestamp() + ", " + this.tradePrice + ", " + this.tradeVolume;
    }
}
