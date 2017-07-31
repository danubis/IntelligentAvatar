package danubis.derrick.sample;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.magicpi.maggie.Maggie;
import com.magicpi.maggie.MaggieImpl;
import com.magicpi.maggie.model.Dictionary;

import java.io.FileNotFoundException;

import danubis.derrick.library.Avatar;
import danubis.derrick.sample.data.MockDataRepository;
import io.indico.Indico;
import io.indico.api.utils.IndicoException;

public class MainActivity extends AppCompatActivity implements Avatar.AvatarListener, MainContract.View {

    private static final String LAGTAG = "MainActivity";
    private TextView subtitleTextView;
    private TextView resultTextView;

    //treat avatar as a view in activity
    private Avatar avatarView;

    private MainContract.Presenter presenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        subtitleTextView = (TextView) findViewById(R.id.sub_textView);
        resultTextView = (TextView) findViewById(R.id.result_textView);

        Button speakButton = (Button) findViewById(R.id.speak_button);

        avatarView = Avatar.builder()
                .context(this)
                .xfAppId("56ef40cc")
                .listener(this)
                .speechEngine(Avatar.GOOGLE)
                .language(Avatar.EN)
                .gender(Avatar.FEMALE)
                .attachButton(speakButton)
                .build();

        String path = Environment.getExternalStorageDirectory() + "/maggie/museum_general_1.json";

        try {
            Indico indico = new Indico("344bcb1eff966604ef32ee8b4ebafcc0");
            Maggie maggie = new MaggieImpl(0.85, 0.40, indico);

            Dictionary dictionary = Dictionary.load(path);
            maggie.setDictionary(dictionary);

            presenter = new MainPresenter(this, new MaggieWrapper(maggie), new MockDataRepository());

        } catch (IndicoException | FileNotFoundException e) {
            e.printStackTrace();
        }

        findViewById(R.id.save_button).setOnClickListener(v -> presenter.saveDictionary(path));
    }

    @Override
    protected void onPause() {
        super.onPause();
        avatarView.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        avatarView.resume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        avatarView.destroy();
        presenter.onDestroy();
    }

    @Override
    public void onSpeakStarted(final String textToSpeak) {
        runOnUiThread(() -> subtitleTextView.setText(textToSpeak));
    }

    @Override
    public void onSpeakEnded() {
        runOnUiThread(() -> subtitleTextView.setText(""));
    }

    @Override
    public void onListenResult(final String result) {

        runOnUiThread(() -> resultTextView.setText("Voice recognition result: " + result));
        presenter.findAnswer(result);
    }

    @Override
    public void speak(String speech) {
        avatarView.speak(speech);
    }

    @Override
    public void suggest(String suggestion) {
        avatarView.speak("do you mean " + suggestion);
    }
}