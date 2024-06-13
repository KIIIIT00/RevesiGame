package jp.ac.ristumei.ise.phy.exp2.is0611if.reversigame;

import static java.security.AccessController.getContext;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;

import java.util.Random;

public class GameActivity extends AppCompatActivity {
    private Common common;
    //プレイヤー1と2の先行を表す変数
    //プレイヤーが先行の時は,1,後攻の時は,2
    public static int OneFirst;
    public static int TwoFirst;
    //プレイヤー名
    public static String OneNameText;
    public static String TwoNameText;
    private SharedPreferencesManager sp;
    private static Context mContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sp = new SharedPreferencesManager(this);
        common = (Common)getApplication();
        OneFirst = common.getOneFirst();
        TwoFirst = common.getTwoFirst();
        OneNameText = common.getOneName();
        TwoNameText = common.getTwoName();
        Intent intent = new Intent(this, ReversiView.class);
        intent.putExtra("PlayerOneFirst", OneFirst);
        intent.putExtra("PlayerTwoFirst", TwoFirst);
        intent.putExtra("PlayerOneName", OneNameText);
        intent.putExtra("PlayerTwoName", TwoNameText);
        //startActivity(intent);
        //setContentView(new ReversiView(this));
        setContentView(new ReversiView(this));
        Log.d("WIn", String.valueOf((sp.getWinId())));
        if(sp.getWinId()){
            Intent intent1  = new Intent(this, TwogameSelection.class);
            startActivity(intent1);
        }
        //startActivity(intent);

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