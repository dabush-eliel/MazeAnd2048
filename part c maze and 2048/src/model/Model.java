package model;


/**
 * Interface Model, which holds the logic of the game.
 * @author Eliel Dabush and Oleg Glizerin.
 *
 */
public interface Model {

	public boolean moveUp();
	public boolean moveDown();
	public boolean moveRight();
	public boolean moveLeft();
	public boolean moveUpRight();
	public boolean moveUpLeft();
	public boolean moveDownRight();
	public boolean moveDownLeft();
	public int[][] getData();
	public void restartGame();
	public void undoMove();
	public void initGame();
	public void doUserCommand(int num);
	public int getScore();
	public boolean isStuck();
	public boolean isSucceed();
	public void save();
	public void load();
	public String getFileName();
	public void setFileName(String s);
	public void getAI(String host, int port);
	public int getHint();
	public void setHintsNum(int num);
	public void setDepth(int num);
	public int getDepth();
	public boolean isChanged(int [][] arr1, int [][] arr2);
}
