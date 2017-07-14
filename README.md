# MailClient
A Javamail email client in Java/Swing

## General
POP3/POP3S (POP3 with SSL encryption) and SMTP protocols are supported. SMTP has both SSL and STARTTLS support.

Attachments are fully supported.

Mstor (https://github.com/benfortuna/mstor) is used for local mail storage. Since the POP3 protocol has no support for "disconnected" operation (unlike the IMAP protocol), Mstor is also used to overcome this limitation in the POP3 protocol. 

#### Notes
* Because of the limited HTML support of the JEditorPane component of Java/Swing, HTML messages will not always render perfectly. 
* HTML viewing mode is enabled by default. The user has to explicity change to Text mode.





