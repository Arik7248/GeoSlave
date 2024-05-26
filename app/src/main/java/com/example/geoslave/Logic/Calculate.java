package com.example.geoslave.Logic;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.geoslave.FormulaActivity;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Calculate {

    @Nullable
    public static Fraction calculate(String expression, Context context) {
        List<String> postfix = shuntingYard(expression, context);
        if (postfix != null) {
            Fraction result = evaluatePostfix(postfix, context);
            if (result != null) {
                Log.d("Calculate", "Result: " + result.getNumerator()+"/");
                return result;
            }else{
                return null;
            }
        }
        return null;
    }

    public static List<String> shuntingYard(String expression, Context context) {
        Map<String, Integer> precedence = new HashMap<>();
        precedence.put("+", 1);
        precedence.put("-", 1);
        precedence.put("*", 2);
        precedence.put("/", 2);
        precedence.put("^", 3);
        precedence.put("sqrt", 4);  // Treat 'sqrt' as the square root operator

        String[] tokens = expression.split("(?<=[-+*/()^])|(?=[-+*/()^])|(?<=\\d)\\s+(?=\\d)|(?<=sqrt)|(?=sqrt)");

        List<String> output = new ArrayList<>();
        Deque<String> operators = new ArrayDeque<>();

        boolean lastTokenWasOperator = true; // Start as true to handle negative at the beginning

        for (String token : tokens) {
            token = token.trim(); // Trim whitespace
            if (token.isEmpty()) {
                continue;
            }
            Log.d("Calculate", "Token: " + token);
            if (token.matches("-?\\d+(\\.\\d+)?")) { // Handle numbers
                output.add(token);
                lastTokenWasOperator = false;
            } else if (token.equals("(")) {
                operators.push(token);
                lastTokenWasOperator = true;
            } else if (token.equals(")")) {
                while (!operators.isEmpty() && !operators.peek().equals("(")) {
                    output.add(operators.pop());
                }
                if (operators.isEmpty() || !operators.peek().equals("(")) {
                    Toast.makeText(context, "Mismatched parentheses", Toast.LENGTH_LONG).show();
                    return null;
                }
                operators.pop(); // Discard the '('
                lastTokenWasOperator = false;
            } else if (precedence.containsKey(token)) { // Check if token is an operator
                // Handle negative numbers after an operator
                if (lastTokenWasOperator && token.equals("-")) {
                    output.add("0");
                }
                while (!operators.isEmpty() && precedence.getOrDefault(token, 0) <= precedence.getOrDefault(operators.peek(), 0)) {
                    output.add(operators.pop());
                }
                operators.push(token);
                lastTokenWasOperator = true;
            } else {
                Toast.makeText(context, "Invalid token: " + token, Toast.LENGTH_LONG).show();
                return null;
            }
        }

        while (!operators.isEmpty()) {
            if (operators.peek().equals("(")) {
                Toast.makeText(context, "Mismatched parentheses", Toast.LENGTH_LONG).show();
                return null;
            }
            output.add(operators.pop());
        }

        return output;
    }

    public static Fraction evaluatePostfix(List<String> expression, Context context) {
        Deque<Fraction> stack = new ArrayDeque<>();

        for (String token : expression) {
            if (token.matches("-?\\d+(\\.\\d+)?")) {
                // Token is a number
                Log.d("Calculate", "Token: " + token);
                if (parseFraction(token) == null){
                    return null;
                }
                stack.push(parseFraction(token));
            } else if (isOperator(token)) {
                // Token is an operator
                if (token.equals("sqrt")) {
                    if (stack.size() < 1) {
                        Toast.makeText(context, "ERROR, INSUFFICIENT OPERANDS FOR OPERATOR: " + token, Toast.LENGTH_LONG).show();
                        return null;
                    }
                    Fraction a = stack.pop();
                    if (a.getNumerator() < 0) {
                        Toast.makeText(context, "Square root of negative number", Toast.LENGTH_LONG).show();
                        return null;
                    }
                    if (a.getNumerator() != Math.sqrt(a.getNumerator()) * Math.sqrt(a.getNumerator()) ||
                            a.getDenominator() != Math.sqrt(a.getDenominator()) * Math.sqrt(a.getDenominator())){
                        FormulaActivity.RightSqrt =false;
                    }
                    a = a.sqrt();
                    stack.push(a);
                } else {
                    if (stack.size() < 2) {
                        Toast.makeText(context, "ERROR, INSUFFICIENT OPERANDS FOR OPERATOR: " + token, Toast.LENGTH_LONG).show();
                        return null;
                    }
                    Fraction b = stack.pop();
                    Fraction a = stack.pop();
                    switch (token) {
                        case "+":
                            a.add(b);
                            stack.push(a);
                            break;
                        case "-":
                            a.subtract(b);
                            stack.push(a);
                            break;
                        case "*":
                            a.multiply(b);
                            stack.push(a);
                            break;
                        case "/":
                            if (b.getNumerator() == 0) {
                                Toast.makeText(context, "ERROR, DIVISION BY ZERO", Toast.LENGTH_LONG).show();
                                return null;
                            }
                            a.divide(b);
                            stack.push(a);
                            break;
                        case "^":
                            if (b.getDenominator() != 1 || a.getDenominator() != 1) {
                                Toast.makeText(context, "Power operation (^) not supported for Fraction", Toast.LENGTH_LONG).show();
                                return null;
                            }
                            a.power(b.getNumerator());
                            stack.push(a);
                            break;
                        default:
                            Toast.makeText(context, "ERROR, UNKNOWN OPERATOR: " + token, Toast.LENGTH_LONG).show();
                            return null;
                    }
                }
            } else {
                Toast.makeText(context, "ERROR, INVALID TOKEN: " + token, Toast.LENGTH_LONG).show();
                return null;
            }
        }

        if (stack.size() != 1) {
            Toast.makeText(context, "ERROR, INVALID EXPRESSION", Toast.LENGTH_LONG).show();
            return null;
        }

        return stack.pop();
    }

    public static int[] AmboxjSqrt(int num){
        int mas = 1;
        for (int i = (int) Math.sqrt(num);i > 1;i--){
            if (num % (i*i) == 0){
                mas*=i;
                num/=i*i;
            }
        }
        return new int[] {mas,num};
    }

    public static boolean containsOnly2And5(int number) {
        while (number % 2 == 0) {
            number /= 2;
        }
        while (number % 5 == 0) {
            number /= 5;
        }
        return number == 1;
    }

    public static Object[] toArmatovString(Fraction f1, Fraction f2){
        int a = f1.getNumerator();
        int b = f2.getNumerator() * f2.getDenominator();
        int c = f1.getDenominator() * f2.getDenominator();
        return a == 1 ? new Object[] {"",b,c} : new Object[] {a,b,c};
    }

    public static String printStr(Fraction result){
        if (result.getDenominator() == 1){
            if (FormulaActivity.isUnderSqrt){
                if (AmboxjSqrt(result.getNumerator())[0] == 1){
                    return ("Result: " + "√" + "("
                            + AmboxjSqrt(result.getNumerator())[1] + ")");
                }else {
                    return ("Result: " + AmboxjSqrt(result.getNumerator())[0] + "√" + "("
                            + AmboxjSqrt(result.getNumerator())[1] + ")");
                }
            }
            else{
                return ("Result: " + result.getNumerator());
            }
        }else{
            if (containsOnly2And5(result.getDenominator()) && !FormulaActivity.isUnderSqrt){
                if (FormulaActivity.isUnderSqrt){
                    return ("Result: " + "√" + "(" +
                            (double) result.getNumerator() / result.getDenominator() + ")");

                }else{
                    return ("Result: " +
                            (double) result.getNumerator() / result.getDenominator());
                }
            }else{
                if (FormulaActivity.isUnderSqrt){
                    Fraction amb = new Fraction(AmboxjSqrt(result.getNumerator())[0],AmboxjSqrt(result.getDenominator())[0]);
                    Fraction chamb = new Fraction(AmboxjSqrt(result.getNumerator())[1],AmboxjSqrt(result.getDenominator())[1]);
                    Object[] ans = toArmatovString(amb, chamb);
                    return ("Result: " + ans[0] + "√" + "(" + ans[1] +
                            ")" + "/" + ans[2]);
                }else {
                    return ("Result: " + result);
                }
            }
        }
    }

    public static String DoubleToFraction(double num){
        int count = 0;
        int denom = 1;
        int numer = (int) num;
        while ((int) num != num){
            num*=10;
            numer = (int) num;
            count++;
        }
        denom = (int) Math.pow(10, count);

        return numer + "/" + denom;
    }

    private static Fraction parseFraction(String token) {
        // Check if the token starts with a zero and is not just "0"
        if (token.matches("^0\\d*$") && token.length() != 1) {
            // If the token starts with a zero followed by digits, consider it invalid
            return null;
        }

        String[] arr = token.split("/");
        if (arr.length == 2) {
            // Handle negative fractions
            int numerator = Integer.parseInt(arr[0]);
            if (numerator < 0) {
                return new Fraction(numerator, Integer.parseInt(arr[1]));
            }

            int denominator = Integer.parseInt(arr[1]);
            return new Fraction(numerator, denominator);
        } else if (arr.length == 1) {
            // If only one part is provided, assume it's the numerator and denominator is 1
            double value = Double.parseDouble(arr[0]);
            int denominator = 1;
            while (value != Math.floor(value)) {
                value *= 10;
                denominator *= 10;
            }
            int numerator = (int) value;
            // Handle negative numbers
            if (numerator < 0) {
                return new Fraction(numerator, denominator);
            }
            return new Fraction(numerator, denominator);
        } else {
            // Invalid fraction format
            return null;
        }
    }




    // Helper method to check if a token is an operator
    private static boolean isOperator(String token) {
        return token.equals("+") || token.equals("-") || token.equals("*") || token.equals("/") || token.equals("^") || token.equals("sqrt");
    }
}
