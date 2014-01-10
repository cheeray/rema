package com.ray.rema.model;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

@SuppressWarnings("serial")
@Entity
@XmlRootElement
@Table(uniqueConstraints = @UniqueConstraint(columnNames = { "url", "parser" }))
public class Source extends AbstractEntity {
	private String url;
	private Pattern pattern;
	private String parser;

	@NotNull
	@Column(length=500)
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@NotNull
	@Embedded
	public Pattern getPattern() {
		return pattern;
	}

	public void setPattern(Pattern pattern) {
		this.pattern = pattern;
	}

	@NotNull
	public String getParser() {
		return parser;
	}

	public void setParser(String parser) {
		this.parser = parser;
	}
}
