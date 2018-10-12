package danubis.derrick.library.ear;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.SpeechUtility;

import danubis.derrick.library.Avatar;

/**
 * Created by yiluo on 27/2/17.
 */

public class XfEar extends Ear {

    private String language;
    private boolean enableTranslation;


    public XfEar(Context context, String language, String appId, boolean enableTranslation) {
        super(context, appId);
        SpeechUtility.createUtility(context, SpeechConstant.APPID + "=" + appId + "," + SpeechConstant.FORCE_LOGIN + "=true");
        this.language = language;
        this.enableTranslation = enableTranslation;
        setAsr();
    }


    @Override
    void setAsr() {
        asr = SpeechRecognizer.createRecognizer(context, null);
        asr.setParameter(SpeechConstant.DOMAIN, "iat");
        asr.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
        asr.setParameter(SpeechConstant.RESULT_TYPE, "plain");

        switch (language) {
            case Avatar.EN:
                asr.setParameter(SpeechConstant.ASR_PTT, "1.1");
                asr.setParameter(SpeechConstant.LANGUAGE, "en_us");
                break;
            case Avatar.ZH:
                asr.setParameter(SpeechConstant.ASR_PTT, "1");
                asr.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
                asr.setParameter(SpeechConstant.ACCENT, "mandarin");
                break;
        }

        asr.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");
        asr.setParameter(SpeechConstant.ASR_AUDIO_PATH,
                Environment.getExternalStorageDirectory() + "/msc/asr.wav");

//        if (enableTranslation) {
//            asr.setParameter( SpeechConstant.ASR_SCH, "1" );
//            asr.setParameter( SpeechConstant.ADD_CAP, "translate" );
//            asr.setParameter( SpeechConstant.TRS_SRC, "its" );
//            asr.setParameter( SpeechConstant.ORI_LANG, "cn" );
//            asr.setParameter( SpeechConstant.TRANS_LANG, "en" );
//        }
    }


    @Override
    public void listen() {
        if (asr.isListening()) {
            asr.stopListening();
        } else {
            tempResult = "";
            asr.startListening(asrListener);
        }
    }


    @Override
    public void stopListening() {
        if (asr.isListening()) {
            asr.stopListening();
        }
    }


    @Override
    public void destroy() {
        if (asr != null) {
            asr.cancel();
            asr.destroy();
        }
    }


    private RecognizerListener asrListener = new RecognizerListener() {

        @Override
        public void onVolumeChanged(int i, byte[] bytes) {
        }

        @Override
        public void onBeginOfSpeech() {
        }

        @Override
        public void onEndOfSpeech() {
        }

        @Override
        public void onResult(RecognizerResult recognizerResult, boolean isLast) {

            if (null != recognizerResult) {
                tempResult = tempResult + recognizerResult.getResultString();
                if (isLast) {
                    if (language.equals(Avatar.ZH)) {
                        tempResult = tempResult.replace("，", "");
                        tempResult = tempResult.replace("？", "");
                        tempResult = tempResult.replace("。", "");
                    }
                    listener.onListenResult(tempResult);
                }
            }
        }

        @Override
        public void onError(SpeechError speechError) {
            Log.e("XfEar", speechError.toString());
        }

        @Override
        public void onEvent(int i, int i1, int i2, Bundle bundle) {

        }
    };

}
