package gameMaze.view.components;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Decorations;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;

public class MenuMaze {
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
//	MenuActions action;
	
	public MenuMaze(Decorations parent, int style) {
		//super(parent, style);
		
		//this.action = action;
		
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
	
/*	public int doAction(){
		MenuMaze m = new MenuMaze(menuBar.getShell(), menuBar.getStyle(), null);
		m.menuBar = this.menuBar;
		m.fileMenuHeader = this.fileMenuHeader;
		m.editMenuHeader = this.editMenuHeader;
		m.fileMenu = this.fileMenu;
		m.editMenu = this.editMenu;
		m.fileSaveItem = this.fileSaveItem;
		m.fileLoadItem = this.fileLoadItem;
		m.fileExitItem = this.fileExitItem;
		m.editUndoItem = this.editUndoItem;
		m.editRestartItem = this.editRestartItem;

		return action.doAction(menuBar.getShell(), m);
	}
	*/
	
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