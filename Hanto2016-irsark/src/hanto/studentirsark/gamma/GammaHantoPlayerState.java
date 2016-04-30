package hanto.studentirsark.gamma;

/*******************************************************************************
 * This files was developed for CS4233: Object-Oriented Analysis & Design.
 * The course was taken at Worcester Polytechnic Institute.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

import hanto.common.HantoException;
import hanto.common.HantoPieceType;
import hanto.studentirsark.common.PlayerState;

/**
 * @author arkessler & irshusdock
 *
 */
public class GammaHantoPlayerState implements PlayerState {

	private final int MAX_NUMBER_BUTTERFLIES = 1;
	private final int MAX_NUMBER_SPARROWS = 5;
	private int butterfliesPlaced = 0;
	private int sparrowsPlaced = 0;
	private PlayerState oppositePlayerState;
	
	
	/* (non-Javadoc)
	 * @see hanto.studentirsark.common.PlayerState#checkPieceCountValidity(hanto.common.HantoPieceType)
	 */
	@Override
	public PlayerState checkPieceCountValidity(HantoPieceType pieceType) throws HantoException {
		if ((pieceType == HantoPieceType.BUTTERFLY) && (butterfliesPlaced < MAX_NUMBER_BUTTERFLIES))
		{
			butterfliesPlaced++;
		}
		else if ((pieceType == HantoPieceType.SPARROW) && (sparrowsPlaced < MAX_NUMBER_SPARROWS))
		{
			sparrowsPlaced++;
		}
		else
		{
			throw new HantoException("Too many of single type placed");
		}
		
		if(oppositePlayerState == null)
		{
			oppositePlayerState = new GammaHantoPlayerState();
		}
		oppositePlayerState.setOppositePlayerState(this);
		return oppositePlayerState;
	}
	
	public void setOppositePlayerState(PlayerState playerState){
		oppositePlayerState = playerState;
	}

}
