package danielj.mailclient;

import javax.mail.internet.MimeBodyPart;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Class for dialog that shows a message´s attachments
 *
 * @author Daniel Johansson
 */
public class ShowAttachmentDialog extends JDialog implements ActionListener {

    //GUI variables
    private JButton mBtnSave;
    private JButton mBtnOk;
    private JTable mTable;

    /**
     * MimeBodyPart array for the attached MimeBodyParts
     */
    private MimeBodyPart[] mArrayParts;

    /**
     * String array for the attached file names
     */
    private String[] mAttachedFilenames;

    /**
     * Constructor
     *
     * @param parent            Jframe parent
     * @param title             title of the JDialog
     * @param modal             modal status
     * @param attachedFilenames String array of attached filenames
     * @param arrayParts        MimeBodyPart array (attachments to save)
     */
    public ShowAttachmentDialog(JFrame parent, String title, boolean modal, String[] attachedFilenames,
                                MimeBodyPart[] arrayParts) {
        super(parent, title, modal);

        this.mArrayParts = arrayParts;
        this.mAttachedFilenames = attachedFilenames;

        initComponents();
    }

    /**
     * Inits GUI components
     */
    public void initComponents() {
        setBounds(100, 100, 450, 300);
        getContentPane().setLayout(new BorderLayout());

        createTable();

        JPanel buttonPane = new JPanel();
        buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
        getContentPane().add(buttonPane, BorderLayout.SOUTH);

        mBtnSave = new JButton("Save to file");
        mBtnSave.addActionListener(this);
        buttonPane.add(mBtnSave);

        mBtnOk = new JButton("OK");
        mBtnOk.setActionCommand("OK");
        mBtnOk.addActionListener(this);

        buttonPane.add(mBtnOk);
        getRootPane().setDefaultButton(mBtnOk);

        JScrollPane scrollPane = new JScrollPane();
        getContentPane().add(scrollPane, BorderLayout.CENTER);
        scrollPane.setViewportView(mTable);

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);
        setVisible(true);
    }

    /**
     * Creates the JTable that shows the attachments
     */
    public void createTable() {
        Object[][] obj = new Object[mAttachedFilenames.length][1];

        //Adds all filenames to the Object array (to be added to the JTable)
        for (int i = 0; i < mAttachedFilenames.length; i++) {
            obj[i][0] = (Object) mAttachedFilenames[i];
        }

        String[] header = {"Filename"};
        mTable = new JTable(obj, header);                                            //Creates the JTable
        mTable.setFont(new Font("Tahoma", Font.PLAIN, 17));
        mTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        mTable.getColumnModel().getColumn(0).setPreferredWidth(440);
    }

    //JButtons event handling
    public void actionPerformed(ActionEvent e) {
        //Closes the dialog
        if (e.getSource() == mBtnOk) {
            Component component = (Component) e.getSource();
            JDialog dialog = (JDialog) SwingUtilities.getRoot(component);
            dialog.dispose();
        }

        //Saves the selected attachment to file
        if (e.getSource() == mBtnSave) {
            int selectedFile = mTable.getSelectedRow();

            if (selectedFile != -1) {
                boolean b=MailHandler.saveAttachment(mArrayParts[selectedFile]);

                if (b==true)
                    JOptionPane.showMessageDialog(this, "File saved!");
            }
        }
    }
}
