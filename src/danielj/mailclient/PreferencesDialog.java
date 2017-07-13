package danielj.mailclient;

import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.util.Properties;

/**
 * Class for preferences dialog
 *
 * @author Daniel Johansson
 */
public class PreferencesDialog extends JDialog implements ActionListener {
    //GUI variables
    private JTextField mTxtPop3Username;
    private JTextField mTxtPop3Hostname;
    private JPasswordField mTxtPop3Password;
    private JTextField mTxtPop3Port;
    private JComboBox mComboBoxPop3Encryption;
    private JComboBox mComboBoxSmtpEncryption;
    private JCheckBox mChckbxSmtpAuth;
    private JTextField mTxtSmtpHost;
    private JTextField mTxtSmtpPort;
    private JTextField mTxtSmtpUsername;
    private JPasswordField mTxtSmtpPassword;
    private JButton mBtnSavePreferences;
    private JTextField mTxtEmail;

    //Items for comboboxes
    private String mItem1Pop3 = "None";
    private String mItem2Pop3 = "SSL";
    private String mItem1Smtp = "None";
    private String mItem2Smtp = "SSL";
    private String mItem3Smtp = "STARTTLS";

    /**
     * Constructor for the dialog
     */
    public PreferencesDialog(JFrame parent, String title, boolean modal) {
        super(parent, title, modal);

        initComponents();
    }

    /**
     * Inits GUI components
     */
    public void initComponents() {
        setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);

