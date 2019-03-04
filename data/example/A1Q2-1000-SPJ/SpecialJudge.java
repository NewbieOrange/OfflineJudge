import xyz.chengzi.offlinejudge.data.TestCase;

public class SpecialJudge extends TestCase {
    public SpecialJudge() {
        super(new String[]{"1", "2", "3", "4", "5", "6", "7", "777"}, null);
        // The input will be "1\n2\n3\n4\n5\n6\n7\n777\n" in System.in,
        // or the entire array as the argument of main(args) function.
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
