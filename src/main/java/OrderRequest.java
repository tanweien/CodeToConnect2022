public class OrderRequest extends Request {

    public OrderRequest(ClientOrder clientOrder, Order order) {
        super(clientOrder, order);
    }

    @Override
    public String toString() {
        return "[0:" + super.toString() + "]";
    }
}
