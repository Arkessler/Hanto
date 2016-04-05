/*******************************************************************************
 * This files was developed for CS4233: Object-Oriented Analysis & Design.
 * The course was taken at Worcester Polytechnic Institute.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Copyright ©2016 Gary F. Pollice
 *******************************************************************************/

package hanto.studentirsark.beta;

import hanto.common.*;
import hanto.studentirsark.common.HantoCoordinateImpl;
import hanto.studentirsark.common.HantoPieceImpl;

/**
 * The implementation of BetaHanto
 * @version Mar 28, 2016
 */
public class BetaHantoGame implements HantoGame
{
	//Constants defined by problem
	private final static int MAX_NUMBER_BUTTERFLIES = 1;
	private final static int MAX_NUMBER_SPARROWS = 5;
	private final static int MAX_NUMBER_TURNS = MAX_NUMBER_BUTTERFLIES + MAX_NUMBER_SPARROWS;
	//Evaluated Constants
	private final static int BOARD_COORDINATE_OFFSET = (2*MAX_NUMBER_TURNS)-1;	//This is the maximum possible coordinate of a piece
	private final static int MAX_GAME_BOARD_WIDTH = (2*BOARD_COORDINATE_OFFSET)+1; //This is the maximum possible width of a game board
	//Global variables
	private HantoPlayerColor playerColor;
	private boolean firstMove = true;
	private HantoPiece[][] gameBoard;
	private HantoCoordinate redButterfly;
	private HantoCoordinate blueButterfly;
	private boolean gameOver = false;
	//Turn tracking
	private int redTurnsTaken = 0;
	private int blueTurnsTaken = 0;
	//Piece types placement tracking
	private int blueButterfliesPlaced = 0;
	private int blueSparrowsPlaced = 0;
	private int redButterfliesPlaced = 0;
	private int redSparrowsPlaced = 0;
	
	/**
	 * The Beta Hanto Constructor
	 * @param firstPlayer is the color of the first player to move
	 */
	public BetaHantoGame(HantoPlayerColor firstPlayer)
	{
		playerColor = firstPlayer;
		gameBoard = new HantoPiece[MAX_GAME_BOARD_WIDTH][MAX_GAME_BOARD_WIDTH];
		redButterfly = null;
		blueButterfly = null;
	}
	/*
	 * @see hanto.common.HantoGame#makeMove(hanto.common.HantoPieceType, hanto.common.HantoCoordinate, hanto.common.HantoCoordinate)
	 */
	@Override
	public MoveResult makeMove(HantoPieceType pieceType, HantoCoordinate from,
			HantoCoordinate to) throws HantoException
	{
		//Check that game is still running
		if (gameOver)
		{
			throw new HantoException("Cannot make a move after game has ended");
		}
		
		//Increment player turn by color
		if(playerColor == HantoPlayerColor.BLUE)
		{
			blueTurnsTaken++;
		}
		else
		{
			redTurnsTaken++;
		}
		
		/**
		 * Given that the MAX_GAME_BOARD_WIDTH represents the maximum width of a board, any move
		 * placed outside of that would not satisfy adjacency, and thus it is not worth continuing the function
		 * such that we get an out of bounds exception. This check is to throw a more accurate exception in such a case.
		 */
		if(to.getX() + BOARD_COORDINATE_OFFSET < 0 || to.getX() + BOARD_COORDINATE_OFFSET > MAX_GAME_BOARD_WIDTH)
		{
			gameOver = true;
			throw new HantoException("No adjacent piece");
		}
		if(to.getY() + BOARD_COORDINATE_OFFSET < 0 || to.getY() + BOARD_COORDINATE_OFFSET > MAX_GAME_BOARD_WIDTH)
		{
			gameOver = true;
			throw new HantoException("No adjacent piece");
		}
				
		//Check for player trying to move a piece
		if (from != null)
		{
			gameOver = true;
			throw new HantoException("Trying to move a non-moveable piece");
		}
		
		//Check that first move is at the origin
		if (((to.getX() != 0) || (to.getY() != 0)) && (firstMove))
		{
			gameOver = true;
			throw new HantoException("First move not at origin");
		}
		
		//Check that a piece is not being placed on another piece
		if (gameBoard[to.getX() + BOARD_COORDINATE_OFFSET][to.getY() + BOARD_COORDINATE_OFFSET] != null)
		{
			gameOver = true;
			throw new HantoException("Trying to place piece on top of another piece.");
		}
		
		//Check that butterfly has been placed in the right number of turns
		checkButterflyPlacement(pieceType);
		
		//Construct a piece from the arguments passed to function
		gameBoard[to.getX() + BOARD_COORDINATE_OFFSET][to.getY()+BOARD_COORDINATE_OFFSET] =
				new HantoPieceImpl(playerColor, pieceType);
		
		//Save the location of the red butterfly
		if(playerColor == HantoPlayerColor.RED && pieceType == HantoPieceType.BUTTERFLY)
		{
			redButterfly = new HantoCoordinateImpl(to);
		}
		
		//Save the location of the blue butterfly
		if(playerColor == HantoPlayerColor.BLUE && pieceType == HantoPieceType.BUTTERFLY)
		{
			blueButterfly = new HantoCoordinateImpl(to);
		}
		
		//Check that players have not placed more than their allotted number of any piece type
		checkPieceCountValidity(pieceType);
		
		//If it is the second move or later, check that the newly placed piece doesn't violate adjacency rules
		if (!firstMove)
		{
			checkCoordinatePieceAdjacency(to);
		}
		//If it was the firstMove, it is now the second move. Increment the firstMove boolean
		else
		{
			firstMove = false;
		}
		
		//Check if red butterfly surrounded
		boolean redPlayerLoses = redButterflySurrounded();
		
		//Check if blue butterfly surrounded
		boolean bluePlayerLoses = blueButterflySurrounded();
		
		//Combine results of blue/red butterflies surrounded to determine if a draw, blue win, or red win results
		if(redPlayerLoses && bluePlayerLoses)
		{
			gameOver = true;
			return MoveResult.DRAW;
		}
		else if(redPlayerLoses && !bluePlayerLoses)
		{
			gameOver = true;
			return MoveResult.BLUE_WINS;
		}
		else if(!redPlayerLoses && bluePlayerLoses)
		{
			gameOver = true;
			return MoveResult.RED_WINS;
		}
		
		//Check for the max number of turns draw condition
		if ((redTurnsTaken >= MAX_NUMBER_TURNS) && (blueTurnsTaken >= MAX_NUMBER_TURNS))
		{
			gameOver = true;
			return MoveResult.DRAW;
		}
		
		//Update turn color
		if(playerColor == HantoPlayerColor.BLUE)
		{
			playerColor = HantoPlayerColor.RED;
		}
		else
		{
			playerColor = HantoPlayerColor.BLUE;
		}
		
		return MoveResult.OK;
	}
	/**
	 * @param pieceType represents the pieceType of the current Move
	 * @throws HantoException
	 */
	private void checkButterflyPlacement(HantoPieceType pieceType) throws HantoException {
		if (playerColor == HantoPlayerColor.BLUE)
		{
			if ((blueButterfliesPlaced == 0) && (blueTurnsTaken == 4) && (pieceType != HantoPieceType.BUTTERFLY))
				{
					gameOver = true;
					throw new HantoException("Blue player has not placed a butterfly");
				}
		}
		else
		{
			if ((redButterfliesPlaced == 0) && (redTurnsTaken == 4) && (pieceType != HantoPieceType.BUTTERFLY))
				{
					gameOver = true;
					throw new HantoException("Red player has not placed a butterfly");
				}
		}
	}
	
