package com.socialmetrix.templater.builders;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import org.apache.poi.ss.usermodel.*;
import org.junit.*;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.runners.MockitoJUnitRunner;

import com.socialmetrix.templater.builders.ComponentsFactory;
import com.socialmetrix.templater.components.ConstantComponent;
import com.socialmetrix.templater.datareaders.DataReader;
import com.socialmetrix.templater.geometry.*;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ComponentsFactoryTest {

	@Mock
	private DataReader dataReader;
	@Mock
	private Sheet sheet;
	@Mock
	private Row row;
	@Mock(answer = Answers.RETURNS_DEEP_STUBS)
	private Cell cell;
	private ComponentsFactory factory;

	@Before
	public void setUp() {
		factory = new ComponentsFactory(dataReader);
		when(cell.getCellType()).thenReturn(Cell.CELL_TYPE_BLANK);
	}

	@Test
	public void canCreateConstantComponent() {
		when(sheet.getRow(1)).thenReturn(row);
		when(row.getCell(2)).thenReturn(cell);

		ConstantComponent constantComponent = factory.createConstantComponent(sheet, new Region(1, 2, 3, 4), new Coordinates(0, 0));

		assertEquals(new Coordinates(1, 2), constantComponent.getRelativePosition());
	}

	@Test
	public void canCreateConstantComponentWhenCellIsUndefined() {
		when(sheet.getRow(1)).thenReturn(row);
		when(row.getCell(2)).thenReturn(null);
		when(row.createCell(2)).thenReturn(cell);

		ConstantComponent constantComponent = factory.createConstantComponent(sheet, new Region(1, 2, 3, 4), new Coordinates(0, 0));

		verify(row).createCell(2);
		assertEquals(new Coordinates(1, 2), constantComponent.getRelativePosition());
	}

	@Test
	public void canCreateConstantComponentWhenRowIsUndefined() {
		when(sheet.getRow(1)).thenReturn(null);
		when(sheet.createRow(1)).thenReturn(row);
		when(row.getCell(2)).thenReturn(null);
		when(row.createCell(2)).thenReturn(cell);

		ConstantComponent constantComponent = factory.createConstantComponent(sheet, new Region(1, 2, 3, 4), new Coordinates(0, 0));

		verify(sheet).createRow(1);
		verify(row).createCell(2);
		assertEquals(new Coordinates(1, 2), constantComponent.getRelativePosition());
	}

}
