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

import hanto.common.HantoCoordinate;
import hanto.common.HantoException;
import hanto.common.HantoPiece;
import hanto.common.HantoPlayerColor;

/**
 * @author Alexi
 *
 */
public class FlyMovement implements MovementStrategy {

	private int maxFlyDistance;
	
	/**
	 * Constructor for FlyMovement
	 * @param maxFlyDistance the maximum distance the piece can fly
	 */
	public FlyMovement( int maxFlyDistance)
	{
		this.maxFlyDistance = maxFlyDistance;
	}
	
	/* (non-Javadoc)
	 * @see hanto.studentirsark.common.MovementStrategy#isValidMovement(hanto.studentirsark.common.BaseHantoGame, java.util.Map, hanto.common.HantoPlayerColor, hanto.common.HantoPieceType, hanto.common.HantoCoordinate, hanto.common.HantoCoordinate, hanto.studentirsark.common.HantoCoordinateImpl, hanto.studentirsark.common.HantoPieceImpl)
	 */
	@Override
	public void checkValidMovement(GameBoard gameBoard,
			HantoPlayerColor playerColor, HantoCoordinate from, HantoCoordinate to,	HantoPiece piece) throws HantoException {
		HantoCoordinateImpl fromCoordImpl = new HantoCoordinateImpl(from);
		HantoCoordinateImpl toCoordImpl = new HantoCoordinateImpl(to);
		if (fromCoordImpl.distanceBetween(toCoordImpl) > maxFlyDistance)
		{
			throw new HantoException("Cannot fly that far");
		}
		GameBoard boardCopy = new GameBoard(gameBoard);
		boardCopy.removePiece(fromCoordImpl);
		boardCopy.addPiece(toCoordImpl, piece);
		boardCopy.checkContiguity(to);
	
	}
	
	
}
