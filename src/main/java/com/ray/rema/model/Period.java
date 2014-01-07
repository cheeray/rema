package com.ray.rema.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.ray.rema.util.DateUtilities;

/**
 * A range of dates.
 * 
 * @author Chengwei.Yan
 * 
 */
@Embeddable
public class Period implements Serializable, Comparable<Period> {
	private static final long serialVersionUID = 1L;
	
	private Date startDate;
	private Date endDate;

	public Period() {
		;
	}

	/**
	 * Constructor.
	 * 
	 * @param startDate The start date.
	 * @param endDate The end date.
	 */
	public Period(Date startDate, Date endDate) {
		this.startDate = startDate;
		this.endDate = endDate;
	}

	@Temporal(TemporalType.DATE)
	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	@Temporal(TemporalType.DATE)
	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	/**
	 * Null safely checking whether two periods are identical.
	 * @param a The first period.
	 * @param b The second period.
	 * @return true if both are null or equals.
	 */
	public static boolean nullSafeEquals(Period a, Period b) {
		if (a == null && b == null) {
			return true;
		}
		if (a == null) {
			a = new Period(null, null);
		}
		if (b == null) {
			b = new Period(null, null);
		}
		return a.equals(b);
	}
	
	/**
	 * Is a day within the range?
	 * 
	 * @param date The date to test.
	 * @return true if day part is within the range.
	 */
	@Transient
	public boolean isDayWithin(Date date) {
		if (date == null) {
			throw new IllegalArgumentException("Unable to test null date.");
		}
		
		if (startDate == null) {
			if (endDate != null) {
				return DateUtilities.isSameDatePart(endDate, date) || DateUtilities.isDatePartAfter(endDate, date);
			} else {
				return true;
			}
		} else if (endDate == null) {
			return DateUtilities.isSameDatePart(startDate, date) || DateUtilities.isDatePartBefore(startDate, date);
		} else {
			return DateUtilities.isDayBetween(date, startDate, endDate);
		}
	}

	@Transient
	public boolean hasOverlaps(Period p) {
		if (p == null) {
			p = new Period(null, null);
		}
		if (this.startDate == null) {
			// Current has no start point ...
			return p.getStartDate() == null;
		} else if (p.getStartDate() == null) {
			// Other has no start point ...
			return false;
		} else if (this.endDate == null) {
			// Current has no end ...
			if (p.getEndDate() == null) {
				return true;
			} else {
				return DateUtilities.isSameDatePart(p.getEndDate(), startDate) || DateUtilities.isDatePartAfter(p.getEndDate(), startDate);
			}
		} else {
			// Fixed range ...
			if (p.getEndDate() == null) {
				return DateUtilities.isSameDatePart(endDate, p.getStartDate()) || DateUtilities.isDatePartAfter(endDate, p.getStartDate());
			} else {
				return !(this.startDate.after(p.getEndDate()) || this.endDate
						.before(p.getStartDate()));
			}
		}
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((endDate == null) ? 0 : endDate.hashCode());
		result = prime * result + ((startDate == null) ? 0 : startDate.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Period other = (Period) obj;
		if (endDate == null) {
			if (other.endDate != null)
				return false;
		} else if (!endDate.equals(other.endDate))
			return false;
		if (startDate == null) {
			if (other.startDate != null)
				return false;
		} else if (!startDate.equals(other.startDate))
			return false;
		return true;
	}

	@Override
	public String toString() {
		final String start = startDate == null ? "N/A" : DateUtilities.toDateString(startDate);
		final String end = endDate == null ? "N/A" : DateUtilities.toDateString(endDate);
		return String.format("%s ~ %s", start, end);
	}

	@Override
	public int compareTo(Period p) {
		if (this.startDate == null) {
			if (p.startDate == null) {
				return 0;
			} else {
				// Not started one is always the latest.
				return 1;
			}
		}
		else {
			if (p.startDate == null) {
				// Not started one is always the latest.
				return -1;
			} else {
				int cmp = this.startDate.compareTo(p.startDate);
				if (cmp == 0) {
					if (this.endDate == null) {
						return p.endDate == null ? 0 : 1;
					} else {
						if (p.endDate == null) {
							return -1;
						} else {
							return this.endDate.compareTo(p.endDate);
						}
					}
				}
				return cmp;
			}
		}
	}
}
