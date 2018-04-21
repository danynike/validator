package com.gui.services;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.gui.model.Validation;

public class XmlValidation {
	
	private static final String MESSAGEFLOW = "MessageFlow";
	private static final String ACTIVITY = "Activity";
	private static final String TRANSITION = "Transition";
	public static Document doc;
	 
	 private static Document getDoc(File fileSelected) throws Exception{
		 try{
			 DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
			 DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
			 return docBuilder.parse (fileSelected);
		 }catch(Exception ex){
			 ex.printStackTrace();
			 throw new Exception(ex);
		 }
	 }

	 public static String getStyle0122AndStyle0123(File fileSelected)
     {
    	 try{
    		 List<Validation> v = new ArrayList<Validation>();
    		 
    		 doc = getDoc(fileSelected);
    		 NodeList listNodes = doc.getElementsByTagName(MESSAGEFLOW);
    		 
    		 if(listNodes != null && listNodes.getLength() > 0){
    			 for (int i = 0; i < listNodes.getLength(); i++){
    				 if(((Element)listNodes.item(i)).getElementsByTagName("ExtendedAttributes") != null){
    					 Validation vl = new Validation();
    					 vl.setMsg("Style 0122. A catching Message event should have incoming message flow And Style 0123.  A throwing Message event should have outgoing message flow.");
    					 vl.setName(listNodes.item(i).getAttributes().getNamedItem("Name").getNodeValue());
    					 vl.setId(listNodes.item(i).getAttributes().getNamedItem("Id").getNodeValue());
    					 
    					 v.add(vl);
    				 }
    			 }

    			 String msg = "";
    			 if (v.size() > 0){
    				 for (Validation validation : v) {
						msg += "ID -> " + validation.getId() + "\r\n";
						msg += "NAME -> " + validation.getName() + "\r\n";
						msg += "MSG -> " + validation.getMsg() + "\r\n";
					}
    				 
					return msg;
    			 }else
					return "";
    		 }
    	 }catch(Exception ex){
    		 ex.printStackTrace();
    	 }
    	 
    	 return "";
     }
	 
     public static String getStyle0104(File fileSelected)
     {
    	 try{
    		 Map<String, String> map = new HashMap<String, String>();
    		 List<String> nameNodes = new ArrayList<String>();
    		 List<Validation> v = new ArrayList<Validation>();
    		 
    		 doc = getDoc(fileSelected);
    		 NodeList listNodes = doc.getElementsByTagName(ACTIVITY);
    		 
    		 for (int i = 0; i < listNodes.getLength(); i++){
    			 nameNodes.add(listNodes.item(i).getAttributes().getNamedItem("Name").getNodeValue());
    			 if(map.containsKey(listNodes.item(i).getAttributes().getNamedItem("Name").getNodeValue()))
    				 map.put(listNodes.item(i).getAttributes().getNamedItem("Name").getNodeValue(), map.get(listNodes.item(i).getAttributes().getNamedItem("Name").getNodeValue()) + "-" + listNodes.item(i).getAttributes().getNamedItem("Id").getNodeValue());
    			 else
    				 map.put(listNodes.item(i).getAttributes().getNamedItem("Name").getNodeValue(), listNodes.item(i).getAttributes().getNamedItem("Id").getNodeValue());
    		 }
    		 
    		 nameNodes.stream().filter(i -> Collections.frequency(nameNodes, i) > 1)
             	.collect(Collectors.toSet()).forEach(x -> {
             		Validation vl = new Validation();
					 vl.setMsg("ERROR Style 0104. Two activities in the same process should not have the same name. (Use global activity to reuse a single activity in a process.).");
					 vl.setName(x);
					 vl.setId(map.get(x));
					 
					 v.add(vl);
			 });
    		 
    		 String msg = "";
			 if (v.size() > 0){
				 for (Validation validation : v) {
					msg += "ID -> " + validation.getId() + "\r\n";
					msg += "NAME -> " + validation.getName() + "\r\n";
					msg += "MSG -> " + validation.getMsg() + "\r\n";
				}
				 
				return msg;
			 }else
				return "";
    	 }catch(Exception ex){
    		 ex.printStackTrace();
    	 }
    	 
    	 return "";
     }

