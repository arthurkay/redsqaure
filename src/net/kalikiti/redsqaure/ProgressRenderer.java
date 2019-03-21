package net.kalikiti.redsqaure;

import javax.swing.JProgressBar;
import javax.swing.table.TableCellRenderer;
import javax.swing.JTable;
import java.awt.Component;

public class ProgressRenderer extends JProgressBar implements TableCellRenderer {

	public ProgressRenderer(int min, int max) {
		super(min, max);
	}
	
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		setValue((int) ((Float) value).floatValue());
		return this;
	}
}
