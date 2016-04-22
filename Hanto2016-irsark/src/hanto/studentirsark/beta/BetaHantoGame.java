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

import static hanto.common.MoveResult.DRAW;
import static hanto.common.MoveResult.OK;

import hanto.common.HantoCoordinate;
import hanto.common.HantoException;
import hanto.common.HantoGame;
import hanto.common.HantoPiece;
import hanto.common.HantoPieceType;
import hanto.common.HantoPlayerColor;
import hanto.common.MoveResult;
import hanto.studentirsark.common.BaseHantoGame;
import hanto.studentirsark.common.HantoCoordinateImpl;

/**
 * The implementation of BetaHanto
 * @version Mar 28, 2016
 */
public class BetaHantoGame extends BaseHantoGame implements HantoGame
{
	//Constants defined by problem
	private final static int MAX_NUMBER_BUTTERFLIES = 1;
	private final static int MAX_NUMBER_SPARROWS = 5;
	private final static int MAX_NUMBER_TURNS = MAX_NUMBER_BUTTERFLIES + MAX_NUMBER_SPARROWS;
	
	/**
	 * The Beta Hanto Constructor
	 * @param firstPlayer is the color of the first player to move
	 */
	public BetaHantoGame(HantoPlayerColor firstPlayer)
	{
		super(firstPlayer);
	}
	
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
	/* (non-Javadoc)
	 * @see hanto.studentirsark.common.BaseHantoGame#placePiece(hanto.common.HantoPieceType, hanto.common.HantoCoordinate, hanto.studentirsark.common.HantoCoordinateImpl, hanto.studentirsark.common.HantoPieceImpl)
	 */
	@Override
	protected void placePiece(HantoCoordinate to, HantoPiece piece) throws HantoException {
		HantoCoordinate toCoordImpl = new HantoCoordinateImpl(to);
		gameBoard.addPiece(toCoordImpl, piece);
	}
	/* (non-Javadoc)
	 * @see hanto.studentirsark.common.BaseHantoGame#movePiece(hanto.common.HantoPieceType, hanto.common.HantoCoordinate, hanto.common.HantoCoordinate, hanto.studentirsark.common.HantoCoordinateImpl, hanto.studentirsark.common.HantoPieceImpl)
	 */
	@Override
	protected void movePiece(HantoCoordinate from, HantoCoordinate to, HantoPiece piece) throws HantoException {
		throw new HantoException("Cannot move pieces in Beta Hanto");
	}

	
	/* (non-Javadoc)
	 * @see hanto.studentirsark.common.BaseHantoGame#checkPieceCountValidity(hanto.common.HantoPieceType)
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

}
