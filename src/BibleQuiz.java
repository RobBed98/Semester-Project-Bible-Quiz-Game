import java.util.*;

/**
 * Author: Robert Beddow
 * Integrity assertion statements:
 * -I have not discussed the source code in my program with anyone other than my
 * instructor’s approved human sources.
 * -I have not used source code obtained from another student, or any other unauthorized
 * source, either modified or unmodified.
 * -If any source code or documentation used in my program was obtained from another
 * source, such as a text book or course notes, that has been clearly noted with a proper
 * citation in the comments of my program.
 * -I have not knowingly designed this program in such a way as to defeat or interfere with the
 * normal operation of any machine it is graded on or to produce apparently correct results
 * when in fact it does not.
 *  ---Seperating line to help my sanity while reading---
 * Bible Quiz Game Check list
 * - Main screen: "Begin"
 * - Difficulty: Easy, Medium, Hard
 * - 10 rounds, increasing point values
 * - Players: You, Moses, Noah, Adam
 * - Round counter (top-right), scoreboard (bottom-left)
 * - 4-choice questions, answer with 1–4.
 * - NPCs have difficulty-based random correctness (never 100%)
 * - Encouraging verse shown on wrong answers and if you lose
 * - Humility message if you win + congrats + final scores
 * - Play again feature returns to main screen
 */
public class BibleQuiz {

    public static void main(String[] args) {
        new Game().run();
    }

    // ========================= Core Game =========================
    static class Game {
        private final Scanner scanner = new Scanner(System.in);
        private final QuestionBank bank = new QuestionBank();
        private final Random rng = new Random();

        private final List<Player> players = Arrays.asList(
                new Player("You", false),
                new Player("Moses", true),
                new Player("Noah", true),
                new Player("Adam", true)
        );

        public void run() {
            while (true) {
                showMainMenu();
                if (!waitForBegin()) {
                    println("\nGoodbye!");
                    return;
                }
                Difficulty difficulty = chooseDifficulty();
                playSession(difficulty);
                if (!askPlayAgain()) {
                    println("\nThanks for playing! Take care.");
                    return;
                }
            }
        }

        private void showMainMenu() {
            clear();
            println(center("=========================================="));
            println(center("           Bible Quiz Game"));
            println(center("=========================================="));
            println(center("Press B to Begin or Q to Quit"));
        }

        private boolean waitForBegin() {
            while (true) {
                print("\nSelect (B/Q): ");
                String s = scanner.nextLine().trim().toLowerCase();
                if (s.equals("b")) return true;
                if (s.equals("q")) return false;
                println("Invalid choice. Please press B to begin or Q to quit.");
            }
        }

        private Difficulty chooseDifficulty() {
            clear();
            println(center("Choose difficulty:"));
            println(center("1) Easy   2) Medium   3) Hard"));
            while (true) {
                print("\nEnter 1, 2, or 3: ");
                String s = scanner.nextLine().trim();
                switch (s) {
                    case "1": return Difficulty.EASY;
                    case "2": return Difficulty.MEDIUM;
                    case "3": return Difficulty.HARD;
                    default: println("Invalid choice. Please enter 1, 2, or 3.");
                }
            }
        }

        private void playSession(Difficulty difficulty) {
            // Reset scores
            players.forEach(p -> p.score = 0);

            List<Question> questions = bank.getRandomQuestions(10, rng);
            int totalRounds = questions.size();

            for (int round = 1; round <= totalRounds; round++) {
                Question q = questions.get(round - 1);
                int points = pointsForRound(round);

                renderRoundHeader(round, totalRounds);
                renderScoreboard();

                // Show question + choices
                println("\n" + q.text);
                for (int i = 0; i < q.choices.size(); i++) {
                    println((i + 1) + ") " + q.choices.get(i));
                }

                // Get user answer
                int userChoice = readAnswerChoice(1, 4);
                boolean userCorrect = (userChoice - 1) == q.correctIndex;

                // Score user
                if (userCorrect) {
                    players.get(0).score += points;
                    println("\nCorrect! +" + points + " points.");
                } else {
                    println("\nIncorrect. The correct answer was: " + q.choices.get(q.correctIndex));
                    println(encouragingVerseBox());
                }

                // NPC answers based on difficulty
                for (int i = 1; i < players.size(); i++) {
                    Player npc = players.get(i);
                    boolean npcCorrect = npcAnswersCorrect(difficulty, q, rng);
                    if (npcCorrect) {
                        npc.score += points;
                        println(npc.name + " answered correctly. +" + points);
                    } else {
                        println(npc.name + " answered incorrectly.");
                    }
                }

                // Small pause to simulate flow
                promptContinue("\nPress Enter to continue to the next round...");
            }

            // Final results
            renderFinalResults();
            boolean userWon = didUserWin();
            if (userWon) {
                println(congratsBox());
                println(humilityMessageBox());
            } else {
                println(encouragingVerseBox()); // show at end if failed
                println("\nKeep going—you’re learning. You can try again!");
            }
        }

