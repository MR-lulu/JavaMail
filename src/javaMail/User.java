package javaMail;

public class User {
	private String emailAddr;
	private String uid;
	private String password;
	
	public User(){
	}
	public User(String emailAddr, String uid) {
		this.emailAddr = emailAddr;
		this.uid = uid;
	}
	public String getEmailAddr() {
		return emailAddr;
	}
	public void setEmailAddr(String emailAddr) {
		this.emailAddr = emailAddr;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}		
}
