package algorithms;

import model.Model;

public class Node {
	
	private Model gameModel;
	private String actionICameFrom;
	private Node[] children; 
	private int numOfChildren;
	private String player;
	
	public Node(Model m, String player, int numOfChildren){
		this.gameModel = m;
		this.player = player;
		this.numOfChildren = numOfChildren;
	}
	
	
	public Model getGameModel(){
		return gameModel;
	}
	public void setGameModel(Model model){
		gameModel = model;
	}
	
	public Node[] getChildren(){
		return children;
	}
	public void setChildren(Node[] n){
		children = n;
	}
	
	public int getNumOfChildren(){
		return numOfChildren;
	}
	
	public void setNumOfChildren(int num){
		numOfChildren = num;
	}
	public String getPlayer(){
		return player;
	}
	public void setPlayer(String p){
		player = p;
	}
	
	

}
