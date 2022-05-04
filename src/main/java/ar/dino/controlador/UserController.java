package ar.dino.controlador;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import ar.dino.entity.User;
import ar.dino.repo.RoleRepository;
import ar.dino.service.UserService;


@Controller
public class UserController {

  @Autowired
  UserService userService;

  @Autowired
  RoleRepository roleRepository;

  @GetMapping("/")
  public String index() {
    return "index";
  }

  @GetMapping("/userForm")
  public String userForm(Model model) {
    model.addAttribute("userForm", new User());
    model.addAttribute("userList", userService.getAllUsers());
    model.addAttribute("roles",roleRepository.findAll());
    model.addAttribute("listTab","active");
    return "user-form/user-view";
  }
  
  //video 4
  
 /* @PostMapping("/userForm")
  public String createUser(@Valid @ModelAttribute("userForm") User user, BindingResult result, ModelMap model ){  //"userForm" coincide con el userForm @{/userForm}" 
  //BindingResult result nos trae el resultado de la validacion

	  if(result.hasErrors()) {
		  
		  model.addAttribute("userForm", user);
		  model.addAttribute("listTab","active");
		  
	  }
	  model.addAttribute("userList", userService.getAllUsers());
	  model.addAttribute("roles",roleRepository.findAll());
	  
	  return "user-form/user-view";
	  
	  
  }*/
  
  // video 5  crear Usuario
  
  @PostMapping("/userForm")
  public String postUserForm(@Valid @ModelAttribute("userForm")User user, BindingResult result, ModelMap model) {
  		if(result.hasErrors()) {
  			model.addAttribute("userForm", user);
  			model.addAttribute("formTab","active");
  		}else {
  			try {
  				userService.createUser(user);                        // crear el metodo para la creacion de Usuario  en el UserService
  				model.addAttribute("userForm", new User());         // le decimos que muestre el form new user para llenar 
  				model.addAttribute("listTab","active");            // le decimos que muestre la lista
  			} catch (Exception e) {                                        // de aca viene el error del UserServiceImpl
  				model.addAttribute("formErrorMessage",e.getMessage());      // con esto le pasamos los mensajes de error al HTML
  				                  
  				model.addAttribute("formTab","active"); //con esto dejamos activos los campos en el forulario
             
  			}
  		}

  		model.addAttribute("userList", userService.getAllUsers());
  		model.addAttribute("roles",roleRepository.findAll());
  		return "user-form/user-view";
  	}
  
  
  
  
  
  
  
  
  
}