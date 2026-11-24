package interpreter.model;

import interpreter.model.adt.IDictionary;
import interpreter.model.adt.IHeap;
import interpreter.model.adt.IList;
import interpreter.model.adt.IStack;
import interpreter.model.adt.MyDictionary;
import interpreter.model.adt.MyHeap;
import interpreter.model.adt.MyList;
import interpreter.model.adt.MyStack;
import interpreter.model.stmt.IStmt;
import interpreter.model.value.IValue;
import interpreter.model.value.StringValue;

import java.io.BufferedReader;
import java.util.TreeMap;

public class PrgState
{
    private final IStack<IStmt> exeStack;
    private final IDictionary<String, IValue> symTable;
    private final IList<IValue> out;
    private final IDictionary<StringValue, BufferedReader> fileTable;
    private final IHeap heap;
    private final IStmt originalProgram;

    public PrgState(
            IStack<IStmt> exeStack,
            IDictionary<String, IValue> symTable,
            IList<IValue> out,
            IDictionary<StringValue, BufferedReader> fileTable,
            IHeap heap,
            IStmt program
    )
    {
        this.exeStack = exeStack;
        this.symTable = symTable;
        this.out = out;
        this.fileTable = fileTable;
        this.heap = heap;
        this.originalProgram = program;
        this.exeStack.push(program);
    }

    public static PrgState of(IStmt program)
    {
        return new PrgState(
                new MyStack<>(),
                new MyDictionary<>(),
                new MyList<>(),
                new MyDictionary<>(),
                new MyHeap(),
                program
        );
    }

    public IStack<IStmt> getExeStack()
    {
        return this.exeStack;
    }

    public IDictionary<String, IValue> getSymTable()
    {
        return this.symTable;
    }

    public IList<IValue> getOut()
    {
        return this.out;
    }

    public IDictionary<StringValue, BufferedReader> getFileTable()
    {
        return this.fileTable;
    }

    public IHeap getHeap()
    {
        return this.heap;
    }

    public IStmt getOriginalProgram()
    {
        return this.originalProgram;
    }

    public String toLogString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("ExeStack:\n");
        for (IStmt stmt : this.exeStack.toList())
        {
            sb.append(stmt).append("\n");
        }
        sb.append("SymTable:\n");
        for (var entry : this.symTable.toMap().entrySet())
        {
            sb.append(entry.getKey()).append(" --> ").append(entry.getValue()).append("\n");
        }
        sb.append("Out:\n");
        for (IValue val : this.out.toList())
        {
            sb.append(val).append("\n");
        }
        sb.append("FileTable:\n");
        for (var entry : this.fileTable.toMap().entrySet())
        {
            sb.append(entry.getKey().val()).append("\n");
        }
        sb.append("Heap:\n");
        new TreeMap<>(this.heap.getContent())
                .forEach((addr, val) -> sb.append(addr).append(" --> ").append(val).append("\n"));
        sb.append("\n");
        return sb.toString();
    }
}