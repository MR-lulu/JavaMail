package javaMail;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Flags;
import javax.mail.Flags.Flag;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.Part;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;
import javax.print.DocFlavor.STRING;

import com.sun.mail.imap.protocol.UID;
import com.sun.mail.pop3.POP3Folder;

public class RecvMail {
	private Session session = null;
	private Store store = null;
	private Folder folder = null;
	private String username = null;  
	
	public RecvMail(){		
	}
	
	public boolean connect(String host,String username, String password) {
		this.username = username;
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
			if (folder != null)
				folder.close(true);
		} catch (MessagingException e) {
			e.printStackTrace();
		} finally {
			try {
				if (store != null)
					store.close();
			} catch (MessagingException e) {
				e.printStackTrace();
			}
		}
	}
	public Message[] getAllmMessages() {
		Message[] message = null;
		//获取返回文件夹对象
		try {
			folder = (POP3Folder) store.getFolder("INBOX");
			//设置权限
			folder.open(Folder.READ_WRITE);
			//获取邮件信息
			message = folder.getMessages();
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		return message;
	}
	public Message[] getNewmMessages() {
		Message[] messages =null;
		Message[] newMessages =null;
		ArrayList<Message> mlist = new ArrayList<Message>();
		try {
			messages = getAllmMessages();
			//打开保存uid的文件
			String filename = username;
			File file = new File(filename+".txt");
			//如果是新用户
			if (!file.exists() && (file.length()==0)) {
				FileWriter writer = new FileWriter(file);
				file.createNewFile();
				for (int i = 0; i < messages.length; i++) {
					MimeMessage mimeMessage = (MimeMessage) messages[i];
					String uid = ((POP3Folder)folder).getUID(mimeMessage);
					//保存uid,将uid写入文件
					writer.write(uid+"\r\n");
				}
				writer.flush();
				writer.close();
				return messages;
			}
			//老用户只收新邮件
			else {
				BufferedReader reader = new BufferedReader(new InputStreamReader(
						new FileInputStream(file)));
				FileWriter writer = new FileWriter(file, true);
				ArrayList<String> list = new ArrayList<String>();
				String userAddr = "";
				while((userAddr=reader.readLine())!=null){
					list.add(userAddr);
				}
				String uid = null ;
				boolean isNew = true;
				for (int i = 0;i<messages.length;i++) {
					isNew = true;
				
					uid = ((POP3Folder)folder).getUID(messages[i]);
					for(String cmp : list){
						if (cmp.equals(uid)) {
							isNew = false;
							break;
						}
					}
					if (isNew) {
						writer.write(uid+"\r\n");
						mlist.add(messages[i]);
					}
				}
				writer.flush();
				writer.close();
				reader.close();
			}
		} catch (NoSuchProviderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (mlist.isEmpty()) {
			return null;
		}
		newMessages = (Message[]) mlist.toArray(new Message[mlist.size()]);
		return newMessages;
	}
	public MailInfo getMailInfo(Message message) throws IOException, Exception{
		MailInfo info = new MailInfo();
		StringBuffer bodytext = new StringBuffer();
		
		InternetAddress[] addresses = (InternetAddress[]) message.getFrom();
		info.setAddr(addresses[0].getAddress());
		info.setNikeName(addresses[0].getPersonal());
		info.setSubject(message.getSubject());
		info.setSize(message.getSize());
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd　HH:mm:ss");
		info.setSendDate(format.format(message.getSentDate()));
		info.setBodyText(getMailContent(message,bodytext));
		saveAttch(message,info.getList());
		
		return info;
	}
	public String getMailContent(Part message,StringBuffer bodytext) throws MessagingException, IOException{
		if (message.isMimeType("text/*")) {
			bodytext.append((String)message.getContent());
		}else if (message.isMimeType("multipart/*")) {
			Multipart multipart=(Multipart)message.getContent();
			int counts=multipart.getCount();
			for(int i=0;i<counts;i++){
				getMailContent(multipart.getBodyPart(i),bodytext);
			}
		}else if(message.isMimeType("message/rfc822")){
			getMailContent((Part)message.getContent(),bodytext);
		} else {}
		return bodytext.toString();
	}
	public ArrayList<File> saveAttch(Part message,ArrayList<File> files) throws MessagingException, IOException {
		File file = null;
		if (message.isMimeType("multipart/*")) {
			Multipart multipart=(Multipart)message.getContent();
			int counts=multipart.getCount();
			for(int i=0;i<counts;i++){
				BodyPart part = multipart.getBodyPart(i);
				String description = part.getDescription();
				if(description!=null && (description.equals(Part.ATTACHMENT)||
						description.equals(Part.INLINE))){
					String filename =  MimeUtility.decodeText(part.getFileName());
		            file=saveFile(filename, part.getInputStream());
		            files.add(file);
				} else if (part.isMimeType("multipart/*")) {
					saveAttch(part,files);
				} 
			}
		}else if (message.isMimeType("message/rfc822")) {     
			saveAttch((Part)message.getContent(),files);     
        }
		return files;
	}
	private File saveFile(String filename,InputStream in) throws IOException {
		File file = new File(filename);
		FileOutputStream out = new FileOutputStream(file);
		int len = 0; 
		while((len=in.read())!=-1){
			out.write(len);
		}
		if (out != null) {
			out.close();
		} if (in != null) {
			in.close();
		}
		return file;
	}
	public void delete(Message message){
		try {
			
			message.setFlag(Flags.Flag.DELETED, true);
		
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}
}
