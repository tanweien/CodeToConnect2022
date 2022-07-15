import domain.ClientOrder;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ClientOrderTest {
    ClientOrder clientOrder;

    @BeforeEach
    void setup() {
        clientOrder = new ClientOrder(5000, 0.10);
    }

    @Test
    public void incrementCumulativeQuantity_returnsCorrectValue( ) {
        clientOrder.incrementCumulativeQuantity(200);
        assertEquals(clientOrder.getCumulativeQuantity(),200);
    }

    @Test
    public void isBehindMin_cumulativeGreaterThanMarketTradedVolume() {
        clientOrder.incrementCumulativeQuantity(1000);
        assertFalse(clientOrder.isBehindMin(10));
    }

    @Test
    public void isBehindMin_cumulativeEqualsToMarketTradedVolume() {
        clientOrder.incrementCumulativeQuantity(1000);
        assertFalse(clientOrder.isBehindMin(1000));
    }

    @Test
    public void isBehindMin_cumulativeLessThanMin() {
        int marketTradedVolume = 1000;
        int minVolume = (int) (marketTradedVolume * 0.8 * 0.10);
        clientOrder.incrementCumulativeQuantity(minVolume - 1);
        assertTrue(clientOrder.isBehindMin(1000));
    }

    @Test
    public void isBreachMax_cumulativeLessThanMarketTradedVolume() {
        clientOrder.incrementCumulativeQuantity(50);
        assertFalse(clientOrder.isBreachMax(950));
    }

    @Test
    public void isBehindMin_cumulativeLessThanMax() {
        int marketTradedVolume = 1000;
        int maxVolume = (int) (marketTradedVolume * 1.2 * 0.10);
        clientOrder.incrementCumulativeQuantity(maxVolume - 1);
        assertFalse(clientOrder.isBreachMax(1000));
    }

    @Test
    public void isBehindMin_cumulativeEqualsToMax() {
        int marketTradedVolume = 1000;
        int maxVolume = (int) (marketTradedVolume * 1.2 * 0.10);
        clientOrder.incrementCumulativeQuantity(maxVolume);
        assertFalse(clientOrder.isBreachMax(1000));
    }

    @Test
    public void isBehindMin_cumulativeGreaterThanMax() {
        int marketTradedVolume = 1000;
        int maxVolume = (int) (marketTradedVolume * 1.2 * 0.10);
        clientOrder.incrementCumulativeQuantity(maxVolume + 1);
        assertTrue(clientOrder.isBreachMax(1000));
    }

    @Test
    public void getShortFall_calculatedShortfallLessThanRemainingQuantity() {
        int marketTradedVolume = 1000;
        double minimumRatio = 0.08;
        int cumulativeQuantity = 0;
        int remainingOrderQuantity = 5000;
        int calulatedRoundedShortfall = (int) (marketTradedVolume * minimumRatio - cumulativeQuantity);
        assertEquals(clientOrder.getShortfall(1000), calulatedRoundedShortfall);
    }

    @Test
    public void getShortFall_calculatedShortfallGreaterThanRemainingQuantity() {
        int marketTradedVolume = 1000;
        double minimumRatio = 0.08;
        clientOrder.incrementCumulativeQuantity(5000);
        int cumulativeQuantity = 5000;
        int remainingOrderQuantity = 0;
        int calulatedRoundedShortfall = (int) (marketTradedVolume * minimumRatio - cumulativeQuantity);
        assertEquals(clientOrder.getShortfall(1000), calulatedRoundedShortfall);
    }

    @Test
    public void getRemainingOrderQuantity_returnsCorrectQuantity() {
        int totalQuantity = 5000;
        int cumulativeQuantity = 4950;
        clientOrder.incrementCumulativeQuantity(cumulativeQuantity);
        int remainingOrderQuantity = totalQuantity - cumulativeQuantity;
        assertEquals(clientOrder.getRemainingOrderQuantity(), remainingOrderQuantity);
    }
}


