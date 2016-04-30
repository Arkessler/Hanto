/*******************************************************************************
 * This files was developed for CS4233: Object-Oriented Analysis & Design.
 * The course was taken at Worcester Polytechnic Institute.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package hanto.studentirsark.common;

import static hanto.common.HantoPieceType.BUTTERFLY;
import static hanto.common.HantoPlayerColor.BLUE;
import static hanto.common.HantoPlayerColor.RED;
import static hanto.common.MoveResult.BLUE_WINS;
import static hanto.common.MoveResult.DRAW;
import static hanto.common.MoveResult.OK;
import static hanto.common.MoveResult.RED_WINS;

import java.util.HashMap;
import java.util.List;

import hanto.common.HantoCoordinate;
import hanto.common.HantoException;
import hanto.common.HantoGame;
import hanto.common.HantoPiece;
import hanto.common.HantoPieceType;
import hanto.common.HantoPlayerColor;
import hanto.common.HantoPrematureResignationException;
import hanto.common.MoveResult;

/**
 * @author Irshusdock, Arkessler
 *
 */
public abstract class BaseHantoGame implements HantoGame{
	//Global variables
	protected GameBoard gameBoard;
	protected HantoPlayerColor playerColor;
	protected boolean firstMove = true;
	protected HantoCoordinate redButterfly;
	protected HantoCoordinate blueButterfly;
	protected boolean gameOver = false;
	protected boolean gameAcceptsResignations = false;
	protected PlayerState playerState;
	//Turn tracking
	protected int redTurnsTaken = 0;
	protected int blueTurnsTaken = 0;
	//Default maxTurnCount
	protected static int MAX_NUMBER_TURNS = Integer.MAX_VALUE;
	
	/**
	 * Constructor for BaseHantoGme
	 * @param firstPlayer the color of the player to move first
	 */
	public BaseHantoGame(HantoPlayerColor firstPlayer)
	{
		playerColor = firstPlayer;
		gameBoard = new GameBoard( new HashMap<HantoCoordinate, HantoPiece>());
	}
	
	/*
	 * (non-Javadoc)
	 * @see hanto.common.HantoGame#makeMove(hanto.common.HantoPieceType, hanto.common.HantoCoordinate, hanto.common.HantoCoordinate)
	 */
	public MoveResult makeMove(HantoPieceType pieceType, HantoCoordinate from, HantoCoordinate to)
			throws HantoException {
		
		//Check if the player is resigning
		MoveResult resignationCheck = checkResignation(pieceType, from, to);
		if (resignationCheck!=OK)
		{
			return resignationCheck;
		}
		//Instantiate coordinateImpl from to-coordinate
		HantoCoordinateImpl toCoordImpl= new HantoCoordinateImpl(to);
		//Instantiate piece from passed in information
		HantoPieceImpl pieceImpl = new HantoPieceImpl(playerColor, pieceType);
		
		//Increment player turn by color
		incrementTurnCounters();
			
		//Check that a butterfly has been placed within the correct number of turns
		checkButterflyPlacement(pieceType);
		
		//Check that game is not over
		if (gameOver)
		{
			throw new HantoException("Cannot move after game is over");
		}
		//Check that toCoordinate is empty
		if (gameBoard.containsKey(toCoordImpl))
		{
			throw new HantoException("Trying to place piece on existing piece");
		}
		
		//Check that first move is at 0,0
		if (firstMove && !(toCoordImpl.equals(new HantoCoordinateImpl(0, 0))))
		{
			throw new HantoException("First move needs to be at 0, 0");
		}
		
		applyMove(from, to, pieceImpl);
		updateButterflyLocation(pieceType, to);
		gameBoard.checkContiguity(to);
		
		MoveResult surroundedCheckResult = checkSurroundedWinCondition();
		
		firstMove = false;
		changePlayerColor();
		
		return checkMaxTurnCount(surroundedCheckResult);
	}

	/**
	 * @param pieceType
	 * @param from
	 * @param to
	 * @throws HantoPrematureResignationException 
	 */
	protected MoveResult checkResignation(HantoPieceType pieceType, HantoCoordinate from, HantoCoordinate to) throws HantoPrematureResignationException {
		if ((pieceType == null) && (from == null) && (to == null) && gameAcceptsResignations)
		{
			if (playerColor == RED)
			{
				return BLUE_WINS;
			}
			else
			{
				return RED_WINS;
			}
		}
		else
		{
			return OK;
		}
	}

