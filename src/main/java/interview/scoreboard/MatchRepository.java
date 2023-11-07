package interview.scoreboard;

import java.util.Optional;

public interface MatchRepository {

    /**
     * UpSert has to be implemented. Persists match to the storage if it does not exist or update if corresponding match exists
     *
     * @return upserted entity with ID
     **/
    MatchEntity save(MatchEntity matchEntity);


    /**
     * @return optional of the match by id.  and returns emtpy optional if match was not found
     */
    Optional<MatchEntity> get(Long id);
}
