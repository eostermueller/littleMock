package com.github.eostermueller.littlemock.xslt;

import java.util.ArrayList;
import java.util.List;

public class XsltRepo {
	private List<XmlFile> myXmlFiles = new ArrayList<XmlFile>();
	private XsltFile xsltFile;
	public List<XmlFile> getXmlFiles() {
		return this.myXmlFiles;
	}
	/**
	 * Almost  by definition, all files in this repo are from the exact same path.
	 * ....so return the first one.
	 * @return
	 */
	public String getPath() {
		String rc = null;
		if (this.getXmlFiles().size() > 0)
			rc = this.getXmlFiles().get(0).getPath();
			
		return rc;
	}
	public void addFile(XmlFile xmlFile) {
		
		if (xmlFile.getExtension().equals("xsl")
			|| xmlFile.getExtension().equals("xslt")	
				) {
			XsltFile xslt = new XsltFile(xmlFile.getFullName());
			this.setXslt(xslt);
		} else {
			this.myXmlFiles.add(xmlFile);
		}
	}

    public XsltRepo() {
		// TODO Auto-generated constructor stub
	}

	public void setXslt(XsltFile val) {
    	this.xsltFile = val;
    }
	public XsltFile getXslt() {
		return this.xsltFile;
	}
}