	/**
	 * Check to see if the maximum number of turns has been reached
	 * @param surroundedCheckResult the current MoveResult from MakeMove
	 * @return the MoveResult based on if the maximum number of turns has been reached
	 */
	protected MoveResult checkMaxTurnCount(MoveResult surroundedCheckResult) {
		if ((redTurnsTaken >= MAX_NUMBER_TURNS) && (blueTurnsTaken >= MAX_NUMBER_TURNS))
		{
			gameOver = true;
			if (surroundedCheckResult == OK)
			{
				return DRAW;
			}
		}
		return surroundedCheckResult;
	}

	/* (non-Javadoc)
	 * @see hanto.common.HantoGame#getPieceAt(hanto.common.HantoCoordinate)
	 */
	@Override
	public HantoPiece getPieceAt(HantoCoordinate where) {
		return gameBoard.getPieceAt(where);
	}

	/**
	 * Verify that a piece being moved actually exists at the start location
	 * @param from the location that a piece will be moved from
	 * @throws HantoException 
	 */
	protected void checkMovingExistentPiece(HantoCoordinate from) throws HantoException {
		if (getPieceAt(from) == null)
		{
			throw new HantoException("Trying to move a non-existant piece");
		}
	}
	
	/* (non-Javadoc)
	 * @see hanto.common.HantoGame#getPrintableBoard()
	 */
	@Override
	public String getPrintableBoard() {
		return gameBoard.getPrintableBoard();
	}
	
	/**
	 * Determine if a move is placing a piece or moving a piece and then execute the move
	 * @param from the start location of the piece
	 * @param to the end location of the piece
	 * @param piece the HantoPiece being moved
	 * @throws HantoException
	 */
	protected void applyMove(HantoCoordinate from, HantoCoordinate to,
			HantoPiece piece) throws HantoException {
		if (from==null)	//If placing a new piece
		{
			playerState = playerState.checkPieceCountValidity(piece.getType());
			placePiece(to, piece);
		}
		else		//If moving a piece
		{
			HantoCoordinateImpl fromCoordinateImpl = new HantoCoordinateImpl(from); 
			checkMovingExistentPiece(from);
			if (getPieceAt(fromCoordinateImpl).getColor() != playerColor)
			{
				throw new HantoException("Can't move opponent's piece");
			}
			if (getPieceAt(fromCoordinateImpl).getType() != piece.getType())
			{
				throw new HantoException("Wrong piece type in makeMove");
			}
			checkButterflyPlacedBeforeMove();
			movePiece(from, to, piece);
		}
	}
	
	/**
	 * increments the turn counter for the current player
	 */
	protected void incrementTurnCounters() {
		if(playerColor == HantoPlayerColor.BLUE)
		{
			blueTurnsTaken++;
		}
		else
		{
			redTurnsTaken++;
		}
	}

	/**
	 * checks whether or not butterflies are surrounded
	 * @return The MoveResult of if any combination of butterflies are surronded
	 */
	protected MoveResult checkSurroundedWinCondition() {
		if (butterflySurrounded(RED) && butterflySurrounded(BLUE))
		{
			gameOver = true;
			return DRAW;
		}
		else if (butterflySurrounded(RED))
		{
			gameOver = true;
			return BLUE_WINS;
		}
		else if (butterflySurrounded(BLUE))
		{
			gameOver = true;
			return RED_WINS;
		}
		else
		{
			return OK;
		}
	}
	
	/**
	 * Check if the butterfly has been placed before allowing a player to move their pieces
	 * @throws HantoException 
	 * 
	 */
	protected void checkButterflyPlacedBeforeMove() throws HantoException {
		if (playerColor == BLUE)
		{
			if (blueButterfly == null)
			{
				throw new HantoException("Cannot move piece before placing butterfly");
			}
		}
		else
		{
			if (redButterfly == null)
			{
				throw new HantoException("Cannot move piece before placing butterfly");
			}
		}
	}
	
