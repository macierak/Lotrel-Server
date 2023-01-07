package com.lotrel.ltserwer.projectModule.repository;

import com.lotrel.ltserwer.projectModule.model.UserTaskEdits;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserTaskEditRepository extends JpaRepository<UserTaskEdits, Long> {
}
