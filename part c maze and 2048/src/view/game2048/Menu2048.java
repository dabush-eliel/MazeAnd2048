package view.game2048;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Decorations;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;

/**
 * The menu of 2048 Game.
 * @author Eliel Dabush and Oleg Glizerin.
 *
 */
public class Menu2048 {
	/**
	 * Menu buttons.
	 */
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
	
	/**
	 * 
	 * @param parent shell.
	 * @param style SWT style.
	 */
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
	
	/**
	 * 
	 * @return menuBar.
	 */
	public Menu getMenuBar() {
		return menuBar;
	}
	/**
	 * 
	 * @return fileSaveItem.
	 */
	
	public MenuItem getFileSaveItem() {
		return fileSaveItem;
	}

	/**
	 * 
	 * @return fileLoadItem.
	 */
	public MenuItem getFileLoadItem() {
		return fileLoadItem;
	}

	/**
	 * 
	 * @return fileExitItem.
	 */
	public MenuItem getFileExitItem() {
		return fileExitItem;
	}

	/**
	 * 
	 * @return editUndoItem.
	 */
	public MenuItem getEditUndoItem() {
		return editUndoItem;
	}

	/**
	 * 
	 * @return ditRestartItem.
	 */
	public MenuItem getEditRestartItem() {
		return editRestartItem;
	}	
}