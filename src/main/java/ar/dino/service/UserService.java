package ar.dino.service;

import javax.validation.Valid;

import ar.dino.entity.User;

public interface UserService {  
 
           
  public Iterable<User> getAllUsers();

  public User createUser(User user) throws Exception;

  public User getUserById(Long id) throws Exception;   //video 6

  public User updateUser(User user) throws Exception;  //video 6
  
  public void deleteUser(Long id) throws Exception;   //video 7
        
}
