import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.SwingConstants;


public class MainWindow {

	private JFrame frmWorkRequestApplication;

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
		frmWorkRequestApplication.getContentPane().setLayout(new BorderLayout(0, 0));
		exitMenuItem.addActionListener(exitAction);
		
		JLabel lblNewLabel = new JLabel("Hello World");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		frmWorkRequestApplication.getContentPane().add(lblNewLabel, BorderLayout.CENTER);
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
