import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.util.ArrayList;

/**
 * Created by dm13y on 9/8/14.
 */
public class CardTableModel extends AbstractTableModel {
    private ArrayList<Card> cards;
    private SQL sql;
    private ArrayList<String> docsId;
    private String[] colHeader = new String[]{"", "Название", "Цена", "%", "Сумма без скидки", "Кол-во"};
    private ArrayList<Boolean> selected;

    public CardTableModel(ArrayList<String> docsId) {
        this.docsId = docsId;
        sql = new SQL();
        updateData();
    }

    private void updateData() {
        cards = sql.getCards(docsId);
        selected = new ArrayList<Boolean>();
        for (int i = 0; i < cards.size(); i++) {
            selected.add(false);
        }
        fireTableDataChanged();
    }

    @Override
    public String getColumnName(int column) {
        return colHeader[column];
    }

    @Override
    public Class getColumnClass(int columnIndex) {
        switch (columnIndex) {
            case 0:
                return Boolean.class;
            case 1:
                return String.class;
            case 2:
                return Long.class;
            case 3:
                return Integer.class;
            case 4:
                return Long.class;
            case 5:
                return Integer.class;
            default:
                return Boolean.class;
        }
    }



    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        if ((columnIndex == 3) || (columnIndex == 5) || (columnIndex == 4)) {
            return true;
        } else {
            return false;
        }
//        return super.isCellEditable(rowIndex, columnIndex);

    }



    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        super.setValueAt(aValue, rowIndex, columnIndex);
        switch (columnIndex) {
            case 0://select
                selected.set(rowIndex, (Boolean)aValue);
                break;
            case 3://rate
                int rate = (Integer) aValue;
                cards.get(rowIndex).setRate(rate);
                break;
            case 5:
                cards.get(rowIndex).setCount((Integer) aValue);
                break;
            case 4:
                cards.get(rowIndex).setFullPrice((Long) aValue);
            default:
        }
        fireTableRowsUpdated(rowIndex, rowIndex);

    }

    public ArrayList<PriceList> getSelectedCard() {
        ArrayList<PriceList> result = new ArrayList<PriceList>();
        for (int i = 0; i < cards.size(); i++) {
            if (selected.get(i)) {
                Card card = cards.get(i);
                PriceList priceList = new PriceList(card);
                result.add(priceList);
            }
        }
        return result;
    }

    @Override
    public int getRowCount() {
        return cards.size();
    }

    @Override
    public int getColumnCount() {
        return 6;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        switch (columnIndex) {
            case 0:
                return selected.get(rowIndex);
            case 1:
                return cards.get(rowIndex).getName() + cards.get(rowIndex).getMeasure();
            case 2:
                return cards.get(rowIndex).getPrice();
            case 3:
                return cards.get(rowIndex).getDiscountRate();
            case 4:
                return cards.get(rowIndex).getDiscountPrice();
            case 5:
                return cards.get(rowIndex).getCount();
            default:
                return "";
        }
    }
}

class CardEditor extends AbstractCellEditor implements TableCellEditor {
    final JTextField tf = new JTextField();
    int value;

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        //     tf.setText(String.valueOf(value));
        if (!(Boolean)table.getValueAt(row, 0)) {
            return null;
        }
        this.value = (Integer) value;
        tf.setText("");
        return tf;
    }

    @Override
    public Object getCellEditorValue() {
        int newVal;
        try {
            newVal = Integer.parseInt(tf.getText());
            return newVal;
        } catch (Exception ex) {
            return value;
        }
    }
}

class CardEditorLongValue extends AbstractCellEditor implements TableCellEditor {
    final JTextField tf = new JTextField();
    Long value;

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        //     tf.setText(String.valueOf(value));
        if (!(Boolean)table.getValueAt(row, 0)) {
            return null;
        }
        this.value = (Long) value;
        tf.setText("");
        return tf;
    }

    @Override
    public Object getCellEditorValue() {
        Long newVal;
        try {
            newVal = Long.parseLong(tf.getText());
            return newVal;
        } catch (Exception ex) {
            return value;
        }
    }
}

class CardRender extends DefaultTableCellRenderer{
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component comp = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        if ((Boolean) table.getValueAt(row, 0)) {
            comp.setBackground(Color.GREEN);
        } else {
            comp.setBackground(Color.WHITE);
        }
//        if (isSelected) {
//            comp.setBackground(Color.LIGHT_GRAY);
//        }
        return comp;
    }
}

class CheckBoxTableCellRenderer extends JCheckBox implements TableCellRenderer {
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        if ((Boolean) table.getValueAt(row, 0)) {
            setBackground(Color.GREEN);
            setSelected(true);
        } else {
            setBackground(Color.WHITE);
            setSelected(false);
        }
//        if (isSelected) {
//            setBackground(Color.LIGHT_GRAY);
//        }
        return this;
    }
}


