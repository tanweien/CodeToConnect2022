package request;

import domain.ClientOrder;
import domain.Order;

public class CancelRequest extends Request {

    public CancelRequest(ClientOrder clientOrder, Order cancelOrder) {
        super(clientOrder, cancelOrder);
    }

    @Override
    public String toString() {
        return "[C:" + super.toString() + "]";
    }
}
