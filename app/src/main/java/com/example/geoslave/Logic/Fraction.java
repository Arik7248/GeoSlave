package com.example.geoslave.Logic;

import java.util.Scanner;
public class Fraction {
    private int numerator;
    private int denominator=1;
    public void add(Fraction fraction) {
        numerator = numerator * fraction.denominator + fraction.numerator * denominator;
        denominator = denominator * fraction.denominator;
        reduce();
    }
    public void add(int n) {
        add(new Fraction(n, 1));
    }
    public void substract(Fraction fraction) {
        numerator = numerator * fraction.denominator - fraction.numerator * denominator;
        denominator = denominator * fraction.denominator;
        reduce();
    }
    public void substract(int n) {
        substract(new Fraction(n, 1));
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
            System.out.println("На эту дробь делить нельзя!");
            return;
        }
        multiply(new Fraction(fraction.denominator, fraction.numerator));
    }
    public void divide(int n) {
        divide(new Fraction(n, 1));
    }
    public void nextFraction() {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int d = sc.nextInt();
        if (d == 0) {
            System.out.println("Знаменатель не может быть нулевым!");
            return;
        }
        numerator=n;
        denominator=d;
        reduce();
    }
    Fraction(){
    }
    Fraction(int numerator, int denominator) {
        if (denominator == 0) {
            System.out.println("Знаменатель не может быть нулевым!");
            return;
        }
        this.numerator = numerator;
        this.denominator = denominator;
        reduce();
    }
    public String toString(){
        return (numerator*denominator<0?"-":" ")+ Math.abs(numerator)+"/"+Math.abs(denominator);
    }
    public void print() {
        if(numerator % denominator == 0){
            System.out.println(numerator/denominator);
            return;
        }
        if (numerator / denominator == 0) {
            System.out.println(" " + Math.abs(numerator));
            System.out.println((numerator*denominator<0?"-":" ")+" ---- или "+ 1.0 * numerator / denominator);
            System.out.println(" " + Math.abs(denominator));
        } else {
            System.out.println(" " + Math.abs(numerator % denominator));
            System.out.println((numerator*denominator<0?"-":"")+numerator / denominator + "---- или"+1.0 * numerator / denominator);
            System.out.println(" " + Math.abs(denominator));
        }
    }
    private int getGCD(int a, int b) { return b==0 ? a : getGCD(b, a%b); }
    private void reduce(){
        int t=getGCD(numerator,denominator);
        numerator/=t;
        denominator/=t;
    }
}