package interpreter.model.stmt;

import interpreter.exceptions.ExpressionException;
import interpreter.exceptions.StatementException;
import interpreter.model.exp.IExp;
import interpreter.model.state.PrgState;
import interpreter.model.type.BoolType;
import interpreter.model.value.BoolValue;
import interpreter.model.value.IValue;

public class IfStmt implements IStmt {
    private final IExp exp;
    private final IStmt thenS;
    private final IStmt elseS;

    public IfStmt(IExp e, IStmt t, IStmt el) {
        exp = e;
        thenS = t;
        elseS = el;
    }

    @Override
    public PrgState execute(PrgState state) throws StatementException {
        try {
            IValue val = exp.eval(state.getSymTable());
            if (!val.getType().equals(new BoolType())) {
                throw new StatementException("Conditional expression is not a boolean");
            }
            boolean cond = ((BoolValue) val).getVal();
            if (cond) {
                state.getExeStack().push(thenS);
            } else {
                state.getExeStack().push(elseS);
            }
        } catch (ExpressionException e) {
            throw new StatementException(e.getMessage());
        }
        return state;
    }

    @Override
    public String toString() {
        return "(IF(" + exp.toString() + ") THEN(" + thenS.toString() + ") ELSE(" + elseS.toString() + "))";
    }
}