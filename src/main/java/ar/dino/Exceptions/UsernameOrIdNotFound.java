package ar.dino.Exceptions;

public class UsernameOrIdNotFound extends Exception{  

    // el ID nos recomienda meter una serilizacion generamos uno



    /**
	 * 
	 */
	private static final long serialVersionUID = -2721192143211720752L;

	public UsernameOrIdNotFound() {        //mensaje comun
		super("Usuario o Id no encontrado");
	}

	public UsernameOrIdNotFound(String message) {  //mensaje predeterminado.
		super(message);
	}
  
} 