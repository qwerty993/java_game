package characters;

public enum Difficulty {
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
