/********************************************************************************
*   Application: Work Request Application
*   	 Author: Team 3
*       @author  Ian Oliver, David Leake, William Tchouente
*   	 Course: CMSC 495-7381 Current Trends and Projects in Computer Science
*   
**********************************************************************************/

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JTextPane;
import java.awt.Font;
import java.awt.Color;
import java.awt.Component;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.awt.event.ActionEvent;
import javax.swing.JRadioButton;

public class Login extends JFrame implements ActionListener
{
	//Variable block
	public static Connection connection;	//sql connection
	private static int connectionType;		//sql connection type, local, azure, other
	
	//Gui variable block
	public JFrame loginFrame = this;
	private JPanel contentPane;
	private static JTextField userNameTextField;
	private static JTextField passwordTextField;
	private JLabel sqlServerAddressLabel;
	private JLabel sqlDatabaseNameLabel;
	private static JTextField sqlServerAddressTextField;
	private static JTextField sqlDatabaseNameTextField;
	private JRadioButton sqlLocalRadioBtn;
	private JRadioButton sqlAzureRadioBtn;
	private JRadioButton sqlServerRadioBtn;
	private ButtonGroup signinOptions;
	private JButton submitBtn;
	
	//declaring vars for server connection
	private static String url;
	private static String user;
	private static String password;
	private static String databaseName;
	
	//Launch Application
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Login frame = new Login();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	//Create the gui
	public Login() 
	{
		createGui();		
	}
	
	public void actionPerformed(ActionEvent e) 
	{
		//Action when a radio button in the button group is selected
		if((e.getSource() == sqlLocalRadioBtn) 
		|| (e.getSource() == sqlAzureRadioBtn) 
		|| (e.getSource() == sqlServerRadioBtn))
		{
			if(sqlServerRadioBtn.isSelected())	//special fields become available
			{
				sqlServerAddressLabel.setEnabled(true);
				sqlDatabaseNameLabel.setEnabled(true);
				sqlServerAddressTextField.setEnabled(true);
				sqlDatabaseNameTextField.setEnabled(true);
				connectionType = 2;
			}
			else //disable server address and database fields
			{
				sqlServerAddressLabel.setEnabled(false);
				sqlDatabaseNameLabel.setEnabled(false);
				sqlServerAddressTextField.setEnabled(false);
				sqlDatabaseNameTextField.setEnabled(false);
				
				if(sqlAzureRadioBtn.isSelected())
					connectionType = 1;
				else if(sqlLocalRadioBtn.isSelected())
					connectionType = 0;
			}
		}
		else if (e.getSource() == submitBtn) //Action when submit button is hit
		{
			if(sqlConnectionOpen(connectionType))//If able to connect to sql database
			{
				String loginInfo = url + databaseName + ';' + user + ';' + password;
				MainWindow window = new MainWindow(loginInfo, connectionType, connection);
				window.frmWorkRequestApplication.setVisible(true);
				setVisible(false);
			}
			else
			{
				JOptionPane.showMessageDialog(contentPane, "Invalid username or password");
			}
		}
	}
	
