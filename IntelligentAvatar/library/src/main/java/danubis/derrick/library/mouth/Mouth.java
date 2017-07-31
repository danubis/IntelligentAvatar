package danubis.derrick.library.mouth;

import android.content.Context;

/**
 * Created by yiluo on 24/1/17.
 */

public abstract class Mouth {

    MouthListener listener;
    Context context;
    String currentSpeak;
    boolean isHelloSpeak;

    Mouth(Context context) {
        this.context = context;
    }

    public void setListener(MouthListener listener) {
        this.listener = listener;
    }

    abstract void setTTS();

    public abstract void speak(String textToSpeak, boolean isHelloSpeak);

    public abstract void speakTime();

    public abstract void speakDate();

    public abstract void stopSpeaking();

    public abstract void destroy();

}
