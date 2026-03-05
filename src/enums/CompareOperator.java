package enums;

public enum CompareOperator {
    GT(">"), LT("<"), EQ("="), GE(">="), LE("<=");

    private final String symbol;

    CompareOperator(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }

    public static CompareOperator fromSymbol(String symbol) {
        for (CompareOperator op : values()) {
            if (op.symbol.equals(symbol)) return op;
        }
        throw new IllegalArgumentException("Unknown operator: " + symbol);
    }
}