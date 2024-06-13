package jp.ac.ristumei.ise.phy.exp2.is0611if.reversigame;

public class JsonData {
    private String PlayerOneName;
    private String PlayerTwoName;
    private int PlayerOneFirst;
    private int PlayerTwoFirst;

    public String getPlayerOneName(){
        return PlayerOneName;
    }

    public String getPlayerTwoName(){
        return PlayerTwoName;
    }

    public int getPlayerOneFirst(){
        return PlayerOneFirst;
    }

    public int getPlayerTwoFirst(){
        return PlayerTwoFirst;
    }

    public void setPlayerOneName(String PlayerOneName){
        this.PlayerOneName = PlayerOneName;
    }
    public void setPlayerTwoName(String PlayerTwoName){
        this.PlayerTwoName = PlayerTwoName;
    }

    public void setPlayerOneFirst(int PlayerOneFirst){
        this.PlayerOneFirst = PlayerOneFirst;
    }

    public void setPlayerTwoFirst(int PlayerTwoFirst){
        this.PlayerTwoFirst = PlayerTwoFirst;
    }

}
