package editor;


import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JLabel;

import java.awt.Font;
import java.awt.SystemColor;

@SuppressWarnings("serial")
public class FreeEditorSaveChanges extends JDialog
{

	/**
	 * Launch the application.
	 */
	public static void main(String[] args)
	{
		try
		{
			FreeEditorSaveChanges dialog = new FreeEditorSaveChanges();
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
	public FreeEditorSaveChanges()
	{
		setTitle("FreeEditor");
		setBounds(100, 100, 400, 200);
		setLocationRelativeTo(null);
		getContentPane().setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBounds(10, 102, 364, 49);
		getContentPane().add(panel);
		
		JLabel lblSaveChanges = new JLabel("Save Changes");
		lblSaveChanges.setForeground(SystemColor.textHighlight);
		lblSaveChanges.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblSaveChanges.setBounds(10, 11, 364, 34);
		getContentPane().add(lblSaveChanges);
	}

}
