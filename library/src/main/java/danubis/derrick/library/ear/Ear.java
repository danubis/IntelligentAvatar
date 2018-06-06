package danubis.derrick.library.ear;

import android.content.Context;

import com.iflytek.cloud.SpeechRecognizer;

/**
 * Created by yiluo on 24/1/17.
 */

public abstract class Ear {

    SpeechRecognizer asr;
    String tempResult = "";

    EarListener listener;
    Context context;
    String appId;

    Ear(Context context, String appId) {
        this.context = context;
        this.appId = appId;
    }

    public void setListener(EarListener listener) {
        this.listener = listener;
    }

    abstract void setAsr();

    public abstract void listen();

    public abstract void stopListening();

    public abstract void destroy();
}
