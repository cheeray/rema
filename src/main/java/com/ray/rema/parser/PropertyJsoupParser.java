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
import com.ray.rema.model.Property;
import com.ray.rema.model.PropertyInfo;
import com.ray.rema.model.Source;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class PropertyJsoupParser implements Parser<Property> {

    @Override
    public Collection<Property> parse(String content, Source source) {
        Document doc = Jsoup.parse(content, source.getUrl());
        final List<Property> ps = new ArrayList<Property>();
        for (Element e : doc.select(source.getPattern().getPrefix())) {
            final Property property = new Property();
            final Set<PropertyInfo> attributeSets = new HashSet<PropertyInfo>();
            property.setAttributeSets(attributeSets);
            final Geolocation geo = new Geolocation();
            property.setGeo(geo);
            final GeoKey geoKey = source.getPattern().getGeoKey();
                //geo.setLatitude((double) e.gete.get(geoKey.getLatitude()));
            //geo.setLongitude((double) o.get(geoKey.getLongitude()));

            ps.add(property);
        }

        return ps;
    }

}
