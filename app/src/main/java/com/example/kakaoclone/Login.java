package com.example.kakaoclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.UserProfileChangeRequest;

public class Login extends AppCompatActivity {
    EditText emailEditText,passwordEditText;
    Button loginBtn,findPasswordBtn,signUpBtn;
    CheckBox loginCheckBox;
    private long backPressedTime = 0;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //view 인플레이션
        emailEditText = (EditText)findViewById(R.id.emailEditText);
        passwordEditText = (EditText)findViewById(R.id.passwordEditText);
        loginCheckBox = (CheckBox)findViewById(R.id.loginCheckBox);
        findPasswordBtn = (Button)findViewById(R.id.findPasswordBtn);
        loginBtn = (Button)findViewById(R.id.loginBtn);
        signUpBtn = (Button)findViewById(R.id.signUpBtn);

        loginBtn.setOnClickListener(onClickListener);
        findPasswordBtn.setOnClickListener(onClickListener);
        signUpBtn.setOnClickListener(onClickListener);

        mAuth = FirebaseAuth.getInstance();
    }
    //버튼 클릭 리스너
    View.OnClickListener onClickListener = new View.OnClickListener(){
        @Override
        public void onClick(View view) {
            switch(view.getId()){
                case R.id.loginBtn:
                    login();
                    break;
                case R.id.findPasswordBtn:
                    startActivity(new Intent(getApplicationContext(),FindAccountOption.class));
                    break;
                case R.id.signUpBtn:
                    Intent intent =new Intent(getApplicationContext(),PrivacyTerms.class);
                    intent.putExtra("using","signUp");
                    startActivity(intent);
                    break;
            }
        }
    };
    public void login(){
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        //이메일,비밀번호 적었는지 확인
        if(email.length() > 0 && password.length() > 0){
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                //자동 로그인 여부 저장
                                SharedPreferences sharedPreferences= getSharedPreferences("autoLogin", MODE_PRIVATE);
                                SharedPreferences.Editor editor= sharedPreferences.edit();
                                editor.putBoolean("isChecked",loginCheckBox.isChecked());
                                editor.commit();
                                //로그인
                                FirebaseUser user = mAuth.getCurrentUser();
                                finish();
                            } else {
                                Toast.makeText(getApplicationContext(),"이메일 또는 비밀번호를 확인해주세요",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }else{
            Toast.makeText(getApplicationContext(),"이메일 또는 비밀번호를 확인해주세요",Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onBackPressed() {
        long curTime = System.currentTimeMillis();
        if(curTime - backPressedTime <= 2000){
            moveTaskToBack(true);
            finishAndRemoveTask();
            android.os.Process.killProcess(android.os.Process.myPid());
            super.onBackPressed();
        }else{
            backPressedTime = curTime;
            Toast.makeText(this,"어플리케이션을 종료합니다",Toast.LENGTH_SHORT).show();
        }

    }
}