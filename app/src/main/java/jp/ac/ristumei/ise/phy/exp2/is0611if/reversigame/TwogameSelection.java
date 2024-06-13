package jp.ac.ristumei.ise.phy.exp2.is0611if.reversigame;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.JsonWriter;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import org.json.JSONObject;

import org.json.JSONException;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class TwogameSelection extends Activity{
    final static String FILENAME = "Preference.xml";
    //プレイヤー1と2の先行を表す変数
    //プレイヤーが先行の時は,1,後攻の時は,2
    public static int OneFirst;
    public static int TwoFirst;
    //プレイヤー名
    public static String OneNameText;
    public static String TwoNameText;
    //グローバル変数を扱うクラス
    private Common common;
    public View view;

    //json
    public String PlayerOneName;
    public String PlayerTwoName;
    public String PlayerOneFirst;
    public String PlayerTwoFirst;
    public JsonWriter writer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_twogame_selection);

        SharedPreferencesManager sp = new SharedPreferencesManager(this);

        common = (Common)getApplication();
        //Gson
//        Gson gson = new Gson();
//        try{
//            //inputStream = this.getAssets().open("sample.json");
//            writer = new JsonWriter(new FileWriter("sample.jsom"));
//            writer.beginObject();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        //InputStreamReaderで

        TextView playerTwoFL = (TextView) findViewById(R.id.playerTwoForL);
        //ラジオボタンの設定
        //ラジオボタンでPlayer1が先行の時,Player2のテキストを後攻に表示
        RadioGroup FirstSelection = (RadioGroup) findViewById(R.id.PlayerOneSelection);
        FirstSelection.setOnCheckedChangeListener((view, id) -> {
            if (id == R.id.PlayerOneFirst) {
                //1:先行,2:後攻
                OneFirst = 1;
                TwoFirst = 2;
                sp.setPOFId(1);
                sp.setPTFId(2);
                //テスト済
                Log.d("ユーザー1F", String.valueOf(sp.getPOFId()));
                Log.d("ユーザー2F", String.valueOf(sp.getPTFId()));
                //
                playerTwoFL.setText("後攻");
            } else if (id == R.id.PlayerOneLater) {
               OneFirst = 2;
                TwoFirst = 1;
                sp.setPOFId(2);
                sp.setPTFId(1);
                //テスト済
                Log.d("ユーザー1F", String.valueOf(sp.getPOFId()));
                Log.d("ユーザー2F", String.valueOf(sp.getPTFId()));
                //
                playerTwoFL.setText("先行");
            }
        });

        //ユーザ入力したものを変数に格納
        EditText OneName = (EditText) findViewById(R.id.UserPlayerOneName);
        sp.setPONId("Player1");
        Log.d("ユーザー1前", sp.getPONId());
        OneName.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //common.setOneName("Player1");
                //Log.d("ユーザー1前", common.getOneName());
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                OneNameText = editable.toString();
                //common.setOneName(OneNameText);
                sp.setPONId(OneNameText);
                //テスト済
                Log.d("ユーザー1", sp.getPONId());
            }
        });
//        OneNameText = OneName.getText().toString();
//        if(OneNameText.length() == 0){
////            OneNameText = "Player1";
//            common.setOneName("Player1");
//            Log.d("テスト", common.getOneName());
//        }else {
////            OneNameText = OneName.getText().toString();
////            common.setOneName(OneName.getText().toString());
////            Log.d("テスト", common.getOneName());
//
//        }

        EditText TwoName = (EditText) findViewById(R.id.UserPlayerTwoName);
        sp.setPTNId("Player2");
        Log.d("ユーザー2前", sp.getPTNId());
        TwoName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //common.setTwoName("Player2");
                //Log.d("ユーザー2前", common.getTwoName());
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                TwoNameText = editable.toString();
                //common.setTwoName(TwoNameText);
                sp.setPTNId(TwoNameText);
                //テスト済
                Log.d("ユーザー2", sp.getPTNId());
            }
        });

        //Gson
        //Gson gson = new Gson();
//        TwoNameText = TwoName.getText().toString();
//        if(TwoNameText.length() == 0){
////            TwoNameText = "Player2";
//            common.setTwoName("Player2");
//        }else {
////            TwoNameText = TwoName.getText().toString();
//            common.setTwoName(TwoName.getText().toString());
//        }
//        String test_text = common.getOneName();
//        int test_num = common.getOneFirst();
////        Log.d(test_text, test_text);
//        Log.d("代入テスト", "OneFirst: " +  test_num + " OneNameText:" + test_text);

    }

    /**
     * 対戦への遷移
     * @param view
     */
    public void onClickTwoVS(View view) {
        Intent intent = new Intent(this, GameActivity.class);


            //inputStream = this.getAssets().open("sample.json");
//            writer = new JsonWriter(new FileWriter("..\\assets\\sample.json"));
//            writer.beginObject();
//            writer.name("PlayerOneName").value(OneNameText);
//            writer.name("PlayerTwoName").value(TwoNameText);
//            writer.name("PlayerOneFirst").value(OneFirst);
//            writer.name("PlayerTwoFirst").value(TwoFirst);
//            writer.close();



//        intent.putExtra("PlayerOneFirst", OneFirst);
//        intent.putExtra("PlayerTwoFirst", TwoFirst);
//        intent.putExtra("PlayerOneName", OneNameText);
//        intent.putExtra("PlayerTwoName", TwoNameText);
        startActivity(intent);
    }

}