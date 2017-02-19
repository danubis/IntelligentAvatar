package danubis.derrick.library;

import android.content.Context;

import danubis.derrick.library.Ear.CnEar;
import danubis.derrick.library.Ear.Ear;
import danubis.derrick.library.Ear.EarListener;
import danubis.derrick.library.Ear.EnEar;
import danubis.derrick.library.Mouth.CnMouth;
import danubis.derrick.library.Mouth.EnMouth;
import danubis.derrick.library.Mouth.Mouth;
import danubis.derrick.library.Mouth.MouthListener;


public class Avatar implements MouthListener, EarListener {

    public static final String ZH_CN = "zhCN";
    public static final String EN = "en";

    public static final String SPEAK_TIME = "time";
    public static final String SPEAK_DATE = "date";


    private Context context;
    private Body body;
    private Ear ear;
    private Mouth mouth;
    private AvatarListener listener;

    private String lastSpokeText;


    private Avatar(Context context,
                   String appId,
                   String language,
                   Body body,
                   AvatarListener listener) {

        this.context = context;
        this.body = body;
        this.listener = listener;

        switch (language) {
            case ZH_CN:
                ear = new CnEar(context, appId);
                mouth = new CnMouth(context);
                break;
            case EN:
                ear = new EnEar(context, appId);
                mouth = new EnMouth(context);
                break;
        }
        ear.setListener(this);
        mouth.setListener(this);
    }


    public static class Builder {

        private Context context;
        private String xfAppId;
        private String language;
        private Body body;
        private AvatarListener listener;

        public Builder context(Context context) {
            this.context = context;
            return this;
        }

        public Builder xfAppId(String appid) {
            this.xfAppId = appid;
            return this;
        }

        public Builder language(String language) {
            this.language = language;
            return this;
        }

        public Builder body(Body body) {
            this.body = body;
            return this;
        }

        public Builder listener(AvatarListener listener) {
            this.listener = listener;
            return this;
        }

        public Avatar build() {
            return new Avatar(context, xfAppId, language, body, listener);
        }
    }


    public void speak(String textToSpeak) {
        lastSpokeText = textToSpeak;
        stopListening();
        mouth.speak(textToSpeak);
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
        if (body != null) {
            body.doWaitingAction();
        }
    }


    public void stopListening() {
        ear.stopListening();
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
    public void onSpeakStarted(String speakingText) {
        listener.onSpeakStarted(speakingText);
        if (body != null) {
            body.doSpeakingAction();
        }
    }


    @Override
    public void onSpeakEnded() {
        listener.onSpeakEnded();
        if (body != null) {
            body.doWaitingAction();
        }
    }


    @Override
    public void onListenResult(String result) {
        listener.onListenResult(result);
    }
}
