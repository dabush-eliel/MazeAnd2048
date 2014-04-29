package gameMaze.view.components;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

public class ScoreLabel{
	Label score;
	
	public ScoreLabel(Composite parent, int style) {
		score = new Label(parent,style);
		score.setLayoutData(new GridData(SWT.FILL,SWT.UNDERLINE_SINGLE,true,false,3,5));
		score.setText("Score: 0");
		score.setSize(30, 10);
	}
	
	public void setText(String str){
		score.setText(str);
	}
	
}

