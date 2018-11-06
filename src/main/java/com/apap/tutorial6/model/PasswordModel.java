package com.apap.tutorial6.model;

public class PasswordModel {
	private String newPassword;
	private String oldPassword;
	private String confrmPassword;
	
	public String getOldPassword() {
		return oldPassword;
	}
	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}
	public String getNewPassword() {
		return newPassword;
	}
	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}
	public String getConPassword() {
		return confrmPassword;
	}
	public void setConPassword(String conPassword) {
		this.confrmPassword = conPassword;
	}


}
