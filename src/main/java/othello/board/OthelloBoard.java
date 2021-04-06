package othello.board;

import java.util.ArrayList;
import java.util.List;

import framework.GameManager;
import framework.board.Board;
import framework.board.BoardPiece;
import framework.player.Player;

public class OthelloBoard extends Board{

	public OthelloBoard(GameManager gameManager) {
		super(gameManager, 8, 8);
	}
	
	@Override
	public List<BoardPiece> getValidMoves(){
		List<BoardPiece> validMoves = new ArrayList<BoardPiece>();
		for(int y=0;y<height;y++) {
			for(int x=0;y<width;x++) {
				BoardPiece boardPiece = getBoardPiece(x,y);
				if(!boardPiece.hasOwner()) {
					if(checkValidMove(boardPiece)) {
						validMoves.add(boardPiece);
					}
				}
			}
		}
		return validMoves;
	}
	
	public boolean checkValidMove(BoardPiece boardPiece){
		for(int y=-1;y<=1;y++){
			for(int x=-1;x<=1;x++) {
				if(x==0 && y==0) {
					continue;
				}
				if(checkLine(boardPiece.getX(), boardPiece.getY(), x, y)){
					return true;
					}
				}
			}
		return false;
		}

	private boolean checkLine(int initialx, int initialy, int xchange, int ychange)
	{
	    int x = initialx+xchange;
	    int y = initialy+ychange;
	    boolean initialized = false;
	    if(x>=0 && y>=0 && x<width && y<height){  // out of bounds check
	    	BoardPiece boardPiece = getBoardPiece(x,y);
	    	if(boardPiece.hasOwner() && boardPiece.getOwner().getID() != getCurrentPlayer().getID()) {
	    		initialized = true;
	    	}
	    }
	    
	    while(initialized && x+xchange>=0 && y+ychange>=0 && x+xchange<width && y+ychange<height)
	    {
	        x = x+xchange;
	        y = y+xchange;
	        BoardPiece boardPiece = getBoardPiece(x,y);
	        if(boardPiece.getOwner().getID() == getCurrentPlayer().getID()){  // check if the tile is you.
		     return true;
	        }
	        else if(!boardPiece.hasOwner()){
	            return false;
	        }
	    }
	    return false;
	}
	/**
	public boolean makeMove(int x, int y){
	    if(!checkValidMove(x,y)){
		return False;
	    }
	    
	}
	
	private void changeMoveLine(int initialx, int initialy, int xchange, int ychange){
	    ArrayList<BoardPiece> templist = new ArrayList<Boardpiece>();
	    int x = initialx+xchange;
	    int y = initialy+ychange;
	    boolean initialized = isOpponent(x,y);
	    if(initialized){templist.add(board.getBoardPiece(x,y));}
	
	    while(initialized && x>=0 && y>=0 && x<xboardsize && y<boardsize){
	        x = x+xchange;
		y = y+xchange;
	        if(isSelf(x,y))
	        {
		     break;
	        }
		else if(isOpponent){
			templist.add(board.getBoardPiece(x,y));
		}
	    }
	    for(BoardPiece boardPiece: templist){
		change the boardpiece to your collor
	    }
	    
	    return false;
	    */


	@Override
	public int getMinPlayers() {
		// TODO Auto-generated method stub
		return 0;
	}


	@Override
	public int getMaxPlayers() {
		// TODO Auto-generated method stub
		return 0;
	}





	@Override
	protected void executeMove(Player player, BoardPiece piece) {
		// TODO Auto-generated method stub
		
	}


	@Override
	protected boolean calculateIsGameOver() {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public Player calculateWinner() {
		// TODO Auto-generated method stub
		return null;
	}
}
