package ar.dino.service;

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
        
}