package Frames;
import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JFormattedTextField;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.text.NumberFormat;

public class DlgConnectionOption extends JDialog {
	private static final long serialVersionUID = 1L;
	
	private JTextField edtServerAddress;
	private JTextField edtLogin;
	private JTextField edtPassword;
	private JFormattedTextField formattedTextField;

	private int result = -1;
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
		setBounds(100, 100, 430, 243);
		getContentPane().setLayout(new BorderLayout());
		
		JPanel ContentPane = new JPanel();
		getContentPane().add(ContentPane, BorderLayout.CENTER);
		ContentPane.setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		ContentPane.add(panel, BorderLayout.NORTH);
		
		JLabel lblWarunungUndSo = new JLabel("<HTML>" +
				"Hier wird einmal eine warnung stehen\r\n" +
				"<br>Hier wird einmal eine warnung stehen\r\n" +
				"<br>Hier wird einmal eine warnung stehen\r\n" +
				"<br>Hier wird einmal eine warnung stehen" +
				"</HTML>");
		panel.add(lblWarunungUndSo);
		
		JPanel panel_1 = new JPanel();
		ContentPane.add(panel_1, BorderLayout.CENTER);
		panel_1.setLayout(null);
		
		JLabel lblDatenbank = new JLabel("Server Adresse");
		lblDatenbank.setBounds(10, 11, 102, 14);
		panel_1.add(lblDatenbank);
		
		JLabel lblServerPort = new JLabel("Server Port");
		lblServerPort.setBounds(10, 36, 102, 14);
		panel_1.add(lblServerPort);
		
		JLabel lblSuperUserLogin = new JLabel("Superuser Login");
		lblSuperUserLogin.setBounds(10, 61, 102, 14);
		panel_1.add(lblSuperUserLogin);
		
		JLabel lblSuperUserPasswort = new JLabel("Superuser Passwort");
		lblSuperUserPasswort.setBounds(10, 86, 102, 14);
		panel_1.add(lblSuperUserPasswort);
		
		edtServerAddress = new JTextField();
		edtServerAddress.setBounds(147, 8, 257, 20);
		panel_1.add(edtServerAddress);
		edtServerAddress.setColumns(10);
		
		edtLogin = new JTextField();
		edtLogin.setColumns(10);
		edtLogin.setBounds(147, 58, 257, 20);
		panel_1.add(edtLogin);
		
		edtPassword = new JTextField();
		edtPassword.setColumns(10);
		edtPassword.setBounds(147, 83, 257, 20);
		panel_1.add(edtPassword);
		
		NumberFormat nf = NumberFormat.getInstance();
		nf.setGroupingUsed(false);
		formattedTextField = new JFormattedTextField(nf);
		formattedTextField.setBounds(147, 33, 257, 20);
		panel_1.add(formattedTextField);
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
}

