package ar.dino.Exceptions;

public class CustomeFieldValidationException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7277928118248873533L;
	/**
	 * 
	 */
	
	private String fieldName;  //nombre del campo a validar

	public CustomeFieldValidationException(String message, String fieldName) { //recibimos el mensaje y el nombre del campo
		super(message); //le mandamos al constructor de la clase padre de la que estamos heredando le mandamos el mensaje 
		this.fieldName = fieldName;
	}

	public String getFieldName() {
		return this.fieldName;
	}

// me aseguro que el fieldname tenga el valor a traves del constructor y tambien que esta Excepcion se controle a traves de los dos valores o sea mandando los dos

}
