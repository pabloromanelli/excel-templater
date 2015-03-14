package com.socialmetrix.templater.components;

import com.socialmetrix.templater.datareaders.DataReader;
import com.socialmetrix.templater.geometry.*;

public class RepeatRight extends RepeatComponent {

	public RepeatRight(DataReader dataReader, String property, Coordinates relativeOrigin) {
		super(dataReader, property, relativeOrigin);
	}

	@Override
	protected Coordinates moveOrigin(Coordinates currentOrigin, Region renderedRegion) {
		// move current origin after last column
		return currentOrigin.withColumn(renderedRegion.getLastColumn() + 1);
	}

}
