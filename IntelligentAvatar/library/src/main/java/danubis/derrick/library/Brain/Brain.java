package danubis.derrick.library.Brain;

import java.util.ArrayList;

/**
 * Created by yiluo on 24/1/17.
 */

public abstract class Brain {

    private String language;
    private String accent;

    public Brain(String language, String accent) {
        this.language = language;
        this.accent = accent;
    }

    public String getLanguage() {
        return language;
    }

    public String getAccent() {
        return accent;
    }

    public abstract ArrayList<? extends Result> think(String question);
}
