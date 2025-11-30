import java.util.*;

public class QuestionBank {
    private List<Question> questions = new ArrayList<>();

    public QuestionBank() {
        questions.add(new Question("Who was swallowed by a great fish?",
                Arrays.asList("Moses", "Jonah", "David", "Paul"), 1));
        questions.add(new Question("Who led the Israelites out of Egypt?",
                Arrays.asList("Moses", "Joshua", "Aaron", "Samuel"), 0));
        // Add more later
    }

    public List<Question> getRandomQuestions(int count) {
        Collections.shuffle(questions);
        return questions.subList(0, Math.min(count, questions.size()));
    }
}