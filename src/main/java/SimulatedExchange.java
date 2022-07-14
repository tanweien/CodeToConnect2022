import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SimulatedExchange {
    private final HashMap<ClientOrder, OrderMap> simulatedExchangeOrderMap;

    public SimulatedExchange() {
        this.simulatedExchangeOrderMap = new HashMap<>();
    }

    public List<Response> handleTradingEngineRequestList(List<Request> requestList) {
        List<Response> responseList = new ArrayList<>();
        for (int i = 0; i < requestList.size(); i++) {
            Request request = requestList.get(i);
            if (request instanceof CancelRequest) {
                CancelRequest cancelRequest = (CancelRequest) request;
                responseList.add(handleCancelRequest(cancelRequest));

            } else if (request instanceof AggressiveOrderRequest) {
                AggressiveOrderRequest aggressiveOrderRequest = (AggressiveOrderRequest) request;
                responseList.add(handleAggressiveOrderRequest(aggressiveOrderRequest));

            } else if (request instanceof OrderRequest){
                OrderRequest orderRequest = (OrderRequest) request;
                responseList.add(handleOrderRequest(orderRequest));
            }
        }
        return responseList;
    }

    public CancelResponse handleCancelRequest(CancelRequest cancelRequest) {
        Order cancelOrder = cancelRequest.getOrder();
        ClientOrder clientOrder = cancelRequest.getClientOrder();
        if (simulatedExchangeOrderMap.containsKey(clientOrder)) {
            simulatedExchangeOrderMap.get(clientOrder).cancelOrder(cancelOrder);
        }
        return new CancelResponse(cancelOrder);
    }

    public AggressiveOrderResponse handleAggressiveOrderRequest(AggressiveOrderRequest aggressiveOrderRequest) {
        Order aggressiveOrder = aggressiveOrderRequest.getOrder();
        return new AggressiveOrderResponse(aggressiveOrder);
    }

    public OrderResponse handleOrderRequest(OrderRequest orderRequest) {
        Order order = orderRequest.getOrder();
        ClientOrder clientOrder = orderRequest.getClientOrder();
        if (!simulatedExchangeOrderMap.containsKey(clientOrder)) {
            simulatedExchangeOrderMap.put(clientOrder, new OrderMap());
        }
        simulatedExchangeOrderMap.get(clientOrder).addOrder(order);
        return new OrderResponse(order);
    }

    public List<Response> generateFillMarketableOrderResponseList(ClientOrder clientOrder, Quote quote) {
        List<Response> responseList = new ArrayList<>();
        if (simulatedExchangeOrderMap.containsKey(clientOrder)) {
            OrderMap orderMap = simulatedExchangeOrderMap.get(clientOrder);
            for (int i = 0; i < quote.getAskList().size(); i++) {
                double price = quote.getAskList().get(i).getAskPrice();
                if (orderMap.hasOrderWithPrice(price)) {
                    List<Order> orderList = orderMap.getOrderList(price);
                    for (int j = 0; j < orderList.size(); j++) {
                        Order order = orderList.get(j);
                        FillResponse fillResponse = new FillResponse(order);
                        responseList.add(fillResponse);
                    }
                    orderMap.removeOrdersByPrice(price);
                }
            }
        }
        return responseList;
    }


    public List<Response> generateFillBestBidOrderResponseList(ClientOrder clientOrder, int timestamp) {
        List<Response> responseList = new ArrayList<>();
        if (simulatedExchangeOrderMap.containsKey(clientOrder)) {
            OrderMap orderMap = simulatedExchangeOrderMap.get(clientOrder);

            if (!orderMap.isEmpty()) {
                double bestBid = orderMap.getBestBid();
                List<Order> bestBidOrderList = orderMap.getBestBidOrders();
                List<Order> newBestBidOrderList = new ArrayList<>();
                for (int i = 0; i < bestBidOrderList.size(); i++) {
                    Order bestBidOrder = bestBidOrderList.get(i);
                    if (bestBidOrder.isLongerThanThreeMinutes(timestamp)) {
                        FillResponse fillResponse = new FillResponse(bestBidOrder);
                        responseList.add(fillResponse);
                    } else {
                        newBestBidOrderList.add(bestBidOrder);
                    }
                }
                orderMap.replaceOrdersByPrice(bestBid, newBestBidOrderList);
            }
        }
        return responseList;
    }
}
