package interview.scoreboard;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ScoreTest {
    @Test
    @DisplayName("When provided home score is less then current then throw an exception")
    void updateHomeScoreInvalid() {
        var score = new Score(2, 3);
        assertThatThrownBy(() -> score.updateScore(0, 3));
        assertThatThrownBy(() -> score.updateScore(1, 4));
    }

    @Test
    @DisplayName("When provided away score is less then current then throw an exception")
    void updateAwayScoreInvalid() {
        var score = new Score(2, 3);
        assertThatThrownBy(() -> score.updateScore(2, 2));
        assertThatThrownBy(() -> score.updateScore(3, 1));
    }


    @Test
    @DisplayName("When initial score is less then zero then throw an exception")
    void newScoreLessThanZero() {
        assertThatThrownBy(() -> new Score(-2323, 0));
        assertThatThrownBy(() -> new Score(Integer.MIN_VALUE, 0));
        assertThatThrownBy(() -> new Score(-1, 3));
        assertThatThrownBy(() -> new Score(34, -1));
        assertThatThrownBy(() -> new Score(323, -23230));
        assertThatThrownBy(() -> new Score(323, Integer.MIN_VALUE));
    }
}