package interview.scoreboard;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MatchRepositoryInMemoryImplTest {

    private final MatchRepository matchRepository = new MatchRepositoryInMemoryImpl();

    @Test
    @DisplayName("When save new entity then generate new ID")
    void saveNewEntity() {
        var matchEntity1 = stubMatchEntity("Italy", "France");
        var matchEntity2 = stubMatchEntity("Italy", "France");

        var savedEntity1 = matchRepository.save(matchEntity1);
        var savedEntity2 = matchRepository.save(matchEntity2);

        assertThat(savedEntity1.getId()).isPositive();
        assertThat(savedEntity2.getId())
            .isPositive()
            .isNotEqualTo(savedEntity1.getId());
    }


    @Test
    @DisplayName("When saving entity with non existing ID then ignore it and generate own id")
    void testSaveNonExistingId() {
        var matchEntity = stubMatchEntity("Germany", "Brasil");
        var manualId = 1L;
        matchEntity.setId(manualId);

        var savedEntity1 = matchRepository.save(matchEntity);

        assertThat(savedEntity1.getId()).isPositive().isNotEqualTo(manualId);
    }

    @Test
    @DisplayName("When saving entity with ID then update corresponding entity")
    void whenSaveWithIdThenUpdate() {
        var matchEntity = stubMatchEntity("Portugal", "Greece");

        var savedEntity1 = matchRepository.save(matchEntity);

        assertThat(savedEntity1.getId()).isPositive();
        assertThat(savedEntity1)
            .returns(matchEntity.getTeamHome(), MatchEntity::getTeamHome)
            .returns(matchEntity.getTeamAway(), MatchEntity::getTeamAway);

        matchEntity.setTeamHome("Italy");
        matchEntity.setTeamAway("Spain");

        var savedEntity2 = matchRepository.save(savedEntity1);

        assertThat(savedEntity1.getId()).isEqualTo(savedEntity2.getId());
        assertThat(savedEntity2)
            .returns(matchEntity.getTeamHome(), MatchEntity::getTeamHome)
            .returns(matchEntity.getTeamAway(), MatchEntity::getTeamAway);

    }


    @Test
    @DisplayName("When requesting match by non-existing id then return empty optional")
    void getNonExisting() {
        var matchEntity = matchRepository.get(4545L);

        assertThat(matchEntity).isEmpty();
    }

    @Test
    @DisplayName("When getting existing match then return its optional")
    void get() {
        var matchEntity = stubMatchEntity("France", "Brasil");
        matchEntity.setMatchState(MatchState.LIVE);
        matchEntity.setScore(new Score(3, 1));
        matchEntity.setStartTime(LocalDateTime.of(1998, 7, 12, 21, 0));

        var savedEntity1 = matchRepository.save(matchEntity);
        var matchEntity1 = matchRepository.get(savedEntity1.getId());

        assertThat(matchEntity1).isPresent().get().isEqualTo(savedEntity1);
    }


    @Test
    @DisplayName("When findAll on empty repo then return empty list")
    void findAllEmpty() {
        MatchRepository cleanRepo = new MatchRepositoryInMemoryImpl();

        var allMatches = cleanRepo.findAll();

        assertThat(allMatches).isNotNull().isEmpty();
    }

    @Test
    @DisplayName("When findAll then return all records")
    void findAll() {
        var m1 = stubMatchEntity("England", "Scotland");
        var m2 = stubMatchEntity("England", "Scotland");
        var m3 = stubMatchEntity("England", "Scotland");
        MatchRepository cleanRepo = new MatchRepositoryInMemoryImpl();

        m1 = cleanRepo.save(m1);
        m2 = cleanRepo.save(m2);
        m3 = cleanRepo.save(m3);

        var allMatches = cleanRepo.findAll();

        assertThat(allMatches).containsOnly(m1, m2, m3);
    }


    private static MatchEntity stubMatchEntity(String homeTeam, String awayTeam) {
        var matchEntity = new MatchEntity();
        matchEntity.setTeamHome(homeTeam);
        matchEntity.setTeamAway(awayTeam);
        matchEntity.setStartTime(LocalDateTime.now());
        matchEntity.setMatchState(MatchState.LIVE);
        return matchEntity;
    }

}