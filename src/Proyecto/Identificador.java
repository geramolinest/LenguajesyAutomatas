package Proyecto;

public class Identificador {

	String nombre;
	String valor;
	String tipo;
	int posicion; //Aquí agregué la posición para la tabla de símbolos
	String alcance; //Aquí agregué el alcance para la tabla de símbolos
		
	public Identificador(String nombre, String valor, String tipo,int pos, String alc) {
		this.nombre = nombre;
		this.valor = valor;
		this.tipo = tipo;
		this.posicion=pos;
		this.alcance=alc;
	}
	public Identificador(String valor, String tipo) {
		this.valor = valor;
		this.tipo = tipo;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getValor() {
		return valor;
	}
	public void setValor(String valor) {
		this.valor = valor;
	}
	
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	
	public int getPosicion() {
		return posicion;
	}
	public void setPosicion(int posicion) {
		this.posicion = posicion;
	}
	public String getAlcance() {
		return alcance;
	}
	public void setAlcance(String alcance) {
		this.alcance = alcance;
	}
	public String toString() {
		return "Identificador [nombre=" + nombre + ", valor=" + valor + ", tipo=" + tipo + ", posición=" + posicion + ", alcance = " + alcance + "]";
	}
}