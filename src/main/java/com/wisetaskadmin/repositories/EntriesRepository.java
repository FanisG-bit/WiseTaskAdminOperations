package com.wisetaskadmin.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.wisetaskadmin.entities.Entry;

@Repository
public interface EntriesRepository extends CrudRepository<Entry, Integer> {

}
