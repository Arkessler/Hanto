/**
 * 
 */
package hanto.studentirsark.gamma;

import static hanto.common.HantoPieceType.*;
import static hanto.common.MoveResult.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import static hanto.common.HantoPlayerColor.*;
import hanto.common.HantoCoordinate;
import hanto.common.HantoException;
import hanto.common.HantoGame;
import hanto.common.HantoPiece;
import hanto.common.HantoPieceType;
import hanto.common.HantoPlayerColor;
import hanto.common.MoveResult;
import hanto.studentirsark.common.HantoCoordinateImpl;
import hanto.studentirsark.common.HantoPieceImpl;

/**
 * @author Alexi
 *
 */
public class GammaHantoGame implements HantoGame {

	//Constants defined by problem
	private final static int MAX_NUMBER_BUTTERFLIES = 1;
	private final static int MAX_NUMBER_SPARROWS = 5;
	//Global variables
	private HashMap<HantoCoordinateImpl, HantoPieceImpl> board = new HashMap<HantoCoordinateImpl, HantoPieceImpl>();
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
	public GammaHantoGame(HantoPlayerColor firstPlayer)
	{
		playerColor = firstPlayer;
	}
	
	/* (non-Javadoc)
	 * @see hanto.common.HantoGame#makeMove(hanto.common.HantoPieceType, hanto.common.HantoCoordinate, hanto.common.HantoCoordinate)
	 */
	@Override
	public MoveResult makeMove(HantoPieceType pieceType, HantoCoordinate from, HantoCoordinate to)
			throws HantoException {
		
		//Declare coordinateImpl for from-coordinate
		HantoCoordinateImpl fromCoordinateImpl;
		//Instantiate coordinateImpl from to-coordinate
		HantoCoordinateImpl toCoordImpl= new HantoCoordinateImpl(to);
		//Instantiate piece from passed in information
		HantoPieceImpl pieceImpl = new HantoPieceImpl(playerColor, pieceType);
		
		//Increment player turn by color
		incrementTurnCounters();
			
		
		//Check that a butterfly has been placed within the correct number of turns
		checkButterflyPlacement(pieceType);
		
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
		
		if (from==null)	//If placing a new piece
		{
			placePiece(pieceType, to, toCoordImpl, pieceImpl);
		}
		else		//If moving a piece
		{
			walkPiece(pieceType, from, to, toCoordImpl, pieceImpl);
		}
		
		firstMove = false;
		changePlayerColor();
		
		return OK;
	}

	/**
	 * @param pieceType
	 * @param from
	 * @param to
	 * @param toCoordImpl
	 * @param pieceImpl
	 * @throws HantoException
	 */
	private void walkPiece(HantoPieceType pieceType, HantoCoordinate from, HantoCoordinate to,
			HantoCoordinateImpl toCoordImpl, HantoPieceImpl pieceImpl) throws HantoException {
		HantoCoordinateImpl fromCoordinateImpl;
		fromCoordinateImpl = new HantoCoordinateImpl(from);
		checkMovePieceValidity(from);
		checkPieceWalkOpening(from, to);
		if (getPieceAt(fromCoordinateImpl).getColor() != playerColor)
		{
			throw new HantoException("Can't move opponent's piece");
		}
		if (getPieceAt(fromCoordinateImpl).getType() != pieceType)
		{
			throw new HantoException("Wrong piece type is makeMove");
		}
		board.remove(fromCoordinateImpl);
		board.put(toCoordImpl, pieceImpl);
		checkContiguity(to);
	}

