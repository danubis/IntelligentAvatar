package danubis.derrick.sample;

import com.magicpi.maggie.Maggie;
import com.magicpi.maggie.model.Dictionary;
import com.magicpi.maggie.model.Response;

import io.reactivex.Observable;


public class MaggieWrapper {

    private Maggie maggie;

    public MaggieWrapper(Maggie maggie) {
        this.maggie = maggie;
    }

    public Observable<Response> findAnswer(String text) {
        return Observable.create(e -> {
            e.onNext(maggie.findAnswer(text));
            e.onComplete();
        });
    }

    public Observable<Response> answerSuggestion(boolean agree) {
        return Observable.create(e -> {
            e.onNext(maggie.answerSuggestion(agree));
            e.onComplete();
        });
    }

    public Dictionary getDictionary(String id) {
        return maggie.getDictionary(id);
    }
}
