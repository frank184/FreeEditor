package editor;


import javax.swing.JDialog;
import javax.swing.JScrollPane;

import java.awt.BorderLayout;
import java.io.File;
import java.io.IOException;

import javax.swing.JEditorPane;

@SuppressWarnings("serial")
public class FreeEditorHelp extends JDialog
{
	/**
	 * Launch the application.
	 */
	public static void main(String[] args)
	{
		try
		{
			FreeEditorHelp dialog = new FreeEditorHelp();
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
	public FreeEditorHelp()
	{
		setTitle("FreeEditor - Help");
		setBounds(100, 100, 575, 547);
		setLocationRelativeTo(null);
		getContentPane().setLayout(null);
		setLayout(new BorderLayout());
		
		
		JEditorPane helpContent = new JEditorPane();
		helpContent.setEditable(false);
		helpContent.setBounds(10, 11, 539, 487);
		try
		{
			helpContent.setPage(new File("index.html").toURI().toURL());
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		JScrollPane jsp = new JScrollPane();
		jsp.add(helpContent);
		jsp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		jsp.setViewportView(helpContent);
		getContentPane().add(jsp, BorderLayout.CENTER);
	}
}
