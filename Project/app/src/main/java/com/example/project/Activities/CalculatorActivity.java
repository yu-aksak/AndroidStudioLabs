package com.example.project.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.project.Classes.Calculator;
import com.example.project.R;

public class CalculatorActivity extends AppCompatActivity {
    private Calculator calculator;
    private TextView text;
    private Button back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        back = (Button) findViewById(R.id.back1);
        int[] numberId = new int[] {
                R.id.buttonZero,
                R.id.buttonOne,
                R.id.buttonTwo,
                R.id.buttonThree,
                R.id.buttonFour,
                R.id.buttonFive,
                R.id.buttonSix,
                R.id.buttonSeven,
                R.id.buttonEight,
                R.id.buttonNine,
                R.id.buttonDot
        };

        int[] operationsId = new int[] {
                R.id.buttonPlus,
                R.id.buttonMinus,
                R.id.buttonMultiplication,
                R.id.buttonDivision,
                R.id.buttonResult,
                R.id.buttonClear,
                R.id.buttonCos,
                R.id.buttonSin,
                R.id.buttonBracketL,
                R.id.buttonBracketR
        };

        int[] memoryButtonsID = new int[]{
                R.id.buttonMS,
                R.id.buttonMR,
                R.id.buttonMC,
                R.id.buttonMMinus,
                R.id.buttonMPlus
        };

        text = findViewById(R.id.text);
        text.setShowSoftInputOnFocus(false);

        calculator = new Calculator();

        View.OnClickListener backButtonOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CalculatorActivity.this, MainActivity.class);
                startActivity(intent);
            }
        };
        back.setOnClickListener(backButtonOnClickListener);

        View.OnClickListener numberButtonOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view){
                calculator.onNumPressed(view.getId());
                text.setText(calculator.getText());
            }
        };

        for(int i = 0; i < numberId.length;i++){
            findViewById(numberId[i]).setOnClickListener(numberButtonOnClickListener);
        }

        View.OnClickListener numberOperationOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view){
                calculator.onActionPressed(view.getId());
                text.setText(calculator.getText());
            }
        };

        for(int i = 0; i < operationsId.length;i++){
            findViewById(operationsId[i]).setOnClickListener(numberOperationOnClickListener);
        }

        View.OnClickListener numberMemoryOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view){
                calculator.onMemoryPressed(view.getId());
                text.setText(calculator.getText());
            }
        };

        for(int i = 0; i < memoryButtonsID.length;i++){
            findViewById(memoryButtonsID[i]).setOnClickListener(numberMemoryOnClickListener);
        }
    }
}