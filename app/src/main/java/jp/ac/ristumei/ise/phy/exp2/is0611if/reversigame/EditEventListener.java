package jp.ac.ristumei.ise.phy.exp2.is0611if.reversigame;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;



public class EditEventListener implements TextWatcher {
    private EditText editTextOne;
    private EditText editTextTwo;


    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        String inputStr = editable.toString();
        Log.d("テスト入力", inputStr);
    }
}
