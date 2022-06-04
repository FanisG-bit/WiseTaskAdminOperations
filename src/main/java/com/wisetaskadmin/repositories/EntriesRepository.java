package com.wisetaskadmin.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.wisetaskadmin.entities.Entry;

/**
 * @author Theofanis Gkoufas
 *
 */
@Repository
public interface EntriesRepository extends CrudRepository<Entry, Integer> {

}
