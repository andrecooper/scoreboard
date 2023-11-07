package interview.scoreboard;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.mockito.Mockito;

class WorldCupTournamentTest {

    private final static String TOURNAMENT_NAME = "Fifa World Cup";
    private WorldCupTournament worldCupTournament;
    private final MatchRepository matchRepository = Mockito.mock(MatchRepository.class);

    @BeforeEach
    void setUp() {
        worldCupTournament = new WorldCupTournament(TOURNAMENT_NAME, matchRepository);
    }


    @Test
    @DisplayName("Tournament has to be named")
    void testNamedTournament() {
        assertThat(worldCupTournament.getName()).isEqualTo(TOURNAMENT_NAME);
    }

    @NullAndEmptySource
    @ParameterizedTest
    @DisplayName("When team name is empty then throw an exception")
    void testInvalidArgs(String teamName) {
        assertThatThrownBy(() -> worldCupTournament.startMatch(teamName, "France")).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> worldCupTournament.startMatch("England", teamName)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("When starting the match then init it with default values: startTime, state and score")
    void startMatch() {
        var homeTeam = "Canada";
        var awayTeam = "France";

        when(matchRepository.save(any(MatchEntity.class)))
            .thenAnswer(answer -> {
                var argument = (MatchEntity) answer.getArgument(0);
                argument.setId(System.currentTimeMillis());
                return argument;
            });

        var match = worldCupTournament.startMatch(homeTeam, awayTeam);

        assertThat(match.getId()).isPositive();
        assertThat(match.getStartTime()).isEqualToIgnoringSeconds(LocalDateTime.now());
        assertThat(match)
            .returns(new Score(0, 0), Match::getScore)
            .returns(homeTeam, Match::getTeamHome)
            .returns(awayTeam, Match::getTeamAway)
            .returns(MatchState.LIVE, Match::getMatchState)
            .returns(null, Match::getEndTime);
    }

    @Test
    @DisplayName("When completing the match then update the state to COMPLETED and set endTime")
    void completeMatch() {
        var matchId = 1L;

        var match = worldCupTournament.completeMatch(matchId);

        assertThat(match.getEndTime()).isEqualToIgnoringSeconds(LocalDateTime.now());
        assertThat(match.getMatchState()).isEqualTo(MatchState.COMPLETED);
    }

    @Test
    @DisplayName("When provided new score then update the entity")
    void updateScore() {
        var expectedHomeScore = 3;
        var expectedAwayScore = 1;

        var match = worldCupTournament.updateScore(1L, expectedHomeScore, expectedAwayScore);

        assertThat(match.getScore())
            .returns(expectedHomeScore, Score::getHome)
            .returns(expectedAwayScore, Score::getAway);

    }

    @Test
    @DisplayName("When listing matches then return LIVE matches ordered by number of goals and start date")
    void listLiveMatches() {
        var matches = worldCupTournament.listLiveMatches();
        assertThat(matches).isNotEmpty()
            .extracting(Match::getMatchState)
            .containsOnly(MatchState.LIVE);
    }
}