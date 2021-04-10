package Proyecto;

public class Token 
{
	private String tipo;
	private String valor;
	private int NumTipo;
	private int posicion; 
	private String alcance; //Aquí agregué el alcance, para utilizarlo en la tabla de símbolos.

	public Token(int Num,String t, String v, int pos,String alc) {
		tipo=t;
		valor=v;
		NumTipo=Num;
		posicion=pos;
		alcance = alc;
	}
	public Token(String t, String v, int pos,String alc) {
		tipo=t;
		valor=v;
		posicion=pos;
		alcance = alc;
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	public String getValor() {
		return valor;
	}
	public int getNumTipo() {
		return NumTipo;
	}
	public void setNumTipo(int numTipo) {
		NumTipo = numTipo;
	}
	public void setValor(String valor) {
		this.valor = valor;
	}
	public int getPosicion() {
		return posicion;
	}
	public void setPosicion(int posicion) {
		this.posicion = posicion;
	}
	public String toString() {
		return getTipo()+" --> "+getValor();
	}
	public String getAlcance() {
		return alcance;
	}
	public void setAlcance(String alcance) {
		this.alcance = alcance;
	}
}
