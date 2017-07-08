package com.github.eostermueller.littlemock.xslt;

import javax.xml.transform.stream.StreamSource;

public interface TextFileLocator {
	public StreamSource getTextFileForThisFolder(String key);
}
