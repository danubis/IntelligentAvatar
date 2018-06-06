package danubis.derrick.library.mouth;

import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;

import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SynthesizerListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import danubis.derrick.library.Avatar;


public class CnMouth extends Mouth {

    private SpeechSynthesizer tts;
    private String language = Avatar.ZH;
    private String gender = Avatar.FEMALE;


    public CnMouth(Context context, String gender) {
        super(context);
        this.gender = gender;
        setTTS();
    }


    public CnMouth(Context context, String language, String gender) {
        super(context);
        this.language = language;
        this.gender = gender;
        setTTS();
    }


    @Override
    void setTTS() {

        tts = SpeechSynthesizer.createSynthesizer(context, null);

        switch (language) {
            case Avatar.EN:
                switch (gender) {
                    case Avatar.MALE:
                        tts.setParameter(SpeechConstant.VOICE_NAME, "xiaofeng");
                        break;
                    case Avatar.FEMALE:
                        tts.setParameter(SpeechConstant.VOICE_NAME, "xiaoqi");
                }
                break;
            case Avatar.ZH:
                switch (gender) {
                    case Avatar.MALE:
                        tts.setParameter(SpeechConstant.VOICE_NAME, "xiaofeng");
                        break;
                    case Avatar.FEMALE:
                        tts.setParameter(SpeechConstant.VOICE_NAME, "xiaoqi");
                }
                break;
        }

        tts.setParameter(SpeechConstant.SPEED, "60");
        tts.setParameter(SpeechConstant.PITCH, "60");
//        tts.setParameter(SpeechConstant.VOLUME, "100");//设置音量,范围 0~100
        tts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD); //设置云端
        tts.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");
        tts.setParameter(SpeechConstant.STREAM_TYPE, "" + AudioManager.STREAM_MUSIC);
        // 设置播放合成音频打断音乐播放，默认为true
        tts.setParameter(SpeechConstant.KEY_REQUEST_FOCUS, "false");
        //采样率 8000/16000 越低文件越小
        tts.setParameter(SpeechConstant.SAMPLE_RATE, "16000");
        tts.setParameter(SpeechConstant.TTS_BUFFER_TIME, "500");
    }


    @Override
    public void speak(String textToSpeak, boolean isHelloSpeak) {
        super.isHelloSpeak = isHelloSpeak;
        currentSpeak = textToSpeak;
        stopSpeaking();
        tts.startSpeaking(textToSpeak, ttsListener);
    }


    @Override
    public void speakTime() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf;
        String timeSpeak = "现在的时间是";
        sdf = new SimpleDateFormat("HH点mm分");

        timeSpeak = timeSpeak + sdf.format(calendar.getTime());
        speak(timeSpeak, false);
    }


    @Override
    public void speakDate() {
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        String dateSpeak = "今天是";
        String[] days_cn = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};

        dateSpeak = dateSpeak + days_cn[day];
        speak(dateSpeak, false);
    }


    @Override
    public void stopSpeaking() {
        if (tts.isSpeaking()) {
            tts.stopSpeaking();
            ttsListener.onCompleted(null);
        }
    }


    @Override
    public void destroy() {
        if (tts != null) {
            tts.stopSpeaking();
            tts.destroy();
        }
    }


    private SynthesizerListener ttsListener = new SynthesizerListener() {

        @Override
        public void onSpeakBegin() {
            listener.onSpeakStarted(currentSpeak, isHelloSpeak);
        }


        @Override
        public void onBufferProgress(int i, int i1, int i2, String s) {

        }


        @Override
        public void onSpeakPaused() {

        }


        @Override
        public void onSpeakResumed() {

        }


        @Override
        public void onSpeakProgress(int i, int i1, int i2) {

        }


        @Override
        public void onCompleted(SpeechError speechError) {
            listener.onSpeakEnded();
        }


        @Override
        public void onEvent(int i, int i1, int i2, Bundle bundle) {
        }
    };

}
