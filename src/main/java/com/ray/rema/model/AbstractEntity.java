package com.ray.rema.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;

import com.ray.rema.util.IdGenerator;


@MappedSuperclass
public abstract class AbstractEntity implements Serializable {
	/* Delimiter of business key. */
	public static final char DELIMITER = ':';
	
	private static final long serialVersionUID = 1L;
	private Long id;
	private String uuid = IdGenerator.uuid();
	private Long version = 0L;
	private Date dateModified;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Version
	@Column(nullable = false)
	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}

	@Column(length = 36)
	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	public Date getDateModified() {
		return dateModified;
	}
	
	@SuppressWarnings("unused")
	private void setDateModified(Date dateModified) {
		this.dateModified = dateModified;
	}
	
	/**
	 * Ensure UUID has been set in case of imported entities.
	 */
	@PrePersist
	@PreUpdate
	public void ensureUUID() {
		if (uuid == null) {
			uuid = IdGenerator.uuid();
		}
                if (dateModified == null) {
                    dateModified = new Date();
                }
	}

	/**
	 * Return a business key string.
	 * @return a string represents the business key.
	 */
	public String bizKey(){
		throw new UnsupportedOperationException("Not implemented.");
	}
	
	@Override
	public int hashCode() {
		if (uuid == null) {
			// For imported entities ...
			return super.hashCode();
		} else {
			return uuid.hashCode();
		}
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		// if (!(obj instanceof AbstractEntity)) {
		// return false;
		// }

		if (obj.getClass() != this.getClass()) {
			return false;
		}
		AbstractEntity other = (AbstractEntity) obj;

		if (id != null && other.id != null) {
			return other.id.equals(id);
		} else if (uuid == null) {
			if (other.uuid == null) {
				// For imported entities ...
				return super.equals(obj);
			} else {
				return false;
			}
		}
		return uuid.equals(other.uuid);
	}
}
