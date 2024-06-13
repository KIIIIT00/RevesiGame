package jp.ac.ristumei.ise.phy.exp2.is0611if.reversigame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.content.SharedPreferences;
import android.widget.ToggleButton;

public class ComgameSelection extends AppCompatActivity {
    final static String FILENAME = "Preference.xml";

    private final String[] dropdownItems ={
            "Level1",
            "Level2",
            "Level3",
            "Level4"
    };

    public String YourNameText;
    public String COMNameText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comgame_selection);
        SharedPreferencesManager sp = new SharedPreferencesManager(this);

        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, dropdownItems);
        Spinner spinner = findViewById(R.id.COMStrength);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String text = (String) parent.getAdapter().getItem(position);
                Toast.makeText(parent.getContext(), text, Toast.LENGTH_SHORT).show();
                sp.setCLId(text);
                //Log.d("レベル", text);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //EditText
        //ユーザの名前を入力
        EditText YourName = (EditText) findViewById(R.id.YourName);
        sp.setYNId("YOU");
        Log.d("前you", sp.getYNId());
        YourName.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                sp.setYNId("YOU");
//                Log.d("前you", sp.getCLId());
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                YourNameText = editable.toString();
                sp.setYNId(YourNameText);
                //テスト済
                Log.d("YourName", YourNameText);

            }
        });

        EditText COMName = (EditText) findViewById(R.id.COMName);
        sp.setCNId("COM");
        Log.d("前COM", sp.getCNId());
        COMName.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                sp.setCNId("COM");
//                Log.d("前COM", sp.getCNId());
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                COMNameText = editable.toString();
                sp.setCNId(COMNameText);
                //テスト済
                Log.d("COMName", sp.getCNId());

            }
        });



        //トグルボタン
//        ToggleButton toggle = (ToggleButton) findViewById(R.id.FirstLater);
//        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
//                if(isChecked){
//                    sp.setHFId(2);
//                }else{
//                    sp.setHFId(1);
//                }
//            }
//        });




    }
    public void onClickCOMVS(View view){
        Intent intent = new Intent(this, COMGameActivity.class);

        startActivity(intent);
    }
}