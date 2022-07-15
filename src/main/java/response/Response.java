package response;

import domain.Order;

abstract public class Response {
    private final Order order;

    public Response(Order order) {
        this.order = order;
    }

    public Order getOrder() {
        return order;
    }
}
