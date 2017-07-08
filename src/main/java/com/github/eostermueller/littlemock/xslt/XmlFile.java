package com.github.eostermueller.littlemock.xslt;

import java.io.InputStream;

public class XmlFile {
	String parts[] = null;

	public InputStream getFileContents() {
		return  getClass().getClassLoader().getResourceAsStream( this.getFullName() );
	}
	public XmlFile(String fullName) {
		this.fullName = fullName.trim();
		parts = this.fullName.split("/");

	}
	String fullName = null;
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String val) {
		this.fullName = val;
	}
	public String getName() {
		return parts[parts.length-1];
	}
	public String getPath() {
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < parts.length -1; i++) {
			if (i>0)
				sb.append('/');
			sb.append(parts[i]);
				
		}
		return sb.toString();
	}
	public String getExtension() {
		String name = getName();
		int i = name.indexOf('.');
		return name.substring(i+1);
	}
	public boolean isXsltExtention() {
		boolean rc = false;
		
		if (this.getExtension().equals("xsl") || this.getExtension().equals(".xslt") )
			rc = true;
		
		return rc;
	}

}
