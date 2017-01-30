package danubis.derrick.sample;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import java.util.ArrayList;

import danubis.derrick.library.Brain.Brain;
import danubis.derrick.library.Brain.Result;

class MyBrain extends Brain {

    private final String LOGTAG = "MyBrain";

    private ArrayList<Question> itemQuestions;
    private ArrayList<Question> generalQuestions;

    private MainActivity activity;

    MyBrain(Activity activity) {
        this.activity = (MainActivity) activity;
    }


    public void setGeneralQuestions(ArrayList<Question> knowledge) {
        this.generalQuestions = knowledge;
    }


    public void setItemQuestions(ArrayList<Question> knowledge) {
        this.itemQuestions = knowledge;
    }


    @Override
    public ArrayList<? extends Result> think(String question) {

        if (!question.equals("")) {
            return findInCommand(question);
        }
        return null;
    }


    public void playAnswer(Answer answer) {

        String answerContent = answer.getAns();

        switch (answer.getAnsType()) {

            case Answer.TYPE_SPEAK:
                avatar.speak(answerContent);
                break;

            case Answer.TYPE_VIDEO:

                playVideo(answerContent, false);
                break;

            case Answer.TYPE_VIDEO_VR:

                playVideo(answerContent, true);
                break;

            case Answer.TYPE_COMMAND:
                switch (answerContent) {
                    case "gc_1":
//                        exitFlag = true;
//                        avatar.speak(activity.getString(R.string.bye_speak));
                        break;
                    case "gc_2":
                        if (avatar.getLastSpokeText() != null) {
                            avatar.speak(avatar.getLastSpokeText());
                        }
                        break;
                    case "gc_3":
                        avatar.speakTime();
                        break;
                    case "gc_4":
                        avatar.speakDate();
                        break;
                }
                break;
        }
    }


    private void playVideo(String videoName, boolean isVR) {

//        if (videoName.contains("|||")) {
//
//            int index = videoName.lastIndexOf("|") + 1;
//            String youtubeVideo = videoName.substring(index);
//            Log.e(LOGTAG, "youtube video id: " + youtubeVideo);
//
//            //start video playback from youtube
//            Intent intent = YouTubeStandalonePlayer.createVideoIntent(activity,
//                    "AIzaSyCtEpfAtVzrhhMWE8n1Ay4AG4r1AXFSzXU", youtubeVideo, 0, true, false);
//            activity.startActivity(intent);
//
//        } else if (videoName.contains("_vr")) {
//
//            Log.e(LOGTAG, "playing vr video: " + videoName);
//
//            Intent intent = new Intent(activity, VrVideoActivity.class);
//            intent.putExtra("VR_VIDEO", videoName);
//            activity.startActivityForResult(intent, activity.VIDEO_REQUEST_CODE);
//
//        } else {
//            Intent intent = new Intent(activity, FullScreenVideoActivity.class);
//            intent.putExtra("VIDEO", videoName);
//            activity.startActivityForResult(intent, activity.VIDEO_REQUEST_CODE);
//        }


//        if (isVR) {
//            Intent intent = new Intent(activity, VrVideoActivity.class);
//            intent.putExtra("VR_VIDEO", videoName);
//            activity.startActivityForResult(intent, activity.VIDEO_REQUEST_CODE);
//
//        } else {
//            Intent intent = new Intent(activity, FullScreenVideoActivity.class);
//            intent.putExtra("VIDEO", videoName);
//            activity.startActivityForResult(intent, activity.VIDEO_REQUEST_CODE);
//        }
    }


    private ArrayList<Answer> findInCommand(String question) {
        return findInItemQus(question);
    }


    private ArrayList<Answer> findInItemQus(String question) {

        if (itemQuestions != null && !itemQuestions.isEmpty()) {
            for (Question q : itemQuestions) {
                for (String keyword : q.getKeyword()) {

                    if (keyword != null && !keyword.equals("")
                            && question.contains(keyword.toLowerCase())) {
                        return (ArrayList<Answer>) q.getAnswer().clone();
                    }
                }
            }
        }
        return findInGeneralQus(question);
    }


    private ArrayList<Answer> findInGeneralQus(String question) {

        if (generalQuestions != null && !generalQuestions.isEmpty()) {
            for (Question q : generalQuestions) {
                for (String keyword : q.getKeyword()) {

                    if (keyword != null && !keyword.equals("")
                            && question.contains(keyword.toLowerCase())) {
                        return (ArrayList<Answer>) q.getAnswer().clone();
                    }
                }
            }
        }
        return null;
    }
}
