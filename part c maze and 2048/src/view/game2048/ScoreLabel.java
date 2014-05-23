package view.game2048;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
	/**
	 * 
	 * @author Eliel Dabush and Oleg Glizerin.
	 *
	 */
public class ScoreLabel{
	Label score;
	
	/**
	 * 
	 * @param parent shell.
	 * @param style SWT style.
	 */
	public ScoreLabel(Composite parent, int style) {
		score = new Label(parent,style);
		score.setLayoutData(new GridData(SWT.FILL,SWT.UNDERLINE_SINGLE,true,false,3,5));
		score.setText("Score: 0");
		score.setSize(30, 10);
	}
	
	/**
	 * 
	 * @param str the score to set.
	 */
	public void setText(String str){
		score.setText(str);
	}
	
}
