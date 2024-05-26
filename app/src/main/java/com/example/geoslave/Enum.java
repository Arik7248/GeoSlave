package com.example.geoslave;

import static com.example.geoslave.Logic.Calculate.calculate;

import android.content.Context;

import com.example.geoslave.Logic.Calculate;
import com.example.geoslave.Logic.Fraction;

import java.util.ArrayList;
import java.util.List;

public class Enum {

    public enum FormulasEnum {
        Heron {
            @Override
            public String[] IsAllOk(String[] arr, Context context) {
                List<Fraction> fractions = new ArrayList<>();
                for (int i = 0; i < FormulaActivity.count; i++) {
                    Fraction result = calculate(arr[i], context);
                    if (result != null) {
                        if (!F1isPositive(result)) {
                            return new String[] {"false",String.valueOf(fractions.size()+1)+" Must To Be A Positive"};
                        }
                        fractions.add(result);
                    } else {
                        return new String[] {"false","Wrong Expression"};
                    }
                }
                if (F1orF2Value(F1addF2(fractions.get(0), fractions.get(1)), fractions.get(2)) != -1 ||
                        F1orF2Value(F1addF2(fractions.get(0), fractions.get(2)), fractions.get(1)) != -1 ||
                        F1orF2Value(F1addF2(fractions.get(1), fractions.get(2)), fractions.get(0)) != -1){
                    return new String[] {"false","Wrong Triangle"};
                }

                return new String[] {"true",""};
            }
        },
        AreaByHeightAndSide {
            @Override
            public String[] IsAllOk(String[] arr, Context context) {
                List<Fraction> fractions = new ArrayList<>();
                for (int i = 0; i < FormulaActivity.count; i++) {
                    Fraction result = calculate(arr[i], context);
                    if (result != null) {
                        if (!F1isPositive(result)) {
                            return new String[] {"false",String.valueOf(fractions.size()+1)+" Must To Be A Positive"};
                        }
                        fractions.add(result);
                    } else {
                        return new String[] {"false","Wrong Expression"};
                    }
                }
                return new String[] {"true",""};
            }
        },
        AreaByTwoSidesOneAngle {
            @Override
            public String[] IsAllOk(String[] arr, Context context) {
                List<Fraction> fractions = new ArrayList<>();
                for (int i = 0; i < FormulaActivity.count; i++) {
                    Fraction result = calculate(arr[i], context);
                    if (result != null) {
                        if (!F1isPositive(result)) {
                            return new String[] {"false",String.valueOf(fractions.size()+1)+" Must To Be A Positive"};
                        }
                        fractions.add(result);
                    } else {
                        return new String[] {"false","Wrong Expression"};
                    }
                }
                if (!F1isNotBiggerOne(calculate(arr[arr.length-1], context))){
                    String[] printArr = arr[arr.length-1].split("/");
                    return new String[] {"false",Calculate.printStr(new Fraction(Integer.parseInt(printArr[0]),Integer.parseInt(printArr[1]))) + " Is Bigger Than 1"};
                }
                return new String[] {"true",""};
            }
        },
        AreaByOneSideTwoAngles {
            @Override
            public String[] IsAllOk(String[] arr, Context context) {
                List<Fraction> fractions = new ArrayList<>();
                for (int i = 0; i < FormulaActivity.count; i++) {
                    Fraction result = calculate(arr[i], context);
                    if (result != null) {
                        if (!F1isPositive(result) && result != calculate(arr[3], context)) {
                            return new String[] {"true",""};
                        }
                        fractions.add(result);
                    } else {
                        return new String[] {"true",""};
                    }
                }
                if (!F1isNotBiggerOne(calculate(arr[1], context)) || !F1isNotBiggerOne(calculate(arr[2], context))){
                    return new String[] {"true",""};
                }
                if (!F1isNotBiggerOne(calculate(arr[3], context)) || !F1isNotLesserNegativeOne(calculate(arr[3], context))){
                    return new String[] {"true",""};
                }
                return new String[] {"true",""};
            }
        },
        AreaByIncircle {
            @Override
            public String[] IsAllOk(String[] arr, Context context) {
                List<Fraction> fractions = new ArrayList<>();
                for (int i = 0; i < FormulaActivity.count; i++) {
                    Fraction result = calculate(arr[i], context);
                    if (result != null) {
                        if (!F1isPositive(result)) {
                            return new String[] {"false",String.valueOf(fractions.size()+1)+" Must To Be A Positive"};
                        }
                        fractions.add(result);
                    } else {
                        return new String[] {"false","Wrong Expression"};
                    }
                }
                return new String[] {"true",""};
            }
        },
        AreaByExcirle {
            @Override
            public String[] IsAllOk(String[] arr, Context context) {
                return FormulasEnum.Heron.IsAllOk(arr,context);
            }
        },
        Cosines {
            @Override
            public String[] IsAllOk(String[] arr, Context context) {
                List<Fraction> fractions = new ArrayList<>();
                for (int i = 0; i < FormulaActivity.count; i++) {
                    Fraction result = calculate(arr[i], context);
                    if (result != null) {
                        if (!F1isPositive(result) && i != arr.length-1) {
                            return new String[] {"false",String.valueOf(fractions.size()+1)+" Must To Be A Positive"};
                        }
                        fractions.add(result);
                    } else {
                        return new String[] {"false","Wrong Expression"};
                    }
                }
                if (!F1isNotBiggerOne(calculate(arr[arr.length-1], context)) || !F1isNotLesserNegativeOne(calculate(arr[arr.length-1], context)) ||
                        calculate(arr[arr.length-1], context).getNumerator()/calculate(arr[arr.length-1], context).getDenominator() == 1 ||
                        calculate(arr[arr.length-1], context).getNumerator()/calculate(arr[arr.length-1], context).getDenominator() == -1){
                    String[] printArr = arr[arr.length-1].split("/");
                    return new String[] {"false",Calculate.printStr(new Fraction(Integer.parseInt(printArr[0]),Integer.parseInt(printArr[1]))) + " Is Bigger Than 1 Or Equal, Or Lesser Than -1 Or Equal"};
                }
                return new String[] {"true",""};
            }
        },
        Sines {
            @Override
            public String[] IsAllOk(String[] arr, Context context) {
                List<Fraction> fractions = new ArrayList<>();
                for (int i = 0; i < FormulaActivity.count; i++) {
                    Fraction result = calculate(arr[i], context);
                    if (result != null) {
                        if (!F1isPositive(result)) {
                            return new String[] {"false",String.valueOf(fractions.size()+1)+" Must To Be A Positive"};
                        }
                        fractions.add(result);
                    } else {
                        return new String[] {"false","Wrong Expression"};
                    }
                }
                if (!F1isNotBiggerOne(calculate(arr[1], context))){
                    String[] printArr = arr[arr.length-1].split("/");
                    return new String[] {"false",Calculate.printStr(new Fraction(Integer.parseInt(printArr[0]),Integer.parseInt(printArr[1]))) + " Is Bigger Than 1"};
                }
                return new String[] {"true",""};
            }
        },
        Height {
            @Override
            public String[] IsAllOk(String[] arr, Context context) {
                return FormulasEnum.Heron.IsAllOk(arr ,context);
            }
        },
        Bisector {
            @Override
            public String[] IsAllOk(String[] arr, Context context) {
                return FormulasEnum.Heron.IsAllOk(arr ,context);
            }
        },
        Median {
            @Override
            public String[] IsAllOk(String[] arr, Context context) {
                return FormulasEnum.Heron.IsAllOk(arr ,context);
            }
        },
        Brahmagupt {
            @Override
            public String[] IsAllOk(String[] arr, Context context) {
                List<Fraction> fractions = new ArrayList<>();
                for (int i = 0; i < FormulaActivity.count; i++) {
                    Fraction result = calculate(arr[i], context);
                    if (result != null) {
                        if (!F1isPositive(result)) {
                            return new String[] {"false",String.valueOf(fractions.size()+1)+" Must To Be A Positive"};
                        }
                        fractions.add(result);
                    } else {
                        return new String[] {"false","Wrong Expression"};
                    }
                }
                if(F1orF2Value(F1addF2(F1addF2(fractions.get(0), fractions.get(1)),fractions.get(2)),fractions.get(3)) != -1 ||
                        F1orF2Value(F1addF2(F1addF2(fractions.get(0), fractions.get(1)),fractions.get(3)),fractions.get(2)) != -1 ||
                        F1orF2Value(F1addF2(F1addF2(fractions.get(0), fractions.get(3)),fractions.get(2)),fractions.get(1)) != -1 ||
                        F1orF2Value(F1addF2(F1addF2(fractions.get(3), fractions.get(1)),fractions.get(2)),fractions.get(0)) != -1
                        ){
                    return new String[] {"false","Wrong Quadrilateral"};
                }
                return new String[] {"true",""};
            }
        },
        AreaOfATrapezoidBySides {
            @Override
            
            public String[] IsAllOk(String[] arr, Context context) {
                return FormulasEnum.Brahmagupt.IsAllOk(arr, context);
            }
        },
        AreaOfAParallelogram {
            @Override
            public String[] IsAllOk(String[] arr, Context context) {
                return FormulasEnum.AreaByTwoSidesOneAngle.IsAllOk(arr, context);
            }
        },
        AreaOfAPolygon {
            @Override
            public String[] IsAllOk(String[] arr, Context context) {
                try{
                    double n = Integer.valueOf(arr[0]);
                    if (n < 3 || n != (int) n){
                        return new String[] {"true",""};
                    }
                }catch(Exception e){
                    return new String[] {"true",""};
                }
                return FormulasEnum.Heron.IsAllOk(arr, context);
            }
        },
        PolygonRadiusOfExcircle {
            @Override
            public String[] IsAllOk(String[] arr, Context context) {
                return FormulasEnum.AreaByTwoSidesOneAngle.IsAllOk(arr, context);
            }
        },
        PolygonRadiusOfIncircle {
            @Override
            public String[] IsAllOk(String[] arr, Context context) {
                return FormulasEnum.Heron.IsAllOk(arr, context);
            }
        },
        AreaSector {
            @Override
            public String[] IsAllOk(String[] arr, Context context) {
                try{
                    Fraction degree = calculate(arr[1], context);
                    if (degree == null){return new String[] {"false","Wrong Expression"}; }
                    if (degree.getNumerator()/degree.getDenominator() > 360 || !F1isPositive(degree)){
                        String[] printArr = arr[arr.length-1].split("/");
                        return new String[] {"false", Calculate.printStr(new Fraction(Integer.parseInt(printArr[0]),Integer.parseInt(printArr[1]))) + " Is Bigger Than 360 or Lesser Than 0"};
                    }
                }catch (Exception e){
                    return new String[] {"false","Wrong Expression"};
                }
                if (!F1isPositive(calculate(arr[0],context))){
                    return new String[] {"false","1 Must Be Positive"};
                }
                return new String[] {"true",""};
            }
        },
        //String[] printArr = arr[arr.length-1].split("/");
        //Calculate.printStr(new Fraction(Integer.parseInt(printArr[0]),Integer.parseInt(printArr[1])))
        AreaSegment {
            @Override
            public String[] IsAllOk(String[] arr, Context context) {
                return new String[] {"true",""};
            }
        },
        HeightSegment {
            @Override
            public String[] IsAllOk(String[] arr, Context context) {
                List<Fraction> fractions = new ArrayList<>();
                for (int i = 0; i < FormulaActivity.count; i++) {
                    Fraction result = calculate(arr[i], context);
                    if (result != null) {
                        if (!F1isPositive(result) && i != arr.length-1) {
                            return new String[] {"false",String.valueOf(fractions.size()+1)+" Must To Be A Positive"};
                        }
                        fractions.add(result);
                    } else {
                        return new String[] {"false","Wrong Expression"};
                    }
                }
                if (!F1isNotBiggerOne(calculate(arr[arr.length-1], context)) || !F1isNotLesserNegativeOne(calculate(arr[arr.length-1], context))){
                    String[] printArr = arr[arr.length-1].split("/");
                    return new String[] {"false",Calculate.printStr(new Fraction(Integer.parseInt(printArr[0]),Integer.parseInt(printArr[1]))) + " Is Bigger Than 1 , Or Lesser Than -1 "};
                }
                return new String[] {"true",""};
            }
        };
        // Abstract method to be implemented by each constant
        public abstract String[] IsAllOk(String[] arr, Context context);
    }

