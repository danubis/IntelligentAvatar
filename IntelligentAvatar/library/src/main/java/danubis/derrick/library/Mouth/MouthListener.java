package danubis.derrick.library.Mouth;

/**
 * Created by yiluo on 25/1/17.
 */

public interface MouthListener {

    void onSpeakStarted(String textToSpeak);
    void onSpeakEnded();
}
