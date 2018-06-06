package danubis.derrick.library.mouth;

/**
 * Created by yiluo on 25/1/17.
 */

public interface MouthListener {

    void onSpeakStarted(String textToSpeak, boolean isHelloSpeak);

    void onSpeakEnded();
}
