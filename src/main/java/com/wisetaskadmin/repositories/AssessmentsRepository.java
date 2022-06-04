package com.wisetaskadmin.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.wisetaskadmin.entities.Assessment;

/**
 * @author Theofanis Gkoufas
 *
 */
@Repository
public interface AssessmentsRepository extends CrudRepository<Assessment, Integer>{

}
