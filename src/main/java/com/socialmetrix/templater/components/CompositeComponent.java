package com.socialmetrix.templater.components;

import java.util.*;

import org.apache.poi.ss.usermodel.Sheet;

import com.socialmetrix.templater.TemplaterException;
import com.socialmetrix.templater.datareaders.DataReader;
import com.socialmetrix.templater.geometry.*;

/**
 * Joins multiple components into one, changing the relative render origin.
 */
public class CompositeComponent extends PropertyReaderComponent {

	protected final Coordinates relativeOrigin;
	protected List<Component> components = new ArrayList<Component>();

	public CompositeComponent(DataReader dataReader, String property, Coordinates relativeOrigin) {
		super(dataReader, property);
		this.relativeOrigin = relativeOrigin;
	}

	public void add(Component component) {
		components.add(component);
	}

	public List<Component> getChildren() {
		return this.components;
	}

	public Coordinates getRelativeOrigin() {
		return this.relativeOrigin;
	}

	@Override
	protected Region renderProperty(Object property, Coordinates parentOrigin, Sheet sheet) {
		Coordinates compositeOrigin = parentOrigin.plus(relativeOrigin);
		return renderChildren(property, compositeOrigin, sheet);
	}

	protected Region renderChildren(Object data, Coordinates compositeOrigin, Sheet sheet) {
		if (components.isEmpty()) {
			throw new EmptyCompositeComponentException();
		}

		Region result = null;
		for (Component component : components) {
			Region renderedRegion = component.render(data, compositeOrigin, sheet);
			result = result == null ? renderedRegion : result.combine(renderedRegion);
		}
		return result;
	}

	public static class EmptyCompositeComponentException extends TemplaterException {
		private static final long serialVersionUID = -8516257265529075916L;

		public EmptyCompositeComponentException() {
			super("Composite components can't be empty.");
		}
	}

}
