package jp.ac.ristumei.ise.phy.exp2.is0611if.reversigame.model;

import static jp.ac.ristumei.ise.phy.exp2.is0611if.reversigame.model.Cell.stringToStatus;

import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;

import java.util.ArrayList;

public class Cell1 {

    public int getNextAvaiableCnt() {
        return mNextAvailableCnt;
    }

    public void setNextAvaiableCnt(int next_available_cnt) {
        this.mNextAvailableCnt = next_available_cnt;
    }

    public enum E_STATUS{
        None,
        Black,
        White
    }

    private Board1 mBoard;
    private E_STATUS status = E_STATUS.None;
    private RectF rect = new RectF();
    private Point point = new Point();

    //このセルにコマを置くと裏返しの対象になるセルのリスト
    ArrayList<Cell1> mReversibleCells = new ArrayList<Cell1>();

    //このセルにコマを置いた場合の局面の評価値
    private int mEval;

    //このセルにコマを置いた場合の次の手数
    private int mNextAvailableCnt;

    public Cell1(Board1 board1, Point point){
        this.mBoard = board1;
        this.point = point;
    }

    public static String statusToDisplay(E_STATUS st){
        String s = "None";
        if(st == E_STATUS.Black){
            s = "Black";
        }else if(st == E_STATUS.White){
            s = "White";
        }
        return s;
    }

    public static E_STATUS stringToStatus(String s){
        E_STATUS st = E_STATUS.None;
        if(s.equals("B")){
            st = E_STATUS.Black;
        }else if(s.equals("W")){
            st = E_STATUS.White;
        }
        return st;
    }

    public Point getPoint(){
        return this.point;
    }

    public int getRow(){
        return this.point.y;
    }

    public int getCol(){
        return this.point.x;
    }

    public void setRectF(RectF rect){
        this.rect = rect;
    }

    public RectF getRectF(){
        return this.rect;
    }

    public void setWidth(float w){
        this.rect.right = this.rect.left + w;
    }

    public float getWidth(){
        return this.rect.width();
    }

    public void setHeight(float h){
        this.rect.bottom = this.rect.top + h;
    }

    public float getHeight(){
        return this.rect.height();
    }

    public void setTop(float top){
        this.rect.top = top;
    }

    public float getTop(){
        return this.rect.top;
    }

    public void setLeft(float left){
        this.rect.left = left;
    }

    public float getLeft(){
        return this.rect.left;
    }

    public Rect getRect(){
        Rect r = new Rect();
        this.rect.round(r);
        return r;
    }

    public float getCx(){
        return this.getRectF().centerX();
    }

    public float getCy(){
        return this.getRectF().centerY();
    }

    public void setStatus(E_STATUS status){
        this.status = status;
    }

    public E_STATUS getStatus(){
        return status;
    }

    public void setEval(int mEval){
        this.mEval = mEval;
    }

    public int getEval(){
        return mEval;
    }

    public void setNextAvailableCnt(int mNextAvailableCnt){
        this.mNextAvailableCnt = mNextAvailableCnt;
    }
    public int getNextAvailableCnt(){
        return mNextAvailableCnt;
    }
    public boolean isBlank(){
        return (this.status == E_STATUS.None);
    }

    public void setStatusString(String status_str) {
        this.status = stringToStatus(status_str);
    }
    public String getStatusString() {
        return statusToString(this.status);
    }

    public static  String statusToString(E_STATUS st) {
        String s = "N";
        if (st == E_STATUS.Black){
            s = "B";
        } else if (st == E_STATUS.White){
            s = "W";
        }
        return s;
    }

    public static E_STATUS getOpponentStatus(E_STATUS turn){
        if (turn == E_STATUS.Black){
            return E_STATUS.White;
        } else if (turn == E_STATUS.White){
            return E_STATUS.Black;
        } else {
            return E_STATUS.None;
        }
    }

    public ArrayList<Cell1> getReversibleCells(){
        return mReversibleCells;
    }

    public void copyReversibleCells(ArrayList<Cell1> cells){
        mReversibleCells.clear();
        Cell1 src, dest;
        for (int i = 0; i < cells.size(); i++){
            src = cells.get(i);
            dest = mBoard.getCell(src.getPoint());
            mReversibleCells.add(dest);
        }
    }

    public void setReversibleCells(E_STATUS current){
        mReversibleCells.clear();

        if (this.getStatus() != E_STATUS.None){
            return;
        }

        E_STATUS opponent = getOpponentStatus(current);

        for (int i=-1; i<=1; i++){
            for (int j=-1; j<=1; j++){
                if (i != 0 || j != 0){
                    ArrayList<Cell1> list = new ArrayList<Cell1>();
                    int n = getCellsInLine(j, i, opponent, list);
                    if (n > 0){
                        Cell1 cell = getNextCell(j * (n+1), i * (n+1));
                        if (cell != null){
                            if (cell.getStatus() == current){
                                mReversibleCells.addAll(list);
                            }
                        }
                    }
                }
            }
        }
    }

    private int getCellsInLine(int dx, int dy, E_STATUS turn, ArrayList<Cell1> list){
        Cell1 cell = getNextCell(dx, dy);
        if (cell != null){
            if (cell.getStatus() == turn){
                list.add(cell);
                cell.getCellsInLine(dx, dy, turn, list);
            }
        }
        return list.size();
    }

    private Cell1 getNextCell(int offx, int offy){
        int px = this.point.x + offx;
        int py = this.point.y + offy;

        if (px < 0 || px >= Board.COLS || py < 0 || py >= Board.ROWS){
            return null;
        }

        Cell1[][] cells = mBoard.getCells();
        return cells[py][px];
    }
}
