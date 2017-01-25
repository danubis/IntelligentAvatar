package danubis.derrick.sample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;

import danubis.derrick.library.Avatar;
import danubis.derrick.library.AvatarListener;

public class MainActivity extends AppCompatActivity implements AvatarListener {

    private MyBrain myBrain;
    private MyBody myBody;
    private Avatar avatar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myBrain = new MyBrain(Avatar.EN, null);
        myBody = new MyBody();
        avatar = new Avatar.Builder()
                .context(this)
                .listener(this)
                .brain(myBrain)
                .body(myBody)
                .build();
    }


    @Override
    protected void onPause() {
        super.onPause();
        avatar.pause();
    }


    @Override
    protected void onResume() {
        super.onResume();
        avatar.resume();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        avatar.destroy();
    }


    @Override
    public void onSpeakStarted(String textToSpeak) {
        //can use this callback to display subtitle
    }


    @Override
    public void onSpeakEnded() {

    }


    @Override
    public void onListenResult(String result) {

        ArrayList<Answer> answers = (ArrayList<Answer>) myBrain.think(result);
        // TODO: 25/1/17 implement your play list of answer logic here.
    }
}
