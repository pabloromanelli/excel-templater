package com.socialmetrix.templater.components;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;

import org.apache.poi.ss.usermodel.Sheet;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.socialmetrix.templater.components.ConstantComponent;
import com.socialmetrix.templater.geometry.*;
import com.socialmetrix.templater.utils.CellDuplicator;

@RunWith(MockitoJUnitRunner.class)
public class ConstantComponentTest {

	@Mock
	private CellDuplicator cellDuplicator;
	@Mock
	private Sheet sheet;

	@Test
	public void canRenderConstantValues() {
		ConstantComponent constantComponent = new ConstantComponent(cellDuplicator, new Coordinates(3, 2));

		Region region = constantComponent.render(null, new Coordinates(2, 1), sheet);
		assertEquals(new Region(new Coordinates(5, 3), 1, 1), region);
		verify(cellDuplicator).duplicateIn(sheet, new Coordinates(5, 3));
	}

}
