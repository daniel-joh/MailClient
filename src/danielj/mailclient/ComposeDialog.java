package danielj.mailclient;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Class for the Dialog that composes new messages
 *
 * @author Daniel Johansson
 */
public class ComposeDialog extends JDialog implements ActionListener {

    //GUI variables
    private JEditorPane mEditorPane;
    private JTextField mTxtTo;
    private JTextField mTxtSubject;
    private JTextField mTxtCc;
    private JTextField mTxtBcc;
    private JButton mBtnSend;
    private JButton mBtnAttachments;

    /**
     * List of attached files
     */
    private java.util.List<File> mListFiles;

    /**
     * File array for the current message´s attached files
     */
    private static File[] attachedFiles;

    /**
     * Constructor for ComposeDialog
     *
     * @param parent Jframe parent
     * @param title  title of the dialog
     * @param modal  modal status
     */
    public ComposeDialog(JFrame parent, String title, boolean modal) {
        super(parent, title, modal);

        initComponents();
    }

    /**
     * Inits GUI components
     */
    public void initComponents() {
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 1044, 697);
        JPanel contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);

        mTxtTo = new JTextField();
        mTxtTo.setFont(new Font("Tahoma", Font.PLAIN, 16));
        mTxtTo.setColumns(10);

        JLabel lblTo = new JLabel("To:");
        lblTo.setFont(new Font("Tahoma", Font.PLAIN, 16));
        lblTo.setHorizontalAlignment(SwingConstants.RIGHT);
        lblTo.setLabelFor(mTxtTo);

        JLabel lblSubject = new JLabel("Subject:");
        lblSubject.setFont(new Font("Tahoma", Font.PLAIN, 16));
        lblSubject.setHorizontalAlignment(SwingConstants.RIGHT);

        mTxtSubject = new JTextField();
        mTxtSubject.setFont(new Font("Tahoma", Font.PLAIN, 16));
        lblSubject.setLabelFor(mTxtSubject);
        mTxtSubject.setColumns(10);

        JLabel lblCc = new JLabel("Cc:");
        lblCc.setFont(new Font("Tahoma", Font.PLAIN, 16));
        lblCc.setHorizontalAlignment(SwingConstants.RIGHT);

        mTxtCc = new JTextField();
        lblCc.setLabelFor(mTxtCc);
        mTxtCc.setColumns(10);

        JLabel lblBcc = new JLabel("Bcc:");
        lblBcc.setFont(new Font("Tahoma", Font.PLAIN, 16));
        lblBcc.setHorizontalAlignment(SwingConstants.RIGHT);

        mTxtBcc = new JTextField();
        lblBcc.setLabelFor(mTxtBcc);
        mTxtBcc.setColumns(10);

        mBtnSend = new JButton("Send mail");
        mBtnSend.setFont(new Font("Tahoma", Font.PLAIN, 16));
        mBtnSend.setToolTipText("Sends mail");
        mBtnSend.addActionListener(this);

        JScrollPane scrollPane = new JScrollPane();

        mEditorPane = new JEditorPane();
        mEditorPane.setFont(new Font("Tahoma", Font.PLAIN, 17));
        scrollPane.setViewportView(mEditorPane);

        mBtnAttachments = new JButton("Attachments");
        mBtnAttachments.setFont(new Font("Tahoma", Font.PLAIN, 16));
        mBtnAttachments.setToolTipText("Shows attachments");
        mBtnAttachments.addActionListener(this);

        GroupLayout gl_contentPane = new GroupLayout(contentPane);
        gl_contentPane.setHorizontalGroup(
                gl_contentPane.createParallelGroup(Alignment.TRAILING)
                        .addGroup(gl_contentPane.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
                                        .addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 998, Short.MAX_VALUE)
                                        .addGroup(gl_contentPane.createSequentialGroup()
                                                .addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
                                                        .addGroup(gl_contentPane.createSequentialGroup()
                                                                .addComponent(lblCc, GroupLayout.PREFERRED_SIZE, 73, GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(ComponentPlacement.RELATED)
                                                                .addComponent(mTxtCc, 528, 528, 528))
                                                        .addGroup(gl_contentPane.createSequentialGroup()
                                                                .addComponent(lblTo, GroupLayout.PREFERRED_SIZE, 73, GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(ComponentPlacement.RELATED)
                                                                .addComponent(mTxtTo, GroupLayout.PREFERRED_SIZE, 528, GroupLayout.PREFERRED_SIZE))
                                                        .addGroup(gl_contentPane.createSequentialGroup()
                                                                .addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING, false)
                                                                        .addComponent(lblSubject, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                                        .addComponent(lblBcc, Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 73, GroupLayout.PREFERRED_SIZE))
                                                                .addPreferredGap(ComponentPlacement.RELATED)
                                                                .addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
                                                                        .addComponent(mTxtBcc, 528, 528, 528)
                                                                        .addComponent(mTxtSubject, 528, 528, 528))))
                                                .addPreferredGap(ComponentPlacement.RELATED, 99, Short.MAX_VALUE)
                                                .addComponent(mBtnAttachments)
                                                .addGap(68)
                                                .addComponent(mBtnSend)))
                                .addContainerGap())
        );
        gl_contentPane.setVerticalGroup(
                gl_contentPane.createParallelGroup(Alignment.LEADING)
                        .addGroup(gl_contentPane.createSequentialGroup()
                                .addGap(26)
                                .addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
                                        .addComponent(lblTo)
                                        .addComponent(mTxtTo, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addGap(32)
                                .addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
                                        .addComponent(lblCc)
                                        .addComponent(mTxtCc, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE))
                                .addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
                                        .addGroup(gl_contentPane.createSequentialGroup()
                                                .addGap(32)
                                                .addComponent(lblBcc))
                                        .addGroup(gl_contentPane.createSequentialGroup()
                                                .addGap(29)
                                                .addComponent(mTxtBcc, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
                                .addGap(30)
                                .addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
                                        .addComponent(lblSubject)
                                        .addComponent(mTxtSubject, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(mBtnSend)
                                        .addComponent(mBtnAttachments))
                                .addGap(18)
                                .addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 390, Short.MAX_VALUE)
                                .addContainerGap())
        );

        mListFiles = new ArrayList<File>();

        contentPane.setLayout(gl_contentPane);

        //If messageFlag=="reply" - calls method to setup a new reply message
        if (MailHandler.getMessageFlag().equals("reply")) {
            setupReplyMessage();
        }

        //If messageFlag=="forward" - calls method to setup a new forwarded message
        else if (MailHandler.getMessageFlag().equals("forward")) {
            setupForwardMessage();
        }

        setResizable(false);
        setVisible(true);

        //Removes attached files (for the current message) when the dialog is closed
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                removeAttachedFiles();
            }
        });
    }

    //Event handler
    public void actionPerformed(ActionEvent e) {
        //If the send button was clicked, sends the composed mail
        if (e.getSource() == mBtnSend) {
            (new SendMailTask()).start();
        }

        //If the attachments button was clicked, opens the attachment dialog
        if (e.getSource() == mBtnAttachments) {
            //To get the JDialog which is used in AttachmentDialog constructor
            Component component = (Component) e.getSource();
            JDialog window = (JDialog) SwingUtilities.getRoot(component);

            AttachmentDialog dialog = new AttachmentDialog(window, "Attachments", true);
        }
    }

    /**
     * Sets up a new reply message
     */
    public void setupReplyMessage() {
        try {
            Message msg = MailHandler.getMessage();

            String subject = "Re: " + msg.getSubject();
            mTxtSubject.setText(subject);

            mTxtTo.setText(InternetAddress.toString(msg.getReplyTo()));

            mEditorPane.setContentType("text/plain");
            mEditorPane.setEditable(true);

            //If the message´s MimeType is "text/html" or "text/plain"
            if (msg.isMimeType("text/html") || msg.isMimeType("text/plain")) {
                String msgContent = (String) msg.getContent();
                String newMsg = "\n\n--- Original message ---\n" + msgContent;
                mEditorPane.setText(newMsg);
            }
            //If the message´s MimeType is "multipart"
            else if (msg.isMimeType("multipart/*")) {

                Multipart multiPart = (Multipart) msg.getContent();    //Gets the Multipart
                MimeBodyPart part = (MimeBodyPart) multiPart.getBodyPart(0);

                //Sets the text content of the multipart message in the JEditorPane
                if (part.isMimeType("text/plain") && (!Part.ATTACHMENT.equalsIgnoreCase(part.getDisposition()))) {
                    String msgContent = (String) part.getContent();
                    String newMsg = "\n\n--- Original message ---\n" + msgContent;
                    mEditorPane.setText(newMsg);
                }
            }
            mEditorPane.setCaretPosition(0);

        } catch (MessagingException | IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets up a new forwarded message
     */
    public void setupForwardMessage() {
        try {
            Message forwardedMessage = MailHandler.getMessage();

            String subject = "Fwd: " + forwardedMessage.getSubject();
            mTxtSubject.setText(subject);

            mTxtTo.setText("");

            handleContent(forwardedMessage);        //Handles the content of the message

            mEditorPane.setCaretPosition(0);

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    /**
     * Handles the content of the forwarded message
     *
     * @param part message to handle
     */
    public void handleContent(Part part) {
        try {
            //If plain text, adds the content of the message to the JEditorPane
            if (part.isMimeType("text/plain")) {
                mEditorPane.setContentType("text/plain");
                mEditorPane.setEditable(true);
                String newMsg = "\n\n--- Forwarded message ---\n" + part.getContent();
                mEditorPane.setText(newMsg);
            }

            //If text/html, treats the message as an attachment and adds it to attached files
            else if (part.isMimeType("text/html")) {
                MimeBodyPart mbp = (MimeBodyPart) part;
                File file = new File("filename.htm");
                mbp.saveFile(file);
                mListFiles.add(file);
                File[] files = mListFiles.toArray(new File[mListFiles.size()]);
                ComposeDialog.setAttachedFiles(files);
            }

            //If multipart
            else if (part.isMimeType("multipart/*")) {

                Multipart multiPart = (Multipart) part.getContent();    //Gets the Multipart
                int count = multiPart.getCount();        //Gets number of parts

                //For all multiparts
                for (int i = 0; i < count; i++) {
                    MimeBodyPart bodyPart = (MimeBodyPart) multiPart.getBodyPart(i);

                    // If "text/plain" and not an attachment, adds the content of the message to the JEditorPane
                    if (bodyPart.isMimeType("text/plain") && i == 0 && (!Part.ATTACHMENT.equalsIgnoreCase(bodyPart.getDisposition()))) {
                        mEditorPane.setContentType("text/plain");
                        mEditorPane.setEditable(true);
                        String newMsg = "\n\n--- Forwarded message ---\n" + bodyPart.getContent();
                        mEditorPane.setText(newMsg);
                    }

                    //If text/html, treats the message as an attachment and adds it to attached files
                    else if (bodyPart.isMimeType("text/html")) {
                        File file = new File("filename.htm");
                        bodyPart.saveFile(file);
                        mListFiles.add(file);
                        File[] files = mListFiles.toArray(new File[mListFiles.size()]);
                        ComposeDialog.setAttachedFiles(files);
                    }

                    //If attachment, saves it to file and adds it to attached files
                    else if (Part.ATTACHMENT.equalsIgnoreCase(bodyPart.getDisposition())) {
                        File file = new File(bodyPart.getFileName());
                        bodyPart.saveFile(file);
                        mListFiles.add(file);
                        File[] files = mListFiles.toArray(new File[mListFiles.size()]);
                        ComposeDialog.setAttachedFiles(files);
                    }

                    //If inline part, treats it as an attachment and saves it to file and adds it to attached files
                    else if (Part.INLINE.equalsIgnoreCase(bodyPart.getDisposition())) {
                        File file;
                        if (bodyPart.getFileName() == null) {
                            file = new File("filename");
                        } else {
                            file = new File(bodyPart.getFileName());
                        }
                        bodyPart.saveFile(file);
                        mListFiles.add(file);
                        File[] files = mListFiles.toArray(new File[mListFiles.size()]);
                        ComposeDialog.setAttachedFiles(files);
                    }

                    //If (nestled) multipart, call the handleContent method again
                    else if (bodyPart.isMimeType("multipart/*")) {
                        handleContent(bodyPart);
                    }

                    //Else, treats the part as an attachment and saves it to file and adds it to attached files
                    else {
                        File file = new File("filename");
                        bodyPart.saveFile(file);
                        mListFiles.add(file);
                        File[] files = mListFiles.toArray(new File[mListFiles.size()]);
                        ComposeDialog.setAttachedFiles(files);
                    }
                }
            }
        } catch (MessagingException | IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets attached files for the new message
     *
     * @return array of files
     */
    public static File[] getAttachedFiles() {
        return attachedFiles;
    }

    /**
     * Sets the attached files for the new message
     *
     * @param files attached files
     */
    public static void setAttachedFiles(File[] files) {
        if (files != null)
            attachedFiles = files;
    }

    /**
     * Removes attached files for the message
     */
    public static void removeAttachedFiles() {
        attachedFiles = null;
    }

    /**
     * Sends the composed mail
     */
    public void sendMail() {
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));                 //Sets the cursor to wait cursor

        if (mTxtTo.getText().equals("")) {
            JOptionPane.showMessageDialog(getContentPane(), "To field cannot be empty!");
            return;
        }

        //Calls the sendMail method in the MailHandler class
        int returnVal = MailHandler.sendMail(mTxtTo.getText(), mTxtSubject.getText(), mEditorPane.getText(),
                mTxtCc.getText(), mTxtBcc.getText());

        //If the mail was sent, informs the user
        if (returnVal == 0)
            JOptionPane.showMessageDialog(getContentPane(), "Mail sent successfully!");
        else
            JOptionPane.showMessageDialog(getContentPane(), "There was a problem with sending the mail. Try again!");

        setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));              //Sets the cursor to default cursor
    }

    //Class for sending mail
    class SendMailTask extends Thread {
        public void run() {
            sendMail();
        }
    }
}
