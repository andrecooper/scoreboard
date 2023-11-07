package interview.scoreboard;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class MatchRepositoryInMemoryImpl implements MatchRepository {

    private final ConcurrentMap<Long, MatchEntity> inMemoryDb = new ConcurrentHashMap<>();
    private final IdGenerator<Long> idGenerator = new IdGeneratorSimplImpl();

    @Override
    public MatchEntity save(MatchEntity matchEntity) {
        if (matchEntity.getId() == null || !inMemoryDb.containsKey(matchEntity.getId())) {
            Long generatedId = idGenerator.generateNextId();
            matchEntity.setId(generatedId);
        }
        inMemoryDb.put(matchEntity.getId(), matchEntity);
        return matchEntity;
    }

    @Override
    public Optional<MatchEntity> get(Long id) {
        var matchEntity = inMemoryDb.get(id);
        return matchEntity == null ? Optional.empty() : Optional.of(matchEntity);
    }

    @Override
    public List<MatchEntity> findAll() {
        return inMemoryDb.values().stream().toList();
    }
}
