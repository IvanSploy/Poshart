package es.urjc.dad.poshart.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;

@Configuration
@EnableWebMvc
public class CSRFHandlerConfiguration {
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new CSRFHandlerInterceptor());
	}
}

class CSRFHandlerInterceptor implements HandlerInterceptor {
	@Override
	public void postHandle(final HttpServletRequest request,
	final HttpServletResponse response, final Object handler,
		final ModelAndView modelAndView) throws Exception {
		 CsrfToken token = (CsrfToken) request.getAttribute("_csrf"); 
		 modelAndView.addObject("token", token.getToken());
	}
}
