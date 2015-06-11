package editor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JCheckBox;
import javax.swing.JRadioButton;
import javax.swing.text.BadLocationException;

@SuppressWarnings("serial")
public class FreeEditorFind extends JDialog implements ActionListener,
		ItemListener
{
	private boolean direction;
	
	/*
	 * Global Components
	 */
	private JTextArea textArea;

	private JTextField fldFindWhat;
	private JTextField fldReplaceWith;

	private JButton btnFindNext;
	private JButton btnReplace;
	private JButton btnReplaceAll;
	private JCheckBox chckbxMatchCase;
	private JButton btnCancel;

	private JRadioButton rdbtnUp;
	private JRadioButton rdbtnDown;

	/**
	 * Create the dialog.
	 */
	public FreeEditorFind(FreeEditorFrame editor)
	{
		init();
		setLocationRelativeTo(editor);
		textArea = editor.getTextArea();
	}

	public void init()
	{
		setTitle("Find");
		setBounds(100, 100, 350, 157);
		setLocationRelativeTo(null);
		getContentPane().setLayout(null);

		JLabel lblFindWhat = new JLabel("Find what:");
		lblFindWhat.setBounds(10, 14, 80, 14);
		getContentPane().add(lblFindWhat);

		JLabel lblReplaceWith = new JLabel("Replace with:");
		lblReplaceWith.setBounds(10, 45, 80, 14);
		getContentPane().add(lblReplaceWith);

		fldFindWhat = new JTextField();
		fldFindWhat.setBounds(92, 11, 119, 20);
		getContentPane().add(fldFindWhat);
		fldFindWhat.setColumns(10);

		fldReplaceWith = new JTextField();
		fldReplaceWith.setBounds(92, 42, 119, 20);
		getContentPane().add(fldReplaceWith);
		fldReplaceWith.setColumns(10);

		btnFindNext = new JButton("Find Next");
		btnFindNext.setBounds(221, 5, 103, 23);
		btnFindNext.addActionListener(this);
		getContentPane().add(btnFindNext);

		btnReplace = new JButton("Replace");
		btnReplace.setBounds(221, 33, 103, 23);
		btnReplace.addActionListener(this);
		getContentPane().add(btnReplace);

		btnReplaceAll = new JButton("Replace All");
		btnReplaceAll.setBounds(221, 61, 103, 23);
		btnReplaceAll.addActionListener(this);
		getContentPane().add(btnReplaceAll);

		btnCancel = new JButton("Cancel");
		btnCancel.setBounds(221, 89, 103, 23);
		btnCancel.addActionListener(this);
		getContentPane().add(btnCancel);

		chckbxMatchCase = new JCheckBox("Match case");
		chckbxMatchCase.setBounds(6, 89, 97, 23);
		getContentPane().add(chckbxMatchCase);

		ButtonGroup btnGroup = new ButtonGroup();
		rdbtnUp = new JRadioButton("Up");
		rdbtnUp.setBounds(156, 69, 65, 23);
		rdbtnUp.addItemListener(this);
		getContentPane().add(rdbtnUp);
		btnGroup.add(rdbtnUp);

		rdbtnDown = new JRadioButton("Down");
		rdbtnDown.setBounds(156, 89, 65, 23);
		rdbtnDown.setSelected(true);
		rdbtnUp.addItemListener(this);
		getContentPane().add(rdbtnDown);
		btnGroup.add(rdbtnDown);
		
		direction = false;
	}

	public void showDialog()
	{
		setVisible(true);
	}

	public void setFindWhat(String text)
	{
		fldFindWhat.setText(text);
	}

	public void setReplaceWith(String text)
	{
		fldReplaceWith.setText(text);
	}

	@Override
	public void itemStateChanged(ItemEvent e)
	{
		if (e.getStateChange() == 1 && e.getSource().equals(rdbtnUp))
		{
			direction = true;
		}
		else
		{
			direction = false;
		}
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		Object src = e.getSource();
		if (src.equals(btnFindNext))
		{
			findNext();
		}
		else
			if (src.equals(btnReplace))
			{
				replace();
			}
			else
				if (src.equals(btnReplaceAll))
				{
					replaceAll();
				}
				else
					if (src.equals(btnCancel))
					{
						exit();
					}
	}

	protected void findNext()
	{
		String findWhat = fldFindWhat.getText();
		if (!findWhat.toString().isEmpty())
		{
			int index = find(findWhat);
			if (index != -1)
			{
				int selectionStart = index;
				if (!direction)
					selectionStart += textArea.getCaretPosition();
				int selectionEnd =  selectionStart + findWhat.length();
				highlight(selectionStart, selectionEnd);
			}
			else
			{
				notFoundMessage(findWhat.toString());
			}
		}
	}

	private void replace()
	{
		if (!fldFindWhat.getText().isEmpty()
				&& !fldReplaceWith.getText().isEmpty())
		{
			if (!fldFindWhat.getText().isEmpty()
					&& !fldReplaceWith.getText().isEmpty())
			{
				
			}
		}
	}

	private void replaceAll()
	{
		
	}
	
	private int find(String text)
	{
		int index = -1;
		if (chckbxMatchCase.isSelected())
		{
			if (direction)
			{
				index = getTextUp().lastIndexOf(text);
			}
			else
			{
				index = getTextDown().indexOf(text);
			}
		}
		else
		{
			if (direction)
			{
				index = getTextUp().toLowerCase().lastIndexOf(text.toLowerCase());
			}
			else
			{
				index = getTextDown().toLowerCase().indexOf(text.toLowerCase());
			}
		}
		return index;
	}
	
	private String getTextUp()
	{
		String text = new String();
		int position = textArea.getCaretPosition();
		if (position != 0)
		{
			try
			{
				int start = 0;
				int length = position;
				if (textArea.getSelectedText() != null)
				{
					length = textArea.getSelectionStart();
				}
				text = textArea.getText(start, length);
			}
			catch (BadLocationException e)
			{
				e.printStackTrace();
			}
		}
		System.out.println(text);
		return text;
	}

	private String getTextDown()
	{
		String text = new String();
		int position = textArea.getCaretPosition();
		if (position != textArea.getDocument().getLength())
		{
			try
			{
				int start = position;
				int length = textArea.getDocument().getLength() - position;
				text = textArea.getText(start , length);
			}
			catch (BadLocationException e)
			{
				e.printStackTrace();
			}
		}
		System.out.println(text);
		return text;
	}
	
	private void highlight(int selectionStart, int selectionEnd)
	{
		textArea.setSelectionStart(selectionStart);
		textArea.setSelectionEnd(selectionEnd);
	}
	
	private void notFoundMessage(String find)
	{
		JOptionPane.showMessageDialog(this, "Cannot find \"" + find + "\"",
				"FreeEditor", JOptionPane.INFORMATION_MESSAGE);
	}
	
	private void exit()
	{
		setVisible(false);
		dispose();
	}
	
	public String getFindWhat()
	{
		return fldFindWhat.getText();
	}
}
