import domain.Bid;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class BidTest {
    Bid bid;

    @BeforeEach
    void setup() {
        bid = new Bid(100.0, 5000);
    }

    @Test
    void getBidPriceTest() {
        assert(bid.getBidPrice() == 100);
    }

    @Test
    void getBidSizeTest() {
        assert(bid.getBidSize() == 5000);
    }
}
