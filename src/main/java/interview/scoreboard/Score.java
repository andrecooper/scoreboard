package interview.scoreboard;


/**
 * Score is implemented as separate model as it will be easier to add more logic in the future. E.g. add players who scored. keep score
 * history, etc
 */
public class Score {

    private int home = 0;
    private int away = 0;

    public int getHome() {
        return home;
    }

    public int getAway() {
        return away;
    }

    public Score(int home, int away) {
        this.home = home;
        this.away = away;
    }
}
