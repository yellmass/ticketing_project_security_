package com.cydeo.entity;

import com.cydeo.entity.common.UserPrincipal;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;

public class BaseEntityListener extends AuditingEntityListener {

    @PrePersist
    private void onPrePersist(BaseEntity baseEntity){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        baseEntity.setInsertDateTime(LocalDateTime.now());
        baseEntity.setLastUpdateDateTime(LocalDateTime.now());

        if(authentication!=null && !authentication.getName().equals("anonymousUser")){
            baseEntity.setInsertUserId(((UserPrincipal)authentication.getPrincipal()).getId());
            baseEntity.setLastUpdateUserId(((UserPrincipal)authentication.getPrincipal()).getId());
        }

    }

    @PreUpdate
    private void onPreUpdate(BaseEntity baseEntity){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        baseEntity.setLastUpdateDateTime(LocalDateTime.now());

        if(authentication!=null && !authentication.getName().equals("anonymousUser")){
            baseEntity.setLastUpdateUserId(((UserPrincipal)authentication.getPrincipal()).getId());
        }

    }
}
