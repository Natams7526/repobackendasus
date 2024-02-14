package com.grandapp.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import com.grandapp.model.ClientModel;

public interface ClientRepository extends JpaRepository<ClientModel, Long> {
	
//	@Transactional(readOnly = true)
//	List<ClientModel> findByName(String  name) throws Exception;
//	
//	@Transactional(readOnly = true)
	@Query(value = "SELECT c.* FROM clients c "
			+ " WHERE c.phone = :phone", nativeQuery = true)
	public ClientModel findByPhone(@Param("phone") String Phone)throws Exception;

	

}
