package interpreter.model;

import interpreter.model.adt.*;
import interpreter.model.stmt.IStmt;
import interpreter.model.value.IValue;
import interpreter.model.value.StringValue;
import java.io.BufferedReader;

public class PrgState {
    private IStack<IStmt> exeStack;
    private IDictionary<String, IValue> symTable;
    private IList<IValue> out;
    private IDictionary<StringValue, BufferedReader> fileTable;
    private IStmt originalProgram;

    public PrgState(IStack<IStmt> stk, IDictionary<String, IValue> symtbl, IList<IValue> ot, IStmt prg) {
        this(stk, symtbl, ot, new MyDictionary<>(), prg);
    }

    public PrgState(IStack<IStmt> stk, IDictionary<String, IValue> symtbl, IList<IValue> ot,
                    IDictionary<StringValue, BufferedReader> fileTable, IStmt prg) {
        this.exeStack = stk;
        this.symTable = symtbl;
        this.out = ot;
        this.fileTable = fileTable;
        this.originalProgram = prg;
        stk.push(prg);
    }

    public static PrgState of(IStmt program) {
        return new PrgState(new MyStack<>(), new MyDictionary<>(), new MyList<>(), new MyDictionary<>(), program);
    }

    public IStack<IStmt> getExeStack() { return exeStack; }
    public IDictionary<String, IValue> getSymTable() { return symTable; }
    public IList<IValue> getOut() { return out; }
    public IDictionary<StringValue, BufferedReader> getFileTable() { return fileTable; }

    public void setExeStack(IStack<IStmt> stk) { exeStack = stk; }
    public void setSymTable(IDictionary<String, IValue> symtbl) { symTable = symtbl; }
    public void setOut(IList<IValue> ot) { out = ot; }
    public void setFileTable(IDictionary<StringValue, BufferedReader> fileTable) { this.fileTable = fileTable; }

    public String toLogString() {
        var sb = new StringBuilder();
        sb.append("ExeStack:\n");
        exeStack.toList().forEach(s -> sb.append(s).append('\n'));
        sb.append("SymTable:\n");
        symTable.toMap().forEach((k, v) -> sb.append(k).append(" --> ").append(v).append('\n'));
        sb.append("Out:\n");
        out.toList().forEach(v -> sb.append(v).append('\n'));
        sb.append("FileTable:\n");
        fileTable.toMap().keySet().forEach(f -> sb.append(f.val()).append('\n'));
        return sb.toString();
    }

    @Override
    public String toString() {
        return "ExeStack:\n" + exeStack.toString() + "\nSymTable:\n" + symTable.toString() + "\nOut:\n" + out.toString() + "\nFileTable:\n" + fileTable.toString() + "\n";
    }
}