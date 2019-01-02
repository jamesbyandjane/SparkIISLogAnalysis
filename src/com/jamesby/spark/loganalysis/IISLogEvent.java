package com.jamesby.spark.loganalysis;

public class IISLogEvent implements java.io.Serializable{
	private static final long serialVersionUID = 1L;
	private String date; 
	private String time; 
	private String sIp;
	private String csMethod;
	private String csUriStem;
	private String csUriQuery; 
	private String sPort; 
	private String csUsername; 
	private String cIp;
	private String csUserAgent; 
	private String csReferer; 
	private String scStatus; 
	private String scSubstatus; 
	private String scWin32Status; 
	private String timeTaken;
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getsIp() {
		return sIp;
	}
	public void setsIp(String sIp) {
		this.sIp = sIp;
	}
	public String getCsMethod() {
		return csMethod;
	}
	public void setCsMethod(String csMethod) {
		this.csMethod = csMethod;
	}
	public String getCsUriStem() {
		return csUriStem;
	}
	public void setCsUriStem(String csUriStem) {
		this.csUriStem = csUriStem;
	}
	public String getCsUriQuery() {
		return csUriQuery;
	}
	public void setCsUriQuery(String csUriQuery) {
		this.csUriQuery = csUriQuery;
	}
	public String getsPort() {
		return sPort;
	}
	public void setsPort(String sPort) {
		this.sPort = sPort;
	}
	public String getCsUsername() {
		return csUsername;
	}
	public void setCsUsername(String csUsername) {
		this.csUsername = csUsername;
	}
	public String getcIp() {
		return cIp;
	}
	public void setcIp(String cIp) {
		this.cIp = cIp;
	}
	public String getCsUserAgent() {
		return csUserAgent;
	}
	public void setCsUserAgent(String csUserAgent) {
		this.csUserAgent = csUserAgent;
	}
	public String getCsReferer() {
		return csReferer;
	}
	public void setCsReferer(String csReferer) {
		this.csReferer = csReferer;
	}
	public String getScStatus() {
		return scStatus;
	}
	public void setScStatus(String scStatus) {
		this.scStatus = scStatus;
	}
	public String getScSubstatus() {
		return scSubstatus;
	}
	public void setScSubstatus(String scSubstatus) {
		this.scSubstatus = scSubstatus;
	}
	public String getScWin32Status() {
		return scWin32Status;
	}
	public void setScWin32Status(String scWin32Status) {
		this.scWin32Status = scWin32Status;
	}
	public String getTimeTaken() {
		return timeTaken;
	}
	public void setTimeTaken(String timeTaken) {
		this.timeTaken = timeTaken;
	}
}
