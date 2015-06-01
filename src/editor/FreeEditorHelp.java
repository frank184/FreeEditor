package editor;


import javax.swing.JDialog;
import javax.swing.JScrollPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import java.awt.BorderLayout;
import java.awt.Component;
import java.io.File;
import java.io.IOException;

import javax.swing.JEditorPane;

@SuppressWarnings("serial")
public class FreeEditorHelp extends JDialog
{
	/**
	 * Create the dialog.
	 */
	public FreeEditorHelp()
	{
		init();
	}
	
	public FreeEditorHelp(Component c)
	{
		init();
		setLocationRelativeTo(c);
	}
	
	private void init()
	{
		try
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e)
		{
			e.printStackTrace();
		}
		setTitle("FreeEditor - Help");
		setBounds(100, 100, 575, 547);
		setLocationRelativeTo(null);
		setLayout(new BorderLayout());
		getContentPane().setLayout(null);
		
		
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
