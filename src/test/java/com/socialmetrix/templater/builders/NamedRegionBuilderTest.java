package com.socialmetrix.templater.builders;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.AreaReference;
import org.junit.*;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.runners.MockitoJUnitRunner;

import com.socialmetrix.templater.builders.NamedRegion.Repeat;
import com.socialmetrix.templater.builders.NamedRegionBuilder.InvalidRegionName;
import com.socialmetrix.templater.geometry.Region;

@RunWith(MockitoJUnitRunner.class)
public class NamedRegionBuilderTest {

	@Mock
	private Workbook workbook;
	@Mock
	private Name name;
	@Mock(answer = Answers.RETURNS_DEEP_STUBS)
	private AreaReference areaReference;

	@Before
	public void setUp() {
		when(areaReference.getFirstCell().getRow()).thenReturn(1);
		when(areaReference.getFirstCell().getCol()).thenReturn((short) 2);
		when(areaReference.getLastCell().getRow()).thenReturn(3);
		when(areaReference.getLastCell().getCol()).thenReturn((short) 4);
	}

	private NamedRegion build() {
		return new NamedRegionBuilder() //
			.name(name, areaReference)
			.workbook(workbook)
			.build();
	}

	@Test
	public void regionIsOk() {
		when(name.getNameName()).thenReturn("_prop_");

		NamedRegion namedRegion = build();

		assertEquals(new Region(1, 2, 3, 3), namedRegion.region);
	}

	@Test
	public void canUseNamesOnlyWithAProperty() {
		when(name.getNameName()).thenReturn("_myProperty01_");

		NamedRegion namedRegion = build();

		assertEquals("myProperty01", namedRegion.getPropertyName());
		assertEquals(Repeat.NONE, namedRegion.getRepeat());
		assertEquals(new Integer(0), namedRegion.getOrder());
	}

	@Test
	public void canUseNamesWithRepetition() {
		when(name.getNameName()).thenReturn("_myProperty01_D");

		NamedRegion namedRegion = build();

		assertEquals("myProperty01", namedRegion.getPropertyName());
		assertEquals(Repeat.DOWN, namedRegion.getRepeat());
		assertEquals(new Integer(0), namedRegion.getOrder());
	}

	@Test
	public void canUseNamesWithOrder() {
		when(name.getNameName()).thenReturn("_myProperty01_5");

		NamedRegion namedRegion = build();

		assertEquals("myProperty01", namedRegion.getPropertyName());
		assertEquals(Repeat.NONE, namedRegion.getRepeat());
		assertEquals(new Integer(5), namedRegion.getOrder());
	}

	@Test
	public void canUseNamesWithRepetitionAndOrder() {
		when(name.getNameName()).thenReturn("_myProperty01_r123");

		NamedRegion namedRegion = build();

		assertEquals("myProperty01", namedRegion.getPropertyName());
		assertEquals(Repeat.RIGHT, namedRegion.getRepeat());
		assertEquals(new Integer(123), namedRegion.getOrder());
	}

	@Test(expected = InvalidRegionName.class)
	public void cantUseInvalidRepetition() {
		when(name.getNameName()).thenReturn("_myProperty01_B123");
		build();
	}

	@Test
	public void canUserDotsInsideTheName() {
		when(name.getNameName()).thenReturn("_some.name.with.dots_");
		NamedRegion namedRegion = build();
		assertEquals("some.name.with.dots", namedRegion.getPropertyName());
	}

}
