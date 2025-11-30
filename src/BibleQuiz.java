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
 * - 4-choice questions, answer with 1–4
 * - NPCs have difficulty-based random correctness (never 100%)
 * - Encouraging verse shown on wrong answers and if you lose
 * - Humility message if you win + congrats + final scores
 * - Play again returns to main screen
 */
public class BibleQuiz {

    public static void main(String[] args) {
        new Game().run();
    }

    // ========================= Core Game =========================
    public class Player {
        private String name;
        private boolean isNPC;
        private int score;

        public Player(String name, boolean isNPC) {
            this.name = name;
            this.isNPC = isNPC;
            this.score = 0;
        }

        public String getName() { return name; }
        public int getScore() { return score; }
        public void addScore(int points) { score += points; }
        public boolean isNPC() { return isNPC; }
    }
}