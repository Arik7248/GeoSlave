package com.example.geoslave;

import static com.example.geoslave.Logic.Calculate.DoubleToFraction;
import static com.example.geoslave.Logic.Calculate.calculate;
import static com.example.geoslave.Logic.Calculate.printStr;
import static com.example.geoslave.Logic.NetworkUtil.CheckNetwork;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.geoslave.Logic.Fraction;
import com.example.geoslave.Logic.NetworkUtil;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.Objects;

public class FormulaActivity extends AppCompatActivity {

    private EditText[] editTexts;
    private LinearLayout[] linearLayouts;
    private TextView[] textViews;
    public static int count = 0;
    private BottomSheetDialog dialog;
    EditText editText;
    private Activity activity = this;
    public static boolean isUnderSqrt = false;
    String[] IsItOk = {"",""};
    String Expression;
    String[] mas,symb;
    public static boolean RightSqrt = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formula);
        Window window = getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.black));

        ImageView image = findViewById(R.id.FormulaImage);
        TextView name = findViewById(R.id.Name);
        ImageView formImage = findViewById(R.id.FormulaFormula);
        name.setText(getIntent().getStringExtra("MyFormulaName" ));
        image.setImageResource(getIntent().getIntExtra("MyFormulaImage",R.drawable.answer_background));
        formImage.setImageResource(getIntent().getIntExtra("MyFormulaFormulaImage",R.drawable.heronform));

        ImageView infoBT = findViewById(R.id.InfoBT);
        infoBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckNetwork(getApplicationContext(), activity);
                String URL = getIntent().getStringExtra("MyFormulaURL");
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(URL));
                startActivity(intent);
            }
        });
        editTexts = new EditText[]{
                findViewById(R.id.editText1),
                findViewById(R.id.editText2),
                findViewById(R.id.editText3),
                findViewById(R.id.editText4),
                findViewById(R.id.editText5)
        };
        linearLayouts = new LinearLayout[]{
                findViewById(R.id.linear1),
                findViewById(R.id.linear2),
                findViewById(R.id.linear3),
                findViewById(R.id.linear4),
                findViewById(R.id.linear5)
        };
        textViews = new TextView[]{
                findViewById(R.id.textView1),
                findViewById(R.id.textView2),
                findViewById(R.id.textView3),
                findViewById(R.id.textView4),
                findViewById(R.id.textView5)
        };
        //
        Enum.FormulasEnum type = Enum.FormulasEnum.valueOf(getIntent().getStringExtra("MyFormulaType"));
        SetExpressionName(type);
        //w
        int widthwithdp = 50;
        int widthwithpx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,widthwithdp,getResources().getDisplayMetrics());
        //w
        count = symb.length;
        mas = new String[count];
        for (int i = 0; i < count; i++) {
            setupEditTextListener(editTexts[i], i);
            textViews[i].setText(symb[i]+" = ");
            if (symb[i].length() == 1 || symb[i].length() == 2){
                ViewGroup.LayoutParams params = textViews[i].getLayoutParams();
                params.width = widthwithpx;
                textViews[i].setLayoutParams(params);
            }
        }
        for (int i = 4; i >= count;i--){
            linearLayouts[i].setVisibility(View.GONE);
        }
        SetExpressionValue(type);

        //Heron

        Button solveBT = findViewById(R.id.solveBT);
        TextView ansTV = findViewById(R.id.ans);
        solveBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckNetwork(getApplicationContext(), activity);

                String TempSTR = Expression;
                for (int i = 0;i < count;i++){
                    mas[i] = String.valueOf(editTexts[i].getText());
                    try {
                        if (!mas[i].contains(".") && mas[i].matches("^-?0\\d*$")) {
                            throw new Exception("Invalid input format: " + mas[i]);
                        }

                        // Check if the input represents a decimal (contains a decimal point)
                        if (mas[i].contains(".")) {
                            // Split the input into integer and decimal parts
                            String[] parts = mas[i].split("\\.");
                            // Check if the integer part has leading zeros
                            if (parts[0].matches("^-?0\\d*$")) {
                                throw new Exception("Invalid input format: " + mas[i]);
                            }
                        }
                        double number = Double.parseDouble(mas[i]);
                        mas[i] = DoubleToFraction(number);
                    }catch (Exception ignored){
                        if (calculate(mas[i], getApplicationContext()) != null){
                            Fraction temp = calculate(mas[i], getApplicationContext());
                            mas[i] = temp.toString();
                        }else{
                            if (RightSqrt) {
                                Toast.makeText(FormulaActivity.this, "Invalid data", Toast.LENGTH_SHORT).show();
                            }
                            if (!RightSqrt){
                                Toast.makeText(FormulaActivity.this, "Sqrt(x) should be integer", Toast.LENGTH_SHORT).show();
                            }
                            return;
                        }
                    }
                    TempSTR = TempSTR.replaceAll(symb[i], mas[i]);
                }//(5/2 * 2/1)/2
                SetIsItOk(type);
                if(Objects.equals(IsItOk[0], "true")){
                    if (calculate(TempSTR, getApplicationContext()) != null){
                        Fraction result = calculate(TempSTR, getApplicationContext());
                        if(printStr(result).contains("-") || printStr(result).equals("Result: 0")) {
                            Toast.makeText(FormulaActivity.this, "Not Correct Data", Toast.LENGTH_SHORT).show();
                        }else{
                            if (type == Enum.FormulasEnum.AreaSector){
                                String[] strarr = printStr(result).split(" ");
                                try {
                                    ansTV.setText(strarr[0]+" π"+strarr[1]);
                                }
                                catch (Exception e){
                                    Toast.makeText(FormulaActivity.this, "Something is Wrong", Toast.LENGTH_SHORT).show();
                                }
                            }else{
                                ansTV.setText(printStr(result));
                            }

                        }
                        isUnderSqrt = false;
                    }else{
                        Toast.makeText(FormulaActivity.this, "Something is Wrong", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(FormulaActivity.this, IsItOk[1], Toast.LENGTH_SHORT).show();
                }

            }
        });


    }

    private void SetExpressionValue(Enum.FormulasEnum type) {
        switch (type){
            case Heron:
                symb = new String[]{"a", "b", "c"};
                Expression = "sqrt((a + b + c)/2 * ((a + b + c)/2 - a) * " +
                        "((a + b + c)/2 - b) * ((a + b + c)/2 - c))";
                break;
            case AreaByHeightAndSide:
                symb = new String[]{"h", "c"};
                Expression ="(h * c)/2";
                break;
            case AreaByTwoSidesOneAngle:
                symb = new String[]{"b", "c", "α"};
                Expression ="(α * c * b)/2";
                break;
            case AreaByOneSideTwoAngles:
                symb = new String[]{"c", "α", "β", "γ"};
                Expression ="(c * c)/2 * (α * β)/γ";
                break;
            case AreaByIncircle:
                symb = new String[]{"P", "r"};
                Expression ="(P * r)/2";
                break;
            case AreaByExcirle:
                symb = new String[]{"a", "b", "c", "R"};
                Expression ="(a * b * c)/(4 * R)";
                break;
            case Cosines:
                symb = new String[]{"b", "c", "α"};
                Expression ="sqrt(b * b + c * c - 2 * b * c * (α))";
                break;
            case Sines:
                symb = new String[]{"a", "α"};
                Expression ="a/(2 * α)";
                break;
            case Height:
                symb = new String[]{"a", "b", "c"};
                Expression = "2/c * "+"sqrt((a + b + c)/2 * ((a + b + c)/2 - a) * " +
                        "((a + b + c)/2 - b) * ((a + b + c)/2 - c))";
                break;
            case Bisector:
                symb = new String[]{"a", "b", "c"};
                Expression ="sqrt(a * b * (a + b + c) * (a + b - c))/(a + b)";
                break;
            case Median:
                symb = new String[]{"a", "b", "c"};
                Expression ="sqrt(2 * a * a + 2 * b * b - c * c)/2";
                break;
            case Brahmagupt:
                symb = new String[]{"a", "b", "c", "d"};
                Expression ="sqrt(((a + b + c)/2 - a) * " +
                        "((a + b + c)/2 - b) * ((a + b + c)/2 - c) * ((a + b + c)/2 - d)) ";
                break;
            case AreaOfATrapezoidBySides:
                symb = new String[]{"a", "b", "c", "d"};
                Expression ="(a + b)/2 * sqrt(c * c - (((a - b) * (a - b) + c * c - d * d)/(2 * (a - b))) * "
                        +"(((a - b) * (a - b) + c * c - d * d)/(2 * (a - b))))";
                break;
            case AreaOfAParallelogram:
                symb = new String[]{"a", "b", "α"};
                Expression ="a * b * α";
                break;
            case AreaOfAPolygon:
                symb = new String[]{"n", "g", "a"};
                Expression ="(n * a * a)/(4 * g)";
                break;
            case PolygonRadiusOfExcircle:
                symb = new String[]{"α", "a"};
                Expression ="a/(2 * α)";
                break;
            case PolygonRadiusOfIncircle:
                symb = new String[]{"α", "a"};
                Expression ="a/(2 * α)";
                break;
            case AreaSector://π
                symb = new String[]{"R", "α"};
                Expression ="(R * R) * (α/360)";
                break;
            case AreaSegment://π
                symb = new String[]{"R", "α", "sin(α)"};
                Expression ="";
                break;
            case HeightSegment://π
                symb = new String[]{"R", "α"};
                Expression ="R * (1 - α)";
                break;

        }
    }

    private void SetExpressionName(Enum.FormulasEnum type) {
        switch (type){
            case Heron:
                symb = new String[]{"a", "b", "c"};
                Expression = "sqrt((a + b + c)/2 * ((a + b + c)/2 - a) * " +
                        "((a + b + c)/2 - b) * ((a + b + c)/2 - c))";
                break;
            case AreaByHeightAndSide:
                symb = new String[]{"h", "c"};
                Expression ="(h * c)/2";
                break;
            case AreaByTwoSidesOneAngle:
                symb = new String[]{"b", "c", "sin(α)"};
                Expression ="(α * c * b)/2";
                break;
            case AreaByOneSideTwoAngles:
                symb = new String[]{"c", "sin(α)", "sin(β)", "sin(α+β)"};
                Expression ="(c * c)/2 * (α * β)/γ";
                break;
            case AreaByIncircle:
                symb = new String[]{"p", "r"};
                Expression ="(P * r)/2";
                break;
            case AreaByExcirle:
                symb = new String[]{"a", "b", "c", "R"};
                Expression ="(a * b * c)/(4 * R)";
                break;
            case Cosines:
                symb = new String[]{"b", "c", "cos(α)"};
                Expression ="sqrt(b * b + c * c - 2 * b * c * α)";
                break;
            case Sines:
                symb = new String[]{"a", "sin(α)"};
                Expression ="a/(2 * α)";
                break;
            case Height:
                symb = new String[]{"a", "b", "c"};
                Expression = "2/c * "+"sqrt((a + b + c)/2 * ((a + b + c)/2 - a) * " +
                        "((a + b + c)/2 - b) * ((a + b + c)/2 - c))";
                break;
            case Bisector:
                symb = new String[]{"a", "b", "c"};
                Expression ="sqrt(a * b * (a + b + c) * (a + b - c))/(a + b)";
                break;
            case Median:
                symb = new String[]{"a", "b", "c"};
                Expression ="sqrt(2 * a * a + 2 * b * b - c * c)/2";
                break;
            case Brahmagupt:
                symb = new String[]{"a", "b", "c", "d"};
                Expression ="sqrt(((a + b + c)/2 - a) * " +
                        "((a + b + c)/2 - b) * ((a + b + c)/2 - c) * ((a + b + c)/2 - d)) ";
                break;
            case AreaOfATrapezoidBySides:
                symb = new String[]{"a", "b", "c", "d"};
                Expression ="(a + b)/2 * sqrt(c * c - (((a - b) * (a - b) + c * c - d * d)/(2 * (a - b))) * "
                        +"(((a - b) * (a - b) + c * c - d * d)/(2 * (a - b)))";
                break;
            case AreaOfAParallelogram:
                symb = new String[]{"a", "b", "sin(α)"};
                Expression ="a * b * α";
                break;
            case AreaOfAPolygon:
                symb = new String[]{"n", "tg(180°/n)", "a"};
                Expression ="(n * a * a)/(4 * tg)";
                break;
            case PolygonRadiusOfExcircle:
                symb = new String[]{"sin(180°/n)", "a"};
                Expression ="a/(2 * α)";
                break;
            case PolygonRadiusOfIncircle:
                symb = new String[]{"tg(180°/n)", "a"};
                Expression ="a/(2 * α)";
                break;
            case AreaSector://π
                symb = new String[]{"R", "α°"};
                Expression ="(R * R) * (α/360)";
                break;
            case AreaSegment://π
                symb = new String[]{"R", "α", "sin(α)"};
                Expression ="";
                break;
            case HeightSegment://π
                symb = new String[]{"R", "cos(α/2)"};
                Expression ="R * (1 - α)";
                break;

        }
    }

    private void SetIsItOk(Enum.FormulasEnum type){
        switch (type){
            case Heron:
                IsItOk = Enum.FormulasEnum.Heron.IsAllOk(mas,getApplicationContext());
                break;
            case AreaByHeightAndSide:
                IsItOk = Enum.FormulasEnum.AreaByHeightAndSide.IsAllOk(mas,getApplicationContext());
                break;
            case AreaByTwoSidesOneAngle:
                IsItOk = Enum.FormulasEnum.AreaByTwoSidesOneAngle.IsAllOk(mas,getApplicationContext());
                break;
            case AreaByOneSideTwoAngles:
                IsItOk = Enum.FormulasEnum.AreaByOneSideTwoAngles.IsAllOk(mas,getApplicationContext());
                break;
            case AreaByIncircle:
                IsItOk = Enum.FormulasEnum.AreaByIncircle.IsAllOk(mas,getApplicationContext());
                break;
            case AreaByExcirle:
                IsItOk = Enum.FormulasEnum.AreaByExcirle.IsAllOk(mas,getApplicationContext());
                break;
            case Cosines:
                IsItOk = Enum.FormulasEnum.Cosines.IsAllOk(mas,getApplicationContext());
                break;
            case Sines:
                IsItOk = Enum.FormulasEnum.Sines.IsAllOk(mas,getApplicationContext());
                break;
            case Height:
                IsItOk = Enum.FormulasEnum.Height.IsAllOk(mas,getApplicationContext());
                break;
            case Bisector:
                IsItOk = Enum.FormulasEnum.Bisector.IsAllOk(mas,getApplicationContext());
                break;
            case Median:
                IsItOk = Enum.FormulasEnum.Median.IsAllOk(mas,getApplicationContext());
                break;
            case Brahmagupt:
                IsItOk = Enum.FormulasEnum.Brahmagupt.IsAllOk(mas,getApplicationContext());
                break;
            case AreaOfATrapezoidBySides:
                IsItOk = Enum.FormulasEnum.AreaOfATrapezoidBySides.IsAllOk(mas,getApplicationContext());
                break;
            case AreaOfAParallelogram:
                IsItOk = Enum.FormulasEnum.AreaOfAParallelogram.IsAllOk(mas,getApplicationContext());
                break;
            case AreaOfAPolygon:
                IsItOk = Enum.FormulasEnum.AreaOfAPolygon.IsAllOk(mas,getApplicationContext());
                break;
            case PolygonRadiusOfExcircle:
                IsItOk = Enum.FormulasEnum.PolygonRadiusOfExcircle.IsAllOk(mas,getApplicationContext());
                break;
            case PolygonRadiusOfIncircle:
                IsItOk = Enum.FormulasEnum.PolygonRadiusOfIncircle.IsAllOk(mas,getApplicationContext());
                break;
            case AreaSector:
                IsItOk = Enum.FormulasEnum.AreaSector.IsAllOk(mas,getApplicationContext());
                break;
            case AreaSegment:
                IsItOk = Enum.FormulasEnum.AreaSegment.IsAllOk(mas,getApplicationContext());
                break;
            case HeightSegment:
                IsItOk = Enum.FormulasEnum.HeightSegment.IsAllOk(mas,getApplicationContext());
                break;
        }
    }
    public void createDialog(){

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