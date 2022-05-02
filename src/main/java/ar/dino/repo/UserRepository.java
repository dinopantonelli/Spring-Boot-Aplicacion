package ar.dino.repo;

import java.util.Set;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import ar.dino.entity.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long>  {  //el CrudRepository es una plantilla para  encontrar por id y varias cosas mas que nos da Spring. Eso nos implementa
 
     public Set<User> findByUsername(String username);                  //findBy palabra clave
   

        
}