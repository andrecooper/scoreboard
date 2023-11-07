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

    public static class MatchBuilder {

        private long id;
        private String teamHome;
        private String teamAway;
        private Score score;
        private LocalDateTime startTime;
        private LocalDateTime endTime;
        private MatchState matchState;

        public MatchBuilder id(long id) {
            this.id = id;
            return this;
        }

        public MatchBuilder teamHome(String teamHome) {
            this.teamHome = teamHome;
            return this;
        }

        public MatchBuilder teamAway(String teamAway) {
            this.teamAway = teamAway;
            return this;
        }

        public MatchBuilder startTime(LocalDateTime startTime) {
            this.startTime = startTime;
            return this;
        }

        public MatchBuilder score(Score score) {
            this.score = score;
            return this;
        }

        public MatchBuilder endTime(LocalDateTime endTime) {
            this.endTime = endTime;
            return this;
        }

        public MatchBuilder matchState(MatchState matchState) {
            this.matchState = matchState;
            return this;
        }

        public Match build() {
            Match match = new Match(id, teamHome, teamAway, startTime);
            match.score = this.score;
            match.endTime = this.endTime;
            match.matchState = this.matchState;
            return match;
        }
    }
}