        private boolean askPlayAgain() {
            println("\nPlay again? (Y/N)");
            while (true) {
                print("Enter Y or N: ");
                String s = scanner.nextLine().trim().toLowerCase();
                if (s.equals("y")) return true;
                if (s.equals("n")) return false;
                println("Please enter Y or N.");
            }
        }

        // ========================= Helpers & Rendering =========================

        private void renderRoundHeader(int current, int total) {
            clear();
            String title = "Bible Quiz Game";
            String roundLabel = "Round " + current + "/" + total;
            // Simulate top-right round counter
            println(title + spaces(Math.max(1, 60 - title.length() - roundLabel.length())) + roundLabel);
            println(repeat("=", 60));
        }

        private void renderScoreboard() {
            println("\n[Scoreboard]");
            for (Player p : players) {
                println(p.name + ": " + p.score + " pts");
            }
            println(repeat("-", 60));
        }

        private void renderFinalResults() {
            clear();
            println(center("============ Final Scores ============"));
            List<Player> sorted = new ArrayList<>(players);
            sorted.sort((a, b) -> Integer.compare(b.score, a.score));
            for (Player p : sorted) {
                println(center(p.name + ": " + p.score + " pts"));
            }
            println(center("======================================"));
        }

        private boolean didUserWin() {
            int yourScore = players.get(0).score;
            for (int i = 1; i < players.size(); i++) {
                if (players.get(i).score > yourScore) {
                    return false;
                }
            }
            return true;
        }

        private int pointsForRound(int round) {
            // Increasing points by round number (e.g., 10, 20, ..., 100)
            return round * 10;
        }

        private int readAnswerChoice(int min, int max) {
            while (true) {
                print("\nChoose your answer (" + min + "-" + max + "): ");
                String s = scanner.nextLine().trim();
                try {
                    int val = Integer.parseInt(s);
                    if (val >= min && val <= max) return val;
                } catch (NumberFormatException ignored) {}
                println("Invalid input. Please enter a number from " + min + " to " + max + ".");
            }
        }

        private boolean npcAnswersCorrect(Difficulty difficulty, Question q, Random rng) {
            // Difficulty-based base probability (never 100%)
            // Easy: 35% base, Medium: 55% base, Hard: 75% base
            double base;
            switch (difficulty) {
                case EASY: base = 0.35; break;
                case MEDIUM: base = 0.55; break;
                case HARD: base = 0.75; break;
                default: base = 0.50;
            }

            // Slight variation per question to avoid determinism
            double jitter = (rng.nextDouble() - 0.5) * 0.10; // ±5%
            double prob = clamp(base + jitter, 0.10, 0.95);  // never below 10% or above 95%

            return rng.nextDouble() < prob;
        }

        // Encouraging verse for wrong answers and failing the game
        private String encouragingVerseBox() {
            String verse = "“I can do all things through Christ who strengthens me.” — Philippians 4:13";
            return box("Encouragement", verse);
        }

        // Humility message for winning
        private String humilityMessageBox() {
            String msg = "“Pride goes before destruction, and a haughty spirit before a fall.” — Proverbs 16:18";
            return box("Humility", msg);
        }

        private String congratsBox() {
            String msg = "Congratulations! You finished the game. Great job staying engaged and learning.";
            return box("Result", msg);
        }

        private String box(String title, String content) {
            String line = repeat("-", 60);
            return "\n" + line + "\n" + title.toUpperCase() + "\n" + content + "\n" + line;
        }

        private void promptContinue(String message) {
            print(message);
            scanner.nextLine();
        }

        // ========================= UI Utils =========================

        private void clear() {
            // Simple console "clear": print several newlines.
            // (Cross-platform full clear requires native calls; this keeps it portable.)
            for (int i = 0; i < 30; i++) System.out.println();
        }

        private void println(String s) { System.out.println(s); }
        private void print(String s) { System.out.print(s); }

