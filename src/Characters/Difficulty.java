package Characters;

public enum Difficulty {
	EASY(100),
	MEDIUM(200),
	HARD(300),
	EXPERT(350),
	INSANE(400);
	
	private int diffLevel;
	
	private Difficulty(int diffLevel) {
		this.diffLevel = diffLevel;
	}

	public int getDiffLevel() {
		return diffLevel;
	}
}
