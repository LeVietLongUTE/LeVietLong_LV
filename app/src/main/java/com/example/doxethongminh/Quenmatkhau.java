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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

    public class Quenmatkhau extends AppCompatActivity {
    Button btntaomoipass;
    EditText editemail;
    private ProgressBar progressBar;
    private FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quenmatkhau);
        progressBar=findViewById(R.id.progressbar);
        btntaomoipass=findViewById(R.id.btnnewpass);
        editemail=findViewById(R.id.txtemail);
        auth=FirebaseAuth.getInstance();
        btntaomoipass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetpassword();
            }
        });

    }
    private void resetpassword(){
        String emails=editemail.getText().toString().trim();
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
        progressBar.setVisibility(View.VISIBLE);
        auth.sendPasswordResetEmail(emails).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(Quenmatkhau.this,"Kiểm tra email của bạn để thiết lập lại mật khẩu", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(Quenmatkhau.this,login.class));
                }else {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(Quenmatkhau.this,"email không tồn tại", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}