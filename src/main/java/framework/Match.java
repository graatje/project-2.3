package framework;

import framework.board.Board;
import framework.player.LocalPlayer;
import framework.player.Player;
import framework.player.ServerPlayer;
import ttt.board.TTTBoard;

public class Match {
    private String opponentName;

    private String gameType;
    private Integer challengeNR;

    public Integer getChallengeNR() {
        return challengeNR;
    }

    public Match(String opponentName, String gameType, String challengeNR)
    {
        this.opponentName = opponentName;

        try{
            this.challengeNR = Integer.parseInt(challengeNR);
        }catch (NumberFormatException e){
            e.printStackTrace();
        }
    }

    public String getOpponentName() {
        return opponentName;
    }
}