     public static String getStyle0115(File fileSelected)
     {
    	 try{
    		 List<Validation> v = new ArrayList<Validation>();
    		 
    		 doc = getDoc(fileSelected);
    		 NodeList listNodes = doc.getElementsByTagName(ACTIVITY);
    		 
    		 for (int i = 0; i < listNodes.getLength(); i++)
    		 {
    			 if (listNodes.item(i).getAttributes().getNamedItem("Name").getNodeValue() == null  || listNodes.item(i).getAttributes().getNamedItem("Name").getNodeValue().equals(""))
    			 {
    				 Validation vl = new Validation();
					 vl.setMsg("ERROR Style 0115.  A throwing intermediate event should be labeled.");
					 vl.setName(listNodes.item(i).getAttributes().getNamedItem("Name").getNodeValue());
					 vl.setId(listNodes.item(i).getAttributes().getNamedItem("Id").getNodeValue());
					 
					 v.add(vl);
    			 }
    		 }
    		 
    		 String msg = "";
			 if (v.size() > 0){
				 for (Validation validation : v) {
					msg += "ID -> " + validation.getId() + "\r\n";
					msg += "NAME -> " + validation.getName() + "\r\n";
					msg += "MSG -> " + validation.getMsg() + "\r\n";
				}
				 
				return msg;
			 }else
				return "";
    	 }catch(Exception ex){
    		 ex.printStackTrace();
    	 }
    	 
    	 return "";
     }

     public static String getBpmn0102(File fileSelected)
     {
    	 try{
    		 
    		 List<Validation> v = new ArrayList<Validation>();
    		 List<String> nodesAct = new ArrayList<String>();
    		 List<String> nodesNameAct = new ArrayList<String>();
    		 List<String> nodesTranFrom = new ArrayList<String>();
    		 List<String> nodesTranTo = new ArrayList<String>();
    		 
    		 doc = getDoc(fileSelected);
    		 
    		 NodeList listActivities = doc.getElementsByTagName(ACTIVITY);
    		 NodeList listTransitions = doc.getElementsByTagName(TRANSITION);
    		 
    		 for (int i = 0; i < listActivities.getLength(); i++){
    			 nodesAct.add(listActivities.item(i).getAttributes().getNamedItem("Id").getNodeValue());
    			 nodesNameAct.add(listActivities.item(i).getAttributes().getNamedItem("Name").getNodeValue());
    		 }
    		 
    		 for (int i = 0; i < listTransitions.getLength(); i++) { 
    			 nodesTranFrom.add(listTransitions.item(i).getAttributes().getNamedItem("From").getNodeValue());
    			 nodesTranTo.add(listTransitions.item(i).getAttributes().getNamedItem("To").getNodeValue());
    		 }
    		 
    		 int index = 0;
    		 for (String Activity : nodesAct)
    		 {
    			 boolean flag = false;
    			 
    			 for (String Transition : nodesTranFrom)
    				 if (Transition == Activity)
    					 flag = true;
    			 
    			 for (String Transition : nodesTranTo)
    				 if (Transition == Activity)
    					 flag = true;
    			 
    			 if (!flag){
    				 Validation vl = new Validation();
					 vl.setMsg("BPMN 0102.  All flow objects other than end events and compensating activities must have an outgoing sequence flow, if the process level includes any start or end events.");
					 vl.setName(nodesNameAct.get(index));
					 vl.setId(Activity);
					 
					 v.add(vl);
    			 }
    			 
    			 index++;
    		 }
    		 
    		 String msg = "";
			 if (v.size() > 0){
				 for (Validation validation : v) {
					msg += "ID -> " + validation.getId() + "\r\n";
					msg += "NAME -> " + validation.getName() + "\r\n";
					msg += "MSG -> " + validation.getMsg() + "\r\n";
				}
				 
				return msg;
			 }else
				return "";
    	 }catch(Exception ex){
    		 ex.printStackTrace();
    	 }
    	 
    	 return "";
     }
 
}
