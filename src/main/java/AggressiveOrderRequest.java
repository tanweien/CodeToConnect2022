public class AggressiveOrderRequest extends Request {

    public AggressiveOrderRequest(ClientOrder clientOrder, Order aggressiveOrder) {
        super(clientOrder, aggressiveOrder);
    }

    @Override
    public String toString() {
        return "[A:" + super.toString() + "]";
    }
}
