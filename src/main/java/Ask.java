public class Ask {
    private final double askPrice;
    private final int askSize;

    public Ask(double askPrice, int askSize) {
        this.askPrice = askPrice;
        this.askSize = askSize;
    }

    public double getAskPrice() {
        return askPrice;
    }

    public int getAskSize() {
        return askSize;
    }

    @Override
    public String toString() {
        return askPrice + " " + askSize;
    }
}
