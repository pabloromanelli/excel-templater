package com.socialmetrix.templater.components;

import org.apache.poi.ss.usermodel.Sheet;

import com.socialmetrix.templater.TemplaterException;
import com.socialmetrix.templater.datareaders.DataReader;
import com.socialmetrix.templater.geometry.*;

public abstract class RepeatComponent extends CompositeComponent {

	public RepeatComponent(DataReader dataReader, String property, Coordinates relativeOrigin) {
		super(dataReader, property, relativeOrigin);
	}

	@Override
	protected Region renderProperty(Object property, Coordinates parentOrigin, Sheet sheet) {
		if (!(property instanceof Iterable<?>)) {
			throw new NonIterableProperty(property);
		}
		Iterable<?> iterable = (Iterable<?>) property;

		Coordinates currentOrigin = parentOrigin.plus(relativeOrigin);
		Region result = null;

		for (Object element : iterable) {
			// render on the current origin
			Region renderedRegion = renderChildren(element, currentOrigin, sheet);
			// accumulate rendered regions
			result = result == null ? renderedRegion : result.combine(renderedRegion);
			currentOrigin = moveOrigin(currentOrigin, renderedRegion);
		}

		if (result == null) {
			throw new EmptyIterableException("Can't use empty iterables for repetitions.");
		}

		return result;
	}

	protected abstract Coordinates moveOrigin(Coordinates currentOrigin, Region renderedRegion);

	public static class EmptyIterableException extends TemplaterException {
		private static final long serialVersionUID = -6501801818816018098L;

		public EmptyIterableException(String message) {
			super(message);
		}
	}

	public static class NonIterableProperty extends TemplaterException {
		private static final long serialVersionUID = 8633053355928171706L;

		public NonIterableProperty(Object property) {
			super("Property '" + property + "' is not iterable.");
		}
	}
}
