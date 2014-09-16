import java.io.IOException;
import java.util.Properties;

/**
 * Created by dm13y on 9/8/14.
 */
public class LoadDefaultPriceListProperties {
    public LoadDefaultPriceListProperties(){
        try {
            Main.defaultPriceListProp = new Properties();
            Main.defaultPriceListProp.load(getClass().getClassLoader().getResourceAsStream(Main.PRICELIST_DEFAULT_FILE_SETTING));
        } catch (IOException io_ex) {
            Main.errMsg(io_ex.getMessage());
        }  catch (Exception ex) {
            Main.errMsg(ex.getMessage());
        }
    }
}
