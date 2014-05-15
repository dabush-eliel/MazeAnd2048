package view.game2048;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

public class Buttons2048{
	private Button restart;
	private Button undo;
	private Button save;
	private Button load;
	private Button exit;
	
	private Button minimax;
	
	
	public Button getMinimax() {
		return minimax;
	}

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
	
	public Buttons2048(Composite parent, int style) {
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
		
		
		minimax = new Button(parent, style);
		minimax.setText("Solve With Minimax");
		
		minimax.setLayoutData(new GridData(SWT.FILL,SWT.TOP,true,false,1,1));
		

		restart.setLayoutData(new GridData(SWT.FILL,SWT.TOP,true,false,1,1));

		
		undo.setLayoutData(new GridData(SWT.FILL,SWT.TOP,true,false,1,1));
		
		load.setLayoutData(new GridData(SWT.FILL,SWT.TOP,true,false,1,1));
		
		save.setLayoutData(new GridData(SWT.FILL,SWT.TOP,true,false,1,1));
		
		exit.setLayoutData(new GridData(SWT.FILL,SWT.TOP,true,false,1,1));
		
	}



}