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
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class UsingPhone extends AppCompatActivity {
    Button backBtn,closeBtn,button;
    EditText phoneNumber;
    FirebaseUser user;
    FirebaseAuth mAuth;
    String mVerificationId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_using_phone);
        //view 인플레이션
        backBtn = (Button)findViewById(R.id.backBtn);
        closeBtn = (Button)findViewById(R.id.closeBtn);
        button = (Button)findViewById(R.id.button);
        phoneNumber = (EditText)findViewById(R.id.phoneNumber);

        //리스너 등록
        button.setOnClickListener(onClickListener);
        backBtn.setOnClickListener(onClickListener);
        closeBtn.setOnClickListener(onClickListener);

        mAuth = FirebaseAuth.getInstance();

    }
    //실제 전화번호 인증 메서드
    private void signInWithPhoneAuthCredential(final PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            user = task.getResult().getUser();

//                            Toast.makeText(UsingPhone.this, user.getPhoneNumber(), Toast.LENGTH_SHORT).show();
                            showDialog();
                        } else {
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                Toast.makeText(UsingPhone.this, "인증 실패", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
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
                    sendMsg();
                    break;
                default:
                    break;
            }
        }
    };


    //인증 문자 보내기
    public void sendMsg(){
        String phone = "+82"+phoneNumber.getText().toString();
        checkPhone(phone);
    }
    public void checkPhone(String phoneNumber){

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                    @Override
                    public void onVerificationCompleted(PhoneAuthCredential credential) {
                        Toast.makeText(getApplicationContext(), "onVerificationCompleted", Toast.LENGTH_SHORT).show();
                        signInWithPhoneAuthCredential(credential);
                    }

                    @Override
                    public void onVerificationFailed(FirebaseException e) {
                        Toast.makeText(getApplicationContext(), "전화번호와 인증번호를 확인하세요", Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onCodeSent(@NonNull String verificationId,
                                           @NonNull PhoneAuthProvider.ForceResendingToken token) {
                        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, "987654");
                        signInWithPhoneAuthCredential(credential);
                    }
                });
    }
    //인증 다이얼로그 구현
    public void showDialog(){
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("새로운 비밀번호를 입력하세요");
        dialog.setIcon(R.drawable.kakao_clone_icon_foreground);
        dialog.setMessage("새 비밀번호: ");
        final EditText editText = new EditText(getApplicationContext());
        dialog.setView(editText);
        dialog.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                user = FirebaseAuth.getInstance().getCurrentUser();
                String newPassword = editText.getText().toString();

                user.updatePassword(newPassword)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getApplicationContext(), "비밀번호가 변경되었습니다", Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(getApplicationContext(), "입력한 비밀번호를 확인하세요", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                dialogInterface.dismiss();
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(),Login.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        dialog.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        dialog.show();
    }
}