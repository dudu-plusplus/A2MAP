package interpreter.model.stmt;

import interpreter.exceptions.MyException;
import interpreter.exceptions.StatementException;
import interpreter.model.PrgState;
import interpreter.model.adt.IStack;
import interpreter.model.exp.IExp;
import interpreter.model.type.BoolType;
import interpreter.model.value.BoolValue;
import interpreter.model.value.IValue;

public class IfStmt implements IStmt
{
    private final IExp exp;
    private final IStmt thenStmt;
    private final IStmt elseStmt;

    public IfStmt(IExp exp, IStmt thenStmt, IStmt elseStmt)
    {
        this.exp = exp;
        this.thenStmt = thenStmt;
        this.elseStmt = elseStmt;
    }

    @Override
    public PrgState execute(PrgState state) throws StatementException
    {
        IValue val;
        try
        {
            val = this.exp.eval(state.getSymTable(), state.getHeap());
        }
        catch (MyException e)
        {
            throw new StatementException(e.getMessage());
        }

        if (!val.getType().equals(new BoolType()))
        {
            throw new StatementException("Condition expression must evaluate to a BoolType");
        }

        IStack<IStmt> exeStack = state.getExeStack();
        if (((BoolValue) val).getVal())
        {
            exeStack.push(this.thenStmt);
        }
        else
        {
            exeStack.push(this.elseStmt);
        }

        return state;
    }

    @Override
    public String toString()
    {
        return "if (" + this.exp + ") then (" + this.thenStmt + ") else (" + this.elseStmt + ")";
    }
}