package com.socialmetrix.templater.components;

import org.apache.poi.ss.usermodel.Sheet;

import com.socialmetrix.templater.geometry.*;
import com.socialmetrix.templater.utils.CellDuplicator;

public class ConstantComponent implements Component {

	private final CellDuplicator cellDuplicator;
	private final Coordinates relativePosition;

	public ConstantComponent(CellDuplicator cellDuplicator, Coordinates relativePosition) {
		this.cellDuplicator = cellDuplicator;
		this.relativePosition = relativePosition;
	}

	@Override
	public Region render(Object unused, Coordinates parentOrigin, Sheet sheet) {
		Coordinates position = parentOrigin.plus(relativePosition);
		cellDuplicator.duplicateIn(sheet, position);

		return new Region(position, 1, 1);
	}

	public Coordinates getRelativePosition() {
		return relativePosition;
	}

}
