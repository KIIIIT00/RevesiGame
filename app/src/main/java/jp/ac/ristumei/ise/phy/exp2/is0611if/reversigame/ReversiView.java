package jp.ac.ristumei.ise.phy.exp2.is0611if.reversigame;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.JsonReader;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import jp.ac.ristumei.ise.phy.exp2.is0611if.reversigame.model.Board;
import jp.ac.ristumei.ise.phy.exp2.is0611if.reversigame.model.Board1;
import jp.ac.ristumei.ise.phy.exp2.is0611if.reversigame.model.Cell;
import jp.ac.ristumei.ise.phy.exp2.is0611if.reversigame.model.Cell.E_STATUS;
import jp.ac.ristumei.ise.phy.exp2.is0611if.reversigame.model.Cell1;
import jp.ac.ristumei.ise.phy.exp2.is0611if.reversigame.model.IPlayerCallback;
import jp.ac.ristumei.ise.phy.exp2.is0611if.reversigame.model.InvalidMoveException;
import jp.ac.ristumei.ise.phy.exp2.is0611if.reversigame.Common;
import jp.ac.ristumei.ise.phy.exp2.is0611if.reversigame.model.Player;


public class ReversiView extends View implements IPlayerCallback {
    private static final int VIEW_ID = 1000;

    private Board1 mBoard;
    Common c;

    private Paint mPaintScreenBg = new Paint();
    private Paint mPaintBoardBg = new Paint();
    private Paint mPaintBoardBorder = new Paint();
    private Paint mPaintCellFgB = new Paint();
    private Paint mPaintCellFgW = new Paint();
    private Paint mPaintCellAvB = new Paint();
    private Paint mPaintCellAvW = new Paint();
    private Paint mPaintTextFg = new Paint();
    private Paint mPaintTurnRect = new Paint();
    private Paint mPaintWinnerRect = new Paint();
    private Paint mPaintCellCur = new Paint();

    private int mWidth;
    private int mHeight;
    private static final float CELL_SIZE_FACTOR = 0.42f;
    private boolean mPaused;

    //先行・後攻
    private int Player1Fir;
    private int Player2Fir;
    //名前
    private String Player1name;
    private String Player2name;

    private SharedPreferencesManager sp;

    public ReversiView(Context context) {
        super(context);

        setId(VIEW_ID);
        setFocusable(true);

        sp = new SharedPreferencesManager(getContext());

        Player1Fir = sp.getPOFId();
        Player2Fir = sp.getPTFId();
        Player1name = sp.getPONId();
        Player2name = sp.getPTNId();


        mPaintScreenBg.setColor(getResources().getColor(R.color.screen_bg));
        mPaintBoardBg.setColor(getResources().getColor(R.color.board_bg));
        mPaintBoardBorder.setColor(getResources().getColor(R.color.board_border));
        mPaintCellFgB.setColor(getResources().getColor(R.color.cell_fg_black));
        mPaintCellFgW.setColor(getResources().getColor(R.color.cell_fg_white));
        mPaintCellAvB.setColor(getResources().getColor(R.color.cell_fg_black));
        mPaintCellAvW.setColor(getResources().getColor(R.color.cell_fg_white));
        mPaintCellCur.setColor(getResources().getColor(R.color.cell_fg_current));
        mPaintTextFg.setColor(getResources().getColor(R.color.text_fg));
        mPaintTurnRect.setColor(getResources().getColor(R.color.turn_rect));
        mPaintWinnerRect.setColor(getResources().getColor(R.color.winner_rect));

        //アンチエイリアスを指定。これをしないと縁がギザギザになる。
        mPaintCellFgB.setAntiAlias(true);
        mPaintCellFgW.setAntiAlias(true);
        mPaintCellAvB.setAntiAlias(true);
        mPaintCellAvW.setAntiAlias(true);
        mPaintCellCur.setAntiAlias(true);

        mPaintCellAvB.setAlpha(32);
        mPaintCellAvW.setAlpha(64);
        mPaintCellCur.setAlpha(128);

        mPaintTextFg.setAntiAlias(true);
        mPaintTextFg.setStyle(Paint.Style.FILL);


        Resources res = getResources();
        int fontSize = res.getDimensionPixelSize(R.dimen.font_size_status);
        mPaintTextFg.setTextSize(fontSize);

        mPaintTurnRect.setAntiAlias(true);
        mPaintTurnRect.setAlpha(128);
        mPaintTurnRect.setStyle(Paint.Style.STROKE);
        mPaintTurnRect.setStrokeWidth(5f);

        mPaintWinnerRect.setAntiAlias(true);
        mPaintWinnerRect.setAlpha(192);
        mPaintWinnerRect.setStyle(Paint.Style.STROKE);
        mPaintWinnerRect.setStrokeWidth(5f);

        init(false);
    }


