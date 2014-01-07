package com.ray.rema.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Serializer {

	public static <T> T decode(Class<T> clazz, byte[] bytes)
			throws IOException, ClassNotFoundException {
		final ObjectInputStream ois = new ObjectInputStream(
				new ByteArrayInputStream(bytes));
		return clazz.cast(ois.readObject());
	}

	public static <T> byte[] encode(Class<?> clazz, Object object)
			throws IOException {
		final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		final ObjectOutputStream objectOutputStream = new ObjectOutputStream(
				byteArrayOutputStream);
		objectOutputStream.writeObject(object);
		objectOutputStream.close();
		return byteArrayOutputStream.toByteArray();
	}
}
