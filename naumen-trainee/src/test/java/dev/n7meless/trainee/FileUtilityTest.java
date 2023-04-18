package dev.n7meless.trainee;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class FileUtilityTest {
    @Test
    void readNonexistentFileTest() {
        Assertions.assertDoesNotThrow(() ->
                Main.readFromFile(";ASLDKALDL;"));
    }

    @Test
    void writeNonexistentFileTest() {
        Assertions.assertDoesNotThrow(() ->
                Main.writeToFile(";ASLDKALDL;", ""));
    }
}