package view;

/**
 * Interface View, which holds the View of the game.
 * @author Eliel Dabush and Oleg Glizerin.
 *
 */
public interface View {
	public void displayScore(int score);
	public void displayData(int [][] data);
	public void displayHint(int hint);
	public int getUserCommand();
	public void initView();
	public void gameOver(boolean succeed);
	public String getFileNamePath();
	public void setFileNamePath(String save);
}
