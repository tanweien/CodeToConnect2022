abstract class Response {
    private final Order order;

    protected Response(Order order) {
        this.order = order;
    }

    public Order getOrder() {
        return order;
    }
}
