package jp.ac.ristumei.ise.phy.exp2.is0611if.reversigame.model;

import android.graphics.Point;
import android.graphics.RectF;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

import jp.ac.ristumei.ise.phy.exp2.is0611if.reversigame.Utils;
import jp.ac.ristumei.ise.phy.exp2.is0611if.reversigame.model.Cell1.E_STATUS;

public class Board1 {
    public static final int COLS = 8;
    public static final int ROWS = 8;

    private RectF rect = new RectF();

    private Cell1 cells[][] = new Cell1[ROWS][COLS];
    private Cell1.E_STATUS turn;
    private Player player1;
    private Player player2;

    public Board1(){
        this.rect.left = 0f;
        this.rect.top = 0f;

        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                cells[i][j] = new Cell1(this, new Point(j, i));
            }
        }

        //初期の配置をセット
        cells[ROWS/2 -1][COLS/2 -1].setStatus(Cell1.E_STATUS.White);
        cells[ROWS/2 -1][COLS/2].setStatus(Cell1.E_STATUS.Black);
        cells[ROWS/2][COLS/2 -1].setStatus(Cell1.E_STATUS.Black);
        cells[ROWS/2][COLS/2].setStatus(Cell1.E_STATUS.White);

        turn = Cell1.E_STATUS.Black;
        ArrayList<Cell1> changedCells = new ArrayList<Cell1>();
        setAllReversibleCells(changedCells);
    }

    public void setRectF(RectF rect) {
        this.rect = rect;
    }
    public RectF getRectF(){
        return this.rect;
    }

    public void setSize(int w, int h){
        int sz = w < h ? w: h;						//正方形になる様に小さいほうに合わせる。

        this.rect.right = this.rect.left + (int)(sz / Board.COLS) * Board.COLS;		//列数で割り切れない場合は余りを捨てる。
        this.rect.bottom = this.rect.top + (int)(sz / Board.ROWS) * Board.ROWS;		//行数で割り切れない場合は余りを捨てる。

        float cellW = this.getCellWidth();
        float cellH = this.getCellHeidht();

        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                cells[i][j].setLeft(j * cellW);
                cells[i][j].setTop(i * cellH);
                cells[i][j].setWidth(cellW);
                cells[i][j].setHeight(cellH);
            }
        }
    }

    public Cell1[][] getCells(){
        return cells;
    }
    public void setCells(Cell1[][] cells){
        this.cells = cells;
    }

    public Cell1 getCell(Point point){
        return cells[point.y][point.x];
    }

    public float getCellWidth(){
        return this.rect.width() / (float)COLS;
    }

    public float getCellHeidht(){
        return this.rect.height() / (float)ROWS;
    }

    public ArrayList<Cell1> changeCell(Point point, Cell1.E_STATUS newStatus) {
        Cell1 cell = cells[point.y][point.x];
        ArrayList<Cell1> list = cell.getReversibleCells();

        ArrayList<Cell1> changedCells = new ArrayList<Cell1>();

        for (Cell1 cell2 : list) {
            cell2.setStatus(newStatus);
            changedCells.add(cell2);
        }

        cell.setStatus(newStatus);
        changedCells.add(cell);

        return changedCells;
    }

    public Cell1.E_STATUS getTurn(){
        return this.turn;
    }

    public boolean isFinished(){
        return (this.turn == Cell1.E_STATUS.None);
    }

    public void setFinished(){
        this.turn = E_STATUS.None;
    }

    public int changeTurn(List<Cell1> changedCells){
        if (this.turn == E_STATUS.Black){
            this.turn = E_STATUS.White;
        } else {
            this.turn = E_STATUS.Black;
        }

        return setAllReversibleCells(changedCells);
    }

    private int setAllReversibleCells(List<Cell1> changedCells){
        int n = 0;
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                Cell1 cell = cells[i][j];

                //再計算の前に前回マークされていた部分を変更リストに追加。
                if (changedCells != null && cell.getReversibleCells().size() > 0){
                    changedCells.add(cell);
                }

                //裏返されるセルのリストを再計算。
                cell.setReversibleCells(this.turn);

                //再計算後に今回マークされた部分を変更リストに追加。
                if (cell.getReversibleCells().size() > 0){
                    n++;
                    if (changedCells != null){
                        changedCells.add(cell);
                    }
                }
            }
        }
        return n;
    }

    public int countCells(Cell1.E_STATUS status){
        int n = 0;
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                if (cells[i][j].getStatus() == status){
                    n++;
                }
            }
        }
        return n;
    }

    public int countBlankCells(){
        return countCells(E_STATUS.None);
    }

    //コマを置く事が出来るセルのリストを返す。
    public ArrayList<Cell1> getAvailableCells(){
        ArrayList<Cell1> available_cells = new ArrayList<Cell1>();
        for (int i = 0; i< Board.ROWS; i++ ){
            for (int j =0; j < Board.COLS; j++){
                cells[i][j].setEval(0);
                if (cells[i][j].getReversibleCells().size() > 0){
                    available_cells.add(cells[i][j]);
                }
            }
        }
        return available_cells;
    }

    public int getAvailableCellCount(boolean recalculate){
        int n = 0;
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                Cell1 cell = cells[i][j];
                if (recalculate){
                    //裏返されるセルのリストを再計算。
                    cell.setReversibleCells(this.turn);
                }
                if (cell.getReversibleCells().size() > 0){
                    n++;
                }
            }
        }
        return n;
    }

    public Cell1.E_STATUS getWinnerStatus(){
        E_STATUS winner = E_STATUS.None;
        int cntB = countCells(E_STATUS.Black);
        int cntW = countCells(E_STATUS.White);
        if (cntB > cntW){
            winner = E_STATUS.Black;
        } else if (cntB < cntW){
            winner = E_STATUS.White;
        }
        return winner;
    }

    public String getTurnDisplay(){
        return Cell1.statusToDisplay(this.turn);
    }


    /**
     * 状態を文字列にシリアライズする。
     */
    public String getStateString(){
        StringBuilder str = new StringBuilder();

        str.append(Cell1.statusToString(this.turn) + ":");

        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                str.append(cells[i][j].getStatusString());
            }
        }

        String s = str.toString();
        Utils.d("getStateString:" + s);
        return s;
    }



    /**
     * 文字列から状態を復元する。
     * @param s
     */
    public void loadFromStateString(String s){
        if (TextUtils.isEmpty(s)) return;

        this.turn = Cell1.stringToStatus(s.substring(0, 1));

        String s2;
        int start = 2;
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                s2 = s.substring(start, start+1);
                cells[i][j].setStatusString(s2);
                start++;
            }
        }

        setAllReversibleCells(null);
    }

    /***
     * 盤面の新しいクローンを作成して返す。
     */
    public Board1 clone(){
        Board1 new_board = new Board1();

        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                new_board.cells[i][j].setStatus(this.cells[i][j].getStatus());
                new_board.cells[i][j].copyReversibleCells(this.cells[i][j].getReversibleCells());
            }
        }
        new_board.turn = this.turn;
//		new_board.setAllReversibleCells(null);

        return new_board;
    }

    public void setPlayer1(Player player1) {
        this.player1 = player1;
    }

    public Player getPlayer1() {
        return player1;
    }

    public void setPlayer2(Player player2) {
        this.player2 = player2;
    }

    public Player getPlayer2() {
        return player2;
    }

    public Player getCurrentPlayer(){
        Player p = null;
        if (turn == E_STATUS.Black){
            p = player1;
        } else if (turn == E_STATUS.White){
            p = player2;
        }
        return p;
    }


}
