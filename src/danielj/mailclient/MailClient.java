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
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.html.HTMLDocument;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Properties;

/**
 * Main application class, for the JFrame
 *
 * @author Daniel Johansson
 */
public class MailClient extends JFrame implements ListSelectionListener, ActionListener {

    //GUI variables
    private JTable mTable;
    private JTextField mTxtFrom;
    private JTextField mTxtTo;
    private JTextField mTxtSubject;
    private JEditorPane mEditorPane;
    private JButton mBtnViewAttachments;
    private JButton mBtnShowContent;
    private JButton mBtnGetMail;
    private JButton mBtnCompose;
    private JButton mBtnReply;
    private JButton mBtnForward;
    private JButton mBtnDelete;
    private JMenuItem mMenuItemGetMail;
    private JMenuItem mMenuItemPreferences;
    private JMenuItem mMenuItemComposeMail;
    private JMenuItem mMenuItemExit;
    private JScrollPane mScrollPane;

    /**
     * DefaultTableModel för the JTable
     */
    private DefaultTableModel mModel;

    /**
     * Array of messages
     */
    private Message[] mMessages = null;

    /**
     * List of attachments
     */
    private java.util.List<MimeBodyPart> mListAttachments;

    /**
     * List of attached filenames
     */
    private java.util.List<String> mListFilenames;

    /**
     * PreferencesDialog
     */
    private PreferencesDialog mPrefsDialog;

    /**
     * Flag to set HTML viewing mode. True==in HTML viewing mode. False==in text viewing mode. Set to true as default
     */
    private static boolean htmlViewingMode = true;

    /**
     * Flag for content of a message. True==HTML content is available
     */
    private static boolean isHtmlAvailable = false;

    /**
     * Flag for content of a message. True==Text/plain content is available
     */
    private static boolean isTextAvailable = false;

    /**
     * Launches the application
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    Locale.setDefault(new Locale("en", "US"));        //Sets the default locale
                    MailClient window = new MailClient();
                    window.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Creates the JFrame
     */
    public MailClient() {
        initComponents();
    }

    /**
     * Inits GUI components
     */
    public void initComponents() {
        setTitle("MailClient");
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 1073, 882);
        JPanel contentPane = new JPanel();
        setContentPane(contentPane);

        JMenuBar menuBar = new JMenuBar();
        ;
        JMenu menuFile = new JMenu("File");
        menuFile.setMnemonic(KeyEvent.VK_F);
        JMenu menuMail = new JMenu("Mail");
        menuMail.setMnemonic(KeyEvent.VK_M);
        mMenuItemGetMail = new JMenuItem("Check Mail", KeyEvent.VK_C);
        mMenuItemGetMail.addActionListener(this);
        mMenuItemComposeMail = new JMenuItem("Compose Mail", KeyEvent.VK_T);
        mMenuItemComposeMail.addActionListener(this);
        mMenuItemPreferences = new JMenuItem("Preferences", KeyEvent.VK_P);
        mMenuItemPreferences.addActionListener(this);
        mMenuItemExit = new JMenuItem("Exit", KeyEvent.VK_P);
        mMenuItemExit.addActionListener(this);

        menuFile.add(mMenuItemPreferences);
        menuFile.add(mMenuItemExit);
        menuMail.add(mMenuItemGetMail);
        menuMail.add(mMenuItemComposeMail);
        menuBar.add(menuFile);
        menuBar.add(menuMail);
        setJMenuBar(menuBar);

        mBtnGetMail = new JButton("Check Mail");
        mBtnGetMail.setToolTipText("Check Mail");
        mBtnGetMail.addActionListener(this);

