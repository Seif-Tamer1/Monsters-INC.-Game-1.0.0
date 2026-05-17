package game.engine.cells;

import game.engine.Board;
import game.engine.cards.Card;
import game.engine.monsters.Monster;

public class CardCell extends Cell {
	private static Card drawnCard;
	
	public CardCell(String name) {
        super(name);
    }
    
    @Override
    public void onLand(Monster landingMonster, Monster opponentMonster) {
    	super.onLand(landingMonster, opponentMonster);
    	
        Card card = Board.drawCard();
        drawnCard=card;
        card.performAction(landingMonster, opponentMonster);
    }

	public static Card getDrawnCard() {
		return drawnCard;
	}
   
    
}
