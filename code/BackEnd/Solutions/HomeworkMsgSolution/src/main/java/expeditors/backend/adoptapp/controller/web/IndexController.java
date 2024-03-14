package expeditors.backend.adoptapp.controller.web;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@Order(1)
public class IndexController implements ErrorController {

   @GetMapping("/")
   public ModelAndView index() {
      return new ModelAndView("index");
   }

   @GetMapping({"/error", "/eek"})
   public ModelAndView error(@RequestParam(name = "message",
         required = false) String message) {
      return new ModelAndView("realerror", "message", message);
   }

   @GetMapping({"/adminPage"})
   public ModelAndView adminPage() {
      return new ModelAndView("adminPage");
   }
}
