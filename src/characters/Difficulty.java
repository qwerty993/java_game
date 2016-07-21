package characters;

public enum Difficulty {
	
	// svaki level tezine moram da pomnozim sa brojem ENEMYJA na tom NIVOU
	// zato sto kad ispalim jedan metak i pogodim jednog enemija
	// na enemiju se kolizija sa metkom manifestuje brojEnemijaNaTomNivou puta
	// zbog niti
	// ili tako da resim, ili da smislim neko normalno resenje!!
	// bolje da smislim nesto, al o tom po tom. 
	
	EASY(150), 
	MEDIUM(250), 
	HARD(350), 
	EXPERT(450), 
	INSANE(550); 
	
	private int diffLevel;
	
	private Difficulty(int diffLevel) {
		this.diffLevel = diffLevel;
	}

	public int getDiffLevel() {
		return diffLevel;
	}
}
