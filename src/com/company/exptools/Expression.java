package com.company.exptools;

import java.util.*;

public class Expression {

    private final char[] expression;

    public Expression(String expression) {
        this.expression = expression
                .toLowerCase()
                .toCharArray();
    }

    public char getChar(int pos) {
        return expression[pos];
    }

    public Expression subExp(int beginIndex) {
        return new Expression(String.valueOf(expression).substring(beginIndex));
    }

    public Expression subExp(int beginIndex, int endIndex) {
        return new Expression(String.valueOf(expression).substring(beginIndex, endIndex));
    }

    public int length() {
        return expression.length;
    }

    @Override
    public String toString() {
        return "Expression{" +
                "expression=" + Arrays.toString(expression) +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Expression that = (Expression) o;

        return Arrays.equals(expression, that.expression);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(expression);
    }
}
