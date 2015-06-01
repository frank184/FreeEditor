package editor;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import java.awt.Font;

import javax.swing.SwingConstants;

import java.awt.SystemColor;

@SuppressWarnings("serial")
public class FreeEditorAbout extends JDialog
{
	/**
	 * Create the dialog.
	 */
	public FreeEditorAbout()
	{
		init();
	}

	public FreeEditorAbout(Component c)
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
		setTitle("FreeEditor - About");
		setBounds(100, 100, 301, 250);
		setLocationRelativeTo(null);
		setResizable(false);
		getContentPane().setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBounds(0, 0, 295, 50);
		getContentPane().add(panel);
		panel.setLayout(new BorderLayout(0, 0));
		
		JLabel lblAboutFreeeditor = new JLabel("About FreeEditor");
		lblAboutFreeeditor.setForeground(SystemColor.controlDkShadow);
		lblAboutFreeeditor.setFont(new Font("Open Sans", Font.PLAIN, 21));
		lblAboutFreeeditor.setBackground(SystemColor.activeCaptionBorder);
		lblAboutFreeeditor.setHorizontalAlignment(SwingConstants.CENTER);
		panel.add(lblAboutFreeeditor, BorderLayout.CENTER);
		
		JTextArea body = new JTextArea();
		body.setForeground(SystemColor.windowBorder);
		body.setFont(new Font("Open Sans", Font.PLAIN, 12));
		body.setText("\nFreeEditor is a small text editor project. This project started May 29th 2015 and version 1.0 was completed /insert here/.\r\n\r\nA special thank you to\r\nMs. Ann Hamilton, the StackOverFlow community, java2s.com and MS Notepad.");
		body.setEditable(false);
		body.setBounds(10, 50, 275, 161);
		body.setLineWrap(true);
		body.setWrapStyleWord(true);
		getContentPane().add(body);
	}
}