	//attempt to connect to sql database
	public static boolean sqlConnectionOpen(int connectedType)
	{		
		boolean connected = false;
		
		//if garbage data then set to local connection type
		if(connectedType > 2 || connectedType < 0)
			connectedType = 0;
		
		switch(connectedType)
		{
		case 0:	//Local
			url ="jdbc:sqlserver://localhost;";
			user = userNameTextField.getText();
			password = passwordTextField.getText();
			databaseName = "databaseName=Pulse;";
			
			try {
				System.out.println("Attempting to connect to: " + url + databaseName);
				connection = DriverManager.getConnection(url + databaseName, user, password);
				System.out.println("Connect to MS SQL Server on Local Host. Good Job Dude.");
				connected = true;
			} catch (SQLException e) {
				System.out.println("Oops, there's an error connecting to the LocalHost");
				e.printStackTrace();
			}
			break;
			
		case 1:	//Azure Server
			url ="jdbc:sqlserver://cmsc495team03.eastus.cloudapp.azure.com;";
			user = userNameTextField.getText();
			password = passwordTextField.getText();
			databaseName = "databaseName=Pulse;";

			try {
				System.out.println("Attempting to connect to: " + url + databaseName);
				connection = DriverManager.getConnection(url + databaseName, user, password);
				System.out.println("Connect to MS SQL Server on Azure. Good Job Dude.");
				connected = true;
			} catch (SQLException e) {
				System.out.println("Oops, there's an error connecting to Azure");
				e.printStackTrace();
			}
			break;
			
		case 2:	//User defined server
			url = sqlServerAddressTextField.getText();
			user = userNameTextField.getText();
			password = passwordTextField.getText();
			databaseName = sqlDatabaseNameTextField.getText();
			
			try {
				System.out.println("Attempting to connect to: " + url + databaseName);
				connection = DriverManager.getConnection(url + databaseName, user, password);
				System.out.println("Connect to MS SQL Server on Azure. Good Job Dude.");
				connected = true;
			} catch (SQLException e) {
				System.out.println("Oops, there's an error connecting to Azure");
				e.printStackTrace();
			}
			break;
		}
		
		return connected;
	}
	
