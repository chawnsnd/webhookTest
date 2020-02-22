package com.scitmasterA4.webhookTest;

public class Info {

	private String title;
	private String decription;

	public Info() {
		// TODO Auto-generated constructor stub
	}

	public Info(String title, String decription) {
		super();
		this.title = title;
		this.decription = decription;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDecription() {
		return decription;
	}

	public void setDecription(String decription) {
		this.decription = decription;
	}

	@Override
	public String toString() {
		return "Info [title=" + title + ", decription=" + decription + "]";
	}

	
}
