package javaMail;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.*;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.Part;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

import com.sun.mail.imap.protocol.MailboxInfo;

public class SendEmail {
	private Session session = null;
	private Message message = null;
	private Transport transport = null;
	private Multipart multipart = new MimeMultipart("mixed");
	
	public SendEmail(){
	}
	
	public void connect(String host,String user,String password) {
		Properties properties = new Properties();
		properties.put("mail.smtp.host", host);
		properties.put("mail.smtp.auth", "true");
		properties.put("mail.transport.protocol", "smtp");
		
		//基本的邮件会话
		session = Session.getInstance(properties, new Authenticator() {
			public PasswordAuthentication getPasswordAuthentication(){
				//登录用户和密码
				return new PasswordAuthentication(user,password);
			}
		});
		//构造信息体
		message = new MimeMessage(session);
	}
	private String setNickName(String nick) throws UnsupportedEncodingException{
		nick=MimeUtility.encodeText(nick);
		return nick;
	}
	public void setAddr(String from,String to) throws MessagingException {
		//发送地址
		Address addressFrom = new InternetAddress(from);
		message.setFrom(addressFrom);
		//收件地址
		Address addressTo = new InternetAddress(to);
		message.setRecipient(MimeMessage.RecipientType.TO, addressTo);
	}
	public void setAddr(String from,String to,String nick) throws UnsupportedEncodingException, MessagingException {
		//发送地址
		Address addressFrom = new InternetAddress(from,setNickName(nick));
		message.setFrom(addressFrom);
		//收件地址
		Address addressTo = new InternetAddress(to);
		message.setRecipient(MimeMessage.RecipientType.TO, addressTo);
	}
	public void setSubject(String text) throws MessagingException {
		//主题
		message.setSubject(text);
	}
	public void addBodyText(String text) throws MessagingException{
		//正文
		BodyPart content = new MimeBodyPart();
		
		content.setContent(text,"text/html");
		multipart.addBodyPart(content);
	}
	public void addAttch(String filename) throws MessagingException, UnsupportedEncodingException {
         // Create a multipar message
         BodyPart messageBodyPart = new MimeBodyPart();
         
         DataSource source = new FileDataSource(filename);
         messageBodyPart.setDataHandler(new DataHandler(source));
         //去掉路径
         File file = new File(filename);
         filename = file.getAbsoluteFile().getName();
         
         messageBodyPart.setFileName(MimeUtility.encodeText(filename));
         messageBodyPart.setDescription(Part.ATTACHMENT);
         
         multipart.addBodyPart(messageBodyPart);  
	}
	public boolean sendMessage(){
		try {
			message.setContent(multipart);
			message.saveChanges();
			session.setDebug(true);
			
			transport = session.getTransport();
			transport.connect();
			transport.sendMessage(message, message.getAllRecipients());
			transport.close();
		} catch (NoSuchProviderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}
}
