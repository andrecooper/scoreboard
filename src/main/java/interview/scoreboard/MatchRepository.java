package interview.scoreboard;

public interface MatchRepository {

    /**
     * UpSert has to be implemented. Persists match to the storage if it does not exist or update if corresponding match exists
     *
     * @return upserted entity with ID
     **/
    MatchEntity save(MatchEntity matchEntity);

}
