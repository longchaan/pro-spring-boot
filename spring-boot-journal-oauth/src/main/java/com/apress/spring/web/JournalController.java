package com.apress.spring.web;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.apress.spring.repository.JournalRepository;

@RestController
public class JournalController {

	private static final String VIEW_INDEX = "index";

	@Autowired
	JournalRepository repo;

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public ModelAndView index(ModelAndView modelAndView) {
		modelAndView.setViewName(VIEW_INDEX);
		modelAndView.addObject("journal", repo.findAll());
		return modelAndView;
	}

	@RequestMapping(value = "/test")
	public void test(javax.servlet.http.HttpServletRequest req, javax.servlet.http.HttpServletResponse resp)
			throws IOException {
		String line;
		do {
			line = req.getReader().readLine();
			resp.getWriter().println(line);
		} while (line != null);
		resp.setContentType("text/plain");
		resp.getWriter().println("Method: " + req.getMethod());
		resp.getWriter().println("RequestURI: " + req.getRequestURI());
		resp.getWriter().println("PathInfo: " + req.getPathInfo());
		resp.getWriter().println("ContentLength: " + req.getContentLength());
		resp.getWriter().println("ParameterMap: " + req.getParameterMap());
	}

}
