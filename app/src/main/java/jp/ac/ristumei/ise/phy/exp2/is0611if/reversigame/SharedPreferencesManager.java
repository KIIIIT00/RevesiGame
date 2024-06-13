package jp.ac.ristumei.ise.phy.exp2.is0611if.reversigame;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesManager {
    final static String FILENAME = "Preference.xml";
    final static String PONId = "PlayerOneName";
    final static String PTNId = "PlayerTwoName";
    final static String POFId = "PlayerOneFirst";
    final static String PTFId = "PlayerTwoFirst";
    final static String HFId = "HumanFirst";
    final static String CLId = "COMLevel";
    final static String YNId = "YourName";
    final static String CNId = "COMName";
    final static String WinId = "WInnerFlag";

    SharedPreferences sp;

    SharedPreferencesManager(Context context){
        sp = context.getSharedPreferences(FILENAME, Context.MODE_PRIVATE);
    }

    private void setName(String tag, String name){
        SharedPreferences.Editor e = sp.edit();
        e.putString(tag,name);
        e.apply();
    }

    private void setFirst(String tag, int value){
        SharedPreferences.Editor e = sp.edit();
        e.putInt(tag, value);
        e.apply();
    }

    private void setLevel(String tag, String level){
        SharedPreferences.Editor e = sp.edit();
        e.putString(tag, level);
        e.apply();
    }

    public void setWinId(boolean flag){
        SharedPreferences.Editor e = sp.edit();
        e.putBoolean(WinId, flag);
        e.apply();
    }

    public boolean getWinId(){
        return sp.getBoolean(WinId, false);
    }

    public void setPONId(String name){
        setName(PONId, name);
    }
    public String getPONId(){
        return sp.getString(PONId,null);
    }

    public void setPTNId(String name){
        setName(PTNId, name);
    }
    public String getPTNId(){
        return sp.getString(PTNId, null);
    }

    public void setPOFId(int value){
        setFirst(POFId, value);
    }
    public int getPOFId(){
        return sp.getInt(POFId, -1);
    }

    public void setPTFId(int value){
        setFirst(PTFId, value);
    }

    public int getPTFId(){
        return sp.getInt(PTFId, -1);
    }

    public void setHFId(int value){
        setFirst(HFId, value);
    }

    public int getHFID(){
        return sp.getInt(HFId, -1);
    }

    public void setCLId(String level){
        setLevel(CLId, level);
    }

    public String getCLId(){
        return sp.getString(CLId, null);
    }

    public void setYNId(String yourname){
        setName(YNId, yourname);
    }

    public String getYNId(){
        return sp.getString(YNId, null);
    }

    public void setCNId(String comname){
        setName(CNId, comname);
    }

    public String getCNId(){
        return sp.getString(CNId, null);
    }




}
