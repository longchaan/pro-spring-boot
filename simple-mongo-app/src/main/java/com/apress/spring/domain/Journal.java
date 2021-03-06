package com.apress.spring.domain;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;

public class Journal {

	@Id
	private String id;
	private String title;
	private Date created;
	private String summary;

	@Transient
	private SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");

	public Journal(String title, String summary, String date) throws ParseException {
		this.title = title;
		this.summary = summary;
		this.created = format.parse(date);
	}

	Journal() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getCreatedAsShort() {
		return format.format(created);
	}

	public String toString() {
		StringBuilder sb = new StringBuilder("* JournalEntry(");
		sb.append("Id: ").append(id);
		sb.append(", Title: ").append(title);
		sb.append(", Summary: ").append(summary);
		sb.append(", Created: ").append(getCreatedAsShort());
		sb.append(")");
		return sb.toString();
	}

}