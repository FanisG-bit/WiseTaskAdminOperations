package com.wisetaskadmin.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.wisetaskadmin.entities.Settings;

/**
 * @author Theofanis Gkoufas
 *
 */
@Repository
public interface SettingsRepository extends CrudRepository<Settings, Integer>{

}
