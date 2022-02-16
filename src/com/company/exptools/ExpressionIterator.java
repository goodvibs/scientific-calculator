package com.company.exptools;

public class ExpressionIterator {

    private final Expression expression;
    private int index;

    public ExpressionIterator(Expression expression) {
        this.expression = expression;
        index = -1;
    }

    public boolean hasNext() {
        return index < expression.length() - 1;
    }

    public char next() {
        return expression.getChar(++index);
    }

    public void progress(int num) {
        index += num;
    }

    public char peekNext() {
        return expression.getChar(index + 1);
    }

    public char peekNext(int num) {
        return expression.getChar(index + num);
    }

    public char curr() {
        return expression.getChar(index);
    }

    public int pos() {
        return index;
    }

    @Override
    public String toString() {
        return "ExpressionIterator{" +
                "curr=" + (index >= 0 ? expression.getChar(index) : null) +
                '}';
    }
}