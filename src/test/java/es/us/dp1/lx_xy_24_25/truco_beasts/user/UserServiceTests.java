package es.us.dp1.lx_xy_24_25.truco_beasts.user;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Pageable;
import es.us.dp1.lx_xy_24_25.truco_beasts.exceptions.ResourceNotFoundException;
import org.springframework.data.domain.Page;

//@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
@SpringBootTest
@AutoConfigureTestDatabase
class UserServiceTests {

	@Autowired
	private UserService userService;

	@Autowired
	private AuthoritiesService authService;


	@Test
	@WithMockUser(username = "player1", password = "0wn3r")
	@DirtiesContext
	void shouldFindCurrentUser() {
		User user = this.userService.findCurrentUser();
		assertEquals("player1", user.getUsername());
	}

	@Test
	@WithMockUser(username = "prueba")
	void shouldNotFindCorrectCurrentUser() {
		assertThrows(ResourceNotFoundException.class, () -> this.userService.findCurrentUser());
	}

	@Test
	void shouldNotFindAuthenticated() {
		assertThrows(ResourceNotFoundException.class, () -> this.userService.findCurrentUser());
	}

	@Test
	void shouldFindAllUsers() {
		List<User> users = (List<User>) this.userService.findAll();
		assertEquals(12, users.size());
	}

	@Test
	void shouldFindUsersByUsername() {
		User user = this.userService.findUser("player1");
		assertEquals("player1", user.getUsername());
	}

	@Test
	void shouldFindUsersByAuthority() {
		List<User> owners = (List<User>) this.userService.findAllByAuthority("PLAYER");
		assertEquals(11, owners.size());

		List<User> admins = (List<User>) this.userService.findAllByAuthority("ADMIN");
		assertEquals(1, admins.size());
		
		List<User> vets = (List<User>) this.userService.findAllByAuthority("VET");
		assertEquals(0, vets.size());
	}

	@Test
	void shouldNotFindUserByIncorrectUsername() {
		assertThrows(ResourceNotFoundException.class, () -> this.userService.findUser("usernotexists"));
	}		

	@Test
	void shouldFindSingleUser() {
		User user = this.userService.findUser(4);
		assertEquals("player3", user.getUsername());
	}

	@Test
	void shouldNotFindSingleUserWithBadID() {
		assertThrows(ResourceNotFoundException.class, () -> this.userService.findUser(100));
	}

	@Test
	@DirtiesContext
	void shouldExistUser() {
		assertEquals(true, this.userService.existsUser("player1"));
	}

	@Test
	void shouldNotExistUser() {
		assertEquals(false, this.userService.existsUser("player10000"));
	}

	@Test
	@Transactional
	void shouldUpdateUser() {
		int idToUpdate = 1;
		String newName="Change";
		User user = this.userService.findUser(idToUpdate);
		user.setUsername(newName);
		userService.updateUser(user, idToUpdate);
		user = this.userService.findUser(idToUpdate);
		assertEquals(newName, user.getUsername());
	}

	@Test
	@Transactional
	void shouldInsertUser() {
		int count = ((Collection<User>) this.userService.findAll()).size();

		User user = new User();
		user.setUsername("Sam");
		user.setPassword("password");
		user.setLastConnection(LocalDateTime.now());
		user.setAuthority(authService.findByAuthority("ADMIN"));

		this.userService.saveUser(user);
		assertNotEquals(0, user.getId().longValue());
		assertNotNull(user.getId());


		int finalCount = ((Collection<User>) this.userService.findAll()).size();
		assertEquals(count + 1, finalCount);
	}

	@Test
	@WithMockUser(username = "player1", password = "0wn3r")
	void shouldUpdateCurrentUser(){ //Hecho por David
		User currentUser = this.userService.findCurrentUser();
		currentUser.setUsername("David");
		currentUser.setPassword("password");
		userService.updateCurrentUser(currentUser);
		currentUser = this.userService.findUser("David");
		assertEquals("David", currentUser.getUsername());
	}

	@Test
	@WithMockUser(username = "player2", password = "0wn3r")
	@DirtiesContext
	void shouldUpdateConnection(){ //Hecho por David
		LocalDateTime lastConnection = this.userService.findCurrentUser().getLastConnection();
		userService.updateConnection();
		User currentUser = this.userService.findCurrentUser();
		assertNotEquals(lastConnection, currentUser.getLastConnection());
	}

	@Test
	@WithMockUser(username = "player1", password = "0wn3r")
	void shouldFindUsuariosPaginacion(){ //Hecho por David
		Pageable pagina = PageRequest.of(0, 5);
		Page<User> users = this.userService.findUsuariosPaginacion(pagina);
		assertEquals(5, users.getSize());
		assertEquals(3, users.getTotalPages());
		assertEquals(12, users.getTotalElements());
		assertEquals(0, users.getNumber());
		assertEquals(5, users.getNumberOfElements());
	}



}
