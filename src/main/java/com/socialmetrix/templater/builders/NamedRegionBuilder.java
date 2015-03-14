package com.socialmetrix.templater.builders;

import java.util.regex.*;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.*;

import com.socialmetrix.templater.TemplaterException;
import com.socialmetrix.templater.builders.NamedRegion.Repeat;
import com.socialmetrix.templater.geometry.*;
import com.socialmetrix.templater.geometry.Region;

/**
 * Builds a named region from a region of a sheet.<br/>
 * It uses the following naming format:<br/>
 * <code>
 * _propertyName_[repetition][orderNumber]
 * </code>
 * <ul>
 * <li><b>propertyName</b> (Required): A name to be used when extracting values
 * from the data object.</li>
 * <li><b>repetition</b> (Optional, default = None): Accepted values are "d"
 * (Down) or "r" (Right).</li>
 * <li><b>orderNumber</b> (Optional, default = 0): Number to sort named regions
 * that has the same region on the same sheet. Lower order means that that
 * component will be first in the components tree (will be a parent of the
 * grater order number named region). It is also useful to disambiguate named
 * regions with the same propertyName because they need to be unique for the
 * workbook.</li>
 * </ul>
 */
public class NamedRegionBuilder {

	private static final String propertyGroup = "property";
	private static final String repeatGroup = "repeat";
	private static final String orderGroup = "order";
	private static final String nameRegex = "^_(?<property>[a-z\\d\\.]+)_(?:(?<repeat>d|r))?(:?(?<order>\\d+))?$";

	private static final Pattern nameParser = Pattern.compile(nameRegex, Pattern.CASE_INSENSITIVE);

	private Name name;
	private Workbook workbook;
	private AreaReference areaReference;

	public NamedRegionBuilder workbook(Workbook workbook) {
		this.workbook = workbook;
		return this;
	}

	public NamedRegionBuilder name(Name name) {
		this.name = name;
		this.areaReference = new AreaReference(name.getRefersToFormula());
		return this;
	}

	protected NamedRegionBuilder name(Name name, AreaReference areaReference) {
		// to test the builder without using "new AreaReference(..."
		this.name = name;
		this.areaReference = areaReference;
		return this;
	}

	public NamedRegion build() {
		if (workbook == null || name == null || areaReference == null) {
			throw new TemplaterException("Incomplete configuration for NamedRegion.");
		}

		Sheet sheet = workbook.getSheet(areaReference.getFirstCell().getSheetName());
		Region region = buildRegion(areaReference);

		Matcher matcher = nameParser.matcher(name.getNameName());
		if (!matcher.matches()) {
			throw new InvalidRegionName(name.getNameName(), name.getRefersToFormula());
		}

		String property = matcher.group(propertyGroup);
		Repeat repeat = Repeat.parse(matcher.group(repeatGroup));
		String orderString = matcher.group(orderGroup);
		Integer order = orderString == null ? 0 : Integer.parseInt(orderString);

		return new NamedRegion(sheet, region, property, repeat, order);
	}

	private static Region buildRegion(AreaReference areaReference) {
		CellReference firstCell = areaReference.getFirstCell();
		Coordinates topLeft = new Coordinates(firstCell.getRow(), firstCell.getCol());

		CellReference lastCell = areaReference.getLastCell();
		Coordinates bottomRight = new Coordinates(lastCell.getRow(), lastCell.getCol());

		return new Region(topLeft, bottomRight);
	}

	public static class InvalidRegionName extends TemplaterException {
		private static final long serialVersionUID = -4324339204011460141L;

		public InvalidRegionName(String name, String regionFormula) {
			super("Region '" + regionFormula + "' with name '" + name + "' is not valid. Pattern: '" + nameRegex + "'.");
		}
	}

}
