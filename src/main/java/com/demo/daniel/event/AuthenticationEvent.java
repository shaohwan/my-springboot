package com.demo.daniel.event;

import com.demo.daniel.model.entity.LogLoginOperationType;
import com.demo.daniel.model.entity.LogLoginStatusType;
import com.demo.daniel.service.LogLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationEvent {

    @Autowired
    private LogLoginService logLoginService;

    @EventListener
    public void onSuccess(AuthenticationSuccessEvent event) {
        UserDetails user = (UserDetails) event.getAuthentication().getPrincipal();
        logLoginService.saveLog(user.getUsername(), LogLoginStatusType.SUCCESS, LogLoginOperationType.LOGIN_SUCCESS);
    }

    @EventListener
    public void onFailure(AbstractAuthenticationFailureEvent event) {
        String username = String.valueOf(event.getAuthentication().getPrincipal());
        logLoginService.saveLog(username, LogLoginStatusType.FAILURE, LogLoginOperationType.ACCOUNT_FAILURE);
    }
}
