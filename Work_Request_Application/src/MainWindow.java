import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import javax.swing.JButton;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JTable;
import java.awt.Component;
import javax.swing.Box;
import java.awt.Color;
import javax.swing.JScrollPane;


public class MainWindow {

	JFrame frmWorkRequestApplication;
	JPanel contentPanel;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainWindow window = new MainWindow();
					window.frmWorkRequestApplication.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MainWindow() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	
	private void initialize() {
		
		frmWorkRequestApplication = new JFrame();
		frmWorkRequestApplication.setTitle("Work Request Application");
		frmWorkRequestApplication.setBounds(100, 100, 450, 300);
		frmWorkRequestApplication.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		CLASSPATH =.;C:\Program Files\Microsoft JDBC Driver 9.4 for SQL Server\sqljdbc_9.4\enu\mssql-jdbc-9.4.0.jre16.jar
		JMenuBar menuBar = new JMenuBar();
		frmWorkRequestApplication.setJMenuBar(menuBar);
		
		JMenu FileMenu = new JMenu("File");
		menuBar.add(FileMenu);
		
		JMenuItem newMenuItem = new JMenuItem("New");
		FileMenu.add(newMenuItem);
		
		JMenuItem exitMenuItem = new JMenuItem("Exit");
		FileMenu.add(exitMenuItem);
		exitMenuItem.addActionListener(exitAction);
		frmWorkRequestApplication.getContentPane().setLayout(new BorderLayout(0, 0));
		
		contentPanel = new JPanel();
		frmWorkRequestApplication.getContentPane().add(contentPanel, BorderLayout.CENTER);
		GridBagLayout gbl_contentPanel = new GridBagLayout();
		gbl_contentPanel.columnWidths = new int[]{0, 0, 0, 0, 0};
		gbl_contentPanel.rowHeights = new int[]{0, 0, 0, 0, 0, 0};
		gbl_contentPanel.columnWeights = new double[]{0.0, 0.0, 1.0, 0.0, Double.MIN_VALUE};
		gbl_contentPanel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		contentPanel.setLayout(gbl_contentPanel);
		
		Component horizontalStrut_1 = Box.createHorizontalStrut(20);
		GridBagConstraints gbc_horizontalStrut_1 = new GridBagConstraints();
		gbc_horizontalStrut_1.insets = new Insets(0, 0, 5, 5);
		gbc_horizontalStrut_1.gridx = 0;
		gbc_horizontalStrut_1.gridy = 1;
		contentPanel.add(horizontalStrut_1, gbc_horizontalStrut_1);
		
		JButton newEngBtn = new JButton("New Engineering Work Request");
		newEngBtn.setBackground(new Color(100, 149, 237));
		GridBagConstraints gbc_newEngBtn = new GridBagConstraints();
		gbc_newEngBtn.insets = new Insets(0, 0, 5, 5);
		gbc_newEngBtn.gridx = 1;
		gbc_newEngBtn.gridy = 1;
		contentPanel.add(newEngBtn, gbc_newEngBtn);
		
		JScrollPane scrollPane = new JScrollPane();
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.gridheight = 3;
		gbc_scrollPane.insets = new Insets(0, 0, 5, 5);
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 2;
		gbc_scrollPane.gridy = 1;
		contentPanel.add(scrollPane, gbc_scrollPane);
		
		Component horizontalStrut = Box.createHorizontalStrut(20);
		GridBagConstraints gbc_horizontalStrut = new GridBagConstraints();
		gbc_horizontalStrut.insets = new Insets(0, 0, 5, 0);
		gbc_horizontalStrut.gridx = 3;
		gbc_horizontalStrut.gridy = 1;
		contentPanel.add(horizontalStrut, gbc_horizontalStrut);
		
		JButton newConstructBtn = new JButton("New Cosntruction Work Request");
		newConstructBtn.setBackground(new Color(119, 136, 153));
		GridBagConstraints gbc_newConstructBtn = new GridBagConstraints();
		gbc_newConstructBtn.insets = new Insets(0, 0, 5, 5);
		gbc_newConstructBtn.gridx = 1;
		gbc_newConstructBtn.gridy = 2;
		contentPanel.add(newConstructBtn, gbc_newConstructBtn);
		
		JButton newEnvironmentalBtn = new JButton("New Environemtal Work Request");
		newEnvironmentalBtn.setBackground(new Color(210, 105, 30));
		GridBagConstraints gbc_newEnvironmentalBtn = new GridBagConstraints();
		gbc_newEnvironmentalBtn.insets = new Insets(0, 0, 5, 5);
		gbc_newEnvironmentalBtn.gridx = 1;
		gbc_newEnvironmentalBtn.gridy = 3;
		contentPanel.add(newEnvironmentalBtn, gbc_newEnvironmentalBtn);
		
		
	}
	
	ActionListener exitAction = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			System.exit(0);
			
		}
		
	};

}
//CLASSPATH =.;C:\Program Files\Microsoft JDBC Driver 9.4 for SQL Server\sqljdbc_9.4\enu\mssql-jdbc-9.4.0.jre16.jar
/*
 * public class SQLDatabaseConnection { // Connect to your database. // Replace
 * server name, username, and password with your credentials public static void
 * main(String[] args) { String connectionUrl =
 * "jdbc:sqlserver://yourserver.database.windows.net:1433;" +
 * "database=AdventureWorks;" + "user=yourusername@yourserver;" +
 * "password=yourpassword;" + "encrypt=true;" + "trustServerCertificate=false;"
 * + "loginTimeout=30;";
 * 
 * try (Connection connection = DriverManager.getConnection(connectionUrl);) {
 * // Code here. } // Handle any errors that may have occurred. catch
 * (SQLException e) { e.printStackTrace(); } } }
 */
