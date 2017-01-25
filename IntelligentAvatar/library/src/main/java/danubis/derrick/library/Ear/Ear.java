package danubis.derrick.library.Ear;

import android.content.Context;

import danubis.derrick.library.AvatarListener;

/**
 * Created by yiluo on 24/1/17.
 */

public abstract class Ear {

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
