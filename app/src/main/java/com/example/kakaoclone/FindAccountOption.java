package com.example.kakaoclone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class FindAccountOption extends AppCompatActivity {
    Button backBtn,closeBtn,usingPhoneBtn,usingEmailBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_account_option);

        //버튼 인플레이션
        backBtn = (Button)findViewById(R.id.backBtn);
        closeBtn = (Button)findViewById(R.id.closeBtn);
        usingPhoneBtn = (Button)findViewById(R.id.usingPhoneBtn);
        usingEmailBtn = (Button)findViewById(R.id.usingEmailBtn);

        //버튼 리스너 등록
        backBtn.setOnClickListener(onClickListener);
        closeBtn.setOnClickListener(onClickListener);
        usingPhoneBtn.setOnClickListener(onClickListener);
        usingEmailBtn.setOnClickListener(onClickListener);
    }
    //버튼 클릭 리스너
    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch(view.getId()){
                case R.id.backBtn:
                    onBackPressed();
                    break;
                case R.id.closeBtn:
                    Intent intent = new Intent(getApplicationContext(),Login.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    break;
                case R.id.usingPhoneBtn:
                    Intent phoneIntent = new Intent(getApplicationContext(),PrivacyTerms.class);
                    phoneIntent.putExtra("using","phone");
                    startActivity(phoneIntent);
                    break;
                case R.id.usingEmailBtn:
                    Intent emailIntent = new Intent(getApplicationContext(),PrivacyTerms.class);
                    emailIntent.putExtra("using","email");
                    startActivity(emailIntent);
                    break;
            }
        }
    };
}