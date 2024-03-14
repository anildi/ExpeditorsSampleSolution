package expeditors.backend.commonconfig.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

/**
 * To deal with 403 Forbidden Errors. 
 * 
 * We do it in a slightly round about way, using a little trick 
 * picked up from SO.  We inject the standard
 * HandlerExceptionResolver, and ask it find a handler for the Exception.
 *
 * To give credit where it's due:
 * https://stackoverflow.com/questions/19767267/handle-spring-security-authentication-exceptions-with-exceptionhandler
 * The answer by Christophe Bornet, about halfway down the page.
 * 
 * Nice thing about this is that we can write the Handler as
 * a standard Spring Exception Handler, which have we declare in 
 * ttl.larku.exceptions.LastStopHandler.  This gives us a nicer
 * interface to work with than the request and response that we 
 * get here.
 * 
 * This needs to get registered with HttpSecurity.  Look in SecurityWithCustomeUser.java.
*/
@Component
public class AuthExceptionHandler implements AuthenticationEntryPoint {

	//This little puppy resolves the given exception to a Spring
	//@ExceptionHandler.  We have one in .../exceptions/LastStopHandler.java
	@Autowired(required = false)
	@Qualifier("handlerExceptionResolver")
	private HandlerExceptionResolver resolver;

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception)
			throws IOException, ServletException {
		resolver.resolveException(request, response, null, exception);
	}
}
