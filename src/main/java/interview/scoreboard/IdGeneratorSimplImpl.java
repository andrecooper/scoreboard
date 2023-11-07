package interview.scoreboard;

public class IdGeneratorSimplImpl implements IdGenerator<Long>{

    @Override
    public Long generateNextId() {
        return System.nanoTime();
    }
}
