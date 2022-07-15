package request;

import domain.ClientOrder;
import domain.Order;

public abstract class Request {
    private final ClientOrder clientOrder;
    private final Order order;

    protected Request(ClientOrder clientOrder, Order order) {
        this.clientOrder = clientOrder;
        this.order = order;
    }

    public ClientOrder getClientOrder() {
        return this.clientOrder;
    }

    public Order getOrder() {
        return this.order;
    }

    @Override
    public String toString() {
        return this.order.getQuantity() + "@" + this.order.getPrice();
    }
}
