package com.sunil.upload;

public class Item {
	private String pid;
	private String regplate;
	private String description;
	private String created_at;
	private String updated_at;
	private String link;
	public String getPid() {
		return pid;
	}
	public void setPid(String pid) {
		this.pid = pid;
	}
	public String getRegPlate() {
		return regplate;
	}
	public void setRegPlate(String name) {
		this.regplate = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getCreated_at() {
		return created_at;
	}
	public void setCreated_at(String created_at) {
		this.created_at = created_at;
	}
	public String getUpdated_at() {
		return updated_at;
	}
	public void setUpdated_at(String updated_at) {
		this.updated_at = updated_at;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}


}
