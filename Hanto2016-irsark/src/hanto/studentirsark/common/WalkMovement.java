/*******************************************************************************
 * This files was developed for CS4233: Object-Oriented Analysis & Design.
 * The course was taken at Worcester Polytechnic Institute.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package hanto.studentirsark.common;

import java.util.ArrayList;
import java.util.List;

import hanto.common.HantoCoordinate;
import hanto.common.HantoException;
import hanto.common.HantoPiece;
import hanto.common.HantoPlayerColor;

/**
 * @author Irshusdock, Arkessler
 *
 */
public class WalkMovement implements MovementStrategy {
	
	private int maxDistanceWalk;
	GameBoard gameBoard;
	
	/**
	 * Constructor for WalkMovement strategy
	 * @param maxDistanceWalk the maxiumum walk distance for the strategy
	 */
	public WalkMovement(int maxDistanceWalk)
	{
		this.maxDistanceWalk = maxDistanceWalk;
	}

	/* (non-Javadoc)
	 * @see hanto.studentirsark.common.MovementStrategy#isValidMovement(java.util.Map, hanto.common.HantoPieceType, hanto.common.HantoCoordinate, hanto.common.HantoCoordinate, hanto.studentirsark.common.HantoCoordinateImpl, hanto.studentirsark.common.HantoPieceImpl)
	 */
	@Override
	public void checkValidMovement(GameBoard gameBoard, 
			HantoPlayerColor playerColor, HantoCoordinate from, HantoCoordinate to, HantoPiece piece)
					throws HantoException {
		this.gameBoard = new GameBoard(gameBoard);
		HantoCoordinateImpl fromCoordinateImpl = new HantoCoordinateImpl(from);
		
		
		if (existsPathTo(this.gameBoard, from, to, maxDistanceWalk))
		{
			GameBoard boardCopy = new GameBoard(gameBoard);
			boardCopy.removePiece(fromCoordinateImpl);
			boardCopy.addPiece(new HantoCoordinateImpl(to), piece);
			boardCopy.checkContiguity(to);
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
			if (gameBoard.getPieceAt(intersectingAdjacentPieces.get(i)) == null)
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
	
	private boolean existsPathTo(GameBoard mutableBoard, HantoCoordinate from, HantoCoordinate dest, 
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
			if (checkPieceWalkOpening(from, coordinate) && gameBoard.getPieceAt(coordinate) == null)
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
				GameBoard boardCopy = new GameBoard(mutableBoard);
				boardCopy.addPiece(new HantoCoordinateImpl(coordinate), new HantoPieceImpl(boardCopy.getPieceAt(startCoordImpl)));
				boardCopy.removePiece(from);
				try
				{
					boardCopy.checkContiguity(coordinate);
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
