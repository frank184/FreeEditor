package editor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JCheckBox;
import javax.swing.JRadioButton;

@SuppressWarnings("serial")
public class FreeEditorFind extends JDialog implements ActionListener
{
	private FreeEditorFrame editor;
	private int lineNumber = 1;
	private int colNumber = 1;

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
	 * Launch the application.
	 */
	public static void main(String[] args)
	{
		try
		{
			FreeEditorFind dialog = new FreeEditorFind();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public FreeEditorFind()
	{
		init();
	}

	public FreeEditorFind(FreeEditorFrame f)
	{
		init();
		editor = f;
		setLocationRelativeTo(editor);
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
		getContentPane().add(rdbtnUp);
		btnGroup.add(rdbtnUp);
		
		rdbtnDown = new JRadioButton("Down");
		rdbtnDown.setBounds(156, 89, 65, 23);
		rdbtnDown.setSelected(true);
		getContentPane().add(rdbtnDown);
		btnGroup.add(rdbtnDown);
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
	public void actionPerformed(ActionEvent e)
	{
		Object src = e.getSource();
		if (src.equals(btnFindNext))
		{
			String text = fldFindWhat.getText();
			if (!text.isEmpty())
			{
				findNext(text);
			}
		}
		else
			if (src.equals(btnReplace))
			{
				exit();
			}
			else
				if (src.equals(btnReplaceAll))
				{
					exit();
				}
				else
					if (src.equals(btnCancel))
					{
						exit();
					}
	}
	
	private int findNext(String text)
	{
		return 0;
	}
	
	private int replace()
	{
		return 0;
	}
	
	private int replaceAll()
	{
		return 0;
	}
	
	private void exit()
	{
		setVisible(false);
		dispose();
	}
}
