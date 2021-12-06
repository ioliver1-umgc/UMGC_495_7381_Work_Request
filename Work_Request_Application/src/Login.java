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
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JRadioButton;

public class Login extends JFrame {

	private JPanel contentPane;
	private JTextField userNameTextField;
	private JTextField passwordTextField;

	/**
	 * Launch the application.
	 */
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

	/**
	 * Create the frame.
	 */
	public Login() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 437, 258);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(240, 240, 240));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]{-8, 0, 58, 110, 40, 73, 39};
		gbl_contentPane.rowHeights = new int[]{88, 0, 0, 0, 0, 0, 0};
		gbl_contentPane.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0};
		gbl_contentPane.rowWeights = new double[]{0.0, 0.0, 0.0, 1.0, 0.0, 1.0, Double.MIN_VALUE};
		contentPane.setLayout(gbl_contentPane);
		
		JTextPane titleTextPane = new JTextPane();
		titleTextPane.setBackground(new Color(240, 240, 240));
		titleTextPane.setFont(new Font("Arial", Font.BOLD, 24));
		titleTextPane.setEditable(false);
		titleTextPane.setText("Work Request Login");
		GridBagConstraints gbc_titleTextPane = new GridBagConstraints();
		gbc_titleTextPane.fill = GridBagConstraints.HORIZONTAL;
		gbc_titleTextPane.gridwidth = 4;
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
		userNameTextField.setText("Username");
		GridBagConstraints gbc_userNameTextField = new GridBagConstraints();
		gbc_userNameTextField.gridwidth = 4;
		gbc_userNameTextField.insets = new Insets(0, 0, 5, 5);
		gbc_userNameTextField.fill = GridBagConstraints.HORIZONTAL;
		gbc_userNameTextField.gridx = 2;
		gbc_userNameTextField.gridy = 1;
		contentPane.add(userNameTextField, gbc_userNameTextField);
		userNameTextField.setColumns(10);
		
		Component horizontalStrut_1 = Box.createHorizontalStrut(20);
		GridBagConstraints gbc_horizontalStrut_1 = new GridBagConstraints();
		gbc_horizontalStrut_1.insets = new Insets(0, 0, 5, 0);
		gbc_horizontalStrut_1.gridx = 6;
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
		passwordTextField.setText("Password");
		GridBagConstraints gbc_passwordTextField = new GridBagConstraints();
		gbc_passwordTextField.gridwidth = 4;
		gbc_passwordTextField.insets = new Insets(0, 0, 5, 5);
		gbc_passwordTextField.fill = GridBagConstraints.HORIZONTAL;
		gbc_passwordTextField.gridx = 2;
		gbc_passwordTextField.gridy = 2;
		contentPane.add(passwordTextField, gbc_passwordTextField);
		passwordTextField.setColumns(10);
		
		JRadioButton sqlAzureRadioBtn = new JRadioButton("Azure Connection");
		GridBagConstraints gbc_sqlAzureRadioBtn = new GridBagConstraints();
		gbc_sqlAzureRadioBtn.gridwidth = 2;
		gbc_sqlAzureRadioBtn.insets = new Insets(0, 0, 5, 5);
		gbc_sqlAzureRadioBtn.gridx = 2;
		gbc_sqlAzureRadioBtn.gridy = 3;
		contentPane.add(sqlAzureRadioBtn, gbc_sqlAzureRadioBtn);
		
		JRadioButton sqlLocalRadioBtn = new JRadioButton("Local Sql Connection");
		sqlLocalRadioBtn.setSelected(true);
		GridBagConstraints gbc_sqlLocalRadioBtn = new GridBagConstraints();
		gbc_sqlLocalRadioBtn.gridwidth = 2;
		gbc_sqlLocalRadioBtn.insets = new Insets(0, 0, 5, 5);
		gbc_sqlLocalRadioBtn.gridx = 4;
		gbc_sqlLocalRadioBtn.gridy = 3;
		contentPane.add(sqlLocalRadioBtn, gbc_sqlLocalRadioBtn);
				
		Component verticalStrut = Box.createVerticalStrut(20);
		GridBagConstraints gbc_verticalStrut = new GridBagConstraints();
		gbc_verticalStrut.insets = new Insets(0, 0, 0, 5);
		gbc_verticalStrut.gridx = 5;
		gbc_verticalStrut.gridy = 5;
		contentPane.add(verticalStrut, gbc_verticalStrut);
		
		JButton submitBtn = new JButton("Submit");
		submitBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//perform a check of a database of users and stored passwords instead of the default values
				if(userNameTextField.getText().equals("Username") && passwordTextField.getText().equals("Password"))
				{
					System.out.println("radio butten local is: " + sqlLocalRadioBtn.isSelected());
					MainWindow window = new MainWindow(sqlLocalRadioBtn.isSelected());
					window.frmWorkRequestApplication.setVisible(true);
					setVisible(false);
				}
				else
				{
					JOptionPane.showMessageDialog(contentPane, "Invalid username or password");
				}
			}
		});
		
		Component verticalStrut_1 = Box.createVerticalStrut(20);
		GridBagConstraints gbc_verticalStrut_1 = new GridBagConstraints();
		gbc_verticalStrut_1.insets = new Insets(0, 0, 5, 0);
		gbc_verticalStrut_1.gridx = 6;
		gbc_verticalStrut_1.gridy = 3;
		contentPane.add(verticalStrut_1, gbc_verticalStrut_1);
		GridBagConstraints gbc_submitBtn = new GridBagConstraints();
		gbc_submitBtn.fill = GridBagConstraints.HORIZONTAL;
		gbc_submitBtn.gridwidth = 2;
		gbc_submitBtn.insets = new Insets(0, 0, 5, 5);
		gbc_submitBtn.gridx = 4;
		gbc_submitBtn.gridy = 4;
		contentPane.add(submitBtn, gbc_submitBtn);
		
	}

}
