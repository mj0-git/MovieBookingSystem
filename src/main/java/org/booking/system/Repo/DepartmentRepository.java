package org.booking.system.Repo;

import org.booking.system.DTO.Department;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartmentRepository
        extends CrudRepository<Department, Long> {
}
