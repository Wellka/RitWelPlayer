package Frames;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JPasswordField;
import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class DlgAccountCreation extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JPasswordField passwordField;
	private JPasswordField passwordField_1;
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;
	private JTextField textField_3;

	/**
	 * Create the dialog.
	 */
	public DlgAccountCreation(JDialog dlg1, boolean modal) {
		super(dlg1, modal);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		
		setBounds(100, 100, 365, 278);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		{
			passwordField = new JPasswordField();
			passwordField.setBounds(201, 46, 138, 20);
			contentPanel.add(passwordField);
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
			passwordField_1 = new JPasswordField();
			passwordField_1.setBounds(201, 77, 138, 20);
			contentPanel.add(passwordField_1);
		}
		
		textField = new JTextField();
		textField.setBounds(201, 15, 105, 20);
		contentPanel.add(textField);
		textField.setColumns(10);
		
		JButton btnNewButton = new JButton("C");
		btnNewButton.setToolTipText("Pr\u00FCfen ob dieser benutzername schon vorhanden ist");
		btnNewButton.setBounds(307, 12, 32, 23);
		contentPanel.add(btnNewButton);
		
		JLabel lblBenutzername = new JLabel("Benutzername");
		lblBenutzername.setBounds(10, 18, 78, 14);
		contentPanel.add(lblBenutzername);
		
		textField_1 = new JTextField();
		textField_1.setBounds(201, 108, 138, 20);
		contentPanel.add(textField_1);
		textField_1.setColumns(10);
		
		textField_2 = new JTextField();
		textField_2.setBounds(201, 139, 138, 20);
		contentPanel.add(textField_2);
		textField_2.setColumns(10);
		
		JLabel lblEmailadresse = new JLabel("E-Mail Adresse:");
		lblEmailadresse.setBounds(10, 111, 138, 14);
		contentPanel.add(lblEmailadresse);
		
		JLabel lblEmailAdresseBesttigen = new JLabel("E-Mail Adresse best\u00E4tigen:");
		lblEmailAdresseBesttigen.setBounds(10, 142, 138, 14);
		contentPanel.add(lblEmailAdresseBesttigen);
		
		JLabel lblEmpfolenVon = new JLabel("Empfolen von:");
		lblEmpfolenVon.setBounds(10, 167, 138, 14);
		contentPanel.add(lblEmpfolenVon);
		
		textField_3 = new JTextField();
		textField_3.setBounds(201, 170, 138, 20);
		contentPanel.add(textField_3);
		textField_3.setColumns(10);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						setVisible(false);
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
						setVisible(false);
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
		setVisible(modal);
	}
}
