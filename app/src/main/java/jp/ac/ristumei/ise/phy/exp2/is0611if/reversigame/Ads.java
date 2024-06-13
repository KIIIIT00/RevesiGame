package jp.ac.ristumei.ise.phy.exp2.is0611if.reversigame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;

public class Ads extends AppCompatActivity {
    Button adsfin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ads);
        adsfin = (Button)findViewById(R.id.adfinish);
        adsfin.setVisibility(View.INVISIBLE);

        //カウントダウンタイマーの設定
        CountDownTimer countDownTimer = new CountDownTimer(10000, 100) {
            @Override
            public void onTick(long millisUntilFinished) {
                int time = (int)millisUntilFinished/1000;
            }

            @Override
            public void onFinish() {
                adsfin.setVisibility(View.VISIBLE);
            }
        }.start();
    }

    public void onClickfin(View view){
        Intent finintent = new Intent(this, MainActivity.class);
        startActivity(finintent);
    }
}