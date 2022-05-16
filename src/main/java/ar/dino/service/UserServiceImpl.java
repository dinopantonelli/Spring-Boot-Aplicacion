package ar.dino.service;

import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import ar.dino.dta.ChangePasswordForm;
import ar.dino.entity.User;
import ar.dino.repo.UserRepository;

@Service
public class UserServiceImpl implements UserService{  
 
   @Autowired
   UserRepository userRepository;
   
   // video 9
	@Autowired
	BCryptPasswordEncoder bCryptPasswordEncoder;
     
         	
	public  Iterable<User> getAllUsers(){
		
		return userRepository.findAll();  //nos trae todos los usuarios, pero puedo poner o traer lo que se me cante manipuladno la interfaz
	}
	
	
	 @Override
		public User createUser(User user) throws Exception {
			if (checkUsernameAvailable(user) && checkPasswordValid(user)) {  // con los booleanos de los metodos me aseguro de crear un usuario que no existe
				//cuando creo un usuario debo encriptar la contraseña tambien asi:
				String encodePassword = bCryptPasswordEncoder.encode(user.getPassword());  // con esto encripto el nuevo password
				user.setPassword(encodePassword);				
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
	    	 if (user.getConfirmPassword() == null || user.getConfirmPassword().isEmpty()) {  //si el confirm password esta vacio o nulo, le agregamos esta excepcion
	 			throw new Exception("Confirm Password es obligatorio");
	 		}
    	
	    	
	    	if ( !user.getPassword().equals(user.getConfirmPassword())) {      //si son diferentes por eso el admiracion al principio
				throw new Exception("Password y Confirm Password no son iguales");
			}
			return true;
	    } 
        
	    
	   //video 6

		@Override
		public User getUserById(Long id) throws Exception {
			return userRepository.findById(id).orElseThrow(() -> new Exception("El usuario para editar no existe."));  //ojo expresion lambda para lanzar una excepcion si no encuentra el usuario

		}


		@Override
		public User updateUser(User fromUser) throws Exception {
			User toUser = getUserById(fromUser.getId());
			mapUser(fromUser, toUser);
		    return userRepository.save(toUser);
		}
	    
		protected void mapUser(User from, User to) {
			to.setUsername(from.getUsername());
			to.setFirstName(from.getFirstName());
			to.setLastName(from.getLastName());
			to.setEmail(from.getEmail());
			to.setRoles(from.getRoles());
		}
		
		
		// video 7
		@PreAuthorize("hasAnyRole('ROLE_ADMIN')")           // VIDEO 9
        public void deleteUser(Long id) throws Exception {
		User user = userRepository.findById(id)
				.orElseThrow(() -> new Exception("UsernotFound in deleteUser -"+ this.getClass().getName()));

		userRepository.delete(user);
	 }
	    
        
        
		// video 8


	     /*@Override
		public User changePassword(ChangePasswordForm form) throws Exception {
			User user = getUserById(form.getId());

			if ( !user.getPassword().equals(form.getCurrentPassword())) {
				throw new Exception ("Current Password invalido.");
			}

			if( user.getPassword().equals(form.getNewPassword())) {
				throw new Exception ("Nuevo debe ser diferente al password actual.");
			}

			if( !form.getNewPassword().equals(form.getConfirmPassword())) {
				throw new Exception ("Nuevo Password y Current Password no coinciden.");
			}

			user.setPassword(form.getNewPassword());
			return repository.save(user);
		}*/
	    
	      // video 9 

	       public User changePassword(ChangePasswordForm form) throws Exception {
			User user = getUserById(form.getId());
			
			if ( !isLoggedUserADMIN() && !user.getPassword().equals(form.getCurrentPassword())) {//si es usuario valide el password actual
				throw new Exception ("Current Password invalido.");
			}
			
			if( user.getPassword().equals(form.getNewPassword())) {
				throw new Exception ("Nuevo password debe ser diferente al password actual.");
			}
			
			if( !form.getNewPassword().equals(form.getConfirmPassword())) {
				throw new Exception ("Nuevo Password y Current Password no coinciden.");
			}
			
			String encodePassword = bCryptPasswordEncoder.encode(form.getNewPassword());  // con esto encripto el nuevo password
			user.setPassword(encodePassword);
			return userRepository.save(user);
		}


         private boolean isLoggedUserADMIN() {  // si soy admin no necesito decir mi password para editar un password ADEMAS de que lo oculte del frontend
	   		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal(); //esto trae el usuario autenticado, con la validacion y todo grabado en session
	   		UserDetails loggedUser = null;
	   		if (principal instanceof UserDetails) {          // preguno si es una instancia de UserDetail, hacer urso de Security                    
	   			loggedUser = (UserDetails) principal;     //   Si es hago el cast
	   		
	   			loggedUser.getAuthorities().stream()                            
	   					.filter(x -> "ADMIN".equals(x.getAuthority() ))     // si es ADMIN atravesando las autoridades traigame el usuario  
	   					.findFirst().orElse(null);                     //loggedUser = null;
	   		}
	   		return loggedUser != null ? true :false;  // esto es si es distinto de null, retorna true, si es null retorna falso
	   	}
        
}