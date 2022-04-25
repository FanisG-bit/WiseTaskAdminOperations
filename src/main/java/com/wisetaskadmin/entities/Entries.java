package com.wisetaskadmin.entities;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
	Having this bean class, as well as its dependency (list) with a scope of "prototype",
	means that when we request such an object (bean) from the spring factory, spring always creates 
	a new object and returns to us a reference of that object.
	Prior to this change, the scope was "singleton" (which is the default one). This created a problem
	when it came to the entries list. Meaning that whenever I retrieved data from the database they were
	added to that list, but then when retrieving a second time, the data from the previous request would
	also be transported (so we retrieved the data twice etc.). The problem was that the elements of the 
	list were never removed and since the object had a singleton scope, every time that I wanted to use
	that bean, spring would return to me a reference to the same object (with the same unchanged list).
	So by changing the respective scopes the problem was fixed.
	See -> https://www.baeldung.com/spring-bean-scopes
*/

@Component(value = "entries")
@Scope("prototype")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Entries {
	
	@Autowired
	@Qualifier("entriesList")
	private List<Entry> list;
	
}
