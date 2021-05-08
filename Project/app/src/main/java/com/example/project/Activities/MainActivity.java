package com.example.project.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.project.R;

public class MainActivity extends AppCompatActivity {
    private EditText login;
    private EditText password;
    private Button enter;
    private Button registration;
    private String log = "admin";
    private String pass = "admin";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        login = (EditText) findViewById(R.id.login);
        password = (EditText) findViewById(R.id.password);
        enter = (Button) findViewById(R.id.enter);
        registration = (Button) findViewById(R.id.registration);

        View.OnClickListener enterButtonOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Login();
            }
        };
        enter.setOnClickListener(enterButtonOnClickListener);

        View.OnClickListener registrationButtonOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Intent intent = new Intent(MainActivity.this,RegistrationActivity.class);
                startActivity(intent);
            }
        };
        registration.setOnClickListener(registrationButtonOnClickListener);

        Intent intent1 = getIntent();
        log = intent1.getStringExtra("login");
        pass = intent1.getStringExtra("password");
        login.setText(log);
        password.setText(pass);
        if(log == null || pass == null){
            log = "admin";
            pass = "admin";
        }
    }

    public void Login() {
        if(login.getText().toString().equals("404") && password.getText().toString().equals("404")){
            Intent browserIntent = new
                    Intent(Intent.ACTION_VIEW, Uri.parse("https://vk.com/yulia.aksak"));
            startActivity(browserIntent);
            return;
        }
        if(login.getText().toString().equals("200") && password.getText().toString().equals("200")){
            Intent browserIntent = new
                    Intent(Intent.ACTION_VIEW, Uri.parse("https://vk.com/id135927103"));
            startActivity(browserIntent);
            return;
        }
        if(login.getText().toString().equals("202") && password.getText().toString().equals("202")){
            Intent browserIntent = new
                    Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/yu-aksak/MDP"));
            startActivity(browserIntent);
            return;
        }

        if (login.getText().toString().equals(log) &&
                password.getText().toString().equals(pass)) {
            Toast.makeText(getApplicationContext(), "Вход выполнен!",Toast.LENGTH_SHORT).show();

            // Выполняем переход на другой экран:
            Intent intent = new Intent(MainActivity.this,CalculatorActivity.class);
            startActivity(intent);
        }

        // В другом случае выдаем сообщение с ошибкой:
        else {
            Toast.makeText(getApplicationContext(), "Неправильные данные!",Toast.LENGTH_SHORT).show();
        }
    }

}