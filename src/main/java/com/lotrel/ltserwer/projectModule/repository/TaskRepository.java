package com.lotrel.ltserwer.projectModule.repository;

import com.lotrel.ltserwer.projectModule.model.Sprint;
import com.lotrel.ltserwer.projectModule.model.Task;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    Optional<Task> findByIdAndDeletedFalse(long id);

    List<Task> findByDeletedFalseAndTaskIdIn(Set<String> taskId, Sort sort);

    List<Task> findAllBySprintAndDeletedFalse(Sprint sprint, Sort sort);

}
