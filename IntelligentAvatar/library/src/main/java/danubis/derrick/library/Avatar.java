package danubis.derrick.library;

import android.content.Context;

import danubis.derrick.library.Body.Body;
import danubis.derrick.library.Body.TransparentBody;
import danubis.derrick.library.Ear.Ear;
import danubis.derrick.library.Ear.EarListener;
import danubis.derrick.library.Ear.XfEar;
import danubis.derrick.library.Mouth.EnMouth;
import danubis.derrick.library.Mouth.Mouth;
import danubis.derrick.library.Mouth.MouthListener;
import danubis.derrick.library.Mouth.XfMouth;


public class Avatar implements MouthListener, EarListener {

    public static final String XUNFEI = "xunfei";
    public static final String GOOGLE = "google";

    public static final String ZH_CN = "zhCN";
    public static final String EN = "en";

    public static final String MALE = "male";
    public static final String FEMALE = "female";

    public static final String SPEAK_TIME = "time";
    public static final String SPEAK_DATE = "date";


    private Context context;
    private Body body;
    private TransparentBody transparentBody;
    private Ear ear;
    private Mouth mouth;
    private AvatarListener listener;

    private String lastSpokeText;
    private boolean isHelloSpeak = false;


    private Avatar(Context context,
                   String appId,
                   String speechEngine,
                   String language,
                   String gender,
                   Body body,
                   TransparentBody transparentBody,
                   AvatarListener listener) {

        this.context = context;
        this.body = body;
        this.transparentBody = transparentBody;
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
                    case ZH_CN:
                        mouth = new XfMouth(context, language, gender);
                        break;
                }

        }

        ear.setListener(this);
        mouth.setListener(this);
    }


    public static class Builder {

        private Context context;
        private String xfAppId;
        private String speechEngine;
        private String language;
        private String gender;
        private Body body;
        private TransparentBody transparentBody;
        private AvatarListener listener;

        public Builder context(Context context) {
            this.context = context;
            return this;
        }

        public Builder xfAppId(String appid) {
            this.xfAppId = appid;
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

        public Builder body(Body body) {
            this.body = body;
            return this;
        }

        public Builder transparentBody(TransparentBody body) {
            this.transparentBody = body;
            return this;
        }

        public Builder listener(AvatarListener listener) {
            this.listener = listener;
            return this;
        }

        public Avatar build() {
            return new Avatar(context, xfAppId, speechEngine,
                    language, gender, body, transparentBody, listener);
        }
    }


    public void helloSpeak(String textToSpeak) {
        lastSpokeText = textToSpeak;
        stopListening();
        isHelloSpeak = true;
        mouth.speak(textToSpeak);
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
        } else if (transparentBody != null) {
            transparentBody.doWaitingAction();
        }
    }


    public void stopListening() {
        ear.stopListening();
    }


    public void idle() {
        stopSpeaking();
        stopListening();
        if (body != null) {
            body.doIdleAction();
        } else if (transparentBody != null) {
            transparentBody.doIdleAction();
        }
    }


    public void pause() {
        stopSpeaking();
        stopListening();
        if (body != null) {
            body.pause();
        } else if (transparentBody != null) {
            transparentBody.pause();
        }
    }


    public void resume() {
        if (body != null) {
            body.resume();
        } else if (transparentBody != null) {
            transparentBody.resume();
        }
    }


    public void destroy() {
        ear.destroy();
        mouth.destroy();
        if (body != null) {
            body.destroy();
        } else if (transparentBody != null) {
            transparentBody.destroy();
        }
    }


    @Override
    public void onSpeakStarted(String speakingText) {
        listener.onSpeakStarted(speakingText);

        if (isHelloSpeak) {
            if (body != null) {
                body.doHelloAction();
            } else if (transparentBody != null) {
                transparentBody.doHelloAction();
            }
            isHelloSpeak = false;
        } else {
            if (body != null) {
                body.doSpeakingAction();
            } else if (transparentBody != null) {
                transparentBody.doSpeakingAction();
            }
        }
    }


    @Override
    public void onSpeakEnded() {
        listener.onSpeakEnded();
        if (body != null) {
            body.doWaitingAction();
        } else if (transparentBody != null) {
            transparentBody.doWaitingAction();
        }
    }


    @Override
    public void onListenResult(String result) {
        listener.onListenResult(result);
    }
}
