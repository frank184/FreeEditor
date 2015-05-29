package editor;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Insets;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JTextArea;
import javax.swing.border.BevelBorder;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.Date;

@SuppressWarnings("serial")
final public class FreeEditorFrame extends JFrame implements FreeEditorControls
{
	private JCheckBoxMenuItem chckbxmntmWordWrap;
	private JCheckBoxMenuItem chckbxmntmStatusBar;

	private JTextArea textArea;
	private JScrollPane jsp;
	private JPanel statusPanel;
	private JLabel lblStatusBar;

	private int lineNumber = 1;
	private int columnNumber = 1;

	private boolean documentChanged;
	private File currentFile;

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
		setBounds(100, 100, 450, 300);
		setLocationRelativeTo(null);
		getContentPane().setLayout(new BorderLayout());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JPopupMenu popupMenu = new JPopupMenu();
		getContentPane().add(popupMenu);

		/*
		 *  File Menu & Menu Items
		 */
		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);

		JMenuItem mntmNew = new JMenuItem("New");
		mntmNew.addActionListener(newFile);
		mnFile.add(mntmNew);

		JMenuItem mntmOpen = new JMenuItem("Open...");
		mntmOpen.addActionListener(open);
		mnFile.add(mntmOpen);

		JMenuItem mntmSave = new JMenuItem("Save");
		mntmSave.addActionListener(save);
		mnFile.add(mntmSave);

		JMenuItem mntmSaveAs = new JMenuItem("Save As...");
		mntmSave.addActionListener(saveAs);
		mnFile.add(mntmSaveAs);

		mnFile.add(new JSeparator());

		JMenuItem mntmPageSetup = new JMenuItem("Page Setup...");
		mntmPageSetup.addActionListener(pageSetup);
		mnFile.add(mntmPageSetup);

		JMenuItem mntmPrint = new JMenuItem("Print...");
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

		JMenuItem mntmUndo = new JMenuItem("Undo");
		mntmUndo.addActionListener(undo);
		mnEdit.add(mntmUndo);

		mnEdit.add(new JSeparator());

		JMenuItem mntmCut = new JMenuItem("Cut");
		mntmCut.addActionListener(cut);
		mnEdit.add(mntmCut);

		JMenuItem mntmCopy = new JMenuItem("Copy");
		mntmCopy.addActionListener(copy);
		mnEdit.add(mntmCopy);

		JMenuItem mntmPaste = new JMenuItem("Paste");
		mntmPaste.addActionListener(paste);
		mnEdit.add(mntmPaste);

		JMenuItem mntmDelete = new JMenuItem("Delete");
		mntmDelete.addActionListener(delete);
		mnEdit.add(mntmDelete);

		mnEdit.add(new JSeparator());

		JMenuItem mntmFind = new JMenuItem("Find...");
		mntmFind.addActionListener(find);
		mnEdit.add(mntmFind);

		JMenuItem mntmFindNext = new JMenuItem("Find Next");
		mntmFindNext.addActionListener(findNext);
		mnEdit.add(mntmFindNext);

		JMenuItem mntmReplace = new JMenuItem("Replace...");
		mntmReplace.addActionListener(replace);
		mnEdit.add(mntmReplace);

		JMenuItem mntmGoTo = new JMenuItem("Go To...");
		mntmGoTo.addActionListener(goTo);
		mnEdit.add(mntmGoTo);

		mnEdit.add(new JSeparator());

		JMenuItem mntmSelectAll = new JMenuItem("Select All");
		mntmSelectAll.addActionListener(selectAll);
		mnEdit.add(mntmSelectAll);

		JMenuItem mntmTimeDate = new JMenuItem("Time/Date");
		mntmTimeDate.addActionListener(timeDate);
		mnEdit.add(mntmTimeDate);

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
		textArea.setMargin(new Insets(1, 0, 0, 0));
		textArea.addCaretListener(new CaretListener()
		{
			@Override
			public void caretUpdate(CaretEvent e)
			{
				/**
				 * @TODO implement columns based of text measurements
				 */
				documentChanged = true;
				try
				{
					int position = textArea.getCaretPosition();
					lineNumber = textArea.getLineOfOffset(position);
					columnNumber = position - textArea.getLineStartOffset(lineNumber);
					columnNumber += 1;
					lineNumber += 1;
				}
				catch (Exception err)
				{
					err.printStackTrace();
				}
				updateStatusBar();
			}
		});
		textArea.getDocument().addUndoableEditListener(new UndoableEditListener()
		{
			
			@Override
			public void undoableEditHappened(UndoableEditEvent e)
			{
				
			}
		});

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
		statusPanel.setLayout(new BoxLayout(statusPanel, BoxLayout.X_AXIS));
		statusPanel.setBorder(new BevelBorder(BevelBorder.LOWERED));
		statusPanel.setPreferredSize(new Dimension(getWidth(), 16));

		lblStatusBar = new JLabel();
		updateStatusBar();
		statusPanel.add(lblStatusBar);

		getContentPane().add(statusPanel, BorderLayout.SOUTH);

		currentFile = new File("Untitled");
		setTitle(getTitle() + " - " + currentFile.getName());
	}

	/*
	 *  ActionListeners
	 */
	ActionListener newFile = new ActionListener()
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
	}

	@Override
	public void open()
	{
	}

	@Override
	public void save()
	{
	}

	@Override
	public void saveAs()
	{
	}

	@Override
	public void print()
	{
	}

	@Override
	public void pageSetup()
	{
	}

	@Override
	public void exit()
	{
		dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
	}

	@Override
	public void undo()
	{
	}

	@Override
	public void cut()
	{
	}

	@Override
	public void copy()
	{
	}

	@Override
	public void paste()
	{
	}

	@Override
	public void delete() throws NullPointerException
	{
		textArea.setText(textArea.getText().replace(textArea.getSelectedText(), ""));
	}

	@Override
	public void find()
	{
	}

	@Override
	public void findNext()
	{
	}

	@Override
	public void replace()
	{
	}

	@Override
	public void goTo()
	{
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
		textArea.append(date.toString());
	}

	@Override
	public void wordWrap()
	{
		setWordWrapEnabled(chckbxmntmWordWrap.getState());
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
		setStatusBarEnabled(chckbxmntmStatusBar.getState());
	}

	@Override
	public void viewHelp()
	{
		FreeEditorHelp helpDialog = new FreeEditorHelp();
		helpDialog.setVisible(true);
	}

	@Override
	public void viewAbout()
	{
		FreeEditorAbout aboutDialog = new FreeEditorAbout();
		aboutDialog.setVisible(true);
	}

	/*
	 *  Private Methods
	 */
	private void setWordWrapEnabled(boolean enabled)
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
	}

	private void setStatusBarEnabled(boolean enabled)
	{
		statusPanel.setVisible(enabled);
	}

	private void updateStatusBar()
	{
		lblStatusBar.setText("Line " + lineNumber + ", Col " + columnNumber);
	}
} // FreeEditorFrame class
