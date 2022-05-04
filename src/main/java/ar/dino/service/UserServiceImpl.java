package ar.dino.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ar.dino.entity.User;
import ar.dino.repo.UserRepository;

@Service
public class UserServiceImpl implements UserService{  
 
   @Autowired
   UserRepository userRepository;
     
         	
	public  Iterable<User> getAllUsers(){
		
		return userRepository.findAll();  //nos trae todos los usuarios, pero puedo poner o traer lo que se me cante manipuladno la interfaz
	}
	
	
	 @Override
		public User createUser(User user) throws Exception {
			if (checkUsernameAvailable(user) && checkPasswordValid(user)) {  // con los booleanos de los metodos me aseguro de crear un usuario que no existe
				user = userRepository.save(user);
			}
			return user;//con esto retorna el usuario y lo tenemos activo que sirva mas adelante
		}
	
	
	private boolean checkUsernameAvailable(User user) throws Exception { 
		Optional<User> userFound = userRepository.findByUsername(user.getUsername());
		if (userFound.isPresent()) {
			throw new Exception("Username no disponible");
		}
		return true;
	}
	 

	    private boolean checkPasswordValid(User user) throws Exception {          //Metodos que lanzan una Excepcion
			if ( !user.getPassword().equals(user.getConfirmPassword())) {      //si son diferentes por eso el admiracion al principio
				throw new Exception("Password y Confirm Password no son iguales");
			}
			return true;
	    } 
        
}