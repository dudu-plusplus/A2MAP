package interpreter.model.value;

import interpreter.model.type.BoolType;
import interpreter.model.type.IType;

public class BoolValue implements IValue
{
    private final boolean val;

    public BoolValue(boolean val)
    {
        this.val = val;
    }

    public boolean getVal()
    {
        return this.val;
    }

    @Override
    public IType getType()
    {
        return new BoolType();
    }

    @Override
    public boolean equals(Object another)
    {
        return another instanceof BoolValue other && this.val == other.val;
    }

    @Override
    public String toString()
    {
        return Boolean.toString(this.val);
    }
}