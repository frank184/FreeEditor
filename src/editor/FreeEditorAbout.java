package editor;

import java.awt.BorderLayout;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JLabel;

import java.awt.Font;

import javax.swing.SwingConstants;
import javax.swing.JEditorPane;

@SuppressWarnings("serial")
public class FreeEditorAbout extends JDialog
{

	/**
	 * Launch the application.
	 */
	public static void main(String[] args)
	{
		try
		{
			FreeEditorAbout dialog = new FreeEditorAbout();
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
	public FreeEditorAbout()
	{
		setTitle("FreeEditor - About");
		setBounds(100, 100, 538, 511);
		setLocationRelativeTo(null);
		getContentPane().setLayout(null);
		
		JPanel header = new JPanel();
		header.setBounds(10, 11, 502, 65);
		getContentPane().add(header);
		header.setLayout(new BorderLayout(0, 0));
		
		JLabel lblAbout = new JLabel("About FreeEditor");
		lblAbout.setHorizontalAlignment(SwingConstants.CENTER);
		lblAbout.setFont(new Font("Tahoma", Font.PLAIN, 18));
		header.add(lblAbout);
		
		JEditorPane body = new JEditorPane();
		body.setEditable(false);
		body.setBounds(10, 87, 502, 375);
		getContentPane().add(body);
	}
}
