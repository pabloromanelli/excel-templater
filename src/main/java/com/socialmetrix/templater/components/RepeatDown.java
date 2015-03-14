package com.socialmetrix.templater.components;

import com.socialmetrix.templater.datareaders.DataReader;
import com.socialmetrix.templater.geometry.*;

public class RepeatDown extends RepeatComponent {

	public RepeatDown(DataReader dataReader, String property, Coordinates relativeOrigin) {
		super(dataReader, property, relativeOrigin);
	}

	@Override
	protected Coordinates moveOrigin(Coordinates currentOrigin, Region renderedRegion) {
		// move current origin below last row
		return currentOrigin.withRow(renderedRegion.getLastRow() + 1);
	}

}
