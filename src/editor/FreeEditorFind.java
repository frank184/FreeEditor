package editor;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JCheckBox;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

@SuppressWarnings("serial")
public class FreeEditorFind extends JDialog
{
	private JTextField textField;
	private JTextField textField_1;

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
		setTitle("Find");
		setModal(true);
		setBounds(100, 100, 350, 157);
		getContentPane().setLayout(null);

		JLabel lblFindWhat = new JLabel("Find what:");
		lblFindWhat.setBounds(10, 14, 80, 14);
		getContentPane().add(lblFindWhat);

		JLabel lblReplaceWith = new JLabel("Replace with:");
		lblReplaceWith.setBounds(10, 45, 80, 14);
		getContentPane().add(lblReplaceWith);

		textField = new JTextField();
		textField.setBounds(92, 11, 119, 20);
		getContentPane().add(textField);
		textField.setColumns(10);

		textField_1 = new JTextField();
		textField_1.setBounds(92, 42, 119, 20);
		getContentPane().add(textField_1);
		textField_1.setColumns(10);

		JButton btnNewButton = new JButton("Find Next");
		btnNewButton.setBounds(221, 5, 103, 23);
		getContentPane().add(btnNewButton);

		JButton btnNewButton_1 = new JButton("Replace");
		btnNewButton_1.setBounds(221, 33, 103, 23);
		getContentPane().add(btnNewButton_1);

		JButton btnNewButton_2 = new JButton("Replace All");
		btnNewButton_2.setBounds(221, 61, 103, 23);
		getContentPane().add(btnNewButton_2);

		JButton btnNewButton_3 = new JButton("Cancel");
		btnNewButton_3.setBounds(221, 89, 103, 23);
		getContentPane().add(btnNewButton_3);

		JCheckBox chckbxMatchCase = new JCheckBox("Match case");
		chckbxMatchCase.setBounds(10, 85, 97, 23);
		getContentPane().add(chckbxMatchCase);
	}
}