        //Handles window events
        addWindowListener(new WindowAdapter() {
            //If the dialog window has been opened - checks if preferences has been set and loads values from properties
            //file to textfields and other components. Then sets properties in MailHandler class
            @Override
            public void windowOpened(WindowEvent we) {
                if (MailHandler.propertiesFileExists()) {
                    if (MailHandler.checkPreferencesStatus() == true) {
                        Properties props = loadProperties();
                        if (props != null)
                            MailHandler.setProperties(props);
                    }
                }
            }

            //If the user has tried to close the dialog window, checks if preferences has been set and closes dialog if true
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                handleDialogClosing();
            }
        });

        setBounds(100, 100, 715, 810);
        JPanel contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);

        mTxtPop3Username = new JTextField();
        mTxtPop3Username.setColumns(10);

        JLabel lblPop3UserName = new JLabel("Username:");
        lblPop3UserName.setHorizontalAlignment(SwingConstants.RIGHT);
        lblPop3UserName.setLabelFor(mTxtPop3Username);

        JLabel lblPop3Host = new JLabel("Host:");
        lblPop3Host.setHorizontalTextPosition(SwingConstants.CENTER);
        lblPop3Host.setHorizontalAlignment(SwingConstants.RIGHT);

        mTxtPop3Hostname = new JTextField();
        lblPop3Host.setLabelFor(mTxtPop3Hostname);
        mTxtPop3Hostname.setColumns(10);

        JLabel lblPop3Password = new JLabel("Password:");
        lblPop3Password.setHorizontalAlignment(SwingConstants.RIGHT);

        mTxtPop3Password = new JPasswordField();
        lblPop3Password.setLabelFor(mTxtPop3Password);
        mTxtPop3Password.setColumns(10);

        JLabel lblPop3 = new JLabel("POP3 settings:");
        lblPop3.setFont(new Font("Tahoma", Font.PLAIN, 18));

        mTxtPop3Port = new JTextField();
        mTxtPop3Port.setColumns(10);

        JLabel lblPop3Port = new JLabel("Port:");
        lblPop3Port.setLabelFor(mTxtPop3Port);

        JList list = new JList();

        mComboBoxPop3Encryption = new JComboBox();

        mComboBoxPop3Encryption.addItem(mItem1Pop3);
        mComboBoxPop3Encryption.addItem(mItem2Pop3);

        JLabel lblPop3Encryption = new JLabel("Encryption:");

        JSeparator separator = new JSeparator();
        separator.setForeground(Color.BLACK);

        JLabel lblSmtpSettings = new JLabel("SMTP settings:");
        lblSmtpSettings.setFont(new Font("Tahoma", Font.PLAIN, 18));

        mTxtSmtpHost = new JTextField();
        mTxtSmtpHost.setColumns(10);

        mTxtSmtpPort = new JTextField();
        mTxtSmtpPort.setColumns(10);

        JLabel lblSmtpPort = new JLabel("Port:");

        JLabel lblSmtpHost = new JLabel("Host:");
        lblSmtpHost.setHorizontalTextPosition(SwingConstants.CENTER);
        lblSmtpHost.setHorizontalAlignment(SwingConstants.RIGHT);

        mChckbxSmtpAuth = new JCheckBox("Authentication required");

        mTxtSmtpUsername = new JTextField();
        mTxtSmtpUsername.setColumns(10);

        JLabel lblSmtpUsername = new JLabel("Username:");
        lblSmtpUsername.setHorizontalAlignment(SwingConstants.RIGHT);

        mTxtSmtpPassword = new JPasswordField();
        mTxtSmtpPassword.setColumns(10);

        JLabel lblSmtpPassword = new JLabel("Password:");
        lblSmtpPassword.setHorizontalAlignment(SwingConstants.RIGHT);

        mComboBoxSmtpEncryption = new JComboBox();

        mComboBoxSmtpEncryption.addItem(mItem1Smtp);
        mComboBoxSmtpEncryption.addItem(mItem2Smtp);
        mComboBoxSmtpEncryption.addItem(mItem3Smtp);

        JLabel lblSmtpEncryption = new JLabel("Encryption:");

        JSeparator separator2 = new JSeparator();
        separator2.setForeground(Color.BLACK);

        JLabel lblEmail = new JLabel("Email:");

        mTxtEmail = new JTextField();
        lblEmail.setLabelFor(mTxtEmail);
        mTxtEmail.setColumns(10);

        mBtnSavePreferences = new JButton("Save preferences");
        mBtnSavePreferences.setToolTipText("Saves preferences");
        mBtnSavePreferences.addActionListener(this);

        GroupLayout gl_contentPane = new GroupLayout(contentPane);
        gl_contentPane.setHorizontalGroup(
                gl_contentPane.createParallelGroup(Alignment.TRAILING)
                        .addGroup(gl_contentPane.createSequentialGroup()
                                .addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
                                        .addGroup(gl_contentPane.createSequentialGroup()
                                                .addContainerGap()
                                                .addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
                                                        .addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING, false)
                                                                .addGroup(gl_contentPane.createSequentialGroup()
                                                                        .addGap(69)
                                                                        .addComponent(lblPop3UserName, GroupLayout.PREFERRED_SIZE, 81, GroupLayout.PREFERRED_SIZE))
                                                                .addComponent(lblPop3Host, GroupLayout.PREFERRED_SIZE, 150, GroupLayout.PREFERRED_SIZE)
                                                                .addComponent(lblPop3Password, GroupLayout.PREFERRED_SIZE, 150, GroupLayout.PREFERRED_SIZE))
                                                        .addComponent(lblPop3Encryption))
                                                .addGap(18)
                                                .addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
                                                        .addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING, false)
                                                                .addComponent(mTxtPop3Password, 334, 334, Short.MAX_VALUE)
                                                                .addComponent(mComboBoxPop3Encryption, GroupLayout.PREFERRED_SIZE, 90, GroupLayout.PREFERRED_SIZE)
                                                                .addComponent(mTxtPop3Username))
                                                        .addComponent(mTxtPop3Hostname, GroupLayout.PREFERRED_SIZE, 334, GroupLayout.PREFERRED_SIZE))
                                                .addGap(217))
                                        .addGroup(gl_contentPane.createSequentialGroup()
                                                .addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
                                                        .addGroup(gl_contentPane.createSequentialGroup()
                                                                .addContainerGap(466, Short.MAX_VALUE)
                                                                .addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
                                                                        .addGroup(gl_contentPane.createSequentialGroup()
                                                                                .addComponent(lblPop3Port)
                                                                                .addPreferredGap(ComponentPlacement.RELATED)
                                                                                .addComponent(mTxtPop3Port, GroupLayout.PREFERRED_SIZE, 50, GroupLayout.PREFERRED_SIZE))
                                                                        .addGroup(gl_contentPane.createSequentialGroup()
                                                                                .addComponent(list, GroupLayout.PREFERRED_SIZE, 1, GroupLayout.PREFERRED_SIZE)
                                                                                .addGap(204))))
                                                        .addGroup(gl_contentPane.createSequentialGroup()
                                                                .addGap(11)
                                                                .addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
                                                                        .addGroup(gl_contentPane.createSequentialGroup()
                                                                                .addComponent(lblSmtpHost, GroupLayout.PREFERRED_SIZE, 150, GroupLayout.PREFERRED_SIZE)
                                                                                .addGap(18)
                                                                                .addComponent(mTxtSmtpHost, GroupLayout.PREFERRED_SIZE, 334, GroupLayout.PREFERRED_SIZE))
                                                                        .addGroup(gl_contentPane.createSequentialGroup()
                                                                                .addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
                                                                                        .addComponent(lblSmtpEncryption)
                                                                                        .addComponent(lblSmtpPassword, GroupLayout.PREFERRED_SIZE, 81, GroupLayout.PREFERRED_SIZE))
                                                                                .addGap(18)
                                                                                .addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
                                                                                        .addComponent(mComboBoxSmtpEncryption, GroupLayout.PREFERRED_SIZE, 90, GroupLayout.PREFERRED_SIZE)
                                                                                        .addComponent(mTxtSmtpPassword, GroupLayout.PREFERRED_SIZE, 334, GroupLayout.PREFERRED_SIZE)))
                                                                        .addGroup(gl_contentPane.createSequentialGroup()
                                                                                .addComponent(lblSmtpUsername, GroupLayout.PREFERRED_SIZE, 81, GroupLayout.PREFERRED_SIZE)
                                                                                .addGap(18)
                                                                                .addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
                                                                                        .addComponent(mChckbxSmtpAuth)
                                                                                        .addComponent(mTxtSmtpUsername, GroupLayout.PREFERRED_SIZE, 334, GroupLayout.PREFERRED_SIZE))))
                                                                .addPreferredGap(ComponentPlacement.RELATED, 64, Short.MAX_VALUE)
                                                                .addComponent(lblSmtpPort, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(ComponentPlacement.RELATED)
                                                                .addComponent(mTxtSmtpPort, GroupLayout.PREFERRED_SIZE, 53, GroupLayout.PREFERRED_SIZE)))
                                                .addGap(63))
                                        .addGroup(gl_contentPane.createSequentialGroup()
                                                .addGap(91)
                                                .addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
                                                        .addComponent(separator, GroupLayout.PREFERRED_SIZE, 505, GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(lblSmtpSettings, GroupLayout.PREFERRED_SIZE, 127, GroupLayout.PREFERRED_SIZE))
                                                .addGap(138))
                                        .addGroup(gl_contentPane.createSequentialGroup()
                                                .addGap(94)
                                                .addComponent(lblPop3))
                                        .addGroup(gl_contentPane.createSequentialGroup()
                                                .addGap(88)
                                                .addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
                                                        .addGroup(gl_contentPane.createSequentialGroup()
                                                                .addGap(38)
                                                                .addComponent(lblEmail)
                                                                .addGap(18)
                                                                .addComponent(mTxtEmail, GroupLayout.PREFERRED_SIZE, 335, GroupLayout.PREFERRED_SIZE))
                                                        .addComponent(separator2, GroupLayout.PREFERRED_SIZE, 506, GroupLayout.PREFERRED_SIZE)))
                                        .addGroup(gl_contentPane.createSequentialGroup()
                                                .addGap(263)
                                                .addComponent(mBtnSavePreferences)))
                                .addContainerGap())
        );
        gl_contentPane.setVerticalGroup(
                gl_contentPane.createParallelGroup(Alignment.LEADING)
                        .addGroup(gl_contentPane.createSequentialGroup()
                                .addGap(8)
                                .addComponent(lblPop3)
                                .addGap(30)
                                .addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
                                        .addComponent(mTxtPop3Hostname, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(lblPop3Host)
                                        .addComponent(lblPop3Port)
                                        .addComponent(mTxtPop3Port, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addGap(33)
                                .addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
                                        .addComponent(mTxtPop3Username, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(lblPop3UserName))
                                .addGap(32)
                                .addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
                                        .addComponent(mTxtPop3Password, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(lblPop3Password))
                                .addGap(32)
                                .addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
                                        .addComponent(mComboBoxPop3Encryption, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(lblPop3Encryption))
                                .addGap(32)
                                .addComponent(separator, GroupLayout.PREFERRED_SIZE, 9, GroupLayout.PREFERRED_SIZE)
                                .addGap(18)
                                .addComponent(lblSmtpSettings, GroupLayout.PREFERRED_SIZE, 22, GroupLayout.PREFERRED_SIZE)
                                .addGap(33)
                                .addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
                                        .addComponent(mTxtSmtpHost, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(lblSmtpPort)
                                        .addComponent(lblSmtpHost)
                                        .addComponent(mTxtSmtpPort, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(ComponentPlacement.UNRELATED)
                                .addComponent(list, GroupLayout.PREFERRED_SIZE, 1, GroupLayout.PREFERRED_SIZE)
                                .addGap(12)
                                .addComponent(mChckbxSmtpAuth)
                                .addGap(18)
                                .addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
                                        .addComponent(mTxtSmtpUsername, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(lblSmtpUsername))
                                .addGap(28)
                                .addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
                                        .addComponent(mTxtSmtpPassword, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(lblSmtpPassword))
                                .addGap(30)
                                .addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
                                        .addComponent(mComboBoxSmtpEncryption, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(lblSmtpEncryption))
                                .addGap(34)
                                .addComponent(separator2, GroupLayout.PREFERRED_SIZE, 10, GroupLayout.PREFERRED_SIZE)
                                .addGap(27)
                                .addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
                                        .addComponent(mTxtEmail, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(lblEmail))
                                .addGap(18)
                                .addComponent(mBtnSavePreferences)
                                .addContainerGap(24, Short.MAX_VALUE))
        );

        contentPane.setLayout(gl_contentPane);

        setVisible(true);
        setResizable(false);
    }

    //Event handler
    public void actionPerformed(ActionEvent e) {
        //Saves properties
        if (e.getSource() == mBtnSavePreferences) {
            saveProperties();
        }
    }

    /**
     * Loads properties from file and sets textfields
     *
     * @return Properties (loaded from file)
     */
    public Properties loadProperties() {
        String pop3Host = "";
        String pop3Port = "";
        String pop3Username = "";
        String pop3Password = "";
        String pop3Encryption = "";

        String smtpHost = "";
        String smtpPort = "";
        String smtpUsername = "";
        String smtpPassword = "";
        String smtpEncryption = "";
        String smtpAuthentication = "";

        String email = "";

        Properties props = new Properties();

        File configFile = new File("properties");

        try {
            //Loads properties from file
            FileReader reader = new FileReader(configFile);
            props.load(reader);

            //Checks for null values
            if (props.getProperty("mail.pop3.host") != null)
                pop3Host = props.getProperty("mail.pop3.host");

            if (props.getProperty("mail.pop3.port") != null)
                pop3Port = props.getProperty("mail.pop3.port");

            if (props.getProperty("pop3Username") != null)
                pop3Username = props.getProperty("pop3Username");

            if (props.getProperty("pop3Password") != null)
                pop3Password = props.getProperty("pop3Password");

            if (props.getProperty("pop3Encryption") != null)
                pop3Encryption = props.getProperty("pop3Encryption");

            if (props.getProperty("smtpHost") != null)
                smtpHost = props.getProperty("smtpHost");

            if (props.getProperty("smtpPort") != null)
                smtpPort = props.getProperty("smtpPort");

            if (props.getProperty("smtpUsername") != null)
                smtpUsername = props.getProperty("smtpUsername");

            if (props.getProperty("smtpPassword") != null)
                smtpPassword = props.getProperty("smtpPassword");

            if (props.getProperty("smtpEncryption") != null)
                smtpEncryption = props.getProperty("smtpEncryption");

            if (props.getProperty("smtpAuthentication") != null)
                smtpAuthentication = props.getProperty("smtpAuthentication");

            if (props.getProperty("email") != null)
                email = props.getProperty("email");

            mTxtPop3Hostname.setText(pop3Host);
            mTxtPop3Port.setText(pop3Port);
            mTxtPop3Username.setText(pop3Username);
            mTxtPop3Password.setText(pop3Password);
            mTxtEmail.setText(email);

            if (pop3Encryption.equals("SSL"))
                mComboBoxPop3Encryption.setSelectedItem(mItem2Pop3);
            else if (pop3Encryption.equals("None"))
                mComboBoxPop3Encryption.setSelectedItem(mItem1Pop3);

            mTxtSmtpHost.setText(smtpHost);
            mTxtSmtpPort.setText(smtpPort);

            if (smtpAuthentication.equals("true")) {
                mChckbxSmtpAuth.setSelected(true);
                mTxtSmtpUsername.setText(smtpUsername);
                mTxtSmtpPassword.setText(smtpPassword);
            } else
                mChckbxSmtpAuth.setSelected(false);

            if (smtpEncryption.equals("SSL"))
                mComboBoxSmtpEncryption.setSelectedItem(mItem2Smtp);
            else if (smtpEncryption.equals("STARTTLS"))
                mComboBoxSmtpEncryption.setSelectedItem(mItem3Smtp);
            else if (smtpEncryption.equals("None"))
                mComboBoxSmtpEncryption.setSelectedItem(mItem1Smtp);

            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return props;
    }

    /**
     * Saves properties from the textfields and other components to file
     *
     * @return saved properties
     */
    public Properties saveProperties() {
        Properties props;

        //Gets values from the encryption comboboxes
        String pop3Encryption = (String) mComboBoxPop3Encryption.getSelectedItem();
        String smtpEncryption = (String) mComboBoxSmtpEncryption.getSelectedItem();

        //Gets the pop3 password and creates a String from the char array
        char[] charsPasswordPop3 = mTxtPop3Password.getPassword();
        String stringPasswordPop3 = new String(charsPasswordPop3);

        char[] charsPasswordSmtp;
        String stringPasswordSmtp = "";

        //If smtp server authentication has been set
        if (mChckbxSmtpAuth.isSelected()) {
            //Gets the smtp password and creates a String from the char array
            charsPasswordSmtp = mTxtSmtpPassword.getPassword();
            stringPasswordSmtp = new String(charsPasswordSmtp);
        }

        //Checks for empty textfields
        if (mTxtPop3Hostname.getText().equals("")) {
            JOptionPane.showMessageDialog(getContentPane(), "Pop3 host cannot be empty! Try again!");
            return null;
        } else if (mTxtPop3Port.getText().equals("")) {
            JOptionPane.showMessageDialog(getContentPane(), "Pop3 port cannot be empty! Try again!");
            return null;
        } else if (mTxtPop3Username.getText().equals("")) {
            JOptionPane.showMessageDialog(getContentPane(), "Pop3 User name cannot be empty! Try again!");
            return null;
        } else if (stringPasswordPop3.equals("")) {
            JOptionPane.showMessageDialog(getContentPane(), "Pop3 password cannot be empty! Try again!");
            return null;
        } else if (mTxtSmtpHost.getText().equals("")) {
            JOptionPane.showMessageDialog(getContentPane(), "Smtp host cannot be empty! Try again!");
            return null;
        } else if (mTxtSmtpPort.getText().equals("")) {
            JOptionPane.showMessageDialog(getContentPane(), "Smtp port cannot be empty! Try again!");
            return null;
        } else if (mTxtEmail.getText().equals("")) {
            JOptionPane.showMessageDialog(getContentPane(), "Email cannot be empty! Try again!");
            return null;
        }
        //If smtp server authentication has been selected
        else if (mChckbxSmtpAuth.isSelected()) {
            if (mTxtSmtpUsername.getText().equals("")) {
                JOptionPane.showMessageDialog(getContentPane(), "Smtp user name cannot be empty! Try again!");
                return null;
            } else if (stringPasswordSmtp.equals("")) {
                JOptionPane.showMessageDialog(getContentPane(), "Smtp password cannot be empty! Try again!");
                return null;
            }
        }

        props = new Properties();

        //Sets properties from the textfields
        props.setProperty("mail.pop3.host", mTxtPop3Hostname.getText());
        props.setProperty("mail.pop3.port", mTxtPop3Port.getText());
        props.setProperty("pop3Username", mTxtPop3Username.getText());
        props.setProperty("pop3Password", stringPasswordPop3);
        props.setProperty("pop3Encryption", pop3Encryption);
        props.setProperty("smtpHost", mTxtSmtpHost.getText());
        props.setProperty("smtpPort", mTxtSmtpPort.getText());
        props.setProperty("email", mTxtEmail.getText());

        //If the Smtp authentication checkbox is checked, the Smtp username and password is set
        if (mChckbxSmtpAuth.isSelected()) {
            props.setProperty("smtpAuthentication", "true");
            props.setProperty("smtpUsername", mTxtSmtpUsername.getText());
            props.setProperty("smtpPassword", stringPasswordSmtp);
        } else
            props.setProperty("smtpAuthentication", "false");

        props.setProperty("smtpEncryption", smtpEncryption);
        props.setProperty("propertiesSaved", "true");

        //Writes the properties to file
        try {
            File f = new File("properties");
            OutputStream out = new FileOutputStream(f);
            props.store(out, "Mail settings");
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        JOptionPane.showMessageDialog(getContentPane(), "Preferences saved!");

        return props;
    }

    /**
     * Handles closing of the dialog. Only closes when preferences have been saved
     */
    public void handleDialogClosing() {
        if (MailHandler.propertiesFileExists()) {
            //If preferences havent been saved, shows error dialog
            if (MailHandler.checkPreferencesStatus() == false)
                JOptionPane.showMessageDialog(getContentPane(), "Preferences need to be saved before closing");

                //If preferences have been saved
            else
                PreferencesDialog.this.dispose();
        } else
            JOptionPane.showMessageDialog(getContentPane(), "Preferences need to be saved before closing");
    }
}
