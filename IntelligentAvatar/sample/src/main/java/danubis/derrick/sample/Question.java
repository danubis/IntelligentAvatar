package danubis.derrick.sample;

import java.util.ArrayList;

import danubis.derrick.library.Brain.Result;

/**
 * Created by yiluo on 25/1/17.
 */

public class Question {

    private ArrayList<Result> answer;
    private ArrayList<String> keyword;
    private String hint;
    private String ref;

    public ArrayList<Result> getAnswer() {
        return answer;
    }

    public void setAnswer(ArrayList<Result> answer) {
        this.answer = answer;
    }

    public ArrayList<String> getKeyword() {
        return keyword;
    }

    public void setKeyword(ArrayList<String> keyword) {
        this.keyword = keyword;
    }

    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }
}
