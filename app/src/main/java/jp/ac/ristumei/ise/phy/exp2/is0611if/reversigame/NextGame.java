package jp.ac.ristumei.ise.phy.exp2.is0611if.reversigame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class NextGame extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next_game);
    }

    public void onNextButton(View view){
        Intent start = new Intent(this, Intro1.class);
        startActivity(start);
    }
}