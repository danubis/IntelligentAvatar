package danubis.derrick.sample;

import danubis.derrick.library.Brain.Result;

/**
 * Created by yiluo on 24/1/17.
 */

public class Answer extends Result {

    private String ansType;
    private String ans;

    public String getAnsType() {
        return ansType;
    }

    public void setAnsType(String ansType) {
        this.ansType = ansType;
    }

    public String getAns() {
        return ans;
    }

    public void setAns(String ans) {
        this.ans = ans;
    }
}
