package javaMail;


import java.io.UnsupportedEncodingException;

import javax.mail.Message;
import javax.mail.MessagingException;

public class Main {
	public static void main(String[] args) {
		
		
		RecvMail mail = new RecvMail();
		Message message[] = null;
		try {
			mail.connect("pop.163.com","a1024909687@163.com","lq19960131");
			message=mail.getNewmMessages();
			if (message == null) {
				System.out.println("null");
			}else
			{
				System.out.println(message.length);
				for (int i = 0; i < message.length; i++) {
					MailInfo info = mail.getMailInfo(message[i]);
					System.out.println(info.getList().get(0).getAbsolutePath());
				}
			}
			//MailInfo info=mail.getMailInfo(message[29]);
			//System.out.println(info.getAddr());
			//System.out.println(info.getBodyText());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			mail.closeConnect();
		}
		
		
	/*	
		SendEmail sendEmail=new SendEmail();
		sendEmail.connect("smtp.163.com", "a1024909687@163.com", "lq19960131");
		try {
			//设置发送地址和收件地址
			sendEmail.setAddr("a1024909687@163.com", "a1024909687@163.com", "测试");
			//或者sendEmail.setAddr("from", "to");
			//设置邮件主题
			sendEmail.setSubject("text");
			//设置邮件内容(如果没有附件)
			sendEmail.addBodyText("<!DOCTYPE html><html><head><title></title></head><body><h1>hello</h1></body></html>");
			//设置附件
			sendEmail.addAttch("E:\\上课文件\\软件体系结构实验.zip");
			//发送
			sendEmail.sendMessage();
		} catch (UnsupportedEncodingException | MessagingException e) {
			e.printStackTrace();
			System.out.println("发送失败！");
		}
		*/
	}
	
}
