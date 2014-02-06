package com.ray.rema.parser;

import java.util.Collection;

import com.ray.rema.model.Source;

public interface Parser<T> {

	Collection<T> parse(String content, Source source);
}
