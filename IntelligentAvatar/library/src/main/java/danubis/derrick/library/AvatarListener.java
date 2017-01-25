package danubis.derrick.library;

/**
 * Created by yiluo on 24/1/17.
 */

public interface AvatarListener {

    void onSpeakStarted(String textToSpeak);
    void onSpeakEnded();
    void onListenResult(String result);
}
