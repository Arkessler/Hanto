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
	 * @throws HantoException if too many of the passed piece type have been placed
	 */
	public PlayerState checkPieceCountValidity(HantoPieceType pieceType) throws HantoException;
	
	public void setOppositePlayerState(PlayerState playerState);
	
}
