package jp.ac.ristumei.ise.phy.exp2.is0611if.reversigame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.Random;

public class COMGameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new VSReversiView(this));
//        LinearLayout LL = new LinearLayout(this);
//        setContentView(LL);
//        VSReversiView vsr = new VSReversiView(this);
//        LL.addView(vsr);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event){
        if(keyCode == KeyEvent.KEYCODE_BACK){
            Log.d("KeyDown", "バックした");
            Random random = new Random();
            int randomValue = random.nextInt(2);
            if(randomValue == 0) {
                Intent finishintent = new Intent(this,Ads.class);
                startActivity(finishintent);
            }else{
                Intent finishintent = new Intent(this, MainActivity.class);
                startActivity(finishintent);
            }
            return false;
        }
        return true;
    }

}