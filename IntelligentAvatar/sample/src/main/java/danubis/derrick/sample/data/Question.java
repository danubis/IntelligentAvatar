package danubis.derrick.sample.data;

/**
 * Created by yiluo on 31/7/17.
 */

public class Question {

    private String hint;
    private String answer;

    public Question(String hint, String answer) {
        this.hint = hint;
        this.answer = answer;
    }

    public String getHint() {
        return hint;
    }

    public String getAnswer() {
        return answer;
    }
}
