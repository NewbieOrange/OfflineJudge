import xyz.chengzi.offlinejudge.data.TestCase;

public class SpecialJudge extends TestCase {
    public SpecialJudge() {
        // The input will be "34\n56\n23\n78\n4395\n1\n34\n66\n" in System.in,
        // or the entire array as the argument of main(args) function.
    }

    public String[] getInputs() {
        return new String[] { "34\n56\n23\n78\n4395\n1\n34\n66\n" };
    }

    @Override
    public InputType getInputType() {
        return InputType.SYSTEM_IN; // Or InputType.ARGS for command-line arguments.
    }

    @Override
    public boolean isAnswerCorrect(String answer) {
        return true; // True for Accepted, False for Wrong Answer.
    }
}
