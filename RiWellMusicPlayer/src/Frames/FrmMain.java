package Frames;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.JLabel;
import javax.swing.JButton;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;
import java.io.File;
import java.io.IOException;

import javax.swing.border.BevelBorder;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.jaudiotagger.tag.reference.PictureTypes;

import cal.ImagePanel;

import musikData.MusikInformation;

import Frames.Dialogs.DlgFileSearch;
import Main.Kontrolle;
import SQL.PSQLConnection;

public class FrmMain {

	private JFrame frame;
	private JTable tblMusik;
	private JTable tblFrinds;
	private JTextField edtSearch;

	private ImagePanel pnlPic;
	private Kontrolle kontrolle;
	/**
	 * Create the application.
	 */
	public FrmMain(Kontrolle kontrolle) {
		initialize();
		this.kontrolle = kontrolle;
		frame.setVisible(true);
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
        KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
        manager.addKeyEventDispatcher(new MyDispatcher());
		
		tblMusik = new JTable();
		tblMusik.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		tblMusik.setBounds(0, 71, 544, 281);
		frame.getContentPane().add(tblMusik);
		
		tblFrinds = new JTable();
		tblFrinds.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		tblFrinds.setBounds(554, 71, 160, 281);
		frame.getContentPane().add(tblFrinds);
		
		JButton btnStop = new JButton("Stop");
		btnStop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				stopSong();
			}
		});
		btnStop.setBounds(256, 411, 89, 23);
		frame.getContentPane().add(btnStop);
		
		JButton btnForw = new JButton("<<");
		btnForw.setBounds(388, 411, 49, 23);
		frame.getContentPane().add(btnForw);
		
		JButton btnBack = new JButton(">>");
		btnBack.setBounds(447, 411, 49, 23);
		frame.getContentPane().add(btnBack);
		
		pnlPic = new ImagePanel();
		pnlPic.setBounds(10, 359, 90, 90);
		frame.getContentPane().add(pnlPic);
		
		ImagePanel pnlLogo = new ImagePanel();
		pnlLogo.setBounds(554, 11, 160, 50);
		frame.getContentPane().add(pnlLogo);
		try {
			pnlLogo.setImage(ImageIO.read(new File(getClass().getResource("/resource/player.jpg").getPath())));
		} catch (IOException e1) {}
		
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
		edtSearch.setBounds(5, 49, 440, 20);
		frame.getContentPane().add(edtSearch);
		edtSearch.setColumns(10);
		
		JButton btnSearch = new JButton("Search");
		btnSearch.setBounds(455, 48, 89, 23);
		frame.getContentPane().add(btnSearch);
		
		JButton btnNewButton = new JButton("Search and upload new songs");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				new DlgFileSearch(kontrolle.getSqlConnection(), 1);
			}
		});
		btnNewButton.setBounds(5, 11, 539, 27);
		frame.getContentPane().add(btnNewButton);
		
		JButton btnPlay = new JButton("Play");
		btnPlay.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				playSong();
			}
		});
		btnPlay.setBounds(157, 411, 89, 23);
		frame.getContentPane().add(btnPlay);
		
	}
	
	private class MyDispatcher implements KeyEventDispatcher {
		@Override
        public boolean dispatchKeyEvent(KeyEvent e) {
            if (e.getID() == KeyEvent.KEY_PRESSED) {
            	if((e.getModifiers()&2) == 2 && e.getKeyCode() == KeyEvent.VK_P){
            		playSong();
            	}
            } else if (e.getID() == KeyEvent.KEY_RELEASED) {
               
            } else if (e.getID() == KeyEvent.KEY_TYPED) {
               
            }
            return false;
        }
   	}	 
	 
	private void playSong(){
		MusikInformation mInformation = new MusikInformation("C:\\Musik\\Pendulum - Immersion\\05 - crush.mp3");
		BufferedImage albumart = mInformation.getAlbumArt();
		albumart = pnlPic.scaleImage(albumart, pnlPic.getWidth(), pnlPic.getHeight());
		
		pnlPic.setImage(albumart);
		kontrolle.playMusik("C:\\Musik\\Pendulum - Immersion\\05 - crush.mp3");
	}
	
	private void stopSong(){
		kontrolle.stopMusik();
	}
}
