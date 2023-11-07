package interview.scoreboard;

import java.util.List;
import java.util.Optional;

public class MatchRepositoryInMemoryImpl implements MatchRepository {

    @Override
    public MatchEntity save(MatchEntity matchEntity) {
        return matchEntity;
    }

    @Override
    public Optional<MatchEntity> get(Long id) {
        return Optional.empty();
    }

    @Override
    public List<MatchEntity> findAll() {
        return List.of();
    }
}
