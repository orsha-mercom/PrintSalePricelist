import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by dm13y on 9/3/14.
 */
public class GUI {
    private JTabbedPane stepsPanel;
    private JProgressBar progressBar;
    private JPanel step0;
    private JPanel step1;
    private JPanel step2;
    private JPanel mainPanel;
    private JPanel selDatePanel;
    private JDateChooser firstDate;
    private JDateChooser endDate;
    private JButton btnSelect;
    private JPanel tablePanel;
    private JPanel btnPanel;
    private JButton btnStep0Next;
    private JTable tblDoc;
    private JTable tblCard;
    private JButton btnStep1Back;
    private JButton btnStep1Next;
    private JRadioButton rBtnA4;
    private JRadioButton rBtn13x8;
    private JButton btnStep2Back;
    private JButton btnPrint;
    private JFrame frame;
    ButtonGroup jRadioMenuSelectFormat;

    private final int STEP_COUNT = stepsPanel.getTabCount();
    public GUI() {
        initFrame();
        setStep(0);
        frame.setVisible(true);

        firstDate.setDate(new Date());
        endDate.setDate(new Date());

        tblDoc.setModel(new DocTableModel(firstDate.getDate(), endDate.getDate()));
        tblDoc.getTableHeader().setReorderingAllowed(false);
        tblDoc.setDefaultRenderer(String.class, new CardRender());
        tblDoc.setDefaultRenderer(Integer.class, new CardRender());
        tblDoc.setDefaultRenderer(Long.class, new CardRender());
        tblDoc.setDefaultRenderer(Boolean.class, new CheckBoxTableCellRenderer());

        tblCard.getTableHeader().setReorderingAllowed(false);
        tblCard.setDefaultEditor(Integer.class, new CardEditor());
        tblCard.setDefaultRenderer(Integer.class, new CardRender());
        tblCard.setDefaultRenderer(String.class, new CardRender());
        tblCard.setDefaultRenderer(Boolean.class, new CheckBoxTableCellRenderer());
        tblCard.setDefaultRenderer(Long.class, new CardRender());
        tblCard.setDefaultRenderer(Object.class, new CardRender());

        jRadioMenuSelectFormat = new ButtonGroup();
        jRadioMenuSelectFormat.add(rBtnA4);
        jRadioMenuSelectFormat.add(rBtn13x8);

        btnSelect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {//update data table
                ((DocTableModel) tblDoc.getModel()).updateDate(firstDate.getDate(), endDate.getDate());
            }
        });
        tblDoc.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {//select current row
                super.mouseClicked(e);
                if (e.getClickCount() == 2) {
                    Boolean curVal = (Boolean) tblDoc.getValueAt(tblDoc.getSelectedRow(), 0);
                    tblDoc.setValueAt(!curVal, tblDoc.getSelectedRow(), 0);
                }
            }
        });
        btnStep0Next.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setStep(1);
            }
        });
        tblCard.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (e.getClickCount() == 2) {
                    if (tblCard.getSelectedColumn() == 0) {
                        Boolean curVal = (Boolean) tblCard.getValueAt(tblCard.getSelectedRow(), 0);
                        tblCard.setValueAt(!curVal, tblCard.getSelectedRow(), 0);
                    }
                }
            }
        });
        btnStep1Back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setStep(0);
            }
        });
        btnStep1Next.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setStep(2);
            }
        });
        btnStep2Back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setStep(1);
            }
        });
        btnPrint.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                printReport();
            }
        });
    }

    private void printReport() {
        ArrayList<PriceList> selCards = ((CardTableModel)tblCard.getModel()).getSelectedCard();
        ArrayList<PriceList> report = new ArrayList<PriceList>();
        for (PriceList repCard : selCards) {
            ((DocTableModel)tblDoc.getModel()).fillDataReport(repCard);
            for (int i = 0; i < repCard.getCount(); i++) {
                report.add(repCard);
            }
        }
        String reportsFile = "";
        if (rBtnA4.isSelected()) {
            reportsFile = "reports/repA4.jasper";
        }else if (rBtn13x8.isSelected()) {
            reportsFile = "reports/rep14x9.jasper";
        }


        new MakeReport(report, reportsFile);



    }


    /**
     * Set attributes and location frame
     */
    private void initFrame() {
        frame = new JFrame("Печать акционных ценников");
        frame.setContentPane(mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        double scrWidth = Toolkit.getDefaultToolkit().getScreenSize().getWidth();
        double scrHeight = Toolkit.getDefaultToolkit().getScreenSize().getHeight();
        frame.setLocation((int) (scrWidth - frame.getWidth()) / 2, (int) (scrHeight - frame.getHeight()) / 2);
    }

    /**
     * Determines whether there is a selected documents or cards
     * @param table documents or cards
     * @return boolean
     */
    private Boolean isSelectedRows(JTable table) {
        if (table.getRowCount() == 0) {
            return false;
        }
        for (int i = 0; i < table.getRowCount(); i++) {
            if ((Boolean) table.getValueAt(i, 0)) {
                return true;
            }
        }
        return false;
    }

    /**
     *
     * @return current step
     */
    private int getCurrentStep() {
        return stepsPanel.getSelectedIndex();
    }




    /**
     * Activated current step and disable other steps
     * @param stepNumber index current step
     */
    private void setStep(int stepNumber) {
        //check step number
        if ((stepNumber >= STEP_COUNT) || (stepNumber < 0)) {
            return;
        }
        //check selected docs
        if ((getCurrentStep() == 0) && (!isSelectedRows(tblDoc)) && (stepNumber == 1)) {
            Main.errMsg("Не выбрано не одного документа!!!");
            return;
        }
        //check selected cards
        if ((getCurrentStep() == 1) && (!isSelectedRows(tblCard)) && (stepNumber == 2)) {
            Main.errMsg("Не выбрано не одного товара!!!");
            return;
        }

        for(int i = 0; i < STEP_COUNT; i++ ) {
            if ((stepNumber == 1) && (getCurrentStep() == 0)) {
                ArrayList<String> selDocs = ((DocTableModel)tblDoc.getModel()).getSelDocId();
                tblCard.setModel(new CardTableModel(selDocs));
            }
            if (i == stepNumber) {
                stepsPanel.setEnabledAt(i, true);
                stepsPanel.setSelectedIndex(i);
                progressBar.setValue(100 / STEP_COUNT + 1 - i);
            } else {
                stepsPanel.setEnabledAt(i, false);
            }
        }
        return;
    }
}
