package game2048.model;

public interface Model {

	public void moveUp();
	public void moveDown();
	public void moveRight();
	public void moveLeft();
	public int getScore();
	public void restartGame();
	public void undoMove();
	public int[][] getBoard();
	public void initGame();
	public void doUserCommand(int num);
	public boolean isStuck();
	public boolean isSucceed();
	public void save();
	public void load();
	public String getFileNameToSave();
	public void setFileNameToSave(String save);
	public String getFileNameToLoad();
	public void setFileNameToLoad(String load);
}
