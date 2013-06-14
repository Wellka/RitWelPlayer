package Frames;

import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.TableModelListener;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableModel;

import musikData.MusikInformation;
import Frames.Dialogs.DlgAddFriend;
import Frames.Dialogs.DlgFileSearch;
import Frames.Dialogs.DlgStatistik;
import Main.Kontrolle;
import cal.FileFunction;
import cal.ImagePanel;

/**
 * @author Kay Wellinger
 */
public class FrmMain {

	//objekte die nachträglich verändert werden müssen, mussten hier oben eingetragen werden
	private JFrame frame;
	private JTextField edtSearch;

	private ImagePanel pnlPic;
	private Kontrolle kontrolle;
	private ArrayList<MusikInformation> musikinformations = null;
	private JTable tblMusik;
	private JButton btnPlay;
	private JTable tblFriends;
	private int selectedUserID;
	/**
	 * Create the application.
	 */
	public FrmMain(Kontrolle kontrolle) {
		this.kontrolle = kontrolle;
		initialize();
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

		frame.getContentPane().add(new JScrollPane());
		JButton btnStop = new JButton("Stop");
		btnStop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				stopSong();
			}
		});
		btnStop.setBounds(256, 411, 89, 23);
		frame.getContentPane().add(btnStop);
		
		JButton btnReload = new JButton("Reload/Reset");
		btnReload.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//zurücksetzen
				selectedUserID = Kontrolle.ALL_USERS;
				edtSearch.setText("");
				updateGlobalMusikInformations(null);
			}
		});
		btnReload.setBounds(401, 359, 143, 23);
		frame.getContentPane().add(btnReload);
		
		pnlPic = new ImagePanel();
		pnlPic.setBounds(10, 359, 90, 90);
		frame.getContentPane().add(pnlPic);
		
		ImagePanel pnlLogo = new ImagePanel();
		pnlLogo.setBounds(554, 11, 160, 50);
		frame.getContentPane().add(pnlLogo);
		try {
			pnlLogo.setImage(ImageIO.read(new File("./player.jpg").getAbsoluteFile()));
		} catch (IOException e1) {}
		
		JLabel lblTrack = new JLabel("");
		lblTrack.setBounds(144, 375, 375, 25);
		frame.getContentPane().add(lblTrack);
		
		JButton btnAddBuddy = new JButton("Add Buddy");
		btnAddBuddy.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String benutzername = new DlgAddFriend().getString();
				if(benutzername != ""){
					String query = "SELECT ID FROM benutzer WHERE benutzer = ?;";
					PreparedStatement ps = null;
					try{
						ps = kontrolle.getSqlConnection().getConnection().prepareStatement(query);
						ps.setString(1, benutzername);
						ResultSet res = ps.executeQuery();
		
						//wenn benutzer vorhanden dann
						if(res.next()){
							int id = res.getInt(1);
							query = "INSERT INTO benutzer_benutzer (id_benutzer1, id_benutzer2) VALUES (?, ?);";
							ps = kontrolle.getSqlConnection().getConnection().prepareStatement(query);
							ps.setInt(1, kontrolle.getActiveUserID());
							ps.setInt(2, id);
							ps.execute();
							
							kontrolle.updateUserFriends();	
							tblFriends.setModel(new FriendTableModel());
						}else{
							JOptionPane.showMessageDialog(frame, "Benutzer nicht vorhanden");
						}
					}catch(SQLException e){
						e.printStackTrace();
					}	
				}
			}
		});
		btnAddBuddy.setBounds(554, 411, 101, 23);
		frame.getContentPane().add(btnAddBuddy);
		
		JButton btnDelBuddy = new JButton("Del");
		btnDelBuddy.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				PreparedStatement ps = null;
				if(tblFriends.getSelectedRow() < 0)
					return;
				int id = kontrolle.getUserFriends().get(tblFriends.getSelectedRow());
				String query = "DELETE FROM benutzer_benutzer WHERE id_benutzer1 = ? AND id_benutzer2 = ?;";
				try{
					ps = kontrolle.getSqlConnection().getConnection().prepareStatement(query);
					ps.setInt(1, kontrolle.getActiveUserID());
					ps.setInt(2, id);
					ps.execute();
					
					kontrolle.updateUserFriends();	
					tblFriends.setModel(new FriendTableModel());
				}catch(SQLException err){
					err.printStackTrace();
				}
			}
		});
		btnDelBuddy.setBounds(654, 411, 60, 23);
		frame.getContentPane().add(btnDelBuddy);
		
		edtSearch = new JTextField();
		edtSearch.setBounds(5, 49, 440, 20);
		frame.getContentPane().add(edtSearch);
		edtSearch.setColumns(10);
		
		JButton btnSearch = new JButton("Search");
		btnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				updateGlobalMusikInformations(edtSearch.getText());			
			}
		});
		btnSearch.setBounds(455, 48, 89, 23);
		frame.getContentPane().add(btnSearch);
		
		JButton btnNewButton = new JButton("Search and upload new songs");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				new DlgFileSearch(kontrolle.getSqlConnection(), kontrolle.getActiveUserID());
				updateGlobalMusikInformations(null);
			}
		});
		btnNewButton.setBounds(5, 11, 539, 27);
		frame.getContentPane().add(btnNewButton);
		
		btnPlay = new JButton("Play");
		btnPlay.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				playSong();
			}
		});
		btnPlay.setBounds(121, 411, 125, 23);
		frame.getContentPane().add(btnPlay);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(5, 76, 539, 276);
		frame.getContentPane().add(scrollPane);
		
		tblMusik = new JTable(new MusikTableModel());
		scrollPane.setViewportView(tblMusik);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(554, 72, 160, 337);
		frame.getContentPane().add(scrollPane_1);
		
		tblFriends = new JTable(new FriendTableModel());
		tblFriends.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent arg0) {
			  	System.out.println("Mouse clicked");
			  	
			  	Point p = arg0.getPoint();
			  	int selected = tblFriends.rowAtPoint(arg0.getPoint());
			  	System.out.println(selected);
			  	
			  	selectedUserID = kontrolle.getUserFriends().get(selected);
			  	updateGlobalMusikInformations(edtSearch.getText());
			}
			@Override
			public void mousePressed(MouseEvent arg0) {}			
			@Override
			public void mouseExited(MouseEvent arg0) {}			
			@Override
			public void mouseEntered(MouseEvent arg0) {}			
			@Override
			public void mouseClicked(MouseEvent arg0) {}
		});
		scrollPane_1.setViewportView(tblFriends);
		
		JButton btnScore = new JButton("Statistik");
		btnScore.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new DlgStatistik(kontrolle.getSqlConnection().getConnection());
			}
		});
		btnScore.setBounds(401, 411, 143, 23);
		frame.getContentPane().add(btnScore);
		tblMusik.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent arg0) {
				MusikInformation mi = musikinformations.get(tblMusik.getSelectedRow());
				if(FileFunction.checkIfFileAlreadyDownloaded(mi)){	
					btnPlay.setText("Play");
				}
				else{
					btnPlay.setText("Download");
				}
			}
			@Override
			public void mousePressed(MouseEvent arg0) {}
			@Override
			public void mouseExited(MouseEvent arg0) {}
			@Override
			public void mouseEntered(MouseEvent arg0) {}
			@Override
			public void mouseClicked(MouseEvent arg0) {}
		});
		
		selectedUserID = Kontrolle.ALL_USERS;
		updateGlobalMusikInformations(null);
	}
	
	public void updateGlobalMusikInformations(String search){
		//daten holen
		try {
			musikinformations = kontrolle.getAllMusikFromDB(selectedUserID, search);
			//aktualisieren
			tblMusik.setModel(new MusikTableModel());
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
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
		//musik laden
		if(tblMusik.getSelectedRow() < 0)
			return;
		MusikInformation mi = musikinformations.get(tblMusik.getSelectedRow());
		try {
			//datei rutnerladen
			String derp = FileFunction.downloadToFile("musikdaten", "data", kontrolle.getSqlConnection().getConnection(), mi);
			mi.setPfad(derp);
			kontrolle.playMusik(derp);
			//wennn hier eh immer spielen
			btnPlay.setText("Play");
			tblMusik.repaint();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private void stopSong(){
		kontrolle.stopMusik();
	}
	
	private class FriendTableModel implements TableModel{

		@Override
		public void addTableModelListener(TableModelListener arg0) {}

		@Override
		public Class<?> getColumnClass(int arg0) {
			return String.class;
		}

		@Override
		public int getColumnCount() {
			return 1;
		}

		@Override
		public String getColumnName(int arg0) {
			return "Name";
		}

		@Override
		public int getRowCount() {
			ArrayList<?> friendIDs = kontrolle.getUserFriends();
			if(friendIDs != null)
				return friendIDs.size();
			return 0;
		}

		@Override
		public Object getValueAt(int row, int col) {
			return kontrolle.getUserNameById(kontrolle.getUserFriends().get(row));
		}

		@Override
		public boolean isCellEditable(int arg0, int arg1) {
			return false; //not editable
		}

		@Override
		public void removeTableModelListener(TableModelListener arg0) {}

		@Override
		public void setValueAt(Object arg0, int arg1, int arg2) {}
		
	}
	
	private class MusikTableModel implements TableModel{

		@Override
		public void setValueAt(Object arg0, int arg1, int arg2) {
			
		}
		
		@Override
		public void removeTableModelListener(TableModelListener arg0) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public boolean isCellEditable(int arg0, int arg1) {
			return false; //edit not allowed
		}
		
		@Override
		public Object getValueAt(int row, int col) {
			//einfügen der daten in die tabelle
			switch(col){
			case 0:
				return ""+musikinformations.get(row).getTitel();
			case 1:
				return ""+musikinformations.get(row).getInterpet();
			case 2:
				return ""+musikinformations.get(row).getAlbum();
			case 3:
				return ""+musikinformations.get(row).getGenre();
			case 4:
				if(FileFunction.checkIfFileAlreadyDownloaded(musikinformations.get(row)))
					return "x";
			break;
			}
			return "";
		}
		
		@Override
		public int getRowCount() {
			return musikinformations.size();
		}
		
		@Override
		public String getColumnName(int arg0) {
			//kopfzeile
			final String[] colnames = {"Titel","Interpret", "Album", "Genre", "Downloaded"};
			System.out.println(colnames);
			return colnames[arg0];
		}
		
		@Override
		public int getColumnCount() {
			return 5;
		}
		
		@Override
		public Class<?> getColumnClass(int arg0) {
			return String.class; //ausgabeklasse ist String
		}
		
		@Override
		public void addTableModelListener(TableModelListener arg0) {
			// TODO Auto-generated method stub
		}
	}
}
