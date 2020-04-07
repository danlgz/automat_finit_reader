import java.io.*;

/*
	Utilice esta clase para guardar la informacion de su
	AFD. NO DEBE CAMBIAR LOS NOMBRES DE LA CLASE NI DE LOS 
	METODOS que ya existen, sin embargo, usted es libre de 
	agregar los campos y metodos que desee.
*/
public class AFD{
	AFDState states[];
	Character alphabet[];
	int statesFromFile[][];
	int totalStates;
	int finalState;
	/*
		Implemente el constructor de la clase AFD
		que recibe como argumento un string que 
		representa el path del archivo que contiene
		la informacion del afd (i.e. "Documentos/archivo.afd").
		Puede utilizar la estructura de datos que desee
	*/
	public AFD(String path) throws Exception{
		File file = null;
		FileReader fr = null;
		BufferedReader br = null;
		statesFromFile = new int[alphabet.length][totalStates];
		//Read File
		try {

			file = new File (path);
			fr = new FileReader (file);
			br = new BufferedReader(fr);

			String line = null;
			int lineNumber = 1;
			while((line=br.readLine())!=null){
				if (lineNumber == 1) this.alphabet = stringToCharList(line);
				else if (lineNumber == 2 ) this.totalStates = Integer.parseInt(line);
				else if (lineNumber == 3) this.finalState = Integer.parseInt(line);
				else this.statesFromFile[lineNumber - 4] = stringToIntList(line);

				lineNumber++;
			}
			
			this.generateStates();
			this.setAllStates();

		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if( null != fr ){   
				fr.close();     
			}  
		}

	}

	/*
		Implemente el metodo transition, que recibe de argumento
		un entero que representa el estado actual del AFD y un
		caracter que representa el simbolo a consumir, y devuelve 
		un entero que representa el siguiente estado
	*/
	public int getTransition(int currentState, char symbol) {
		if (currentState > this.states.length) return 0;
		return this.states[currentState].getNextState(symbol);
	}

	/*
		Implemente el metodo accept, que recibe como argumento
		un String que representa la cuerda a evaluar, y devuelve
		un boolean dependiendo de si la cuerda es aceptada o no 
		por el afd
	*/
	public boolean accept(String string){
		Character elements[] = this.stringToCharList(string);
		AFDState currentState = getState(1);

		for (int i = 0; i < elements.length; i++) {
			currentState = getState(
				getTransition(currentState.getStatePosition(), elements[i])
			);
		}

		return currentState.getStatePosition() == this.finalState;
	}

	/*
		El metodo main debe recibir como primer argumento el path
		donde se encuentra el archivo ".afd", como segundo argumento 
		una bandera ("-f" o "-i"). Si la bandera es "-f", debe recibir
		como tercer argumento el path del archivo con las cuerdas a 
		evaluar, y si es "-i", debe empezar a evaluar cuerdas ingresadas
		por el usuario una a una hasta leer una cuerda vacia (""), en cuyo
		caso debe terminar. Tiene la libertad de implementar este metodo
		de la forma que desee. 
	*/
	public static void main(String[] args) throws Exception{
		//Paramns
		String afd_path = args[0];
		String type = args[1];

		//Generate AFD
		AFD afd = new AFD(afd_path);

		BufferedReader sbr = null;
		String currentString = null;

		if(type.equals("-f")){
			//Param
			String strings_path = args[2];

			File Stringsfile = null;
			FileReader sfr = null;
			//Read File
			try {

				Stringsfile = new File (strings_path);
				sfr = new FileReader (Stringsfile);
				sbr = new BufferedReader(sfr);

				while((currentString=sbr.readLine())!=null){
					String message = afd.accept(currentString) ? "Cuerda aceptada" : "Cuerda no aceptada";
					System.out.println(message);
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				if( null != sfr ){   
					sfr.close();     
				}  
			}
		}else if(type.equals("-i")){
			InputStreamReader reader = new InputStreamReader(System.in);
			sbr = new BufferedReader(reader);
			while(true){
				System.out.println("Ingrese una cuerda: ");
				currentString = sbr.readLine();
				if(currentString != null || !currentString.equals("")){
					afd.accept(currentString);
				}else{
					System.exit(0);
				}
			}
		}else{
			System.exit(0);
		}



	}

	/* HELPERS */
	private void generateStates() {
		this.states = new AFDState[this.totalStates];

		for (int i = 0; i < this.totalStates; i++) {
			this.states[i] = new AFDState(i);
		}
	}

	private AFDState getState(int state) {
		return this.states[state];
	}

	private void setNextState(int state, char symbol, int goToState) {
		AFDState localState = this.getState(state);
		localState.setStage(symbol, goToState);
	}

	private void setAllStates() {
		for (int i = 0; i < this.alphabet.length; i ++) {
			// i fom alphabet is equal to j from statesFromFile
			char symbol = this.alphabet[i];
			for (int j = 0; j < this.statesFromFile.length; j ++) {
				for (int k = 0; k < this.statesFromFile[j].length; k++) {
					int goTo = this.statesFromFile[j][k];
					this.setNextState(k, symbol, goTo);
				}
			}
		}
	}

	private Character[] stringToCharList(String line) {
		String elements[] = line.split(",");
		Character result[] = new Character[elements.length];

		for (int i = 0; i < elements.length; i++) {
			String letter = elements[i];
			result[i] = letter.charAt(0);
		}

		return result;
	}

	private int[] stringToIntList(String line) {
		Character elements[] = this.stringToCharList(line);
		int result[] = new int[elements.length];

		for (int i = 0; i < elements.length; i++) {
			result[i] = (int) elements[i];
		}

		return result;
	}
}
