package ar.dino.controlador;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import ar.dino.Exceptions.CustomeFieldValidationException;
import ar.dino.Exceptions.UsernameOrIdNotFound;
import ar.dino.dta.ChangePasswordForm;
import ar.dino.entity.Role;
import ar.dino.entity.User;
import ar.dino.repo.RoleRepository;
import ar.dino.service.UserService;


@Controller
public class UserController {

  @Autowired
  UserService userService;

  @Autowired
  RoleRepository roleRepository;

  @GetMapping({"/","/login"})
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
  
/* @PostMapping("/userForm")
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
  	} */
  
  
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
  			
            }catch (CustomeFieldValidationException cfve) {           // de aca viene el error del UserServiceImpl para CAMPOS, si hay otro tipo de error se va por el otro catch
  				result.rejectValue(cfve.getFieldName(), null, cfve.getMessage());
                model.addAttribute("userForm", user);
  				model.addAttribute("formTab","active"); //con esto dejamos activos los campos en el forulario
                model.addAttribute("userList", userService.getAllUsers());
				model.addAttribute("roles",roleRepository.findAll());
  			} 
  			catch (Exception e) {                                        // de aca viene el error del UserServiceImpl
  				model.addAttribute("formErrorMessage",e.getMessage());      // con esto le pasamos los mensajes de error al HTML
  				model.addAttribute("userForm", user);
                model.addAttribute("formTab","active"); //con esto dejamos activos los campos en el forulario
                model.addAttribute("userList", userService.getAllUsers());
				model.addAttribute("roles",roleRepository.findAll());
  			}
  		}

  		model.addAttribute("userList", userService.getAllUsers());
  		model.addAttribute("roles",roleRepository.findAll());
  		return "user-form/user-view";
  	}
 
 
 
  //VIDEO 6 editar usuario
  
  // Con este metodo obtengo el usuario que quiero editar y su infomacion
  @GetMapping("/editUser/{id}")                                                      // de esta forma le decimos a spring que la url nos pasa un parametro. Fijate como lo hice en la ApiRest o en Mito
  public String getEditUserForm(Model model, @PathVariable(name ="id")Long id)throws Exception{    //el name del PathVariable es el name que viene del formulario
  		User userToEdit = userService.getUserById(id);

  		model.addAttribute("userForm", userToEdit);
  		model.addAttribute("userList", userService.getAllUsers());
  		model.addAttribute("roles",roleRepository.findAll());
  		model.addAttribute("formTab","active");
  		model.addAttribute("editMode","true");         //esto regula el comportamiento de la pagina, nos dice si es para editar o para guardar segun lo que venga del user-form
  		model.addAttribute("passwordForm",new ChangePasswordForm(userToEdit.getId()));
  		
  		return "user-form/user-view";
  	}
  
  
  
  // con este recibo y guardo la informacion editada del usuario seleccionado
  @PostMapping("/editUser")
	public String postEditUserForm(@Valid @ModelAttribute("userForm")User user, BindingResult result, ModelMap model) {
		if(result.hasErrors()) {
			model.addAttribute("userForm", user);
			model.addAttribute("formTab","active");
			model.addAttribute("editMode","true");
			model.addAttribute("passwordForm",new ChangePasswordForm(user.getId()));
		}else {
			try {
				userService.updateUser(user);                  //nuevo metodo para actualizar usuario
				model.addAttribute("userForm", new User());
				model.addAttribute("listTab","active");
			} catch (Exception e) {
				model.addAttribute("formErrorMessage",e.getMessage());
				model.addAttribute("userForm", user);
				model.addAttribute("formTab","active");
				model.addAttribute("userList", userService.getAllUsers());
				model.addAttribute("roles",roleRepository.findAll());
				model.addAttribute("editMode","true");
				model.addAttribute("passwordForm",new ChangePasswordForm(user.getId()));   //video 8 con esto llamo al form de edit pass
			}
		}

		model.addAttribute("userList", userService.getAllUsers());
		model.addAttribute("roles",roleRepository.findAll());
		return "user-form/user-view";

	}
  
  
  // boton de cancelar
    @GetMapping("/userForm/cancel")
   	public String cancelEditUser(ModelMap model) {
		return "redirect:/userForm";  //  hace un redirect al user-form 
	}
  
  
  // Video 7
  // Delete User
    @GetMapping("/deleteUser/{id}")
   	public String deleteUser(Model model, @PathVariable(name="id") Long id) {
   		try {
   			userService.deleteUser(id);
   		} catch (UsernameOrIdNotFound e) {
   			model.addAttribute("deleteError","El usuario no pudo ser borrado.");
   		}
   		
         return userForm(model); 
   	}  
    
    
    // video 8
    
    
    @PostMapping("/editUser/changePassword") //primero tiene que estar en modo editUser
	public ResponseEntity postEditUseChangePassword(@Valid @RequestBody ChangePasswordForm form, Errors errors) {
		
          try {
			if( errors.hasErrors()) {                               
				String result = errors.getAllErrors()
                        .stream().map(x -> x.getDefaultMessage())
                        .collect(Collectors.joining(""));

				throw new Exception(result);
			}
			userService.changePassword(form);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
		return ResponseEntity.ok("Success");
	}
 
  
    
    //video 12 metodo para sign up, me trae el Role USER que necesito
    
    
    @GetMapping("/signup")
    public String signUp(Model model) {
    	
    	 Role userRole= roleRepository.findByName("USER");
    	 List<Role> roles= Arrays.asList(userRole);// creo una lista con los roles para no modificar tanto el HTML, ya que lo tengo como lista
    	 model.addAttribute("userForm", new User());
    	 model.addAttribute("roles",roles);
    	 model.addAttribute("signup","true"); // esta es la bandera que estoy en modo signup
    	return "user-form/signup";
    }
    
    // crear usuario desde el signup
   
    @PostMapping("/signup")
	public String signupAction(@Valid @ModelAttribute("userForm")User user, BindingResult result, ModelMap model) {
		Role userRole = roleRepository.findByName("USER");
		List<Role> roles = Arrays.asList(userRole);
		model.addAttribute("userForm", user);
		model.addAttribute("roles",roles);
		model.addAttribute("signup",true);

		if(result.hasErrors()) {
			return "user-form/signup";
		}else {
			try {
				userService.createUser(user);
			} catch (CustomeFieldValidationException cfve) {
				result.rejectValue(cfve.getFieldName(), null, cfve.getMessage());
				return "user-form/signup";
			}catch (Exception e) {
				model.addAttribute("formErrorMessage",e.getMessage());
			}
		}
		return index();
	}

    
    
  
}