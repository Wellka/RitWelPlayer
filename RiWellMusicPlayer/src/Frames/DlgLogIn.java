package Frames;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JCheckBox;

public class DlgLogIn extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JTextField textField;
	private JPasswordField passwordField;

	/**
	 * Create the dialog.
	 */
	public DlgLogIn(Frame frm,boolean modal) {
		super(frm, modal);
		
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 303, 244);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		{
			textField = new JTextField();
			textField.setBounds(137, 91, 140, 20);
			contentPanel.add(textField);
			textField.setColumns(10);
		}
		
		passwordField = new JPasswordField();
		passwordField.setBounds(137, 122, 140, 20);
		contentPanel.add(passwordField);
		
		JLabel lblNewLabel = new JLabel("Login Name:");
		lblNewLabel.setBounds(10, 94, 117, 14);
		contentPanel.add(lblNewLabel);
		
		JLabel lblPasswort = new JLabel("Passwort:");
		lblPasswort.setBounds(10, 125, 117, 14);
		contentPanel.add(lblPasswort);
		
		JPanel panel = new JPanel();
		panel.setBounds(10, 11, 267, 69);
		contentPanel.add(panel);
		
		JLabel lblTodoImage = new JLabel("TODO IMAGE");
		panel.add(lblTodoImage);
		
		JCheckBox chckbxPasswortMerken = new JCheckBox("Passwort merken");
		chckbxPasswortMerken.setBounds(137, 149, 140, 23);
		contentPanel.add(chckbxPasswortMerken);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			
			JButton btnNewButton = new JButton("Account Erstellen");
			final JDialog dlg1 = this;
			btnNewButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					new DlgAccountCreation(dlg1, true);
				}
			});
			btnNewButton.setHorizontalAlignment(SwingConstants.LEADING);
			buttonPane.add(btnNewButton);
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