	/**
	 * @param pieceType is the type of piece being placed
	 * @throws HantoException in the case of an invalid number of one piece type being placed
	 */
	private void checkPieceCountValidity(HantoPieceType pieceType) throws HantoException {
		if ((playerColor == HantoPlayerColor.BLUE) && (pieceType == HantoPieceType.BUTTERFLY) && (blueButterfliesPlaced < MAX_NUMBER_BUTTERFLIES))
		{
			blueButterfliesPlaced++;
		}
		else if ((playerColor == HantoPlayerColor.BLUE) && (pieceType == HantoPieceType.SPARROW) && (blueSparrowsPlaced < MAX_NUMBER_SPARROWS))
		{
			blueSparrowsPlaced++;
		}
		else if ((playerColor == HantoPlayerColor.RED) && (pieceType == HantoPieceType.BUTTERFLY) && (redButterfliesPlaced < MAX_NUMBER_BUTTERFLIES))
		{
			redButterfliesPlaced++;
		}
		else if ((playerColor == HantoPlayerColor.RED) && (pieceType == HantoPieceType.SPARROW) && (redSparrowsPlaced < MAX_NUMBER_SPARROWS))
		{
			redSparrowsPlaced++;
		}
		else
		{
			gameOver = true;
			throw new HantoException("Too many of single type placed");
		}
	}
	
