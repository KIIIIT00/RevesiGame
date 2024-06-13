package jp.ac.ristumei.ise.phy.exp2.is0611if.reversigame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    /**
    COMと対戦の選択画面に遷移
     * @param view
     */
    public void onCOMButtonTaped(View view) {
        Intent intent = new Intent(this, ComgameSelection.class);
        startActivity(intent);
    }

    /**
     * 二人で対戦の画面に遷移
     * @param view
     */
    public void onTwoButtonTaped(View view){
        Intent intent  = new Intent(this, TwogameSelection.class);
        startActivity(intent);
    }

    public void onNextGameButton(View view){
        Intent intent = new Intent(this,NextGame.class);
        startActivity(intent);
    }
}