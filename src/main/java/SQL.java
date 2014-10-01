import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Properties;

/**
 * Created by dm13y on 9/3/14.
 */
public class SQL {
    private Connection conn = null;
    private String[] typeDocId;

    /**
     * init conn string and SQLQuery
     */
    public SQL() {
        Properties prop = new Properties();
        try {
            File file = new File("SQL.properties");
            if (!file.exists()) {
                Main.errMsg("asfasdfa");
            }
            InputStream is = new FileInputStream(file);
            prop.load(is);
            //defaultPriceListProp.load(getClass().getClassLoader().getResourceAsStream("SQL.properties"));
            conn = DriverManager.getConnection(prop.getProperty("connString"));
            typeDocId = prop.getProperty("typeDocId").split(",");
        } catch (IOException io_ex) {
            conn = null;
            Main.errMsg(io_ex.getMessage());
        } catch (SQLException sql_ex) {
            conn = null;
            Main.errMsg(sql_ex.getMessage());
        } catch (Exception ex) {
            conn = null;
            Main.errMsg(ex.getMessage());
        }
    }

    /**
     * //adds the count day in the date
     * @param date
     * @param countDay
     * @return string "yyyy-MM-dd 0:00"
     */
    private String getDateForSQL(java.util.Date date, int countDay) {
        Calendar cl = Calendar.getInstance();
        cl.setTime(date);
        cl.add(Calendar.DATE, countDay);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(cl.getTime()) + " 0:00";
    }

    /**
     * Convert an array of strings into a single string separated by spaces
     * @param docsId array
     * @return String
     */
    private String getDocsId(ArrayList<String> docsId) {
        String result = "";
        for (int i = 0; i < docsId.size(); i++) {
            if(i == docsId.size() - 1) {
                result = result + " docContent.WHDocumentID = " + docsId.get(i);
            }else {
                result = result + " docContent.WHDocumentID = " + docsId.get(i) + " or";
            }
        }
        return  result;
    }


    /**
     * Selection of maps for the selected docs
     * @param docsId id selected documents
     * @return array of cards
     */
    public ArrayList<Card> getCards(ArrayList<String> docsId) {
        ArrayList<Card> cards = new ArrayList<Card>();
        Statement st = null;
        String query = "" +
                "SELECT tmc.name, docContent.pricePAll, docContent.WHDocumentID, country.name as countryName, measure.name as measure, measure.QPInit FROM WHDocumentContent as docContent " +
                "JOIN tmc on docContent.nomenclid = tmc.id " +
                "JOIN DivisionExt on tmc.producerId = DivisionExt.id " +
                "JOIN Country on country.id = DivisionExt.CountryId " +
                "JOIN Measure on tmc.MeasureID = Measure.id " +
                "WHERE " + getDocsId(docsId);

        try {
            st = conn.createStatement();
            ResultSet rs = st.executeQuery(query);
            while(rs.next()) {
                String name = rs.getString("name");
                String docId = rs.getString("WHDocumentID");
                String measure = rs.getString("measure") + " " + rs.getString("QPInit");
                Long price = (long)(Double.parseDouble(rs.getString("pricePAll")));
                String country = rs.getString("countryName");
                cards.add(new Card(docId, name, price, country, measure));
            }
        } catch (SQLException sql_ex) {
            Main.errMsg(sql_ex.getMessage());
        } finally {
            try {
                st.close();
                return cards;
            } catch (SQLException sql_ex) {
                return cards;
            }
        }


    }

    public String getDocKind() {
        String result = "(";
        for(String kind :typeDocId) {
            if (result.length() > 1) {
                result = result + " or ";
            }
            result = result + "doc.kind = " + kind;
        }
        result = result + ")";
        return result;
    }

    /**
     * get array documents with database
     * @param firstDate - date condition
     * @param endDate - date condition
     * @return arrayList with documents
     */
    public ArrayList<Document> getDocs(java.util.Date firstDate, java.util.Date endDate) {
        ArrayList<Document> docs = new ArrayList<Document>();
        Statement st = null;
        String query = "" +
                "SELECT doc.id, div.name, doc.date, doc.number FROM whdocument as doc " +
                "JOIN Division as div on div.id = doc.divisionid " +
                "WHERE doc.status = 1 and " +
                "doc.warehouseid = 1 and " +
                getDocKind() + " and " +
                "doc.date > '" + getDateForSQL(firstDate, -1) + "' and " +
                "doc.date < '" + getDateForSQL(endDate, 1) + "'";
        try {
            st = conn.createStatement();
            ResultSet rs = st.executeQuery(query);
            while (rs.next()) {
                String name = rs.getString("name");
                Long id = Long.parseLong(rs.getString("id"));
                String date = rs.getString("date").split(" ")[0];
                String number = rs.getString("number");
                docs.add(new Document(id, date, number, name));
//                return docs;
            }
        } catch (SQLException sql_ex) {
            Main.errMsg(sql_ex.getMessage());
//            return docs;
        } catch (Exception ex) {
            Main.errMsg(ex.getMessage());
//            return docs;
        } finally {
            try {
                st.close();
                return docs;
            } catch (SQLException ex) {
                return docs;
            }
        }
    }

}
