package Frames;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JDesktopPane;
import java.awt.BorderLayout;
import javax.swing.JInternalFrame;
import javax.swing.JTable;
import javax.swing.JScrollBar;
import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.border.BevelBorder;
import javax.swing.border.LineBorder;
import java.awt.Color;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class FrmMain {

	private JFrame frame;
	private JTable tblMusik;
	private JTable tblFrinds;
	private JTextField edtSearch;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					FrmMain window = new FrmMain();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public FrmMain() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setResizable(false);
		frame.setBounds(100, 100, 726, 480);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		tblMusik = new JTable();
		tblMusik.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		tblMusik.setBounds(0, 71, 544, 281);
		frame.getContentPane().add(tblMusik);
		
		tblFrinds = new JTable();
		tblFrinds.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		tblFrinds.setBounds(554, 71, 156, 281);
		frame.getContentPane().add(tblFrinds);
		
		JButton btnPlay = new JButton("Play");
		btnPlay.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnPlay.setBounds(157, 411, 89, 23);
		frame.getContentPane().add(btnPlay);
		
		JButton btnStop = new JButton("Stop");
		btnStop.setBounds(256, 411, 89, 23);
		frame.getContentPane().add(btnStop);
		
		JButton btnForw = new JButton("<<");
		btnForw.setBounds(388, 411, 49, 23);
		frame.getContentPane().add(btnForw);
		
		JButton btnBack = new JButton(">>");
		btnBack.setBounds(447, 411, 49, 23);
		frame.getContentPane().add(btnBack);
		
		JPanel pnlPic = new JPanel();
		pnlPic.setBounds(32, 363, 82, 71);
		frame.getContentPane().add(pnlPic);
		
		JPanel pnlLogo = new JPanel();
		pnlLogo.setBounds(554, 11, 156, 49);
		frame.getContentPane().add(pnlLogo);
		
		JLabel lblTrack = new JLabel("");
		lblTrack.setBounds(144, 375, 375, 25);
		frame.getContentPane().add(lblTrack);
		
		JButton btnAddBuddy = new JButton("Add Buddy");
		btnAddBuddy.setBounds(554, 363, 146, 23);
		frame.getContentPane().add(btnAddBuddy);
		
		JButton btnDelBuddy = new JButton("Del Buddy");
		btnDelBuddy.setBounds(554, 389, 146, 23);
		frame.getContentPane().add(btnDelBuddy);
		
		edtSearch = new JTextField();
		edtSearch.setBounds(10, 28, 340, 20);
		frame.getContentPane().add(edtSearch);
		edtSearch.setColumns(10);
		
		JButton btnSearch = new JButton("Search");
		btnSearch.setBounds(376, 27, 89, 23);
		frame.getContentPane().add(btnSearch);
	}
}
