public class AggressiveOrderResponse extends Response {

    public AggressiveOrderResponse(Order order) {
        super(order);
    }

    public int getFillQuantity() {
        return super.getOrder().getQuantity();
    }

    public double getFillPrice() {
        return super.getOrder().getPrice();
    }
}