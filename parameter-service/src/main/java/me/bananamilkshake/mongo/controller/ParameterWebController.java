package me.bananamilkshake.mongo.controller;

import lombok.AllArgsConstructor;
import me.bananamilkshake.mongo.service.ParameterService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/parameters")
@AllArgsConstructor
public class ParameterWebController {

	private final ParameterService parameterService;

	@GetMapping
	public ModelAndView parametersView() {
		final ModelAndView modelAndView = new ModelAndView("parameters");
		modelAndView.addObject("types", parameterService.getTypes());
		return modelAndView;
	}
}