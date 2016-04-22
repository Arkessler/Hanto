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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hanto.common.HantoCoordinate;
import hanto.common.HantoException;
import hanto.common.HantoPiece;

/**
 * @author Arkessler & Irshusdock
 *
 */
public class GameBoard {

	private Map<HantoCoordinate, HantoPiece> board = new HashMap<HantoCoordinate, HantoPiece>();
	
	/**
	 * Constructor for game board
	 * @param board the map to initialize the game board with
	 */
	public GameBoard(Map<HantoCoordinate, HantoPiece> board)
	{
		this.board = new HashMap<HantoCoordinate, HantoPiece>(board);
	}
	
	/**
	 * Constructor for game board
	 * @param copyBoard the game board to copy the board of
	 */
	public GameBoard(GameBoard copyBoard)
	{
		board = new HashMap<HantoCoordinate, HantoPiece>(copyBoard.getBoard());
	}
	
	/* (non-Javadoc)
	 * @see hanto.studentirsark.common.GameBoard#getPieceAt(hanto.common.HantoCoordinate)
	 */
	public HantoPiece getPieceAt(HantoCoordinate coord) {
		HantoCoordinateImpl whereCoordImpl = new HantoCoordinateImpl(coord);
		return board.get(whereCoordImpl);
	}

	/* (non-Javadoc)
	 * @see hanto.studentirsark.common.GameBoard#checkContiguity(hanto.common.HantoCoordinate)
	 */
	public void checkContiguity(HantoCoordinate nextPos) throws HantoException {
		HantoCoordinateImpl nextPosCoordImpl = new HantoCoordinateImpl(nextPos);
		List<HantoCoordinate> visited = new ArrayList<HantoCoordinate>();
		List<HantoCoordinate> toVisit = new ArrayList<HantoCoordinate>();
		visited.add(nextPosCoordImpl);
		List<HantoCoordinate> adjacentToOrigin = nextPosCoordImpl.getAdjacentCoordinates();
		for (HantoCoordinate coord :  adjacentToOrigin)
		{
			if ((getPieceAt(coord)!=null) && (!(visited.contains(coord))))
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
	 * Wrapper for hashmap contains on board
	 * @param obj the key to check
	 * @return true if a key is found, false otherwise
	 */
	public boolean containsKey(Object obj)
	{
		return board.containsKey(obj);
	}
	
	/**
	 * Return a printable version of the game board
	 * @return a String that is a printable version of the game board
	 */
	public String getPrintableBoard()
	{
		String toReturn = "";
		for(HantoCoordinate coord: board.keySet())
		{
			toReturn += "(" + coord.getX() + ", " + coord.getY() + "): " + board.get(coord).getColor() + " " + board.get(coord).getType() + "\n";
		}
		
		return toReturn;
	}

	/**
	 * Wrapper for adding a piece to the hashmap game board
	 * @param coord the key to add at
	 * @param piece the value to add
	 */
	public void addPiece(HantoCoordinate coord, HantoPiece piece)
	{
		board.put(coord, piece);
	}

	/**
	 * Wrapper for removing a piece from the hashmap
	 * @param coord the key to use to remove
	 * @return the value removed, null if no value was found
	 */
	public HantoPiece removePiece(HantoCoordinate coord)
	{
		return board.remove(coord);
	}
	public Map<HantoCoordinate, HantoPiece> getBoard()
	{
		return board;
	}
}