    public void init(boolean auto_start){
        mBoard = new Board1();
        mPaused = false;
        sp.setWinId(false);

        if(Player1Fir == 1){
            Pref1.setPlayer1Name(getContext(), Player1name);
            Pref1.setPlayer2Name(getContext(), Player2name);
        }else if(Player1Fir == 2){
            Pref1.setPlayer1Name(getContext(), Player2name);
            Pref1.setPlayer2Name(getContext(), Player1name);
        }
        Pref1.setPlayer1(getContext(), String.valueOf(-1));
        Pref1.setPlayer2(getContext(), String.valueOf(-1));
        mBoard.setPlayer1(Player.getPlayer1(getContext(), mBoard, Cell1.E_STATUS.Black));
        mBoard.setPlayer2(Player.getPlayer2(getContext(), mBoard, Cell1.E_STATUS.White));

        invalidate();

        Utils.d("init");
        if (auto_start){
            callPlayer();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        this.mWidth = getWidth();
        this.mHeight = getHeight();
        mBoard.setSize(this.mWidth, this.mHeight);

        drawBoard(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                int r = (int)(y / mBoard.getCellHeidht());
                int c = (int)(x / mBoard.getCellWidth());
                if (r < Board1.ROWS && c < Board1.COLS && r >=0 && c >= 0){
                    Player p = mBoard.getCurrentPlayer();
                    if (p != null && p.isHuman()){
                        move(new Point(c, r));
                    }
                }
                break;
            default:
        }

        return true;
    }

    private void move(Point point){
        List<Cell1> changedCells = null;

        if (mBoard.getCell(point).getReversibleCells().size() == 0){
            String s = String.format("Invalid move. (r,c=%d,%d)", point.y, point.x);
            //Toast.makeText(this.getContext(), s, Toast.LENGTH_SHORT).show();
            Utils.d(s);
            return;
        }

        changedCells = mBoard.changeCell(point, mBoard.getTurn());

        int nextAvailableCellCount = mBoard.changeTurn(changedCells);
        if (nextAvailableCellCount == 0){
            if (mBoard.countBlankCells() == 0){				//全部のセルが埋まった場合は終了
                finish();
            } else {
                showSkippMessage();					//スキップ
                nextAvailableCellCount = mBoard.changeTurn(changedCells);
                if (nextAvailableCellCount == 0){	//どちらも打つ場所が無くなった場合は終了
                    finish();
                }
            }
        }

        callPlayer();

        if (changedCells != null){
            for (Cell1 cell : changedCells) {
                invalidate(cell.getRect());			//変更された領域のみを再描画
            }
        }
    }

    @Override
    public void onEndThinking(Point pos) {
        if (pos == null) return;
        if (pos.y < 0 || pos.x < 0) return;
        if (mPaused) return;

        move(pos);
    }

    @Override
    public void onProgress() {
        invalidate(0, (int)mBoard.getRectF().bottom, mWidth, mHeight);
    }

    @Override
    public void onPointStarted(Point pos) {
        Cell1 cell = mBoard.getCell(pos);
        invalidate(cell.getRect());			//変更された領域のみを再描画
    }

    @Override
    public void onPointEnded(Point pos) {
        Cell1 cell = mBoard.getCell(pos);
        invalidate(cell.getRect());			//変更された領域のみを再描画
    }

    private void finish(){
        mBoard.setFinished();
//		showCountsToast();
    }

    public void showCountsToast(){
        Cell1.E_STATUS winner = mBoard.getWinnerStatus();
        String msg = "Black: " + mBoard.countCells(Cell1.E_STATUS.Black) + "\n"
                + "White: " + mBoard.countCells(Cell1.E_STATUS.White) + "\n\n";

        if (mBoard.isFinished()){
            if (winner != Cell1.E_STATUS.None){
                msg += "Winner is: " + Cell1.statusToDisplay(winner) + "!!";
            } else {
                msg += "Draw game!";
            }
        } else {
            if (winner != Cell1.E_STATUS.None){
                msg += Cell1.statusToDisplay(winner) + " is winning...\n\n";
            }
            msg += mBoard.getTurnDisplay() + "'s turn.";
        }
        Toast.makeText(this.getContext(), msg, Toast.LENGTH_LONG).show();
        Utils.d(msg);
    }

    private void showSkippMessage(){
        String msg = Cell1.statusToDisplay(mBoard.getTurn()) + " has been skipped.";
        Toast.makeText(this.getContext(), msg, Toast.LENGTH_SHORT).show();
        Utils.d(msg);
    }

    private void drawBoard(Canvas canvas){

        if (mBoard.getRectF().width() <= 0f ) return;

        float bw = mBoard.getRectF().width();
        float bh = mBoard.getRectF().height();
        float cw = mBoard.getCellWidth();
        float ch = mBoard.getCellHeidht();
        float w = cw * CELL_SIZE_FACTOR;
        boolean show_hints = Pref.getShowHints(getContext());

        //画面全体の背景
        canvas.drawRect(0 ,0, mWidth, mHeight, mPaintScreenBg);

        //ボードの背景
        canvas.drawRect(mBoard.getRectF(), mPaintBoardBg);

        //縦線
        for (int i = 0; i < Board1.COLS; i++) {
            canvas.drawLine(cw * (i+1), 0, cw * (i+1), bh, mPaintBoardBorder);
        }
        //横線
        for (int i = 0; i < Board1.ROWS; i++) {
            canvas.drawLine(0, ch * (i+1), bw, ch * (i+1), mPaintBoardBorder);
        }

        //全てのCellについてコマが配置されていれば描く
        Cell1[][] cells = mBoard.getCells();
        for (int i = 0; i < Board1.ROWS; i++) {
            for (int j = 0; j < Board1.COLS; j++) {
                Cell1 cell =cells[i][j];
                Cell1.E_STATUS st = cell.getStatus();

                if (st == Cell1.E_STATUS.Black){
                    canvas.drawCircle(cell.getCx(), cell.getCy(), w, mPaintCellFgB);
                } else if(st == Cell1.E_STATUS.White){
                    canvas.drawCircle(cell.getCx(), cell.getCy(), w, mPaintCellFgW);
                } else {
                    drawHints(cell, canvas, cw, show_hints);
                }
            }
        }

        //手番の表示、現在の黒と白の数の表示
        drawStatus(canvas);
    }

    private void drawHints(Cell1 cell, Canvas canvas, float cw, boolean show_hints){
        if (!show_hints){
            return;
        }

        float aw = cw * 0.1f;

        //次に配置可能なセルであれば小さな丸を表示する
        if (cell.getReversibleCells().size() > 0){
            if (mBoard.getTurn() == Cell1.E_STATUS.Black){
                canvas.drawCircle(cell.getCx(), cell.getCy(), aw, mPaintCellAvB);
            } else {
                canvas.drawCircle(cell.getCx(), cell.getCy(), aw, mPaintCellAvW);
            }
        } else {
            canvas.drawCircle(cell.getCx(), cell.getCy(), aw, mPaintBoardBg);
        }
    }

    private void drawStatus(Canvas canvas){
        Resources res = getResources();
        float turn_rect_inset = res.getDimension(R.dimen.turn_rect_inset);
        float turn_rect_round = res.getDimension(R.dimen.turn_rect_round);
        float turn_circle_x = res.getDimension(R.dimen.turn_circle_x);
        float turn_circle_y = res.getDimension(R.dimen.turn_circle_y);
        float turn_text_x = res.getDimension(R.dimen.turn_text_x);
        float turn_text_y = res.getDimension(R.dimen.turn_text_y);
        float top = mBoard.getRectF().bottom;
        float center = mBoard.getRectF().width() / 2f;

        if (!mBoard.isFinished()){
            RectF rect;
            if (mBoard.getTurn() == Cell1.E_STATUS.Black){
                rect = new RectF(turn_rect_inset, top + turn_rect_inset, center - turn_rect_inset, mHeight - turn_rect_inset);
            } else {
                rect = new RectF(center + turn_rect_inset, top + turn_rect_inset, mWidth - turn_rect_inset, mHeight - turn_rect_inset);
            }
            mPaintTurnRect.setStyle(Paint.Style.FILL);
            mPaintTurnRect.setAlpha(128);
            canvas.drawRoundRect(rect, turn_rect_round, turn_rect_round, mPaintTurnRect);	//背景
            mPaintTurnRect.setStyle(Paint.Style.STROKE);
            mPaintTurnRect.setAlpha(255);
            canvas.drawRoundRect(rect, turn_rect_round, turn_rect_round, mPaintTurnRect);	//枠
        }

        canvas.drawCircle(turn_circle_x, top + turn_circle_y, mBoard.getCellWidth() * CELL_SIZE_FACTOR, mPaintCellFgB);
        String s = String.valueOf(mBoard.countCells(Cell1.E_STATUS.Black));
        canvas.drawText(s, turn_text_x, top + turn_text_y, mPaintTextFg);					//黒のコマ数
        canvas.drawText(mBoard.getPlayer1().getName(), turn_circle_x, top + turn_text_y*1.8f, mPaintTextFg);			//黒の名前

        canvas.drawCircle(center + turn_circle_x, top + turn_circle_y, mBoard.getCellWidth() * CELL_SIZE_FACTOR, mPaintCellFgB);
        canvas.drawCircle(center + turn_circle_x, top + turn_circle_y, mBoard.getCellWidth() * CELL_SIZE_FACTOR * 0.94f, mPaintCellFgW);
        s = String.valueOf(mBoard.countCells(Cell1.E_STATUS.White));
        canvas.drawText(s, center + turn_text_x, top + turn_text_y, mPaintTextFg);		//白のコマ数
        canvas.drawText(mBoard.getPlayer2().getName(), center + turn_circle_x, top + turn_text_y*1.8f, mPaintTextFg);  //白の名前


        //コンピュータの思考中の場合は進捗状況を表示。
        Player p = mBoard.getCurrentPlayer();
        if (p != null && !p.isHuman()){
//			s = String.valueOf(mBoard.getCurrentPlayer().getProgress()) + "%";
//			canvas.drawText(s, rect.left + turn_circle_x, top + turn_text_y*2.5f, mPaintTextFg);

            Cell1 cell = p.getCurrentCell();
            if (cell != null){
                canvas.drawCircle(cell.getCx(), cell.getCy(),  mBoard.getCellWidth() * CELL_SIZE_FACTOR, mPaintCellCur);
                invalidate(cell.getRect());
            }
        }

        if (mBoard.isFinished()){
            drawWinner(canvas);
        }

        invalidate(0, (int)mBoard.getRectF().bottom, mWidth, mHeight);
    }

    private void drawWinner(Canvas canvas){
        Resources res = getResources();
        float center_x = mBoard.getRectF().width() / 2f;
        float center_y = mBoard.getRectF().height() / 2f;
        String s;

        if (mBoard.getWinnerStatus() == Cell1.E_STATUS.Black){
            s = mBoard.getPlayer1().getName() + " wins!";
        } else if (mBoard.getWinnerStatus() == Cell1.E_STATUS.White) {
            s = mBoard.getPlayer2().getName() + " wins!";
        } else {
            s = "Draw game!";
        }

        Paint paintBg = new Paint();
        paintBg.setColor(Color.BLACK);
        paintBg.setAlpha(128);
        canvas.drawRect(mBoard.getRectF(), paintBg);

        Paint paint = new Paint(mPaintTextFg);
        paint.setColor(Color.YELLOW);
        paint.setAlpha(255);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(mPaintTextFg.getTextSize() * 1.8f);
        paint.setTextSkewX(-0.3f);
        paint.setShadowLayer(2, 2, 2, Color.argb(200, 0, 0, 0));

        canvas.save();
        canvas.rotate(-30, center_x, center_y);
        canvas.drawText(s, center_x, center_y, paint);
        canvas.restore();
        sp.setWinId(true);
        invalidate();			//タイミングによっては文字の一部が消える場合があるので必要。

    }


    public String getState(){
        String s = mBoard.getStateString();
        Utils.d("getState: state=" + s);
        return s;
    }

    public void pause(){
        mPaused = true;

        Player p = mBoard.getCurrentPlayer();
        if (p != null && !p.isHuman()){
            //別スレッドで思考ルーチンが動いていれば中断する。
            p.stopThinking();
        }
    }

    public void resume(String state){
        Utils.d("onResume: state=" + state);

        mPaused = false;
        if (!TextUtils.isEmpty(state)){
            mBoard.loadFromStateString(state);

            mBoard.setPlayer1(Player.getPlayer1(getContext(), mBoard, Cell1.E_STATUS.Black));
            mBoard.setPlayer2(Player.getPlayer2(getContext(), mBoard, Cell1.E_STATUS.Black));
        }

        callPlayer();
    }

    private void callPlayer(){
        if (mPaused) return;

        Player p = mBoard.getCurrentPlayer();
        if (p != null && !p.isHuman()){
            p.startThinking(this);
        }
    }

//    //プレイヤー1と2の先行を表す変数
//    //プレイヤーが先行の時は,1,後攻の時は,2
//    public static int OneFirst;
//    public static int TwoFirst;
//    //プレイヤー名
//    public static String OneNameText;
//    public static String TwoNameText;
//
//    //グローバル変数を扱うクラス
//    private Common common;
//
//    private static final String TAG = "ReversiView";
//
//    private Board mBoard = new Board();
//    private int mWidth;
//    private int mHeight;
//    private static final float CELL_SIZE_FACTOR = 0.42f;
//
//
//    //ペイント
//    private Paint mPaintScreenBg = new Paint();
//    private Paint mPaintBoardBg = new Paint();
//    private Paint mPaintBoardBorder = new Paint();
//    private Paint mPaintCellFgB = new Paint();
//    private Paint mPaintCellFgW = new Paint();
//    private Paint mPaintCellAvB = new Paint();
//    private Paint mPaintCellAvW = new Paint();
//    private Paint mPaintTextFg = new Paint();
//    private Paint mPaintTurnRect = new Paint();
//
//    //Gson
//    //public JsonReader reader;
//
//
//    public ReversiView(Context context) {
//        super(context);
//        try(FileReader fileReader = new FileReader("//assets//result.json")){
//            Object ob = new JsonParser().parse(new FileReader("//assets//result.json"));
//
//            JSONObject json = (JSONObject) ob;
//
//            OneNameText = (String)json.get("PlayerOneName");
//            TwoNameText = (String)json.get("PlayerTwoName");
//            OneFirst = (int)json.get("PlayerOneFirst");
//            TwoFirst = (int)json.get("PlayerTwoFirst");
//
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        //TwogameSelectionからのデータの受け取り
////        Intent intent = new Intent();
////        OneFirst = intent.getIntExtra("PlayerOneFirst", OneFirst);
////        TwoFirst = intent.getIntExtra("PlayerTwoFirst", TwoFirst);
////        OneNameText = intent.getStringExtra("PlayerOneName");
////        TwoNameText = intent.getStringExtra("PlayerTwoName");
//////        common = (Common)this.getApplication();
////        Log.d("代入テスト", "OneFirst: " + OneFirst + " OneNameText:" + OneNameText);
//
//
//        setFocusable(true);
//
//
//        mPaintScreenBg.setColor(getResources().getColor(R.color.screen_bg));
//        mPaintBoardBg.setColor(getResources().getColor(R.color.board_bg));
//        mPaintBoardBorder.setColor(getResources().getColor(R.color.board_border));
//        mPaintCellFgB.setColor(getResources().getColor(R.color.cell_fg_black));
//        mPaintCellFgW.setColor(getResources().getColor(R.color.cell_fg_white));
//        mPaintCellAvB.setColor(getResources().getColor(R.color.cell_fg_black));
//        mPaintCellAvW.setColor(getResources().getColor(R.color.cell_fg_white));
//        mPaintTextFg.setColor(getResources().getColor(R.color.text_fg));
//        mPaintTurnRect.setColor(getResources().getColor(R.color.turn_rect));
//
//        //アンチエイリアス
//        mPaintCellFgB.setAntiAlias(true);
//        mPaintCellFgW.setAntiAlias(true);
//        mPaintCellAvB.setAntiAlias(true);
//        mPaintCellAvW.setAntiAlias(true);
//
//        mPaintCellAvB.setAlpha(32);
//        mPaintCellAvW.setAlpha(64);
//
//        mPaintTextFg.setAntiAlias(true);
//        mPaintTextFg.setStyle(Paint.Style.FILL);
//
//        Resources res = getResources();
//        int fontSize = res.getDimensionPixelSize(R.dimen.font_size_status);
//        mPaintTextFg.setTextSize(fontSize);
//
//        mPaintTurnRect.setAntiAlias(true);
//        mPaintTurnRect.setAlpha(128);
//        mPaintTurnRect.setStyle(Paint.Style.STROKE);
//        mPaintTurnRect.setStrokeWidth(5f);
//
//
////        try{
////            JsonReader reader = new JsonReader(new FileReader("sample.json"));
////            reader.beginObject();
////            while(reader.hasNext()){
////                String name = reader.nextName();
////                if(name.equals("PlayerOneName")){
////                    OneNameText = reader.nextString();
////                }else if(name.equals("PlayerTwoName")){
////                    TwoNameText = reader.nextString();
////                }else if(name.equals("PlayerOneFirst")){
////                    OneFirst = reader.nextInt();
////                }else if(name.equals("PlayerTwoFirst")){
////                    TwoFirst = reader.nextInt();
////                }
////                reader.endObject();;
////                reader.close();
////            }
////        } catch (FileNotFoundException e) {
////            e.printStackTrace();
////        } catch (IOException e) {
////            e.printStackTrace();
////        }
//        //Log.d("Json", OneNameText);
//
//
//    }
//
//    @Override
//    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);
//
//        this.mWidth = getWidth();
//        this.mHeight = getHeight();
//        mBoard.setSize(this.mWidth, this.mHeight);
//
//        drawBoard(canvas);
//    }
//
//    public void init(){
//        mBoard = new Board();
//        invalidate();
//    }
//
//
//    @Override
//    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
//        super.onSizeChanged(w, h, oldw, oldh);
//
//
//    }
//
//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        float x = event.getX();
//        float y = event.getY();
//
//        switch (event.getAction()) {
//            case MotionEvent.ACTION_UP:
//                int r = (int)(y / mBoard.getCellHeidht());
//                int c = (int)(x / mBoard.getCellWidth());
//                if (r < Board.ROWS && c < Board.COLS){
//                    List<Cell> changeCells = null;
//                    try {
//                        changeCells = mBoard.changeCell(r, c, mBoard.getTurn());
//                        int nextAvailableCellCount = mBoard.changeTurn(changeCells);
//                        if(nextAvailableCellCount == 0){
//                            if(mBoard.countBlankCells() == 0){
//                                finish();
//                            }else{
//                                showSkippMessage();
//                                nextAvailableCellCount = mBoard.changeTurn(changeCells);
//                                if(nextAvailableCellCount == 0){
//                                    finish();
//                                }
//                            }
//                        }
//                    } catch (Exception e) {
//                        //Toast.makeText(this.getContext(), e.getMessage(), 300).show();
//                    } catch (InvalidMoveException e) {
//                        e.printStackTrace();
//                    }
//                    if(changeCells != null){
//                        for(Cell cell: changeCells){
//                            invalidate(cell.getRect());
//                        }
//                    }
//
//                    //invalidate();			//画面を再描画
//                }
//                break;
//            default:
//                return true;
//        }
//        return true;
//    }
//
//    private void finish(){
//        mBoard.setFinished();;
//        showCountsToast();
//    }
//
//    public void showCountsToast(){
//        Cell.E_STATUS winner = mBoard.getWinner();
//        String message;
//        if(OneFirst == 1){
//            //message = OneNameText + mBoard.countCells(E_STATUS.Black) + "\n"
//            //       + TwoNameText + mBoard.countCells(E_STATUS.White) + "\n\n";
//            message = "Black" + mBoard.countCells(E_STATUS.Black) + "\n"
//                    + "White" + mBoard.countCells(E_STATUS.White) + "\n\n";
//        }else{
////            message = TwoNameText + mBoard.countCells(E_STATUS.Black) + "\n"
////                    + OneNameText + mBoard.countCells(E_STATUS.White) + "\n\n";
//            message = "Black" + mBoard.countCells(E_STATUS.Black) + "\n"
//                    + "White" + mBoard.countCells(E_STATUS.White) + "\n\n";
//        }
////        String message = "Black:" + mBoard.countCells(E_STATUS.Black) + "\n"
////                       + "White:" + mBoard.countCells(E_STATUS.White) + "\n\n";
//
//        if(mBoard.isFinished()){
//            String winner_color = Cell.statusToString(winner);
//            if(winner_color == "Black"){
//                if(OneFirst == 1){
//                    message += "Winner is:" + OneNameText + "!!";
//                }else{
//                    message += "Winner is:" + TwoNameText + "!!";
//                }
//
//            }else{
//                if(winner_color == "White"){
//                    if(OneFirst == 2){
//                        message += "Winner is:" + OneNameText + "!!";
//                    }else{
//                        message += "Winner is:" + TwoNameText + "!!";
//                    }
//                }
//            }
//            //message += "Winner is:" + Cell.statusToDisplay(winner) + "!!";
//        }else{
//            if(winner != Cell.E_STATUS.None){
//                message += Cell.statusToDisplay(winner) + "is winning...\n\n";
//            }
//            message += showTurnMessage() + "'s turn";
//        }
//        Toast.makeText(this.getContext(), message, Toast.LENGTH_LONG).show();
//        Log.d(TAG, message);
//
//    }
//
//    private void showSkippMessage(){
//        String message = Cell.statusToDisplay(mBoard.getTurn()) + " has been skipped.";
//        Toast.makeText(this.getContext(), message, Toast.LENGTH_SHORT).show();
//        Log.d(TAG, message);
//    }
//
//    private String showTurnMessage(){
//        String turnmsg = mBoard.getTurnDisplay();
//        String ansmsg = "";
//        if(OneFirst == 1){
//            if(turnmsg == "Black"){
//                ansmsg += OneNameText;
//            }else{
//                ansmsg += TwoNameText;
//            }
//        }else if(OneFirst == 2){
//            if(turnmsg == "White"){
//                ansmsg += OneNameText;
//            }else{
//                ansmsg += TwoNameText;
//            }
//        }
//        return ansmsg;
//    }
//
//    /**
//     * オセロ盤の描画
//     * オセロ盤の描画
//     * @param canvas
//     */
//    private void drawBoard(Canvas canvas){
//        float bw = mBoard.getRectF().width();
//        float bh = mBoard.getRectF().height();
//        float cw = mBoard.getCellWidth();
//        float ch = mBoard.getCellHeidht();
//        float width = cw * CELL_SIZE_FACTOR;
//        boolean show_hints = Pref.getShowHints(getContext());
//
//        if (mBoard.getRectF().width() <=0 ) return;
//
//        Paint paint = new Paint();
//        paint.setColor(Color.rgb(0, 180, 0));
//
//        canvas.drawRect( 0, 0, bw, bh, paint);
//
//        paint.setColor(Color.rgb(40, 40, 40));		//罫線の色
//
//        //縦線
//        for (int i = 0; i < Board.COLS; i++) {
//            canvas.drawLine(cw * (i+1), 0, cw * (i+1), bh, paint);
//        }
//        //横線
//        for (int i = 0; i < Board.ROWS; i++) {
//            canvas.drawLine(0, ch * (i+1), bw, ch * (i+1), paint);
//        }
//
//        //円を描く前にアンチエイリアスを指定。これをしないと円がギザギザになる。
//        paint.setAntiAlias(true);
//
//        Cell[][] cells = mBoard.getCells();
//        for (int i = 0; i < Board.ROWS; i++) {
//            for (int j = 0; j < Board.COLS; j++) {
//                Cell cell =cells[i][j];
//                Cell.E_STATUS st = cell.getStatus();
//
//                if (st == E_STATUS.Black){
//                    paint.setColor(Color.BLACK);
//                } else if(st == E_STATUS.White){
//                    paint.setColor(Color.WHITE);
//                } else{
//                    drawHints(cell, canvas, cw, show_hints);
//                }
//
//                if (st != E_STATUS.None){
//                    canvas.drawCircle(cell.getCx(), cell.getCy(), (float) (cw * 0.46), paint);
//                }
//            }
//        }
//
//        //手番の表示と現在の黒と白の数の表示
//        drawStatus(canvas);
//    }
//
//    /**
//     * 配置可能場所を示す
//     * @param cell
//     * @param Cell_w
//     * @param show_hints
//     */
//    private void drawHints(Cell cell, Canvas canvas, float Cell_w, boolean show_hints){
//        if(!show_hints){
//            return;
//        }
//
//        float answer_w = Cell_w * 0.1f;
//
//        //次に配置可能なセルであれば，小さな丸を表示する
//        if(cell.getReversibleCells().size() > 0){
//            if(mBoard.getTurn() == Cell.E_STATUS.Black){
//                canvas.drawCircle(cell.getCx(), cell.getCy(), answer_w, mPaintCellAvB);
//            }else{
//                canvas.drawCircle(cell.getCx(), cell.getCy(), answer_w, mPaintCellAvW);
//            }
//        }else{
//            canvas.drawCircle(cell.getCx(), cell.getCy(), answer_w, mPaintBoardBg);
//        }
//    }
//
//    private void drawStatus(Canvas canvas){
//        Resources res = getResources();
//        float turn_rect_inset = res.getDimension(R.dimen.turn_rect_inset);
//        float turn_rect_round = res.getDimension(R.dimen.turn_rect_round);
//        float turn_circle_x = res.getDimension(R.dimen.turn_circle_x);
//        float turn_circle_y = res.getDimension(R.dimen.turn_circle_y);
//        float turn_text_x = res.getDimension(R.dimen.turn_text_x);
//        float turn_text_y = res.getDimension(R.dimen.turn_text_y);
//        float top = mBoard.getRectF().bottom;
//        float center = mBoard.getRectF().width() / 2f;
//
//        RectF rect;
//        if (mBoard.getTurn() == E_STATUS.Black){
//            rect = new RectF(turn_rect_inset, top + turn_rect_inset, center - turn_rect_inset, mHeight - turn_rect_inset);
//        } else {
//            rect = new RectF(center + turn_rect_inset, top + turn_rect_inset, mWidth - turn_rect_inset, mHeight - turn_rect_inset);
//        }
//        mPaintTurnRect.setStyle(Paint.Style.FILL);
//        mPaintTurnRect.setAlpha(128);
//        canvas.drawRoundRect(rect, turn_rect_round, turn_rect_round, mPaintTurnRect);	//背景
//        mPaintTurnRect.setStyle(Paint.Style.STROKE);
//        mPaintTurnRect.setAlpha(255);
//        canvas.drawRoundRect(rect, turn_rect_round, turn_rect_round, mPaintTurnRect);	//枠
//
//
//        canvas.drawCircle(turn_circle_x, top + turn_circle_y, mBoard.getCellWidth() * CELL_SIZE_FACTOR, mPaintCellFgB);
//        String s = String.valueOf(mBoard.countCells(E_STATUS.Black));
//        canvas.drawText(s, turn_text_x, top + turn_text_y, mPaintTextFg);
//
//        canvas.drawCircle(center + turn_circle_x, top + turn_circle_y, mBoard.getCellWidth() * CELL_SIZE_FACTOR, mPaintCellFgB);
//        canvas.drawCircle(center + turn_circle_x, top + turn_circle_y, mBoard.getCellWidth() * CELL_SIZE_FACTOR * 0.94f, mPaintCellFgW);
//        s = String.valueOf(mBoard.countCells(E_STATUS.White));
//        canvas.drawText(s, center + turn_text_x, top + turn_text_y, mPaintTextFg);
//
//        invalidate(0, (int)mBoard.getRectF().bottom, mWidth, mHeight);
//
//    }
//
//    public String getState(){
//        String s = mBoard.getStateString();
//        Log.d(TAG, "onPause: value=" + s);
//        return s;
//    }
//
//    public void setState(String value){
//        if(TextUtils.isEmpty(value)) return;
//
//        Log.d(TAG, "onResume: value=" + value);
//        mBoard.loadFromStateString(value);
//    }

}
