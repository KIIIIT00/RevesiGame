package jp.ac.ristumei.ise.phy.exp2.is0611if.reversigame.model;

import android.graphics.Point;


import android.os.Handler;

import java.util.Comparator;

import jp.ac.ristumei.ise.phy.exp2.is0611if.reversigame.model.Cell1.E_STATUS;

public abstract class ComputerPlayer extends Player implements  Runnable{

    private Handler mHandler = new Handler();
    private IPlayerCallback mCallback;
    private Thread mThread;
    private boolean mStopped;

    public ComputerPlayer(Cell1.E_STATUS turn, String name, Board1 board){
        super(turn, name, board);
        mStopped = false;
    }

    @Override
    public boolean isHuman() {
        return false;
    }

    @Override
    public synchronized void startThinking(IPlayerCallback callback) {
        mCallback = callback;
        mStopped = false;

        if (mBoard.getAvailableCellCount(true) == 0){
            callback.onEndThinking(null);
            return;
        }

        //別スレッドでタイトルの取得処理を開始。
        mThread = new Thread(this);
        mThread.start();
    }

    @Override
    public synchronized void stopThinking() {
        mStopped = true;
    }

    @Override
    public void run() {
        //思考ルーチンを実行。
        final Point pos = think();

        //処理完了後、ハンドラにUIスレッド側で実行する処理を渡す。
        mHandler.post(new Runnable(){
            @Override
            public void run(){
                mCallback.onEndThinking(pos);
            }
        });
    }

    protected abstract Point think();

    public void setStopped(boolean mStopped) {
        this.mStopped = mStopped;
    }

    public boolean isStopped() {
        return mStopped;
    }

    protected void onProgress(final int percent){
        this.setProgress(percent);

        mHandler.post(new Runnable(){
            @Override
            public void run(){
                mCallback.onProgress();
            }
        });
    }

    public int getWeight(Cell1 cell, int[][] weight_table){
        Point pt = cell.getPoint();
        return weight_table[pt.y][pt.x];
    }

    public int getWeight(Point pt, int[][] weight_table){
        return weight_table[pt.y][pt.x];
    }


    /***
     * セルの位置の評価値で降順にソートする為のComparatorクラス。
     * @author mike
     *
     */
    public class WeightComparator implements Comparator<Cell1> {

        private int[][] mWeightTable;

        public WeightComparator(int[][] weight_table){
            mWeightTable = weight_table;
        }

        @Override
        public int compare(Cell1 cell1, Cell1 cell2) {
            //0：等しい。1：より大きい。-1：より小さい
            int weight1 = getWeight(cell1, mWeightTable);
            int weight2 = getWeight(cell2, mWeightTable);
            if (weight1 > weight2) return -1;
            if (weight1 < weight2) return 1;
            return 0;
        }
    }

    /***
     * セルの位置の評価値で降順にソートする為のComparatorクラス。
     * @author mike
     *
     */
    public class EvaluationComparator implements Comparator<Cell1> {
        @Override
        public int compare(Cell1 cell1, Cell1 cell2) {
            //0：等しい。1：より大きい。-1：より小さい
            int val1 = cell1.getEval();
            int val2 = cell2.getEval();
            if (val1 > val2) return -1;
            if (val1 < val2) return 1;
            if (val1 == val2){
                if (cell1.getNextAvaiableCnt() > cell2.getNextAvaiableCnt()) return -1;
                if (cell1.getNextAvaiableCnt() < cell2.getNextAvaiableCnt()) return 1;
            }
            return 0;
        }
    }

    public int getWeightTotal(Board1 board, int [][] weight_table){
        int total = 0;
        Cell1[][] cells = board.getCells();
        E_STATUS player_turn = board.getTurn();
        E_STATUS opp_turn = Cell1.getOpponentStatus(player_turn);

        int cur_count = 0, opp_count = 0, blank_count = 0;

        for (int i = 0; i< Board1.ROWS; i++ ){
            for (int j =0; j < Board1.COLS; j++){
                E_STATUS st = cells[i][j].getStatus();
                if (st == player_turn){
                    cur_count++;
                    total += getWeight(cells[i][j], weight_table);
                } else if (st == opp_turn){
                    opp_count++;
                    total -= getWeight(cells[i][j], weight_table);
                } else {
                    blank_count++;
                }
            }
        }

        return total;
    }
}