	/**
	 * Update the location of the butterfly
	 * @param pieceType the type of piece that was moved or placed
	 * @param to the location the piece was moved to or placed at
	 */
	private void updateButterflyLocation(HantoPieceType pieceType, HantoCoordinate to) {
		if (pieceType == BUTTERFLY)
		{
			if (playerColor == RED)
			{
				redButterfly = to;
			}
			else
			{
				blueButterfly = to;
			}
		}
	}

	/**
	 * Determine if a butterfly of a certain color is surronded
	 * @param color the color of the butterfly to check
	 * @return whether or not the butterfly of that color is surrounded
	 */
	protected boolean butterflySurrounded(HantoPlayerColor color)
	{
		boolean emptySpaceFound = false;
		HantoCoordinate butterflyLocation;
		if (color == RED)
		{
			butterflyLocation = redButterfly;
		}
		else
		{
			butterflyLocation = blueButterfly;
		}
		if (butterflyLocation == null)
		{
			return false;
		}
		HantoCoordinateImpl butterflyLocImpl = new HantoCoordinateImpl(butterflyLocation);
		for (HantoCoordinate coord: butterflyLocImpl.getAdjacentCoordinates())
		{
			if (getPieceAt(coord) == null)
			{
				emptySpaceFound = true;
			}
		}
		return !emptySpaceFound;
	}
	
	/**
	 *  changes the color of the active player
	 */
	protected void changePlayerColor() {
		if(playerColor == HantoPlayerColor.BLUE)
		{
			playerColor = HantoPlayerColor.RED;
		}
		else
		{
			playerColor = HantoPlayerColor.BLUE;
		}
	}
	
	/**
	 * Determine if a given coordinate is next to enemies or allies. Note: not used by BETA hanto
	 * @param where is the location to check if there are any adjacent pieces to
	 * @throws HantoException
	 */
	protected void checkCoordinatePieceAdjacency(HantoCoordinate where) throws HantoException
	{
		HantoCoordinateImpl originImpl = new HantoCoordinateImpl(where);
		boolean foundSameColorAdjacent = false;
		List<HantoCoordinate> surroundingCoordinates = originImpl.getAdjacentCoordinates();
		for (int i = 0; i < surroundingCoordinates.size(); i++)
		{
			HantoPiece pieceAtCoord = getPieceAt(surroundingCoordinates.get(i));
			if (pieceAtCoord != null)
			{
				if (pieceAtCoord.getColor() == playerColor)
				{
					foundSameColorAdjacent = true;
				}
				else
				{
					throw new HantoException("Cannot place piece next to opponent piece");
				}
			}
		}
		if (!foundSameColorAdjacent)
		{
			throw new HantoException("Must place piece next to piece of same color");
		}
	}
	
	/**
	 * Determine if the butterfly has been placed within 4 turns
	 * @param pieceType represents the pieceType of the current Move
	 * @throws HantoException
	 */
	protected void checkButterflyPlacement(HantoPieceType pieceType) throws HantoException {
		if (playerColor == HantoPlayerColor.BLUE)
		{
			if ((blueButterfly == null) && (blueTurnsTaken == 4) && (pieceType != HantoPieceType.BUTTERFLY))
				{
					gameOver = true;
					throw new HantoException("Blue player has not placed a butterfly");
				}
		}
		else
		{
			if ((redButterfly == null) && (redTurnsTaken == 4) && (pieceType != HantoPieceType.BUTTERFLY))
				{
					gameOver = true;
					throw new HantoException("Red player has not placed a butterfly");
				}
		}
	}
	
	/**
	 * Place a piece at a given coordinate
	 * @param to the location to place the piece 
	 * @param piece the piece to place
	 * @throws HantoException
	 */
	protected void placePiece(HantoCoordinate to, HantoPiece piece) throws HantoException {
		//Ignore adjacency rules for the first two turns
		if (blueTurnsTaken + redTurnsTaken > 2)
		{
			checkCoordinatePieceAdjacency(to);
		}
		HantoCoordinate toCoordImpl = new HantoCoordinateImpl(to);
		gameBoard.addPiece(toCoordImpl, piece);
	}

	/**
	 * move a piece from one location to another
	 * @param from the coordinate to move from
	 * @param to the coordinate to move to
	 * @param piece the piece to move
	 * @throws HantoException
	 */
	protected abstract void movePiece (HantoCoordinate from, HantoCoordinate to,
			HantoPiece piece) throws HantoException;
	
	
}
