package danubis.derrick.library;

import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import danubis.derrick.library.body.Body;
import danubis.derrick.library.ear.Ear;
import danubis.derrick.library.ear.EarListener;
import danubis.derrick.library.ear.XfEar;
import danubis.derrick.library.mouth.EnMouth;
import danubis.derrick.library.mouth.Mouth;
import danubis.derrick.library.mouth.MouthListener;
import danubis.derrick.library.mouth.XfMouth;


public class Avatar implements MouthListener, EarListener {

    public static final String LAGTAG = "Avatar";

    public static final String XUNFEI = "xunfei";
    public static final String GOOGLE = "google";

    public static final String ZH = "zh";
    public static final String EN = "en";

    public static final String MALE = "male";
    public static final String FEMALE = "female";

    public static final String SPEAK_TIME = "time";
    public static final String SPEAK_DATE = "date";

    private Ear ear;
    private Mouth mouth;
    private Body body;
    private AvatarListener listener;

    private String lastSpokeText;

    private Avatar(Context context, Body body,
                   String appId, String speechEngine,
                   String language, String gender,
                   AvatarListener listener, View view) {

        this.body = body;
        this.listener = listener;

        switch (speechEngine) {
            case XUNFEI:
                ear = new XfEar(context, language, appId);
                mouth = new XfMouth(context, language, gender);
                break;
            case GOOGLE:
                ear = new XfEar(context, language, appId);
                switch (language) {
                    case EN:
                        mouth = new EnMouth(context, gender);
                        break;
                    case ZH:
                        mouth = new XfMouth(context, language, gender);
                        break;
                }
        }

        ear.setListener(this);
        mouth.setListener(this);
        if (view != null) {
            view.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            Log.e(LAGTAG, "on action down: " + v.getId());
                            listen();
                            break;
                        case MotionEvent.ACTION_UP:
                            Log.e(LAGTAG, "on action up: " + v.getId());
                            stopListening();
                            break;
                    }
                    return true;
                }
            });
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private Context context;
        private Body body;
        private String xfAppId;
        private String speechEngine;
        private String language;
        private String gender;
        private AvatarListener listener;
        private View view;

        private Builder() {
        }

        public Builder context(Context context) {
            this.context = context;
            return this;
        }

        public Builder xfAppId(String appid) {
            this.xfAppId = appid;
            return this;
        }

        public Builder body(Body body) {
            this.body = body;
            return this;
        }

        public Builder speechEngine(String speechEngine) {
            this.speechEngine = speechEngine;
            return this;
        }

        public Builder language(String language) {
            this.language = language;
            return this;
        }

        public Builder gender(String gender) {
            this.gender = gender;
            return this;
        }

        public Builder listener(AvatarListener listener) {
            this.listener = listener;
            return this;
        }

        public Builder attachButton(View view) {
            this.view = view;
            return this;
        }

        public Avatar build() {
            return new Avatar(context, body, xfAppId, speechEngine,
                    language, gender, listener, view);
        }
    }

    public void helloSpeak(String textToSpeak) {
        lastSpokeText = textToSpeak;
        stopListening();
        mouth.speak(textToSpeak, true);
    }

    public void speak(String textToSpeak) {
        lastSpokeText = textToSpeak;
        stopListening();
        mouth.speak(textToSpeak, false);
    }

    public void speakTime() {
        mouth.speakTime();
    }

    public void speakDate() {
        mouth.speakDate();
    }

    public void stopSpeaking() {
        mouth.stopSpeaking();
    }

    public String getLastSpokeText() {
        return lastSpokeText;
    }

    public void listen() {
        stopSpeaking();
        ear.listen();
    }

    public void stopListening() {
        ear.stopListening();
    }

    public void idle() {
        stopSpeaking();
        stopListening();
        if (body != null) {
            body.doIdleAction();
        }
    }

    public void pause() {
        stopSpeaking();
        stopListening();
        if (body != null) {
            body.pause();
        }
    }

    public void resume() {
        if (body != null) {
            body.resume();
        }
    }

    public void destroy() {
        ear.destroy();
        mouth.destroy();
        if (body != null) {
            body.destroy();
        }
    }

    @Override
    public void onSpeakStarted(String speakingText, boolean isHelloSpeak) {
        listener.onSpeakStarted(speakingText);
        if (body != null) {
            if (isHelloSpeak) {
                body.doHelloAction();
            } else {
                body.doSpeakAction();
            }
        }
    }

    @Override
    public void onSpeakEnded() {
        listener.onSpeakEnded();
        if (body != null) {
            body.doWaitAction();
        }
    }

    @Override
    public void onListenResult(String result) {
        listener.onListenResult(result);
    }

    public interface AvatarListener {

        void onSpeakStarted(String textToSpeak);

        void onSpeakEnded();

        void onListenResult(String result);
    }

}