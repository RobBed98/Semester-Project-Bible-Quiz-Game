import java.util.List;

public class Questions {
    private String text;
    private List<String> choices;
    private int correctIndex;

    public Question(String text, List<String> choices, int correctIndex) {
        this.text = text;
        this.choices = choices;
        this.correctIndex = correctIndex;
    }

    public String getText() { return text; }
    public List<String> getChoices() { return choices; }
    public int getCorrectIndex() { return correctIndex; }
}