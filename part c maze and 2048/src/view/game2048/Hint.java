package view.game2048;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class Hint {
	Label hint;	
	Text hintText;
	Label hintNumberLabel;
	Label hintDepthLabel;
	String[] hintNum 	= "FULL 1 2 3 4 5 6 7 8 9".split(" ");
	String[] hintDepth 	= "DEFAULT-(7) 3 5 7 9 11 13 15".split(" ");
	Combo combo1;
	Combo combo2;
	
	//for hint
	public Hint(Composite parent, int style) {
		
		hintDepthLabel = new Label(parent,style);
		hintDepthLabel.setText("Minimax depth: ");
		
		hintNumberLabel = new Label(parent,style);
		hintNumberLabel.setText("Number of hints: ");
	
		hintDepthLabel.setLayoutData(new GridData(SWT.FILL,SWT.TOP,false,false,1,8));
		combo1 = new Combo(parent, SWT.DROP_DOWN);
		combo1.setItems(hintDepth);
		combo1.select(0);
		combo1.setLayoutData(new GridData(SWT.FILL,SWT.RIGHT,true,false,1,1));
		


		hintNumberLabel.setLayoutData(new GridData(SWT.FILL,SWT.TOP,false,false,1,8));
		combo2 = new Combo(parent, SWT.DROP_DOWN | SWT.READ_ONLY);
		combo2.setItems(hintNum);
		combo2.select(0);
		combo2.setLayoutData(new GridData(SWT.FILL,SWT.RIGHT,false,false,1,1));

		hint 		= new Label(parent,style);
		hint.setLayoutData(new GridData(SWT.FILL,SWT.TOP,false,false,1,1));		
		hint.setText("Hint: --");
		hint.setSize(50, 20);

		
	}
	
	public Label getHint() {
		return hint;
	}

	public Combo getCombo1() {
		return combo1;
	}

	public Combo getCombo2() {
		return combo2;
	}
	
	public void setText(String hintString){
		hint.setText(hintString);
	}
	
	public Text getHintText(){
		return hintText;
	}
}
