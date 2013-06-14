package Frames.Dialogs;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * @author Kay Wellinger
 */
public class DlgStatistik extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTable tblScore;
	
	//liked hash map da diese eine bestimmte reihenfolge garantiert
	LinkedHashMap<String,Integer> mapScore = new LinkedHashMap<String,Integer>();
	//private HashMap<String, Integer> mapScore = new HashMap<String, Integer>();
	
	/**
	 * @wbp.parser.constructor
	 */
	public DlgStatistik(Connection con){
		this(null, true, con);
		setTitle("HalfBeatPlayer - Statistik");
	}
	public DlgStatistik(Frame frm,boolean modal, Connection con) {
		super(frm, modal);
		setBounds(100, 100, 622, 427);
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));
		{
			JScrollPane scrollPane = new JScrollPane();
			contentPanel.add(scrollPane, BorderLayout.CENTER);
			{
				tblScore = new JTable();
				scrollPane.setViewportView(tblScore);
			}
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton btnClose = new JButton("Close");
				btnClose.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						setVisible(false);
					}
				});
				btnClose.setActionCommand("Cancel");
				buttonPane.add(btnClose);
			}
		}
		
		inititializeData(con);
		tblScore.setModel(new ScoreTableModel());
		this.setVisible(true);
	}
	
	public void inititializeData(Connection con){
		try {
			PreparedStatement ps = con.prepareStatement("SELECT * FROM score;");
			ResultSet res = ps.executeQuery();
			
			while(res.next()){
				mapScore.put(res.getString(1), res.getInt(2));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}		
	}
	
	private class ScoreTableModel implements TableModel{

		@Override
		public void addTableModelListener(TableModelListener l) {}

		@Override
		public Class<?> getColumnClass(int columnIndex) {
			return String.class; //ausgabe ist nur stirng
		}

		@Override
		public int getColumnCount() {
			return 2;
		}

		@Override
		public String getColumnName(int columnIndex) {
			switch(columnIndex){
			case 0:
				return "Name";
			case 1:
				return "Hochgeladen";
			}
			return "";
		}

		@Override
		public int getRowCount() {
			return mapScore.size();
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			if(columnIndex == 0)
			return (new ArrayList<String>(mapScore.keySet())).get(rowIndex);
			else
			return (new ArrayList<Integer>(mapScore.values())).get(rowIndex);
			
		}

		@Override
		public boolean isCellEditable(int rowIndex, int columnIndex) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public void removeTableModelListener(TableModelListener l) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
			// TODO Auto-generated method stub
			
		}
	}

}
