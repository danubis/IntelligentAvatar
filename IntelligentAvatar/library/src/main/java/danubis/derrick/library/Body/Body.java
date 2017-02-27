package danubis.derrick.library.Body;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.AttributeSet;
import android.widget.VideoView;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by yiluo on 24/1/17.
 */

public class Body extends VideoView implements MediaPlayer.OnCompletionListener {

    public static int ON_HELLO_SPEAK_START = 5100;
    public static int ON_SPEAK_START = 10000;
    public static int ON_SPEAK_END = 41000;
    public static int ON_IDLE_1 = -1;
    public static int ON_IDLE_2 = -1;
    public static int ON_IDLE_3 = -1;

    private int pausedPosition;


    public Body(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOnCompletionListener(this);
    }


    public void doSpeakingAction() {
        doAction(ON_SPEAK_START);
    }


    public void doIdleAction() {

        ArrayList<Integer> idleActions = new ArrayList<>();

        if (ON_IDLE_1 != -1) {
            idleActions.add(ON_IDLE_1);
        }

        if (ON_IDLE_2 != -1) {
            idleActions.add(ON_IDLE_2);
        }

        if (ON_IDLE_3 != -1) {
            idleActions.add(ON_IDLE_3);
        }

        if (idleActions.isEmpty()) {
            doWaitingAction();
        } else {
            Random random = new Random();
            int idleAction = random.nextInt(idleActions.size());
            doAction(idleActions.get(idleAction));
        }
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
