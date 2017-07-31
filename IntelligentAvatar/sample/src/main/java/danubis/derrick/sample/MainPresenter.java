package danubis.derrick.sample;

import android.util.Log;

import com.google.gson.Gson;
import com.magicpi.maggie.model.Dictionary;
import com.magicpi.maggie.model.Response;

import java.io.IOException;

import danubis.derrick.sample.data.DataRepository;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

import static com.magicpi.maggie.model.Response.ANSWER;
import static com.magicpi.maggie.model.Response.ERROR;
import static com.magicpi.maggie.model.Response.SUGGESTION;


public class MainPresenter implements MainContract.Presenter {

    private MainContract.View view;
    private MaggieWrapper maggie;
    private DataRepository dataRepository;

    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private boolean isSuggesting;

    public MainPresenter(MainContract.View view, MaggieWrapper maggie, DataRepository dataRepository) {
        this.view = view;
        this.maggie = maggie;
        this.dataRepository = dataRepository;
    }

    @Override
    public void findAnswer(String question) {

        if (isSuggesting) {

            if (question.toLowerCase().contains("yes")) {
                respondToSuggestion(true);
            } else if (question.toLowerCase().contains("no")) {
                respondToSuggestion(false);
            } else {
                compositeDisposable.add(maggie.findAnswer(question)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(this::handleResponseFromMaggie));
            }

        } else {
            compositeDisposable.add(maggie.findAnswer(question)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::handleResponseFromMaggie));
        }
    }

    @Override
    public void saveDictionary(String path) {
        Dictionary dictionary = maggie.getDictionary("museum_general");
        try {
            if (dictionary.isUpdated()) {
                Log.e("Presenter", "dictionary was updated...");
                dictionary.save(path, new Gson());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void respondToSuggestion(boolean agree) {
        compositeDisposable.add(maggie.answerSuggestion(agree)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::handleResponseFromMaggie));
    }

    private void handleResponseFromMaggie(Response response) {
        switch (response.state) {
            case ANSWER:
                view.speak(dataRepository.getAnswerByRef(response.message));
                isSuggesting = false;
                break;
            case SUGGESTION:
                view.suggest(dataRepository.getHintByRef(response.message));
                isSuggesting = true;
                break;
            case ERROR:
                view.speak("Sorry, Let me think about this question and answer you next time.");
                isSuggesting = false;
                break;
        }
    }

    @Override
    public void onDestroy() {
        compositeDisposable.clear();
    }
}