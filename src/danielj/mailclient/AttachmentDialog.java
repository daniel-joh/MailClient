package danielj.mailclient;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.*;
import java.util.List;

/**
 * Class for dialog that handles a new message´s attachments
 *
 * @author Daniel Johansson
 */
public class AttachmentDialog extends JDialog implements ActionListener {
    //GUI variables
    private JButton mBtnAddAttachment;
    private JButton mBtnRemoveAttachments;
    private JButton mBtnOK;
    private JScrollPane mScrollPane;
    private DefaultTableModel mModel;
    private JTable mTable;

    /**
     * List of attached files
     */
    private List<File> mAttachedFiles;

    /**
     * Constructor
     *
     * @param parent JDialog parent
     * @param title  title of dialog
     * @param modal  modal status
     */
    public AttachmentDialog(JDialog parent, String title, boolean modal) {
        super(parent, title, modal);

        initComponents();
    }

    /**
     * Inits GUI components
     */
    public void initComponents() {
        setBounds(100, 100, 474, 347);
        getContentPane().setLayout(new BorderLayout());
        mAttachedFiles = new ArrayList<File>();

        JPanel buttonPane = new JPanel();
        buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
        getContentPane().add(buttonPane, BorderLayout.SOUTH);

        mBtnAddAttachment = new JButton("Add attachment");
        mBtnAddAttachment.setToolTipText("Add attachment");
        mBtnAddAttachment.setActionCommand("OK");
        mBtnAddAttachment.addActionListener(this);
        buttonPane.add(mBtnAddAttachment);
        getRootPane().setDefaultButton(mBtnAddAttachment);

        mBtnRemoveAttachments = new JButton("Remove attachments");
        buttonPane.add(mBtnRemoveAttachments);
        mBtnRemoveAttachments.addActionListener(this);

        mBtnOK = new JButton("OK");
        mBtnOK.addActionListener(this);
        buttonPane.add(mBtnOK);

        mScrollPane = new JScrollPane();
        getContentPane().add(mScrollPane, BorderLayout.CENTER);

        createTable();                                              //Creates the JTable for the attached filenames

        addAttachedFiles();                                         //Adds attached filenames to the DefaultTableModel

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);
        setVisible(true);
    }

    /**
     * Creates the JTable
     */
    public void createTable() {
        Object[][] obj = new Object[0][1];
        String[] header = {"Attached File"};
        mModel = new DefaultTableModel(obj, header);
        mTable = new JTable(mModel);
        mScrollPane.setViewportView(mTable);
        mTable.setFont(new Font("Tahoma", Font.PLAIN, 15));
        mTable.getColumnModel().getColumn(0).setPreferredWidth(440);
    }

    /**
     * Gets the attached files array from ComposeDialog and adds all files filenames to the DefaultTableModel
     */
    public void addAttachedFiles() {
        //Gets attached files array
        File[] files = ComposeDialog.getAttachedFiles();

        //If returned files arent null
        if (files != null) {
            mAttachedFiles = new ArrayList<>(Arrays.asList(files));          //Converts File[] to List

            Object[][] obj = new Object[mAttachedFiles.size()][1];

            //Adds all filenames to the model
            for (int i = 0; i < mAttachedFiles.size(); i++) {
                obj[i][0] = mAttachedFiles.get(i).getAbsolutePath();
            }

            String[] header = {"Attached File"};
            mModel.setDataVector(obj, header);
        }
    }

    public void actionPerformed(ActionEvent e) {
        //Handles adding attachments
        if (e.getSource() == mBtnAddAttachment) {
            JFileChooser fc = new JFileChooser();
            Locale locale = new Locale.Builder().setLanguage("en").setRegion("US").build();
            fc.setDefaultLocale(locale);
            fc.setLocale(locale);
            int result = fc.showDialog(getParent(), "Attach file");

            //If file has been chosen by user
            if (result == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                String filename = file.getAbsolutePath();
                Vector row = new Vector();
                row.add(filename);
                mModel.addRow(row);
                mAttachedFiles.add(file);

                //Converts to File[], then sets the attached files in the ComposeDialog.setAttachedFiles() method
                File[] files = mAttachedFiles.toArray(new File[mAttachedFiles.size()]);
                ComposeDialog.setAttachedFiles(files);
            }
        }

        //Handles removing attachments (removes all attachments)
        if (e.getSource() == mBtnRemoveAttachments) {
            int returVal = JOptionPane.showConfirmDialog(getParent(), "Do you want to remove all attached files?",
                    "Remove attached files", JOptionPane.OK_CANCEL_OPTION);

            if (returVal == JOptionPane.OK_OPTION) {
                mAttachedFiles.clear();
                ComposeDialog.removeAttachedFiles();

                while (mModel.getRowCount() > 0) {
                    mModel.removeRow(0);
                }
            }
        }

        //Sets attached files and closes the dialog
        if (e.getSource() == mBtnOK) {
            //Converts to File[], then sets the attached files in the ComposeDialog.setAttachedFiles() method
            File[] files = mAttachedFiles.toArray(new File[mAttachedFiles.size()]);
            ComposeDialog.setAttachedFiles(files);

            //Dispose of the dialog window
            Component component = (Component) e.getSource();
            JDialog dialog = (JDialog) SwingUtilities.getRoot(component);
            dialog.dispose();
        }
    }
}
