package domain;

public class ClientOrder {
    private final String order;
    private final int totalOrderQuantity;
    private final double targetDecimal;
    private final double minimumRatio;
    private final double maximumRatio;
    private int cumulativeQuantity;

    public ClientOrder(int orderQuantity, double targetDecimal) {
        this.order = "B";
        this.totalOrderQuantity = orderQuantity;
        this.targetDecimal = targetDecimal;
        this.minimumRatio = 0.8 * targetDecimal;
        this.maximumRatio = 1.2 * targetDecimal;
        this.cumulativeQuantity = 0;
    }

    public void incrementCumulativeQuantity(double filledQuantity) {
        this.cumulativeQuantity += filledQuantity;
    }

    public boolean isBehindMin(int marketTradedVolume) {
        int roundedDown = (int) (marketTradedVolume * minimumRatio);
        return this.cumulativeQuantity < roundedDown;
    }

    public int getShortfall(int marketTradedVolume) {
        int calculatedRoundedDownShortfall = (int) (marketTradedVolume * minimumRatio - this.cumulativeQuantity);
        if (calculatedRoundedDownShortfall > getRemainingOrderQuantity()) {
            return getRemainingOrderQuantity();
        } else {
            return calculatedRoundedDownShortfall;
        }
    }

    public boolean isBreachMax(int marketTradedVolume) {
        return this.cumulativeQuantity > marketTradedVolume * maximumRatio;
    }

    public int getOrderQuantity() {
        return this.totalOrderQuantity;
    }

    public int getCumulativeQuantity() {
        return this.cumulativeQuantity;
    }

    public int getRemainingOrderQuantity() {
        return this.totalOrderQuantity - this.cumulativeQuantity;
    }

    public double getTargetDecimal() {
        return targetDecimal;
    }

    @Override
    public String toString() {
        return this.totalOrderQuantity + ":" + this.targetDecimal;
    }
}
