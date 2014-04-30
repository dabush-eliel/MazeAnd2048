package gameMaze.view.components;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

public class ButtonsMaze {
	private Button restart;
	private Button undo;
	private Button save;
	private Button load;
	private Button exit;
//	private int command = 0;
	

	public Button getRestart() {
		return restart;
	}

	public Button getUndo() {
		return undo;
	}
	
	public Button getSave() {
		return save;
	}

	public Button getLoad() {
		return load;
	}
	
	public Button getExit(){
		return exit;
	}
	
	public ButtonsMaze(Composite parent, int style) {
		restart = new Button(parent,style);
		restart.setText("Restart");
		
		undo = new Button(parent,style);
		undo.setText("Undo");
		
		save = new Button(parent, style);
		save.setText("Save");
		
		load = new Button(parent, style);
		load.setText("Load");
		
		exit = new Button(parent, style);
		exit.setText("Exit");
		
//		int maxX = parent.getSize().x;
//		int maxY = parent.getSize().y;
		
		restart.setLayoutData(new GridData(SWT.FILL,SWT.TOP,true,false,1,1));
//		restart.setLocation((maxX/3)*2,5);
		
		undo.setLayoutData(new GridData(SWT.FILL,SWT.TOP,true,false,1,1));
//		undo.setLocation(restart.getLocation().x,restart.getLocation().y+5);
		
		load.setLayoutData(new GridData(SWT.FILL,SWT.TOP,true,false,1,1));
//		load.setLocation(undo.getLocation().x,undo.getLocation().y+5);
		
		save.setLayoutData(new GridData(SWT.FILL,SWT.TOP,true,false,1,1));
//		save.setLocation(load.getLocation().x,load.getLocation().y+5);
		
		exit.setLayoutData(new GridData(SWT.FILL,SWT.TOP,true,false,1,1));
	}
}