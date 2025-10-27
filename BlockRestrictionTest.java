import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.lang.reflect.Field;

public class BlockRestrictionTest {
    public static void main(String[] args) throws Exception {
        BlockRestrictionTest test = new BlockRestrictionTest();
        test.testOnlyTargetMayBlock();
        System.out.println("All block restriction tests passed.");
    }

    private void testOnlyTargetMayBlock() throws Exception {
        String simulatedInput = "3\n2\n\n";
        InputStream originalIn = System.in;
        PrintStream originalOut = System.out;
        ByteArrayInputStream testIn = new ByteArrayInputStream(simulatedInput.getBytes());
        ByteArrayOutputStream outputBuffer = new ByteArrayOutputStream();
        PrintStream testOut = new PrintStream(outputBuffer, true);

        System.setIn(testIn);
        System.setOut(testOut);
        Game.resetInputScanner();

        Game game = new Game();
        Player[] players = new Player[]{
                new Player("Alice", 1),
                new Player("Bob", 2),
                new Player("Charlie", 3)
        };
        Field playersField = Game.class.getDeclaredField("players");
        playersField.setAccessible(true);
        playersField.set(game, players);

        boolean result;
        try {
            result = game.blockAndChallenge(0, "assassination", Influence.INFLUENCES[4], 1);
        } finally {
            System.setIn(originalIn);
            System.setOut(originalOut);
            Game.resetInputScanner();
        }

        if (result) {
            throw new AssertionError("Expected the assassination to be blocked by the target.");
        }

        String output = outputBuffer.toString();
        if (!output.contains("Only player 2 may block this action.")) {
            throw new AssertionError("Expected an error when a third party attempted to block, but got: " + output);
        }

        System.out.println("testOnlyTargetMayBlock passed.");
    }
}
