package gameMaze.view;

public interface View {
	
	public void displayScore(int score);
	public void displayData(int [][] data);
	public int getUserCommand();
	public void initView();
	public void gameOver(boolean succeed);
	public String getFileNamePath();
	public void setFileNamePath(String save);
	
}
