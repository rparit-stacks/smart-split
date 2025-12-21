package com.rps.smartsplit.Audit;

import com.rps.smartsplit.model.User;
import com.rps.smartsplit.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service("auditAware")
public class AuditAware implements AuditorAware<User> {

    @Autowired
    private UserService userService;



    @Override
    public Optional<User> getCurrentAuditor() {
        return Optional.ofNullable(userService.getLoggedInUser());
    }
}
