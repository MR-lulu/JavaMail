package javaMail;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Flags;
import javax.mail.Flags.Flag;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Store;

import com.sun.mail.pop3.POP3Folder;

public class DeleteMail {
	private Session session = null;
	private Store store = null;
	private Folder folder = null; 
	
	public void delete(Message message){
		try {
			
			message.setFlag(Flags.Flag.DELETED, true);
		
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}
	public boolean connect(String host,String username, String password) {
		//连接信息
		Properties properties = new Properties();
		properties.setProperty("mail.store.protocol", "pop3");
		properties.setProperty("mail.host",host);
		//创建session对象
		session = Session.getInstance(properties, new Authenticator() {
			public PasswordAuthentication getPasswordAuthentication(){
			//登录用户和密码
				return new PasswordAuthentication(username,password);
			}
		});
		try {
			store = session.getStore();
			store.connect();
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
	public void closeConnect() {
		try {
			folder.close(true);
		} catch (MessagingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
			try {
				if (store != null)
					store.close();
			} catch (MessagingException e) {
				e.printStackTrace();
			}
	}
}
