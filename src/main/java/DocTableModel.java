import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by dm13y on 9/3/14.
 */
public class DocTableModel extends AbstractTableModel {
    private ArrayList<Document> docs;
    private SQL sql;
    private static String headers[] = {"", "Дата", "Номер", "Поставщик"};
    private ArrayList<Boolean> selects;

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        selects.set(rowIndex, (Boolean) aValue);
        fireTableRowsUpdated(rowIndex, rowIndex);
    }

    public static String[] getHeaders() {
        return headers;
    }

    public DocTableModel(Date firstDate, Date endDate) {
        sql = new SQL();
        updateDate(firstDate, endDate);
    }

    public void updateDate(Date firstDate, Date endDate) {
        docs = new ArrayList<Document>();
        docs = sql.getDocs(firstDate, endDate);
        selects = new ArrayList<Boolean>();
        for(int i = 0; i < docs.size(); i++) {
            selects.add(false);
        }
        fireTableDataChanged();
    }


    @Override
    public int getRowCount() {
        return docs.size();
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex) {
            case 0:
                return Boolean.class;
            case 1:
                return Long.class;
            case 2:
                return String.class;
            case 3:
                return String.class;
            default:
                return Boolean.class;
        }
    }

    @Override
    public int getColumnCount() {
        return 4;
    }

    @Override
    public String getColumnName(int column) {
        return headers[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        switch (columnIndex) {
            case 0:
                return selects.get(rowIndex);
            case 1:
                return docs.get(rowIndex).getDate();
            case 2:
                return docs.get(rowIndex).getNumber();
            case 3:
                return docs.get(rowIndex).getName();
            default:
                return null;
        }
    }

    /**
     * @return all selected docs id
     */
    public ArrayList<String> getSelDocId() {
        ArrayList<String> result = new ArrayList<String>();
        for (int i = 0; i < docs.size(); i++) {
            if (selects.get(i)) {
                result.add(docs.get(i).getId().toString());
            }
        }
        return result;
    }

    /**
     * Add to the PriceList relevant document
     * @param priceList objcet
     */
    public void fillDataReport(PriceList priceList) {
        for (Document doc : docs) {
            if(doc.getId().equals(priceList.getCard().getDocId())){
                priceList.setDoc(doc);
                return;
            }
        }
    }
}
