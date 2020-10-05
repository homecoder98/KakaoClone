package com.example.kakaoclone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

public class PrivacyTerms extends AppCompatActivity {
    CheckBox allCheck,firstCheck,secondCheck,thirdCheck,fourthCheck,fifthCheck;
    Button backBtn,closeBtn;
    Button button,firstButton,secondButton,thirdButton,fourthButton,fifthButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_terms);

        //비밀번호 찾기 방법 인텐트로 넘어옴
        Intent intent = getIntent();
        final String using = intent.getStringExtra("using");

        //view 인플레이션
        allCheck = (CheckBox)findViewById(R.id.allCheck);
        firstCheck = (CheckBox)findViewById(R.id.firstCheck);
        secondCheck = (CheckBox)findViewById(R.id.secondCheck);
        thirdCheck = (CheckBox)findViewById(R.id.thirdCheck);
        fourthCheck = (CheckBox)findViewById(R.id.fourthCheck);
        fifthCheck = (CheckBox)findViewById(R.id.fifthCheck);

        backBtn = (Button)findViewById(R.id.backBtn);
        closeBtn = (Button)findViewById(R.id.closeBtn);
        button = (Button)findViewById(R.id.button);
        firstButton = (Button)findViewById(R.id.firstButton);
        secondButton = (Button)findViewById(R.id.secondButton);
        thirdButton = (Button)findViewById(R.id.thirdButton);
        fourthButton = (Button)findViewById(R.id.fourthButton);
        fifthButton = (Button)findViewById(R.id.fifthButton);


        //리스너 등록
        allCheck.setOnCheckedChangeListener(onCheckChanged);
        backBtn.setOnClickListener(onclickButton);
        closeBtn.setOnClickListener(onclickButton);
        firstButton.setOnClickListener(onclickButton);
        secondButton.setOnClickListener(onclickButton);
        thirdButton.setOnClickListener(onclickButton);
        fourthButton.setOnClickListener(onclickButton);
        fifthButton.setOnClickListener(onclickButton);

        //확인 버튼 클릭
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //개인정보 동의했는지 확인
                if(firstCheck.isChecked() && secondCheck.isChecked() && thirdCheck.isChecked()
                    && fourthCheck.isChecked() && fifthCheck.isChecked()){
                    //전화번호 or 이메일로 계정 찾기 액티비티로 이동
                    switch(using){
                        case "phone":
                            startActivity(new Intent(getApplicationContext(),UsingPhone.class));
                            break;
                        case "email":
                            startActivity(new Intent(getApplicationContext(),UsingEmail.class));
                            break;
                        case "signUp":
                            startActivity(new Intent(getApplicationContext(),SignUp.class));
                            break;
                        default:
                            Toast.makeText(PrivacyTerms.this, "넘어온값: "+using+"인데 아무고토 못함", Toast.LENGTH_SHORT).show();
                            break;
                    }
                }else{
                    Toast.makeText(PrivacyTerms.this, "개인정보 수집에 동의해주세요", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    //onCreate 끝
    //모두 동의 체크박스 리스너
    CheckBox.OnCheckedChangeListener onCheckChanged = new CheckBox.OnCheckedChangeListener(){
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            if(b){
                firstCheck.setChecked(true);
                secondCheck.setChecked(true);
                thirdCheck.setChecked(true);
                fourthCheck.setChecked(true);
                fifthCheck.setChecked(true);
            }else{
                firstCheck.setChecked(false);
                secondCheck.setChecked(false);
                thirdCheck.setChecked(false);
                fourthCheck.setChecked(false);
                fifthCheck.setChecked(false);
            }
        }
    };
    //체크박스 옆 버튼 리스너
    //개인정보 포털 사이트로 넘어감(임시)
    View.OnClickListener onclickButton = new View.OnClickListener(){
        @Override
        public void onClick(View view) {
            switch(view.getId()){
                case R.id.firstButton: case R.id.secondButton: case R.id.thirdButton: case R.id.fourthButton: case R.id.fifthButton:
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.privacy.go.kr/")));
                    break;
                case R.id.backBtn:
                    finish();
                    break;
                case R.id.closeBtn:
                    Intent intent = new Intent(getApplicationContext(),Login.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    break;
            }
        }
    };
}