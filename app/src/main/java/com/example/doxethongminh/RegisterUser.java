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

import com.example.doxethongminh.Model.User;
import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Pattern;

public class RegisterUser extends AppCompatActivity implements View.OnClickListener {
    TextView banner;
    Button btndangki;
    EditText username, email, password;
    private ProgressBar progressBar;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);
        auth = FirebaseAuth.getInstance();

        banner = findViewById(R.id.banner);
        banner.setOnClickListener(this);
        btndangki = findViewById(R.id.dangki);
        btndangki.setOnClickListener(this);
        username = findViewById(R.id.username);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        progressBar = findViewById(R.id.progressbar);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.banner:
                startActivity(new Intent(this, login.class));
                break;
            case R.id.dangki:
                dangkimoi();

        }
    }

    private void dangkimoi() {
        String usernames = username.getText().toString().trim();
        String emails = email.getText().toString().trim();
        String passwords = password.getText().toString().trim();
        if (usernames.isEmpty()) {
            username.setError("m???i nh???p username");
            username.requestFocus();
            return;
        }
        if (emails.isEmpty()) {
            email.setError("m???i nh???p email");
            email.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(emails).matches()) {
            email.setError("vui l??ng cung c???p email h???p l???");
            email.requestFocus();
            return;
        }
        if (passwords.isEmpty()) {
            password.setError("m???i nh???p password");
            password.requestFocus();
            return;
        }
        if (passwords.length() < 6) {
            password.setError("m???i nh???p t???i thi???u >6 k?? t???");
            password.requestFocus();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        auth.createUserWithEmailAndPassword(emails, passwords).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    User user = new User(usernames, emails);
                    FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance()
                                        .getCurrentUser().getUid()).child("cuocphi").child("phitheongay").setValue("2000");
                                FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance()
                                        .getCurrentUser().getUid()).child("tongxera").setValue("0");
                                FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance()
                                        .getCurrentUser().getUid()).child("tongcuocphi").setValue("0");
                                Toast.makeText(RegisterUser.this, "????ng k?? th??nh c??ng", Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                                startActivity(new Intent(RegisterUser.this, login.class));
                            } else {
                                Toast.makeText(RegisterUser.this, "????ng k?? th???t b???i", Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                            }
                        }
                    });
                }
            }
        });
    }
}