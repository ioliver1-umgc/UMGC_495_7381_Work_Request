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
import java.io.File;
import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
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
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.BoxLayout;
import javax.swing.JSpinner;
import javax.swing.border.EtchedBorder;
import javax.swing.table.DefaultTableModel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.data.gantt.Task;
import org.jfree.data.gantt.TaskSeries;
import org.jfree.data.gantt.TaskSeriesCollection;
import org.jfree.data.general.DefaultPieDataset;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;

public class MainWindow extends SQLHandler
{
	public static JFrame frmWorkRequestApplication;
	public static ArrayList<Map<String, Object>> workRequests;
	public SortedMap<String, Object> sendMap;
	public static int selectedRow;
	static ChartPanel pieChartPanel;
	Vector<WRStatusIDItem> wrStatusIDVector;


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
					MainWindow.frmWorkRequestApplication.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	//No data from the login window, default to local settings
	public MainWindow() 
	{	
		this(loginInfo = "jdbc:sqlserver://localhost;databaseName=Pulse;sa;1234567890", 0, sqlConnectionOpen());		
	}
	
	//Received login data
	public MainWindow(String loginInfo, int connectionType, Connection connection) 
	{	
		//store default sql connection info
		MainWindow.connectionType = connectionType;
		MainWindow.connection = connection;
		MainWindow.loginInfo = loginInfo;
		
		//Generate internal data structure
		workRequests = new ArrayList<Map<String, Object>>();
		sendMap = new TreeMap<String, Object>();
		wrStatusIDVector = new Vector<WRStatusIDItem>();
		
		//setup vector for use in WRStatusID ComboBox
		wrStatusIDVector.addElement(new WRStatusIDItem("Not submitted", 1));
		wrStatusIDVector.addElement(new WRStatusIDItem("Assigned to PA", 2));
		wrStatusIDVector.addElement(new WRStatusIDItem("Pending PM Submission", 3));
		wrStatusIDVector.addElement(new WRStatusIDItem("Assigned to ECES", 4));
		wrStatusIDVector.addElement(new WRStatusIDItem("In Progress", 5));
		wrStatusIDVector.addElement(new WRStatusIDItem("Pending ECES Technical Completion", 6));
		wrStatusIDVector.addElement(new WRStatusIDItem("Returned to Requester", 7));
		wrStatusIDVector.addElement(new WRStatusIDItem("Closed", 8));
		wrStatusIDVector.addElement(new WRStatusIDItem("Pending PM Completion", 9));
		wrStatusIDVector.addElement(new WRStatusIDItem("Pending ECES Financial Completion", 10));
		wrStatusIDVector.addElement(new WRStatusIDItem("Marked Deleted", 11));
		
		//create the GUI
		createGui();
		
		//get data from sql server
		try 
		{
			workRequests = getDataFromSQL(connection);
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("didn't grab wrISUD");
		}
		
		//create/update selection table
		makeSelectionTable();
		
		//fillout GUI from record
		fillGUI(selectedRow);
	}
	
	
	//save data in field to internal data structure
	public boolean saveWRtoMap()
	{
		boolean saved = false;
		
		sendMap.put("WRNumber", newWRNumTextField.getText());
		sendMap.put("WRStatusID", ((WRStatusIDItem) newWRStatusComboBox.getSelectedItem()).getInt());
		sendMap.put("DatePrepared",newDatePrepTextField.getText());
		sendMap.put("DraftDueDate",newDraftDueDateTextField.getText());
		sendMap.put("Requestor",newRequesterTextField.getText());
		sendMap.put("ProjectManager",newProjectManagerTextField.getText());
		sendMap.put("Supervisor",newSupervisorTextField.getText());
		sendMap.put("ProjectName",newProjectNameTextField.getText());
		sendMap.put("ProjectNumber",newProjectNumberSpinner.getValue());
		sendMap.put("ProgramYear",newProgramYearTextField.getText());
		sendMap.put("FundSourceText",newFundSourceTextField.getValue());
		sendMap.put("StartDate",newStartDateTextField.getText());
		sendMap.put("CompletionDate",newCompletionDateTextField.getText());
		sendMap.put("WRID", (int) workRequests.get(workRequests.size()-1).get("WRID") + 1);
		sendMap.put("ProjectInfoID", (int) workRequests.get(workRequests.size()-1).get("ProjectInfoID") + 1);
		
		if(sendMap.get("WRNumber") == newWRNumTextField.getText())
		{
			saved = true;
			System.out.println("Data saved to sendMap...");
		}
		
		return saved;
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
	
	private static void generatePieChartJPEG()
	{
		if(selectedRow < 0 || selectedRow > workRequests.size())
			selectedRow = 0;
		
		DefaultPieDataset<String> dataset = new DefaultPieDataset<String>();
		
		dataset.setValue("Civil",(double) workRequests.get(selectedRow).get("costHoursCivil"));
		dataset.setValue("Architectural",(double)workRequests.get(selectedRow).get("costHoursArchitectural"));
		dataset.setValue("Structural",(double)workRequests.get(selectedRow).get("costHoursStructural"));
		dataset.setValue("ForceProtection",(double)workRequests.get(selectedRow).get("costHoursForceProtection"));
		dataset.setValue("Mechanical",(double)workRequests.get(selectedRow).get("costHoursMechanical"));
		dataset.setValue("FireProtection",(double)workRequests.get(selectedRow).get("costHoursFireProtection"));
		dataset.setValue("Electrical",(double)workRequests.get(selectedRow).get("costHoursElectrical"));
		dataset.setValue("Communications",(double)workRequests.get(selectedRow).get("costHoursCommunications"));
		dataset.setValue("LEED",(double)workRequests.get(selectedRow).get("costHoursLEED"));
		dataset.setValue("Environmental",(double)workRequests.get(selectedRow).get("costHoursEnvironmental"));
		dataset.setValue("CostEngineer",(double)workRequests.get(selectedRow).get("costHoursCostEngineer"));
		dataset.setValue("Geotechnical",(double)workRequests.get(selectedRow).get("costHoursGeotechnical"));
		dataset.setValue("AEContracting",(double)workRequests.get(selectedRow).get("costHoursAEContracting"));
		dataset.setValue("ValueEngineer",(double)workRequests.get(selectedRow).get("costHoursValueEngineer"));
		dataset.setValue("Translator",(double)workRequests.get(selectedRow).get("costHoursTranslator"));
		dataset.setValue("Specifications",(double)workRequests.get(selectedRow).get("costHoursSpecifications"));
		dataset.setValue("Other",(double)workRequests.get(selectedRow).get("costHoursOther"));
			
		JFreeChart chart = ChartFactory.createPieChart(
				"Designated Hours Working", //chart title
				dataset,					//dataset
				true,						//include legend
				true,						//include tooltip
				false);						//exclude urls
		
		int width = 560;
		int height = 370;
		File pieChart = new File("PieChart.jpeg");
		
		try {
			ChartUtils.saveChartAsJPEG( pieChart , chart , width , height );
			System.out.println("PieChart JPEG Made...");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private void generateGnattChartJPEG()
	{   
		//First Task Series for Gnatt
		TaskSeries series1 = new TaskSeries("Estimated Date"); 
		series1.add(new Task("Requirement",   Date.from(LocalDate.of(2021,7,3).atStartOfDay().toInstant(ZoneOffset.UTC)),  
		Date.from(LocalDate.of(2021, 7,7).atStartOfDay().toInstant(ZoneOffset.UTC))));  
	           
	    series1.add(new Task("Design",Date.from(LocalDate.of(2021, 7,10).atStartOfDay().toInstant(ZoneOffset.UTC)),  
	    Date.from(LocalDate.of(2021, 7, 14).atStartOfDay().toInstant(ZoneOffset.UTC))));  
	           
	    series1.add(new Task("Coding",Date.from(LocalDate.of(2021, 7,17).atStartOfDay().toInstant(ZoneOffset.UTC)),  
	    Date.from(LocalDate.of(2021, 7, 21).atStartOfDay().toInstant(ZoneOffset.UTC))));  
	           
	    series1.add(new Task("Testing", Date.from(LocalDate.of(2021, 7,24).atStartOfDay().toInstant(ZoneOffset.UTC)),  
	    Date.from(LocalDate.of(2021, 7, 28).atStartOfDay().toInstant(ZoneOffset.UTC))));  
	           
	    series1.add(new Task("Deployment", Date.from(LocalDate.of(2021, 07,31).atStartOfDay().toInstant(ZoneOffset.UTC)),  
	    Date.from(LocalDate.of(2021, 8, 4).atStartOfDay().toInstant(ZoneOffset.UTC))));  
	           
	    //Second task series for Gnatt   
		TaskSeries series2 = new TaskSeries("Actual Date");  
		series2.add(new Task("Requirement",Date.from(LocalDate.of(2021, 7,3).atStartOfDay().toInstant(ZoneOffset.UTC)),  
		Date.from(LocalDate.of(2021, 7, 05).atStartOfDay().toInstant(ZoneOffset.UTC))));  
		           
		series2.add(new Task("Design",  
		Date.from(LocalDate.of(2021, 7, 6).atStartOfDay().toInstant(ZoneOffset.UTC)),  
		Date.from(LocalDate.of(2021, 7, 17).atStartOfDay().toInstant(ZoneOffset.UTC))));  
		       
		series2.add(new Task("Coding",  
		Date.from(LocalDate.of(2021, 7, 18).atStartOfDay().toInstant(ZoneOffset.UTC)),  
		Date.from(LocalDate.of(2021, 7, 27).atStartOfDay().toInstant(ZoneOffset.UTC))));  
		       
		series2.add(new Task("Testing",  
		Date.from(LocalDate.of(2021, 7, 28).atStartOfDay().toInstant(ZoneOffset.UTC)),  
		Date.from(LocalDate.of(2021, 8, 1).atStartOfDay().toInstant(ZoneOffset.UTC))));  
		       
		series2.add(new Task("Deployment",  
		Date.from(LocalDate.of(2021, 8, 2).atStartOfDay().toInstant(ZoneOffset.UTC)),  
		Date.from(LocalDate.of(2021, 8, 4).atStartOfDay().toInstant(ZoneOffset.UTC))));  
		 
		TaskSeriesCollection dataset = new TaskSeriesCollection();  
		dataset.add(series1);
		dataset.add(series2); 
		
		// Create chart  
		JFreeChart chart = ChartFactory.createGanttChart(  
				"Gantt 2021 example", 	// Chart title  
				"Software Dev Phases",			// X-Axis Label  
				"Timeline", 					// Y-Axis Label  
				dataset);  
		
		int width = 415;
		int height = 220;
		File pieChart = new File("GnattChart.jpeg");
		
		try {
			ChartUtils.saveChartAsJPEG( pieChart , chart , width , height );
			System.out.println("Gnatt JPEG Made...");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	        
	}
	
	//Quick check for null to "null" for display purposes
	private Object checkData(Object o)
	{	
		if(o == null)
			return "null";
		
		return o;
	}
	
	private void fillGUI(int index)
	{
		//check if index is valid, if not default to 0
		if(index < 0 || index > workRequests.size())
			index = 0;
		
		//Fill General Info
		viewWRNumTextField.setText(checkData(workRequests.get(index).get("WRNumber")).toString());
		viewDatePrepTextField.setText(checkData(workRequests.get(index).get("DatePrepared")).toString());
		viewDraftDueDateTextField.setText(checkData(workRequests.get(index).get("DraftDueDate")).toString());
		viewRequesterTextField.setText(checkData(workRequests.get(index).get("Requestor")).toString());
		viewProjectManagerTextField.setText(checkData(workRequests.get(index).get("ProjectManager")).toString());
		viewSupervisorTextField.setText(checkData(workRequests.get(index).get("Supervisor")).toString());
		viewProjectNameTextField.setText(checkData(workRequests.get(index).get("ProjectName")).toString());
		viewProjectNumberTextField.setText(checkData(workRequests.get(index).get("ProjectNumber")).toString());
		viewProgramYearTextField.setText(checkData(workRequests.get(index).get("ProgramYear")).toString());
		viewFundSourceTextField.setText(checkData(workRequests.get(index).get("FundSourceText")).toString());
		viewStartDateTextField.setText(checkData(workRequests.get(index).get("StartDate")).toString());
		viewCompletionDateTextField.setText(checkData(workRequests.get(index).get("CompletionDate")).toString());
		
		//analyis tab
		analysisWRNumberTextField.setText(checkData(workRequests.get(index).get("WRNumber")).toString());
		analysisPMTextField.setText(checkData(workRequests.get(index).get("ProjectManager")).toString());
		analysisSupervisorTextField.setText(checkData(workRequests.get(index).get("Supervisor")).toString());
		analysisDraftDueDateTextField.setText(checkData(workRequests.get(index).get("DraftDueDate")).toString());
		analysisStartDateFieldText.setText(checkData(workRequests.get(index).get("StartDate")).toString());
		
		//Fill Cost Dist
		viewCivillSpinner.setValue(checkData(workRequests.get(index).get("costHoursCivil")));
		viewArchitecturalSpinner.setValue(checkData(workRequests.get(index).get("costHoursArchitectural")));
		viewStructuralSpinner.setValue(checkData(workRequests.get(index).get("costHoursStructural")));
		viewForceProtSpinner.setValue(checkData(workRequests.get(index).get("costHoursForceProtection")));
		viewMechSpinner.setValue(checkData(workRequests.get(index).get("costHoursMechanical")));
		viewFireProtSpinner.setValue(checkData(workRequests.get(index).get("costHoursFireProtection")));
		viewElectricSpinner.setValue(checkData(workRequests.get(index).get("costHoursElectrical")));
		viewCommSpinner.setValue(checkData(workRequests.get(index).get("costHoursCommunications")));
		viewLEEDSpinner.setValue(checkData(workRequests.get(index).get("costHoursLEED")));
		viewEnvinronmentalSpinner.setValue(checkData(workRequests.get(index).get("costHoursEnvironmental")));
		viewCostEngSpinner.setValue(checkData(workRequests.get(index).get("costHoursCostEngineer")));
		viewGeotechSpinner.setValue(checkData(workRequests.get(index).get("costHoursGeotechnical")));
		viewAEContSpinner.setValue(checkData(workRequests.get(index).get("costHoursAEContracting")));
		viewValEngSpinner.setValue(checkData(workRequests.get(index).get("costHoursValueEngineer")));
		viewTranslatorSpinner.setValue(checkData(workRequests.get(index).get("costHoursTranslator")));
		viewSpecificationSpinner.setValue(checkData(workRequests.get(index).get("costHoursSpecifications")));
		viewOtherSpinner.setValue(checkData(workRequests.get(index).get("costHoursOther")));
		
		viewCivilCostLabel.setText(checkData(workRequests.get(index).get("costCivil")).toString());
		viewArchCostLabel.setText(checkData(workRequests.get(index).get("costArchitectural")).toString());
		viewStructCostLabel.setText(checkData(workRequests.get(index).get("costStructural")).toString());
		viewForceProtCostLabel.setText(checkData(workRequests.get(index).get("costForceProtection")).toString());
		viewMechCostLabel.setText(checkData(workRequests.get(index).get("costMechanical")).toString());
		viewFireProtCostLabel.setText(checkData(workRequests.get(index).get("costFireProtection")).toString());
		viewElectricalCostLabel.setText(checkData(workRequests.get(index).get("costElectrical")).toString());
		viewCommsCostLabel.setText(checkData(workRequests.get(index).get("costCommunications")).toString());
		viewLEEDCostLabel.setText(checkData(workRequests.get(index).get("costLEED")).toString());
		viewEnvCostLabel.setText(checkData(workRequests.get(index).get("costEnvironmental")).toString());
		viewCostEngCostLabel.setText(checkData(workRequests.get(index).get("costCostEngineer")).toString());
		viewGeotechCostLabel.setText(checkData(workRequests.get(index).get("costGeotechnical")).toString());
		viewAEContractingLabel.setText(checkData(workRequests.get(index).get("costAEContracting")).toString());
		viewValueEngCostLabel.setText(checkData(workRequests.get(index).get("costValueEngineer")).toString());
		viewTranslatorCostLabel.setText(checkData(workRequests.get(index).get("costTranslator")).toString());
		viewSpecCostLabel.setText(checkData(workRequests.get(index).get("costSpecifications")).toString());
		viewOtherCostLabel.setText(checkData(workRequests.get(index).get("costOther")).toString());
		
		generatePieChartJPEG();
		generateGnattChartJPEG();
		
		//System.out.println("Index::" + index + "WRNum::-" + viewWRNumTextField.getText() + "What it should be: " + checkData(workRequests.get(index).get("WRNumber")).toString());
		MainWindow.frmWorkRequestApplication.invalidate();
		MainWindow.frmWorkRequestApplication.validate();
		MainWindow.frmWorkRequestApplication.repaint();
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
        viewWorkRequestsPane.addTab("Analytics", null, requestAnalyticsPane, null);
        GridBagLayout gbl_requestAnalyticsPane = new GridBagLayout();
        gbl_requestAnalyticsPane.columnWidths = new int[]{0, 120, 344, 0, 90, 88, 88, 88, 89, 0, 0};
        gbl_requestAnalyticsPane.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 42, 48, 38, 0};
        gbl_requestAnalyticsPane.columnWeights = new double[]{0.0, 0.0, 1.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
        gbl_requestAnalyticsPane.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
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
        
        analysisWRNumberLabel = new JLabel("Work Request Number");
        analysisWRNumberLabel.setHorizontalAlignment(SwingConstants.TRAILING);
        GridBagConstraints gbc_analysisWRNumberLabel = new GridBagConstraints();
        gbc_analysisWRNumberLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_analysisWRNumberLabel.insets = new Insets(0, 0, 5, 5);
        gbc_analysisWRNumberLabel.gridx = 1;
        gbc_analysisWRNumberLabel.gridy = 1;
        requestAnalyticsPane.add(analysisWRNumberLabel, gbc_analysisWRNumberLabel);
        
        analysisWRNumberTextField = new JTextField();
        GridBagConstraints gbc_analysisWRNumberTextField = new GridBagConstraints();
        gbc_analysisWRNumberTextField.insets = new Insets(0, 0, 5, 5);
        gbc_analysisWRNumberTextField.fill = GridBagConstraints.HORIZONTAL;
        gbc_analysisWRNumberTextField.gridx = 2;
        gbc_analysisWRNumberTextField.gridy = 1;
        requestAnalyticsPane.add(analysisWRNumberTextField, gbc_analysisWRNumberTextField);
        analysisWRNumberTextField.setColumns(10);
        
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
        analysisPieChartPane.setLayout(new BorderLayout(0, 0));
        
        pieChartLabel = new JLabel("");
        analysisPieChartPane.add(pieChartLabel, BorderLayout.CENTER);
        pieChartLabel.setIcon(new ImageIcon("PieChart.jpeg"));
                              
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
        
        analysisDraftDueDateLabel = new JLabel("Draft Due Date");
        analysisDraftDueDateLabel.setHorizontalAlignment(SwingConstants.TRAILING);
        GridBagConstraints gbc_analysisDraftDueDateLabel = new GridBagConstraints();
        gbc_analysisDraftDueDateLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_analysisDraftDueDateLabel.insets = new Insets(0, 0, 5, 5);
        gbc_analysisDraftDueDateLabel.gridx = 1;
        gbc_analysisDraftDueDateLabel.gridy = 4;
        requestAnalyticsPane.add(analysisDraftDueDateLabel, gbc_analysisDraftDueDateLabel);
        
        analysisDraftDueDateTextField = new JTextField();
        GridBagConstraints gbc_analysisDraftDueDateTextField = new GridBagConstraints();
        gbc_analysisDraftDueDateTextField.insets = new Insets(0, 0, 5, 5);
        gbc_analysisDraftDueDateTextField.fill = GridBagConstraints.HORIZONTAL;
        gbc_analysisDraftDueDateTextField.gridx = 2;
        gbc_analysisDraftDueDateTextField.gridy = 4;
        requestAnalyticsPane.add(analysisDraftDueDateTextField, gbc_analysisDraftDueDateTextField);
        analysisDraftDueDateTextField.setColumns(10);
        
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
        analyisOtherPanel.setLayout(new BorderLayout(0, 0));
        
        gnattChartLabel = new JLabel("");
        analyisOtherPanel.add(gnattChartLabel, BorderLayout.CENTER);
        gnattChartLabel.setIcon(new ImageIcon("GnattChart.jpeg"));
        
        verticalStrut_15 = Box.createVerticalStrut(20);
        GridBagConstraints gbc_verticalStrut_15 = new GridBagConstraints();
        gbc_verticalStrut_15.gridwidth = 2;
        gbc_verticalStrut_15.insets = new Insets(0, 0, 0, 5);
        gbc_verticalStrut_15.gridx = 0;
        gbc_verticalStrut_15.gridy = 11;
        requestAnalyticsPane.add(verticalStrut_15, gbc_verticalStrut_15);
        
        
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
        saveRequestBtn.addActionListener(workRequestAction);
        GridBagConstraints gbc_saveRequestBtn = new GridBagConstraints();
        gbc_saveRequestBtn.insets = new Insets(0, 0, 5, 5);
        gbc_saveRequestBtn.gridx = 2;
        gbc_saveRequestBtn.gridy = 3;
        remarkSavePane.add(saveRequestBtn, gbc_saveRequestBtn);
        
        submitWorkRequestBtn = new JButton("Submit to PA");
        submitWorkRequestBtn.addActionListener(workRequestAction);
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
        gbl_generalInfoPane.columnWidths = new int[]{0, 0, 0, 243, 0, 0, 0};
        gbl_generalInfoPane.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
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
        
        newWRNumberLabel = new JLabel("Work Request Number");
        GridBagConstraints gbc_newWRNumberLabel = new GridBagConstraints();
        gbc_newWRNumberLabel.anchor = GridBagConstraints.EAST;
        gbc_newWRNumberLabel.insets = new Insets(0, 0, 5, 5);
        gbc_newWRNumberLabel.gridx = 2;
        gbc_newWRNumberLabel.gridy = 1;
        generalInfoPane.add(newWRNumberLabel, gbc_newWRNumberLabel);
        
        newWRNumTextField = new JTextField();
        GridBagConstraints gbc_newWRNumTextField = new GridBagConstraints();
        gbc_newWRNumTextField.gridwidth = 2;
        gbc_newWRNumTextField.insets = new Insets(0, 0, 5, 5);
        gbc_newWRNumTextField.fill = GridBagConstraints.HORIZONTAL;
        gbc_newWRNumTextField.gridx = 3;
        gbc_newWRNumTextField.gridy = 1;
        generalInfoPane.add(newWRNumTextField, gbc_newWRNumTextField);
        newWRNumTextField.setColumns(10);
        
        lblWorkRequestStatus = new JLabel("Work Request Status");
        GridBagConstraints gbc_lblWorkRequestStatus = new GridBagConstraints();
        gbc_lblWorkRequestStatus.anchor = GridBagConstraints.EAST;
        gbc_lblWorkRequestStatus.insets = new Insets(0, 0, 5, 5);
        gbc_lblWorkRequestStatus.gridx = 2;
        gbc_lblWorkRequestStatus.gridy = 2;
        generalInfoPane.add(lblWorkRequestStatus, gbc_lblWorkRequestStatus);
        
        newWRStatusComboBox = new JComboBox(wrStatusIDVector);
        GridBagConstraints gbc_newWRStatusComboBox = new GridBagConstraints();
        gbc_newWRStatusComboBox.gridwidth = 2;
        gbc_newWRStatusComboBox.insets = new Insets(0, 0, 5, 5);
        gbc_newWRStatusComboBox.fill = GridBagConstraints.HORIZONTAL;
        gbc_newWRStatusComboBox.gridx = 3;
        gbc_newWRStatusComboBox.gridy = 2;
        generalInfoPane.add(newWRStatusComboBox, gbc_newWRStatusComboBox);
        
        newDatePreparedLabel = new JLabel("Date Prepared");
        GridBagConstraints gbc_newDatePreparedLabel = new GridBagConstraints();
        gbc_newDatePreparedLabel.anchor = GridBagConstraints.EAST;
        gbc_newDatePreparedLabel.insets = new Insets(0, 0, 5, 5);
        gbc_newDatePreparedLabel.gridx = 2;
        gbc_newDatePreparedLabel.gridy = 3;
        generalInfoPane.add(newDatePreparedLabel, gbc_newDatePreparedLabel);
        
        newDatePrepTextField = new JTextField();
        newDatePrepTextField.setText("yyyy-mm-dd hh:mm:ss");
        newDatePrepTextField.setColumns(10);
        GridBagConstraints gbc_newDatePrepTextField = new GridBagConstraints();
        gbc_newDatePrepTextField.gridwidth = 2;
        gbc_newDatePrepTextField.insets = new Insets(0, 0, 5, 5);
        gbc_newDatePrepTextField.fill = GridBagConstraints.HORIZONTAL;
        gbc_newDatePrepTextField.gridx = 3;
        gbc_newDatePrepTextField.gridy = 3;
        generalInfoPane.add(newDatePrepTextField, gbc_newDatePrepTextField);
        
        newDraftDueDateLabel = new JLabel("Draft Due Date");
        GridBagConstraints gbc_newDraftDueDateLabel = new GridBagConstraints();
        gbc_newDraftDueDateLabel.anchor = GridBagConstraints.EAST;
        gbc_newDraftDueDateLabel.insets = new Insets(0, 0, 5, 5);
        gbc_newDraftDueDateLabel.gridx = 2;
        gbc_newDraftDueDateLabel.gridy = 4;
        generalInfoPane.add(newDraftDueDateLabel, gbc_newDraftDueDateLabel);
        
        newDraftDueDateTextField = new JTextField();
        newDraftDueDateTextField.setText("yyyy-mm-dd hh:mm:ss");
        newDraftDueDateTextField.setColumns(10);
        GridBagConstraints gbc_newDraftDueDateTextField = new GridBagConstraints();
        gbc_newDraftDueDateTextField.gridwidth = 2;
        gbc_newDraftDueDateTextField.insets = new Insets(0, 0, 5, 5);
        gbc_newDraftDueDateTextField.fill = GridBagConstraints.HORIZONTAL;
        gbc_newDraftDueDateTextField.gridx = 3;
        gbc_newDraftDueDateTextField.gridy = 4;
        generalInfoPane.add(newDraftDueDateTextField, gbc_newDraftDueDateTextField);
        
        lblRequester = new JLabel("Requester");
        GridBagConstraints gbc_lblRequester = new GridBagConstraints();
        gbc_lblRequester.anchor = GridBagConstraints.EAST;
        gbc_lblRequester.insets = new Insets(0, 0, 5, 5);
        gbc_lblRequester.gridx = 2;
        gbc_lblRequester.gridy = 5;
        generalInfoPane.add(lblRequester, gbc_lblRequester);
        
        newRequesterTextField = new JTextField();
        newRequesterTextField.setColumns(10);
        GridBagConstraints gbc_newRequesterTextField = new GridBagConstraints();
        gbc_newRequesterTextField.gridwidth = 2;
        gbc_newRequesterTextField.insets = new Insets(0, 0, 5, 5);
        gbc_newRequesterTextField.fill = GridBagConstraints.HORIZONTAL;
        gbc_newRequesterTextField.gridx = 3;
        gbc_newRequesterTextField.gridy = 5;
        generalInfoPane.add(newRequesterTextField, gbc_newRequesterTextField);
        
        lblProjectManager = new JLabel("Project Manager");
        GridBagConstraints gbc_lblProjectManager = new GridBagConstraints();
        gbc_lblProjectManager.anchor = GridBagConstraints.EAST;
        gbc_lblProjectManager.insets = new Insets(0, 0, 5, 5);
        gbc_lblProjectManager.gridx = 2;
        gbc_lblProjectManager.gridy = 6;
        generalInfoPane.add(lblProjectManager, gbc_lblProjectManager);
        
        newProjectManagerTextField = new JTextField();
        newProjectManagerTextField.setColumns(10);
        GridBagConstraints gbc_newProjectManagerTextField = new GridBagConstraints();
        gbc_newProjectManagerTextField.gridwidth = 2;
        gbc_newProjectManagerTextField.insets = new Insets(0, 0, 5, 5);
        gbc_newProjectManagerTextField.fill = GridBagConstraints.HORIZONTAL;
        gbc_newProjectManagerTextField.gridx = 3;
        gbc_newProjectManagerTextField.gridy = 6;
        generalInfoPane.add(newProjectManagerTextField, gbc_newProjectManagerTextField);
        
        newSupervisorLabel = new JLabel("Supervisor");
        GridBagConstraints gbc_newSupervisorLabel = new GridBagConstraints();
        gbc_newSupervisorLabel.anchor = GridBagConstraints.EAST;
        gbc_newSupervisorLabel.insets = new Insets(0, 0, 5, 5);
        gbc_newSupervisorLabel.gridx = 2;
        gbc_newSupervisorLabel.gridy = 7;
        generalInfoPane.add(newSupervisorLabel, gbc_newSupervisorLabel);
        
        newSupervisorTextField = new JTextField();
        newSupervisorTextField.setColumns(10);
        GridBagConstraints gbc_newSupervisorTextField = new GridBagConstraints();
        gbc_newSupervisorTextField.gridwidth = 2;
        gbc_newSupervisorTextField.insets = new Insets(0, 0, 5, 5);
        gbc_newSupervisorTextField.fill = GridBagConstraints.HORIZONTAL;
        gbc_newSupervisorTextField.gridx = 3;
        gbc_newSupervisorTextField.gridy = 7;
        generalInfoPane.add(newSupervisorTextField, gbc_newSupervisorTextField);
        
        newProjectNameLabel = new JLabel("Project Name");
        newProjectNameLabel.setHorizontalAlignment(SwingConstants.TRAILING);
        GridBagConstraints gbc_newProjectNameLabel = new GridBagConstraints();
        gbc_newProjectNameLabel.anchor = GridBagConstraints.EAST;
        gbc_newProjectNameLabel.insets = new Insets(0, 0, 5, 5);
        gbc_newProjectNameLabel.gridx = 2;
        gbc_newProjectNameLabel.gridy = 8;
        generalInfoPane.add(newProjectNameLabel, gbc_newProjectNameLabel);
        
        newProjectNameTextField = new JTextField();
        newProjectNameTextField.setColumns(10);
        GridBagConstraints gbc_newProjectNameTextField = new GridBagConstraints();
        gbc_newProjectNameTextField.gridwidth = 2;
        gbc_newProjectNameTextField.insets = new Insets(0, 0, 5, 5);
        gbc_newProjectNameTextField.fill = GridBagConstraints.HORIZONTAL;
        gbc_newProjectNameTextField.gridx = 3;
        gbc_newProjectNameTextField.gridy = 8;
        generalInfoPane.add(newProjectNameTextField, gbc_newProjectNameTextField);
        
        newProjectNumberLabel = new JLabel("Project Number");
        newProjectNumberLabel.setHorizontalAlignment(SwingConstants.TRAILING);
        GridBagConstraints gbc_newProjectNumberLabel = new GridBagConstraints();
        gbc_newProjectNumberLabel.anchor = GridBagConstraints.EAST;
        gbc_newProjectNumberLabel.insets = new Insets(0, 0, 5, 5);
        gbc_newProjectNumberLabel.gridx = 2;
        gbc_newProjectNumberLabel.gridy = 9;
        generalInfoPane.add(newProjectNumberLabel, gbc_newProjectNumberLabel);
        
        newProjectNumberSpinner = new JSpinner();
        GridBagConstraints gbc_newProjectNumberSpinner = new GridBagConstraints();
        gbc_newProjectNumberSpinner.gridwidth = 2;
        gbc_newProjectNumberSpinner.insets = new Insets(0, 0, 5, 5);
        gbc_newProjectNumberSpinner.fill = GridBagConstraints.HORIZONTAL;
        gbc_newProjectNumberSpinner.gridx = 3;
        gbc_newProjectNumberSpinner.gridy = 9;
        generalInfoPane.add(newProjectNumberSpinner, gbc_newProjectNumberSpinner);
        
        newProgramYearLabel = new JLabel("Program Year");
        newProgramYearLabel.setHorizontalAlignment(SwingConstants.TRAILING);
        GridBagConstraints gbc_newProgramYearLabel = new GridBagConstraints();
        gbc_newProgramYearLabel.anchor = GridBagConstraints.EAST;
        gbc_newProgramYearLabel.insets = new Insets(0, 0, 5, 5);
        gbc_newProgramYearLabel.gridx = 2;
        gbc_newProgramYearLabel.gridy = 10;
        generalInfoPane.add(newProgramYearLabel, gbc_newProgramYearLabel);
        
        newProgramYearTextField = new JTextField();
        newProgramYearTextField.setText("yyyy");
        newProgramYearTextField.setColumns(10);
        GridBagConstraints gbc_newProgramYearTextField = new GridBagConstraints();
        gbc_newProgramYearTextField.gridwidth = 2;
        gbc_newProgramYearTextField.insets = new Insets(0, 0, 5, 5);
        gbc_newProgramYearTextField.fill = GridBagConstraints.HORIZONTAL;
        gbc_newProgramYearTextField.gridx = 3;
        gbc_newProgramYearTextField.gridy = 10;
        generalInfoPane.add(newProgramYearTextField, gbc_newProgramYearTextField);
        
        newFundSourceLabel = new JLabel("Fund Source");
        newFundSourceLabel.setHorizontalAlignment(SwingConstants.TRAILING);
        GridBagConstraints gbc_newFundSourceLabel = new GridBagConstraints();
        gbc_newFundSourceLabel.anchor = GridBagConstraints.EAST;
        gbc_newFundSourceLabel.insets = new Insets(0, 0, 5, 5);
        gbc_newFundSourceLabel.gridx = 2;
        gbc_newFundSourceLabel.gridy = 11;
        generalInfoPane.add(newFundSourceLabel, gbc_newFundSourceLabel);
        
        newFundSourceTextField = new JSpinner();
        GridBagConstraints gbc_newFundSourceTextField = new GridBagConstraints();
        gbc_newFundSourceTextField.gridwidth = 2;
        gbc_newFundSourceTextField.insets = new Insets(0, 0, 5, 5);
        gbc_newFundSourceTextField.fill = GridBagConstraints.HORIZONTAL;
        gbc_newFundSourceTextField.gridx = 3;
        gbc_newFundSourceTextField.gridy = 11;
        generalInfoPane.add(newFundSourceTextField, gbc_newFundSourceTextField);
        
        newStartDateLabel = new JLabel("Start Date");
        GridBagConstraints gbc_newStartDateLabel = new GridBagConstraints();
        gbc_newStartDateLabel.anchor = GridBagConstraints.EAST;
        gbc_newStartDateLabel.insets = new Insets(0, 0, 5, 5);
        gbc_newStartDateLabel.gridx = 2;
        gbc_newStartDateLabel.gridy = 12;
        generalInfoPane.add(newStartDateLabel, gbc_newStartDateLabel);
        
        newStartDateTextField = new JTextField();
        newStartDateTextField.setText("yyyy-mm-dd hh:mm:ss");
        newStartDateTextField.setColumns(10);
        GridBagConstraints gbc_newStartDateTextField = new GridBagConstraints();
        gbc_newStartDateTextField.gridwidth = 2;
        gbc_newStartDateTextField.insets = new Insets(0, 0, 5, 5);
        gbc_newStartDateTextField.fill = GridBagConstraints.HORIZONTAL;
        gbc_newStartDateTextField.gridx = 3;
        gbc_newStartDateTextField.gridy = 12;
        generalInfoPane.add(newStartDateTextField, gbc_newStartDateTextField);
        
        newCompletionDateLabel = new JLabel("Completion Date");
        GridBagConstraints gbc_newCompletionDateLabel = new GridBagConstraints();
        gbc_newCompletionDateLabel.anchor = GridBagConstraints.EAST;
        gbc_newCompletionDateLabel.insets = new Insets(0, 0, 5, 5);
        gbc_newCompletionDateLabel.gridx = 2;
        gbc_newCompletionDateLabel.gridy = 13;
        generalInfoPane.add(newCompletionDateLabel, gbc_newCompletionDateLabel);
        
        newCompletionDateTextField = new JTextField();
        newCompletionDateTextField.setText("yyyy-mm-dd hh:mm:ss");
        newCompletionDateTextField.setColumns(10);
        GridBagConstraints gbc_newCompletionDateTextField = new GridBagConstraints();
        gbc_newCompletionDateTextField.gridwidth = 2;
        gbc_newCompletionDateTextField.insets = new Insets(0, 0, 5, 5);
        gbc_newCompletionDateTextField.fill = GridBagConstraints.HORIZONTAL;
        gbc_newCompletionDateTextField.gridx = 3;
        gbc_newCompletionDateTextField.gridy = 13;
        generalInfoPane.add(newCompletionDateTextField, gbc_newCompletionDateTextField);
        
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
        
        costDistroPane = new JPanel();
        newWorkRequestPane.addTab("Cost Distribution", null, costDistroPane, null);   	
        GridBagLayout gbl_costDistroPane = new GridBagLayout();
        gbl_costDistroPane.columnWidths = new int[]{0, 202, 147, 159, 0, 0, 0};
        gbl_costDistroPane.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        gbl_costDistroPane.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 1.0, 0.0, Double.MIN_VALUE};
        gbl_costDistroPane.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
        costDistroPane.setLayout(gbl_costDistroPane);
        
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
        gbl_viewGeneralInfoPane.columnWidths = new int[]{0, 0, 0, 242, 0, 0, 0};
        gbl_viewGeneralInfoPane.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
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
        viewWorkRequestNumLabel.setHorizontalAlignment(SwingConstants.TRAILING);
        GridBagConstraints gbc_viewWorkRequestNumLabel = new GridBagConstraints();
        gbc_viewWorkRequestNumLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_viewWorkRequestNumLabel.insets = new Insets(0, 0, 5, 5);
        gbc_viewWorkRequestNumLabel.gridx = 2;
        gbc_viewWorkRequestNumLabel.gridy = 1;
        viewGeneralInfoPane.add(viewWorkRequestNumLabel, gbc_viewWorkRequestNumLabel);
        
        viewWRNumTextField = new JTextField();
        viewWRNumTextField.setEditable(false);
        GridBagConstraints gbc_viewWRNumTextField = new GridBagConstraints();
        gbc_viewWRNumTextField.gridwidth = 2;
        gbc_viewWRNumTextField.insets = new Insets(0, 0, 5, 5);
        gbc_viewWRNumTextField.fill = GridBagConstraints.HORIZONTAL;
        gbc_viewWRNumTextField.gridx = 3;
        gbc_viewWRNumTextField.gridy = 1;
        viewGeneralInfoPane.add(viewWRNumTextField, gbc_viewWRNumTextField);
        viewWRNumTextField.setColumns(10);
        
        viewWRStatusLabel = new JLabel("Work Request Status");
        viewWRStatusLabel.setHorizontalAlignment(SwingConstants.TRAILING);
        GridBagConstraints gbc_viewWRStatusLabel = new GridBagConstraints();
        gbc_viewWRStatusLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_viewWRStatusLabel.insets = new Insets(0, 0, 5, 5);
        gbc_viewWRStatusLabel.gridx = 2;
        gbc_viewWRStatusLabel.gridy = 2;
        viewGeneralInfoPane.add(viewWRStatusLabel, gbc_viewWRStatusLabel);
        
        viewWRStatusComboBox = new JComboBox(wrStatusIDVector);
        GridBagConstraints gbc_viewWRStatusComboBox = new GridBagConstraints();
        gbc_viewWRStatusComboBox.gridwidth = 2;
        gbc_viewWRStatusComboBox.insets = new Insets(0, 0, 5, 5);
        gbc_viewWRStatusComboBox.fill = GridBagConstraints.HORIZONTAL;
        gbc_viewWRStatusComboBox.gridx = 3;
        gbc_viewWRStatusComboBox.gridy = 2;
        viewGeneralInfoPane.add(viewWRStatusComboBox, gbc_viewWRStatusComboBox);
        
        viewWRDatePreppedLabel = new JLabel("Date Prepared");
        viewWRDatePreppedLabel.setHorizontalAlignment(SwingConstants.TRAILING);
        GridBagConstraints gbc_viewWRDatePreppedLabel = new GridBagConstraints();
        gbc_viewWRDatePreppedLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_viewWRDatePreppedLabel.insets = new Insets(0, 0, 5, 5);
        gbc_viewWRDatePreppedLabel.gridx = 2;
        gbc_viewWRDatePreppedLabel.gridy = 3;
        viewGeneralInfoPane.add(viewWRDatePreppedLabel, gbc_viewWRDatePreppedLabel);
        
        viewDatePrepTextField = new JTextField();
        viewDatePrepTextField.setEditable(false);
        viewDatePrepTextField.setColumns(10);
        GridBagConstraints gbc_viewDatePrepTextField = new GridBagConstraints();
        gbc_viewDatePrepTextField.gridwidth = 2;
        gbc_viewDatePrepTextField.insets = new Insets(0, 0, 5, 5);
        gbc_viewDatePrepTextField.fill = GridBagConstraints.HORIZONTAL;
        gbc_viewDatePrepTextField.gridx = 3;
        gbc_viewDatePrepTextField.gridy = 3;
        viewGeneralInfoPane.add(viewDatePrepTextField, gbc_viewDatePrepTextField);
        
        viewDraftDueDateLabel = new JLabel("Draft Due Date");
        viewDraftDueDateLabel.setHorizontalAlignment(SwingConstants.TRAILING);
        GridBagConstraints gbc_viewDraftDueDateLabel = new GridBagConstraints();
        gbc_viewDraftDueDateLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_viewDraftDueDateLabel.insets = new Insets(0, 0, 5, 5);
        gbc_viewDraftDueDateLabel.gridx = 2;
        gbc_viewDraftDueDateLabel.gridy = 4;
        viewGeneralInfoPane.add(viewDraftDueDateLabel, gbc_viewDraftDueDateLabel);
        
        viewDraftDueDateTextField = new JTextField();
        viewDraftDueDateTextField.setEditable(false);
        viewDraftDueDateTextField.setText((String) null);
        viewDraftDueDateTextField.setColumns(10);
        GridBagConstraints gbc_viewDraftDueDateTextField = new GridBagConstraints();
        gbc_viewDraftDueDateTextField.gridwidth = 2;
        gbc_viewDraftDueDateTextField.insets = new Insets(0, 0, 5, 5);
        gbc_viewDraftDueDateTextField.fill = GridBagConstraints.HORIZONTAL;
        gbc_viewDraftDueDateTextField.gridx = 3;
        gbc_viewDraftDueDateTextField.gridy = 4;
        viewGeneralInfoPane.add(viewDraftDueDateTextField, gbc_viewDraftDueDateTextField);
        
        viewRequesterLabel = new JLabel("Requester");
        viewRequesterLabel.setHorizontalAlignment(SwingConstants.TRAILING);
        GridBagConstraints gbc_viewRequesterLabel = new GridBagConstraints();
        gbc_viewRequesterLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_viewRequesterLabel.insets = new Insets(0, 0, 5, 5);
        gbc_viewRequesterLabel.gridx = 2;
        gbc_viewRequesterLabel.gridy = 5;
        viewGeneralInfoPane.add(viewRequesterLabel, gbc_viewRequesterLabel);
        
        viewRequesterTextField = new JTextField();
        viewRequesterTextField.setEditable(false);
        viewRequesterTextField.setColumns(10);
        GridBagConstraints gbc_viewRequesterTextField = new GridBagConstraints();
        gbc_viewRequesterTextField.gridwidth = 2;
        gbc_viewRequesterTextField.insets = new Insets(0, 0, 5, 5);
        gbc_viewRequesterTextField.fill = GridBagConstraints.HORIZONTAL;
        gbc_viewRequesterTextField.gridx = 3;
        gbc_viewRequesterTextField.gridy = 5;
        viewGeneralInfoPane.add(viewRequesterTextField, gbc_viewRequesterTextField);
        
        viewProjectManagerLabel = new JLabel("Project Manager");
        viewProjectManagerLabel.setHorizontalAlignment(SwingConstants.TRAILING);
        GridBagConstraints gbc_viewProjectManagerLabel = new GridBagConstraints();
        gbc_viewProjectManagerLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_viewProjectManagerLabel.insets = new Insets(0, 0, 5, 5);
        gbc_viewProjectManagerLabel.gridx = 2;
        gbc_viewProjectManagerLabel.gridy = 6;
        viewGeneralInfoPane.add(viewProjectManagerLabel, gbc_viewProjectManagerLabel);
        
        viewProjectManagerTextField = new JTextField();
        viewProjectManagerTextField.setEditable(false);
        viewProjectManagerTextField.setColumns(10);
        GridBagConstraints gbc_viewviewProjectManagerTextField = new GridBagConstraints();
        gbc_viewviewProjectManagerTextField.gridwidth = 2;
        gbc_viewviewProjectManagerTextField.insets = new Insets(0, 0, 5, 5);
        gbc_viewviewProjectManagerTextField.fill = GridBagConstraints.HORIZONTAL;
        gbc_viewviewProjectManagerTextField.gridx = 3;
        gbc_viewviewProjectManagerTextField.gridy = 6;
        viewGeneralInfoPane.add(viewProjectManagerTextField, gbc_viewviewProjectManagerTextField);
        
        viewSupervisorLabel = new JLabel("Supervisor");
        viewSupervisorLabel.setHorizontalAlignment(SwingConstants.TRAILING);
        GridBagConstraints gbc_viewSupervisorLabel = new GridBagConstraints();
        gbc_viewSupervisorLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_viewSupervisorLabel.insets = new Insets(0, 0, 5, 5);
        gbc_viewSupervisorLabel.gridx = 2;
        gbc_viewSupervisorLabel.gridy = 7;
        viewGeneralInfoPane.add(viewSupervisorLabel, gbc_viewSupervisorLabel);
        
        viewSupervisorTextField = new JTextField();
        viewSupervisorTextField.setEditable(false);
        viewSupervisorTextField.setColumns(10);
        GridBagConstraints gbc_viewSupervisorTextField = new GridBagConstraints();
        gbc_viewSupervisorTextField.gridwidth = 2;
        gbc_viewSupervisorTextField.insets = new Insets(0, 0, 5, 5);
        gbc_viewSupervisorTextField.fill = GridBagConstraints.HORIZONTAL;
        gbc_viewSupervisorTextField.gridx = 3;
        gbc_viewSupervisorTextField.gridy = 7;
        viewGeneralInfoPane.add(viewSupervisorTextField, gbc_viewSupervisorTextField);
        
        viewProjectNameLabel = new JLabel("Project Name");
        viewProjectNameLabel.setHorizontalAlignment(SwingConstants.TRAILING);
        GridBagConstraints gbc_viewProjectNameLabel = new GridBagConstraints();
        gbc_viewProjectNameLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_viewProjectNameLabel.insets = new Insets(0, 0, 5, 5);
        gbc_viewProjectNameLabel.gridx = 2;
        gbc_viewProjectNameLabel.gridy = 8;
        viewGeneralInfoPane.add(viewProjectNameLabel, gbc_viewProjectNameLabel);
        
        viewProjectNameTextField = new JTextField();
        viewProjectNameTextField.setEditable(false);
        viewProjectNameTextField.setColumns(10);
        GridBagConstraints gbc_viewProjectNameTextField = new GridBagConstraints();
        gbc_viewProjectNameTextField.gridwidth = 2;
        gbc_viewProjectNameTextField.insets = new Insets(0, 0, 5, 5);
        gbc_viewProjectNameTextField.fill = GridBagConstraints.HORIZONTAL;
        gbc_viewProjectNameTextField.gridx = 3;
        gbc_viewProjectNameTextField.gridy = 8;
        viewGeneralInfoPane.add(viewProjectNameTextField, gbc_viewProjectNameTextField);
        
        viewProjectNumberLabel = new JLabel("Project Number");
        viewProjectNumberLabel.setHorizontalAlignment(SwingConstants.TRAILING);
        GridBagConstraints gbc_viewProjectNumberLabel = new GridBagConstraints();
        gbc_viewProjectNumberLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_viewProjectNumberLabel.insets = new Insets(0, 0, 5, 5);
        gbc_viewProjectNumberLabel.gridx = 2;
        gbc_viewProjectNumberLabel.gridy = 9;
        viewGeneralInfoPane.add(viewProjectNumberLabel, gbc_viewProjectNumberLabel);
        
        viewProjectNumberTextField = new JTextField();
        viewProjectNumberTextField.setEditable(false);
        viewProjectNumberTextField.setColumns(10);
        GridBagConstraints gbc_viewProjectNumberTextField = new GridBagConstraints();
        gbc_viewProjectNumberTextField.gridwidth = 2;
        gbc_viewProjectNumberTextField.insets = new Insets(0, 0, 5, 5);
        gbc_viewProjectNumberTextField.fill = GridBagConstraints.HORIZONTAL;
        gbc_viewProjectNumberTextField.gridx = 3;
        gbc_viewProjectNumberTextField.gridy = 9;
        viewGeneralInfoPane.add(viewProjectNumberTextField, gbc_viewProjectNumberTextField);
        
        viewProgramYearLabel = new JLabel("Program Year");
        viewProgramYearLabel.setHorizontalAlignment(SwingConstants.TRAILING);
        GridBagConstraints gbc_viewProgramYearLabel = new GridBagConstraints();
        gbc_viewProgramYearLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_viewProgramYearLabel.insets = new Insets(0, 0, 5, 5);
        gbc_viewProgramYearLabel.gridx = 2;
        gbc_viewProgramYearLabel.gridy = 10;
        viewGeneralInfoPane.add(viewProgramYearLabel, gbc_viewProgramYearLabel);
        
        viewProgramYearTextField = new JTextField();
        viewProgramYearTextField.setEditable(false);
        viewProgramYearTextField.setColumns(10);
        GridBagConstraints gbc_viewProgramYearTextField = new GridBagConstraints();
        gbc_viewProgramYearTextField.gridwidth = 2;
        gbc_viewProgramYearTextField.insets = new Insets(0, 0, 5, 5);
        gbc_viewProgramYearTextField.fill = GridBagConstraints.HORIZONTAL;
        gbc_viewProgramYearTextField.gridx = 3;
        gbc_viewProgramYearTextField.gridy = 10;
        viewGeneralInfoPane.add(viewProgramYearTextField, gbc_viewProgramYearTextField);
        
        viewFundSourceTextLabel = new JLabel("Fund Source");
        viewFundSourceTextLabel.setHorizontalAlignment(SwingConstants.TRAILING);
        GridBagConstraints gbc_viewFundSourceTextLabel = new GridBagConstraints();
        gbc_viewFundSourceTextLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_viewFundSourceTextLabel.insets = new Insets(0, 0, 5, 5);
        gbc_viewFundSourceTextLabel.gridx = 2;
        gbc_viewFundSourceTextLabel.gridy = 11;
        viewGeneralInfoPane.add(viewFundSourceTextLabel, gbc_viewFundSourceTextLabel);
        
        viewFundSourceTextField = new JTextField();
        viewFundSourceTextField.setEditable(false);
        viewFundSourceTextField.setColumns(10);
        GridBagConstraints gbc_viewFundSourceTextField = new GridBagConstraints();
        gbc_viewFundSourceTextField.gridwidth = 2;
        gbc_viewFundSourceTextField.insets = new Insets(0, 0, 5, 5);
        gbc_viewFundSourceTextField.fill = GridBagConstraints.HORIZONTAL;
        gbc_viewFundSourceTextField.gridx = 3;
        gbc_viewFundSourceTextField.gridy = 11;
        viewGeneralInfoPane.add(viewFundSourceTextField, gbc_viewFundSourceTextField);
        
        viewStartDateLabel = new JLabel("Start Date");
        viewStartDateLabel.setHorizontalAlignment(SwingConstants.TRAILING);
        GridBagConstraints gbc_viewStartDateLabel = new GridBagConstraints();
        gbc_viewStartDateLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_viewStartDateLabel.insets = new Insets(0, 0, 5, 5);
        gbc_viewStartDateLabel.gridx = 2;
        gbc_viewStartDateLabel.gridy = 12;
        viewGeneralInfoPane.add(viewStartDateLabel, gbc_viewStartDateLabel);
        
        viewStartDateTextField = new JTextField();
        viewStartDateTextField.setEditable(false);
        viewStartDateTextField.setColumns(10);
        GridBagConstraints gbc_viewStartDateTextField = new GridBagConstraints();
        gbc_viewStartDateTextField.gridwidth = 2;
        gbc_viewStartDateTextField.insets = new Insets(0, 0, 5, 5);
        gbc_viewStartDateTextField.fill = GridBagConstraints.HORIZONTAL;
        gbc_viewStartDateTextField.gridx = 3;
        gbc_viewStartDateTextField.gridy = 12;
        viewGeneralInfoPane.add(viewStartDateTextField, gbc_viewStartDateTextField);
        
        viewCompletionDateLabel = new JLabel("Completion Date");
        viewCompletionDateLabel.setHorizontalAlignment(SwingConstants.TRAILING);
        GridBagConstraints gbc_viewCompletionDateLabel = new GridBagConstraints();
        gbc_viewCompletionDateLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_viewCompletionDateLabel.insets = new Insets(0, 0, 5, 5);
        gbc_viewCompletionDateLabel.gridx = 2;
        gbc_viewCompletionDateLabel.gridy = 13;
        viewGeneralInfoPane.add(viewCompletionDateLabel, gbc_viewCompletionDateLabel);
        
        viewCompletionDateTextField = new JTextField();
        viewCompletionDateTextField.setEditable(false);
        viewCompletionDateTextField.setColumns(10);
        GridBagConstraints gbc_viewCompletionDateTextField = new GridBagConstraints();
        gbc_viewCompletionDateTextField.gridwidth = 2;
        gbc_viewCompletionDateTextField.insets = new Insets(0, 0, 5, 5);
        gbc_viewCompletionDateTextField.fill = GridBagConstraints.HORIZONTAL;
        gbc_viewCompletionDateTextField.gridx = 3;
        gbc_viewCompletionDateTextField.gridy = 13;
        viewGeneralInfoPane.add(viewCompletionDateTextField, gbc_viewCompletionDateTextField);
        
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
        viewDateAddedTextField.setEditable(false);
        GridBagConstraints gbc_viewDateAddedTextField = new GridBagConstraints();
        gbc_viewDateAddedTextField.insets = new Insets(0, 0, 0, 5);
        gbc_viewDateAddedTextField.fill = GridBagConstraints.HORIZONTAL;
        gbc_viewDateAddedTextField.gridx = 2;
        gbc_viewDateAddedTextField.gridy = 15;
        viewGeneralInfoPane.add(viewDateAddedTextField, gbc_viewDateAddedTextField);
        viewDateAddedTextField.setColumns(10);
        
        viewUserTextField = new JTextField();
        viewUserTextField.setEditable(false);
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
        viewNotesTextField.setEditable(false);
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
        workRequestsSelectionTable.getSelectionModel().addListSelectionListener(new ListSelectionListener()
        {
            public void valueChanged(ListSelectionEvent event) 
            {
                try {
					getDataFromSQL(connection);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
                
                selectedRow = workRequestsSelectionTable.getSelectedRow();
                System.out.println("row selection changed to: "+ selectedRow);
                fillGUI(selectedRow);
                frmWorkRequestApplication.invalidate();
                frmWorkRequestApplication.validate();
                frmWorkRequestApplication.repaint();
            }
        });
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
        //
        
    	viewCostDistroPane = new JPanel();
    	viewWorkRequestsPane.addTab("Cost Distribution", null, viewCostDistroPane, null);   	
    	GridBagLayout gbl_viewCostDistroPane = new GridBagLayout();
    	gbl_viewCostDistroPane.columnWidths = new int[]{0, 202, 147, 159, 0, 0, 0};
    	gbl_viewCostDistroPane.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    	gbl_viewCostDistroPane.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 1.0, 0.0, Double.MIN_VALUE};
    	gbl_viewCostDistroPane.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
    	viewCostDistroPane.setLayout(gbl_viewCostDistroPane);

    	viewCostDistroHorizontalStrut = Box.createHorizontalStrut(20);
    	GridBagConstraints gbc_viewCostDistroHorizontalStrut = new GridBagConstraints();
    	gbc_viewCostDistroHorizontalStrut.insets = new Insets(0, 0, 5, 5);
    	gbc_viewCostDistroHorizontalStrut.gridx = 0;
    	gbc_viewCostDistroHorizontalStrut.gridy = 0;
    	viewCostDistroPane.add(viewCostDistroHorizontalStrut, gbc_viewCostDistroHorizontalStrut);

    	viewDiscLabel = new JLabel("Discipline");
    	viewDiscLabel.setHorizontalAlignment(SwingConstants.CENTER);
    	viewDiscLabel.setOpaque(true);
    	viewDiscLabel.setForeground(new Color(255, 255, 255));
    	viewDiscLabel.setBackground(new Color(153, 153, 153));
    	GridBagConstraints gbc_viewDiscLabel = new GridBagConstraints();
    	gbc_viewDiscLabel.fill = GridBagConstraints.HORIZONTAL;
    	gbc_viewDiscLabel.insets = new Insets(0, 0, 5, 5);
    	gbc_viewDiscLabel.gridx = 1;
    	gbc_viewDiscLabel.gridy = 0;
    	viewCostDistroPane.add(viewDiscLabel, gbc_viewDiscLabel);

    	viewDirectTaskLabel = new JLabel("Direct Task Hours");
    	viewDirectTaskLabel.setHorizontalAlignment(SwingConstants.CENTER);
    	viewDirectTaskLabel.setForeground(new Color(255, 255, 255));
    	viewDirectTaskLabel.setOpaque(true);
    	viewDirectTaskLabel.setBackground(new Color(153, 153, 153));
    	GridBagConstraints gbc_viewDirectTaskLabel = new GridBagConstraints();
    	gbc_viewDirectTaskLabel.fill = GridBagConstraints.HORIZONTAL;
    	gbc_viewDirectTaskLabel.insets = new Insets(0, 0, 5, 5);
    	gbc_viewDirectTaskLabel.gridx = 2;
    	gbc_viewDirectTaskLabel.gridy = 0;
    	viewCostDistroPane.add(viewDirectTaskLabel, gbc_viewDirectTaskLabel);

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
    	viewCostDistroPane.add(costLabel, gbc_costLabel);

    	viewAssigneePrevWRLabel = new JLabel("Assignee From Previous WR");
    	viewAssigneePrevWRLabel.setHorizontalAlignment(SwingConstants.CENTER);
    	viewAssigneePrevWRLabel.setForeground(new Color(255, 255, 255));
    	viewAssigneePrevWRLabel.setOpaque(true);
    	viewAssigneePrevWRLabel.setBackground(new Color(153, 153, 153));
    	GridBagConstraints gbc_viewAssigneePrevWRLabel = new GridBagConstraints();
    	gbc_viewAssigneePrevWRLabel.fill = GridBagConstraints.HORIZONTAL;
    	gbc_viewAssigneePrevWRLabel.insets = new Insets(0, 0, 5, 5);
    	gbc_viewAssigneePrevWRLabel.gridx = 4;
    	gbc_viewAssigneePrevWRLabel.gridy = 0;
    	viewCostDistroPane.add(viewAssigneePrevWRLabel, gbc_viewAssigneePrevWRLabel);

    	viewCostDistroHorizontalStrut_2 = Box.createHorizontalStrut(20);
    	GridBagConstraints gbc_viewCostDistroHorizontalStrut_2 = new GridBagConstraints();
    	gbc_viewCostDistroHorizontalStrut_2.insets = new Insets(0, 0, 5, 0);
    	gbc_viewCostDistroHorizontalStrut_2.gridx = 5;
    	gbc_viewCostDistroHorizontalStrut_2.gridy = 0;
    	viewCostDistroPane.add(viewCostDistroHorizontalStrut_2, gbc_viewCostDistroHorizontalStrut_2);

    	viewCivilLabel = new JLabel("Civil");
    	GridBagConstraints gbc_viewCivilLabel = new GridBagConstraints();
    	gbc_viewCivilLabel.insets = new Insets(0, 0, 5, 5);
    	gbc_viewCivilLabel.gridx = 1;
    	gbc_viewCivilLabel.gridy = 1;
    	viewCostDistroPane.add(viewCivilLabel, gbc_viewCivilLabel);

    	viewCivillSpinner = new JSpinner();
    	GridBagConstraints gbc_viewCivillSpinner = new GridBagConstraints();
    	gbc_viewCivillSpinner.fill = GridBagConstraints.HORIZONTAL;
    	gbc_viewCivillSpinner.insets = new Insets(0, 0, 5, 5);
    	gbc_viewCivillSpinner.gridx = 2;
    	gbc_viewCivillSpinner.gridy = 1;
    	viewCostDistroPane.add(viewCivillSpinner, gbc_viewCivillSpinner);

    	viewCivilCostLabel = new JLabel("$0.00");
    	viewCivilCostLabel.setHorizontalAlignment(SwingConstants.TRAILING);
    	GridBagConstraints gbc_civialCostLabel = new GridBagConstraints();
    	gbc_civialCostLabel.fill = GridBagConstraints.HORIZONTAL;
    	gbc_civialCostLabel.insets = new Insets(0, 0, 5, 5);
    	gbc_civialCostLabel.gridx = 3;
    	gbc_civialCostLabel.gridy = 1;
    	viewCostDistroPane.add(viewCivilCostLabel, gbc_civialCostLabel);

    	viewPrevCivialCostLabel = new JLabel("- none -");
    	GridBagConstraints gbc_viewPrevCivialCostLabel = new GridBagConstraints();
    	gbc_viewPrevCivialCostLabel.fill = GridBagConstraints.HORIZONTAL;
    	gbc_viewPrevCivialCostLabel.insets = new Insets(0, 0, 5, 5);
    	gbc_viewPrevCivialCostLabel.gridx = 4;
    	gbc_viewPrevCivialCostLabel.gridy = 1;
    	viewCostDistroPane.add(viewPrevCivialCostLabel, gbc_viewPrevCivialCostLabel);

    	viewArchLabel = new JLabel("Architectural");
    	GridBagConstraints gbc_viewArchLabel = new GridBagConstraints();
    	gbc_viewArchLabel.insets = new Insets(0, 0, 5, 5);
    	gbc_viewArchLabel.gridx = 1;
    	gbc_viewArchLabel.gridy = 2;
    	viewCostDistroPane.add(viewArchLabel, gbc_viewArchLabel);

    	viewArchitecturalSpinner = new JSpinner();
    	GridBagConstraints gbc_viewArchitecturalSpinner = new GridBagConstraints();
    	gbc_viewArchitecturalSpinner.fill = GridBagConstraints.HORIZONTAL;
    	gbc_viewArchitecturalSpinner.insets = new Insets(0, 0, 5, 5);
    	gbc_viewArchitecturalSpinner.gridx = 2;
    	gbc_viewArchitecturalSpinner.gridy = 2;
    	viewCostDistroPane.add(viewArchitecturalSpinner, gbc_viewArchitecturalSpinner);

    	viewArchCostLabel = new JLabel("$0.00");
    	viewArchCostLabel.setHorizontalAlignment(SwingConstants.TRAILING);
    	GridBagConstraints gbc_archCostLabel = new GridBagConstraints();
    	gbc_archCostLabel.fill = GridBagConstraints.HORIZONTAL;
    	gbc_archCostLabel.insets = new Insets(0, 0, 5, 5);
    	gbc_archCostLabel.gridx = 3;
    	gbc_archCostLabel.gridy = 2;
    	viewCostDistroPane.add(viewArchCostLabel, gbc_archCostLabel);

    	viewPrevArchCostLabel = new JLabel("- none -");
    	GridBagConstraints gbc_viewPrevArchCostLabel = new GridBagConstraints();
    	gbc_viewPrevArchCostLabel.fill = GridBagConstraints.HORIZONTAL;
    	gbc_viewPrevArchCostLabel.insets = new Insets(0, 0, 5, 5);
    	gbc_viewPrevArchCostLabel.gridx = 4;
    	gbc_viewPrevArchCostLabel.gridy = 2;
    	viewCostDistroPane.add(viewPrevArchCostLabel, gbc_viewPrevArchCostLabel);

    	viewStructLabel = new JLabel("Structural");
    	GridBagConstraints gbc_viewStructLabel = new GridBagConstraints();
    	gbc_viewStructLabel.insets = new Insets(0, 0, 5, 5);
    	gbc_viewStructLabel.gridx = 1;
    	gbc_viewStructLabel.gridy = 3;
    	viewCostDistroPane.add(viewStructLabel, gbc_viewStructLabel);

    	viewStructuralSpinner = new JSpinner();
    	GridBagConstraints gbc_viewStructuralSpinner = new GridBagConstraints();
    	gbc_viewStructuralSpinner.fill = GridBagConstraints.HORIZONTAL;
    	gbc_viewStructuralSpinner.insets = new Insets(0, 0, 5, 5);
    	gbc_viewStructuralSpinner.gridx = 2;
    	gbc_viewStructuralSpinner.gridy = 3;
    	viewCostDistroPane.add(viewStructuralSpinner, gbc_viewStructuralSpinner);

    	viewStructCostLabel = new JLabel("$0.00");
    	viewStructCostLabel.setHorizontalAlignment(SwingConstants.TRAILING);
    	GridBagConstraints gbc_strucCostLabel = new GridBagConstraints();
    	gbc_strucCostLabel.fill = GridBagConstraints.HORIZONTAL;
    	gbc_strucCostLabel.insets = new Insets(0, 0, 5, 5);
    	gbc_strucCostLabel.gridx = 3;
    	gbc_strucCostLabel.gridy = 3;
    	viewCostDistroPane.add(viewStructCostLabel, gbc_strucCostLabel);

    	viewPrevStrucCostLabel = new JLabel("- none -");
    	GridBagConstraints gbc_viewPrevStrucCostLabel = new GridBagConstraints();
    	gbc_viewPrevStrucCostLabel.fill = GridBagConstraints.HORIZONTAL;
    	gbc_viewPrevStrucCostLabel.insets = new Insets(0, 0, 5, 5);
    	gbc_viewPrevStrucCostLabel.gridx = 4;
    	gbc_viewPrevStrucCostLabel.gridy = 3;
    	viewCostDistroPane.add(viewPrevStrucCostLabel, gbc_viewPrevStrucCostLabel);

    	viewAntiterrorLabel = new JLabel("Antiterrosim/Force Protection");
    	GridBagConstraints gbc_viewAntiterrorLabel = new GridBagConstraints();
    	gbc_viewAntiterrorLabel.insets = new Insets(0, 0, 5, 5);
    	gbc_viewAntiterrorLabel.gridx = 1;
    	gbc_viewAntiterrorLabel.gridy = 4;
    	viewCostDistroPane.add(viewAntiterrorLabel, gbc_viewAntiterrorLabel);

    	viewForceProtSpinner = new JSpinner();
    	GridBagConstraints gbc_viewForceProtSpinner = new GridBagConstraints();
    	gbc_viewForceProtSpinner.fill = GridBagConstraints.HORIZONTAL;
    	gbc_viewForceProtSpinner.insets = new Insets(0, 0, 5, 5);
    	gbc_viewForceProtSpinner.gridx = 2;
    	gbc_viewForceProtSpinner.gridy = 4;
    	viewCostDistroPane.add(viewForceProtSpinner, gbc_viewForceProtSpinner);

    	viewForceProtCostLabel = new JLabel("$0.00");
    	viewForceProtCostLabel.setHorizontalAlignment(SwingConstants.TRAILING);
    	GridBagConstraints gbc_viewForceProtCostLabel = new GridBagConstraints();
    	gbc_viewForceProtCostLabel.fill = GridBagConstraints.HORIZONTAL;
    	gbc_viewForceProtCostLabel.insets = new Insets(0, 0, 5, 5);
    	gbc_viewForceProtCostLabel.gridx = 3;
    	gbc_viewForceProtCostLabel.gridy = 4;
    	viewCostDistroPane.add(viewForceProtCostLabel, gbc_viewForceProtCostLabel);

    	viewPrevForceProtCostLabel = new JLabel("- none -");
    	GridBagConstraints gbc_viewPrevForceProtCostLabel = new GridBagConstraints();
    	gbc_viewPrevForceProtCostLabel.fill = GridBagConstraints.HORIZONTAL;
    	gbc_viewPrevForceProtCostLabel.insets = new Insets(0, 0, 5, 5);
    	gbc_viewPrevForceProtCostLabel.gridx = 4;
    	gbc_viewPrevForceProtCostLabel.gridy = 4;
    	viewCostDistroPane.add(viewPrevForceProtCostLabel, gbc_viewPrevForceProtCostLabel);

    	viewMechLabel = new JLabel("Mechanical");
    	GridBagConstraints gbc_viewMechLabel = new GridBagConstraints();
    	gbc_viewMechLabel.insets = new Insets(0, 0, 5, 5);
    	gbc_viewMechLabel.gridx = 1;
    	gbc_viewMechLabel.gridy = 5;
    	viewCostDistroPane.add(viewMechLabel, gbc_viewMechLabel);

    	viewMechSpinner = new JSpinner();
    	GridBagConstraints gbc_viewMechSpinner = new GridBagConstraints();
    	gbc_viewMechSpinner.fill = GridBagConstraints.HORIZONTAL;
    	gbc_viewMechSpinner.insets = new Insets(0, 0, 5, 5);
    	gbc_viewMechSpinner.gridx = 2;
    	gbc_viewMechSpinner.gridy = 5;
    	viewCostDistroPane.add(viewMechSpinner, gbc_viewMechSpinner);

    	viewMechCostLabel = new JLabel("$0.00");
    	viewMechCostLabel.setHorizontalAlignment(SwingConstants.TRAILING);
    	GridBagConstraints gbc_viewMechCostLabel = new GridBagConstraints();
    	gbc_viewMechCostLabel.fill = GridBagConstraints.HORIZONTAL;
    	gbc_viewMechCostLabel.insets = new Insets(0, 0, 5, 5);
    	gbc_viewMechCostLabel.gridx = 3;
    	gbc_viewMechCostLabel.gridy = 5;
    	viewCostDistroPane.add(viewMechCostLabel, gbc_viewMechCostLabel);

    	viewPrevMechCostLabel = new JLabel("- none -");
    	GridBagConstraints gbc_viewPrevMechCostLabel = new GridBagConstraints();
    	gbc_viewPrevMechCostLabel.fill = GridBagConstraints.HORIZONTAL;
    	gbc_viewPrevMechCostLabel.insets = new Insets(0, 0, 5, 5);
    	gbc_viewPrevMechCostLabel.gridx = 4;
    	gbc_viewPrevMechCostLabel.gridy = 5;
    	viewCostDistroPane.add(viewPrevMechCostLabel, gbc_viewPrevMechCostLabel);

    	viewFireProtLabel = new JLabel("Fire Protection");
    	GridBagConstraints gbc_viewFireProtLabel = new GridBagConstraints();
    	gbc_viewFireProtLabel.insets = new Insets(0, 0, 5, 5);
    	gbc_viewFireProtLabel.gridx = 1;
    	gbc_viewFireProtLabel.gridy = 6;
    	viewCostDistroPane.add(viewFireProtLabel, gbc_viewFireProtLabel);

    	viewFireProtSpinner = new JSpinner();
    	GridBagConstraints gbc_viewFireProtSpinner = new GridBagConstraints();
    	gbc_viewFireProtSpinner.fill = GridBagConstraints.HORIZONTAL;
    	gbc_viewFireProtSpinner.insets = new Insets(0, 0, 5, 5);
    	gbc_viewFireProtSpinner.gridx = 2;
    	gbc_viewFireProtSpinner.gridy = 6;
    	viewCostDistroPane.add(viewFireProtSpinner, gbc_viewFireProtSpinner);

    	viewFireProtCostLabel = new JLabel("$0.00");
    	viewFireProtCostLabel.setHorizontalAlignment(SwingConstants.TRAILING);
    	GridBagConstraints gbc_viewFireProtCostLabel = new GridBagConstraints();
    	gbc_viewFireProtCostLabel.fill = GridBagConstraints.HORIZONTAL;
    	gbc_viewFireProtCostLabel.insets = new Insets(0, 0, 5, 5);
    	gbc_viewFireProtCostLabel.gridx = 3;
    	gbc_viewFireProtCostLabel.gridy = 6;
    	viewCostDistroPane.add(viewFireProtCostLabel, gbc_viewFireProtCostLabel);

    	viewPrevFireProtCostLabel = new JLabel("- none -");
    	GridBagConstraints gbc_viewPrevFireProtCostLabel = new GridBagConstraints();
    	gbc_viewPrevFireProtCostLabel.fill = GridBagConstraints.HORIZONTAL;
    	gbc_viewPrevFireProtCostLabel.insets = new Insets(0, 0, 5, 5);
    	gbc_viewPrevFireProtCostLabel.gridx = 4;
    	gbc_viewPrevFireProtCostLabel.gridy = 6;
    	viewCostDistroPane.add(viewPrevFireProtCostLabel, gbc_viewPrevFireProtCostLabel);

    	viewElectLabel = new JLabel("Electrical");
    	GridBagConstraints gbc_viewElectLabel = new GridBagConstraints();
    	gbc_viewElectLabel.insets = new Insets(0, 0, 5, 5);
    	gbc_viewElectLabel.gridx = 1;
    	gbc_viewElectLabel.gridy = 7;
    	viewCostDistroPane.add(viewElectLabel, gbc_viewElectLabel);

    	viewElectricSpinner = new JSpinner();
    	GridBagConstraints gbc_viewElectricSpinner = new GridBagConstraints();
    	gbc_viewElectricSpinner.fill = GridBagConstraints.HORIZONTAL;
    	gbc_viewElectricSpinner.insets = new Insets(0, 0, 5, 5);
    	gbc_viewElectricSpinner.gridx = 2;
    	gbc_viewElectricSpinner.gridy = 7;
    	viewCostDistroPane.add(viewElectricSpinner, gbc_viewElectricSpinner);

    	viewElectricalCostLabel = new JLabel("$0.00");
    	viewElectricalCostLabel.setHorizontalAlignment(SwingConstants.TRAILING);
    	GridBagConstraints gbc_viewElectricalCostLabel = new GridBagConstraints();
    	gbc_viewElectricalCostLabel.fill = GridBagConstraints.HORIZONTAL;
    	gbc_viewElectricalCostLabel.insets = new Insets(0, 0, 5, 5);
    	gbc_viewElectricalCostLabel.gridx = 3;
    	gbc_viewElectricalCostLabel.gridy = 7;
    	viewCostDistroPane.add(viewElectricalCostLabel, gbc_viewElectricalCostLabel);

    	viewPrevElectricalCostLabel = new JLabel("- none -");
    	GridBagConstraints gbc_viewPrevElectricalCostLabel = new GridBagConstraints();
    	gbc_viewPrevElectricalCostLabel.fill = GridBagConstraints.HORIZONTAL;
    	gbc_viewPrevElectricalCostLabel.insets = new Insets(0, 0, 5, 5);
    	gbc_viewPrevElectricalCostLabel.gridx = 4;
    	gbc_viewPrevElectricalCostLabel.gridy = 7;
    	viewCostDistroPane.add(viewPrevElectricalCostLabel, gbc_viewPrevElectricalCostLabel);

    	viewComLabel = new JLabel("Communications");
    	GridBagConstraints gbc_viewComLabel = new GridBagConstraints();
    	gbc_viewComLabel.insets = new Insets(0, 0, 5, 5);
    	gbc_viewComLabel.gridx = 1;
    	gbc_viewComLabel.gridy = 8;
    	viewCostDistroPane.add(viewComLabel, gbc_viewComLabel);

    	viewCommSpinner = new JSpinner();
    	GridBagConstraints gbc_viewCommSpinner = new GridBagConstraints();
    	gbc_viewCommSpinner.fill = GridBagConstraints.HORIZONTAL;
    	gbc_viewCommSpinner.insets = new Insets(0, 0, 5, 5);
    	gbc_viewCommSpinner.gridx = 2;
    	gbc_viewCommSpinner.gridy = 8;
    	viewCostDistroPane.add(viewCommSpinner, gbc_viewCommSpinner);

    	viewCommsCostLabel = new JLabel("$0.00");
    	viewCommsCostLabel.setHorizontalAlignment(SwingConstants.TRAILING);
    	GridBagConstraints gbc_viewCommsCostLabel = new GridBagConstraints();
    	gbc_viewCommsCostLabel.fill = GridBagConstraints.HORIZONTAL;
    	gbc_viewCommsCostLabel.insets = new Insets(0, 0, 5, 5);
    	gbc_viewCommsCostLabel.gridx = 3;
    	gbc_viewCommsCostLabel.gridy = 8;
    	viewCostDistroPane.add(viewCommsCostLabel, gbc_viewCommsCostLabel);

    	viewPrevCommsCostLabel = new JLabel("- none -");
    	GridBagConstraints gbc_viewPrevCommsCostLabel = new GridBagConstraints();
    	gbc_viewPrevCommsCostLabel.fill = GridBagConstraints.HORIZONTAL;
    	gbc_viewPrevCommsCostLabel.insets = new Insets(0, 0, 5, 5);
    	gbc_viewPrevCommsCostLabel.gridx = 4;
    	gbc_viewPrevCommsCostLabel.gridy = 8;
    	viewCostDistroPane.add(viewPrevCommsCostLabel, gbc_viewPrevCommsCostLabel);

    	viewLEEDLabel = new JLabel("LEED");
    	GridBagConstraints gbc_viewLEEDLabel = new GridBagConstraints();
    	gbc_viewLEEDLabel.insets = new Insets(0, 0, 5, 5);
    	gbc_viewLEEDLabel.gridx = 1;
    	gbc_viewLEEDLabel.gridy = 9;
    	viewCostDistroPane.add(viewLEEDLabel, gbc_viewLEEDLabel);

    	viewLEEDSpinner = new JSpinner();
    	GridBagConstraints gbc_viewLEEDSpinner = new GridBagConstraints();
    	gbc_viewLEEDSpinner.fill = GridBagConstraints.HORIZONTAL;
    	gbc_viewLEEDSpinner.insets = new Insets(0, 0, 5, 5);
    	gbc_viewLEEDSpinner.gridx = 2;
    	gbc_viewLEEDSpinner.gridy = 9;
    	viewCostDistroPane.add(viewLEEDSpinner, gbc_viewLEEDSpinner);

    	viewLEEDCostLabel = new JLabel("$0.00");
    	viewLEEDCostLabel.setHorizontalAlignment(SwingConstants.TRAILING);
    	GridBagConstraints gbc_viewLEEDCostLabel = new GridBagConstraints();
    	gbc_viewLEEDCostLabel.fill = GridBagConstraints.HORIZONTAL;
    	gbc_viewLEEDCostLabel.insets = new Insets(0, 0, 5, 5);
    	gbc_viewLEEDCostLabel.gridx = 3;
    	gbc_viewLEEDCostLabel.gridy = 9;
    	viewCostDistroPane.add(viewLEEDCostLabel, gbc_viewLEEDCostLabel);

    	viewPrevLEEDCostLabel = new JLabel("- none -");
    	GridBagConstraints gbc_viewPrevLEEDCostLabel = new GridBagConstraints();
    	gbc_viewPrevLEEDCostLabel.fill = GridBagConstraints.HORIZONTAL;
    	gbc_viewPrevLEEDCostLabel.insets = new Insets(0, 0, 5, 5);
    	gbc_viewPrevLEEDCostLabel.gridx = 4;
    	gbc_viewPrevLEEDCostLabel.gridy = 9;
    	viewCostDistroPane.add(viewPrevLEEDCostLabel, gbc_viewPrevLEEDCostLabel);

    	viewEnvLabel = new JLabel("Environmental");
    	GridBagConstraints gbc_viewEnvLabel = new GridBagConstraints();
    	gbc_viewEnvLabel.insets = new Insets(0, 0, 5, 5);
    	gbc_viewEnvLabel.gridx = 1;
    	gbc_viewEnvLabel.gridy = 10;
    	viewCostDistroPane.add(viewEnvLabel, gbc_viewEnvLabel);

    	viewEnvinronmentalSpinner = new JSpinner();
    	GridBagConstraints gbc_viewEnvinronmentalSpinner = new GridBagConstraints();
    	gbc_viewEnvinronmentalSpinner.fill = GridBagConstraints.HORIZONTAL;
    	gbc_viewEnvinronmentalSpinner.insets = new Insets(0, 0, 5, 5);
    	gbc_viewEnvinronmentalSpinner.gridx = 2;
    	gbc_viewEnvinronmentalSpinner.gridy = 10;
    	viewCostDistroPane.add(viewEnvinronmentalSpinner, gbc_viewEnvinronmentalSpinner);

    	viewEnvCostLabel = new JLabel("$0.00");
    	viewEnvCostLabel.setHorizontalAlignment(SwingConstants.TRAILING);
    	GridBagConstraints gbc_viewEnvCostLabel = new GridBagConstraints();
    	gbc_viewEnvCostLabel.fill = GridBagConstraints.HORIZONTAL;
    	gbc_viewEnvCostLabel.insets = new Insets(0, 0, 5, 5);
    	gbc_viewEnvCostLabel.gridx = 3;
    	gbc_viewEnvCostLabel.gridy = 10;
    	viewCostDistroPane.add(viewEnvCostLabel, gbc_viewEnvCostLabel);

    	viewPrevEnvCostLabel = new JLabel("- none -");
    	GridBagConstraints gbc_viewPrevEnvCostLabel = new GridBagConstraints();
    	gbc_viewPrevEnvCostLabel.fill = GridBagConstraints.HORIZONTAL;
    	gbc_viewPrevEnvCostLabel.insets = new Insets(0, 0, 5, 5);
    	gbc_viewPrevEnvCostLabel.gridx = 4;
    	gbc_viewPrevEnvCostLabel.gridy = 10;
    	viewCostDistroPane.add(viewPrevEnvCostLabel, gbc_viewPrevEnvCostLabel);

    	viewCostEngLabel = new JLabel("Cost Engineer");
    	GridBagConstraints gbc_viewCostEngLabel = new GridBagConstraints();
    	gbc_viewCostEngLabel.insets = new Insets(0, 0, 5, 5);
    	gbc_viewCostEngLabel.gridx = 1;
    	gbc_viewCostEngLabel.gridy = 11;
    	viewCostDistroPane.add(viewCostEngLabel, gbc_viewCostEngLabel);

    	viewCostEngSpinner = new JSpinner();
    	GridBagConstraints gbc_viewCostEngSpinner = new GridBagConstraints();
    	gbc_viewCostEngSpinner.fill = GridBagConstraints.HORIZONTAL;
    	gbc_viewCostEngSpinner.insets = new Insets(0, 0, 5, 5);
    	gbc_viewCostEngSpinner.gridx = 2;
    	gbc_viewCostEngSpinner.gridy = 11;
    	viewCostDistroPane.add(viewCostEngSpinner, gbc_viewCostEngSpinner);

    	viewCostEngCostLabel = new JLabel("$0.00");
    	viewCostEngCostLabel.setHorizontalAlignment(SwingConstants.TRAILING);
    	GridBagConstraints gbc_viewCostEngCostLabel = new GridBagConstraints();
    	gbc_viewCostEngCostLabel.fill = GridBagConstraints.HORIZONTAL;
    	gbc_viewCostEngCostLabel.insets = new Insets(0, 0, 5, 5);
    	gbc_viewCostEngCostLabel.gridx = 3;
    	gbc_viewCostEngCostLabel.gridy = 11;
    	viewCostDistroPane.add(viewCostEngCostLabel, gbc_viewCostEngCostLabel);

    	viewPrevCostEngCostLabel = new JLabel("- none -");
    	GridBagConstraints gbc_viewPrevCostEngCostLabel = new GridBagConstraints();
    	gbc_viewPrevCostEngCostLabel.fill = GridBagConstraints.HORIZONTAL;
    	gbc_viewPrevCostEngCostLabel.insets = new Insets(0, 0, 5, 5);
    	gbc_viewPrevCostEngCostLabel.gridx = 4;
    	gbc_viewPrevCostEngCostLabel.gridy = 11;
    	viewCostDistroPane.add(viewPrevCostEngCostLabel, gbc_viewPrevCostEngCostLabel);

    	viewGeotechLabel = new JLabel("Geotechnical");
    	GridBagConstraints gbc_viewGeotechLabel = new GridBagConstraints();
    	gbc_viewGeotechLabel.insets = new Insets(0, 0, 5, 5);
    	gbc_viewGeotechLabel.gridx = 1;
    	gbc_viewGeotechLabel.gridy = 12;
    	viewCostDistroPane.add(viewGeotechLabel, gbc_viewGeotechLabel);

    	viewGeotechSpinner = new JSpinner();
    	GridBagConstraints gbc_viewGeotechSpinner = new GridBagConstraints();
    	gbc_viewGeotechSpinner.fill = GridBagConstraints.HORIZONTAL;
    	gbc_viewGeotechSpinner.insets = new Insets(0, 0, 5, 5);
    	gbc_viewGeotechSpinner.gridx = 2;
    	gbc_viewGeotechSpinner.gridy = 12;
    	viewCostDistroPane.add(viewGeotechSpinner, gbc_viewGeotechSpinner);

    	viewGeotechCostLabel = new JLabel("$0.00");
    	viewGeotechCostLabel.setHorizontalAlignment(SwingConstants.TRAILING);
    	GridBagConstraints gbc_viewGeotechCostLabel = new GridBagConstraints();
    	gbc_viewGeotechCostLabel.fill = GridBagConstraints.HORIZONTAL;
    	gbc_viewGeotechCostLabel.insets = new Insets(0, 0, 5, 5);
    	gbc_viewGeotechCostLabel.gridx = 3;
    	gbc_viewGeotechCostLabel.gridy = 12;
    	viewCostDistroPane.add(viewGeotechCostLabel, gbc_viewGeotechCostLabel);

    	viewPrevGeotechCostLabel = new JLabel("- none -");
    	GridBagConstraints gbc_viewPrevGeotechCostLabel = new GridBagConstraints();
    	gbc_viewPrevGeotechCostLabel.fill = GridBagConstraints.HORIZONTAL;
    	gbc_viewPrevGeotechCostLabel.insets = new Insets(0, 0, 5, 5);
    	gbc_viewPrevGeotechCostLabel.gridx = 4;
    	gbc_viewPrevGeotechCostLabel.gridy = 12;
    	viewCostDistroPane.add(viewPrevGeotechCostLabel, gbc_viewPrevGeotechCostLabel);

    	viewAEContractLabel = new JLabel("AE Contracting");
    	GridBagConstraints gbc_viewAEContractLabel = new GridBagConstraints();
    	gbc_viewAEContractLabel.insets = new Insets(0, 0, 5, 5);
    	gbc_viewAEContractLabel.gridx = 1;
    	gbc_viewAEContractLabel.gridy = 13;
    	viewCostDistroPane.add(viewAEContractLabel, gbc_viewAEContractLabel);

    	viewAEContSpinner = new JSpinner();
    	GridBagConstraints gbc_viewAEContSpinner = new GridBagConstraints();
    	gbc_viewAEContSpinner.fill = GridBagConstraints.HORIZONTAL;
    	gbc_viewAEContSpinner.insets = new Insets(0, 0, 5, 5);
    	gbc_viewAEContSpinner.gridx = 2;
    	gbc_viewAEContSpinner.gridy = 13;
    	viewCostDistroPane.add(viewAEContSpinner, gbc_viewAEContSpinner);

    	viewAEContractingLabel = new JLabel("$0.00");
    	viewAEContractingLabel.setHorizontalAlignment(SwingConstants.TRAILING);
    	GridBagConstraints gbc_viewAEContractingLabel = new GridBagConstraints();
    	gbc_viewAEContractingLabel.fill = GridBagConstraints.HORIZONTAL;
    	gbc_viewAEContractingLabel.insets = new Insets(0, 0, 5, 5);
    	gbc_viewAEContractingLabel.gridx = 3;
    	gbc_viewAEContractingLabel.gridy = 13;
    	viewCostDistroPane.add(viewAEContractingLabel, gbc_viewAEContractingLabel);

    	viewPrevAEContractingLabel = new JLabel("- none -");
    	GridBagConstraints gbc_viewPrevAEContractingLabel = new GridBagConstraints();
    	gbc_viewPrevAEContractingLabel.fill = GridBagConstraints.HORIZONTAL;
    	gbc_viewPrevAEContractingLabel.insets = new Insets(0, 0, 5, 5);
    	gbc_viewPrevAEContractingLabel.gridx = 4;
    	gbc_viewPrevAEContractingLabel.gridy = 13;
    	viewCostDistroPane.add(viewPrevAEContractingLabel, gbc_viewPrevAEContractingLabel);

    	viewValueEngLabel = new JLabel("Value Engineer");
    	GridBagConstraints gbc_viewValueEngLabel = new GridBagConstraints();
    	gbc_viewValueEngLabel.insets = new Insets(0, 0, 5, 5);
    	gbc_viewValueEngLabel.gridx = 1;
    	gbc_viewValueEngLabel.gridy = 14;
    	viewCostDistroPane.add(viewValueEngLabel, gbc_viewValueEngLabel);

    	viewValEngSpinner = new JSpinner();
    	GridBagConstraints gbc_viewValEngSpinner = new GridBagConstraints();
    	gbc_viewValEngSpinner.fill = GridBagConstraints.HORIZONTAL;
    	gbc_viewValEngSpinner.insets = new Insets(0, 0, 5, 5);
    	gbc_viewValEngSpinner.gridx = 2;
    	gbc_viewValEngSpinner.gridy = 14;
    	viewCostDistroPane.add(viewValEngSpinner, gbc_viewValEngSpinner);

    	viewValueEngCostLabel = new JLabel("$0.00");
    	viewValueEngCostLabel.setHorizontalAlignment(SwingConstants.TRAILING);
    	GridBagConstraints gbc_viewValueEngCostLabel = new GridBagConstraints();
    	gbc_viewValueEngCostLabel.fill = GridBagConstraints.HORIZONTAL;
    	gbc_viewValueEngCostLabel.insets = new Insets(0, 0, 5, 5);
    	gbc_viewValueEngCostLabel.gridx = 3;
    	gbc_viewValueEngCostLabel.gridy = 14;
    	viewCostDistroPane.add(viewValueEngCostLabel, gbc_viewValueEngCostLabel);

    	viewPrevValueEngCostLabel = new JLabel("- none -");
    	GridBagConstraints gbc_viewPrevValueEngCostLabel = new GridBagConstraints();
    	gbc_viewPrevValueEngCostLabel.fill = GridBagConstraints.HORIZONTAL;
    	gbc_viewPrevValueEngCostLabel.insets = new Insets(0, 0, 5, 5);
    	gbc_viewPrevValueEngCostLabel.gridx = 4;
    	gbc_viewPrevValueEngCostLabel.gridy = 14;
    	viewCostDistroPane.add(viewPrevValueEngCostLabel, gbc_viewPrevValueEngCostLabel);

    	viewTranslatorLabel = new JLabel("Translator");
    	GridBagConstraints gbc_viewTranslatorLabel = new GridBagConstraints();
    	gbc_viewTranslatorLabel.insets = new Insets(0, 0, 5, 5);
    	gbc_viewTranslatorLabel.gridx = 1;
    	gbc_viewTranslatorLabel.gridy = 15;
    	viewCostDistroPane.add(viewTranslatorLabel, gbc_viewTranslatorLabel);

    	viewTranslatorSpinner = new JSpinner();
    	GridBagConstraints gbc_viewTranslatorSpinner = new GridBagConstraints();
    	gbc_viewTranslatorSpinner.fill = GridBagConstraints.HORIZONTAL;
    	gbc_viewTranslatorSpinner.insets = new Insets(0, 0, 5, 5);
    	gbc_viewTranslatorSpinner.gridx = 2;
    	gbc_viewTranslatorSpinner.gridy = 15;
    	viewCostDistroPane.add(viewTranslatorSpinner, gbc_viewTranslatorSpinner);

    	viewTranslatorCostLabel = new JLabel("$0.00");
    	viewTranslatorCostLabel.setHorizontalAlignment(SwingConstants.TRAILING);
    	GridBagConstraints gbc_viewTranslatorCostLabel = new GridBagConstraints();
    	gbc_viewTranslatorCostLabel.fill = GridBagConstraints.HORIZONTAL;
    	gbc_viewTranslatorCostLabel.insets = new Insets(0, 0, 5, 5);
    	gbc_viewTranslatorCostLabel.gridx = 3;
    	gbc_viewTranslatorCostLabel.gridy = 15;
    	viewCostDistroPane.add(viewTranslatorCostLabel, gbc_viewTranslatorCostLabel);

    	viewPrevTranslatorCostLabel = new JLabel("- none -");
    	GridBagConstraints gbc_viewPrevTranslatorCostLabel = new GridBagConstraints();
    	gbc_viewPrevTranslatorCostLabel.fill = GridBagConstraints.HORIZONTAL;
    	gbc_viewPrevTranslatorCostLabel.insets = new Insets(0, 0, 5, 5);
    	gbc_viewPrevTranslatorCostLabel.gridx = 4;
    	gbc_viewPrevTranslatorCostLabel.gridy = 15;
    	viewCostDistroPane.add(viewPrevTranslatorCostLabel, gbc_viewPrevTranslatorCostLabel);

    	viewSpecLabel = new JLabel("Specifications");
    	GridBagConstraints gbc_viewSpecLabel = new GridBagConstraints();
    	gbc_viewSpecLabel.insets = new Insets(0, 0, 5, 5);
    	gbc_viewSpecLabel.gridx = 1;
    	gbc_viewSpecLabel.gridy = 16;
    	viewCostDistroPane.add(viewSpecLabel, gbc_viewSpecLabel);

    	viewSpecificationSpinner = new JSpinner();
    	GridBagConstraints gbc_viewSpecificationSpinner = new GridBagConstraints();
    	gbc_viewSpecificationSpinner.fill = GridBagConstraints.HORIZONTAL;
    	gbc_viewSpecificationSpinner.insets = new Insets(0, 0, 5, 5);
    	gbc_viewSpecificationSpinner.gridx = 2;
    	gbc_viewSpecificationSpinner.gridy = 16;
    	viewCostDistroPane.add(viewSpecificationSpinner, gbc_viewSpecificationSpinner);

    	viewSpecCostLabel = new JLabel("$0.00");
    	viewSpecCostLabel.setHorizontalAlignment(SwingConstants.TRAILING);
    	GridBagConstraints gbc_viewSpecCostLabel = new GridBagConstraints();
    	gbc_viewSpecCostLabel.fill = GridBagConstraints.HORIZONTAL;
    	gbc_viewSpecCostLabel.insets = new Insets(0, 0, 5, 5);
    	gbc_viewSpecCostLabel.gridx = 3;
    	gbc_viewSpecCostLabel.gridy = 16;
    	viewCostDistroPane.add(viewSpecCostLabel, gbc_viewSpecCostLabel);

    	viewPrevSpecCostLabel = new JLabel("- none -");
    	GridBagConstraints gbc_viewPrevSpecCostLabel = new GridBagConstraints();
    	gbc_viewPrevSpecCostLabel.fill = GridBagConstraints.HORIZONTAL;
    	gbc_viewPrevSpecCostLabel.insets = new Insets(0, 0, 5, 5);
    	gbc_viewPrevSpecCostLabel.gridx = 4;
    	gbc_viewPrevSpecCostLabel.gridy = 16;
    	viewCostDistroPane.add(viewPrevSpecCostLabel, gbc_viewPrevSpecCostLabel);

    	viewOtherLabel = new JLabel("Other");
    	GridBagConstraints gbc_viewOtherLabel = new GridBagConstraints();
    	gbc_viewOtherLabel.insets = new Insets(0, 0, 0, 5);
    	gbc_viewOtherLabel.gridx = 1;
    	gbc_viewOtherLabel.gridy = 17;
    	viewCostDistroPane.add(viewOtherLabel, gbc_viewOtherLabel);

    	viewOtherSpinner = new JSpinner();
    	GridBagConstraints gbc_viewOtherSpinner = new GridBagConstraints();
    	gbc_viewOtherSpinner.fill = GridBagConstraints.HORIZONTAL;
    	gbc_viewOtherSpinner.insets = new Insets(0, 0, 0, 5);
    	gbc_viewOtherSpinner.gridx = 2;
    	gbc_viewOtherSpinner.gridy = 17;
    	viewCostDistroPane.add(viewOtherSpinner, gbc_viewOtherSpinner);

    	viewOtherCostLabel = new JLabel("$0.00");
    	viewOtherCostLabel.setHorizontalAlignment(SwingConstants.TRAILING);
    	GridBagConstraints gbc_viewOtherCostLabel = new GridBagConstraints();
    	gbc_viewOtherCostLabel.fill = GridBagConstraints.HORIZONTAL;
    	gbc_viewOtherCostLabel.insets = new Insets(0, 0, 0, 5);
    	gbc_viewOtherCostLabel.gridx = 3;
    	gbc_viewOtherCostLabel.gridy = 17;
    	viewCostDistroPane.add(viewOtherCostLabel, gbc_viewOtherCostLabel);

    	viewPrevOtherCostLabel = new JLabel("- none -");
    	GridBagConstraints gbc_viewPrevOtherCostLabel = new GridBagConstraints();
    	gbc_viewPrevOtherCostLabel.fill = GridBagConstraints.HORIZONTAL;
    	gbc_viewPrevOtherCostLabel.insets = new Insets(0, 0, 0, 5);
    	gbc_viewPrevOtherCostLabel.gridx = 4;
    	gbc_viewPrevOtherCostLabel.gridy = 17;
    	viewCostDistroPane.add(viewPrevOtherCostLabel, gbc_viewPrevOtherCostLabel);
        
        //end view Work Requests
	}
	
	//
	ActionListener workRequestAction = new ActionListener() 
	{
		public void actionPerformed(ActionEvent e) 
		{
			if(e.getSource() == viewGeneralInfoPane)
			{
				try {
					getDataFromSQL(connection);
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			if(e.getSource() == submitWorkRequestBtn)
			{
				try {
					
					saveWRtoMap(); 			//save the data before sending
					//sendWRtoSQLPreparedStatmentTest();
					sendWRtoSQLPreparedStatment(sendMap);	//send the data to the 
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			if(e.getSource() == saveRequestBtn)
			{
				System.out.println("Saving to sendMap...");
				saveWRtoMap();
			}
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
	private JLabel viewRequesterLabel;
	private JTextField viewRequesterTextField;
	private JLabel viewProjectManagerLabel;
	private JTextField viewProjectManagerTextField;
	private JLabel viewWRStatusLabel;
	private JComboBox<WRStatusIDItem> viewWRStatusComboBox;
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
	private JPanel costDistroPane;
	private JPanel remarkSavePane;
	private JTextArea remarksTextArea;
	private JTabbedPane newWorkRequestPane;
	private JLabel newWRNumberLabel;
	private Component verticalStrut;
	private Component horizontalStrut;
	private JLabel newDatePreparedLabel;
	private JLabel lblRequester;
	private JLabel lblProjectManager;
	private JLabel lblWorkRequestStatus;
	private JTextField newWRNumTextField;
	private JTextField newDatePrepTextField;
	private JTextField newRequesterTextField;
	private JTextField newProjectManagerTextField;
	private JComboBox<WRStatusIDItem> newWRStatusComboBox;
	private Component horizontalStrut_1;
	private JLabel lblNewLabel_3;
	private JLabel lblNewLabel_4;
	private JLabel lblNewLabel_5;
	private JTextField dateAddedTextField;
	private JTextField userTextField;
	private JTextArea notesTextField;
	private JButton saveRequestBtn;
	private JButton submitWorkRequestBtn;
	private JTextArea wrRemarkTextArea;
	private Component verticalStrut_6;
	private Component verticalStrut_1;
	private JScrollPane scrollPane;
	private Component horizontalStrut_5;
	private JScrollPane scrollPane_1;
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
	private Component horizontalStrut_4;
	private JPanel workRequestSelectionPane;
	private JTable workRequestsSelectionTable;
	private Component workRequestSelectionStrut;
	private Component workRequestSelectionStrut_1;
	private Component workRequestVertSelectionStrut;
	private Component workRequestVertSelectionStrut_1;
	private JScrollPane scrollPane_7;	
	private JPanel analysisPieChartPane;
	private JLabel analysisWRNumberLabel;
	private Component horizontalStrut_17;
	private Component verticalStrut_12;
	private JLabel analysisPMLabel;
	private JLabel analysisStartDateLabel;
	private JLabel analysisSupervisorLabel;
	private JTextField analysisWRNumberTextField;
	private JTextField analysisPMTextField;
	private JTextField analysisSupervisorTextField;
	private JTextField analysisStartDateFieldText;
	private JLabel analysisDraftDueDateLabel;
	private JTextField analysisDraftDueDateTextField;
	private JPanel analyisOtherPanel;
	private Component verticalStrut_14;
	private Component verticalStrut_15;
	private Component verticalStrut_16;
	
	private JPanel viewCostDistroPane;
	private Component viewCostDistroHorizontalStrut;
	private JLabel viewDiscLabel;
	private JLabel viewDirectTaskLabel;
	private JLabel viewAssigneePrevWRLabel;
	private Component viewCostDistroHorizontalStrut_2;
	private JLabel viewCivilLabel;
	private JLabel viewArchLabel;
	private JLabel viewStructLabel;
	private JLabel viewAntiterrorLabel;
	private JLabel viewMechLabel;
	private JLabel viewFireProtLabel;
	private JLabel viewElectLabel;
	private JLabel viewComLabel;
	private JLabel viewLEEDLabel;
	private JLabel viewEnvLabel;
	private JLabel viewCostEngLabel;
	private JLabel viewGeotechLabel;
	private JLabel viewAEContractLabel;
	private JLabel viewValueEngLabel;
	private JLabel viewTranslatorLabel;
	private JLabel viewSpecLabel;
	private JLabel viewOtherLabel;
	private JSpinner viewCivillSpinner;
	private JSpinner viewArchitecturalSpinner;
	private JSpinner viewStructuralSpinner;
	private JSpinner viewForceProtSpinner;
	private JSpinner viewMechSpinner;
	private JSpinner viewFireProtSpinner;
	private JSpinner viewElectricSpinner;
	private JSpinner viewCommSpinner;
	private JSpinner viewLEEDSpinner;
	private JSpinner viewEnvinronmentalSpinner;
	private JSpinner viewCostEngSpinner;
	private JSpinner viewGeotechSpinner;
	private JSpinner viewAEContSpinner;
	private JSpinner viewValEngSpinner;
	private JSpinner viewTranslatorSpinner;
	private JSpinner viewSpecificationSpinner;
	private JSpinner viewOtherSpinner;
	private JLabel viewCivilCostLabel;
	private JLabel viewArchCostLabel;
	private JLabel viewStructCostLabel;
	private JLabel viewForceProtCostLabel;
	private JLabel viewMechCostLabel;
	private JLabel viewFireProtCostLabel;
	private JLabel viewElectricalCostLabel;
	private JLabel viewCommsCostLabel;
	private JLabel viewLEEDCostLabel;
	private JLabel viewEnvCostLabel;
	private JLabel viewCostEngCostLabel;
	private JLabel viewGeotechCostLabel;
	private JLabel viewAEContractingLabel;
	private JLabel viewValueEngCostLabel;
	private JLabel viewTranslatorCostLabel;
	private JLabel viewSpecCostLabel;
	private JLabel viewOtherCostLabel;
	private JLabel viewPrevCivialCostLabel;
	private JLabel viewPrevArchCostLabel;
	private JLabel viewPrevStrucCostLabel;
	private JLabel viewPrevForceProtCostLabel;
	private JLabel viewPrevMechCostLabel;
	private JLabel viewPrevFireProtCostLabel;
	private JLabel viewPrevElectricalCostLabel;
	private JLabel viewPrevCommsCostLabel;
	private JLabel viewPrevLEEDCostLabel;
	private JLabel viewPrevEnvCostLabel;
	private JLabel viewPrevCostEngCostLabel;
	private JLabel viewPrevGeotechCostLabel;
	private JLabel viewPrevAEContractingLabel;
	private JLabel viewPrevValueEngCostLabel;
	private JLabel viewPrevTranslatorCostLabel;
	private JLabel viewPrevSpecCostLabel;
	private JLabel viewPrevOtherCostLabel;
	private JLabel viewDraftDueDateLabel;
	private JTextField viewDraftDueDateTextField;
	private JLabel viewSupervisorLabel;
	private JTextField viewSupervisorTextField;
	private JLabel viewCompletionDateLabel;
	private JTextField viewCompletionDateTextField;
	private JLabel viewStartDateLabel;
	private JTextField viewStartDateTextField;
	private JLabel newDraftDueDateLabel;
	private JTextField newDraftDueDateTextField;
	private JLabel newSupervisorLabel;
	private JTextField newSupervisorTextField;
	private JLabel newStartDateLabel;
	private JLabel newCompletionDateLabel;
	private JTextField newStartDateTextField;
	private JTextField newCompletionDateTextField;
	private JLabel viewProjectNameLabel;
	private JTextField viewProjectNameTextField;
	private JTextField viewProjectNumberTextField;
	private JTextField viewProgramYearTextField;
	private JTextField viewFundSourceTextField;
	private JLabel viewProjectNumberLabel;
	private JLabel viewProgramYearLabel;
	private JLabel viewFundSourceTextLabel;
	private JLabel newProjectNameLabel;
	private JLabel newProjectNumberLabel;
	private JLabel newProgramYearLabel;
	private JLabel newFundSourceLabel;
	private JTextField newProjectNameTextField;
	private JSpinner newProjectNumberSpinner;
	private JTextField newProgramYearTextField;
	private JSpinner newFundSourceTextField;
	private JLabel pieChartLabel;
	private JLabel gnattChartLabel;
}

//Class for filling the WRStatusID Combo boxes and returning int values
class WRStatusIDItem
{
	private String string;
	private int num;

	WRStatusIDItem(String s, int i) 
	{
		this.string = s;
		this.num = i;
	} 
	
	@Override
	public String toString() { return string; }
	public String getDescription() { return string; }
	public int getInt()	{ return num; }
}

//Class for handling all of the SQL
class SQLHandler
{
	public static Connection connection;
	public static int connectionType;
	protected static String loginInfo;
	
	
	//Send Work Request to sql server to the table
//	public void sendWRtoSQLTableTest() throws SQLException
//	{
//		if(connection.isClosed())
//			sqlConnectionOpen();
//		
//		String sql = "INSERT INTO WRWorkRequest";
//		Statement stmt = connection.createStatement();
//		
//		System.out.println("Inserting new record into WRWorkRequest table...");
//		sql += " VALUES ("
//		/*1@TYPE_ACTION VARCHAR(25)='select ',   */ + "''"
//		/*2@ID INT = 2,                          */ + ", "
//		/*3@AverageRate DECIMAL =1.1,            */ + ", 0"
//		/*4@BranchAssignmentID INT =2,           */ + ", 0"
//		/*5@BranchID INT =2 ,                    */ + ", 0"
//		/*6@CompletionDate DATETIME2='',         */ + ", '2021-12-12 00:00:01'"
//		/*7@DatePrepared DATETIME2='',           */ + ", '2021-12-12 00:00:02'"
//		/*8@DraftDueDate DATETIME='',            */ + ", '2021-12-12 00:00:03'"
//		/*9@FinancialInfoID INT =2,              */ + ", 0"
//		/*10@FY INT =2021,                        */ + ", 0"
//		/*11@ProjectInfoID INT = 2,               */ + ", 0"
//		/*12@ProjectManager VARCHAR(255)='PM',    */ + ", 'Dave'"
//		/*13@ProjectPulseID INT =2,               */ + ", 0"
//		/*14@Requestor VARCHAR(255) ='me',        */ + ", 'Ian'"
	//		/*15@StartDate DATETIME2='',              */ + ", '2021-12-12 00:00:04'"
	//	/*16@SubmissionDate DATETIME2='',         */ + ", 0"
	//	/*17@Supervisor VARCHAR(255)='superivsor',*/ + ", 'Will'"
	//	/*18@WRNumber VARCHAR(255)='wrnum',       */ + ", '13'"
	//	/*19@WRStatusID INT=2                     */ + ", 11"
	//	+ ")";
	//		stmt.executeUpdate(sql);
	//		
	//	sql = "INSERT INTO WRProjectInfo";
	//	stmt = connection.prepareStatement(sql);
	//		
	//	System.out.println("Inserting new record into WRProjectInfo table...");
	//	sql += " VALUES ("
	//	/*1@Background VARCHAR(max)='',       */ + "''"
	//	/*2@BranchId INT=1,                   */ + ", 0"
	//	/*3@ContinentID INT=1,                */ + ", 0"
	//	/*4@ContractNumber VARCHAR(255)='',   */ + "''"
	//	/*5@DirWR INT=1,                      */ + ", 0"
	//	/*6@FundSourceID INT=1,               */ + ", 0"
	//	/*7@FundSourceText VARCHAR(255)='fun',*/ + ", 'fun'"
	//	/*8@InstallationID INT=1,             */ + ", 0"
	//	/*9@LocationID INT=1,                 */ + ", 0"
	//	/*10@MilitarySiteID INT=1,             */ + ", 0"
	//	/*11@OrgCode VARCHAR(255)='org',       */ + ", 0"
	//	/*12@ProgramYear VARCHAR(255)='2021',  */ + ", '2021'"
	//	/*13@ProjectName VARCHAR(255)='name',  */ + ", 'ProjectName'"
	//	/*14@ProjectNotes Varchar(max)='notes',*/ + ", 0"
	//	/*15@ProjectNumber VARCHAR(255)='1',   */ + ", '1111'"
	//	/*16@PulseCityRegionID INT=2,          */ + ", 0"
	//	/*17@PulseCountryID INT =2,            */ + ", 0"
	//	/*18@SectionId INT =1,                 */ + ", 0"
	//	/*19@Site VARCHAR(255)='site',         */ + "''"
	//	/*20@WRID INT=2,                       */ + ", 0"
	//	/*21@TYPE_ACTION VARCHAR(25)='select'  */ + "''"
	//	+ ")";
	//	stmt.executeUpdate(sql);
	//	
	//	stmt.close();
	//	sqlConnectionClose();
	//	System.out.println("Records updated...");
	//}
	
	//Send Work Request to sql server to the table
//	public void sendWRtoSQLTable(SortedMap<String, Object> sendMap) throws SQLException
//	{
//		if(connection.isClosed())
//			sqlConnectionOpen();
//		
//		String sql = "INSERT INTO WRWorkRequest";
//		Statement stmt = connection.createStatement();
//		
//		System.out.println("Inserting new record into WRWorkRequest table...");
//		sql = sql + " ("
//		/*1@TYPE_ACTION VARCHAR(25)='select ',   */ + "''"
//		/*2@ID INT = 2,                          */ + ", 0"
//		/*3@AverageRate DECIMAL =1.1,            */ + ", 0"
//		/*4@BranchAssignmentID INT =2,           */ + ", 0"
//		/*5@BranchID INT =2 ,                    */ + ", 0"
//		/*6@CompletionDate DATETIME2='',         */ + ", " + Timestamp.valueOf(sendMap.get("CompletionDate").toString())
//		/*7@DatePrepared DATETIME2='',           */ + ", " + Timestamp.valueOf(sendMap.get("DatePrepared").toString())
//		/*8@DraftDueDate DATETIME='',            */ + ", " + Timestamp.valueOf(sendMap.get("DraftDueDate").toString())
//		/*9@FinancialInfoID INT =2,              */ + ", 0"
//		/*10@FY INT =2021,                        */ + ", 0"
//		/*11@ProjectInfoID INT = 2,               */ + ", 0"
//		/*12@ProjectManager VARCHAR(255)='PM',    */ + ", '" + sendMap.get("ProjectManager").toString() + "'"
//		/*13@ProjectPulseID INT =2,               */ + ", 0"
//		/*14@Requestor VARCHAR(255) ='me',        */ + ", '" + sendMap.get("Requestor").toString() + "'"
//		/*15@StartDate DATETIME2='',              */ + ", " + Timestamp.valueOf(sendMap.get("StartDate").toString())
//		/*16@SubmissionDate DATETIME2='',         */ + ", 0"
//		/*17@Supervisor VARCHAR(255)='superivsor',*/ + ", '" + sendMap.get("Supervisor").toString() + "'"
//		/*18@WRNumber VARCHAR(255)='wrnum',       */ + ", '" + sendMap.get("WRNumber").toString() + "'"
//		/*19@WRStatusID INT=2                     */ + ", " + sendMap.get("WRStatusID");
//		stmt.executeUpdate(sql);
//		
//		sql = "INSERT INTO WRProjectInfo";
//		stmt = connection.prepareStatement(sql);
//		
//		System.out.println("Inserting new record into WRProjectInfo table...");
//		sql = sql + " ("
//		/*1@Background VARCHAR(max)='',       */ + "''"
//		/*2@BranchId INT=1,                   */ + ", 0"
//		/*3@ContinentID INT=1,                */ + ", 0"
//		/*4@ContractNumber VARCHAR(255)='',   */ + "''"
//		/*5@DirWR INT=1,                      */ + ", 0"
//		/*6@FundSourceID INT=1,               */ + ", 0"
//		/*7@FundSourceText VARCHAR(255)='fun',*/ + ", '" + sendMap.get("FundSourceText").toString() + "'"
//		/*8@InstallationID INT=1,             */ + ", 0"
//		/*9@LocationID INT=1,                 */ + ", 0"
//		/*10@MilitarySiteID INT=1,             */ + ", 0"
//		/*11@OrgCode VARCHAR(255)='org',       */ + ", 0"
//		/*12@ProgramYear VARCHAR(255)='2021',  */ + ", '" + sendMap.get("ProgramYear").toString() + "'"
//		/*13@ProjectName VARCHAR(255)='name',  */ + ", '" + sendMap.get("ProjectName").toString() + "'"
//		/*14@ProjectNotes Varchar(max)='notes',*/ + ", 0"
//		/*15@ProjectNumber VARCHAR(255)='1',   */ + ", '" + sendMap.get("ProjectNumber") + "'"
//		/*16@PulseCityRegionID INT=2,          */ + ", 0"
//		/*17@PulseCountryID INT =2,            */ + ", 0"
//		/*18@SectionId INT =1,                 */ + ", 0"
//		/*19@Site VARCHAR(255)='site',         */ + "''"
//		/*20@WRID INT=2,                       */ + ", 0"
//		/*21@TYPE_ACTION VARCHAR(25)='select'  */ + "''";
//		stmt.executeUpdate(sql);
//		
//		stmt.close();
//		sqlConnectionClose();
//		System.out.println("Records updated...");
//	}
	
	//Send Work Request to sql server using prepared statment
	public void sendWRtoSQLPreparedStatmentTest() throws SQLException
	{
		if(connection.isClosed())
			sqlConnectionOpen();
		
		String sql = "INSERT INTO WRWorkRequest "
				+ "(CompletionDate, DatePrepared, DraftDueDate, ProjectInfoID, ProjectManager, Requestor, StartDate, Supervisor, WRNumber, WRSTatusID) "
				+ "VALUES (?,?,?,?,?,?,?,?,?,?)";
		PreparedStatement pstmt = connection.prepareStatement(sql);
		
		System.out.println("Inserting new record into WRWorkRequest table...");
		
		/*1@TYPE_ACTION VARCHAR(25)='select ',   */ 
		/*2@ID INT = 2,                          */ 
		/*3@AverageRate DECIMAL =1.1,            */ 
		/*4@BranchAssignmentID INT =2,           */ 
		/*5@BranchID INT =2 ,                    */ 
		/*6@CompletionDate DATETIME2='',         */ pstmt.setTimestamp(1, Timestamp.valueOf("2021-12-12 00:00:03"));
		/*7@DatePrepared DATETIME2='',           */ pstmt.setTimestamp(2, Timestamp.valueOf("2021-12-12 00:00:03"));
		/*8@DraftDueDate DATETIME='',            */ pstmt.setTimestamp(3, Timestamp.valueOf("2021-12-12 00:00:03"));
		/*9@FinancialInfoID INT =2,              */
		/*10@FY INT =2021,                        */
		/*11@ProjectInfoID INT = 2,               */ pstmt.setInt(4, 222);
		/*12@ProjectManager VARCHAR(255)='PM',    */ pstmt.setString(5,"ProjectManager");
		/*13@ProjectPulseID INT =2,               */
		/*14@Requestor VARCHAR(255) ='me',        */ pstmt.setString(6, "Requestor");
		/*15@StartDate DATETIME2='',              */ pstmt.setTimestamp(7, Timestamp.valueOf("2021-12-12 00:00:03"));
		/*16@SubmissionDate DATETIME2='',         */ 
		/*17@Supervisor VARCHAR(255)='superivsor',*/ pstmt.setString(8, "Supervisor");
		/*18@WRNumber VARCHAR(255)='wrnum',       */ pstmt.setString(9, "WRNumber");
		/*19@WRStatusID INT=2                     */ pstmt.setInt(10, 5);
		pstmt.executeUpdate();
		
		
		sql = "INSERT INTO WRProjectInfo "
		+ "(DirWR, FundSourceText, ProgramYear, ProjectName, ProjectNumber, WRID) "
		+ "VALUES (?,?,?,?,?,?)";
		pstmt = connection.prepareStatement(sql);
		
		System.out.println("Inserting new record into WRProjectInfo table...");
		
		/*1@Background VARCHAR(max)='',       */
		/*2@BranchId INT=1,                   */
		/*3@ContinentID INT=1,                */
		/*4@ContractNumber VARCHAR(255)='',   */
		/*5@DirWR INT=1,                      */ pstmt.setInt(1, 1);
		/*6@FundSourceID INT=1,               */
		/*7@FundSourceText VARCHAR(255)='fun',*/ pstmt.setString(2, "FundSourceText");
		/*8@InstallationID INT=1,             */
		/*9@LocationID INT=1,                 */
		/*10@MilitarySiteID INT=1,             */
		/*11@OrgCode VARCHAR(255)='org',       */
		/*12@ProgramYear VARCHAR(255)='2021',  */ pstmt.setString(3, "2021 ProgramYear");
		/*13@ProjectName VARCHAR(255)='name',  */ pstmt.setString(4, "ProjectName");
		/*14@ProjectNotes Varchar(max)='notes',*/
		/*15@ProjectNumber VARCHAR(255)='1',   */ pstmt.setString(5, "ProjectNumber - 1");
		/*16@PulseCityRegionID INT=2,          */
		/*17@PulseCountryID INT =2,            */
		/*18@SectionId INT =1,                 */
		/*19@Site VARCHAR(255)='site',         */
		/*20@WRID INT=2,                       */ pstmt.setInt(6, 3);
		/*21@TYPE_ACTION VARCHAR(25)='select'  */
		pstmt.executeUpdate();
		
		pstmt.close();
		sqlConnectionClose();
		
	}
	
	//Send Work Request to sql server using prepared statment
	public void sendWRtoSQLPreparedStatment(SortedMap<String, Object> sendMap) throws SQLException
	{
		if(connection.isClosed())
			sqlConnectionOpen();
		
		String sql = "INSERT INTO WRWorkRequest "
				+ "(CompletionDate, DatePrepared, DraftDueDate, ProjectInfoID, ProjectManager, Requestor, StartDate, Supervisor, WRNumber, WRSTatusID) "
				+ "VALUES (?,?,?,?,?,?,?,?,?,?)";
		PreparedStatement pstmt = connection.prepareStatement(sql);
		
		System.out.println("Inserting new record into WRWorkRequest table...");
		
		/*1@TYPE_ACTION VARCHAR(25)='select ',   */ 
		/*2@ID INT = 2,                          */ 
		/*3@AverageRate DECIMAL =1.1,            */ 
		/*4@BranchAssignmentID INT =2,           */ 
		/*5@BranchID INT =2 ,                    */ System.out.println("CompletionDate :: " + sendMap.get("CompletionDate").toString());
		/*6@CompletionDate DATETIME2='',         */ pstmt.setTimestamp(1, Timestamp.valueOf(sendMap.get("CompletionDate").toString()));
		/*7@DatePrepared DATETIME2='',           */ pstmt.setTimestamp(2, Timestamp.valueOf(sendMap.get("DatePrepared").toString()));
		/*8@DraftDueDate DATETIME='',            */ pstmt.setTimestamp(3, Timestamp.valueOf(sendMap.get("DraftDueDate").toString()));
		/*9@FinancialInfoID INT =2,              */
		/*10@FY INT =2021,                        */
		/*11@ProjectInfoID INT = 2,               */ pstmt.setInt(4, (int) sendMap.get("ProjectInfoID"));
		/*12@ProjectManager VARCHAR(255)='PM',    */ pstmt.setString(5, sendMap.get("ProjectManager").toString());
		/*13@ProjectPulseID INT =2,               */
		/*14@Requestor VARCHAR(255) ='me',        */ pstmt.setString(6, sendMap.get("Requestor").toString());
		/*15@StartDate DATETIME2='',              */ pstmt.setTimestamp(7, Timestamp.valueOf(sendMap.get("StartDate").toString()));
		/*16@SubmissionDate DATETIME2='',         */ 
		/*17@Supervisor VARCHAR(255)='superivsor',*/ pstmt.setString(8, sendMap.get("Supervisor").toString());
		/*18@WRNumber VARCHAR(255)='wrnum',       */ pstmt.setString(9, sendMap.get("WRNumber").toString());
		/*19@WRStatusID INT=2                     */ pstmt.setInt(10, (int) sendMap.get("WRStatusID"));
		pstmt.execute();
		
		
		sql = "INSERT INTO WRProjectInfo "
		+ "(DirWR, FundSourceText, ProgramYear, ProjectName, ProjectNumber, WRID) "
		+ "VALUES (?,?,?,?,?,?)";
		pstmt = connection.prepareStatement(sql);
		
		System.out.println("Inserting new record into WRProjectInfo table...");
		
		/*1@Background VARCHAR(max)='',       */
		/*2@BranchId INT=1,                   */
		/*3@ContinentID INT=1,                */
		/*4@ContractNumber VARCHAR(255)='',   */
		/*5@DirWR INT=1,                      */ pstmt.setInt(1, 1);
		/*6@FundSourceID INT=1,               */
		/*7@FundSourceText VARCHAR(255)='fun',*/ pstmt.setString(2, sendMap.get("FundSourceText").toString());
		/*8@InstallationID INT=1,             */
		/*9@LocationID INT=1,                 */
		/*10@MilitarySiteID INT=1,             */
		/*11@OrgCode VARCHAR(255)='org',       */
		/*12@ProgramYear VARCHAR(255)='2021',  */ pstmt.setString(3, sendMap.get("ProgramYear").toString());
		/*13@ProjectName VARCHAR(255)='name',  */ pstmt.setString(4, sendMap.get("ProjectName").toString());
		/*14@ProjectNotes Varchar(max)='notes',*/
		/*15@ProjectNumber VARCHAR(255)='1',   */ pstmt.setInt(5, (int) sendMap.get("ProjectNumber"));
		/*16@PulseCityRegionID INT=2,          */
		/*17@PulseCountryID INT =2,            */
		/*18@SectionId INT =1,                 */
		/*19@Site VARCHAR(255)='site',         */
		/*20@WRID INT=2,                       */ pstmt.setInt(6, (int) sendMap.get("WRID"));
		/*21@TYPE_ACTION VARCHAR(25)='select'  */
		pstmt.execute();
		
		pstmt.close();
		sqlConnectionClose();
		
	}
	
	//create a SQL connection to localhost
	public static Connection sqlConnectionOpen()
	{
		//default values for server login
		String localurl = "jdbc:sqlserver://localhost;databaseName=Pulse;";
		String localuser = "sa";
		String localpassword = "1234567890";
		
		//if there is login info use that instead of default
		if(loginInfo != null && !loginInfo.isEmpty())
		{
			String[] sArray = loginInfo.split(";");
			localurl = sArray[0] + ';' + sArray[1] + ';';
			localuser = sArray[2];
			localpassword = sArray[3];
		}
		
		//try to connection
		try 
		{
			System.out.println("Attempting to connect to: " + localurl);
			connection = DriverManager.getConnection(localurl, localuser, localpassword);
			System.out.println("Connect to MS SQL Server on Local Host. Good Job Dude.");
		} 
		catch (SQLException e) 
		{
			System.out.println("Oops, there's an error connecting to the LocalHost");
			e.printStackTrace();
		}
		
		//return connection status
		return connection;
				
	}
	
	//Close sql connection
	public static void sqlConnectionClose()
	{
		try 
		{
			connection.close();
		} catch (SQLException e) {
			System.out.println("SQL Connection Failed to Close!");
			e.printStackTrace();
		}
	}

	//get data from the sql server and put in internal data structure
	public static ArrayList<Map<String, Object>> getDataFromSQL(Connection con) throws SQLException 
	{
		//if no active connection, open one
		if(con.isClosed())
			con = sqlConnectionOpen();
		
		//set connection to newly opened con
		if(connection != con)
			connection = con;
		
		ArrayList<Map<String, Object>> paramArrayListMap = null;
		
		String query = "select * from [dbo].[WRWorkRequest]";
		String query2 = "select * from [dbo].[WRProjectInfo]";
		
		ResultSet rs;
		ResultSet rs2;
		CallableStatement cstmt;

		System.out.println("Calling uspWRWorkRequest_ISUD Select...");
		cstmt = con.prepareCall(query); 
    	rs = cstmt.executeQuery();
    	System.out.println("uspWRWorkRequest_ISUD Complete...");
    	
    	System.out.println("Calling uspWRProjectInfo_ISUD Select...");
    	cstmt = con.prepareCall(query2);
    	rs2 = cstmt.executeQuery();
    	System.out.println("uspWRProjectInfo_ISUD Complete...");

        paramArrayListMap = new ArrayList<Map<String, Object>>();
        while(rs.next() && rs2.next())
        {
            SortedMap<String,Object> paramMap = new TreeMap<String,Object>();
            
            //Get data from WRWorkRequest
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
    		
    		//Get data from WRProjectInfo
        	paramMap.put("Background", rs2.getString("Background"));
        	paramMap.put("BranchId", rs2.getInt("BranchId"));
        	paramMap.put("ContinentID", rs2.getInt("ContinentID"));
        	paramMap.put("ContractNumber", rs2.getString("ContractNumber"));
        	paramMap.put("DirWR", rs2.getInt("DirWR"));
        	paramMap.put("FundSourceID", rs2.getInt("FundSourceID"));
        	paramMap.put("FundSourceText", rs2.getString("FundSourceText"));
        	paramMap.put("InstallationID", rs2.getInt("InstallationID"));
        	paramMap.put("LocationID", rs2.getInt("LocationID"));
        	paramMap.put("MilitarySiteID", rs2.getInt("MilitarySiteID"));
        	paramMap.put("OrgCode", rs2.getString("OrgCode"));
        	paramMap.put("ProgramYear", rs2.getString("ProgramYear"));
        	paramMap.put("ProjectName", rs2.getString("ProjectName"));
        	paramMap.put("ProjectNotes", rs2.getString("ProjectNotes"));
        	paramMap.put("ProjectNumber", rs2.getString("ProjectNumber"));
        	paramMap.put("PulseCityRegionID", rs2.getInt("PulseCityRegionID"));
        	paramMap.put("PulseCountryID", rs2.getInt("PulseCountryID"));
        	paramMap.put("SectionId", rs2.getInt("SectionId"));
        	paramMap.put("Site", rs2.getString("Site"));
        	paramMap.put("WRID", rs2.getInt("WRID"));
        	
        	//Generates cost distribution fields for analysis graph data
        	paramMap.putAll(generateCostDistFields());
        	
        	//Add paramMap to the arraylist for each record
    		paramArrayListMap.add(paramMap);
    		//paramMap.forEach((key, value) -> System.out.println(key + ": " + value));
    		//System.out.println("ArrayListSize: " + paramArrayListMap.size());
        }
        
        //close connection
        rs.close();
        rs2.close();
        cstmt.close();
        sqlConnectionClose();
        
	    return paramArrayListMap;
	}
	
	//Generates random values for cost distribution fields
	public static Map<String,Object> generateCostDistFields()
	{
		SortedMap<String,Object> costMap = new TreeMap<String,Object>();
		
		costMap.put("costCivil", (Math.floor(Math.random() * (100) + 1) * 100) / 100);
		costMap.put("costArchitectural", (Math.floor(Math.random() * (100) + 1) * 100) / 100);
		costMap.put("costStructural", (Math.floor(Math.random() * (100) + 1) * 100) / 100);
		costMap.put("costForceProtection", (Math.floor(Math.random() * (100) + 1) * 100) / 100);
		costMap.put("costMechanical", (Math.floor(Math.random() * (100) + 1) * 100) / 100);
		costMap.put("costFireProtection", (Math.floor(Math.random() * (100) + 1) * 100) / 100);
		costMap.put("costElectrical", (Math.floor(Math.random() * (100) + 1) * 100) / 100);
		costMap.put("costCommunications", (Math.floor(Math.random() * (100) + 1) * 100) / 100);
		costMap.put("costLEED", (Math.floor(Math.random() * (100) + 1) * 100) / 100);
		costMap.put("costEnvironmental", (Math.floor(Math.random() * (100) + 1) * 100) / 100);
		costMap.put("costCostEngineer", (Math.floor(Math.random() * (100) + 1) * 100) / 100);
		costMap.put("costGeotechnical", (Math.floor(Math.random() * (100) + 1) * 100) / 100);
		costMap.put("costAEContracting", (Math.floor(Math.random() * (100) + 1) * 100) / 100);
		costMap.put("costValueEngineer", (Math.floor(Math.random() * (100) + 1) * 100) / 100);
		costMap.put("costTranslator", (Math.floor(Math.random() * (100) + 1) * 100) / 100);
		costMap.put("costSpecifications", (Math.floor(Math.random() * (100) + 1) * 100) / 100);
		costMap.put("costOther", (Math.floor(Math.random() * (100) + 1) * 100) / 100);
		
		costMap.put("costHoursCivil", (Math.floor(Math.random() * (100) + 1) * 100) / 100);
		costMap.put("costHoursArchitectural", (Math.floor(Math.random() * (100) + 1) * 100) / 100);
		costMap.put("costHoursStructural", (Math.floor(Math.random() * (100) + 1) * 100) / 100);
		costMap.put("costHoursForceProtection", (Math.floor(Math.random() * (100) + 1) * 100) / 100);
		costMap.put("costHoursMechanical", (Math.floor(Math.random() * (100) + 1) * 100) / 100);
		costMap.put("costHoursFireProtection", (Math.floor(Math.random() * (100) + 1) * 100) / 100);
		costMap.put("costHoursElectrical", (Math.floor(Math.random() * (100) + 1) * 100) / 100);
		costMap.put("costHoursCommunications", (Math.floor(Math.random() * (100) + 1) * 100) / 100);
		costMap.put("costHoursLEED", (Math.floor(Math.random() * (100) + 1) * 100) / 100);
		costMap.put("costHoursEnvironmental", (Math.floor(Math.random() * (100) + 1) * 100) / 100);
		costMap.put("costHoursCostEngineer", (Math.floor(Math.random() * (100) + 1) * 100) / 100);
		costMap.put("costHoursGeotechnical", (Math.floor(Math.random() * (100) + 1) * 100) / 100);
		costMap.put("costHoursAEContracting", (Math.floor(Math.random() * (100) + 1) * 100) / 100);
		costMap.put("costHoursValueEngineer", (Math.floor(Math.random() * (100) + 1) * 100) / 100);
		costMap.put("costHoursTranslator", (Math.floor(Math.random() * (100) + 1) * 100) / 100);
		costMap.put("costHoursSpecifications", (Math.floor(Math.random() * (100) + 1) * 100) / 100);
		costMap.put("costHoursOther", (Math.floor(Math.random() * (100) + 1) * 100) / 100);
		
		return costMap;
	}
	
}