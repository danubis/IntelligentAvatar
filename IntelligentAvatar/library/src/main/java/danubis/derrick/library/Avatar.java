package danubis.derrick.library;

import android.content.Context;

import danubis.derrick.library.Body.Body;
import danubis.derrick.library.Brain.Brain;
import danubis.derrick.library.Ear.Ear;
import danubis.derrick.library.Ear.EarListener;
import danubis.derrick.library.Mouth.Mouth;
import danubis.derrick.library.Mouth.MouthListener;


// TODO: 24/1/17 need to think about the listener again 
public class Avatar {

    public static final String ZH_CN = "zhCN";
    public static final String EN = "en";


    private Context context;
    private Brain brain;
    private Body body;
    private Ear ear;
    private Mouth mouth;
    private AvatarListener listener;


    private Avatar(Context context,
                   Brain brain,
                   Body body,
                   AvatarListener listener) {

        this.context = context;
        this.brain = brain;
        this.body = body;
        this.listener = listener;

        ear = new Ear(brain.getLanguage(), brain.getAccent());
        ear.setListener(listener);

        mouth = new Mouth(brain.getLanguage(), brain.getAccent());
        mouth.setListener(listener);
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

    }


    public void stopSpeaking() {

    }


    public void listen() {

    }


    public void stopListening() {

    }


    public void think(String question) {

    }


    public void pause() {

    }


    public void resume() {

    }


    public void destroy() {

    }

}
