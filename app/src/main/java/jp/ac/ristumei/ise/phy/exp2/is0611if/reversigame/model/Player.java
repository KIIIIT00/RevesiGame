package jp.ac.ristumei.ise.phy.exp2.is0611if.reversigame.model;

import android.content.Context;
import jp.ac.ristumei.ise.phy.exp2.is0611if.reversigame.Pref;
import jp.ac.ristumei.ise.phy.exp2.is0611if.reversigame.Pref1;
import jp.ac.ristumei.ise.phy.exp2.is0611if.reversigame.model.Cell1.E_STATUS;

public abstract class Player {

    protected E_STATUS mTurn;
    protected String mName;
    protected Board1 mBoard;

    private int mProgress;
    private Cell1 mCurrentCell;

    public Player(E_STATUS turn, String name, Board1 board){
        setTurn(turn);
        setName(name);
        mBoard = board;
    }

    public abstract boolean isHuman();

    public void setTurn(E_STATUS mTurn){
        this.mTurn = mTurn;
    }

    public E_STATUS getmTurn(){
        return mTurn;
    }

    public void setName(String mName){
        this.mName = mName;
    }

    public String getName(){
        return mName;
    }

    public abstract void startThinking(IPlayerCallback callback);
    public abstract void stopThinking();

    private static final Player getPlayer(String name, Board1 board, E_STATUS turn, String value){
        int int_value = Integer.valueOf(value);
        Player player;
        switch(int_value){
            case 1:
                player = new ComputerPlayerLevel1(turn, name, board);
                break;

            case 2:
                player  =new ComputerPlayerLevel2(turn, name, board);
                break;

            case 3:
                player = new ComputerPlayerLevel3(turn, name, board);
                break;

            case 4:
                player = new ComputerPlayerLevel4(turn, name, board);

            default:
                player = new HumanPlayer(turn, name, board);

        }
        return player;
    }

    public static final Player getPlayer1(Context con, Board1 board, E_STATUS turn){
        String name = Pref1.getPlayer1Name(con);
        String value = Pref1.getPlayer1(con);
        return getPlayer(name, board, turn, value);
    }

    public static final Player getPlayer2(Context con, Board1 board, E_STATUS turn){
        String name = Pref1.getPlayer2Name(con);
        String value = Pref1.getPlayer2(con);
        return getPlayer(name, board, turn, value);
    }

    public static final void setPlayer1(Context con, Board1 board, E_STATUS turn, String value){
        Pref1.setPlayer1(con, value);
    }

    public static final void setPlayer2(Context con, Board1 board, E_STATUS turn, String value){
        Pref1.setPlayer2(con, value);
    }

    public void setProgress(int mProgress) {
        this.mProgress = mProgress;
    }

    public int getProgress() {
        return mProgress;
    }

    public void setCurrentCell(Cell1 mCurrentCell) {
        this.mCurrentCell = mCurrentCell;
    }

    public Cell1 getCurrentCell() {
        return mCurrentCell;
    }


}
