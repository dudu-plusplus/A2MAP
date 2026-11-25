package interpreter.model.stmt;

import interpreter.exceptions.MyException;
import interpreter.exceptions.StatementException;
import interpreter.model.PrgState;
import interpreter.model.adt.IStack;
import interpreter.model.exp.IExp;
import interpreter.model.type.BoolType;
import interpreter.model.value.BoolValue;
import interpreter.model.value.IValue;

public class WhileStmt implements IStmt
{
    private final IExp exp;
    private final IStmt stmt;

    public WhileStmt(IExp exp, IStmt stmt)
    {
        this.exp = exp;
        this.stmt = stmt;
    }

    @Override
    public PrgState execute(PrgState state) throws StatementException
    {
        IValue condVal;
        try
        {
            condVal = this.exp.eval(state.getSymTable(), state.getHeap());
        }
        catch (MyException e)
        {
            throw new StatementException(e.getMessage());
        }

        if (!(condVal.getType() instanceof BoolType) || !(condVal instanceof BoolValue boolVal))
        {
            throw new StatementException("Condition expression in while is not a boolean");
        }

        if (boolVal.getVal())
        {
            IStack<IStmt> exeStack = state.getExeStack();
            exeStack.push(this);
            exeStack.push(this.stmt);
        }

        return state;
    }

    @Override
    public String toString()
    {
        return "while(" + this.exp + ") " + this.stmt;
    }
}