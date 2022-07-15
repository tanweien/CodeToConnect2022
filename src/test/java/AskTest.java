import domain.Ask;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class AskTest {
    Ask ask;

    @BeforeEach
    void setup() {
        ask = new Ask(100.0, 5000);
    }

    @Test
    void getAskPriceTest() {
        assertEquals(ask.getAskPrice(),100);
    }

    @Test
    void getAskSizeTest() {
        assertEquals(ask.getAskSize(),5000);
    }
}
