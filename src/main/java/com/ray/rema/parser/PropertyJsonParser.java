package com.ray.rema.parser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.ray.rema.model.GeoKey;
import com.ray.rema.model.Geolocation;
import com.ray.rema.model.Pattern;
import com.ray.rema.model.Property;
import com.ray.rema.model.PropertyInfo;
import com.ray.rema.model.Source;

public class PropertyJsonParser implements Parser<Property> {

	@Override
	public Collection<Property> parse(String content, Source source) {
                final Pattern pattern = source.getPattern();
		final int start = content.indexOf(pattern.getPrefix());
		content = content.substring(start + pattern.getPrefix().length());
		final int end = content.indexOf(pattern.getSuffix());
		final List<Property> ps = new ArrayList<Property>();
		try {
			final JSONArray array = new JSONArray(content);
			
			for (int i = 0; i < array.length(); i++) {
				final JSONObject o = (JSONObject) array.get(i);
				final Property property = new Property();
				final Set<PropertyInfo> attributeSets = new HashSet<PropertyInfo>();
				property.setAttributeSets(attributeSets);
				final Geolocation geo = new Geolocation();
				property.setGeo(geo);
				o.get(pattern.getIdKey());
				final GeoKey geoKey = pattern.getGeoKey();
				geo.setLatitude((Double)o.get(geoKey.getLatitude()));
				geo.setLongitude((Double) o.get(geoKey.getLongitude()));
				
				ps.add(property);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ps;
	}

}
