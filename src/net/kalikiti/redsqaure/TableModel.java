package net.kalikiti.redsqaure;

import javax.swing.table.AbstractTableModel;

public class TableModel extends AbstractTableModel {

	private String[] columnNames = {
			"URL", "File Size", "Progress", "Status"
	};
	
	private Object[][] tableData = {
	};
	
	public int getRowCount() {
		return tableData.length;
	}
	
	public int getColumnCount() {
		return columnNames.length;
	}
	
	public String getColumnName(int c) {
		return columnNames[c];
	}
	
	public Object getValueAt(int row, int col) {
		return tableData[row][col];
	}
	
	@SuppressWarnings("unchecked")
	public Class getColumnClass(int col) {
		return getValueAt(0, col).getClass();
	}
	
	public void setValueAt(Object value, int row, int col) {
		tableData[row][col] = value;
		fireTableCellUpdated(row, col);
	}
	
}
