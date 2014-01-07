package com.ray.rema.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

/**
 * Class with some utilities to facilitate Date and Calendar manipulation
 * 
 * @author odair.brentam
 */
public abstract class DateUtilities {
	/* Simple date formats */
	public static final String SDF_dd_MM_yyyy = "dd/MM/yyyy";

	public static SimpleDateFormat getDDMMYYYFormater() {
		// simpledateformat are not thread safe therefore we must create a new
		// instance every time
		return new SimpleDateFormat(SDF_dd_MM_yyyy);

	}

	public static String formatDateToDDMMYYY(Date date) {
		return getDDMMYYYFormater().format(date);
	}

	public static String formatDateToDDMMYYY(Calendar calendar) {
		return getDDMMYYYFormater().format(calendar.getTime());
	}

	/**
	 * Compare two Dates
	 * 
	 * @param first
	 *            first Date
	 * @param second
	 *            second Date
	 * @return
	 */
	private static int compare(Date first, Date second) {
		Calendar fc = Calendar.getInstance();
		Calendar sc = Calendar.getInstance();
		fc.setTime(first);
		sc.setTime(second);
		return new DatePartComparator().compare(fc, sc);
	}

	/**
	 * provides a date part (day/month/year) only comparator
	 * 
	 * @return the Comparator for the Calendar class
	 */
	public static Comparator<Calendar> getDatePartComparator() {
		return new DatePartComparator();
	}

	/**
	 * Compare only the date part (day/month/year) of two Date Objects
	 * 
	 * @param first
	 * @param second
	 * @return true if both dates are representing the same day/month/year
	 */
	public static boolean isSameDatePart(Date first, Date second) {
		return 0 == compare(first, second);
	}

	/**
	 * Compares the date part of the earlier and later dates
	 * 
	 * @param laterDate
	 *            the supposed later date
	 * @param earlierDate
	 *            the supposed earlier date
	 * @return true if the laterDate is greater than the earlierDate date
	 */
	public static boolean isDatePartAfter(Date laterDate, Date earlierDate) {
		return 0 < compare(laterDate, earlierDate);
	}

	/**
	 * Compares the date part of the earlier and later date
	 * 
	 * @param earlierDate
	 *            the supposed earlier date
	 * @param laterDate
	 *            the supposed later date
	 * @return true if the earlier date is before than the later date date
	 */
	public static boolean isDatePartBefore(Date earlierDate, Date laterDate) {
		return 0 > compare(earlierDate, laterDate);
	}

	/**
	 * Obtains the next day of the given day.
	 * 
	 * @param day
	 *            The given day.
	 * @return the date of the next day.
	 */
	public static Date nextDay(Date day) {
		final Calendar calendar = Calendar.getInstance();
		calendar.setTime(day);
		calendar.add(Calendar.DAY_OF_YEAR, 1);
		return calendar.getTime();
	}

	/**
	 * method to parse date as string to date as java.util.Date
	 * 
	 * @param date
	 * @param dateFormat
	 * @return parsed date
	 */
	public static Date getStringToDate(String date, String dateFormat) {
		SimpleDateFormat format = new SimpleDateFormat(dateFormat);
		Date parsedDate = null;
		try {
			parsedDate = format.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return parsedDate;
	}

	/**
	 * Wind back or forward a date by years.
	 * 
	 * @param date
	 *            The given date.
	 * @param years
	 *            The number of years.
	 * @return The back or forward winded date.
	 */
	public static Date windYear(Date date, int years) {
		final Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.YEAR, years);
		return calendar.getTime();
	}

	/**
	 * Add's the time component to the passed in date object
	 * 
	 * @param date
	 * @param time
	 * @return a Date object with time
	 */
	public static Date getTimeAddedDate(Date date, Date time) {
		GregorianCalendar timeCalender = new GregorianCalendar();
		timeCalender.setTime(time);
		GregorianCalendar dateCalender = new GregorianCalendar();
		dateCalender.setTime(date);
		dateCalender.set(GregorianCalendar.HOUR_OF_DAY,
				timeCalender.get(GregorianCalendar.HOUR_OF_DAY));
		dateCalender.set(GregorianCalendar.MINUTE,
				timeCalender.get(GregorianCalendar.MINUTE));
		dateCalender.set(GregorianCalendar.SECOND,
				timeCalender.get(GregorianCalendar.SECOND));
		return dateCalender.getTime();
	}

	/**
	 * Is the day of the given date between the start and end period dates?
	 * 
	 * @param current
	 *            The current date calendar.
	 * @param startDate
	 *            The start date.
	 * @param endDate
	 *            The end date.
	 * @return true if the day of the current date is between the start and end
	 *         period days, false otherwise.
	 */
	public static boolean isDayBetween(Date current, Date startDate,
			Date endDate) {
		final Calendar cc = Calendar.getInstance();
		cc.setTime(current);
		final Calendar ca = Calendar.getInstance();
		ca.setTime(startDate);
		final Calendar cb = Calendar.getInstance();
		cb.setTime(endDate);
		final int year = cc.get(Calendar.YEAR);
		if (year >= ca.get(Calendar.YEAR) && year <= cb.get(Calendar.YEAR)) {
			final int day = cc.get(Calendar.DAY_OF_YEAR);
			final int start = ca.get(Calendar.DAY_OF_YEAR);
			final int end = cb.get(Calendar.DAY_OF_YEAR);
			return day >= start && day <= end;
		}
		return false;
	}

