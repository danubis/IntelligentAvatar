package danubis.derrick.library.body;

/**
 * Created by yiluo on 24/7/17.
 */

public interface Body {

    void init();

    void pause();

    void resume();

    void destroy();

    void doHelloAction();

    void doSpeakAction();

    void doWaitAction();

    void doIdleAction();
}
