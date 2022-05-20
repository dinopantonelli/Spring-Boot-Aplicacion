package ar.dino.Exceptions;

public class UsernameOrIdNotFound extends Exception{  

    // el ID nos recomienda meter una serilizacion generamos uno



    public UsernameOrIdNotFound() {        //mensaje comun
		super("Usuario o Id no encontrado");
	}

	public UsernameOrIdNotFound(String message) {  //mensaje predeterminado.
		super(message);
	}
  
} 