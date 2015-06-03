package editor;

import javax.swing.JDialog;
import javax.swing.JScrollPane;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JLabel;

@SuppressWarnings("serial")
public class FreeEditorHelp extends JDialog
{
	private JEditorPane editorPane;

	private File[] files = { new File("index.html"), new File("navigation.html"),
			new File("basictasks.html"), new File("FAQ.html") };

	private final int INDEX = 0;
	private final int NAVIGATION = 1;
	private final int BASIC_TASKS = 2;
	private final int FAQ = 3;

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
		setTitle("FreeEditor - Help");
		setModal(true);
		setBounds(100, 100, 488, 473);
		setResizable(false);
		setLocationRelativeTo(null);
		getContentPane().setLayout(null);

		JPanel panel = new JPanel();
		panel.setBounds(0, 0, 75, 445);
		getContentPane().add(panel);
		panel.setLayout(null);

		JLabel lblIndroduction = new JLabel("Indroduction");
		lblIndroduction.setBounds(10, 11, 65, 14);
		lblIndroduction.addMouseListener(introduction);
		panel.add(lblIndroduction);

		JLabel lblNavigation = new JLabel("Navigation");
		lblNavigation.setBounds(10, 36, 65, 14);
		lblNavigation.addMouseListener(navigation);
		panel.add(lblNavigation);

		JLabel lblBasicTasks = new JLabel("Basic Tasks");
		lblBasicTasks.setBounds(10, 61, 65, 14);
		lblBasicTasks.addMouseListener(basicTasks);
		panel.add(lblBasicTasks);

		JLabel lblFaq = new JLabel("FAQ");
		lblFaq.setBounds(10, 86, 65, 14);
		lblFaq.addMouseListener(faq);
		panel.add(lblFaq);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(85, 0, 397, 445);
		scrollPane
				.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		getContentPane().add(scrollPane, BorderLayout.CENTER);

		editorPane = new JEditorPane();
		editorPane.setEditable(false);
		scrollPane.setViewportView(editorPane);
		try
		{
			editorPane.setPage(new File("index.html").toURI().toURL());
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	private MouseAdapter introduction = new MouseAdapter()
	{
		@Override
		public void mouseClicked(MouseEvent e)
		{
			setPage(INDEX);
		}
	};

	private MouseAdapter navigation = new MouseAdapter()
	{
		@Override
		public void mouseClicked(MouseEvent e)
		{
			setPage(NAVIGATION);
		}
	};

	private MouseAdapter basicTasks = new MouseAdapter()
	{
		@Override
		public void mouseClicked(MouseEvent e)
		{
			setPage(BASIC_TASKS);
		}
	};

	private MouseAdapter faq = new MouseAdapter()
	{
		@Override
		public void mouseClicked(MouseEvent e)
		{
			setPage(FAQ);
		}
	};

	private void setPage(int i)
	{
		try
		{
			editorPane.setPage(files[i].toURI().toURL());
		}
		catch (MalformedURLException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
