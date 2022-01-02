package org.example.interpreter;

import java.util.Map;

public class ScopeStack {

    private final Map<String, Value> context;

    private final ScopeStack parent;

    public ScopeStack(Map<String, Value> context, ScopeStack parent) {
        this.context = context;
        this.parent = parent;
    }

    public Value getVariableValue(String variableName) {
        Value variableValue = context.get(variableName);
        if (variableValue != null) {
            return variableValue;
        }
        return parent == null? null: parent.getVariableValue(variableName);
    }

    public void put(String variableName, Value variableValue) {
        context.put(variableName, variableValue);
    }

    public boolean existInCurrentScope(String variableName) {
        return context.containsKey(variableName);
    }

    public ScopeStack pop() {
        return parent;
    }
}
