package com.socialmetrix.templater.builders;

import java.io.*;
import java.util.*;

import org.apache.poi.ss.usermodel.*;

import com.socialmetrix.templater.TemplaterException;
import com.socialmetrix.templater.components.Component;
import com.socialmetrix.templater.geometry.Coordinates;

public class Template {

	private Workbook workbook;
	private Collection<SheetTemplate> sheetTemplates = new ArrayList<Template.SheetTemplate>();

	public Template(Workbook workbook) {
		this.workbook = workbook;
	}

	public void add(Sheet sheet, Component component) {
		sheetTemplates.add(new SheetTemplate(sheet, component));
	}

	public Template render(Object data) {
		for (SheetTemplate sheetTemplate : sheetTemplates) {
			sheetTemplate.render(data);
		}
		return this;
	}

	public void write(OutputStream stream) {
		try {
			workbook.write(stream);
		} catch (IOException e) {
			throw new TemplaterException(e);
		}
	}

	public static class SheetTemplate {
		private final Sheet sheet;
		private final Component component;

		public SheetTemplate(Sheet sheet, Component component) {
			this.component = component;
			this.sheet = sheet;
		}

		public void render(Object data) {
			component.render(data, new Coordinates(0, 0), sheet);
		}
	}

}
