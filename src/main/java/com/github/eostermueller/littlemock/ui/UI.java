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
	private String xpathMessage = "Hello World";
	@Value("${other.message:test}")
	private String otherMessage = "Hello World";
	@Value("${busy.message:test}")
	private String busyMessage = "Hello World";
	
    @RequestMapping("/ui")
    public String optimizations(@RequestParam(value="name", required=false, defaultValue="World") String name, Model model) {
        model.addAttribute("name", name);
        model.addAttribute("xpathMessage", this.xpathMessage);
        model.addAttribute("otherMessage", this.otherMessage);
        model.addAttribute("busyMessage", this.busyMessage);
        return "optimizations";
    }

}
