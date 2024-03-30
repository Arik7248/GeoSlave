package com.example.geoslave;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.geoslave.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;

public class FormulaActivity extends AppCompatActivity {

    private EditText[] editTexts;
    private int currentEditTextIndex;
    private BottomSheetDialog dialog;
    EditText editText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formula);
        Window window = getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.black));

//        dialog = new BottomSheetDialog(this);
//        createDialog();
//        editText = findViewById(R.id.editText);
//        editText.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.show();
//            }
//        });
        editTexts = new EditText[]{
                findViewById(R.id.editText),
                findViewById(R.id.editText2),
                findViewById(R.id.editText3),
                findViewById(R.id.editText4),
        };
        for (int i = 0; i < editTexts.length; i++) {
            setupEditTextListener(editTexts[i], i);
        }

    }
    private void createDialog(){

        View view = getLayoutInflater().inflate(R.layout.keyboard,null,false);
        Button submit = view.findViewById(R.id.submit);
        editText = findViewById(R.id.editText);
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