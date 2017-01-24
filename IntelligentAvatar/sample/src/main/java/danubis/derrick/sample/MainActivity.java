package danubis.derrick.sample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import danubis.derrick.library.Avatar;
import danubis.derrick.library.Ear.EarListener;
import danubis.derrick.library.Mouth.MouthListener;

public class MainActivity extends AppCompatActivity implements EarListener, MouthListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MyBrain myBrain = new MyBrain(Avatar.EN, null);

        Avatar avatar = new Avatar.Builder()
                .context(this)
                .earListener(this)
                .mouthListener(this)
                .brain(myBrain)
                .build();
    }

    @Override
    public void onListenResult(String result) {

    }

    @Override
    public void onSpeakCompleted() {

    }
}
