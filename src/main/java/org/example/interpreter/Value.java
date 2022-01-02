package org.example.interpreter;

public abstract class Value {

    private Object v;

    public Value(Object v) {
        this.v = v;
    }

    public void set(Object v) {
        this.v = v;
    }

    Object get() {
        return v;
    }

}