	//Create the login page gui
	public void createGui()
	{
		loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		loginFrame.setBounds(100, 100, 522, 315);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(240, 240, 240));
		loginFrame.setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]{-8, 0, 50, 59, 59, 67, 58, 39};
		gbl_contentPane.rowHeights = new int[]{66, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gbl_contentPane.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0};
		gbl_contentPane.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		contentPane.setLayout(gbl_contentPane);
		
		JTextPane titleTextPane = new JTextPane();
		titleTextPane.setBackground(new Color(240, 240, 240));
		titleTextPane.setFont(new Font("Arial", Font.BOLD, 24));
		titleTextPane.setEditable(false);
		titleTextPane.setText("Work Request Login");
		GridBagConstraints gbc_titleTextPane = new GridBagConstraints();
		gbc_titleTextPane.fill = GridBagConstraints.HORIZONTAL;
		gbc_titleTextPane.gridwidth = 5;
		gbc_titleTextPane.insets = new Insets(0, 0, 5, 5);
		gbc_titleTextPane.gridx = 2;
		gbc_titleTextPane.gridy = 0;
		contentPane.add(titleTextPane, gbc_titleTextPane);
		
		Component horizontalStrut = Box.createHorizontalStrut(20);
		GridBagConstraints gbc_horizontalStrut = new GridBagConstraints();
		gbc_horizontalStrut.insets = new Insets(0, 0, 5, 5);
		gbc_horizontalStrut.gridx = 0;
		gbc_horizontalStrut.gridy = 1;
		contentPane.add(horizontalStrut, gbc_horizontalStrut);
		
		JLabel userNameLabel = new JLabel("User Name");
		GridBagConstraints gbc_userNameLabel = new GridBagConstraints();
		gbc_userNameLabel.anchor = GridBagConstraints.EAST;
		gbc_userNameLabel.insets = new Insets(0, 0, 5, 5);
		gbc_userNameLabel.gridx = 1;
		gbc_userNameLabel.gridy = 1;
		contentPane.add(userNameLabel, gbc_userNameLabel);
		
		userNameTextField = new JTextField();
		userNameTextField.setText("sa");
		GridBagConstraints gbc_userNameTextField = new GridBagConstraints();
		gbc_userNameTextField.gridwidth = 5;
		gbc_userNameTextField.insets = new Insets(0, 0, 5, 5);
		gbc_userNameTextField.fill = GridBagConstraints.HORIZONTAL;
		gbc_userNameTextField.gridx = 2;
		gbc_userNameTextField.gridy = 1;
		contentPane.add(userNameTextField, gbc_userNameTextField);
		userNameTextField.setColumns(10);
		
		Component horizontalStrut_1 = Box.createHorizontalStrut(20);
		GridBagConstraints gbc_horizontalStrut_1 = new GridBagConstraints();
		gbc_horizontalStrut_1.insets = new Insets(0, 0, 5, 0);
		gbc_horizontalStrut_1.gridx = 7;
		gbc_horizontalStrut_1.gridy = 1;
		contentPane.add(horizontalStrut_1, gbc_horizontalStrut_1);
		
		JLabel passwordLabel = new JLabel("Password");
		GridBagConstraints gbc_passwordLabel = new GridBagConstraints();
		gbc_passwordLabel.anchor = GridBagConstraints.EAST;
		gbc_passwordLabel.insets = new Insets(0, 0, 5, 5);
		gbc_passwordLabel.gridx = 1;
		gbc_passwordLabel.gridy = 2;
		contentPane.add(passwordLabel, gbc_passwordLabel);
		
		passwordTextField = new JTextField();
		passwordTextField.setText("1234567890");
		GridBagConstraints gbc_passwordTextField = new GridBagConstraints();
		gbc_passwordTextField.gridwidth = 5;
		gbc_passwordTextField.insets = new Insets(0, 0, 5, 5);
		gbc_passwordTextField.fill = GridBagConstraints.HORIZONTAL;
		gbc_passwordTextField.gridx = 2;
		gbc_passwordTextField.gridy = 2;
		contentPane.add(passwordTextField, gbc_passwordTextField);
		passwordTextField.setColumns(10);
		
		//Make a button group
		signinOptions = new ButtonGroup();
		
		sqlAzureRadioBtn = new JRadioButton("Azure Connection");
		sqlAzureRadioBtn.addActionListener(this);
		GridBagConstraints gbc_sqlAzureRadioBtn = new GridBagConstraints();
		gbc_sqlAzureRadioBtn.gridwidth = 2;
		gbc_sqlAzureRadioBtn.insets = new Insets(0, 0, 5, 5);
		gbc_sqlAzureRadioBtn.gridx = 3;
		gbc_sqlAzureRadioBtn.gridy = 3;
		contentPane.add(sqlAzureRadioBtn, gbc_sqlAzureRadioBtn);
		signinOptions.add(sqlAzureRadioBtn);
		
		sqlLocalRadioBtn = new JRadioButton("Local Sql Connection");
		sqlLocalRadioBtn.addActionListener(this);
		sqlLocalRadioBtn.setSelected(true);
		GridBagConstraints gbc_sqlLocalRadioBtn = new GridBagConstraints();
		gbc_sqlLocalRadioBtn.gridwidth = 2;
		gbc_sqlLocalRadioBtn.insets = new Insets(0, 0, 5, 5);
		gbc_sqlLocalRadioBtn.gridx = 5;
		gbc_sqlLocalRadioBtn.gridy = 3;
		contentPane.add(sqlLocalRadioBtn, gbc_sqlLocalRadioBtn);
		signinOptions.add(sqlLocalRadioBtn);
		
		sqlServerRadioBtn = new JRadioButton("SQL Server Connection");
		sqlServerRadioBtn.addActionListener(this);
		GridBagConstraints gbc_sqlServerRadioBtn = new GridBagConstraints();
		gbc_sqlServerRadioBtn.gridwidth = 2;
		gbc_sqlServerRadioBtn.insets = new Insets(0, 0, 5, 5);
		gbc_sqlServerRadioBtn.gridx = 1;
		gbc_sqlServerRadioBtn.gridy = 3;
		contentPane.add(sqlServerRadioBtn, gbc_sqlServerRadioBtn);
		signinOptions.add(sqlServerRadioBtn);
		
		sqlServerAddressLabel = new JLabel("Server Name");
		sqlServerAddressLabel.setEnabled(false);
		GridBagConstraints gbc_sqlServerAddressLabel = new GridBagConstraints();
		gbc_sqlServerAddressLabel.anchor = GridBagConstraints.EAST;
		gbc_sqlServerAddressLabel.insets = new Insets(0, 0, 5, 5);
		gbc_sqlServerAddressLabel.gridx = 1;
		gbc_sqlServerAddressLabel.gridy = 4;
		contentPane.add(sqlServerAddressLabel, gbc_sqlServerAddressLabel);
		
		sqlServerAddressTextField = new JTextField();
		sqlServerAddressTextField.setEnabled(false);
		GridBagConstraints gbc_sqlServerAddressTextField = new GridBagConstraints();
		gbc_sqlServerAddressTextField.gridwidth = 5;
		gbc_sqlServerAddressTextField.insets = new Insets(0, 0, 5, 5);
		gbc_sqlServerAddressTextField.fill = GridBagConstraints.HORIZONTAL;
		gbc_sqlServerAddressTextField.gridx = 2;
		gbc_sqlServerAddressTextField.gridy = 4;
		contentPane.add(sqlServerAddressTextField, gbc_sqlServerAddressTextField);
		sqlServerAddressTextField.setColumns(10);
		
		sqlDatabaseNameLabel = new JLabel("Database Name");
		sqlDatabaseNameLabel.setEnabled(false);
		GridBagConstraints gbc_sqlDatabaseNameLabel = new GridBagConstraints();
		gbc_sqlDatabaseNameLabel.anchor = GridBagConstraints.EAST;
		gbc_sqlDatabaseNameLabel.insets = new Insets(0, 0, 5, 5);
		gbc_sqlDatabaseNameLabel.gridx = 1;
		gbc_sqlDatabaseNameLabel.gridy = 5;
		contentPane.add(sqlDatabaseNameLabel, gbc_sqlDatabaseNameLabel);
		
		sqlDatabaseNameTextField = new JTextField();
		sqlDatabaseNameTextField.setEnabled(false);
		sqlDatabaseNameTextField.setColumns(10);
		GridBagConstraints gbc_sqlDatabaseNameTextField = new GridBagConstraints();
		gbc_sqlDatabaseNameTextField.gridwidth = 5;
		gbc_sqlDatabaseNameTextField.insets = new Insets(0, 0, 5, 5);
		gbc_sqlDatabaseNameTextField.fill = GridBagConstraints.HORIZONTAL;
		gbc_sqlDatabaseNameTextField.gridx = 2;
		gbc_sqlDatabaseNameTextField.gridy = 5;
		contentPane.add(sqlDatabaseNameTextField, gbc_sqlDatabaseNameTextField);
				
		Component verticalStrut = Box.createVerticalStrut(20);
		GridBagConstraints gbc_verticalStrut = new GridBagConstraints();
		gbc_verticalStrut.insets = new Insets(0, 0, 0, 5);
		gbc_verticalStrut.gridx = 6;
		gbc_verticalStrut.gridy = 8;
		contentPane.add(verticalStrut, gbc_verticalStrut);
		
		submitBtn = new JButton("Submit");	
		submitBtn.addActionListener(this); 
		Component verticalStrut_1 = Box.createVerticalStrut(20);
		GridBagConstraints gbc_verticalStrut_1 = new GridBagConstraints();
		gbc_verticalStrut_1.insets = new Insets(0, 0, 5, 0);
		gbc_verticalStrut_1.gridx = 7;
		gbc_verticalStrut_1.gridy = 5;
		contentPane.add(verticalStrut_1, gbc_verticalStrut_1);
		GridBagConstraints gbc_submitBtn = new GridBagConstraints();
		gbc_submitBtn.fill = GridBagConstraints.HORIZONTAL;
		gbc_submitBtn.gridwidth = 2;
		gbc_submitBtn.insets = new Insets(0, 0, 5, 5);
		gbc_submitBtn.gridx = 5;
		gbc_submitBtn.gridy = 7;
		contentPane.add(submitBtn, gbc_submitBtn);
	}
}