	/**
	 * @param pieceType
	 * @param to
	 * @param toCoordImpl
	 * @param pieceImpl
	 * @throws HantoException
	 */
	private void placePiece(HantoPieceType pieceType, HantoCoordinate to, HantoCoordinateImpl toCoordImpl,
			HantoPieceImpl pieceImpl) throws HantoException {
		checkPieceCountValidity(pieceType);
		board.put(toCoordImpl, pieceImpl);
		//Ignore adjacency rules for the first two turns
		if (blueTurnsTaken + redTurnsTaken > 2)
		{
			checkCoordinatePieceAdjacency(to);
		}
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
	 *  changes the color of the active player
	 */
	private void changePlayerColor() {
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
	 * increments the turn counter for the current player
	 */
	private void incrementTurnCounters() {
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
	 * @throws HantoException 
	 * 
	 */
	private void checkContiguity(HantoCoordinate nextPos) throws HantoException {
		HantoCoordinateImpl nextPosCoordImpl = new HantoCoordinateImpl(nextPos);
		ArrayList<HantoCoordinateImpl> visited = new ArrayList<HantoCoordinateImpl>();
		ArrayList<HantoCoordinateImpl> toVisit = new ArrayList<HantoCoordinateImpl>();
		visited.add(nextPosCoordImpl);
		ArrayList<HantoCoordinateImpl> adjacentToOrigin = nextPosCoordImpl.getAdjacentCoordinates();
		for (HantoCoordinateImpl coord :  adjacentToOrigin)
		{
			if ((getPieceAt(coord)!=null) && (!(visited.contains(coord))))
			{
				toVisit.add(coord);
			}
		}
		while (!toVisit.isEmpty())
		{
			HantoCoordinateImpl nextCoord = toVisit.get(0);
			toVisit.remove(0);
			if (!visited.contains(nextCoord))
			{
				visited.add(nextCoord);
			}
			ArrayList<HantoCoordinateImpl> adjacentToCurr = nextCoord.getAdjacentCoordinates();
			for (HantoCoordinateImpl coord :  adjacentToCurr)
			{
				if ((getPieceAt(coord)!=null) && (!(visited.contains(coord))))
				{
					toVisit.add(coord);
				}
			}
		}
		if (visited.size() != board.size())
		{
			throw new HantoException("Cannot have discontiguous piece");
		}
	}

	/**
	 * @param from: the location that a piece will be moved from
	 * @throws HantoException 
	 */
	private void checkMovePieceValidity(HantoCoordinate from) throws HantoException {
		if (getPieceAt(from) == null)
		{
			throw new HantoException("Trying to move a non-existant piece");
		}
	}

	/**
	 * @param from: the origin of the piece being moved
	 * @throws HantoException 
	 */
	private void checkPieceWalkOpening(HantoCoordinate from, HantoCoordinate to) throws HantoException {
		boolean validOpening = false;
		HantoCoordinateImpl fromCoordImpl = new HantoCoordinateImpl(from);
		HantoCoordinateImpl toCoordImpl = new HantoCoordinateImpl(to);
		/*
		 * The intersection of the adjacencies of both from and to consists of the coordinates to either side of
		 * the movement path. If either of these places is free, then there is room to walk
		 */
		ArrayList<HantoCoordinateImpl> intersectingAdjacentPieces = 
				getIntersectionOfCoordinateLists(fromCoordImpl.getAdjacentCoordinates(), toCoordImpl.getAdjacentCoordinates());
		for (int i = 0; i < intersectingAdjacentPieces.size(); i++)
		{
			if (getPieceAt(intersectingAdjacentPieces.get(i)) == null)
			{
				validOpening = true;
			}
		}
		if (!validOpening)
		{
			throw new HantoException("Cannot move through single hex opening");
		}
	}

	/**
	 * 
	 * @param arr1 the first arraylist of hantocoordinateimpls
	 * @param arr2 the second arraylist of hantoCoordinateImpls
	 * @return the intersection of the two lists
	 */
	private ArrayList<HantoCoordinateImpl> getIntersectionOfCoordinateLists(ArrayList<HantoCoordinateImpl> arr1, 
			ArrayList<HantoCoordinateImpl> arr2)
	{
		ArrayList<HantoCoordinateImpl> retList = new ArrayList<HantoCoordinateImpl>();
		for (HantoCoordinateImpl coord: arr1)
		{
			if (arr2.contains(coord))
			{
				retList.add(coord);
			}
		}
		return retList;
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
		HantoCoordinateImpl originImpl = new HantoCoordinateImpl(where);
		boolean foundSameColorAdjacent = false;
		ArrayList<HantoCoordinateImpl> surroundingCoordinates = originImpl.getAdjacentCoordinates();
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
	
	/* (non-Javadoc)
	 * @see hanto.common.HantoGame#getPieceAt(hanto.common.HantoCoordinate)
	 */
	@Override
	public HantoPiece getPieceAt(HantoCoordinate where) {
		// TODO Auto-generated method stub
		HantoCoordinateImpl whereCoordImpl = new HantoCoordinateImpl(where);
		return board.get(whereCoordImpl);
	}

	/* (non-Javadoc)
	 * @see hanto.common.HantoGame#getPrintableBoard()
	 */
	@Override
	public String getPrintableBoard() {
		// TODO Auto-generated method stub
		return null;
	}

}
