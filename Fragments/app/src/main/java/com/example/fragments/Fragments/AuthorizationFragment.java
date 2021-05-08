package com.example.fragments.Fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.fragments.R;

public class AuthorizationFragment extends Fragment {
    private EditText login;
    private EditText password;
    private Button enter;
    private Button registration;
    private String log = "admin";
    private String pass = "admin";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView =
                inflater.inflate(R.layout.fragment_authorization, container, false);

        login = (EditText) rootView.findViewById(R.id.login);
        password = (EditText) rootView.findViewById(R.id.password);
        enter = (Button) rootView.findViewById(R.id.enter);
        registration = (Button) rootView.findViewById(R.id.registration);

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
                Registration();
            }
        };
        registration.setOnClickListener(registrationButtonOnClickListener);

        Bundle bundle = new Bundle();
        bundle = getArguments();
        if (bundle != null) {
            log = bundle.getString("login");
            pass = bundle.getString("password");
            login.setText(log);
            password.setText(pass);
        }
        else{
            log = "admin";
            pass = "admin";
        }
        return rootView;
    }

    public void Registration(){
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, new RegistrationFragment()).commit();
    }

    public void Login() {
        if (login.getText().toString().equals(log) &&
                password.getText().toString().equals(pass)) {
            Toast.makeText(getActivity().getApplicationContext(), "Вход выполнен!", Toast.LENGTH_SHORT).show();

            // Выполняем переход на другой фрагмент:
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, new ImageMusicFragment()).commit();
        }

        // В другом случае выдаем сообщение с ошибкой:
        else {
            Toast.makeText(getActivity().getApplicationContext(), "Неправильные данные!", Toast.LENGTH_SHORT).show();
        }
    }
}