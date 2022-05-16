package ar.dino.service;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ar.dino.entity.Role;
import ar.dino.repo.UserRepository;

@Service
@Transactional
public class UserDetailsServiceImpl implements UserDetailsService  {

	@Autowired
	UserRepository userRepository;
	
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		
		ar.dino.entity.User appUser= userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Login Username Invalido."));
		
		 Set grantList = new HashSet(); 
		    
		    //Crear la lista de los roles/accessos que tienen el usuarios
		    for (Role role: appUser.getRoles()) {
		        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(role.getDescription());
		            grantList.add(grantedAuthority);
		    }
				
		    //Crear y retornar Objeto de usuario soportado por Spring Security
		    UserDetails user = (UserDetails) new User(appUser.getUsername(), appUser.getPassword(), grantList);
		    return user;
			
		}
	

}


