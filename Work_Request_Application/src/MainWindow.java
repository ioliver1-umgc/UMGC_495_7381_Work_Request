import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.JLabel;
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
import javax.swing.JTabbedPane;
import java.awt.FlowLayout;
import javax.swing.JTextField;
import javax.swing.JCheckBox;
import javax.swing.JTextArea;
import javax.swing.JComboBox;
import javax.swing.JRadioButton;
import java.awt.SystemColor;
import javax.swing.SwingConstants;
import javax.swing.BoxLayout;


public class MainWindow {

	JFrame frmWorkRequestApplication;
	JPanel newRequestPane;
	JPanel requestAnalyticsPane;
	JPanel generalInfoPane;
	JPanel projectInfoPane;
	JPanel assignmentTypePane;
	JPanel costDistroPane;
	JPanel personalAssignmentPane;
	JPanel remarkSavePane;
	JTextArea remarksTextArea;

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
		frmWorkRequestApplication.setBounds(100, 100, 907, 730);
		frmWorkRequestApplication.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frmWorkRequestApplication.getContentPane().setLayout(new BorderLayout(0, 0));
        
        JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        frmWorkRequestApplication.getContentPane().add(tabbedPane);
               
        newRequestPane = new JPanel();
        tabbedPane.addTab("New Work Request", null, newRequestPane, null);
        
        requestAnalyticsPane = new JPanel();
        tabbedPane.addTab("Analytics", null, requestAnalyticsPane, null);
        newRequestPane.setLayout(new BoxLayout(newRequestPane, BoxLayout.Y_AXIS));
        
        newWorkRequestPane = new JTabbedPane(JTabbedPane.TOP);
        newRequestPane.add(newWorkRequestPane);
        
