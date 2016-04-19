/**
 * 
 */
package hanto.studentirsark.common;

import static hanto.common.HantoPieceType.BUTTERFLY;
import static hanto.common.HantoPlayerColor.BLUE;
import static hanto.common.HantoPlayerColor.RED;
import static hanto.common.MoveResult.BLUE_WINS;
import static hanto.common.MoveResult.DRAW;
import static hanto.common.MoveResult.OK;
import static hanto.common.MoveResult.RED_WINS;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hanto.common.HantoCoordinate;
import hanto.common.HantoException;
import hanto.common.HantoGame;
import hanto.common.HantoPiece;
import hanto.common.HantoPieceType;
import hanto.common.HantoPlayerColor;
import hanto.common.MoveResult;

/**
 * @author Irshusdock, Arkessler
 *
 */
public abstract class BaseHantoGame implements HantoGame{
	//Global variables
	protected Map<HantoCoordinate, HantoPiece> board = new HashMap<HantoCoordinate, HantoPiece>();
	protected HantoPlayerColor playerColor;
	protected boolean firstMove = true;
	protected HantoCoordinate redButterfly;
	protected HantoCoordinate blueButterfly;
	protected boolean gameOver = false;
	//Turn tracking
	protected int redTurnsTaken = 0;
	protected int blueTurnsTaken = 0;
	//Piece types placement tracking
	protected int blueButterfliesPlaced = 0;
	protected int blueSparrowsPlaced = 0;
	protected int blueCrabsPlaced = 0;
	protected int redButterfliesPlaced = 0;
	protected int redSparrowsPlaced = 0;
	protected int redCrabsPlaced = 0;
	
	public BaseHantoGame(HantoPlayerColor firstPlayer)
	{
		playerColor = firstPlayer;
	}
	
	public MoveResult makeMove(HantoPieceType pieceType, HantoCoordinate from, HantoCoordinate to)
			throws HantoException {

		//Instantiate coordinateImpl from to-coordinate
		HantoCoordinateImpl toCoordImpl= new HantoCoordinateImpl(to);
		//Instantiate piece from passed in information
		HantoPieceImpl pieceImpl = new HantoPieceImpl(playerColor, pieceType);
		
		//Increment player turn by color
		incrementTurnCounters();
			
		//Check that a butterfly has been placed within the correct number of turns
		checkButterflyPlacement(pieceType);
		
		//Check that game is not over
		if (gameOver == true)
		{
			throw new HantoException("Cannot move after game is over");
		}
		//Check that toCoordinate is empty
		if (board.containsKey(toCoordImpl))
		{
			throw new HantoException("Trying to place piece on existing piece");
		}
		
		//Check that first move is at 0,0
		if (firstMove && !(toCoordImpl.equals(new HantoCoordinateImpl(0, 0))))
		{
			throw new HantoException("First move needs to be at 0, 0");
		}
		
		applyMove(pieceType, from, to, toCoordImpl, pieceImpl);
		updateButterflyLocation(pieceType, to);
		checkContiguity(to, this.board);
		
		MoveResult surroundedCheckResult = checkSurroundedWinCondition();
		
		firstMove = false;
		changePlayerColor();
		
		return checkMaxTurnCount(surroundedCheckResult);
	}

	/**
	 * @param surroundedCheckResult
	 */
	protected abstract MoveResult checkMaxTurnCount(MoveResult surroundedCheckResult);

	/* (non-Javadoc)
	 * @see hanto.common.HantoGame#getPieceAt(hanto.common.HantoCoordinate)
	 */
	@Override
	public HantoPiece getPieceAt(HantoCoordinate where) {
		HantoCoordinateImpl whereCoordImpl = new HantoCoordinateImpl(where);
		return board.get(whereCoordImpl);
	}

