package danubis.derrick.library.Brain;

/**
 * Created by yiluo on 24/1/17.
 */

public class Result {

    public static final String TYPE_SPEAK = "speak";
    public static final String TYPE_VIDEO = "video";
    public static final String TYPE_VIDEO_VR = "video_vr";
    public static final String TYPE_COMMAND = "GeneralCommands";

    private String ansType;
    private String ans;

    public String getAnsType() {
        return ansType;
    }

    public void setAnsType(String ansType) {
        this.ansType = ansType;
    }

    public String getAns() {
        return ans;
    }

    public void setAns(String ans) {
        this.ans = ans;
    }
}
