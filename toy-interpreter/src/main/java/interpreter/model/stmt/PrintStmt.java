package interpreter.model.stmt;

import interpreter.exceptions.ExpressionException;
import interpreter.exceptions.StatementException;
import interpreter.model.exp.IExp;
import interpreter.model.PrgState;
import interpreter.model.value.IValue;

public class PrintStmt implements IStmt {
    private final IExp exp;

    public PrintStmt(IExp exp) {
        this.exp = exp;
    }

    @Override
    public PrgState execute(PrgState state) throws StatementException {
        try {
            IValue val = exp.eval(state.getSymTable());
            state.getOut().add(val);
        } catch (ExpressionException e) {
            throw new StatementException(e.getMessage());
        }
        return state;
    }

    @Override
    public String toString() {
        return "print(" + exp.toString() + ")";
    }
}