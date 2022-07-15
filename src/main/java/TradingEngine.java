import domain.*;

import request.AggressiveOrderRequest;
import request.CancelRequest;
import request.OrderRequest;
import request.Request;
import response.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TradingEngine {
    private int marketTradedVolume;
    private final OrderMap orderMap;

    public TradingEngine() {
        this.marketTradedVolume = 0;
        this.orderMap = new OrderMap();
    }

    public List<Request> generateRequestList(ClientOrder clientOrder, Quote quote) {
        List<Request> requestList = new ArrayList<>();
        int timestamp = quote.getTimestamp();
        int totalFillQuantity = 0;

        if (clientOrder.isBreachMax(marketTradedVolume)) {
            return generateCancelAllRequest(clientOrder);
        }

        if (clientOrder.isBehindMin(marketTradedVolume)) {
            totalFillQuantity = calculateAggressiveFillQuantity(clientOrder,quote);
            if (totalFillQuantity > 0) {
                List<Request> aggressiveOrderRequestList = generateAggressiveOrderRequestList(clientOrder, quote, totalFillQuantity, timestamp);
                requestList.addAll(aggressiveOrderRequestList);
            }
        }

        int remainingClientOrderQuantity = clientOrder.getRemainingOrderQuantity() - totalFillQuantity;
        if (remainingClientOrderQuantity <= 0) {
            return requestList;
        }

        double targetPercentage = clientOrder.getTargetDecimal();
        List<Order> targetOrderList = new ArrayList<>();

        for (int i = 0; i < quote.getBidList().size(); i++) {

            if (remainingClientOrderQuantity > 0) {
                Bid bid = quote.getBidList().get(i);
                double orderPrice = bid.getBidPrice();
                int roundedDownOrderQuantity = (int) (bid.getBidSize() * targetPercentage);
                if (roundedDownOrderQuantity > remainingClientOrderQuantity) {
                    roundedDownOrderQuantity = remainingClientOrderQuantity;
                }

                Order order = new Order(timestamp, roundedDownOrderQuantity, orderPrice);
                remainingClientOrderQuantity -= roundedDownOrderQuantity;
                targetOrderList.add(order);
            }
        }

        List<Order> cancelOrderList = new ArrayList<>();
        List<Order> orderList = new ArrayList<>();

        List<Double> priceList = this.orderMap.getListOfOrderPrices();
        List<Double> cancelOrderAtPriceList = new ArrayList<>();
        List<Double> targetPriceList = new ArrayList<>();

        for (int i = 0; i < targetOrderList.size(); i++) {
            targetPriceList.add(targetOrderList.get(i).getPrice());
        }
        for (int i = 0; i < priceList.size(); i++) {
            if (!targetPriceList.contains(priceList.get(i))) {
                cancelOrderAtPriceList.add(priceList.get(i));
            }
        }

        for (int i = 0; i < cancelOrderAtPriceList.size(); i++) {
            cancelOrderList.addAll(orderMap.getOrderList(cancelOrderAtPriceList.get(i)));
        }

        for (int i = 0; i < targetOrderList.size(); i++) {
            Order targetOrder = targetOrderList.get(i);
            double price = targetOrder.getPrice();
            int targetQuantity = targetOrder.getQuantity();
            if (orderMap.hasOrderWithPrice(price)) {
                int existingQuantity = orderMap.getTotalQuantity(price);
                if (targetQuantity > existingQuantity) {
                    int difference = targetQuantity - existingQuantity;

                    Order order = new Order(timestamp, difference, price);
                    orderList.add(order);
                } else if (targetQuantity < existingQuantity){
                    cancelOrderList.addAll(orderMap.getOrderList(price));
                    Order order = new Order(timestamp, targetQuantity, price);
                    orderList.add(order);
                }

            } else {
                Order order = new Order(timestamp, targetQuantity, price);
                orderList.add(order);
            }
        }

        for (int i = 0; i < cancelOrderList.size(); i++) {
            CancelRequest cancelRequest = new CancelRequest(clientOrder, cancelOrderList.get(i));
            requestList.add(cancelRequest);
        }

        for (int i = 0; i < orderList.size(); i++) {
            OrderRequest orderRequest = new OrderRequest(clientOrder, orderList.get(i));
            requestList.add(orderRequest);
        }

        if (!requestList.isEmpty()) {
            System.out.println(generateStrategy(requestList));
        }
        return requestList;
    }

    public void handleResponse(ClientOrder clientOrder, List<Response> responseList) {
        for (int i = 0; i < responseList.size(); i++) {
            Response response = responseList.get(i);
            if (response instanceof FillResponse) {
                FillResponse fillResponse = (FillResponse) response;
                handleFillResponse(clientOrder, fillResponse);
            } else if (response instanceof OrderResponse) {
                OrderResponse orderResponse = (OrderResponse) response;
                handleOrderResponse(orderResponse);
            } else if (response instanceof CancelResponse) {
                CancelResponse cancelResponse = (CancelResponse) response;
                handleCancelResponse(cancelResponse);
            } else if (response instanceof AggressiveOrderResponse) {
                AggressiveOrderResponse aggressiveOrderResponse = (AggressiveOrderResponse) response;
                handleAggressiveOrderResponse(clientOrder, aggressiveOrderResponse);
            }
        }
    }

    private void handleOrderResponse(OrderResponse orderResponse) {
        Order order = orderResponse.getOrder();
        orderMap.addOrder(order);
    }

    private void handleCancelResponse(CancelResponse cancelResponse) {
        Order cancelOrder = cancelResponse.getOrder();
        orderMap.cancelOrder(cancelOrder);
    }

    private void handleAggressiveOrderResponse(ClientOrder clientOrder, AggressiveOrderResponse aggressiveOrderResponse) {
        int fillQuantity = aggressiveOrderResponse.getFillQuantity();
        double fillPrice = aggressiveOrderResponse.getFillPrice();
        if (fillQuantity > 0) {
            incrementClientCumulativeQuantity(clientOrder, fillQuantity);
            printFill(clientOrder, fillQuantity, fillPrice);
        }
    }

    private void handleFillResponse(ClientOrder clientOrder, FillResponse fillResponse) {
        Order order = fillResponse.getOrder();
        orderMap.cancelOrder(order);
        int fillQuantity = fillResponse.getFillQuantity();
        double fillPrice = fillResponse.getFillPrice();
        if (fillQuantity > 0) {
            incrementClientCumulativeQuantity(clientOrder, fillQuantity);
            printFill(clientOrder, fillQuantity, fillPrice);
        }
    }

    private List<Request> generateCancelAllRequest(ClientOrder clientOrder) {
        List<Request> cancelRequestList = new ArrayList<>();
        List<Order> orderList = orderMap.getAllOrders();
        for (int i = 0; i < orderList.size(); i++) {
            Order cancelOrder = orderList.get(i);
            CancelRequest cancelRequest = new CancelRequest(clientOrder, cancelOrder);
            cancelRequestList.add(cancelRequest);
        }
        return cancelRequestList;
    }

    private int calculateAggressiveFillQuantity(ClientOrder clientOrder, Quote quote) {
        int aggressiveFillQuantity;
        int shortfall = clientOrder.getShortfall(this.marketTradedVolume);
        Ask bestAsk = quote.getBestAsk();

        if (shortfall <= bestAsk.getAskSize()) {
            aggressiveFillQuantity = shortfall;
        } else {
            aggressiveFillQuantity = bestAsk.getAskSize();
        }
        return aggressiveFillQuantity;
    }

    private List<Request> generateAggressiveOrderRequestList(ClientOrder clientOrder, Quote quote, int aggressiveFillQuantity, int timestamp) {
        List<Request> aggressiveOrderRequestList= new ArrayList<>();
        double orderPrice = quote.getBestAsk().getAskPrice();
        Order order = new Order(timestamp, aggressiveFillQuantity, orderPrice);
        AggressiveOrderRequest aggressiveOrderRequest = new AggressiveOrderRequest(clientOrder, order);
        aggressiveOrderRequestList.add(aggressiveOrderRequest);
        return aggressiveOrderRequestList;
    }

    private void printFill(ClientOrder clientOrder, int fillQuantity, double fillPrice) {
        System.out.println("Filled: " + fillQuantity + "@" + fillPrice + ", Cumulative Quantity: " + clientOrder.getCumulativeQuantity());
    }

    public void incrementMarketTradedVolume(double additionalMarketTradedVolume) {
        this.marketTradedVolume += additionalMarketTradedVolume;
    }

    private void incrementClientCumulativeQuantity(ClientOrder clientOrder, double quantity) {
        clientOrder.incrementCumulativeQuantity(quantity);
    }

    private String generateStrategy(List<Request> requestList) {
        String resultString = "Strategy out: ";
        HashMap<Double, Integer> cancelRequestHashMap = new HashMap<>();
        HashMap<Double, Integer> orderRequestHashMap = new HashMap<>();
        List<CancelRequest> cancelRequestList = new ArrayList<>();
        List<OrderRequest> orderRequestList = new ArrayList<>();

        for (int i = 0; i < requestList.size(); i++) {
            Request request = requestList.get(i);
            if (request instanceof CancelRequest) {
                CancelRequest cancelRequest = (CancelRequest) request;
                cancelRequestList.add(cancelRequest);
            } else if (request instanceof OrderRequest) {
                OrderRequest orderRequest = (OrderRequest) request;
                orderRequestList.add(orderRequest);
            } else if (request instanceof AggressiveOrderRequest) {
                AggressiveOrderRequest aggressiveOrderRequest = (AggressiveOrderRequest) request;
                double price = aggressiveOrderRequest.getOrder().getPrice();
                int quantity = aggressiveOrderRequest.getOrder().getQuantity();
                if (orderRequestHashMap.containsKey(price)) {
                    int newQuantity = orderRequestHashMap.get(price) + quantity;
                    orderRequestHashMap.put(price, newQuantity);
                } else {
                    orderRequestHashMap.put(price, quantity);
                }
            }
        }

        for (int i = 0; i < cancelRequestList.size(); i++) {
            CancelRequest cancelRequest = cancelRequestList.get(i);
            double price = cancelRequest.getOrder().getPrice();
            int quantity = cancelRequest.getOrder().getQuantity();
            if (cancelRequestHashMap.containsKey(price)) {
                int newQuantity = cancelRequestHashMap.get(price) + quantity;
                cancelRequestHashMap.put(price, newQuantity);
            } else {
                cancelRequestHashMap.put(price, quantity);
            }
        }

        for (int i = 0; i < orderRequestList.size(); i++) {
            OrderRequest orderRequest = orderRequestList.get(i);
            double price = orderRequest.getOrder().getPrice();
            int quantity = orderRequest.getOrder().getQuantity();
            if (orderRequestHashMap.containsKey(price)) {
                int newQuantity = orderRequestHashMap.get(price) + quantity;
                orderRequestHashMap.put(price, newQuantity);
            } else {
                orderRequestHashMap.put(price, quantity);
            }
        }

        if (!cancelRequestHashMap.isEmpty()) {
            for (HashMap.Entry<Double, Integer> entry: cancelRequestHashMap.entrySet()) {
                resultString += "[C:" + entry.getValue() + "@" + entry.getKey() + "]";
            }
        }

        if (!orderRequestHashMap.isEmpty()) {
            for (HashMap.Entry<Double, Integer> entry: orderRequestHashMap.entrySet()) {
                resultString += "[N:" + entry.getValue() + "@" + entry.getKey() + "]";
            }
        }
        return resultString;
    }

    public void clearChildOrders() {
        this.orderMap.clear();
    }
}
