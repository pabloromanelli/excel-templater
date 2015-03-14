package com.socialmetrix.templater.builders;

import java.io.*;
import java.util.*;
import java.util.Map.Entry;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.AreaReference;

import com.socialmetrix.templater.TemplaterException;
import com.socialmetrix.templater.builders.ComponentsTreeBuilder.ComponentsTreeBuilderProvider;
import com.socialmetrix.templater.builders.FreeOneByOneRegionIterable.FreeOneByOneRegionIterableProvider;
import com.socialmetrix.templater.components.Component;
import com.socialmetrix.templater.datareaders.DataReader;

public class TemplateBuilder {

	private NamedRegionBuilder namedRegionBuilder;
	private ComponentsTreeBuilderProvider componentsTreeBuilderProvider;
	private Workbook workbook;

	public TemplateBuilder namedRegionBuilder(NamedRegionBuilder namedRegionBuilder) {
		this.namedRegionBuilder = namedRegionBuilder;
		return this;
	}

	public TemplateBuilder dataReader(DataReader dataReader) {
		this.componentsTreeBuilderProvider = new ComponentsTreeBuilderProvider( //
				new ComponentsFactory(dataReader),
				new FreeOneByOneRegionIterableProvider());
		return this;
	}

	protected TemplateBuilder componentsTreeBuilderProvider(ComponentsTreeBuilderProvider componentsTreeBuilderProvider) {
		this.componentsTreeBuilderProvider = componentsTreeBuilderProvider;
		return this;
	}

	public TemplateBuilder fromFile(File file) {
		try {
			this.workbook = WorkbookFactory.create(file);
		} catch (InvalidFormatException e) {
			throw new TemplaterException(e);
		} catch (IOException e) {
			throw new TemplaterException(e);
		}
		return this;
	}

	public TemplateBuilder fromInputStream(InputStream stream) {
		try {
			this.workbook = WorkbookFactory.create(stream);
		} catch (InvalidFormatException e) {
			throw new TemplaterException(e);
		} catch (IOException e) {
			throw new TemplaterException(e);
		}
		return this;
	}

	public TemplateBuilder workbook(Workbook workbook) {
		this.workbook = workbook;
		return this;
	}

	public Template build() {
		Template template = new Template(workbook);

		for (Entry<Sheet, List<NamedRegion>> entry : getNamedRegions(workbook).entrySet()) {
			Sheet sheet = entry.getKey();
			List<NamedRegion> regions = entry.getValue();

			Component rootComponent = componentsTreeBuilderProvider.get().sheet(sheet).regions(regions).build();
			template.add(sheet, rootComponent);
		}

		return template;
	}

	protected Map<Sheet, List<NamedRegion>> getNamedRegions(Workbook workbook) {
		Map<Sheet, List<NamedRegion>> result = new HashMap<Sheet, List<NamedRegion>>();

		for (int i = 0; i < workbook.getNumberOfNames(); i++) {
			Name name = workbook.getNameAt(i);

			if (name.getNameName().startsWith("_") && //
				AreaReference.isContiguous(name.getRefersToFormula())) {

				NamedRegion namedRegion = namedRegionBuilder //
					.name(name)
					.workbook(workbook)
					.build();

				Sheet sheet = namedRegion.getSheet();
				if (!result.containsKey(sheet)) {
					result.put(sheet, new ArrayList<NamedRegion>());
				}

				result.get(sheet).add(namedRegion);
			}
		}

		return result;
	}

}
