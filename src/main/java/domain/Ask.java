package domain;

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

    @Override
    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }

        if (!(object instanceof Ask)) {
            return false;
        }

        Ask ask = (Ask) object;

        return ask.getAskSize() == this.getAskSize() && ask.getAskPrice() == this.getAskPrice();
    }
}
