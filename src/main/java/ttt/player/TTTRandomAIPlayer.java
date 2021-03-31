package ttt.player;

import framework.board.Board;
import framework.board.BoardPiece;
import framework.player.AIPlayer;
import framework.player.Player;

import java.util.ArrayList;
import java.util.List;

public class TTTRandomAIPlayer extends AIPlayer {
    public TTTRandomAIPlayer(Board board, String name) {
        super(board, name);
    }

    @Override
    public void requestMove() {
        try{
            Thread.sleep(500);
        }catch(InterruptedException ignored) {}

        List<BoardPiece> validMoves = board.getValidMoves();
        BoardPiece move = validMoves.get((int) (Math.random() * validMoves.size()));

        board.makeMove(this, move);
    }
    public void requestMediumMove()
    {
    	Player currentPlayer = board.getCurrentPlayer();
    	int opponentPlayerid = currentPlayer.getID() ==1? 0:1;
        int[] playerids = {currentPlayer.getID(), opponentPlayerid};
    	for(int id: playerids)
    	{
	        for(BoardPiece boardPiece:getPlayerBoardPieces(id))
	        {
	        	for(int x=-1; x<=1;x++)
	            {
	        		if(boardPiece.getX() + x < 0 || boardPiece.getX() + x >=3)
	        		{
	        			continue;
	        		}
	            	for(int y=-1;y<=1;x++)
	            	{
	            		if(boardPiece.getY() + y <= 0 || boardPiece.getY() + y >=3)
	            		{
	            			continue;
	            		}
	            		if(y==0 && x==0)
	            		{
	            			continue;
	            		}
	            		
	            		if(calculateLines(boardPiece.getX(), boardPiece.getY(), id))
	            		{
	            			if(Math.random() * 100 < 50)
	            			{
		            			board.makeMove(this, board.getBoardPiece(boardPiece.getX() + x, boardPiece.getY() + y));
		            			return;
	            			}
	            		}
	            		if(!(board.getBoardPiece(boardPiece.getX() + x, boardPiece.getY() + y).hasOwner()))
	            		{
	            			if(Math.random() * 100 < 50)
	            			{
		            			board.makeMove(this, board.getBoardPiece(boardPiece.getX() + x, boardPiece.getY() + y));
		            			return;
	            			}
	            		}
	            	}
	            }
	        }
    	}
    	List<BoardPiece> validMoves = board.getValidMoves();
    	BoardPiece move = validMoves.get((int) (Math.random() * validMoves.size()));
    }
    public void requestHardMove()
    {
    	try{
            Thread.sleep(500);
        }catch(InterruptedException ignored) {}

    	Player currentPlayer = board.getCurrentPlayer();
    	int opponentPlayerid = currentPlayer.getID() ==1? 0:1;
        int[] playerids = {currentPlayer.getID(), opponentPlayerid};
        // first check if you can do a winning move and do so if you can.
        // then check if opponent can do a winning move and block it if you can.
    	for(int id: playerids)
    	{
	        for(BoardPiece boardPiece:getPlayerBoardPieces(id))
	        {
	        	for(int x=-1; x<=1;x++)
	            {
	        		if(boardPiece.getX() + x < 0 || boardPiece.getX() + x >=3)
	        		{
	        			continue;
	        		}
	            	for(int y=-1;y<=1;x++)
	            	{
	            		if(boardPiece.getY() + y <= 0 || boardPiece.getY() + y >=3)
	            		{
	            			continue;
	            		}
	            		if(y==0 && x==0)
	            		{
	            			continue;
	            		}
	            		
	            		if(calculateLines(boardPiece.getX(), boardPiece.getY(), id))
	            		{
	            			board.makeMove(this, board.getBoardPiece(boardPiece.getX() + x, boardPiece.getY() + y));
	            			return;
	            		}
	            		if(!(board.getBoardPiece(boardPiece.getX() + x, boardPiece.getY() + y).hasOwner()))
	            		{
	            			board.makeMove(this, board.getBoardPiece(boardPiece.getX() + x, boardPiece.getY() + y));
	            			return;
	            		}
	            	}
	            }
	        }
    	}
    
        int[][] preferredMoves = {{0,0}, {2,2}, {0,2}, {2,0}, {1,1}};
        List<BoardPiece> validMoves = board.getValidMoves();
        //preferred move
        for(int[] coord:preferredMoves)
        {
        	for(BoardPiece boardPiece: validMoves)
        	{
        		if(boardPiece.getX()==coord[0] || boardPiece.getY()==coord[1])
        		{
        			board.makeMove(this, board.getBoardPiece(boardPiece.getX(), boardPiece.getY()));
        			return;
        		}
        	}
        }
        // if we haven't got a preferred piece make random move.
        BoardPiece move = validMoves.get((int) (Math.random() * validMoves.size()));
        board.makeMove(this, move);
    }
    private boolean calculateLines(int xpoint, int ypoint, int playerid)
    {
    	int[][][]  somelist = {{{xpoint-1, ypoint}, {xpoint+1, ypoint}}, {{xpoint-1, ypoint-1}, {xpoint+1, ypoint-1}}, {{xpoint, ypoint-1}, {xpoint, ypoint+1}}}; 
    	for(int[][] cross:somelist)
    	{
    		try {
    			if(board.getBoardPiece(cross[0][0], cross[0][1]).getOwner().getID()==playerid && board.getBoardPiece(cross[1][0], cross[1][1]).getOwner().getID()==playerid)
    			{
    				return true;
    			}
    		}
    		catch(IndexOutOfBoundsException e)
    		{
    		}
    	}
    	return false;
    	
    }
     private List<BoardPiece> getPlayerBoardPieces(int playerid)
     {
    	 List<BoardPiece> boardpiecelist = new ArrayList<>();
    	 for(int x=0;x<board.getWidth();x++)
    	 {
    		 for(int y=0;y<board.getHeight();y++)
    		 {
    			 if(board.getBoardPiece(x, y).getOwner().getID()==playerid)
    			 {
    				 boardpiecelist.add(board.getBoardPiece(x, y));
    			 }
    		 }
    	 }
    	 return boardpiecelist;
     }
}
