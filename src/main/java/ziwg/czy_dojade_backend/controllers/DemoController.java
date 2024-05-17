package ziwg.czy_dojade_backend.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ziwg.czy_dojade_backend.utils.RegexUtil;

@RestController
@RequestMapping("/api/auth/demo")
@Tag(name = "DemoController", description = "Demo controller for testing purposes")
public class DemoController {
    @GetMapping
    public ResponseEntity<String> demo() {
        String p1 = "pp"; // invalid password
        String p2 = "pppppppppppppppppppppppppppppppppppppppP1@PPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPP"; // invalid password
        String p3 = "Password123#@"; // valid password
        String p4 = "Password123"; // invalid password
        String p5 = "password123#@"; // invalid password
        String p6 = "password#"; // invalid password
        String p7 = ""; // invalid password
        String p8 = "Password123#@"; // valid password

        String m1 = "m";
        String m2 = "m@m";
        String m3 = "mmm@m.m";
        String m4 = "m@m.m";
        String m5 = "m@192.168.10.10.com";
        String m6 = "m@m.m.m.m";
        String m7 = "test.example.com";
        String m8 = "test@examplecom";
        String m9 = "test@wp.pl";
        String m10 = "";
        String m11 = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa@wp.pl";
        String m12 = "test@example.com";

        System.out.println(p1 + " " + RegexUtil.isValidPassword(p1));
        System.out.println(p2 + " " + RegexUtil.isValidPassword(p2));
        System.out.println(p3 + " " + RegexUtil.isValidPassword(p3));
        System.out.println(p4 + " " + RegexUtil.isValidPassword(p4));
        System.out.println(p5 + " " + RegexUtil.isValidPassword(p5));
        System.out.println(p6 + " " + RegexUtil.isValidPassword(p6));
        System.out.println(p7 + " " + RegexUtil.isValidPassword(p7));
        System.out.println(p8 + " " + RegexUtil.isValidPassword(p8));

        System.out.println(m1 + " " + RegexUtil.isValidEmail(m1));
        System.out.println(m2 + " " + RegexUtil.isValidEmail(m2));
        System.out.println(m3 + " " + RegexUtil.isValidEmail(m3));
        System.out.println(m4 + " " + RegexUtil.isValidEmail(m4));
        System.out.println(m5 + " " + RegexUtil.isValidEmail(m5));
        System.out.println(m6 + " " + RegexUtil.isValidEmail(m6));
        System.out.println(m7 + " " + RegexUtil.isValidEmail(m7));
        System.out.println(m8 + " " + RegexUtil.isValidEmail(m8));
        System.out.println(m9 + " " + RegexUtil.isValidEmail(m9));
        System.out.println(m10 + " " + RegexUtil.isValidEmail(m10));
        System.out.println(m11 + " " + RegexUtil.isValidEmail(m11));
        System.out.println(m12 + " " + RegexUtil.isValidEmail(m12));

        return new ResponseEntity<>("Demo", HttpStatus.OK);
    }
}
