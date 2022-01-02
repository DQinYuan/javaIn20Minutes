package org.example.interpreter;

public class RightValue extends Value {

    public RightValue(Object v) {
        super(v);
    }

    @Override
    public void set(Object v) {
        throw new RuntimeException("can not set right value");
    }
}
