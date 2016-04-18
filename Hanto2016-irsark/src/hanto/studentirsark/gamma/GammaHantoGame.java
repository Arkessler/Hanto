/**
 * 
 */
package hanto.studentirsark.gamma;

import static hanto.common.HantoPieceType.*;
import static hanto.common.MoveResult.*;

import java.util.ArrayList;
import java.util.List;

import static hanto.common.HantoPlayerColor.*;
import hanto.common.HantoCoordinate;
import hanto.common.HantoException;
import hanto.common.HantoGame;
import hanto.common.HantoPiece;
import hanto.common.HantoPieceType;
import hanto.common.HantoPlayerColor;
import hanto.common.MoveResult;
import hanto.studentirsark.common.BaseHantoGame;
import hanto.studentirsark.common.HantoCoordinateImpl;
import hanto.studentirsark.common.HantoPieceImpl;

/**
 * @author Irshusdock, Arkessler
 *
 */
public class GammaHantoGame extends BaseHantoGame implements HantoGame {

	//Constants defined by problem
	private final static int MAX_NUMBER_BUTTERFLIES = 1;
	private final static int MAX_NUMBER_SPARROWS = 5;
	private final static int MAX_NUMBER_TURNS = 20;
	
	/**
	 * @param firstPlayer
	 */
	public GammaHantoGame(HantoPlayerColor firstPlayer) {
		super(firstPlayer);
	}

	/**
	 * @param pieceType
	 * @param from
	 * @param to
	 * @param toCoordImpl
	 * @param pieceImpl
	 * @throws HantoException
	 */
	@Override
	protected void movePiece(HantoPieceType pieceType, HantoCoordinate from, HantoCoordinate to,
			HantoCoordinateImpl toCoordImpl, HantoPieceImpl pieceImpl) throws HantoException {
		HantoCoordinateImpl fromCoordinateImpl;
		fromCoordinateImpl = new HantoCoordinateImpl(from);
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
	}

	/**
	 * @param pieceType
	 * @param to
	 * @param toCoordImpl
	 * @param pieceImpl
	 * @throws HantoException
	 */
	@Override
	protected void placePiece(HantoPieceType pieceType, HantoCoordinate to, HantoCoordinateImpl toCoordImpl,
			HantoPieceImpl pieceImpl) throws HantoException {
		checkPieceCountValidity(pieceType);
		//Ignore adjacency rules for the first two turns
		if (blueTurnsTaken + redTurnsTaken > 2)
		{
			checkCoordinatePieceAdjacency(to);
		}
		board.put(toCoordImpl, pieceImpl);
	}


	/**
	 * @param from: the origin of the piece being moved
	 * @throws HantoException 
	 */
	private void checkPieceWalkOpening(HantoCoordinate from, HantoCoordinate to) throws HantoException {
		boolean validOpening = false;
		HantoCoordinateImpl fromCoordImpl = new HantoCoordinateImpl(from);
		HantoCoordinateImpl toCoordImpl = new HantoCoordinateImpl(to);
		if (!(fromCoordImpl.getAdjacentCoordinates().contains(toCoordImpl)))
		{
			throw new HantoException("Cannot move more than one spot");
		}
		/*
		 * The intersection of the adjacencies of both from and to consists of the coordinates to either side of
		 * the movement path. If either of these places is free, then there is room to walk
		 */
		List<HantoCoordinate> intersectingAdjacentPieces = 
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
	private List<HantoCoordinate> getIntersectionOfCoordinateLists(List<HantoCoordinate> arr1, 
			List<HantoCoordinate> arr2)
	{
		List<HantoCoordinate> retList = new ArrayList<HantoCoordinate>();
		for (HantoCoordinate coord: arr1)
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
	@Override
	protected void checkPieceCountValidity(HantoPieceType pieceType) throws HantoException {
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
	
	/* (non-Javadoc)
	 * @see hanto.studentirsark.common.BaseHantoGame#checkMaxTurnCount(hanto.common.MoveResult)
	 */
	@Override
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

}
