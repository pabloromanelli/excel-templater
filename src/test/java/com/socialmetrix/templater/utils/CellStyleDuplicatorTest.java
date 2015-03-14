package com.socialmetrix.templater.utils;

import static org.mockito.Mockito.*;

import org.apache.poi.ss.usermodel.*;
import org.junit.*;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.runners.MockitoJUnitRunner;

import com.socialmetrix.templater.geometry.Coordinates;
import com.socialmetrix.templater.utils.CellStyleDuplicator;

@RunWith(MockitoJUnitRunner.class)
public class CellStyleDuplicatorTest {

	@Mock(answer = Answers.RETURNS_DEEP_STUBS)
	private Cell cell;
	@Mock
	private Cell newCell;
	@Mock
	private Sheet sheet;
	@Mock
	private Row row;
	@Mock
	private CellStyle rowStyle;
	@Mock
	private CellStyle cellStyle;

	@Before
	public void setUp() {
		when(cell.getColumnIndex()).thenReturn(5);
		when(cell.getSheet().getColumnWidth(5)).thenReturn(123);
		when(cell.getCellStyle()).thenReturn(cellStyle);
		when(cell.getCellType()).thenReturn(444);
		when(cell.getRow().getHeight()).thenReturn((short) 456);
		when(cell.getRow().getRowStyle()).thenReturn(rowStyle);
		when(row.createCell(3, 444)).thenReturn(newCell);
	}

	@Test
	public void canDuplicateOnAnExistingRow() {
		when(sheet.getRow(1)).thenReturn(row);

		CellStyleDuplicator cellStyleDuplicator = new CellStyleDuplicator(cell);
		cellStyleDuplicator.duplicateIn(sheet, new Coordinates(1, 3));

		verify(row).setRowStyle(rowStyle);
		verify(row).setHeight((short) 456);
		verify(row).createCell(3, 444);
		verify(sheet).setColumnWidth(3, 123);
		verify(newCell).setCellStyle(cellStyle);
	}

	@Test
	public void canDuplicateOnAnUndefinedRow() {
		when(sheet.getRow(1)).thenReturn(null);
		when(sheet.createRow(1)).thenReturn(row);

		CellStyleDuplicator cellStyleDuplicator = new CellStyleDuplicator(cell);
		cellStyleDuplicator.duplicateIn(sheet, new Coordinates(1, 3));

		verify(row).setRowStyle(rowStyle);
		verify(row).setHeight((short) 456);
		verify(row).createCell(3, 444);
		verify(sheet).setColumnWidth(3, 123);
		verify(newCell).setCellStyle(cellStyle);
	}

}
