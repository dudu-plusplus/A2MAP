package interpreter.model.type;

import interpreter.model.value.IValue;
import interpreter.model.value.BoolValue;

public class BoolType implements IType {
    @Override
    public IValue defaultValue() {
        return new BoolValue(false);
    }

    @Override
    public boolean equals(Object another) {
        return another instanceof BoolType;
    }

    @Override
    public String toString() {
        return "bool";
    }
}