package com.ray.rema.util;

import java.util.Calendar;
import java.util.Comparator;

public class DatePartComparator implements Comparator<Calendar> {

	@Override
	public int compare(Calendar firstCalendar, Calendar secondCalendar) {
		if (firstCalendar.get(Calendar.YEAR) != secondCalendar
				.get(Calendar.YEAR)) {
			return firstCalendar.get(Calendar.YEAR)
					- secondCalendar.get(Calendar.YEAR);
		}
		if (firstCalendar.get(Calendar.MONTH) != secondCalendar
				.get(Calendar.MONTH)) {
			return firstCalendar.get(Calendar.MONTH)
					- secondCalendar.get(Calendar.MONTH);
		}
		return firstCalendar.get(Calendar.DAY_OF_MONTH)
				- secondCalendar.get(Calendar.DAY_OF_MONTH);

	}
}