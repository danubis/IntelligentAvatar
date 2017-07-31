package danubis.derrick.sample.data;


import java.util.HashMap;
import java.util.Map;

/**
 * Created by yiluo on 31/7/17.
 */

public class MockDataRepository implements DataRepository {

    private Map<String, Question> questions = new HashMap<>();

    public MockDataRepository() {
        questions.put("q0001", new Question("How much is the ticket of MagicPi museum?", "It's free."));
        questions.put("q0002", new Question("Do I need a VISA to come to Australia?", "Yes, I can apply one for you now."));
        questions.put("q0003", new Question("How old are you?", "I'm very young, that's all I can say."));
        questions.put("q0004", new Question("Where is Port Douglas?", "Somewhere in Australia."));
        questions.put("q0005", new Question("How do I get from China to the Whitsunday Islands?", "By airplane of course."));
        questions.put("q0006", new Question("Can I bring Food and Drinks?", "No, you can't."));
        questions.put("q0007", new Question("Can I fly direct from China to Cairns?", "Yes, you can if you have a ticket."));
        questions.put("q0008", new Question("How do I contact the Museum?", "Please call 000."));
        questions.put("q0009", new Question("Where are the Twelve Apostles?", "On earth, I guess."));
        questions.put("q0010", new Question("When does the Shop open?", "It opens on last Tuesday of each month."));
        questions.put("q0011", new Question("Who is most beautiful girl?", "Obviously, it's not you. "));
        questions.put("q0012", new Question("Where can I find parking?", "Don't worry about parking, you don't have a car."));
        questions.put("q0013", new Question("Where can I buy souvenirs?", "We don't sell drugs."));
        questions.put("q0014", new Question("Where is Queensland?", "Ask Victoria."));
    }

    @Override
    public String getHintByRef(String questionRef) {
        return questions.get(questionRef).getHint();
    }

    @Override
    public String getAnswerByRef(String questionRef) {
        return questions.get(questionRef).getAnswer();
    }
}
