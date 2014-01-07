package com.ray.rema.util;

import java.util.UUID;

public final class IdGenerator {

	public static String uuid()
	{
		return UUID.randomUUID().toString();
	}
}
