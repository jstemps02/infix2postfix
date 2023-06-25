/**
 *  
 * @author Justin Templeton
 *
 **/

/**
 * 
 * this class takes a file input input.txt, which is comprised of infix expressions with spaces inbetween all separate entities
 * processes them and puts them into postfix, and then uses a PrinterWriter to write to an output file output.txt
 *
 */
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.Stack;

public class Infix2Postfix {

	
	/**
	 * read a line of input from the file and pass it into the process method
	 * then it is written to a file called output.txt 
	 * 
	 * @param args
	 **/
	public static void main(String[] args) throws FileNotFoundException{
		File in = new File("input.txt");
		Scanner fileScanner = new Scanner(in);
		fileCreator(); //creates the file output.txt
		PrintWriter w = null;
		try {
			w = new PrintWriter("output.txt"); //opens the file in the file writer
		}
		catch(FileNotFoundException e) {
			e.printStackTrace(); //catch the error, print the stack
		}
		
		while(fileScanner.hasNextLine()) {
			String line = fileScanner.nextLine();
			String l = process(line);
			if(fileScanner.hasNextLine()) {
				w.println(l); //writes the string if it needs a newline
			}
			else {
				w.print(l); //writes the string if it does not need a newline to conform with the specification
			}
		}
		fileScanner.close();
		w.close(); //closes the PrintWriter
	}
	/**
	 *Splits up the input string by the .split() method, using space as the key of where to split
	 *in which a for each loop is used to go through the new String array and convert the infix 
	 *equation to postfix, errors are also caught here
	 * 
	 * @param in the lines of the file that is passed in
	 * @return A string either comprised of the correct postfix expression or the correct error statement and the corresponding notation
	 **/
	private static String process(String in) {
		int tooManyOperators = 0;
		int tooManyOperands = 0;
		String post = "";
		Stack<Character> s = new Stack<>();	
		String[] inv = in.split(" ");
				
		for (String token : inv) { //for each index of the String array
			try { //try if it is a number
				Integer.parseInt(token); 
				post = post + " " + token; //if is a number, add it to the string
				
				if(tooManyOperands >= 1) { //if there are >= 1 operands in a row, throw an error
					return "Error: too many operands (" + token + ")";
				}
				tooManyOperands++; //increases count of the operands
				tooManyOperators = 0; //resets count of the operators
			}
			catch(NumberFormatException e){ //if it is not a number, then it is a operator
				
				char[] operator = token.toCharArray(); //uses the toCharArray method to go from String to char
				char op = operator[0]; //stores the char array's first index into op
				int prec = precedence(op); //gets the precedence of op
				
				
				if(op == '('){
					s.push(op); //handles opening parentheses by pushing it to the stack
				}
				
				else if(op == ')') {
					if(s.peek() == '(' && tooManyOperands != 1) { // if it is not a closing parentheses and there is not an operand in the middle
						return "Error: no subexpression detected ()"; //throw the error
					}
					while(!s.empty() && s.peek() != '('){ //check the stack for any operators inbetween the parentheses
						post = post + " " + s.pop(); //pop them to the string
						
					}
					if(s.isEmpty()) { //detect if there is no opening parentheses
						return "Error: no opening parenthesis detected";
					}
					
					s.pop(); //pop the opening parentheses
				}
				
				else if(s.isEmpty()) { //handles the first operand if it is not parentheses
					s.push(op); 
					tooManyOperators++; //everything but parentheses count, and since those are already caught, it must be a operator
					tooManyOperands = 0; //resets the operand count

				}
				
				else {					//if it is an operator that is not parentheses and the stack is not empty
					tooManyOperators++; //count up since its an operator
					tooManyOperands = 0; //reset the operand counter
					if(prec > precedence(s.peek())){ 
						s.push(op);		//if op is of higher precedence, push op on the stack
					}
					else { 	
						while(!s.isEmpty() && prec <= precedence(s.peek())){ //making sure the stack has items and precedence is maintained
							post = post + " " + s.pop(); //pops of the top of the stack
						}
						s.push(op); //pushes the operator on
					}
				}
			}
			if(tooManyOperators > 1) { //catches if there are too many operators
				return "Error: too many operators (" + token + ")";
			}
		}
		//end of for-each loop
		while(!s.isEmpty()) {  //while the stack is not empty, pop the contents off and put them on the end
			char spop = s.pop();
			if(spop == '(') { // check if there is a parenthesis error
				return "Error: no closing parenthesis detected";
			}
			post = post + " " + spop; //append the data to the string
			
		}
		
		return post.substring(1); //return the substring without the extra space
		
	}
	/**
	 * 
	 * Based on stack precedence from the PowerPoint 24 infix2postfix-1.pptx
	 * This method returns an integer for the input corresponding to its precedence
	 * 
	 * @param in  the character of the operator that requires the precedence to be returned
	 * @return the corresponding number to the character passed into the method
	 **/
	private static int precedence(char in) {
		if(in == '(') { //from powerpoint, could also be zero and still work
			return -1; //Pemdas --- -1 is handled by the parse method
		}
		
		if(in == '^') {
			return 3; //pEmdas
		}
		
		if(in == '*' || in == '/' || in == '%') {
			return 2; //peMDas
		}
		
		if(in == '+' || in == '-') {
			return 1; //pemdAS
		}
		
		if(in == '(') {
			return -1; //same as opening
		}
		
		else {
			return 0; //base case, should never happen
		}
	}
	
	/**
	 * This method creates a file called 
	 * output.txt using the .createNewFile method
	 * 
	 **/
	public static void fileCreator() {
		File f = new File("output.txt");
		try {
			f.createNewFile(); //creates the file output
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
}
