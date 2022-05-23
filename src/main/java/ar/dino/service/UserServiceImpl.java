package ar.dino.service;

import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import ar.dino.Exceptions.CustomeFieldValidationException;
import ar.dino.Exceptions.UsernameOrIdNotFound;
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
			            throw new CustomeFieldValidationException("Username no disponible","username");
			}
			return true;
		}

	        private boolean checkPasswordValid(User user) throws Exception {
			if (user.getConfirmPassword() == null || user.getConfirmPassword().isEmpty()) {
				throw new CustomeFieldValidationException("Confirm Password es obligatorio","confirmPassword");  //mensaje y nombre del campo
			}

			if ( !user.getPassword().equals(user.getConfirmPassword())) {
				throw new CustomeFieldValidationException("Password y Confirm Password no son iguales","password");
			}
			return true;
		}
        
	    
	   //video 6

		@Override
		public User getUserById(Long id) throws UsernameOrIdNotFound {
			return userRepository.findById(id).orElseThrow(() -> new UsernameOrIdNotFound("El usuario no existe."));  //ojo expresion lambda para lanzar una excepcion si no encuentra el usuario

		}


		
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
		@PreAuthorize("hasAuthority('ROLE_ADMIN')")           // VIDEO 9
        public void deleteUser(Long id) throws UsernameOrIdNotFound {
		//User user = userRepository.findById(id)
			//	.orElseThrow(() -> new Exception("Usuario no encontrado para borrar"+ this.getClass().getName()));
		if (isLoggedUserADMIN()) {	
         User user=getUserById(id);
         System.out.println(isLoggedUserADMIN());
         userRepository.delete(user);
		}else {
			throw new UsernameOrIdNotFound ("No tiene permiso para borrar un usuario");	
		}
			
		
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


	       private boolean isLoggedUserADMIN() {
	   		//Obtener el usuario logeado
	   		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal(); //asi se trae el objeto que este en el contexto y que este autenticado

	   		UserDetails loggedUser = null;
	   		Object roles = null;     //creo un objeto roles

	   		//Verificar que ese objeto traido de sesion es el usuario
	   		if (principal instanceof UserDetails) {
	   			loggedUser = (UserDetails) principal;

	   			roles = loggedUser.getAuthorities().stream()
	   					.filter(x -> "ROLE_ADMIN".equals(x.getAuthority())).findFirst()
	   					.orElse(null); 
	   		}
	   		return roles != null ? true : false;
	   	}
	       
	       
	       
	       
	       private User getLoggedUser() throws Exception {
	   		//Obtener el usuario logeado
	   		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	    
	                  UserDetails loggedUser = null;

	   		//Verificar que ese objeto traido de sesion es el usuario
	   		if (principal instanceof UserDetails) {    //verificamos si es un objeto de spring security
	   			loggedUser = (UserDetails) principal;
	   		}

	                   User myUser = userRepository.findByUsername(loggedUser.getUsername()).orElseThrow(() -> 
	                       new Exception("Error obteniendo el usuario logeado desde la sesion.")); //lo encuentra por el nombre en el repositorio fijarse donde esta el metodo

	   		return myUser;
	   	}

        
}