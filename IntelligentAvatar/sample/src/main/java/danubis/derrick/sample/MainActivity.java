package danubis.derrick.sample;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import danubis.derrick.library.Avatar;
import danubis.derrick.library.AvatarListener;
import danubis.derrick.library.Body;

public class MainActivity extends AppCompatActivity implements AvatarListener{

    private MyBrain myBrain;
    private Body myBody;
    private Avatar avatar;

    private TextView subtitleTextView;
    private TextView resultTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String path = "your mp4 video file path";

        myBody = (Body) findViewById(R.id.videoView);
        myBody.setVideoPath(path);
        myBody.start();

        subtitleTextView = (TextView) findViewById(R.id.sub_textView);
        resultTextView = (TextView) findViewById(R.id.result_textView);

        findViewById(R.id.speak_button).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        avatar.listen();
                        break;
                    case MotionEvent.ACTION_UP:
                        avatar.stopListening();
                        break;
                }
                return true;
            }
        });

        myBrain = new MyBrain(Avatar.EN, null);
        avatar = new Avatar.Builder()
                .context(this)
                .xfAppId("-------")
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
    public void onSpeakStarted(final String textToSpeak) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                subtitleTextView.setText(textToSpeak);
            }
        });
    }


    @Override
    public void onSpeakEnded() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                subtitleTextView.setText("");
            }
        });
    }


    @Override
    public void onListenResult(final String result) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                resultTextView.setText(result);
                avatar.speak(result);
            }
        });

        //implement your play list of answer logic here
    }
}
