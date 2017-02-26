package danubis.derrick.library.Body;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.AttributeSet;
import android.widget.VideoView;

/**
 * Created by yiluo on 24/1/17.
 */

public class Body extends VideoView implements MediaPlayer.OnCompletionListener {

    public static final int ON_HELLO_SPEAK_START = 5100;
    public static final int ON_SPEAK_START = 10000;
    public static final int ON_SPEAK_END = 41000;

    private int pausedPosition;


    public Body(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOnCompletionListener(this);
    }


    public void doSpeakingAction() {
        doAction(ON_SPEAK_START);
    }


    public void doWaitingAction() {
        doAction(ON_SPEAK_END);
    }


    private void doAction(int msec) {
        pause();
        seekTo(msec);
        start();
    }


    @Override
    public void pause() {
        super.pause();
        pausedPosition = getCurrentPosition();
    }


    @Override
    public void resume() {
        seekTo(pausedPosition);
        start();
    }


    public void destroy() {
        stopPlayback();
    }


    @Override
    public void onCompletion(MediaPlayer mp) {
        mp.pause();
        mp.seekTo(ON_SPEAK_END);
        mp.start();
    }
}
