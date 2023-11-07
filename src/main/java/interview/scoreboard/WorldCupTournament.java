package interview.scoreboard;

import java.util.List;

public class WorldCupTournament extends AbstractTournament{

    protected WorldCupTournament(String name) {
        super(name);
    }

    @Override
    protected Match startMatch(String teamHome, String teamAway) {
        return null;
    }

    @Override
    protected Match completeMatch(long matchId) {
        return null;
    }

    @Override
    protected Match updateScore(long matchId, int homeScore, int awayScore) {
        return null;
    }

    @Override
    protected List<Match> listLiveMatches() {
        return null;
    }
}
