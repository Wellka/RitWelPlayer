package Frames.Dialogs;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import cal.ImagePanel;

public class DlgLogIn extends JDialog {
	/**
	 * @author Kay Wellinger
	 */
	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JTextField textField;
	private JPasswordField passwordField;
	private int result;

	private JLabel lblStatus;
	/**
	 * Create the dialog.
	 */
	public DlgLogIn(){
		this(null, true);
	}
	public DlgLogIn(Frame frm,boolean modal) {
		super(frm, modal);
		setTitle("HalfBeatPlayer - Login");
		result = -1;
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 303, 237);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		{
			textField = new JTextField();
			textField.setBounds(137, 78, 140, 20);
			contentPanel.add(textField);
			textField.setColumns(10);
		}
		
		passwordField = new JPasswordField();
		passwordField.setBounds(137, 109, 140, 20);
		contentPanel.add(passwordField);
		
		JLabel lblNewLabel = new JLabel("Login Name:");
		lblNewLabel.setBounds(10, 81, 117, 14);
		contentPanel.add(lblNewLabel);
		
		JLabel lblPasswort = new JLabel("Passwort:");
		lblPasswort.setBounds(10, 112, 117, 14);
		contentPanel.add(lblPasswort);
		
		ImagePanel panel = new ImagePanel();
		panel.setBounds(63, 11, 160, 50);
		contentPanel.add(panel);
		try {
			panel.setImage(ImageIO.read(new File("./player.jpg")));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		lblStatus = new JLabel("");
		lblStatus.setBounds(10, 140, 267, 14);
		contentPanel.add(lblStatus);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			
			JButton btnNewButton = new JButton("Account Erstellen");
			//final JDialog dlg1 = this;
			btnNewButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					//new DlgAccountCreation(dlg1, true);
					result = 2;//dialog erstellen
					setVisible(false);
				}
			});
			btnNewButton.setHorizontalAlignment(SwingConstants.LEADING);
			buttonPane.add(btnNewButton);
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
	
	public String getPassword(){
		return new String(passwordField.getPassword());
	}
	
	public String getUsername(){
		return textField.getText();
	}
	
	public int getResult(){
		return result;
	}
	
	public void setStatus(String text, Color color){
		lblStatus.setForeground(color);
		lblStatus.setText(text);
	}
	
	public void setVisible(boolean vis){
		if(vis){
			result = -1;
		}
		super.setVisible(vis);
	}
	
}
