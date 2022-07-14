public class ClientOrder {
    // assuming only 1 client order, and order is buy
    private final String order;
    private final int totalOrderQuantity;
    private final double targetPercentage;
    private final double minimumRatio;
    private final double maximumRatio;
    private int cumulativeQuantity;

    ClientOrder(int orderQuantity, double targetPercentage) {
        this.order = "B";
        this.totalOrderQuantity = orderQuantity;
        this.targetPercentage = targetPercentage;
        this.minimumRatio = 0.8 * targetPercentage;
        this.maximumRatio = 1.2 * targetPercentage;
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

    public double getTargetPercentage() {
        return targetPercentage;
    }

    @Override
    public String toString() {
        return this.totalOrderQuantity + ":" + this.targetPercentage;
    }
}
