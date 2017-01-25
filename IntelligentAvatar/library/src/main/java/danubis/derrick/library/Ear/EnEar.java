package danubis.derrick.library.Ear;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;

import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.SpeechUtility;

/**
 * Created by yiluo on 25/1/17.
 */

public class EnEar extends Ear {

    private SpeechRecognizer asr;
    private String tempResult = "";


    public EnEar(Context context) {
        super(context);
        SpeechUtility.createUtility(context, SpeechConstant.APPID + "=56ef40cc," + SpeechConstant.FORCE_LOGIN + "=true");
        setAsr();
    }


    @Override
    protected void setAsr() {
        asr = SpeechRecognizer.createRecognizer(context, null);

        //语义区域 日常用语
        asr.setParameter(SpeechConstant.DOMAIN, "iat");
        //语音听写方式 云
        asr.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
        //语音听写结果格式
        asr.setParameter(SpeechConstant.RESULT_TYPE, "plain");
        //设置听写文本结果是否带有标点
        asr.setParameter(SpeechConstant.ASR_PTT, "1.1");
        asr.setParameter(SpeechConstant.LANGUAGE, "en_us");
        // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
        // 注：AUDIO_FORMAT参数语记需要更新版本才能生效
        asr.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");
        asr.setParameter(SpeechConstant.ASR_AUDIO_PATH,
                Environment.getExternalStorageDirectory() + "/msc/asr.wav");
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
                    listener.onListenResult(tempResult);
                }
            }
        }

        @Override
        public void onError(SpeechError speechError) {
        }

        @Override
        public void onEvent(int i, int i1, int i2, Bundle bundle) {

        }
    };

}
