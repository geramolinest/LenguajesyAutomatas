package Proyecto;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

public class Semantico {
	ArrayList<Identificador> aux;//Hago visible la tabla de identificadores del sintactico a traves de este arraylist
	ArrayList<Identificador> asignaciones;//Creo un arraylist para las asignaciones
	static ArrayList<Identificador> declaraciones;//Y otro para las declaraciones
	public Semantico() {
		//Inicializo los arreglos
		aux=Sintactico.TablaSimbolos;//Hago visible la tabla de identificadores del sintactico
		declaraciones= new ArrayList<Identificador>();
		asignaciones= new ArrayList<Identificador>();
		filtrar();//Mando a filtrar los identificadores asi como tambien asignaciones
		checaDeclaraciones();//Mando a checar las declaraciones
		checaAsignaciones();//Mando a checar las asignaciones
		if(Lexico.errores.size()==2)
			Lexico.errores.add("No hay errores semanticos");
		CambiaValores();
	}
	public void filtrar() {//Separo en los dos arraylist lo que son asignaciones y declaraciones de variables
		for(int i=0; i<aux.size(); i++) {
			if(aux.get(i).tipo.equals("Asignacion")) {
				asignaciones.add(aux.get(i));
			}else {
				declaraciones.add(aux.get(i));
			}
		}
	}
	public void CambiaValores() {
		for(int i=0; i<Sintactico.TablaSimbolos.size(); i++) {
			String linea=Sintactico.TablaSimbolos.get(i).getValor(),tipo=Sintactico.TablaSimbolos.get(i).getTipo();
			if(tipo.equals("int")||tipo.equals("double")) {
				
			}
		}
	}
	public void checaDeclaraciones() {//Primero todas las declaraciones de variables
		for(int i=0; i<declaraciones.size(); i++) {
			checaValor(declaraciones.get(i));//Y mando a checar el valor
			int contador=0;//Voy a necesitar un contador por que se puede dar el caso que una variable se repita
			for(int k=0; k<declaraciones.size(); k++) {//Recorro de nuevo las variables
				if(declaraciones.get(i).getNombre().equals(declaraciones.get(k).getNombre()))//Cuento cuantas veces aparece
					contador++;
			}
			if(contador==2)//Si aparece dos veces quiere decir que esta repetida
				Lexico.errores.add("Error nombre de variable duplicada "+declaraciones.get(i).getNombre());
			if (contador == 0)
				Lexico.errores.add("Error nombre de variable no declarada "+declaraciones.get(i).getNombre());
		}
	}
	//Aquí válido las asignaciones, variables usadas y no declaradas, a su vez si las variables ya fueron declaradas
	//De igual manera, aquí mismo se validan los operandos
	public void checaValor(Identificador iden) {//Recibo la variable o identificador
		String separado="";
		StringTokenizer tokenizer;
		int cont=0;
		switch(iden.getTipo()) {//Y ingreso el tipo de dato de la variable para filtrar su valor
		case "int":
			separado=Lexico.colocaEspacios(iden.getValor());
			tokenizer= new StringTokenizer(separado);//Extraigo el valor de la variable
			cont=tokenizer.countTokens();
			for(int i=0; i<cont; i++) {
				String token=tokenizer.nextToken();
		    	if(!Pattern.matches("^(\\d+)$",token)&&!Arrays.asList("(",")","+","*","/","-").contains(token)) {
		    		//Sino contiene nada de estos caracteres quiere decir que es una variable por lo que procedemos a checar su tipo
		    		int pos=getposicion(token);
		    		if(pos==-1) {//En caso de que no encuentre la variable quiere decir que no esta declarada y mando un mensaje
						Lexico.errores.add("Error la variable "+token+" no se encuentra declarada o el valor no es entero");
					}else {//Quiere decir que si encontre la posicion por lo que hago un identificador temporal
					if (asignaciones.size() >0)
					{
						Identificador temp = new Identificador(asignaciones.get(i).getValor(),declaraciones.get(pos).getTipo());
						if(!iden.getTipo().equals(temp.getTipo()))
							Lexico.errores.add("Error de asignacion, el dato no es entero");
						}
					}	
					
		    	}
		    }
			break;
		case "double":
			separado=Lexico.colocaEspacios(iden.getValor());
			tokenizer= new StringTokenizer(separado);//Extraigo el valor de la variable
			cont=tokenizer.countTokens();
			for(int i=0; i<cont; i++) {
				String token=tokenizer.nextToken();
		    	if(!Pattern.matches("^(\\d+)$",token)&&!Pattern.matches("(^[0-9]+([.][0-9]+)?$)",token)&&!Arrays.asList("(",")","+","*","/","-").contains(token)) {
		    		//Sino contiene nada de estos caracteres quiere decir que es una variable por lo que procedemos a checar su tipo
		    		int pos=getposicion(token);
		    		if(pos==-1) {//En caso de que no encuentre la variable quiere decir que no esta declarada y mando un mensaje
						Lexico.errores.add("Error la variable "+token+" no se encuentra declarada o el valor no es decimal");
					}else {//Quiere decir que si encontre la posicion por lo que hago un identificador temporal
					Identificador temp = new Identificador(asignaciones.get(i).getValor(),declaraciones.get(pos).getTipo());
					if(!iden.getTipo().equals(temp.getTipo()))//Si el tipo de dato no es igual en las dos variables mando el mensaje de error
						Lexico.errores.add("Error de asignacion, el dato no es decimal");
					}
		    	}
		    }
			break;
		case "String":
			if(!Pattern.matches("^['][a-zA-Z0-9\\s]+[']$",iden.getValor())) //Con esta expresion checo si es alfanumerico
				Lexico.errores.add("Error de asignacion, el dato no es un string");
			break;
		case "boolean":
			if(!Arrays.asList("true","false").contains(iden.getValor()))//Con esta expresion checo si es boolean
				Lexico.errores.add("Error de asignacion, se esperaba un dato true o false");
			break;
		case "":
			Lexico.errores.add("La variable no se encuentra declarada");
			break;
		}
	}
	public int getposicion(String nom) {
		for(int i=0; i<declaraciones.size(); i++) {
			if(declaraciones.get(i).getNombre().equals(nom)) {
				return i;
			}
		}
		return -1;
	}
	public void checaAsignaciones() {
		for(int i=0; i<asignaciones.size(); i++) {
			//Primero extraigo la posicion de la declaracion de la variable a la que le voy asignar el valor
			int sub=getposicion(asignaciones.get(i).getNombre());
			if(sub==-1) {//En caso de que no encuentre la variable quiere decir que no esta declarada y mando un mensaje
				Lexico.errores.add("Error la variable no se encuentra declarada");
			}else {//Quiere decir que si encontre la posicion por lo que hago un identificador temporal
			Identificador temp = new Identificador(asignaciones.get(i).getValor(),declaraciones.get(sub).getTipo());
			checaValor(temp);//Y mando a checar el valor
			}
		}		
	}
	
	
}