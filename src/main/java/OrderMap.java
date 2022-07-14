import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

public class OrderMap {
    private final TreeMap<Double, List<Order>> orderMap;

    public OrderMap() {
        this.orderMap = new TreeMap<>();
    }

    public TreeMap<Double, List<Order>> getOrderMap() {
        return this.orderMap;
    }

    public double getBestBid() {
        return this.orderMap.lastEntry().getKey();
    }

    public List<Order> getBestBidOrders() {
        return this.orderMap.lastEntry().getValue();
    }

    public int getTotalQuantity(double price) {
        int totalQuantity = 0;
        if (this.orderMap.containsKey(price)) {
            List<Order> orderList = orderMap.get(price);
            for (int i = 0; i < orderList.size(); i++) {
                totalQuantity += orderList.get(i).getQuantity();
            }
        }
        return totalQuantity;
    }

    public void cancelOrder(Order cancelOrder) {
        double price = cancelOrder.getPrice();
        if (orderMap.containsKey(price)) {
            List<Order> orderList = orderMap.get(price);
            orderList.remove(cancelOrder);
            if (orderList.isEmpty()) {
                orderMap.remove(price);
            }
        }
    }

    public boolean hasOrderWithPrice(double price) {
        return this.orderMap.containsKey(price);
    }

    public List<Order> getOrderList(double price) {
        if (this.orderMap.containsKey(price)) {
            return orderMap.get(price);
        } else {
            return null;
        }
    }

    public List<Order> getAllOrders() {
        List<Order> allOrderList = new ArrayList<>();
        List<Double> priceList = getListOfOrderPrices();
        for (int i = 0; i < priceList.size(); i++) {
            List<Order> orderList = this.orderMap.get(priceList.get(i));
            allOrderList.addAll(orderList);
        }
        return allOrderList;
    }

    public List<Double> getListOfOrderPrices() {
        Set<Double> priceSet = this.orderMap.keySet();
        return new ArrayList<>(priceSet);
    }

    public void removeOrdersByPrice(double price) {
        this.orderMap.remove(price);
    }

    public void replaceOrdersByPrice(double price, List<Order> orderList) {
        this.orderMap.put(price, orderList);
    }

    public boolean isEmpty() {
        return this.orderMap.isEmpty();
    }

    public void addOrder(Order order) {
        Double price = order.getPrice();
        if (orderMap.containsKey(price)) {
            List<Order> orderMapList = orderMap.get(price);
            orderMapList.add(order);
            orderMap.put(price, orderMapList);
        } else {
            List<Order> orderMapList = new ArrayList<>();
            orderMapList.add(order);
            orderMap.put(price, orderMapList);
        }
    }

    public void clear() {
        this.orderMap.clear();
    }
}
