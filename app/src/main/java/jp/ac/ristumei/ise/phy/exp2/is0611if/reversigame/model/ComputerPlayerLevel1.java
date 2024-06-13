package jp.ac.ristumei.ise.phy.exp2.is0611if.reversigame.model;


import android.graphics.Point;

import java.util.ArrayList;
import java.util.Random;
import jp.ac.ristumei.ise.phy.exp2.is0611if.reversigame.model.Cell1.E_STATUS;

public class ComputerPlayerLevel1 extends ComputerPlayer{

    private static int WAIT_MSEC = 10;
    private Random mRnd;

    public ComputerPlayerLevel1(E_STATUS turn, String name, Board1 board){
        super(turn, name, board);

        mRnd = new Random();
    }


    @Override
    protected Point think() {
        Point pos = null;
        try{
            Thread.sleep(WAIT_MSEC);
        }catch (InterruptedException e){
            setStopped(true);
        }
        if(isStopped()) return pos;

        //コマを置く事が出来る場所のリストを得る。
        ArrayList<Cell1> available_cells = mBoard.getAvailableCells();
        if (available_cells.size() == 0){
            return pos;
        }

        if (isStopped()) return pos;					//中断フラグが立っていたら抜ける。

        //置く事が出来る場所からランダムに選ぶ。
        int n = mRnd.nextInt(available_cells.size());
        Cell1 chosenCell = available_cells.get(n);
        pos = chosenCell.getPoint();

        return pos;
    }

}

