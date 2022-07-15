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
        String result = "Strategy out: [N:100@52.95][N:200@52.9][N:1050@53.0]\n" +
                "Strategy out: [C:100@52.95][N:50@52.95][N:5000@53.05]\n" +
                "Filled: 5000@53.05, Cumulative Quantity: 5000\n" +
                "Strategy out: [C:50@52.95][C:1050@53.0][N:50@52.85][N:280@53.05][N:350@52.8]\n" +
                "Filled: 280@53.05, Cumulative Quantity: 5280\n" +
                "Strategy out: [C:200@52.9][N:250@52.75]\n" +
                "Strategy out: [C:50@52.85][N:800@52.6]\n";
        assertEquals(outContent.toString(), result);
    }
}