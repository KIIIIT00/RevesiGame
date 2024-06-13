package jp.ac.ristumei.ise.phy.exp2.is0611if.reversigame.model;

import android.graphics.Point;

public interface IPlayerCallback {
    public void onEndThinking(Point pos);
    public void onProgress();
    public void onPointStarted(Point pos);
    public void onPointEnded(Point pos);
}
