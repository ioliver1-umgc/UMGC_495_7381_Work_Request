/********************************************************************************
*   Application: Work Request Application
*   	 Author: Team 3
*       @author  Ian Oliver, David Leake, William Tchouente
*   	 Course: CMSC 495-7381 Current Trends and Projects in Computer Science
*   
**********************************************************************************/
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import javax.swing.JFrame;
import javax.swing.JLabel;
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
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JCheckBox;
import javax.swing.JTextArea;
import javax.swing.JComboBox;
import javax.swing.JRadioButton;
import javax.swing.SwingConstants;
import javax.swing.BoxLayout;
import javax.swing.JSpinner;
import javax.swing.border.EtchedBorder;
import javax.swing.table.DefaultTableModel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

public class MainWindow 
{
	public JFrame frmWorkRequestApplication;
	public static Connection connection;
	public static int connectionType;
	public ArrayList<Map<String, Object>> workRequests;

	//Launch the application.
	public static void main(String[] args) 
	{
		EventQueue.invokeLater(new Runnable() 
		{
			public void run() 
			{
				try 
				{
					MainWindow window = new MainWindow();
					window.frmWorkRequestApplication.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	//No data from the login window, default to local settings
	public MainWindow() 
	{	
		this(0, sqlConnection());		
	}
	
	//Received login data
	public MainWindow(int connectionType, Connection connection) 
	{	
		workRequests = new ArrayList<Map<String, Object>>();
		createGui();
		try 
		{
			workRequests = uspWRWorkRequest_ISUD(connection);
			makeSelectionTable();
			displayWR(0);
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("didn't grab wrISUD");
		}
		
		//Close the connection
		try 
		{
			connection.close();
		} catch (SQLException e) {
			System.out.println("SQL Connection Failed to Close!");
			e.printStackTrace();
		}
	}
	
	
	//create a SQL connection to localhost
	public static Connection sqlConnection()
	{
		String Localurl ="jdbc:sqlserver://localhost;databaseName=Pulse;";
		String Localuser = "sa";
		String Localpassword = "1234567890";
		
		try 
		{
			System.out.println("Attempting to connect to: " + Localurl);
			connection = DriverManager.getConnection(Localurl, Localuser, Localpassword);
			System.out.println("Connect to MS SQL Server on Local Host. Good Job Dude.");
		} 
		catch (SQLException e) 
		{
			System.out.println("Oops, there's an error connecting to the LocalHost");
			e.printStackTrace();
		}
		
		return connection;
				
	}
	
	public static ArrayList<Map<String, Object>> uspWRWorkRequest_ISUD(Connection con) throws SQLException 
	{
		ArrayList<Map<String, Object>> paramArrayListMap = null;
		
		String query = "{call uspWRWorkRequest_ISUD}";
		ResultSet rs;

	    try(CallableStatement pstmt = con.prepareCall(query); ) 
	    {  
	        rs = pstmt.executeQuery();  
	        
	        paramArrayListMap = new ArrayList<Map<String, Object>>();
            while(rs.next())
            {
	            Map<String,Object> paramMap= new HashMap<>();
	    		paramMap.put("AverageRate", rs.getDouble("AverageRate"));
	    		paramMap.put("BranchAssignmentID", rs.getInt("BranchAssignmentID"));
	    		paramMap.put("BranchID", rs.getInt("BranchID"));
	    		paramMap.put("CompletionDate", rs.getTimestamp("CompletionDate"));
	    		paramMap.put("DatePrepared", rs.getTimestamp("DatePrepared"));
	    		paramMap.put("DraftDueDate", rs.getTimestamp("DraftDueDate"));
	    		paramMap.put("FinancialInfoID", rs.getInt("FinancialInfoID"));
	    		paramMap.put("FY", rs.getInt("FY"));
	    		paramMap.put("ID", rs.getInt("ID"));
	    		paramMap.put("ProjectInfoID", rs.getInt("ProjectInfoID"));
	    		paramMap.put("ProjectManager", rs.getString("ProjectManager"));
	    		paramMap.put("ProjectPulseID", rs.getInt("ProjectPulseID"));
	    		paramMap.put("Requestor", rs.getString("Requestor"));
	    		paramMap.put("StartDate", rs.getTimestamp("StartDate"));
	    		paramMap.put("SubmissionDate", rs.getTimestamp("SubmissionDate"));
	    		paramMap.put("Supervisor", rs.getString("Supervisor"));
	    		paramMap.put("WRNumber", rs.getString("WRNumber"));
	    		paramMap.put("WRStatusID", rs.getInt("WRStatusID"));
	    		//paramMap.put("TYPE_ACTION", rs.getObject(19));
	    		paramArrayListMap.add(paramMap);
	    		paramMap.forEach((key, value) -> System.out.println(key + ": " + value));
            }
	        

	    }
		catch (SQLException e) 
		{
			System.out.println("Problem accessing uspWRWork_Request_ISUD...");
			e.printStackTrace();
		}
	    
	    return paramArrayListMap;
	}
	
	private void makeSelectionTable()
	{
		
		for(Map<String, Object> data : workRequests)
		{
			Vector<Object> dataRow = new Vector<Object>();
			dataRow.add(checkData(data.get("WRNumber")).toString());
			dataRow.add(checkData(data.get("ProjectManager")).toString());
			dataRow.add(checkData(data.get("WRStatusID")).toString());
			System.out.println("dataRow:" + dataRow);
			tableModel.addRow(dataRow);
		}
	}
	
	private void generateTimelineGraph()
	{
		//analysisTimeLinePanel
	}
	
	private void generatePieChart()
	{
		//analysisPieChartPane
	}
	
	private void generateProgressBarChartBySection()
	{
		
	}
	
	//Quick check for null to "null" for display purposes
	private Object checkData(Object o)
	{	
		if(o == null)
			return "null";
		
		return o;
	}
	
	private void displayWR(int index)
	{
		viewWRNumTextField.setText(checkData(workRequests.get(index).get("WRNumber")).toString());
		viewDatePrepTextField.setText(checkData(workRequests.get(index).get("DatePrepared")).toString());
		viewOrgTextField.setText(checkData(workRequests.get(index).get("BranchID")).toString());
		viewRequesterTextField.setText(checkData(workRequests.get(index).get("Requestor")).toString());
		
		return;
	}

	//GUI Creation
	private void createGui() {
		
		frmWorkRequestApplication = new JFrame();
		frmWorkRequestApplication.setTitle("Work Request Application");
		frmWorkRequestApplication.setBounds(100, 100, 999, 730);
		frmWorkRequestApplication.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frmWorkRequestApplication.getContentPane().setLayout(new BorderLayout(0, 0));
        
        gernalTabbedPane = new JTabbedPane(JTabbedPane.TOP);
        frmWorkRequestApplication.getContentPane().add(gernalTabbedPane);
        
        createViewWorkRequestGUI();
        createAnalysisGUI();
        createNewWorkRequestGUI();
        
	}
	
	//Analysis GUI
	private void createAnalysisGUI()
	{
        //view Analytics
        requestAnalyticsPane = new JPanel();
        requestAnalyticsPane.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
        gernalTabbedPane.addTab("Analytics", null, requestAnalyticsPane, null);
        GridBagLayout gbl_requestAnalyticsPane = new GridBagLayout();
        gbl_requestAnalyticsPane.columnWidths = new int[]{0, 121, 288, 0, 90, 88, 88, 88, 89, 0, 0};
        gbl_requestAnalyticsPane.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        gbl_requestAnalyticsPane.columnWeights = new double[]{0.0, 0.0, 1.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
        gbl_requestAnalyticsPane.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, Double.MIN_VALUE};
        requestAnalyticsPane.setLayout(gbl_requestAnalyticsPane);
        
        verticalStrut_12 = Box.createVerticalStrut(20);
        GridBagConstraints gbc_verticalStrut_12 = new GridBagConstraints();
        gbc_verticalStrut_12.insets = new Insets(0, 0, 5, 5);
        gbc_verticalStrut_12.gridx = 1;
        gbc_verticalStrut_12.gridy = 0;
        requestAnalyticsPane.add(verticalStrut_12, gbc_verticalStrut_12);
        
        horizontalStrut_17 = Box.createHorizontalStrut(20);
        GridBagConstraints gbc_horizontalStrut_17 = new GridBagConstraints();
        gbc_horizontalStrut_17.insets = new Insets(0, 0, 5, 5);
        gbc_horizontalStrut_17.gridx = 0;
        gbc_horizontalStrut_17.gridy = 1;
        requestAnalyticsPane.add(horizontalStrut_17, gbc_horizontalStrut_17);
        
        analysisWRIDLabel = new JLabel("Work Request ID");
        analysisWRIDLabel.setHorizontalAlignment(SwingConstants.TRAILING);
        GridBagConstraints gbc_analysisWRIDLabel = new GridBagConstraints();
        gbc_analysisWRIDLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_analysisWRIDLabel.insets = new Insets(0, 0, 5, 5);
        gbc_analysisWRIDLabel.gridx = 1;
        gbc_analysisWRIDLabel.gridy = 1;
        requestAnalyticsPane.add(analysisWRIDLabel, gbc_analysisWRIDLabel);
        
        analysisWRIDTextField = new JTextField();
        GridBagConstraints gbc_analysisWRIDTextField = new GridBagConstraints();
        gbc_analysisWRIDTextField.insets = new Insets(0, 0, 5, 5);
        gbc_analysisWRIDTextField.fill = GridBagConstraints.HORIZONTAL;
        gbc_analysisWRIDTextField.gridx = 2;
        gbc_analysisWRIDTextField.gridy = 1;
        requestAnalyticsPane.add(analysisWRIDTextField, gbc_analysisWRIDTextField);
        analysisWRIDTextField.setColumns(10);
        
        analysisPieChartPane = new JPanel();
        analysisPieChartPane.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        GridBagConstraints gbc_analysisPieChartPane = new GridBagConstraints();
        gbc_analysisPieChartPane.gridheight = 10;
        gbc_analysisPieChartPane.gridwidth = 5;
        gbc_analysisPieChartPane.insets = new Insets(0, 0, 5, 5);
        gbc_analysisPieChartPane.fill = GridBagConstraints.BOTH;
        gbc_analysisPieChartPane.gridx = 4;
        gbc_analysisPieChartPane.gridy = 1;
        requestAnalyticsPane.add(analysisPieChartPane, gbc_analysisPieChartPane);
        
        analysisPMLabel = new JLabel("Project Manager");
        analysisPMLabel.setHorizontalAlignment(SwingConstants.TRAILING);
        GridBagConstraints gbc_analysisPMLabel = new GridBagConstraints();
        gbc_analysisPMLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_analysisPMLabel.insets = new Insets(0, 0, 5, 5);
        gbc_analysisPMLabel.gridx = 1;
        gbc_analysisPMLabel.gridy = 2;
        requestAnalyticsPane.add(analysisPMLabel, gbc_analysisPMLabel);
        
        analysisPMTextField = new JTextField();
        GridBagConstraints gbc_analysisPMTextField = new GridBagConstraints();
        gbc_analysisPMTextField.insets = new Insets(0, 0, 5, 5);
        gbc_analysisPMTextField.fill = GridBagConstraints.HORIZONTAL;
        gbc_analysisPMTextField.gridx = 2;
        gbc_analysisPMTextField.gridy = 2;
        requestAnalyticsPane.add(analysisPMTextField, gbc_analysisPMTextField);
        analysisPMTextField.setColumns(10);
        
        analysisSupervisorLabel = new JLabel("Supervisor");
        analysisSupervisorLabel.setHorizontalAlignment(SwingConstants.TRAILING);
        GridBagConstraints gbc_analysisSupervisorLabel = new GridBagConstraints();
        gbc_analysisSupervisorLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_analysisSupervisorLabel.insets = new Insets(0, 0, 5, 5);
        gbc_analysisSupervisorLabel.gridx = 1;
        gbc_analysisSupervisorLabel.gridy = 3;
        requestAnalyticsPane.add(analysisSupervisorLabel, gbc_analysisSupervisorLabel);
        
        analysisSupervisorTextField = new JTextField();
        GridBagConstraints gbc_analysisSupervisorTextField = new GridBagConstraints();
        gbc_analysisSupervisorTextField.insets = new Insets(0, 0, 5, 5);
        gbc_analysisSupervisorTextField.fill = GridBagConstraints.HORIZONTAL;
        gbc_analysisSupervisorTextField.gridx = 2;
        gbc_analysisSupervisorTextField.gridy = 3;
        requestAnalyticsPane.add(analysisSupervisorTextField, gbc_analysisSupervisorTextField);
        analysisSupervisorTextField.setColumns(10);
        
        analysisSubmissionDateLabel = new JLabel("Submission Date");
        analysisSubmissionDateLabel.setHorizontalAlignment(SwingConstants.TRAILING);
        GridBagConstraints gbc_analysisSubmissionDateLabel = new GridBagConstraints();
        gbc_analysisSubmissionDateLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_analysisSubmissionDateLabel.insets = new Insets(0, 0, 5, 5);
        gbc_analysisSubmissionDateLabel.gridx = 1;
        gbc_analysisSubmissionDateLabel.gridy = 4;
        requestAnalyticsPane.add(analysisSubmissionDateLabel, gbc_analysisSubmissionDateLabel);
        
        analysisSubmissionDateTextField = new JTextField();
        GridBagConstraints gbc_analysisSubmissionDateTextField = new GridBagConstraints();
        gbc_analysisSubmissionDateTextField.insets = new Insets(0, 0, 5, 5);
        gbc_analysisSubmissionDateTextField.fill = GridBagConstraints.HORIZONTAL;
        gbc_analysisSubmissionDateTextField.gridx = 2;
        gbc_analysisSubmissionDateTextField.gridy = 4;
        requestAnalyticsPane.add(analysisSubmissionDateTextField, gbc_analysisSubmissionDateTextField);
        analysisSubmissionDateTextField.setColumns(10);
        
        analysisStartDateLabel = new JLabel("Start Date");
        analysisStartDateLabel.setHorizontalAlignment(SwingConstants.TRAILING);
        GridBagConstraints gbc_analysisStartDateLabel = new GridBagConstraints();
        gbc_analysisStartDateLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_analysisStartDateLabel.insets = new Insets(0, 0, 5, 5);
        gbc_analysisStartDateLabel.gridx = 1;
        gbc_analysisStartDateLabel.gridy = 5;
        requestAnalyticsPane.add(analysisStartDateLabel, gbc_analysisStartDateLabel);
        
        analysisStartDateFieldText = new JTextField();
        GridBagConstraints gbc_analysisStartDateFieldText = new GridBagConstraints();
        gbc_analysisStartDateFieldText.insets = new Insets(0, 0, 5, 5);
        gbc_analysisStartDateFieldText.fill = GridBagConstraints.HORIZONTAL;
        gbc_analysisStartDateFieldText.gridx = 2;
        gbc_analysisStartDateFieldText.gridy = 5;
        requestAnalyticsPane.add(analysisStartDateFieldText, gbc_analysisStartDateFieldText);
        analysisStartDateFieldText.setColumns(10);
        
        verticalStrut_16 = Box.createVerticalStrut(100);
        GridBagConstraints gbc_verticalStrut_16 = new GridBagConstraints();
        gbc_verticalStrut_16.gridheight = 4;
        gbc_verticalStrut_16.insets = new Insets(0, 0, 5, 5);
        gbc_verticalStrut_16.gridx = 0;
        gbc_verticalStrut_16.gridy = 7;
        requestAnalyticsPane.add(verticalStrut_16, gbc_verticalStrut_16);
        
        verticalStrut_14 = Box.createVerticalStrut(20);
        GridBagConstraints gbc_verticalStrut_14 = new GridBagConstraints();
        gbc_verticalStrut_14.gridwidth = 2;
        gbc_verticalStrut_14.insets = new Insets(0, 0, 5, 5);
        gbc_verticalStrut_14.gridx = 0;
        gbc_verticalStrut_14.gridy = 6;
        requestAnalyticsPane.add(verticalStrut_14, gbc_verticalStrut_14);
        
        analyisOtherPanel = new JPanel();
        analyisOtherPanel.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        GridBagConstraints gbc_analyisOtherPanel = new GridBagConstraints();
        gbc_analyisOtherPanel.gridheight = 4;
        gbc_analyisOtherPanel.gridwidth = 2;
        gbc_analyisOtherPanel.insets = new Insets(0, 0, 5, 5);
        gbc_analyisOtherPanel.fill = GridBagConstraints.BOTH;
        gbc_analyisOtherPanel.gridx = 1;
        gbc_analyisOtherPanel.gridy = 7;
        requestAnalyticsPane.add(analyisOtherPanel, gbc_analyisOtherPanel);
        
        verticalStrut_15 = Box.createVerticalStrut(20);
        GridBagConstraints gbc_verticalStrut_15 = new GridBagConstraints();
        gbc_verticalStrut_15.gridwidth = 2;
        gbc_verticalStrut_15.insets = new Insets(0, 0, 5, 5);
        gbc_verticalStrut_15.gridx = 0;
        gbc_verticalStrut_15.gridy = 11;
        requestAnalyticsPane.add(verticalStrut_15, gbc_verticalStrut_15);
        
        analysisTimeLinePanel = new JPanel();
        analysisTimeLinePanel.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        GridBagConstraints gbc_analysisTimeLinePanel = new GridBagConstraints();
        gbc_analysisTimeLinePanel.gridwidth = 8;
        gbc_analysisTimeLinePanel.insets = new Insets(0, 0, 5, 5);
        gbc_analysisTimeLinePanel.fill = GridBagConstraints.BOTH;
        gbc_analysisTimeLinePanel.gridx = 1;
        gbc_analysisTimeLinePanel.gridy = 12;
        requestAnalyticsPane.add(analysisTimeLinePanel, gbc_analysisTimeLinePanel);
        
        horizontalStrut_18 = Box.createHorizontalStrut(20);
        GridBagConstraints gbc_horizontalStrut_18 = new GridBagConstraints();
        gbc_horizontalStrut_18.insets = new Insets(0, 0, 5, 0);
        gbc_horizontalStrut_18.gridx = 9;
        gbc_horizontalStrut_18.gridy = 12;
        requestAnalyticsPane.add(horizontalStrut_18, gbc_horizontalStrut_18);
        
        verticalStrut_13 = Box.createVerticalStrut(20);
        GridBagConstraints gbc_verticalStrut_13 = new GridBagConstraints();
        gbc_verticalStrut_13.insets = new Insets(0, 0, 0, 5);
        gbc_verticalStrut_13.gridx = 1;
        gbc_verticalStrut_13.gridy = 13;
        requestAnalyticsPane.add(verticalStrut_13, gbc_verticalStrut_13);
        
        
        //end Analytics
	}
	
	//New Work Request GUI
	private void createNewWorkRequestGUI()
	{
        //new Work Requests
        newRequestPane = new JPanel();
        gernalTabbedPane.addTab("New Work Request", null, newRequestPane, null);
        newRequestPane.setLayout(new BoxLayout(newRequestPane, BoxLayout.Y_AXIS));
        
        newWorkRequestPane = new JTabbedPane(JTabbedPane.TOP);
        newRequestPane.add(newWorkRequestPane);
        
        remarksTextArea = new JTextArea();
        
        remarkSavePane = new JPanel();
        newRequestPane.add(remarkSavePane);
        GridBagLayout gbl_remarkSavePane = new GridBagLayout();
        gbl_remarkSavePane.columnWidths = new int[]{0, 0, 0, 0, 0, 0};
        gbl_remarkSavePane.rowHeights = new int[]{0, 0, 0, 0, 0, 0};
        gbl_remarkSavePane.columnWeights = new double[]{0.0, 0.0, 1.0, 1.0, 0.0, Double.MIN_VALUE};
        gbl_remarkSavePane.rowWeights = new double[]{0.0, 1.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
        remarkSavePane.setLayout(gbl_remarkSavePane);
        
        verticalStrut_1 = Box.createVerticalStrut(20);
        GridBagConstraints gbc_verticalStrut_1 = new GridBagConstraints();
        gbc_verticalStrut_1.insets = new Insets(0, 0, 5, 5);
        gbc_verticalStrut_1.gridx = 2;
        gbc_verticalStrut_1.gridy = 0;
        remarkSavePane.add(verticalStrut_1, gbc_verticalStrut_1);
        
        scrollPane = new JScrollPane();
        GridBagConstraints gbc_scrollPane = new GridBagConstraints();
        gbc_scrollPane.fill = GridBagConstraints.BOTH;
        gbc_scrollPane.gridheight = 2;
        gbc_scrollPane.gridwidth = 2;
        gbc_scrollPane.insets = new Insets(0, 0, 5, 5);
        gbc_scrollPane.gridx = 2;
        gbc_scrollPane.gridy = 1;
        remarkSavePane.add(scrollPane, gbc_scrollPane);
        
        wrRemarkTextArea = new JTextArea();
        scrollPane.setViewportView(wrRemarkTextArea);
        
        horizontalStrut_5 = Box.createHorizontalStrut(20);
        GridBagConstraints gbc_horizontalStrut_5 = new GridBagConstraints();
        gbc_horizontalStrut_5.insets = new Insets(0, 0, 5, 0);
        gbc_horizontalStrut_5.gridx = 4;
        gbc_horizontalStrut_5.gridy = 1;
        remarkSavePane.add(horizontalStrut_5, gbc_horizontalStrut_5);
        
        horizontalStrut_4 = Box.createHorizontalStrut(20);
        GridBagConstraints gbc_horizontalStrut_4 = new GridBagConstraints();
        gbc_horizontalStrut_4.insets = new Insets(0, 0, 5, 5);
        gbc_horizontalStrut_4.gridx = 1;
        gbc_horizontalStrut_4.gridy = 2;
        remarkSavePane.add(horizontalStrut_4, gbc_horizontalStrut_4);
        
        saveRequestBtn = new JButton("Save");
        GridBagConstraints gbc_saveRequestBtn = new GridBagConstraints();
        gbc_saveRequestBtn.insets = new Insets(0, 0, 5, 5);
        gbc_saveRequestBtn.gridx = 2;
        gbc_saveRequestBtn.gridy = 3;
        remarkSavePane.add(saveRequestBtn, gbc_saveRequestBtn);
        
        submitWorkRequestBtn = new JButton("Submit to PA");
        GridBagConstraints gbc_submitWorkRequestBtn = new GridBagConstraints();
        gbc_submitWorkRequestBtn.insets = new Insets(0, 0, 5, 5);
        gbc_submitWorkRequestBtn.gridx = 3;
        gbc_submitWorkRequestBtn.gridy = 3;
        remarkSavePane.add(submitWorkRequestBtn, gbc_submitWorkRequestBtn);
        
        verticalStrut_6 = Box.createVerticalStrut(150);
        GridBagConstraints gbc_verticalStrut_6 = new GridBagConstraints();
        gbc_verticalStrut_6.gridheight = 5;
        gbc_verticalStrut_6.insets = new Insets(0, 0, 0, 5);
        gbc_verticalStrut_6.gridx = 0;
        gbc_verticalStrut_6.gridy = 0;
        remarkSavePane.add(verticalStrut_6, gbc_verticalStrut_6);
        
        generalInfoPane = new JPanel();
        newWorkRequestPane.addTab("General Information", null, generalInfoPane, null);
        GridBagLayout gbl_generalInfoPane = new GridBagLayout();
        gbl_generalInfoPane.columnWidths = new int[]{0, 0, 0, 273, 0, 0, 0};
        gbl_generalInfoPane.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 58, 0, 0, 0};
        gbl_generalInfoPane.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 1.0, 0.0, Double.MIN_VALUE};
        gbl_generalInfoPane.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
        generalInfoPane.setLayout(gbl_generalInfoPane);
        
        verticalStrut = Box.createVerticalStrut(20);
        GridBagConstraints gbc_verticalStrut = new GridBagConstraints();
        gbc_verticalStrut.insets = new Insets(0, 0, 5, 5);
        gbc_verticalStrut.gridx = 1;
        gbc_verticalStrut.gridy = 0;
        generalInfoPane.add(verticalStrut, gbc_verticalStrut);
        
        horizontalStrut_1 = Box.createHorizontalStrut(20);
        GridBagConstraints gbc_horizontalStrut_1 = new GridBagConstraints();
        gbc_horizontalStrut_1.insets = new Insets(0, 0, 5, 0);
        gbc_horizontalStrut_1.gridx = 5;
        gbc_horizontalStrut_1.gridy = 0;
        generalInfoPane.add(horizontalStrut_1, gbc_horizontalStrut_1);
        
        horizontalStrut = Box.createHorizontalStrut(20);
        GridBagConstraints gbc_horizontalStrut = new GridBagConstraints();
        gbc_horizontalStrut.insets = new Insets(0, 0, 5, 5);
        gbc_horizontalStrut.gridx = 0;
        gbc_horizontalStrut.gridy = 1;
        generalInfoPane.add(horizontalStrut, gbc_horizontalStrut);
        
        lblNewLabel = new JLabel("Work Request Number");
        GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
        gbc_lblNewLabel.anchor = GridBagConstraints.EAST;
        gbc_lblNewLabel.insets = new Insets(0, 0, 5, 5);
        gbc_lblNewLabel.gridx = 2;
        gbc_lblNewLabel.gridy = 1;
        generalInfoPane.add(lblNewLabel, gbc_lblNewLabel);
        
        wrNumTextField = new JTextField();
        GridBagConstraints gbc_wrNumTextField = new GridBagConstraints();
        gbc_wrNumTextField.gridwidth = 2;
        gbc_wrNumTextField.insets = new Insets(0, 0, 5, 5);
        gbc_wrNumTextField.fill = GridBagConstraints.HORIZONTAL;
        gbc_wrNumTextField.gridx = 3;
        gbc_wrNumTextField.gridy = 1;
        generalInfoPane.add(wrNumTextField, gbc_wrNumTextField);
        wrNumTextField.setColumns(10);
        
        lblNewLabel_1 = new JLabel("Date Prepared");
        GridBagConstraints gbc_lblNewLabel_1 = new GridBagConstraints();
        gbc_lblNewLabel_1.anchor = GridBagConstraints.EAST;
        gbc_lblNewLabel_1.insets = new Insets(0, 0, 5, 5);
        gbc_lblNewLabel_1.gridx = 2;
        gbc_lblNewLabel_1.gridy = 2;
        generalInfoPane.add(lblNewLabel_1, gbc_lblNewLabel_1);
        
        datePrepTextField = new JTextField();
        datePrepTextField.setColumns(10);
        GridBagConstraints gbc_datePrepTextField = new GridBagConstraints();
        gbc_datePrepTextField.gridwidth = 2;
        gbc_datePrepTextField.insets = new Insets(0, 0, 5, 5);
        gbc_datePrepTextField.fill = GridBagConstraints.HORIZONTAL;
        gbc_datePrepTextField.gridx = 3;
        gbc_datePrepTextField.gridy = 2;
        generalInfoPane.add(datePrepTextField, gbc_datePrepTextField);
        
        lblNewLabel_2 = new JLabel("PM for this Work Request");
        GridBagConstraints gbc_lblNewLabel_2 = new GridBagConstraints();
        gbc_lblNewLabel_2.anchor = GridBagConstraints.EAST;
        gbc_lblNewLabel_2.insets = new Insets(0, 0, 5, 5);
        gbc_lblNewLabel_2.gridx = 2;
        gbc_lblNewLabel_2.gridy = 3;
        generalInfoPane.add(lblNewLabel_2, gbc_lblNewLabel_2);
        
        isPMCheckBox = new JCheckBox("");
        GridBagConstraints gbc_isPMCheckBox = new GridBagConstraints();
        gbc_isPMCheckBox.anchor = GridBagConstraints.WEST;
        gbc_isPMCheckBox.insets = new Insets(0, 0, 5, 5);
        gbc_isPMCheckBox.gridx = 3;
        gbc_isPMCheckBox.gridy = 3;
        generalInfoPane.add(isPMCheckBox, gbc_isPMCheckBox);
        
        lblRequester = new JLabel("Requester");
        GridBagConstraints gbc_lblRequester = new GridBagConstraints();
        gbc_lblRequester.anchor = GridBagConstraints.EAST;
        gbc_lblRequester.insets = new Insets(0, 0, 5, 5);
        gbc_lblRequester.gridx = 2;
        gbc_lblRequester.gridy = 4;
        generalInfoPane.add(lblRequester, gbc_lblRequester);
        
        requesterTextField = new JTextField();
        requesterTextField.setColumns(10);
        GridBagConstraints gbc_requesterTextField = new GridBagConstraints();
        gbc_requesterTextField.gridwidth = 2;
        gbc_requesterTextField.insets = new Insets(0, 0, 5, 5);
        gbc_requesterTextField.fill = GridBagConstraints.HORIZONTAL;
        gbc_requesterTextField.gridx = 3;
        gbc_requesterTextField.gridy = 4;
        generalInfoPane.add(requesterTextField, gbc_requesterTextField);
        
        lblRequsterEmail = new JLabel("Requster Email");
        GridBagConstraints gbc_lblRequsterEmail = new GridBagConstraints();
        gbc_lblRequsterEmail.anchor = GridBagConstraints.EAST;
        gbc_lblRequsterEmail.insets = new Insets(0, 0, 5, 5);
        gbc_lblRequsterEmail.gridx = 2;
        gbc_lblRequsterEmail.gridy = 5;
        generalInfoPane.add(lblRequsterEmail, gbc_lblRequsterEmail);
        
        reqEmailTextField = new JTextField();
        reqEmailTextField.setColumns(10);
        GridBagConstraints gbc_reqEmailTextField = new GridBagConstraints();
        gbc_reqEmailTextField.gridwidth = 2;
        gbc_reqEmailTextField.insets = new Insets(0, 0, 5, 5);
        gbc_reqEmailTextField.fill = GridBagConstraints.HORIZONTAL;
        gbc_reqEmailTextField.gridx = 3;
        gbc_reqEmailTextField.gridy = 5;
        generalInfoPane.add(reqEmailTextField, gbc_reqEmailTextField);
        
        lblRequesterPhone = new JLabel("Requester Phone");
        GridBagConstraints gbc_lblRequesterPhone = new GridBagConstraints();
        gbc_lblRequesterPhone.anchor = GridBagConstraints.EAST;
        gbc_lblRequesterPhone.insets = new Insets(0, 0, 5, 5);
        gbc_lblRequesterPhone.gridx = 2;
        gbc_lblRequesterPhone.gridy = 6;
        generalInfoPane.add(lblRequesterPhone, gbc_lblRequesterPhone);
        
        reqPhoneTextField = new JTextField();
        reqPhoneTextField.setColumns(10);
        GridBagConstraints gbc_reqPhoneTextField = new GridBagConstraints();
        gbc_reqPhoneTextField.gridwidth = 2;
        gbc_reqPhoneTextField.insets = new Insets(0, 0, 5, 5);
        gbc_reqPhoneTextField.fill = GridBagConstraints.HORIZONTAL;
        gbc_reqPhoneTextField.gridx = 3;
        gbc_reqPhoneTextField.gridy = 6;
        generalInfoPane.add(reqPhoneTextField, gbc_reqPhoneTextField);
        
        lblOrganisation = new JLabel("Organisation");
        GridBagConstraints gbc_lblOrganisation = new GridBagConstraints();
        gbc_lblOrganisation.anchor = GridBagConstraints.EAST;
        gbc_lblOrganisation.insets = new Insets(0, 0, 5, 5);
        gbc_lblOrganisation.gridx = 2;
        gbc_lblOrganisation.gridy = 7;
        generalInfoPane.add(lblOrganisation, gbc_lblOrganisation);
        
        orgTextField = new JTextField();
        orgTextField.setColumns(10);
        GridBagConstraints gbc_orgTextField = new GridBagConstraints();
        gbc_orgTextField.gridwidth = 2;
        gbc_orgTextField.insets = new Insets(0, 0, 5, 5);
        gbc_orgTextField.fill = GridBagConstraints.HORIZONTAL;
        gbc_orgTextField.gridx = 3;
        gbc_orgTextField.gridy = 7;
        generalInfoPane.add(orgTextField, gbc_orgTextField);
        
        lblRequestedOnBehalf = new JLabel("Requested on behalf of");
        GridBagConstraints gbc_lblRequestedOnBehalf = new GridBagConstraints();
        gbc_lblRequestedOnBehalf.anchor = GridBagConstraints.EAST;
        gbc_lblRequestedOnBehalf.insets = new Insets(0, 0, 5, 5);
        gbc_lblRequestedOnBehalf.gridx = 2;
        gbc_lblRequestedOnBehalf.gridy = 8;
        generalInfoPane.add(lblRequestedOnBehalf, gbc_lblRequestedOnBehalf);
        
        onBehalfTextField = new JTextField();
        onBehalfTextField.setColumns(10);
        GridBagConstraints gbc_onBehalfTextField = new GridBagConstraints();
        gbc_onBehalfTextField.gridwidth = 2;
        gbc_onBehalfTextField.insets = new Insets(0, 0, 5, 5);
        gbc_onBehalfTextField.fill = GridBagConstraints.HORIZONTAL;
        gbc_onBehalfTextField.gridx = 3;
        gbc_onBehalfTextField.gridy = 8;
        generalInfoPane.add(onBehalfTextField, gbc_onBehalfTextField);
        
        lblProjectManager = new JLabel("Project Manager");
        GridBagConstraints gbc_lblProjectManager = new GridBagConstraints();
        gbc_lblProjectManager.anchor = GridBagConstraints.EAST;
        gbc_lblProjectManager.insets = new Insets(0, 0, 5, 5);
        gbc_lblProjectManager.gridx = 2;
        gbc_lblProjectManager.gridy = 9;
        generalInfoPane.add(lblProjectManager, gbc_lblProjectManager);
        
        pmTextField = new JTextField();
        pmTextField.setColumns(10);
        GridBagConstraints gbc_pmTextField = new GridBagConstraints();
        gbc_pmTextField.gridwidth = 2;
        gbc_pmTextField.insets = new Insets(0, 0, 5, 5);
        gbc_pmTextField.fill = GridBagConstraints.HORIZONTAL;
        gbc_pmTextField.gridx = 3;
        gbc_pmTextField.gridy = 9;
        generalInfoPane.add(pmTextField, gbc_pmTextField);
        
        lblProjectManagerEmail = new JLabel("Project Manager Email");
        GridBagConstraints gbc_lblProjectManagerEmail = new GridBagConstraints();
        gbc_lblProjectManagerEmail.anchor = GridBagConstraints.EAST;
        gbc_lblProjectManagerEmail.insets = new Insets(0, 0, 5, 5);
        gbc_lblProjectManagerEmail.gridx = 2;
        gbc_lblProjectManagerEmail.gridy = 10;
        generalInfoPane.add(lblProjectManagerEmail, gbc_lblProjectManagerEmail);
        
        pmEmailTextField = new JTextField();
        pmEmailTextField.setColumns(10);
        GridBagConstraints gbc_pmEmailTextField = new GridBagConstraints();
        gbc_pmEmailTextField.gridwidth = 2;
        gbc_pmEmailTextField.insets = new Insets(0, 0, 5, 5);
        gbc_pmEmailTextField.fill = GridBagConstraints.HORIZONTAL;
        gbc_pmEmailTextField.gridx = 3;
        gbc_pmEmailTextField.gridy = 10;
        generalInfoPane.add(pmEmailTextField, gbc_pmEmailTextField);
        
        lblProjectManagerPhone = new JLabel("Project Manager Phone");
        GridBagConstraints gbc_lblProjectManagerPhone = new GridBagConstraints();
        gbc_lblProjectManagerPhone.anchor = GridBagConstraints.EAST;
        gbc_lblProjectManagerPhone.insets = new Insets(0, 0, 5, 5);
        gbc_lblProjectManagerPhone.gridx = 2;
        gbc_lblProjectManagerPhone.gridy = 11;
        generalInfoPane.add(lblProjectManagerPhone, gbc_lblProjectManagerPhone);
        
        pmPhoneTextField = new JTextField();
        pmPhoneTextField.setColumns(10);
        GridBagConstraints gbc_pmPhoneTextField = new GridBagConstraints();
        gbc_pmPhoneTextField.gridwidth = 2;
        gbc_pmPhoneTextField.insets = new Insets(0, 0, 5, 5);
        gbc_pmPhoneTextField.fill = GridBagConstraints.HORIZONTAL;
        gbc_pmPhoneTextField.gridx = 3;
        gbc_pmPhoneTextField.gridy = 11;
        generalInfoPane.add(pmPhoneTextField, gbc_pmPhoneTextField);
        
        lblWorkRequestStatus = new JLabel("Work Request Status");
        GridBagConstraints gbc_lblWorkRequestStatus = new GridBagConstraints();
        gbc_lblWorkRequestStatus.anchor = GridBagConstraints.EAST;
        gbc_lblWorkRequestStatus.insets = new Insets(0, 0, 5, 5);
        gbc_lblWorkRequestStatus.gridx = 2;
        gbc_lblWorkRequestStatus.gridy = 12;
        generalInfoPane.add(lblWorkRequestStatus, gbc_lblWorkRequestStatus);
        
        wrStatusTextFeild = new JTextField();
        wrStatusTextFeild.setColumns(10);
        GridBagConstraints gbc_wrStatusTextFeild = new GridBagConstraints();
        gbc_wrStatusTextFeild.gridwidth = 2;
        gbc_wrStatusTextFeild.insets = new Insets(0, 0, 5, 5);
        gbc_wrStatusTextFeild.fill = GridBagConstraints.HORIZONTAL;
        gbc_wrStatusTextFeild.gridx = 3;
        gbc_wrStatusTextFeild.gridy = 12;
        generalInfoPane.add(wrStatusTextFeild, gbc_wrStatusTextFeild);
        
        verticalStrut_2 = Box.createVerticalStrut(20);
        GridBagConstraints gbc_verticalStrut_2 = new GridBagConstraints();
        gbc_verticalStrut_2.insets = new Insets(0, 0, 5, 5);
        gbc_verticalStrut_2.gridx = 0;
        gbc_verticalStrut_2.gridy = 13;
        generalInfoPane.add(verticalStrut_2, gbc_verticalStrut_2);
        
        prevWRLabel = new JLabel("Previous WR Info");
        GridBagConstraints gbc_prevWRLabel = new GridBagConstraints();
        gbc_prevWRLabel.anchor = GridBagConstraints.NORTH;
        gbc_prevWRLabel.insets = new Insets(0, 0, 5, 5);
        gbc_prevWRLabel.gridx = 2;
        gbc_prevWRLabel.gridy = 13;
        generalInfoPane.add(prevWRLabel, gbc_prevWRLabel);
        
        copyPrevWRBtn = new JButton("Copy from Previous WR");
        GridBagConstraints gbc_copyPrevWRBtn = new GridBagConstraints();
        gbc_copyPrevWRBtn.anchor = GridBagConstraints.NORTHWEST;
        gbc_copyPrevWRBtn.insets = new Insets(0, 0, 5, 5);
        gbc_copyPrevWRBtn.gridx = 3;
        gbc_copyPrevWRBtn.gridy = 13;
        generalInfoPane.add(copyPrevWRBtn, gbc_copyPrevWRBtn);
        
        prevWRCopyPane = new JPanel();
        GridBagConstraints gbc_prevWRCopyPane = new GridBagConstraints();
        gbc_prevWRCopyPane.anchor = GridBagConstraints.NORTH;
        gbc_prevWRCopyPane.insets = new Insets(0, 0, 5, 5);
        gbc_prevWRCopyPane.fill = GridBagConstraints.HORIZONTAL;
        gbc_prevWRCopyPane.gridx = 4;
        gbc_prevWRCopyPane.gridy = 13;
        generalInfoPane.add(prevWRCopyPane, gbc_prevWRCopyPane);
        prevWRCopyPane.setLayout(new BoxLayout(prevWRCopyPane, BoxLayout.Y_AXIS));
        
        wrNumberLabel = new JLabel("Work Order Number: none");
        prevWRCopyPane.add(wrNumberLabel);
        
        projNameLabel = new JLabel("Project Name: none");
        prevWRCopyPane.add(projNameLabel);
        
        p2NumLabel = new JLabel("P2 Number: none");
        prevWRCopyPane.add(p2NumLabel);
        
        lblNewLabel_3 = new JLabel("Date Added");
        lblNewLabel_3.setForeground(Color.DARK_GRAY);
        lblNewLabel_3.setHorizontalAlignment(SwingConstants.CENTER);
        lblNewLabel_3.setBackground(new Color(176, 224, 230));
        GridBagConstraints gbc_lblNewLabel_3 = new GridBagConstraints();
        gbc_lblNewLabel_3.fill = GridBagConstraints.HORIZONTAL;
        gbc_lblNewLabel_3.insets = new Insets(0, 0, 5, 5);
        gbc_lblNewLabel_3.gridx = 2;
        gbc_lblNewLabel_3.gridy = 14;
        generalInfoPane.add(lblNewLabel_3, gbc_lblNewLabel_3);
        
        lblNewLabel_4 = new JLabel("User");
        lblNewLabel_4.setForeground(Color.DARK_GRAY);
        lblNewLabel_4.setBackground(new Color(176, 224, 230));
        lblNewLabel_4.setHorizontalAlignment(SwingConstants.CENTER);
        GridBagConstraints gbc_lblNewLabel_4 = new GridBagConstraints();
        gbc_lblNewLabel_4.fill = GridBagConstraints.HORIZONTAL;
        gbc_lblNewLabel_4.insets = new Insets(0, 0, 5, 5);
        gbc_lblNewLabel_4.gridx = 3;
        gbc_lblNewLabel_4.gridy = 14;
        generalInfoPane.add(lblNewLabel_4, gbc_lblNewLabel_4);
        
        lblNewLabel_5 = new JLabel("Remark/Note");
        lblNewLabel_5.setForeground(Color.DARK_GRAY);
        lblNewLabel_5.setBackground(new Color(176, 224, 230));
        lblNewLabel_5.setHorizontalAlignment(SwingConstants.CENTER);
        GridBagConstraints gbc_lblNewLabel_5 = new GridBagConstraints();
        gbc_lblNewLabel_5.fill = GridBagConstraints.HORIZONTAL;
        gbc_lblNewLabel_5.insets = new Insets(0, 0, 5, 5);
        gbc_lblNewLabel_5.gridx = 4;
        gbc_lblNewLabel_5.gridy = 14;
        generalInfoPane.add(lblNewLabel_5, gbc_lblNewLabel_5);
        
        dateAddedTextField = new JTextField();
        GridBagConstraints gbc_dateAddedTextField = new GridBagConstraints();
        gbc_dateAddedTextField.insets = new Insets(0, 0, 0, 5);
        gbc_dateAddedTextField.fill = GridBagConstraints.HORIZONTAL;
        gbc_dateAddedTextField.gridx = 2;
        gbc_dateAddedTextField.gridy = 15;
        generalInfoPane.add(dateAddedTextField, gbc_dateAddedTextField);
        dateAddedTextField.setColumns(10);
        
        userTextField = new JTextField();
        GridBagConstraints gbc_userTextField = new GridBagConstraints();
        gbc_userTextField.insets = new Insets(0, 0, 0, 5);
        gbc_userTextField.fill = GridBagConstraints.HORIZONTAL;
        gbc_userTextField.gridx = 3;
        gbc_userTextField.gridy = 15;
        generalInfoPane.add(userTextField, gbc_userTextField);
        userTextField.setColumns(10);
        
        scrollPane_1 = new JScrollPane();
        GridBagConstraints gbc_scrollPane_1 = new GridBagConstraints();
        gbc_scrollPane_1.fill = GridBagConstraints.BOTH;
        gbc_scrollPane_1.insets = new Insets(0, 0, 0, 5);
        gbc_scrollPane_1.gridx = 4;
        gbc_scrollPane_1.gridy = 15;
        generalInfoPane.add(scrollPane_1, gbc_scrollPane_1);
        
        notesTextField = new JTextArea();
        scrollPane_1.setViewportView(notesTextField);
        notesTextField.setColumns(10);
        
        projectInfoPane = new JPanel();
        newWorkRequestPane.addTab("Project Information", null, projectInfoPane, null);
        GridBagLayout gbl_projectInfoPane = new GridBagLayout();
        gbl_projectInfoPane.columnWidths = new int[]{0, 0, 0, 0, 0, 0, 0, 0};
        gbl_projectInfoPane.rowHeights = new int[]{-13, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        gbl_projectInfoPane.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, Double.MIN_VALUE};
        gbl_projectInfoPane.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
        projectInfoPane.setLayout(gbl_projectInfoPane);
        
        horizontalStrut_3 = Box.createHorizontalStrut(20);
        GridBagConstraints gbc_horizontalStrut_3 = new GridBagConstraints();
        gbc_horizontalStrut_3.insets = new Insets(0, 0, 5, 0);
        gbc_horizontalStrut_3.gridx = 6;
        gbc_horizontalStrut_3.gridy = 0;
        projectInfoPane.add(horizontalStrut_3, gbc_horizontalStrut_3);
        
        horizontalStrut_2 = Box.createHorizontalStrut(20);
        GridBagConstraints gbc_horizontalStrut_2 = new GridBagConstraints();
        gbc_horizontalStrut_2.insets = new Insets(0, 0, 5, 5);
        gbc_horizontalStrut_2.gridx = 0;
        gbc_horizontalStrut_2.gridy = 1;
        projectInfoPane.add(horizontalStrut_2, gbc_horizontalStrut_2);
        
        projectNameLabel = new JLabel("Project Name");
        GridBagConstraints gbc_projectNameLabel = new GridBagConstraints();
        gbc_projectNameLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_projectNameLabel.insets = new Insets(0, 0, 5, 5);
        gbc_projectNameLabel.gridx = 1;
        gbc_projectNameLabel.gridy = 1;
        projectInfoPane.add(projectNameLabel, gbc_projectNameLabel);
        
        projectNameTextField = new JTextField();
        GridBagConstraints gbc_projectNameTextField = new GridBagConstraints();
        gbc_projectNameTextField.gridwidth = 4;
        gbc_projectNameTextField.insets = new Insets(0, 0, 5, 5);
        gbc_projectNameTextField.fill = GridBagConstraints.HORIZONTAL;
        gbc_projectNameTextField.gridx = 2;
        gbc_projectNameTextField.gridy = 1;
        projectInfoPane.add(projectNameTextField, gbc_projectNameTextField);
        projectNameTextField.setColumns(10);
        
        projectNumLabel = new JLabel("Project Number");
        GridBagConstraints gbc_projectNumLabel = new GridBagConstraints();
        gbc_projectNumLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_projectNumLabel.insets = new Insets(0, 0, 5, 5);
        gbc_projectNumLabel.gridx = 1;
        gbc_projectNumLabel.gridy = 2;
        projectInfoPane.add(projectNumLabel, gbc_projectNumLabel);
        
        projectNumTextField = new JTextField();
        projectNumTextField.setColumns(10);
        GridBagConstraints gbc_projectNumTextField = new GridBagConstraints();
        gbc_projectNumTextField.gridwidth = 4;
        gbc_projectNumTextField.insets = new Insets(0, 0, 5, 5);
        gbc_projectNumTextField.fill = GridBagConstraints.HORIZONTAL;
        gbc_projectNumTextField.gridx = 2;
        gbc_projectNumTextField.gridy = 2;
        projectInfoPane.add(projectNumTextField, gbc_projectNumTextField);
        
        painLabel = new JLabel("PAIN");
        GridBagConstraints gbc_painLabel = new GridBagConstraints();
        gbc_painLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_painLabel.insets = new Insets(0, 0, 5, 5);
        gbc_painLabel.gridx = 1;
        gbc_painLabel.gridy = 3;
        projectInfoPane.add(painLabel, gbc_painLabel);
        
        painTextField = new JTextField();
        painTextField.setColumns(10);
        GridBagConstraints gbc_painTextField = new GridBagConstraints();
        gbc_painTextField.gridwidth = 4;
        gbc_painTextField.insets = new Insets(0, 0, 5, 5);
        gbc_painTextField.fill = GridBagConstraints.HORIZONTAL;
        gbc_painTextField.gridx = 2;
        gbc_painTextField.gridy = 3;
        projectInfoPane.add(painTextField, gbc_painTextField);
        
        amountLabel = new JLabel("Program Amount");
        GridBagConstraints gbc_amountLabel = new GridBagConstraints();
        gbc_amountLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_amountLabel.insets = new Insets(0, 0, 5, 5);
        gbc_amountLabel.gridx = 1;
        gbc_amountLabel.gridy = 4;
        projectInfoPane.add(amountLabel, gbc_amountLabel);
        
        programAmountTextField = new JTextField();
        programAmountTextField.setColumns(10);
        GridBagConstraints gbc_programAmountTextField = new GridBagConstraints();
        gbc_programAmountTextField.gridwidth = 4;
        gbc_programAmountTextField.insets = new Insets(0, 0, 5, 5);
        gbc_programAmountTextField.fill = GridBagConstraints.HORIZONTAL;
        gbc_programAmountTextField.gridx = 2;
        gbc_programAmountTextField.gridy = 4;
        projectInfoPane.add(programAmountTextField, gbc_programAmountTextField);
        
        proYearLabel = new JLabel("Program Year");
        GridBagConstraints gbc_proYearLabel = new GridBagConstraints();
        gbc_proYearLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_proYearLabel.insets = new Insets(0, 0, 5, 5);
        gbc_proYearLabel.gridx = 1;
        gbc_proYearLabel.gridy = 5;
        projectInfoPane.add(proYearLabel, gbc_proYearLabel);
        
        programYearTextField = new JTextField();
        GridBagConstraints gbc_programYearTextField = new GridBagConstraints();
        gbc_programYearTextField.gridwidth = 4;
        gbc_programYearTextField.insets = new Insets(0, 0, 5, 5);
        gbc_programYearTextField.fill = GridBagConstraints.HORIZONTAL;
        gbc_programYearTextField.gridx = 2;
        gbc_programYearTextField.gridy = 5;
        projectInfoPane.add(programYearTextField, gbc_programYearTextField);
        programYearTextField.setColumns(10);
        
        p2Label = new JLabel("P2 Number");
        GridBagConstraints gbc_p2Label = new GridBagConstraints();
        gbc_p2Label.fill = GridBagConstraints.HORIZONTAL;
        gbc_p2Label.insets = new Insets(0, 0, 5, 5);
        gbc_p2Label.gridx = 1;
        gbc_p2Label.gridy = 6;
        projectInfoPane.add(p2Label, gbc_p2Label);
        
        p2TextField = new JTextField();
        p2TextField.setText("");
        GridBagConstraints gbc_p2TextField = new GridBagConstraints();
        gbc_p2TextField.gridwidth = 4;
        gbc_p2TextField.insets = new Insets(0, 0, 5, 5);
        gbc_p2TextField.fill = GridBagConstraints.HORIZONTAL;
        gbc_p2TextField.gridx = 2;
        gbc_p2TextField.gridy = 6;
        projectInfoPane.add(p2TextField, gbc_p2TextField);
        p2TextField.setColumns(10);
        
        contractNumberLabel = new JLabel("Contract Number");
        GridBagConstraints gbc_contractNumberLabel = new GridBagConstraints();
        gbc_contractNumberLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_contractNumberLabel.insets = new Insets(0, 0, 5, 5);
        gbc_contractNumberLabel.gridx = 1;
        gbc_contractNumberLabel.gridy = 7;
        projectInfoPane.add(contractNumberLabel, gbc_contractNumberLabel);
        
        contractNumTextField = new JTextField();
        contractNumTextField.setText("");
        GridBagConstraints gbc_contractNumTextField = new GridBagConstraints();
        gbc_contractNumTextField.gridwidth = 4;
        gbc_contractNumTextField.insets = new Insets(0, 0, 5, 5);
        gbc_contractNumTextField.fill = GridBagConstraints.HORIZONTAL;
        gbc_contractNumTextField.gridx = 2;
        gbc_contractNumTextField.gridy = 7;
        projectInfoPane.add(contractNumTextField, gbc_contractNumTextField);
        contractNumTextField.setColumns(10);
        
        fundSrcLabel = new JLabel("Fund Source");
        GridBagConstraints gbc_fundSrcLabel = new GridBagConstraints();
        gbc_fundSrcLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_fundSrcLabel.insets = new Insets(0, 0, 5, 5);
        gbc_fundSrcLabel.gridx = 1;
        gbc_fundSrcLabel.gridy = 8;
        projectInfoPane.add(fundSrcLabel, gbc_fundSrcLabel);
        
        fundSourceComboBox = new JComboBox();
        GridBagConstraints gbc_fundSourceComboBox = new GridBagConstraints();
        gbc_fundSourceComboBox.gridwidth = 4;
        gbc_fundSourceComboBox.insets = new Insets(0, 0, 5, 5);
        gbc_fundSourceComboBox.fill = GridBagConstraints.HORIZONTAL;
        gbc_fundSourceComboBox.gridx = 2;
        gbc_fundSourceComboBox.gridy = 8;
        projectInfoPane.add(fundSourceComboBox, gbc_fundSourceComboBox);
        
        sectionLabel = new JLabel("Section");
        GridBagConstraints gbc_sectionLabel = new GridBagConstraints();
        gbc_sectionLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_sectionLabel.insets = new Insets(0, 0, 5, 5);
        gbc_sectionLabel.gridx = 1;
        gbc_sectionLabel.gridy = 9;
        projectInfoPane.add(sectionLabel, gbc_sectionLabel);
        
        sectionComboBox = new JComboBox();
        GridBagConstraints gbc_sectionComboBox = new GridBagConstraints();
        gbc_sectionComboBox.gridwidth = 4;
        gbc_sectionComboBox.insets = new Insets(0, 0, 5, 5);
        gbc_sectionComboBox.fill = GridBagConstraints.HORIZONTAL;
        gbc_sectionComboBox.gridx = 2;
        gbc_sectionComboBox.gridy = 9;
        projectInfoPane.add(sectionComboBox, gbc_sectionComboBox);
        
        countryLabel = new JLabel("Country");
        GridBagConstraints gbc_countryLabel = new GridBagConstraints();
        gbc_countryLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_countryLabel.insets = new Insets(0, 0, 5, 5);
        gbc_countryLabel.gridx = 1;
        gbc_countryLabel.gridy = 10;
        projectInfoPane.add(countryLabel, gbc_countryLabel);
        
        countryComboBox = new JComboBox();
        GridBagConstraints gbc_countryComboBox = new GridBagConstraints();
        gbc_countryComboBox.gridwidth = 4;
        gbc_countryComboBox.insets = new Insets(0, 0, 5, 5);
        gbc_countryComboBox.fill = GridBagConstraints.HORIZONTAL;
        gbc_countryComboBox.gridx = 2;
        gbc_countryComboBox.gridy = 10;
        projectInfoPane.add(countryComboBox, gbc_countryComboBox);
        
        regionLabel = new JLabel("City/Region");
        GridBagConstraints gbc_regionLabel = new GridBagConstraints();
        gbc_regionLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_regionLabel.insets = new Insets(0, 0, 5, 5);
        gbc_regionLabel.gridx = 1;
        gbc_regionLabel.gridy = 11;
        projectInfoPane.add(regionLabel, gbc_regionLabel);
        
        regionComboBox = new JComboBox();
        GridBagConstraints gbc_regionComboBox = new GridBagConstraints();
        gbc_regionComboBox.gridwidth = 4;
        gbc_regionComboBox.insets = new Insets(0, 0, 5, 5);
        gbc_regionComboBox.fill = GridBagConstraints.HORIZONTAL;
        gbc_regionComboBox.gridx = 2;
        gbc_regionComboBox.gridy = 11;
        projectInfoPane.add(regionComboBox, gbc_regionComboBox);
        
        siteLabel = new JLabel("Site");
        GridBagConstraints gbc_siteLabel = new GridBagConstraints();
        gbc_siteLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_siteLabel.insets = new Insets(0, 0, 5, 5);
        gbc_siteLabel.gridx = 1;
        gbc_siteLabel.gridy = 12;
        projectInfoPane.add(siteLabel, gbc_siteLabel);
        
        siteComboBox = new JComboBox();
        GridBagConstraints gbc_siteComboBox = new GridBagConstraints();
        gbc_siteComboBox.gridwidth = 4;
        gbc_siteComboBox.insets = new Insets(0, 0, 5, 5);
        gbc_siteComboBox.fill = GridBagConstraints.HORIZONTAL;
        gbc_siteComboBox.gridx = 2;
        gbc_siteComboBox.gridy = 12;
        projectInfoPane.add(siteComboBox, gbc_siteComboBox);
        
        analystSuperLabel = new JLabel("Program Analyst Supervisor");
        GridBagConstraints gbc_analystSuperLabel = new GridBagConstraints();
        gbc_analystSuperLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_analystSuperLabel.insets = new Insets(0, 0, 5, 5);
        gbc_analystSuperLabel.gridx = 1;
        gbc_analystSuperLabel.gridy = 13;
        projectInfoPane.add(analystSuperLabel, gbc_analystSuperLabel);
        
        programAnalystSupervisorComboBox = new JComboBox();
        GridBagConstraints gbc_programAnalystSupervisorComboBox = new GridBagConstraints();
        gbc_programAnalystSupervisorComboBox.gridwidth = 4;
        gbc_programAnalystSupervisorComboBox.insets = new Insets(0, 0, 5, 5);
        gbc_programAnalystSupervisorComboBox.fill = GridBagConstraints.HORIZONTAL;
        gbc_programAnalystSupervisorComboBox.gridx = 2;
        gbc_programAnalystSupervisorComboBox.gridy = 13;
        projectInfoPane.add(programAnalystSupervisorComboBox, gbc_programAnalystSupervisorComboBox);
        
        branchLabel = new JLabel("Branch");
        GridBagConstraints gbc_branchLabel = new GridBagConstraints();
        gbc_branchLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_branchLabel.insets = new Insets(0, 0, 5, 5);
        gbc_branchLabel.gridx = 1;
        gbc_branchLabel.gridy = 14;
        projectInfoPane.add(branchLabel, gbc_branchLabel);
        
        branchTextField = new JTextField();
        GridBagConstraints gbc_branchTextField = new GridBagConstraints();
        gbc_branchTextField.gridwidth = 4;
        gbc_branchTextField.insets = new Insets(0, 0, 5, 5);
        gbc_branchTextField.fill = GridBagConstraints.HORIZONTAL;
        gbc_branchTextField.gridx = 2;
        gbc_branchTextField.gridy = 14;
        projectInfoPane.add(branchTextField, gbc_branchTextField);
        branchTextField.setColumns(10);
        
        directProLabel = new JLabel("Direct/Indirect Project");
        GridBagConstraints gbc_directProLabel = new GridBagConstraints();
        gbc_directProLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_directProLabel.insets = new Insets(0, 0, 5, 5);
        gbc_directProLabel.gridx = 1;
        gbc_directProLabel.gridy = 15;
        projectInfoPane.add(directProLabel, gbc_directProLabel);
        
        rdbtnDirect = new JRadioButton("Direct");
        GridBagConstraints gbc_rdbtnDirect = new GridBagConstraints();
        gbc_rdbtnDirect.insets = new Insets(0, 0, 5, 5);
        gbc_rdbtnDirect.gridx = 2;
        gbc_rdbtnDirect.gridy = 15;
        projectInfoPane.add(rdbtnDirect, gbc_rdbtnDirect);
        
        rdbtnIndirect = new JRadioButton("Indirect");
        GridBagConstraints gbc_rdbtnIndirect = new GridBagConstraints();
        gbc_rdbtnIndirect.insets = new Insets(0, 0, 5, 5);
        gbc_rdbtnIndirect.gridx = 3;
        gbc_rdbtnIndirect.gridy = 15;
        projectInfoPane.add(rdbtnIndirect, gbc_rdbtnIndirect);
        
        rdbtnNotApplicable = new JRadioButton("Not Applicable");
        GridBagConstraints gbc_rdbtnNotApplicable = new GridBagConstraints();
        gbc_rdbtnNotApplicable.insets = new Insets(0, 0, 5, 5);
        gbc_rdbtnNotApplicable.gridx = 4;
        gbc_rdbtnNotApplicable.gridy = 15;
        projectInfoPane.add(rdbtnNotApplicable, gbc_rdbtnNotApplicable);
        
        workReqDocFolderLabel = new JLabel("Work Request Document Folder");
        GridBagConstraints gbc_workReqDocFolderLabel = new GridBagConstraints();
        gbc_workReqDocFolderLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_workReqDocFolderLabel.insets = new Insets(0, 0, 0, 5);
        gbc_workReqDocFolderLabel.gridx = 1;
        gbc_workReqDocFolderLabel.gridy = 16;
        projectInfoPane.add(workReqDocFolderLabel, gbc_workReqDocFolderLabel);
        
        wrPathTextField = new JTextField();
        GridBagConstraints gbc_wrPathTextField = new GridBagConstraints();
        gbc_wrPathTextField.gridwidth = 4;
        gbc_wrPathTextField.insets = new Insets(0, 0, 0, 5);
        gbc_wrPathTextField.fill = GridBagConstraints.HORIZONTAL;
        gbc_wrPathTextField.gridx = 2;
        gbc_wrPathTextField.gridy = 16;
        projectInfoPane.add(wrPathTextField, gbc_wrPathTextField);
        wrPathTextField.setColumns(10);
        
        assignmentTypePane = new JPanel();
        newWorkRequestPane.addTab("Assignment Type", null, assignmentTypePane, null);
        GridBagLayout gbl_assignmentTypePane = new GridBagLayout();
        gbl_assignmentTypePane.columnWidths = new int[]{0, 0, 0, 135, 0, 0, 0};
        gbl_assignmentTypePane.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 194, 0};
        gbl_assignmentTypePane.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 1.0, 0.0, Double.MIN_VALUE};
        gbl_assignmentTypePane.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
        assignmentTypePane.setLayout(gbl_assignmentTypePane);
        
        verticalStrut_3 = Box.createVerticalStrut(20);
        GridBagConstraints gbc_verticalStrut_3 = new GridBagConstraints();
        gbc_verticalStrut_3.insets = new Insets(0, 0, 5, 5);
        gbc_verticalStrut_3.gridx = 1;
        gbc_verticalStrut_3.gridy = 0;
        assignmentTypePane.add(verticalStrut_3, gbc_verticalStrut_3);
        
        horizontalStrut_7 = Box.createHorizontalStrut(20);
        GridBagConstraints gbc_horizontalStrut_7 = new GridBagConstraints();
        gbc_horizontalStrut_7.insets = new Insets(0, 0, 5, 5);
        gbc_horizontalStrut_7.gridx = 0;
        gbc_horizontalStrut_7.gridy = 1;
        assignmentTypePane.add(horizontalStrut_7, gbc_horizontalStrut_7);
        
        assignmentCatLabel = new JLabel("Assignment Category");
        GridBagConstraints gbc_assignmentCatLabel = new GridBagConstraints();
        gbc_assignmentCatLabel.anchor = GridBagConstraints.EAST;
        gbc_assignmentCatLabel.insets = new Insets(0, 0, 5, 5);
        gbc_assignmentCatLabel.gridx = 1;
        gbc_assignmentCatLabel.gridy = 1;
        assignmentTypePane.add(assignmentCatLabel, gbc_assignmentCatLabel);
        
        assignmentCatComboBox = new JComboBox();
        GridBagConstraints gbc_assignmentCatComboBox = new GridBagConstraints();
        gbc_assignmentCatComboBox.gridwidth = 3;
        gbc_assignmentCatComboBox.insets = new Insets(0, 0, 5, 5);
        gbc_assignmentCatComboBox.fill = GridBagConstraints.HORIZONTAL;
        gbc_assignmentCatComboBox.gridx = 2;
        gbc_assignmentCatComboBox.gridy = 1;
        assignmentTypePane.add(assignmentCatComboBox, gbc_assignmentCatComboBox);
        
        horizontalStrut_6 = Box.createHorizontalStrut(20);
        GridBagConstraints gbc_horizontalStrut_6 = new GridBagConstraints();
        gbc_horizontalStrut_6.insets = new Insets(0, 0, 5, 0);
        gbc_horizontalStrut_6.gridx = 5;
        gbc_horizontalStrut_6.gridy = 1;
        assignmentTypePane.add(horizontalStrut_6, gbc_horizontalStrut_6);
        
        assignmentTypeLabel = new JLabel("Assignment Type");
        GridBagConstraints gbc_assignmentTypeLabel = new GridBagConstraints();
        gbc_assignmentTypeLabel.anchor = GridBagConstraints.EAST;
        gbc_assignmentTypeLabel.insets = new Insets(0, 0, 5, 5);
        gbc_assignmentTypeLabel.gridx = 1;
        gbc_assignmentTypeLabel.gridy = 2;
        assignmentTypePane.add(assignmentTypeLabel, gbc_assignmentTypeLabel);
        
        assignmentTypeComboBox = new JComboBox();
        GridBagConstraints gbc_assignmentTypeComboBox = new GridBagConstraints();
        gbc_assignmentTypeComboBox.gridwidth = 3;
        gbc_assignmentTypeComboBox.insets = new Insets(0, 0, 5, 5);
        gbc_assignmentTypeComboBox.fill = GridBagConstraints.HORIZONTAL;
        gbc_assignmentTypeComboBox.gridx = 2;
        gbc_assignmentTypeComboBox.gridy = 2;
        assignmentTypePane.add(assignmentTypeComboBox, gbc_assignmentTypeComboBox);
        
        assignmentSubTypeLabel = new JLabel("Assignment Subtype");
        GridBagConstraints gbc_assignmentSubTypeLabel = new GridBagConstraints();
        gbc_assignmentSubTypeLabel.insets = new Insets(0, 0, 5, 5);
        gbc_assignmentSubTypeLabel.anchor = GridBagConstraints.EAST;
        gbc_assignmentSubTypeLabel.gridx = 1;
        gbc_assignmentSubTypeLabel.gridy = 3;
        assignmentTypePane.add(assignmentSubTypeLabel, gbc_assignmentSubTypeLabel);
        
        assignmentSubTypeComboBox = new JComboBox();
        GridBagConstraints gbc_assignmentSubTypeComboBox = new GridBagConstraints();
        gbc_assignmentSubTypeComboBox.gridwidth = 3;
        gbc_assignmentSubTypeComboBox.insets = new Insets(0, 0, 5, 5);
        gbc_assignmentSubTypeComboBox.fill = GridBagConstraints.HORIZONTAL;
        gbc_assignmentSubTypeComboBox.gridx = 2;
        gbc_assignmentSubTypeComboBox.gridy = 3;
        assignmentTypePane.add(assignmentSubTypeComboBox, gbc_assignmentSubTypeComboBox);
        
        supervisorInfo = new JLabel("Supervisor/Acting Supervisor");
        GridBagConstraints gbc_supervisorInfo = new GridBagConstraints();
        gbc_supervisorInfo.insets = new Insets(0, 0, 5, 5);
        gbc_supervisorInfo.gridx = 1;
        gbc_supervisorInfo.gridy = 4;
        assignmentTypePane.add(supervisorInfo, gbc_supervisorInfo);
        
        selectSuperCheckBox = new JCheckBox("Manually Select Supervisor");
        GridBagConstraints gbc_selectSuperCheckBox = new GridBagConstraints();
        gbc_selectSuperCheckBox.anchor = GridBagConstraints.WEST;
        gbc_selectSuperCheckBox.gridwidth = 2;
        gbc_selectSuperCheckBox.insets = new Insets(0, 0, 5, 5);
        gbc_selectSuperCheckBox.gridx = 2;
        gbc_selectSuperCheckBox.gridy = 4;
        assignmentTypePane.add(selectSuperCheckBox, gbc_selectSuperCheckBox);
        
        wrStartDateLabel = new JLabel("Work Order Start Date");
        GridBagConstraints gbc_wrStartDateLabel = new GridBagConstraints();
        gbc_wrStartDateLabel.anchor = GridBagConstraints.EAST;
        gbc_wrStartDateLabel.insets = new Insets(0, 0, 5, 5);
        gbc_wrStartDateLabel.gridx = 1;
        gbc_wrStartDateLabel.gridy = 5;
        assignmentTypePane.add(wrStartDateLabel, gbc_wrStartDateLabel);
        
        textField_4 = new JTextField();
        GridBagConstraints gbc_textField_4 = new GridBagConstraints();
        gbc_textField_4.gridwidth = 3;
        gbc_textField_4.insets = new Insets(0, 0, 5, 5);
        gbc_textField_4.fill = GridBagConstraints.HORIZONTAL;
        gbc_textField_4.gridx = 2;
        gbc_textField_4.gridy = 5;
        assignmentTypePane.add(textField_4, gbc_textField_4);
        textField_4.setColumns(10);
        
        wrCompletionDateLabel = new JLabel("Work Order Completion Date");
        GridBagConstraints gbc_wrCompletionDateLabel = new GridBagConstraints();
        gbc_wrCompletionDateLabel.anchor = GridBagConstraints.EAST;
        gbc_wrCompletionDateLabel.insets = new Insets(0, 0, 5, 5);
        gbc_wrCompletionDateLabel.gridx = 1;
        gbc_wrCompletionDateLabel.gridy = 6;
        assignmentTypePane.add(wrCompletionDateLabel, gbc_wrCompletionDateLabel);
        
        textField_5 = new JTextField();
        textField_5.setColumns(10);
        GridBagConstraints gbc_textField_5 = new GridBagConstraints();
        gbc_textField_5.gridwidth = 3;
        gbc_textField_5.insets = new Insets(0, 0, 5, 5);
        gbc_textField_5.fill = GridBagConstraints.HORIZONTAL;
        gbc_textField_5.gridx = 2;
        gbc_textField_5.gridy = 6;
        assignmentTypePane.add(textField_5, gbc_textField_5);
        
        comInDRChecksLabel = new JLabel("Comments in DrChecks");
        GridBagConstraints gbc_comInDRChecksLabel = new GridBagConstraints();
        gbc_comInDRChecksLabel.insets = new Insets(0, 0, 5, 5);
        gbc_comInDRChecksLabel.gridx = 1;
        gbc_comInDRChecksLabel.gridy = 7;
        assignmentTypePane.add(comInDRChecksLabel, gbc_comInDRChecksLabel);
        
        drChecks_YesRadioBtn = new JRadioButton("Yes");
        GridBagConstraints gbc_drChecks_YesRadioBtn = new GridBagConstraints();
        gbc_drChecks_YesRadioBtn.anchor = GridBagConstraints.WEST;
        gbc_drChecks_YesRadioBtn.insets = new Insets(0, 0, 5, 5);
        gbc_drChecks_YesRadioBtn.gridx = 2;
        gbc_drChecks_YesRadioBtn.gridy = 7;
        assignmentTypePane.add(drChecks_YesRadioBtn, gbc_drChecks_YesRadioBtn);
        
        drChecks_NoRadioBtn = new JRadioButton("No");
        GridBagConstraints gbc_drChecks_NoRadioBtn = new GridBagConstraints();
        gbc_drChecks_NoRadioBtn.anchor = GridBagConstraints.WEST;
        gbc_drChecks_NoRadioBtn.insets = new Insets(0, 0, 5, 5);
        gbc_drChecks_NoRadioBtn.gridx = 3;
        gbc_drChecks_NoRadioBtn.gridy = 7;
        assignmentTypePane.add(drChecks_NoRadioBtn, gbc_drChecks_NoRadioBtn);
        
        docHandlingLabel = new JLabel("Document Handling After Completion");
        GridBagConstraints gbc_docHandlingLabel = new GridBagConstraints();
        gbc_docHandlingLabel.insets = new Insets(0, 0, 5, 5);
        gbc_docHandlingLabel.gridx = 1;
        gbc_docHandlingLabel.gridy = 8;
        assignmentTypePane.add(docHandlingLabel, gbc_docHandlingLabel);
        
        rdbtnNewRadioButton = new JRadioButton("New radio button");
        GridBagConstraints gbc_rdbtnNewRadioButton = new GridBagConstraints();
        gbc_rdbtnNewRadioButton.anchor = GridBagConstraints.WEST;
        gbc_rdbtnNewRadioButton.insets = new Insets(0, 0, 5, 5);
        gbc_rdbtnNewRadioButton.gridx = 2;
        gbc_rdbtnNewRadioButton.gridy = 8;
        assignmentTypePane.add(rdbtnNewRadioButton, gbc_rdbtnNewRadioButton);
        
        rdbtnNewRadioButton_1 = new JRadioButton("New radio button");
        GridBagConstraints gbc_rdbtnNewRadioButton_1 = new GridBagConstraints();
        gbc_rdbtnNewRadioButton_1.anchor = GridBagConstraints.WEST;
        gbc_rdbtnNewRadioButton_1.insets = new Insets(0, 0, 5, 5);
        gbc_rdbtnNewRadioButton_1.gridx = 3;
        gbc_rdbtnNewRadioButton_1.gridy = 8;
        assignmentTypePane.add(rdbtnNewRadioButton_1, gbc_rdbtnNewRadioButton_1);
        
        rdbtnNewRadioButton_2 = new JRadioButton("New radio button");
        GridBagConstraints gbc_rdbtnNewRadioButton_2 = new GridBagConstraints();
        gbc_rdbtnNewRadioButton_2.anchor = GridBagConstraints.WEST;
        gbc_rdbtnNewRadioButton_2.insets = new Insets(0, 0, 5, 5);
        gbc_rdbtnNewRadioButton_2.gridx = 4;
        gbc_rdbtnNewRadioButton_2.gridy = 8;
        assignmentTypePane.add(rdbtnNewRadioButton_2, gbc_rdbtnNewRadioButton_2);
        
        addtionalInfoLabel = new JLabel("Background/Additional Information");
        GridBagConstraints gbc_addtionalInfoLabel = new GridBagConstraints();
        gbc_addtionalInfoLabel.insets = new Insets(0, 0, 0, 5);
        gbc_addtionalInfoLabel.gridx = 1;
        gbc_addtionalInfoLabel.gridy = 9;
        assignmentTypePane.add(addtionalInfoLabel, gbc_addtionalInfoLabel);
        
        scrollPane_2 = new JScrollPane();
        GridBagConstraints gbc_scrollPane_2 = new GridBagConstraints();
        gbc_scrollPane_2.fill = GridBagConstraints.BOTH;
        gbc_scrollPane_2.gridwidth = 3;
        gbc_scrollPane_2.insets = new Insets(0, 0, 0, 5);
        gbc_scrollPane_2.gridx = 2;
        gbc_scrollPane_2.gridy = 9;
        assignmentTypePane.add(scrollPane_2, gbc_scrollPane_2);
        
        additionalInfoTextArea = new JTextArea();
        scrollPane_2.setViewportView(additionalInfoTextArea);
        
        costDistroPane = new JPanel();
        newWorkRequestPane.addTab("Cost Distribution", null, costDistroPane, null);   	
        GridBagLayout gbl_costDistroPane = new GridBagLayout();
        gbl_costDistroPane.columnWidths = new int[]{0, 202, 147, 159, 0, 0, 0};
        gbl_costDistroPane.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        gbl_costDistroPane.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 1.0, 0.0, Double.MIN_VALUE};
        gbl_costDistroPane.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
        costDistroPane.setLayout(gbl_costDistroPane);
        
        costDistroContPane = new JPanel();
        newWorkRequestPane.addTab("Cost Distribution Cont.", null, costDistroContPane, null);
        costDistroContPane.setLayout(new BoxLayout(costDistroContPane, BoxLayout.Y_AXIS));
        
        otherDiscplinesPane = new JPanel();
        costDistroContPane.add(otherDiscplinesPane);
        GridBagLayout gbl_otherDiscplinesPane = new GridBagLayout();
        gbl_otherDiscplinesPane.columnWidths = new int[]{0, 129, 0, 0};
        gbl_otherDiscplinesPane.rowHeights = new int[]{24, 0, 0, 0};
        gbl_otherDiscplinesPane.columnWeights = new double[]{0.0, 1.0, 0.0, Double.MIN_VALUE};
        gbl_otherDiscplinesPane.rowWeights = new double[]{0.0, 1.0, 0.0, Double.MIN_VALUE};
        otherDiscplinesPane.setLayout(gbl_otherDiscplinesPane);
        
        horizontalStrut_13 = Box.createHorizontalStrut(20);
        GridBagConstraints gbc_horizontalStrut_13 = new GridBagConstraints();
        gbc_horizontalStrut_13.insets = new Insets(0, 0, 5, 5);
        gbc_horizontalStrut_13.gridx = 0;
        gbc_horizontalStrut_13.gridy = 0;
        otherDiscplinesPane.add(horizontalStrut_13, gbc_horizontalStrut_13);
        
        otherDiscLabel = new JLabel("Other Disciplines Required:");
        GridBagConstraints gbc_otherDiscLabel = new GridBagConstraints();
        gbc_otherDiscLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_otherDiscLabel.insets = new Insets(0, 0, 5, 5);
        gbc_otherDiscLabel.gridx = 1;
        gbc_otherDiscLabel.gridy = 0;
        otherDiscplinesPane.add(otherDiscLabel, gbc_otherDiscLabel);
        
        horizontalStrut_14 = Box.createHorizontalStrut(20);
        GridBagConstraints gbc_horizontalStrut_14 = new GridBagConstraints();
        gbc_horizontalStrut_14.insets = new Insets(0, 0, 5, 0);
        gbc_horizontalStrut_14.gridx = 2;
        gbc_horizontalStrut_14.gridy = 0;
        otherDiscplinesPane.add(horizontalStrut_14, gbc_horizontalStrut_14);
        
        scrollPane_3 = new JScrollPane();
        GridBagConstraints gbc_scrollPane_3 = new GridBagConstraints();
        gbc_scrollPane_3.gridheight = 2;
        gbc_scrollPane_3.insets = new Insets(0, 0, 5, 5);
        gbc_scrollPane_3.fill = GridBagConstraints.BOTH;
        gbc_scrollPane_3.gridx = 1;
        gbc_scrollPane_3.gridy = 1;
        otherDiscplinesPane.add(scrollPane_3, gbc_scrollPane_3);
        
        otherDisciplineTextArea = new JTextArea();
        scrollPane_3.setViewportView(otherDisciplineTextArea);
        
        verticalStrut_9 = Box.createVerticalStrut(20);
        GridBagConstraints gbc_verticalStrut_9 = new GridBagConstraints();
        gbc_verticalStrut_9.insets = new Insets(0, 0, 5, 0);
        gbc_verticalStrut_9.gridx = 2;
        gbc_verticalStrut_9.gridy = 1;
        otherDiscplinesPane.add(verticalStrut_9, gbc_verticalStrut_9);
        
        verticalStrut_10 = Box.createVerticalStrut(20);
        GridBagConstraints gbc_verticalStrut_10 = new GridBagConstraints();
        gbc_verticalStrut_10.gridx = 2;
        gbc_verticalStrut_10.gridy = 2;
        otherDiscplinesPane.add(verticalStrut_10, gbc_verticalStrut_10);
        
        contDiscPane = new JPanel();
        costDistroContPane.add(contDiscPane);
        GridBagLayout gbl_contDiscPane = new GridBagLayout();
        gbl_contDiscPane.columnWidths = new int[]{0, 0, 240, 229, 0, 0, 0};
        gbl_contDiscPane.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0};
        gbl_contDiscPane.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 1.0, 0.0, Double.MIN_VALUE};
        gbl_contDiscPane.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
        contDiscPane.setLayout(gbl_contDiscPane);
        
        horizontalStrut_10 = Box.createHorizontalStrut(20);
        GridBagConstraints gbc_horizontalStrut_10 = new GridBagConstraints();
        gbc_horizontalStrut_10.insets = new Insets(0, 0, 5, 5);
        gbc_horizontalStrut_10.gridx = 0;
        gbc_horizontalStrut_10.gridy = 0;
        contDiscPane.add(horizontalStrut_10, gbc_horizontalStrut_10);
        
        p2NumDispLabel = new JLabel("P2 Number");
        GridBagConstraints gbc_p2NumDispLabel = new GridBagConstraints();
        gbc_p2NumDispLabel.anchor = GridBagConstraints.EAST;
        gbc_p2NumDispLabel.insets = new Insets(0, 0, 5, 5);
        gbc_p2NumDispLabel.gridx = 1;
        gbc_p2NumDispLabel.gridy = 0;
        contDiscPane.add(p2NumDispLabel, gbc_p2NumDispLabel);
        
        p2NumberTextField = new JTextField();
        GridBagConstraints gbc_p2NumberTextField = new GridBagConstraints();
        gbc_p2NumberTextField.insets = new Insets(0, 0, 5, 5);
        gbc_p2NumberTextField.fill = GridBagConstraints.HORIZONTAL;
        gbc_p2NumberTextField.gridx = 2;
        gbc_p2NumberTextField.gridy = 0;
        contDiscPane.add(p2NumberTextField, gbc_p2NumberTextField);
        p2NumberTextField.setColumns(10);
        
        totalCostLabel = new JLabel("Total Costs from Discplines");
        GridBagConstraints gbc_totalCostLabel = new GridBagConstraints();
        gbc_totalCostLabel.anchor = GridBagConstraints.EAST;
        gbc_totalCostLabel.insets = new Insets(0, 0, 5, 5);
        gbc_totalCostLabel.gridx = 3;
        gbc_totalCostLabel.gridy = 0;
        contDiscPane.add(totalCostLabel, gbc_totalCostLabel);
        
        totalCostOtherDisplinesTextField = new JTextField();
        totalCostOtherDisplinesTextField.setColumns(10);
        GridBagConstraints gbc_totalCostOtherDisplinesTextField = new GridBagConstraints();
        gbc_totalCostOtherDisplinesTextField.insets = new Insets(0, 0, 5, 5);
        gbc_totalCostOtherDisplinesTextField.fill = GridBagConstraints.HORIZONTAL;
        gbc_totalCostOtherDisplinesTextField.gridx = 4;
        gbc_totalCostOtherDisplinesTextField.gridy = 0;
        contDiscPane.add(totalCostOtherDisplinesTextField, gbc_totalCostOtherDisplinesTextField);
        
        horizontalStrut_11 = Box.createHorizontalStrut(20);
        GridBagConstraints gbc_horizontalStrut_11 = new GridBagConstraints();
        gbc_horizontalStrut_11.insets = new Insets(0, 0, 5, 0);
        gbc_horizontalStrut_11.gridx = 5;
        gbc_horizontalStrut_11.gridy = 0;
        contDiscPane.add(horizontalStrut_11, gbc_horizontalStrut_11);
        
        laborChargeCodeLabel = new JLabel("Labor Charge Code");
        GridBagConstraints gbc_laborChargeCodeLabel = new GridBagConstraints();
        gbc_laborChargeCodeLabel.anchor = GridBagConstraints.EAST;
        gbc_laborChargeCodeLabel.insets = new Insets(0, 0, 5, 5);
        gbc_laborChargeCodeLabel.gridx = 1;
        gbc_laborChargeCodeLabel.gridy = 1;
        contDiscPane.add(laborChargeCodeLabel, gbc_laborChargeCodeLabel);
        
        laborChargeCodeTextField = new JTextField();
        laborChargeCodeTextField.setColumns(10);
        GridBagConstraints gbc_laborChargeCodeTextField = new GridBagConstraints();
        gbc_laborChargeCodeTextField.insets = new Insets(0, 0, 5, 5);
        gbc_laborChargeCodeTextField.fill = GridBagConstraints.HORIZONTAL;
        gbc_laborChargeCodeTextField.gridx = 2;
        gbc_laborChargeCodeTextField.gridy = 1;
        contDiscPane.add(laborChargeCodeTextField, gbc_laborChargeCodeTextField);
        
        fixedFeeLabel = new JLabel("Fixed Fee Assignment Type");
        GridBagConstraints gbc_fixedFeeLabel = new GridBagConstraints();
        gbc_fixedFeeLabel.anchor = GridBagConstraints.EAST;
        gbc_fixedFeeLabel.insets = new Insets(0, 0, 5, 5);
        gbc_fixedFeeLabel.gridx = 3;
        gbc_fixedFeeLabel.gridy = 1;
        contDiscPane.add(fixedFeeLabel, gbc_fixedFeeLabel);
        
        fixedFeeAssignTypeTextField = new JTextField();
        fixedFeeAssignTypeTextField.setColumns(10);
        GridBagConstraints gbc_fixedFeeAssignTypeTextField = new GridBagConstraints();
        gbc_fixedFeeAssignTypeTextField.insets = new Insets(0, 0, 5, 5);
        gbc_fixedFeeAssignTypeTextField.fill = GridBagConstraints.HORIZONTAL;
        gbc_fixedFeeAssignTypeTextField.gridx = 4;
        gbc_fixedFeeAssignTypeTextField.gridy = 1;
        contDiscPane.add(fixedFeeAssignTypeTextField, gbc_fixedFeeAssignTypeTextField);
        
        fundedWRLabel = new JLabel("Funded Work Item");
        GridBagConstraints gbc_fundedWRLabel = new GridBagConstraints();
        gbc_fundedWRLabel.anchor = GridBagConstraints.EAST;
        gbc_fundedWRLabel.insets = new Insets(0, 0, 5, 5);
        gbc_fundedWRLabel.gridx = 1;
        gbc_fundedWRLabel.gridy = 2;
        contDiscPane.add(fundedWRLabel, gbc_fundedWRLabel);
        
        fundedWorkItemTextField = new JTextField();
        fundedWorkItemTextField.setColumns(10);
        GridBagConstraints gbc_fundedWorkItemTextField = new GridBagConstraints();
        gbc_fundedWorkItemTextField.insets = new Insets(0, 0, 5, 5);
        gbc_fundedWorkItemTextField.fill = GridBagConstraints.HORIZONTAL;
        gbc_fundedWorkItemTextField.gridx = 2;
        gbc_fundedWorkItemTextField.gridy = 2;
        contDiscPane.add(fundedWorkItemTextField, gbc_fundedWorkItemTextField);
        
        managementCostLabel = new JLabel("Management and Admin Cost");
        GridBagConstraints gbc_managementCostLabel = new GridBagConstraints();
        gbc_managementCostLabel.anchor = GridBagConstraints.EAST;
        gbc_managementCostLabel.insets = new Insets(0, 0, 5, 5);
        gbc_managementCostLabel.gridx = 3;
        gbc_managementCostLabel.gridy = 2;
        contDiscPane.add(managementCostLabel, gbc_managementCostLabel);
        
        managementCostTextField = new JTextField();
        managementCostTextField.setColumns(10);
        GridBagConstraints gbc_managementCostTextField = new GridBagConstraints();
        gbc_managementCostTextField.insets = new Insets(0, 0, 5, 5);
        gbc_managementCostTextField.fill = GridBagConstraints.HORIZONTAL;
        gbc_managementCostTextField.gridx = 4;
        gbc_managementCostTextField.gridy = 2;
        contDiscPane.add(managementCostTextField, gbc_managementCostTextField);
        
        lccBalanceLabel = new JLabel("LCC Balance");
        GridBagConstraints gbc_lccBalanceLabel = new GridBagConstraints();
        gbc_lccBalanceLabel.anchor = GridBagConstraints.EAST;
        gbc_lccBalanceLabel.insets = new Insets(0, 0, 5, 5);
        gbc_lccBalanceLabel.gridx = 1;
        gbc_lccBalanceLabel.gridy = 3;
        contDiscPane.add(lccBalanceLabel, gbc_lccBalanceLabel);
        
        lccBalTextField = new JTextField();
        lccBalTextField.setColumns(10);
        GridBagConstraints gbc_lccBalTextField = new GridBagConstraints();
        gbc_lccBalTextField.insets = new Insets(0, 0, 5, 5);
        gbc_lccBalTextField.fill = GridBagConstraints.HORIZONTAL;
        gbc_lccBalTextField.gridx = 2;
        gbc_lccBalTextField.gridy = 3;
        contDiscPane.add(lccBalTextField, gbc_lccBalTextField);
        
        totalLaborCostLabel = new JLabel("Total Labor Cost");
        GridBagConstraints gbc_totalLaborCostLabel = new GridBagConstraints();
        gbc_totalLaborCostLabel.anchor = GridBagConstraints.EAST;
        gbc_totalLaborCostLabel.insets = new Insets(0, 0, 5, 5);
        gbc_totalLaborCostLabel.gridx = 3;
        gbc_totalLaborCostLabel.gridy = 3;
        contDiscPane.add(totalLaborCostLabel, gbc_totalLaborCostLabel);
        
        totalLaborCostTextField = new JTextField();
        totalLaborCostTextField.setColumns(10);
        GridBagConstraints gbc_totalLaborCostTextField = new GridBagConstraints();
        gbc_totalLaborCostTextField.insets = new Insets(0, 0, 5, 5);
        gbc_totalLaborCostTextField.fill = GridBagConstraints.HORIZONTAL;
        gbc_totalLaborCostTextField.gridx = 4;
        gbc_totalLaborCostTextField.gridy = 3;
        contDiscPane.add(totalLaborCostTextField, gbc_totalLaborCostTextField);
        
        fwiBalanceLabel = new JLabel("FWI Balance");
        GridBagConstraints gbc_fwiBalanceLabel = new GridBagConstraints();
        gbc_fwiBalanceLabel.anchor = GridBagConstraints.EAST;
        gbc_fwiBalanceLabel.insets = new Insets(0, 0, 5, 5);
        gbc_fwiBalanceLabel.gridx = 1;
        gbc_fwiBalanceLabel.gridy = 4;
        contDiscPane.add(fwiBalanceLabel, gbc_fwiBalanceLabel);
        
        fwiBalTextArea = new JTextField();
        fwiBalTextArea.setColumns(10);
        GridBagConstraints gbc_fwiBalTextArea = new GridBagConstraints();
        gbc_fwiBalTextArea.insets = new Insets(0, 0, 5, 5);
        gbc_fwiBalTextArea.fill = GridBagConstraints.HORIZONTAL;
        gbc_fwiBalTextArea.gridx = 2;
        gbc_fwiBalTextArea.gridy = 4;
        contDiscPane.add(fwiBalTextArea, gbc_fwiBalTextArea);
        
        orderItemLabel = new JLabel("Order Work Item");
        GridBagConstraints gbc_orderItemLabel = new GridBagConstraints();
        gbc_orderItemLabel.anchor = GridBagConstraints.EAST;
        gbc_orderItemLabel.insets = new Insets(0, 0, 5, 5);
        gbc_orderItemLabel.gridx = 1;
        gbc_orderItemLabel.gridy = 5;
        contDiscPane.add(orderItemLabel, gbc_orderItemLabel);
        
        orderWorkItemTextArea = new JTextField();
        orderWorkItemTextArea.setColumns(10);
        GridBagConstraints gbc_orderWorkItemTextArea = new GridBagConstraints();
        gbc_orderWorkItemTextArea.insets = new Insets(0, 0, 5, 5);
        gbc_orderWorkItemTextArea.fill = GridBagConstraints.HORIZONTAL;
        gbc_orderWorkItemTextArea.gridx = 2;
        gbc_orderWorkItemTextArea.gridy = 5;
        contDiscPane.add(orderWorkItemTextArea, gbc_orderWorkItemTextArea);
        
        issueOrgCodeLabel = new JLabel("Issued to Organziation Code");
        GridBagConstraints gbc_issueOrgCodeLabel = new GridBagConstraints();
        gbc_issueOrgCodeLabel.anchor = GridBagConstraints.EAST;
        gbc_issueOrgCodeLabel.insets = new Insets(0, 0, 5, 5);
        gbc_issueOrgCodeLabel.gridx = 1;
        gbc_issueOrgCodeLabel.gridy = 6;
        contDiscPane.add(issueOrgCodeLabel, gbc_issueOrgCodeLabel);
        
        issuedOrgCodTextArea = new JTextField();
        issuedOrgCodTextArea.setColumns(10);
        GridBagConstraints gbc_issuedOrgCodTextArea = new GridBagConstraints();
        gbc_issuedOrgCodTextArea.insets = new Insets(0, 0, 5, 5);
        gbc_issuedOrgCodTextArea.fill = GridBagConstraints.HORIZONTAL;
        gbc_issuedOrgCodTextArea.gridx = 2;
        gbc_issuedOrgCodTextArea.gridy = 6;
        contDiscPane.add(issuedOrgCodTextArea, gbc_issuedOrgCodTextArea);
        
        verticalGlue = Box.createVerticalGlue();
        GridBagConstraints gbc_verticalGlue = new GridBagConstraints();
        gbc_verticalGlue.insets = new Insets(0, 0, 0, 5);
        gbc_verticalGlue.gridx = 1;
        gbc_verticalGlue.gridy = 7;
        contDiscPane.add(verticalGlue, gbc_verticalGlue);
        
        travelInfoPane = new JPanel();
        costDistroContPane.add(travelInfoPane);
        GridBagLayout gbl_travelInfoPane = new GridBagLayout();
        gbl_travelInfoPane.columnWidths = new int[]{145, 413, 119, 0, 0, 0};
        gbl_travelInfoPane.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0};
        gbl_travelInfoPane.columnWeights = new double[]{0.0, 1.0, 0.0, 1.0, 0.0, Double.MIN_VALUE};
        gbl_travelInfoPane.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
        travelInfoPane.setLayout(gbl_travelInfoPane);
        
        travelCatLabel = new JLabel("Transportation");
        travelCatLabel.setHorizontalAlignment(SwingConstants.CENTER);
        travelCatLabel.setOpaque(true);
        travelCatLabel.setForeground(new Color(255, 255, 255));
        travelCatLabel.setBackground(new Color(153, 153, 153));
        GridBagConstraints gbc_travelCatLabel = new GridBagConstraints();
        gbc_travelCatLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_travelCatLabel.insets = new Insets(0, 0, 5, 5);
        gbc_travelCatLabel.gridx = 0;
        gbc_travelCatLabel.gridy = 0;
        travelInfoPane.add(travelCatLabel, gbc_travelCatLabel);
        
        specificTravelIndoLabel = new JLabel("Specific Travel Info");
        specificTravelIndoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        specificTravelIndoLabel.setOpaque(true);
        specificTravelIndoLabel.setForeground(new Color(255, 255, 255));
        specificTravelIndoLabel.setBackground(new Color(153, 153, 153));
        GridBagConstraints gbc_specificTravelIndoLabel = new GridBagConstraints();
        gbc_specificTravelIndoLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_specificTravelIndoLabel.insets = new Insets(0, 0, 5, 5);
        gbc_specificTravelIndoLabel.gridx = 1;
        gbc_specificTravelIndoLabel.gridy = 0;
        travelInfoPane.add(specificTravelIndoLabel, gbc_specificTravelIndoLabel);
        
        allowCostLabel = new JLabel("Allowed Cost");
        allowCostLabel.setHorizontalAlignment(SwingConstants.CENTER);
        allowCostLabel.setOpaque(true);
        allowCostLabel.setForeground(new Color(255, 255, 255));
        allowCostLabel.setBackground(new Color(153, 153, 153));
        GridBagConstraints gbc_allowCostLabel = new GridBagConstraints();
        gbc_allowCostLabel.gridwidth = 2;
        gbc_allowCostLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_allowCostLabel.insets = new Insets(0, 0, 5, 5);
        gbc_allowCostLabel.gridx = 2;
        gbc_allowCostLabel.gridy = 0;
        travelInfoPane.add(allowCostLabel, gbc_allowCostLabel);
        
        horizontalStrut_12 = Box.createHorizontalStrut(20);
        GridBagConstraints gbc_horizontalStrut_12 = new GridBagConstraints();
        gbc_horizontalStrut_12.insets = new Insets(0, 0, 5, 0);
        gbc_horizontalStrut_12.gridx = 4;
        gbc_horizontalStrut_12.gridy = 0;
        travelInfoPane.add(horizontalStrut_12, gbc_horizontalStrut_12);
        
        transLabel = new JLabel("Transportation");
        GridBagConstraints gbc_transLabel = new GridBagConstraints();
        gbc_transLabel.gridheight = 2;
        gbc_transLabel.insets = new Insets(0, 0, 5, 5);
        gbc_transLabel.gridx = 0;
        gbc_transLabel.gridy = 1;
        travelInfoPane.add(transLabel, gbc_transLabel);
        
        scrollPane_6 = new JScrollPane();
        GridBagConstraints gbc_scrollPane_6 = new GridBagConstraints();
        gbc_scrollPane_6.fill = GridBagConstraints.BOTH;
        gbc_scrollPane_6.gridheight = 2;
        gbc_scrollPane_6.insets = new Insets(0, 0, 5, 5);
        gbc_scrollPane_6.gridx = 1;
        gbc_scrollPane_6.gridy = 1;
        travelInfoPane.add(scrollPane_6, gbc_scrollPane_6);
        
        transportationTextArea = new JTextArea();
        scrollPane_6.setViewportView(transportationTextArea);
        
        dollarLabel = new JLabel("$");
        GridBagConstraints gbc_dollarLabel = new GridBagConstraints();
        gbc_dollarLabel.insets = new Insets(0, 0, 5, 5);
        gbc_dollarLabel.anchor = GridBagConstraints.EAST;
        gbc_dollarLabel.gridx = 2;
        gbc_dollarLabel.gridy = 1;
        travelInfoPane.add(dollarLabel, gbc_dollarLabel);
        
        travelTransportationSpinBox = new JSpinner();
        GridBagConstraints gbc_travelTransportationSpinBox = new GridBagConstraints();
        gbc_travelTransportationSpinBox.insets = new Insets(0, 0, 5, 5);
        gbc_travelTransportationSpinBox.fill = GridBagConstraints.HORIZONTAL;
        gbc_travelTransportationSpinBox.gridx = 3;
        gbc_travelTransportationSpinBox.gridy = 1;
        travelInfoPane.add(travelTransportationSpinBox, gbc_travelTransportationSpinBox);
        
        prcLabel = new JLabel("PR&C");
        GridBagConstraints gbc_prcLabel = new GridBagConstraints();
        gbc_prcLabel.insets = new Insets(0, 0, 5, 5);
        gbc_prcLabel.anchor = GridBagConstraints.EAST;
        gbc_prcLabel.gridx = 2;
        gbc_prcLabel.gridy = 2;
        travelInfoPane.add(prcLabel, gbc_prcLabel);
        
        travelTransportationPRCSpinBox = new JTextField();
        travelTransportationPRCSpinBox.setColumns(10);
        GridBagConstraints gbc_travelTransportationPRCSpinBox = new GridBagConstraints();
        gbc_travelTransportationPRCSpinBox.insets = new Insets(0, 0, 5, 5);
        gbc_travelTransportationPRCSpinBox.fill = GridBagConstraints.HORIZONTAL;
        gbc_travelTransportationPRCSpinBox.gridx = 3;
        gbc_travelTransportationPRCSpinBox.gridy = 2;
        travelInfoPane.add(travelTransportationPRCSpinBox, gbc_travelTransportationPRCSpinBox);
        
        perDiemLabel = new JLabel("Per Diem");
        GridBagConstraints gbc_perDiemLabel = new GridBagConstraints();
        gbc_perDiemLabel.gridheight = 2;
        gbc_perDiemLabel.insets = new Insets(0, 0, 5, 5);
        gbc_perDiemLabel.gridx = 0;
        gbc_perDiemLabel.gridy = 3;
        travelInfoPane.add(perDiemLabel, gbc_perDiemLabel);
        
        scrollPane_4 = new JScrollPane();
        GridBagConstraints gbc_scrollPane_4 = new GridBagConstraints();
        gbc_scrollPane_4.fill = GridBagConstraints.BOTH;
        gbc_scrollPane_4.gridheight = 2;
        gbc_scrollPane_4.insets = new Insets(0, 0, 5, 5);
        gbc_scrollPane_4.gridx = 1;
        gbc_scrollPane_4.gridy = 3;
        travelInfoPane.add(scrollPane_4, gbc_scrollPane_4);
        
        perDiemTextArea = new JTextArea();
        scrollPane_4.setViewportView(perDiemTextArea);
        
        dollar2Label = new JLabel("$");
        GridBagConstraints gbc_dollar2Label = new GridBagConstraints();
        gbc_dollar2Label.gridheight = 2;
        gbc_dollar2Label.insets = new Insets(0, 0, 5, 5);
        gbc_dollar2Label.anchor = GridBagConstraints.EAST;
        gbc_dollar2Label.gridx = 2;
        gbc_dollar2Label.gridy = 3;
        travelInfoPane.add(dollar2Label, gbc_dollar2Label);
        
        travelPerDiemSpinBox = new JSpinner();
        GridBagConstraints gbc_travelPerDiemSpinBox = new GridBagConstraints();
        gbc_travelPerDiemSpinBox.gridheight = 2;
        gbc_travelPerDiemSpinBox.insets = new Insets(0, 0, 5, 5);
        gbc_travelPerDiemSpinBox.fill = GridBagConstraints.HORIZONTAL;
        gbc_travelPerDiemSpinBox.gridx = 3;
        gbc_travelPerDiemSpinBox.gridy = 3;
        travelInfoPane.add(travelPerDiemSpinBox, gbc_travelPerDiemSpinBox);
        
        verticalStrut_4 = Box.createVerticalStrut(20);
        GridBagConstraints gbc_verticalStrut_4 = new GridBagConstraints();
        gbc_verticalStrut_4.insets = new Insets(0, 0, 5, 0);
        gbc_verticalStrut_4.gridx = 4;
        gbc_verticalStrut_4.gridy = 3;
        travelInfoPane.add(verticalStrut_4, gbc_verticalStrut_4);
        
        verticalStrut_7 = Box.createVerticalStrut(20);
        GridBagConstraints gbc_verticalStrut_7 = new GridBagConstraints();
        gbc_verticalStrut_7.insets = new Insets(0, 0, 5, 0);
        gbc_verticalStrut_7.gridx = 4;
        gbc_verticalStrut_7.gridy = 4;
        travelInfoPane.add(verticalStrut_7, gbc_verticalStrut_7);
        
        travOtherLabel = new JLabel("Other");
        GridBagConstraints gbc_travOtherLabel = new GridBagConstraints();
        gbc_travOtherLabel.gridheight = 2;
        gbc_travOtherLabel.insets = new Insets(0, 0, 5, 5);
        gbc_travOtherLabel.gridx = 0;
        gbc_travOtherLabel.gridy = 5;
        travelInfoPane.add(travOtherLabel, gbc_travOtherLabel);
        
        scrollPane_5 = new JScrollPane();
        GridBagConstraints gbc_scrollPane_5 = new GridBagConstraints();
        gbc_scrollPane_5.fill = GridBagConstraints.BOTH;
        gbc_scrollPane_5.gridheight = 2;
        gbc_scrollPane_5.insets = new Insets(0, 0, 5, 5);
        gbc_scrollPane_5.gridx = 1;
        gbc_scrollPane_5.gridy = 5;
        travelInfoPane.add(scrollPane_5, gbc_scrollPane_5);
        
        otherTravelTextArea = new JTextArea();
        scrollPane_5.setViewportView(otherTravelTextArea);
        
        dollar3Label = new JLabel("$");
        GridBagConstraints gbc_dollar3Label = new GridBagConstraints();
        gbc_dollar3Label.gridheight = 2;
        gbc_dollar3Label.insets = new Insets(0, 0, 5, 5);
        gbc_dollar3Label.anchor = GridBagConstraints.EAST;
        gbc_dollar3Label.gridx = 2;
        gbc_dollar3Label.gridy = 5;
        travelInfoPane.add(dollar3Label, gbc_dollar3Label);
        
        travelOtherSpinBox = new JSpinner();
        GridBagConstraints gbc_travelOtherSpinBox = new GridBagConstraints();
        gbc_travelOtherSpinBox.gridheight = 2;
        gbc_travelOtherSpinBox.insets = new Insets(0, 0, 5, 5);
        gbc_travelOtherSpinBox.fill = GridBagConstraints.HORIZONTAL;
        gbc_travelOtherSpinBox.gridx = 3;
        gbc_travelOtherSpinBox.gridy = 5;
        travelInfoPane.add(travelOtherSpinBox, gbc_travelOtherSpinBox);
        
        verticalStrut_5 = Box.createVerticalStrut(20);
        GridBagConstraints gbc_verticalStrut_5 = new GridBagConstraints();
        gbc_verticalStrut_5.insets = new Insets(0, 0, 5, 0);
        gbc_verticalStrut_5.gridx = 4;
        gbc_verticalStrut_5.gridy = 5;
        travelInfoPane.add(verticalStrut_5, gbc_verticalStrut_5);
        
        verticalStrut_8 = Box.createVerticalStrut(20);
        GridBagConstraints gbc_verticalStrut_8 = new GridBagConstraints();
        gbc_verticalStrut_8.insets = new Insets(0, 0, 5, 0);
        gbc_verticalStrut_8.gridx = 4;
        gbc_verticalStrut_8.gridy = 6;
        travelInfoPane.add(verticalStrut_8, gbc_verticalStrut_8);
        
        travelCostLabel = new JLabel("Total Travel Cost");
        GridBagConstraints gbc_travelCostLabel = new GridBagConstraints();
        gbc_travelCostLabel.anchor = GridBagConstraints.EAST;
        gbc_travelCostLabel.insets = new Insets(0, 0, 0, 5);
        gbc_travelCostLabel.gridx = 2;
        gbc_travelCostLabel.gridy = 7;
        travelInfoPane.add(travelCostLabel, gbc_travelCostLabel);
        
        travelCostTextField = new JTextField();
        travelCostTextField.setColumns(10);
        GridBagConstraints gbc_travelCostTextField = new GridBagConstraints();
        gbc_travelCostTextField.insets = new Insets(0, 0, 0, 5);
        gbc_travelCostTextField.fill = GridBagConstraints.HORIZONTAL;
        gbc_travelCostTextField.gridx = 3;
        gbc_travelCostTextField.gridy = 7;
        travelInfoPane.add(travelCostTextField, gbc_travelCostTextField);
        
        horizontalStrut_8 = Box.createHorizontalStrut(20);
        GridBagConstraints gbc_horizontalStrut_8 = new GridBagConstraints();
        gbc_horizontalStrut_8.insets = new Insets(0, 0, 5, 5);
        gbc_horizontalStrut_8.gridx = 0;
        gbc_horizontalStrut_8.gridy = 0;
        costDistroPane.add(horizontalStrut_8, gbc_horizontalStrut_8);
        
        discLabel = new JLabel("Discipline");
        discLabel.setHorizontalAlignment(SwingConstants.CENTER);
        discLabel.setOpaque(true);
        discLabel.setForeground(new Color(255, 255, 255));
        discLabel.setBackground(new Color(153, 153, 153));
        GridBagConstraints gbc_discLabel = new GridBagConstraints();
        gbc_discLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_discLabel.insets = new Insets(0, 0, 5, 5);
        gbc_discLabel.gridx = 1;
        gbc_discLabel.gridy = 0;
        costDistroPane.add(discLabel, gbc_discLabel);
        
        directTaskLabel = new JLabel("Direct Task Hours");
        directTaskLabel.setHorizontalAlignment(SwingConstants.CENTER);
        directTaskLabel.setForeground(new Color(255, 255, 255));
        directTaskLabel.setOpaque(true);
        directTaskLabel.setBackground(new Color(153, 153, 153));
        GridBagConstraints gbc_directTaskLabel = new GridBagConstraints();
        gbc_directTaskLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_directTaskLabel.insets = new Insets(0, 0, 5, 5);
        gbc_directTaskLabel.gridx = 2;
        gbc_directTaskLabel.gridy = 0;
        costDistroPane.add(directTaskLabel, gbc_directTaskLabel);
        
        costLabel = new JLabel("Costs");
        costLabel.setHorizontalAlignment(SwingConstants.CENTER);
        costLabel.setForeground(new Color(255, 255, 255));
        costLabel.setOpaque(true);
        costLabel.setBackground(new Color(153, 153, 153));
        GridBagConstraints gbc_costLabel = new GridBagConstraints();
        gbc_costLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_costLabel.insets = new Insets(0, 0, 5, 5);
        gbc_costLabel.gridx = 3;
        gbc_costLabel.gridy = 0;
        costDistroPane.add(costLabel, gbc_costLabel);
        
        assigneePrevWRLabel = new JLabel("Assignee From Previous WR");
        assigneePrevWRLabel.setHorizontalAlignment(SwingConstants.CENTER);
        assigneePrevWRLabel.setForeground(new Color(255, 255, 255));
        assigneePrevWRLabel.setOpaque(true);
        assigneePrevWRLabel.setBackground(new Color(153, 153, 153));
        GridBagConstraints gbc_assigneePrevWRLabel = new GridBagConstraints();
        gbc_assigneePrevWRLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_assigneePrevWRLabel.insets = new Insets(0, 0, 5, 5);
        gbc_assigneePrevWRLabel.gridx = 4;
        gbc_assigneePrevWRLabel.gridy = 0;
        costDistroPane.add(assigneePrevWRLabel, gbc_assigneePrevWRLabel);
        
        horizontalStrut_9 = Box.createHorizontalStrut(20);
        GridBagConstraints gbc_horizontalStrut_9 = new GridBagConstraints();
        gbc_horizontalStrut_9.insets = new Insets(0, 0, 5, 0);
        gbc_horizontalStrut_9.gridx = 5;
        gbc_horizontalStrut_9.gridy = 0;
        costDistroPane.add(horizontalStrut_9, gbc_horizontalStrut_9);
        
        civilLabel = new JLabel("Civil");
        GridBagConstraints gbc_civilLabel = new GridBagConstraints();
        gbc_civilLabel.insets = new Insets(0, 0, 5, 5);
        gbc_civilLabel.gridx = 1;
        gbc_civilLabel.gridy = 1;
        costDistroPane.add(civilLabel, gbc_civilLabel);
        
        spinner = new JSpinner();
        GridBagConstraints gbc_spinner = new GridBagConstraints();
        gbc_spinner.fill = GridBagConstraints.HORIZONTAL;
        gbc_spinner.insets = new Insets(0, 0, 5, 5);
        gbc_spinner.gridx = 2;
        gbc_spinner.gridy = 1;
        costDistroPane.add(spinner, gbc_spinner);
        
        civialCostLabel = new JLabel("$0.00");
        civialCostLabel.setHorizontalAlignment(SwingConstants.TRAILING);
        GridBagConstraints gbc_civialCostLabel = new GridBagConstraints();
        gbc_civialCostLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_civialCostLabel.insets = new Insets(0, 0, 5, 5);
        gbc_civialCostLabel.gridx = 3;
        gbc_civialCostLabel.gridy = 1;
        costDistroPane.add(civialCostLabel, gbc_civialCostLabel);
        
        prevCivialCostLabel = new JLabel("- none -");
        GridBagConstraints gbc_prevCivialCostLabel = new GridBagConstraints();
        gbc_prevCivialCostLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_prevCivialCostLabel.insets = new Insets(0, 0, 5, 5);
        gbc_prevCivialCostLabel.gridx = 4;
        gbc_prevCivialCostLabel.gridy = 1;
        costDistroPane.add(prevCivialCostLabel, gbc_prevCivialCostLabel);
        
        archLabel = new JLabel("Architectural");
        GridBagConstraints gbc_archLabel = new GridBagConstraints();
        gbc_archLabel.insets = new Insets(0, 0, 5, 5);
        gbc_archLabel.gridx = 1;
        gbc_archLabel.gridy = 2;
        costDistroPane.add(archLabel, gbc_archLabel);
        
        spinner_1 = new JSpinner();
        GridBagConstraints gbc_spinner_1 = new GridBagConstraints();
        gbc_spinner_1.fill = GridBagConstraints.HORIZONTAL;
        gbc_spinner_1.insets = new Insets(0, 0, 5, 5);
        gbc_spinner_1.gridx = 2;
        gbc_spinner_1.gridy = 2;
        costDistroPane.add(spinner_1, gbc_spinner_1);
        
        archCostLabel = new JLabel("$0.00");
        archCostLabel.setHorizontalAlignment(SwingConstants.TRAILING);
        GridBagConstraints gbc_archCostLabel = new GridBagConstraints();
        gbc_archCostLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_archCostLabel.insets = new Insets(0, 0, 5, 5);
        gbc_archCostLabel.gridx = 3;
        gbc_archCostLabel.gridy = 2;
        costDistroPane.add(archCostLabel, gbc_archCostLabel);
        
        prevArchCostLabel = new JLabel("- none -");
        GridBagConstraints gbc_prevArchCostLabel = new GridBagConstraints();
        gbc_prevArchCostLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_prevArchCostLabel.insets = new Insets(0, 0, 5, 5);
        gbc_prevArchCostLabel.gridx = 4;
        gbc_prevArchCostLabel.gridy = 2;
        costDistroPane.add(prevArchCostLabel, gbc_prevArchCostLabel);
        
        structLabel = new JLabel("Structural");
        GridBagConstraints gbc_structLabel = new GridBagConstraints();
        gbc_structLabel.insets = new Insets(0, 0, 5, 5);
        gbc_structLabel.gridx = 1;
        gbc_structLabel.gridy = 3;
        costDistroPane.add(structLabel, gbc_structLabel);
        
        spinner_2 = new JSpinner();
        GridBagConstraints gbc_spinner_2 = new GridBagConstraints();
        gbc_spinner_2.fill = GridBagConstraints.HORIZONTAL;
        gbc_spinner_2.insets = new Insets(0, 0, 5, 5);
        gbc_spinner_2.gridx = 2;
        gbc_spinner_2.gridy = 3;
        costDistroPane.add(spinner_2, gbc_spinner_2);
        
        strucCostLabel = new JLabel("$0.00");
        strucCostLabel.setHorizontalAlignment(SwingConstants.TRAILING);
        GridBagConstraints gbc_strucCostLabel = new GridBagConstraints();
        gbc_strucCostLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_strucCostLabel.insets = new Insets(0, 0, 5, 5);
        gbc_strucCostLabel.gridx = 3;
        gbc_strucCostLabel.gridy = 3;
        costDistroPane.add(strucCostLabel, gbc_strucCostLabel);
        
        prevStrucCostLabel = new JLabel("- none -");
        GridBagConstraints gbc_prevStrucCostLabel = new GridBagConstraints();
        gbc_prevStrucCostLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_prevStrucCostLabel.insets = new Insets(0, 0, 5, 5);
        gbc_prevStrucCostLabel.gridx = 4;
        gbc_prevStrucCostLabel.gridy = 3;
        costDistroPane.add(prevStrucCostLabel, gbc_prevStrucCostLabel);
        
        antiterrorLabel = new JLabel("Antiterrosim/Force Protection");
        GridBagConstraints gbc_antiterrorLabel = new GridBagConstraints();
        gbc_antiterrorLabel.insets = new Insets(0, 0, 5, 5);
        gbc_antiterrorLabel.gridx = 1;
        gbc_antiterrorLabel.gridy = 4;
        costDistroPane.add(antiterrorLabel, gbc_antiterrorLabel);
        
        spinner_3 = new JSpinner();
        GridBagConstraints gbc_spinner_3 = new GridBagConstraints();
        gbc_spinner_3.fill = GridBagConstraints.HORIZONTAL;
        gbc_spinner_3.insets = new Insets(0, 0, 5, 5);
        gbc_spinner_3.gridx = 2;
        gbc_spinner_3.gridy = 4;
        costDistroPane.add(spinner_3, gbc_spinner_3);
        
        forceProtCostLabel = new JLabel("$0.00");
        forceProtCostLabel.setHorizontalAlignment(SwingConstants.TRAILING);
        GridBagConstraints gbc_forceProtCostLabel = new GridBagConstraints();
        gbc_forceProtCostLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_forceProtCostLabel.insets = new Insets(0, 0, 5, 5);
        gbc_forceProtCostLabel.gridx = 3;
        gbc_forceProtCostLabel.gridy = 4;
        costDistroPane.add(forceProtCostLabel, gbc_forceProtCostLabel);
        
        prevForceProtCostLabel = new JLabel("- none -");
        GridBagConstraints gbc_prevForceProtCostLabel = new GridBagConstraints();
        gbc_prevForceProtCostLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_prevForceProtCostLabel.insets = new Insets(0, 0, 5, 5);
        gbc_prevForceProtCostLabel.gridx = 4;
        gbc_prevForceProtCostLabel.gridy = 4;
        costDistroPane.add(prevForceProtCostLabel, gbc_prevForceProtCostLabel);
        
        mechLabel = new JLabel("Mechanical");
        GridBagConstraints gbc_mechLabel = new GridBagConstraints();
        gbc_mechLabel.insets = new Insets(0, 0, 5, 5);
        gbc_mechLabel.gridx = 1;
        gbc_mechLabel.gridy = 5;
        costDistroPane.add(mechLabel, gbc_mechLabel);
        
        spinner_4 = new JSpinner();
        GridBagConstraints gbc_spinner_4 = new GridBagConstraints();
        gbc_spinner_4.fill = GridBagConstraints.HORIZONTAL;
        gbc_spinner_4.insets = new Insets(0, 0, 5, 5);
        gbc_spinner_4.gridx = 2;
        gbc_spinner_4.gridy = 5;
        costDistroPane.add(spinner_4, gbc_spinner_4);
        
        mechCostLabel = new JLabel("$0.00");
        mechCostLabel.setHorizontalAlignment(SwingConstants.TRAILING);
        GridBagConstraints gbc_mechCostLabel = new GridBagConstraints();
        gbc_mechCostLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_mechCostLabel.insets = new Insets(0, 0, 5, 5);
        gbc_mechCostLabel.gridx = 3;
        gbc_mechCostLabel.gridy = 5;
        costDistroPane.add(mechCostLabel, gbc_mechCostLabel);
        
        prevMechCostLabel = new JLabel("- none -");
        GridBagConstraints gbc_prevMechCostLabel = new GridBagConstraints();
        gbc_prevMechCostLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_prevMechCostLabel.insets = new Insets(0, 0, 5, 5);
        gbc_prevMechCostLabel.gridx = 4;
        gbc_prevMechCostLabel.gridy = 5;
        costDistroPane.add(prevMechCostLabel, gbc_prevMechCostLabel);
        
        fireProtLabel = new JLabel("Fire Protection");
        GridBagConstraints gbc_fireProtLabel = new GridBagConstraints();
        gbc_fireProtLabel.insets = new Insets(0, 0, 5, 5);
        gbc_fireProtLabel.gridx = 1;
        gbc_fireProtLabel.gridy = 6;
        costDistroPane.add(fireProtLabel, gbc_fireProtLabel);
        
        spinner_5 = new JSpinner();
        GridBagConstraints gbc_spinner_5 = new GridBagConstraints();
        gbc_spinner_5.fill = GridBagConstraints.HORIZONTAL;
        gbc_spinner_5.insets = new Insets(0, 0, 5, 5);
        gbc_spinner_5.gridx = 2;
        gbc_spinner_5.gridy = 6;
        costDistroPane.add(spinner_5, gbc_spinner_5);
        
        fireProtCostLabel = new JLabel("$0.00");
        fireProtCostLabel.setHorizontalAlignment(SwingConstants.TRAILING);
        GridBagConstraints gbc_fireProtCostLabel = new GridBagConstraints();
        gbc_fireProtCostLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_fireProtCostLabel.insets = new Insets(0, 0, 5, 5);
        gbc_fireProtCostLabel.gridx = 3;
        gbc_fireProtCostLabel.gridy = 6;
        costDistroPane.add(fireProtCostLabel, gbc_fireProtCostLabel);
        
        prevFireProtCostLabel = new JLabel("- none -");
        GridBagConstraints gbc_prevFireProtCostLabel = new GridBagConstraints();
        gbc_prevFireProtCostLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_prevFireProtCostLabel.insets = new Insets(0, 0, 5, 5);
        gbc_prevFireProtCostLabel.gridx = 4;
        gbc_prevFireProtCostLabel.gridy = 6;
        costDistroPane.add(prevFireProtCostLabel, gbc_prevFireProtCostLabel);
        
        electLabel = new JLabel("Electrical");
        GridBagConstraints gbc_electLabel = new GridBagConstraints();
        gbc_electLabel.insets = new Insets(0, 0, 5, 5);
        gbc_electLabel.gridx = 1;
        gbc_electLabel.gridy = 7;
        costDistroPane.add(electLabel, gbc_electLabel);
        
        spinner_6 = new JSpinner();
        GridBagConstraints gbc_spinner_6 = new GridBagConstraints();
        gbc_spinner_6.fill = GridBagConstraints.HORIZONTAL;
        gbc_spinner_6.insets = new Insets(0, 0, 5, 5);
        gbc_spinner_6.gridx = 2;
        gbc_spinner_6.gridy = 7;
        costDistroPane.add(spinner_6, gbc_spinner_6);
        
        electricalCostLabel = new JLabel("$0.00");
        electricalCostLabel.setHorizontalAlignment(SwingConstants.TRAILING);
        GridBagConstraints gbc_electricalCostLabel = new GridBagConstraints();
        gbc_electricalCostLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_electricalCostLabel.insets = new Insets(0, 0, 5, 5);
        gbc_electricalCostLabel.gridx = 3;
        gbc_electricalCostLabel.gridy = 7;
        costDistroPane.add(electricalCostLabel, gbc_electricalCostLabel);
        
        prevElectricalCostLabel = new JLabel("- none -");
        GridBagConstraints gbc_prevElectricalCostLabel = new GridBagConstraints();
        gbc_prevElectricalCostLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_prevElectricalCostLabel.insets = new Insets(0, 0, 5, 5);
        gbc_prevElectricalCostLabel.gridx = 4;
        gbc_prevElectricalCostLabel.gridy = 7;
        costDistroPane.add(prevElectricalCostLabel, gbc_prevElectricalCostLabel);
        
        comLabel = new JLabel("Communications");
        GridBagConstraints gbc_comLabel = new GridBagConstraints();
        gbc_comLabel.insets = new Insets(0, 0, 5, 5);
        gbc_comLabel.gridx = 1;
        gbc_comLabel.gridy = 8;
        costDistroPane.add(comLabel, gbc_comLabel);
        
        spinner_7 = new JSpinner();
        GridBagConstraints gbc_spinner_7 = new GridBagConstraints();
        gbc_spinner_7.fill = GridBagConstraints.HORIZONTAL;
        gbc_spinner_7.insets = new Insets(0, 0, 5, 5);
        gbc_spinner_7.gridx = 2;
        gbc_spinner_7.gridy = 8;
        costDistroPane.add(spinner_7, gbc_spinner_7);
        
        commsCostLabel = new JLabel("$0.00");
        commsCostLabel.setHorizontalAlignment(SwingConstants.TRAILING);
        GridBagConstraints gbc_commsCostLabel = new GridBagConstraints();
        gbc_commsCostLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_commsCostLabel.insets = new Insets(0, 0, 5, 5);
        gbc_commsCostLabel.gridx = 3;
        gbc_commsCostLabel.gridy = 8;
        costDistroPane.add(commsCostLabel, gbc_commsCostLabel);
        
        prevCommsCostLabel = new JLabel("- none -");
        GridBagConstraints gbc_prevCommsCostLabel = new GridBagConstraints();
        gbc_prevCommsCostLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_prevCommsCostLabel.insets = new Insets(0, 0, 5, 5);
        gbc_prevCommsCostLabel.gridx = 4;
        gbc_prevCommsCostLabel.gridy = 8;
        costDistroPane.add(prevCommsCostLabel, gbc_prevCommsCostLabel);
        
        leedLabel = new JLabel("LEED");
        GridBagConstraints gbc_leedLabel = new GridBagConstraints();
        gbc_leedLabel.insets = new Insets(0, 0, 5, 5);
        gbc_leedLabel.gridx = 1;
        gbc_leedLabel.gridy = 9;
        costDistroPane.add(leedLabel, gbc_leedLabel);
        
        spinner_8 = new JSpinner();
        GridBagConstraints gbc_spinner_8 = new GridBagConstraints();
        gbc_spinner_8.fill = GridBagConstraints.HORIZONTAL;
        gbc_spinner_8.insets = new Insets(0, 0, 5, 5);
        gbc_spinner_8.gridx = 2;
        gbc_spinner_8.gridy = 9;
        costDistroPane.add(spinner_8, gbc_spinner_8);
        
        leedCostLabel = new JLabel("$0.00");
        leedCostLabel.setHorizontalAlignment(SwingConstants.TRAILING);
        GridBagConstraints gbc_leedCostLabel = new GridBagConstraints();
        gbc_leedCostLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_leedCostLabel.insets = new Insets(0, 0, 5, 5);
        gbc_leedCostLabel.gridx = 3;
        gbc_leedCostLabel.gridy = 9;
        costDistroPane.add(leedCostLabel, gbc_leedCostLabel);
        
        prevLEEDCostLabel = new JLabel("- none -");
        GridBagConstraints gbc_prevLEEDCostLabel = new GridBagConstraints();
        gbc_prevLEEDCostLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_prevLEEDCostLabel.insets = new Insets(0, 0, 5, 5);
        gbc_prevLEEDCostLabel.gridx = 4;
        gbc_prevLEEDCostLabel.gridy = 9;
        costDistroPane.add(prevLEEDCostLabel, gbc_prevLEEDCostLabel);
        
        envLabel = new JLabel("Environmental");
        GridBagConstraints gbc_envLabel = new GridBagConstraints();
        gbc_envLabel.insets = new Insets(0, 0, 5, 5);
        gbc_envLabel.gridx = 1;
        gbc_envLabel.gridy = 10;
        costDistroPane.add(envLabel, gbc_envLabel);
        
        spinner_9 = new JSpinner();
        GridBagConstraints gbc_spinner_9 = new GridBagConstraints();
        gbc_spinner_9.fill = GridBagConstraints.HORIZONTAL;
        gbc_spinner_9.insets = new Insets(0, 0, 5, 5);
        gbc_spinner_9.gridx = 2;
        gbc_spinner_9.gridy = 10;
        costDistroPane.add(spinner_9, gbc_spinner_9);
        
        envCostLabel = new JLabel("$0.00");
        envCostLabel.setHorizontalAlignment(SwingConstants.TRAILING);
        GridBagConstraints gbc_envCostLabel = new GridBagConstraints();
        gbc_envCostLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_envCostLabel.insets = new Insets(0, 0, 5, 5);
        gbc_envCostLabel.gridx = 3;
        gbc_envCostLabel.gridy = 10;
        costDistroPane.add(envCostLabel, gbc_envCostLabel);
        
        prevEnvCostLabel = new JLabel("- none -");
        GridBagConstraints gbc_prevEnvCostLabel = new GridBagConstraints();
        gbc_prevEnvCostLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_prevEnvCostLabel.insets = new Insets(0, 0, 5, 5);
        gbc_prevEnvCostLabel.gridx = 4;
        gbc_prevEnvCostLabel.gridy = 10;
        costDistroPane.add(prevEnvCostLabel, gbc_prevEnvCostLabel);
        
        costEngLabel = new JLabel("Cost Engineer");
        GridBagConstraints gbc_costEngLabel = new GridBagConstraints();
        gbc_costEngLabel.insets = new Insets(0, 0, 5, 5);
        gbc_costEngLabel.gridx = 1;
        gbc_costEngLabel.gridy = 11;
        costDistroPane.add(costEngLabel, gbc_costEngLabel);
        
        spinner_10 = new JSpinner();
        GridBagConstraints gbc_spinner_10 = new GridBagConstraints();
        gbc_spinner_10.fill = GridBagConstraints.HORIZONTAL;
        gbc_spinner_10.insets = new Insets(0, 0, 5, 5);
        gbc_spinner_10.gridx = 2;
        gbc_spinner_10.gridy = 11;
        costDistroPane.add(spinner_10, gbc_spinner_10);
        
        costEngCostLabel = new JLabel("$0.00");
        costEngCostLabel.setHorizontalAlignment(SwingConstants.TRAILING);
        GridBagConstraints gbc_costEngCostLabel = new GridBagConstraints();
        gbc_costEngCostLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_costEngCostLabel.insets = new Insets(0, 0, 5, 5);
        gbc_costEngCostLabel.gridx = 3;
        gbc_costEngCostLabel.gridy = 11;
        costDistroPane.add(costEngCostLabel, gbc_costEngCostLabel);
        
        prevCostEngCostLabel = new JLabel("- none -");
        GridBagConstraints gbc_prevCostEngCostLabel = new GridBagConstraints();
        gbc_prevCostEngCostLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_prevCostEngCostLabel.insets = new Insets(0, 0, 5, 5);
        gbc_prevCostEngCostLabel.gridx = 4;
        gbc_prevCostEngCostLabel.gridy = 11;
        costDistroPane.add(prevCostEngCostLabel, gbc_prevCostEngCostLabel);
        
        geotechLabel = new JLabel("Geotechnical");
        GridBagConstraints gbc_geotechLabel = new GridBagConstraints();
        gbc_geotechLabel.insets = new Insets(0, 0, 5, 5);
        gbc_geotechLabel.gridx = 1;
        gbc_geotechLabel.gridy = 12;
        costDistroPane.add(geotechLabel, gbc_geotechLabel);
        
        spinner_11 = new JSpinner();
        GridBagConstraints gbc_spinner_11 = new GridBagConstraints();
        gbc_spinner_11.fill = GridBagConstraints.HORIZONTAL;
        gbc_spinner_11.insets = new Insets(0, 0, 5, 5);
        gbc_spinner_11.gridx = 2;
        gbc_spinner_11.gridy = 12;
        costDistroPane.add(spinner_11, gbc_spinner_11);
        
        geotechCostLabel = new JLabel("$0.00");
        geotechCostLabel.setHorizontalAlignment(SwingConstants.TRAILING);
        GridBagConstraints gbc_geotechCostLabel = new GridBagConstraints();
        gbc_geotechCostLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_geotechCostLabel.insets = new Insets(0, 0, 5, 5);
        gbc_geotechCostLabel.gridx = 3;
        gbc_geotechCostLabel.gridy = 12;
        costDistroPane.add(geotechCostLabel, gbc_geotechCostLabel);
        
        prevGeotechCostLabel = new JLabel("- none -");
        GridBagConstraints gbc_prevGeotechCostLabel = new GridBagConstraints();
        gbc_prevGeotechCostLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_prevGeotechCostLabel.insets = new Insets(0, 0, 5, 5);
        gbc_prevGeotechCostLabel.gridx = 4;
        gbc_prevGeotechCostLabel.gridy = 12;
        costDistroPane.add(prevGeotechCostLabel, gbc_prevGeotechCostLabel);
        
        aeContractLabel = new JLabel("AE Contracting");
        GridBagConstraints gbc_aeContractLabel = new GridBagConstraints();
        gbc_aeContractLabel.insets = new Insets(0, 0, 5, 5);
        gbc_aeContractLabel.gridx = 1;
        gbc_aeContractLabel.gridy = 13;
        costDistroPane.add(aeContractLabel, gbc_aeContractLabel);
        
        spinner_12 = new JSpinner();
        GridBagConstraints gbc_spinner_12 = new GridBagConstraints();
        gbc_spinner_12.fill = GridBagConstraints.HORIZONTAL;
        gbc_spinner_12.insets = new Insets(0, 0, 5, 5);
        gbc_spinner_12.gridx = 2;
        gbc_spinner_12.gridy = 13;
        costDistroPane.add(spinner_12, gbc_spinner_12);
        
        aeContractingLabel = new JLabel("$0.00");
        aeContractingLabel.setHorizontalAlignment(SwingConstants.TRAILING);
        GridBagConstraints gbc_aeContractingLabel = new GridBagConstraints();
        gbc_aeContractingLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_aeContractingLabel.insets = new Insets(0, 0, 5, 5);
        gbc_aeContractingLabel.gridx = 3;
        gbc_aeContractingLabel.gridy = 13;
        costDistroPane.add(aeContractingLabel, gbc_aeContractingLabel);
        
        prevAEContractingLabel = new JLabel("- none -");
        GridBagConstraints gbc_prevAEContractingLabel = new GridBagConstraints();
        gbc_prevAEContractingLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_prevAEContractingLabel.insets = new Insets(0, 0, 5, 5);
        gbc_prevAEContractingLabel.gridx = 4;
        gbc_prevAEContractingLabel.gridy = 13;
        costDistroPane.add(prevAEContractingLabel, gbc_prevAEContractingLabel);
        
        valueEngLabel = new JLabel("Value Engineer");
        GridBagConstraints gbc_valueEngLabel = new GridBagConstraints();
        gbc_valueEngLabel.insets = new Insets(0, 0, 5, 5);
        gbc_valueEngLabel.gridx = 1;
        gbc_valueEngLabel.gridy = 14;
        costDistroPane.add(valueEngLabel, gbc_valueEngLabel);
        
        spinner_13 = new JSpinner();
        GridBagConstraints gbc_spinner_13 = new GridBagConstraints();
        gbc_spinner_13.fill = GridBagConstraints.HORIZONTAL;
        gbc_spinner_13.insets = new Insets(0, 0, 5, 5);
        gbc_spinner_13.gridx = 2;
        gbc_spinner_13.gridy = 14;
        costDistroPane.add(spinner_13, gbc_spinner_13);
        
        valueEngCostLabel = new JLabel("$0.00");
        valueEngCostLabel.setHorizontalAlignment(SwingConstants.TRAILING);
        GridBagConstraints gbc_valueEngCostLabel = new GridBagConstraints();
        gbc_valueEngCostLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_valueEngCostLabel.insets = new Insets(0, 0, 5, 5);
        gbc_valueEngCostLabel.gridx = 3;
        gbc_valueEngCostLabel.gridy = 14;
        costDistroPane.add(valueEngCostLabel, gbc_valueEngCostLabel);
        
        prevValueEngCostLabel = new JLabel("- none -");
        GridBagConstraints gbc_prevValueEngCostLabel = new GridBagConstraints();
        gbc_prevValueEngCostLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_prevValueEngCostLabel.insets = new Insets(0, 0, 5, 5);
        gbc_prevValueEngCostLabel.gridx = 4;
        gbc_prevValueEngCostLabel.gridy = 14;
        costDistroPane.add(prevValueEngCostLabel, gbc_prevValueEngCostLabel);
        
        translatorLabel = new JLabel("Translator");
        GridBagConstraints gbc_translatorLabel = new GridBagConstraints();
        gbc_translatorLabel.insets = new Insets(0, 0, 5, 5);
        gbc_translatorLabel.gridx = 1;
        gbc_translatorLabel.gridy = 15;
        costDistroPane.add(translatorLabel, gbc_translatorLabel);
        
        spinner_14 = new JSpinner();
        GridBagConstraints gbc_spinner_14 = new GridBagConstraints();
        gbc_spinner_14.fill = GridBagConstraints.HORIZONTAL;
        gbc_spinner_14.insets = new Insets(0, 0, 5, 5);
        gbc_spinner_14.gridx = 2;
        gbc_spinner_14.gridy = 15;
        costDistroPane.add(spinner_14, gbc_spinner_14);
        
        translatorCostLabel = new JLabel("$0.00");
        translatorCostLabel.setHorizontalAlignment(SwingConstants.TRAILING);
        GridBagConstraints gbc_translatorCostLabel = new GridBagConstraints();
        gbc_translatorCostLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_translatorCostLabel.insets = new Insets(0, 0, 5, 5);
        gbc_translatorCostLabel.gridx = 3;
        gbc_translatorCostLabel.gridy = 15;
        costDistroPane.add(translatorCostLabel, gbc_translatorCostLabel);
        
        prevTranslatorCostLabel = new JLabel("- none -");
        GridBagConstraints gbc_prevTranslatorCostLabel = new GridBagConstraints();
        gbc_prevTranslatorCostLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_prevTranslatorCostLabel.insets = new Insets(0, 0, 5, 5);
        gbc_prevTranslatorCostLabel.gridx = 4;
        gbc_prevTranslatorCostLabel.gridy = 15;
        costDistroPane.add(prevTranslatorCostLabel, gbc_prevTranslatorCostLabel);
        
        specLabel = new JLabel("Specifications");
        GridBagConstraints gbc_specLabel = new GridBagConstraints();
        gbc_specLabel.insets = new Insets(0, 0, 5, 5);
        gbc_specLabel.gridx = 1;
        gbc_specLabel.gridy = 16;
        costDistroPane.add(specLabel, gbc_specLabel);
        
        spinner_15 = new JSpinner();
        GridBagConstraints gbc_spinner_15 = new GridBagConstraints();
        gbc_spinner_15.fill = GridBagConstraints.HORIZONTAL;
        gbc_spinner_15.insets = new Insets(0, 0, 5, 5);
        gbc_spinner_15.gridx = 2;
        gbc_spinner_15.gridy = 16;
        costDistroPane.add(spinner_15, gbc_spinner_15);
        
        specCostLabel = new JLabel("$0.00");
        specCostLabel.setHorizontalAlignment(SwingConstants.TRAILING);
        GridBagConstraints gbc_specCostLabel = new GridBagConstraints();
        gbc_specCostLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_specCostLabel.insets = new Insets(0, 0, 5, 5);
        gbc_specCostLabel.gridx = 3;
        gbc_specCostLabel.gridy = 16;
        costDistroPane.add(specCostLabel, gbc_specCostLabel);
        
        prevSpecCostLabel = new JLabel("- none -");
        GridBagConstraints gbc_prevSpecCostLabel = new GridBagConstraints();
        gbc_prevSpecCostLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_prevSpecCostLabel.insets = new Insets(0, 0, 5, 5);
        gbc_prevSpecCostLabel.gridx = 4;
        gbc_prevSpecCostLabel.gridy = 16;
        costDistroPane.add(prevSpecCostLabel, gbc_prevSpecCostLabel);
        
        otherLabel = new JLabel("Other");
        GridBagConstraints gbc_otherLabel = new GridBagConstraints();
        gbc_otherLabel.insets = new Insets(0, 0, 0, 5);
        gbc_otherLabel.gridx = 1;
        gbc_otherLabel.gridy = 17;
        costDistroPane.add(otherLabel, gbc_otherLabel);
        
        spinner_16 = new JSpinner();
        GridBagConstraints gbc_spinner_16 = new GridBagConstraints();
        gbc_spinner_16.fill = GridBagConstraints.HORIZONTAL;
        gbc_spinner_16.insets = new Insets(0, 0, 0, 5);
        gbc_spinner_16.gridx = 2;
        gbc_spinner_16.gridy = 17;
        costDistroPane.add(spinner_16, gbc_spinner_16);
        
        otherCostLabel = new JLabel("$0.00");
        otherCostLabel.setHorizontalAlignment(SwingConstants.TRAILING);
        GridBagConstraints gbc_otherCostLabel = new GridBagConstraints();
        gbc_otherCostLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_otherCostLabel.insets = new Insets(0, 0, 0, 5);
        gbc_otherCostLabel.gridx = 3;
        gbc_otherCostLabel.gridy = 17;
        costDistroPane.add(otherCostLabel, gbc_otherCostLabel);
        
        prevOtherCostLabel = new JLabel("- none -");
        GridBagConstraints gbc_prevOtherCostLabel = new GridBagConstraints();
        gbc_prevOtherCostLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_prevOtherCostLabel.insets = new Insets(0, 0, 0, 5);
        gbc_prevOtherCostLabel.gridx = 4;
        gbc_prevOtherCostLabel.gridy = 17;
        costDistroPane.add(prevOtherCostLabel, gbc_prevOtherCostLabel);
        
        personnelAssignmentPane = new JPanel();
        newWorkRequestPane.addTab("Personnel Assignment", null, personnelAssignmentPane, null);
        personnelAssignmentPane.setLayout(new BoxLayout(personnelAssignmentPane, BoxLayout.Y_AXIS));
        
        personnelTotalCostPane = new JPanel();
        personnelAssignmentPane.add(personnelTotalCostPane);
        GridBagLayout gbl_personnelTotalCostPane = new GridBagLayout();
        gbl_personnelTotalCostPane.columnWidths = new int[]{0, 0, 213, 0, 0, 0, 0};
        gbl_personnelTotalCostPane.rowHeights = new int[]{0, 0, 0, 0, 0};
        gbl_personnelTotalCostPane.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 1.0, 0.0, Double.MIN_VALUE};
        gbl_personnelTotalCostPane.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
        personnelTotalCostPane.setLayout(gbl_personnelTotalCostPane);
        
        verticalStrut_11 = Box.createVerticalStrut(20);
        GridBagConstraints gbc_verticalStrut_11 = new GridBagConstraints();
        gbc_verticalStrut_11.insets = new Insets(0, 0, 5, 5);
        gbc_verticalStrut_11.gridx = 1;
        gbc_verticalStrut_11.gridy = 0;
        personnelTotalCostPane.add(verticalStrut_11, gbc_verticalStrut_11);
        
        horizontalStrut_15 = Box.createHorizontalStrut(20);
        GridBagConstraints gbc_horizontalStrut_15 = new GridBagConstraints();
        gbc_horizontalStrut_15.insets = new Insets(0, 0, 5, 5);
        gbc_horizontalStrut_15.gridx = 0;
        gbc_horizontalStrut_15.gridy = 1;
        personnelTotalCostPane.add(horizontalStrut_15, gbc_horizontalStrut_15);
        
        personnelAssignedTotalCostLabel = new JLabel("Total Cost Provided");
        GridBagConstraints gbc_personnelAssignedTotalCostLabel = new GridBagConstraints();
        gbc_personnelAssignedTotalCostLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_personnelAssignedTotalCostLabel.insets = new Insets(0, 0, 5, 5);
        gbc_personnelAssignedTotalCostLabel.gridx = 1;
        gbc_personnelAssignedTotalCostLabel.gridy = 1;
        personnelTotalCostPane.add(personnelAssignedTotalCostLabel, gbc_personnelAssignedTotalCostLabel);
        
        varPersonnelAssignedTotalCostLabel = new JLabel("$0.00");
        GridBagConstraints gbc_varPersonnelAssignedTotalCostLabel = new GridBagConstraints();
        gbc_varPersonnelAssignedTotalCostLabel.insets = new Insets(0, 0, 5, 5);
        gbc_varPersonnelAssignedTotalCostLabel.gridx = 2;
        gbc_varPersonnelAssignedTotalCostLabel.gridy = 1;
        personnelTotalCostPane.add(varPersonnelAssignedTotalCostLabel, gbc_varPersonnelAssignedTotalCostLabel);
        
        personnelSubmissionDateLabel = new JLabel("Submission Date");
        GridBagConstraints gbc_personnelSubmissionDateLabel = new GridBagConstraints();
        gbc_personnelSubmissionDateLabel.anchor = GridBagConstraints.EAST;
        gbc_personnelSubmissionDateLabel.insets = new Insets(0, 0, 5, 5);
        gbc_personnelSubmissionDateLabel.gridx = 3;
        gbc_personnelSubmissionDateLabel.gridy = 1;
        personnelTotalCostPane.add(personnelSubmissionDateLabel, gbc_personnelSubmissionDateLabel);
        
        personnelSubmissionDateTextField = new JTextField();
        GridBagConstraints gbc_personnelSubmissionDateTextField = new GridBagConstraints();
        gbc_personnelSubmissionDateTextField.insets = new Insets(0, 0, 5, 5);
        gbc_personnelSubmissionDateTextField.fill = GridBagConstraints.HORIZONTAL;
        gbc_personnelSubmissionDateTextField.gridx = 4;
        gbc_personnelSubmissionDateTextField.gridy = 1;
        personnelTotalCostPane.add(personnelSubmissionDateTextField, gbc_personnelSubmissionDateTextField);
        personnelSubmissionDateTextField.setColumns(10);
        
        horizontalStrut_16 = Box.createHorizontalStrut(20);
        GridBagConstraints gbc_horizontalStrut_16 = new GridBagConstraints();
        gbc_horizontalStrut_16.insets = new Insets(0, 0, 5, 0);
        gbc_horizontalStrut_16.gridx = 5;
        gbc_horizontalStrut_16.gridy = 1;
        personnelTotalCostPane.add(horizontalStrut_16, gbc_horizontalStrut_16);
        
        personnelProvidedTotalCostLabel = new JLabel("Total Cost Assigned");
        GridBagConstraints gbc_personnelProvidedTotalCostLabel = new GridBagConstraints();
        gbc_personnelProvidedTotalCostLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_personnelProvidedTotalCostLabel.insets = new Insets(0, 0, 5, 5);
        gbc_personnelProvidedTotalCostLabel.gridx = 1;
        gbc_personnelProvidedTotalCostLabel.gridy = 2;
        personnelTotalCostPane.add(personnelProvidedTotalCostLabel, gbc_personnelProvidedTotalCostLabel);
        
        varPersonnelProvidedTotalCostLabel = new JLabel("$0.00");
        GridBagConstraints gbc_varPersonnelProvidedTotalCostLabel = new GridBagConstraints();
        gbc_varPersonnelProvidedTotalCostLabel.insets = new Insets(0, 0, 5, 5);
        gbc_varPersonnelProvidedTotalCostLabel.gridx = 2;
        gbc_varPersonnelProvidedTotalCostLabel.gridy = 2;
        personnelTotalCostPane.add(varPersonnelProvidedTotalCostLabel, gbc_varPersonnelProvidedTotalCostLabel);
        
        wrDocFolderLabel = new JLabel("Work Request Document Folder");
        GridBagConstraints gbc_wrDocFolderLabel = new GridBagConstraints();
        gbc_wrDocFolderLabel.anchor = GridBagConstraints.EAST;
        gbc_wrDocFolderLabel.insets = new Insets(0, 0, 5, 5);
        gbc_wrDocFolderLabel.gridx = 3;
        gbc_wrDocFolderLabel.gridy = 2;
        personnelTotalCostPane.add(wrDocFolderLabel, gbc_wrDocFolderLabel);
        
        wrDocFolderTextField = new JTextField();
        GridBagConstraints gbc_wrDocFolderTextField = new GridBagConstraints();
        gbc_wrDocFolderTextField.insets = new Insets(0, 0, 5, 5);
        gbc_wrDocFolderTextField.fill = GridBagConstraints.HORIZONTAL;
        gbc_wrDocFolderTextField.gridx = 4;
        gbc_wrDocFolderTextField.gridy = 2;
        personnelTotalCostPane.add(wrDocFolderTextField, gbc_wrDocFolderTextField);
        wrDocFolderTextField.setColumns(10);
        
        allAssigneesCheckBox = new JCheckBox("Select all Assignees");
        GridBagConstraints gbc_allAssigneesCheckBox = new GridBagConstraints();
        gbc_allAssigneesCheckBox.insets = new Insets(0, 0, 0, 5);
        gbc_allAssigneesCheckBox.gridx = 1;
        gbc_allAssigneesCheckBox.gridy = 3;
        personnelTotalCostPane.add(allAssigneesCheckBox, gbc_allAssigneesCheckBox);
        
        docLocationLabel = new JLabel("Documents Location");
        GridBagConstraints gbc_docLocationLabel = new GridBagConstraints();
        gbc_docLocationLabel.insets = new Insets(0, 0, 0, 5);
        gbc_docLocationLabel.anchor = GridBagConstraints.EAST;
        gbc_docLocationLabel.gridx = 3;
        gbc_docLocationLabel.gridy = 3;
        personnelTotalCostPane.add(docLocationLabel, gbc_docLocationLabel);
        
        docLocationTextField = new JTextField();
        GridBagConstraints gbc_docLocationTextField = new GridBagConstraints();
        gbc_docLocationTextField.insets = new Insets(0, 0, 0, 5);
        gbc_docLocationTextField.fill = GridBagConstraints.HORIZONTAL;
        gbc_docLocationTextField.gridx = 4;
        gbc_docLocationTextField.gridy = 3;
        personnelTotalCostPane.add(docLocationTextField, gbc_docLocationTextField);
        docLocationTextField.setColumns(10);
        
        assignmentScrollPane = new JScrollPane();
        personnelAssignmentPane.add(assignmentScrollPane);
        
        assignPersonnelPane = new JPanel();
        assignmentScrollPane.setViewportView(assignPersonnelPane);
        assignPersonnelPane.setLayout(new BoxLayout(assignPersonnelPane, BoxLayout.Y_AXIS));
        
        civilPersonnelPane = new JPanel();
		assignPersonnelPane.add(civilPersonnelPane);
        GridBagLayout gbl_civilPersonnelPane = new GridBagLayout();
        gbl_civilPersonnelPane.columnWidths = new int[]{0, 0, 310, 131, 138, 121, 80, 0, 0};
        gbl_civilPersonnelPane.rowHeights = new int[] {0, 0, 0, 0, 0};
        gbl_civilPersonnelPane.columnWeights = new double[]{0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
        gbl_civilPersonnelPane.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
        civilPersonnelPane.setLayout(gbl_civilPersonnelPane);
        
        civilVerticalStrut = Box.createVerticalStrut(20);
        GridBagConstraints gbc_civilVerticalStrut = new GridBagConstraints();
        gbc_civilVerticalStrut.insets = new Insets(0, 0, 5, 5);
        gbc_civilVerticalStrut.gridx = 1;
        gbc_civilVerticalStrut.gridy = 0;
        civilPersonnelPane.add(civilVerticalStrut, gbc_civilVerticalStrut);
        
        civilHorzizontalStrut = Box.createHorizontalStrut(20);
        GridBagConstraints gbc_civilHorzizontalStrut = new GridBagConstraints();
        gbc_civilHorzizontalStrut.insets = new Insets(0, 0, 5, 5);
        gbc_civilHorzizontalStrut.gridx = 0;
        gbc_civilHorzizontalStrut.gridy = 1;
        civilPersonnelPane.add(civilHorzizontalStrut, gbc_civilHorzizontalStrut);
        
        civilHorzizontalStrut_2 = Box.createHorizontalStrut(20);
        GridBagConstraints gbc_civilHorzizontalStrut_2 = new GridBagConstraints();
        gbc_civilHorzizontalStrut_2.insets = new Insets(0, 0, 5, 0);
        gbc_civilHorzizontalStrut_2.gridx = 7;
        gbc_civilHorzizontalStrut_2.gridy = 2;
        civilPersonnelPane.add(civilHorzizontalStrut_2, gbc_civilHorzizontalStrut_2);
				
		civilTitleLabel = new JLabel("civil");
        civilTitleLabel.setOpaque(true);
        civilTitleLabel.setBackground(new Color(255, 255, 204));
        GridBagConstraints gbc_civilTitleLabel = new GridBagConstraints();
        gbc_civilTitleLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_civilTitleLabel.gridwidth = 6;
        gbc_civilTitleLabel.insets = new Insets(0, 0, 5, 5);
        gbc_civilTitleLabel.gridx = 1;
        gbc_civilTitleLabel.gridy = 1;
        civilPersonnelPane.add(civilTitleLabel, gbc_civilTitleLabel);
        
        civilFillerLabel = new JLabel("  ");
        civilFillerLabel.setOpaque(true);
        civilFillerLabel.setBackground(new Color(153, 153, 153));
        GridBagConstraints gbc_civilFillerLabel = new GridBagConstraints();
        gbc_civilFillerLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_civilFillerLabel.insets = new Insets(0, 0, 5, 5);
        gbc_civilFillerLabel.gridx = 1;
        gbc_civilFillerLabel.gridy = 2;
        civilPersonnelPane.add(civilFillerLabel, gbc_civilFillerLabel);
        
        civilAssigneeTitleLabel = new JLabel("Assignee");
        civilAssigneeTitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        civilAssigneeTitleLabel.setOpaque(true);
        civilAssigneeTitleLabel.setForeground(new Color(255, 255, 255));
        civilAssigneeTitleLabel.setBackground(new Color(153, 153, 153));
        GridBagConstraints gbc_civilAssigneeTitleLabel = new GridBagConstraints();
        gbc_civilAssigneeTitleLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_civilAssigneeTitleLabel.insets = new Insets(0, 0, 5, 5);
        gbc_civilAssigneeTitleLabel.gridx = 2;
        gbc_civilAssigneeTitleLabel.gridy = 2;
        civilPersonnelPane.add(civilAssigneeTitleLabel, gbc_civilAssigneeTitleLabel);
        
        civilHoursAssingedTitleLabel = new JLabel("Hours Assigned");
        civilHoursAssingedTitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        civilHoursAssingedTitleLabel.setOpaque(true);
        civilHoursAssingedTitleLabel.setForeground(new Color(255, 255, 255));
        civilHoursAssingedTitleLabel.setBackground(new Color(153, 153, 153));
        GridBagConstraints gbc_civilHoursAssingedTitleLabel = new GridBagConstraints();
        gbc_civilHoursAssingedTitleLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_civilHoursAssingedTitleLabel.insets = new Insets(0, 0, 5, 5);
        gbc_civilHoursAssingedTitleLabel.gridx = 3;
        gbc_civilHoursAssingedTitleLabel.gridy = 2;
        civilPersonnelPane.add(civilHoursAssingedTitleLabel, gbc_civilHoursAssingedTitleLabel);
        
        civilWorkRateTitleLabel = new JLabel("Work Rate");
        civilWorkRateTitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        civilWorkRateTitleLabel.setOpaque(true);
        civilWorkRateTitleLabel.setForeground(new Color(255, 255, 255));
        civilWorkRateTitleLabel.setBackground(new Color(153, 153, 153));
        GridBagConstraints gbc_civilWorkRateTitleLabel = new GridBagConstraints();
        gbc_civilWorkRateTitleLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_civilWorkRateTitleLabel.insets = new Insets(0, 0, 5, 5);
        gbc_civilWorkRateTitleLabel.gridx = 4;
        gbc_civilWorkRateTitleLabel.gridy = 2;
        civilPersonnelPane.add(civilWorkRateTitleLabel, gbc_civilWorkRateTitleLabel);
        
        civilDateCompleteTitleLabel = new JLabel("Date Completed");
        civilDateCompleteTitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        civilDateCompleteTitleLabel.setOpaque(true);
        civilDateCompleteTitleLabel.setForeground(new Color(255, 255, 255));
        civilDateCompleteTitleLabel.setBackground(new Color(153, 153, 153));
        GridBagConstraints gbc_civilDateCompleteTitleLabel = new GridBagConstraints();
        gbc_civilDateCompleteTitleLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_civilDateCompleteTitleLabel.insets = new Insets(0, 0, 5, 5);
        gbc_civilDateCompleteTitleLabel.gridx = 5;
        gbc_civilDateCompleteTitleLabel.gridy = 2;
        civilPersonnelPane.add(civilDateCompleteTitleLabel, gbc_civilDateCompleteTitleLabel);
        
        civilBackcheckTitleLabel = new JLabel("Backcheck");
        civilBackcheckTitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        civilBackcheckTitleLabel.setOpaque(true);
        civilBackcheckTitleLabel.setForeground(new Color(255, 255, 255));
        civilBackcheckTitleLabel.setBackground(new Color(153, 153, 153));
        GridBagConstraints gbc_civilBackcheckTitleLabel = new GridBagConstraints();
        gbc_civilBackcheckTitleLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_civilBackcheckTitleLabel.insets = new Insets(0, 0, 5, 5);
        gbc_civilBackcheckTitleLabel.gridx = 6;
        gbc_civilBackcheckTitleLabel.gridy = 2;
        civilPersonnelPane.add(civilBackcheckTitleLabel, gbc_civilBackcheckTitleLabel);
        
        civilAssigneeCheckBox = new JCheckBox("");
        GridBagConstraints gbc_civilAssigneeCheckBox = new GridBagConstraints();
        gbc_civilAssigneeCheckBox.insets = new Insets(0, 0, 0, 5);
        gbc_civilAssigneeCheckBox.gridx = 1;
        gbc_civilAssigneeCheckBox.gridy = 3;
        civilPersonnelPane.add(civilAssigneeCheckBox, gbc_civilAssigneeCheckBox);
        
        civilAssigneeName = new JLabel("Assignee Name");
        GridBagConstraints gbc_civilAssigneeName = new GridBagConstraints();
        gbc_civilAssigneeName.fill = GridBagConstraints.HORIZONTAL;
        gbc_civilAssigneeName.insets = new Insets(0, 0, 0, 5);
        gbc_civilAssigneeName.gridx = 2;
        gbc_civilAssigneeName.gridy = 3;
        civilPersonnelPane.add(civilAssigneeName, gbc_civilAssigneeName);
        
        civilHoursAssignedLabel = new JLabel("0.00");
        civilHoursAssignedLabel.setHorizontalAlignment(SwingConstants.TRAILING);
        GridBagConstraints gbc_civilHoursAssignedLabel = new GridBagConstraints();
        gbc_civilHoursAssignedLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_civilHoursAssignedLabel.insets = new Insets(0, 0, 0, 5);
        gbc_civilHoursAssignedLabel.gridx = 3;
        gbc_civilHoursAssignedLabel.gridy = 3;
        civilPersonnelPane.add(civilHoursAssignedLabel, gbc_civilHoursAssignedLabel);
        
        civilWorkRateLabel = new JLabel("Work Rate");
        civilWorkRateLabel.setHorizontalAlignment(SwingConstants.TRAILING);
        GridBagConstraints gbc_civilWorkRateLabel = new GridBagConstraints();
        gbc_civilWorkRateLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_civilWorkRateLabel.insets = new Insets(0, 0, 0, 5);
        gbc_civilWorkRateLabel.gridx = 4;
        gbc_civilWorkRateLabel.gridy = 3;
        civilPersonnelPane.add(civilWorkRateLabel, gbc_civilWorkRateLabel);
        
        civilDateCompletedLabel = new JLabel("00/00/1987");
        civilDateCompletedLabel.setHorizontalAlignment(SwingConstants.TRAILING);
        GridBagConstraints gbc_civilDateCompletedLabel = new GridBagConstraints();
        gbc_civilDateCompletedLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_civilDateCompletedLabel.insets = new Insets(0, 0, 0, 5);
        gbc_civilDateCompletedLabel.gridx = 5;
        gbc_civilDateCompletedLabel.gridy = 3;
        civilPersonnelPane.add(civilDateCompletedLabel, gbc_civilDateCompletedLabel);
        
        civilBackcheckLabel = new JLabel("--");
        civilBackcheckLabel.setHorizontalAlignment(SwingConstants.CENTER);
        GridBagConstraints gbc_civilBackcheckLabel = new GridBagConstraints();
        gbc_civilBackcheckLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_civilBackcheckLabel.insets = new Insets(0, 0, 0, 5);
        gbc_civilBackcheckLabel.gridx = 6;
        gbc_civilBackcheckLabel.gridy = 3;
        civilPersonnelPane.add(civilBackcheckLabel, gbc_civilBackcheckLabel);
        
        architecturalPersonnelPane = new JPanel();
		assignPersonnelPane.add(architecturalPersonnelPane);
        GridBagLayout gbl_architecturalPersonnelPane = new GridBagLayout();
        gbl_architecturalPersonnelPane.columnWidths = new int[]{0, 0, 310, 131, 138, 121, 80, 0, 0};
        gbl_architecturalPersonnelPane.rowHeights = new int[]{0, 0, 0, 0, 0};
        gbl_architecturalPersonnelPane.columnWeights = new double[]{0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
        gbl_architecturalPersonnelPane.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
        architecturalPersonnelPane.setLayout(gbl_architecturalPersonnelPane);
        
        architecturalVerticalStrut = Box.createVerticalStrut(20);
        GridBagConstraints gbc_architecturalVerticalStrut = new GridBagConstraints();
        gbc_architecturalVerticalStrut.insets = new Insets(0, 0, 5, 5);
        gbc_architecturalVerticalStrut.gridx = 1;
        gbc_architecturalVerticalStrut.gridy = 0;
        architecturalPersonnelPane.add(architecturalVerticalStrut, gbc_architecturalVerticalStrut);
        
        architecturalHorzizontalStrut = Box.createHorizontalStrut(20);
        GridBagConstraints gbc_architecturalHorzizontalStrut = new GridBagConstraints();
        gbc_architecturalHorzizontalStrut.insets = new Insets(0, 0, 5, 5);
        gbc_architecturalHorzizontalStrut.gridx = 0;
        gbc_architecturalHorzizontalStrut.gridy = 1;
        architecturalPersonnelPane.add(architecturalHorzizontalStrut, gbc_architecturalHorzizontalStrut);
        
        architecturalHorzizontalStrut_2 = Box.createHorizontalStrut(20);
        GridBagConstraints gbc_architecturalHorzizontalStrut_2 = new GridBagConstraints();
        gbc_architecturalHorzizontalStrut_2.insets = new Insets(0, 0, 5, 0);
        gbc_architecturalHorzizontalStrut_2.gridx = 7;
        gbc_architecturalHorzizontalStrut_2.gridy = 2;
        architecturalPersonnelPane.add(architecturalHorzizontalStrut_2, gbc_architecturalHorzizontalStrut_2);
				
		architecturalTitleLabel = new JLabel("architectural");
        architecturalTitleLabel.setOpaque(true);
        architecturalTitleLabel.setBackground(new Color(255, 255, 204));
        GridBagConstraints gbc_architecturalTitleLabel = new GridBagConstraints();
        gbc_architecturalTitleLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_architecturalTitleLabel.gridwidth = 6;
        gbc_architecturalTitleLabel.insets = new Insets(0, 0, 5, 5);
        gbc_architecturalTitleLabel.gridx = 1;
        gbc_architecturalTitleLabel.gridy = 1;
        architecturalPersonnelPane.add(architecturalTitleLabel, gbc_architecturalTitleLabel);
        
        architecturalFillerLabel = new JLabel("  ");
        architecturalFillerLabel.setOpaque(true);
        architecturalFillerLabel.setBackground(new Color(153, 153, 153));
        GridBagConstraints gbc_architecturalFillerLabel = new GridBagConstraints();
        gbc_architecturalFillerLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_architecturalFillerLabel.insets = new Insets(0, 0, 5, 5);
        gbc_architecturalFillerLabel.gridx = 1;
        gbc_architecturalFillerLabel.gridy = 2;
        architecturalPersonnelPane.add(architecturalFillerLabel, gbc_architecturalFillerLabel);
        
        architecturalAssigneeTitleLabel = new JLabel("Assignee");
        architecturalAssigneeTitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        architecturalAssigneeTitleLabel.setOpaque(true);
        architecturalAssigneeTitleLabel.setForeground(new Color(255, 255, 255));
        architecturalAssigneeTitleLabel.setBackground(new Color(153, 153, 153));
        GridBagConstraints gbc_architecturalAssigneeTitleLabel = new GridBagConstraints();
        gbc_architecturalAssigneeTitleLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_architecturalAssigneeTitleLabel.insets = new Insets(0, 0, 5, 5);
        gbc_architecturalAssigneeTitleLabel.gridx = 2;
        gbc_architecturalAssigneeTitleLabel.gridy = 2;
        architecturalPersonnelPane.add(architecturalAssigneeTitleLabel, gbc_architecturalAssigneeTitleLabel);
        
        architecturalHoursAssingedTitleLabel = new JLabel("Hours Assigned");
        architecturalHoursAssingedTitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        architecturalHoursAssingedTitleLabel.setOpaque(true);
        architecturalHoursAssingedTitleLabel.setForeground(new Color(255, 255, 255));
        architecturalHoursAssingedTitleLabel.setBackground(new Color(153, 153, 153));
        GridBagConstraints gbc_architecturalHoursAssingedTitleLabel = new GridBagConstraints();
        gbc_architecturalHoursAssingedTitleLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_architecturalHoursAssingedTitleLabel.insets = new Insets(0, 0, 5, 5);
        gbc_architecturalHoursAssingedTitleLabel.gridx = 3;
        gbc_architecturalHoursAssingedTitleLabel.gridy = 2;
        architecturalPersonnelPane.add(architecturalHoursAssingedTitleLabel, gbc_architecturalHoursAssingedTitleLabel);
        
        architecturalWorkRateTitleLabel = new JLabel("Work Rate");
        architecturalWorkRateTitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        architecturalWorkRateTitleLabel.setOpaque(true);
        architecturalWorkRateTitleLabel.setForeground(new Color(255, 255, 255));
        architecturalWorkRateTitleLabel.setBackground(new Color(153, 153, 153));
        GridBagConstraints gbc_architecturalWorkRateTitleLabel = new GridBagConstraints();
        gbc_architecturalWorkRateTitleLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_architecturalWorkRateTitleLabel.insets = new Insets(0, 0, 5, 5);
        gbc_architecturalWorkRateTitleLabel.gridx = 4;
        gbc_architecturalWorkRateTitleLabel.gridy = 2;
        architecturalPersonnelPane.add(architecturalWorkRateTitleLabel, gbc_architecturalWorkRateTitleLabel);
        
        architecturalDateCompleteTitleLabel = new JLabel("Date Completed");
        architecturalDateCompleteTitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        architecturalDateCompleteTitleLabel.setOpaque(true);
        architecturalDateCompleteTitleLabel.setForeground(new Color(255, 255, 255));
        architecturalDateCompleteTitleLabel.setBackground(new Color(153, 153, 153));
        GridBagConstraints gbc_architecturalDateCompleteTitleLabel = new GridBagConstraints();
        gbc_architecturalDateCompleteTitleLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_architecturalDateCompleteTitleLabel.insets = new Insets(0, 0, 5, 5);
        gbc_architecturalDateCompleteTitleLabel.gridx = 5;
        gbc_architecturalDateCompleteTitleLabel.gridy = 2;
        architecturalPersonnelPane.add(architecturalDateCompleteTitleLabel, gbc_architecturalDateCompleteTitleLabel);
        
        architecturalBackcheckTitleLabel = new JLabel("Backcheck");
        architecturalBackcheckTitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        architecturalBackcheckTitleLabel.setOpaque(true);
        architecturalBackcheckTitleLabel.setForeground(new Color(255, 255, 255));
        architecturalBackcheckTitleLabel.setBackground(new Color(153, 153, 153));
        GridBagConstraints gbc_architecturalBackcheckTitleLabel = new GridBagConstraints();
        gbc_architecturalBackcheckTitleLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_architecturalBackcheckTitleLabel.insets = new Insets(0, 0, 5, 5);
        gbc_architecturalBackcheckTitleLabel.gridx = 6;
        gbc_architecturalBackcheckTitleLabel.gridy = 2;
        architecturalPersonnelPane.add(architecturalBackcheckTitleLabel, gbc_architecturalBackcheckTitleLabel);
        
        architecturalAssigneeCheckBox = new JCheckBox("");
        GridBagConstraints gbc_architecturalAssigneeCheckBox = new GridBagConstraints();
        gbc_architecturalAssigneeCheckBox.insets = new Insets(0, 0, 5, 5);
        gbc_architecturalAssigneeCheckBox.gridx = 1;
        gbc_architecturalAssigneeCheckBox.gridy = 3;
        architecturalPersonnelPane.add(architecturalAssigneeCheckBox, gbc_architecturalAssigneeCheckBox);
        
        architecturalAssigneeName = new JLabel("Assignee Name");
        GridBagConstraints gbc_architecturalAssigneeName = new GridBagConstraints();
        gbc_architecturalAssigneeName.fill = GridBagConstraints.HORIZONTAL;
        gbc_architecturalAssigneeName.insets = new Insets(0, 0, 5, 5);
        gbc_architecturalAssigneeName.gridx = 2;
        gbc_architecturalAssigneeName.gridy = 3;
        architecturalPersonnelPane.add(architecturalAssigneeName, gbc_architecturalAssigneeName);
        
        architecturalHoursAssignedLabel = new JLabel("0.00");
        architecturalHoursAssignedLabel.setHorizontalAlignment(SwingConstants.TRAILING);
        GridBagConstraints gbc_architecturalHoursAssignedLabel = new GridBagConstraints();
        gbc_architecturalHoursAssignedLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_architecturalHoursAssignedLabel.insets = new Insets(0, 0, 5, 5);
        gbc_architecturalHoursAssignedLabel.gridx = 3;
        gbc_architecturalHoursAssignedLabel.gridy = 3;
        architecturalPersonnelPane.add(architecturalHoursAssignedLabel, gbc_architecturalHoursAssignedLabel);
        
        architecturalWorkRateLabel = new JLabel("Work Rate");
        architecturalWorkRateLabel.setHorizontalAlignment(SwingConstants.TRAILING);
        GridBagConstraints gbc_architecturalWorkRateLabel = new GridBagConstraints();
        gbc_architecturalWorkRateLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_architecturalWorkRateLabel.insets = new Insets(0, 0, 5, 5);
        gbc_architecturalWorkRateLabel.gridx = 4;
        gbc_architecturalWorkRateLabel.gridy = 3;
        architecturalPersonnelPane.add(architecturalWorkRateLabel, gbc_architecturalWorkRateLabel);
        
        architecturalDateCompletedLabel = new JLabel("00/00/1987");
        architecturalDateCompletedLabel.setHorizontalAlignment(SwingConstants.TRAILING);
        GridBagConstraints gbc_architecturalDateCompletedLabel = new GridBagConstraints();
        gbc_architecturalDateCompletedLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_architecturalDateCompletedLabel.insets = new Insets(0, 0, 5, 5);
        gbc_architecturalDateCompletedLabel.gridx = 5;
        gbc_architecturalDateCompletedLabel.gridy = 3;
        architecturalPersonnelPane.add(architecturalDateCompletedLabel, gbc_architecturalDateCompletedLabel);
        
        architecturalBackcheckLabel = new JLabel("--");
        architecturalBackcheckLabel.setHorizontalAlignment(SwingConstants.CENTER);
        GridBagConstraints gbc_architecturalBackcheckLabel = new GridBagConstraints();
        gbc_architecturalBackcheckLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_architecturalBackcheckLabel.insets = new Insets(0, 0, 5, 5);
        gbc_architecturalBackcheckLabel.gridx = 6;
        gbc_architecturalBackcheckLabel.gridy = 3;
        architecturalPersonnelPane.add(architecturalBackcheckLabel, gbc_architecturalBackcheckLabel);
        
        structuralPersonnelPane = new JPanel();
		assignPersonnelPane.add(structuralPersonnelPane);
        GridBagLayout gbl_structuralPersonnelPane = new GridBagLayout();
        gbl_structuralPersonnelPane.columnWidths = new int[]{0, 0, 310, 131, 138, 121, 80, 0, 0};
        gbl_structuralPersonnelPane.rowHeights = new int[]{0, 0, 0, 0, 0};
        gbl_structuralPersonnelPane.columnWeights = new double[]{0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
        gbl_structuralPersonnelPane.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
        structuralPersonnelPane.setLayout(gbl_structuralPersonnelPane);
        
        structuralVerticalStrut = Box.createVerticalStrut(20);
        GridBagConstraints gbc_structuralVerticalStrut = new GridBagConstraints();
        gbc_structuralVerticalStrut.insets = new Insets(0, 0, 5, 5);
        gbc_structuralVerticalStrut.gridx = 1;
        gbc_structuralVerticalStrut.gridy = 0;
        structuralPersonnelPane.add(structuralVerticalStrut, gbc_structuralVerticalStrut);
        
        structuralHorzizontalStrut = Box.createHorizontalStrut(20);
        GridBagConstraints gbc_structuralHorzizontalStrut = new GridBagConstraints();
        gbc_structuralHorzizontalStrut.insets = new Insets(0, 0, 5, 5);
        gbc_structuralHorzizontalStrut.gridx = 0;
        gbc_structuralHorzizontalStrut.gridy = 1;
        structuralPersonnelPane.add(structuralHorzizontalStrut, gbc_structuralHorzizontalStrut);
        
        structuralHorzizontalStrut_2 = Box.createHorizontalStrut(20);
        GridBagConstraints gbc_structuralHorzizontalStrut_2 = new GridBagConstraints();
        gbc_structuralHorzizontalStrut_2.insets = new Insets(0, 0, 5, 0);
        gbc_structuralHorzizontalStrut_2.gridx = 7;
        gbc_structuralHorzizontalStrut_2.gridy = 2;
        structuralPersonnelPane.add(structuralHorzizontalStrut_2, gbc_structuralHorzizontalStrut_2);
				
		structuralTitleLabel = new JLabel("structural");
        structuralTitleLabel.setOpaque(true);
        structuralTitleLabel.setBackground(new Color(255, 255, 204));
        GridBagConstraints gbc_structuralTitleLabel = new GridBagConstraints();
        gbc_structuralTitleLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_structuralTitleLabel.gridwidth = 6;
        gbc_structuralTitleLabel.insets = new Insets(0, 0, 5, 5);
        gbc_structuralTitleLabel.gridx = 1;
        gbc_structuralTitleLabel.gridy = 1;
        structuralPersonnelPane.add(structuralTitleLabel, gbc_structuralTitleLabel);
        
        structuralFillerLabel = new JLabel("  ");
        structuralFillerLabel.setOpaque(true);
        structuralFillerLabel.setBackground(new Color(153, 153, 153));
        GridBagConstraints gbc_structuralFillerLabel = new GridBagConstraints();
        gbc_structuralFillerLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_structuralFillerLabel.insets = new Insets(0, 0, 5, 5);
        gbc_structuralFillerLabel.gridx = 1;
        gbc_structuralFillerLabel.gridy = 2;
        structuralPersonnelPane.add(structuralFillerLabel, gbc_structuralFillerLabel);
        
        structuralAssigneeTitleLabel = new JLabel("Assignee");
        structuralAssigneeTitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        structuralAssigneeTitleLabel.setOpaque(true);
        structuralAssigneeTitleLabel.setForeground(new Color(255, 255, 255));
        structuralAssigneeTitleLabel.setBackground(new Color(153, 153, 153));
        GridBagConstraints gbc_structuralAssigneeTitleLabel = new GridBagConstraints();
        gbc_structuralAssigneeTitleLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_structuralAssigneeTitleLabel.insets = new Insets(0, 0, 5, 5);
        gbc_structuralAssigneeTitleLabel.gridx = 2;
        gbc_structuralAssigneeTitleLabel.gridy = 2;
        structuralPersonnelPane.add(structuralAssigneeTitleLabel, gbc_structuralAssigneeTitleLabel);
        
        structuralHoursAssingedTitleLabel = new JLabel("Hours Assigned");
        structuralHoursAssingedTitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        structuralHoursAssingedTitleLabel.setOpaque(true);
        structuralHoursAssingedTitleLabel.setForeground(new Color(255, 255, 255));
        structuralHoursAssingedTitleLabel.setBackground(new Color(153, 153, 153));
        GridBagConstraints gbc_structuralHoursAssingedTitleLabel = new GridBagConstraints();
        gbc_structuralHoursAssingedTitleLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_structuralHoursAssingedTitleLabel.insets = new Insets(0, 0, 5, 5);
        gbc_structuralHoursAssingedTitleLabel.gridx = 3;
        gbc_structuralHoursAssingedTitleLabel.gridy = 2;
        structuralPersonnelPane.add(structuralHoursAssingedTitleLabel, gbc_structuralHoursAssingedTitleLabel);
        
        structuralWorkRateTitleLabel = new JLabel("Work Rate");
        structuralWorkRateTitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        structuralWorkRateTitleLabel.setOpaque(true);
        structuralWorkRateTitleLabel.setForeground(new Color(255, 255, 255));
        structuralWorkRateTitleLabel.setBackground(new Color(153, 153, 153));
        GridBagConstraints gbc_structuralWorkRateTitleLabel = new GridBagConstraints();
        gbc_structuralWorkRateTitleLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_structuralWorkRateTitleLabel.insets = new Insets(0, 0, 5, 5);
        gbc_structuralWorkRateTitleLabel.gridx = 4;
        gbc_structuralWorkRateTitleLabel.gridy = 2;
        structuralPersonnelPane.add(structuralWorkRateTitleLabel, gbc_structuralWorkRateTitleLabel);
        
        structuralDateCompleteTitleLabel = new JLabel("Date Completed");
        structuralDateCompleteTitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        structuralDateCompleteTitleLabel.setOpaque(true);
        structuralDateCompleteTitleLabel.setForeground(new Color(255, 255, 255));
        structuralDateCompleteTitleLabel.setBackground(new Color(153, 153, 153));
        GridBagConstraints gbc_structuralDateCompleteTitleLabel = new GridBagConstraints();
        gbc_structuralDateCompleteTitleLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_structuralDateCompleteTitleLabel.insets = new Insets(0, 0, 5, 5);
        gbc_structuralDateCompleteTitleLabel.gridx = 5;
        gbc_structuralDateCompleteTitleLabel.gridy = 2;
        structuralPersonnelPane.add(structuralDateCompleteTitleLabel, gbc_structuralDateCompleteTitleLabel);
        
        structuralBackcheckTitleLabel = new JLabel("Backcheck");
        structuralBackcheckTitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        structuralBackcheckTitleLabel.setOpaque(true);
        structuralBackcheckTitleLabel.setForeground(new Color(255, 255, 255));
        structuralBackcheckTitleLabel.setBackground(new Color(153, 153, 153));
        GridBagConstraints gbc_structuralBackcheckTitleLabel = new GridBagConstraints();
        gbc_structuralBackcheckTitleLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_structuralBackcheckTitleLabel.insets = new Insets(0, 0, 5, 5);
        gbc_structuralBackcheckTitleLabel.gridx = 6;
        gbc_structuralBackcheckTitleLabel.gridy = 2;
        structuralPersonnelPane.add(structuralBackcheckTitleLabel, gbc_structuralBackcheckTitleLabel);
        
        structuralAssigneeCheckBox = new JCheckBox("");
        GridBagConstraints gbc_structuralAssigneeCheckBox = new GridBagConstraints();
        gbc_structuralAssigneeCheckBox.insets = new Insets(0, 0, 5, 5);
        gbc_structuralAssigneeCheckBox.gridx = 1;
        gbc_structuralAssigneeCheckBox.gridy = 3;
        structuralPersonnelPane.add(structuralAssigneeCheckBox, gbc_structuralAssigneeCheckBox);
        
        structuralAssigneeName = new JLabel("Assignee Name");
        GridBagConstraints gbc_structuralAssigneeName = new GridBagConstraints();
        gbc_structuralAssigneeName.fill = GridBagConstraints.HORIZONTAL;
        gbc_structuralAssigneeName.insets = new Insets(0, 0, 5, 5);
        gbc_structuralAssigneeName.gridx = 2;
        gbc_structuralAssigneeName.gridy = 3;
        structuralPersonnelPane.add(structuralAssigneeName, gbc_structuralAssigneeName);
        
        structuralHoursAssignedLabel = new JLabel("0.00");
        structuralHoursAssignedLabel.setHorizontalAlignment(SwingConstants.TRAILING);
        GridBagConstraints gbc_structuralHoursAssignedLabel = new GridBagConstraints();
        gbc_structuralHoursAssignedLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_structuralHoursAssignedLabel.insets = new Insets(0, 0, 5, 5);
        gbc_structuralHoursAssignedLabel.gridx = 3;
        gbc_structuralHoursAssignedLabel.gridy = 3;
        structuralPersonnelPane.add(structuralHoursAssignedLabel, gbc_structuralHoursAssignedLabel);
        
        structuralWorkRateLabel = new JLabel("Work Rate");
        structuralWorkRateLabel.setHorizontalAlignment(SwingConstants.TRAILING);
        GridBagConstraints gbc_structuralWorkRateLabel = new GridBagConstraints();
        gbc_structuralWorkRateLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_structuralWorkRateLabel.insets = new Insets(0, 0, 5, 5);
        gbc_structuralWorkRateLabel.gridx = 4;
        gbc_structuralWorkRateLabel.gridy = 3;
        structuralPersonnelPane.add(structuralWorkRateLabel, gbc_structuralWorkRateLabel);
        
        structuralDateCompletedLabel = new JLabel("00/00/1987");
        structuralDateCompletedLabel.setHorizontalAlignment(SwingConstants.TRAILING);
        GridBagConstraints gbc_structuralDateCompletedLabel = new GridBagConstraints();
        gbc_structuralDateCompletedLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_structuralDateCompletedLabel.insets = new Insets(0, 0, 5, 5);
        gbc_structuralDateCompletedLabel.gridx = 5;
        gbc_structuralDateCompletedLabel.gridy = 3;
        structuralPersonnelPane.add(structuralDateCompletedLabel, gbc_structuralDateCompletedLabel);
        
        structuralBackcheckLabel = new JLabel("--");
        structuralBackcheckLabel.setHorizontalAlignment(SwingConstants.CENTER);
        GridBagConstraints gbc_structuralBackcheckLabel = new GridBagConstraints();
        gbc_structuralBackcheckLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_structuralBackcheckLabel.insets = new Insets(0, 0, 5, 5);
        gbc_structuralBackcheckLabel.gridx = 6;
        gbc_structuralBackcheckLabel.gridy = 3;
        structuralPersonnelPane.add(structuralBackcheckLabel, gbc_structuralBackcheckLabel);
        
        forceProtectionPersonnelPane = new JPanel();
		assignPersonnelPane.add(forceProtectionPersonnelPane);
        GridBagLayout gbl_forceProtectionPersonnelPane = new GridBagLayout();
        gbl_forceProtectionPersonnelPane.columnWidths = new int[]{0, 0, 310, 131, 138, 121, 80, 0, 0};
        gbl_forceProtectionPersonnelPane.rowHeights = new int[]{0, 0, 0, 0, 0};
        gbl_forceProtectionPersonnelPane.columnWeights = new double[]{0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
        gbl_forceProtectionPersonnelPane.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
        forceProtectionPersonnelPane.setLayout(gbl_forceProtectionPersonnelPane);
        
        forceProtectionVerticalStrut = Box.createVerticalStrut(20);
        GridBagConstraints gbc_forceProtectionVerticalStrut = new GridBagConstraints();
        gbc_forceProtectionVerticalStrut.insets = new Insets(0, 0, 5, 5);
        gbc_forceProtectionVerticalStrut.gridx = 1;
        gbc_forceProtectionVerticalStrut.gridy = 0;
        forceProtectionPersonnelPane.add(forceProtectionVerticalStrut, gbc_forceProtectionVerticalStrut);
        
        forceProtectionHorzizontalStrut = Box.createHorizontalStrut(20);
        GridBagConstraints gbc_forceProtectionHorzizontalStrut = new GridBagConstraints();
        gbc_forceProtectionHorzizontalStrut.insets = new Insets(0, 0, 5, 5);
        gbc_forceProtectionHorzizontalStrut.gridx = 0;
        gbc_forceProtectionHorzizontalStrut.gridy = 1;
        forceProtectionPersonnelPane.add(forceProtectionHorzizontalStrut, gbc_forceProtectionHorzizontalStrut);
        
        forceProtectionHorzizontalStrut_2 = Box.createHorizontalStrut(20);
        GridBagConstraints gbc_forceProtectionHorzizontalStrut_2 = new GridBagConstraints();
        gbc_forceProtectionHorzizontalStrut_2.insets = new Insets(0, 0, 5, 0);
        gbc_forceProtectionHorzizontalStrut_2.gridx = 7;
        gbc_forceProtectionHorzizontalStrut_2.gridy = 2;
        forceProtectionPersonnelPane.add(forceProtectionHorzizontalStrut_2, gbc_forceProtectionHorzizontalStrut_2);
				
		forceProtectionTitleLabel = new JLabel("Force Protection");
        forceProtectionTitleLabel.setOpaque(true);
        forceProtectionTitleLabel.setBackground(new Color(255, 255, 204));
        GridBagConstraints gbc_forceProtectionTitleLabel = new GridBagConstraints();
        gbc_forceProtectionTitleLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_forceProtectionTitleLabel.gridwidth = 6;
        gbc_forceProtectionTitleLabel.insets = new Insets(0, 0, 5, 5);
        gbc_forceProtectionTitleLabel.gridx = 1;
        gbc_forceProtectionTitleLabel.gridy = 1;
        forceProtectionPersonnelPane.add(forceProtectionTitleLabel, gbc_forceProtectionTitleLabel);
        
        forceProtectionFillerLabel = new JLabel("  ");
        forceProtectionFillerLabel.setOpaque(true);
        forceProtectionFillerLabel.setBackground(new Color(153, 153, 153));
        GridBagConstraints gbc_forceProtectionFillerLabel = new GridBagConstraints();
        gbc_forceProtectionFillerLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_forceProtectionFillerLabel.insets = new Insets(0, 0, 5, 5);
        gbc_forceProtectionFillerLabel.gridx = 1;
        gbc_forceProtectionFillerLabel.gridy = 2;
        forceProtectionPersonnelPane.add(forceProtectionFillerLabel, gbc_forceProtectionFillerLabel);
        
        forceProtectionAssigneeTitleLabel = new JLabel("Assignee");
        forceProtectionAssigneeTitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        forceProtectionAssigneeTitleLabel.setOpaque(true);
        forceProtectionAssigneeTitleLabel.setForeground(new Color(255, 255, 255));
        forceProtectionAssigneeTitleLabel.setBackground(new Color(153, 153, 153));
        GridBagConstraints gbc_forceProtectionAssigneeTitleLabel = new GridBagConstraints();
        gbc_forceProtectionAssigneeTitleLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_forceProtectionAssigneeTitleLabel.insets = new Insets(0, 0, 5, 5);
        gbc_forceProtectionAssigneeTitleLabel.gridx = 2;
        gbc_forceProtectionAssigneeTitleLabel.gridy = 2;
        forceProtectionPersonnelPane.add(forceProtectionAssigneeTitleLabel, gbc_forceProtectionAssigneeTitleLabel);
        
        forceProtectionHoursAssingedTitleLabel = new JLabel("Hours Assigned");
        forceProtectionHoursAssingedTitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        forceProtectionHoursAssingedTitleLabel.setOpaque(true);
        forceProtectionHoursAssingedTitleLabel.setForeground(new Color(255, 255, 255));
        forceProtectionHoursAssingedTitleLabel.setBackground(new Color(153, 153, 153));
        GridBagConstraints gbc_forceProtectionHoursAssingedTitleLabel = new GridBagConstraints();
        gbc_forceProtectionHoursAssingedTitleLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_forceProtectionHoursAssingedTitleLabel.insets = new Insets(0, 0, 5, 5);
        gbc_forceProtectionHoursAssingedTitleLabel.gridx = 3;
        gbc_forceProtectionHoursAssingedTitleLabel.gridy = 2;
        forceProtectionPersonnelPane.add(forceProtectionHoursAssingedTitleLabel, gbc_forceProtectionHoursAssingedTitleLabel);
        
        forceProtectionWorkRateTitleLabel = new JLabel("Work Rate");
        forceProtectionWorkRateTitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        forceProtectionWorkRateTitleLabel.setOpaque(true);
        forceProtectionWorkRateTitleLabel.setForeground(new Color(255, 255, 255));
        forceProtectionWorkRateTitleLabel.setBackground(new Color(153, 153, 153));
        GridBagConstraints gbc_forceProtectionWorkRateTitleLabel = new GridBagConstraints();
        gbc_forceProtectionWorkRateTitleLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_forceProtectionWorkRateTitleLabel.insets = new Insets(0, 0, 5, 5);
        gbc_forceProtectionWorkRateTitleLabel.gridx = 4;
        gbc_forceProtectionWorkRateTitleLabel.gridy = 2;
        forceProtectionPersonnelPane.add(forceProtectionWorkRateTitleLabel, gbc_forceProtectionWorkRateTitleLabel);
        
        forceProtectionDateCompleteTitleLabel = new JLabel("Date Completed");
        forceProtectionDateCompleteTitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        forceProtectionDateCompleteTitleLabel.setOpaque(true);
        forceProtectionDateCompleteTitleLabel.setForeground(new Color(255, 255, 255));
        forceProtectionDateCompleteTitleLabel.setBackground(new Color(153, 153, 153));
        GridBagConstraints gbc_forceProtectionDateCompleteTitleLabel = new GridBagConstraints();
        gbc_forceProtectionDateCompleteTitleLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_forceProtectionDateCompleteTitleLabel.insets = new Insets(0, 0, 5, 5);
        gbc_forceProtectionDateCompleteTitleLabel.gridx = 5;
        gbc_forceProtectionDateCompleteTitleLabel.gridy = 2;
        forceProtectionPersonnelPane.add(forceProtectionDateCompleteTitleLabel, gbc_forceProtectionDateCompleteTitleLabel);
        
        forceProtectionBackcheckTitleLabel = new JLabel("Backcheck");
        forceProtectionBackcheckTitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        forceProtectionBackcheckTitleLabel.setOpaque(true);
        forceProtectionBackcheckTitleLabel.setForeground(new Color(255, 255, 255));
        forceProtectionBackcheckTitleLabel.setBackground(new Color(153, 153, 153));
        GridBagConstraints gbc_forceProtectionBackcheckTitleLabel = new GridBagConstraints();
        gbc_forceProtectionBackcheckTitleLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_forceProtectionBackcheckTitleLabel.insets = new Insets(0, 0, 5, 5);
        gbc_forceProtectionBackcheckTitleLabel.gridx = 6;
        gbc_forceProtectionBackcheckTitleLabel.gridy = 2;
        forceProtectionPersonnelPane.add(forceProtectionBackcheckTitleLabel, gbc_forceProtectionBackcheckTitleLabel);
        
        forceProtectionAssigneeCheckBox = new JCheckBox("");
        GridBagConstraints gbc_forceProtectionAssigneeCheckBox = new GridBagConstraints();
        gbc_forceProtectionAssigneeCheckBox.insets = new Insets(0, 0, 5, 5);
        gbc_forceProtectionAssigneeCheckBox.gridx = 1;
        gbc_forceProtectionAssigneeCheckBox.gridy = 3;
        forceProtectionPersonnelPane.add(forceProtectionAssigneeCheckBox, gbc_forceProtectionAssigneeCheckBox);
        
        forceProtectionAssigneeName = new JLabel("Assignee Name");
        GridBagConstraints gbc_forceProtectionAssigneeName = new GridBagConstraints();
        gbc_forceProtectionAssigneeName.fill = GridBagConstraints.HORIZONTAL;
        gbc_forceProtectionAssigneeName.insets = new Insets(0, 0, 5, 5);
        gbc_forceProtectionAssigneeName.gridx = 2;
        gbc_forceProtectionAssigneeName.gridy = 3;
        forceProtectionPersonnelPane.add(forceProtectionAssigneeName, gbc_forceProtectionAssigneeName);
        
        forceProtectionHoursAssignedLabel = new JLabel("0.00");
        forceProtectionHoursAssignedLabel.setHorizontalAlignment(SwingConstants.TRAILING);
        GridBagConstraints gbc_forceProtectionHoursAssignedLabel = new GridBagConstraints();
        gbc_forceProtectionHoursAssignedLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_forceProtectionHoursAssignedLabel.insets = new Insets(0, 0, 5, 5);
        gbc_forceProtectionHoursAssignedLabel.gridx = 3;
        gbc_forceProtectionHoursAssignedLabel.gridy = 3;
        forceProtectionPersonnelPane.add(forceProtectionHoursAssignedLabel, gbc_forceProtectionHoursAssignedLabel);
        
        forceProtectionWorkRateLabel = new JLabel("Work Rate");
        forceProtectionWorkRateLabel.setHorizontalAlignment(SwingConstants.TRAILING);
        GridBagConstraints gbc_forceProtectionWorkRateLabel = new GridBagConstraints();
        gbc_forceProtectionWorkRateLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_forceProtectionWorkRateLabel.insets = new Insets(0, 0, 5, 5);
        gbc_forceProtectionWorkRateLabel.gridx = 4;
        gbc_forceProtectionWorkRateLabel.gridy = 3;
        forceProtectionPersonnelPane.add(forceProtectionWorkRateLabel, gbc_forceProtectionWorkRateLabel);
        
        forceProtectionDateCompletedLabel = new JLabel("00/00/1987");
        forceProtectionDateCompletedLabel.setHorizontalAlignment(SwingConstants.TRAILING);
        GridBagConstraints gbc_forceProtectionDateCompletedLabel = new GridBagConstraints();
        gbc_forceProtectionDateCompletedLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_forceProtectionDateCompletedLabel.insets = new Insets(0, 0, 5, 5);
        gbc_forceProtectionDateCompletedLabel.gridx = 5;
        gbc_forceProtectionDateCompletedLabel.gridy = 3;
        forceProtectionPersonnelPane.add(forceProtectionDateCompletedLabel, gbc_forceProtectionDateCompletedLabel);
        
        forceProtectionBackcheckLabel = new JLabel("--");
        forceProtectionBackcheckLabel.setHorizontalAlignment(SwingConstants.CENTER);
        GridBagConstraints gbc_forceProtectionBackcheckLabel = new GridBagConstraints();
        gbc_forceProtectionBackcheckLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_forceProtectionBackcheckLabel.insets = new Insets(0, 0, 5, 5);
        gbc_forceProtectionBackcheckLabel.gridx = 6;
        gbc_forceProtectionBackcheckLabel.gridy = 3;
        forceProtectionPersonnelPane.add(forceProtectionBackcheckLabel, gbc_forceProtectionBackcheckLabel);
               
        mechanicalPersonnelPane = new JPanel();
		assignPersonnelPane.add(mechanicalPersonnelPane);
        GridBagLayout gbl_mechanicalPersonnelPane = new GridBagLayout();
        gbl_mechanicalPersonnelPane.columnWidths = new int[]{0, 0, 310, 131, 138, 121, 80, 0, 0};
        gbl_mechanicalPersonnelPane.rowHeights = new int[]{0, 0, 0, 0, 0};
        gbl_mechanicalPersonnelPane.columnWeights = new double[]{0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
        gbl_mechanicalPersonnelPane.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
        mechanicalPersonnelPane.setLayout(gbl_mechanicalPersonnelPane);
        
        mechanicalVerticalStrut = Box.createVerticalStrut(20);
        GridBagConstraints gbc_mechanicalVerticalStrut = new GridBagConstraints();
        gbc_mechanicalVerticalStrut.insets = new Insets(0, 0, 5, 5);
        gbc_mechanicalVerticalStrut.gridx = 1;
        gbc_mechanicalVerticalStrut.gridy = 0;
        mechanicalPersonnelPane.add(mechanicalVerticalStrut, gbc_mechanicalVerticalStrut);
        
        mechanicalHorzizontalStrut = Box.createHorizontalStrut(20);
        GridBagConstraints gbc_mechanicalHorzizontalStrut = new GridBagConstraints();
        gbc_mechanicalHorzizontalStrut.insets = new Insets(0, 0, 5, 5);
        gbc_mechanicalHorzizontalStrut.gridx = 0;
        gbc_mechanicalHorzizontalStrut.gridy = 1;
        mechanicalPersonnelPane.add(mechanicalHorzizontalStrut, gbc_mechanicalHorzizontalStrut);
        
        mechanicalHorzizontalStrut_2 = Box.createHorizontalStrut(20);
        GridBagConstraints gbc_mechanicalHorzizontalStrut_2 = new GridBagConstraints();
        gbc_mechanicalHorzizontalStrut_2.insets = new Insets(0, 0, 5, 0);
        gbc_mechanicalHorzizontalStrut_2.gridx = 7;
        gbc_mechanicalHorzizontalStrut_2.gridy = 2;
        mechanicalPersonnelPane.add(mechanicalHorzizontalStrut_2, gbc_mechanicalHorzizontalStrut_2);
				
		mechanicalTitleLabel = new JLabel("Mechanical");
        mechanicalTitleLabel.setOpaque(true);
        mechanicalTitleLabel.setBackground(new Color(255, 255, 204));
        GridBagConstraints gbc_mechanicalTitleLabel = new GridBagConstraints();
        gbc_mechanicalTitleLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_mechanicalTitleLabel.gridwidth = 6;
        gbc_mechanicalTitleLabel.insets = new Insets(0, 0, 5, 5);
        gbc_mechanicalTitleLabel.gridx = 1;
        gbc_mechanicalTitleLabel.gridy = 1;
        mechanicalPersonnelPane.add(mechanicalTitleLabel, gbc_mechanicalTitleLabel);
        
        mechanicalFillerLabel = new JLabel("  ");
        mechanicalFillerLabel.setOpaque(true);
        mechanicalFillerLabel.setBackground(new Color(153, 153, 153));
        GridBagConstraints gbc_mechanicalFillerLabel = new GridBagConstraints();
        gbc_mechanicalFillerLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_mechanicalFillerLabel.insets = new Insets(0, 0, 5, 5);
        gbc_mechanicalFillerLabel.gridx = 1;
        gbc_mechanicalFillerLabel.gridy = 2;
        mechanicalPersonnelPane.add(mechanicalFillerLabel, gbc_mechanicalFillerLabel);
        
        mechanicalAssigneeTitleLabel = new JLabel("Assignee");
        mechanicalAssigneeTitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        mechanicalAssigneeTitleLabel.setOpaque(true);
        mechanicalAssigneeTitleLabel.setForeground(new Color(255, 255, 255));
        mechanicalAssigneeTitleLabel.setBackground(new Color(153, 153, 153));
        GridBagConstraints gbc_mechanicalAssigneeTitleLabel = new GridBagConstraints();
        gbc_mechanicalAssigneeTitleLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_mechanicalAssigneeTitleLabel.insets = new Insets(0, 0, 5, 5);
        gbc_mechanicalAssigneeTitleLabel.gridx = 2;
        gbc_mechanicalAssigneeTitleLabel.gridy = 2;
        mechanicalPersonnelPane.add(mechanicalAssigneeTitleLabel, gbc_mechanicalAssigneeTitleLabel);
        
        mechanicalHoursAssingedTitleLabel = new JLabel("Hours Assigned");
        mechanicalHoursAssingedTitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        mechanicalHoursAssingedTitleLabel.setOpaque(true);
        mechanicalHoursAssingedTitleLabel.setForeground(new Color(255, 255, 255));
        mechanicalHoursAssingedTitleLabel.setBackground(new Color(153, 153, 153));
        GridBagConstraints gbc_mechanicalHoursAssingedTitleLabel = new GridBagConstraints();
        gbc_mechanicalHoursAssingedTitleLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_mechanicalHoursAssingedTitleLabel.insets = new Insets(0, 0, 5, 5);
        gbc_mechanicalHoursAssingedTitleLabel.gridx = 3;
        gbc_mechanicalHoursAssingedTitleLabel.gridy = 2;
        mechanicalPersonnelPane.add(mechanicalHoursAssingedTitleLabel, gbc_mechanicalHoursAssingedTitleLabel);
        
        mechanicalWorkRateTitleLabel = new JLabel("Work Rate");
        mechanicalWorkRateTitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        mechanicalWorkRateTitleLabel.setOpaque(true);
        mechanicalWorkRateTitleLabel.setForeground(new Color(255, 255, 255));
        mechanicalWorkRateTitleLabel.setBackground(new Color(153, 153, 153));
        GridBagConstraints gbc_mechanicalWorkRateTitleLabel = new GridBagConstraints();
        gbc_mechanicalWorkRateTitleLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_mechanicalWorkRateTitleLabel.insets = new Insets(0, 0, 5, 5);
        gbc_mechanicalWorkRateTitleLabel.gridx = 4;
        gbc_mechanicalWorkRateTitleLabel.gridy = 2;
        mechanicalPersonnelPane.add(mechanicalWorkRateTitleLabel, gbc_mechanicalWorkRateTitleLabel);
        
        mechanicalDateCompleteTitleLabel = new JLabel("Date Completed");
        mechanicalDateCompleteTitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        mechanicalDateCompleteTitleLabel.setOpaque(true);
        mechanicalDateCompleteTitleLabel.setForeground(new Color(255, 255, 255));
        mechanicalDateCompleteTitleLabel.setBackground(new Color(153, 153, 153));
        GridBagConstraints gbc_mechanicalDateCompleteTitleLabel = new GridBagConstraints();
        gbc_mechanicalDateCompleteTitleLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_mechanicalDateCompleteTitleLabel.insets = new Insets(0, 0, 5, 5);
        gbc_mechanicalDateCompleteTitleLabel.gridx = 5;
        gbc_mechanicalDateCompleteTitleLabel.gridy = 2;
        mechanicalPersonnelPane.add(mechanicalDateCompleteTitleLabel, gbc_mechanicalDateCompleteTitleLabel);
        
        mechanicalBackcheckTitleLabel = new JLabel("Backcheck");
        mechanicalBackcheckTitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        mechanicalBackcheckTitleLabel.setOpaque(true);
        mechanicalBackcheckTitleLabel.setForeground(new Color(255, 255, 255));
        mechanicalBackcheckTitleLabel.setBackground(new Color(153, 153, 153));
        GridBagConstraints gbc_mechanicalBackcheckTitleLabel = new GridBagConstraints();
        gbc_mechanicalBackcheckTitleLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_mechanicalBackcheckTitleLabel.insets = new Insets(0, 0, 5, 5);
        gbc_mechanicalBackcheckTitleLabel.gridx = 6;
        gbc_mechanicalBackcheckTitleLabel.gridy = 2;
        mechanicalPersonnelPane.add(mechanicalBackcheckTitleLabel, gbc_mechanicalBackcheckTitleLabel);
        
        mechanicalAssigneeCheckBox = new JCheckBox("");
        GridBagConstraints gbc_mechanicalAssigneeCheckBox = new GridBagConstraints();
        gbc_mechanicalAssigneeCheckBox.insets = new Insets(0, 0, 5, 5);
        gbc_mechanicalAssigneeCheckBox.gridx = 1;
        gbc_mechanicalAssigneeCheckBox.gridy = 3;
        mechanicalPersonnelPane.add(mechanicalAssigneeCheckBox, gbc_mechanicalAssigneeCheckBox);
        
        mechanicalAssigneeName = new JLabel("Assignee Name");
        GridBagConstraints gbc_mechanicalAssigneeName = new GridBagConstraints();
        gbc_mechanicalAssigneeName.fill = GridBagConstraints.HORIZONTAL;
        gbc_mechanicalAssigneeName.insets = new Insets(0, 0, 5, 5);
        gbc_mechanicalAssigneeName.gridx = 2;
        gbc_mechanicalAssigneeName.gridy = 3;
        mechanicalPersonnelPane.add(mechanicalAssigneeName, gbc_mechanicalAssigneeName);
        
        mechanicalHoursAssignedLabel = new JLabel("0.00");
        mechanicalHoursAssignedLabel.setHorizontalAlignment(SwingConstants.TRAILING);
        GridBagConstraints gbc_mechanicalHoursAssignedLabel = new GridBagConstraints();
        gbc_mechanicalHoursAssignedLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_mechanicalHoursAssignedLabel.insets = new Insets(0, 0, 5, 5);
        gbc_mechanicalHoursAssignedLabel.gridx = 3;
        gbc_mechanicalHoursAssignedLabel.gridy = 3;
        mechanicalPersonnelPane.add(mechanicalHoursAssignedLabel, gbc_mechanicalHoursAssignedLabel);
        
        mechanicalWorkRateLabel = new JLabel("Work Rate");
        mechanicalWorkRateLabel.setHorizontalAlignment(SwingConstants.TRAILING);
        GridBagConstraints gbc_mechanicalWorkRateLabel = new GridBagConstraints();
        gbc_mechanicalWorkRateLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_mechanicalWorkRateLabel.insets = new Insets(0, 0, 5, 5);
        gbc_mechanicalWorkRateLabel.gridx = 4;
        gbc_mechanicalWorkRateLabel.gridy = 3;
        mechanicalPersonnelPane.add(mechanicalWorkRateLabel, gbc_mechanicalWorkRateLabel);
        
        mechanicalDateCompletedLabel = new JLabel("00/00/1987");
        mechanicalDateCompletedLabel.setHorizontalAlignment(SwingConstants.TRAILING);
        GridBagConstraints gbc_mechanicalDateCompletedLabel = new GridBagConstraints();
        gbc_mechanicalDateCompletedLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_mechanicalDateCompletedLabel.insets = new Insets(0, 0, 5, 5);
        gbc_mechanicalDateCompletedLabel.gridx = 5;
        gbc_mechanicalDateCompletedLabel.gridy = 3;
        mechanicalPersonnelPane.add(mechanicalDateCompletedLabel, gbc_mechanicalDateCompletedLabel);
        
        mechanicalBackcheckLabel = new JLabel("--");
        mechanicalBackcheckLabel.setHorizontalAlignment(SwingConstants.CENTER);
        GridBagConstraints gbc_mechanicalBackcheckLabel = new GridBagConstraints();
        gbc_mechanicalBackcheckLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_mechanicalBackcheckLabel.insets = new Insets(0, 0, 5, 5);
        gbc_mechanicalBackcheckLabel.gridx = 6;
        gbc_mechanicalBackcheckLabel.gridy = 3;
        mechanicalPersonnelPane.add(mechanicalBackcheckLabel, gbc_mechanicalBackcheckLabel);
        
        fireProtectionPersonnelPane = new JPanel();
		assignPersonnelPane.add(fireProtectionPersonnelPane);
        GridBagLayout gbl_fireProtectionPersonnelPane = new GridBagLayout();
        gbl_fireProtectionPersonnelPane.columnWidths = new int[]{0, 0, 310, 131, 138, 121, 80, 0, 0};
        gbl_fireProtectionPersonnelPane.rowHeights = new int[]{0, 0, 0, 0, 0};
        gbl_fireProtectionPersonnelPane.columnWeights = new double[]{0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
        gbl_fireProtectionPersonnelPane.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
        fireProtectionPersonnelPane.setLayout(gbl_fireProtectionPersonnelPane);
        
        fireProtectionVerticalStrut = Box.createVerticalStrut(20);
        GridBagConstraints gbc_fireProtectionVerticalStrut = new GridBagConstraints();
        gbc_fireProtectionVerticalStrut.insets = new Insets(0, 0, 5, 5);
        gbc_fireProtectionVerticalStrut.gridx = 1;
        gbc_fireProtectionVerticalStrut.gridy = 0;
        fireProtectionPersonnelPane.add(fireProtectionVerticalStrut, gbc_fireProtectionVerticalStrut);
        
        fireProtectionHorzizontalStrut = Box.createHorizontalStrut(20);
        GridBagConstraints gbc_fireProtectionHorzizontalStrut = new GridBagConstraints();
        gbc_fireProtectionHorzizontalStrut.insets = new Insets(0, 0, 5, 5);
        gbc_fireProtectionHorzizontalStrut.gridx = 0;
        gbc_fireProtectionHorzizontalStrut.gridy = 1;
        fireProtectionPersonnelPane.add(fireProtectionHorzizontalStrut, gbc_fireProtectionHorzizontalStrut);
        
        fireProtectionHorzizontalStrut_2 = Box.createHorizontalStrut(20);
        GridBagConstraints gbc_fireProtectionHorzizontalStrut_2 = new GridBagConstraints();
        gbc_fireProtectionHorzizontalStrut_2.insets = new Insets(0, 0, 5, 0);
        gbc_fireProtectionHorzizontalStrut_2.gridx = 7;
        gbc_fireProtectionHorzizontalStrut_2.gridy = 2;
        fireProtectionPersonnelPane.add(fireProtectionHorzizontalStrut_2, gbc_fireProtectionHorzizontalStrut_2);
				
		fireProtectionTitleLabel = new JLabel("Fire Protection");
        fireProtectionTitleLabel.setOpaque(true);
        fireProtectionTitleLabel.setBackground(new Color(255, 255, 204));
        GridBagConstraints gbc_fireProtectionTitleLabel = new GridBagConstraints();
        gbc_fireProtectionTitleLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_fireProtectionTitleLabel.gridwidth = 6;
        gbc_fireProtectionTitleLabel.insets = new Insets(0, 0, 5, 5);
        gbc_fireProtectionTitleLabel.gridx = 1;
        gbc_fireProtectionTitleLabel.gridy = 1;
        fireProtectionPersonnelPane.add(fireProtectionTitleLabel, gbc_fireProtectionTitleLabel);
        
        fireProtectionFillerLabel = new JLabel("  ");
        fireProtectionFillerLabel.setOpaque(true);
        fireProtectionFillerLabel.setBackground(new Color(153, 153, 153));
        GridBagConstraints gbc_fireProtectionFillerLabel = new GridBagConstraints();
        gbc_fireProtectionFillerLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_fireProtectionFillerLabel.insets = new Insets(0, 0, 5, 5);
        gbc_fireProtectionFillerLabel.gridx = 1;
        gbc_fireProtectionFillerLabel.gridy = 2;
        fireProtectionPersonnelPane.add(fireProtectionFillerLabel, gbc_fireProtectionFillerLabel);
        
        fireProtectionAssigneeTitleLabel = new JLabel("Assignee");
        fireProtectionAssigneeTitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        fireProtectionAssigneeTitleLabel.setOpaque(true);
        fireProtectionAssigneeTitleLabel.setForeground(new Color(255, 255, 255));
        fireProtectionAssigneeTitleLabel.setBackground(new Color(153, 153, 153));
        GridBagConstraints gbc_fireProtectionAssigneeTitleLabel = new GridBagConstraints();
        gbc_fireProtectionAssigneeTitleLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_fireProtectionAssigneeTitleLabel.insets = new Insets(0, 0, 5, 5);
        gbc_fireProtectionAssigneeTitleLabel.gridx = 2;
        gbc_fireProtectionAssigneeTitleLabel.gridy = 2;
        fireProtectionPersonnelPane.add(fireProtectionAssigneeTitleLabel, gbc_fireProtectionAssigneeTitleLabel);
        
        fireProtectionHoursAssingedTitleLabel = new JLabel("Hours Assigned");
        fireProtectionHoursAssingedTitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        fireProtectionHoursAssingedTitleLabel.setOpaque(true);
        fireProtectionHoursAssingedTitleLabel.setForeground(new Color(255, 255, 255));
        fireProtectionHoursAssingedTitleLabel.setBackground(new Color(153, 153, 153));
        GridBagConstraints gbc_fireProtectionHoursAssingedTitleLabel = new GridBagConstraints();
        gbc_fireProtectionHoursAssingedTitleLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_fireProtectionHoursAssingedTitleLabel.insets = new Insets(0, 0, 5, 5);
        gbc_fireProtectionHoursAssingedTitleLabel.gridx = 3;
        gbc_fireProtectionHoursAssingedTitleLabel.gridy = 2;
        fireProtectionPersonnelPane.add(fireProtectionHoursAssingedTitleLabel, gbc_fireProtectionHoursAssingedTitleLabel);
        
        fireProtectionWorkRateTitleLabel = new JLabel("Work Rate");
        fireProtectionWorkRateTitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        fireProtectionWorkRateTitleLabel.setOpaque(true);
        fireProtectionWorkRateTitleLabel.setForeground(new Color(255, 255, 255));
        fireProtectionWorkRateTitleLabel.setBackground(new Color(153, 153, 153));
        GridBagConstraints gbc_fireProtectionWorkRateTitleLabel = new GridBagConstraints();
        gbc_fireProtectionWorkRateTitleLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_fireProtectionWorkRateTitleLabel.insets = new Insets(0, 0, 5, 5);
        gbc_fireProtectionWorkRateTitleLabel.gridx = 4;
        gbc_fireProtectionWorkRateTitleLabel.gridy = 2;
        fireProtectionPersonnelPane.add(fireProtectionWorkRateTitleLabel, gbc_fireProtectionWorkRateTitleLabel);
        
        fireProtectionDateCompleteTitleLabel = new JLabel("Date Completed");
        fireProtectionDateCompleteTitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        fireProtectionDateCompleteTitleLabel.setOpaque(true);
        fireProtectionDateCompleteTitleLabel.setForeground(new Color(255, 255, 255));
        fireProtectionDateCompleteTitleLabel.setBackground(new Color(153, 153, 153));
        GridBagConstraints gbc_fireProtectionDateCompleteTitleLabel = new GridBagConstraints();
        gbc_fireProtectionDateCompleteTitleLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_fireProtectionDateCompleteTitleLabel.insets = new Insets(0, 0, 5, 5);
        gbc_fireProtectionDateCompleteTitleLabel.gridx = 5;
        gbc_fireProtectionDateCompleteTitleLabel.gridy = 2;
        fireProtectionPersonnelPane.add(fireProtectionDateCompleteTitleLabel, gbc_fireProtectionDateCompleteTitleLabel);
        
        fireProtectionBackcheckTitleLabel = new JLabel("Backcheck");
        fireProtectionBackcheckTitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        fireProtectionBackcheckTitleLabel.setOpaque(true);
        fireProtectionBackcheckTitleLabel.setForeground(new Color(255, 255, 255));
        fireProtectionBackcheckTitleLabel.setBackground(new Color(153, 153, 153));
        GridBagConstraints gbc_fireProtectionBackcheckTitleLabel = new GridBagConstraints();
        gbc_fireProtectionBackcheckTitleLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_fireProtectionBackcheckTitleLabel.insets = new Insets(0, 0, 5, 5);
        gbc_fireProtectionBackcheckTitleLabel.gridx = 6;
        gbc_fireProtectionBackcheckTitleLabel.gridy = 2;
        fireProtectionPersonnelPane.add(fireProtectionBackcheckTitleLabel, gbc_fireProtectionBackcheckTitleLabel);
        
        fireProtectionAssigneeCheckBox = new JCheckBox("");
        GridBagConstraints gbc_fireProtectionAssigneeCheckBox = new GridBagConstraints();
        gbc_fireProtectionAssigneeCheckBox.insets = new Insets(0, 0, 5, 5);
        gbc_fireProtectionAssigneeCheckBox.gridx = 1;
        gbc_fireProtectionAssigneeCheckBox.gridy = 3;
        fireProtectionPersonnelPane.add(fireProtectionAssigneeCheckBox, gbc_fireProtectionAssigneeCheckBox);
        
        fireProtectionAssigneeName = new JLabel("Assignee Name");
        GridBagConstraints gbc_fireProtectionAssigneeName = new GridBagConstraints();
        gbc_fireProtectionAssigneeName.fill = GridBagConstraints.HORIZONTAL;
        gbc_fireProtectionAssigneeName.insets = new Insets(0, 0, 5, 5);
        gbc_fireProtectionAssigneeName.gridx = 2;
        gbc_fireProtectionAssigneeName.gridy = 3;
        fireProtectionPersonnelPane.add(fireProtectionAssigneeName, gbc_fireProtectionAssigneeName);
        
        fireProtectionHoursAssignedLabel = new JLabel("0.00");
        fireProtectionHoursAssignedLabel.setHorizontalAlignment(SwingConstants.TRAILING);
        GridBagConstraints gbc_fireProtectionHoursAssignedLabel = new GridBagConstraints();
        gbc_fireProtectionHoursAssignedLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_fireProtectionHoursAssignedLabel.insets = new Insets(0, 0, 5, 5);
        gbc_fireProtectionHoursAssignedLabel.gridx = 3;
        gbc_fireProtectionHoursAssignedLabel.gridy = 3;
        fireProtectionPersonnelPane.add(fireProtectionHoursAssignedLabel, gbc_fireProtectionHoursAssignedLabel);
        
        fireProtectionWorkRateLabel = new JLabel("Work Rate");
        fireProtectionWorkRateLabel.setHorizontalAlignment(SwingConstants.TRAILING);
        GridBagConstraints gbc_fireProtectionWorkRateLabel = new GridBagConstraints();
        gbc_fireProtectionWorkRateLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_fireProtectionWorkRateLabel.insets = new Insets(0, 0, 5, 5);
        gbc_fireProtectionWorkRateLabel.gridx = 4;
        gbc_fireProtectionWorkRateLabel.gridy = 3;
        fireProtectionPersonnelPane.add(fireProtectionWorkRateLabel, gbc_fireProtectionWorkRateLabel);
        
        fireProtectionDateCompletedLabel = new JLabel("00/00/1987");
        fireProtectionDateCompletedLabel.setHorizontalAlignment(SwingConstants.TRAILING);
        GridBagConstraints gbc_fireProtectionDateCompletedLabel = new GridBagConstraints();
        gbc_fireProtectionDateCompletedLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_fireProtectionDateCompletedLabel.insets = new Insets(0, 0, 5, 5);
        gbc_fireProtectionDateCompletedLabel.gridx = 5;
        gbc_fireProtectionDateCompletedLabel.gridy = 3;
        fireProtectionPersonnelPane.add(fireProtectionDateCompletedLabel, gbc_fireProtectionDateCompletedLabel);
        
        fireProtectionBackcheckLabel = new JLabel("--");
        fireProtectionBackcheckLabel.setHorizontalAlignment(SwingConstants.CENTER);
        GridBagConstraints gbc_fireProtectionBackcheckLabel = new GridBagConstraints();
        gbc_fireProtectionBackcheckLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_fireProtectionBackcheckLabel.insets = new Insets(0, 0, 5, 5);
        gbc_fireProtectionBackcheckLabel.gridx = 6;
        gbc_fireProtectionBackcheckLabel.gridy = 3;
        fireProtectionPersonnelPane.add(fireProtectionBackcheckLabel, gbc_fireProtectionBackcheckLabel);
        
        electricalPersonnelPane = new JPanel();
		assignPersonnelPane.add(electricalPersonnelPane);
        GridBagLayout gbl_electricalPersonnelPane = new GridBagLayout();
        gbl_electricalPersonnelPane.columnWidths = new int[]{0, 0, 310, 131, 138, 121, 80, 0, 0};
        gbl_electricalPersonnelPane.rowHeights = new int[]{0, 0, 0, 0, 0};
        gbl_electricalPersonnelPane.columnWeights = new double[]{0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
        gbl_electricalPersonnelPane.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
        electricalPersonnelPane.setLayout(gbl_electricalPersonnelPane);
        
        electricalVerticalStrut = Box.createVerticalStrut(20);
        GridBagConstraints gbc_electricalVerticalStrut = new GridBagConstraints();
        gbc_electricalVerticalStrut.insets = new Insets(0, 0, 5, 5);
        gbc_electricalVerticalStrut.gridx = 1;
        gbc_electricalVerticalStrut.gridy = 0;
        electricalPersonnelPane.add(electricalVerticalStrut, gbc_electricalVerticalStrut);
        
        electricalHorzizontalStrut = Box.createHorizontalStrut(20);
        GridBagConstraints gbc_electricalHorzizontalStrut = new GridBagConstraints();
        gbc_electricalHorzizontalStrut.insets = new Insets(0, 0, 5, 5);
        gbc_electricalHorzizontalStrut.gridx = 0;
        gbc_electricalHorzizontalStrut.gridy = 1;
        electricalPersonnelPane.add(electricalHorzizontalStrut, gbc_electricalHorzizontalStrut);
        
        electricalHorzizontalStrut_2 = Box.createHorizontalStrut(20);
        GridBagConstraints gbc_electricalHorzizontalStrut_2 = new GridBagConstraints();
        gbc_electricalHorzizontalStrut_2.insets = new Insets(0, 0, 5, 0);
        gbc_electricalHorzizontalStrut_2.gridx = 7;
        gbc_electricalHorzizontalStrut_2.gridy = 2;
        electricalPersonnelPane.add(electricalHorzizontalStrut_2, gbc_electricalHorzizontalStrut_2);
				
		electricalTitleLabel = new JLabel("Electrical");
        electricalTitleLabel.setOpaque(true);
        electricalTitleLabel.setBackground(new Color(255, 255, 204));
        GridBagConstraints gbc_electricalTitleLabel = new GridBagConstraints();
        gbc_electricalTitleLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_electricalTitleLabel.gridwidth = 6;
        gbc_electricalTitleLabel.insets = new Insets(0, 0, 5, 5);
        gbc_electricalTitleLabel.gridx = 1;
        gbc_electricalTitleLabel.gridy = 1;
        electricalPersonnelPane.add(electricalTitleLabel, gbc_electricalTitleLabel);
        
        electricalFillerLabel = new JLabel("  ");
        electricalFillerLabel.setOpaque(true);
        electricalFillerLabel.setBackground(new Color(153, 153, 153));
        GridBagConstraints gbc_electricalFillerLabel = new GridBagConstraints();
        gbc_electricalFillerLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_electricalFillerLabel.insets = new Insets(0, 0, 5, 5);
        gbc_electricalFillerLabel.gridx = 1;
        gbc_electricalFillerLabel.gridy = 2;
        electricalPersonnelPane.add(electricalFillerLabel, gbc_electricalFillerLabel);
        
        electricalAssigneeTitleLabel = new JLabel("Assignee");
        electricalAssigneeTitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        electricalAssigneeTitleLabel.setOpaque(true);
        electricalAssigneeTitleLabel.setForeground(new Color(255, 255, 255));
        electricalAssigneeTitleLabel.setBackground(new Color(153, 153, 153));
        GridBagConstraints gbc_electricalAssigneeTitleLabel = new GridBagConstraints();
        gbc_electricalAssigneeTitleLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_electricalAssigneeTitleLabel.insets = new Insets(0, 0, 5, 5);
        gbc_electricalAssigneeTitleLabel.gridx = 2;
        gbc_electricalAssigneeTitleLabel.gridy = 2;
        electricalPersonnelPane.add(electricalAssigneeTitleLabel, gbc_electricalAssigneeTitleLabel);
        
        electricalHoursAssingedTitleLabel = new JLabel("Hours Assigned");
        electricalHoursAssingedTitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        electricalHoursAssingedTitleLabel.setOpaque(true);
        electricalHoursAssingedTitleLabel.setForeground(new Color(255, 255, 255));
        electricalHoursAssingedTitleLabel.setBackground(new Color(153, 153, 153));
        GridBagConstraints gbc_electricalHoursAssingedTitleLabel = new GridBagConstraints();
        gbc_electricalHoursAssingedTitleLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_electricalHoursAssingedTitleLabel.insets = new Insets(0, 0, 5, 5);
        gbc_electricalHoursAssingedTitleLabel.gridx = 3;
        gbc_electricalHoursAssingedTitleLabel.gridy = 2;
        electricalPersonnelPane.add(electricalHoursAssingedTitleLabel, gbc_electricalHoursAssingedTitleLabel);
        
        electricalWorkRateTitleLabel = new JLabel("Work Rate");
        electricalWorkRateTitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        electricalWorkRateTitleLabel.setOpaque(true);
        electricalWorkRateTitleLabel.setForeground(new Color(255, 255, 255));
        electricalWorkRateTitleLabel.setBackground(new Color(153, 153, 153));
        GridBagConstraints gbc_electricalWorkRateTitleLabel = new GridBagConstraints();
        gbc_electricalWorkRateTitleLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_electricalWorkRateTitleLabel.insets = new Insets(0, 0, 5, 5);
        gbc_electricalWorkRateTitleLabel.gridx = 4;
        gbc_electricalWorkRateTitleLabel.gridy = 2;
        electricalPersonnelPane.add(electricalWorkRateTitleLabel, gbc_electricalWorkRateTitleLabel);
        
        electricalDateCompleteTitleLabel = new JLabel("Date Completed");
        electricalDateCompleteTitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        electricalDateCompleteTitleLabel.setOpaque(true);
        electricalDateCompleteTitleLabel.setForeground(new Color(255, 255, 255));
        electricalDateCompleteTitleLabel.setBackground(new Color(153, 153, 153));
        GridBagConstraints gbc_electricalDateCompleteTitleLabel = new GridBagConstraints();
        gbc_electricalDateCompleteTitleLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_electricalDateCompleteTitleLabel.insets = new Insets(0, 0, 5, 5);
        gbc_electricalDateCompleteTitleLabel.gridx = 5;
        gbc_electricalDateCompleteTitleLabel.gridy = 2;
        electricalPersonnelPane.add(electricalDateCompleteTitleLabel, gbc_electricalDateCompleteTitleLabel);
        
        electricalBackcheckTitleLabel = new JLabel("Backcheck");
        electricalBackcheckTitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        electricalBackcheckTitleLabel.setOpaque(true);
        electricalBackcheckTitleLabel.setForeground(new Color(255, 255, 255));
        electricalBackcheckTitleLabel.setBackground(new Color(153, 153, 153));
        GridBagConstraints gbc_electricalBackcheckTitleLabel = new GridBagConstraints();
        gbc_electricalBackcheckTitleLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_electricalBackcheckTitleLabel.insets = new Insets(0, 0, 5, 5);
        gbc_electricalBackcheckTitleLabel.gridx = 6;
        gbc_electricalBackcheckTitleLabel.gridy = 2;
        electricalPersonnelPane.add(electricalBackcheckTitleLabel, gbc_electricalBackcheckTitleLabel);
        
        electricalAssigneeCheckBox = new JCheckBox("");
        GridBagConstraints gbc_electricalAssigneeCheckBox = new GridBagConstraints();
        gbc_electricalAssigneeCheckBox.insets = new Insets(0, 0, 5, 5);
        gbc_electricalAssigneeCheckBox.gridx = 1;
        gbc_electricalAssigneeCheckBox.gridy = 3;
        electricalPersonnelPane.add(electricalAssigneeCheckBox, gbc_electricalAssigneeCheckBox);
        
        electricalAssigneeName = new JLabel("Assignee Name");
        GridBagConstraints gbc_electricalAssigneeName = new GridBagConstraints();
        gbc_electricalAssigneeName.fill = GridBagConstraints.HORIZONTAL;
        gbc_electricalAssigneeName.insets = new Insets(0, 0, 5, 5);
        gbc_electricalAssigneeName.gridx = 2;
        gbc_electricalAssigneeName.gridy = 3;
        electricalPersonnelPane.add(electricalAssigneeName, gbc_electricalAssigneeName);
        
        electricalHoursAssignedLabel = new JLabel("0.00");
        electricalHoursAssignedLabel.setHorizontalAlignment(SwingConstants.TRAILING);
        GridBagConstraints gbc_electricalHoursAssignedLabel = new GridBagConstraints();
        gbc_electricalHoursAssignedLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_electricalHoursAssignedLabel.insets = new Insets(0, 0, 5, 5);
        gbc_electricalHoursAssignedLabel.gridx = 3;
        gbc_electricalHoursAssignedLabel.gridy = 3;
        electricalPersonnelPane.add(electricalHoursAssignedLabel, gbc_electricalHoursAssignedLabel);
        
        electricalWorkRateLabel = new JLabel("Work Rate");
        electricalWorkRateLabel.setHorizontalAlignment(SwingConstants.TRAILING);
        GridBagConstraints gbc_electricalWorkRateLabel = new GridBagConstraints();
        gbc_electricalWorkRateLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_electricalWorkRateLabel.insets = new Insets(0, 0, 5, 5);
        gbc_electricalWorkRateLabel.gridx = 4;
        gbc_electricalWorkRateLabel.gridy = 3;
        electricalPersonnelPane.add(electricalWorkRateLabel, gbc_electricalWorkRateLabel);
        
        electricalDateCompletedLabel = new JLabel("00/00/1987");
        electricalDateCompletedLabel.setHorizontalAlignment(SwingConstants.TRAILING);
        GridBagConstraints gbc_electricalDateCompletedLabel = new GridBagConstraints();
        gbc_electricalDateCompletedLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_electricalDateCompletedLabel.insets = new Insets(0, 0, 5, 5);
        gbc_electricalDateCompletedLabel.gridx = 5;
        gbc_electricalDateCompletedLabel.gridy = 3;
        electricalPersonnelPane.add(electricalDateCompletedLabel, gbc_electricalDateCompletedLabel);
        
        electricalBackcheckLabel = new JLabel("--");
        electricalBackcheckLabel.setHorizontalAlignment(SwingConstants.CENTER);
        GridBagConstraints gbc_electricalBackcheckLabel = new GridBagConstraints();
        gbc_electricalBackcheckLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_electricalBackcheckLabel.insets = new Insets(0, 0, 5, 5);
        gbc_electricalBackcheckLabel.gridx = 6;
        gbc_electricalBackcheckLabel.gridy = 3;
        electricalPersonnelPane.add(electricalBackcheckLabel, gbc_electricalBackcheckLabel);
        
        communicationsPersonnelPane = new JPanel();
		assignPersonnelPane.add(communicationsPersonnelPane);
        GridBagLayout gbl_communicationsPersonnelPane = new GridBagLayout();
        gbl_communicationsPersonnelPane.columnWidths = new int[]{0, 0, 310, 131, 138, 121, 80, 0, 0};
        gbl_communicationsPersonnelPane.rowHeights = new int[]{0, 0, 0, 0, 0};
        gbl_communicationsPersonnelPane.columnWeights = new double[]{0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
        gbl_communicationsPersonnelPane.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
        communicationsPersonnelPane.setLayout(gbl_communicationsPersonnelPane);
        
        communicationsVerticalStrut = Box.createVerticalStrut(20);
        GridBagConstraints gbc_communicationsVerticalStrut = new GridBagConstraints();
        gbc_communicationsVerticalStrut.insets = new Insets(0, 0, 5, 5);
        gbc_communicationsVerticalStrut.gridx = 1;
        gbc_communicationsVerticalStrut.gridy = 0;
        communicationsPersonnelPane.add(communicationsVerticalStrut, gbc_communicationsVerticalStrut);
        
        communicationsHorzizontalStrut = Box.createHorizontalStrut(20);
        GridBagConstraints gbc_communicationsHorzizontalStrut = new GridBagConstraints();
        gbc_communicationsHorzizontalStrut.insets = new Insets(0, 0, 5, 5);
        gbc_communicationsHorzizontalStrut.gridx = 0;
        gbc_communicationsHorzizontalStrut.gridy = 1;
        communicationsPersonnelPane.add(communicationsHorzizontalStrut, gbc_communicationsHorzizontalStrut);
        
        communicationsHorzizontalStrut_2 = Box.createHorizontalStrut(20);
        GridBagConstraints gbc_communicationsHorzizontalStrut_2 = new GridBagConstraints();
        gbc_communicationsHorzizontalStrut_2.insets = new Insets(0, 0, 5, 0);
        gbc_communicationsHorzizontalStrut_2.gridx = 7;
        gbc_communicationsHorzizontalStrut_2.gridy = 2;
        communicationsPersonnelPane.add(communicationsHorzizontalStrut_2, gbc_communicationsHorzizontalStrut_2);
				
		communicationsTitleLabel = new JLabel("Communications");
        communicationsTitleLabel.setOpaque(true);
        communicationsTitleLabel.setBackground(new Color(255, 255, 204));
        GridBagConstraints gbc_communicationsTitleLabel = new GridBagConstraints();
        gbc_communicationsTitleLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_communicationsTitleLabel.gridwidth = 6;
        gbc_communicationsTitleLabel.insets = new Insets(0, 0, 5, 5);
        gbc_communicationsTitleLabel.gridx = 1;
        gbc_communicationsTitleLabel.gridy = 1;
        communicationsPersonnelPane.add(communicationsTitleLabel, gbc_communicationsTitleLabel);
        
        communicationsFillerLabel = new JLabel("  ");
        communicationsFillerLabel.setOpaque(true);
        communicationsFillerLabel.setBackground(new Color(153, 153, 153));
        GridBagConstraints gbc_communicationsFillerLabel = new GridBagConstraints();
        gbc_communicationsFillerLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_communicationsFillerLabel.insets = new Insets(0, 0, 5, 5);
        gbc_communicationsFillerLabel.gridx = 1;
        gbc_communicationsFillerLabel.gridy = 2;
        communicationsPersonnelPane.add(communicationsFillerLabel, gbc_communicationsFillerLabel);
        
        communicationsAssigneeTitleLabel = new JLabel("Assignee");
        communicationsAssigneeTitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        communicationsAssigneeTitleLabel.setOpaque(true);
        communicationsAssigneeTitleLabel.setForeground(new Color(255, 255, 255));
        communicationsAssigneeTitleLabel.setBackground(new Color(153, 153, 153));
        GridBagConstraints gbc_communicationsAssigneeTitleLabel = new GridBagConstraints();
        gbc_communicationsAssigneeTitleLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_communicationsAssigneeTitleLabel.insets = new Insets(0, 0, 5, 5);
        gbc_communicationsAssigneeTitleLabel.gridx = 2;
        gbc_communicationsAssigneeTitleLabel.gridy = 2;
        communicationsPersonnelPane.add(communicationsAssigneeTitleLabel, gbc_communicationsAssigneeTitleLabel);
        
        communicationsHoursAssingedTitleLabel = new JLabel("Hours Assigned");
        communicationsHoursAssingedTitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        communicationsHoursAssingedTitleLabel.setOpaque(true);
        communicationsHoursAssingedTitleLabel.setForeground(new Color(255, 255, 255));
        communicationsHoursAssingedTitleLabel.setBackground(new Color(153, 153, 153));
        GridBagConstraints gbc_communicationsHoursAssingedTitleLabel = new GridBagConstraints();
        gbc_communicationsHoursAssingedTitleLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_communicationsHoursAssingedTitleLabel.insets = new Insets(0, 0, 5, 5);
        gbc_communicationsHoursAssingedTitleLabel.gridx = 3;
        gbc_communicationsHoursAssingedTitleLabel.gridy = 2;
        communicationsPersonnelPane.add(communicationsHoursAssingedTitleLabel, gbc_communicationsHoursAssingedTitleLabel);
        
        communicationsWorkRateTitleLabel = new JLabel("Work Rate");
        communicationsWorkRateTitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        communicationsWorkRateTitleLabel.setOpaque(true);
        communicationsWorkRateTitleLabel.setForeground(new Color(255, 255, 255));
        communicationsWorkRateTitleLabel.setBackground(new Color(153, 153, 153));
        GridBagConstraints gbc_communicationsWorkRateTitleLabel = new GridBagConstraints();
        gbc_communicationsWorkRateTitleLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_communicationsWorkRateTitleLabel.insets = new Insets(0, 0, 5, 5);
        gbc_communicationsWorkRateTitleLabel.gridx = 4;
        gbc_communicationsWorkRateTitleLabel.gridy = 2;
        communicationsPersonnelPane.add(communicationsWorkRateTitleLabel, gbc_communicationsWorkRateTitleLabel);
        
        communicationsDateCompleteTitleLabel = new JLabel("Date Completed");
        communicationsDateCompleteTitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        communicationsDateCompleteTitleLabel.setOpaque(true);
        communicationsDateCompleteTitleLabel.setForeground(new Color(255, 255, 255));
        communicationsDateCompleteTitleLabel.setBackground(new Color(153, 153, 153));
        GridBagConstraints gbc_communicationsDateCompleteTitleLabel = new GridBagConstraints();
        gbc_communicationsDateCompleteTitleLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_communicationsDateCompleteTitleLabel.insets = new Insets(0, 0, 5, 5);
        gbc_communicationsDateCompleteTitleLabel.gridx = 5;
        gbc_communicationsDateCompleteTitleLabel.gridy = 2;
        communicationsPersonnelPane.add(communicationsDateCompleteTitleLabel, gbc_communicationsDateCompleteTitleLabel);
        
        communicationsBackcheckTitleLabel = new JLabel("Backcheck");
        communicationsBackcheckTitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        communicationsBackcheckTitleLabel.setOpaque(true);
        communicationsBackcheckTitleLabel.setForeground(new Color(255, 255, 255));
        communicationsBackcheckTitleLabel.setBackground(new Color(153, 153, 153));
        GridBagConstraints gbc_communicationsBackcheckTitleLabel = new GridBagConstraints();
        gbc_communicationsBackcheckTitleLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_communicationsBackcheckTitleLabel.insets = new Insets(0, 0, 5, 5);
        gbc_communicationsBackcheckTitleLabel.gridx = 6;
        gbc_communicationsBackcheckTitleLabel.gridy = 2;
        communicationsPersonnelPane.add(communicationsBackcheckTitleLabel, gbc_communicationsBackcheckTitleLabel);
        
        communicationsAssigneeCheckBox = new JCheckBox("");
        GridBagConstraints gbc_communicationsAssigneeCheckBox = new GridBagConstraints();
        gbc_communicationsAssigneeCheckBox.insets = new Insets(0, 0, 5, 5);
        gbc_communicationsAssigneeCheckBox.gridx = 1;
        gbc_communicationsAssigneeCheckBox.gridy = 3;
        communicationsPersonnelPane.add(communicationsAssigneeCheckBox, gbc_communicationsAssigneeCheckBox);
        
        communicationsAssigneeName = new JLabel("Assignee Name");
        GridBagConstraints gbc_communicationsAssigneeName = new GridBagConstraints();
        gbc_communicationsAssigneeName.fill = GridBagConstraints.HORIZONTAL;
        gbc_communicationsAssigneeName.insets = new Insets(0, 0, 5, 5);
        gbc_communicationsAssigneeName.gridx = 2;
        gbc_communicationsAssigneeName.gridy = 3;
        communicationsPersonnelPane.add(communicationsAssigneeName, gbc_communicationsAssigneeName);
        
        communicationsHoursAssignedLabel = new JLabel("0.00");
        communicationsHoursAssignedLabel.setHorizontalAlignment(SwingConstants.TRAILING);
        GridBagConstraints gbc_communicationsHoursAssignedLabel = new GridBagConstraints();
        gbc_communicationsHoursAssignedLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_communicationsHoursAssignedLabel.insets = new Insets(0, 0, 5, 5);
        gbc_communicationsHoursAssignedLabel.gridx = 3;
        gbc_communicationsHoursAssignedLabel.gridy = 3;
        communicationsPersonnelPane.add(communicationsHoursAssignedLabel, gbc_communicationsHoursAssignedLabel);
        
        communicationsWorkRateLabel = new JLabel("Work Rate");
        communicationsWorkRateLabel.setHorizontalAlignment(SwingConstants.TRAILING);
        GridBagConstraints gbc_communicationsWorkRateLabel = new GridBagConstraints();
        gbc_communicationsWorkRateLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_communicationsWorkRateLabel.insets = new Insets(0, 0, 5, 5);
        gbc_communicationsWorkRateLabel.gridx = 4;
        gbc_communicationsWorkRateLabel.gridy = 3;
        communicationsPersonnelPane.add(communicationsWorkRateLabel, gbc_communicationsWorkRateLabel);
        
        communicationsDateCompletedLabel = new JLabel("00/00/1987");
        communicationsDateCompletedLabel.setHorizontalAlignment(SwingConstants.TRAILING);
        GridBagConstraints gbc_communicationsDateCompletedLabel = new GridBagConstraints();
        gbc_communicationsDateCompletedLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_communicationsDateCompletedLabel.insets = new Insets(0, 0, 5, 5);
        gbc_communicationsDateCompletedLabel.gridx = 5;
        gbc_communicationsDateCompletedLabel.gridy = 3;
        communicationsPersonnelPane.add(communicationsDateCompletedLabel, gbc_communicationsDateCompletedLabel);
        
        communicationsBackcheckLabel = new JLabel("--");
        communicationsBackcheckLabel.setHorizontalAlignment(SwingConstants.CENTER);
        GridBagConstraints gbc_communicationsBackcheckLabel = new GridBagConstraints();
        gbc_communicationsBackcheckLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_communicationsBackcheckLabel.insets = new Insets(0, 0, 5, 5);
        gbc_communicationsBackcheckLabel.gridx = 6;
        gbc_communicationsBackcheckLabel.gridy = 3;
        communicationsPersonnelPane.add(communicationsBackcheckLabel, gbc_communicationsBackcheckLabel);
        
        leedPersonnelPane = new JPanel();
		assignPersonnelPane.add(leedPersonnelPane);
        GridBagLayout gbl_leedPersonnelPane = new GridBagLayout();
        gbl_leedPersonnelPane.columnWidths = new int[]{0, 0, 310, 131, 138, 121, 80, 0, 0};
        gbl_leedPersonnelPane.rowHeights = new int[]{0, 0, 0, 0, 0};
        gbl_leedPersonnelPane.columnWeights = new double[]{0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
        gbl_leedPersonnelPane.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
        leedPersonnelPane.setLayout(gbl_leedPersonnelPane);
        
        leedVerticalStrut = Box.createVerticalStrut(20);
        GridBagConstraints gbc_leedVerticalStrut = new GridBagConstraints();
        gbc_leedVerticalStrut.insets = new Insets(0, 0, 5, 5);
        gbc_leedVerticalStrut.gridx = 1;
        gbc_leedVerticalStrut.gridy = 0;
        leedPersonnelPane.add(leedVerticalStrut, gbc_leedVerticalStrut);
        
        leedHorzizontalStrut = Box.createHorizontalStrut(20);
        GridBagConstraints gbc_leedHorzizontalStrut = new GridBagConstraints();
        gbc_leedHorzizontalStrut.insets = new Insets(0, 0, 5, 5);
        gbc_leedHorzizontalStrut.gridx = 0;
        gbc_leedHorzizontalStrut.gridy = 1;
        leedPersonnelPane.add(leedHorzizontalStrut, gbc_leedHorzizontalStrut);
        
        leedHorzizontalStrut_2 = Box.createHorizontalStrut(20);
        GridBagConstraints gbc_leedHorzizontalStrut_2 = new GridBagConstraints();
        gbc_leedHorzizontalStrut_2.insets = new Insets(0, 0, 5, 0);
        gbc_leedHorzizontalStrut_2.gridx = 7;
        gbc_leedHorzizontalStrut_2.gridy = 2;
        leedPersonnelPane.add(leedHorzizontalStrut_2, gbc_leedHorzizontalStrut_2);
				
		leedTitleLabel = new JLabel("LEED");
        leedTitleLabel.setOpaque(true);
        leedTitleLabel.setBackground(new Color(255, 255, 204));
        GridBagConstraints gbc_leedTitleLabel = new GridBagConstraints();
        gbc_leedTitleLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_leedTitleLabel.gridwidth = 6;
        gbc_leedTitleLabel.insets = new Insets(0, 0, 5, 5);
        gbc_leedTitleLabel.gridx = 1;
        gbc_leedTitleLabel.gridy = 1;
        leedPersonnelPane.add(leedTitleLabel, gbc_leedTitleLabel);
        
        leedFillerLabel = new JLabel("  ");
        leedFillerLabel.setOpaque(true);
        leedFillerLabel.setBackground(new Color(153, 153, 153));
        GridBagConstraints gbc_leedFillerLabel = new GridBagConstraints();
        gbc_leedFillerLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_leedFillerLabel.insets = new Insets(0, 0, 5, 5);
        gbc_leedFillerLabel.gridx = 1;
        gbc_leedFillerLabel.gridy = 2;
        leedPersonnelPane.add(leedFillerLabel, gbc_leedFillerLabel);
        
        leedAssigneeTitleLabel = new JLabel("Assignee");
        leedAssigneeTitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        leedAssigneeTitleLabel.setOpaque(true);
        leedAssigneeTitleLabel.setForeground(new Color(255, 255, 255));
        leedAssigneeTitleLabel.setBackground(new Color(153, 153, 153));
        GridBagConstraints gbc_leedAssigneeTitleLabel = new GridBagConstraints();
        gbc_leedAssigneeTitleLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_leedAssigneeTitleLabel.insets = new Insets(0, 0, 5, 5);
        gbc_leedAssigneeTitleLabel.gridx = 2;
        gbc_leedAssigneeTitleLabel.gridy = 2;
        leedPersonnelPane.add(leedAssigneeTitleLabel, gbc_leedAssigneeTitleLabel);
        
        leedHoursAssingedTitleLabel = new JLabel("Hours Assigned");
        leedHoursAssingedTitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        leedHoursAssingedTitleLabel.setOpaque(true);
        leedHoursAssingedTitleLabel.setForeground(new Color(255, 255, 255));
        leedHoursAssingedTitleLabel.setBackground(new Color(153, 153, 153));
        GridBagConstraints gbc_leedHoursAssingedTitleLabel = new GridBagConstraints();
        gbc_leedHoursAssingedTitleLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_leedHoursAssingedTitleLabel.insets = new Insets(0, 0, 5, 5);
        gbc_leedHoursAssingedTitleLabel.gridx = 3;
        gbc_leedHoursAssingedTitleLabel.gridy = 2;
        leedPersonnelPane.add(leedHoursAssingedTitleLabel, gbc_leedHoursAssingedTitleLabel);
        
        leedWorkRateTitleLabel = new JLabel("Work Rate");
        leedWorkRateTitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        leedWorkRateTitleLabel.setOpaque(true);
        leedWorkRateTitleLabel.setForeground(new Color(255, 255, 255));
        leedWorkRateTitleLabel.setBackground(new Color(153, 153, 153));
        GridBagConstraints gbc_leedWorkRateTitleLabel = new GridBagConstraints();
        gbc_leedWorkRateTitleLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_leedWorkRateTitleLabel.insets = new Insets(0, 0, 5, 5);
        gbc_leedWorkRateTitleLabel.gridx = 4;
        gbc_leedWorkRateTitleLabel.gridy = 2;
        leedPersonnelPane.add(leedWorkRateTitleLabel, gbc_leedWorkRateTitleLabel);
        
        leedDateCompleteTitleLabel = new JLabel("Date Completed");
        leedDateCompleteTitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        leedDateCompleteTitleLabel.setOpaque(true);
        leedDateCompleteTitleLabel.setForeground(new Color(255, 255, 255));
        leedDateCompleteTitleLabel.setBackground(new Color(153, 153, 153));
        GridBagConstraints gbc_leedDateCompleteTitleLabel = new GridBagConstraints();
        gbc_leedDateCompleteTitleLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_leedDateCompleteTitleLabel.insets = new Insets(0, 0, 5, 5);
        gbc_leedDateCompleteTitleLabel.gridx = 5;
        gbc_leedDateCompleteTitleLabel.gridy = 2;
        leedPersonnelPane.add(leedDateCompleteTitleLabel, gbc_leedDateCompleteTitleLabel);
        
        leedBackcheckTitleLabel = new JLabel("Backcheck");
        leedBackcheckTitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        leedBackcheckTitleLabel.setOpaque(true);
        leedBackcheckTitleLabel.setForeground(new Color(255, 255, 255));
        leedBackcheckTitleLabel.setBackground(new Color(153, 153, 153));
        GridBagConstraints gbc_leedBackcheckTitleLabel = new GridBagConstraints();
        gbc_leedBackcheckTitleLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_leedBackcheckTitleLabel.insets = new Insets(0, 0, 5, 5);
        gbc_leedBackcheckTitleLabel.gridx = 6;
        gbc_leedBackcheckTitleLabel.gridy = 2;
        leedPersonnelPane.add(leedBackcheckTitleLabel, gbc_leedBackcheckTitleLabel);
        
        leedAssigneeCheckBox = new JCheckBox("");
        GridBagConstraints gbc_leedAssigneeCheckBox = new GridBagConstraints();
        gbc_leedAssigneeCheckBox.insets = new Insets(0, 0, 5, 5);
        gbc_leedAssigneeCheckBox.gridx = 1;
        gbc_leedAssigneeCheckBox.gridy = 3;
        leedPersonnelPane.add(leedAssigneeCheckBox, gbc_leedAssigneeCheckBox);
        
        leedAssigneeName = new JLabel("Assignee Name");
        GridBagConstraints gbc_leedAssigneeName = new GridBagConstraints();
        gbc_leedAssigneeName.fill = GridBagConstraints.HORIZONTAL;
        gbc_leedAssigneeName.insets = new Insets(0, 0, 5, 5);
        gbc_leedAssigneeName.gridx = 2;
        gbc_leedAssigneeName.gridy = 3;
        leedPersonnelPane.add(leedAssigneeName, gbc_leedAssigneeName);
        
        leedHoursAssignedLabel = new JLabel("0.00");
        leedHoursAssignedLabel.setHorizontalAlignment(SwingConstants.TRAILING);
        GridBagConstraints gbc_leedHoursAssignedLabel = new GridBagConstraints();
        gbc_leedHoursAssignedLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_leedHoursAssignedLabel.insets = new Insets(0, 0, 5, 5);
        gbc_leedHoursAssignedLabel.gridx = 3;
        gbc_leedHoursAssignedLabel.gridy = 3;
        leedPersonnelPane.add(leedHoursAssignedLabel, gbc_leedHoursAssignedLabel);
        
        leedWorkRateLabel = new JLabel("Work Rate");
        leedWorkRateLabel.setHorizontalAlignment(SwingConstants.TRAILING);
        GridBagConstraints gbc_leedWorkRateLabel = new GridBagConstraints();
        gbc_leedWorkRateLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_leedWorkRateLabel.insets = new Insets(0, 0, 5, 5);
        gbc_leedWorkRateLabel.gridx = 4;
        gbc_leedWorkRateLabel.gridy = 3;
        leedPersonnelPane.add(leedWorkRateLabel, gbc_leedWorkRateLabel);
        
        leedDateCompletedLabel = new JLabel("00/00/1987");
        leedDateCompletedLabel.setHorizontalAlignment(SwingConstants.TRAILING);
        GridBagConstraints gbc_leedDateCompletedLabel = new GridBagConstraints();
        gbc_leedDateCompletedLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_leedDateCompletedLabel.insets = new Insets(0, 0, 5, 5);
        gbc_leedDateCompletedLabel.gridx = 5;
        gbc_leedDateCompletedLabel.gridy = 3;
        leedPersonnelPane.add(leedDateCompletedLabel, gbc_leedDateCompletedLabel);
        
        leedBackcheckLabel = new JLabel("--");
        leedBackcheckLabel.setHorizontalAlignment(SwingConstants.CENTER);
        GridBagConstraints gbc_leedBackcheckLabel = new GridBagConstraints();
        gbc_leedBackcheckLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_leedBackcheckLabel.insets = new Insets(0, 0, 5, 5);
        gbc_leedBackcheckLabel.gridx = 6;
        gbc_leedBackcheckLabel.gridy = 3;
        leedPersonnelPane.add(leedBackcheckLabel, gbc_leedBackcheckLabel);
        
        environementalPersonnelPane = new JPanel();
		assignPersonnelPane.add(environementalPersonnelPane);
        GridBagLayout gbl_environementalPersonnelPane = new GridBagLayout();
        gbl_environementalPersonnelPane.columnWidths = new int[]{0, 0, 310, 131, 138, 121, 80, 0, 0};
        gbl_environementalPersonnelPane.rowHeights = new int[]{0, 0, 0, 0, 0};
        gbl_environementalPersonnelPane.columnWeights = new double[]{0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
        gbl_environementalPersonnelPane.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
        environementalPersonnelPane.setLayout(gbl_environementalPersonnelPane);
        
        environementalVerticalStrut = Box.createVerticalStrut(20);
        GridBagConstraints gbc_environementalVerticalStrut = new GridBagConstraints();
        gbc_environementalVerticalStrut.insets = new Insets(0, 0, 5, 5);
        gbc_environementalVerticalStrut.gridx = 1;
        gbc_environementalVerticalStrut.gridy = 0;
        environementalPersonnelPane.add(environementalVerticalStrut, gbc_environementalVerticalStrut);
        
        environementalHorzizontalStrut = Box.createHorizontalStrut(20);
        GridBagConstraints gbc_environementalHorzizontalStrut = new GridBagConstraints();
        gbc_environementalHorzizontalStrut.insets = new Insets(0, 0, 5, 5);
        gbc_environementalHorzizontalStrut.gridx = 0;
        gbc_environementalHorzizontalStrut.gridy = 1;
        environementalPersonnelPane.add(environementalHorzizontalStrut, gbc_environementalHorzizontalStrut);
        
        environementalHorzizontalStrut_2 = Box.createHorizontalStrut(20);
        GridBagConstraints gbc_environementalHorzizontalStrut_2 = new GridBagConstraints();
        gbc_environementalHorzizontalStrut_2.insets = new Insets(0, 0, 5, 0);
        gbc_environementalHorzizontalStrut_2.gridx = 7;
        gbc_environementalHorzizontalStrut_2.gridy = 2;
        environementalPersonnelPane.add(environementalHorzizontalStrut_2, gbc_environementalHorzizontalStrut_2);
				
		environementalTitleLabel = new JLabel("Environemental");
        environementalTitleLabel.setOpaque(true);
        environementalTitleLabel.setBackground(new Color(255, 255, 204));
        GridBagConstraints gbc_environementalTitleLabel = new GridBagConstraints();
        gbc_environementalTitleLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_environementalTitleLabel.gridwidth = 6;
        gbc_environementalTitleLabel.insets = new Insets(0, 0, 5, 5);
        gbc_environementalTitleLabel.gridx = 1;
        gbc_environementalTitleLabel.gridy = 1;
        environementalPersonnelPane.add(environementalTitleLabel, gbc_environementalTitleLabel);
        
        environementalFillerLabel = new JLabel("  ");
        environementalFillerLabel.setOpaque(true);
        environementalFillerLabel.setBackground(new Color(153, 153, 153));
        GridBagConstraints gbc_environementalFillerLabel = new GridBagConstraints();
        gbc_environementalFillerLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_environementalFillerLabel.insets = new Insets(0, 0, 5, 5);
        gbc_environementalFillerLabel.gridx = 1;
        gbc_environementalFillerLabel.gridy = 2;
        environementalPersonnelPane.add(environementalFillerLabel, gbc_environementalFillerLabel);
        
        environementalAssigneeTitleLabel = new JLabel("Assignee");
        environementalAssigneeTitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        environementalAssigneeTitleLabel.setOpaque(true);
        environementalAssigneeTitleLabel.setForeground(new Color(255, 255, 255));
        environementalAssigneeTitleLabel.setBackground(new Color(153, 153, 153));
        GridBagConstraints gbc_environementalAssigneeTitleLabel = new GridBagConstraints();
        gbc_environementalAssigneeTitleLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_environementalAssigneeTitleLabel.insets = new Insets(0, 0, 5, 5);
        gbc_environementalAssigneeTitleLabel.gridx = 2;
        gbc_environementalAssigneeTitleLabel.gridy = 2;
        environementalPersonnelPane.add(environementalAssigneeTitleLabel, gbc_environementalAssigneeTitleLabel);
        
        environementalHoursAssingedTitleLabel = new JLabel("Hours Assigned");
        environementalHoursAssingedTitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        environementalHoursAssingedTitleLabel.setOpaque(true);
        environementalHoursAssingedTitleLabel.setForeground(new Color(255, 255, 255));
        environementalHoursAssingedTitleLabel.setBackground(new Color(153, 153, 153));
        GridBagConstraints gbc_environementalHoursAssingedTitleLabel = new GridBagConstraints();
        gbc_environementalHoursAssingedTitleLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_environementalHoursAssingedTitleLabel.insets = new Insets(0, 0, 5, 5);
        gbc_environementalHoursAssingedTitleLabel.gridx = 3;
        gbc_environementalHoursAssingedTitleLabel.gridy = 2;
        environementalPersonnelPane.add(environementalHoursAssingedTitleLabel, gbc_environementalHoursAssingedTitleLabel);
        
        environementalWorkRateTitleLabel = new JLabel("Work Rate");
        environementalWorkRateTitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        environementalWorkRateTitleLabel.setOpaque(true);
        environementalWorkRateTitleLabel.setForeground(new Color(255, 255, 255));
        environementalWorkRateTitleLabel.setBackground(new Color(153, 153, 153));
        GridBagConstraints gbc_environementalWorkRateTitleLabel = new GridBagConstraints();
        gbc_environementalWorkRateTitleLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_environementalWorkRateTitleLabel.insets = new Insets(0, 0, 5, 5);
        gbc_environementalWorkRateTitleLabel.gridx = 4;
        gbc_environementalWorkRateTitleLabel.gridy = 2;
        environementalPersonnelPane.add(environementalWorkRateTitleLabel, gbc_environementalWorkRateTitleLabel);
        
        environementalDateCompleteTitleLabel = new JLabel("Date Completed");
        environementalDateCompleteTitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        environementalDateCompleteTitleLabel.setOpaque(true);
        environementalDateCompleteTitleLabel.setForeground(new Color(255, 255, 255));
        environementalDateCompleteTitleLabel.setBackground(new Color(153, 153, 153));
        GridBagConstraints gbc_environementalDateCompleteTitleLabel = new GridBagConstraints();
        gbc_environementalDateCompleteTitleLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_environementalDateCompleteTitleLabel.insets = new Insets(0, 0, 5, 5);
        gbc_environementalDateCompleteTitleLabel.gridx = 5;
        gbc_environementalDateCompleteTitleLabel.gridy = 2;
        environementalPersonnelPane.add(environementalDateCompleteTitleLabel, gbc_environementalDateCompleteTitleLabel);
        
        environementalBackcheckTitleLabel = new JLabel("Backcheck");
        environementalBackcheckTitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        environementalBackcheckTitleLabel.setOpaque(true);
        environementalBackcheckTitleLabel.setForeground(new Color(255, 255, 255));
        environementalBackcheckTitleLabel.setBackground(new Color(153, 153, 153));
        GridBagConstraints gbc_environementalBackcheckTitleLabel = new GridBagConstraints();
        gbc_environementalBackcheckTitleLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_environementalBackcheckTitleLabel.insets = new Insets(0, 0, 5, 5);
        gbc_environementalBackcheckTitleLabel.gridx = 6;
        gbc_environementalBackcheckTitleLabel.gridy = 2;
        environementalPersonnelPane.add(environementalBackcheckTitleLabel, gbc_environementalBackcheckTitleLabel);
        
        environementalAssigneeCheckBox = new JCheckBox("");
        GridBagConstraints gbc_environementalAssigneeCheckBox = new GridBagConstraints();
        gbc_environementalAssigneeCheckBox.insets = new Insets(0, 0, 5, 5);
        gbc_environementalAssigneeCheckBox.gridx = 1;
        gbc_environementalAssigneeCheckBox.gridy = 3;
        environementalPersonnelPane.add(environementalAssigneeCheckBox, gbc_environementalAssigneeCheckBox);
        
        environementalAssigneeName = new JLabel("Assignee Name");
        GridBagConstraints gbc_environementalAssigneeName = new GridBagConstraints();
        gbc_environementalAssigneeName.fill = GridBagConstraints.HORIZONTAL;
        gbc_environementalAssigneeName.insets = new Insets(0, 0, 5, 5);
        gbc_environementalAssigneeName.gridx = 2;
        gbc_environementalAssigneeName.gridy = 3;
        environementalPersonnelPane.add(environementalAssigneeName, gbc_environementalAssigneeName);
        
        environementalHoursAssignedLabel = new JLabel("0.00");
        environementalHoursAssignedLabel.setHorizontalAlignment(SwingConstants.TRAILING);
        GridBagConstraints gbc_environementalHoursAssignedLabel = new GridBagConstraints();
        gbc_environementalHoursAssignedLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_environementalHoursAssignedLabel.insets = new Insets(0, 0, 5, 5);
        gbc_environementalHoursAssignedLabel.gridx = 3;
        gbc_environementalHoursAssignedLabel.gridy = 3;
        environementalPersonnelPane.add(environementalHoursAssignedLabel, gbc_environementalHoursAssignedLabel);
        
        environementalWorkRateLabel = new JLabel("Work Rate");
        environementalWorkRateLabel.setHorizontalAlignment(SwingConstants.TRAILING);
        GridBagConstraints gbc_environementalWorkRateLabel = new GridBagConstraints();
        gbc_environementalWorkRateLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_environementalWorkRateLabel.insets = new Insets(0, 0, 5, 5);
        gbc_environementalWorkRateLabel.gridx = 4;
        gbc_environementalWorkRateLabel.gridy = 3;
        environementalPersonnelPane.add(environementalWorkRateLabel, gbc_environementalWorkRateLabel);
        
        environementalDateCompletedLabel = new JLabel("00/00/1987");
        environementalDateCompletedLabel.setHorizontalAlignment(SwingConstants.TRAILING);
        GridBagConstraints gbc_environementalDateCompletedLabel = new GridBagConstraints();
        gbc_environementalDateCompletedLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_environementalDateCompletedLabel.insets = new Insets(0, 0, 5, 5);
        gbc_environementalDateCompletedLabel.gridx = 5;
        gbc_environementalDateCompletedLabel.gridy = 3;
        environementalPersonnelPane.add(environementalDateCompletedLabel, gbc_environementalDateCompletedLabel);
        
        environementalBackcheckLabel = new JLabel("--");
        environementalBackcheckLabel.setHorizontalAlignment(SwingConstants.CENTER);
        GridBagConstraints gbc_environementalBackcheckLabel = new GridBagConstraints();
        gbc_environementalBackcheckLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_environementalBackcheckLabel.insets = new Insets(0, 0, 5, 5);
        gbc_environementalBackcheckLabel.gridx = 6;
        gbc_environementalBackcheckLabel.gridy = 3;
        environementalPersonnelPane.add(environementalBackcheckLabel, gbc_environementalBackcheckLabel);
        
        costEngineerPersonnelPane = new JPanel();
		assignPersonnelPane.add(costEngineerPersonnelPane);
        GridBagLayout gbl_costEngineerPersonnelPane = new GridBagLayout();
        gbl_costEngineerPersonnelPane.columnWidths = new int[]{0, 0, 310, 131, 138, 121, 80, 0, 0};
        gbl_costEngineerPersonnelPane.rowHeights = new int[]{0, 0, 0, 0, 0};
        gbl_costEngineerPersonnelPane.columnWeights = new double[]{0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
        gbl_costEngineerPersonnelPane.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
        costEngineerPersonnelPane.setLayout(gbl_costEngineerPersonnelPane);
        
        costEngineerVerticalStrut = Box.createVerticalStrut(20);
        GridBagConstraints gbc_costEngineerVerticalStrut = new GridBagConstraints();
        gbc_costEngineerVerticalStrut.insets = new Insets(0, 0, 5, 5);
        gbc_costEngineerVerticalStrut.gridx = 1;
        gbc_costEngineerVerticalStrut.gridy = 0;
        costEngineerPersonnelPane.add(costEngineerVerticalStrut, gbc_costEngineerVerticalStrut);
        
        costEngineerHorzizontalStrut = Box.createHorizontalStrut(20);
        GridBagConstraints gbc_costEngineerHorzizontalStrut = new GridBagConstraints();
        gbc_costEngineerHorzizontalStrut.insets = new Insets(0, 0, 5, 5);
        gbc_costEngineerHorzizontalStrut.gridx = 0;
        gbc_costEngineerHorzizontalStrut.gridy = 1;
        costEngineerPersonnelPane.add(costEngineerHorzizontalStrut, gbc_costEngineerHorzizontalStrut);
        
        costEngineerHorzizontalStrut_2 = Box.createHorizontalStrut(20);
        GridBagConstraints gbc_costEngineerHorzizontalStrut_2 = new GridBagConstraints();
        gbc_costEngineerHorzizontalStrut_2.insets = new Insets(0, 0, 5, 0);
        gbc_costEngineerHorzizontalStrut_2.gridx = 7;
        gbc_costEngineerHorzizontalStrut_2.gridy = 2;
        costEngineerPersonnelPane.add(costEngineerHorzizontalStrut_2, gbc_costEngineerHorzizontalStrut_2);
				
		costEngineerTitleLabel = new JLabel("Cost Engineer");
        costEngineerTitleLabel.setOpaque(true);
        costEngineerTitleLabel.setBackground(new Color(255, 255, 204));
        GridBagConstraints gbc_costEngineerTitleLabel = new GridBagConstraints();
        gbc_costEngineerTitleLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_costEngineerTitleLabel.gridwidth = 6;
        gbc_costEngineerTitleLabel.insets = new Insets(0, 0, 5, 5);
        gbc_costEngineerTitleLabel.gridx = 1;
        gbc_costEngineerTitleLabel.gridy = 1;
        costEngineerPersonnelPane.add(costEngineerTitleLabel, gbc_costEngineerTitleLabel);
        
        costEngineerFillerLabel = new JLabel("  ");
        costEngineerFillerLabel.setOpaque(true);
        costEngineerFillerLabel.setBackground(new Color(153, 153, 153));
        GridBagConstraints gbc_costEngineerFillerLabel = new GridBagConstraints();
        gbc_costEngineerFillerLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_costEngineerFillerLabel.insets = new Insets(0, 0, 5, 5);
        gbc_costEngineerFillerLabel.gridx = 1;
        gbc_costEngineerFillerLabel.gridy = 2;
        costEngineerPersonnelPane.add(costEngineerFillerLabel, gbc_costEngineerFillerLabel);
        
        costEngineerAssigneeTitleLabel = new JLabel("Assignee");
        costEngineerAssigneeTitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        costEngineerAssigneeTitleLabel.setOpaque(true);
        costEngineerAssigneeTitleLabel.setForeground(new Color(255, 255, 255));
        costEngineerAssigneeTitleLabel.setBackground(new Color(153, 153, 153));
        GridBagConstraints gbc_costEngineerAssigneeTitleLabel = new GridBagConstraints();
        gbc_costEngineerAssigneeTitleLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_costEngineerAssigneeTitleLabel.insets = new Insets(0, 0, 5, 5);
        gbc_costEngineerAssigneeTitleLabel.gridx = 2;
        gbc_costEngineerAssigneeTitleLabel.gridy = 2;
        costEngineerPersonnelPane.add(costEngineerAssigneeTitleLabel, gbc_costEngineerAssigneeTitleLabel);
        
        costEngineerHoursAssingedTitleLabel = new JLabel("Hours Assigned");
        costEngineerHoursAssingedTitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        costEngineerHoursAssingedTitleLabel.setOpaque(true);
        costEngineerHoursAssingedTitleLabel.setForeground(new Color(255, 255, 255));
        costEngineerHoursAssingedTitleLabel.setBackground(new Color(153, 153, 153));
        GridBagConstraints gbc_costEngineerHoursAssingedTitleLabel = new GridBagConstraints();
        gbc_costEngineerHoursAssingedTitleLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_costEngineerHoursAssingedTitleLabel.insets = new Insets(0, 0, 5, 5);
        gbc_costEngineerHoursAssingedTitleLabel.gridx = 3;
        gbc_costEngineerHoursAssingedTitleLabel.gridy = 2;
        costEngineerPersonnelPane.add(costEngineerHoursAssingedTitleLabel, gbc_costEngineerHoursAssingedTitleLabel);
        
        costEngineerWorkRateTitleLabel = new JLabel("Work Rate");
        costEngineerWorkRateTitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        costEngineerWorkRateTitleLabel.setOpaque(true);
        costEngineerWorkRateTitleLabel.setForeground(new Color(255, 255, 255));
        costEngineerWorkRateTitleLabel.setBackground(new Color(153, 153, 153));
        GridBagConstraints gbc_costEngineerWorkRateTitleLabel = new GridBagConstraints();
        gbc_costEngineerWorkRateTitleLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_costEngineerWorkRateTitleLabel.insets = new Insets(0, 0, 5, 5);
        gbc_costEngineerWorkRateTitleLabel.gridx = 4;
        gbc_costEngineerWorkRateTitleLabel.gridy = 2;
        costEngineerPersonnelPane.add(costEngineerWorkRateTitleLabel, gbc_costEngineerWorkRateTitleLabel);
        
        costEngineerDateCompleteTitleLabel = new JLabel("Date Completed");
        costEngineerDateCompleteTitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        costEngineerDateCompleteTitleLabel.setOpaque(true);
        costEngineerDateCompleteTitleLabel.setForeground(new Color(255, 255, 255));
        costEngineerDateCompleteTitleLabel.setBackground(new Color(153, 153, 153));
        GridBagConstraints gbc_costEngineerDateCompleteTitleLabel = new GridBagConstraints();
        gbc_costEngineerDateCompleteTitleLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_costEngineerDateCompleteTitleLabel.insets = new Insets(0, 0, 5, 5);
        gbc_costEngineerDateCompleteTitleLabel.gridx = 5;
        gbc_costEngineerDateCompleteTitleLabel.gridy = 2;
        costEngineerPersonnelPane.add(costEngineerDateCompleteTitleLabel, gbc_costEngineerDateCompleteTitleLabel);
        
        costEngineerBackcheckTitleLabel = new JLabel("Backcheck");
        costEngineerBackcheckTitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        costEngineerBackcheckTitleLabel.setOpaque(true);
        costEngineerBackcheckTitleLabel.setForeground(new Color(255, 255, 255));
        costEngineerBackcheckTitleLabel.setBackground(new Color(153, 153, 153));
        GridBagConstraints gbc_costEngineerBackcheckTitleLabel = new GridBagConstraints();
        gbc_costEngineerBackcheckTitleLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_costEngineerBackcheckTitleLabel.insets = new Insets(0, 0, 5, 5);
        gbc_costEngineerBackcheckTitleLabel.gridx = 6;
        gbc_costEngineerBackcheckTitleLabel.gridy = 2;
        costEngineerPersonnelPane.add(costEngineerBackcheckTitleLabel, gbc_costEngineerBackcheckTitleLabel);
        
        costEngineerAssigneeCheckBox = new JCheckBox("");
        GridBagConstraints gbc_costEngineerAssigneeCheckBox = new GridBagConstraints();
        gbc_costEngineerAssigneeCheckBox.insets = new Insets(0, 0, 5, 5);
        gbc_costEngineerAssigneeCheckBox.gridx = 1;
        gbc_costEngineerAssigneeCheckBox.gridy = 3;
        costEngineerPersonnelPane.add(costEngineerAssigneeCheckBox, gbc_costEngineerAssigneeCheckBox);
        
        costEngineerAssigneeName = new JLabel("Assignee Name");
        GridBagConstraints gbc_costEngineerAssigneeName = new GridBagConstraints();
        gbc_costEngineerAssigneeName.fill = GridBagConstraints.HORIZONTAL;
        gbc_costEngineerAssigneeName.insets = new Insets(0, 0, 5, 5);
        gbc_costEngineerAssigneeName.gridx = 2;
        gbc_costEngineerAssigneeName.gridy = 3;
        costEngineerPersonnelPane.add(costEngineerAssigneeName, gbc_costEngineerAssigneeName);
        
        costEngineerHoursAssignedLabel = new JLabel("0.00");
        costEngineerHoursAssignedLabel.setHorizontalAlignment(SwingConstants.TRAILING);
        GridBagConstraints gbc_costEngineerHoursAssignedLabel = new GridBagConstraints();
        gbc_costEngineerHoursAssignedLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_costEngineerHoursAssignedLabel.insets = new Insets(0, 0, 5, 5);
        gbc_costEngineerHoursAssignedLabel.gridx = 3;
        gbc_costEngineerHoursAssignedLabel.gridy = 3;
        costEngineerPersonnelPane.add(costEngineerHoursAssignedLabel, gbc_costEngineerHoursAssignedLabel);
        
        costEngineerWorkRateLabel = new JLabel("Work Rate");
        costEngineerWorkRateLabel.setHorizontalAlignment(SwingConstants.TRAILING);
        GridBagConstraints gbc_costEngineerWorkRateLabel = new GridBagConstraints();
        gbc_costEngineerWorkRateLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_costEngineerWorkRateLabel.insets = new Insets(0, 0, 5, 5);
        gbc_costEngineerWorkRateLabel.gridx = 4;
        gbc_costEngineerWorkRateLabel.gridy = 3;
        costEngineerPersonnelPane.add(costEngineerWorkRateLabel, gbc_costEngineerWorkRateLabel);
        
        costEngineerDateCompletedLabel = new JLabel("00/00/1987");
        costEngineerDateCompletedLabel.setHorizontalAlignment(SwingConstants.TRAILING);
        GridBagConstraints gbc_costEngineerDateCompletedLabel = new GridBagConstraints();
        gbc_costEngineerDateCompletedLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_costEngineerDateCompletedLabel.insets = new Insets(0, 0, 5, 5);
        gbc_costEngineerDateCompletedLabel.gridx = 5;
        gbc_costEngineerDateCompletedLabel.gridy = 3;
        costEngineerPersonnelPane.add(costEngineerDateCompletedLabel, gbc_costEngineerDateCompletedLabel);
        
        costEngineerBackcheckLabel = new JLabel("--");
        costEngineerBackcheckLabel.setHorizontalAlignment(SwingConstants.CENTER);
        GridBagConstraints gbc_costEngineerBackcheckLabel = new GridBagConstraints();
        gbc_costEngineerBackcheckLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_costEngineerBackcheckLabel.insets = new Insets(0, 0, 5, 5);
        gbc_costEngineerBackcheckLabel.gridx = 6;
        gbc_costEngineerBackcheckLabel.gridy = 3;
        costEngineerPersonnelPane.add(costEngineerBackcheckLabel, gbc_costEngineerBackcheckLabel);
        
        geotechnicalPersonnelPane = new JPanel();
		assignPersonnelPane.add(geotechnicalPersonnelPane);
        GridBagLayout gbl_geotechnicalPersonnelPane = new GridBagLayout();
        gbl_geotechnicalPersonnelPane.columnWidths = new int[]{0, 0, 310, 131, 138, 121, 80, 0, 0};
        gbl_geotechnicalPersonnelPane.rowHeights = new int[]{0, 0, 0, 0, 0};
        gbl_geotechnicalPersonnelPane.columnWeights = new double[]{0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
        gbl_geotechnicalPersonnelPane.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
        geotechnicalPersonnelPane.setLayout(gbl_geotechnicalPersonnelPane);
        
        geotechnicalVerticalStrut = Box.createVerticalStrut(20);
        GridBagConstraints gbc_geotechnicalVerticalStrut = new GridBagConstraints();
        gbc_geotechnicalVerticalStrut.insets = new Insets(0, 0, 5, 5);
        gbc_geotechnicalVerticalStrut.gridx = 1;
        gbc_geotechnicalVerticalStrut.gridy = 0;
        geotechnicalPersonnelPane.add(geotechnicalVerticalStrut, gbc_geotechnicalVerticalStrut);
        
        geotechnicalHorzizontalStrut = Box.createHorizontalStrut(20);
        GridBagConstraints gbc_geotechnicalHorzizontalStrut = new GridBagConstraints();
        gbc_geotechnicalHorzizontalStrut.insets = new Insets(0, 0, 5, 5);
        gbc_geotechnicalHorzizontalStrut.gridx = 0;
        gbc_geotechnicalHorzizontalStrut.gridy = 1;
        geotechnicalPersonnelPane.add(geotechnicalHorzizontalStrut, gbc_geotechnicalHorzizontalStrut);
        
        geotechnicalHorzizontalStrut_2 = Box.createHorizontalStrut(20);
        GridBagConstraints gbc_geotechnicalHorzizontalStrut_2 = new GridBagConstraints();
        gbc_geotechnicalHorzizontalStrut_2.insets = new Insets(0, 0, 5, 0);
        gbc_geotechnicalHorzizontalStrut_2.gridx = 7;
        gbc_geotechnicalHorzizontalStrut_2.gridy = 2;
        geotechnicalPersonnelPane.add(geotechnicalHorzizontalStrut_2, gbc_geotechnicalHorzizontalStrut_2);
				
		geotechnicalTitleLabel = new JLabel("Geotechnical");
        geotechnicalTitleLabel.setOpaque(true);
        geotechnicalTitleLabel.setBackground(new Color(255, 255, 204));
        GridBagConstraints gbc_geotechnicalTitleLabel = new GridBagConstraints();
        gbc_geotechnicalTitleLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_geotechnicalTitleLabel.gridwidth = 6;
        gbc_geotechnicalTitleLabel.insets = new Insets(0, 0, 5, 5);
        gbc_geotechnicalTitleLabel.gridx = 1;
        gbc_geotechnicalTitleLabel.gridy = 1;
        geotechnicalPersonnelPane.add(geotechnicalTitleLabel, gbc_geotechnicalTitleLabel);
        
        geotechnicalFillerLabel = new JLabel("  ");
        geotechnicalFillerLabel.setOpaque(true);
        geotechnicalFillerLabel.setBackground(new Color(153, 153, 153));
        GridBagConstraints gbc_geotechnicalFillerLabel = new GridBagConstraints();
        gbc_geotechnicalFillerLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_geotechnicalFillerLabel.insets = new Insets(0, 0, 5, 5);
        gbc_geotechnicalFillerLabel.gridx = 1;
        gbc_geotechnicalFillerLabel.gridy = 2;
        geotechnicalPersonnelPane.add(geotechnicalFillerLabel, gbc_geotechnicalFillerLabel);
        
        geotechnicalAssigneeTitleLabel = new JLabel("Assignee");
        geotechnicalAssigneeTitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        geotechnicalAssigneeTitleLabel.setOpaque(true);
        geotechnicalAssigneeTitleLabel.setForeground(new Color(255, 255, 255));
        geotechnicalAssigneeTitleLabel.setBackground(new Color(153, 153, 153));
        GridBagConstraints gbc_geotechnicalAssigneeTitleLabel = new GridBagConstraints();
        gbc_geotechnicalAssigneeTitleLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_geotechnicalAssigneeTitleLabel.insets = new Insets(0, 0, 5, 5);
        gbc_geotechnicalAssigneeTitleLabel.gridx = 2;
        gbc_geotechnicalAssigneeTitleLabel.gridy = 2;
        geotechnicalPersonnelPane.add(geotechnicalAssigneeTitleLabel, gbc_geotechnicalAssigneeTitleLabel);
        
        geotechnicalHoursAssingedTitleLabel = new JLabel("Hours Assigned");
        geotechnicalHoursAssingedTitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        geotechnicalHoursAssingedTitleLabel.setOpaque(true);
        geotechnicalHoursAssingedTitleLabel.setForeground(new Color(255, 255, 255));
        geotechnicalHoursAssingedTitleLabel.setBackground(new Color(153, 153, 153));
        GridBagConstraints gbc_geotechnicalHoursAssingedTitleLabel = new GridBagConstraints();
        gbc_geotechnicalHoursAssingedTitleLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_geotechnicalHoursAssingedTitleLabel.insets = new Insets(0, 0, 5, 5);
        gbc_geotechnicalHoursAssingedTitleLabel.gridx = 3;
        gbc_geotechnicalHoursAssingedTitleLabel.gridy = 2;
        geotechnicalPersonnelPane.add(geotechnicalHoursAssingedTitleLabel, gbc_geotechnicalHoursAssingedTitleLabel);
        
        geotechnicalWorkRateTitleLabel = new JLabel("Work Rate");
        geotechnicalWorkRateTitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        geotechnicalWorkRateTitleLabel.setOpaque(true);
        geotechnicalWorkRateTitleLabel.setForeground(new Color(255, 255, 255));
        geotechnicalWorkRateTitleLabel.setBackground(new Color(153, 153, 153));
        GridBagConstraints gbc_geotechnicalWorkRateTitleLabel = new GridBagConstraints();
        gbc_geotechnicalWorkRateTitleLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_geotechnicalWorkRateTitleLabel.insets = new Insets(0, 0, 5, 5);
        gbc_geotechnicalWorkRateTitleLabel.gridx = 4;
        gbc_geotechnicalWorkRateTitleLabel.gridy = 2;
        geotechnicalPersonnelPane.add(geotechnicalWorkRateTitleLabel, gbc_geotechnicalWorkRateTitleLabel);
        
        geotechnicalDateCompleteTitleLabel = new JLabel("Date Completed");
        geotechnicalDateCompleteTitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        geotechnicalDateCompleteTitleLabel.setOpaque(true);
        geotechnicalDateCompleteTitleLabel.setForeground(new Color(255, 255, 255));
        geotechnicalDateCompleteTitleLabel.setBackground(new Color(153, 153, 153));
        GridBagConstraints gbc_geotechnicalDateCompleteTitleLabel = new GridBagConstraints();
        gbc_geotechnicalDateCompleteTitleLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_geotechnicalDateCompleteTitleLabel.insets = new Insets(0, 0, 5, 5);
        gbc_geotechnicalDateCompleteTitleLabel.gridx = 5;
        gbc_geotechnicalDateCompleteTitleLabel.gridy = 2;
        geotechnicalPersonnelPane.add(geotechnicalDateCompleteTitleLabel, gbc_geotechnicalDateCompleteTitleLabel);
        
        geotechnicalBackcheckTitleLabel = new JLabel("Backcheck");
        geotechnicalBackcheckTitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        geotechnicalBackcheckTitleLabel.setOpaque(true);
        geotechnicalBackcheckTitleLabel.setForeground(new Color(255, 255, 255));
        geotechnicalBackcheckTitleLabel.setBackground(new Color(153, 153, 153));
        GridBagConstraints gbc_geotechnicalBackcheckTitleLabel = new GridBagConstraints();
        gbc_geotechnicalBackcheckTitleLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_geotechnicalBackcheckTitleLabel.insets = new Insets(0, 0, 5, 5);
        gbc_geotechnicalBackcheckTitleLabel.gridx = 6;
        gbc_geotechnicalBackcheckTitleLabel.gridy = 2;
        geotechnicalPersonnelPane.add(geotechnicalBackcheckTitleLabel, gbc_geotechnicalBackcheckTitleLabel);
        
        geotechnicalAssigneeCheckBox = new JCheckBox("");
        GridBagConstraints gbc_geotechnicalAssigneeCheckBox = new GridBagConstraints();
        gbc_geotechnicalAssigneeCheckBox.insets = new Insets(0, 0, 5, 5);
        gbc_geotechnicalAssigneeCheckBox.gridx = 1;
        gbc_geotechnicalAssigneeCheckBox.gridy = 3;
        geotechnicalPersonnelPane.add(geotechnicalAssigneeCheckBox, gbc_geotechnicalAssigneeCheckBox);
        
        geotechnicalAssigneeName = new JLabel("Assignee Name");
        GridBagConstraints gbc_geotechnicalAssigneeName = new GridBagConstraints();
        gbc_geotechnicalAssigneeName.fill = GridBagConstraints.HORIZONTAL;
        gbc_geotechnicalAssigneeName.insets = new Insets(0, 0, 5, 5);
        gbc_geotechnicalAssigneeName.gridx = 2;
        gbc_geotechnicalAssigneeName.gridy = 3;
        geotechnicalPersonnelPane.add(geotechnicalAssigneeName, gbc_geotechnicalAssigneeName);
        
        geotechnicalHoursAssignedLabel = new JLabel("0.00");
        geotechnicalHoursAssignedLabel.setHorizontalAlignment(SwingConstants.TRAILING);
        GridBagConstraints gbc_geotechnicalHoursAssignedLabel = new GridBagConstraints();
        gbc_geotechnicalHoursAssignedLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_geotechnicalHoursAssignedLabel.insets = new Insets(0, 0, 5, 5);
        gbc_geotechnicalHoursAssignedLabel.gridx = 3;
        gbc_geotechnicalHoursAssignedLabel.gridy = 3;
        geotechnicalPersonnelPane.add(geotechnicalHoursAssignedLabel, gbc_geotechnicalHoursAssignedLabel);
        
        geotechnicalWorkRateLabel = new JLabel("Work Rate");
        geotechnicalWorkRateLabel.setHorizontalAlignment(SwingConstants.TRAILING);
        GridBagConstraints gbc_geotechnicalWorkRateLabel = new GridBagConstraints();
        gbc_geotechnicalWorkRateLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_geotechnicalWorkRateLabel.insets = new Insets(0, 0, 5, 5);
        gbc_geotechnicalWorkRateLabel.gridx = 4;
        gbc_geotechnicalWorkRateLabel.gridy = 3;
        geotechnicalPersonnelPane.add(geotechnicalWorkRateLabel, gbc_geotechnicalWorkRateLabel);
        
        geotechnicalDateCompletedLabel = new JLabel("00/00/1987");
        geotechnicalDateCompletedLabel.setHorizontalAlignment(SwingConstants.TRAILING);
        GridBagConstraints gbc_geotechnicalDateCompletedLabel = new GridBagConstraints();
        gbc_geotechnicalDateCompletedLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_geotechnicalDateCompletedLabel.insets = new Insets(0, 0, 5, 5);
        gbc_geotechnicalDateCompletedLabel.gridx = 5;
        gbc_geotechnicalDateCompletedLabel.gridy = 3;
        geotechnicalPersonnelPane.add(geotechnicalDateCompletedLabel, gbc_geotechnicalDateCompletedLabel);
        
        geotechnicalBackcheckLabel = new JLabel("--");
        geotechnicalBackcheckLabel.setHorizontalAlignment(SwingConstants.CENTER);
        GridBagConstraints gbc_geotechnicalBackcheckLabel = new GridBagConstraints();
        gbc_geotechnicalBackcheckLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_geotechnicalBackcheckLabel.insets = new Insets(0, 0, 5, 5);
        gbc_geotechnicalBackcheckLabel.gridx = 6;
        gbc_geotechnicalBackcheckLabel.gridy = 3;
        geotechnicalPersonnelPane.add(geotechnicalBackcheckLabel, gbc_geotechnicalBackcheckLabel);
        
        aeContractingPersonnelPane = new JPanel();
		assignPersonnelPane.add(aeContractingPersonnelPane);
        GridBagLayout gbl_aeContractingPersonnelPane = new GridBagLayout();
        gbl_aeContractingPersonnelPane.columnWidths = new int[]{0, 0, 310, 131, 138, 121, 80, 0, 0};
        gbl_aeContractingPersonnelPane.rowHeights = new int[]{0, 0, 0, 0, 0};
        gbl_aeContractingPersonnelPane.columnWeights = new double[]{0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
        gbl_aeContractingPersonnelPane.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
        aeContractingPersonnelPane.setLayout(gbl_aeContractingPersonnelPane);
        
        aeContractingVerticalStrut = Box.createVerticalStrut(20);
        GridBagConstraints gbc_aeContractingVerticalStrut = new GridBagConstraints();
        gbc_aeContractingVerticalStrut.insets = new Insets(0, 0, 5, 5);
        gbc_aeContractingVerticalStrut.gridx = 1;
        gbc_aeContractingVerticalStrut.gridy = 0;
        aeContractingPersonnelPane.add(aeContractingVerticalStrut, gbc_aeContractingVerticalStrut);
        
        aeContractingHorzizontalStrut = Box.createHorizontalStrut(20);
        GridBagConstraints gbc_aeContractingHorzizontalStrut = new GridBagConstraints();
        gbc_aeContractingHorzizontalStrut.insets = new Insets(0, 0, 5, 5);
        gbc_aeContractingHorzizontalStrut.gridx = 0;
        gbc_aeContractingHorzizontalStrut.gridy = 1;
        aeContractingPersonnelPane.add(aeContractingHorzizontalStrut, gbc_aeContractingHorzizontalStrut);
        
        aeContractingHorzizontalStrut_2 = Box.createHorizontalStrut(20);
        GridBagConstraints gbc_aeContractingHorzizontalStrut_2 = new GridBagConstraints();
        gbc_aeContractingHorzizontalStrut_2.insets = new Insets(0, 0, 5, 0);
        gbc_aeContractingHorzizontalStrut_2.gridx = 7;
        gbc_aeContractingHorzizontalStrut_2.gridy = 2;
        aeContractingPersonnelPane.add(aeContractingHorzizontalStrut_2, gbc_aeContractingHorzizontalStrut_2);
				
		aeContractingTitleLabel = new JLabel("AE Contracting");
        aeContractingTitleLabel.setOpaque(true);
        aeContractingTitleLabel.setBackground(new Color(255, 255, 204));
        GridBagConstraints gbc_aeContractingTitleLabel = new GridBagConstraints();
        gbc_aeContractingTitleLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_aeContractingTitleLabel.gridwidth = 6;
        gbc_aeContractingTitleLabel.insets = new Insets(0, 0, 5, 5);
        gbc_aeContractingTitleLabel.gridx = 1;
        gbc_aeContractingTitleLabel.gridy = 1;
        aeContractingPersonnelPane.add(aeContractingTitleLabel, gbc_aeContractingTitleLabel);
        
        aeContractingFillerLabel = new JLabel("  ");
        aeContractingFillerLabel.setOpaque(true);
        aeContractingFillerLabel.setBackground(new Color(153, 153, 153));
        GridBagConstraints gbc_aeContractingFillerLabel = new GridBagConstraints();
        gbc_aeContractingFillerLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_aeContractingFillerLabel.insets = new Insets(0, 0, 5, 5);
        gbc_aeContractingFillerLabel.gridx = 1;
        gbc_aeContractingFillerLabel.gridy = 2;
        aeContractingPersonnelPane.add(aeContractingFillerLabel, gbc_aeContractingFillerLabel);
        
        aeContractingAssigneeTitleLabel = new JLabel("Assignee");
        aeContractingAssigneeTitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        aeContractingAssigneeTitleLabel.setOpaque(true);
        aeContractingAssigneeTitleLabel.setForeground(new Color(255, 255, 255));
        aeContractingAssigneeTitleLabel.setBackground(new Color(153, 153, 153));
        GridBagConstraints gbc_aeContractingAssigneeTitleLabel = new GridBagConstraints();
        gbc_aeContractingAssigneeTitleLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_aeContractingAssigneeTitleLabel.insets = new Insets(0, 0, 5, 5);
        gbc_aeContractingAssigneeTitleLabel.gridx = 2;
        gbc_aeContractingAssigneeTitleLabel.gridy = 2;
        aeContractingPersonnelPane.add(aeContractingAssigneeTitleLabel, gbc_aeContractingAssigneeTitleLabel);
        
        aeContractingHoursAssingedTitleLabel = new JLabel("Hours Assigned");
        aeContractingHoursAssingedTitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        aeContractingHoursAssingedTitleLabel.setOpaque(true);
        aeContractingHoursAssingedTitleLabel.setForeground(new Color(255, 255, 255));
        aeContractingHoursAssingedTitleLabel.setBackground(new Color(153, 153, 153));
        GridBagConstraints gbc_aeContractingHoursAssingedTitleLabel = new GridBagConstraints();
        gbc_aeContractingHoursAssingedTitleLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_aeContractingHoursAssingedTitleLabel.insets = new Insets(0, 0, 5, 5);
        gbc_aeContractingHoursAssingedTitleLabel.gridx = 3;
        gbc_aeContractingHoursAssingedTitleLabel.gridy = 2;
        aeContractingPersonnelPane.add(aeContractingHoursAssingedTitleLabel, gbc_aeContractingHoursAssingedTitleLabel);
        
        aeContractingWorkRateTitleLabel = new JLabel("Work Rate");
        aeContractingWorkRateTitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        aeContractingWorkRateTitleLabel.setOpaque(true);
        aeContractingWorkRateTitleLabel.setForeground(new Color(255, 255, 255));
        aeContractingWorkRateTitleLabel.setBackground(new Color(153, 153, 153));
        GridBagConstraints gbc_aeContractingWorkRateTitleLabel = new GridBagConstraints();
        gbc_aeContractingWorkRateTitleLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_aeContractingWorkRateTitleLabel.insets = new Insets(0, 0, 5, 5);
        gbc_aeContractingWorkRateTitleLabel.gridx = 4;
        gbc_aeContractingWorkRateTitleLabel.gridy = 2;
        aeContractingPersonnelPane.add(aeContractingWorkRateTitleLabel, gbc_aeContractingWorkRateTitleLabel);
        
        aeContractingDateCompleteTitleLabel = new JLabel("Date Completed");
        aeContractingDateCompleteTitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        aeContractingDateCompleteTitleLabel.setOpaque(true);
        aeContractingDateCompleteTitleLabel.setForeground(new Color(255, 255, 255));
        aeContractingDateCompleteTitleLabel.setBackground(new Color(153, 153, 153));
        GridBagConstraints gbc_aeContractingDateCompleteTitleLabel = new GridBagConstraints();
        gbc_aeContractingDateCompleteTitleLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_aeContractingDateCompleteTitleLabel.insets = new Insets(0, 0, 5, 5);
        gbc_aeContractingDateCompleteTitleLabel.gridx = 5;
        gbc_aeContractingDateCompleteTitleLabel.gridy = 2;
        aeContractingPersonnelPane.add(aeContractingDateCompleteTitleLabel, gbc_aeContractingDateCompleteTitleLabel);
        
        aeContractingBackcheckTitleLabel = new JLabel("Backcheck");
        aeContractingBackcheckTitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        aeContractingBackcheckTitleLabel.setOpaque(true);
        aeContractingBackcheckTitleLabel.setForeground(new Color(255, 255, 255));
        aeContractingBackcheckTitleLabel.setBackground(new Color(153, 153, 153));
        GridBagConstraints gbc_aeContractingBackcheckTitleLabel = new GridBagConstraints();
        gbc_aeContractingBackcheckTitleLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_aeContractingBackcheckTitleLabel.insets = new Insets(0, 0, 5, 5);
        gbc_aeContractingBackcheckTitleLabel.gridx = 6;
        gbc_aeContractingBackcheckTitleLabel.gridy = 2;
        aeContractingPersonnelPane.add(aeContractingBackcheckTitleLabel, gbc_aeContractingBackcheckTitleLabel);
        
        aeContractingAssigneeCheckBox = new JCheckBox("");
        GridBagConstraints gbc_aeContractingAssigneeCheckBox = new GridBagConstraints();
        gbc_aeContractingAssigneeCheckBox.insets = new Insets(0, 0, 5, 5);
        gbc_aeContractingAssigneeCheckBox.gridx = 1;
        gbc_aeContractingAssigneeCheckBox.gridy = 3;
        aeContractingPersonnelPane.add(aeContractingAssigneeCheckBox, gbc_aeContractingAssigneeCheckBox);
        
        aeContractingAssigneeName = new JLabel("Assignee Name");
        GridBagConstraints gbc_aeContractingAssigneeName = new GridBagConstraints();
        gbc_aeContractingAssigneeName.fill = GridBagConstraints.HORIZONTAL;
        gbc_aeContractingAssigneeName.insets = new Insets(0, 0, 5, 5);
        gbc_aeContractingAssigneeName.gridx = 2;
        gbc_aeContractingAssigneeName.gridy = 3;
        aeContractingPersonnelPane.add(aeContractingAssigneeName, gbc_aeContractingAssigneeName);
        
        aeContractingHoursAssignedLabel = new JLabel("0.00");
        aeContractingHoursAssignedLabel.setHorizontalAlignment(SwingConstants.TRAILING);
        GridBagConstraints gbc_aeContractingHoursAssignedLabel = new GridBagConstraints();
        gbc_aeContractingHoursAssignedLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_aeContractingHoursAssignedLabel.insets = new Insets(0, 0, 5, 5);
        gbc_aeContractingHoursAssignedLabel.gridx = 3;
        gbc_aeContractingHoursAssignedLabel.gridy = 3;
        aeContractingPersonnelPane.add(aeContractingHoursAssignedLabel, gbc_aeContractingHoursAssignedLabel);
        
        aeContractingWorkRateLabel = new JLabel("Work Rate");
        aeContractingWorkRateLabel.setHorizontalAlignment(SwingConstants.TRAILING);
        GridBagConstraints gbc_aeContractingWorkRateLabel = new GridBagConstraints();
        gbc_aeContractingWorkRateLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_aeContractingWorkRateLabel.insets = new Insets(0, 0, 5, 5);
        gbc_aeContractingWorkRateLabel.gridx = 4;
        gbc_aeContractingWorkRateLabel.gridy = 3;
        aeContractingPersonnelPane.add(aeContractingWorkRateLabel, gbc_aeContractingWorkRateLabel);
        
        aeContractingDateCompletedLabel = new JLabel("00/00/1987");
        aeContractingDateCompletedLabel.setHorizontalAlignment(SwingConstants.TRAILING);
        GridBagConstraints gbc_aeContractingDateCompletedLabel = new GridBagConstraints();
        gbc_aeContractingDateCompletedLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_aeContractingDateCompletedLabel.insets = new Insets(0, 0, 5, 5);
        gbc_aeContractingDateCompletedLabel.gridx = 5;
        gbc_aeContractingDateCompletedLabel.gridy = 3;
        aeContractingPersonnelPane.add(aeContractingDateCompletedLabel, gbc_aeContractingDateCompletedLabel);
        
        aeContractingBackcheckLabel = new JLabel("--");
        aeContractingBackcheckLabel.setHorizontalAlignment(SwingConstants.CENTER);
        GridBagConstraints gbc_aeContractingBackcheckLabel = new GridBagConstraints();
        gbc_aeContractingBackcheckLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_aeContractingBackcheckLabel.insets = new Insets(0, 0, 5, 5);
        gbc_aeContractingBackcheckLabel.gridx = 6;
        gbc_aeContractingBackcheckLabel.gridy = 3;
        aeContractingPersonnelPane.add(aeContractingBackcheckLabel, gbc_aeContractingBackcheckLabel);
        
        valueEngineerPersonnelPane = new JPanel();
		assignPersonnelPane.add(valueEngineerPersonnelPane);
        GridBagLayout gbl_valueEngineerPersonnelPane = new GridBagLayout();
        gbl_valueEngineerPersonnelPane.columnWidths = new int[]{0, 0, 310, 131, 138, 121, 80, 0, 0};
        gbl_valueEngineerPersonnelPane.rowHeights = new int[]{0, 0, 0, 0, 0};
        gbl_valueEngineerPersonnelPane.columnWeights = new double[]{0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
        gbl_valueEngineerPersonnelPane.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
        valueEngineerPersonnelPane.setLayout(gbl_valueEngineerPersonnelPane);
        
        valueEngineerVerticalStrut = Box.createVerticalStrut(20);
        GridBagConstraints gbc_valueEngineerVerticalStrut = new GridBagConstraints();
        gbc_valueEngineerVerticalStrut.insets = new Insets(0, 0, 5, 5);
        gbc_valueEngineerVerticalStrut.gridx = 1;
        gbc_valueEngineerVerticalStrut.gridy = 0;
        valueEngineerPersonnelPane.add(valueEngineerVerticalStrut, gbc_valueEngineerVerticalStrut);
        
        valueEngineerHorzizontalStrut = Box.createHorizontalStrut(20);
        GridBagConstraints gbc_valueEngineerHorzizontalStrut = new GridBagConstraints();
        gbc_valueEngineerHorzizontalStrut.insets = new Insets(0, 0, 5, 5);
        gbc_valueEngineerHorzizontalStrut.gridx = 0;
        gbc_valueEngineerHorzizontalStrut.gridy = 1;
        valueEngineerPersonnelPane.add(valueEngineerHorzizontalStrut, gbc_valueEngineerHorzizontalStrut);
        
        valueEngineerHorzizontalStrut_2 = Box.createHorizontalStrut(20);
        GridBagConstraints gbc_valueEngineerHorzizontalStrut_2 = new GridBagConstraints();
        gbc_valueEngineerHorzizontalStrut_2.insets = new Insets(0, 0, 5, 0);
        gbc_valueEngineerHorzizontalStrut_2.gridx = 7;
        gbc_valueEngineerHorzizontalStrut_2.gridy = 2;
        valueEngineerPersonnelPane.add(valueEngineerHorzizontalStrut_2, gbc_valueEngineerHorzizontalStrut_2);
				
		valueEngineerTitleLabel = new JLabel("Value Engineer");
        valueEngineerTitleLabel.setOpaque(true);
        valueEngineerTitleLabel.setBackground(new Color(255, 255, 204));
        GridBagConstraints gbc_valueEngineerTitleLabel = new GridBagConstraints();
        gbc_valueEngineerTitleLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_valueEngineerTitleLabel.gridwidth = 6;
        gbc_valueEngineerTitleLabel.insets = new Insets(0, 0, 5, 5);
        gbc_valueEngineerTitleLabel.gridx = 1;
        gbc_valueEngineerTitleLabel.gridy = 1;
        valueEngineerPersonnelPane.add(valueEngineerTitleLabel, gbc_valueEngineerTitleLabel);
        
        valueEngineerFillerLabel = new JLabel("  ");
        valueEngineerFillerLabel.setOpaque(true);
        valueEngineerFillerLabel.setBackground(new Color(153, 153, 153));
        GridBagConstraints gbc_valueEngineerFillerLabel = new GridBagConstraints();
        gbc_valueEngineerFillerLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_valueEngineerFillerLabel.insets = new Insets(0, 0, 5, 5);
        gbc_valueEngineerFillerLabel.gridx = 1;
        gbc_valueEngineerFillerLabel.gridy = 2;
        valueEngineerPersonnelPane.add(valueEngineerFillerLabel, gbc_valueEngineerFillerLabel);
        
        valueEngineerAssigneeTitleLabel = new JLabel("Assignee");
        valueEngineerAssigneeTitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        valueEngineerAssigneeTitleLabel.setOpaque(true);
        valueEngineerAssigneeTitleLabel.setForeground(new Color(255, 255, 255));
        valueEngineerAssigneeTitleLabel.setBackground(new Color(153, 153, 153));
        GridBagConstraints gbc_valueEngineerAssigneeTitleLabel = new GridBagConstraints();
        gbc_valueEngineerAssigneeTitleLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_valueEngineerAssigneeTitleLabel.insets = new Insets(0, 0, 5, 5);
        gbc_valueEngineerAssigneeTitleLabel.gridx = 2;
        gbc_valueEngineerAssigneeTitleLabel.gridy = 2;
        valueEngineerPersonnelPane.add(valueEngineerAssigneeTitleLabel, gbc_valueEngineerAssigneeTitleLabel);
        
        valueEngineerHoursAssingedTitleLabel = new JLabel("Hours Assigned");
        valueEngineerHoursAssingedTitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        valueEngineerHoursAssingedTitleLabel.setOpaque(true);
        valueEngineerHoursAssingedTitleLabel.setForeground(new Color(255, 255, 255));
        valueEngineerHoursAssingedTitleLabel.setBackground(new Color(153, 153, 153));
        GridBagConstraints gbc_valueEngineerHoursAssingedTitleLabel = new GridBagConstraints();
        gbc_valueEngineerHoursAssingedTitleLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_valueEngineerHoursAssingedTitleLabel.insets = new Insets(0, 0, 5, 5);
        gbc_valueEngineerHoursAssingedTitleLabel.gridx = 3;
        gbc_valueEngineerHoursAssingedTitleLabel.gridy = 2;
        valueEngineerPersonnelPane.add(valueEngineerHoursAssingedTitleLabel, gbc_valueEngineerHoursAssingedTitleLabel);
        
        valueEngineerWorkRateTitleLabel = new JLabel("Work Rate");
        valueEngineerWorkRateTitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        valueEngineerWorkRateTitleLabel.setOpaque(true);
        valueEngineerWorkRateTitleLabel.setForeground(new Color(255, 255, 255));
        valueEngineerWorkRateTitleLabel.setBackground(new Color(153, 153, 153));
        GridBagConstraints gbc_valueEngineerWorkRateTitleLabel = new GridBagConstraints();
        gbc_valueEngineerWorkRateTitleLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_valueEngineerWorkRateTitleLabel.insets = new Insets(0, 0, 5, 5);
        gbc_valueEngineerWorkRateTitleLabel.gridx = 4;
        gbc_valueEngineerWorkRateTitleLabel.gridy = 2;
        valueEngineerPersonnelPane.add(valueEngineerWorkRateTitleLabel, gbc_valueEngineerWorkRateTitleLabel);
        
        valueEngineerDateCompleteTitleLabel = new JLabel("Date Completed");
        valueEngineerDateCompleteTitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        valueEngineerDateCompleteTitleLabel.setOpaque(true);
        valueEngineerDateCompleteTitleLabel.setForeground(new Color(255, 255, 255));
        valueEngineerDateCompleteTitleLabel.setBackground(new Color(153, 153, 153));
        GridBagConstraints gbc_valueEngineerDateCompleteTitleLabel = new GridBagConstraints();
        gbc_valueEngineerDateCompleteTitleLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_valueEngineerDateCompleteTitleLabel.insets = new Insets(0, 0, 5, 5);
        gbc_valueEngineerDateCompleteTitleLabel.gridx = 5;
        gbc_valueEngineerDateCompleteTitleLabel.gridy = 2;
        valueEngineerPersonnelPane.add(valueEngineerDateCompleteTitleLabel, gbc_valueEngineerDateCompleteTitleLabel);
        
        valueEngineerBackcheckTitleLabel = new JLabel("Backcheck");
        valueEngineerBackcheckTitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        valueEngineerBackcheckTitleLabel.setOpaque(true);
        valueEngineerBackcheckTitleLabel.setForeground(new Color(255, 255, 255));
        valueEngineerBackcheckTitleLabel.setBackground(new Color(153, 153, 153));
        GridBagConstraints gbc_valueEngineerBackcheckTitleLabel = new GridBagConstraints();
        gbc_valueEngineerBackcheckTitleLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_valueEngineerBackcheckTitleLabel.insets = new Insets(0, 0, 5, 5);
        gbc_valueEngineerBackcheckTitleLabel.gridx = 6;
        gbc_valueEngineerBackcheckTitleLabel.gridy = 2;
        valueEngineerPersonnelPane.add(valueEngineerBackcheckTitleLabel, gbc_valueEngineerBackcheckTitleLabel);
        
        valueEngineerAssigneeCheckBox = new JCheckBox("");
        GridBagConstraints gbc_valueEngineerAssigneeCheckBox = new GridBagConstraints();
        gbc_valueEngineerAssigneeCheckBox.insets = new Insets(0, 0, 5, 5);
        gbc_valueEngineerAssigneeCheckBox.gridx = 1;
        gbc_valueEngineerAssigneeCheckBox.gridy = 3;
        valueEngineerPersonnelPane.add(valueEngineerAssigneeCheckBox, gbc_valueEngineerAssigneeCheckBox);
        
        valueEngineerAssigneeName = new JLabel("Assignee Name");
        GridBagConstraints gbc_valueEngineerAssigneeName = new GridBagConstraints();
        gbc_valueEngineerAssigneeName.fill = GridBagConstraints.HORIZONTAL;
        gbc_valueEngineerAssigneeName.insets = new Insets(0, 0, 5, 5);
        gbc_valueEngineerAssigneeName.gridx = 2;
        gbc_valueEngineerAssigneeName.gridy = 3;
        valueEngineerPersonnelPane.add(valueEngineerAssigneeName, gbc_valueEngineerAssigneeName);
        
        valueEngineerHoursAssignedLabel = new JLabel("0.00");
        valueEngineerHoursAssignedLabel.setHorizontalAlignment(SwingConstants.TRAILING);
        GridBagConstraints gbc_valueEngineerHoursAssignedLabel = new GridBagConstraints();
        gbc_valueEngineerHoursAssignedLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_valueEngineerHoursAssignedLabel.insets = new Insets(0, 0, 5, 5);
        gbc_valueEngineerHoursAssignedLabel.gridx = 3;
        gbc_valueEngineerHoursAssignedLabel.gridy = 3;
        valueEngineerPersonnelPane.add(valueEngineerHoursAssignedLabel, gbc_valueEngineerHoursAssignedLabel);
        
        valueEngineerWorkRateLabel = new JLabel("Work Rate");
        valueEngineerWorkRateLabel.setHorizontalAlignment(SwingConstants.TRAILING);
        GridBagConstraints gbc_valueEngineerWorkRateLabel = new GridBagConstraints();
        gbc_valueEngineerWorkRateLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_valueEngineerWorkRateLabel.insets = new Insets(0, 0, 5, 5);
        gbc_valueEngineerWorkRateLabel.gridx = 4;
        gbc_valueEngineerWorkRateLabel.gridy = 3;
        valueEngineerPersonnelPane.add(valueEngineerWorkRateLabel, gbc_valueEngineerWorkRateLabel);
        
        valueEngineerDateCompletedLabel = new JLabel("00/00/1987");
        valueEngineerDateCompletedLabel.setHorizontalAlignment(SwingConstants.TRAILING);
        GridBagConstraints gbc_valueEngineerDateCompletedLabel = new GridBagConstraints();
        gbc_valueEngineerDateCompletedLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_valueEngineerDateCompletedLabel.insets = new Insets(0, 0, 5, 5);
        gbc_valueEngineerDateCompletedLabel.gridx = 5;
        gbc_valueEngineerDateCompletedLabel.gridy = 3;
        valueEngineerPersonnelPane.add(valueEngineerDateCompletedLabel, gbc_valueEngineerDateCompletedLabel);
        
        valueEngineerBackcheckLabel = new JLabel("--");
        valueEngineerBackcheckLabel.setHorizontalAlignment(SwingConstants.CENTER);
        GridBagConstraints gbc_valueEngineerBackcheckLabel = new GridBagConstraints();
        gbc_valueEngineerBackcheckLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_valueEngineerBackcheckLabel.insets = new Insets(0, 0, 5, 5);
        gbc_valueEngineerBackcheckLabel.gridx = 6;
        gbc_valueEngineerBackcheckLabel.gridy = 3;
        valueEngineerPersonnelPane.add(valueEngineerBackcheckLabel, gbc_valueEngineerBackcheckLabel);
        
        translatorPersonnelPane = new JPanel();
		assignPersonnelPane.add(translatorPersonnelPane);
        GridBagLayout gbl_translatorPersonnelPane = new GridBagLayout();
        gbl_translatorPersonnelPane.columnWidths = new int[]{0, 0, 310, 131, 138, 121, 80, 0, 0};
        gbl_translatorPersonnelPane.rowHeights = new int[]{0, 0, 0, 0, 0};
        gbl_translatorPersonnelPane.columnWeights = new double[]{0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
        gbl_translatorPersonnelPane.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
        translatorPersonnelPane.setLayout(gbl_translatorPersonnelPane);
        
        translatorVerticalStrut = Box.createVerticalStrut(20);
        GridBagConstraints gbc_translatorVerticalStrut = new GridBagConstraints();
        gbc_translatorVerticalStrut.insets = new Insets(0, 0, 5, 5);
        gbc_translatorVerticalStrut.gridx = 1;
        gbc_translatorVerticalStrut.gridy = 0;
        translatorPersonnelPane.add(translatorVerticalStrut, gbc_translatorVerticalStrut);
        
        translatorHorzizontalStrut = Box.createHorizontalStrut(20);
        GridBagConstraints gbc_translatorHorzizontalStrut = new GridBagConstraints();
        gbc_translatorHorzizontalStrut.insets = new Insets(0, 0, 5, 5);
        gbc_translatorHorzizontalStrut.gridx = 0;
        gbc_translatorHorzizontalStrut.gridy = 1;
        translatorPersonnelPane.add(translatorHorzizontalStrut, gbc_translatorHorzizontalStrut);
        
        translatorHorzizontalStrut_2 = Box.createHorizontalStrut(20);
        GridBagConstraints gbc_translatorHorzizontalStrut_2 = new GridBagConstraints();
        gbc_translatorHorzizontalStrut_2.insets = new Insets(0, 0, 5, 0);
        gbc_translatorHorzizontalStrut_2.gridx = 7;
        gbc_translatorHorzizontalStrut_2.gridy = 2;
        translatorPersonnelPane.add(translatorHorzizontalStrut_2, gbc_translatorHorzizontalStrut_2);
				
		translatorTitleLabel = new JLabel("Translator");
        translatorTitleLabel.setOpaque(true);
        translatorTitleLabel.setBackground(new Color(255, 255, 204));
        GridBagConstraints gbc_translatorTitleLabel = new GridBagConstraints();
        gbc_translatorTitleLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_translatorTitleLabel.gridwidth = 6;
        gbc_translatorTitleLabel.insets = new Insets(0, 0, 5, 5);
        gbc_translatorTitleLabel.gridx = 1;
        gbc_translatorTitleLabel.gridy = 1;
        translatorPersonnelPane.add(translatorTitleLabel, gbc_translatorTitleLabel);
        
        translatorFillerLabel = new JLabel("  ");
        translatorFillerLabel.setOpaque(true);
        translatorFillerLabel.setBackground(new Color(153, 153, 153));
        GridBagConstraints gbc_translatorFillerLabel = new GridBagConstraints();
        gbc_translatorFillerLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_translatorFillerLabel.insets = new Insets(0, 0, 5, 5);
        gbc_translatorFillerLabel.gridx = 1;
        gbc_translatorFillerLabel.gridy = 2;
        translatorPersonnelPane.add(translatorFillerLabel, gbc_translatorFillerLabel);
        
        translatorAssigneeTitleLabel = new JLabel("Assignee");
        translatorAssigneeTitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        translatorAssigneeTitleLabel.setOpaque(true);
        translatorAssigneeTitleLabel.setForeground(new Color(255, 255, 255));
        translatorAssigneeTitleLabel.setBackground(new Color(153, 153, 153));
        GridBagConstraints gbc_translatorAssigneeTitleLabel = new GridBagConstraints();
        gbc_translatorAssigneeTitleLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_translatorAssigneeTitleLabel.insets = new Insets(0, 0, 5, 5);
        gbc_translatorAssigneeTitleLabel.gridx = 2;
        gbc_translatorAssigneeTitleLabel.gridy = 2;
        translatorPersonnelPane.add(translatorAssigneeTitleLabel, gbc_translatorAssigneeTitleLabel);
        
        translatorHoursAssingedTitleLabel = new JLabel("Hours Assigned");
        translatorHoursAssingedTitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        translatorHoursAssingedTitleLabel.setOpaque(true);
        translatorHoursAssingedTitleLabel.setForeground(new Color(255, 255, 255));
        translatorHoursAssingedTitleLabel.setBackground(new Color(153, 153, 153));
        GridBagConstraints gbc_translatorHoursAssingedTitleLabel = new GridBagConstraints();
        gbc_translatorHoursAssingedTitleLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_translatorHoursAssingedTitleLabel.insets = new Insets(0, 0, 5, 5);
        gbc_translatorHoursAssingedTitleLabel.gridx = 3;
        gbc_translatorHoursAssingedTitleLabel.gridy = 2;
        translatorPersonnelPane.add(translatorHoursAssingedTitleLabel, gbc_translatorHoursAssingedTitleLabel);
        
        translatorWorkRateTitleLabel = new JLabel("Work Rate");
        translatorWorkRateTitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        translatorWorkRateTitleLabel.setOpaque(true);
        translatorWorkRateTitleLabel.setForeground(new Color(255, 255, 255));
        translatorWorkRateTitleLabel.setBackground(new Color(153, 153, 153));
        GridBagConstraints gbc_translatorWorkRateTitleLabel = new GridBagConstraints();
        gbc_translatorWorkRateTitleLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_translatorWorkRateTitleLabel.insets = new Insets(0, 0, 5, 5);
        gbc_translatorWorkRateTitleLabel.gridx = 4;
        gbc_translatorWorkRateTitleLabel.gridy = 2;
        translatorPersonnelPane.add(translatorWorkRateTitleLabel, gbc_translatorWorkRateTitleLabel);
        
        translatorDateCompleteTitleLabel = new JLabel("Date Completed");
        translatorDateCompleteTitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        translatorDateCompleteTitleLabel.setOpaque(true);
        translatorDateCompleteTitleLabel.setForeground(new Color(255, 255, 255));
        translatorDateCompleteTitleLabel.setBackground(new Color(153, 153, 153));
        GridBagConstraints gbc_translatorDateCompleteTitleLabel = new GridBagConstraints();
        gbc_translatorDateCompleteTitleLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_translatorDateCompleteTitleLabel.insets = new Insets(0, 0, 5, 5);
        gbc_translatorDateCompleteTitleLabel.gridx = 5;
        gbc_translatorDateCompleteTitleLabel.gridy = 2;
        translatorPersonnelPane.add(translatorDateCompleteTitleLabel, gbc_translatorDateCompleteTitleLabel);
        
        translatorBackcheckTitleLabel = new JLabel("Backcheck");
        translatorBackcheckTitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        translatorBackcheckTitleLabel.setOpaque(true);
        translatorBackcheckTitleLabel.setForeground(new Color(255, 255, 255));
        translatorBackcheckTitleLabel.setBackground(new Color(153, 153, 153));
        GridBagConstraints gbc_translatorBackcheckTitleLabel = new GridBagConstraints();
        gbc_translatorBackcheckTitleLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_translatorBackcheckTitleLabel.insets = new Insets(0, 0, 5, 5);
        gbc_translatorBackcheckTitleLabel.gridx = 6;
        gbc_translatorBackcheckTitleLabel.gridy = 2;
        translatorPersonnelPane.add(translatorBackcheckTitleLabel, gbc_translatorBackcheckTitleLabel);
        
        translatorAssigneeCheckBox = new JCheckBox("");
        GridBagConstraints gbc_translatorAssigneeCheckBox = new GridBagConstraints();
        gbc_translatorAssigneeCheckBox.insets = new Insets(0, 0, 5, 5);
        gbc_translatorAssigneeCheckBox.gridx = 1;
        gbc_translatorAssigneeCheckBox.gridy = 3;
        translatorPersonnelPane.add(translatorAssigneeCheckBox, gbc_translatorAssigneeCheckBox);
        
        translatorAssigneeName = new JLabel("Assignee Name");
        GridBagConstraints gbc_translatorAssigneeName = new GridBagConstraints();
        gbc_translatorAssigneeName.fill = GridBagConstraints.HORIZONTAL;
        gbc_translatorAssigneeName.insets = new Insets(0, 0, 5, 5);
        gbc_translatorAssigneeName.gridx = 2;
        gbc_translatorAssigneeName.gridy = 3;
        translatorPersonnelPane.add(translatorAssigneeName, gbc_translatorAssigneeName);
        
        translatorHoursAssignedLabel = new JLabel("0.00");
        translatorHoursAssignedLabel.setHorizontalAlignment(SwingConstants.TRAILING);
        GridBagConstraints gbc_translatorHoursAssignedLabel = new GridBagConstraints();
        gbc_translatorHoursAssignedLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_translatorHoursAssignedLabel.insets = new Insets(0, 0, 5, 5);
        gbc_translatorHoursAssignedLabel.gridx = 3;
        gbc_translatorHoursAssignedLabel.gridy = 3;
        translatorPersonnelPane.add(translatorHoursAssignedLabel, gbc_translatorHoursAssignedLabel);
        
        translatorWorkRateLabel = new JLabel("Work Rate");
        translatorWorkRateLabel.setHorizontalAlignment(SwingConstants.TRAILING);
        GridBagConstraints gbc_translatorWorkRateLabel = new GridBagConstraints();
        gbc_translatorWorkRateLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_translatorWorkRateLabel.insets = new Insets(0, 0, 5, 5);
        gbc_translatorWorkRateLabel.gridx = 4;
        gbc_translatorWorkRateLabel.gridy = 3;
        translatorPersonnelPane.add(translatorWorkRateLabel, gbc_translatorWorkRateLabel);
        
        translatorDateCompletedLabel = new JLabel("00/00/1987");
        translatorDateCompletedLabel.setHorizontalAlignment(SwingConstants.TRAILING);
        GridBagConstraints gbc_translatorDateCompletedLabel = new GridBagConstraints();
        gbc_translatorDateCompletedLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_translatorDateCompletedLabel.insets = new Insets(0, 0, 5, 5);
        gbc_translatorDateCompletedLabel.gridx = 5;
        gbc_translatorDateCompletedLabel.gridy = 3;
        translatorPersonnelPane.add(translatorDateCompletedLabel, gbc_translatorDateCompletedLabel);
        
        translatorBackcheckLabel = new JLabel("--");
        translatorBackcheckLabel.setHorizontalAlignment(SwingConstants.CENTER);
        GridBagConstraints gbc_translatorBackcheckLabel = new GridBagConstraints();
        gbc_translatorBackcheckLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_translatorBackcheckLabel.insets = new Insets(0, 0, 5, 5);
        gbc_translatorBackcheckLabel.gridx = 6;
        gbc_translatorBackcheckLabel.gridy = 3;
        translatorPersonnelPane.add(translatorBackcheckLabel, gbc_translatorBackcheckLabel);
        
        specificationsPersonnelPane = new JPanel();
		assignPersonnelPane.add(specificationsPersonnelPane);
        GridBagLayout gbl_specificationsPersonnelPane = new GridBagLayout();
        gbl_specificationsPersonnelPane.columnWidths = new int[]{0, 0, 310, 131, 138, 121, 80, 0, 0};
        gbl_specificationsPersonnelPane.rowHeights = new int[]{0, 0, 0, 0, 0};
        gbl_specificationsPersonnelPane.columnWeights = new double[]{0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
        gbl_specificationsPersonnelPane.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
        specificationsPersonnelPane.setLayout(gbl_specificationsPersonnelPane);
        
        specificationsVerticalStrut = Box.createVerticalStrut(20);
        GridBagConstraints gbc_specificationsVerticalStrut = new GridBagConstraints();
        gbc_specificationsVerticalStrut.insets = new Insets(0, 0, 5, 5);
        gbc_specificationsVerticalStrut.gridx = 1;
        gbc_specificationsVerticalStrut.gridy = 0;
        specificationsPersonnelPane.add(specificationsVerticalStrut, gbc_specificationsVerticalStrut);
        
        specificationsHorzizontalStrut = Box.createHorizontalStrut(20);
        GridBagConstraints gbc_specificationsHorzizontalStrut = new GridBagConstraints();
        gbc_specificationsHorzizontalStrut.insets = new Insets(0, 0, 5, 5);
        gbc_specificationsHorzizontalStrut.gridx = 0;
        gbc_specificationsHorzizontalStrut.gridy = 1;
        specificationsPersonnelPane.add(specificationsHorzizontalStrut, gbc_specificationsHorzizontalStrut);
        
        specificationsHorzizontalStrut_2 = Box.createHorizontalStrut(20);
        GridBagConstraints gbc_specificationsHorzizontalStrut_2 = new GridBagConstraints();
        gbc_specificationsHorzizontalStrut_2.insets = new Insets(0, 0, 5, 0);
        gbc_specificationsHorzizontalStrut_2.gridx = 7;
        gbc_specificationsHorzizontalStrut_2.gridy = 2;
        specificationsPersonnelPane.add(specificationsHorzizontalStrut_2, gbc_specificationsHorzizontalStrut_2);
				
		specificationsTitleLabel = new JLabel("Specifications");
        specificationsTitleLabel.setOpaque(true);
        specificationsTitleLabel.setBackground(new Color(255, 255, 204));
        GridBagConstraints gbc_specificationsTitleLabel = new GridBagConstraints();
        gbc_specificationsTitleLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_specificationsTitleLabel.gridwidth = 6;
        gbc_specificationsTitleLabel.insets = new Insets(0, 0, 5, 5);
        gbc_specificationsTitleLabel.gridx = 1;
        gbc_specificationsTitleLabel.gridy = 1;
        specificationsPersonnelPane.add(specificationsTitleLabel, gbc_specificationsTitleLabel);
        
        specificationsFillerLabel = new JLabel("  ");
        specificationsFillerLabel.setOpaque(true);
        specificationsFillerLabel.setBackground(new Color(153, 153, 153));
        GridBagConstraints gbc_specificationsFillerLabel = new GridBagConstraints();
        gbc_specificationsFillerLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_specificationsFillerLabel.insets = new Insets(0, 0, 5, 5);
        gbc_specificationsFillerLabel.gridx = 1;
        gbc_specificationsFillerLabel.gridy = 2;
        specificationsPersonnelPane.add(specificationsFillerLabel, gbc_specificationsFillerLabel);
        
        specificationsAssigneeTitleLabel = new JLabel("Assignee");
        specificationsAssigneeTitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        specificationsAssigneeTitleLabel.setOpaque(true);
        specificationsAssigneeTitleLabel.setForeground(new Color(255, 255, 255));
        specificationsAssigneeTitleLabel.setBackground(new Color(153, 153, 153));
        GridBagConstraints gbc_specificationsAssigneeTitleLabel = new GridBagConstraints();
        gbc_specificationsAssigneeTitleLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_specificationsAssigneeTitleLabel.insets = new Insets(0, 0, 5, 5);
        gbc_specificationsAssigneeTitleLabel.gridx = 2;
        gbc_specificationsAssigneeTitleLabel.gridy = 2;
        specificationsPersonnelPane.add(specificationsAssigneeTitleLabel, gbc_specificationsAssigneeTitleLabel);
        
        specificationsHoursAssingedTitleLabel = new JLabel("Hours Assigned");
        specificationsHoursAssingedTitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        specificationsHoursAssingedTitleLabel.setOpaque(true);
        specificationsHoursAssingedTitleLabel.setForeground(new Color(255, 255, 255));
        specificationsHoursAssingedTitleLabel.setBackground(new Color(153, 153, 153));
        GridBagConstraints gbc_specificationsHoursAssingedTitleLabel = new GridBagConstraints();
        gbc_specificationsHoursAssingedTitleLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_specificationsHoursAssingedTitleLabel.insets = new Insets(0, 0, 5, 5);
        gbc_specificationsHoursAssingedTitleLabel.gridx = 3;
        gbc_specificationsHoursAssingedTitleLabel.gridy = 2;
        specificationsPersonnelPane.add(specificationsHoursAssingedTitleLabel, gbc_specificationsHoursAssingedTitleLabel);
        
        specificationsWorkRateTitleLabel = new JLabel("Work Rate");
        specificationsWorkRateTitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        specificationsWorkRateTitleLabel.setOpaque(true);
        specificationsWorkRateTitleLabel.setForeground(new Color(255, 255, 255));
        specificationsWorkRateTitleLabel.setBackground(new Color(153, 153, 153));
        GridBagConstraints gbc_specificationsWorkRateTitleLabel = new GridBagConstraints();
        gbc_specificationsWorkRateTitleLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_specificationsWorkRateTitleLabel.insets = new Insets(0, 0, 5, 5);
        gbc_specificationsWorkRateTitleLabel.gridx = 4;
        gbc_specificationsWorkRateTitleLabel.gridy = 2;
        specificationsPersonnelPane.add(specificationsWorkRateTitleLabel, gbc_specificationsWorkRateTitleLabel);
        
        specificationsDateCompleteTitleLabel = new JLabel("Date Completed");
        specificationsDateCompleteTitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        specificationsDateCompleteTitleLabel.setOpaque(true);
        specificationsDateCompleteTitleLabel.setForeground(new Color(255, 255, 255));
        specificationsDateCompleteTitleLabel.setBackground(new Color(153, 153, 153));
        GridBagConstraints gbc_specificationsDateCompleteTitleLabel = new GridBagConstraints();
        gbc_specificationsDateCompleteTitleLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_specificationsDateCompleteTitleLabel.insets = new Insets(0, 0, 5, 5);
        gbc_specificationsDateCompleteTitleLabel.gridx = 5;
        gbc_specificationsDateCompleteTitleLabel.gridy = 2;
        specificationsPersonnelPane.add(specificationsDateCompleteTitleLabel, gbc_specificationsDateCompleteTitleLabel);
        
        specificationsBackcheckTitleLabel = new JLabel("Backcheck");
        specificationsBackcheckTitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        specificationsBackcheckTitleLabel.setOpaque(true);
        specificationsBackcheckTitleLabel.setForeground(new Color(255, 255, 255));
        specificationsBackcheckTitleLabel.setBackground(new Color(153, 153, 153));
        GridBagConstraints gbc_specificationsBackcheckTitleLabel = new GridBagConstraints();
        gbc_specificationsBackcheckTitleLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_specificationsBackcheckTitleLabel.insets = new Insets(0, 0, 5, 5);
        gbc_specificationsBackcheckTitleLabel.gridx = 6;
        gbc_specificationsBackcheckTitleLabel.gridy = 2;
        specificationsPersonnelPane.add(specificationsBackcheckTitleLabel, gbc_specificationsBackcheckTitleLabel);
        
        specificationsAssigneeCheckBox = new JCheckBox("");
        GridBagConstraints gbc_specificationsAssigneeCheckBox = new GridBagConstraints();
        gbc_specificationsAssigneeCheckBox.insets = new Insets(0, 0, 5, 5);
        gbc_specificationsAssigneeCheckBox.gridx = 1;
        gbc_specificationsAssigneeCheckBox.gridy = 3;
        specificationsPersonnelPane.add(specificationsAssigneeCheckBox, gbc_specificationsAssigneeCheckBox);
        
        specificationsAssigneeName = new JLabel("Assignee Name");
        GridBagConstraints gbc_specificationsAssigneeName = new GridBagConstraints();
        gbc_specificationsAssigneeName.fill = GridBagConstraints.HORIZONTAL;
        gbc_specificationsAssigneeName.insets = new Insets(0, 0, 5, 5);
        gbc_specificationsAssigneeName.gridx = 2;
        gbc_specificationsAssigneeName.gridy = 3;
        specificationsPersonnelPane.add(specificationsAssigneeName, gbc_specificationsAssigneeName);
        
        specificationsHoursAssignedLabel = new JLabel("0.00");
        specificationsHoursAssignedLabel.setHorizontalAlignment(SwingConstants.TRAILING);
        GridBagConstraints gbc_specificationsHoursAssignedLabel = new GridBagConstraints();
        gbc_specificationsHoursAssignedLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_specificationsHoursAssignedLabel.insets = new Insets(0, 0, 5, 5);
        gbc_specificationsHoursAssignedLabel.gridx = 3;
        gbc_specificationsHoursAssignedLabel.gridy = 3;
        specificationsPersonnelPane.add(specificationsHoursAssignedLabel, gbc_specificationsHoursAssignedLabel);
        
        specificationsWorkRateLabel = new JLabel("Work Rate");
        specificationsWorkRateLabel.setHorizontalAlignment(SwingConstants.TRAILING);
        GridBagConstraints gbc_specificationsWorkRateLabel = new GridBagConstraints();
        gbc_specificationsWorkRateLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_specificationsWorkRateLabel.insets = new Insets(0, 0, 5, 5);
        gbc_specificationsWorkRateLabel.gridx = 4;
        gbc_specificationsWorkRateLabel.gridy = 3;
        specificationsPersonnelPane.add(specificationsWorkRateLabel, gbc_specificationsWorkRateLabel);
        
        specificationsDateCompletedLabel = new JLabel("00/00/1987");
        specificationsDateCompletedLabel.setHorizontalAlignment(SwingConstants.TRAILING);
        GridBagConstraints gbc_specificationsDateCompletedLabel = new GridBagConstraints();
        gbc_specificationsDateCompletedLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_specificationsDateCompletedLabel.insets = new Insets(0, 0, 5, 5);
        gbc_specificationsDateCompletedLabel.gridx = 5;
        gbc_specificationsDateCompletedLabel.gridy = 3;
        specificationsPersonnelPane.add(specificationsDateCompletedLabel, gbc_specificationsDateCompletedLabel);
        
        specificationsBackcheckLabel = new JLabel("--");
        specificationsBackcheckLabel.setHorizontalAlignment(SwingConstants.CENTER);
        GridBagConstraints gbc_specificationsBackcheckLabel = new GridBagConstraints();
        gbc_specificationsBackcheckLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_specificationsBackcheckLabel.insets = new Insets(0, 0, 5, 5);
        gbc_specificationsBackcheckLabel.gridx = 6;
        gbc_specificationsBackcheckLabel.gridy = 3;
        specificationsPersonnelPane.add(specificationsBackcheckLabel, gbc_specificationsBackcheckLabel);
        
        otherPersonnelPane = new JPanel();
		assignPersonnelPane.add(otherPersonnelPane);
        GridBagLayout gbl_otherPersonnelPane = new GridBagLayout();
        gbl_otherPersonnelPane.columnWidths = new int[]{0, 0, 310, 131, 138, 121, 80, 0, 0};
        gbl_otherPersonnelPane.rowHeights = new int[]{0, 0, 0, 0, 0};
        gbl_otherPersonnelPane.columnWeights = new double[]{0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
        gbl_otherPersonnelPane.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
        otherPersonnelPane.setLayout(gbl_otherPersonnelPane);
        
        otherVerticalStrut = Box.createVerticalStrut(20);
        GridBagConstraints gbc_otherVerticalStrut = new GridBagConstraints();
        gbc_otherVerticalStrut.insets = new Insets(0, 0, 5, 5);
        gbc_otherVerticalStrut.gridx = 1;
        gbc_otherVerticalStrut.gridy = 0;
        otherPersonnelPane.add(otherVerticalStrut, gbc_otherVerticalStrut);
        
        otherHorzizontalStrut = Box.createHorizontalStrut(20);
        GridBagConstraints gbc_otherHorzizontalStrut = new GridBagConstraints();
        gbc_otherHorzizontalStrut.insets = new Insets(0, 0, 5, 5);
        gbc_otherHorzizontalStrut.gridx = 0;
        gbc_otherHorzizontalStrut.gridy = 1;
        otherPersonnelPane.add(otherHorzizontalStrut, gbc_otherHorzizontalStrut);
        
        otherHorzizontalStrut_2 = Box.createHorizontalStrut(20);
        GridBagConstraints gbc_otherHorzizontalStrut_2 = new GridBagConstraints();
        gbc_otherHorzizontalStrut_2.insets = new Insets(0, 0, 5, 0);
        gbc_otherHorzizontalStrut_2.gridx = 7;
        gbc_otherHorzizontalStrut_2.gridy = 2;
        otherPersonnelPane.add(otherHorzizontalStrut_2, gbc_otherHorzizontalStrut_2);
				
		otherTitleLabel = new JLabel("Other");
        otherTitleLabel.setOpaque(true);
        otherTitleLabel.setBackground(new Color(255, 255, 204));
        GridBagConstraints gbc_otherTitleLabel = new GridBagConstraints();
        gbc_otherTitleLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_otherTitleLabel.gridwidth = 6;
        gbc_otherTitleLabel.insets = new Insets(0, 0, 5, 5);
        gbc_otherTitleLabel.gridx = 1;
        gbc_otherTitleLabel.gridy = 1;
        otherPersonnelPane.add(otherTitleLabel, gbc_otherTitleLabel);
        
        otherFillerLabel = new JLabel("  ");
        otherFillerLabel.setOpaque(true);
        otherFillerLabel.setBackground(new Color(153, 153, 153));
        GridBagConstraints gbc_otherFillerLabel = new GridBagConstraints();
        gbc_otherFillerLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_otherFillerLabel.insets = new Insets(0, 0, 5, 5);
        gbc_otherFillerLabel.gridx = 1;
        gbc_otherFillerLabel.gridy = 2;
        otherPersonnelPane.add(otherFillerLabel, gbc_otherFillerLabel);
        
        otherAssigneeTitleLabel = new JLabel("Assignee");
        otherAssigneeTitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        otherAssigneeTitleLabel.setOpaque(true);
        otherAssigneeTitleLabel.setForeground(new Color(255, 255, 255));
        otherAssigneeTitleLabel.setBackground(new Color(153, 153, 153));
        GridBagConstraints gbc_otherAssigneeTitleLabel = new GridBagConstraints();
        gbc_otherAssigneeTitleLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_otherAssigneeTitleLabel.insets = new Insets(0, 0, 5, 5);
        gbc_otherAssigneeTitleLabel.gridx = 2;
        gbc_otherAssigneeTitleLabel.gridy = 2;
        otherPersonnelPane.add(otherAssigneeTitleLabel, gbc_otherAssigneeTitleLabel);
        
        otherHoursAssingedTitleLabel = new JLabel("Hours Assigned");
        otherHoursAssingedTitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        otherHoursAssingedTitleLabel.setOpaque(true);
        otherHoursAssingedTitleLabel.setForeground(new Color(255, 255, 255));
        otherHoursAssingedTitleLabel.setBackground(new Color(153, 153, 153));
        GridBagConstraints gbc_otherHoursAssingedTitleLabel = new GridBagConstraints();
        gbc_otherHoursAssingedTitleLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_otherHoursAssingedTitleLabel.insets = new Insets(0, 0, 5, 5);
        gbc_otherHoursAssingedTitleLabel.gridx = 3;
        gbc_otherHoursAssingedTitleLabel.gridy = 2;
        otherPersonnelPane.add(otherHoursAssingedTitleLabel, gbc_otherHoursAssingedTitleLabel);
        
        otherWorkRateTitleLabel = new JLabel("Work Rate");
        otherWorkRateTitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        otherWorkRateTitleLabel.setOpaque(true);
        otherWorkRateTitleLabel.setForeground(new Color(255, 255, 255));
        otherWorkRateTitleLabel.setBackground(new Color(153, 153, 153));
        GridBagConstraints gbc_otherWorkRateTitleLabel = new GridBagConstraints();
        gbc_otherWorkRateTitleLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_otherWorkRateTitleLabel.insets = new Insets(0, 0, 5, 5);
        gbc_otherWorkRateTitleLabel.gridx = 4;
        gbc_otherWorkRateTitleLabel.gridy = 2;
        otherPersonnelPane.add(otherWorkRateTitleLabel, gbc_otherWorkRateTitleLabel);
        
        otherDateCompleteTitleLabel = new JLabel("Date Completed");
        otherDateCompleteTitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        otherDateCompleteTitleLabel.setOpaque(true);
        otherDateCompleteTitleLabel.setForeground(new Color(255, 255, 255));
        otherDateCompleteTitleLabel.setBackground(new Color(153, 153, 153));
        GridBagConstraints gbc_otherDateCompleteTitleLabel = new GridBagConstraints();
        gbc_otherDateCompleteTitleLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_otherDateCompleteTitleLabel.insets = new Insets(0, 0, 5, 5);
        gbc_otherDateCompleteTitleLabel.gridx = 5;
        gbc_otherDateCompleteTitleLabel.gridy = 2;
        otherPersonnelPane.add(otherDateCompleteTitleLabel, gbc_otherDateCompleteTitleLabel);
        
        otherBackcheckTitleLabel = new JLabel("Backcheck");
        otherBackcheckTitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        otherBackcheckTitleLabel.setOpaque(true);
        otherBackcheckTitleLabel.setForeground(new Color(255, 255, 255));
        otherBackcheckTitleLabel.setBackground(new Color(153, 153, 153));
        GridBagConstraints gbc_otherBackcheckTitleLabel = new GridBagConstraints();
        gbc_otherBackcheckTitleLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_otherBackcheckTitleLabel.insets = new Insets(0, 0, 5, 5);
        gbc_otherBackcheckTitleLabel.gridx = 6;
        gbc_otherBackcheckTitleLabel.gridy = 2;
        otherPersonnelPane.add(otherBackcheckTitleLabel, gbc_otherBackcheckTitleLabel);
        
        otherAssigneeCheckBox = new JCheckBox("");
        GridBagConstraints gbc_otherAssigneeCheckBox = new GridBagConstraints();
        gbc_otherAssigneeCheckBox.insets = new Insets(0, 0, 5, 5);
        gbc_otherAssigneeCheckBox.gridx = 1;
        gbc_otherAssigneeCheckBox.gridy = 3;
        otherPersonnelPane.add(otherAssigneeCheckBox, gbc_otherAssigneeCheckBox);
        
        otherAssigneeName = new JLabel("Assignee Name");
        GridBagConstraints gbc_otherAssigneeName = new GridBagConstraints();
        gbc_otherAssigneeName.fill = GridBagConstraints.HORIZONTAL;
        gbc_otherAssigneeName.insets = new Insets(0, 0, 5, 5);
        gbc_otherAssigneeName.gridx = 2;
        gbc_otherAssigneeName.gridy = 3;
        otherPersonnelPane.add(otherAssigneeName, gbc_otherAssigneeName);
        
        otherHoursAssignedLabel = new JLabel("0.00");
        otherHoursAssignedLabel.setHorizontalAlignment(SwingConstants.TRAILING);
        GridBagConstraints gbc_otherHoursAssignedLabel = new GridBagConstraints();
        gbc_otherHoursAssignedLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_otherHoursAssignedLabel.insets = new Insets(0, 0, 5, 5);
        gbc_otherHoursAssignedLabel.gridx = 3;
        gbc_otherHoursAssignedLabel.gridy = 3;
        otherPersonnelPane.add(otherHoursAssignedLabel, gbc_otherHoursAssignedLabel);
        
        otherWorkRateLabel = new JLabel("Work Rate");
        otherWorkRateLabel.setHorizontalAlignment(SwingConstants.TRAILING);
        GridBagConstraints gbc_otherWorkRateLabel = new GridBagConstraints();
        gbc_otherWorkRateLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_otherWorkRateLabel.insets = new Insets(0, 0, 5, 5);
        gbc_otherWorkRateLabel.gridx = 4;
        gbc_otherWorkRateLabel.gridy = 3;
        otherPersonnelPane.add(otherWorkRateLabel, gbc_otherWorkRateLabel);
        
        otherDateCompletedLabel = new JLabel("00/00/1987");
        otherDateCompletedLabel.setHorizontalAlignment(SwingConstants.TRAILING);
        GridBagConstraints gbc_otherDateCompletedLabel = new GridBagConstraints();
        gbc_otherDateCompletedLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_otherDateCompletedLabel.insets = new Insets(0, 0, 5, 5);
        gbc_otherDateCompletedLabel.gridx = 5;
        gbc_otherDateCompletedLabel.gridy = 3;
        otherPersonnelPane.add(otherDateCompletedLabel, gbc_otherDateCompletedLabel);
        
        otherBackcheckLabel = new JLabel("--");
        otherBackcheckLabel.setHorizontalAlignment(SwingConstants.CENTER);
        GridBagConstraints gbc_otherBackcheckLabel = new GridBagConstraints();
        gbc_otherBackcheckLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_otherBackcheckLabel.insets = new Insets(0, 0, 5, 5);
        gbc_otherBackcheckLabel.gridx = 6;
        gbc_otherBackcheckLabel.gridy = 3;
        otherPersonnelPane.add(otherBackcheckLabel, gbc_otherBackcheckLabel);
        
        historyPane = new JPanel();
        newWorkRequestPane.addTab("History", null, historyPane, null);
        historyPane.setLayout(new BoxLayout(historyPane, BoxLayout.Y_AXIS));
        
        historyScrollPane = new JScrollPane();
        historyPane.add(historyScrollPane);
        
        historyObjectHolderPane = new JPanel();
        historyScrollPane.setViewportView(historyObjectHolderPane);
        historyObjectHolderPane.setLayout(new BoxLayout(historyObjectHolderPane, BoxLayout.Y_AXIS));
        
        historyObjectTitlePane = new JPanel();
        historyObjectHolderPane.add(historyObjectTitlePane);
        GridBagLayout gbl_historyObjectTitlePane = new GridBagLayout();
        gbl_historyObjectTitlePane.columnWidths = new int[]{0, 0, 288, 270, 147, 0, 0};
        gbl_historyObjectTitlePane.rowHeights = new int[] {0, 0, 0};
        gbl_historyObjectTitlePane.columnWeights = new double[]{0.0, 0.0, 1.0, 1.0, 0.0, 0.0, Double.MIN_VALUE};
        gbl_historyObjectTitlePane.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
        historyObjectTitlePane.setLayout(gbl_historyObjectTitlePane);
        
        historyObjectTitleVerticalStrut = Box.createVerticalStrut(20);
        GridBagConstraints gbc_historyObjectTitleVerticalStrut = new GridBagConstraints();
        gbc_historyObjectTitleVerticalStrut.insets = new Insets(0, 0, 5, 5);
        gbc_historyObjectTitleVerticalStrut.gridx = 0;
        gbc_historyObjectTitleVerticalStrut.gridy = 0;
        historyObjectTitlePane.add(historyObjectTitleVerticalStrut, gbc_historyObjectTitleVerticalStrut);
        
        historyObjectTitleHorizontalStrut = Box.createHorizontalStrut(20);
        GridBagConstraints gbc_historyObjectTitleHorizontalStrut = new GridBagConstraints();
        gbc_historyObjectTitleHorizontalStrut.insets = new Insets(0, 0, 0, 5);
        gbc_historyObjectTitleHorizontalStrut.gridx = 0;
        gbc_historyObjectTitleHorizontalStrut.gridy = 1;
        historyObjectTitlePane.add(historyObjectTitleHorizontalStrut, gbc_historyObjectTitleHorizontalStrut);
        
        modDateTimeTitleLabel = new JLabel("Date Modified");
        modDateTimeTitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        modDateTimeTitleLabel.setOpaque(true);
        modDateTimeTitleLabel.setForeground(new Color(255, 255, 255));
        modDateTimeTitleLabel.setBackground(new Color(153, 153, 153));
        GridBagConstraints gbc_modDateTimeTitleLabel = new GridBagConstraints();
        gbc_modDateTimeTitleLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_modDateTimeTitleLabel.insets = new Insets(0, 0, 0, 5);
        gbc_modDateTimeTitleLabel.gridx = 1;
        gbc_modDateTimeTitleLabel.gridy = 1;
        historyObjectTitlePane.add(modDateTimeTitleLabel, gbc_modDateTimeTitleLabel);
        
        oldValueTitleLabel = new JLabel("Old Value");
        oldValueTitleLabel.setOpaque(true);
        oldValueTitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        oldValueTitleLabel.setForeground(Color.WHITE);
        oldValueTitleLabel.setBackground(new Color(153, 153, 153));
        GridBagConstraints gbc_oldValueTitleLabel = new GridBagConstraints();
        gbc_oldValueTitleLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_oldValueTitleLabel.insets = new Insets(0, 0, 0, 5);
        gbc_oldValueTitleLabel.gridx = 2;
        gbc_oldValueTitleLabel.gridy = 1;
        historyObjectTitlePane.add(oldValueTitleLabel, gbc_oldValueTitleLabel);
        
        newValueTitleLabel = new JLabel("New Values");
        newValueTitleLabel.setOpaque(true);
        newValueTitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        newValueTitleLabel.setForeground(Color.WHITE);
        newValueTitleLabel.setBackground(new Color(153, 153, 153));
        GridBagConstraints gbc_newValueTitleLabel = new GridBagConstraints();
        gbc_newValueTitleLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_newValueTitleLabel.insets = new Insets(0, 0, 0, 5);
        gbc_newValueTitleLabel.gridx = 3;
        gbc_newValueTitleLabel.gridy = 1;
        historyObjectTitlePane.add(newValueTitleLabel, gbc_newValueTitleLabel);
        
        modByTitleLabel = new JLabel("Modified By");
        modByTitleLabel.setOpaque(true);
        modByTitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        modByTitleLabel.setForeground(Color.WHITE);
        modByTitleLabel.setBackground(new Color(153, 153, 153));
        GridBagConstraints gbc_modByTitleLabel = new GridBagConstraints();
        gbc_modByTitleLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_modByTitleLabel.insets = new Insets(0, 0, 0, 5);
        gbc_modByTitleLabel.gridx = 4;
        gbc_modByTitleLabel.gridy = 1;
        historyObjectTitlePane.add(modByTitleLabel, gbc_modByTitleLabel);
        
        historyObjectTitleVerticalStrut_2 = Box.createHorizontalStrut(20);
        GridBagConstraints gbc_historyObjectTitleVerticalStrut_2 = new GridBagConstraints();
        gbc_historyObjectTitleVerticalStrut_2.gridx = 5;
        gbc_historyObjectTitleVerticalStrut_2.gridy = 1;
        historyObjectTitlePane.add(historyObjectTitleVerticalStrut_2, gbc_historyObjectTitleVerticalStrut_2);
        
        object1_historyObjectPane = new JPanel();
        historyObjectHolderPane.add(object1_historyObjectPane);
        GridBagLayout gbl_historyObjectPane = new GridBagLayout();
        gbl_historyObjectPane.columnWidths = new int[]{0, 0, 288, 270, 147, 0, 0};
        gbl_historyObjectPane.rowHeights = new int[] {0, 0, 0, 0};
        gbl_historyObjectPane.columnWeights = new double[]{0.0, 0.0, 1.0, 1.0, 0.0, 0.0, Double.MIN_VALUE};
        gbl_historyObjectPane.rowWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
        object1_historyObjectPane.setLayout(gbl_historyObjectPane);
        
        object1_horizontalStrut = Box.createHorizontalStrut(20);
        GridBagConstraints gbc_object1_horizontalStrut = new GridBagConstraints();
        gbc_object1_horizontalStrut.insets = new Insets(0, 0, 5, 5);
        gbc_object1_horizontalStrut.gridx = 0;
        gbc_object1_horizontalStrut.gridy = 0;
        object1_historyObjectPane.add(object1_horizontalStrut, gbc_object1_horizontalStrut);
                
        object1_fillerLabel = new JLabel("    ");
        object1_fillerLabel.setOpaque(true);
        object1_fillerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        object1_fillerLabel.setForeground(Color.WHITE);
        object1_fillerLabel.setBackground(new Color(204, 204, 204));
        GridBagConstraints gbc_object1_fillerLabel = new GridBagConstraints();
        gbc_object1_fillerLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_object1_fillerLabel.gridwidth = 4;
        gbc_object1_fillerLabel.insets = new Insets(0, 0, 5, 5);
        gbc_object1_fillerLabel.gridx = 1;
        gbc_object1_fillerLabel.gridy = 0;
        object1_historyObjectPane.add(object1_fillerLabel, gbc_object1_fillerLabel);
        
        object1_horizontalStrut_2 = Box.createHorizontalStrut(20);
        GridBagConstraints gbc_object1_horizontalStrut_2 = new GridBagConstraints();
        gbc_object1_horizontalStrut_2.insets = new Insets(0, 0, 5, 0);
        gbc_object1_horizontalStrut_2.gridx = 5;
        gbc_object1_horizontalStrut_2.gridy = 0;
        object1_historyObjectPane.add(object1_horizontalStrut_2, gbc_object1_horizontalStrut_2);
        
        object1_verticalStrut = Box.createVerticalStrut(40);
        GridBagConstraints gbc_object1_verticalStrut = new GridBagConstraints();
        gbc_object1_verticalStrut.fill = GridBagConstraints.VERTICAL;
        gbc_object1_verticalStrut.insets = new Insets(0, 0, 5, 5);
        gbc_object1_verticalStrut.gridx = 0;
        gbc_object1_verticalStrut.gridy = 1;
        object1_historyObjectPane.add(object1_verticalStrut, gbc_object1_verticalStrut);
        
        object1_dateModifiedLabel = new JLabel("0/0/1987");
        GridBagConstraints gbc_object1_dateModifiedLabel = new GridBagConstraints();
        gbc_object1_dateModifiedLabel.insets = new Insets(0, 0, 5, 5);
        gbc_object1_dateModifiedLabel.gridx = 1;
        gbc_object1_dateModifiedLabel.gridy = 1;
        object1_historyObjectPane.add(object1_dateModifiedLabel, gbc_object1_dateModifiedLabel);
        
        object1_OldValueScrollPane = new JScrollPane();
        GridBagConstraints gbc_object1_OldValueScrollPane = new GridBagConstraints();
        gbc_object1_OldValueScrollPane.fill = GridBagConstraints.BOTH;
        gbc_object1_OldValueScrollPane.gridheight = 2;
        gbc_object1_OldValueScrollPane.insets = new Insets(0, 0, 0, 5);
        gbc_object1_OldValueScrollPane.gridx = 2;
        gbc_object1_OldValueScrollPane.gridy = 1;
        object1_historyObjectPane.add(object1_OldValueScrollPane, gbc_object1_OldValueScrollPane);
        
        object1_OldValueTextArea = new JTextArea();
        object1_OldValueScrollPane.setViewportView(object1_OldValueTextArea);
        object1_OldValueTextArea.setLineWrap(true);
        object1_OldValueTextArea.setEnabled(true);
        object1_OldValueTextArea.setEditable(true);
        object1_OldValueTextArea.setText("");
        
        object1_NewValueScrollPane = new JScrollPane();
        GridBagConstraints gbc_object1_NewValueScrollPane = new GridBagConstraints();
        gbc_object1_NewValueScrollPane.fill = GridBagConstraints.BOTH;
        gbc_object1_NewValueScrollPane.gridheight = 2;
        gbc_object1_NewValueScrollPane.insets = new Insets(0, 0, 0, 5);
        gbc_object1_NewValueScrollPane.gridx = 3;
        gbc_object1_NewValueScrollPane.gridy = 1;
        object1_historyObjectPane.add(object1_NewValueScrollPane, gbc_object1_NewValueScrollPane);
        
        object1_NewValueTextArea_1 = new JTextArea();
        object1_NewValueScrollPane.setViewportView(object1_NewValueTextArea_1);
        object1_NewValueTextArea_1.setText("");
        object1_NewValueTextArea_1.setLineWrap(true);
        object1_NewValueTextArea_1.setEnabled(true);
        object1_NewValueTextArea_1.setEditable(true);
        
        object1_ModifiedByLabel = new JLabel("System");
        GridBagConstraints gbc_object1_ModifiedByLabel = new GridBagConstraints();
        gbc_object1_ModifiedByLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_object1_ModifiedByLabel.gridheight = 2;
        gbc_object1_ModifiedByLabel.insets = new Insets(0, 0, 0, 5);
        gbc_object1_ModifiedByLabel.gridx = 4;
        gbc_object1_ModifiedByLabel.gridy = 1;
        object1_historyObjectPane.add(object1_ModifiedByLabel, gbc_object1_ModifiedByLabel);
        
        object1_verticalStrut_2 = Box.createVerticalStrut(40);
        GridBagConstraints gbc_object1_verticalStrut_2 = new GridBagConstraints();
        gbc_object1_verticalStrut_2.insets = new Insets(0, 0, 0, 5);
        gbc_object1_verticalStrut_2.gridx = 0;
        gbc_object1_verticalStrut_2.gridy = 2;
        object1_historyObjectPane.add(object1_verticalStrut_2, gbc_object1_verticalStrut_2);
        
        object1_timeModifiedLabel = new JLabel("00:00:01 AM");
        GridBagConstraints gbc_object1_timeModifiedLabel = new GridBagConstraints();
        gbc_object1_timeModifiedLabel.insets = new Insets(0, 0, 0, 5);
        gbc_object1_timeModifiedLabel.gridx = 1;
        gbc_object1_timeModifiedLabel.gridy = 2;
        object1_historyObjectPane.add(object1_timeModifiedLabel, gbc_object1_timeModifiedLabel);
        
		object2_historyObjectPane = new JPanel();
        historyObjectHolderPane.add(object2_historyObjectPane);
        GridBagLayout gbl_object2_historyObjectPane = new GridBagLayout();
        gbl_object2_historyObjectPane.columnWidths = new int[]{0, 0, 288, 270, 147, 0, 0};
        gbl_object2_historyObjectPane.rowHeights = new int[] {0, 0, 0, 0};
        gbl_object2_historyObjectPane.columnWeights = new double[]{0.0, 0.0, 1.0, 1.0, 0.0, 0.0, Double.MIN_VALUE};
        gbl_object2_historyObjectPane.rowWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
        object2_historyObjectPane.setLayout(gbl_object2_historyObjectPane);
        
        object2_horizontalStrut = Box.createHorizontalStrut(20);
        GridBagConstraints gbc_object2_horizontalStrut = new GridBagConstraints();
        gbc_object2_horizontalStrut.insets = new Insets(0, 0, 5, 5);
        gbc_object2_horizontalStrut.gridx = 0;
        gbc_object2_horizontalStrut.gridy = 0;
        object2_historyObjectPane.add(object2_horizontalStrut, gbc_object2_horizontalStrut);
                
        object2_fillerLabel = new JLabel("    ");
        object2_fillerLabel.setOpaque(true);
        object2_fillerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        object2_fillerLabel.setForeground(Color.WHITE);
        object2_fillerLabel.setBackground(new Color(204, 204, 204));
        GridBagConstraints gbc_object2_fillerLabel = new GridBagConstraints();
        gbc_object2_fillerLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_object2_fillerLabel.gridwidth = 4;
        gbc_object2_fillerLabel.insets = new Insets(0, 0, 5, 5);
        gbc_object2_fillerLabel.gridx = 1;
        gbc_object2_fillerLabel.gridy = 0;
        object2_historyObjectPane.add(object2_fillerLabel, gbc_object2_fillerLabel);
        
        object2_horizontalStrut_2 = Box.createHorizontalStrut(20);
        GridBagConstraints gbc_object2_horizontalStrut_2 = new GridBagConstraints();
        gbc_object2_horizontalStrut_2.insets = new Insets(0, 0, 5, 0);
        gbc_object2_horizontalStrut_2.gridx = 5;
        gbc_object2_horizontalStrut_2.gridy = 0;
        object2_historyObjectPane.add(object2_horizontalStrut_2, gbc_object2_horizontalStrut_2);
        
        object2_verticalStrut = Box.createVerticalStrut(40);
        GridBagConstraints gbc_object2_verticalStrut = new GridBagConstraints();
        gbc_object2_verticalStrut.fill = GridBagConstraints.VERTICAL;
        gbc_object2_verticalStrut.insets = new Insets(0, 0, 5, 5);
        gbc_object2_verticalStrut.gridx = 0;
        gbc_object2_verticalStrut.gridy = 1;
        object2_historyObjectPane.add(object2_verticalStrut, gbc_object2_verticalStrut);
        
        object2_dateModifiedLabel = new JLabel("0/0/1987");
        GridBagConstraints gbc_object2_dateModifiedLabel = new GridBagConstraints();
        gbc_object2_dateModifiedLabel.insets = new Insets(0, 0, 5, 5);
        gbc_object2_dateModifiedLabel.gridx = 1;
        gbc_object2_dateModifiedLabel.gridy = 1;
        object2_historyObjectPane.add(object2_dateModifiedLabel, gbc_object2_dateModifiedLabel);
        
        object2_OldValueScrollPane = new JScrollPane();
        GridBagConstraints gbc_object2_OldValueScrollPane = new GridBagConstraints();
        gbc_object2_OldValueScrollPane.fill = GridBagConstraints.BOTH;
        gbc_object2_OldValueScrollPane.gridheight = 2;
        gbc_object2_OldValueScrollPane.insets = new Insets(0, 0, 0, 5);
        gbc_object2_OldValueScrollPane.gridx = 2;
        gbc_object2_OldValueScrollPane.gridy = 1;
        object2_historyObjectPane.add(object2_OldValueScrollPane, gbc_object2_OldValueScrollPane);
        
        object2_OldValueTextArea = new JTextArea();
        object2_OldValueScrollPane.setViewportView(object2_OldValueTextArea);
        object2_OldValueTextArea.setLineWrap(true);
        object2_OldValueTextArea.setEnabled(true);
        object2_OldValueTextArea.setEditable(true);
        object2_OldValueTextArea.setText("");
        
        object2_NewValueScrollPane = new JScrollPane();
        GridBagConstraints gbc_object2_NewValueScrollPane = new GridBagConstraints();
        gbc_object2_NewValueScrollPane.fill = GridBagConstraints.BOTH;
        gbc_object2_NewValueScrollPane.gridheight = 2;
        gbc_object2_NewValueScrollPane.insets = new Insets(0, 0, 0, 5);
        gbc_object2_NewValueScrollPane.gridx = 3;
        gbc_object2_NewValueScrollPane.gridy = 1;
        object2_historyObjectPane.add(object2_NewValueScrollPane, gbc_object2_NewValueScrollPane);
        
        object2_NewValueTextArea_1 = new JTextArea();
        object2_NewValueScrollPane.setViewportView(object2_NewValueTextArea_1);
        object2_NewValueTextArea_1.setText("");
        object2_NewValueTextArea_1.setLineWrap(true);
        object2_NewValueTextArea_1.setEnabled(true);
        object2_NewValueTextArea_1.setEditable(true);
        
        object2_ModifiedByLabel = new JLabel("System");
        GridBagConstraints gbc_object2_ModifiedByLabel = new GridBagConstraints();
        gbc_object2_ModifiedByLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_object2_ModifiedByLabel.gridheight = 2;
        gbc_object2_ModifiedByLabel.insets = new Insets(0, 0, 0, 5);
        gbc_object2_ModifiedByLabel.gridx = 4;
        gbc_object2_ModifiedByLabel.gridy = 1;
        object2_historyObjectPane.add(object2_ModifiedByLabel, gbc_object2_ModifiedByLabel);
        
        object2_verticalStrut_2 = Box.createVerticalStrut(40);
        GridBagConstraints gbc_object2_verticalStrut_2 = new GridBagConstraints();
        gbc_object2_verticalStrut_2.insets = new Insets(0, 0, 0, 5);
        gbc_object2_verticalStrut_2.gridx = 0;
        gbc_object2_verticalStrut_2.gridy = 2;
        object2_historyObjectPane.add(object2_verticalStrut_2, gbc_object2_verticalStrut_2);
        
        object2_timeModifiedLabel = new JLabel("00:00:01 AM");
        GridBagConstraints gbc_object2_timeModifiedLabel = new GridBagConstraints();
        gbc_object2_timeModifiedLabel.insets = new Insets(0, 0, 0, 5);
        gbc_object2_timeModifiedLabel.gridx = 1;
        gbc_object2_timeModifiedLabel.gridy = 2;
        object2_historyObjectPane.add(object2_timeModifiedLabel, gbc_object2_timeModifiedLabel);
		
		object3_historyObjectPane = new JPanel();
        historyObjectHolderPane.add(object3_historyObjectPane);
        GridBagLayout gbl_object3_historyObjectPane = new GridBagLayout();
        gbl_object3_historyObjectPane.columnWidths = new int[]{0, 0, 288, 270, 147, 0, 0};
        gbl_object3_historyObjectPane.rowHeights = new int[] {0, 0, 0, 0};
        gbl_object3_historyObjectPane.columnWeights = new double[]{0.0, 0.0, 1.0, 1.0, 0.0, 0.0, Double.MIN_VALUE};
        gbl_object3_historyObjectPane.rowWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
        object3_historyObjectPane.setLayout(gbl_object3_historyObjectPane);
        
        object3_horizontalStrut = Box.createHorizontalStrut(20);
        GridBagConstraints gbc_object3_horizontalStrut = new GridBagConstraints();
        gbc_object3_horizontalStrut.insets = new Insets(0, 0, 5, 5);
        gbc_object3_horizontalStrut.gridx = 0;
        gbc_object3_horizontalStrut.gridy = 0;
        object3_historyObjectPane.add(object3_horizontalStrut, gbc_object3_horizontalStrut);
                
        object3_fillerLabel = new JLabel("    ");
        object3_fillerLabel.setOpaque(true);
        object3_fillerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        object3_fillerLabel.setForeground(Color.WHITE);
        object3_fillerLabel.setBackground(new Color(204, 204, 204));
        GridBagConstraints gbc_object3_fillerLabel = new GridBagConstraints();
        gbc_object3_fillerLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_object3_fillerLabel.gridwidth = 4;
        gbc_object3_fillerLabel.insets = new Insets(0, 0, 5, 5);
        gbc_object3_fillerLabel.gridx = 1;
        gbc_object3_fillerLabel.gridy = 0;
        object3_historyObjectPane.add(object3_fillerLabel, gbc_object3_fillerLabel);
        
        object3_horizontalStrut_2 = Box.createHorizontalStrut(20);
        GridBagConstraints gbc_object3_horizontalStrut_2 = new GridBagConstraints();
        gbc_object3_horizontalStrut_2.insets = new Insets(0, 0, 5, 0);
        gbc_object3_horizontalStrut_2.gridx = 5;
        gbc_object3_horizontalStrut_2.gridy = 0;
        object3_historyObjectPane.add(object3_horizontalStrut_2, gbc_object3_horizontalStrut_2);
        
        object3_verticalStrut = Box.createVerticalStrut(40);
        GridBagConstraints gbc_object3_verticalStrut = new GridBagConstraints();
        gbc_object3_verticalStrut.fill = GridBagConstraints.VERTICAL;
        gbc_object3_verticalStrut.insets = new Insets(0, 0, 5, 5);
        gbc_object3_verticalStrut.gridx = 0;
        gbc_object3_verticalStrut.gridy = 1;
        object3_historyObjectPane.add(object3_verticalStrut, gbc_object3_verticalStrut);
        
        object3_dateModifiedLabel = new JLabel("0/0/1987");
        GridBagConstraints gbc_object3_dateModifiedLabel = new GridBagConstraints();
        gbc_object3_dateModifiedLabel.insets = new Insets(0, 0, 5, 5);
        gbc_object3_dateModifiedLabel.gridx = 1;
        gbc_object3_dateModifiedLabel.gridy = 1;
        object3_historyObjectPane.add(object3_dateModifiedLabel, gbc_object3_dateModifiedLabel);
        
        object3_OldValueScrollPane = new JScrollPane();
        GridBagConstraints gbc_object3_OldValueScrollPane = new GridBagConstraints();
        gbc_object3_OldValueScrollPane.fill = GridBagConstraints.BOTH;
        gbc_object3_OldValueScrollPane.gridheight = 2;
        gbc_object3_OldValueScrollPane.insets = new Insets(0, 0, 0, 5);
        gbc_object3_OldValueScrollPane.gridx = 2;
        gbc_object3_OldValueScrollPane.gridy = 1;
        object3_historyObjectPane.add(object3_OldValueScrollPane, gbc_object3_OldValueScrollPane);
        
        object3_OldValueTextArea = new JTextArea();
        object3_OldValueScrollPane.setViewportView(object3_OldValueTextArea);
        object3_OldValueTextArea.setLineWrap(true);
        object3_OldValueTextArea.setEnabled(true);
        object3_OldValueTextArea.setEditable(true);
        object3_OldValueTextArea.setText("");
        
        object3_NewValueScrollPane = new JScrollPane();
        GridBagConstraints gbc_object3_NewValueScrollPane = new GridBagConstraints();
        gbc_object3_NewValueScrollPane.fill = GridBagConstraints.BOTH;
        gbc_object3_NewValueScrollPane.gridheight = 2;
        gbc_object3_NewValueScrollPane.insets = new Insets(0, 0, 0, 5);
        gbc_object3_NewValueScrollPane.gridx = 3;
        gbc_object3_NewValueScrollPane.gridy = 1;
        object3_historyObjectPane.add(object3_NewValueScrollPane, gbc_object3_NewValueScrollPane);
        
        object3_NewValueTextArea_1 = new JTextArea();
        object3_NewValueScrollPane.setViewportView(object3_NewValueTextArea_1);
        object3_NewValueTextArea_1.setText("");
        object3_NewValueTextArea_1.setLineWrap(true);
        object3_NewValueTextArea_1.setEnabled(true);
        object3_NewValueTextArea_1.setEditable(true);
        
        object3_ModifiedByLabel = new JLabel("System");
        GridBagConstraints gbc_object3_ModifiedByLabel = new GridBagConstraints();
        gbc_object3_ModifiedByLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_object3_ModifiedByLabel.gridheight = 2;
        gbc_object3_ModifiedByLabel.insets = new Insets(0, 0, 0, 5);
        gbc_object3_ModifiedByLabel.gridx = 4;
        gbc_object3_ModifiedByLabel.gridy = 1;
        object3_historyObjectPane.add(object3_ModifiedByLabel, gbc_object3_ModifiedByLabel);
        
        object3_verticalStrut_2 = Box.createVerticalStrut(40);
        GridBagConstraints gbc_object3_verticalStrut_2 = new GridBagConstraints();
        gbc_object3_verticalStrut_2.insets = new Insets(0, 0, 0, 5);
        gbc_object3_verticalStrut_2.gridx = 0;
        gbc_object3_verticalStrut_2.gridy = 2;
        object3_historyObjectPane.add(object3_verticalStrut_2, gbc_object3_verticalStrut_2);
        
        object3_timeModifiedLabel = new JLabel("00:00:01 AM");
        GridBagConstraints gbc_object3_timeModifiedLabel = new GridBagConstraints();
        gbc_object3_timeModifiedLabel.insets = new Insets(0, 0, 0, 5);
        gbc_object3_timeModifiedLabel.gridx = 1;
        gbc_object3_timeModifiedLabel.gridy = 2;
        object3_historyObjectPane.add(object3_timeModifiedLabel, gbc_object3_timeModifiedLabel);
        
		object4_historyObjectPane = new JPanel();
        historyObjectHolderPane.add(object4_historyObjectPane);
        GridBagLayout gbl_object4_historyObjectPane = new GridBagLayout();
        gbl_object4_historyObjectPane.columnWidths = new int[]{0, 0, 288, 270, 147, 0, 0};
        gbl_object4_historyObjectPane.rowHeights = new int[] {0, 0, 0, 0};
        gbl_object4_historyObjectPane.columnWeights = new double[]{0.0, 0.0, 1.0, 1.0, 0.0, 0.0, Double.MIN_VALUE};
        gbl_object4_historyObjectPane.rowWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
        object4_historyObjectPane.setLayout(gbl_object4_historyObjectPane);
        
        object4_horizontalStrut = Box.createHorizontalStrut(20);
        GridBagConstraints gbc_object4_horizontalStrut = new GridBagConstraints();
        gbc_object4_horizontalStrut.insets = new Insets(0, 0, 5, 5);
        gbc_object4_horizontalStrut.gridx = 0;
        gbc_object4_horizontalStrut.gridy = 0;
        object4_historyObjectPane.add(object4_horizontalStrut, gbc_object4_horizontalStrut);
                
        object4_fillerLabel = new JLabel("    ");
        object4_fillerLabel.setOpaque(true);
        object4_fillerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        object4_fillerLabel.setForeground(Color.WHITE);
        object4_fillerLabel.setBackground(new Color(204, 204, 204));
        GridBagConstraints gbc_object4_fillerLabel = new GridBagConstraints();
        gbc_object4_fillerLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_object4_fillerLabel.gridwidth = 4;
        gbc_object4_fillerLabel.insets = new Insets(0, 0, 5, 5);
        gbc_object4_fillerLabel.gridx = 1;
        gbc_object4_fillerLabel.gridy = 0;
        object4_historyObjectPane.add(object4_fillerLabel, gbc_object4_fillerLabel);
        
        object4_horizontalStrut_2 = Box.createHorizontalStrut(20);
        GridBagConstraints gbc_object4_horizontalStrut_2 = new GridBagConstraints();
        gbc_object4_horizontalStrut_2.insets = new Insets(0, 0, 5, 0);
        gbc_object4_horizontalStrut_2.gridx = 5;
        gbc_object4_horizontalStrut_2.gridy = 0;
        object4_historyObjectPane.add(object4_horizontalStrut_2, gbc_object4_horizontalStrut_2);
        
        object4_verticalStrut = Box.createVerticalStrut(40);
        GridBagConstraints gbc_object4_verticalStrut = new GridBagConstraints();
        gbc_object4_verticalStrut.fill = GridBagConstraints.VERTICAL;
        gbc_object4_verticalStrut.insets = new Insets(0, 0, 5, 5);
        gbc_object4_verticalStrut.gridx = 0;
        gbc_object4_verticalStrut.gridy = 1;
        object4_historyObjectPane.add(object4_verticalStrut, gbc_object4_verticalStrut);
        
        object4_dateModifiedLabel = new JLabel("0/0/1987");
        GridBagConstraints gbc_object4_dateModifiedLabel = new GridBagConstraints();
        gbc_object4_dateModifiedLabel.insets = new Insets(0, 0, 5, 5);
        gbc_object4_dateModifiedLabel.gridx = 1;
        gbc_object4_dateModifiedLabel.gridy = 1;
        object4_historyObjectPane.add(object4_dateModifiedLabel, gbc_object4_dateModifiedLabel);
        
        object4_OldValueScrollPane = new JScrollPane();
        GridBagConstraints gbc_object4_OldValueScrollPane = new GridBagConstraints();
        gbc_object4_OldValueScrollPane.fill = GridBagConstraints.BOTH;
        gbc_object4_OldValueScrollPane.gridheight = 2;
        gbc_object4_OldValueScrollPane.insets = new Insets(0, 0, 0, 5);
        gbc_object4_OldValueScrollPane.gridx = 2;
        gbc_object4_OldValueScrollPane.gridy = 1;
        object4_historyObjectPane.add(object4_OldValueScrollPane, gbc_object4_OldValueScrollPane);
        
        object4_OldValueTextArea = new JTextArea();
        object4_OldValueScrollPane.setViewportView(object4_OldValueTextArea);
        object4_OldValueTextArea.setLineWrap(true);
        object4_OldValueTextArea.setEnabled(true);
        object4_OldValueTextArea.setEditable(true);
        object4_OldValueTextArea.setText("");
        
        object4_NewValueScrollPane = new JScrollPane();
        GridBagConstraints gbc_object4_NewValueScrollPane = new GridBagConstraints();
        gbc_object4_NewValueScrollPane.fill = GridBagConstraints.BOTH;
        gbc_object4_NewValueScrollPane.gridheight = 2;
        gbc_object4_NewValueScrollPane.insets = new Insets(0, 0, 0, 5);
        gbc_object4_NewValueScrollPane.gridx = 3;
        gbc_object4_NewValueScrollPane.gridy = 1;
        object4_historyObjectPane.add(object4_NewValueScrollPane, gbc_object4_NewValueScrollPane);
        
        object4_NewValueTextArea_1 = new JTextArea();
        object4_NewValueScrollPane.setViewportView(object4_NewValueTextArea_1);
        object4_NewValueTextArea_1.setText("");
        object4_NewValueTextArea_1.setLineWrap(true);
        object4_NewValueTextArea_1.setEnabled(true);
        object4_NewValueTextArea_1.setEditable(true);
        
        object4_ModifiedByLabel = new JLabel("System");
        GridBagConstraints gbc_object4_ModifiedByLabel = new GridBagConstraints();
        gbc_object4_ModifiedByLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_object4_ModifiedByLabel.gridheight = 2;
        gbc_object4_ModifiedByLabel.insets = new Insets(0, 0, 0, 5);
        gbc_object4_ModifiedByLabel.gridx = 4;
        gbc_object4_ModifiedByLabel.gridy = 1;
        object4_historyObjectPane.add(object4_ModifiedByLabel, gbc_object4_ModifiedByLabel);
        
        object4_verticalStrut_2 = Box.createVerticalStrut(40);
        GridBagConstraints gbc_object4_verticalStrut_2 = new GridBagConstraints();
        gbc_object4_verticalStrut_2.insets = new Insets(0, 0, 0, 5);
        gbc_object4_verticalStrut_2.gridx = 0;
        gbc_object4_verticalStrut_2.gridy = 2;
        object4_historyObjectPane.add(object4_verticalStrut_2, gbc_object4_verticalStrut_2);
        
        object4_timeModifiedLabel = new JLabel("00:00:01 AM");
        GridBagConstraints gbc_object4_timeModifiedLabel = new GridBagConstraints();
        gbc_object4_timeModifiedLabel.insets = new Insets(0, 0, 0, 5);
        gbc_object4_timeModifiedLabel.gridx = 1;
        gbc_object4_timeModifiedLabel.gridy = 2;
        object4_historyObjectPane.add(object4_timeModifiedLabel, gbc_object4_timeModifiedLabel);
        
		object5_historyObjectPane = new JPanel();
        historyObjectHolderPane.add(object5_historyObjectPane);
        GridBagLayout gbl_object5_historyObjectPane = new GridBagLayout();
        gbl_object5_historyObjectPane.columnWidths = new int[]{0, 0, 288, 270, 147, 0, 0};
        gbl_object5_historyObjectPane.rowHeights = new int[] {0, 0, 0, 0};
        gbl_object5_historyObjectPane.columnWeights = new double[]{0.0, 0.0, 1.0, 1.0, 0.0, 0.0, Double.MIN_VALUE};
        gbl_object5_historyObjectPane.rowWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
        object5_historyObjectPane.setLayout(gbl_object5_historyObjectPane);
        
        object5_horizontalStrut = Box.createHorizontalStrut(20);
        GridBagConstraints gbc_object5_horizontalStrut = new GridBagConstraints();
        gbc_object5_horizontalStrut.insets = new Insets(0, 0, 5, 5);
        gbc_object5_horizontalStrut.gridx = 0;
        gbc_object5_horizontalStrut.gridy = 0;
        object5_historyObjectPane.add(object5_horizontalStrut, gbc_object5_horizontalStrut);
                
        object5_fillerLabel = new JLabel("    ");
        object5_fillerLabel.setOpaque(true);
        object5_fillerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        object5_fillerLabel.setForeground(Color.WHITE);
        object5_fillerLabel.setBackground(new Color(204, 204, 204));
        GridBagConstraints gbc_object5_fillerLabel = new GridBagConstraints();
        gbc_object5_fillerLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_object5_fillerLabel.gridwidth = 4;
        gbc_object5_fillerLabel.insets = new Insets(0, 0, 5, 5);
        gbc_object5_fillerLabel.gridx = 1;
        gbc_object5_fillerLabel.gridy = 0;
        object5_historyObjectPane.add(object5_fillerLabel, gbc_object5_fillerLabel);
        
        object5_horizontalStrut_2 = Box.createHorizontalStrut(20);
        GridBagConstraints gbc_object5_horizontalStrut_2 = new GridBagConstraints();
        gbc_object5_horizontalStrut_2.insets = new Insets(0, 0, 5, 0);
        gbc_object5_horizontalStrut_2.gridx = 5;
        gbc_object5_horizontalStrut_2.gridy = 0;
        object5_historyObjectPane.add(object5_horizontalStrut_2, gbc_object5_horizontalStrut_2);
        
        object5_verticalStrut = Box.createVerticalStrut(40);
        GridBagConstraints gbc_object5_verticalStrut = new GridBagConstraints();
        gbc_object5_verticalStrut.fill = GridBagConstraints.VERTICAL;
        gbc_object5_verticalStrut.insets = new Insets(0, 0, 5, 5);
        gbc_object5_verticalStrut.gridx = 0;
        gbc_object5_verticalStrut.gridy = 1;
        object5_historyObjectPane.add(object5_verticalStrut, gbc_object5_verticalStrut);
        
        object5_dateModifiedLabel = new JLabel("0/0/1987");
        GridBagConstraints gbc_object5_dateModifiedLabel = new GridBagConstraints();
        gbc_object5_dateModifiedLabel.insets = new Insets(0, 0, 5, 5);
        gbc_object5_dateModifiedLabel.gridx = 1;
        gbc_object5_dateModifiedLabel.gridy = 1;
        object5_historyObjectPane.add(object5_dateModifiedLabel, gbc_object5_dateModifiedLabel);
        
        object5_OldValueScrollPane = new JScrollPane();
        GridBagConstraints gbc_object5_OldValueScrollPane = new GridBagConstraints();
        gbc_object5_OldValueScrollPane.fill = GridBagConstraints.BOTH;
        gbc_object5_OldValueScrollPane.gridheight = 2;
        gbc_object5_OldValueScrollPane.insets = new Insets(0, 0, 0, 5);
        gbc_object5_OldValueScrollPane.gridx = 2;
        gbc_object5_OldValueScrollPane.gridy = 1;
        object5_historyObjectPane.add(object5_OldValueScrollPane, gbc_object5_OldValueScrollPane);
        
        object5_OldValueTextArea = new JTextArea();
        object5_OldValueScrollPane.setViewportView(object5_OldValueTextArea);
        object5_OldValueTextArea.setLineWrap(true);
        object5_OldValueTextArea.setEnabled(true);
        object5_OldValueTextArea.setEditable(true);
        object5_OldValueTextArea.setText("");
        
        object5_NewValueScrollPane = new JScrollPane();
        GridBagConstraints gbc_object5_NewValueScrollPane = new GridBagConstraints();
        gbc_object5_NewValueScrollPane.fill = GridBagConstraints.BOTH;
        gbc_object5_NewValueScrollPane.gridheight = 2;
        gbc_object5_NewValueScrollPane.insets = new Insets(0, 0, 0, 5);
        gbc_object5_NewValueScrollPane.gridx = 3;
        gbc_object5_NewValueScrollPane.gridy = 1;
        object5_historyObjectPane.add(object5_NewValueScrollPane, gbc_object5_NewValueScrollPane);
        
        object5_NewValueTextArea_1 = new JTextArea();
        object5_NewValueScrollPane.setViewportView(object5_NewValueTextArea_1);
        object5_NewValueTextArea_1.setText("");
        object5_NewValueTextArea_1.setLineWrap(true);
        object5_NewValueTextArea_1.setEnabled(true);
        object5_NewValueTextArea_1.setEditable(true);
        
        object5_ModifiedByLabel = new JLabel("System");
        GridBagConstraints gbc_object5_ModifiedByLabel = new GridBagConstraints();
        gbc_object5_ModifiedByLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_object5_ModifiedByLabel.gridheight = 2;
        gbc_object5_ModifiedByLabel.insets = new Insets(0, 0, 0, 5);
        gbc_object5_ModifiedByLabel.gridx = 4;
        gbc_object5_ModifiedByLabel.gridy = 1;
        object5_historyObjectPane.add(object5_ModifiedByLabel, gbc_object5_ModifiedByLabel);
        
        object5_verticalStrut_2 = Box.createVerticalStrut(40);
        GridBagConstraints gbc_object5_verticalStrut_2 = new GridBagConstraints();
        gbc_object5_verticalStrut_2.insets = new Insets(0, 0, 0, 5);
        gbc_object5_verticalStrut_2.gridx = 0;
        gbc_object5_verticalStrut_2.gridy = 2;
        object5_historyObjectPane.add(object5_verticalStrut_2, gbc_object5_verticalStrut_2);
        
        object5_timeModifiedLabel = new JLabel("00:00:01 AM");
        GridBagConstraints gbc_object5_timeModifiedLabel = new GridBagConstraints();
        gbc_object5_timeModifiedLabel.insets = new Insets(0, 0, 0, 5);
        gbc_object5_timeModifiedLabel.gridx = 1;
        gbc_object5_timeModifiedLabel.gridy = 2;
        object5_historyObjectPane.add(object5_timeModifiedLabel, gbc_object5_timeModifiedLabel);
        //end new Work Requests
	}
	
	//View Work Request GUI
	private void createViewWorkRequestGUI()
	{
		//view Work Requests
		viewWRPane = new JPanel();
        gernalTabbedPane.addTab("Work Requests", null, viewWRPane, null);
		viewWRPane.setLayout(new BoxLayout(viewWRPane, BoxLayout.Y_AXIS));
        
        viewWorkRequestsPane = new JTabbedPane(JTabbedPane.TOP);
        viewWRPane.add(viewWorkRequestsPane);
        
        viewGeneralInfoPane = new JPanel();
        viewWorkRequestsPane.addTab("General Information", null, viewGeneralInfoPane, null);
        GridBagLayout gbl_viewGeneralInfoPane = new GridBagLayout();
        gbl_viewGeneralInfoPane.columnWidths = new int[]{0, 0, 0, 273, 0, 0, 0};
        gbl_viewGeneralInfoPane.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 47, 0, 0, 0};
        gbl_viewGeneralInfoPane.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 1.0, 0.0, Double.MIN_VALUE};
        gbl_viewGeneralInfoPane.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
        viewGeneralInfoPane.setLayout(gbl_viewGeneralInfoPane);
        
        viewGeneralInfoVerticalStrut = Box.createVerticalStrut(20);
        GridBagConstraints gbc_viewGeneralInfoVerticalStrut = new GridBagConstraints();
        gbc_viewGeneralInfoVerticalStrut.insets = new Insets(0, 0, 5, 5);
        gbc_viewGeneralInfoVerticalStrut.gridx = 1;
        gbc_viewGeneralInfoVerticalStrut.gridy = 0;
        viewGeneralInfoPane.add(viewGeneralInfoVerticalStrut, gbc_viewGeneralInfoVerticalStrut);
        
        viewGenInfoviewGenInfohorizontalStrut_1 = Box.createHorizontalStrut(20);
        GridBagConstraints gbc_viewGenInfoviewGenInfohorizontalStrut_1 = new GridBagConstraints();
        gbc_viewGenInfoviewGenInfohorizontalStrut_1.insets = new Insets(0, 0, 5, 0);
        gbc_viewGenInfoviewGenInfohorizontalStrut_1.gridx = 5;
        gbc_viewGenInfoviewGenInfohorizontalStrut_1.gridy = 0;
        viewGeneralInfoPane.add(viewGenInfoviewGenInfohorizontalStrut_1, gbc_viewGenInfoviewGenInfohorizontalStrut_1);
        
        viewGenInfohorizontalStrut = Box.createHorizontalStrut(20);
        GridBagConstraints gbc_viewGenInfohorizontalStrut = new GridBagConstraints();
        gbc_viewGenInfohorizontalStrut.insets = new Insets(0, 0, 5, 5);
        gbc_viewGenInfohorizontalStrut.gridx = 0;
        gbc_viewGenInfohorizontalStrut.gridy = 1;
        viewGeneralInfoPane.add(viewGenInfohorizontalStrut, gbc_viewGenInfohorizontalStrut);
        
        viewWorkRequestNumLabel = new JLabel("Work Request Number");
        GridBagConstraints gbc_viewWorkRequestNumLabel = new GridBagConstraints();
        gbc_viewWorkRequestNumLabel.anchor = GridBagConstraints.EAST;
        gbc_viewWorkRequestNumLabel.insets = new Insets(0, 0, 5, 5);
        gbc_viewWorkRequestNumLabel.gridx = 2;
        gbc_viewWorkRequestNumLabel.gridy = 1;
        viewGeneralInfoPane.add(viewWorkRequestNumLabel, gbc_viewWorkRequestNumLabel);
        
        viewWRNumTextField = new JTextField();
        GridBagConstraints gbc_viewWRNumTextField = new GridBagConstraints();
        gbc_viewWRNumTextField.gridwidth = 2;
        gbc_viewWRNumTextField.insets = new Insets(0, 0, 5, 5);
        gbc_viewWRNumTextField.fill = GridBagConstraints.HORIZONTAL;
        gbc_viewWRNumTextField.gridx = 3;
        gbc_viewWRNumTextField.gridy = 1;
        viewGeneralInfoPane.add(viewWRNumTextField, gbc_viewWRNumTextField);
        viewWRNumTextField.setColumns(10);
        
        viewWRDatePreppedLabel = new JLabel("Date Prepared");
        GridBagConstraints gbc_viewWRDatePreppedLabel = new GridBagConstraints();
        gbc_viewWRDatePreppedLabel.anchor = GridBagConstraints.EAST;
        gbc_viewWRDatePreppedLabel.insets = new Insets(0, 0, 5, 5);
        gbc_viewWRDatePreppedLabel.gridx = 2;
        gbc_viewWRDatePreppedLabel.gridy = 2;
        viewGeneralInfoPane.add(viewWRDatePreppedLabel, gbc_viewWRDatePreppedLabel);
        
        viewDatePrepTextField = new JTextField();
        viewDatePrepTextField.setColumns(10);
        GridBagConstraints gbc_viewDatePrepTextField = new GridBagConstraints();
        gbc_viewDatePrepTextField.gridwidth = 2;
        gbc_viewDatePrepTextField.insets = new Insets(0, 0, 5, 5);
        gbc_viewDatePrepTextField.fill = GridBagConstraints.HORIZONTAL;
        gbc_viewDatePrepTextField.gridx = 3;
        gbc_viewDatePrepTextField.gridy = 2;
        viewGeneralInfoPane.add(viewDatePrepTextField, gbc_viewDatePrepTextField);
        
        viewPMforWRLabel = new JLabel("PM for this Work Request");
        GridBagConstraints gbc_viewPMforWRLabel = new GridBagConstraints();
        gbc_viewPMforWRLabel.anchor = GridBagConstraints.EAST;
        gbc_viewPMforWRLabel.insets = new Insets(0, 0, 5, 5);
        gbc_viewPMforWRLabel.gridx = 2;
        gbc_viewPMforWRLabel.gridy = 3;
        viewGeneralInfoPane.add(viewPMforWRLabel, gbc_viewPMforWRLabel);
        
        viewIsPMCheckBox = new JCheckBox("");
        GridBagConstraints gbc_viewIsPMCheckBox = new GridBagConstraints();
        gbc_viewIsPMCheckBox.anchor = GridBagConstraints.WEST;
        gbc_viewIsPMCheckBox.insets = new Insets(0, 0, 5, 5);
        gbc_viewIsPMCheckBox.gridx = 3;
        gbc_viewIsPMCheckBox.gridy = 3;
        viewGeneralInfoPane.add(viewIsPMCheckBox, gbc_viewIsPMCheckBox);
        
        viewRequesterLabel = new JLabel("Requester");
        GridBagConstraints gbc_viewRequesterLabel = new GridBagConstraints();
        gbc_viewRequesterLabel.anchor = GridBagConstraints.EAST;
        gbc_viewRequesterLabel.insets = new Insets(0, 0, 5, 5);
        gbc_viewRequesterLabel.gridx = 2;
        gbc_viewRequesterLabel.gridy = 4;
        viewGeneralInfoPane.add(viewRequesterLabel, gbc_viewRequesterLabel);
        
        viewRequesterTextField = new JTextField();
        viewRequesterTextField.setColumns(10);
        GridBagConstraints gbc_viewRequesterTextField = new GridBagConstraints();
        gbc_viewRequesterTextField.gridwidth = 2;
        gbc_viewRequesterTextField.insets = new Insets(0, 0, 5, 5);
        gbc_viewRequesterTextField.fill = GridBagConstraints.HORIZONTAL;
        gbc_viewRequesterTextField.gridx = 3;
        gbc_viewRequesterTextField.gridy = 4;
        viewGeneralInfoPane.add(viewRequesterTextField, gbc_viewRequesterTextField);
        
        viewRequesterEmailLabel = new JLabel("Requster Email");
        GridBagConstraints gbc_viewRequesterEmailLabel = new GridBagConstraints();
        gbc_viewRequesterEmailLabel.anchor = GridBagConstraints.EAST;
        gbc_viewRequesterEmailLabel.insets = new Insets(0, 0, 5, 5);
        gbc_viewRequesterEmailLabel.gridx = 2;
        gbc_viewRequesterEmailLabel.gridy = 5;
        viewGeneralInfoPane.add(viewRequesterEmailLabel, gbc_viewRequesterEmailLabel);
        
        viewReqEmailTextField = new JTextField();
        viewReqEmailTextField.setColumns(10);
        GridBagConstraints gbc_viewReqEmailTextField = new GridBagConstraints();
        gbc_viewReqEmailTextField.gridwidth = 2;
        gbc_viewReqEmailTextField.insets = new Insets(0, 0, 5, 5);
        gbc_viewReqEmailTextField.fill = GridBagConstraints.HORIZONTAL;
        gbc_viewReqEmailTextField.gridx = 3;
        gbc_viewReqEmailTextField.gridy = 5;
        viewGeneralInfoPane.add(viewReqEmailTextField, gbc_viewReqEmailTextField);
        
        viewRequestPhoneLabel = new JLabel("Requester Phone");
        GridBagConstraints gbc_viewRequestPhoneLabel = new GridBagConstraints();
        gbc_viewRequestPhoneLabel.anchor = GridBagConstraints.EAST;
        gbc_viewRequestPhoneLabel.insets = new Insets(0, 0, 5, 5);
        gbc_viewRequestPhoneLabel.gridx = 2;
        gbc_viewRequestPhoneLabel.gridy = 6;
        viewGeneralInfoPane.add(viewRequestPhoneLabel, gbc_viewRequestPhoneLabel);
        
        viewReqPhoneTextField = new JTextField();
        viewReqPhoneTextField.setColumns(10);
        GridBagConstraints gbc_viewReqPhoneTextField = new GridBagConstraints();
        gbc_viewReqPhoneTextField.gridwidth = 2;
        gbc_viewReqPhoneTextField.insets = new Insets(0, 0, 5, 5);
        gbc_viewReqPhoneTextField.fill = GridBagConstraints.HORIZONTAL;
        gbc_viewReqPhoneTextField.gridx = 3;
        gbc_viewReqPhoneTextField.gridy = 6;
        viewGeneralInfoPane.add(viewReqPhoneTextField, gbc_viewReqPhoneTextField);
        
        viewOrganisationLabel = new JLabel("Organisation");
        GridBagConstraints gbc_viewOrganisationLabel = new GridBagConstraints();
        gbc_viewOrganisationLabel.anchor = GridBagConstraints.EAST;
        gbc_viewOrganisationLabel.insets = new Insets(0, 0, 5, 5);
        gbc_viewOrganisationLabel.gridx = 2;
        gbc_viewOrganisationLabel.gridy = 7;
        viewGeneralInfoPane.add(viewOrganisationLabel, gbc_viewOrganisationLabel);
        
        viewOrgTextField = new JTextField();
        viewOrgTextField.setColumns(10);
        GridBagConstraints gbc_viewOrgTextField = new GridBagConstraints();
        gbc_viewOrgTextField.gridwidth = 2;
        gbc_viewOrgTextField.insets = new Insets(0, 0, 5, 5);
        gbc_viewOrgTextField.fill = GridBagConstraints.HORIZONTAL;
        gbc_viewOrgTextField.gridx = 3;
        gbc_viewOrgTextField.gridy = 7;
        viewGeneralInfoPane.add(viewOrgTextField, gbc_viewOrgTextField);
        
        viewRequestedOnBehalfLabel = new JLabel("Requested on behalf of");
        GridBagConstraints gbc_viewRequestedOnBehalfLabel = new GridBagConstraints();
        gbc_viewRequestedOnBehalfLabel.anchor = GridBagConstraints.EAST;
        gbc_viewRequestedOnBehalfLabel.insets = new Insets(0, 0, 5, 5);
        gbc_viewRequestedOnBehalfLabel.gridx = 2;
        gbc_viewRequestedOnBehalfLabel.gridy = 8;
        viewGeneralInfoPane.add(viewRequestedOnBehalfLabel, gbc_viewRequestedOnBehalfLabel);
        
        viewOnBehalfTextField = new JTextField();
        viewOnBehalfTextField.setColumns(10);
        GridBagConstraints gbc_viewOnBehalfTextField = new GridBagConstraints();
        gbc_viewOnBehalfTextField.gridwidth = 2;
        gbc_viewOnBehalfTextField.insets = new Insets(0, 0, 5, 5);
        gbc_viewOnBehalfTextField.fill = GridBagConstraints.HORIZONTAL;
        gbc_viewOnBehalfTextField.gridx = 3;
        gbc_viewOnBehalfTextField.gridy = 8;
        viewGeneralInfoPane.add(viewOnBehalfTextField, gbc_viewOnBehalfTextField);
        
        viewProjectManagerLabel = new JLabel("Project Manager");
        GridBagConstraints gbc_viewProjectManagerLabel = new GridBagConstraints();
        gbc_viewProjectManagerLabel.anchor = GridBagConstraints.EAST;
        gbc_viewProjectManagerLabel.insets = new Insets(0, 0, 5, 5);
        gbc_viewProjectManagerLabel.gridx = 2;
        gbc_viewProjectManagerLabel.gridy = 9;
        viewGeneralInfoPane.add(viewProjectManagerLabel, gbc_viewProjectManagerLabel);
        
        viewPmTextField = new JTextField();
        viewPmTextField.setColumns(10);
        GridBagConstraints gbc_viewPmTextField = new GridBagConstraints();
        gbc_viewPmTextField.gridwidth = 2;
        gbc_viewPmTextField.insets = new Insets(0, 0, 5, 5);
        gbc_viewPmTextField.fill = GridBagConstraints.HORIZONTAL;
        gbc_viewPmTextField.gridx = 3;
        gbc_viewPmTextField.gridy = 9;
        viewGeneralInfoPane.add(viewPmTextField, gbc_viewPmTextField);
        
        viewPMEmailLabel = new JLabel("Project Manager Email");
        GridBagConstraints gbc_viewPMEmailLabel = new GridBagConstraints();
        gbc_viewPMEmailLabel.anchor = GridBagConstraints.EAST;
        gbc_viewPMEmailLabel.insets = new Insets(0, 0, 5, 5);
        gbc_viewPMEmailLabel.gridx = 2;
        gbc_viewPMEmailLabel.gridy = 10;
        viewGeneralInfoPane.add(viewPMEmailLabel, gbc_viewPMEmailLabel);
        
        viewPMEmailTextField = new JTextField();
        viewPMEmailTextField.setColumns(10);
        GridBagConstraints gbc_viewPMEmailTextField = new GridBagConstraints();
        gbc_viewPMEmailTextField.gridwidth = 2;
        gbc_viewPMEmailTextField.insets = new Insets(0, 0, 5, 5);
        gbc_viewPMEmailTextField.fill = GridBagConstraints.HORIZONTAL;
        gbc_viewPMEmailTextField.gridx = 3;
        gbc_viewPMEmailTextField.gridy = 10;
        viewGeneralInfoPane.add(viewPMEmailTextField, gbc_viewPMEmailTextField);
        
        viewPMPhoneLabel = new JLabel("Project Manager Phone");
        GridBagConstraints gbc_viewPMPhoneLabel = new GridBagConstraints();
        gbc_viewPMPhoneLabel.anchor = GridBagConstraints.EAST;
        gbc_viewPMPhoneLabel.insets = new Insets(0, 0, 5, 5);
        gbc_viewPMPhoneLabel.gridx = 2;
        gbc_viewPMPhoneLabel.gridy = 11;
        viewGeneralInfoPane.add(viewPMPhoneLabel, gbc_viewPMPhoneLabel);
        
        viewPMPhoneTextField = new JTextField();
        viewPMPhoneTextField.setColumns(10);
        GridBagConstraints gbc_viewPMPhoneTextField = new GridBagConstraints();
        gbc_viewPMPhoneTextField.gridwidth = 2;
        gbc_viewPMPhoneTextField.insets = new Insets(0, 0, 5, 5);
        gbc_viewPMPhoneTextField.fill = GridBagConstraints.HORIZONTAL;
        gbc_viewPMPhoneTextField.gridx = 3;
        gbc_viewPMPhoneTextField.gridy = 11;
        viewGeneralInfoPane.add(viewPMPhoneTextField, gbc_viewPMPhoneTextField);
        
        viewWRStatusLabel = new JLabel("Work Request Status");
        GridBagConstraints gbc_viewWRStatusLabel = new GridBagConstraints();
        gbc_viewWRStatusLabel.anchor = GridBagConstraints.EAST;
        gbc_viewWRStatusLabel.insets = new Insets(0, 0, 5, 5);
        gbc_viewWRStatusLabel.gridx = 2;
        gbc_viewWRStatusLabel.gridy = 12;
        viewGeneralInfoPane.add(viewWRStatusLabel, gbc_viewWRStatusLabel);
        
        viewWRStatusTextField = new JTextField();
        viewWRStatusTextField.setColumns(10);
        GridBagConstraints gbc_viewWRStatusTextField = new GridBagConstraints();
        gbc_viewWRStatusTextField.gridwidth = 2;
        gbc_viewWRStatusTextField.insets = new Insets(0, 0, 5, 5);
        gbc_viewWRStatusTextField.fill = GridBagConstraints.HORIZONTAL;
        gbc_viewWRStatusTextField.gridx = 3;
        gbc_viewWRStatusTextField.gridy = 12;
        viewGeneralInfoPane.add(viewWRStatusTextField, gbc_viewWRStatusTextField);
        
        viewGeneralInfoHorizontalStrut = Box.createHorizontalStrut(20);
        GridBagConstraints gbc_viewGeneralInfoHorizontalStrut = new GridBagConstraints();
        gbc_viewGeneralInfoHorizontalStrut.insets = new Insets(0, 0, 5, 5);
        gbc_viewGeneralInfoHorizontalStrut.gridx = 0;
        gbc_viewGeneralInfoHorizontalStrut.gridy = 13;
        viewGeneralInfoPane.add(viewGeneralInfoHorizontalStrut, gbc_viewGeneralInfoHorizontalStrut);
        
        viewPrevWRLabel = new JLabel("Previous WR Info");
        GridBagConstraints gbc_viewPrevWRLabel = new GridBagConstraints();
        gbc_viewPrevWRLabel.anchor = GridBagConstraints.NORTH;
        gbc_viewPrevWRLabel.insets = new Insets(0, 0, 5, 5);
        gbc_viewPrevWRLabel.gridx = 2;
        gbc_viewPrevWRLabel.gridy = 13;
        viewGeneralInfoPane.add(viewPrevWRLabel, gbc_viewPrevWRLabel);
        
        viewCopyfromPrevWRBtn = new JButton("Copy from Previous WR");
        viewCopyfromPrevWRBtn.setEnabled(false);
        GridBagConstraints gbc_viewCopyfromPrevWRBtn = new GridBagConstraints();
        gbc_viewCopyfromPrevWRBtn.anchor = GridBagConstraints.NORTHWEST;
        gbc_viewCopyfromPrevWRBtn.insets = new Insets(0, 0, 5, 5);
        gbc_viewCopyfromPrevWRBtn.gridx = 3;
        gbc_viewCopyfromPrevWRBtn.gridy = 13;
        viewGeneralInfoPane.add(viewCopyfromPrevWRBtn, gbc_viewCopyfromPrevWRBtn);
        
        viewPrevWRCopyPane = new JPanel();
        GridBagConstraints gbc_viewPrevWRCopyPane = new GridBagConstraints();
        gbc_viewPrevWRCopyPane.insets = new Insets(0, 0, 5, 5);
        gbc_viewPrevWRCopyPane.fill = GridBagConstraints.BOTH;
        gbc_viewPrevWRCopyPane.gridx = 4;
        gbc_viewPrevWRCopyPane.gridy = 13;
        viewGeneralInfoPane.add(viewPrevWRCopyPane, gbc_viewPrevWRCopyPane);
        viewPrevWRCopyPane.setLayout(new BoxLayout(viewPrevWRCopyPane, BoxLayout.Y_AXIS));
        
        viewWRNumLabel = new JLabel("Work Order Number: none");
        viewPrevWRCopyPane.add(viewWRNumLabel);
        
        viewProjNameLabel = new JLabel("Project Name: none");
        viewPrevWRCopyPane.add(viewProjNameLabel);
        
        viewP2NumLabel = new JLabel("P2 Number: none");
        viewPrevWRCopyPane.add(viewP2NumLabel);
        
        viewDateAddedLabel = new JLabel("Date Added");
        viewDateAddedLabel.setForeground(Color.DARK_GRAY);
        viewDateAddedLabel.setHorizontalAlignment(SwingConstants.CENTER);
        viewDateAddedLabel.setBackground(new Color(176, 224, 230));
        GridBagConstraints gbc_viewDateAddedLabel = new GridBagConstraints();
        gbc_viewDateAddedLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_viewDateAddedLabel.insets = new Insets(0, 0, 5, 5);
        gbc_viewDateAddedLabel.gridx = 2;
        gbc_viewDateAddedLabel.gridy = 14;
        viewGeneralInfoPane.add(viewDateAddedLabel, gbc_viewDateAddedLabel);
        
        viewUserLabel = new JLabel("User");
        viewUserLabel.setForeground(Color.DARK_GRAY);
        viewUserLabel.setBackground(new Color(176, 224, 230));
        viewUserLabel.setHorizontalAlignment(SwingConstants.CENTER);
        GridBagConstraints gbc_viewUserLabel = new GridBagConstraints();
        gbc_viewUserLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_viewUserLabel.insets = new Insets(0, 0, 5, 5);
        gbc_viewUserLabel.gridx = 3;
        gbc_viewUserLabel.gridy = 14;
        viewGeneralInfoPane.add(viewUserLabel, gbc_viewUserLabel);
        
        viewRemarksLabel = new JLabel("Remark/Note");
        viewRemarksLabel.setForeground(Color.DARK_GRAY);
        viewRemarksLabel.setBackground(new Color(176, 224, 230));
        viewRemarksLabel.setHorizontalAlignment(SwingConstants.CENTER);
        GridBagConstraints gbc_viewRemarksLabel = new GridBagConstraints();
        gbc_viewRemarksLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_viewRemarksLabel.insets = new Insets(0, 0, 5, 5);
        gbc_viewRemarksLabel.gridx = 4;
        gbc_viewRemarksLabel.gridy = 14;
        viewGeneralInfoPane.add(viewRemarksLabel, gbc_viewRemarksLabel);
        
        viewDateAddedTextField = new JTextField();
        GridBagConstraints gbc_viewDateAddedTextField = new GridBagConstraints();
        gbc_viewDateAddedTextField.insets = new Insets(0, 0, 0, 5);
        gbc_viewDateAddedTextField.fill = GridBagConstraints.HORIZONTAL;
        gbc_viewDateAddedTextField.gridx = 2;
        gbc_viewDateAddedTextField.gridy = 15;
        viewGeneralInfoPane.add(viewDateAddedTextField, gbc_viewDateAddedTextField);
        viewDateAddedTextField.setColumns(10);
        
        viewUserTextField = new JTextField();
        GridBagConstraints gbc_viewUserTextField = new GridBagConstraints();
        gbc_viewUserTextField.insets = new Insets(0, 0, 0, 5);
        gbc_viewUserTextField.fill = GridBagConstraints.HORIZONTAL;
        gbc_viewUserTextField.gridx = 3;
        gbc_viewUserTextField.gridy = 15;
        viewGeneralInfoPane.add(viewUserTextField, gbc_viewUserTextField);
        viewUserTextField.setColumns(10);
        
        viewNotesScollPane = new JScrollPane();
        GridBagConstraints gbc_viewNotesScollPane = new GridBagConstraints();
        gbc_viewNotesScollPane.fill = GridBagConstraints.BOTH;
        gbc_viewNotesScollPane.insets = new Insets(0, 0, 0, 5);
        gbc_viewNotesScollPane.gridx = 4;
        gbc_viewNotesScollPane.gridy = 15;
        viewGeneralInfoPane.add(viewNotesScollPane, gbc_viewNotesScollPane);
        
        viewNotesTextField = new JTextArea();
        viewNotesScollPane.setViewportView(viewNotesTextField);
        viewNotesTextField.setColumns(10);
        
        workRequestSelectionPane = new JPanel();
        viewWRPane.add(workRequestSelectionPane);
        GridBagLayout gbl_workRequestSelectionPane = new GridBagLayout();
        gbl_workRequestSelectionPane.columnWidths = new int[]{0, 0, 0, 0};
        gbl_workRequestSelectionPane.rowHeights = new int[]{0, 0, 0, 0};
        gbl_workRequestSelectionPane.columnWeights = new double[]{0.0, 1.0, 0.0, Double.MIN_VALUE};
        gbl_workRequestSelectionPane.rowWeights = new double[]{0.0, 1.0, 0.0, Double.MIN_VALUE};
        workRequestSelectionPane.setLayout(gbl_workRequestSelectionPane);
        
        workRequestVertSelectionStrut = Box.createVerticalStrut(20);
        GridBagConstraints gbc_workRequestVertSelectionStrut = new GridBagConstraints();
        gbc_workRequestVertSelectionStrut.insets = new Insets(0, 0, 5, 5);
        gbc_workRequestVertSelectionStrut.gridx = 1;
        gbc_workRequestVertSelectionStrut.gridy = 0;
        workRequestSelectionPane.add(workRequestVertSelectionStrut, gbc_workRequestVertSelectionStrut);
        
        workRequestSelectionStrut = Box.createHorizontalStrut(20);
        GridBagConstraints gbc_workRequestSelectionStrut = new GridBagConstraints();
        gbc_workRequestSelectionStrut.insets = new Insets(0, 0, 5, 5);
        gbc_workRequestSelectionStrut.gridx = 0;
        gbc_workRequestSelectionStrut.gridy = 1;
        workRequestSelectionPane.add(workRequestSelectionStrut, gbc_workRequestSelectionStrut);
                
        String columnNames[] = {"WR Number", "Project Manager", "WR Status ID"};
        tableModel = new DefaultTableModel(columnNames,0);
        
        scrollPane_7 = new JScrollPane();
        GridBagConstraints gbc_scrollPane_7 = new GridBagConstraints();
        gbc_scrollPane_7.fill = GridBagConstraints.BOTH;
        gbc_scrollPane_7.insets = new Insets(0, 0, 5, 5);
        gbc_scrollPane_7.gridx = 1;
        gbc_scrollPane_7.gridy = 1;
        workRequestSelectionPane.add(scrollPane_7, gbc_scrollPane_7);
        workRequestsSelectionTable = new JTable(tableModel);
        scrollPane_7.setViewportView(workRequestsSelectionTable);
        workRequestsSelectionTable.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
        
        workRequestSelectionStrut_1 = Box.createHorizontalStrut(20);
        GridBagConstraints gbc_workRequestSelectionStrut_1 = new GridBagConstraints();
        gbc_workRequestSelectionStrut_1.insets = new Insets(0, 0, 5, 0);
        gbc_workRequestSelectionStrut_1.gridx = 2;
        gbc_workRequestSelectionStrut_1.gridy = 1;
        workRequestSelectionPane.add(workRequestSelectionStrut_1, gbc_workRequestSelectionStrut_1);
        
        workRequestVertSelectionStrut_1 = Box.createVerticalStrut(20);
        GridBagConstraints gbc_workRequestVertSelectionStrut_1 = new GridBagConstraints();
        gbc_workRequestVertSelectionStrut_1.insets = new Insets(0, 0, 0, 5);
        gbc_workRequestVertSelectionStrut_1.gridx = 1;
        gbc_workRequestVertSelectionStrut_1.gridy = 2;
        workRequestSelectionPane.add(workRequestVertSelectionStrut_1, gbc_workRequestVertSelectionStrut_1);
        
        //end view Work Requests
	}
	
	//
	ActionListener workRequestAction = new ActionListener() 
	{
		public void actionPerformed(ActionEvent e) 
		{
			
		}
	};
	
	private JTabbedPane gernalTabbedPane;

	//View Work Request Fields
	private JPanel viewWRPane;
	private JTabbedPane viewWorkRequestsPane;
	private JPanel viewGeneralInfoPane;
	private Component viewGeneralInfoVerticalStrut;
	private Component viewGenInfoviewGenInfohorizontalStrut_1;
	private Component viewGenInfohorizontalStrut;
	private JLabel viewWorkRequestNumLabel;
	private JTextField viewWRNumTextField;
	private JLabel viewWRDatePreppedLabel;
	private JTextField viewDatePrepTextField;
	private JLabel viewPMforWRLabel;
	private JCheckBox viewIsPMCheckBox;
	private JLabel viewRequesterLabel;
	private JTextField viewRequesterTextField;
	private JLabel viewRequesterEmailLabel;
	private JTextField viewReqEmailTextField;
	private JLabel viewRequestPhoneLabel;
	private JTextField viewReqPhoneTextField;
	private JLabel viewOrganisationLabel;
	private JTextField viewOrgTextField;
	private JLabel viewRequestedOnBehalfLabel;
	private JTextField viewOnBehalfTextField;
	private JLabel viewProjectManagerLabel;
	private JTextField viewPmTextField;
	private JLabel viewPMEmailLabel;
	private JTextField viewPMEmailTextField;
	private JLabel viewPMPhoneLabel;
	private JTextField viewPMPhoneTextField;
	private JLabel viewWRStatusLabel;
	private JTextField viewWRStatusTextField;
	private Component viewGeneralInfoHorizontalStrut;
	private JLabel viewPrevWRLabel;
	private JButton viewCopyfromPrevWRBtn;
	private JPanel viewPrevWRCopyPane;
	private JLabel viewWRNumLabel;
	private JLabel viewProjNameLabel;
	private JLabel viewP2NumLabel;
	private JLabel viewDateAddedLabel;
	private JLabel viewUserLabel;
	private JLabel viewRemarksLabel;
	private JTextField viewDateAddedTextField;
	private JTextField viewUserTextField;
	private JScrollPane viewNotesScollPane;
	private JTextArea viewNotesTextField;
	private DefaultTableModel tableModel;
	
	//New Work Request Fields
	private JPanel newRequestPane;
	private JPanel requestAnalyticsPane;
	private JPanel generalInfoPane;
	private JPanel projectInfoPane;
	private JPanel assignmentTypePane;
	private JPanel costDistroPane;
	private JPanel remarkSavePane;
	private JTextArea remarksTextArea;
	private JTabbedPane newWorkRequestPane;
	private JLabel lblNewLabel;
	private Component verticalStrut;
	private Component horizontalStrut;
	private JLabel lblNewLabel_1;
	private JLabel lblNewLabel_2;
	private JLabel lblRequester;
	private JLabel lblRequsterEmail;
	private JLabel lblRequesterPhone;
	private JLabel lblOrganisation;
	private JLabel lblRequestedOnBehalf;
	private JLabel lblProjectManager;
	private JLabel lblProjectManagerEmail;
	private JLabel lblProjectManagerPhone;
	private JLabel lblWorkRequestStatus;
	private JTextField wrNumTextField;
	private JTextField datePrepTextField;
	private JTextField requesterTextField;
	private JTextField reqEmailTextField;
	private JTextField reqPhoneTextField;
	private JTextField orgTextField;
	private JTextField onBehalfTextField;
	private JTextField pmTextField;
	private JTextField pmEmailTextField;
	private JTextField pmPhoneTextField;
	private JTextField wrStatusTextFeild;
	private JCheckBox isPMCheckBox;
	private Component horizontalStrut_1;
	private JLabel lblNewLabel_3;
	private JLabel lblNewLabel_4;
	private JLabel lblNewLabel_5;
	private Component verticalStrut_2;
	private JTextField dateAddedTextField;
	private JTextField userTextField;
	private JTextArea notesTextField;
	private JLabel projectNameLabel;
	private JLabel projectNumLabel;
	private JLabel painLabel;
	private JLabel amountLabel;
	private JLabel proYearLabel;
	private JLabel p2Label;
	private JLabel contractNumberLabel;
	private JLabel fundSrcLabel;
	private JLabel sectionLabel;
	private JLabel countryLabel;
	private JLabel regionLabel;
	private JLabel siteLabel;
	private JLabel analystSuperLabel;
	private JLabel branchLabel;
	private JLabel directProLabel;
	private JLabel workReqDocFolderLabel;
	private JTextField projectNameTextField;
	private JTextField projectNumTextField;
	private JTextField painTextField;
	private JTextField programAmountTextField;
	private JTextField programYearTextField;
	private JTextField p2TextField;
	private JTextField contractNumTextField;
	private JComboBox fundSourceComboBox;
	private JComboBox sectionComboBox;
	private JComboBox countryComboBox;
	private JComboBox regionComboBox;
	private JComboBox siteComboBox;
	private JComboBox programAnalystSupervisorComboBox;
	private JTextField branchTextField;
	private JRadioButton rdbtnDirect;
	private JRadioButton rdbtnIndirect;
	private JRadioButton rdbtnNotApplicable;
	private JTextField wrPathTextField;
	private Component horizontalStrut_3;
	private JButton saveRequestBtn;
	private JButton submitWorkRequestBtn;
	private JTextArea wrRemarkTextArea;
	private Component verticalStrut_6;
	private Component verticalStrut_1;
	private JScrollPane scrollPane;
	private Component horizontalStrut_5;
	private JScrollPane scrollPane_1;
	private JPanel historyPane;
	private JLabel prevWRLabel;
	private JButton copyPrevWRBtn;
	private JPanel prevWRCopyPane;
	private JLabel wrNumberLabel;
	private JLabel projNameLabel;
	private JLabel p2NumLabel;
	private JLabel assignmentCatLabel;
	private JLabel assignmentTypeLabel;
	private JLabel wrStartDateLabel;
	private JLabel wrCompletionDateLabel;
	private JLabel comInDRChecksLabel;
	private JLabel docHandlingLabel;
	private JLabel addtionalInfoLabel;
	private JRadioButton rdbtnNewRadioButton;
	private JRadioButton rdbtnNewRadioButton_1;
	private JRadioButton rdbtnNewRadioButton_2;
	private JRadioButton drChecks_YesRadioBtn;
	private JRadioButton drChecks_NoRadioBtn;
	private JCheckBox selectSuperCheckBox;
	private JComboBox assignmentCatComboBox;
	private JComboBox assignmentTypeComboBox;
	private JComboBox assignmentSubTypeComboBox;
	private Component horizontalStrut_6;
	private Component verticalStrut_3;
	private Component horizontalStrut_7;
	private JTextArea additionalInfoTextArea;
	private JLabel supervisorInfo;
	private JLabel assignmentSubTypeLabel;
	private JTextField textField_4;
	private JTextField textField_5;
	private JLabel discLabel;
	private JLabel directTaskLabel;
	private JLabel costLabel;
	private JLabel assigneePrevWRLabel;
	private Component horizontalStrut_8;
	private Component horizontalStrut_9;
	private JLabel civilLabel;
	private JLabel archLabel;
	private JLabel structLabel;
	private JLabel antiterrorLabel;
	private JLabel mechLabel;
	private JLabel fireProtLabel;
	private JLabel electLabel;
	private JLabel comLabel;
	private JLabel leedLabel;
	private JLabel envLabel;
	private JLabel costEngLabel;
	private JLabel geotechLabel;
	private JLabel aeContractLabel;
	private JLabel valueEngLabel;
	private JLabel translatorLabel;
	private JLabel specLabel;
	private JLabel otherLabel;
	private JSpinner spinner;
	private JSpinner spinner_1;
	private JSpinner spinner_2;
	private JSpinner spinner_3;
	private JSpinner spinner_4;
	private JSpinner spinner_5;
	private JSpinner spinner_6;
	private JSpinner spinner_7;
	private JSpinner spinner_8;
	private JSpinner spinner_9;
	private JSpinner spinner_10;
	private JSpinner spinner_11;
	private JSpinner spinner_12;
	private JSpinner spinner_13;
	private JSpinner spinner_14;
	private JSpinner spinner_15;
	private JSpinner spinner_16;
	private JPanel costDistroContPane;
	private JScrollPane scrollPane_2;
	private Component horizontalStrut_2;
	private JPanel otherDiscplinesPane;
	private JLabel otherDiscLabel;
	private JTextArea otherDisciplineTextArea;
	private JScrollPane scrollPane_3;
	private JPanel contDiscPane;
	private JLabel p2NumDispLabel;
	private Component horizontalStrut_10;
	private JLabel laborChargeCodeLabel;
	private JLabel fundedWRLabel;
	private JLabel lccBalanceLabel;
	private JLabel fwiBalanceLabel;
	private JLabel orderItemLabel;
	private JLabel issueOrgCodeLabel;
	private JTextField p2NumberTextField;
	private JTextField laborChargeCodeTextField;
	private JTextField fundedWorkItemTextField;
	private JTextField lccBalTextField;
	private JTextField fwiBalTextArea;
	private JTextField orderWorkItemTextArea;
	private JTextField issuedOrgCodTextArea;
	private JLabel totalCostLabel;
	private JTextField totalCostOtherDisplinesTextField;
	private JLabel fixedFeeLabel;
	private JLabel managementCostLabel;
	private JLabel totalLaborCostLabel;
	private JTextField fixedFeeAssignTypeTextField;
	private JTextField managementCostTextField;
	private JTextField totalLaborCostTextField;
	private JPanel travelInfoPane;
	private Component horizontalStrut_11;
	private Component verticalGlue;
	private JLabel transLabel;
	private JLabel perDiemLabel;
	private JLabel travOtherLabel;
	private JLabel travelCatLabel;
	private JLabel specificTravelIndoLabel;
	private JLabel allowCostLabel;
	private Component horizontalStrut_12;
	private JTextArea transportationTextArea;
	private JSpinner travelTransportationSpinBox;
	private JTextField travelTransportationPRCSpinBox;
	private JTextArea perDiemTextArea;
	private Component verticalStrut_4;
	private Component verticalStrut_5;
	private JLabel travelCostLabel;
	private JTextField travelCostTextField;
	private JTextArea otherTravelTextArea;
	private JSpinner travelPerDiemSpinBox;
	private JSpinner travelOtherSpinBox;
	private Component verticalStrut_7;
	private Component verticalStrut_8;
	private JScrollPane scrollPane_4;
	private JScrollPane scrollPane_5;
	private JScrollPane scrollPane_6;
	private JLabel dollarLabel;
	private JLabel prcLabel;
	private JLabel dollar2Label;
	private JLabel dollar3Label;
	private Component horizontalStrut_13;
	private Component horizontalStrut_14;
	private Component verticalStrut_9;
	private Component verticalStrut_10;
	private JLabel civialCostLabel;
	private JLabel archCostLabel;
	private JLabel strucCostLabel;
	private JLabel forceProtCostLabel;
	private JLabel mechCostLabel;
	private JLabel fireProtCostLabel;
	private JLabel electricalCostLabel;
	private JLabel commsCostLabel;
	private JLabel leedCostLabel;
	private JLabel envCostLabel;
	private JLabel costEngCostLabel;
	private JLabel geotechCostLabel;
	private JLabel aeContractingLabel;
	private JLabel valueEngCostLabel;
	private JLabel translatorCostLabel;
	private JLabel specCostLabel;
	private JLabel otherCostLabel;
	private JLabel prevCivialCostLabel;
	private JLabel prevArchCostLabel;
	private JLabel prevStrucCostLabel;
	private JLabel prevForceProtCostLabel;
	private JLabel prevMechCostLabel;
	private JLabel prevFireProtCostLabel;
	private JLabel prevElectricalCostLabel;
	private JLabel prevCommsCostLabel;
	private JLabel prevLEEDCostLabel;
	private JLabel prevEnvCostLabel;
	private JLabel prevCostEngCostLabel;
	private JLabel prevGeotechCostLabel;
	private JLabel prevAEContractingLabel;
	private JLabel prevValueEngCostLabel;
	private JLabel prevTranslatorCostLabel;
	private JLabel prevSpecCostLabel;
	private JLabel prevOtherCostLabel;
	private JPanel personnelAssignmentPane;
	private JPanel personnelTotalCostPane;
	private JPanel assignPersonnelPane;
	private JLabel personnelAssignedTotalCostLabel;
	private JLabel personnelProvidedTotalCostLabel;
	private JLabel personnelSubmissionDateLabel;
	private JLabel wrDocFolderLabel;
	private JTextField wrDocFolderTextField;;
	private JLabel docLocationLabel;
	private JTextField docLocationTextField;
	private JCheckBox allAssigneesCheckBox;
	private Component horizontalStrut_15;
	private Component horizontalStrut_16;
	private Component verticalStrut_11;
	private JLabel varPersonnelAssignedTotalCostLabel;
	private JLabel varPersonnelProvidedTotalCostLabel;
	private JTextField personnelSubmissionDateTextField;
	private JScrollPane assignmentScrollPane;
	
	private JPanel civilPersonnelPane;
	private Component civilVerticalStrut;
	private Component civilHorzizontalStrut;
	private Component civilHorzizontalStrut_2;
	private JLabel civilTitleLabel;
	private JLabel civilFillerLabel;
	private JLabel civilAssigneeTitleLabel;
	private JLabel civilHoursAssingedTitleLabel;
	private JLabel civilWorkRateTitleLabel;
	private JLabel civilDateCompleteTitleLabel;
	private JLabel civilBackcheckTitleLabel;
	private JCheckBox civilAssigneeCheckBox;
	private JLabel civilAssigneeName;
	private JLabel civilHoursAssignedLabel;
	private JLabel civilWorkRateLabel;
	private JLabel civilDateCompletedLabel;
	private JLabel civilBackcheckLabel;
	
	private JPanel architecturalPersonnelPane;
	private Component architecturalVerticalStrut;
	private Component architecturalHorzizontalStrut;
	private Component architecturalHorzizontalStrut_2;
	private JLabel architecturalTitleLabel;
	private JLabel architecturalFillerLabel;
	private JLabel architecturalAssigneeTitleLabel;
	private JLabel architecturalHoursAssingedTitleLabel;
	private JLabel architecturalWorkRateTitleLabel;
	private JLabel architecturalDateCompleteTitleLabel;
	private JLabel architecturalBackcheckTitleLabel;
	private JCheckBox architecturalAssigneeCheckBox;
	private JLabel architecturalAssigneeName;
	private JLabel architecturalHoursAssignedLabel;
	private JLabel architecturalWorkRateLabel;
	private JLabel architecturalDateCompletedLabel;
	private JLabel architecturalBackcheckLabel;
	
	private JPanel structuralPersonnelPane;
	private Component structuralVerticalStrut;
	private Component structuralHorzizontalStrut;
	private Component structuralHorzizontalStrut_2;
	private JLabel structuralTitleLabel;
	private JLabel structuralFillerLabel;
	private JLabel structuralAssigneeTitleLabel;
	private JLabel structuralHoursAssingedTitleLabel;
	private JLabel structuralWorkRateTitleLabel;
	private JLabel structuralDateCompleteTitleLabel;
	private JLabel structuralBackcheckTitleLabel;
	private JCheckBox structuralAssigneeCheckBox;
	private JLabel structuralAssigneeName;
	private JLabel structuralHoursAssignedLabel;
	private JLabel structuralWorkRateLabel;
	private JLabel structuralDateCompletedLabel;
	private JLabel structuralBackcheckLabel;

	private JPanel forceProtectionPersonnelPane;
	private Component forceProtectionVerticalStrut;
	private Component forceProtectionHorzizontalStrut;
	private Component forceProtectionHorzizontalStrut_2;
	private JLabel forceProtectionTitleLabel;
	private JLabel forceProtectionFillerLabel;
	private JLabel forceProtectionAssigneeTitleLabel;
	private JLabel forceProtectionHoursAssingedTitleLabel;
	private JLabel forceProtectionWorkRateTitleLabel;
	private JLabel forceProtectionDateCompleteTitleLabel;
	private JLabel forceProtectionBackcheckTitleLabel;
	private JCheckBox forceProtectionAssigneeCheckBox;
	private JLabel forceProtectionAssigneeName;
	private JLabel forceProtectionHoursAssignedLabel;
	private JLabel forceProtectionWorkRateLabel;
	private JLabel forceProtectionDateCompletedLabel;
	private JLabel forceProtectionBackcheckLabel;
	
	private JPanel mechanicalPersonnelPane;
	private Component mechanicalVerticalStrut;
	private Component mechanicalHorzizontalStrut;
	private Component mechanicalHorzizontalStrut_2;
	private JLabel mechanicalTitleLabel;
	private JLabel mechanicalFillerLabel;
	private JLabel mechanicalAssigneeTitleLabel;
	private JLabel mechanicalHoursAssingedTitleLabel;
	private JLabel mechanicalWorkRateTitleLabel;
	private JLabel mechanicalDateCompleteTitleLabel;
	private JLabel mechanicalBackcheckTitleLabel;
	private JCheckBox mechanicalAssigneeCheckBox;
	private JLabel mechanicalAssigneeName;
	private JLabel mechanicalHoursAssignedLabel;
	private JLabel mechanicalWorkRateLabel;
	private JLabel mechanicalDateCompletedLabel;
	private JLabel mechanicalBackcheckLabel;
	
	private JPanel fireProtectionPersonnelPane;
	private Component fireProtectionVerticalStrut;
	private Component fireProtectionHorzizontalStrut;
	private Component fireProtectionHorzizontalStrut_2;
	private JLabel fireProtectionTitleLabel;
	private JLabel fireProtectionFillerLabel;
	private JLabel fireProtectionAssigneeTitleLabel;
	private JLabel fireProtectionHoursAssingedTitleLabel;
	private JLabel fireProtectionWorkRateTitleLabel;
	private JLabel fireProtectionDateCompleteTitleLabel;
	private JLabel fireProtectionBackcheckTitleLabel;
	private JCheckBox fireProtectionAssigneeCheckBox;
	private JLabel fireProtectionAssigneeName;
	private JLabel fireProtectionHoursAssignedLabel;
	private JLabel fireProtectionWorkRateLabel;
	private JLabel fireProtectionDateCompletedLabel;
	private JLabel fireProtectionBackcheckLabel;
	
	private JPanel electricalPersonnelPane;
	private Component electricalVerticalStrut;
	private Component electricalHorzizontalStrut;
	private Component electricalHorzizontalStrut_2;
	private JLabel electricalTitleLabel;
	private JLabel electricalFillerLabel;
	private JLabel electricalAssigneeTitleLabel;
	private JLabel electricalHoursAssingedTitleLabel;
	private JLabel electricalWorkRateTitleLabel;
	private JLabel electricalDateCompleteTitleLabel;
	private JLabel electricalBackcheckTitleLabel;
	private JCheckBox electricalAssigneeCheckBox;
	private JLabel electricalAssigneeName;
	private JLabel electricalHoursAssignedLabel;
	private JLabel electricalWorkRateLabel;
	private JLabel electricalDateCompletedLabel;
	private JLabel electricalBackcheckLabel;
	
	private JPanel communicationsPersonnelPane;
	private Component communicationsVerticalStrut;
	private Component communicationsHorzizontalStrut;
	private Component communicationsHorzizontalStrut_2;
	private JLabel communicationsTitleLabel;
	private JLabel communicationsFillerLabel;
	private JLabel communicationsAssigneeTitleLabel;
	private JLabel communicationsHoursAssingedTitleLabel;
	private JLabel communicationsWorkRateTitleLabel;
	private JLabel communicationsDateCompleteTitleLabel;
	private JLabel communicationsBackcheckTitleLabel;
	private JCheckBox communicationsAssigneeCheckBox;
	private JLabel communicationsAssigneeName;
	private JLabel communicationsHoursAssignedLabel;
	private JLabel communicationsWorkRateLabel;
	private JLabel communicationsDateCompletedLabel;
	private JLabel communicationsBackcheckLabel;
	
	private JPanel leedPersonnelPane;
	private Component leedVerticalStrut;
	private Component leedHorzizontalStrut;
	private Component leedHorzizontalStrut_2;
	private JLabel leedTitleLabel;
	private JLabel leedFillerLabel;
	private JLabel leedAssigneeTitleLabel;
	private JLabel leedHoursAssingedTitleLabel;
	private JLabel leedWorkRateTitleLabel;
	private JLabel leedDateCompleteTitleLabel;
	private JLabel leedBackcheckTitleLabel;
	private JCheckBox leedAssigneeCheckBox;
	private JLabel leedAssigneeName;
	private JLabel leedHoursAssignedLabel;
	private JLabel leedWorkRateLabel;
	private JLabel leedDateCompletedLabel;
	private JLabel leedBackcheckLabel;
	
	private JPanel environementalPersonnelPane;
	private Component environementalVerticalStrut;
	private Component environementalHorzizontalStrut;
	private Component environementalHorzizontalStrut_2;
	private JLabel environementalTitleLabel;
	private JLabel environementalFillerLabel;
	private JLabel environementalAssigneeTitleLabel;
	private JLabel environementalHoursAssingedTitleLabel;
	private JLabel environementalWorkRateTitleLabel;
	private JLabel environementalDateCompleteTitleLabel;
	private JLabel environementalBackcheckTitleLabel;
	private JCheckBox environementalAssigneeCheckBox;
	private JLabel environementalAssigneeName;
	private JLabel environementalHoursAssignedLabel;
	private JLabel environementalWorkRateLabel;
	private JLabel environementalDateCompletedLabel;
	private JLabel environementalBackcheckLabel;
	
	private JPanel costEngineerPersonnelPane;
	private Component costEngineerVerticalStrut;
	private Component costEngineerHorzizontalStrut;
	private Component costEngineerHorzizontalStrut_2;
	private JLabel costEngineerTitleLabel;
	private JLabel costEngineerFillerLabel;
	private JLabel costEngineerAssigneeTitleLabel;
	private JLabel costEngineerHoursAssingedTitleLabel;
	private JLabel costEngineerWorkRateTitleLabel;
	private JLabel costEngineerDateCompleteTitleLabel;
	private JLabel costEngineerBackcheckTitleLabel;
	private JCheckBox costEngineerAssigneeCheckBox;
	private JLabel costEngineerAssigneeName;
	private JLabel costEngineerHoursAssignedLabel;
	private JLabel costEngineerWorkRateLabel;
	private JLabel costEngineerDateCompletedLabel;
	private JLabel costEngineerBackcheckLabel;
	
	private JPanel geotechnicalPersonnelPane;
	private Component geotechnicalVerticalStrut;
	private Component geotechnicalHorzizontalStrut;
	private Component geotechnicalHorzizontalStrut_2;
	private JLabel geotechnicalTitleLabel;
	private JLabel geotechnicalFillerLabel;
	private JLabel geotechnicalAssigneeTitleLabel;
	private JLabel geotechnicalHoursAssingedTitleLabel;
	private JLabel geotechnicalWorkRateTitleLabel;
	private JLabel geotechnicalDateCompleteTitleLabel;
	private JLabel geotechnicalBackcheckTitleLabel;
	private JCheckBox geotechnicalAssigneeCheckBox;
	private JLabel geotechnicalAssigneeName;
	private JLabel geotechnicalHoursAssignedLabel;
	private JLabel geotechnicalWorkRateLabel;
	private JLabel geotechnicalDateCompletedLabel;
	private JLabel geotechnicalBackcheckLabel;
	
	private JPanel aeContractingPersonnelPane;
	private Component aeContractingVerticalStrut;
	private Component aeContractingHorzizontalStrut;
	private Component aeContractingHorzizontalStrut_2;
	private JLabel aeContractingTitleLabel;
	private JLabel aeContractingFillerLabel;
	private JLabel aeContractingAssigneeTitleLabel;
	private JLabel aeContractingHoursAssingedTitleLabel;
	private JLabel aeContractingWorkRateTitleLabel;
	private JLabel aeContractingDateCompleteTitleLabel;
	private JLabel aeContractingBackcheckTitleLabel;
	private JCheckBox aeContractingAssigneeCheckBox;
	private JLabel aeContractingAssigneeName;
	private JLabel aeContractingHoursAssignedLabel;
	private JLabel aeContractingWorkRateLabel;
	private JLabel aeContractingDateCompletedLabel;
	private JLabel aeContractingBackcheckLabel;
	
	private JPanel valueEngineerPersonnelPane;
	private Component valueEngineerVerticalStrut;
	private Component valueEngineerHorzizontalStrut;
	private Component valueEngineerHorzizontalStrut_2;
	private JLabel valueEngineerTitleLabel;
	private JLabel valueEngineerFillerLabel;
	private JLabel valueEngineerAssigneeTitleLabel;
	private JLabel valueEngineerHoursAssingedTitleLabel;
	private JLabel valueEngineerWorkRateTitleLabel;
	private JLabel valueEngineerDateCompleteTitleLabel;
	private JLabel valueEngineerBackcheckTitleLabel;
	private JCheckBox valueEngineerAssigneeCheckBox;
	private JLabel valueEngineerAssigneeName;
	private JLabel valueEngineerHoursAssignedLabel;
	private JLabel valueEngineerWorkRateLabel;
	private JLabel valueEngineerDateCompletedLabel;
	private JLabel valueEngineerBackcheckLabel;
	
	private JPanel translatorPersonnelPane;
	private Component translatorVerticalStrut;
	private Component translatorHorzizontalStrut;
	private Component translatorHorzizontalStrut_2;
	private JLabel translatorTitleLabel;
	private JLabel translatorFillerLabel;
	private JLabel translatorAssigneeTitleLabel;
	private JLabel translatorHoursAssingedTitleLabel;
	private JLabel translatorWorkRateTitleLabel;
	private JLabel translatorDateCompleteTitleLabel;
	private JLabel translatorBackcheckTitleLabel;
	private JCheckBox translatorAssigneeCheckBox;
	private JLabel translatorAssigneeName;
	private JLabel translatorHoursAssignedLabel;
	private JLabel translatorWorkRateLabel;
	private JLabel translatorDateCompletedLabel;
	private JLabel translatorBackcheckLabel;
	
	private JPanel specificationsPersonnelPane;
	private Component specificationsVerticalStrut;
	private Component specificationsHorzizontalStrut;
	private Component specificationsHorzizontalStrut_2;
	private JLabel specificationsTitleLabel;
	private JLabel specificationsFillerLabel;
	private JLabel specificationsAssigneeTitleLabel;
	private JLabel specificationsHoursAssingedTitleLabel;
	private JLabel specificationsWorkRateTitleLabel;
	private JLabel specificationsDateCompleteTitleLabel;
	private JLabel specificationsBackcheckTitleLabel;
	private JCheckBox specificationsAssigneeCheckBox;
	private JLabel specificationsAssigneeName;
	private JLabel specificationsHoursAssignedLabel;
	private JLabel specificationsWorkRateLabel;
	private JLabel specificationsDateCompletedLabel;
	private JLabel specificationsBackcheckLabel;
	
	private JPanel otherPersonnelPane;
	private Component otherVerticalStrut;
	private Component otherHorzizontalStrut;
	private Component otherHorzizontalStrut_2;
	private JLabel otherTitleLabel;
	private JLabel otherFillerLabel;
	private JLabel otherAssigneeTitleLabel;
	private JLabel otherHoursAssingedTitleLabel;
	private JLabel otherWorkRateTitleLabel;
	private JLabel otherDateCompleteTitleLabel;
	private JLabel otherBackcheckTitleLabel;
	private JCheckBox otherAssigneeCheckBox;
	private JLabel otherAssigneeName;
	private JLabel otherHoursAssignedLabel;
	private JLabel otherWorkRateLabel;
	private JLabel otherDateCompletedLabel;
	private JLabel otherBackcheckLabel;
	private Component horizontalStrut_4;
	private JScrollPane historyScrollPane;
	private JPanel historyObjectHolderPane;
	private JPanel historyObjectTitlePane;
	private JPanel object1_historyObjectPane;
	private JLabel modDateTimeTitleLabel;
	private JLabel oldValueTitleLabel;
	private JLabel newValueTitleLabel;
	private JLabel modByTitleLabel;
	private Component historyObjectTitleHorizontalStrut;
	private Component historyObjectTitleVerticalStrut_2;
	private Component historyObjectTitleVerticalStrut;
	private JLabel object1_dateModifiedLabel;
	private JLabel object1_timeModifiedLabel;
	private JTextArea object1_OldValueTextArea;
	private JTextArea object1_NewValueTextArea_1;
	private JLabel object1_ModifiedByLabel;
	private JLabel object1_fillerLabel;
	private JScrollPane object1_OldValueScrollPane;
	private JScrollPane object1_NewValueScrollPane;
	private Component object1_horizontalStrut;
	private Component object1_horizontalStrut_2;
	private Component object1_verticalStrut;
	private Component object1_verticalStrut_2;
	
	private JPanel object2_historyObjectPane;
	private Component object2_horizontalStrut;
	private JLabel object2_fillerLabel;
	private Component object2_horizontalStrut_2;
	private Component object2_verticalStrut;
	private JLabel object2_dateModifiedLabel;
	private JScrollPane object2_OldValueScrollPane;
	private JTextArea object2_OldValueTextArea;
	private JScrollPane object2_NewValueScrollPane;
	private JTextArea object2_NewValueTextArea_1;
	private JLabel object2_ModifiedByLabel;
	private Component object2_verticalStrut_2;
	private JLabel object2_timeModifiedLabel;
	
	private JPanel object3_historyObjectPane;
	private Component object3_horizontalStrut;
	private JLabel object3_fillerLabel;
	private Component object3_horizontalStrut_2;
	private Component object3_verticalStrut;
	private JLabel object3_dateModifiedLabel;
	private JScrollPane object3_OldValueScrollPane;
	private JTextArea object3_OldValueTextArea;
	private JScrollPane object3_NewValueScrollPane;
	private JTextArea object3_NewValueTextArea_1;
	private JLabel object3_ModifiedByLabel;
	private Component object3_verticalStrut_2;
	private JLabel object3_timeModifiedLabel;
	
	private JPanel object4_historyObjectPane;
	private Component object4_horizontalStrut;
	private JLabel object4_fillerLabel;
	private Component object4_horizontalStrut_2;
	private Component object4_verticalStrut;
	private JLabel object4_dateModifiedLabel;
	private JScrollPane object4_OldValueScrollPane;
	private JTextArea object4_OldValueTextArea;
	private JScrollPane object4_NewValueScrollPane;
	private JTextArea object4_NewValueTextArea_1;
	private JLabel object4_ModifiedByLabel;
	private Component object4_verticalStrut_2;
	private JLabel object4_timeModifiedLabel;
	
	private JPanel object5_historyObjectPane;
	private Component object5_horizontalStrut;
	private JLabel object5_fillerLabel;
	private Component object5_horizontalStrut_2;
	private Component object5_verticalStrut;
	private JLabel object5_dateModifiedLabel;
	private JScrollPane object5_OldValueScrollPane;
	private JTextArea object5_OldValueTextArea;
	private JScrollPane object5_NewValueScrollPane;
	private JTextArea object5_NewValueTextArea_1;
	private JLabel object5_ModifiedByLabel;
	private Component object5_verticalStrut_2;
	private JLabel object5_timeModifiedLabel;
	private JPanel workRequestSelectionPane;
	private JTable workRequestsSelectionTable;
	private Component workRequestSelectionStrut;
	private Component workRequestSelectionStrut_1;
	private Component workRequestVertSelectionStrut;
	private Component workRequestVertSelectionStrut_1;
	private JScrollPane scrollPane_7;	
	private JPanel analysisPieChartPane;
	private JLabel analysisWRIDLabel;
	private Component horizontalStrut_17;
	private Component verticalStrut_12;
	private JLabel analysisPMLabel;
	private JLabel analysisStartDateLabel;
	private JLabel analysisSupervisorLabel;
	private JTextField analysisWRIDTextField;
	private JTextField analysisPMTextField;
	private JTextField analysisSupervisorTextField;
	private JTextField analysisStartDateFieldText;
	private JLabel analysisSubmissionDateLabel;
	private JTextField analysisSubmissionDateTextField;
	private JPanel analysisTimeLinePanel;
	private JPanel analyisOtherPanel;
	private Component horizontalStrut_18;
	private Component verticalStrut_13;
	private Component verticalStrut_14;
	private Component verticalStrut_15;
	private Component verticalStrut_16;
}