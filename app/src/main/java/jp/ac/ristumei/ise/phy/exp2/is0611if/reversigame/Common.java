package jp.ac.ristumei.ise.phy.exp2.is0611if.reversigame;

import android.app.Application;

public class Common extends Application {
    //グローバルに扱う変数
    public int OneFirst;
    public int TwoFirst;
    //プレイヤー名
    public String OneNameText;
    public String TwoNameText;
    String fromActivityname; //遷移元の画面名称

    @Override
    public void onCreate() {
        super.onCreate();
        this.OneFirst = 0;
        this.TwoFirst = 0;
        this.OneNameText = "";
        this.TwoNameText = "";
        fromActivityname = "";
    }
    public void setOneName(String OneName){
        this.OneNameText = OneName;
    }

    public void setTwoName(String TwoName){
        this.TwoNameText = TwoName;
    }

    public void setOneFirst(int OneFirstnum){
        this.OneFirst = OneFirstnum;
    }
    public void setTwoFirst(int TwoFirstnum){
        this.TwoFirst = TwoFirstnum;
    }

    public String getOneName(){
        return this.OneNameText;
    }

    public String getTwoName(){
        return this.TwoNameText;
    }

    public int getOneFirst(){
        return this.OneFirst;
    }

    public int getTwoFirst(){
        return this.TwoFirst;
    }

}
