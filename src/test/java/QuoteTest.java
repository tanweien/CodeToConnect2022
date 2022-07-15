import domain.Ask;
import domain.Bid;
import domain.Quote;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class QuoteTest {
    Quote quote;

    @BeforeEach
    void setup() {
        List<Ask> askList = new ArrayList<>();
        List<Bid> bidList = new ArrayList<>();

        Ask ask = new Ask(4000, 1000);
        Ask secondAsk = new Ask(9999, 50);
        Ask thirdAsk = new Ask(10000, 20);

        askList.add(ask);
        askList.add(secondAsk);
        askList.add(thirdAsk);

        Bid bid = new Bid(900, 50);
        Bid secondBid = new Bid(1000, 1000);
        Bid thirdBid = new Bid(1050, 20);

        bidList.add(bid);
        bidList.add(secondBid);
        bidList.add(thirdBid);

        quote = new Quote(50000, bidList, askList);
    }

    @Test
    public void getBestAsk_returnsBestAsk() {
        Ask bestAsk = new Ask(4000, 1000);
        assertEquals(quote.getBestAsk(), bestAsk);
    }
}
