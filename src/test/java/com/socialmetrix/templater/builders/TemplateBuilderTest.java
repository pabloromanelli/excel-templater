package com.socialmetrix.templater.builders;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.util.*;

import org.apache.poi.ss.usermodel.*;
import org.junit.*;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.runners.MockitoJUnitRunner;

import com.socialmetrix.templater.builders.ComponentsTreeBuilder.ComponentsTreeBuilderProvider;

@RunWith(MockitoJUnitRunner.class)
public class TemplateBuilderTest {

	@Mock
	private Workbook workbook;
	@Mock(answer = Answers.RETURNS_DEEP_STUBS)
	private NamedRegionBuilder namedRegionBuilder;
	@Mock
	private ComponentsTreeBuilderProvider componentsTreeBuilderProvider;
	private TemplateBuilder builder;

	@Before
	public void setUp() {
		builder = new TemplateBuilder() //
			.namedRegionBuilder(namedRegionBuilder)
			.componentsTreeBuilderProvider(componentsTreeBuilderProvider);
	}

	@Test
	public void canGetNamedRegions() {
		HashMap<Sheet, List<NamedRegion>> expected = new HashMap<Sheet, List<NamedRegion>>();

		when(workbook.getNumberOfNames()).thenReturn(5);
		Name name0 = workbookName("_prop1_", 0, true);
		@SuppressWarnings("unused")
		Name name1 = workbookName("_prop2_", 1, false);
		@SuppressWarnings("unused")
		Name name2 = workbookName("prop1_", 2, true);
		Name name3 = workbookName("_prop1_", 3, true);
		Name name4 = workbookName("_prop1_", 4, true);

		Sheet sheet0 = mock(Sheet.class);
		Sheet sheet1 = mock(Sheet.class);
		NamedRegion region0 = namedRegion(name0, sheet0);
		NamedRegion region3 = namedRegion(name3, sheet1);
		NamedRegion region4 = namedRegion(name4, sheet0);
		expected.put(sheet0, Arrays.asList(region0, region4));
		expected.put(sheet1, Arrays.asList(region3));

		Map<Sheet, List<NamedRegion>> actual = builder.getNamedRegions(workbook);

		assertEquals(expected, actual);
	}

	private NamedRegion namedRegion(Name name, Sheet sheet) {
		NamedRegion region = mock(NamedRegion.class);
		when(region.getSheet()).thenReturn(sheet);
		when(namedRegionBuilder.name(name).workbook(workbook).build()).thenReturn(region);
		return region;
	}

	private Name workbookName(String nameName, int index, boolean contiguous) {
		Name name = mock(Name.class);
		when(name.getNameName()).thenReturn(nameName);
		if (contiguous) {
			when(name.getRefersToFormula()).thenReturn("");
		} else {
			when(name.getRefersToFormula()).thenReturn(",");
		}
		when(workbook.getNameAt(index)).thenReturn(name);
		return name;
	}

}
