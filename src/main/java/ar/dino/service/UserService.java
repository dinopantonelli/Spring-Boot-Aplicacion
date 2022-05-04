package ar.dino.service;

import ar.dino.entity.User;

public interface UserService {  
 
           
  public Iterable<User> getAllUsers();

  public User createUser(User user) throws Exception;

        
}
