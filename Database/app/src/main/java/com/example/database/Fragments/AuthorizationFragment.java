package com.example.database.Fragments;

import android.os.Bundle;

import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.database.DB.FirebaseHelper;
import com.example.database.DB.SQLightHelper;
import com.example.database.MainActivity;
import com.example.database.R;
import com.vk.api.sdk.VK;
import com.vk.api.sdk.auth.VKScope;

import java.util.ArrayList;


public class AuthorizationFragment extends Fragment {
    private EditText login;
    private EditText password;
    private SwitchCompat database;

    private String name = "admin";
    private boolean role = true;

    public AuthorizationFragment(){
        MainActivity.databaseHelper = new SQLightHelper(getContext());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView =
                inflater.inflate(R.layout.fragment_authorization, container, false);

        login = rootView.findViewById(R.id.login);
        password = rootView.findViewById(R.id.password);
        database = rootView.findViewById(R.id.databaseSwitch);

        database.setChecked(false);
        database.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked) {
                MainActivity.databaseHelper = new FirebaseHelper();
            }
            else
                MainActivity.databaseHelper = new SQLightHelper(getContext());
            });

        Button enter = rootView.findViewById(R.id.enter);
        Button registration = rootView.findViewById(R.id.registration);
        Button vk = rootView.findViewById(R.id.enterVK);

        View.OnClickListener enterButtonOnClickListener = view -> Login();
        enter.setOnClickListener(enterButtonOnClickListener);

        View.OnClickListener registrationButtonOnClickListener = view -> Registration();
        registration.setOnClickListener(registrationButtonOnClickListener);

        View.OnClickListener vkButtonOnClickListener = view ->
                vkLogInButton();
        vk.setOnClickListener(vkButtonOnClickListener);

        Bundle bundle = getArguments();
        if (bundle != null) {
            String log = bundle.getString("login");
            String pass = bundle.getString("password");
            name = bundle.getString("name");
            role = false;
            login.setText(log);
            password.setText(pass);
        }
        return rootView;
    }

    public void Registration(){
        requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container,
                new RegistrationFragment()).commit();
    }

    public void Login() {
        if(!login.getText().toString().equals("") && !password.getText().toString().equals("")) {
            if (!MainActivity.databaseHelper.readUser(login.getText().toString(),
                    password.getText().toString()).equals("-1")) {
                Toast.makeText(requireActivity().getApplicationContext(), "Вход выполнен!",
                        Toast.LENGTH_SHORT).show();
                // Выполняем переход на другой фрагмент:
                ImageMusicFragment imf = new ImageMusicFragment();
                Bundle bundle = new Bundle();
                bundle.putString("name", name);
                bundle.putBoolean("role", role);
                imf.setArguments(bundle);
                requireActivity().getSupportFragmentManager().beginTransaction().
                        replace(R.id.container, imf, "imageMusic").commit();
            }

            // В другом случае выдаем сообщение с ошибкой:
            else {
                Toast.makeText(requireActivity().getApplicationContext(),
                        "Неправильные данные!", Toast.LENGTH_SHORT).show();
            }
        }
        else
            Toast.makeText(requireActivity().getApplicationContext(), "Данные не введены",
                    Toast.LENGTH_SHORT).show();
    }

    public void vkLogInButton() {
        ArrayList<VKScope> arrayList = new ArrayList<>();
        arrayList.add(VKScope.FRIENDS);
        arrayList.add(VKScope.WALL);
        arrayList.add(VKScope.EMAIL);
        VK.login(requireActivity(), arrayList);
    }

}