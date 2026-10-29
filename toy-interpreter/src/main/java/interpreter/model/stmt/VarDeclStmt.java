package interpreter.model.stmt;

import interpreter.exceptions.StatementException;
import interpreter.model.PrgState;
import interpreter.model.type.IType;

public class VarDeclStmt implements IStmt {
    private final String name;
    private final IType typ;

    public VarDeclStmt(String name, IType typ) {
        this.name = name;
        this.typ = typ;
    }

    @Override
    public PrgState execute(PrgState state) throws StatementException {
        var symTable = state.getSymTable();
        if (symTable.isDefined(name)) {
            throw new StatementException("Variable " + name + " already exists in the symTable.");
        }
        symTable.put(name, typ.defaultValue());
        return state;
    }

    @Override
    public String toString() {
        return typ.toString() + " " + name;
    }
}