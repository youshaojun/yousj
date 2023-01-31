package top.yousj.security.matcher;

import java.util.Collections;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpMethod;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.security.web.util.matcher.RequestVariablesExtractor;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.util.UrlPathHelper;
import top.yousj.core.constant.UaaConstant;

/**
 * copy {@link org.springframework.security.web.util.matcher.AntPathRequestMatcher}
 *
 * 更改{@link CustomizeAntPathRequestMatcher#matches(javax.servlet.http.HttpServletRequest)}
 */
public final class CustomizeAntPathRequestMatcher implements RequestMatcher, RequestVariablesExtractor {

	private static final String MATCH_ALL = "/**";

	private final Matcher matcher;

	private final String pattern;

	private final HttpMethod httpMethod;

	private final boolean caseSensitive;

	private final UrlPathHelper urlPathHelper;

	/**
	 * Creates a matcher with the specific pattern which will match all HTTP methods in a
	 * case sensitive manner.
	 * @param pattern the ant pattern to use for matching
	 */
	public CustomizeAntPathRequestMatcher(String pattern) {
		this(pattern, null);
	}

	/**
	 * Creates a matcher with the supplied pattern and HTTP method in a case sensitive
	 * manner.
	 * @param pattern the ant pattern to use for matching
	 * @param httpMethod the HTTP method. The {@code matches} method will return false if
	 * the incoming request doesn't have the same method.
	 */
	public CustomizeAntPathRequestMatcher(String pattern, String httpMethod) {
		this(pattern, httpMethod, true);
	}

	/**
	 * Creates a matcher with the supplied pattern which will match the specified Http
	 * method
	 * @param pattern the ant pattern to use for matching
	 * @param httpMethod the HTTP method. The {@code matches} method will return false if
	 * the incoming request doesn't doesn't have the same method.
	 * @param caseSensitive true if the matcher should consider case, else false
	 */
	public CustomizeAntPathRequestMatcher(String pattern, String httpMethod, boolean caseSensitive) {
		this(pattern, httpMethod, caseSensitive, null);
	}

	/**
	 * Creates a matcher with the supplied pattern which will match the specified Http
	 * method
	 * @param pattern the ant pattern to use for matching
	 * @param httpMethod the HTTP method. The {@code matches} method will return false if
	 * the incoming request doesn't have the same method.
	 * @param caseSensitive true if the matcher should consider case, else false
	 * @param urlPathHelper if non-null, will be used for extracting the path from the
	 * HttpServletRequest
	 */
	public CustomizeAntPathRequestMatcher(String pattern, String httpMethod, boolean caseSensitive,
										  UrlPathHelper urlPathHelper) {
		Assert.hasText(pattern, "Pattern cannot be null or empty");
		this.caseSensitive = caseSensitive;
		if (pattern.equals(MATCH_ALL) || pattern.equals("**")) {
			pattern = MATCH_ALL;
			this.matcher = null;
		}
		else {
			// If the pattern ends with {@code /**} and has no other wildcards or path
			// variables, then optimize to a sub-path match
			if (pattern.endsWith(MATCH_ALL)
					&& (pattern.indexOf('?') == -1 && pattern.indexOf('{') == -1 && pattern.indexOf('}') == -1)
					&& pattern.indexOf("*") == pattern.length() - 2) {
				this.matcher = new SubpathMatcher(pattern.substring(0, pattern.length() - 3), caseSensitive);
			}
			else {
				this.matcher = new SpringAntMatcher(pattern, caseSensitive);
			}
		}
		this.pattern = pattern;
		this.httpMethod = StringUtils.hasText(httpMethod) ? HttpMethod.valueOf(httpMethod) : null;
		this.urlPathHelper = urlPathHelper;
	}

