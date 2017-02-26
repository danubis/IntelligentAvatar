package danubis.derrick.library.Mouth;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.regex.Pattern;

/**
 * Created by yiluo on 25/1/17.
 */

public class EnMouth extends Mouth {

    private TextToSpeech tts;

    private boolean isSpeaking = false;


    public EnMouth(Context context) {
        super(context);
        setTTS();
    }


    @Override
    void setTTS() {
        tts = new TextToSpeech(super.context, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                tts.setLanguage(new Locale("en", "US"));
                tts.setSpeechRate(1.1f);
                tts.setPitch(1.4f);
            }
        });
        tts.setOnUtteranceProgressListener(ttsListener);
    }


    @Override
    public void speak(String textToSpeak) {
        currentSpeak = textToSpeak;
        stopSpeaking();
        textToSpeak = textToSpeak.replaceAll("(?i)" + Pattern.quote("magicpi"), "MagicPai");
        tts.speak(textToSpeak, TextToSpeech.QUEUE_ADD, null, "my_speak");
    }


    @Override
    public void speakTime() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf;
        String timeSpeak = "The time is ";
        sdf = new SimpleDateFormat("HH:mm");

        timeSpeak = timeSpeak + sdf.format(calendar.getTime());
        speak(timeSpeak);
    }


    @Override
    public void speakDate() {
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        String dateSpeak = "Today is ";
        String[] days_en = {"SUNDAY", "MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY"};

        dateSpeak = dateSpeak + days_en[day];
        speak(dateSpeak);
    }


    @Override
    public void stopSpeaking() {
        if (isSpeaking) {
            tts.stop();
            ttsListener.onDone(null);
        }
    }

    @Override
    public void destroy() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
    }


    private UtteranceProgressListener ttsListener = new UtteranceProgressListener() {
        @Override
        public void onStart(String utteranceId) {

            isSpeaking = true;
            listener.onSpeakStarted(currentSpeak);

        }

        @Override
        public void onDone(String utteranceId) {
            isSpeaking = false;
            listener.onSpeakEnded();
        }

        @Override
        public void onError(String utteranceId) {
        }
    };
}
