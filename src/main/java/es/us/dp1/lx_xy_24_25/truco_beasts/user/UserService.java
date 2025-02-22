/*
 * Copyright 2002-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package es.us.dp1.lx_xy_24_25.truco_beasts.user;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.us.dp1.lx_xy_24_25.truco_beasts.exceptions.AccessDeniedException;
import es.us.dp1.lx_xy_24_25.truco_beasts.exceptions.NameDuplicatedException;
import es.us.dp1.lx_xy_24_25.truco_beasts.exceptions.ResourceNotFoundException;
import jakarta.validation.Valid;

@Service
public class UserService {

	private UserRepository userRepository;	
	private final PasswordEncoder encoder;


	@Autowired
	public UserService(UserRepository userRepository, PasswordEncoder encoder) {

		this.userRepository = userRepository;
		this.encoder=encoder;
	}

    

	@Transactional
	public User saveUser(User user) throws DataAccessException {
		userRepository.save(user);
		return user;
	}

	@Transactional(readOnly = true)
	public User findUser(String username) {
		return userRepository.findByUsername(username)
				.orElseThrow(() -> new ResourceNotFoundException("User", "username", username));
	}

	@Transactional(readOnly = true)
	public User findUser(Integer id) {
		return userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
	}	

	@Transactional(readOnly = true)
	public User findCurrentUser() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth == null)
			throw new ResourceNotFoundException("Nobody authenticated!");
		else
			
			return userRepository.findByUsername(auth.getName())
					.orElseThrow(() -> new ResourceNotFoundException("User", "Username", auth.getName()));
		
	}

	@Transactional(readOnly =true)
	public Boolean existsUser(String username) {
		return userRepository.existsByUsername(username);
	}



	@Transactional(readOnly = true)
	public Iterable<User> findAll() {
		return userRepository.findAll();
	}

	public Iterable<User> findAllByAuthority(String auth) {
		return userRepository.findAllByAuthority(auth);
	}

	@Transactional
	public User updateUser( User user, Integer idToUpdate) {
		User toUpdate = findUser(idToUpdate);
		if(toUpdate == null) {
            throw new AccessDeniedException("Tu usuario no ha sido encontrado");
        }
		
        if(!existsUser(user.getUsername()) || (existsUser(user.getUsername()) && (user.getUsername().equals(toUpdate.getUsername())))){
            toUpdate.setUsername(user.getUsername());
            if(!(user.getPassword()==null)) {
                toUpdate.setPassword(encoder.encode(user.getPassword()));
            }
			if(user.getAuthority() != null){
				toUpdate.setAuthority(user.getAuthority());
			}
            saveUser(toUpdate);
            return toUpdate;
        }else{
            throw new NameDuplicatedException("Ese nombre está en uso");
        } 
			
	}
	@Transactional(rollbackFor = {AccessDeniedException.class,NameDuplicatedException.class})
	public User updateCurrentUser(@Valid User user){
		User currentUser = findCurrentUser();
		if(currentUser == null) {
            throw new AccessDeniedException("Tu usuario no ha sido encontrado");
        }
		
        if(!existsUser(user.getUsername()) || (existsUser(user.getUsername()) && (user.getUsername().equals(currentUser.getUsername())))){
            currentUser.setUsername(user.getUsername());
            if(!(user.getPassword()==null)) {
                currentUser.setPassword(encoder.encode(user.getPassword()));
            }
			currentUser.setLastConnection(LocalDateTime.now());
			saveUser(currentUser);
            return currentUser;
        }else{
            throw new NameDuplicatedException("Ese nombre está en uso");
        } 
	}

	@Transactional
	public void deleteUser(Integer id) {
		User toDelete = findUser(id);
//		deleteRelations(id, toDelete.getAuthority().getAuthority());
//		this.userRepository.deletePlayerRelation(id);
		this.userRepository.delete(toDelete);
	}

	@Transactional
	public void updateConnection() {
		User usuarioActual = findCurrentUser();
		if (usuarioActual==null) {
			return;
		}
		usuarioActual.setLastConnection(LocalDateTime.now());
		userRepository.save(usuarioActual);
	}

	@Transactional(readOnly = true)
	public Page<User> findUsuariosPaginacion(Pageable pageable) throws DataAccessException {
		return userRepository.findUsuariosPags(pageable);
	}
	

}
