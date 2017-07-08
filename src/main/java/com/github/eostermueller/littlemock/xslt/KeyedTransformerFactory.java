package com.github.eostermueller.littlemock.xslt;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.sax.SAXTransformerFactory;

import org.apache.commons.pool2.BaseKeyedPooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;


public class KeyedTransformerFactory
    extends BaseKeyedPooledObjectFactory<String, Transformer> {

	private TextFileLocator xslLocator = null;

	public KeyedTransformerFactory(TextFileLocator val) {
		this.xslLocator = val;
	}
    @Override
    public Transformer create(String key) throws TransformerConfigurationException {
    	
        javax.xml.transform.TransformerFactory factory = SAXTransformerFactory.newInstance();
        Transformer transformer = factory.newTransformer(this.xslLocator.getTextFileForThisFolder(key));
    	
        return transformer;
    }

    @Override
    public PooledObject<Transformer> wrap(Transformer t) {
        return new DefaultPooledObject<Transformer>(t);
    }

    @Override
    public void passivateObject(String key, PooledObject<Transformer> pooledObject) {
    }

}
