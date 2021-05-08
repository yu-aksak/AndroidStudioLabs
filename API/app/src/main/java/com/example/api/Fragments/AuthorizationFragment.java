package com.example.api.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.api.MainActivity;
import com.example.api.R;
import com.vk.api.sdk.VK;
import com.vk.api.sdk.VKApiManager;
import com.vk.api.sdk.auth.VKAccessToken;
import com.vk.api.sdk.auth.VKAuthCallback;
import com.vk.api.sdk.auth.VKScope;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;


public class AuthorizationFragment extends Fragment {
    private static final int VK_REQUEST = 282;
    private EditText login;
    private EditText password;
    private String log = "admin";
    private String pass = "admin";
    private String name = "admin";
    private boolean role = true;

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
            log = bundle.getString("login");
            pass = bundle.getString("password");
            name = bundle.getString("name");
            role = false;
            login.setText(log);
            password.setText(pass);
        }
        return rootView;
    }

    public void Registration(){
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container,
                new RegistrationFragment()).commit();
    }

    public void Login() {
        if (login.getText().toString().equals(log) &&
                password.getText().toString().equals(pass)) {
            Toast.makeText(getActivity().getApplicationContext(), "Вход выполнен!",
                    Toast.LENGTH_SHORT).show();
            // Выполняем переход на другой фрагмент:
            ImageMusicFragment imf = new ImageMusicFragment();
            Bundle bundle = new Bundle();
            bundle.putString("name", name);
            bundle.putBoolean("role", role);
            imf.setArguments(bundle);
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container,
                    imf, "imageMusic").commit();
        }

        // В другом случае выдаем сообщение с ошибкой:
        else {
            Toast.makeText(getActivity().getApplicationContext(), "Неправильные данные!",
                    Toast.LENGTH_SHORT).show();
        }
    }

    public void vkLogInButton() {
        ArrayList<VKScope> arrayList = new ArrayList<>();
        arrayList.add(VKScope.FRIENDS);
        arrayList.add(VKScope.WALL);
        arrayList.add(VKScope.EMAIL);
        VK.login(getActivity(), arrayList);
    }

}