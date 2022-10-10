package com.etz.fraudeagleeyemanager.config;

import static java.util.Objects.isNull;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtTokenHelper implements Serializable {

    private static final long serialVersionUID = -2550185165626007488L;


    @Value("${security.secret-key}")
    private String secret;


    /**
     * retrieve username from jwt token
     * @param token userObject from authService
     * @return subject
     */
    public Optional<String> getUsernameFromToken(String token) {
        return Optional.ofNullable(getAllClaimsFromToken(token).get("user_name",String.class));
    }


    /**
     * for retrieving any information from token we will need the secret key
     * @param token jwt
     * @return Claim
     */
    private Claims getAllClaimsFromToken(String token) {

        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

    private JsonObject decodeTokenPayloadToJsonObject(DecodedJWT decodedJWT) {
        try {
            String payloadAsString = decodedJWT.getPayload();
            return new Gson().fromJson(
                    new String(Base64.getDecoder().decode(payloadAsString), StandardCharsets.UTF_8),
                    JsonObject.class);
        }   catch (RuntimeException exception){
            throw new InvalidTokenException("Invalid JWT or JSON format of each of the jwt parts", exception);
        }
    }

    public Optional<String> getUsername(String value) {
        JsonObject payloadAsJson = getPayloadAsJsonObject(value);

        return Optional.ofNullable(
                payloadAsJson.getAsJsonPrimitive("user_name").getAsString());
    }

    private JsonObject getPayloadAsJsonObject(String value) {
        DecodedJWT decodedJWT = decodeToken(value);
        return decodeTokenPayloadToJsonObject(decodedJWT);
    }

    private DecodedJWT decodeToken(String value) {
        if (isNull(value)){
            throw new InvalidTokenException("Token has not been provided");
        }
        return JWT.decode(value);
    }





}