package interview.scoreboard;


/**
 * Score is implemented as separate model as it will be easier to add more logic in the future. E.g. add players who scored. keep score
 * history, etc
 */
public class Score {

    public Score() {
    }
    public Score(int home, int away) {
        this.home = home;
        this.away = away;
    }

    private int home = 0;
    private int away = 0;

    public int getHome() {
        return home;
    }

    public int getAway() {
        return away;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Score score = (Score) o;

        if (home != score.home) {
            return false;
        }
        return away == score.away;
    }

    @Override
    public int hashCode() {
        int result = home;
        result = 31 * result + away;
        return result;
    }
}
