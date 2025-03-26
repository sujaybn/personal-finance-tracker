package com.backend.finance.finance_tracker_backend.repository;

import com.backend.finance.finance_tracker_backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);  // ✅ Add this method
}
