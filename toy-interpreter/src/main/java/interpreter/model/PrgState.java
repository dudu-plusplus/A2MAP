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

public class PrgState
{
    private IStack<IStmt> exeStack;
    private IDictionary<String, IValue> symTable;
    private IList<IValue> out;
    private IDictionary<StringValue, BufferedReader> fileTable;
    private IHeap heap;
    private IStmt originalProgram;

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
        return this.toString() + "\n";
    }

    @Override
    public String toString()
    {
        return "ExeStack:\n" + this.exeStack +
                "\nSymTable:\n" + this.symTable +
                "\nOut:\n" + this.out +
                "\nFileTable:\n" + this.fileTable +
                "\nHeap:\n" + this.heap +
                "\n----------------------------------------";
    }
}