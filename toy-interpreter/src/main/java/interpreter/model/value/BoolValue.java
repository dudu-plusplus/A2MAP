package interpreter.model.value;

import interpreter.model.type.IType;
import interpreter.model.type.BoolType;

public class BoolValue implements IValue {
    private final boolean val;

    public BoolValue(boolean v) {
        val = v;
    }

    public boolean getVal() {
        return val;
    }

    @Override
    public IType getType() {
        return new BoolType();
    }

    @Override
    public boolean equals(Object another) {
        if (!(another instanceof BoolValue)) {
            return false;
        }
        return this.val == ((BoolValue) another).getVal();
    }

    @Override
    public String toString() {
        return Boolean.toString(val);
    }
}