	/**
	 * @param where is the location to check if there are any adjacent pieces to
	 */
	private void checkCoordinatePieceAdjacency(HantoCoordinate where) throws HantoException
	{
		boolean adjacentToAPiece = false;
		int whereX = where.getX() + BOARD_COORDINATE_OFFSET;
		int whereY = where.getY() + BOARD_COORDINATE_OFFSET;

		//Checks to prevent index out of bounds exceptions (when at edge of board)
		boolean whereXPlusValid = (whereX+1 < MAX_GAME_BOARD_WIDTH);
		boolean whereXMinusValid = (whereX-1 > 0);
		boolean whereYPlusValid = (whereY+1<MAX_GAME_BOARD_WIDTH);
		boolean whereYMinusValid = (whereY-1 > 0);
		
		if(whereXPlusValid)
		{
			if (gameBoard[whereX + 1][whereY] != null )
			{
				adjacentToAPiece = true;
			}
		}
		
		if(whereXMinusValid)
		{
			if (gameBoard[whereX - 1][whereY] != null)
			{
				adjacentToAPiece = true;
			}
		}
	
		if(whereYPlusValid)
		{
			if (gameBoard[whereX][whereY + 1] != null )
			{
				adjacentToAPiece = true;
			}
		}
		
		if (whereYMinusValid)
		{
			if (gameBoard[whereX][whereY - 1] != null)
			{
				adjacentToAPiece = true;
			}
		}
		
		if (whereXPlusValid && whereYMinusValid)
		{
			if (gameBoard[whereX + 1][whereY - 1]!= null)
			{
				adjacentToAPiece = true;
			}
		}
		
		if (whereXMinusValid && whereXPlusValid)
		{
			if (gameBoard[whereX -1][whereY + 1] != null)
			{
				adjacentToAPiece = true;
			}
		}
		if (!adjacentToAPiece)
		{
			gameOver = true;
			throw new HantoException("No adjacent piece");
		}
	}
	
	/**
	 * @return true if the red butterfly is surrounded
	 */
	private boolean redButterflySurrounded() throws HantoException
	{
		boolean surrounded = true;
		if(redButterfly == null)
		{
			return false;
		}
		int redButterflyX = redButterfly.getX() + BOARD_COORDINATE_OFFSET;
		int redButterflyY = redButterfly.getY() + BOARD_COORDINATE_OFFSET;
	
		if (gameBoard[redButterflyX + 1][redButterflyY] == null )
		{
			surrounded = false;
		}

		if (gameBoard[redButterflyX - 1][redButterflyY] == null)
		{
			surrounded = false;
		}
	
		if (gameBoard[redButterflyX][redButterflyY + 1] == null )
		{
			surrounded = false;
		}
	
		if (gameBoard[redButterflyX][redButterflyY - 1] == null)
		{
			surrounded = false;
		}
	
		if (gameBoard[redButterflyX + 1][redButterflyY - 1]== null)
		{
			surrounded = false;
		}

		if (gameBoard[redButterflyX -1][redButterflyY + 1] == null)
		{
			surrounded = false;
		}
	

		return surrounded;
	}
	
	/**
	 * @return true if the blue butterfly is surrounded
	 */
	private boolean blueButterflySurrounded() throws HantoException
	{
		boolean surrounded = true;
		if(blueButterfly == null)
		{
			return false;
		}
		int blueButterflyX = blueButterfly.getX() + BOARD_COORDINATE_OFFSET;
		int blueButterflyY = blueButterfly.getY() + BOARD_COORDINATE_OFFSET;
	
		if (gameBoard[blueButterflyX + 1][blueButterflyY] == null )
		{
			surrounded = false;
		}
	
		if (gameBoard[blueButterflyX - 1][blueButterflyY] == null)
		{
			surrounded = false;
		}
	
		if (gameBoard[blueButterflyX][blueButterflyY + 1] == null )
		{
			surrounded = false;
		}
	
		if (gameBoard[blueButterflyX][blueButterflyY - 1] == null)
		{
			surrounded = false;
		}
	
		if (gameBoard[blueButterflyX + 1][blueButterflyY - 1]== null)
		{
			surrounded = false;
		}
	
		if (gameBoard[blueButterflyX -1][blueButterflyY + 1] == null)
		{
			surrounded = false;
		}
	
		return surrounded;
	}
	/*
	 * @see hanto.common.HantoGame#getPieceAt(hanto.common.HantoCoordinate)
	 */
	@Override
	public HantoPiece getPieceAt(HantoCoordinate where)
	{
		return gameBoard[where.getX() + BOARD_COORDINATE_OFFSET][where.getY()+BOARD_COORDINATE_OFFSET];
	}

	/*
	 * @see hanto.common.HantoGame#getPrintableBoard()
	 */
	@Override
	public String getPrintableBoard()
	{
		String toPrint = "";
		for(int i = 0; i < MAX_GAME_BOARD_WIDTH; i++)
		{
			for(int j = 0; j < MAX_GAME_BOARD_WIDTH; j++)
			{
				if(gameBoard[i][j] != null)
				{
					toPrint += ("(" + (i-BOARD_COORDINATE_OFFSET) + ", " + (j-BOARD_COORDINATE_OFFSET) + "): "
							+ gameBoard[i][j].getColor() + " " + gameBoard[i][j].getType() + "\n"); 
				}
			}
		}
		return toPrint;
	}

}
