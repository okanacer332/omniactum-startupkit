package com.omniactum.startupkit.modules.audit.repository;

import com.omniactum.startupkit.modules.audit.model.AuditLog;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuditLogRepository extends MongoRepository<AuditLog, String> {
    List<AuditLog> findTop5ByOrderByTimestampDesc(); // BU SATIRI EKLE
}

