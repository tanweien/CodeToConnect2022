import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SimulatorTest {
    Simulator simulator;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;

    @BeforeEach
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    @AfterEach
    public void restoreStreams() {
        System.setOut(originalOut);
        System.setErr(originalErr);
    }

    @Test
    void runSimulator_testPassivePosting() {
        simulator = new Simulator();
        simulator.init("passive_posting_test.csv", "passive_posting_client_order_test.csv");
        simulator.run();
        String result = "Strategy out: [N:500@10.0][N:100@9.8][N:400@9.9]\n";
        assertEquals(outContent.toString(), result);
    }

    @Test
    void runSimulator_testSimulator() {
        simulator = new Simulator();
        simulator.init("simulator_test.csv", "simulator_client_order_test.csv");
        simulator.run();
        String result = "Strategy out: [N:1000@53.0]\n" +
                "Filled: 1000@53.05, Cumulative Quantity: 1000\n" +
                "Client order completed\n";
        assertEquals(outContent.toString(), result);
    }
}
