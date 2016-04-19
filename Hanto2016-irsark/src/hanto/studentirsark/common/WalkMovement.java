/**
 * 
 */
package hanto.studentirsark.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hanto.common.HantoCoordinate;
import hanto.common.HantoException;
import hanto.common.HantoPiece;
import hanto.common.HantoPieceType;
import hanto.common.HantoPlayerColor;

/**
 * @author Irshusdock, Arkessler
 *
 */
public class WalkMovement implements MovementStrategy {
	
	private int maxDistanceWalk;
	
	public WalkMovement(int maxDistanceWalk)
	{
		this.maxDistanceWalk = maxDistanceWalk;
	}

	BaseHantoGame game;
	/* (non-Javadoc)
	 * @see hanto.studentirsark.common.MovementStrategy#isValidMovement(java.util.Map, hanto.common.HantoPieceType, hanto.common.HantoCoordinate, hanto.common.HantoCoordinate, hanto.studentirsark.common.HantoCoordinateImpl, hanto.studentirsark.common.HantoPieceImpl)
	 */
	@Override
	public void checkValidMovement(BaseHantoGame game, Map<HantoCoordinate, HantoPiece> board, 
			HantoPlayerColor playerColor, HantoPieceType pieceType, HantoCoordinate from, HantoCoordinate to, 
			HantoCoordinateImpl toCoordImpl, HantoPieceImpl pieceImpl)
					throws HantoException {
		this.game = game;
		HantoCoordinateImpl fromCoordinateImpl = new HantoCoordinateImpl(from);
		
		
		if (existsPathTo(board, from, to, maxDistanceWalk))
		{
			return;
		}
		throw new HantoException("Cannot find valid walking path to destination");
	}

	private boolean checkPieceWalkOpening(HantoCoordinate from, HantoCoordinate to) throws HantoException {
		boolean validOpening = false;
		HantoCoordinateImpl fromCoordImpl = new HantoCoordinateImpl(from);
		HantoCoordinateImpl toCoordImpl = new HantoCoordinateImpl(to);
		/*
		 * The intersection of the adjacencies of both from and to consists of the coordinates to either side of
		 * the movement path. If either of these places is free, then there is room to walk
		 */
		List<HantoCoordinate> intersectingAdjacentPieces = 
				getIntersectionOfCoordinateLists(fromCoordImpl.getAdjacentCoordinates(), toCoordImpl.getAdjacentCoordinates());
		for (int i = 0; i < intersectingAdjacentPieces.size(); i++)
		{
			if (game.getPieceAt(intersectingAdjacentPieces.get(i)) == null)
			{
				validOpening = true;
			}
		}
		return validOpening;
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
	
	private boolean existsPathTo(Map<HantoCoordinate, HantoPiece> board, HantoCoordinate from, HantoCoordinate dest, 
			int distanceWalk) throws HantoException
	{
		if (distanceWalk == 0)
		{
			return false;
		}
		HantoCoordinateImpl startCoordImpl = new HantoCoordinateImpl(from);
		List<HantoCoordinate> surroundingCoords = startCoordImpl.getAdjacentCoordinates();
		List<HantoCoordinate> walkableSurroundingCoords = new ArrayList<HantoCoordinate>();
		
		for (HantoCoordinate coordinate: surroundingCoords)
		{
			if (checkPieceWalkOpening(from, coordinate) && game.getPieceAt(coordinate) == null)
			{
				walkableSurroundingCoords.add(coordinate);
			}
		}
		
		if (walkableSurroundingCoords.contains(new HantoCoordinateImpl(dest)))
		{
			return true;
		}
		else
		{
			for (HantoCoordinate coordinate: walkableSurroundingCoords)
			{
				Map<HantoCoordinate, HantoPiece> boardCopy = new HashMap<HantoCoordinate, HantoPiece>(board);
				boardCopy.put(new HantoCoordinateImpl(coordinate), new HantoPieceImpl(boardCopy.get(startCoordImpl)));
				boardCopy.remove(from);
				try
				{
					game.checkContiguity(coordinate, boardCopy);
					if (existsPathTo(boardCopy, coordinate, dest, distanceWalk-1))
					{
						return true;
					}
				} catch(HantoException exc)
				{
					continue;
				}
			}
			
		}
		return false;		
	}
}
