package interview.scoreboard;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
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
    @DisplayName("When completing non existing match then throw an exception")
    void completeNonExistingMatch() {
        var nonExistingId = 45345L;

        when(matchRepository.get(nonExistingId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> worldCupTournament.completeMatch(nonExistingId)).isInstanceOf(NoSuchElementException.class);
    }

    @Test
    @DisplayName("When completing already completed match then throw an exception")
    void completeAlreadyCompletedMatch() {
        var matchId = 1L;
        var liveMatch = new MatchEntity();
        liveMatch.setMatchState(MatchState.COMPLETED);

        when(matchRepository.get(matchId)).thenReturn(Optional.of(liveMatch));

        assertThatThrownBy(() -> worldCupTournament.completeMatch(matchId)).isInstanceOf(IllegalStateException.class);
    }

    @Test
    @DisplayName("When completing the match then update the state to COMPLETED and set endTime")
    void completeMatch() {
        var matchId = 1L;
        var liveMatch = new MatchEntity();
        liveMatch.setId(matchId);
        liveMatch.setMatchState(MatchState.LIVE);

        when(matchRepository.get(matchId)).thenReturn(Optional.of(liveMatch));
        when(matchRepository.save(any(MatchEntity.class))).thenAnswer(answer -> answer.getArgument(0));

        var match = worldCupTournament.completeMatch(matchId);

        assertThat(match.getEndTime()).isEqualToIgnoringSeconds(LocalDateTime.now());
        assertThat(match.getMatchState()).isEqualTo(MatchState.COMPLETED);
    }

    @Test
    @DisplayName("When updating score of non existing match then throw an exception")
    void updateScoreOfNonExistingMatch() {
        var nonExistingId = 45345L;

        when(matchRepository.get(nonExistingId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> worldCupTournament.updateScore(nonExistingId, 3, 2))
            .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    @DisplayName("When provided new score then update the entity")
    void updateScore() {
        var expectedHomeScore = 3;
        var expectedAwayScore = 1;

        var matchId = 1L;
        var liveMatch = new MatchEntity();
        liveMatch.setId(matchId);
        liveMatch.setScore(new Score(0, 0));
        liveMatch.setMatchState(MatchState.LIVE);

        when(matchRepository.get(matchId)).thenReturn(Optional.of(liveMatch));
        when(matchRepository.save(any(MatchEntity.class))).thenAnswer(answer -> answer.getArgument(0));

        var match = worldCupTournament.updateScore(1L, expectedHomeScore, expectedAwayScore);

        assertThat(match.getScore())
            .returns(expectedHomeScore, Score::getHome)
            .returns(expectedAwayScore, Score::getAway);
    }

    @Test
    @DisplayName("When no matches then return empty list")
    void testListNoMatches() {
        when(matchRepository.findAll()).thenReturn(Collections.emptyList());

        var matches = worldCupTournament.listLiveMatches();

        assertThat(matches).isEmpty();
    }

    @Test
    @DisplayName("When no LIVE matches then return empty list")
    void testListNoLiveMatches() {
        var m1 = new MatchEntity();
        var m2 = new MatchEntity();

        m1.setMatchState(MatchState.COMPLETED);
        m2.setMatchState(MatchState.COMPLETED);

        when(matchRepository.findAll()).thenReturn(List.of(m1, m2));

        var matches = worldCupTournament.listLiveMatches();

        assertThat(matches).isEmpty();
    }

    @Test
    @DisplayName("When listing matches then return LIVE matches ordered by number of goals and start date")
    void listLiveMatches() {
        var allMatches = List.of(
            stubMatch("Mexico", "Canada", 0, 5, MatchState.LIVE),
            stubMatch("Greece", "Portugal", 10, 5, MatchState.COMPLETED),
            stubMatch("Spain", "Brazil", 10, 2, MatchState.LIVE),
            stubMatch("Germany", "France", 2, 2, MatchState.LIVE),
            stubMatch("Nigeria", "Austria", 2, 12, MatchState.COMPLETED),
            stubMatch("Uruguay", "Italy", 6, 6, MatchState.LIVE),
            stubMatch("Argentina", "Australia", 3, 1, MatchState.LIVE));

        when(matchRepository.findAll()).thenReturn(allMatches);

        var matches = worldCupTournament.listLiveMatches();
        assertThat(matches)
            .extracting(Match::getMatchState)
            .containsOnly(MatchState.LIVE);

        assertThat(matches).extracting(Match::getTeamHome)
            .containsExactly("Uruguay", "Spain", "Mexico", "Argentina", "Germany");
    }

    private static MatchEntity stubMatch(String mexico, String canada, int home, int away, MatchState matchState) {
        var matchEntity = new MatchEntity();
        matchEntity.setId(System.currentTimeMillis());
        matchEntity.setTeamHome(mexico);
        matchEntity.setTeamAway(canada);
        matchEntity.setScore(new Score(home, away));
        matchEntity.setStartTime(LocalDateTime.now());
        matchEntity.setMatchState(matchState);
        return matchEntity;
    }
}