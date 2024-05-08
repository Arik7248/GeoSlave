package com.example.geoslave;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.geoslave.Logic.Fraction;
import com.example.geoslave.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FormulaActivity extends AppCompatActivity {

    private EditText[] editTexts;
    private int currentEditTextIndex;
    private BottomSheetDialog dialog;
    EditText editText;
    static TextView ansTV;
    public static boolean isUnderSqrt = false;
    public static Fraction calculate(String expression) {
        List<String> postfix = shuntingYard(expression);
        //ans.setText("Postfix expression: " + postfix);
        return evaluatePostfix(postfix);
    }
    private static List<String> shuntingYard(String expression) {
        Map<String, Integer> precedence = new HashMap<>();
        precedence.put("+", 1);
        precedence.put("-", 1);
        precedence.put("*", 2);
        precedence.put("/", 2);
        precedence.put("^", 3);
        precedence.put("sqrt", 4);

        List<String> output = new ArrayList<>();
        Deque<String> operators = new ArrayDeque<>();

        // Use a regex pattern to split the expression while preserving numbers with commas
        String[] tokens = expression.split("(?<=\\d)(?=\\D)|(?<=\\D)(?=\\d)|(?<=\\W)|(?=\\W)|(?<=\\^)");

        for (String token : tokens) {
            token = token.trim(); // Trim whitespace
            if (token.isEmpty()) {
                continue;
            }
            if (token.matches("\\d+")) {
                output.add(token);
            } else if (token.equals("(")) {
                operators.push(token);
            } else if (token.equals(")")) {
                while (!operators.isEmpty() && !operators.peek().equals("(")) {
                    output.add(operators.pop());
                }
                if (operators.isEmpty() || !operators.peek().equals("(")) {
                    throw new IllegalArgumentException("Mismatched parentheses");
                }
                operators.pop(); // Discard the '('
            } else if (precedence.containsKey(token)) { // Check if token is an operator
                while (!operators.isEmpty() && precedence.getOrDefault(token, 0) <= precedence.getOrDefault(operators.peek(), 0)) {
                    output.add(operators.pop());
                }
                operators.push(token);
            } else {
                throw new IllegalArgumentException("Invalid token: " + token);
            }
            //ans.setText("Expression tokens: " + Arrays.toString(tokens));
        }

        while (!operators.isEmpty()) {
            if (operators.peek().equals("(")) {
                throw new IllegalArgumentException("Mismatched parentheses");
            }
            output.add(operators.pop());
            //ans.setText("Expression tokens: " + Arrays.toString(tokens));
        }

        return output;
    }
    private static Fraction evaluatePostfix(List<String> expression) {
        Deque<Fraction> stack = new ArrayDeque<>();

        for (String token : expression) {
            if (token.matches("\\d+")) {
                stack.push(new Fraction(Integer.parseInt(token), 1)); // Convert token to Fraction
            } else {
                if (stack.isEmpty()) {
                    Toast.makeText(ansTV.getContext(), "ERROR, IVALID DATA", Toast.LENGTH_LONG).show();
                    break;
                }
                Fraction b, a;
                switch (token) {
                    case "+":
                        b = stack.pop();
                        a = stack.pop();
                        a.add(b);
                        stack.push(a);
                        break;
                    case "-":
                        b = stack.pop();
                        a = stack.pop();
                        a.subtract(b);
                        stack.push(a);
                        break;
                    case "*":
                        b = stack.pop();
                        a = stack.pop();
                        a.multiply(b);
                        stack.push(a);
                        break;
                    case "/":
                        b = stack.pop();
                        a = stack.pop();
                        a.divide(b);
                        stack.push(a);
                        break;
                    case "^":
                        b = stack.pop();
                        a = stack.pop();
                        if (b.getDenominator() != 1 || a.getDenominator() != 1) {
                            throw new UnsupportedOperationException("Power operation (^) not supported for Fraction");
                        }
                        a.power(b.getNumerator());
                        stack.push(a);
                        break;
                    case "sqrt":
                        a = stack.pop();
                        if (a.getNumerator() < 0) {
                            throw new IllegalArgumentException("Square root of negative number");
                        }
                        a = a.sqrt();
                        int numeratorSqrt = a.getNumerator();
                        int denominatorSqrt = a.getDenominator();
                        stack.push(new Fraction(numeratorSqrt, denominatorSqrt));
                        break;
                    default:throw new IllegalArgumentException("Invalid token in postfix expression");
                }
            }
            // ans.setText("Token: " + token + ", Stack: " + stack);
        }

        if (stack.size() != 1) {
            throw new IllegalArgumentException("Invalid expression");
        }

        return stack.pop();
    }
    private static int[] AmboxjSqrt(int num){
        int mas = 1;
        for (int i = (int) Math.sqrt(num);i > 1;i--){
            if (num % (i*i) == 0){
                mas*=i;
                num/=i*i;
            }
        }
        return new int[] {mas,num};
    }
    private static boolean containsOnly2And5(int number) {
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
//    3   armat(5)
//    2   armat(7)
        // 3armat(35)/14
        if (a == 1){
            return new Object[] {"",b,c};
        }else{
            return new Object[] {a,b,c};
        }

    }
    private static void print(Fraction result){
        if (result.getDenominator() == 1){
            if (isUnderSqrt){
                if (AmboxjSqrt(result.getNumerator())[0] == 1){
                    ansTV.setText("Result: " + "√" + "("
                            + AmboxjSqrt(result.getNumerator())[1] + ")");
                }else {
                    ansTV.setText("Result: " + AmboxjSqrt(result.getNumerator())[0] + "√" + "("
                            + AmboxjSqrt(result.getNumerator())[1] + ")");
                }
                //ansTV.setText("Result: " + "√" + "(" + result.getNumerator() + ")");
            }
            else{
                ansTV.setText("Result: " + result.getNumerator());
            }
        }else{
            if (containsOnly2And5(result.getDenominator()) && !isUnderSqrt){
                if (isUnderSqrt){
                    //12
                    //5
                    ansTV.setText("Result: " + "√" + "(" +
                            (double) result.getNumerator() / result.getDenominator() + ")");

                }else{
                    ansTV.setText("Result: " +
                            (double) result.getNumerator() / result.getDenominator());
                }
            }else{
                if (isUnderSqrt){
                    Fraction amb = new Fraction(AmboxjSqrt(result.getNumerator())[0],AmboxjSqrt(result.getDenominator())[0]);
                    Fraction chamb = new Fraction(AmboxjSqrt(result.getNumerator())[1],AmboxjSqrt(result.getDenominator())[1]);
                    Object[] ans = toArmatovString(amb, chamb);
                    ansTV.setText("Result: " + ans[0] + "√" + "(" + ans[1] +
                            ")" + "/" + ans[2]);
                }else {
                    ansTV.setText("Result: " + result);
                }
            }
        }
    }
    private static String DoubleToFraction(double num){
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formula);
        Window window = getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.black));

        ansTV = findViewById(R.id.ans);
        ImageView infoBT = findViewById(R.id.InfoBT);
        infoBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String HeronURL = "https://en.wikipedia.org/wiki/Heron%27s_formula";
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(HeronURL));
                startActivity(intent);
            }
        });
        editTexts = new EditText[]{
                findViewById(R.id.editText2),
                findViewById(R.id.editText3),
                findViewById(R.id.editText4),
        };
        for (int i = 0; i < editTexts.length; i++) {
            setupEditTextListener(editTexts[i], i);
        }

        String HeronSTR = "sqrt((a + b + c)/2 * ((a + b + c)/2 - a) * " +
                "((a + b + c)/2 - b) * ((a + b + c)/2 - c))";
        //String problem = scanner.nextLine(); // Use nextLine() to read the whole line
        String[] mas = new String[3];
        String[] symb = {"a","b","c"};
        Button solveBT = findViewById(R.id.solveBT);
        solveBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String TempSTR = HeronSTR;
                for (int i = 0;i < 3;i++){
                    mas[i] = String.valueOf(editTexts[i].getText());
                    try {
                        double number = Double.valueOf(mas[i]);
                        mas[i] = DoubleToFraction(number);
                    }catch (Exception ignored){

                    }
//                    if ()
//                    if (mas[0] + mas[1] > mas[2] && mas[1] + mas[2] > mas[0] && mas[0] + mas[2] > mas[1]){
//
//                    }
                    TempSTR = TempSTR.replaceAll(symb[i], mas[i]);
                }
                Fraction result = calculate(TempSTR);
                print(result);
                isUnderSqrt = false;
            }
        });

//        dialog = new BottomSheetDialog(this);
//        createDialog();
//        editText = findViewById(R.id.editText);
//        editText.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.show();
//            }
//        });

    }
    private void createDialog(){

        View view = getLayoutInflater().inflate(R.layout.keyboard,null,false);
        Button submit = view.findViewById(R.id.submit);
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.setText(editText.getText()+"Nigger");
            }
        });
        dialog.setContentView(view);
    }
    // Функция для настройки слушателя действия редактора для EditText
    private void setupEditTextListener(final EditText editText, final int index) {
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_NEXT || (keyEvent != null && keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    if (index < editTexts.length - 1) {
                        editTexts[index + 1].requestFocus();
                    }
                    return true;
                }
                return false;
            }
        });
    }

}