	/**
	 * Returns true if the configured pattern (and HTTP-Method) match those of the
	 * supplied request.
	 * @param request the request to match against. The ant pattern will be matched
	 * against the {@code servletPath} + {@code pathInfo} of the request.
	 */
	@Override
	public boolean matches(HttpServletRequest request) {
		if (this.httpMethod != null && StringUtils.hasText(request.getMethod())
				&& this.httpMethod != HttpMethod.resolve(request.getMethod())) {
			return false;
		}
		if (this.pattern.equals(MATCH_ALL)) {
			return true;
		}
		String url = request.getHeader(UaaConstant.FORWARD_AUTH_HEADER_URI);
		return this.matcher.matches(url);
	}

	@Override
	@Deprecated
	public Map<String, String> extractUriTemplateVariables(HttpServletRequest request) {
		return matcher(request).getVariables();
	}

	@Override
	public MatchResult matcher(HttpServletRequest request) {
		if (!matches(request)) {
			return MatchResult.notMatch();
		}
		if (this.matcher == null) {
			return MatchResult.match();
		}
		String url = getRequestPath(request);
		return MatchResult.match(this.matcher.extractUriTemplateVariables(url));
	}

	private String getRequestPath(HttpServletRequest request) {
		if (this.urlPathHelper != null) {
			return this.urlPathHelper.getPathWithinApplication(request);
		}
		String url = request.getServletPath();
		String pathInfo = request.getPathInfo();
		if (pathInfo != null) {
			url = StringUtils.hasLength(url) ? url + pathInfo : pathInfo;
		}
		return url;
	}

	public String getPattern() {
		return this.pattern;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof CustomizeAntPathRequestMatcher)) {
			return false;
		}
		CustomizeAntPathRequestMatcher other = (CustomizeAntPathRequestMatcher) obj;
		return this.pattern.equals(other.pattern) && this.httpMethod == other.httpMethod
				&& this.caseSensitive == other.caseSensitive;
	}

	@Override
	public int hashCode() {
		int result = (this.pattern != null) ? this.pattern.hashCode() : 0;
		result = 31 * result + ((this.httpMethod != null) ? this.httpMethod.hashCode() : 0);
		result = 31 * result + (this.caseSensitive ? 1231 : 1237);
		return result;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Ant [pattern='").append(this.pattern).append("'");
		if (this.httpMethod != null) {
			sb.append(", ").append(this.httpMethod);
		}
		sb.append("]");
		return sb.toString();
	}

	private interface Matcher {

		boolean matches(String path);

		Map<String, String> extractUriTemplateVariables(String path);

	}

	private static final class SpringAntMatcher implements Matcher {

		private final AntPathMatcher antMatcher;

		private final String pattern;

		private SpringAntMatcher(String pattern, boolean caseSensitive) {
			this.pattern = pattern;
			this.antMatcher = createMatcher(caseSensitive);
		}

		@Override
		public boolean matches(String path) {
			return this.antMatcher.match(this.pattern, path);
		}

		@Override
		public Map<String, String> extractUriTemplateVariables(String path) {
			return this.antMatcher.extractUriTemplateVariables(this.pattern, path);
		}

		private static AntPathMatcher createMatcher(boolean caseSensitive) {
			AntPathMatcher matcher = new AntPathMatcher();
			matcher.setTrimTokens(false);
			matcher.setCaseSensitive(caseSensitive);
			return matcher;
		}

	}

	/**
	 * Optimized matcher for trailing wildcards
	 */
	private static final class SubpathMatcher implements Matcher {

		private final String subpath;

		private final int length;

		private final boolean caseSensitive;

		private SubpathMatcher(String subpath, boolean caseSensitive) {
			Assert.isTrue(!subpath.contains("*"), "subpath cannot contain \"*\"");
			this.subpath = caseSensitive ? subpath : subpath.toLowerCase();
			this.length = subpath.length();
			this.caseSensitive = caseSensitive;
		}

		@Override
		public boolean matches(String path) {
			if (!this.caseSensitive) {
				path = path.toLowerCase();
			}
			return path.startsWith(this.subpath) && (path.length() == this.length || path.charAt(this.length) == '/');
		}

		@Override
		public Map<String, String> extractUriTemplateVariables(String path) {
			return Collections.emptyMap();
		}

	}

}
