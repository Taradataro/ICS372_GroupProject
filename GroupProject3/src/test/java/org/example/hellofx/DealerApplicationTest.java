package org.example.hellofx;

import javafx.stage.Stage;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

public class DealerApplicationTest {

    @Test
    void startMethodExists() throws NoSuchMethodException {
        // Test the start(Stage) method
        Method start = DealerApplication.class.getMethod("start", Stage.class);
        assertNotNull(start, "DealerApplication.start(Stage) should exist");
    }

    @Test
    void exitProgramMethodExists() throws NoSuchMethodException {
        // Test the exitProgram(Stage) method
        Method exit = DealerApplication.class.getMethod("exitProgram", Stage.class);
        assertNotNull(exit, "DealerApplication.exitProgram(Stage) should exist");
    }

    @Test
    void mainMethodExists() throws NoSuchMethodException {
        // Test the main(String[]) method
        Method main = DealerApplication.class.getMethod("main", String[].class);
        assertNotNull(main, "DealerApplication.main(String[]) should exist");
    }
}
