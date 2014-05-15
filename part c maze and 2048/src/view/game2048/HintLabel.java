package view.game2048;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

public class HintLabel {
	Label hint;
	
	public HintLabel(Composite parent, int style) {
		hint = new Label(parent,style);
		hint.setLayoutData(new GridData(SWT.FILL,SWT.TOP,true,false,1,1));
		hint.setText("hint: 0");
		hint.setSize(30, 10);
	}
	
	public void setText(String hintString){
		hint.setText(hintString);
	}
}
