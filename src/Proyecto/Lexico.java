package Proyecto;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;

public class Lexico
{
	int renglon=1, columna=0;//Para señalar donde fue el error
	static ArrayList<Token> tokenAnalizados;//Guardo los token analizados
	static ArrayList<String> errores;//Guardo los token analizados
	String aux="";
	String alcance = "";
	int modificadores = 0;
	boolean ban=false;
	public Lexico(String nom) {//Recibe el nombre del archivo de texto
		procesoAnalisis(nom);
	}
	public void procesoAnalisis(String nom) {
		String linea="", aux="";//Una es para sacar el renglon y el otro es para saber que palabra es
		StringTokenizer tokenizer;//Para cortar las palabras y poder analizarlas
		try{
	          FileReader file = new FileReader(nom);//Aqui abro el flujo del archivo
	          BufferedReader archivoEntrada = new BufferedReader(file);
	          linea = archivoEntrada.readLine();//Leo la primera linea
	          tokenAnalizados=new ArrayList<Token>();
	          errores=new ArrayList<String>();
	          while (linea != null){//Hago esto para recorrer todas las lineas del archivo
	        	    columna=0;//La inicializo otra vez por que cambia de renglon
	        	    linea = colocaEspacios(linea);//Checo si en la linea hay operadores o identificadores combinados y le agrego espacios
	                tokenizer = new StringTokenizer(linea);//Utilizo para partir el renglon que ya tengo
	                int cont=tokenizer.countTokens();//Cuento las palabras que analizare
	                for(int i=0; i<cont; i++) {//Recorro las columnas
	                columna++;//Para checar en que columna se produce el error
	                aux = tokenizer.nextToken();//Extraigo la palabra
	                checarToken(aux,renglon);
	                }
	                linea=archivoEntrada.readLine();//Leo las lineas siguientes
	                renglon++;
	          }
	          if(errores.size()==0)
	        	  errores.add("No hay errores lexicos");
	          archivoEntrada.close();
		}catch(IOException e) {
			JOptionPane.showMessageDialog(null,"No se encontro el archivo favor de checar la ruta","Alerta",JOptionPane.ERROR_MESSAGE);//Mando un mensaje por si no encuentro el archivo
		}
	}
	//Resolvi el problema con recorrer caracter por caracter el pedo esta en los identificadores
	public void checarToken(String token, int renglon) {
		//Hice un metodo para analizar cada uno
		if(esModificador(token,renglon))
			return;
		if(esPalabraReservada(token,renglon))
			return;
		if(esTipoDato(token,renglon))
			return;
		if(esSimbolo(token,renglon))
			return;
		if(esOperadorRelacional(token,renglon))
			return;
		if(esOperadorAritmetico(token,renglon))
			return;
		if(esCadena(token,renglon))
			return;
		if(esValor(token,renglon))
			return;
		if(esClase(token,renglon))
			return;
		Pattern pat = Pattern.compile("^[a-zA-Z0-9]+$");//Checo si cumple con esta expresion regular
		Matcher mat = pat.matcher(token);
		if(mat.find()) {//Si la expresion cumple con las reglas del identificador
			if(modificadores == 1 && !tokenAnalizados.get(renglon).getTipo().equals("Clase"))
			{
				alcance = "Global";
			}
			else
			{	
				if (tokenAnalizados.get(renglon).getTipo().equals("Clase"))
				{
					alcance= "";
				}
				else
				{
					alcance = "Local";
				}				
			}
			tokenAnalizados.add(new Token("Identificador",token,renglon,alcance));//Guardo el token analizado
		}else {
			errores.add("Error token no valido: Renglon "+renglon+" Columna "+columna+" "+token);//Mando un error de token en que renglon fue ocasionado y columna
		}
	}
	public boolean esCadena(String token,int renglon) {
		//Como partimos el token por espacios al extraer la informacion del archivo el valor estara separado
		//Por lo que lo pegamos
		if(Pattern.matches("^['][A-Za-z0-9]+[']$",token)) {
			tokenAnalizados.add(new Token("Constantes",token,renglon,""));//Guardo el token analizado
			return true;
		}
		if(Pattern.matches("^['][A-Za-z0-9]+$",token)) {
			ban=true;
			aux+=token;
			return true;
		}
		if(Pattern.matches("^[A-Za-z0-9]+$",token) && ban) {
			aux+=" "+token;
			return true;
		}
		if(Pattern.matches("^[A-Za-z0-9]+[']$",token)) {
			aux+=" "+token;
			tokenAnalizados.add(new Token("Constantes",aux,renglon,""));//Guardo el token analizado
			aux="";
			ban=false;
			return true;
		}
		return false;
	}
	public boolean esValor(String token,int renglon) {
		if(Arrays.asList("true","false").contains(token)) {
			tokenAnalizados.add(new Token("Constantes",token,renglon,""));//Guardo el token analizado
			return true;
		}
		if(Pattern.matches("^(\\d+)$",token)||Pattern.matches("(^[0-9]+([.][0-9]+)?$)",token)) {
			tokenAnalizados.add(new Token("Constantes",token,renglon,alcance));//Guardo el token analizado
			return true;
		}
		return false;
	}
	public boolean esModificador(String token,int renglon) {
		if(token.equals("public")||token.equals("private")||token.equals("protected")) {
			tokenAnalizados.add(new Token("Modificador",token,renglon,""));//Guardo el token analizado
			modificadores ++;
			return true;
		}else 
			return false;
	}
	public boolean esPalabraReservada(String token,int renglon) {
		if(token.equals("else")||token.equals("if")) {
			tokenAnalizados.add(new Token("Palabra Reservada",token,renglon,""));//Guardo el token analizado
			return true;
		}else 
			return false;
	}
	public boolean esTipoDato(String token, int renglon) {
		if(token.equals("int")||token.equals("double")||token.equals("String")||token.equals("boolean")) {
			tokenAnalizados.add(new Token("Tipo de datos",token,renglon,alcance));//Guardo el token analizado
			return true;
		}else
			return false;
	}
	public boolean esSimbolo(String token, int renglon) {
		if(token.equals("(")||token.equals(")")||token.equals("{")||token.equals("}")||token.equals(";")||token.equals("=")) {
			tokenAnalizados.add(new Token("Simbolo",token,renglon,""));//Guardo el token analizado
			return true;
		}else
			return false;
	}
	public boolean esOperadorRelacional(String token,int renglon) {
		if(token.equals("<")||token.equals("<=")||token.equals(">=")||token.equals("==")||token.equals("!")||token.equals(">")) {
			tokenAnalizados.add(new Token("Operador Relacional",token,renglon,""));//Guardo el token analizado
			return true;
		}else
			return false;
	}
	public boolean esOperadorAritmetico(String token, int renglon) {
		if(token.equals("+")||token.equals("-")||token.equals("*")||token.equals("/")) {
			tokenAnalizados.add(new Token("Operador Aritmetico",token,renglon,""));//Guardo el token analizado
			return true;
		}else
			return false;
	}
	public boolean esClase(String token, int renglon) {
		if(token.equals("class")) {
			tokenAnalizados.add(new Token("Clase",token,renglon,"Global"));//Guardo el token analizado
			return true;
		}else
			return false;
	}
	public static String colocaEspacios(String linea){
		for (String string : Arrays.asList("(",")","{","}","=",";","*","+","/","-")) {
			if(string.equals("=")) {
				//Si en medio de los parentesis hay este operador doy espaciado para que los tome y los identifique
				if(linea.indexOf(">=")>=0) {
					linea = linea.replace(">=", " >= ");
					break;
				}
				if(linea.indexOf("<=")>=0) {
					linea = linea.replace("<=", " <= ");
					break;
				}
				if(linea.indexOf("==")>=0){
					linea = linea.replace("==", " == ");
					break;
				}
				if(linea.indexOf("<")>=0) {
					linea = linea.replace("<", " < ");
					break;
				}
				if(linea.indexOf(">")>=0) {
					linea = linea.replace(">", " > ");
					break;
				}
			}
			if(linea.contains(string)) //Uso el for each por si trae los simbolo ya declarados entonces hago que se añadan espacios
				linea = linea.replace(string, " "+string+" ");
		}
		return linea;
	}
}
