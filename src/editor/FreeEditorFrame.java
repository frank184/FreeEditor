package editor;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Insets;
import java.awt.Toolkit;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.BevelBorder;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.Element;
import javax.swing.undo.UndoManager;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.print.PrinterException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.Date;
import java.util.Scanner;

import javax.swing.SwingConstants;

@SuppressWarnings("serial")
final public class FreeEditorFrame extends JFrame implements FreeEditorControls
{
	/*
	 * Constants
	 */
	private static final File DEFAULT_FILE = new File("Untitled");
	private final File LAST_FILE = new File("lastFile");

	/*
	 * Document instances
	 */
	private File currentFile;
	private boolean currentFileIsDefault;
	private boolean documentTextSelected;
	private boolean documentChanged;
	private boolean documentEmpty;
	
	private int lineNumber = 1;
	private int columnNumber = 1;
	
	private UndoManager undoManager;

	/*
	 *  Global Components
	 */
	private FreeEditorFind findDialog;
	private JTextArea textArea;
	private JScrollPane jsp;
	private JPanel statusPanel;
	private JLabel lblStatusBar;

	// File
	private JMenuItem mntmSave;
	private JMenuItem mntmSaveAs;

	// Edit
	private JMenuItem mntmUndo;
	private JMenuItem mntmRedo;
	private JMenuItem mntmCut;
	private JMenuItem mntmCutPopup;
	private JMenuItem mntmCopy;
	private JMenuItem mntmCopyPopup;
	private JMenuItem mntmPaste;
	private JMenuItem mntmPastePopup;
	private JMenuItem mntmDelete;
	private JMenuItem mntmDeletePopup;
	private JMenuItem mntmFind;
	private JMenuItem mntmFindPopup;
	private JMenuItem mntmFindNext;
	private JMenuItem mntmFindNextPopup;
	private JMenuItem mntmReplace;
	private JMenuItem mntmReplacePopup;

	// Format
	private JCheckBoxMenuItem chckbxmntmWordWrap;

