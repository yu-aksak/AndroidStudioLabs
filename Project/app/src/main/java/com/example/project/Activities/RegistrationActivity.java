package com.example.project.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegistrationActivity extends AppCompatActivity {
    private Button registration;
    private Button back;
    private EditText login;
    private EditText password;
    private EditText password1;
    private TextView rule;
    private boolean upperCase = false;
    private boolean number = false;
    private static final String PASSWORD_PATTERN = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%])(?=\\S+$).{8,}$";
    private static final String LOGIN_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    public static boolean isValid(String str, String regex)
    {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        registration = (Button) findViewById(R.id.registration1);
        back = (Button) findViewById(R.id.back);
        login = (EditText) findViewById(R.id.login1);
        password = (EditText) findViewById(R.id.password1);
        password1 = (EditText) findViewById(R.id.password2);
        rule = (TextView) findViewById(R.id.rule);

        View.OnClickListener registrationButtonOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Registration();
            }
        };
        registration.setOnClickListener(registrationButtonOnClickListener);

        View.OnClickListener backButtonOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegistrationActivity.this, MainActivity.class);
                startActivity(intent);
            }
        };
        back.setOnClickListener(backButtonOnClickListener);
    }

    @SuppressLint("SetTextI18n")
    private void Registration() {
        if (isValid(login.getText().toString(), LOGIN_PATTERN))
            if (isValid(password.getText().toString(), PASSWORD_PATTERN))
                if (password.getText().toString().equals(password1.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "Регистрация прошла успешно!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RegistrationActivity.this, MainActivity.class);
                    intent.putExtra("login", login.getText().toString());
                    intent.putExtra("password", password.getText().toString());
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "Пароли не совпадают!", Toast.LENGTH_SHORT).show();
                }
            else {
                Toast.makeText(getApplicationContext(), "Не правильный формат пароля!", Toast.LENGTH_SHORT).show();
                rule.setText("Пароль не должен быть короче 8 символов\n" +
                        "Вы должны использовать:\n" +
                        "Одну цифру 0-9\n" +
                        "Символ латиницы в нижнем регистре\n" +
                        "Символ латиницы в верхнем регистре\nОдин из символов  @#$%");
            }
        else {
            Toast.makeText(getApplicationContext(), "Не правильный формат логина!", Toast.LENGTH_SHORT).show();
            rule.setText("должна быть латиница\n" +
                    "Первым должен быть символ латиницы от а до z в любом регистре или цифра\n" +
                    "Далее может встречаться или “.” или “_” или “-“\n" +
                    "Далее один символ @\n" +
                    "Далее хоть один символ латиницы или цифра(или множество оных)\n" +
                    "Снова допустимые знаки без задвоения\n" +
                    "Обязательная точка, и два и более символа латиницей");
        }
    }
}