package danubis.derrick.library.Brain;

import java.util.ArrayList;

import danubis.derrick.library.Avatar;


public abstract class Brain {

    protected Avatar avatar;

    public void attachToAvatar(Avatar avatar) {
        this.avatar = avatar;
    }

    public abstract ArrayList<? extends Result> think(String question);
}
