package com.socialmetrix.templater.builders;

import static com.socialmetrix.templater.utils.CellUtils.getCell;

import org.apache.poi.ss.usermodel.*;

import com.socialmetrix.templater.TemplaterException;
import com.socialmetrix.templater.builders.NamedRegion.Repeat;
import com.socialmetrix.templater.components.*;
import com.socialmetrix.templater.datareaders.DataReader;
import com.socialmetrix.templater.geometry.*;
import com.socialmetrix.templater.utils.*;

/**
 * Creates components from named regions
 */
public class ComponentsFactory {

	private final DataReader dataReader;

	public ComponentsFactory(DataReader dataReader) {
		this.dataReader = dataReader;
	}

	/**
	 * Creates a new composite component located at (0, 0) that doesn't change
	 * the data parameter on render (it passes the same value to all it's
	 * components).
	 */
	public CompositeComponent createRootComponent() {
		return new CompositeComponent(DataReader.identityDataReader, null, new Coordinates(0, 0));
	}

	/**
	 * Creates a new variable component, relative positioned to its parent
	 * origin.
	 * 
	 * @param namedRegion
	 *            Sheet region from where the cell style will be copied.<br/>
	 *            The named region also informs the component property name to
	 *            extract the correct value from the data object.
	 * @param parentOrigin
	 *            Absolute position to calculate the value relative position.
	 */
	public VariableComponent createVariableComponent(NamedRegion namedRegion, Coordinates parentOrigin) {
		Sheet sheet = namedRegion.getSheet();
		Region region = namedRegion.getRegion();
		Cell cell = getCell(sheet, region.getOrigin());
		CellStyleDuplicator cellStyleDuplicator = new CellStyleDuplicator(cell);
		Coordinates relativePosition = relativePosition(region, parentOrigin);
		return new VariableComponent( //
			dataReader,
			namedRegion.getPropertyName(),
			cellStyleDuplicator,
			relativePosition);
	}

	/**
	 * Creates a new composite component, relative positioned to its parent
	 * origin.
	 * 
	 * @param namedRegion
	 *            If the region is defined to iterate (down or right), an
	 *            iterable component will be created. If not, a simple composite
	 *            component will be created. <br/>
	 *            The named region also informs the component property name to
	 *            extract the correct value from the data object.
	 * @param parentOrigin
	 *            Parent's absolute position to calculate the value relative
	 *            position.
	 */
	public CompositeComponent createCompositeComponent(NamedRegion namedRegion, Coordinates parentOrigin) {
		Coordinates relativePosition = relativePosition(namedRegion.getRegion(), parentOrigin);
		String property = namedRegion.getPropertyName();
		Repeat repeat = namedRegion.getRepeat();
		switch (repeat) {
			case NONE:
				return new CompositeComponent(dataReader, property, relativePosition);
			case DOWN:
				return new RepeatDown(dataReader, property, relativePosition);
			case RIGHT:
				return new RepeatRight(dataReader, property, relativePosition);
		}
		throw new TemplaterException("Invalid region repetition '" + repeat + "'.");
	}

	/**
	 * Creates a new constant component to copy cell style and content.
	 * 
	 * @param sheet
	 *            Sheet where the cell is located.
	 * @param region
	 *            Absolute position of the cell within the sheet.
	 * @param parentOrigin
	 *            Parent's absolute position to calculate component's relative
	 *            position.
	 */
	public ConstantComponent createConstantComponent(Sheet sheet, Region region, Coordinates parentOrigin) {
		Cell cell = getCell(sheet, region.getOrigin());
		CellDuplicator cellDuplicator = CellDuplicator.duplicatorFor(cell);
		Coordinates relativePosition = relativePosition(region, parentOrigin);
		return new ConstantComponent(cellDuplicator, relativePosition);
	}

	private Coordinates relativePosition(Region region, Coordinates parentOrigin) {
		return region.getOrigin().minus(parentOrigin);
	}

}
