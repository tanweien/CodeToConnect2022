package response;

import domain.Order;

public class CancelResponse extends Response {

    public CancelResponse(Order cancelOrder) {
        super(cancelOrder);
    }

}
