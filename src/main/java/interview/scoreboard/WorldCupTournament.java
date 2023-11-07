package interview.scoreboard;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

public class WorldCupTournament extends AbstractTournament {

    public WorldCupTournament(String name, MatchRepository matchRepository) {
        super(name);
        this.matchRepository = matchRepository;
        matchComparator = MatchComparator.defaultComparator();
    }

    private final MatchRepository matchRepository;
    private final MatchComparator matchComparator;

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
    public Match completeMatch(long matchId) {
        var matchEntityOpt = matchRepository.get(matchId);

        return matchEntityOpt.map(match -> {
                if (match.getMatchState() == MatchState.COMPLETED) {
                    throw new IllegalStateException("Completing the match that's already completed is not allowed");
                }
                match.setEndTime(LocalDateTime.now());
                match.setMatchState(MatchState.COMPLETED);
                return matchRepository.save(match);
            })
            .map(WorldCupTournament::mapToModel)
            .orElseThrow(NoSuchElementException::new);
    }

    @Override
    public Match updateScore(long matchId, int homeScore, int awayScore) {
        var matchEntityOpt = matchRepository.get(matchId);

        return matchEntityOpt.map(match -> {
                var score = match.getScore();
                score.updateScore(homeScore, awayScore);
                return matchRepository.save(match);
            })
            .map(WorldCupTournament::mapToModel)
            .orElseThrow(NoSuchElementException::new);
    }

    @Override
    public List<Match> listLiveMatches() {
        return matchRepository.findAll()
            .stream()
            .filter(match -> match.getMatchState() == MatchState.LIVE)
            .sorted(matchComparator)
            .map(WorldCupTournament::mapToModel)
            .toList();
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
