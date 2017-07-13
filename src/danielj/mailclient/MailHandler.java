package danielj.mailclient;

import com.sun.mail.util.MailConnectException;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.*;
import java.util.Properties;

/**
 * Class for handling mail
 *
 * @author Daniel Johansson
 */
public class MailHandler {

    /**
     * Mail count
     */
    private static int mailCount;

    /**
     * Mail store
     */
    private static Store store;

    /**
     * Mail folder
     */
    private static Folder emailFolder;

    /**
     * Email message
     */
    private static Message message;

    /**
     * Flag for a new message. "reply" for a reply message, and "forward" for a forwarded message
     */
    private static String messageFlag;

    /**
     * Flag that indicates that the user has entered incorrect mail server settings
     */
    private static boolean wrongServerSettingsFlag;

    /**
     * Local mail folder
     */
    private static Folder localEmailFolder;

    /**
     * Properties
     */
    private static Properties properties;

    /* Setters and getters */
    public static int getMailCount() {
        return mailCount;
    }

    public static void setMessage(Message msg) {
        message = msg;
    }

    public static Message getMessage() {
        return message;
    }

    public static void setMessageFlag(String flag) {
        messageFlag = flag;
    }

    public static String getMessageFlag() {
        return messageFlag;
    }

    public static boolean getWrongServerSettingsFlag() {
        return wrongServerSettingsFlag;
    }

    public static void setWrongServerSettingsFlag(Boolean flag) {
        wrongServerSettingsFlag = flag;
    }

    public static void setProperties(Properties props) {
        properties = props;
    }

    /**
     * Checks if properties file exists
     *
     * @return true if properties file exists, false if not properties file exists
     */
    public static boolean propertiesFileExists() {
        File f = new File("properties");
        if (f.exists()) {
            return true;
        } else
            return false;
    }

