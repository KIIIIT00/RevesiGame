package jp.ac.ristumei.ise.phy.exp2.is0611if.reversigame.model;

import jp.ac.ristumei.ise.phy.exp2.is0611if.reversigame.model.Cell1.E_STATUS;

public class HumanPlayer extends Player{

    public HumanPlayer(E_STATUS turn, String name, Board1 board) {
        super(turn, name, board);
    }

    @Override
    public boolean isHuman() {
        return true;
    }

    @Override
    public void startThinking(IPlayerCallback callback) {
        callback.onEndThinking(null);
    }

    @Override
    public void stopThinking() {

    }
}
