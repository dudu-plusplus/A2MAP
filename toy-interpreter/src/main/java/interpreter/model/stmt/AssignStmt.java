package interpreter.model.stmt;

import interpreter.exceptions.ADTException;
import interpreter.exceptions.ExpressionException;
import interpreter.exceptions.StatementException;
import interpreter.model.exp.IExp;
import interpreter.model.state.PrgState;
import interpreter.model.type.IType;
import interpreter.model.value.IValue;

public class AssignStmt implements IStmt {
    private final String id;
    private final IExp exp;

    public AssignStmt(String id, IExp exp) {
        this.id = id;
        this.exp = exp;
    }

    @Override
    public PrgState execute(PrgState state) throws StatementException {
        var symTable = state.getSymTable();
        if (!symTable.isDefined(id)) {
            throw new StatementException("Used variable " + id + " was not declared before.");
        }
        try {
            IValue val = exp.eval(symTable);
            IType typId = symTable.lookup(id).getType();
            if (!val.getType().equals(typId)) {
                throw new StatementException("Declared type of variable " + id + " and type of the assigned expression do not match.");
            }
            symTable.put(id, val);
        } catch (ExpressionException | ADTException e) {
            throw new StatementException(e.getMessage());
        }
        return state;
    }

    @Override
    public String toString() {
        return id + "=" + exp.toString();
    }
}