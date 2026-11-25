package interpreter.controller;

import interpreter.exceptions.MyException;
import interpreter.model.PrgState;
import interpreter.model.adt.MyDictionary;
import interpreter.model.adt.MyHeap;
import interpreter.model.adt.MyList;
import interpreter.model.adt.MyStack;
import interpreter.model.exp.ValueExp;
import interpreter.model.exp.VarExp;
import interpreter.model.stmt.CompStmt;
import interpreter.model.stmt.IStmt;
import interpreter.model.stmt.NewStmt;
import interpreter.model.stmt.PrintStmt;
import interpreter.model.stmt.VarDeclStmt;
import interpreter.model.type.IntType;
import interpreter.model.type.RefType;
import interpreter.model.value.IValue;
import interpreter.model.value.IntValue;
import interpreter.model.value.RefValue;
import interpreter.repository.IRepository;
import interpreter.repository.Repository;
import interpreter.view.Examples;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class AllStepGarbageCollectorTests
{
    @Test
    void allStepRunsEx6AndCollectsGarbage() throws MyException
    {
        PrgState state = new PrgState(
                new MyStack<>(),
                new MyDictionary<>(),
                new MyList<>(),
                new MyDictionary<>(),
                new MyHeap(),
                Examples.EX6
        );
        IRepository repo = new Repository(state, "test_log.txt");
        Controller controller = new Controller(repo, false);

        controller.allStep();

        assertTrue(state.getExeStack().isEmpty());
        assertEquals(List.of(new IntValue(20), new IntValue(25)), state.getOut().toList());

        RefValue vRef = (RefValue) state.getSymTable().lookup("v");
        RefValue aRef = (RefValue) state.getSymTable().lookup("a");
        Map<Integer, IValue> heapContent = state.getHeap().getContent();

        assertEquals(Map.of(
                vRef.address(), new IntValue(20),
                aRef.address(), vRef
        ), heapContent);
    }

    @Test
    void allStepRemovesUnreachableHeapEntriesAfterReassignment() throws MyException
    {
        // Ref int v; new(v, 10); new(v, 20); print(v);
        IStmt program = new CompStmt(
                new VarDeclStmt("v", new RefType(new IntType())),
                new CompStmt(
                        new NewStmt("v", new ValueExp(new IntValue(10))),
                        new CompStmt(
                                new NewStmt("v", new ValueExp(new IntValue(20))),
                                new PrintStmt(new VarExp("v"))
                        )
                )
        );

        PrgState state = new PrgState(
                new MyStack<>(),
                new MyDictionary<>(),
                new MyList<>(),
                new MyDictionary<>(),
                new MyHeap(),
                program
        );
        IRepository repo = new Repository(state, "test_log.txt");
        Controller controller = new Controller(repo, false);

        controller.allStep();

        RefValue vRef = (RefValue) state.getSymTable().lookup("v");
        Map<Integer, IValue> heapContent = state.getHeap().getContent();

        // Address 1 (value 10) becomes unreachable after second new(v, 20) and is swept
        assertFalse(heapContent.containsKey(1));
        assertEquals(Map.of(vRef.address(), new IntValue(20)), heapContent);
    }

    @Test
    void allStepKeepsTransitivelyReachableHeapEntries() throws MyException
    {
        // Ref int v; new(v, 10); Ref Ref int a; new(a, v); new(v, 20); print(v);
        IStmt program = new CompStmt(
                new VarDeclStmt("v", new RefType(new IntType())),
                new CompStmt(
                        new NewStmt("v", new ValueExp(new IntValue(10))),
                        new CompStmt(
                                new VarDeclStmt("a", new RefType(new RefType(new IntType()))),
                                new CompStmt(
                                        new NewStmt("a", new VarExp("v")),
                                        new CompStmt(
                                                new NewStmt("v", new ValueExp(new IntValue(20))),
                                                new PrintStmt(new VarExp("v"))
                                        )
                                )
                        )
                )
        );

        PrgState state = new PrgState(
                new MyStack<>(),
                new MyDictionary<>(),
                new MyList<>(),
                new MyDictionary<>(),
                new MyHeap(),
                program
        );
        IRepository repo = new Repository(state, "test_log.txt");
        Controller controller = new Controller(repo, false);

        controller.allStep();

        RefValue vRef = (RefValue) state.getSymTable().lookup("v");
        RefValue aRef = (RefValue) state.getSymTable().lookup("a");
        RefValue oldVRef = (RefValue) state.getHeap().get(aRef.address());
        Map<Integer, IValue> heapContent = state.getHeap().getContent();

        // Addresses 1 (10), 2 (Ref(1)), and 3 (20) are all reachable
        assertEquals(3, heapContent.size());
        assertEquals(new IntValue(10), heapContent.get(oldVRef.address()));
        assertEquals(new IntValue(20), heapContent.get(vRef.address()));
    }
}