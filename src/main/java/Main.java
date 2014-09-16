import javax.swing.*;
import java.util.Properties;

/**
 * run gui and load default properties
 */
public class Main {
    public static Properties defaultPriceListProp;
    public final static String PRICELIST_DEFAULT_FILE_SETTING = "PriceListDefault.properties";


    /**
     * Show error message
     * @param msg message
     */
    public static void errMsg(String msg) {
        JOptionPane.showMessageDialog(null, msg, "Ошибка!", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * @param key
     * @return param
     */
    public static String getDefaultPropPriceList(String key) {
        return defaultPriceListProp.getProperty(key);
    }

    public static void main(String[] args) {
        //for windows set look and feel
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
        }
        //start gui
        SwingUtilities.invokeLater(
                new Runnable() {
                    @Override
                    public void run() {
                        new GUI();
                    }
                }
        );
        new LoadDefaultPriceListProperties();
    }
}
