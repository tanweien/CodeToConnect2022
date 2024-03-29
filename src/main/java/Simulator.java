import domain.ClientOrder;
import domain.MarketData;
import domain.Quote;
import domain.Trade;
import loader.ClientLoader;
import loader.MarketLoader;
import request.Request;
import response.Response;


/* Areas of improvement:
 1. the recommended design pattern to use is the Chain of Responsibility (handler) pattern
 2. simulatedExchange should be able to work with multiple trading engines (ie use hashmap)
 3. improve unit tests to cover all test cases (lack of time during actual hackathon)
 */

import java.util.List;

public class Simulator {
    private final TradingEngine tradingEngine;
    private final SimulatedExchange simulatedExchange;
    private final MarketLoader marketLoader;
    private final ClientLoader clientLoader;

    public Simulator() {
        TradingEngine tradingEngine = new TradingEngine();
        SimulatedExchange simulatedExchange = new SimulatedExchange();
        this.tradingEngine = tradingEngine;
        this.simulatedExchange = simulatedExchange;
        this.marketLoader = new MarketLoader();
        this.clientLoader = new ClientLoader();
    }

    public void init(String marketData, String clientOrderData) {
        marketLoader.loadMarketData(marketData);
        clientLoader.loadClientOrders(clientOrderData);
    }

    public void handleTrade(MarketData marketData) {
        Trade trade = (Trade) marketData;
        int tradeVolume = trade.getTradeVolume();
        tradingEngine.incrementMarketTradedVolume(tradeVolume);
    }

    public void fillMarketableOrders(ClientOrder clientOrder, Quote quote) {
        List<Response> fillMarketableOrdersResponseList = simulatedExchange.generateFillMarketableOrderResponseList(clientOrder, quote);
        tradingEngine.handleResponse(clientOrder, fillMarketableOrdersResponseList);
    }

    public void fillBestBidOrders(ClientOrder clientOrder) {
        int timestamp = marketLoader.checkNextTimestamp();
        List<Response> fillBestBidResponseList = simulatedExchange.generateFillBestBidOrderResponseList(clientOrder, timestamp);
        tradingEngine.handleResponse(clientOrder, fillBestBidResponseList);
    }

    public void simulateInteraction(ClientOrder clientOrder, Quote quote) {
        List<Request> requestList = tradingEngine.generateRequestList(clientOrder, quote);
        List<Response> responseList = simulatedExchange.handleTradingEngineRequestList(requestList);
        tradingEngine.handleResponse(clientOrder, responseList);
    }

    public void run() {
        while (clientLoader.hasClientOrder()) {
            ClientOrder clientOrder = clientLoader.getClientOrder();
            while (marketLoader.hasMarketData()) {
                MarketData marketData = marketLoader.popMarketData();
                if (marketData.getDataType().equals("T")) {
                    handleTrade(marketData);
                } else {
                    Quote quote = (Quote) marketData;
                    fillMarketableOrders(clientOrder, quote);

                    if (clientOrder.getRemainingOrderQuantity() <= 0) {
                        System.out.println("Client order completed");
                        tradingEngine.clearChildOrders();
                        break;
                    }

                    simulateInteraction(clientOrder, quote);
                }
                if (clientOrder.getRemainingOrderQuantity() <= 0) {
                    System.out.println("Client order completed");
                    tradingEngine.clearChildOrders();
                    break;
                }

                if (marketLoader.hasMarketData()) {
                    fillBestBidOrders(clientOrder);
                }
                if (clientOrder.getRemainingOrderQuantity() <= 0) {
                    System.out.println("Client order completed");
                    tradingEngine.clearChildOrders();
                    break;
                }
            }
        }
    }

    public static void main(String[] args) {
        Simulator simulator = new Simulator();
        simulator.init("market_data.csv", "client_order.csv");
        simulator.run();
    }
}
