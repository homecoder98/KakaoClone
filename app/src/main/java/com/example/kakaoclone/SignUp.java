package com.example.kakaoclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignUp extends AppCompatActivity {
    EditText emailEditText,passwordEditText,passwordCheckEditText;
    Button signUpBtn,closeBtn,backBtn;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //view 인플레이션
        emailEditText = (EditText)findViewById(R.id.emailEditText);
        passwordEditText = (EditText)findViewById(R.id.passwordEditText);
        passwordCheckEditText = (EditText)findViewById(R.id.passwordCheckEditText);
        signUpBtn = (Button)findViewById(R.id.signUpBtn);
        backBtn = (Button)findViewById(R.id.backBtn);
        closeBtn = (Button)findViewById(R.id.closeBtn);

        //리스너 등록
        backBtn.setOnClickListener(onClickListener);
        closeBtn.setOnClickListener(onClickListener);
        signUpBtn.setOnClickListener(onClickListener);

        mAuth = FirebaseAuth.getInstance();
    }
    //버튼 클릭 리스너
    View.OnClickListener onClickListener = new View.OnClickListener(){
        @Override
        public void onClick(View view) {
            switch(view.getId()){
                case R.id.backBtn:
                    finish();
                    break;
                case R.id.closeBtn:
                    Intent intent =new Intent(getApplicationContext(),Login.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    break;
                case R.id.signUpBtn:
                    signUp();
                    break;
                default:
                    break;
            }
        }
    };
    class User{
        String email,pw;
        User(String email,String pw){
            this.email = email;
            this.pw = pw;
        }
    }
    public void signUp(){
        final String email = emailEditText.getText().toString();
        final String password = passwordEditText.getText().toString();
        String passwordCheck = passwordCheckEditText.getText().toString();
        //공백 확인
        if(email.length() > 0 && password.length() > 0 && passwordCheck.length() > 0){
            //비밀번호,비밀번호 확인 같은지 확인
            if(!password.equals(passwordCheck)){
                Toast.makeText(getApplicationContext(), "비밀번호가 일치하지않습니다", Toast.LENGTH_SHORT).show();
            }else{

                    //회원가입
                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(getApplicationContext(), "계정이 생성되었습니다", Toast.LENGTH_SHORT).show();
                                        //realtime database 저장
                                        Log.d("test","db저장 시작");
                                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                                        final DatabaseReference myRef = database.getReference("Users");
                                        myRef.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                Log.d("test","db읽기 시작");
                                                int num = (int)dataSnapshot.getChildrenCount();
                                                Toast.makeText(SignUp.this, num, Toast.LENGTH_SHORT).show();
                                                Log.d("test",""+num);
//                                                myRef.setValue("User_"+num);
//                                                User user = new User(email,password);
//                                                myRef.child("User_"+num).setValue(user);
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError error) {
                                                Log.d("test","db저장 실패");
                                                Toast.makeText(SignUp.this, "db에 저장 실패", Toast.LENGTH_SHORT).show();
                                            }
                                        });


                                        Log.d("test","액티비티 실행 시작");
                                        Intent intent = new Intent(getApplicationContext(), Login.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(intent);
                                    } else {
                                        Toast.makeText(getApplicationContext(), "아이디 또는 비밀번호를 확인해주세요", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                    }
        }else{
            Toast.makeText(getApplicationContext(), "이메일 또는 비밀번호를 입력하세요", Toast.LENGTH_SHORT).show();
        }
    }
}