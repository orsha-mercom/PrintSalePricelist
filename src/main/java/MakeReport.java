import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.swing.JRViewer;
import net.sf.jasperreports.swing.JRViewerToolbar;
import net.sf.jasperreports.view.JRSaveContributor;

import javax.swing.*;
import java.util.Collection;

/**
 * Creates and displays a specified report
 */
public class MakeReport {
    public MakeReport(Collection<PriceList> coll, String reportFileName) {
        //load fields report
        JRBeanCollectionDataSource jrBeanCollectionDataSource = new JRBeanCollectionDataSource(coll);
        JRViewer jv = null;

        try {
            //fill report
            JasperPrint jp = JasperFillManager.fillReport(reportFileName, null, jrBeanCollectionDataSource);
            jv = new JRViewer(jp);

            //delete contributors from save menu
            JRSaveContributor[] saveContributor = ((JRViewerToolbar)jv.getComponent(0)).getSaveContributors();
            for (JRSaveContributor jrSaveContributor : saveContributor) {
                ((JRViewerToolbar)jv.getComponent(0)).removeSaveContributor(jrSaveContributor);
            }
        } catch (JRException jasp_exp) {
            Main.errMsg(jasp_exp.getMessage());
            jasp_exp.printStackTrace();
            return;
        } catch (Exception ex) {
            Main.errMsg(ex.getMessage());
            ex.printStackTrace();
            return;
        }
        JFrame frame = new JFrame("Печать выбранный ценников");

        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.getContentPane().add(jv);
        frame.pack();
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setVisible(true);
    }
}
