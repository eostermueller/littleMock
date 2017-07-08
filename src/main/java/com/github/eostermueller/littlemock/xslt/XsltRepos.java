package com.github.eostermueller.littlemock.xslt;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.xml.transform.stream.StreamSource;


public class XsltRepos implements TextFileLocator {
	
	public static XsltRepos SINGLETON;
	
	public XsltRepos(String cfg) {
		String[] parts = cfg.split(";");
		for(String oneFileName : parts) {
			if (oneFileName!=null && oneFileName.trim().length() > 0) {
				XmlFile x = new XmlFile(oneFileName);
				addFile(x);
			}
		}
	}
	public List<XsltRepo> getRepos() {
		return this.myRepos;
	}
	private void addFile(XmlFile x) {
		XsltRepo repo = this.repoHashMap.get(x.getPath());
		if (repo==null) { // file belongs to a repo (aka path) that hasn't been added yet
			
			repo = new XsltRepo();
			repo.addFile(x);
			addRepo(x.getPath(),repo);
		} else {
			repo.addFile(x);//Add file to repo with other files in that same repo (aka path)
		}
		
	}
	private void addRepo(String path, XsltRepo repo) {
		this.repoHashMap.put(path, repo);
		this.myRepos.add(repo);
	}
	private Map<String, XsltRepo>  repoHashMap = new ConcurrentHashMap<String, XsltRepo>();
	private List<XsltRepo> myRepos = new ArrayList<XsltRepo>();

	@Override
	public StreamSource getTextFileForThisFolder(String key) {
		XsltFile xslt = new XsltFile(key);
		if (xslt.getFullName()==null || xslt.getFullName().length() <=0) {
			throw new RuntimeException("Did not find repo key [" + key + "] ");
		} else if (!xslt.isXsltExtention() )
			throw new RuntimeException("repo key is not an xslt file [" + key + "] ");
		//return new StreamSource(new StringReader(r.getOnePerFolder().textFromFile));
		return new StreamSource( xslt.getFileContents() );
	}



}
