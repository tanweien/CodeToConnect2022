package response;

import domain.Order;

public class FillResponse extends Response {

    public FillResponse(Order order) {
        super(order);
    }

    public int getFillQuantity() {
        return super.getOrder().getQuantity();
    }

    public double getFillPrice() {
        return super.getOrder().getPrice();
    }
}
