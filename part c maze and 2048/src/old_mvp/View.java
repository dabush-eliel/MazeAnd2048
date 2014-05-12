package old_mvp;

public interface View {
	
	public void displayData(int [][] data);
	public int getUserCommand();
	public void displayScore(int score);
	public void initView();
	public void gameOver(boolean succeed);
	public String getFileNamePath();
	public void setFileNamePath(String save);
}
