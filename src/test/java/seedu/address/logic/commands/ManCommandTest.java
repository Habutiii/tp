package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class ManCommandTest {

    @Test
    public void man_returnsManualString() {
        // empty target => any constructor you use; if your ManCommand takes (String), pass ""
        ManCommand cmd = new ManCommand("");
        String manual = cmd.man();

        assertTrue(manual.contains("NAME"));
        assertTrue(manual.contains("USAGE"));
        assertTrue(manual.toLowerCase().contains("man"));
    }
}
