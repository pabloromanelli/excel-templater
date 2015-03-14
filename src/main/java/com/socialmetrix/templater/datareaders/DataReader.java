package com.socialmetrix.templater.datareaders;

import com.socialmetrix.templater.TemplaterException;

public interface DataReader {

	/**
	 * Extracts a property by name from the "data" parameter.
	 * 
	 * @param data
	 *            Input data where the property will be extracted.
	 * @param property
	 *            Property to extract from data object.
	 * @throws PropertyNotFound
	 *             If the property can't be extracted from data.
	 */
	Object read(Object data, String property);

	/**
	 * Data reader that returns the same value after read (doesn't use property
	 * name).
	 */
	public final static DataReader identityDataReader = new IdentityDataReader();

	public static class IdentityDataReader implements DataReader {
		@Override
		public Object read(Object data, String property) {
			return data;
		}
	}

	public static class PropertyNotFound extends TemplaterException {
		private static final long serialVersionUID = -5172002643386109470L;

		public PropertyNotFound(Object data, String property) {
			super("Can't find property '" + property + "' in '" + data + "'");
		}
	}

}