	// View
	private JCheckBoxMenuItem chckbxmntmStatusBar;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args)
	{
		EventQueue.invokeLater(new Runnable()
		{
			public void run()
			{
				try
				{
					FreeEditorFrame frame = new FreeEditorFrame();
					frame.setVisible(true);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public FreeEditorFrame()
	{
		super("FreeEditor");
		try
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException | UnsupportedLookAndFeelException e)
		{
			e.printStackTrace();
		}
		setBounds(100, 100, 450, 300);
		setLocationRelativeTo(null);
		getContentPane().setLayout(new BorderLayout());
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		addWindowListener(windowAdapter);

		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JPopupMenu popupMenu = new JPopupMenu();

		/*
		 *  File Menu & Menu Items
		 */
		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);

		JMenuItem mntmNew = new JMenuItem("New");
		mntmNew.addActionListener(newFile);
		mntmNew.setAccelerator(KeyStroke.getKeyStroke('N', Toolkit
				.getDefaultToolkit().getMenuShortcutKeyMask()));
		mnFile.add(mntmNew);

		JMenuItem mntmOpen = new JMenuItem("Open...");
		mntmOpen.addActionListener(open);
		mntmOpen.setAccelerator(KeyStroke.getKeyStroke('O', Toolkit
				.getDefaultToolkit().getMenuShortcutKeyMask()));
		mnFile.add(mntmOpen);

		mntmSave = new JMenuItem("Save");
		mntmSave.setEnabled(false);
		mntmSave.addActionListener(save);
		mntmSave.setAccelerator(KeyStroke.getKeyStroke('S', Toolkit
				.getDefaultToolkit().getMenuShortcutKeyMask()));
		mnFile.add(mntmSave);

		mntmSaveAs = new JMenuItem("Save As...");
		mntmSaveAs.addActionListener(saveAs);
		mnFile.add(mntmSaveAs);

		mnFile.add(new JSeparator());

		JMenuItem mntmPageSetup = new JMenuItem("Page Setup...");
		mntmPageSetup.addActionListener(pageSetup);
		mnFile.add(mntmPageSetup);

		JMenuItem mntmPrint = new JMenuItem("Print...");
		mntmPrint.setAccelerator(KeyStroke.getKeyStroke('P', Toolkit
				.getDefaultToolkit().getMenuShortcutKeyMask()));
		mntmPrint.addActionListener(print);
		mnFile.add(mntmPrint);

		mnFile.add(new JSeparator());

		JMenuItem mntmExit = new JMenuItem("Exit");
		mntmExit.addActionListener(exit);
		mnFile.add(mntmExit);

		/*
		 *  Edit Menu & Menu Items
		 */
		JMenu mnEdit = new JMenu("Edit");
		menuBar.add(mnEdit);

		mntmUndo = new JMenuItem("Undo");
		mntmUndo.setEnabled(false);
		mntmUndo.setAccelerator(KeyStroke.getKeyStroke('Z', Toolkit
				.getDefaultToolkit().getMenuShortcutKeyMask()));
		mntmUndo.addActionListener(undo);
		mnEdit.add(mntmUndo);

		mntmRedo = new JMenuItem("Redo");
		mntmRedo.setEnabled(false);
		mntmRedo.setAccelerator(KeyStroke.getKeyStroke('Y', Toolkit
				.getDefaultToolkit().getMenuShortcutKeyMask()));
		mntmRedo.addActionListener(redo);
		mnEdit.add(mntmRedo);

		mnEdit.add(new JSeparator());

		mntmCut = new JMenuItem("Cut");
		mntmCut.setEnabled(false);
		mntmCut.setAccelerator(KeyStroke.getKeyStroke('X', Toolkit
				.getDefaultToolkit().getMenuShortcutKeyMask()));
		mntmCut.addActionListener(cut);
		mnEdit.add(mntmCut);
		// Clone for JPopupMenu
		mntmCutPopup = new JMenuItem("Cut");
		mntmCutPopup.setEnabled(false);
		mntmCutPopup.setAccelerator(KeyStroke.getKeyStroke('X', Toolkit
				.getDefaultToolkit().getMenuShortcutKeyMask()));
		mntmCutPopup.addActionListener(cut);
		popupMenu.add(mntmCutPopup);

		mntmCopy = new JMenuItem("Copy");
		mntmCopy.setEnabled(false);
		mntmCopy.setAccelerator(KeyStroke.getKeyStroke('C', Toolkit
				.getDefaultToolkit().getMenuShortcutKeyMask()));
		mntmCopy.addActionListener(copy);
		mnEdit.add(mntmCopy);
		// Clone for JPopupMenu
		mntmCopyPopup = new JMenuItem("Copy");
		mntmCopyPopup.setEnabled(false);
		mntmCopyPopup.setAccelerator(KeyStroke.getKeyStroke('C', Toolkit
				.getDefaultToolkit().getMenuShortcutKeyMask()));
		mntmCopyPopup.addActionListener(copy);
		popupMenu.add(mntmCopyPopup);

		mntmPaste = new JMenuItem("Paste");
		mntmPaste.setAccelerator(KeyStroke.getKeyStroke('V', Toolkit
				.getDefaultToolkit().getMenuShortcutKeyMask()));
		mntmPaste.addActionListener(paste);
		mnEdit.add(mntmPaste);
		// Clone for JPopupMenu
		mntmPastePopup = new JMenuItem("Paste");
		mntmPastePopup.setAccelerator(KeyStroke.getKeyStroke('V', Toolkit
				.getDefaultToolkit().getMenuShortcutKeyMask()));
		mntmPastePopup.addActionListener(paste);
		popupMenu.add(mntmPastePopup);

		mntmDelete = new JMenuItem("Delete");
		mntmDelete.setEnabled(false);
		mntmDelete.setAccelerator(KeyStroke.getKeyStroke("DELETE"));
		mntmDelete.addActionListener(delete);
		mnEdit.add(mntmDelete);
		// Clone for JPopupMenu
		mntmDeletePopup = new JMenuItem("Delete");
		mntmDeletePopup.setEnabled(false);
		mntmDeletePopup.setAccelerator(KeyStroke.getKeyStroke("DELETE"));
		mntmDeletePopup.addActionListener(delete);
		popupMenu.add(mntmDeletePopup);

		mnEdit.add(new JSeparator());
		popupMenu.add(new JSeparator());

		mntmFind = new JMenuItem("Find...");
		mntmFind.setEnabled(false);
		mntmFind.setAccelerator(KeyStroke.getKeyStroke('F', Toolkit
				.getDefaultToolkit().getMenuShortcutKeyMask()));
		mntmFind.addActionListener(find);
		mnEdit.add(mntmFind);
		// Clone for JPopupMenu
		mntmFindPopup = new JMenuItem("Find...");
		mntmFindPopup.setEnabled(false);
		mntmFindPopup.setAccelerator(KeyStroke.getKeyStroke('F', Toolkit
				.getDefaultToolkit().getMenuShortcutKeyMask()));
		mntmFindPopup.addActionListener(find);
		popupMenu.add(mntmFindPopup);

		mntmFindNext = new JMenuItem("Find Next");
		mntmFindNext.setEnabled(false);
		mntmFindNext.setAccelerator(KeyStroke.getKeyStroke("F3"));
		mntmFindNext.addActionListener(findNext);
		mnEdit.add(mntmFindNext);
		// Clone for JPopupMenu
		mntmFindNextPopup = new JMenuItem("Find Next");
		mntmFindNextPopup.setEnabled(false);
		mntmFindNextPopup.setAccelerator(KeyStroke.getKeyStroke("F3"));
		mntmFindNextPopup.addActionListener(findNext);
		popupMenu.add(mntmFindNextPopup);

		mntmReplace = new JMenuItem("Replace...");
		mntmReplace.setEnabled(false);
		mntmReplace.setAccelerator(KeyStroke.getKeyStroke('H', Toolkit
				.getDefaultToolkit().getMenuShortcutKeyMask()));
		mntmReplace.addActionListener(replace);
		mnEdit.add(mntmReplace);
		// Clone for JPopupMenu
		mntmReplacePopup = new JMenuItem("Replace...");
		mntmReplacePopup.setEnabled(false);
		mntmReplacePopup.setAccelerator(KeyStroke.getKeyStroke('H', Toolkit
				.getDefaultToolkit().getMenuShortcutKeyMask()));
		mntmReplacePopup.addActionListener(replace);
		popupMenu.add(mntmReplacePopup);

		JMenuItem mntmGoTo = new JMenuItem("Go To...");
		mntmGoTo.setAccelerator(KeyStroke.getKeyStroke('G', Toolkit
				.getDefaultToolkit().getMenuShortcutKeyMask()));
		mntmGoTo.addActionListener(goTo);
		mnEdit.add(mntmGoTo);
		// Clone for JPopupMenu
		JMenuItem mntmGoToPopup = new JMenuItem("Go To...");
		mntmGoToPopup.setAccelerator(KeyStroke.getKeyStroke('G', Toolkit
				.getDefaultToolkit().getMenuShortcutKeyMask()));
		mntmGoToPopup.addActionListener(goTo);
		popupMenu.add(mntmGoToPopup);

		mnEdit.add(new JSeparator());
		popupMenu.add(new JSeparator());

		JMenuItem mntmSelectAll = new JMenuItem("Select All");
		mntmSelectAll.setAccelerator(KeyStroke.getKeyStroke('A', Toolkit
				.getDefaultToolkit().getMenuShortcutKeyMask()));
		mntmSelectAll.addActionListener(selectAll);
		mnEdit.add(mntmSelectAll);
		// Clone for JPopupMenu
		JMenuItem mntmSelectAllPopup = new JMenuItem("Select All");
		mntmSelectAll.setAccelerator(KeyStroke.getKeyStroke('A', Toolkit
				.getDefaultToolkit().getMenuShortcutKeyMask()));
		mntmSelectAllPopup.addActionListener(selectAll);
		popupMenu.add(mntmSelectAllPopup);

		JMenuItem mntmTimeDate = new JMenuItem("Time/Date");
		mntmTimeDate.setAccelerator(KeyStroke.getKeyStroke("F5"));
		mntmTimeDate.addActionListener(timeDate);
		mnEdit.add(mntmTimeDate);
		// Clone for JPopupMenu
		JMenuItem mntmTimeDatePopup = new JMenuItem("Time/Date");
		mntmTimeDatePopup.setAccelerator(KeyStroke.getKeyStroke("F5"));
		mntmTimeDatePopup.addActionListener(timeDate);
		popupMenu.add(mntmTimeDatePopup);

		/*
		 *  Format Menu & Menu Items
		 */
		JMenu mnFormat = new JMenu("Format");
		menuBar.add(mnFormat);

		chckbxmntmWordWrap = new JCheckBoxMenuItem("Word Wrap");
		chckbxmntmWordWrap.addActionListener(wordWrap);
		mnFormat.add(chckbxmntmWordWrap);

		JMenuItem mntmFont = new JMenuItem("Font...");
		mntmFont.addActionListener(selectFont);
		mnFormat.add(mntmFont);

		/*
		 *  View Menu & Menu Items
		 */
		JMenu mnView = new JMenu("View");
		menuBar.add(mnView);

		chckbxmntmStatusBar = new JCheckBoxMenuItem("Status Bar");
		chckbxmntmStatusBar.addActionListener(statusBar);
		mnView.add(chckbxmntmStatusBar);

		/*
		 *  Help Menu & Menu Items
		 */
		JMenu mnHelp = new JMenu("Help");
		menuBar.add(mnHelp);

		JMenuItem mntmViewHelp = new JMenuItem("View Help");
		mntmViewHelp.addActionListener(viewHelp);
		mnHelp.add(mntmViewHelp);

		JMenuItem mntmAboutFreeEditor = new JMenuItem("About Free Editor");
		mntmAboutFreeEditor.addActionListener(viewAbout);
		mnHelp.add(mntmAboutFreeEditor);

		/*
		 * TextArea
		 */
		textArea = new JTextArea();
		textArea.setFont(new Font("Lucida Console", Font.PLAIN, 14));
		textArea.setMargin(new Insets(2, 2, 0, 0));
		textArea.setComponentPopupMenu(popupMenu);
		textArea.addCaretListener(caretUpdate);
		setDocumentListener();

		/*
		 * JScrollPane
		 */
		jsp = new JScrollPane();
		jsp.add(textArea);
		jsp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		jsp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		jsp.setViewportView(textArea);
		getContentPane().add(jsp, BorderLayout.CENTER);

		statusPanel = new JPanel();
		statusPanel.setVisible(false);
		statusPanel.setLayout(new BorderLayout());
		statusPanel.setBorder(new BevelBorder(BevelBorder.LOWERED));
		statusPanel.setPreferredSize(new Dimension(getWidth(), 16));

		lblStatusBar = new JLabel();
		lblStatusBar.setHorizontalAlignment(SwingConstants.RIGHT);
		updateStatusBar();
		statusPanel.add(lblStatusBar);
		getContentPane().add(statusPanel, BorderLayout.SOUTH);
		
		findDialog = new FreeEditorFind(this);
		undoManager = new UndoManager();
		loadLastFile();
	}

	/*
	 *  Listeners
	 */
	private WindowAdapter windowAdapter = new WindowAdapter()
	{
		@Override
		public void windowClosing(WindowEvent e)
		{
			exit();
		}
	};
	
	private UndoableEditListener undoableEdit = new UndoableEditListener()
	{
		@Override
		public void undoableEditHappened(UndoableEditEvent e)
		{
			undoManager.addEdit(e.getEdit());
		}
	};

	private DocumentListener documentUpdate = new DocumentListener()
	{
		@Override
		public void changedUpdate(DocumentEvent e)
		{
			documentUpdate();
		}

		@Override
		public void insertUpdate(DocumentEvent e)
		{
			documentUpdate();
		}

		@Override
		public void removeUpdate(DocumentEvent e)
		{
			documentUpdate();
		}
	};

	private CaretListener caretUpdate = new CaretListener()
	{
		@Override
		public void caretUpdate(CaretEvent e)
		{
			updateCaret();
		}
	};

	private ActionListener newFile = new ActionListener()
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			newFile();
		}
	};

	private ActionListener open = new ActionListener()
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			open();
		}
	};

	private ActionListener save = new ActionListener()
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			save();
		}
	};

	private ActionListener saveAs = new ActionListener()
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			saveAs();
		}
	};

	private ActionListener pageSetup = new ActionListener()
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			pageSetup();
		}
	};

	private ActionListener print = new ActionListener()
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			print();
		}
	};

	private ActionListener exit = new ActionListener()
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			exit();
		}
	};

	private ActionListener undo = new ActionListener()
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			undo();
		}
	};

	private ActionListener redo = new ActionListener()
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			redo();
		}
	};

	private ActionListener cut = new ActionListener()
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			cut();
		}
	};

	private ActionListener copy = new ActionListener()
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			copy();
		}
	};

	private ActionListener paste = new ActionListener()
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			paste();
		}
	};

	private ActionListener delete = new ActionListener()
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			delete();
		}
	};

	private ActionListener find = new ActionListener()
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			find();
		}
	};

	private ActionListener findNext = new ActionListener()
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			findNext();
		}
	};

	private ActionListener replace = new ActionListener()
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			replace();
		}
	};

	private ActionListener goTo = new ActionListener()
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			goTo();
		}
	};

	private ActionListener selectAll = new ActionListener()
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			selectAll();
		}
	};

	private ActionListener timeDate = new ActionListener()
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			timeDate();
		}
	};

	private ActionListener wordWrap = new ActionListener()
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			wordWrap();
		}
	};

	private ActionListener selectFont = new ActionListener()
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			selectFont();
		}
	};

	private ActionListener statusBar = new ActionListener()
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			statusBar();
		}
	};

	private ActionListener viewHelp = new ActionListener()
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			viewHelp();
		}
	};

	private ActionListener viewAbout = new ActionListener()
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			viewAbout();
		}
	};

	/*
	 *  EditorControls implementations
	 * @see editor.EditorControls
	 */
	@Override
	public void newFile()
	{
		textArea.setText("");
		setDefaultFile();
	}

	@Override
	public void open()
	{
		JFileChooser openDialog = new JFileChooser();
		openDialog.showOpenDialog(this);
		File selectedFile = openDialog.getSelectedFile();
		if (selectedFile != null)
		{
			currentFile = selectedFile;
			openCurrentFile();
		}
	}

	@Override
	public void save()
	{
		if (currentFile != null)
		{
			if (currentFile.exists())
			{
				saveCurrentFile();
			}
			else
			{
				saveAs();
			}
		}
		else
		{
			saveAs();
		}
	}

	@Override
	public void saveAs()
	{
		JFileChooser saveAsDialog = new JFileChooser("Save As");
		saveAsDialog.showDialog(this, "Save As");
		File selectedFile = saveAsDialog.getSelectedFile();
		if (selectedFile != null)
		{
			currentFile = selectedFile;
			saveCurrentFile();
		}
	}

	@Override
	public void print()
	{
		try 
		{
			textArea.print(new MessageFormat(this.getTitle()), new MessageFormat("{0}"), true, null, null, true);
		}
		catch (PrinterException e)
		{
			JOptionPane.showMessageDialog(this, "Couldn't print the document", "Printing error", JOptionPane.ERROR_MESSAGE);
		}
	}

	@Override
	public void pageSetup()
	{
		try 
		{
			textArea.print(new MessageFormat(this.getTitle()), new MessageFormat("{0}"), true, null, null, true);
		}
		catch (PrinterException e)
		{
			JOptionPane.showMessageDialog(this, "Couldn't print the document", "Printing error", JOptionPane.ERROR_MESSAGE);
		}
	}

	@Override
	public void exit()
	{
		if (documentChanged)
		{
			FreeEditorSaveChanges saveChangesDialog = new FreeEditorSaveChanges(this);
			switch (saveChangesDialog.showDialog())
			{
			case FreeEditorSaveChanges.DONT_SAVE:
				writeLastFile();
				System.exit(0);
				break;
			case FreeEditorSaveChanges.SAVE:
				save();
				writeLastFile();
				System.exit(0);
				break;
			}
		}
		else
		{
			System.exit(0);
		}
	}

	@Override
	public void undo()
	{
		undoManager.undo();
		if (!undoManager.canUndo())
		{
			setDocumentChanged(false);
		}
	}

	@Override
	public void redo()
	{
		undoManager.redo();
	}

	@Override
	public void cut()
	{
		textArea.cut();
	}

	@Override
	public void copy()
	{
		textArea.copy();
	}

	@Override
	public void paste()
	{
		textArea.paste();
	}

	@Override
	public void delete()
	{
		textArea.replaceSelection("");
	}

	@Override
	public void find()
	{
		if (!findDialog.isVisible())
		{
			findDialog.setTitle("Find");
			if (documentTextSelected)
			{
				findDialog.setFindWhat(textArea.getSelectedText());
			}
			findDialog.showDialog();
		}
		else
		{
			findDialog.toFront();
		}
	}

	@Override
	public void findNext()
	{
		if (!findDialog.isVisible() && findDialog.getFindWhat().isEmpty())
		{
			findDialog.setTitle("Find Next");
			if (documentTextSelected)
			{
				findDialog.setFindWhat(textArea.getSelectedText());
			}
			findDialog.showDialog();
		}
		else
		{
			findDialog.findNext();
		}
	}

	@Override
	public void replace()
	{
		if (!findDialog.isVisible())
		{
			findDialog.setTitle("Replace");
			if (documentTextSelected)
			{
				findDialog.setReplaceWith(textArea.getSelectedText());
			}
			findDialog.showDialog();
		}
		else
		{
			findDialog.toFront();
		}
	}

	@Override
	public void goTo()
	{
		try
		{
			int returnVal = Integer.parseInt((String) JOptionPane.showInputDialog(this,
					"Line Number:", "Go To Line", JOptionPane.PLAIN_MESSAGE, null, null, 1));
			setCaretToLine(returnVal);
		}
		catch (NumberFormatException nfe)
		{
			System.err.println("Error: goTo() - line number must be an integer.");
		}
	}

	@Override
	public void selectAll()
	{
		textArea.selectAll();
	}

	@Override
	public void timeDate()
	{

		Date date = new Date();
		textArea.insert(date.toString(), textArea.getCaretPosition());
	}

	@Override
	public void wordWrap()
	{
		setWordWrapTrigger(chckbxmntmWordWrap.getState());
	}

	@Override
	public void selectFont()
	{
		JFontChooser fontDialog = new JFontChooser();
		int dialogCode = fontDialog.showDialog(this);
		if (dialogCode == JFontChooser.OK_OPTION)
		{
			textArea.setFont(fontDialog.getSelectedFont());
		}
	}

	@Override
	public void statusBar()
	{
		setStatusBarSetVisible(chckbxmntmStatusBar.getState());
	}

	@Override
	public void viewHelp()
	{
		FreeEditorHelp helpDialog = new FreeEditorHelp(this);
		helpDialog.setVisible(true);
	}

	@Override
	public void viewAbout()
	{
		FreeEditorAbout aboutDialog = new FreeEditorAbout(this);
		aboutDialog.setVisible(true);
	}

	/*
	 *  Private Methods
	 */
	private void openCurrentFile()
	{
		if (currentFile != null)
		{
			try
			{
				FileReader r = new FileReader(currentFile);
				textArea.setText("");
				textArea.read(r, null);
				r.close();

				setDocumentListener();
				setDocumentChanged(false);
				setDocumentEmpty();
				printStuff();
			}
			catch (FileNotFoundException e)
			{
				e.printStackTrace();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}

	private void saveCurrentFile()
	{
		if (documentChanged && currentFile != null)
		{
			try
			{
				FileWriter w = new FileWriter(currentFile);
				textArea.write(w);
				w.close();

				setDocumentChanged(false);
				setDocumentEmpty();
				printStuff();
			}
			catch (FileNotFoundException e)
			{
				e.printStackTrace();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}

	private void setDefaultFile()
	{
		currentFile = DEFAULT_FILE;
		currentFileIsDefault = true;
		setDocumentEmpty();
		setDocumentChanged(false);
		setDocumentListener();
		printStuff();
	}

	private void loadLastFile()
	{
		Scanner r = null;
		try
		{
			r = new Scanner(LAST_FILE);
			if (r.hasNextLine())
			{
				File lastFile = new File(r.nextLine());
				if (lastFile.exists())
				{
					currentFile = lastFile;
					openCurrentFile();
				}
				else
				{
					try
					{
						FileWriter w = new FileWriter(LAST_FILE);
						w.write("");
					}
					catch (IOException e)
					{
						e.printStackTrace();
					}
				}
				
			}
			else
			{
				r.close();
				throw new FileNotFoundException();
			}
			r.close();
		}
		catch (FileNotFoundException e)
		{
			setDefaultFile();
		}
	}

	private void writeLastFile()
	{
		if (!currentFile.equals(DEFAULT_FILE))
		{
			try
			{
				FileWriter w = new FileWriter(LAST_FILE);
				w.write(currentFile.getAbsolutePath());
				w.close();
			}
			catch (FileNotFoundException e)
			{
				e.printStackTrace();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}

	private void setWordWrapTrigger(boolean enabled)
	{
		if (enabled)
		{
			jsp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		}
		else
		{
			jsp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		}
		textArea.setLineWrap(enabled);
		textArea.setWrapStyleWord(enabled);
	}

	private void setStatusBarSetVisible(boolean enabled)
	{
		statusPanel.setVisible(enabled);
	}

	private void documentUpdate()
	{
		setDocumentChanged(true);
		setDocumentEmpty();
		printStuff();
	}

	private void updateCaret()
	{
		try
		{
			int position = textArea.getCaretPosition();
			lineNumber = textArea.getLineOfOffset(position);
			columnNumber = position - textArea.getLineStartOffset(lineNumber);
			columnNumber += 1;
			lineNumber += 1;
			updateStatusBar();
		}
		catch (Exception err)
		{
			err.printStackTrace();
		}

		setDocumentTextSelected();
		setDocumentEmpty();

		printStuff();
	}

	private void updateStatusBar()
	{
		lblStatusBar.setText("Line " + lineNumber + ", Col " + columnNumber);
	}

	private void updateTitle()
	{
		if (documentChanged)
		{
			setTitle("*" + currentFile.getName() + " - FreeEditor");
		}
		else
		{
			setTitle(currentFile.getName() + " - FreeEditor");
		}
	}

	private void setDocumentListener()
	{
		textArea.getDocument().addDocumentListener(documentUpdate);
		textArea.getDocument().addUndoableEditListener(undoableEdit);
	}

	private void setDocumentChanged(boolean enabled)
	{
		documentChanged = enabled;
		documentUpdateSetEnabled(enabled);
		updateTitle();
	}

	private void setDocumentEmpty()
	{
		if (textArea.getText().isEmpty())
		{
			documentEmpty = true;
			documentEmptySetEnabled(false);
			if (currentFileIsDefault)
			{
				setDocumentChanged(false);
			}
		}
		else
		{
			documentEmpty = false;
			documentEmptySetEnabled(true);
		}
	}

	private void setDocumentTextSelected()
	{
		if (textArea.getSelectedText() != null)
		{
			documentTextSelected = true;
			textSelectedSetEnabled(true);
		}
		else
		{
			documentTextSelected = false;
			textSelectedSetEnabled(false);
		}
	}
	
	private void setCaretToLine(int line)
	{
		Element root = textArea.getDocument().getDefaultRootElement();
		line = Math.max(line, 1);
		line = Math.min(line, root.getElementCount());
		int startOfLineOffset = root.getElement(line - 1).getStartOffset();
		try
		{
			textArea.setCaretPosition(startOfLineOffset);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private void textSelectedSetEnabled(boolean enabled)
	{
		mntmCut.setEnabled(enabled);
		mntmCopy.setEnabled(enabled);
		mntmDelete.setEnabled(enabled);

		mntmCutPopup.setEnabled(enabled);
		mntmCopyPopup.setEnabled(enabled);
		mntmDeletePopup.setEnabled(enabled);
	}

	private void documentUpdateSetEnabled(boolean enabled)
	{
		mntmSave.setEnabled(enabled);
		mntmUndo.setEnabled(enabled);
		mntmRedo.setEnabled(enabled);
	}

	private void documentEmptySetEnabled(boolean enabled)
	{
		mntmFind.setEnabled(enabled);
		mntmFindNext.setEnabled(enabled);
		mntmReplace.setEnabled(enabled);

		mntmFindPopup.setEnabled(enabled);
		mntmFindNextPopup.setEnabled(enabled);
		mntmReplacePopup.setEnabled(enabled);
	}

	private void printStuff()
	{
		System.out.println("currentFile: " + currentFile);
		System.out.println("currentFileIsDefault: " + currentFileIsDefault);
		System.out.println("documentEmpty: " + documentEmpty);
		System.out.println("documentChanged: " + documentChanged);
		System.out.println("documentTextSelected: " + documentTextSelected);
		System.out.println();
	}

	protected String getCurrentFile()
	{
		return currentFile.getName();
	}
	
	protected JTextArea getTextArea()
	{
		return textArea;
	}
} // FreeEditorFrame class
