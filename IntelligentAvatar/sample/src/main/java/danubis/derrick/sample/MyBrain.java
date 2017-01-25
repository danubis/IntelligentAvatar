package danubis.derrick.sample;

import java.util.ArrayList;

import danubis.derrick.library.Brain.Result;
import danubis.derrick.library.Brain.Brain;

/**
 * Created by yiluo on 24/1/17.
 */

public class MyBrain extends Brain {


    public MyBrain(String language, String accent) {
        super(language, accent);
    }

    @Override
    public ArrayList<? extends Result> think(String question) {

        Answer answer = new Answer();
        ArrayList<Answer> answers = new ArrayList<>();
        answers.add(answer);

        return answers;
    }

}
