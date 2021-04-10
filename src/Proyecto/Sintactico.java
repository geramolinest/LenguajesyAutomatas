package Proyecto;

import java.util.ArrayList;
import java.util.Arrays;

public class Sintactico {
	//Token vacio=new Token(9,"","");//Por si esta vacio el token
	int iniciador;
	boolean error=false,errorS,banLlave=false,banPar=false;
	//Aquí se crea la tabla de símbolos(01)
	static  ArrayList<Identificador> TablaSimbolos;
	public Sintactico() {
		TablaSimbolos = new ArrayList<Identificador>();//Aqui guardo los identificadores
		iniciador=0;
		errorS=false;
		//Aqui primeramente checo si la sintaxis del termino del programa es una llave
		if(!Lexico.tokenAnalizados.get(Lexico.tokenAnalizados.size()-1).getValor().equals("}")) {
			Lexico.errores.add("Error al colocar la llave final del programa");
			return;
		}
		analisis();//Y mando analizar sintacticamente los token
		for(int i=0; i<Lexico.tokenAnalizados.size();i++) {
			if(Lexico.tokenAnalizados.get(i).getTipo().equals("Tipo de dato")) {
				if(i+1>Lexico.tokenAnalizados.size()-1) {
					Lexico.errores.add("Error sintactico declaracion de variable invalida");
					break;
				}
				if(Lexico.tokenAnalizados.get(i+1).getTipo().equals("Identificador")) {
					if(i+2>Lexico.tokenAnalizados.size()-1) {
						Lexico.errores.add("Error sintactico declaracion de variable invalida");
						break;
					}
					if(Lexico.tokenAnalizados.get(i+1).getValor().equals(";")) {
						Lexico.errores.add("Error sintactico declaracion de variable invalida");
						break;
					}
				}
			}
		}
		if(Lexico.errores.get(Lexico.errores.size()-1).equals("No hay errores lexicos"))//Si el ultimo token dice que no hay errores sintacticos
			Lexico.errores.add("No hay errores sintacticos");//Entonces no hay errores sintacticos
		for (Identificador identificador : TablaSimbolos) {
			if (identificador.getTipo().equals("")) {
				String x =buscar(identificador.getNombre());
				identificador.setTipo(x);
			}
			
			System.out.println(identificador);
		}
	}
	public void analisis() {
		int cant = Lexico.tokenAnalizados.size();
		while(iniciador<cant){
			if(Lexico.tokenAnalizados.get(iniciador).getTipo().equals("Modificador"))
			{
				if(!Lexico.tokenAnalizados.get(iniciador+1).getTipo().equals("Tipo de datos") && !Lexico.tokenAnalizados.get(iniciador+1).getTipo().equals("Clase"))
				{
					Lexico.errores.add("Error Sintactico se esperaba un identificador o clase.");
					errorS=true;
				}
				iniciador++;

				continue;
			}
			if(Lexico.tokenAnalizados.get(iniciador).getTipo().equals("Identificador"))
			{
				if(!Arrays.asList("{","=",";",")",">","<","<=",">=").contains(Lexico.tokenAnalizados.get(iniciador+1).getValor()))
				{
					Lexico.errores.add("Error Sintactico se esperaba un simbolo.");
					errorS=true;
				}
				if(Lexico.tokenAnalizados.get(iniciador-1).getTipo().equals("Clase"))
					TablaSimbolos.add(new Identificador("",Lexico.tokenAnalizados.get(iniciador).getValor(),"Clase",Lexico.tokenAnalizados.get(iniciador).getPosicion(),Lexico.tokenAnalizados.get(iniciador).getAlcance()));
				iniciador++;
				continue;
			}
			if(Lexico.tokenAnalizados.get(iniciador).getTipo().equals("Clase") || Lexico.tokenAnalizados.get(iniciador).getTipo().equals("Tipo de datos"))
			{
				if(!Lexico.tokenAnalizados.get(iniciador-1).getTipo().equals("Modificador") && Lexico.tokenAnalizados.get(iniciador).getTipo().equals("Tipo de datos"))
				{
					if(!Lexico.tokenAnalizados.get(iniciador+1).getTipo().equals("Identificador"))
					{
						Lexico.errores.add("Error Sintactico se esperaba un identificador.");
						errorS=true;
					}
				}
				else if(Lexico.tokenAnalizados.get(iniciador-1).getTipo().equals("Modificador"))
				{
					if(!Lexico.tokenAnalizados.get(iniciador+1).getTipo().equals("Identificador"))
					{
						Lexico.errores.add("Error Sintactico se esperaba un identificador.");
						errorS=true;
					}
				}
				else
				{
					Lexico.errores.add("Error Sintactico se esperaba un modificador.");
					errorS=true;
				}
				
				iniciador++;
				continue;
			}
			if(Lexico.tokenAnalizados.get(iniciador).getTipo().equals("Simbolo"))
			{
				if(Arrays.asList("{","}").contains(Lexico.tokenAnalizados.get(iniciador).getValor()))
				{
					if(cuentaLlaves())
					{
						banLlave = true;
						errorS=true;
					}
				}
				else if(Arrays.asList("(",")").contains(Lexico.tokenAnalizados.get(iniciador).getValor()))
				{
					if(cuentaParentesis())
					{						
						banPar = true;
						errorS=true;
					}
				}
				else if(Lexico.tokenAnalizados.get(iniciador).getValor().equals("=") || Lexico.tokenAnalizados.get(iniciador).getValor().equals("Identificador") )
				{
					if(Lexico.tokenAnalizados.get(iniciador-1).getTipo().equals("Identificador"))
					{
						if(Lexico.tokenAnalizados.get(iniciador+1).getTipo().equals("Constantes") || Lexico.tokenAnalizados.get(iniciador+1).getTipo().equals("Identificador")||Lexico.tokenAnalizados.get(iniciador+1).getTipo().equals("Simbolo"))
						{
							String a=Lexico.tokenAnalizados.get(iniciador-2).getValor(),iden=Lexico.tokenAnalizados.get(iniciador-1).getValor();
							if(!a.equals("int")&&!a.equals("double")&&!a.equals("String")&&!a.equals("boolean")) {
								a="Asignacion";
							}
							String valor="";
							iniciador++;
							while(!Lexico.tokenAnalizados.get(iniciador).getValor().equals(";")) {
								valor+=Lexico.tokenAnalizados.get(iniciador).getValor();
								iniciador++;
							}
							TablaSimbolos.add(new Identificador(iden,valor,a,Lexico.tokenAnalizados.get(iniciador-1).getPosicion(),Lexico.tokenAnalizados.get(iniciador-1).getAlcance()));
						}else {
							Lexico.errores.add("Error Sintactico se esperaba una constante o variable.");
							errorS=true;
						}
					}
					else
					{
						Lexico.errores.add("Error Sintactico se esperaba un identificador.");
						errorS=true;
					}
				}
				iniciador++;
				continue;
			}
			if(Lexico.tokenAnalizados.get(iniciador).getTipo().equals("Constantes"))
			{
				if(Lexico.tokenAnalizados.get(iniciador-1).getValor().equals("="))
				{
					if(!Lexico.tokenAnalizados.get(iniciador+1).getValor().equals(";"))
					{
						Lexico.errores.add("Error Sintactico asignacion invalida.");
						errorS=true;
					}
				}
				iniciador++;
				continue;
			}
			if(Lexico.tokenAnalizados.get(iniciador).getTipo().equals("Palabra Reservada"))
			{
				if(Lexico.tokenAnalizados.get(iniciador).getValor().equals("if"))
				{
					if(!Lexico.tokenAnalizados.get(iniciador+1).getValor().equals("("))
					{
						Lexico.errores.add("Error Sintactico se esperaba un (");
						errorS=true;
					}
				}
				iniciador++;
				continue;
			}
			if(Lexico.tokenAnalizados.get(iniciador).getTipo().equals("Operador Relacional"))
			{
				if(!Lexico.tokenAnalizados.get(iniciador-1).getTipo().equals("Constantes") && !Lexico.tokenAnalizados.get(iniciador-1).getTipo().equals("Identificador"))
				{
					Lexico.errores.add("Error Sintactico se esperaba una constante o identificador");
					errorS=true;
				}
				if(!Lexico.tokenAnalizados.get(iniciador+1).getTipo().equals("Constantes") && !Lexico.tokenAnalizados.get(iniciador-1).getTipo().equals("Identificador"))
				{
					Lexico.errores.add("Error Sintactico se esperaba una constante o identificador");
					errorS=true;
				}
				iniciador++;
				continue;
			}
		}
		if(banLlave)
			Lexico.errores.add("Error Sintactico falta de llave.");
		if(banPar)
			Lexico.errores.add("Error Sintactico falta de parentesis.");
	}
	public boolean cuentaParentesis(){
		int cant1=0,cant2=0;
		for(int i = 0;i<Lexico.tokenAnalizados.size();i++)
		{
			if(Lexico.tokenAnalizados.get(i).getValor().equals("("))
				cant1++;
			if(Lexico.tokenAnalizados.get(i).getValor().equals(")"))
				cant2++;
		}
		if(cant1==cant2)
			return false;
		return true;
	}
	public boolean cuentaLlaves(){
		int cant1=0,cant2=0;
		for(int i = 0;i<Lexico.tokenAnalizados.size();i++)
		{
			if(Lexico.tokenAnalizados.get(i).getValor().equals("{"))
				cant1++;
			if(Lexico.tokenAnalizados.get(i).getValor().equals("}"))
				cant2++;
		}
		if(cant1==cant2)
			return false;
		return true;
	}
	private String buscar(String id)//Busca si ya existe el identificador
	{
		for (int i = TablaSimbolos.size()-1; i >=0; i--) {
			Identificador identificador = TablaSimbolos.get(i);
			if(identificador.getNombre().equals(id))
				return identificador.getTipo();
		}
		return "";
	}
}
