package com.scitmasterA4.webhookTest;

public class Info {

	private String title;
	private String description;

	public Info() {
		// TODO Auto-generated constructor stub
	}

	public Info(String title, String description) {
		super();
		this.title = title;
		this.description = description;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDecription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return "Info [title=" + title + ", description=" + description + "]";
	}

	
}