	/**
	 * Convert Util date to Custom Format as String
	 * 
	 * @param dtDate
	 * @return a string
	 */
	public static String dateToStringFormat(Date dtDate, String dateFormat) {
		String strDate = "";
		if (dtDate != null) {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
			strDate = simpleDateFormat.format(dtDate);
		}
		return strDate;
	}

	/**
	 * Returns the passed in date as a XMLGregorianCalendar object
	 * 
	 * @param date
	 * @return A <code>XMLGregorianCalendar<code> object
	 */
	public static XMLGregorianCalendar getXMLGregorianDate(Date date) {
		XMLGregorianCalendar now = null;
		DatatypeFactory datatypeFactory;
		try {
			GregorianCalendar gregorianCalendar = new GregorianCalendar();
			gregorianCalendar.setTime(date);
			datatypeFactory = DatatypeFactory.newInstance();
			now = datatypeFactory.newXMLGregorianCalendar(gregorianCalendar);
			now.setTimezone(DatatypeConstants.FIELD_UNDEFINED);
		} catch (DatatypeConfigurationException e) {
			e.printStackTrace();
		}
		return now;
	}

	/**
	 * Format a duration as xxhours:xxmins:xxseconds.
	 * 
	 * @param duration
	 *            The duration in milliseconds.
	 * @return a string depicts the duration.
	 */
	public static String toDuration(long duration) {
		return toDuration(duration, TimeUnit.MILLISECONDS);
	}

	/**
	 * Format a duration as xxhours:xxmins:xxseconds.
	 * 
	 * @param unit
	 *            The time unit.
	 * @param duration
	 *            The duration in milliseconds.
	 * @return a string depicts the duration.
	 */
	public static String toDuration(long duration, TimeUnit unit) {
		return String.format("%shrs:%smins:%ssecs", unit.toHours(duration),
				unit.toMinutes(duration), unit.toSeconds(duration));
	}

	/**
	 * Method to get the end time of a given day
	 * 
	 * @param date
	 * @return
	 */
	public static Date getEndTimeOfDate(Date date) {
		GregorianCalendar calander = new GregorianCalendar();
		calander.setTime(date);
		calander.set(GregorianCalendar.HOUR_OF_DAY, 23);
		calander.set(GregorianCalendar.MINUTE, 59);
		calander.set(GregorianCalendar.SECOND, 59);
		return calander.getTime();
	}

	/**
	 * List all dates between the given dates.
	 * 
	 * @param startDate
	 *            The start date.
	 * @param endDate
	 *            The end date.
	 * @return a list of matched dates.
	 */
	public static List<Date> listDates(Date startDate, Date endDate) {
		final List<Date> dates = new ArrayList<>();
		final Calendar cal = Calendar.getInstance();
		cal.setTime(startDate);
		Date date = startDate;
		while (compareDays(date, endDate) <= 0) {
			dates.add(date);
			cal.add(Calendar.DATE, 1);
			date = cal.getTime();
		}
		return dates;
	}

	/**
	 * Compares two dates by its day.
	 * 
	 * @param a
	 *            This date.
	 * @param b
	 *            That date.
	 * @return 0 if two dates are same day; a negative value if this date is
	 *         before that date; otherwise a positive value indicates this date
	 *         is after that date.
	 */
	public static int compareDays(Date a, Date b) {
		final Calendar ca = Calendar.getInstance();
		ca.setTime(a);
		final Calendar cb = Calendar.getInstance();
		cb.setTime(b);
		return (ca.get(Calendar.YEAR) - cb.get(Calendar.YEAR)) * 365
				+ ca.get(Calendar.DAY_OF_YEAR) - cb.get(Calendar.DAY_OF_YEAR);
	}

	/**
	 * Methood to get the start time of a given day
	 * 
	 * @param date
	 * @return
	 */
	public static Date getStartTimeOfDate(Date date) {
		GregorianCalendar calander = new GregorianCalendar();
		calander.setTime(date);
		calander.set(GregorianCalendar.HOUR_OF_DAY, 0);
		calander.set(GregorianCalendar.MINUTE, 0);
		calander.set(GregorianCalendar.SECOND, 0);
		calander.set(GregorianCalendar.MILLISECOND, 0);
		return calander.getTime();
	}

	/**
	 * Converts a <code>Date<code> object to a <code>Calendar<code>
	 * 
	 * @param date
	 * @return a Calendar object
	 */
	public static GregorianCalendar getCalendar(Date date) {
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		return calendar;
	}
	
	
	/**
	 * Returns the date string.
	 * @param date The date.
	 * @return a string represents the date as "dd/mm/yyyy".
	 */
	public static String toDateString(Date date) {
		return String.format("%1$td/%1$tm/%1$ty", date);
	}
}
