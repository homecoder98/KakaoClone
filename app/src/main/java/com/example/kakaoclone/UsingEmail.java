package com.example.kakaoclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class UsingEmail extends AppCompatActivity {
    Button backBtn,closeBtn,button;
    EditText emailEdit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_using_email);
        //view 인플레이션
        backBtn = (Button)findViewById(R.id.backBtn);
        closeBtn = (Button)findViewById(R.id.closeBtn);
        button = (Button)findViewById(R.id.button);
        emailEdit = (EditText)findViewById(R.id.phoneNumber);

        //리스너 등록
        button.setOnClickListener(onClickListener);
        backBtn.setOnClickListener(onClickListener);
        closeBtn.setOnClickListener(onClickListener);
    }
    //버튼 리스너
    View.OnClickListener onClickListener = new View.OnClickListener(){
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.backBtn:
                    finish();
                    break;
                case R.id.closeBtn:
                    Intent intent = new Intent(getApplicationContext(),Login.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    break;
                case R.id.button:
                    sendEmail();
                    break;
                default:
                    break;
            }
        }
    };

    public void sendEmail(){
        String email = emailEdit.getText().toString();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(),"비밀번호 변경 이메일을 전송했습니다",Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(),Login.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }
                        else{
                            Toast.makeText(getApplicationContext(),"이메일 주소를 확인해주세요",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}