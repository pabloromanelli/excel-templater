package com.socialmetrix.templater.components;

import org.apache.poi.ss.usermodel.Sheet;

import com.socialmetrix.templater.geometry.*;

public interface Component {

	/**
	 * Renders the component.
	 * @param data Actual data to be written.
	 * @param parentOrigin Parent component origin, where this component is positioned.
	 * @param sheet Sheet where data will be written.
	 * @return Absolute region where the data was rendered.
	 */
	Region render(Object data, Coordinates parentOrigin, Sheet sheet);

}
