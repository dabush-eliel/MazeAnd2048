package game2048.view.components;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Decorations;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;

public class Menu2048 {
	Menu menuBar;
	MenuItem fileMenuHeader;
	MenuItem editMenuHeader;
	Menu fileMenu;
	Menu editMenu;
	MenuItem fileSaveItem;
	MenuItem fileLoadItem;
	MenuItem fileExitItem;
	MenuItem editUndoItem;
	MenuItem editRestartItem;	
	
	public Menu2048(Decorations parent, int style) {
		//super(parent, style);
		menuBar = new Menu(parent, style);
		fileMenuHeader = new MenuItem(menuBar, SWT.CASCADE);
	    fileMenuHeader.setText("File");
	    
		editMenuHeader = new MenuItem(menuBar, SWT.CASCADE);
	    editMenuHeader.setText("Edit");
		
	    fileMenu = new Menu(parent, SWT.DROP_DOWN);
	    fileMenuHeader.setMenu(fileMenu);
	    
	    editMenu = new Menu(parent, SWT.DROP_DOWN);
	    editMenuHeader.setMenu(editMenu);
	    
	    fileSaveItem = new MenuItem(fileMenu, SWT.PUSH);
	    fileSaveItem.setText("Save");
	    
	    fileLoadItem = new MenuItem(fileMenu, SWT.PUSH);
	    fileLoadItem.setText("Load");

	    fileExitItem = new MenuItem(fileMenu, SWT.PUSH);
	    fileExitItem.setText("Exit");
	    
	    editUndoItem = new MenuItem(editMenu, SWT.PUSH);
	    editUndoItem.setText("Undo");
	    
	    editRestartItem = new MenuItem(editMenu, SWT.PUSH);
	    editRestartItem.setText("Restart");
	}
	
	
	public Menu getMenuBar() {
		return menuBar;
	}
	
	public MenuItem getFileSaveItem() {
		return fileSaveItem;
	}


	public MenuItem getFileLoadItem() {
		return fileLoadItem;
	}


	public MenuItem getFileExitItem() {
		return fileExitItem;
	}


	public MenuItem getEditUndoItem() {
		return editUndoItem;
	}


	public MenuItem getEditRestartItem() {
		return editRestartItem;
	}	
}