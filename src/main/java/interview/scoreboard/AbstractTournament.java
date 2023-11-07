package interview.scoreboard;

import java.util.List;

public abstract class AbstractTournament {

    private final String name;

    protected AbstractTournament(String name) {
        this.name = name;
    }

    /**
     * Starts new match and sets initial score 0:0
     *
     * @return match in initial state
     */
    protected abstract Match startMatch(String teamHome, String teamAway);

    protected abstract Match completeMatch(long matchId);

    /**
     * Receives absolute score of the match
     *
     * @return match with updated score
     */
    protected abstract Match updateScore(long matchId, int homeScore, int awayScore);

    /**
     * Provides list of active matches
     */
    protected abstract List<Match> listLiveMatches();

    public String getName() {
        return name;
    }

}
