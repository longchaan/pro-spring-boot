package com.apress.spring.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.apress.spring.repository.JournalRepository;

@Controller
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

	@RequestMapping(value = "/env", method = RequestMethod.GET)
	public void env(javax.servlet.http.HttpServletResponse resp) {
		resp.setCharacterEncoding("EUC-KR");
		java.io.PrintWriter out;
		try {
			resp.setContentType("text/html");
			resp.setCharacterEncoding("UTF-8");
			out = resp.getWriter();
			out.println("<html>");
			out.println("<body>");
			out.println("<h1>시스템 환경</h1>");
			out.print("<pre>");
			Map<String, String> map = System.getenv();
			for (String key : map.keySet()) {
				out.print(key);
				out.print("=");
				out.println(System.getenv(key));
			}
			out.println("</pre>");
			out.println("</body>");
			out.println("</html>");
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@RequestMapping(value = "/props", method = RequestMethod.GET)
	public void props(javax.servlet.http.HttpServletResponse resp) {
		resp.setCharacterEncoding("EUC-KR");
		java.io.PrintWriter out;
		try {
			resp.setContentType("text/html");
			resp.setCharacterEncoding("UTF-8");
			out = resp.getWriter();
			out.println("<html>");
			out.println("<body>");
			out.println("<h1>시스템 프로퍼티</h1>");
			out.print("<pre>");
			Properties props = System.getProperties();
			String[] keys = props.keySet().toArray(new String[0]);
			for (String key : keys) {
				out.print(key);
				out.print("=");
				out.println(System.getProperty(key));
			}
			out.println("</pre>");
			out.println("</body>");
			out.println("</html>");
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@RequestMapping(value = "/vol", method = RequestMethod.GET)
	public void volume(javax.servlet.http.HttpServletResponse resp) {
		java.io.PrintWriter out;
		try {
			resp.setContentType("text/html");
			resp.setCharacterEncoding("UTF-8");
			out = resp.getWriter();
			java.io.File dir = new java.io.File(getContainerDir());
			out.println("<html>");
			out.println("<body>");
			out.println("<h1>Volume 목록</h1>");
			out.print("<pre>");
			out.println(dir.getAbsolutePath());
			java.io.File[] files = dir.listFiles();
			for (java.io.File f : files) {
				
				out.print(f.isDirectory() ? "d " : "f ");
				out.print(f.length() + " ");
				out.println(f.getName());
			}
			out.println("</pre>");
			out.println("</body>");
			out.println("</html>");
			out.flush();
		} catch (Exception e) {
			try {
				out = resp.getWriter();
				out.println("<html>");
				out.println("<body>");
				out.println("<h1>Volume 목록 예외</h1>");
				out.print("<pre>");
				e.printStackTrace(out);
				out.println("</pre>");
				out.println("</body>");
				out.println("</html>");
				out.flush();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}

	@SuppressWarnings("unchecked")
	public static String getContainerDir() {
		String containerDir = null;
		try {
			String VCAP_SERVICES = System.getenv("VCAP_SERVICES");
			ObjectMapper mapper = new ObjectMapper();
			Map<String, Object> services = mapper.readValue(VCAP_SERVICES, Map.class);
			String[] serviceNames = services.keySet().toArray(new String[0]);
			String volumeService = null;
			for (String svc : serviceNames) {
				if (svc.toLowerCase().indexOf("volume") != -1) {
					volumeService = svc;
					break;
				}
			}
			List<Map<String, Object>> service = (List<Map<String, Object>>) services.get(volumeService);
			Map<String, Object> serviceInfo = service.get(0);
			List<Map<String, Object>> volumeMounts = (List<Map<String, Object>>) serviceInfo.get("volume_mounts");
			Map<String, Object> volumeMount = volumeMounts.get(0);
			containerDir = (String) volumeMount.get("container_dir");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return containerDir;
	}
}
