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
	
	@Value("${busy.description:test}")
	private String busyDescription = "Larger #'s = more processing, less performance.  X * Y = number of iterations of random number generation and getter/setter activiy.";
	
	@Value("${busy.update:test}")
	private String busyUpdate = "Update";
		
	@Value("${busy.sleep:test}")
	private String busySleep = "Time in milliseconds to Thread.sleep().";
		
    @RequestMapping("/ui")
    public String optimizations(@RequestParam(value="name", required=false, defaultValue="World") String name, Model model) {
        model.addAttribute("name", name);
        model.addAttribute("xpathMessage", this.xpathMessage);
        model.addAttribute("otherMessage", this.otherMessage);
        model.addAttribute("busyMessage", this.busyMessage);
        model.addAttribute("busyDescription", this.busyDescription);
        model.addAttribute("busyUpdate", this.busyUpdate);
        model.addAttribute("busySleep", this.busySleep);
        return "optimizations";
    }

}
