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
public interface MovementStrategy {
	
	/**
	 * Method for determining if a movement is valid
	 * @param gameBoard the board to check movement on
	 * @param playerColor the current players turn
	 * @param from the coordinate moving from
	 * @param to the coordinate moving to
	 * @param piece the piece being moved
	 * @throws HantoException
	 */
	void checkValidMovement(GameBoard gameBoard, HantoPlayerColor playerColor, 
			HantoCoordinate from, HantoCoordinate to, HantoPiece piece) throws HantoException;
}
