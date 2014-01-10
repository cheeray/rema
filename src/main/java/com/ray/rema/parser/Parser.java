package com.ray.rema.parser;

import java.util.Collection;

import com.ray.rema.model.Pattern;

public interface Parser<T> {

	Collection<T> parse(String content, Pattern pattern);
}
