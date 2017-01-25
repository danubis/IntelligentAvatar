package danubis.derrick.library;

import android.content.Context;

import danubis.derrick.library.Body.Body;
import danubis.derrick.library.Brain.Brain;
import danubis.derrick.library.Ear.CnEar;
import danubis.derrick.library.Ear.Ear;
import danubis.derrick.library.Ear.EarListener;
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


    private Avatar(Context context,
                   Brain brain,
                   Body body,
                   AvatarListener listener) {

        this.context = context;
        this.body = body;
        this.listener = listener;

        switch (brain.getLanguage()) {
            case ZH_CN:
                ear = new CnEar();
                mouth = new CnMouth();
                break;
            case EN:
                ear = new CnEar();
                mouth = new EnMouth();
                break;
        }
        ear.setListener(this);
        mouth.setListener(this);
    }


    public static class Builder {

        private Context context;
        private Brain brain;
        private Body body;
        private AvatarListener listener;

        public Builder context(Context context) {
            this.context = context;
            return this;
        }

        public Builder brain(Brain brain) {
            this.brain = brain;
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
            return new Avatar(context, brain, body, listener);
        }
    }


    public void speak(String textToSpeak) {
        mouth.speak(textToSpeak);
    }


    public void stopSpeaking() {
        mouth.stopSpeaking();
    }


    public void listen() {
        ear.listen();
    }


    public void stopListening() {
        ear.stopListening();
    }


    public void pause() {
        body.pause();
    }


    public void resume() {
        body.resume();
    }


    public void destroy() {
        body.destroy();
        ear.destroy();
        mouth.destroy();
    }


    @Override
    public void onSpeakStarted(String speakingText) {
        body.doSpeakingAction();
        listener.onSpeakStarted(speakingText);
    }


    @Override
    public void onSpeakEnded() {
        body.doWaitingAction();
        listener.onSpeakEnded();
    }


    @Override
    public void onListenResult(String result) {
        listener.onListenResult(result);
    }
}
