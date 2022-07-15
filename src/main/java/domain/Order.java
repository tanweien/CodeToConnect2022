package domain;

public class Order {
    private int timestamp;
    private int quantity;
    private double price;

    public Order(int timestamp, int quantity, double price) {
        this.timestamp = timestamp;
        this.quantity = quantity;
        this.price = price;
    }

    public int getQuantity() {
        return this.quantity;
    }

    public double getPrice() {
        return this.price;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public boolean isLongerThanThreeMinutes(int nextTime) {
        return (nextTime - timestamp > 180000);
    }

    @Override
    public String toString() {
        return this.timestamp + ":" + this.getQuantity() + "@" + this.getPrice();
    }

    @Override
    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }

        if (!(object instanceof Order)) {
            return false;
        }

        Order order = (Order) object;

        return order.getPrice() == this.getPrice() && order.getTimestamp() == this.getTimestamp() && order.getQuantity() == this.getQuantity();
    }
}

