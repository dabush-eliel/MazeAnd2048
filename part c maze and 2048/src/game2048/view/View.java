package game2048.view;

public interface View {
	
	public void displayBoard(int [][] data);
	public int getUserCommand();
	public void displayScore(int score);
	public void initView(int [][] data);
//	public void run();
	public void gameOver(boolean succeed);
}
