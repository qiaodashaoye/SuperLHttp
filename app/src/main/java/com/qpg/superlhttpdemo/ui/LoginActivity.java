package com.qpg.superlhttpdemo.ui;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.lifecycle.ViewModelProviders;
import com.qpg.superlhttpdemo.MainActivity;
import com.qpg.superlhttpdemo.R;
import com.qpg.superlhttpdemo.viewmodel.LoginViewModel;

public class LoginActivity extends AppCompatActivity {
    private LoginViewModel loginViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        startActivity(new Intent(LoginActivity.this, MainActivity.class));

        loginViewModel = ViewModelProviders.of(this).get(LoginViewModel.class);
        findViewById(R.id.btn_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginViewModel.login(LoginActivity.this);
                loginViewModel.login1(LoginActivity.this);
            }
        });

    }
}
