package editor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JCheckBox;

@SuppressWarnings("serial")
public class FreeEditorFind extends JDialog implements ActionListener
{
	private int lineNumber = 1;

	private JTextField fldFindWhat;
	private JTextField fldReplaceWith;

	private JButton btnFindNext;
	private JButton btnReplace;
	private JButton btnReplaceAll;
	private JCheckBox chckbxMatchCase;
	private JButton btnCancel;

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

	public FreeEditorFind(FreeEditorFrame frame)
	{
		init();
		setLocationRelativeTo(frame);
	}

	public void init()
	{
		setTitle("Find");
		setModal(true);
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

		chckbxMatchCase = new JCheckBox("Match case");
		chckbxMatchCase.setBounds(10, 85, 97, 23);
		getContentPane().add(chckbxMatchCase);

		btnCancel = new JButton("Cancel");
		btnCancel.setBounds(221, 89, 103, 23);
		btnCancel.addActionListener(this);
		getContentPane().add(btnCancel);
	}

	public int showDialog()
	{
		setVisible(true);
		return lineNumber;
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		Object src = e.getSource();
		if (src.equals(btnFindNext))
		{
			exit();
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
	
	private void exit()
	{
		setVisible(false);
		dispose();
	}
}
