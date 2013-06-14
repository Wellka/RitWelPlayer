package Frames.Dialogs;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

/**
 * @author Florian Ritterbusch
 */
public class DlgAddFriend extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JTextField textField;
	private String userString;

	/**
	 * Create the dialog.
	 */
	public DlgAddFriend(){
		this(null, true);
	}
	
	public DlgAddFriend(JDialog dlg, boolean modal) {
		super(dlg, modal);
		setBounds(100, 100, 450, 130);
		contentPanel.setBounds(0, 0, 434, 61);
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		//DlgAddFriend dialog = new DlgAddFriend();
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		getContentPane().setLayout(null);
		getContentPane().add(contentPanel);
		contentPanel.setLayout(null);
		
		textField = new JTextField();
		textField.setBounds(10, 28, 414, 20);
		contentPanel.add(textField);
		textField.setColumns(10);
		
		JLabel lblNewLabel = new JLabel("Benutzername");
		lblNewLabel.setBounds(10, 11, 414, 14);
		contentPanel.add(lblNewLabel);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setBounds(0, 59, 434, 32);
			getContentPane().add(buttonPane);
			{
				JButton okButton = new JButton("OK");
				okButton.setBounds(270, 5, 64, 23);
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						userString = textField.getText();
						setVisible(false);
					}
				});
				buttonPane.setLayout(null);
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						userString = "";
						setVisible(false);
					}
				});
				cancelButton.setBounds(338, 5, 91, 23);
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
		this.setVisible(true);
	}
	public String getString(){
		return userString;
	}
}