        private String repeat(String s, int n) {
            StringBuilder sb = new StringBuilder(s.length() * n);
            for (int i = 0; i < n; i++) sb.append(s);
            return sb.toString();
        }

        private String spaces(int n) {
            return repeat(" ", Math.max(0, n));
        }

        private String center(String s) {
            int width = 60;
            int pad = Math.max(0, (width - s.length()) / 2);
            return spaces(pad) + s;
        }

        private double clamp(double v, double min, double max) {
            return Math.max(min, Math.min(max, v));
        }
    }

    // ========================= Domain =========================

    enum Difficulty { EASY, MEDIUM, HARD }

    static class Player {
        final String name;
        final boolean isNPC;
        int score = 0;

        Player(String name, boolean isNPC) {
            this.name = name;
            this.isNPC = isNPC;
        }
    }

    static class Question {
        final String text;
        final List<String> choices;
        final int correctIndex;

        Question(String text, List<String> choices, int correctIndex) {
            this.text = text;
            this.choices = choices;
            this.correctIndex = correctIndex;
        }
    }

    static class QuestionBank {
        private final List<Question> all = new ArrayList<>();

        QuestionBank() {
            // A curated set of Bible-based sample questions (multiple-choice)
            all.add(q("Who was swallowed by a great fish?",
                    list("Moses", "Jonah", "David", "Paul"), 1));
            all.add(q("Who led the Israelites out of Egypt?",
                    list("Moses", "Joshua", "Aaron", "Samuel"), 0));
            all.add(q("Which city’s walls fell after Israelites marched around it?",
                    list("Jerusalem", "Nineveh", "Jericho", "Bethlehem"), 2));
            all.add(q("Who was the first man created by God?",
                    list("Adam", "Noah", "Abraham", "Jacob"), 0));
            all.add(q("Who built the ark?",
                    list("Noah", "Moses", "Solomon", "Peter"), 0));
            all.add(q("Which prophet confronted King Ahab and the prophets of Baal?",
                    list("Elijah", "Elisha", "Isaiah", "Jeremiah"), 0));
            all.add(q("In the Parable of the Prodigal Son, who welcomed the son back?",
                    list("His brother", "His father", "The townspeople", "The priest"), 1));
            all.add(q("Who denied Jesus three times?",
                    list("Judas", "Peter", "Thomas", "John"), 1));
            all.add(q("Which book begins with “In the beginning God created the heavens and the earth”?",
                    list("Psalms", "Genesis", "John", "Exodus"), 1));
            all.add(q("Who interpreted dreams for Pharaoh in Egypt?",
                    list("Daniel", "Joseph", "Moses", "Nehemiah"), 1));
            all.add(q("What is the first commandment?",
                    list("Do not murder", "Honor your father and mother", "You shall have no other gods before Me", "Remember the Sabbath day"), 2));
            all.add(q("Who was thrown into the lions’ den?",
                    list("Daniel", "David", "Samson", "Saul"), 0));
            all.add(q("Which apostle was known as the 'doubter'?",
                    list("Peter", "Thomas", "James", "Andrew"), 1));
            all.add(q("Who was the mother of Jesus?",
                    list("Mary Magdalene", "Elizabeth", "Mary", "Martha"), 2));
            all.add(q("Which king wrote many of the Psalms?",
                    list("Saul", "Solomon", "David", "Hezekiah"), 2));
            all.add(q("What did God create on the first day?",
                    list("Animals", "Sun and moon", "Light", "Plants"), 2));
            all.add(q("What fruit did Eve eat in the Garden (commonly depicted)?",
                    list("Fig", "Grape", "Apple", "Pomegranate"), 2));
            all.add(q("Who was swallowed by a fish and then preached to Nineveh?",
                    list("Jonah", "Nahum", "Micah", "Hosea"), 0));
            all.add(q("Who wrestled with an angel and was renamed Israel?",
                    list("Esau", "Jacob", "Joseph", "Isaac"), 1));
            all.add(q("Which river did John the Baptist baptize in?",
                    list("Euphrates", "Jordan", "Nile", "Tigris"), 1));
        }

        Question q(String text, List<String> choices, int correctIndex) {
            return new Question(text, choices, correctIndex);
        }

        List<String> list(String... s) { return Arrays.asList(s); }

        List<Question> getRandomQuestions(int count, Random rng) {
            List<Question> copy = new ArrayList<>(all);
            Collections.shuffle(copy, rng);
            if (count >= copy.size()) return copy.subList(0, copy.size());
            return copy.subList(0, count);
        }
    }
}