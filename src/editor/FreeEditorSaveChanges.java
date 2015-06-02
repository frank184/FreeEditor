package editor;


import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JLabel;

import java.awt.Font;
import java.awt.SystemColor;

import javax.swing.JButton;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.SwingConstants;

@SuppressWarnings("serial")
public class FreeEditorSaveChanges extends JDialog implements ActionListener
{
	protected static final int CANCEL = 0;
	protected static final int DONT_SAVE = 1;
	protected static final int SAVE = 2;
	private int val;
	
	private JLabel lblText;
	private JButton btnCancel;
	private JButton btnDontSave;
	private JButton btnSave;

	/**
	 * Create the dialog.
	 */
	public FreeEditorSaveChanges()
	{
		init();
	}
	
	public FreeEditorSaveChanges(FreeEditorFrame c)
	{
		init();
		setLocationRelativeTo(c);
		lblText.setText("Do you want to save changes to " + c.getCurrentFile());
	}
		
	private void init()
	{
		setTitle("FreeEditor");
		setModal(true);
		setBounds(100, 100, 350, 150);
		setLocationRelativeTo(null);
		getContentPane().setLayout(null);
		addWindowListener(new WindowAdapter()
		{
			@Override
			public void windowClosing(WindowEvent e)
			{
				exit();
			}
		});
		
		JPanel textPanel = new JPanel();
		textPanel.setBackground(SystemColor.text);
		textPanel.setBounds(0, 0, 384, 67);
		getContentPane().add(textPanel);
		textPanel.setLayout(null);
		
		lblText = new JLabel();
		lblText.setVerticalAlignment(SwingConstants.TOP);
		lblText.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblText.setForeground(new Color(0, 0, 205));
		lblText.setBounds(10, 11, 312, 45);
		textPanel.add(lblText);
		
		btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(this);
		btnCancel.setBounds(241, 78, 83, 23);
		getContentPane().add(btnCancel);
		
		btnDontSave = new JButton("Don't Save");
		btnDontSave.addActionListener(this);
		btnDontSave.setBounds(134, 78, 97, 23);
		getContentPane().add(btnDontSave);
		
		btnSave = new JButton("Save");
		btnSave.addActionListener(this);
		btnSave.setBounds(53, 78, 71, 23);
		getContentPane().add(btnSave);
	}

	public int showDialog()
	{
		setVisible(true);
		return val;
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		Object src = e.getSource();
		if (src == btnCancel)
		{
			val = CANCEL;
		}
		else
			if (src == btnDontSave)
			{
				val = DONT_SAVE;
			}
			else
				if (src == btnSave)
				{
					val = SAVE;
				}
		exit();
	}
	
	private void exit()
	{
		setVisible(false);
		dispose();
	}
}
