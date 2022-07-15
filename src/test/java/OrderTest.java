import domain.Order;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class OrderTest {
    Order order;

    @BeforeEach
    void setup() {
        order = new Order(50000, 100, 53.05);
    }

    @Test
    void isLongerThanThreeMinutesTest() {
        int threeMins = 50000 + 180000;
        int lessThanThreeMins = 50000 + 180000 - 1;
        int longerThanThreeMins = 50000 + 180000 + 1;
        assertTrue(order.isLongerThanThreeMinutes(longerThanThreeMins));
        assertFalse(order.isLongerThanThreeMinutes(threeMins));
        assertFalse(order.isLongerThanThreeMinutes(lessThanThreeMins));
    }
}
