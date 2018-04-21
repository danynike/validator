package com.gui.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.servlet.http.Part;

import com.gui.services.IValidationService;

@ManagedBean(name="validation")
public class ValidationController {

	private Part file;
	private String fileContent;
	
	@EJB private IValidationService service;

	public void upload() {
		try {
			InputStream initialStream = file.getInputStream();
		    byte[] buffer = new byte[initialStream.available()];
		    initialStream.read(buffer);
			 
		    File temp = File.createTempFile("temp-file-name", ".xpdl"); 
		    OutputStream outStream = new FileOutputStream(temp);
		    outStream.write(buffer);
		    outStream.close();
			fileContent = service.getResultFromValidation(file.getSubmittedFileName(), temp);
			temp.delete();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Part getFile() {
		return file;
	}

	public void setFile(Part file) {
		this.file = file;
	}

	public void validateFile(FacesContext ctx, UIComponent comp, Object value) {
		List<FacesMessage> msgs = new ArrayList<FacesMessage>();
		Part file = (Part) value;
		if (!"xpdl".equals(file.getSubmittedFileName().substring(file.getSubmittedFileName().lastIndexOf(".") + 1))) {
			msgs.add(new FacesMessage("not a xpdl file"));
		}
		if (!msgs.isEmpty()) {
			throw new ValidatorException(msgs);
		}
	}
	
	public String getFileContent() {
		return fileContent;
	}
	
	public void setFileContent(String fileContent) {
		this.fileContent = fileContent;
	}

}
