package danubis.derrick.sample;

import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;

import danubis.derrick.library.Avatar;
import danubis.derrick.library.AvatarListener;
import danubis.derrick.library.Body.Body;
import danubis.derrick.library.Body.TransparentBody;

public class MainActivity extends AppCompatActivity implements AvatarListener {

    public final int VIDEO_REQUEST_CODE = 8901;

    private MyBrain myBrain;
    private Avatar avatar;

    private ArrayList<Answer> answers;
    private int currentAnswerIndex = 0;

    private TextView subtitleTextView;
    private TextView resultTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String path = Environment.getExternalStorageDirectory()
                + "/TestData/dt.mp4";

        TransparentBody.ON_HELLO_SPEAK_START = 0;
        TransparentBody.ON_SPEAK_START = 0;
        TransparentBody.ON_SPEAK_END = 30000;
        TransparentBody.ON_IDLE_1 = 50000;
        TransparentBody.ON_IDLE_2 = 75000;
        TransparentBody.ON_IDLE_3 = 100000;

        TransparentBody myBody = (TransparentBody) findViewById(R.id.video_view);
        myBody.setVideoPath(path);

        avatar = new Avatar.Builder()
                .context(this)
                .xfAppId("56ef40cc")
                .listener(this)
                .speechEngine(Avatar.GOOGLE)
                .language(Avatar.EN)
                .gender(Avatar.FEMALE)
                .transparentBody(myBody)
                .build();

        myBrain = new MyBrain(this);
        myBrain.attachToAvatar(avatar);

        subtitleTextView = (TextView) findViewById(R.id.sub_textView);
        resultTextView = (TextView) findViewById(R.id.result_textView);

        findViewById(R.id.idle_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                avatar.idle();
            }
        });

        findViewById(R.id.sample_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                avatar.speak(getString(R.string.sample));
            }
        });

        findViewById(R.id.speak_button).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:

                        if (answers != null) {
                            currentAnswerIndex = answers.size();
                        }
                        avatar.listen();
                        break;
                    case MotionEvent.ACTION_UP:
                        avatar.stopListening();
                        break;
                }
                return true;
            }
        });
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case VIDEO_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    onAnswerEnded();
                }
                break;
        }
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

        onAnswerEnded();

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
            }
        });
        avatar.speak(result);
//        answers = (ArrayList<Answer>) myBrain.think(result);
//        currentAnswerIndex = 0;
//
//        if (answers != null && !answers.isEmpty()) {
//            myBrain.playAnswer(answers.get(currentAnswerIndex));
//        }
    }


    private void onAnswerEnded() {

        if (answers != null && answers.isEmpty()) {
            currentAnswerIndex++;
            if (currentAnswerIndex < answers.size()) {
                myBrain.playAnswer(answers.get(currentAnswerIndex));
            }
        }
    }
}
