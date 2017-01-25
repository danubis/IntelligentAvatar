package danubis.derrick.library.Ear;

import danubis.derrick.library.AvatarListener;

/**
 * Created by yiluo on 24/1/17.
 */

public abstract class Ear {

    private EarListener listener;

    public void setListener(EarListener listener) {
        this.listener = listener;
    }

    public abstract void listen();

    public abstract void stopListening();

    public abstract void destroy();
}
