package com.example.ecommerce.security;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.example.ecommerce.exceptions.InvalidToken;
import com.example.ecommerce.model.User;
import com.example.ecommerce.repositories.UserServiceRepo;
import com.example.ecommerce.services.JWTService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class JWTRequestFilter extends OncePerRequestFilter implements ChannelInterceptor {
    private JWTService jwtService;
    private UserServiceRepo userServiceRepo;

    public JWTRequestFilter(JWTService jwtService, UserServiceRepo userServiceRepo) {
        this.jwtService = jwtService;
        this.userServiceRepo = userServiceRepo;
    }

    //Multiple Filter Chain is present on security level. This method we add manually so that it gets added to filterChain.
    //doFilter method is called internally and not by us.
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //Request the header value with Authorization as label.
        String tokenHeader = request.getHeader("Authorization");

        //Check the token and get authenticated user.
        UsernamePasswordAuthenticationToken userAuthentication = checkToken(tokenHeader);
        if(userAuthentication != null) {
            //Set the details, so spring knows about the request associated to userAuthentication object.
            userAuthentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        }
        filterChain.doFilter(request,response);
    }

    private UsernamePasswordAuthenticationToken checkToken(String tokenHeader){
        if(tokenHeader != null && tokenHeader.startsWith("Bearer ")){
            //Remove "Bearer " from header value and only take value of token.
            String token = tokenHeader.substring(7);
            try{
                //Search for username using token from JWTService, JWT will decode the token with the help of username and withClaim.
                String username = jwtService.getUsername(token);
                Optional<User> userOptional = userServiceRepo.findByNameIgnoreCase(username);
                if(userOptional.isPresent()){
                    User user = userOptional.get();

                    //Create authentication object
                    UsernamePasswordAuthenticationToken userAuthentication = new UsernamePasswordAuthenticationToken(user,null,new ArrayList<>());

                    //Set the userAuthentication object to Authenticated. So whenever the token gets passed along with the Request it is considered as authenticated.
                    SecurityContextHolder.getContext().setAuthentication(userAuthentication);
                    return userAuthentication;
                }
                else{
                    throw new InvalidToken("User is not present with the provided token.");
                }
            }
            catch(JWTDecodeException e){

            } catch (InvalidToken e) {
                throw new RuntimeException(e);
            }
        }

        //If there is any issue during validation, set User authentication to null to reject the authentication.
        SecurityContextHolder.getContext().setAuthentication(null);

        //Return null
        return null;
    }

    //It is a method of ChannelInterceptor, To validate the client request and if validated then pass the message to the channel.
    //PreSend is called internally.
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        //If the message type is CONNECT then only check for validation, if already connected then no need to check for validation.
        if(message.getHeaders().get("simpMessageType").equals(SimpMessageType.CONNECT)) {
            Map nativeHeaders = (Map) message.getHeaders().get("nativeHeaders");
            if (nativeHeaders != null) {
                List authToken = (List) nativeHeaders.get("Authorization");
                if (authToken != null) {
                    String tokenHeader = (String) authToken.get(0);
                    checkToken(tokenHeader);
                }
            }
        }
        return message;
    }
}
