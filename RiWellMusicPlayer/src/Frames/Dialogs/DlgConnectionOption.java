package Frames.Dialogs;
import java.awt.BorderLayout;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * @author Florian Ritterbusch
 */
public class DlgConnectionOption extends JDialog {
	private static final long serialVersionUID = 1L;
	
	private JTextField edtServerAddress;
	private JTextField edtLogin;
	private JTextField edtPassword;
	private JFormattedTextField formattedTextField;

	private int result = -1;
	private JTextField txtDbprojekt;
	/**
	 * Create the dialog.
	 */
	public DlgConnectionOption(){
		this(null, true);
	}
	
	public DlgConnectionOption(JDialog dlg, boolean modal) {
		super(dlg, modal);
		setTitle("Verbindungsoptionen");
		setResizable(false);
		setBounds(100, 100, 503, 243);
		getContentPane().setLayout(new BorderLayout());
		
		JPanel ContentPane = new JPanel();
		getContentPane().add(ContentPane, BorderLayout.CENTER);
		ContentPane.setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		ContentPane.add(panel, BorderLayout.NORTH);
		
		JLabel lblWarunungUndSo = new JLabel("<HTML>Hinweis:\r\n<br>Bitte geben Sie die Daten f\u00FCr den Datenbank-Connect an.</HTML>");
		panel.add(lblWarunungUndSo);
		
		JPanel panel_1 = new JPanel();
		ContentPane.add(panel_1, BorderLayout.CENTER);
		panel_1.setLayout(null);
		
		final JLabel lblDatenbank = new JLabel("Server Adresse");
		lblDatenbank.setBounds(10, 11, 172, 14);
		panel_1.add(lblDatenbank);
		
		final JLabel lblServerPort = new JLabel("Server Port");
		lblServerPort.setBounds(10, 36, 172, 14);
		panel_1.add(lblServerPort);
		
		final JLabel lblSuperUserLogin = new JLabel("DB User Login");
		lblSuperUserLogin.setBounds(10, 61, 172, 14);
		panel_1.add(lblSuperUserLogin);
		
		final JLabel lblSuperUserPasswort = new JLabel("DB User Passwort");
		lblSuperUserPasswort.setBounds(10, 86, 172, 14);
		panel_1.add(lblSuperUserPasswort);
		
		edtServerAddress = new JTextField();
		edtServerAddress.setText("localhost");
		edtServerAddress.setBounds(192, 11, 257, 20);
		panel_1.add(edtServerAddress);
		edtServerAddress.setColumns(10);
		
		edtLogin = new JTextField();
		edtLogin.setText("projekt");
		edtLogin.setColumns(10);
		edtLogin.setBounds(192, 61, 257, 20);
		panel_1.add(edtLogin);
		
		edtPassword = new JTextField();
		edtPassword.setText("geheim");
		edtPassword.setColumns(10);
		edtPassword.setBounds(192, 86, 257, 20);
		panel_1.add(edtPassword);
		
		NumberFormat nf = NumberFormat.getInstance();
		nf.setGroupingUsed(false);
		formattedTextField = new JFormattedTextField(nf);
		formattedTextField.setText("5432");
		formattedTextField.setBounds(192, 36, 257, 20);
		panel_1.add(formattedTextField);
		
		final JCheckBox chckbxDatenbankAnlegen = new JCheckBox("Datenbank anlegen");
		chckbxDatenbankAnlegen.setEnabled(false);
		chckbxDatenbankAnlegen.setBounds(331, 166, 129, 23);
		panel_1.add(chckbxDatenbankAnlegen);
		
		JLabel lblDatabase = new JLabel("Datenbank name");
		lblDatabase.setBounds(10, 111, 172, 14);
		panel_1.add(lblDatabase);
		
		txtDbprojekt = new JTextField();
		txtDbprojekt.setText("dbprojekt");
		txtDbprojekt.setColumns(10);
		txtDbprojekt.setBounds(192, 111, 257, 20);
		panel_1.add(txtDbprojekt);
		chckbxDatenbankAnlegen.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) 
			{
				if( chckbxDatenbankAnlegen.isSelected() )
				{
					lblSuperUserLogin.setText("DB Superuser Login");
					lblSuperUserPasswort.setText("DB Superuser Passwort");
				}
				else
				{
					lblSuperUserLogin.setText("DB User Login");
					lblSuperUserPasswort.setText("DB User Passwort");
				}
			}
		});
		
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						result = 1;
						setVisible(false);
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						result = 0;
						setVisible(false);
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
		setVisible(true);
		
//7cbx
	}
	
	public String getServerName(){
		return edtServerAddress.getText();
	}
	public int getServerPort(){
		return Integer.parseInt(formattedTextField.getText());
	}
	public String getLogin(){
		return edtLogin.getText();
	}
	public String getPassword(){
		return edtPassword.getText();
	}
	
	public int getResult(){
		return result;
	}
	
	public String getDatabase(){
		return txtDbprojekt.getText();
	}
}

