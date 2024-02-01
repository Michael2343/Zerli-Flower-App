package Entities;

public class ClientInfo {
	
	 private String ip;
	 private String hostName;
	 private String status;
	 

	public ClientInfo(String ip, String hostName, String status) { 		
		this.ip= ip;
		this.hostName = hostName;
		this.status = status;
	}

	public String getIp() {
		return ip;
	}

	public String getHostName() {
		return hostName;
	}

	public String getStatus() {
		return status;
	}
}
