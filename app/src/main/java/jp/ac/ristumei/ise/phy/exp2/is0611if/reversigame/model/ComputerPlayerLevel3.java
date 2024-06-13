package jp.ac.ristumei.ise.phy.exp2.is0611if.reversigame.model;

import android.graphics.Point;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import jp.ac.ristumei.ise.phy.exp2.is0611if.reversigame.model.Cell1.E_STATUS;

public class ComputerPlayerLevel3 extends ComputerPlayer{

    private static int WAIT_MSEC = 10;
    private Random mRnd;

    //場所毎の重み付け
    protected static final int[][] weight_table
            = { { 40,-12,  0, -1, -1,  0,-12, 40 },
            {-12,-15, -3, -3, -3, -3,-15,-12 },
            {  0, -3,  0, -1, -1,  0, -3,  0 },
            { -1, -3, -1, -1, -1, -1, -3, -1 },
            { -1, -3, -1, -1, -1, -1, -3, -1 },
            {  0, -3,  0, -1, -1,  0, -3,  0 },
            {-12,-15, -3, -3, -3, -3,-15,-12 },
            { 40,-12,  0, -1, -1,  0,-12, 40 }
    };

    public ComputerPlayerLevel3(E_STATUS turn, String name, Board1 board){
        super(turn, name, board);
        mRnd = new Random();
    }

    @Override
    protected Point think() {
        Point pos = null;

        try {
            Thread.sleep(WAIT_MSEC);
        } catch (InterruptedException e) {
            setStopped(true);
        }
        if (isStopped()) return pos;					//中断フラグが立っていたら抜ける。

        //コマを置く事が出来るセルのリストを得る。
        ArrayList<Cell1> available_cells = mBoard.getAvailableCells();
        if (available_cells.size() == 0){
            return pos;
        }

        if (isStopped()) return pos;					//中断フラグが立っていたら抜ける。

        for (int i = 0; i < available_cells.size(); i++) {
            Cell1 cur = available_cells.get(i);
            //1手先用の盤面を作成。
            Board1 new_board = mBoard.clone();
            //1手打つ。
            new_board.changeCell(cur.getPoint(), new_board.getTurn());
            //新しい盤面の局面の評価値を得る。
            cur.setEval(getWeightTotal(new_board, weight_table));
        }

        //評価値の高いものから降順にソート。
        Collections.sort(available_cells, new EvaluationComparator());

////DEBUG
//Utils.d(String.format("Sorted available cells:\n"));
//for (int i = 0; i < available_cells.size(); i++) {
//	Cell cur = available_cells.get(i);
//	Utils.d(String.format("%d x,y=%d,%d   val=%d", i, cur.getCol(), cur.getRow(),  cur.getEval()));
//}

        ArrayList<Cell1> max_cells = new ArrayList<Cell1>();
        max_cells.add(available_cells.get(0));					//ソート後先頭に来たものを最終候補リストに追加。
        int max_eval = available_cells.get(0).getEval();

        //２番目以降で先頭と同じ評価値を持つものを最終候補リストに追加。
        for (int i = 1; i < available_cells.size(); i++) {
            Cell1 current = available_cells.get(i);
            if (max_eval == current.getEval()){
                max_cells.add(current);
            } else {
                break;
            }
        }

        //最終候補が複数ある場合はそのなかからランダムに選ぶ。
        int n = mRnd.nextInt(max_cells.size());
        Cell1 chosenCell = max_cells.get(n);
        pos = chosenCell.getPoint();

        return pos;
    }
}
