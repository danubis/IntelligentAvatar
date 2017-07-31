package danubis.derrick.sample;

/**
 * Created by yiluo on 24/7/17.
 */

public interface MainContract {

    interface View {
        void speak(String speech);

        void suggest(String suggestion);
    }

    interface Presenter {
        void findAnswer(String question);

        void saveDictionary(String path);

        void onDestroy();
    }
}