	/**
	 * @param from: the location that a piece will be moved from
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
		String toReturn = "";
		for(HantoCoordinate coord: board.keySet())
		{
			toReturn += "(" + coord.getX() + ", " + coord.getY() + "): " + board.get(coord).getColor() + " " + board.get(coord).getType() + "\n";
		}
		
		return toReturn;
	}
	
	protected void applyMove(HantoPieceType pieceType, HantoCoordinate from, HantoCoordinate to,
			HantoCoordinateImpl toCoordImpl, HantoPieceImpl pieceImpl) throws HantoException {
		if (from==null)	//If placing a new piece
		{
			placePiece(pieceType, to, toCoordImpl, pieceImpl);
		}
		else		//If moving a piece
		{
			HantoCoordinateImpl fromCoordinateImpl = new HantoCoordinateImpl(from); 
			checkMovingExistentPiece(from);
			if (getPieceAt(fromCoordinateImpl).getColor() != playerColor)
			{
				throw new HantoException("Can't move opponent's piece");
			}
			if (getPieceAt(fromCoordinateImpl).getType() != pieceType)
			{
				throw new HantoException("Wrong piece type in makeMove");
			}
			checkButterflyPlacedBeforeMove();
			movePiece(pieceType, from, to, toCoordImpl, pieceImpl);
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
	 * @param pieceType
	 * @param to
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
	 * 
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
	 * @throws HantoException 
	 * 
	 */
	protected void checkContiguity(HantoCoordinate nextPos, Map<HantoCoordinate, HantoPiece> boardToCheck) throws HantoException {
		HantoCoordinateImpl nextPosCoordImpl = new HantoCoordinateImpl(nextPos);
		List<HantoCoordinate> visited = new ArrayList<HantoCoordinate>();
		List<HantoCoordinate> toVisit = new ArrayList<HantoCoordinate>();
		visited.add(nextPosCoordImpl);
		List<HantoCoordinate> adjacentToOrigin = nextPosCoordImpl.getAdjacentCoordinates();
		for (HantoCoordinate coord :  adjacentToOrigin)
		{
			if ((getPieceAt(coord, boardToCheck)!=null) && (!(visited.contains(coord))))
			{
				toVisit.add(coord);
			}
		}
		while (!toVisit.isEmpty())
		{
			HantoCoordinateImpl nextCoord = new HantoCoordinateImpl(toVisit.get(0));
			toVisit.remove(0);
			if (!visited.contains(nextCoord))
			{
				visited.add(nextCoord);
			}
			List<HantoCoordinate> adjacentToCurr = nextCoord.getAdjacentCoordinates();
			for (HantoCoordinate coord :  adjacentToCurr)
			{
				if ((getPieceAt(coord, boardToCheck)!=null) && (!(visited.contains(coord))))
				{
					toVisit.add(coord);
				}
			}
		}
		if (visited.size() != boardToCheck.size())
		{
			throw new HantoException("Cannot have discontiguous piece");
		}
	}
	/**
	 * @param coord
	 * @param board2
	 * @return
	 */
	private Object getPieceAt(HantoCoordinate where, Map<HantoCoordinate, HantoPiece> board2) {
		HantoCoordinateImpl whereCoordImpl = new HantoCoordinateImpl(where);
		return board.get(whereCoordImpl);
	}

	/**
	 * @param where is the location to check if there are any adjacent pieces to
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
	 * @param pieceType represents the pieceType of the current Move
	 * @throws HantoException
	 */
	protected void checkButterflyPlacement(HantoPieceType pieceType) throws HantoException {
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
	
	protected abstract void placePiece(HantoPieceType pieceType, HantoCoordinate to, HantoCoordinateImpl toCoordImpl,
			HantoPieceImpl pieceImpl) throws HantoException;

	protected abstract void movePiece (HantoPieceType pieceType, HantoCoordinate from, HantoCoordinate to,
			HantoCoordinateImpl toCoordImpl, HantoPieceImpl pieceImpl) throws HantoException;
	
	protected abstract void checkPieceCountValidity(HantoPieceType pieceType) throws HantoException;
	
}
