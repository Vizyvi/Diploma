package main.controller;

import main.api.bean.CaptchaBean;
import main.api.request.LoginRequest;
import main.api.response.Response;
import main.api.response.LoginResponse;
import main.api.response.RegisterUserBean;
import main.entity.User;
import main.security.SecurityService;
import main.service.CaptchaService;
import main.service.UserService;
import main.service.ValidateService;
import org.springframework.http.ResponseEntity;

import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class ApiAuthController {

    private final CaptchaService captchaService;
    private final ValidateService validateService;
    private final UserService userService;
    private final SecurityService securityService;


    public ApiAuthController(CaptchaService captchaService, ValidateService validateService, UserService userService, SecurityService securityService) {
        this.captchaService = captchaService;
        this.validateService = validateService;
        this.userService = userService;
        this.securityService = securityService;
    }

    @PostMapping(value = "/register")
        public Response createUser(@RequestBody RegisterUserBean bean) {
        return validateService.validateUser(bean);
    }

    @GetMapping(value = "/captcha")
    public CaptchaBean getCaptcha() {
        return captchaService.getCaptcha();
    }

    @GetMapping("/check")
    private Response authCheckResponse(Principal principal) {
        User user = userService.getCurrentUser();
        if(user == null) {
            return new Response();
        }
        return userService.getLoginResponse(user);
    }

    @PostMapping("/login")
    public ResponseEntity<Response> authorizeUser(HttpServletRequest request, @RequestBody LoginRequest loginRequest) {
        try {
            Map<String, String[]> parameterMap = request.getParameterMap();
            User user = securityService.login(loginRequest);
            return ResponseEntity.ok(userService.getLoginResponse(user));
        } catch (AuthenticationException ex) {
            return ResponseEntity.ok(new Response());
        }
    }

    @GetMapping("/logout")
    public Response logout() {
        securityService.logout();
        Response response = new Response();
        response.result = true;
        return response;
    }
}
