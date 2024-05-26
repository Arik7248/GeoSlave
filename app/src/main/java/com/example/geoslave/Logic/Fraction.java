package com.example.geoslave.Logic;

import com.example.geoslave.FormulaActivity;

import java.util.Scanner;

public class Fraction {
    private int numerator;
    private int denominator;

    public int getNumerator() {
        return numerator;
    }

    public int getDenominator() {
        return denominator;
    }

    public void add(Fraction fraction) {
        numerator = numerator * fraction.denominator + fraction.numerator * denominator;
        denominator = denominator * fraction.denominator;
        reduce();
    }


    public void add(int n) {
        add(new Fraction(n, 1));
    }

    public void subtract(Fraction fraction) {
        numerator = numerator * fraction.denominator - fraction.numerator * denominator;
        denominator = denominator * fraction.denominator;
        reduce();
    }


    public void subtract(int n) {
        subtract(new Fraction(n, 1));
    }

    public void multiply(Fraction fraction) {
        numerator = numerator * fraction.numerator;
        denominator = denominator * fraction.denominator;
        reduce();
    }

    public void multiply(int n) {
        multiply(new Fraction(n, 1));
    }

    public void divide(Fraction fraction) {
        if (fraction.numerator == 0) {
            throw new IllegalArgumentException("Division by zero");
        }
        multiply(new Fraction(fraction.denominator, fraction.numerator));
    }

    public void divide(int n) {
        divide(new Fraction(n, 1));
    }

    public void power(int exponent) {
        numerator = (int) Math.pow(numerator, exponent);
        denominator = (int) Math.pow(denominator, exponent);
        reduce();
    }

    public Fraction sqrt() {
        if((int) Math.sqrt(numerator) * (int) Math.sqrt(numerator) == numerator &&
                (int) Math.sqrt(denominator) * (int) Math.sqrt(denominator) == denominator){
            numerator = (int) Math.sqrt(numerator);
            denominator = (int) Math.sqrt(denominator);
        }else{
            FormulaActivity.isUnderSqrt = true;
        }
        reduce();
        return new Fraction(numerator,denominator);
    }

    public void nextFraction() {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int d = sc.nextInt();
        if (d == 0) {
            throw new IllegalArgumentException("Denominator cannot be zero");
        }
        numerator = n;
        denominator = d;
        reduce();
    }

    Fraction() {}
    //
    public Fraction(int numerator, int denominator) {
        if (denominator == 0) {
            //throw new IllegalArgumentException("Denominator cannot be zero");
            return;
        }
        this.numerator = numerator;
        this.denominator = denominator;
        reduce();
    }

    public String toString(){
        return (numerator*denominator<0?"-":"")+ Math.abs(numerator)+"/"+Math.abs(denominator);
    }


    private int getGCD(int a, int b) {
        return b == 0 ? a : getGCD(b, a % b);
    }

    private void reduce(){
        int t=getGCD(numerator,denominator);
        numerator/=t;
        denominator/=t;
    }

}
