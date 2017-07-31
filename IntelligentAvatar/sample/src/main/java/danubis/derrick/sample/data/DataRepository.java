package danubis.derrick.sample.data;

/**
 * Created by yiluo on 31/7/17.
 */

public interface DataRepository {

    String getHintByRef(String questionRef);

    String getAnswerByRef(String questionRef);
}
