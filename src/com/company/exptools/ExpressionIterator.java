package com.company.exptools;

public class ExpressionIterator {

    private final Expression expression;
    private int i;

    public ExpressionIterator(Expression expression) {
        this.expression = expression;
        i = -1;
    }

    public boolean hasNext() {
        return i < expression.length() - 1;
    }

    public char next() {
        return expression.getChar(++i);
    }

    public char next(int num) {
        i += num;
        return expression.getChar(i);
    }

    public char peekNext() {
        return expression.getChar(i + 1);
    }

    public char peekNext(int num) {
        return expression.getChar(i + num);
    }

    public char curr() {
        return expression.getChar(i);
    }

    public int pos() {
        return i;
    }

    @Override
    public String toString() {
        return "ExpressionIterator{" +
                "curr=" + (i >= 0 ? expression.getChar(i) : null) +
                '}';
    }
}