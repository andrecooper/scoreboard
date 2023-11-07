package interview.scoreboard;

import java.time.LocalDateTime;
import java.util.List;

public class WorldCupTournament extends AbstractTournament{

    private final MatchRepository matchRepository;

    protected WorldCupTournament(String name, MatchRepository matchRepository) {
        super(name);
        this.matchRepository = matchRepository;
    }

    @Override
    public Match startMatch(String teamHome, String teamAway) {
        if (teamHome == null || teamHome.isEmpty()) {
            throw new IllegalArgumentException("Home team should not be blank");
        }

        if (teamAway == null || teamAway.isEmpty()) {
            throw new IllegalArgumentException("Away team should not be blank");
        }

        var matchEntity = new MatchEntity();
        matchEntity.setMatchState(MatchState.LIVE);
        matchEntity.setStartTime(LocalDateTime.now());
        matchEntity.setTeamHome(teamHome);
        matchEntity.setTeamAway(teamAway);

        var saved = matchRepository.save(matchEntity);
        return mapToModel(saved);
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


    private static Match mapToModel(MatchEntity saved) {
        return new Match.MatchBuilder()
            .id(saved.getId())
            .startTime(saved.getStartTime())
            .endTime(saved.getEndTime())
            .matchState(saved.getMatchState())
            .score(saved.getScore())
            .teamHome(saved.getTeamHome())
            .teamAway(saved.getTeamAway())
            .build();
    }
}
