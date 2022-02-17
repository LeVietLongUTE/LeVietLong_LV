package com.example.doxethongminh;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class login extends AppCompatActivity implements View.OnClickListener {

        private TextView dangkimoi,quenmatkhau;
        EditText editemail,editpassword;
        Button btndangnhap;
        ProgressBar progressBar;
        FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        dangkimoi=findViewById(R.id.dangkinew);
        dangkimoi.setOnClickListener(this);
        quenmatkhau=findViewById(R.id.quenmatkhau);
        quenmatkhau.setOnClickListener(this);
        btndangnhap=findViewById(R.id.dangnhap);
        btndangnhap.setOnClickListener(this);
        progressBar=findViewById(R.id.progressbar);
        editemail=findViewById(R.id.email);
        editpassword=findViewById(R.id.password);
        auth=FirebaseAuth.getInstance();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.dangkinew:
                startActivity(new Intent(this,RegisterUser.class));
                break;
            case R.id.dangnhap:
                userlogin();
                break;
            case R.id.quenmatkhau:
                startActivity(new Intent(this,Quenmatkhau.class));
                break;
        }
    }

    private void userlogin() {
        String emails=editemail.getText().toString().trim();
        String passwords=editpassword.getText().toString().trim();
        if (emails.isEmpty()){
            editemail.setError("mời nhập email");
            editemail.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(emails).matches()){
            editemail.setError("vui lòng cung cấp email hợp lệ");
            editemail.requestFocus();
            return;
        }

        if (passwords.isEmpty()){
            editpassword.setError("mời nhập password");
            editpassword.requestFocus();
            return;
        }
        if (passwords.length()<6){
            editpassword.setError("mời nhập tối thiểu >6 kí tự");
            editpassword.requestFocus();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        auth.signInWithEmailAndPassword(emails,passwords).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
                    if (user.isEmailVerified()){
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(login.this,"Đăng nhập thành công",Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(login.this,MainActivity.class));
                    }else {
                        progressBar.setVisibility(View.GONE);
                        user.sendEmailVerification();
                        Toast.makeText(login.this,"kiểm tra email của bạn để xác minh tài khoản của bạn",Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(login.this,"Đăng nhập thất bại",Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            }
        });

    }
}