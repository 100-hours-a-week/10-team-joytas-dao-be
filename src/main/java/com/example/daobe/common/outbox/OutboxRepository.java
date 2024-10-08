package com.example.daobe.common.outbox;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OutboxRepository extends JpaRepository<Outbox, String> {

    @Query("""
            SELECT ob FROM Outbox ob
            WHERE ob.isComplete = false
            """)
    List<Outbox> findAllByIsCompleteFalse();
}
