package com.ray.rema.model;

import java.io.File;

import javax.persistence.Embeddable;

@Embeddable
public class PropertyMeta extends AttributeMeta {

	private static final long serialVersionUID = 1L;

	/**
	 * Attribute meta info for division.
	 * 
	 * @author Chengwei.Yan
	 */
	public static enum PropertyField {
		PRICE		("Price", "Price", String.class),
		LAND		("Land", "Land Size", String.class),
		DESC		("Description", "Description", String.class),
		IMAGE		("Image", "Image", File.class);
		
		private final String label;
		private final String alias;
		private final Class<?> javaClass;

		private PropertyField(String label, String alias, Class<?> clazz) {
			this.label = label;
			this.alias = alias;
			this.javaClass = clazz;
		}

		public String getLabel() {
			return label;
		}

		public String getAlias() {
			return alias;
		}

		public Class<?> getJavaClass() {
			return javaClass;
		}
	}
}
