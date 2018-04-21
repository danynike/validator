package com.gui.services;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.ejb.Stateless;

@Stateless
public class ValidationServiceImpl implements IValidationService{

	@Override
	public String getResultFromValidation(String file, File input) {
		try {
			return getValidation(file, input);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return "";
	}
	
	private String getValidation(String file, File matchingFiles)
			throws IllegalAccessException, IllegalArgumentException,
			InvocationTargetException {
		String x = "";
		x += "File: " + file + "\n";
		for (Method method : XmlValidation.class.getDeclaredMethods()) {
			method.setAccessible(true);
			if (method.getName().startsWith("getStyle")
					|| method.getName().startsWith("getBpmn")) {
				x += method.invoke(null, matchingFiles) + "\n";
			}
		}
		x += "";
		return x;
	}

}
