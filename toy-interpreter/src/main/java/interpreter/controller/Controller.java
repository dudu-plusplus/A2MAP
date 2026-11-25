package interpreter.controller;

import interpreter.exceptions.MyException;
import interpreter.exceptions.StatementException;
import interpreter.model.PrgState;
import interpreter.model.stmt.IStmt;
import interpreter.model.value.IValue;
import interpreter.model.value.RefValue;
import interpreter.repository.IRepository;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class Controller
{
    private final IRepository repo;
    private boolean displayFlag;

    public Controller(IRepository repo)
    {
        this(repo, true);
    }

    public Controller(IRepository repo, boolean displayFlag)
    {
        this.repo = repo;
        this.displayFlag = displayFlag;
    }

    public void setDisplayFlag(boolean displayFlag)
    {
        this.displayFlag = displayFlag;
    }

    public PrgState oneStep(PrgState state) throws MyException
    {
        var stack = state.getExeStack();
        if (stack.isEmpty())
        {
            throw new StatementException("program state stack is empty");
        }
        IStmt crtStmt = stack.pop();
        return crtStmt.execute(state);
    }

    public void allStep() throws MyException
    {
        PrgState prg = repo.getPrgList().get(0);
        repo.logPrgStateExec(prg);
        while (!prg.getExeStack().isEmpty())
        {
            oneStep(prg);
            prg.getHeap().setContent(
                    garbageCollector(
                            prg.getSymTable().getContent().values(),
                            prg.getHeap().getContent()
                    )
            );
            repo.logPrgStateExec(prg);
            display(prg);
        }
    }

    private Map<Integer, IValue> garbageCollector(Collection<IValue> symTableValues, Map<Integer, IValue> heapContent)
    {
        Set<Integer> reachable = collectReachableAddresses(symTableValues, heapContent);
        return heapContent.entrySet().stream()
                .filter(entry -> reachable.contains(entry.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private Set<Integer> collectReachableAddresses(Collection<IValue> symTableValues, Map<Integer, IValue> heapContent)
    {
        Set<Integer> reachable = new HashSet<>();

        for (IValue val : symTableValues)
        {
            if (val instanceof RefValue refVal)
            {
                reachable.add(refVal.address());
            }
        }

        boolean changed;
        do
        {
            changed = false;
            Set<Integer> snapshot = new HashSet<>(reachable);
            for (Integer addr : snapshot)
            {
                IValue heapVal = heapContent.get(addr);
                if (heapVal instanceof RefValue refVal && reachable.add(refVal.address()))
                {
                    changed = true;
                }
            }
        } while (changed);

        return reachable;
    }

    private void display(PrgState prg)
    {
        if (displayFlag)
        {
            System.out.println(prg);
        }
    }
}