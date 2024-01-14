package com.example.ecommerce.security;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.authorization.AuthorizationEventPublisher;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.authorization.SpringAuthorizationEventPublisher;
import org.springframework.security.messaging.access.intercept.AuthorizationChannelInterceptor;
import org.springframework.security.messaging.access.intercept.MessageMatcherDelegatingAuthorizationManager;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocket
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    private ApplicationContext context;
    private JWTRequestFilter jwtRequestFilter;
    private 

    public WebSocketConfig(ApplicationContext context, JWTRequestFilter jwtRequestFilter) {
        this.context = context;
        this.jwtRequestFilter = jwtRequestFilter;
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        //End point : The url which point to Web Socket.
        //AllowedOriginPatterns : The pattern that needs to be allowed.
        //WithSockJS : Our frontend is Sock JS based,
        //SockJs is a polyfill browser library which provides http-based fallback when websocket connection fails
        registry.addEndpoint("/websocket").setAllowedOriginPatterns("**").withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        //Anything that starts with "/topic" will be sent to this broker.
        registry.enableSimpleBroker("/topic");

        registry.setApplicationDestinationPrefixes("/app");
    }

    //Create authentication manager to store the information, like which message needs to be checked for authentication
    // and which should be permitted without check
    private AuthorizationManager<Message<?>> makeMessageAuthorizationManager(){
        MessageMatcherDelegatingAuthorizationManager.Builder message =
                new MessageMatcherDelegatingAuthorizationManager.Builder();

        message.
                simpDestMatchers("/topic/user/**").authenticated()
                .anyMessage().permitAll();

        return message.build();
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        AuthorizationManager<Message<?>> authorizationManager =
                makeMessageAuthorizationManager();
        //Creating an interceptor.
        //Interceptor are the component which can be added in between a chain. Same as FilterChain.
        AuthorizationChannelInterceptor authorizationChannelInterceptor =
                new AuthorizationChannelInterceptor(authorizationManager);

        //Creating an authorization event publisher, it will be used to notify the application about the "Authorization".
        AuthorizationEventPublisher publisher =
                new SpringAuthorizationEventPublisher(context);

        //Setting up the event publisher of the interceptor.
        authorizationChannelInterceptor.setAuthorizationEventPublisher(publisher);

        //Adding interceptor to our Channel and adding the Request filter before the interceptor.
        // This means that it will check if the token is validated then The message is passed to the topics.
        registration.interceptors(jwtRequestFilter,authorizationChannelInterceptor);
    }

    //Class is used to reject client messages based on destination(specific topic).
    private class RejectClientMessagesOnChannelsChannelInterceptor implements ChannelInterceptor {
        private String[] paths = new String[]{
                ".topic/user/*/address"
        };
        @Override
        public Message<?> preSend(Message<?> message, MessageChannel channel) {
            //Check if the message type is message
            if(message.getHeaders().get("simpMessageType").equals(SimpMessageType.MESSAGE)){
                //Check if the simpDestination matches with our paths.
                for(String path : paths){
                    if(MATCHER.)
                }
            }
        }
    }
}
