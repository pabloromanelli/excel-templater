package com.socialmetrix.templater.components;

import org.apache.poi.ss.usermodel.Sheet;

import com.socialmetrix.templater.datareaders.DataReader;
import com.socialmetrix.templater.geometry.*;

public abstract class PropertyReaderComponent implements Component {

	protected final DataReader dataReader;
	protected final String property;

	public PropertyReaderComponent(DataReader dataReader, String property) {
		this.dataReader = dataReader;
		this.property = property;
	}

	@Override
	public Region render(Object data, Coordinates parentOrigin, Sheet sheet) {
		Object extractedData = dataReader.read(data, property);
		return renderProperty(extractedData, parentOrigin, sheet);
	}

	protected abstract Region renderProperty(Object property, Coordinates parentOrigin, Sheet sheet);

}
