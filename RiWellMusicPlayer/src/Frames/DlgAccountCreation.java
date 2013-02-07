package Frames;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JPasswordField;
import javax.swing.JLabel;
import javax.swing.JTextField;

import cal.Encriptions;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.sql.SQLException;
import java.sql.Statement;

public class DlgAccountCreation extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JPasswordField edtPW;
	private JPasswordField edtPWConfirm;
	private JTextField Benutzername;
	private JTextField edtEmail;
	private JTextField edtEmailConfirm;
	private JTextField edtSecondName;

	private boolean result;
	private Statement statement;
	private JTextField edtFirstName;
	/**
	 * Create the dialog.
	 */
	public DlgAccountCreation(Statement state){
		this(null, true, state);
	}
	/**
	 * @wbp.parser.constructor
	 */
	public DlgAccountCreation(JDialog dlg1, boolean modal, Statement state) {
		super(dlg1, modal);
		if(state == null){
			return;
		}
		statement = state;
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		
		setBounds(100, 100, 365, 311);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		{
			edtPW = new JPasswordField();
			edtPW.setBounds(201, 46, 138, 20);
			contentPanel.add(edtPW);
		}
		{
			JLabel lblPasswort = new JLabel("Passwort:");
			lblPasswort.setBounds(10, 49, 78, 14);
			contentPanel.add(lblPasswort);
		}
		{
			JLabel lblNewLabel = new JLabel("Passwort best\u00E4tigen:");
			lblNewLabel.setBounds(10, 80, 138, 14);
			contentPanel.add(lblNewLabel);
		}
		{
			edtPWConfirm = new JPasswordField();
			edtPWConfirm.setBounds(201, 77, 138, 20);
			contentPanel.add(edtPWConfirm);
		}
		
		Benutzername = new JTextField();
		Benutzername.setBounds(201, 15, 105, 20);
		contentPanel.add(Benutzername);
		Benutzername.setColumns(10);
		
		JButton btnTestUsername = new JButton("C");
		btnTestUsername.setToolTipText("Pr\u00FCfen ob dieser benutzername schon vorhanden ist");
		btnTestUsername.setBounds(307, 12, 32, 23);
		contentPanel.add(btnTestUsername);
		
		JLabel lblBenutzername = new JLabel("Benutzername");
		lblBenutzername.setBounds(10, 18, 78, 14);
		contentPanel.add(lblBenutzername);
		
		edtEmail = new JTextField();
		edtEmail.setBounds(201, 108, 138, 20);
		contentPanel.add(edtEmail);
		edtEmail.setColumns(10);
		
		edtEmailConfirm = new JTextField();
		edtEmailConfirm.setBounds(201, 139, 138, 20);
		contentPanel.add(edtEmailConfirm);
		edtEmailConfirm.setColumns(10);
		
		JLabel lblEmailadresse = new JLabel("E-Mail Adresse:");
		lblEmailadresse.setBounds(10, 111, 138, 14);
		contentPanel.add(lblEmailadresse);
		
		JLabel lblEmailAdresseBesttigen = new JLabel("E-Mail Adresse best\u00E4tigen:");
		lblEmailAdresseBesttigen.setBounds(10, 142, 138, 14);
		contentPanel.add(lblEmailAdresseBesttigen);
		
		JLabel lblEmpfolenVon = new JLabel("Vorname:");
		lblEmpfolenVon.setBounds(10, 173, 138, 14);
		contentPanel.add(lblEmpfolenVon);
		
		edtSecondName = new JTextField();
		edtSecondName.setBounds(201, 201, 138, 20);
		contentPanel.add(edtSecondName);
		edtSecondName.setColumns(10);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						if((result = createUser())){
							setVisible(false);
							//JOptionPane.showMessageDialog(null, "Benutzer wurde angelegt", "Erfolg", 1);
						}else{
							JOptionPane.showMessageDialog(null, "Benutzer konnte nicht erstellt werden", "Fehler", 0);
						}
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Abbrechen");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						result = false;
						setVisible(false);
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
		Benutzername.setText("1");
		edtEmail.setText("2");
		edtEmailConfirm.setText("3");
		
		edtFirstName = new JTextField();
		edtFirstName.setBounds(201, 170, 138, 20);
		contentPanel.add(edtFirstName);
		edtFirstName.setColumns(10);
		
		JLabel lblNachname = new JLabel("Nachname:");
		lblNachname.setBounds(10, 204, 138, 14);
		contentPanel.add(lblNachname);
		setVisible(modal);
	}
	
	private boolean createUser(){
		String password = new String(edtPW.getPassword());
		boolean bAllOk = true;
		//passwort darf nicht leer sein und beide müssen gleich sein
		if(!edtEmail.getText().equals(edtEmailConfirm.getText()) || edtEmail.getText().equals("")){
			edtEmail.setBackground(new Color(255,200,200));
			edtEmailConfirm.setBackground(new Color(255,200,200));
			bAllOk = false;
		}
		if((password.equals("")) || !(new String(edtPWConfirm.getPassword()).equals(password))){
			edtPW.setBackground(new Color(255,200,200));
			edtPWConfirm.setBackground(new Color(255,200,200));
			bAllOk = false;
		}
		if(bAllOk){
			try {
				String querry = "INSERT INTO Benutzer( benutzer, email, vorname, nachname, passwort) VALUES ('" + Benutzername.getText() + "','" + edtEmail.getText() + "','" + edtFirstName.getText() + "','"+ edtSecondName.getText()+ "','" + Encriptions.StringToMD5(password)+"');";
				System.out.println(querry);
				System.out.println(querry.substring(100));
				statement.execute(querry);
				return true;
			} catch (SQLException e) {
				System.out.println(e.getErrorCode());
				e.printStackTrace();
				return false;
			}
		}
		return false;
	}
	
	public boolean getResult(){
		return result;
	}
}
