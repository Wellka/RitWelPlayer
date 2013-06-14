package Frames.Dialogs;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;

import musikData.MusikInformation;
import Main.Kontrolle;
import SQL.PSQLConnection;
import cal.FileFunction;
import cal.fileTreeSearch.FileSearchListener;
import cal.fileTreeSearch.TreeFileSearch;

/**
 * @author Kay Wellinger
 */
public class DlgFileSearch extends JDialog implements FileSearchListener {
	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JTextField edtPath;
	private JList<Object> lstPath;
	private DefaultListModel<String> listModel = new DefaultListModel<String>();
	private JProgressBar progressBar;
	private JProgressBar pbCurrentFolder;
	private Thread workerThread = new Thread();
	private JLabel lblCurrentFile;
	private JButton btnClose;
	private JButton btnStart;
	private PSQLConnection connection;
	private ArrayList<MusikInformation> musikInformations =new ArrayList<MusikInformation>();
	private int currentUserId;
	/**
	 * Create the dialog.
	 * @wbp.parser.constructor
	 */
	public DlgFileSearch(PSQLConnection con, int userID){
		this(null, true, con, userID);
		setTitle("HalfBeatPlayer - Dateien Hochladen");
	}
	@SuppressWarnings("unchecked")
	public DlgFileSearch(Frame frm, boolean modal, PSQLConnection con, int userID) {
		super(frm, modal);
		this.connection = con;
		this.currentUserId = userID;
		
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

		setBounds(100, 100, 496, 404);
		setPreferredSize(new Dimension(496, 404));
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);

		JLabel lblOrdnerWhlen = new JLabel("Ordner W\u00E4hlen");
		lblOrdnerWhlen.setBounds(10, 11, 460, 14);
		contentPanel.add(lblOrdnerWhlen);

		edtPath = new JTextField();
		edtPath.setBounds(10, 179, 422, 20);
		edtPath.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {
			}

