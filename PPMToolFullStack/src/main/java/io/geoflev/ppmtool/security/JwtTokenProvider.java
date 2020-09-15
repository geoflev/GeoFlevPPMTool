package io.geoflev.ppmtool.security;

import io.geoflev.ppmtool.domain.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static io.geoflev.ppmtool.security.SecurityConstants.EXPIRATION_TIME;
import static io.geoflev.ppmtool.security.SecurityConstants.SECRET;

@Component
public class JwtTokenProvider {

    //The method that generates the token when we have valid credentials

    public String generateToken(Authentication authentication){

        //get the authenticated user in type User
        //Validate the token
        //Get the user id from the token

        User user = (User) authentication.getPrincipal();

        Date now = new Date(System.currentTimeMillis());
        Date expiredDate = new Date(now.getTime() + EXPIRATION_TIME);

        String userId = Long.toString(user.getId());

        //create the map to use on jwts.builder
        //we do that so we include information in our token
        //could include the Role too for future reference
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", (Long.toString(user.getId())));
        claims.put("username",user.getUsername());
        claims.put("fullName",user.getFullName());

        return Jwts.builder()
                    .setSubject(userId)
                    .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiredDate)
                .signWith(SignatureAlgorithm.HS512, SECRET)
                .compact();
    }


}
