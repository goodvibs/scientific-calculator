package com.company;

import com.company.exptools.Expression;
import com.company.exptools.ExpressionIterator;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

public class Calculator {

    public static class Builder {

        int places;

        public Builder() {
            places = Calculator.PLACES_DEFAULT;
        }

        public Builder places(int places) {
            this.places = places;
            return this;
        }

        public Calculator build() {
            return new Calculator(places);
        }
    }

    public static final int PLACES_DEFAULT = 12;

    private final int places;

    public Calculator() {
        places = PLACES_DEFAULT;
    }

    public Calculator(int places) {
        this.places = places;
    }

    public static Builder builder() {
        return new Builder();
    }

    private static final Map<String, Integer> opsPrecedences = Map.of(
            "+", 0,
            "-", 0,
            "*", 1,
            "/", 1,
            "%", 1,
            "^", 2
    );

    private static final Map<String, Double> constants = Map.of(
            "pi", Math.PI,
            "e", Math.E
    );

    public double calculate(String expression) throws Exception {
        return calculate(new Expression(expression));
    }

    public double calculate(Expression expression) throws Exception {
        ExpressionIterator iter = new ExpressionIterator(expression);
        Deque<Double> nums = new ArrayDeque<>();
        Deque<String> tokens = new ArrayDeque<>();
        char last = ' ';
        while (iter.hasNext()) {
            if (iter.next() == ' ')
                continue;
            if (Character.isDigit(iter.curr()) || iter.curr() == '.') {
                if (last == ')') {
                    while (!tokens.isEmpty() && hasPrecedence(tokens.peek(), "*"))
                        nums.push(applyOp(tokens.pop(), nums.pop(), nums.pop()));
                    tokens.push("*");
                }
                StringBuilder numBldr = new StringBuilder("0").append(iter.curr());
                while (iter.hasNext() && (Character.isDigit(iter.peekNext()) || iter.peekNext() == '.'))
                    numBldr.append(iter.next());
                nums.push(Double.parseDouble(numBldr.toString()));
            } else if (iter.curr() == '(') {
                if (last == ')' || (Character.isDigit(last))) {
                    while (!tokens.isEmpty() && hasPrecedence(tokens.peek(), "*"))
                        nums.push(applyOp(tokens.pop(), nums.pop(), nums.pop()));
                    tokens.push("*");
                }
                tokens.push(String.valueOf(iter.curr()));
            } else if (iter.curr() == ')') {
                int commas = 0;
                while (!tokens.isEmpty() && !tokens.peek().equals("(")) {
                    if (tokens.peek().equals(",")) {
                        commas++;
                        tokens.pop();
                    } else
                        nums.push(applyOp(tokens.pop(), nums.pop(), nums.pop()));
                } tokens.pop();
                if (!tokens.isEmpty() && !opsPrecedences.containsKey(tokens.peek()) && !constants.containsKey(tokens.peek())) {
                    List<Double> inputs = new ArrayList<>();
                    for (int i = 0; i <= commas; i++)
                        inputs.add(0, nums.pop());
                    nums.push(applyFunc(tokens.pop(), inputs));
                }
            } else if (iter.curr() == ',') {
                while (!tokens.isEmpty() && !tokens.peek().equals(",") && !tokens.peek().equals("("))
                    nums.push(applyOp(tokens.pop(), nums.pop(), nums.pop()));
                tokens.push(",");
            } else if (opsPrecedences.containsKey(String.valueOf(iter.curr()))) {
                while (!tokens.isEmpty() && hasPrecedence(tokens.peek(), String.valueOf(iter.curr())))
                    nums.push(applyOp(tokens.pop(), nums.pop(), nums.pop()));
                tokens.push(String.valueOf(iter.curr()));
            } else if (Character.isLetter(iter.curr())) {
                if (last == ')' || Character.isDigit(last))
                    tokens.push("*");
                StringBuilder strBldr = new StringBuilder().append(iter.curr());
                while (iter.hasNext() && Character.isLetter(iter.peekNext()))
                    strBldr.append(iter.next());
                String str = strBldr.toString();
                boolean isConstant = false;
                for (String constant : constants.keySet())
                    if (constant.equals(str)) {
                        nums.push(constants.get(str));
                        isConstant = true;
                        iter.next(-1);
                        break;
                    }
                if (isConstant) {
                    if (iter.hasNext() && iter.next() == '(')
                        tokens.push("*");
                } else
                    tokens.push(str);
            } else
                throw new Exception("something went wrong");
            last = iter.curr();
        } while (!tokens.isEmpty())
            nums.push(applyOp(tokens.pop(), nums.pop(), nums.pop()));
        return round(nums.pop());
    }

