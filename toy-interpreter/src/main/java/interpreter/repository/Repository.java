package interpreter.repository;

import interpreter.model.state.PrgState;
import java.util.ArrayList;
import java.util.List;

public class Repository implements IRepository {
    private final List<PrgState> repo = new ArrayList<>();

    public Repository(PrgState prg) {
        this.repo.add(prg);
    }

    @Override
    public PrgState getCrtPrg() {
        return repo.get(0);
    }

    @Override
    public void add(PrgState prg) {
        repo.add(prg);
    }
}