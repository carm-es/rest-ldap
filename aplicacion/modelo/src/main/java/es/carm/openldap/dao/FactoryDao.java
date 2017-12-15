package es.carm.openldap.dao;

import es.carm.openldap.ConnectionError;
import es.carm.openldap.NoDataFound;

public interface FactoryDao<T>  {

	// Obtener un objeto por su Login o NIF
	public T  get(String id) throws NoDataFound,ConnectionError;
		
    
}
