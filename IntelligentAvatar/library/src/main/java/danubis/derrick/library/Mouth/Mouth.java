package danubis.derrick.library.Mouth;

import android.content.Context;

/**
 * Created by yiluo on 24/1/17.
 */

public abstract class Mouth {

    protected MouthListener listener;
    protected Context context;

    protected String currentSpeak;


    Mouth(Context context) {
        this.context = context;
    }

    public void setListener(MouthListener listener) {
        this.listener = listener;
    }

    abstract void setTTS();

    public abstract void speak(String textToSpeak);

    public abstract void stopSpeaking();

    public abstract void destroy();

}
