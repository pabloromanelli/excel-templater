package com.socialmetrix.templater.datareaders;

import java.beans.*;
import java.lang.reflect.*;

import com.socialmetrix.templater.TemplaterException;

public class ReflectiveDataReader implements DataReader {

	private static final String This = "this";

	@Override
	public Object read(Object data, String propertyName) {
		if (propertyName.equals(This)) {
			// to allow list of lists
			return data;
		}

		try {
			return getByBeanProperty(data, propertyName);
		} catch (PropertyNotFound e1) {
			try {
				return getByField(data, propertyName);
			} catch (PropertyNotFound e2) {
				return getByMethod(data, propertyName);
			}
		}
	}

	private Object getByMethod(Object data, String propertyName) {
		try {
			return data.getClass().getMethod(propertyName).invoke(data);
		} catch (IllegalAccessException e) {
			throw new TemplaterException(e);
		} catch (IllegalArgumentException e) {
			throw new TemplaterException(e);
		} catch (InvocationTargetException e) {
			throw new TemplaterException(e);
		} catch (NoSuchMethodException e) {
			throw new PropertyNotFound(data, propertyName);
		} catch (SecurityException e) {
			throw new TemplaterException(e);
		}
	}

	private Object getByField(Object data, String propertyName) {
		try {
			Field field = data.getClass().getField(propertyName);
			return field.get(data);
		} catch (NoSuchFieldException e) {
			throw new PropertyNotFound(data, propertyName);
		} catch (SecurityException e) {
			throw new TemplaterException(e);
		} catch (IllegalArgumentException e) {
			throw new TemplaterException(e);
		} catch (IllegalAccessException e) {
			throw new TemplaterException(e);
		}
	}

	private Object getByBeanProperty(Object data, String propertyName) {
		try {
			BeanInfo beanInfo = Introspector.getBeanInfo(data.getClass());
			PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
			for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
				if (propertyDescriptor.getName().equals(propertyName)) {
					return propertyDescriptor.getReadMethod().invoke(data);
				}
			}
			throw new PropertyNotFound(data, propertyName);
		} catch (IntrospectionException e) {
			throw new TemplaterException(e);
		} catch (IllegalAccessException e) {
			throw new TemplaterException(e);
		} catch (IllegalArgumentException e) {
			throw new TemplaterException(e);
		} catch (InvocationTargetException e) {
			throw new TemplaterException(e.getCause());
		}
	}

}
