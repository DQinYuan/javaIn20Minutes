package org.example.interpreter;

public class TreeNodeResult {

    public static final TreeNodeResult NULL_RESULT = new TreeNodeResult(new RightValue(null), false);

    private final Value value;

    /**
     * 含有 return 语句时该值才为 true
     */
    private final boolean ret;

    private TreeNodeResult(Value value, boolean ret) {
        this.value = value;
        this.ret = ret;
    }

    public static TreeNodeResult newResultWithValue(Object value) {
        return new TreeNodeResult(new RightValue(value), false);
    }

    public static TreeNodeResult newResultWithValue(Value value) {
        return new TreeNodeResult(value, false);
    }

    public static TreeNodeResult newReturnResult(Value value) {
        return new TreeNodeResult(value, true);
    }

    public Value getValue() {
        return value;
    }

    public Object getValueObj() {
        return value.get();
    }

    public boolean isRet() {
        return ret;
    }
}
