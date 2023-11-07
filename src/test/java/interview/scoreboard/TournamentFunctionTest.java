package interview.scoreboard;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TournamentFunctionTest {


    @Test
    @DisplayName("Run world cup with matches from requirements")
    public void runWorldCup() {
        var worldCupTournament = new WorldCupTournament("Fifa world cup");

        var m1 = worldCupTournament.startMatch("Mexico", "Canada");
        var m2 = worldCupTournament.startMatch("Spain", "Brasil");
        var m3 = worldCupTournament.startMatch("Germany", "France");
        var m4 = worldCupTournament.startMatch("Uruguay", "Italy");
        var m5 = worldCupTournament.startMatch("Argentina", "Australia");

        worldCupTournament.updateScore(m1.getId(), 0, 5);
        worldCupTournament.updateScore(m2.getId(), 10, 2);
        worldCupTournament.updateScore(m3.getId(), 2, 2);
        worldCupTournament.updateScore(m4.getId(), 6, 6);
        worldCupTournament.updateScore(m5.getId(), 3, 1);

        assert (worldCupTournament.listLiveMatches().size() == 5);

        worldCupTournament.listLiveMatches().forEach(
            m -> System.out.printf("%s  %s : %s %s%n", m.getTeamHome(), m.getScore().getHome(), m.getScore().getAway(), m.getTeamAway())
        );

        worldCupTournament.completeMatch(m1.getId());
        worldCupTournament.completeMatch(m3.getId());
        worldCupTournament.completeMatch(m5.getId());

        assert (worldCupTournament.listLiveMatches().size() == 2);

        worldCupTournament.completeMatch(m2.getId());
        worldCupTournament.completeMatch(m4.getId());

        assert (worldCupTournament.listLiveMatches().isEmpty());

    }
}