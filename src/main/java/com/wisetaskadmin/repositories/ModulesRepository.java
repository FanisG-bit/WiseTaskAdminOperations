package com.wisetaskadmin.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.wisetaskadmin.entities.Module;

@Repository
public interface ModulesRepository extends CrudRepository<Module, Integer>{

}
