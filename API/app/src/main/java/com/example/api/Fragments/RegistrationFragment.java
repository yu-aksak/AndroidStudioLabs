package com.example.api.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.api.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegistrationFragment extends Fragment {
    private EditText login;
    private EditText password;
    private EditText password1;
    private EditText name;
    private TextView rule;
    private static final String PASSWORD_PATTERN = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%])(?=\\S+$).{8,}$";
    private static final String LOGIN_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    public static boolean isValid(String str, String regex)
    {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView;
        rootView = inflater.inflate(R.layout.fragment_registration, container, false);

        Button registration = rootView.findViewById(R.id.registration1);
        Button back = rootView.findViewById(R.id.back);
        login = rootView.findViewById(R.id.login1);
        password = rootView.findViewById(R.id.password1);
        password1 = rootView.findViewById(R.id.password2);
        name = rootView.findViewById(R.id.name1);
        rule = rootView.findViewById(R.id.rule);

        View.OnClickListener registrationButtonOnClickListener = view -> Registration();
        registration.setOnClickListener(registrationButtonOnClickListener);

        View.OnClickListener backButtonOnClickListener = view -> getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, new AuthorizationFragment()).commit();
        back.setOnClickListener(backButtonOnClickListener);
        return rootView;
    }

    @SuppressLint("SetTextI18n")
    private void Registration() {
        if (isValid(login.getText().toString(), LOGIN_PATTERN))
            if (isValid(password.getText().toString(), PASSWORD_PATTERN))
                if (password.getText().toString().equals(password1.getText().toString())) {
                    if(name.getText().toString() != null) {
                        Toast.makeText(getActivity().getApplicationContext(), "Регистрация прошла успешно!", Toast.LENGTH_SHORT).show();
                        AuthorizationFragment af = new AuthorizationFragment();
                        Bundle bundle = new Bundle();
                        bundle.putString("name", name.getText().toString());
                        bundle.putString("login", login.getText().toString());
                        bundle.putString("password", password.getText().toString());
                        af.setArguments(bundle);
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, af).commit();
                    }
                    else {
                        rule.setText("");
                        Toast.makeText(getActivity().getApplicationContext(), "Введите имя!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity().getApplicationContext(), "Пароли не совпадают!", Toast.LENGTH_SHORT).show();
                }
            else {
                Toast.makeText(getActivity().getApplicationContext(), "Не правильный формат пароля!", Toast.LENGTH_SHORT).show();
                rule.setText("Пароль не должен быть короче 8 символов\n" +
                        "Вы должны использовать:\n" +
                        "Одну цифру 0-9\n" +
                        "Символ латиницы в нижнем регистре\n" +
                        "Символ латиницы в верхнем регистре\nОдин из символов  @#$%");
            }
        else {
            Toast.makeText(getActivity().getApplicationContext(), "Не правильный формат логина!", Toast.LENGTH_SHORT).show();
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