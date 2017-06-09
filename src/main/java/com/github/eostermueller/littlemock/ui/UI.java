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
	@Value("${xpath.message:test}")
	private String xpathMessage = "XPath Optimizations";
	
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
	private String updateCDescription = "bytes are retained for a random duration, this is the upper bound (milliseconds)";
	
	@Value("${busy.update:test}")
	private String busyUpdate = "Update";
		
	@Value("${busy.sleep:test}")
	private String busySleep = "Time in milliseconds to Thread.sleep().";
		
	@Value("${code.link:test}")
	private String codeLink = "(link to source code)";
		
    @RequestMapping("/ui")
    public String optimizations(@RequestParam(value="name", required=false, defaultValue="World") String name, Model model) {
        model.addAttribute("name", name);
        model.addAttribute("xpathMessage", this.xpathMessage);
        model.addAttribute("otherMessage", this.otherMessage);
        model.addAttribute("busyMessage", this.busyMessage);
        model.addAttribute("oldGenMessage", this.oldGenMessage);
        model.addAttribute("busyDescription", this.busyDescription);
        model.addAttribute("oldGenDescription", this.oldGenDescription);
        model.addAttribute("updateADescription", this.updateADescription);
        model.addAttribute("updateBDescription", this.updateBDescription);
        model.addAttribute("updateCDescription", this.updateCDescription);
        model.addAttribute("busyUpdate", this.busyUpdate);
        model.addAttribute("busySleep", this.busySleep);
        model.addAttribute("codeLink", this.codeLink);
        return "optimizations";
    }

}