    public static Fraction F1subtractF2(Fraction f1, Fraction f2) {
        int numerator = f1.getNumerator() * f2.getDenominator() - f2.getNumerator() * f1.getDenominator();
        int denominator = f1.getDenominator() * f2.getDenominator();
        return new Fraction(numerator, denominator);
    }

    public static Fraction F1addF2(Fraction f1, Fraction f2) {
        int numerator = f1.getNumerator() * f2.getDenominator() + f2.getNumerator() * f1.getDenominator();
        int denominator = f1.getDenominator() * f2.getDenominator();
        return new Fraction(numerator, denominator);
    }

    public static int F1orF2Value(Fraction f1, Fraction f2) {
        double Fvalue = (double) F1subtractF2(f1, f2).getNumerator() / F1subtractF2(f1, f2).getDenominator();
        if (Fvalue > 0) {
            return -1;
        } else if (Fvalue == 0) {
            return 0;
        } else {
            return 1;
        }
    }

    public static boolean F1isPositive(Fraction fraction) {
        return (double) fraction.getNumerator() / fraction.getDenominator() > 0;
    }
    public static boolean F1isNotBiggerOne(Fraction fraction) {
        return (double) fraction.getNumerator() / fraction.getDenominator() <= 1;
    }
    public static boolean F1isNotLesserNegativeOne(Fraction fraction) {
        return (double) fraction.getNumerator() / fraction.getDenominator() >= -1;
    }
}
