package yu.cse.odiga.auth.controller;


import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import yu.cse.odiga.auth.domain.CustomUserDetails;

@AllArgsConstructor
@RestController
@RequestMapping("/")
public class TestController {

    @GetMapping("test")
    public String test(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        System.out.println(customUserDetails.getUsername());
        return customUserDetails.toString();
    }
}
