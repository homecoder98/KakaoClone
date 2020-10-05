package com.example.kakaoclone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    private FirebaseUser user;
    private FirebaseAuth mAuth;
    private long backPressedTime = 0;
    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //스플래시 액티비티 2초
        startActivity(new Intent(this,Splash.class));
        setContentView(R.layout.activity_main);

        //자동 로그인 여부 확인
        SharedPreferences sharedPreferences= getSharedPreferences("autoLogin", MODE_PRIVATE);
        Boolean isCheck = sharedPreferences.getBoolean("isChecked",false);
        if(!isCheck) {
            FirebaseAuth.getInstance().signOut();
        }

        //처음 스플래시 2초
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    showToast("로그인됨");
                } else {
                    startActivity(new Intent(getApplicationContext(),Login.class));
                }
            }
        }, 2000);

        textView = (TextView)findViewById(R.id.textView);



        //로그아웃 하고 끝
        findViewById(R.id.logOutBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        if(user != null){
            showProfile();
        }
        super.onStart();
    }

    public void showProfile(){
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Users");

        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String email = user.getEmail();
                for(DataSnapshot d : dataSnapshot.getChildren()){

                    //현재 로그인 계정이랑 같은 user테이블 찾기
                    if(d.child("email").getValue().toString().equals(email)){
                        for(DataSnapshot a : d.getChildren()){
                            textView.append("key: "+a.getKey()+"\nvalue: "+a.getValue().toString()+"\n\n");
                        }
//                        textView.append("email: "+d.child("email").getValue().toString()+"\n");
//                        textView.append("pw: " +d.child("pw").getValue().toString()+"\n");
//                        textView.append("phone: "+d.child("phone").getValue().toString()+"\n");
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                showToast("데이터 읽기 실패");
            }
        });
    }
    //2초이내 뒤로가기 두번 클릭시 종료
    @Override
    public void onBackPressed() {
        long curTime = System.currentTimeMillis();
        if(curTime - backPressedTime <= 2000){
            super.onBackPressed();
        }else{
            backPressedTime = curTime;
            showToast("어플리케이션을 종료합니다");
        }
    }
    //토스트 메세지
    public void showToast(String msg){
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }

}