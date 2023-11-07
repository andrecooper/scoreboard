package interview.scoreboard;

import java.util.Comparator;

public interface MatchComparator extends Comparator<MatchEntity> {

    static MatchComparator defaultComparator() {
        return (m1, m2) -> {
            var compareResByGoals = Integer.compare(m2.getScore().getNumberOfGoals(), m1.getScore().getNumberOfGoals());
            return compareResByGoals == 0 ? m2.getStartTime().compareTo(m1.getStartTime()) : compareResByGoals;
        };

    }
}