    /**
     * Checks if preferences has been saved
     *
     * @return true if saved, false if not saved
     */
    public static boolean checkPreferencesStatus() {
        String propertiesSaved = "";
        Properties props = new Properties();
        File configFile = new File("properties");

        try {
            //Loads properties from file
            FileReader reader = new FileReader(configFile);
            props.load(reader);

            //Checks for null value
            if (props.getProperty("propertiesSaved") != null) {
                propertiesSaved = props.getProperty("propertiesSaved");
            }

            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (propertiesSaved.equals("true"))
            return true;
        else
            return false;
    }

    /**
     * Gets mail from mailserver
     *
     * @return message array
     */
    public static Message[] getMail() {
        Message[] messages = null;

        try {
            String user = properties.getProperty("pop3Username");
            String password = properties.getProperty("pop3Password");

            //If SSL encryption has been set, sets pop3s protocol and SSL properties
            if (properties.getProperty("pop3Encryption").equals("SSL")) {
                properties.setProperty("mail.store.protocol", "pop3s");
                properties.setProperty("mail.pop3s.host", properties.getProperty("mail.pop3.host"));
                properties.setProperty("mail.pop3s.port", properties.getProperty("mail.pop3.port"));
                properties.setProperty("mail.pop3.ssl.enable", "true");
            }
            //If no encryption, sets standard pop3 protocol
            else if (properties.getProperty("pop3Encryption").equals("None")) {
                properties.setProperty("mail.store.protocol", "pop3");
            }

            //Creates a new session
            Session session = Session.getInstance(properties);

            //Creates the store object and connects to the mail server
            store = session.getStore();
            store.connect(user, password);

            //Creates the folder object and opens it in read mode
            emailFolder = store.getFolder("INBOX");
            emailFolder.open(Folder.READ_ONLY);

            //Gets the messages from the folder
            messages = emailFolder.getMessages();
            mailCount = messages.length;

            //If theres a problem with connecting to the mail server
        } catch (MailConnectException e) {
            e.printStackTrace();
            setWrongServerSettingsFlag(true);               //Sets flag to indicate wrong mail server settings
            return null;
        } catch (MessagingException e) {
            e.printStackTrace();
            return null;
        }

        setWrongServerSettingsFlag(false);                  //Sets flag to indicate correct mail server settings

        return messages;
    }

    /**
     * Sends a mail message
     *
     * @param to          to recipients
     * @param subject     subject of message
     * @param messageText text of message
     * @param cc          cc recipients
     * @param bcc         bcc recipients
     * @return 0 if success, -1 if failed
     */
    public static int sendMail(String to, String subject, String messageText, String cc, String bcc) {
        properties.setProperty("mail.smtp.host", properties.getProperty("smtpHost"));
        properties.setProperty("mail.smtp.port", properties.getProperty("smtpPort"));

        //If smtp with SSL
        if (properties.getProperty("smtpEncryption").equals("SSL")) {
            properties.setProperty("mail.smtp.ssl.enable", "true");
            properties.setProperty("mail.smtp.ssl.trust", "*");
        }
        //If smtp with STARTTLS
        else if (properties.getProperty("smtpEncryption").equals("STARTTLS")) {
            properties.setProperty("mail.smtp.starttls.enable", "true");
            //If smtp with no encryption
        } else {
            properties.setProperty("mail.smtp.ssl.enable", "false");
            properties.setProperty("mail.smtp.starttls.enable", "false");
        }

        String user = "";
        String password = "";

        //If smtp authentication is required, sets relevant properties
        if (properties.getProperty("smtpAuthentication").equals("true")) {
            properties.setProperty("mail.smtp.auth", "true");
            user = properties.getProperty("smtpUsername");
            password = properties.getProperty("smtpPassword");
        }
        //If no smtp authentication is required, sets relevant properties
        else {
            properties.setProperty("mail.smtp.auth", "false");
        }

        try {
            Session session = Session.getInstance(properties);

            //Creates a MimeMessage
            MimeMessage message = new MimeMessage(session);

            //Sets from field
            message.setFrom(new InternetAddress(properties.getProperty("email")));

            //Sets to field
            if (!to.equals("")) {
                //If "to" includes a comma, then it´s a list of addresses and uses the setRecipients method
                if (to.indexOf(',') > 0)
                    message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to, false));
                    //If not, setRecipient method is used
                else
                    message.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
            }

            //Sets cc field
            if (!cc.equals("")) {
                //If "cc" includes a comma, then it´s a list of addresses and uses the setRecipients method
                if (cc.indexOf(',') > 0)
                    message.setRecipients(Message.RecipientType.CC, InternetAddress.parse(cc, false));
                    //If not, setRecipient method is used
                else
                    message.setRecipient(Message.RecipientType.CC, new InternetAddress(cc));
            }

            //Sets bcc field
            if (!bcc.equals("")) {
                //If "bcc" includes a comma, then it´s a list of addresses and uses the setRecipients method
                if (bcc.indexOf(',') > 0)
                    message.setRecipients(Message.RecipientType.BCC, InternetAddress.parse(bcc, false));
                    //If not, setRecipient method is used
                else
                    message.setRecipient(Message.RecipientType.BCC, new InternetAddress(bcc));
            }

            //Sets subject field
            message.setSubject(subject);

            //Gets attached files
            File[] files = ComposeDialog.getAttachedFiles();

            //If attached files array is not null, creates MimeBodyParts and a
            //MimeMultipart to hold them
            if (files != null) {
                //Creates message (the text) part
                MimeBodyPart part = new MimeBodyPart();
                part.setText(messageText);

                MimeBodyPart[] attachmentParts = new MimeBodyPart[files.length];
                int i = 0;

                //For all files that the user has chosen to attach - creates
                //MimeBodyParts for them and attach. Uses attachmentParts array
                for (File file : files) {
                    attachmentParts[i] = new MimeBodyPart();
                    attachmentParts[i].attachFile(file);
                    i++;
                }

                //Creates multi-part
                MimeMultipart multiPart = new MimeMultipart();
                i = 0;

                //Adds MimeBodyParts to the MimeMultipart
                for (MimeBodyPart item : attachmentParts) {
                    multiPart.addBodyPart(item);
                    i++;
                }

                //Sets content
                message.setContent(multiPart);

            } else {
                message.setText(messageText);
            }

            //If smtp authentication has been set, sends the message with user and password
            if (properties.getProperty("smtpAuthentication").equals("true")) {
                Transport.send(message, user, password);
            }
            //If smtp authentication has not been set, sends the message with no user and password
            else {
                Transport.send(message);
            }

        } catch (MessagingException | IOException e) {
            e.printStackTrace();
            return -1;
        }

