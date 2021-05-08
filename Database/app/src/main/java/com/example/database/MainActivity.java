package com.example.database;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.example.database.DB.DatabaseHelper;
import com.example.database.DB.SQLightHelper;
import com.example.database.Fragments.AuthorizationFragment;
import com.example.database.Fragments.ImageMusicFragment;
import com.vk.api.sdk.VK;
import com.vk.api.sdk.auth.VKAccessToken;
import com.vk.api.sdk.auth.VKAuthCallback;

import org.jetbrains.annotations.NotNull;

public class MainActivity extends AppCompatActivity {

    private static final int VK_REQUEST = 282;
    public static String vkToken;
    public static ImageMusicFragment imf = new ImageMusicFragment();
    public static DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] {
                    Manifest.permission.READ_EXTERNAL_STORAGE }, 1);
        }
        Fragment au = new AuthorizationFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.container, au).commit();

        databaseHelper = new SQLightHelper(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == VK_REQUEST) {
            VK.onActivityResult(requestCode, resultCode, data, new VKAuthCallback() {
                @Override
                public void onLogin(@NotNull VKAccessToken vkAccessToken) {

                    getSupportFragmentManager().beginTransaction().replace(R.id.container,
                            imf, "imageMusic").commit();
                    imf.setFromVK(true);
                    vkToken = vkAccessToken.getAccessToken();
                }

                @Override
                public void onLoginFailed(int i) {

                }
            });
        }
    }
}