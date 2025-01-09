package es.us.dp1.lx_xy_24_25.truco_beasts.user;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;


public interface UserRepository extends  CrudRepository<User, Integer>{			


	Optional<User> findByUsername(String username);

	Boolean existsByUsername(String username);



	Optional<User> findById(Integer id);
	
	@Query("SELECT u FROM User u WHERE u.authority.authority = :auth")
	Iterable<User> findAllByAuthority(String auth);

	@Query("SELECT u FROM User u")
	Page<User> findUsuariosPags(Pageable pageable);
	
	//@Query("DELETE FROM Player o WHERE o.user.id = :userId")
	//@Modifying
	//void deletePlayerRelation(int userId);
	
	
	
}
