package domain;

abstract public class MarketData {
    private final String dataType;
    private final int timestamp;


    protected MarketData(String dataType, int timestamp) {
        this.dataType = dataType;
        this.timestamp = timestamp;
    }

    public String getDataType() {
        return this.dataType;
    }

    public int getTimestamp() {
        return this.timestamp;
    }
}
