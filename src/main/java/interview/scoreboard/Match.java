package interview.scoreboard;

import java.time.LocalDateTime;

public class Match {

    private final Long id;
    private final String teamHome;
    private final String teamAway;
    private Score score;
    private final LocalDateTime startTime;
    private LocalDateTime endTime;
    private MatchState matchState;


    private Match(Long id, String teamHome, String teamAway, LocalDateTime startTime) {
        this.id = id;
        this.teamHome = teamHome;
        this.teamAway = teamAway;
        this.startTime = startTime;
    }


    public Long getId() {
        return id;
    }

    public String getTeamHome() {
        return teamHome;
    }

    public String getTeamAway() {
        return teamAway;
    }

    public Score getScore() {
        return score;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public MatchState getMatchState() {
        return matchState;
    }
}
