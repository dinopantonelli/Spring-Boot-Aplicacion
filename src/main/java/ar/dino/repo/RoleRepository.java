package ar.dino.repo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import ar.dino.entity.Role;


@Repository
public interface RoleRepository extends CrudRepository<Role, Long>  {  //el CrudRepository es una plantilla para  encontrar por id y varias cosas mas que nos da Spring. Eso nos implementa
 
     
   public Role findByName(String name);

        
}