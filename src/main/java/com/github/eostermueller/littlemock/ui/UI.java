package com.github.eostermueller.littlemock.ui;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@EnableAutoConfiguration

public class UI {

	// inject via application.properties
	@Value("${perfKey.message:test}")
	private String perfKeyMessage = "Performance Key";

	@Value("${perfKey.description.01:test}")
	private String perfKeyDescr01 = "Performance Key";

	@Value("${perfKey.description.02:test}")
	private String perfKeyDescr02 = "Performance Key";

	@Value("${perfKey.description.03:test}")
	private String perfKeyDescr03 = "Performance Key";
	
	@Value("${xpath.message:test}")
	private String xpathMessage = "XPath Optimizations";
	
	@Value("${xslt.message:test}")
	private String xsltMessage = "XSLT Optimizations";
	
	@Value("${xslt.impl.00:test}")
	private String xsltImpl00 = "XSLT Optimizations";
	
	@Value("${xslt.impl.01:test}")
	private String xsltImpl01 = "XSLT Optimizations";
	
	@Value("${xslt.impl.02:test}")
	private String xsltImpl02 = "XSLT Optimizations";
	
	@Value("${other.message:test}")
	private String otherMessage = "Other Optimizations";
	
	@Value("${busy.message:test}")
	private String busyMessage = "Busy Processing Optimizations";
	
	@Value("${old.gen.message:test}")
	private String oldGenMessage = "Create Old Gen Garbage";
	
	@Value("${busy.description:test}")
	private String busyDescription = "Larger #'s = more processing, less performance.  X * Y = number of iterations of random number generation and getter/setter activity.";
	
	@Value("${old.gen.description:test}")
	private String oldGenDescription = "Create old gen garbage or perhaps or out-of-memory condition.";
	
	@Value("${update.a.description:test}")
	private String updateADescription = "Larger numbers = less frequent purges of old gen";
	
	@Value("${update.b.description:test}")
	private String updateBDescription = "byte counts are randomly allocated, this is the upper bound";
	
	@Value("${update.c.description:test}")
	private String updateCDescription = "min range of duration for which bytes are retained (selected at random / unit = ms)";
	
	@Value("${update.d.description:test}")
	private String updateDDescription = "max range of duration for which bytes are retained (selected at random / unit = ms)";
	
	@Value("${clear.old.gen.description:test}")
	private String clearOldGenDescription = "Call the .clear() method on the singleton/map with old gen data.";
	
	@Value("${busy.update:test}")
	private String busyUpdate = "Update";
		
	@Value("${busy.sleep:test}")
	private String busySleep = "Time in milliseconds to Thread.sleep().";
		
	@Value("${code.link:test}")
	private String codeLink = "(link to source code)";
		
    @RequestMapping("/ui")
    public String optimizations(@RequestParam(value="name", required=false, defaultValue="World") String name, Model model) {
        model.addAttribute("name", name);
        model.addAttribute("perfKeyMessage", this.perfKeyMessage);
        model.addAttribute("perfKeyDescr01", this.perfKeyDescr01);
        model.addAttribute("perfKeyDescr02", this.perfKeyDescr02);
        model.addAttribute("perfKeyDescr03", this.perfKeyDescr03);
        
        
        
        model.addAttribute("xpathMessage", this.xpathMessage);
        model.addAttribute("xsltMessage", this.xsltMessage);
        model.addAttribute("xsltImpl00", this.xsltImpl00);
        model.addAttribute("xsltImpl01", this.xsltImpl01);
        model.addAttribute("xsltImpl02", this.xsltImpl02);
        
        
        model.addAttribute("otherMessage", this.otherMessage);
        model.addAttribute("busyMessage", this.busyMessage);
        model.addAttribute("oldGenMessage", this.oldGenMessage);
        model.addAttribute("busyDescription", this.busyDescription);
        model.addAttribute("oldGenDescription", this.oldGenDescription);
        model.addAttribute("updateADescription", this.updateADescription);
        model.addAttribute("updateBDescription", this.updateBDescription);
        model.addAttribute("updateCDescription", this.updateCDescription);
        model.addAttribute("updateDDescription", this.updateDDescription);
        model.addAttribute("clearOldGenDescription", this.clearOldGenDescription);
        model.addAttribute("busyUpdate", this.busyUpdate);
        model.addAttribute("busySleep", this.busySleep);
        model.addAttribute("codeLink", this.codeLink);
        return "optimizations";
    }

}
