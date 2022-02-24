//
//package com.windstoss.messanger.api.websocket;

//import com.windstoss.messanger.security.Service.AuthenticationService;
//import com.windstoss.messanger.services.UserService;
//import org.springframework.http.server.ServerHttpRequest;
//import org.springframework.http.server.ServerHttpResponse;
//import org.springframework.stereotype.Component;
//import org.springframework.web.socket.WebSocketHandler;
//import org.springframework.web.socket.server.HandshakeInterceptor;
//
//import java.util.Map;
//
//@Component
//public class HttpHandshakeInterceptor implements HandshakeInterceptor {
//
//    private final AuthenticationService authenticationService;
//
//    public HttpHandshakeInterceptor(AuthenticationService authenticationService){
//        this.authenticationService = authenticationService;
//    }
//
//    @Override
//    public boolean beforeHandshake(ServerHttpRequest request,
//                                   ServerHttpResponse response,
//                                   WebSocketHandler wsHandler,
//                                   Map<String, Object> attributes) throws Exception {
//
//        return true;
//    }
//
//    @Override
//    public void afterHandshake(ServerHttpRequest request,
//                               ServerHttpResponse response,
//                               WebSocketHandler wsHandler,
//                               Exception exception) {
//
//    }
//}
