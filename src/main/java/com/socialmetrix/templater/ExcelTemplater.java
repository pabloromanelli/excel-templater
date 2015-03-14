package com.socialmetrix.templater;

import java.io.*;

import com.socialmetrix.templater.builders.*;
import com.socialmetrix.templater.datareaders.ReflectiveDataReader;

public class ExcelTemplater {

	/**
	 * Builds a template from the input excel file with the provided data and
	 * saves it to the output, using a reflective data reader.
	 * 
	 * @param input
	 *            Excel file where named regions are defined.
	 * @param data
	 *            Input data to render the template components.
	 * @param output
	 *            Output stream where the new excel file will be created.
	 */
	public void renderTemplateTo(File input, Object data, OutputStream output) {
		new TemplateBuilder() //
			.namedRegionBuilder(new NamedRegionBuilder())
			.dataReader(new ReflectiveDataReader())
			.fromFile(input)
			.build()
			.render(data)
			.write(output);
	}
	
	/**
	 * Builds a template from the input excel file with the provided data and
	 * saves it to the output, using a reflective data reader.
	 * 
	 * @param input
	 *            Excel file as input stream where named regions are defined.
	 * @param data
	 *            Input data to render the template components.
	 * @param output
	 *            Output stream where the new excel file will be created.
	 */
	public void renderTemplateTo(InputStream input, Object data, OutputStream output) {
		new TemplateBuilder() //
		.namedRegionBuilder(new NamedRegionBuilder())
		.dataReader(new ReflectiveDataReader())
		.fromInputStream(input)
		.build()
		.render(data)
		.write(output);
	}

}
