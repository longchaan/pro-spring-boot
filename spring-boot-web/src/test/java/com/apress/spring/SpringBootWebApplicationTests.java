package com.apress.spring;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.iterableWithSize;

import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Arrays;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import com.apress.spring.domain.JournalEntry;

@RunWith(SpringRunner.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SpringBootWebApplicationTests {

	private static final Logger LOG = LoggerFactory.getLogger(SpringBootWebApplicationTests.class);

	private final String SPRING_BOOT_MATCH = "스프링 부트";
	private final String CLOUD_MATCH = "클라우드";

	@SuppressWarnings("rawtypes")
	private HttpMessageConverter mappingJackson2HttpMessageConverter;
	private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
			MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));
	private MockMvc mockMvc;

	@Autowired
	private WebApplicationContext webApplicationContext;

	@Autowired
	void setConverters(HttpMessageConverter<?>[] converters) {
		this.mappingJackson2HttpMessageConverter = Arrays.asList(converters).stream()
				.filter(converter -> converter instanceof MappingJackson2HttpMessageConverter).findAny().get();
	}

	@Before
	public void setup() throws Exception {
		this.mockMvc = webAppContextSetup(webApplicationContext).build();
	}

	@Test
	public void getAll() throws Exception {
		mockMvc.perform(get("/journal/all")).andExpect(status().isOk()).andExpect(content().contentType(contentType))
				.andExpect(jsonPath("$", iterableWithSize(5)))
				.andExpect(jsonPath("$[0]['title']", containsString(SPRING_BOOT_MATCH)));
	}

	@Test
	public void findByTitle() throws Exception {
		String title = new String(CLOUD_MATCH.getBytes("UTF-8"), "UTF-8");
		mockMvc.perform(get("/journal/findBy/title/" + title)).andExpect(status().isOk())
				.andExpect(content().contentType(contentType)).andExpect(jsonPath("$", iterableWithSize(1)))
				.andExpect(jsonPath("$[0]['title']", containsString(CLOUD_MATCH)));
	}

	@Test
	public void add() throws Exception {
		mockMvc.perform(post("/journal")
				.content(this.toJsonString(new JournalEntry("스프링 부트 테스트", "스프링부트 단위 테스트를 생성했다", "09/14/2017")))
				.contentType(contentType)).andExpect(status().isOk());
	}

	@SuppressWarnings("unchecked")
	protected String toJsonString(Object obj) throws IOException {
		MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
		this.mappingJackson2HttpMessageConverter.write(obj, MediaType.APPLICATION_JSON, mockHttpOutputMessage);
		return mockHttpOutputMessage.getBodyAsString();
	}

	@Test
	public void contextLoads() {
		LOG.info("SpringBootWebApplicationTests.contextLoads() {...}");
	}

	/**
	 * Indicates whether the given character is in the {@code ALPHA} set.
	 * @see <a href="http://www.ietf.org/rfc/rfc3986.txt">RFC 3986, appendix A</a>
	 */
	// org.springframework.web.util.HierarchicalUriComponents.Type.isAlpha(int)
	protected boolean isAlpha(int c) {
		return (c >= 'a' && c <= 'z' || c >= 'A' && c <= 'Z');
	}

	/**
	 * Indicates whether the given character is in the {@code DIGIT} set.
	 * @see <a href="http://www.ietf.org/rfc/rfc3986.txt">RFC 3986, appendix A</a>
	 */
	protected boolean isDigit(int c) {
		return (c >= '0' && c <= '9');
	}
	
	/**
	 * Indicates whether the given character is in the {@code sub-delims} set.
	 * @see <a href="http://www.ietf.org/rfc/rfc3986.txt">RFC 3986, appendix A</a>
	 */
	protected boolean isSubDelimiter(int c) {
		return ('!' == c || '$' == c || '&' == c || '\'' == c || '(' == c || ')' == c || '*' == c || '+' == c ||
				',' == c || ';' == c || '=' == c);
	}
	
	/**
	 * Indicates whether the given character is in the {@code unreserved} set.
	 * @see <a href="http://www.ietf.org/rfc/rfc3986.txt">RFC 3986, appendix A</a>
	 */
	protected boolean isUnreserved(int c) {
		return (isAlpha(c) || isDigit(c) || '-' == c || '.' == c || '_' == c || '~' == c);
	}

	/**
	 * Indicates whether the given character is in the {@code pchar} set.
	 * @see <a href="http://www.ietf.org/rfc/rfc3986.txt">RFC 3986, appendix A</a>
	 */
	protected boolean isPchar(int c) {
		return (isUnreserved(c) || isSubDelimiter(c) || ':' == c || '@' == c);
	}

	// org.springframework.web.util.HierarchicalUriComponents.encodeUriComponent(String, String, Type)
	// org.springframework.web.util.HierarchicalUriComponents.encodeBytes(byte[], Type)
	public String encode(String s) throws UnsupportedEncodingException {
		byte[] source = s.getBytes("UTF-8");
		ByteArrayOutputStream bos = new ByteArrayOutputStream(s.length());
		for (byte b : source) {
			if (b < 0) {
				b += 256;
			}
			if (isPchar(b)) {
				bos.write(b);
			}
			else {
				bos.write('%');
				char hex1 = Character.toUpperCase(Character.forDigit((b >> 4) & 0xF, 16));
				char hex2 = Character.toUpperCase(Character.forDigit(b & 0xF, 16));
				bos.write(hex1);
				bos.write(hex2);
			}
		}
		byte[] bytes = bos.toByteArray();
		return new String(bytes, "US-ASCII");
	}
	
}
