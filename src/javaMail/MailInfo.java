package javaMail;

import java.io.File;
import java.util.ArrayList;

public class MailInfo {
	private String addr;
	private String nikeName;
	private String subject;
	private String sendDate;
	private String bodyText;
	private int size;
	
	private ArrayList<File> list;
	
	public MailInfo() {
		list = new ArrayList<File>();
	}
	
	public ArrayList<File> getList() {
		return list;
	}

	public String getAddr() {
		return addr;
	}
	public void setAddr(String addr) {
		this.addr = addr;
	}
	public String getNikeName() {
		return nikeName;
	}
	public void setNikeName(String nikeName) {
		this.nikeName = nikeName;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getSendDate() {
		return sendDate;
	}
	public void setSendDate(String sendDate) {
		this.sendDate = sendDate;
	}
	public String getBodyText() {
		return bodyText;
	}
	public void setBodyText(String bodyText) {
		this.bodyText = bodyText;
	}
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
}