			@Override
			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					listModel.addElement(edtPath.getText());
				}
			}

			@Override
			public void keyPressed(KeyEvent e) {
			}
		});
		contentPanel.add(edtPath);
		edtPath.setColumns(10);

		JButton btnChoose = new JButton("...");
		btnChoose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String path = showFolderChooserDlg();
				if (path != "") {
					edtPath.setText(path);
				}
			}
		});
		btnChoose.setBounds(434, 178, 36, 23);
		contentPanel.add(btnChoose);

		final JCheckBox cbxUnterordner = new JCheckBox("Unterordner durchsuchen?");
		cbxUnterordner.setBounds(2, 234, 206, 23);
		contentPanel.add(cbxUnterordner);

		if(listModel != null)
			lstPath = new JList(listModel);
		// Nur eins auswählen
		lstPath.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		//lstPath.setBorder(UIManager.getBorder("PopupMenu.border"));
		lstPath.setBounds(10, 36, 460, 132);
		contentPanel.add(lstPath);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				btnClose = new JButton("Close");
				btnClose.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						setVisible(false);
					}
				});
				btnClose.setActionCommand("Cancel");
				buttonPane.add(btnClose);
			}
		}

		JButton btnAdd = new JButton("Hinzuf\u00FCgen");
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String path = edtPath.getText();
				System.out.println("edtPath.gettext()" + edtPath.getText());
				if (path != "") {
					listModel.addElement(path);
					lstPath.setSelectedIndex(listModel.size() - 1); // letzten
																	// eintrag
																	// wählen
				}
			}
		});
		btnAdd.setBounds(360, 204, 110, 23);
		contentPanel.add(btnAdd);

		JButton btnDel = new JButton("L\u00F6schen");
		btnDel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				listModel.remove(lstPath.getSelectedIndex());
				lstPath.setSelectedIndex(listModel.size() - 1); // letzten
																// eintrag
																// wählen
			}
		});
		btnDel.setBounds(10, 204, 89, 23);
		contentPanel.add(btnDel);

		progressBar = new JProgressBar();
		progressBar.setBounds(85, 308, 286, 14);
		contentPanel.add(progressBar);

		progressBar.setString(null);
		progressBar.setStringPainted(true);

		btnStart = new JButton("Start");
		final FileSearchListener fsl = this; // muss leider hier
												// zwischengespeichert werden;
		// In inner classes kann das später nichtmehr aufgerufen werden
		btnStart.addActionListener(new ActionListener() {
			@SuppressWarnings("deprecation")
			public void actionPerformed(ActionEvent e) {
				if (!workerThread.isAlive()) {
					workerThread = new Thread() {
						public void run() {
							if(listModel.size() <= 0)
								return;
							// Progressbar daten Setzen
							progressBar.setMinimum(0);
							progressBar.setMaximum(listModel.getSize());

							progressBar.setStringPainted(true);
							progressBar.setString(null);

							btnStart.setText("Stop");
							btnClose.setEnabled(false);
							int rekursiv = TreeFileSearch.RECURSION_NONE;
							if(cbxUnterordner.isSelected())
								rekursiv = TreeFileSearch.RECURSION_UNLIMITED;
							for (int i = 0; i < listModel.getSize(); i++) {
								String path = listModel.get(i);
								
								System.out.println("actionPerformed(): " +path);
								TreeFileSearch fts = new TreeFileSearch(fsl, path, rekursiv, ".mp3");

								// schauen ob fertig
								// sonst werden zuviele threads gestartet
								while (!fts.isDone()) {
									try {
										Thread.sleep(100);
									} catch (InterruptedException e) {
									}
								}

								progressBar.setValue(i + 1); // eins weiter
							}
							btnStart.setText("Start");
							btnClose.setEnabled(true);
						}
					};
					workerThread.start();
				} else {
					btnStart.setText("Start");
					btnClose.setEnabled(true);
					workerThread.stop();
				}
			}
		});
		btnStart.setBounds(381, 295, 89, 27);
		contentPanel.add(btnStart);

		JPanel Bevel = new JPanel();
		Bevel.setBounds(0, 264, 480, 3);
		contentPanel.add(Bevel);

		pbCurrentFolder = new JProgressBar();
		pbCurrentFolder.setBounds(85, 295, 286, 14);
		contentPanel.add(pbCurrentFolder);
		pbCurrentFolder.setString(null);
		pbCurrentFolder.setStringPainted(true);

		JLabel lblStaticCurrentFile = new JLabel("Datei:");
		lblStaticCurrentFile.setBounds(10, 273, 75, 14);
		contentPanel.add(lblStaticCurrentFile);

		lblCurrentFile = new JLabel("");
		lblCurrentFile.setBounds(85, 273, 385, 14);
		contentPanel.add(lblCurrentFile);

		JLabel lblCurrent = new JLabel("Current:");
		lblCurrent.setBounds(10, 295, 75, 14);
		contentPanel.add(lblCurrent);

		JLabel lblTotal = new JLabel("Total:");
		lblTotal.setBounds(10, 306, 75, 14);
		contentPanel.add(lblTotal);
		
		this.setResizable(false);
		pack();
		this.setVisible(true);
	}

	public String showFolderChooserDlg() {
		// dialog anlegen
		JFileChooser chooser = new JFileChooser();
		// chooser.setCurrentDirectory(new java.io.File("."));

		chooser.setDialogTitle("Wählen sie einen Ordner");
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

		// Dialog zeigen und auf eingabe prüfen
		if (chooser.showOpenDialog(this)  == JFileChooser.APPROVE_OPTION) {
			System.out.println("getCurrentDirectory(): "
					+ chooser.getCurrentDirectory());
			System.out.println("getSelectedFile() : "
					+ chooser.getSelectedFile());
			return chooser.getSelectedFile().getAbsolutePath();
		} else {
			System.out.println("No Selection ");
			return "";
		}
	}

	@Override
	public void onFolderFound(String lastfolderPath, long count) {
		lblCurrentFile.setText(lastfolderPath);
		System.out.println(lastfolderPath);
	}

	@Override
	public void onFileFound(String lastfilePath, long count) {
		System.out.println(lastfilePath);
		MusikInformation mi = new MusikInformation(lastfilePath);
		try {
			mi.uploadToDatabase(connection, currentUserId);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		musikInformations.add(mi);
		System.out.println("uploading file");
		FileFunction.uploadFile("musikdaten", "data", this.connection.getConnection(), lastfilePath, mi.getID());
		System.out.println("Finished uploading");
	}

	@Override
	public void onStart() {
	}

	@Override
	public void onFinish(Vector<String> files) {
		// wird vorher gemacht...
	}

	public void onFileError(String file) {
		System.out.println("Die Datei/der Ordner: \"" + file
				+ "\" ist nicht vorhanden");
	}

	public void onProgress(long progress, long max) {
		pbCurrentFolder.setMinimum(0);
		pbCurrentFolder.setMaximum((int) max);
		pbCurrentFolder.setValue((int) progress);
	}
}