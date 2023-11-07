package interview.scoreboard;


/**
 * Score is implemented as separate model as it will be easier to add more logic in the future. E.g. add players who scored. keep score
 * history, etc
 */
public class Score {

    public Score() {
    }
    public Score(int home, int away) {
        if (home < 0) {
            throw new IllegalStateException("Home score cannot be less than 0");
        }

        if (away < 0) {
            throw new IllegalStateException("Away score cannot be less than 0");
        }
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

    public void updateScore(int newHomeScore, int newAwayScore) {
        if (newHomeScore < this.home) {
            throw new IllegalStateException("New home score cannot be less than current");
        }

        if (newAwayScore < this.away) {
            throw new IllegalStateException("New away score cannot be less than current");
        }
        this.home = newHomeScore;
        this.away = newAwayScore;
    }

    public int getNumberOfGoals() {
        return this.home + this.away;
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
