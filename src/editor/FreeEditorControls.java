package editor;

public abstract interface FreeEditorControls
{
	// File Menu
	public abstract void newFile();
	public abstract void open();
	public abstract void save();
	public abstract void saveAs();
	public abstract void print();
	public abstract void pageSetup();
	public abstract void exit();
	
	// Edit Menu
	public abstract void undo();
	public abstract void redo();
	public abstract void cut();
	public abstract void copy();
	public abstract void paste();
	public abstract void delete();
	public abstract void find();
	public abstract void findNext();
	public abstract void replace();
	public abstract void goTo();
	public abstract void selectAll();
	public abstract void timeDate();
	
	// Format Menu
	public abstract void wordWrap();
	public abstract void selectFont();
	
	// View Menu
	public abstract void statusBar();
	
	// Help
	public abstract void viewHelp();
	public abstract void viewAbout();
}