        return 0;
    }

    /**
     * Saves attachment to file
     *
     * @param part to save
     * @return true if success, false if failed
     */
    public static boolean saveAttachment(MimeBodyPart part) {
        try {
            part.saveFile(new File(part.getFileName()));
        } catch (MessagingException | IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * Deletes message from mail server
     *
     * @param subject       subject of message
     * @param sentDate      date message was sent
     * @param from          from address
     * @param messageNumber message number
     */
    public static void deleteMessage(String subject, String sentDate, String from, int messageNumber) {
        Message[] messages = null;

        try {
            String user = properties.getProperty("pop3Username");
            String password = properties.getProperty("pop3Password");

            //If SSL encryption has been set, sets pop3s protocol and SSL properties
            if (properties.getProperty("pop3Encryption").equals("SSL")) {
                properties.setProperty("mail.store.protocol", "pop3s");
                properties.setProperty("mail.pop3s.host", properties.getProperty("mail.pop3.host"));
                properties.setProperty("mail.pop3s.port", properties.getProperty("mail.pop3.port"));
                properties.setProperty("mail.pop3.ssl.enable", "true");
            }
            //If no encryption, sets standard pop3 protocol
            else if (properties.getProperty("pop3Encryption").equals("None")) {
                properties.setProperty("mail.store.protocol", "pop3");
            }

            //Creates a new session
            Session session = Session.getInstance(properties);

            //Creates the store object and connects to the mail server
            store = session.getStore();
            store.connect(user, password);

            //Creates the folder object and opens it
            emailFolder = store.getFolder("INBOX");
            emailFolder.open(Folder.READ_WRITE);                        //Opens folder in Read/write mode

            //Gets messages from mail server
            messages = emailFolder.getMessages();

            Message remoteMessage = messages[messageNumber - 1];
            String remoteMessageSubject = remoteMessage.getSubject();
            String remoteMessageSentDate = remoteMessage.getSentDate().toString();
            InternetAddress[] remoteMessageAdressFrom = (InternetAddress[]) remoteMessage.getFrom();
            String remoteMessageFrom = remoteMessageAdressFrom[0].toUnicodeString();
            int remoteMessageNumber = remoteMessage.getMessageNumber();

            //Compares subject, from, sent date and message number fields for the local mail with the
            //corresponding remote mail to see if they match
            if (subject.equals(remoteMessageSubject) && sentDate.equals(remoteMessageSentDate)
                    && from.equals(remoteMessageFrom) && messageNumber == remoteMessageNumber) {
                remoteMessage.setFlag(Flags.Flag.DELETED, true);
            }

            //Closes the store and folder objects
            emailFolder.close(true);
            store.close();

        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets data from the mail messages. To be used in the DefaultTableModel in MailClient
     *
     * @param messages mail messages
     * @return Object array
     */
    public static Object[][] getMailData(Message[] messages) {
        Object[][] obj = null;

        try {
            obj = new Object[messages.length][3];

            //For all messages
            for (int i = 0; i < messages.length; i++) {
                //Sets subject field
                obj[i][0] = messages[i].getSubject();

                //Sets from field
                InternetAddress[] adressFrom = (InternetAddress[]) messages[i].getFrom();
                if (adressFrom != null)
                    obj[i][1] = adressFrom[0].toUnicodeString();
                else
                    obj[i][1] = "Unavailable";

                String sentDate = null;

                //If getSentdate() is not null, sets sent date field
                if (messages[i].getSentDate() != null) {
                    sentDate = messages[i].getSentDate().toString();
                    obj[i][2] = sentDate.substring(0, 19);
                } else
                    obj[i][2] = "Unavailable";
            }

        } catch (MessagingException e) {
            e.printStackTrace();
        }

        return obj;
    }

    /**
     * Disconnects from mail server
     */
    public static void disconnect() {
        try {
            emailFolder.close(true);
            store.close();

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets all mail from the local mailbox
     *
     * @return messages array
     */
    public static Message[] getLocalMail() {
        Message[] messages = null;

        try {
            Properties properties = new Properties();
            setMstorProps(properties);

            Session session = Session.getInstance(properties);

            //Connects to the Mstor store
            Store store = session.getStore(new URLName("mstor:Mailbox/"));
            store.connect();

            //Gets messages from folder
            localEmailFolder = store.getDefaultFolder().getFolder("Inbox");
            localEmailFolder.open(Folder.READ_ONLY);
            messages = localEmailFolder.getMessages();

        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return messages;
    }

    /**
     * Creates local mailbox and appends messages to it
     *
     * @param messages messages to be appended
     */
    @SuppressWarnings("unchecked")
    public static void createMbox(Message[] messages) {
        try {
            Properties properties = new Properties();
            setMstorProps(properties);                              //Sets Mstor properties

            Session session = Session.getInstance(properties);

            //The mailbox is stored in the Mailbox.sbd directory
            Store store = session.getStore(new URLName("mstor:Mailbox/"));
            store.connect();

            localEmailFolder = store.getDefaultFolder().getFolder("Inbox");

            //The local mail folder is created if it doesnt exist
            if (!localEmailFolder.exists()) {
                localEmailFolder.create(Folder.HOLDS_MESSAGES);
            }

            localEmailFolder.appendMessages(messages);               //Appends messages to the local mail folder

        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Deletes the contents of the local mailbox
     */
    public static void deleteMailbox() {
        try {
            File mailboxDirectory = new File("Mailbox.sbd");

            //Creates the mailbox directory if it doesnt exist
            if (!mailboxDirectory.exists()) {
                mailboxDirectory.mkdir();
            }
            PrintWriter pw = new PrintWriter("Mailbox.sbd/Inbox");
            pw.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets properties for Mstor
     */
    public static void setMstorProps(Properties properties) {
        properties.setProperty("mail.store.protocol", "mstor");
        properties.setProperty("mstor.mbox.metadataStrategy", "none");
        properties.setProperty("mstor.mbox.cacheBuffers", "disabled");
        properties.setProperty("mstor.cache.disabled", "true");
        properties.setProperty("mstor.mbox.bufferStrategy", "default");
        properties.setProperty("mstor.metadata", "disabled");
    }
}
