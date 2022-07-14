import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MarketLoader {
    private final List<MarketData> marketDataList;

    public MarketLoader() {
        this.marketDataList = new ArrayList<>();
    }

    public void loadMarketData(String fileName) {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] data = line.split(",");
                int timestamp = Integer.parseInt(data[1]);
                if (data[0].equals("Q")) {
                    String[] bid = data[2].split(" ");
                    List<Bid> bidList = new ArrayList<>();
                    Bid firstBid = new Bid(Double.parseDouble(bid[0]), Integer.parseInt(bid[1]));
                    Bid secondBid = new Bid(Double.parseDouble(bid[2]), Integer.parseInt(bid[3]));
                    Bid thirdBid = new Bid(Double.parseDouble(bid[4]), Integer.parseInt(bid[5]));
                    bidList.add(firstBid);
                    bidList.add(secondBid);
                    bidList.add(thirdBid);

                    String[] ask = data[3].split(" ");
                    List<Ask> askList = new ArrayList<>();
                    Ask firstAsk = new Ask(Double.parseDouble(ask[0]), Integer.parseInt(ask[1]));
                    Ask secondAsk = new Ask(Double.parseDouble(ask[2]), Integer.parseInt(ask[3]));
                    Ask thirdAsk = new Ask(Double.parseDouble(ask[4]), Integer.parseInt(ask[5]));
                    askList.add(firstAsk);
                    askList.add(secondAsk);
                    askList.add(thirdAsk);

                    Quote quote = new Quote(timestamp, bidList, askList);
                    marketDataList.add(quote);

                } else {
                    Trade trade = new Trade(timestamp, Double.parseDouble(data[2]), Integer.parseInt(data[3]));
                    marketDataList.add(trade);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int checkNextTimestamp() {
        return this.marketDataList.get(0).getTimestamp();
    }

    public boolean hasMarketData() {
        return this.marketDataList.size() > 0;
    }

    public MarketData popMarketData() {
        if (this.marketDataList.size() > 0) {
            MarketData marketData = this.marketDataList.get(0);
            this.marketDataList.remove(marketData);
            return marketData;
        } else {
            return null;
        }
    }
}
