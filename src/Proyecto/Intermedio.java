package Proyecto;

import java.util.*;

public class Intermedio {
	static ArrayList<String> indice = new ArrayList<String>();
	ArrayList<Identificador> aux;
	public Intermedio(String caracter,String ex,String nombre){
		Scanner read = new Scanner(System.in);
		aux=Sintactico.TablaSimbolos;
		String expresion = ex, expresion2="", x=ex, aux2="";
		int cont=0;
		
		for(int i=0;i<expresion.length();i++) {
			if(expresion.charAt(i)=='/' || expresion.charAt(i)=='*' || expresion.charAt(i)=='-' || expresion.charAt(i)=='+') {
				if(expresion.charAt(i+1)=='-') {
					cont++;
					expresion2 += expresion.charAt(i+1)+" "+tomarAdelante(expresion,i+1)+" "+"***"+"  T"+cont;
					String aux = expresion.charAt(i+1)+""+tomarAdelante(expresion,i+1);
					x = expresion.replace(aux, "T"+cont);
					indice.add(nombre+" "+expresion2);
					System.out.println("expresion 2:"+expresion2);
					System.out.println("aux:"+aux);
					System.out.println("x:"+x);
					expresion2="";
				}
			}
			expresion=x;
		}
		expresion=x;
	
			for(int i=0; i<expresion.length(); i++) {
				if(expresion.charAt(i)=='/' || expresion.charAt(i)=='*') {
					cont++;
					expresion2 += expresion.charAt(i)+" "+tomarAtras(expresion,i)+" "+tomarAdelante(expresion,i)+"  T"+cont;
					String aux = tomarAtras(expresion,i)+""+expresion.charAt(i)+""+tomarAdelante(expresion,i);
					x = expresion.replace(aux, "T"+cont);
					indice.add(nombre+" "+expresion2);
					expresion2="";
					System.out.println("expresion 2:"+expresion2);
					System.out.println("aux:"+aux);
					System.out.println("x:"+x);
					//tomarAdelante(expresion,i);
					//tomarAtras(expresion,i);
					//System.out.println(i+".- "+x);
				}
				
			}
			expresion=x;
			while(expresion.contains("+") || expresion.contains("-")) {
				for(int i=0; i<expresion.length(); i++) {
					if(expresion.charAt(i)=='+' || expresion.charAt(i)=='-'){
						cont++;
						expresion2 += expresion.charAt(i)+" "+tomarAtras(expresion,i)+" "+tomarAdelante(expresion,i)+"  T"+cont;
						String aux = tomarAtras(expresion,i)+""+expresion.charAt(i)+""+tomarAdelante(expresion,i);
						x = expresion.replace(aux, "T"+cont);
						indice.add(nombre+" "+expresion2);
						expresion2="";
						System.out.println("expresion 2:"+expresion2);
						System.out.println("aux:"+aux);
						System.out.println("x:"+x);
						break;
						//System.out.println("2.- "+x);
					}
				}
				expresion=x;
			}
//			indice.add("S/N "+"="+" "+"T"+cont+" "+"***"+" "+caracter);
			//new Cuadruplo();
			for(int i=0; i<expresion.length(); i++) {
				if(expresion.charAt(i) == '=') {
					cont++;
					expresion2 = expresion.charAt(i)+"  T"+cont+"     "+tomarAtras(expresion,i);
					String aux = tomarAtras(expresion,i)+""+expresion.charAt(i)+""+tomarAdelante(expresion,i);
					x = expresion.replace(aux, "T"+cont);
					indice.add(x);
				}
			}
			
			//System.out.println("3.- "+expresion2);
			//System.out.println("4.- "+expresion+"\n");
			String nom = "",operador = "",operand1 = "",operand2 = "",result = "";
			
			for(int i=0; i<indice.size(); i++) {
				System.out.println(" ");
				System.out.println("               Cuadruplo #"+ (i+1));
				System.out.println("               "+nombre +" = "+ ex);
				System.out.println(" ");
				StringTokenizer token= new StringTokenizer(indice.get(i));
				nom = token.nextToken();
				operador = token.nextToken();
				operand1 = RetornaValor(token.nextToken());
				operand2 = RetornaValor(token.nextToken());
				result = token.nextToken();	
				switch(RetornaTipo(nom))
				{
				case "int":
					switch(operador)
					{
					case "+":
						result = (Integer.parseInt(operand1)+Integer.parseInt(operand2))+"";
						break;
					case "-":
						result = (Integer.parseInt(operand1)-Integer.parseInt(operand2))+"";
						break;
					case "*":
						result = (Integer.parseInt(operand1)*Integer.parseInt(operand2))+"";
						break;
					case "/":
						result = (Integer.parseInt(operand1)/Integer.parseInt(operand2))+"";
						break;
					}
					
					break;
				case "double":
					switch(operador)
					{
					case "+":
						result = (Double.parseDouble(operand1)+Double.parseDouble(operand2))+"";
						break;
					case "-":
						result = (Double.parseDouble(operand1)-Double.parseDouble(operand2))+"";
						break;
					case "*":
						result = (Double.parseDouble(operand1)*Double.parseDouble(operand2))+"";
						break;
					case "/":
						result = (Double.parseDouble(operand1)/Double.parseDouble(operand2))+"";
						break;
					}
					break;
				}
				System.out.println("Operador"+" "+"Operand1"+" "+"Operand2"+" "+"Result");
				System.out.println("    "+operador+"       "+operand1+"      "+operand2+"        "+result);
				System.out.println("    "+nom + ":="+result);
			}
	}
	public String RetornaValor(String operando)
	{
		for(int i = 0; i<=aux.size()-1;i++)
		{
			if (operando.equals(aux.get(i).getNombre()))
			{
				return aux.get(i).getValor();
			}
		}
		return operando;
	}
	public String RetornaTipo(String nombre)
	{
		for(int i = 0; i<=aux.size()-1;i++)
		{
			if (nombre.equals(aux.get(i).getNombre()))
			{
				return aux.get(i).getTipo();
			}
		}
		return nombre;
	}
	// Toma todos los caracteres excepto los operadores
	public static String tomarAdelante(String expresion, int posicion) {
		String aux="";
		posicion++;
		for(int i=posicion; i<expresion.length(); i++) {
			if(expresion.charAt(i) != '/' && expresion.charAt(i) != '*' && expresion.charAt(i) != '+' && expresion.charAt(i) != '-') {
				aux+=expresion.charAt(i);
			}
			else
				break;
		}
		return aux;
	}
	
	public static String tomarAtras(String expresion, int posicion) {
		String aux="";
		posicion--;
		for(int i=posicion; i>=0; i--) {
			if(expresion.charAt(i) != '/' && expresion.charAt(i) != '*' && expresion.charAt(i) != '+' && expresion.charAt(i) != '-') {
				aux=expresion.charAt(i)+aux;
			}
			else
				break;
		}
		return aux;
	}
	
	public static void main(String[] args) {
		
	}
}
	