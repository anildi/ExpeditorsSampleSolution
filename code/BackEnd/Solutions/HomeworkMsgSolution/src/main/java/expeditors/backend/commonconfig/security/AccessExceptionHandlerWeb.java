package expeditors.backend.commonconfig.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;

/**
 * To deal with 403 Forbidden Errors.
 * <p>
 * We do it in a slightly round about way, using a little trick
 * picked up from so.  We inject the standard
 * HandlerExceptionResolver, and ask it find a handler for the Exception.
 * <p>
 * Nice thing about this is that we can write the Handler as
 * a standard Spring Exception Handler, which have we declare in
 * ttl.larku.exceptions.LastStopHandler.  This gives us a nicer
 * interface to work with than the request and response that we
 * get here.
 *
 * @author whynot
 */
@Component
public class AccessExceptionHandlerWeb implements AccessDeniedHandler {

    //This little puppy resolves the given exception to a Spring
    //@ExceptionHandler.  We have one in .../exceptions/LastStopHandler.java
    @Autowired(required = false)
    @Qualifier("handlerExceptionResolver")
    private HandlerExceptionResolver resolver;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException exception) throws IOException, ServletException {
        var cPath = request.getContextPath();
        var requestURI = request.getRequestURI();
        request.setAttribute("message", exception.getMessage());
        var rd = request.getRequestDispatcher("/error");
        var responseURL = "/error?message=" + exception.getMessage();
        response.sendRedirect(responseURL);
//        rd.forward(request, response);
//        var mav = new ModelAndView("redirect:/error", "message", ex.getMessage());
//        resolver.resolveException(request, response, null,
//              new OurAuthWebException(exception.getMessage(), exception));
    }
}