    private boolean hasPrecedence(String op1, String op2) {
        if (op1.matches("[(),]") || op2.matches("[(),]"))
            return false;
        return opsPrecedences.get(op1) >= opsPrecedences.get(op2);
    }

    private double applyOp(String op, double num2, double num1) {
        switch (op) {
            case "+":
                return num1 + num2;
            case "-":
                return num1 - num2;
            case "*":
                return num1 * num2;
            case "/":
                return num1 / num2;
            case "%":
                return num1 % num2;
            case "^":
                return Math.pow(num1, num2);
        }
        return 0;
    }

    private double applyFunc(String function, List<Double> inputs) throws Exception {
        switch (function) {
            case "sqrt":
                if (inputs.size() == 1)
                    return Math.sqrt(inputs.get(0));
                break;
            case "cbrt":
                if (inputs.size() == 1)
                    return Math.cbrt(inputs.get(0));
                break;
            case "rt":
                if (inputs.size() == 2)
                    return Math.pow(inputs.get(1), 1 / inputs.get(0));
                break;
            case "log":
                if (inputs.size() == 1)
                    return Math.log10(inputs.get(0));
                if (inputs.size() == 2)
                    return Math.log10(inputs.get(1)) / Math.log10(inputs.get(0));
                break;
            case "ln":
                if (inputs.size() == 1)
                    return Math.log(inputs.get(0));
                break;
            case "sin":
                if (inputs.size() == 1)
                    return Math.sin(inputs.get(0));
                break;
            case "cos":
                if (inputs.size() == 1)
                    return Math.cos(inputs.get(0));
                break;
            case "tan":
                if (inputs.size() == 1)
                    return Math.tan(inputs.get(0));
                break;
            case "csc":
                if (inputs.size() == 1)
                    return 1 / Math.sin(inputs.get(0));
                break;
            case "sec":
                if (inputs.size() == 1)
                    return 1 / Math.cos(inputs.get(0));
                break;
            case "cot":
                if (inputs.size() == 1)
                    return 1 / Math.tan(inputs.get(0));
                break;
            case "asin":
                if (inputs.size() == 1)
                    return Math.asin(inputs.get(0));
                break;
            case "acos":
                if (inputs.size() == 1)
                    return Math.acos(inputs.get(0));
                break;
            case "atan":
                if (inputs.size() == 1)
                    return Math.atan(inputs.get(0));
                break;
        }
        throw new Exception(String.format("invalid function: %s([%d arguments])", function, inputs.size()));
    }

    private double round(double val) {
        BigDecimal bd = new BigDecimal(Double.toString(val));
        bd = bd.setScale(this.places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public static void main(String[] args) throws Exception {
        System.out.println("\nValid symbols: +, -, *, /, %, ^, (, ), pi, e");
        System.out.println("Supported single-input functions: sqrt(), cbrt(), log(), ln(), sin(), cos(), tan(), csc(), sec(), cot(), asin(), acos(), atan()");
        System.out.println("Supported multi-input functions: rt(DEGREE, NUM), log(BASE, NUM)");
        System.out.println("\nEnter 'stop' to terminate the program\n");
        Scanner sc = new Scanner(System.in);
        Calculator calc = Calculator.builder().places(12).build();
        while (true) {
            System.out.print("Input: ");
            String userInput = sc.nextLine();
            if (userInput.equalsIgnoreCase("stop"))
                break;
            System.out.println("Result: " + calc.calculate(userInput));
            System.out.println();
        }

        //System.out.println(new Calculator().calculate("5*60+301-72"));
    }
}
