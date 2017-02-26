package danubis.derrick.sample;

import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

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
                + "/Museum_Data/Video_Files/avatar_3.mp4";

        TransparentBody myBody = (TransparentBody) findViewById(R.id.videoView);
        myBody.setVideoPath(path);
//        myBody.start();

        subtitleTextView = (TextView) findViewById(R.id.sub_textView);
        resultTextView = (TextView) findViewById(R.id.result_textView);

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

        avatar = new Avatar.Builder()
                .context(this)
                .xfAppId("56ef40cc")
                .listener(this)
                .language(Avatar.EN)
                .transparentBody(myBody)
                .build();

        myBrain = new MyBrain(this);
        myBrain.attachToAvatar(avatar);
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
