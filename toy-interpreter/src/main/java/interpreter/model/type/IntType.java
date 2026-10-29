package interpreter.model.type;

import interpreter.model.value.IValue;
import interpreter.model.value.IntValue;

public class IntType implements IType {
    @Override
    public IValue defaultValue() {
        return new IntValue(0);
    }

    @Override
    public boolean equals(Object another) {
        return another instanceof IntType;
    }

    @Override
    public String toString() {
        return "int";
    }
}