package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private EditText edtEmail;
    private EditText edtPassword;
    private EditText edtConfirmPassword;
    private TextView tvLogin;
    private Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_user);

        mAuth = FirebaseAuth.getInstance();
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.password);
        edtConfirmPassword = findViewById(R.id.confirmPassword);
        btnRegister = findViewById(R.id.btnRegister);
        tvLogin = findViewById(R.id.tvLogin);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = edtEmail.getText().toString();
                String password = edtPassword.getText().toString();
                String confirmPassword = edtConfirmPassword.getText().toString();
                if (confirmPassword.equals(password)) {
                    register(email, password);
                } else {
                    Toast.makeText(RegisterActivity.this, "Mật khẩu không trùng nhau yêu cầu nhập lại.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        tvLogin.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    private void register(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(RegisterActivity.this, "Tạo tải khoản thành công.",
                                    Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                            finish();
                        } else {
                            if (password.toString().length() <= 6) {
                                Toast.makeText(RegisterActivity.this, "Mật khẩu ít nhất 6 ký tự!",
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(RegisterActivity.this, "Email chưa chính xác!",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }
}
