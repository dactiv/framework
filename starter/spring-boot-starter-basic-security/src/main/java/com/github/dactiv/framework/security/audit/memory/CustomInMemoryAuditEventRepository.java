package com.github.dactiv.framework.security.audit.memory;

import com.github.dactiv.framework.security.audit.AuditEventRepositoryInterceptor;
import org.springframework.boot.actuate.audit.AuditEvent;
import org.springframework.boot.actuate.audit.InMemoryAuditEventRepository;

import java.util.List;

public class CustomInMemoryAuditEventRepository extends InMemoryAuditEventRepository {

    private final List<AuditEventRepositoryInterceptor<Object>> interceptors;

    public CustomInMemoryAuditEventRepository(int capacity, List<AuditEventRepositoryInterceptor<Object>> interceptors) {
        super(capacity);
        this.interceptors = interceptors;
    }

    @Override
    public void add(AuditEvent event) {
        for (AuditEventRepositoryInterceptor<Object> interceptor : interceptors) {
            if (!interceptor.preAddHandle(event)) {
                return ;
            }
        }

        super.add(event);

        interceptors.forEach(i -> i.postAddHandle(event));
    }
}
