package leti.sidis.plans.api;

import javax.servlet.http.HttpServletRequest;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.stereotype.Component;


@Component
public class Helper {


    private JwtDecoder jwtDecoder;

    public String getUserByToken(HttpServletRequest request){
        String token = request.getHeader("Authorization");
        String newToken = token.replace("Bearer ", "");
        Jwt dToken = this.jwtDecoder.decode(newToken);
        String s = (String) dToken.getClaims().get("user");
        String id = String.valueOf(s.split(",")[0]);

        return id;
    }

}
