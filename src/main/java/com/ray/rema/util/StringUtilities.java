package com.ray.rema.util;

import java.util.List;

public abstract class StringUtilities {

	public static boolean isNullOrEmpty(String s) {
		return (s == null || s.trim().isEmpty());
	}

	/**
	 * Compare two list of strings lexicographically per item in the index order. 
	 * @param l1 The list of strings.
	 * @param l2 Another list of strings.
	 * @return the value <code>0</code> if each string item is equal to 
     *         other lists'; a value less than <code>0</code> if the first mismatched string 
     *         is lexicographically less than the other lists'; and a 
     *         value greater than <code>0</code> if the first mismatched string is 
     *         lexicographically greater than the string argument.
	 */
	public static int compare(List<String> l1, List<String> l2) {
		for (int i = 0; i < l1.size(); i++) {
			if (l2.size() < i) {
				return 1;
			}
			final String s1 = l1.get(i);
			final String s2 = l2.get(i);
			if (s1 == null) {
				if (s2 != null) {
					return -1;
				}
			} else if (s2 == null){
				return 1;
			} else {
				int c = s1.compareTo(s2);
				if (c != 0) {
					return c;
				}
			}
		}
		return 0;
	}

}
