package danubis.derrick.intelligentavatar;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import danubis.derrick.library.Avatar;

public class MainActivity extends AppCompatActivity {

    String[] speeches = {
            "好的.",
            "你好罗宾. 欢迎向我提问.",
            "本层有三个洗手间, 最近的一个请向前走50米, 在你的左手边.",
            "这是一个在70年代非常受欢迎的澳大利亚电视连续剧, 讲述了一个小男孩和他的丛林袋鼠的故事. 这是一只东方灰色袋鼠."};
    int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Avatar avatar = Avatar.builder()
                .context(this)
                .language(Avatar.ZH)
                .gender(Avatar.FEMALE)
                .speechEngine(Avatar.XUNFEI)
                .listener(new Avatar.AvatarListener() {
                    @Override
                    public void onSpeakStarted(String textToSpeak) {

                    }

                    @Override
                    public void onSpeakEnded() {

                    }

                    @Override
                    public void onListenResult(String result) {

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
    }
}
