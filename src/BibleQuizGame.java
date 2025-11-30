import java.util.*;

public class BibleQuizGame {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        QuestionBank bank = new QuestionBank();
        List<Question> quiz = bank.getRandomQuestions(2); // test with 2 rounds

        Player you = new Player("You", false);

        int round = 1;
        for (Question q : quiz) {
            System.out.println("Round " + round++);
            System.out.println(q.getText());
            List<String> choices = q.getChoices();
            for (int i = 0; i < choices.size(); i++) {
                System.out.println((i+1) + ") " + choices.get(i));
            }

            System.out.print("Your answer: ");
            int choice = scanner.nextInt();
            if (choice - 1 == q.getCorrectIndex()) {
                you.addScore(10);
                System.out.println("Correct! +10 points");
            } else {
                System.out.println("Incorrect.");
            }
        }

        System.out.println("Final score: " + you.getScore());
    }
    // inside BibleQuizGame main
    List<Player> players = Arrays.asList(
            new Player("You", false),
            new Player("Moses", true),
            new Player("Noah", true),
            new Player("Adam", true)
    );

    Random rng = new Random();

for (Question q : quiz) {
        System.out.println(q.getText());
        for (int i = 0; i < q.getChoices().size(); i++) {
            System.out.println((i+1) + ") " + q.getChoices().get(i));
        }

        System.out.print("Your answer: ");
        int choice = scanner.nextInt();
        if (choice - 1 == q.getCorrectIndex()) {
            players.get(0).addScore(10);
            System.out.println("Correct!");
        } else {
            System.out.println("Wrong!");
        }

        // NPC answers
        for (int i = 1; i < players.size(); i++) {
            boolean npcCorrect = rng.nextDouble() < 0.5; // 50% chance for now
            if (npcCorrect) {
                players.get(i).addScore(10);
                System.out.println(players.get(i).getName() + " answered correctly.");
            } else {
                System.out.println(players.get(i).getName() + " answered incorrectly.");
            }
        }
    }
}