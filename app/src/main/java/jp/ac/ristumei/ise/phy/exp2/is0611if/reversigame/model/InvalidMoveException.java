package jp.ac.ristumei.ise.phy.exp2.is0611if.reversigame.model;

public class InvalidMoveException extends Throwable {
    String mMsg = "Invalid move.";

    public InvalidMoveException(){
        //
    }

    public InvalidMoveException(String msg){
        mMsg = msg;
    }

    @Override
    public String getMessage(){
        return mMsg;
    }
}
