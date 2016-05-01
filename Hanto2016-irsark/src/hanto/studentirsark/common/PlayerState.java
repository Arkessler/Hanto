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

import hanto.common.HantoException;
import hanto.common.HantoPieceType;

/**
 * @author arkessler & irshusdock
 *
 */
public interface PlayerState {

	/**
	 * Update the number of pieces of passed type placed and check if too many have been placed
	 * @param pieceType the type of the piece to update the count of
	 * @throws HantoException if too many of the passed piece type have been placed
	 * @return The opposite players PlayerState
	 */
	PlayerState checkPieceCountValidity(HantoPieceType pieceType) throws HantoException;
	
	/**
	 * Set the opposite player state (to return when checkPieceCountValidity is run)
	 * @param playerState the player state to set oppositePlayerState to
	 */
	void setOppositePlayerState(PlayerState playerState);

	/**
	 * Undo the incrememnting on a certain piece type that comes from checkPieceCountValidity
	 * @param pieceType the piece type to undo incrementing of
	 */
	void undoIncrement(HantoPieceType pieceType);
	
}
