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
			//���÷��͵�ַ���ռ���ַ
			sendEmail.setAddr("a1024909687@163.com", "a1024909687@163.com", "����");
			//����sendEmail.setAddr("from", "to");
			//�����ʼ�����
			sendEmail.setSubject("text");
			//�����ʼ�����(���û�и���)
			sendEmail.addBodyText("<!DOCTYPE html><html><head><title></title></head><body><h1>hello</h1></body></html>");
			//���ø���
			sendEmail.addAttch("E:\\�Ͽ��ļ�\\�����ϵ�ṹʵ��.zip");
			//����
			sendEmail.sendMessage();
		} catch (UnsupportedEncodingException | MessagingException e) {
			e.printStackTrace();
			System.out.println("����ʧ�ܣ�");
		}
		*/
	}
	
}
