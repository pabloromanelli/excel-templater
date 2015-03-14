package com.socialmetrix.templater.builders;

import org.apache.poi.ss.usermodel.Sheet;

import com.socialmetrix.templater.TemplaterException;
import com.socialmetrix.templater.geometry.Region;

public class NamedRegion {

	protected final Sheet sheet;
	protected final Region region;
	protected final String propertyName;
	protected final Repeat repeat;
	protected final Integer order;

	public NamedRegion(Sheet sheet, Region region, String propertyName, Repeat repeat, Integer order) {
		this.sheet = sheet;
		this.region = region;
		this.propertyName = propertyName;
		this.repeat = repeat;
		this.order = order;
	}

	/**
	 * Returns true if:
	 * <ul>
	 * <li>this region intersects with the other region but non of them is included in the other OR</li>
	 * <li>if both have the same region and the same order.</li>
	 * </ul>
	 */
	public boolean invalidOverlapping(NamedRegion other) {
		return ( //
				this.region.intersects(other.region) && //
				(!this.region.contains(other.region) && !other.region.contains(this.region)) //
				) ||
				(this.region.equals(other.region) && this.order.equals(other.order));
	}

	/**
	 * A named region includes another when its region includes the other region or both regions are the same but this region has
	 * a lower order than the other region.
	 */
	public boolean includes(NamedRegion other) {
		return (this.region.equals(other.region) && (this.order < other.order)) || //
				(!this.region.equals(other.region) && (this.region.contains(other.region)));
	}

	@Override
	public String toString() {
		return this.propertyName + " | repeat " + this.repeat + " | order " + this.order + " | region " + this.region;
	}

	/*
	 * Accessors
	 */

	public Sheet getSheet() {
		return sheet;
	}

	public Region getRegion() {
		return region;
	}

	public String getPropertyName() {
		return propertyName;
	}

	public Repeat getRepeat() {
		return repeat;
	}

	public Integer getOrder() {
		return order;
	}

	public static enum Repeat {
		NONE("none"), DOWN("d"), RIGHT("r");

		private String key;

		private Repeat(String key) {
			this.key = key;
		}

		public static Repeat parse(String value) {
			if (value == null || value.isEmpty()) {
				return NONE;
			}
			String lowerCaseValue = value.toLowerCase();
			for (Repeat repetition : Repeat.values()) {
				if (repetition.key.equals(lowerCaseValue)) {
					return repetition;
				}
			}
			throw new TemplaterException("Invalid repetition '" + value + "'");
		}
	}

}
