package danubis.derrick.intelligentavatar;

import android.annotation.SuppressLint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.TextView;

import danubis.derrick.library.Avatar;

public class MainActivity extends AppCompatActivity {

    String[] speeches = {
            "好的.",
            "你好罗宾. 欢迎向我提问.",
            "本层有三个洗手间, 最近的一个请向前走50米, 在你的左手边.",
            "这是一个在70年代非常受欢迎的澳大利亚电视连续剧, 讲述了一个小男孩和他的丛林袋鼠的故事. 这是一只东方灰色袋鼠."};
    int i = 0;

    private TextView result;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        result = findViewById(R.id.result);

        Avatar avatar = Avatar.builder()
                .context(this)
                .language(Avatar.ZH)
                .gender(Avatar.FEMALE)
                .speechEngine(Avatar.XUNFEI)
                .xfAppId("56ef40cc")
                .enableTranslation(true)
                .listener(new Avatar.AvatarListener() {
                    @Override
                    public void onSpeakStarted(String textToSpeak) {
                        Log.e("test", textToSpeak);
                    }

                    @Override
                    public void onSpeakEnded() {

                    }

                    @Override
                    public void onListenResult(String resultStr) {
                        result.setText(resultStr);
                    }
                })
                .build();

        findViewById(R.id.speak_button).setOnClickListener(view -> {
            if (i >= speeches.length) {
                i = 0;
            }
            avatar.speak(speeches[i]);
            i++;
        });

        findViewById(R.id.listen_button).setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    avatar.listen();
                    return true;
                case MotionEvent.ACTION_UP:
                    avatar.stopListening();
                    return true;
            }
            return false;
        });
    }
}