        mScrollPane = new JScrollPane();
        JScrollPane scrollPaneEditor = new JScrollPane();

        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(220, 220, 220));

        mBtnCompose = new JButton("Compose Mail");
        mBtnCompose.setToolTipText("Composes mail");
        mBtnCompose.addActionListener(this);

        GroupLayout gl_contentPane = new GroupLayout(contentPane);
        gl_contentPane.setHorizontalGroup(
                gl_contentPane.createParallelGroup(Alignment.TRAILING)
                        .addGroup(gl_contentPane.createSequentialGroup()
                                .addContainerGap(459, Short.MAX_VALUE)
                                .addComponent(mBtnGetMail)
                                .addGap(83)
                                .addComponent(mBtnCompose)
                                .addGap(369))
                        .addGroup(gl_contentPane.createSequentialGroup()
                                .addContainerGap(137, Short.MAX_VALUE)
                                .addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING, false)
                                        .addComponent(headerPanel, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(mScrollPane, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 907, Short.MAX_VALUE)
                                        .addComponent(scrollPaneEditor, Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 953, GroupLayout.PREFERRED_SIZE))
                                .addGap(59))
        );
        gl_contentPane.setVerticalGroup(
                gl_contentPane.createParallelGroup(Alignment.LEADING)
                        .addGroup(gl_contentPane.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
                                        .addComponent(mBtnGetMail)
                                        .addComponent(mBtnCompose))
                                .addGap(18)
                                .addComponent(mScrollPane, GroupLayout.PREFERRED_SIZE, 326, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(ComponentPlacement.UNRELATED)
                                .addComponent(headerPanel, GroupLayout.PREFERRED_SIZE, 109, GroupLayout.PREFERRED_SIZE)
                                .addGap(18)
                                .addComponent(scrollPaneEditor, GroupLayout.DEFAULT_SIZE, 263, Short.MAX_VALUE)
                                .addContainerGap())
        );

        mEditorPane = new JEditorPane();
        scrollPaneEditor.setViewportView(mEditorPane);
        mEditorPane.setEditable(false);

        JLabel lblSubject = new JLabel("Subject");
        lblSubject.setFont(new Font("Tahoma", Font.PLAIN, 12));

        mTxtFrom = new JTextField();
        mTxtFrom.setEditable(false);
        mTxtFrom.setBackground(UIManager.getColor("TextField.disabledBackground"));
        mTxtFrom.setColumns(10);
        mTxtFrom.setFont(new Font("Tahoma", Font.PLAIN, 12));

        JLabel lblFrom = new JLabel("From:");
        lblFrom.setLabelFor(mTxtFrom);
        lblFrom.setFont(new Font("Tahoma", Font.PLAIN, 12));
        JLabel lblTo = new JLabel("To:");
        lblTo.setFont(new Font("Tahoma", Font.PLAIN, 12));

        mTxtTo = new JTextField();
        mTxtTo.setEditable(false);
        mTxtTo.setBackground(UIManager.getColor("TextField.disabledBackground"));
        lblTo.setLabelFor(mTxtTo);
        mTxtTo.setFont(new Font("Tahoma", Font.PLAIN, 12));
        mTxtTo.setColumns(10);

        mBtnForward = new JButton("Forward");
        mBtnForward.setToolTipText("Forwards message");
        mBtnForward.addActionListener(this);

        mBtnDelete = new JButton("Delete");
        mBtnDelete.setToolTipText("Deletes message");
        mBtnDelete.addActionListener(this);

        mBtnReply = new JButton("Reply");
        mBtnReply.setToolTipText("Replies to message");
        mBtnReply.addActionListener(this);

        mTxtSubject = new JTextField();
        lblSubject.setLabelFor(mTxtSubject);
        mTxtSubject.setFont(new Font("Tahoma", Font.PLAIN, 12));
        mTxtSubject.setEditable(false);
        mTxtSubject.setColumns(10);
        mTxtSubject.setBackground(UIManager.getColor("TextArea.disabledBackground"));

        mBtnViewAttachments = new JButton("View attachments");
        mBtnViewAttachments.setVisible(true);
        mBtnViewAttachments.setEnabled(false);
        mBtnViewAttachments.setToolTipText("Show attachments");
        mBtnViewAttachments.addActionListener(this);

        mBtnShowContent = new JButton("Show as Text");
        mBtnShowContent.addActionListener(this);

        GroupLayout gl_headerPanel = new GroupLayout(headerPanel);
        gl_headerPanel.setHorizontalGroup(
                gl_headerPanel.createParallelGroup(Alignment.TRAILING)
                        .addGroup(gl_headerPanel.createSequentialGroup()
                                .addGroup(gl_headerPanel.createParallelGroup(Alignment.LEADING)
                                        .addGroup(gl_headerPanel.createSequentialGroup()
                                                .addComponent(lblSubject)
                                                .addPreferredGap(ComponentPlacement.RELATED)
                                                .addComponent(mTxtSubject, GroupLayout.DEFAULT_SIZE, 465, Short.MAX_VALUE))
                                        .addGroup(gl_headerPanel.createSequentialGroup()
                                                .addGroup(gl_headerPanel.createParallelGroup(Alignment.LEADING)
                                                        .addComponent(lblFrom)
                                                        .addComponent(lblTo))
                                                .addPreferredGap(ComponentPlacement.UNRELATED)
                                                .addGroup(gl_headerPanel.createParallelGroup(Alignment.LEADING)
                                                        .addComponent(mTxtTo, GroupLayout.DEFAULT_SIZE, 467, Short.MAX_VALUE)
                                                        .addComponent(mTxtFrom, GroupLayout.DEFAULT_SIZE, 467, Short.MAX_VALUE))))
                                .addGap(30)
                                .addGroup(gl_headerPanel.createParallelGroup(Alignment.LEADING)
                                        .addGroup(gl_headerPanel.createSequentialGroup()
                                                .addComponent(mBtnReply, GroupLayout.PREFERRED_SIZE, 91, GroupLayout.PREFERRED_SIZE)
                                                .addGap(18)
                                                .addComponent(mBtnForward))
                                        .addComponent(mBtnShowContent))
                                .addGap(18)
                                .addGroup(gl_headerPanel.createParallelGroup(Alignment.LEADING)
                                        .addComponent(mBtnDelete)
                                        .addComponent(mBtnViewAttachments))
                                .addGap(35))
        );
        gl_headerPanel.setVerticalGroup(
                gl_headerPanel.createParallelGroup(Alignment.LEADING)
                        .addGroup(gl_headerPanel.createSequentialGroup()
                                .addGap(32)
                                .addGroup(gl_headerPanel.createParallelGroup(Alignment.LEADING)
                                        .addGroup(gl_headerPanel.createSequentialGroup()
                                                .addGroup(gl_headerPanel.createParallelGroup(Alignment.BASELINE)
                                                        .addComponent(lblSubject)
                                                        .addComponent(mTxtSubject, GroupLayout.PREFERRED_SIZE, 15, GroupLayout.PREFERRED_SIZE))
                                                .addPreferredGap(ComponentPlacement.RELATED)
                                                .addGroup(gl_headerPanel.createParallelGroup(Alignment.BASELINE)
                                                        .addComponent(lblFrom)
                                                        .addComponent(mTxtFrom, GroupLayout.PREFERRED_SIZE, 15, GroupLayout.PREFERRED_SIZE))
                                                .addPreferredGap(ComponentPlacement.RELATED)
                                                .addGroup(gl_headerPanel.createParallelGroup(Alignment.BASELINE)
                                                        .addComponent(lblTo)
                                                        .addComponent(mTxtTo, GroupLayout.PREFERRED_SIZE, 16, GroupLayout.PREFERRED_SIZE)))
                                        .addGroup(gl_headerPanel.createSequentialGroup()
                                                .addGroup(gl_headerPanel.createParallelGroup(Alignment.BASELINE)
                                                        .addComponent(mBtnForward)
                                                        .addComponent(mBtnDelete)
                                                        .addComponent(mBtnReply))
                                                .addPreferredGap(ComponentPlacement.UNRELATED)
                                                .addGroup(gl_headerPanel.createParallelGroup(Alignment.BASELINE)
                                                        .addComponent(mBtnShowContent, GroupLayout.PREFERRED_SIZE, 29, GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(mBtnViewAttachments))))
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        headerPanel.setLayout(gl_headerPanel);

        mListAttachments = new ArrayList<>();
        mListFilenames = new ArrayList<>();

        handleProperties();                             //Handles properties

        createTable();                                    //Creates the JTable

        contentPane.setLayout(gl_contentPane);

        selectAndScrollToLastRow();                     //Selects and scrolls to the last row in the JTable
    }

    /**
     * Handles properties
     */
    public void handleProperties() {
        //If properties file exists
        if (MailHandler.propertiesFileExists()) {
            //If preferences hasn´t been set, opens a new PreferencesDialog so the user can enter mail settings
            if (MailHandler.checkPreferencesStatus() == false) {
                mPrefsDialog = new PreferencesDialog(this, "Preferences", true);
            }
            //If preferences has been set, loads properties from file and sets them in MailHandler class
            else {
                Properties props = loadProperties();

                if (props != null) {
                    MailHandler.setProperties(props);
                }
            }
        } else {
            mPrefsDialog = new PreferencesDialog(this, "Preferences", true);
            handleProperties();
        }
    }

    /**
     * Selects the last row of the JTable and scrolls to it
     */
    public void selectAndScrollToLastRow() {
        int numberOfRows = mTable.getModel().getRowCount();

        //Selects the last row of the JTable
        if (numberOfRows != 0) {
            mTable.setRowSelectionInterval(numberOfRows - 1, numberOfRows - 1);
            mTable.scrollRectToVisible(mTable.getCellRect(numberOfRows - 1, 0, true));
        }
    }

    //Event handling for JTable
    public void valueChanged(ListSelectionEvent event) {
        if (!event.getValueIsAdjusting())
            displayMessageInfo();
    }

    //Event handling for JButtons
    public void actionPerformed(ActionEvent e) {

        //If mBtnGetMail is clicked, updates JTable with new mail data
        if (e.getSource() == mBtnGetMail) {
            (new CheckMailTask()).start();
        }

        //If mBtnCompose is clicked, opens ComposeDialog dialog
        if (e.getSource() == mBtnCompose) {
            MailHandler.setMessageFlag("");

            //To get the JFrame which is used in ComposeDialog
            Component component = (Component) e.getSource();
            JFrame frame = (JFrame) SwingUtilities.getRoot(component);
            ComposeDialog composeDialog = new ComposeDialog(frame, "Compose mail", true);
        }

        //Gets mail
        if (e.getSource() == mMenuItemGetMail) {
            (new CheckMailTask()).start();
        }

        //Exists the application
        if (e.getSource() == mMenuItemExit) {
            System.exit(0);
        }

        //Opens a PreferencesDialog
        if (e.getSource() == mMenuItemPreferences) {
            //To get the JFrame which is used in ComposeDialog()
            Component component = (Component) e.getSource();
            JFrame frame = (JFrame) SwingUtilities.getRoot(component);
            mPrefsDialog = new PreferencesDialog(frame, "Preferences", true);
        }

        //Opens a ComposeDialog to compose a new mail
        if (e.getSource() == mMenuItemComposeMail) {
            MailHandler.setMessageFlag("");

            //To get the JFrame which is used in ComposeDialog
            Component component = (Component) e.getSource();
            JFrame frame = (JFrame) SwingUtilities.getRoot(component);
            ComposeDialog composeDialog = new ComposeDialog(frame, "Compose mail", true);
        }

        //Shows a message´s attachments
        if (e.getSource() == mBtnViewAttachments) {
            //To get the JDialog which is used in ShowAttachmentDialog constructor
            Component component = (Component) e.getSource();
            JFrame window = (JFrame) SwingUtilities.getRoot(component);
            ShowAttachmentDialog dialog = new ShowAttachmentDialog(window, "Show attachments", true, mListFilenames.toArray(new String[mListFilenames.size()]), mListAttachments.toArray(new MimeBodyPart[mListAttachments.size()]));

        }

        //Opens a ComposeDialog so the user can reply to a message
        if (e.getSource() == mBtnReply) {
            Component component = (Component) e.getSource();
            JFrame frame = (JFrame) SwingUtilities.getRoot(component);

            int selectedRow = mTable.getSelectedRow();
            MailHandler.setMessage(mMessages[selectedRow]);         //Sets the message to be replied to in MailHandler
            MailHandler.setMessageFlag("reply");                    //Sets the messageFlag to "reply"

            ComposeDialog composeDialog = new ComposeDialog(frame, "Compose mail", true);
        }

        //Opens a ComposeDialog so the user can forward a message
        if (e.getSource() == mBtnForward) {
            Component component = (Component) e.getSource();
            JFrame frame = (JFrame) SwingUtilities.getRoot(component);

            int selectedRow = mTable.getSelectedRow();
            MailHandler.setMessage(mMessages[selectedRow]);           //Sets the message to be forwarded in MailHandler
            MailHandler.setMessageFlag("forward");                    //Sets the messageFlag to "forward"

            ComposeDialog composeDialog = new ComposeDialog(frame, "Compose mail", true);
        }

        //Deletes a message
        if (e.getSource() == mBtnDelete) {
            deleteMessage();
        }

        //Changes the viewing mode of the message
        if (e.getSource() == mBtnShowContent) {
            changeViewMode();
        }
    }

    /**
     * Deletes the selected message
     */
    public void deleteMessage() {
        int input = JOptionPane.showConfirmDialog(MailClient.this,
                "Do you want to delete this message?", "Confirm deletion", JOptionPane.YES_NO_OPTION);

        //If message deletion confirmed
        if (input == 0) {
            int selectedRow = mTable.getSelectedRow();         //Gets the selected Message (row)

            try {
                //From the local mailbox
                Message localMessage = mMessages[selectedRow];
                String localMessageSubject = localMessage.getSubject();
                String localMessageSentDate = localMessage.getSentDate().toString();
                InternetAddress[] localMessageAdressFrom = (InternetAddress[]) localMessage.getFrom();
                String localMessageFrom = localMessageAdressFrom[0].toUnicodeString();
                int localMessageNumber = localMessage.getMessageNumber();

                //Deletes the message
                MailHandler.deleteMessage(localMessageSubject, localMessageSentDate, localMessageFrom, localMessageNumber);

                updateTable();                                      //Updates the Jtable to refresh

            } catch (MessagingException me) {
                me.printStackTrace();
            }
        }
    }

    /**
     * Changes the viewing mode - content is shown as HTML or plain text in the EditorPane
     */
    public void changeViewMode() {
        //If user is in html viewing mode, then switches to text viewing mode
        if (htmlViewingMode == true) {
            mBtnShowContent.setText("Show as HTML");
            htmlViewingMode = false;                           //Sets flag to text viewing mode (==false)
            displayMessageInfo();                              //Displays the message
        }
        //If user is in text viewing mode, then switches to HTML viewing mode
        else {
            mBtnShowContent.setText("Show as Text");
            htmlViewingMode = true;                              //Sets flag to HTML viewing mode (==true)
            displayMessageInfo();                                //Displays the message
        }
    }

    /**
     * Displays information (headers) about a message (selected by the user) in textfields and calls handleMessageContent
     * for further handling of the message
     */
    public void displayMessageInfo() {
        isHtmlAvailable = false;
        isTextAvailable = false;

        int selectedRow = mTable.getSelectedRow();

        //If there´s a selected row
        if (selectedRow != -1) {
            try {
                Message message = mMessages[selectedRow];

                //Clears the list of attachments and the list of attached filenames
                mListAttachments.clear();
                mListFilenames.clear();

                //Only enable Attachments button if there are attachments
                mBtnViewAttachments.setEnabled(false);

                //Adds Subject text
                mTxtSubject.setText(message.getSubject());
                mTxtSubject.setCaretPosition(0);

                //Adds From text
                InternetAddress[] adressFrom = (InternetAddress[]) message.getFrom();
                mTxtFrom.setText(adressFrom[0].toUnicodeString());
                mTxtFrom.setCaretPosition(0);

                //Adds To text
                InternetAddress[] adressTo = (InternetAddress[]) message
                        .getRecipients(Message.RecipientType.TO);

                StringBuilder sb = new StringBuilder();

                //If more than 1 recipient, adds them
                if (adressTo.length > 1) {
                    for (int i = 0; i < adressTo.length; i++) {
                        sb.append(adressTo[i].toUnicodeString());
                        sb.append(", ");
                    }
                    mTxtTo.setText(sb.toString());
                    mTxtTo.setCaretPosition(0);
                }

                //If just 1 recipient, adds it
                else {
                    mTxtTo.setText(adressTo[0].toUnicodeString());
                    mTxtTo.setCaretPosition(0);
                }

                //Handles the content of the message
                handleMessageContent(message);

            } catch (MessagingException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Handles the content of a message
     *
     * @param part the message/part to handle
     */
    public void handleMessageContent(Part part) {
        try {
            //If the part is plain text
            if (part.isMimeType("text/plain")) {
                isTextAvailable = true;                           //Sets the flag to indicate that text/plain is available
                displayMessageContent(part);                    //Displays the content of the message in the JEditorPane
            }

            //If the part is HTML
            else if (part.isMimeType("text/html")) {
                isHtmlAvailable = true;                           //Sets the flag to indicate that text/html is available
                displayMessageContent(part);                    //Displays the content of the message in the JEditorPane
            }

            //If the part is multipart
            else if (part.isMimeType("multipart/*")) {
                Multipart multiPart = (Multipart) part.getContent();                    //Gets the Multipart
                int count = multiPart.getCount();                                       //Gets number of parts

                //For all multiparts
                for (int i = 0; i < count; i++) {
                    MimeBodyPart bodyPart = (MimeBodyPart) multiPart.getBodyPart(i);

                    //If bodypart is "text/plain" and not an attachment
                    if (bodyPart.isMimeType("text/plain") && i == 0 &&
                            (!Part.ATTACHMENT.equalsIgnoreCase(bodyPart.getDisposition()))) {
                        isTextAvailable = true;
                        displayMessageContent(bodyPart);                    //Displays the content in the JEditorPane

                        //If bodypart is "text/html"
                    } else if (bodyPart.isMimeType("text/html") &&
                            (!Part.ATTACHMENT.equalsIgnoreCase(bodyPart.getDisposition()))) {
                        isHtmlAvailable = true;
                        displayMessageContent(bodyPart);                    //Displays the content in the JEditorPane
                    }
                    //If attachment
                    else if (Part.ATTACHMENT.equalsIgnoreCase(bodyPart.getDisposition())) {
                        //To show attached files names in the AttachmentFrame JTable
                        String fileName = bodyPart.getFileName();

                        mListAttachments.add(bodyPart);         //Adds the part to the list of attachments
                        mListFilenames.add(fileName);           //Adds the filename of the part to the list of filenames

                        mBtnViewAttachments.setEnabled(true);   //Enables the View attachments JButton
                    }

                    //If bodypart is a nestled multipart
                    else if (bodyPart.isMimeType("multipart/*")) {
                        //If (nestled) multipart, calls the handleMessageContent method again
                        handleMessageContent(bodyPart);
                    }
                }
            }

        } catch (MessagingException | IOException e) {
            e.printStackTrace();
        }

        //If both plain text and html is unavailable, shows error dialog and returns
        if (isHtmlAvailable == false && isTextAvailable == false) {
            mEditorPane.setText("");
            JOptionPane.showMessageDialog(this,
                    "Message has no text content to display!");
            return;
        }

        //If HTML isn´t available but the user is in HTML viewing mode, then calls changeViewMode method to change
        //viewing mode to text viewing mode and shows error dialog
        if (htmlViewingMode == true && isHtmlAvailable == false) {
            changeViewMode();                                   //
            JOptionPane.showMessageDialog(this,
                    "HTML is not available for this message. \nViewing mode changed to text viewing mode");
        }

        //If text/plain isn´t available but the user is in text viewing mode, then calls changeViewMode method to change
        //viewing mode to HTML viewing mode
        if (htmlViewingMode == false && isTextAvailable == false) {
            changeViewMode();
            JOptionPane.showMessageDialog(this,
                    "Plain text is not available for this message. \nViewing mode changed to HTML viewing mode");
        }
    }

    /**
     * Displays message content in the JEditorPane
     *
     * @param part message/part that holds the content
     */
    public void displayMessageContent(Part part) {
        try {
            //If htmlViewingMode==false and Mimetype of content is "text/plain", then sets the content of the message
            //to the JEditorPane
            if (part.isMimeType("text/plain") && htmlViewingMode == false && isTextAvailable == true) {
                mEditorPane.setContentType("text/plain");
                mEditorPane.setText((String) part.getContent());
                mEditorPane.setEditable(false);
                mEditorPane.setCaretPosition(0);
            }

            //If htmlViewingMode==true and Mimetype of content is "text/html", then calls displayHtml method to display
            //the message
            else if (htmlViewingMode == true && part.isMimeType("text/html") && isHtmlAvailable == true) {
                mEditorPane.setContentType("text/html");
                HTMLDocument currentHTML = (HTMLDocument) mEditorPane.getDocument();
                currentHTML.putProperty("IgnoreCharsetDirective", Boolean.TRUE);    //Needed for charset problems
                displayHtml(part);
            }

        } catch (MessagingException | IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Displays the part´s HTML content in the JEditorPane
     *
     * @param part message/part that holds the content
     */
    public void displayHtml(Part part) {
        try {
            mEditorPane.setText((String) part.getContent());
            //If theres a problem with setting the HTML content (JEditorPane has limited HTML capabilities),
            //changes viewing mode to text instead
        } catch (RuntimeException r) {
            changeViewMode();
            JOptionPane.showMessageDialog(this, "Unable to show the HTML content of this " +
                    "message due to\na limitation in the HTML capabilities of Java/Swing (JEditorPane)");
        } catch (IOException | MessagingException e) {
            e.printStackTrace();
        }
        mEditorPane.setEditable(false);
        mEditorPane.setCaretPosition(0);
    }

    /**
     * Creates and sets up the JTable
     */
    public void createTable() {
        MailHandler.deleteMailbox();                            //Deletes the content of the local mailbox
        mMessages = MailHandler.getMail();                      //Gets mail from mail server

        //If messages has been retrieved
        if (mMessages != null) {
            MailHandler.createMbox(mMessages);                  //Creates local mailbox and fills it with messages
            MailHandler.disconnect();                           //Disconnects from mail server
            mMessages = MailHandler.getLocalMail();             //Gets mail from local mail box

            //Gets mail data and inits the DefaultTableModel with it
            String[] header = {"Subject", "From", "Sent date"};
            Object[][] obj = MailHandler.getMailData(mMessages);
            mModel = new DefaultTableModel(obj, header);
            mTable = new JTable(mModel);
            mTable.getSelectionModel().addListSelectionListener(this);

            //JTable settings
            mTable.setFont(new Font("Tahoma", Font.PLAIN, 16));
            mTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            mTable.getColumnModel().getColumn(0).setPreferredWidth(515);
            mTable.getColumnModel().getColumn(1).setPreferredWidth(250);
            mTable.getColumnModel().getColumn(2).setPreferredWidth(170);

            mScrollPane.setViewportView(mTable);
            mTable.setSelectionMode(0);
        }
        //If messages hasn´t been retrieved
        else {
            //If theres a problem with connecting to the mail server (due to wrong mail server settings)
            if (MailHandler.getWrongServerSettingsFlag() == true) {
                JOptionPane.showMessageDialog(this, "Mail server problem. Check your preferences!");

                //Opens a PreferencesDialog so the user can enter correct mail server settings
                mPrefsDialog = new PreferencesDialog(this, "Preferences", true);

                //Loads the new properties
                Properties props = loadProperties();

                if (props != null)
                    MailHandler.setProperties(props);                   //Sets the properties in MailHandler class

                createTable();                                          //Calls the createTable method again
            }
            //If theres a messaging problem
            else {
                JOptionPane.showMessageDialog(this, "Messaging problem. Retrying");
                createTable();                                          //Calls the createTable method again
            }
        }
    }

    /**
     * Updates the JTable with new mail data
     */
    public void updateTable() {
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));      //Sets the mouse cursor to wait cursor

        int tempMailCount = MailHandler.getMailCount();                    //Mail count before checking new mail
        MailHandler.deleteMailbox();                                    //Deletes the content of the local mailbox
        mMessages = MailHandler.getMail();                              //Gets mail from mail server

        if (mMessages != null) {
            MailHandler.createMbox(mMessages);                          //Creates local mailbox and fills it with messages
            MailHandler.disconnect();                                   //Disconnects from the mail server
            mMessages = MailHandler.getLocalMail();                     //Gets mail from local mail box

            //Updates the JTable with new data
            String[] header = {"Subject", "From", "Sent date"};
            Object[][] obj = MailHandler.getMailData(mMessages);
            mModel.setDataVector(obj, header);

            //JTable settings
            mTable.setFont(new Font("Tahoma", Font.PLAIN, 16));
            mTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            mTable.getColumnModel().getColumn(0).setPreferredWidth(515);
            mTable.getColumnModel().getColumn(1).setPreferredWidth(250);
            mTable.getColumnModel().getColumn(2).setPreferredWidth(170);

            //If new mail count is higher than earlier mail count, informs user
            if (MailHandler.getMailCount() > tempMailCount) {
                JOptionPane.showMessageDialog(this, "New mail!");
                selectAndScrollToLastRow();
            } else
                JOptionPane.showMessageDialog(this, "No new mail");
        } else {
            //If theres a problem with connecting to the mail server (due to wrong mail server settings)
            if (MailHandler.getWrongServerSettingsFlag() == true) {
                JOptionPane.showMessageDialog(this, "Mail server problem. Check your preferences!");

                //Opens a PreferencesDialog so the user can enter correct mail server settings
                mPrefsDialog = new PreferencesDialog(this, "Preferences", true);

                //Loads the new properties
                Properties props = loadProperties();

                if (props != null)
                    MailHandler.setProperties(props);           //Sets the properties in MailHandler

                updateTable();                                  //Calls the updateTable method again
            }
            //If theres a messaging problem
            else {
                JOptionPane.showMessageDialog(this, "Messaging problem. Retrying");
                updateTable();                                  //Calls the updateTable method again
            }
        }

        setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));           //Sets the mouse cursor to the default
    }

    /**
     * Loads properties from file
     *
     * @return saved properties
     */
    public Properties loadProperties() {
        Properties props = new Properties();
        File configFile = new File("properties");

        try {
            //Loads properties from file
            FileReader reader = new FileReader(configFile);
            props.load(reader);
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return props;
    }

    //Class for checking mail (updating the JTable of MailClient)
    class CheckMailTask extends Thread {
        public void run() {
            updateTable();
        }
    }
}

