package com.example.calculator;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity{
    private EditText login;
    private EditText password;
    private Button enter;
    private Button registration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.authorization);
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

    }

    // Обрабатываем нажатие кнопки "Войти":
    public void Login() {

        // Если введенные логин и пароль будут словом "admin",
        // показываем Toast сообщение об успешном входе:
        if (login.getText().toString().equals("admin") &&
                password.getText().toString().equals("admin")) {
            Toast.makeText(getApplicationContext(), "Вход выполнен!",Toast.LENGTH_SHORT).show();

            // Выполняем переход на другой экран:
            Intent intent = new Intent(MainActivity.this,CalculatorActivity.class);
            startActivity(intent);
        }

        // В другом случае выдаем сообщение с ошибкой:
        /*else {
            Toast.makeText(getApplicationContext(), "Неправильные данные!",Toast.LENGTH_SHORT).show();
            numberOfRemainingLoginAttempts--;

            // Делаем видимыми текстовые поля, указывающие на количество оставшихся попыток:
            attempts.setVisibility(View.VISIBLE);
            numberOfAttempts.setVisibility(View.VISIBLE);
            numberOfAttempts.setText(Integer.toString(numberOfRemainingLoginAttempts));

            // Когда выполнено 3 безуспешных попытки залогиниться,
            // делаем видимым текстовое поле с надписью, что все пропало и выставляем
            // кнопке настройку невозможности нажатия setEnabled(false):
            if (numberOfRemainingLoginAttempts == 0) {
                login.setEnabled(false);
                loginLocked.setVisibility(View.VISIBLE);
                loginLocked.setBackgroundColor(Color.RED);
                loginLocked.setText("Вход заблокирован!!!");
            }
        }*/
    }

    private void Registration(){

    }
}