package jp.ac.ristumei.ise.phy.exp2.is0611if.reversigame;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Random;

public class Intro1 extends AppCompatActivity {
    private TextView countQ;
    private Button ansBtn1, ansBtn2, ansBtn3, ansBtn4;
    private String rightAns;
    private int rightAnsCnt;
    private int quizCount = 1;
    static final private int QUIZ_COUNT = 14;
    private MediaPlayer media;


    ArrayList<ArrayList<String>> quizArray = new ArrayList<ArrayList<String>>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro1);

        //初期設定
        countQ  = findViewById(R.id.quizCnt);
        ansBtn1 = findViewById(R.id.select1);
        ansBtn2 = findViewById(R.id.select2);
        ansBtn3 = findViewById(R.id.select3);
        ansBtn4 = findViewById(R.id.select4);

        setVolumeControlStream(AudioManager.STREAM_MUSIC);

        String[][] quizdata= {
                //MediaPlayer, 正解, 選択肢1, 選択肢2, 選択肢3，選択肢4
                {String.valueOf(R.raw.dandan), "Dandadan", "ハルジオン", "Inside Of Me", "The band"},
                {String.valueOf(R.raw.dqn), "DQNなりたい,40代で死にたい","良いDJ", "5RATS", "繋いだ手から"},
                {String.valueOf(R.raw.fade), "fade", "まごパワー", "1999", "namida"},
                {String.valueOf(R.raw.insideofme), "Inside Of Me", "ペンペン", "君へのラブソング", "LIFE IS BEAUTIFUL"},
                {String.valueOf(R.raw.keepgoing), "Keep going", "バームクーヘン", "soup", "Letter"},
                {String.valueOf(R.raw.killingme), "KiLLiNG ME", "青いの", "鏡", "FUZ LOVE"},
                {String.valueOf(R.raw.letmepunk), "Let It Punk", "Cycle", "Just", "Jumper"},
                {String.valueOf(R.raw.monomane), "モノマネ", "ICHIDAIJI", "BLUE", "ラブコール"},
                {String.valueOf(R.raw.myskin), "Living In My Skin", "光","再生","クリスマスソング"},
                {String.valueOf(R.raw.ritsumeikan), "立命館大学校歌",  "ワールドイズマイン", "ギター", "いつか"},
                {String.valueOf(R.raw.shikisai), "色彩","あるいは映画のような","クリーム","a.m.3:21"},
                {String.valueOf(R.raw.sorrow), "Endless Sorrow", "2nd Youth",  "Over", "California"},
                {String.valueOf(R.raw.thelast), "THE LAST ROCKSTARS", "紅", "生活","かくれんぼ"},
                {String.valueOf(R.raw.upup), "時給アップアップソング", "バンドマンきらいかも", "閃光","いいから"},
        };

        //quizdataからクイズ出題用のquizArrayを作成
        for(int i = 0; i < quizdata.length; i++){
            //新しいArrayListを準備
            ArrayList<String> tmpArray = new ArrayList<>();

            //クイズデータを追加
            tmpArray.add(quizdata[i][0]); //mp4
            tmpArray.add(quizdata[i][1]);//正解
            tmpArray.add(quizdata[i][2]);//選択肢1
            tmpArray.add(quizdata[i][3]);//選択肢2
            tmpArray.add(quizdata[i][4]);//選択肢3

            quizArray.add(tmpArray);
        }

        shownextQuiz();
    }

    public void audioPlay(String mediaString){
        media = MediaPlayer.create(this, Integer.valueOf(mediaString));
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        if(media != null){
            media.start();
        }
    }

    public void shownextQuiz(){
        countQ.setText("Q" + quizCount);

        //ランダムな数字を取得
        Random random = new Random();
        int randomnum = random.nextInt(quizArray.size());

        //randomNumを使って，quizArrayからクイズを1つ取り出す
        ArrayList<String> quiz = quizArray.get(randomnum);

        //問題の音声を流す
        audioPlay(quiz.get(0));

        //正解をrightAnsにセット
        rightAns = quiz.get(1);

        //クイズ配列から音声を削除
        quiz.remove(0);

        //正解と選択肢4つシャッフル
        Collections.shuffle(quiz);

        //解答ボタンに正解と選択肢3つを表示
        ansBtn1.setText(quiz.get(0));
        ansBtn2.setText(quiz.get(1));
        ansBtn3.setText(quiz.get(2));
        ansBtn4.setText(quiz.get(3));

        //このクイズをquizArrayから削除
        quizArray.remove(randomnum);

    }

    public void checkANs(View view){
        media.stop();
        //どの解答ボタンが押されたか
        Button answeBtn = findViewById(view.getId());
        String btnText = answeBtn.getText().toString();

        String alertTitle;
        if(btnText.equals(rightAns)){
            alertTitle = "正解!!";
            rightAnsCnt++;
        }else{
            alertTitle = "不正解!!";
        }

        //ダイアログを作成
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(alertTitle);
        builder.setMessage("答え:" + rightAns);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (quizCount == QUIZ_COUNT) {
                    // 結果画面へ移動
                    Intent intent = new Intent(Intro1.this, ResultActivity.class);
                    intent.putExtra("RIGHT_ANSWER_COUNT", rightAnsCnt);
                    startActivity(intent);
                } else {
                    quizCount++;
                    shownextQuiz();
                }
            }
        });
        builder.setCancelable(false);
        builder.show();
    }
}