        remarksTextArea = new JTextArea();
        remarkSavePane = new JPanel();
        newRequestPane.add(remarkSavePane);
        GridBagLayout gbl_remarkSavePane = new GridBagLayout();
        gbl_remarkSavePane.columnWidths = new int[]{0, 0, 0, 0, 0};
        gbl_remarkSavePane.rowHeights = new int[]{0, 0, 0, 0, 0, 0};
        gbl_remarkSavePane.columnWeights = new double[]{0.0, 1.0, 1.0, 0.0, Double.MIN_VALUE};
        gbl_remarkSavePane.rowWeights = new double[]{0.0, 1.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
        remarkSavePane.setLayout(gbl_remarkSavePane);
        
        verticalStrut_1 = Box.createVerticalStrut(20);
        GridBagConstraints gbc_verticalStrut_1 = new GridBagConstraints();
        gbc_verticalStrut_1.insets = new Insets(0, 0, 5, 5);
        gbc_verticalStrut_1.gridx = 1;
        gbc_verticalStrut_1.gridy = 0;
        remarkSavePane.add(verticalStrut_1, gbc_verticalStrut_1);
        
        scrollPane = new JScrollPane();
        GridBagConstraints gbc_scrollPane = new GridBagConstraints();
        gbc_scrollPane.fill = GridBagConstraints.BOTH;
        gbc_scrollPane.gridheight = 2;
        gbc_scrollPane.gridwidth = 2;
        gbc_scrollPane.insets = new Insets(0, 0, 5, 5);
        gbc_scrollPane.gridx = 1;
        gbc_scrollPane.gridy = 1;
        remarkSavePane.add(scrollPane, gbc_scrollPane);
        
        textArea = new JTextArea();
        scrollPane.setViewportView(textArea);
        
        horizontalStrut_5 = Box.createHorizontalStrut(20);
        GridBagConstraints gbc_horizontalStrut_5 = new GridBagConstraints();
        gbc_horizontalStrut_5.insets = new Insets(0, 0, 5, 0);
        gbc_horizontalStrut_5.gridx = 3;
        gbc_horizontalStrut_5.gridy = 1;
        remarkSavePane.add(horizontalStrut_5, gbc_horizontalStrut_5);
        
        horizontalStrut_4 = Box.createHorizontalStrut(20);
        GridBagConstraints gbc_horizontalStrut_4 = new GridBagConstraints();
        gbc_horizontalStrut_4.insets = new Insets(0, 0, 5, 5);
        gbc_horizontalStrut_4.gridx = 0;
        gbc_horizontalStrut_4.gridy = 2;
        remarkSavePane.add(horizontalStrut_4, gbc_horizontalStrut_4);
        
        saveRequestBtn = new JButton("Save");
        GridBagConstraints gbc_saveRequestBtn = new GridBagConstraints();
        gbc_saveRequestBtn.insets = new Insets(0, 0, 5, 5);
        gbc_saveRequestBtn.gridx = 1;
        gbc_saveRequestBtn.gridy = 3;
        remarkSavePane.add(saveRequestBtn, gbc_saveRequestBtn);
        
        submitWorkRequestBtn = new JButton("Submit to PA");
        GridBagConstraints gbc_submitWorkRequestBtn = new GridBagConstraints();
        gbc_submitWorkRequestBtn.insets = new Insets(0, 0, 5, 5);
        gbc_submitWorkRequestBtn.gridx = 2;
        gbc_submitWorkRequestBtn.gridy = 3;
        remarkSavePane.add(submitWorkRequestBtn, gbc_submitWorkRequestBtn);
        
        verticalStrut_6 = Box.createVerticalStrut(20);
        GridBagConstraints gbc_verticalStrut_6 = new GridBagConstraints();
        gbc_verticalStrut_6.insets = new Insets(0, 0, 0, 5);
        gbc_verticalStrut_6.gridx = 0;
        gbc_verticalStrut_6.gridy = 4;
        remarkSavePane.add(verticalStrut_6, gbc_verticalStrut_6);
        
        generalInfoPane = new JPanel();
        newWorkRequestPane.addTab("General Information", null, generalInfoPane, null);
        GridBagLayout gbl_generalInfoPane = new GridBagLayout();
        gbl_generalInfoPane.columnWidths = new int[]{0, 0, 0, 273, 0, 0, 0};
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
        gbl_projectInfoPane.columnWidths = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0};
        gbl_projectInfoPane.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        gbl_projectInfoPane.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, Double.MIN_VALUE};
        gbl_projectInfoPane.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
        projectInfoPane.setLayout(gbl_projectInfoPane);
        
        verticalStrut_5 = Box.createVerticalStrut(20);
        GridBagConstraints gbc_verticalStrut_5 = new GridBagConstraints();
        gbc_verticalStrut_5.insets = new Insets(0, 0, 5, 5);
        gbc_verticalStrut_5.gridx = 1;
        gbc_verticalStrut_5.gridy = 0;
        projectInfoPane.add(verticalStrut_5, gbc_verticalStrut_5);
        
        horizontalStrut_3 = Box.createHorizontalStrut(20);
        GridBagConstraints gbc_horizontalStrut_3 = new GridBagConstraints();
        gbc_horizontalStrut_3.insets = new Insets(0, 0, 5, 0);
        gbc_horizontalStrut_3.gridx = 7;
        gbc_horizontalStrut_3.gridy = 0;
        projectInfoPane.add(horizontalStrut_3, gbc_horizontalStrut_3);
        
        horizontalStrut_2 = Box.createHorizontalStrut(20);
        GridBagConstraints gbc_horizontalStrut_2 = new GridBagConstraints();
        gbc_horizontalStrut_2.insets = new Insets(0, 0, 5, 5);
        gbc_horizontalStrut_2.gridx = 1;
        gbc_horizontalStrut_2.gridy = 1;
        projectInfoPane.add(horizontalStrut_2, gbc_horizontalStrut_2);
        
        projectNameLabel = new JLabel("Project Name");
        GridBagConstraints gbc_projectNameLabel = new GridBagConstraints();
        gbc_projectNameLabel.anchor = GridBagConstraints.EAST;
        gbc_projectNameLabel.insets = new Insets(0, 0, 5, 5);
        gbc_projectNameLabel.gridx = 2;
        gbc_projectNameLabel.gridy = 1;
        projectInfoPane.add(projectNameLabel, gbc_projectNameLabel);
        
        textField = new JTextField();
        GridBagConstraints gbc_textField = new GridBagConstraints();
        gbc_textField.gridwidth = 4;
        gbc_textField.insets = new Insets(0, 0, 5, 5);
        gbc_textField.fill = GridBagConstraints.HORIZONTAL;
        gbc_textField.gridx = 3;
        gbc_textField.gridy = 1;
        projectInfoPane.add(textField, gbc_textField);
        textField.setColumns(10);
        
        projectNumLabel = new JLabel("Project Number");
        GridBagConstraints gbc_projectNumLabel = new GridBagConstraints();
        gbc_projectNumLabel.anchor = GridBagConstraints.EAST;
        gbc_projectNumLabel.insets = new Insets(0, 0, 5, 5);
        gbc_projectNumLabel.gridx = 2;
        gbc_projectNumLabel.gridy = 2;
        projectInfoPane.add(projectNumLabel, gbc_projectNumLabel);
        
        textField_1 = new JTextField();
        textField_1.setColumns(10);
        GridBagConstraints gbc_textField_1 = new GridBagConstraints();
        gbc_textField_1.gridwidth = 4;
        gbc_textField_1.insets = new Insets(0, 0, 5, 5);
        gbc_textField_1.fill = GridBagConstraints.HORIZONTAL;
        gbc_textField_1.gridx = 3;
        gbc_textField_1.gridy = 2;
        projectInfoPane.add(textField_1, gbc_textField_1);
        
        painLabel = new JLabel("PAIN");
        GridBagConstraints gbc_painLabel = new GridBagConstraints();
        gbc_painLabel.anchor = GridBagConstraints.EAST;
        gbc_painLabel.insets = new Insets(0, 0, 5, 5);
        gbc_painLabel.gridx = 2;
        gbc_painLabel.gridy = 3;
        projectInfoPane.add(painLabel, gbc_painLabel);
        
        textField_2 = new JTextField();
        textField_2.setColumns(10);
        GridBagConstraints gbc_textField_2 = new GridBagConstraints();
        gbc_textField_2.gridwidth = 4;
        gbc_textField_2.insets = new Insets(0, 0, 5, 5);
        gbc_textField_2.fill = GridBagConstraints.HORIZONTAL;
        gbc_textField_2.gridx = 3;
        gbc_textField_2.gridy = 3;
        projectInfoPane.add(textField_2, gbc_textField_2);
        
        amountLabel = new JLabel("Program Amount");
        GridBagConstraints gbc_amountLabel = new GridBagConstraints();
        gbc_amountLabel.anchor = GridBagConstraints.EAST;
        gbc_amountLabel.insets = new Insets(0, 0, 5, 5);
        gbc_amountLabel.gridx = 2;
        gbc_amountLabel.gridy = 4;
        projectInfoPane.add(amountLabel, gbc_amountLabel);
        
        textField_3 = new JTextField();
        textField_3.setColumns(10);
        GridBagConstraints gbc_textField_3 = new GridBagConstraints();
        gbc_textField_3.gridwidth = 4;
        gbc_textField_3.insets = new Insets(0, 0, 5, 5);
        gbc_textField_3.fill = GridBagConstraints.HORIZONTAL;
        gbc_textField_3.gridx = 3;
        gbc_textField_3.gridy = 4;
        projectInfoPane.add(textField_3, gbc_textField_3);
        
        proYearLabel = new JLabel("Program Year");
        GridBagConstraints gbc_proYearLabel = new GridBagConstraints();
        gbc_proYearLabel.anchor = GridBagConstraints.EAST;
        gbc_proYearLabel.insets = new Insets(0, 0, 5, 5);
        gbc_proYearLabel.gridx = 2;
        gbc_proYearLabel.gridy = 5;
        projectInfoPane.add(proYearLabel, gbc_proYearLabel);
        
        programYearTextField = new JTextField();
        GridBagConstraints gbc_programYearTextField = new GridBagConstraints();
        gbc_programYearTextField.gridwidth = 4;
        gbc_programYearTextField.insets = new Insets(0, 0, 5, 5);
        gbc_programYearTextField.fill = GridBagConstraints.HORIZONTAL;
        gbc_programYearTextField.gridx = 3;
        gbc_programYearTextField.gridy = 5;
        projectInfoPane.add(programYearTextField, gbc_programYearTextField);
        programYearTextField.setColumns(10);
        
        p2Label = new JLabel("P2 Number");
        GridBagConstraints gbc_p2Label = new GridBagConstraints();
        gbc_p2Label.anchor = GridBagConstraints.EAST;
        gbc_p2Label.insets = new Insets(0, 0, 5, 5);
        gbc_p2Label.gridx = 2;
        gbc_p2Label.gridy = 6;
        projectInfoPane.add(p2Label, gbc_p2Label);
        
        p2TextField = new JTextField();
        p2TextField.setText("");
        GridBagConstraints gbc_p2TextField = new GridBagConstraints();
        gbc_p2TextField.gridwidth = 4;
        gbc_p2TextField.insets = new Insets(0, 0, 5, 5);
        gbc_p2TextField.fill = GridBagConstraints.HORIZONTAL;
        gbc_p2TextField.gridx = 3;
        gbc_p2TextField.gridy = 6;
        projectInfoPane.add(p2TextField, gbc_p2TextField);
        p2TextField.setColumns(10);
        
        contractNumberLabel = new JLabel("Contract Number");
        GridBagConstraints gbc_contractNumberLabel = new GridBagConstraints();
        gbc_contractNumberLabel.anchor = GridBagConstraints.EAST;
        gbc_contractNumberLabel.insets = new Insets(0, 0, 5, 5);
        gbc_contractNumberLabel.gridx = 2;
        gbc_contractNumberLabel.gridy = 7;
        projectInfoPane.add(contractNumberLabel, gbc_contractNumberLabel);
        
        contractNumTextField = new JTextField();
        contractNumTextField.setText("");
        GridBagConstraints gbc_contractNumTextField = new GridBagConstraints();
        gbc_contractNumTextField.gridwidth = 4;
        gbc_contractNumTextField.insets = new Insets(0, 0, 5, 5);
        gbc_contractNumTextField.fill = GridBagConstraints.HORIZONTAL;
        gbc_contractNumTextField.gridx = 3;
        gbc_contractNumTextField.gridy = 7;
        projectInfoPane.add(contractNumTextField, gbc_contractNumTextField);
        contractNumTextField.setColumns(10);
        
        fundSrcLabel = new JLabel("Fund Source");
        GridBagConstraints gbc_fundSrcLabel = new GridBagConstraints();
        gbc_fundSrcLabel.anchor = GridBagConstraints.EAST;
        gbc_fundSrcLabel.insets = new Insets(0, 0, 5, 5);
        gbc_fundSrcLabel.gridx = 2;
        gbc_fundSrcLabel.gridy = 8;
        projectInfoPane.add(fundSrcLabel, gbc_fundSrcLabel);
        
        fundSourceComboBox = new JComboBox();
        GridBagConstraints gbc_fundSourceComboBox = new GridBagConstraints();
        gbc_fundSourceComboBox.gridwidth = 4;
        gbc_fundSourceComboBox.insets = new Insets(0, 0, 5, 5);
        gbc_fundSourceComboBox.fill = GridBagConstraints.HORIZONTAL;
        gbc_fundSourceComboBox.gridx = 3;
        gbc_fundSourceComboBox.gridy = 8;
        projectInfoPane.add(fundSourceComboBox, gbc_fundSourceComboBox);
        
        sectionLabel = new JLabel("Section");
        GridBagConstraints gbc_sectionLabel = new GridBagConstraints();
        gbc_sectionLabel.anchor = GridBagConstraints.EAST;
        gbc_sectionLabel.insets = new Insets(0, 0, 5, 5);
        gbc_sectionLabel.gridx = 2;
        gbc_sectionLabel.gridy = 9;
        projectInfoPane.add(sectionLabel, gbc_sectionLabel);
        
        sectionComboBox = new JComboBox();
        GridBagConstraints gbc_sectionComboBox = new GridBagConstraints();
        gbc_sectionComboBox.gridwidth = 4;
        gbc_sectionComboBox.insets = new Insets(0, 0, 5, 5);
        gbc_sectionComboBox.fill = GridBagConstraints.HORIZONTAL;
        gbc_sectionComboBox.gridx = 3;
        gbc_sectionComboBox.gridy = 9;
        projectInfoPane.add(sectionComboBox, gbc_sectionComboBox);
        
        countryLabel = new JLabel("Country");
        GridBagConstraints gbc_countryLabel = new GridBagConstraints();
        gbc_countryLabel.anchor = GridBagConstraints.EAST;
        gbc_countryLabel.insets = new Insets(0, 0, 5, 5);
        gbc_countryLabel.gridx = 2;
        gbc_countryLabel.gridy = 10;
        projectInfoPane.add(countryLabel, gbc_countryLabel);
        
        countryComboBox = new JComboBox();
        GridBagConstraints gbc_countryComboBox = new GridBagConstraints();
        gbc_countryComboBox.gridwidth = 4;
        gbc_countryComboBox.insets = new Insets(0, 0, 5, 5);
        gbc_countryComboBox.fill = GridBagConstraints.HORIZONTAL;
        gbc_countryComboBox.gridx = 3;
        gbc_countryComboBox.gridy = 10;
        projectInfoPane.add(countryComboBox, gbc_countryComboBox);
        
        regionLabel = new JLabel("City/Region");
        GridBagConstraints gbc_regionLabel = new GridBagConstraints();
        gbc_regionLabel.anchor = GridBagConstraints.EAST;
        gbc_regionLabel.insets = new Insets(0, 0, 5, 5);
        gbc_regionLabel.gridx = 2;
        gbc_regionLabel.gridy = 11;
        projectInfoPane.add(regionLabel, gbc_regionLabel);
        
        regionComboBox = new JComboBox();
        GridBagConstraints gbc_regionComboBox = new GridBagConstraints();
        gbc_regionComboBox.gridwidth = 4;
        gbc_regionComboBox.insets = new Insets(0, 0, 5, 5);
        gbc_regionComboBox.fill = GridBagConstraints.HORIZONTAL;
        gbc_regionComboBox.gridx = 3;
        gbc_regionComboBox.gridy = 11;
        projectInfoPane.add(regionComboBox, gbc_regionComboBox);
        
        siteLabel = new JLabel("Site");
        GridBagConstraints gbc_siteLabel = new GridBagConstraints();
        gbc_siteLabel.anchor = GridBagConstraints.EAST;
        gbc_siteLabel.insets = new Insets(0, 0, 5, 5);
        gbc_siteLabel.gridx = 2;
        gbc_siteLabel.gridy = 12;
        projectInfoPane.add(siteLabel, gbc_siteLabel);
        
        siteComboBox = new JComboBox();
        GridBagConstraints gbc_siteComboBox = new GridBagConstraints();
        gbc_siteComboBox.gridwidth = 4;
        gbc_siteComboBox.insets = new Insets(0, 0, 5, 5);
        gbc_siteComboBox.fill = GridBagConstraints.HORIZONTAL;
        gbc_siteComboBox.gridx = 3;
        gbc_siteComboBox.gridy = 12;
        projectInfoPane.add(siteComboBox, gbc_siteComboBox);
        
        analystSuperLabel = new JLabel("Program Analyst Supervisor");
        GridBagConstraints gbc_analystSuperLabel = new GridBagConstraints();
        gbc_analystSuperLabel.anchor = GridBagConstraints.EAST;
        gbc_analystSuperLabel.insets = new Insets(0, 0, 5, 5);
        gbc_analystSuperLabel.gridx = 2;
        gbc_analystSuperLabel.gridy = 13;
        projectInfoPane.add(analystSuperLabel, gbc_analystSuperLabel);
        
        programAnalystSupervisorComboBox = new JComboBox();
        GridBagConstraints gbc_programAnalystSupervisorComboBox = new GridBagConstraints();
        gbc_programAnalystSupervisorComboBox.gridwidth = 4;
        gbc_programAnalystSupervisorComboBox.insets = new Insets(0, 0, 5, 5);
        gbc_programAnalystSupervisorComboBox.fill = GridBagConstraints.HORIZONTAL;
        gbc_programAnalystSupervisorComboBox.gridx = 3;
        gbc_programAnalystSupervisorComboBox.gridy = 13;
        projectInfoPane.add(programAnalystSupervisorComboBox, gbc_programAnalystSupervisorComboBox);
        
        branchLabel = new JLabel("Branch");
        GridBagConstraints gbc_branchLabel = new GridBagConstraints();
        gbc_branchLabel.anchor = GridBagConstraints.EAST;
        gbc_branchLabel.insets = new Insets(0, 0, 5, 5);
        gbc_branchLabel.gridx = 2;
        gbc_branchLabel.gridy = 14;
        projectInfoPane.add(branchLabel, gbc_branchLabel);
        
        branchTextField = new JTextField();
        GridBagConstraints gbc_branchTextField = new GridBagConstraints();
        gbc_branchTextField.gridwidth = 4;
        gbc_branchTextField.insets = new Insets(0, 0, 5, 5);
        gbc_branchTextField.fill = GridBagConstraints.HORIZONTAL;
        gbc_branchTextField.gridx = 3;
        gbc_branchTextField.gridy = 14;
        projectInfoPane.add(branchTextField, gbc_branchTextField);
        branchTextField.setColumns(10);
        
        directProLabel = new JLabel("Direct/Indirect Project");
        GridBagConstraints gbc_directProLabel = new GridBagConstraints();
        gbc_directProLabel.insets = new Insets(0, 0, 5, 5);
        gbc_directProLabel.gridx = 2;
        gbc_directProLabel.gridy = 15;
        projectInfoPane.add(directProLabel, gbc_directProLabel);
        
        rdbtnDirect = new JRadioButton("Direct");
        GridBagConstraints gbc_rdbtnDirect = new GridBagConstraints();
        gbc_rdbtnDirect.insets = new Insets(0, 0, 5, 5);
        gbc_rdbtnDirect.gridx = 3;
        gbc_rdbtnDirect.gridy = 15;
        projectInfoPane.add(rdbtnDirect, gbc_rdbtnDirect);
        
        rdbtnIndirect = new JRadioButton("Indirect");
        GridBagConstraints gbc_rdbtnIndirect = new GridBagConstraints();
        gbc_rdbtnIndirect.insets = new Insets(0, 0, 5, 5);
        gbc_rdbtnIndirect.gridx = 4;
        gbc_rdbtnIndirect.gridy = 15;
        projectInfoPane.add(rdbtnIndirect, gbc_rdbtnIndirect);
        
        rdbtnNotApplicable = new JRadioButton("Not Applicable");
        GridBagConstraints gbc_rdbtnNotApplicable = new GridBagConstraints();
        gbc_rdbtnNotApplicable.insets = new Insets(0, 0, 5, 5);
        gbc_rdbtnNotApplicable.gridx = 5;
        gbc_rdbtnNotApplicable.gridy = 15;
        projectInfoPane.add(rdbtnNotApplicable, gbc_rdbtnNotApplicable);
        
        workReqDocFolderLabel = new JLabel("Work Request Document Folder");
        GridBagConstraints gbc_workReqDocFolderLabel = new GridBagConstraints();
        gbc_workReqDocFolderLabel.anchor = GridBagConstraints.EAST;
        gbc_workReqDocFolderLabel.insets = new Insets(0, 0, 5, 5);
        gbc_workReqDocFolderLabel.gridx = 2;
        gbc_workReqDocFolderLabel.gridy = 16;
        projectInfoPane.add(workReqDocFolderLabel, gbc_workReqDocFolderLabel);
        
        wrPathTextField = new JTextField();
        GridBagConstraints gbc_wrPathTextField = new GridBagConstraints();
        gbc_wrPathTextField.gridwidth = 4;
        gbc_wrPathTextField.insets = new Insets(0, 0, 5, 5);
        gbc_wrPathTextField.fill = GridBagConstraints.HORIZONTAL;
        gbc_wrPathTextField.gridx = 3;
        gbc_wrPathTextField.gridy = 16;
        projectInfoPane.add(wrPathTextField, gbc_wrPathTextField);
        wrPathTextField.setColumns(10);
        
        verticalStrut_4 = Box.createVerticalStrut(20);
        GridBagConstraints gbc_verticalStrut_4 = new GridBagConstraints();
        gbc_verticalStrut_4.insets = new Insets(0, 0, 0, 5);
        gbc_verticalStrut_4.gridx = 0;
        gbc_verticalStrut_4.gridy = 17;
        projectInfoPane.add(verticalStrut_4, gbc_verticalStrut_4);
        
        assignmentTypePane = new JPanel();
        newWorkRequestPane.addTab("Assignment Type", null, assignmentTypePane, null);
        
        costDistroPane = new JPanel();
        newWorkRequestPane.addTab("Cost Distribution", null, costDistroPane, null);
        
        personalAssignmentPane = new JPanel();
        newWorkRequestPane.addTab("Personal Assignment", null, personalAssignmentPane, null);   	
		
	}
	
	ActionListener exitAction = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			System.exit(0);
			
		}
		
	};
	private JTabbedPane newWorkRequestPane;
	private JPanel panel;
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
	private Component verticalStrut_4;
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;
	private JTextField textField_3;
	private Component horizontalStrut_2;
	private Component verticalStrut_5;
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
	private JTextArea textArea;
	private Component horizontalStrut_4;
	private Component verticalStrut_6;
	private Component verticalStrut_1;
	private JScrollPane scrollPane;
	private Component horizontalStrut_5;
	private JScrollPane scrollPane_1;

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
