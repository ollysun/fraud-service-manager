package com.etz.fraudeagleeyemanager.config;

import com.etz.fraudeagleeyemanager.constant.AppConstant;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.filter.OncePerRequestFilter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {


    @Autowired
    private JwtTokenHelper jwtTokenUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException, ServletException, IOException {

        String authToken = getAuthToken(req.getHeader(AppConstant.AUTHORIZATION)).orElse(null);
        String username = getUsername(authToken).orElse(null);
        if(username != null){
            req.setAttribute(AppConstant.USERNAME, username);
        }

        chain.doFilter(req, res);
    }

    private Optional<String> getAuthToken(String header){
        if (!Objects.isNull(header) && header.startsWith(AppConstant.TOKEN_PREFIX)) {
            return Optional.of(header.replace(AppConstant.TOKEN_PREFIX, ""));
        }
        return Optional.ofNullable(header);
    }

    private Optional<String> getUsername(String authToken){

        if (!Objects.isNull(authToken)){
            try {
                return  jwtTokenUtil.getUsername(authToken);
            } catch (IllegalArgumentException e) {
                log.error("an error occurred during getting username fromUser token", e);
            } catch (ExpiredJwtException e) {
                log.warn("the token is expired and not valid anymore "+ e.getMessage());
            } catch (SignatureException e) {
                log.warn("Authentication Failed. Username or Password not valid. "+e.getMessage());
            } catch (MalformedJwtException e) {
                log.warn("Malformed token."+e.getMessage());
            }
        }

        return Optional.empty();
    }
}