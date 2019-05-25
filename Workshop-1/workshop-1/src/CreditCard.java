import java.util.Scanner;

public class CreditCard {

	public static void main(String[] args) {

	System.out.println("***** CREDIT CARD VALIDATION *****" + "\n");	
	
	Scanner scan = new Scanner(System.in);
	
	// declare variables
	String userInput;
	int digitNumb;
	long cardNumb = 0L;
	boolean validInput = false;
	
	do {
		System.out.print("Please enter the CreditCard number: ");
		userInput=scan.nextLine();
		
		// users enters a number
		if(isLong(userInput)) 
		{
			cardNumb = Long.parseLong(userInput);
			
			if(cardNumb>=1000000000000L && cardNumb<=9999999999999999L)				// checks, if number has 13-16-digits
			{					
				digitNumb = getSize(cardNumb);										// gets the number of digits				
				
				if(prefixMatched(cardNumb, digitNumb))								// checks, if cardType/prefix matches 
				{
					if (isValid(cardNumb)) {										// checks, if cardNumber is valid
						System.out.println(cardNumb + " is valid");
					}
					else {
						System.out.println(cardNumb + " is invalid");
					}
					validInput = true;												// exits the loop
				}
				else {
					System.out.print(">>> CardNumber must start with:" + "\n" +
									"    4  for Visa cards" + "\n" +
									"    5  for MasterCards" + "\n" +
									"    37 for American Express cards" +"\n" +
									"    6  for Discover cards" +"\n");
				}				
			}
			else {
				System.out.println(">>> CardNumber must have between 13-16 digits.");
			}
		}
		else {			
			System.out.println(">>> Wrong Input!");									// when input is NOT a number
		}		
	}
	while(!validInput);
	
	scan.close();																	// to escape from Resource leak, Java compiler hints
		
		// end of main scope
	}
	
	
	//*****************************************************************
	// isValid: checks if the cardNumber is valid
	//*****************************************************************
	public static boolean isValid(long cardNumb) {
		// declare local variables
		boolean isValid = false;
		int sumOdd, sumEven, sumTotal;
		
		sumOdd = sumOfOddPlace(cardNumb);							// calculates sum of odd-place digits
		sumEven = sumOfDoubleEvenPlace(cardNumb);					// calculates sum of even-place digits
		sumTotal = sumOdd + sumEven;								// calculates sumTotal
		
		// check if cardNumb is valid
		if(sumTotal % 10 == 0) {
			isValid = true;
		}	
		return isValid;
	}
	
	
	//*****************************************************************
	// isLong:  checks if user enters numbers
	//*****************************************************************
	public static boolean isLong(String input) {
		try {
			Long.parseLong(input);
			return true;
		}
		catch(Exception e){
			return false;
		}
	}
	
	//*****************************************************************
	// getSize: - returns the number of digits on inputValue
	//*****************************************************************
	public static int getSize(long cardNumb) {
		int digitNumb;
		
		// checks for the length of cardNumber 
		if(cardNumb<=9999999999999L) {
			digitNumb = 13;
		}
		else if(cardNumb<=99999999999999L) {
			digitNumb = 14;
		}
		else if(cardNumb<=999999999999999L) {
			digitNumb = 15;
		}
		else {
			digitNumb = 16;
		}
		return digitNumb;
	}
	
	//*****************************************************************
	// prefixMatched: checks if the prefix matches
	//*****************************************************************
	public static boolean prefixMatched(long number, int d) {
		// declare variables
		boolean matches;
		long div = (long)(Math.pow(10, (d-1)));		// e.g. for 16-digit-number divider = 1,000,000,000,000,000
		long div2 = (long)(Math.pow(10, (d-2)));	// e.g. in case if number has prefix 3, for 16-digit-number,  divider2 =  100,000,000,000,000
		
		int prefix = (int)(number / div);			// gives the 1st-digit number
		
		if (prefix == 3)							// prefix matches: checks for prefix 3 
		{
			int prefix2 = (int)(number / div2);
			prefix2 = prefix2 - prefix*10;
			
			if(prefix2 == 7)						// checks for prefix2 / for 7
				matches=true;
			else
				matches=false;
		}
		else if(prefix >=4 && prefix <=6) {			// prefix matches: checks for prefixes 4,5,6
			matches=true;
		}
		else										// prefix doesn't match
			matches=false;
		
		return matches;
	}
	
	
	//*****************************************************************
	// sumOfOddPlace: returns sum of odd-place digits
	//*****************************************************************
	public static int sumOfOddPlace(long cardNumb) {
		// declare variables
		int sumOdd = 0;
		int digitSize = getSize(cardNumb);
		int oddPlace;
		long tempCardNumb;

		// loop through the cardNumber to separate odd-place digits
		for(int i=digitSize; i>0; i=i-2) {
			tempCardNumb = getPrefix(cardNumb,i);					// get first i number of digits from cardNumb
			oddPlace = (int)(tempCardNumb % 10);					// get odd-place digit number
			sumOdd = sumOdd + oddPlace;								// calculate total for sumOdd
		}	
		return sumOdd;
	}
	
	
	//*****************************************************************
	// sumOfDoubleEvenPlace: returns sum of even-place digits
	//*****************************************************************
	public static int sumOfDoubleEvenPlace(long cardNumb) {
		//declare variables
		int sumEven = 0;
		int digitSize = getSize(cardNumb);
		int evenPlace;
		long tempCardNumb;
		
		// loop through the cardNumber to separate even-place digits
		for(int i=(digitSize-1); i>0; i=i-2) {
			tempCardNumb = getPrefix(cardNumb, i);					// get first i number of digits from cardNumb
			evenPlace = (int)(tempCardNumb % 10);					// get even-place digit number
			evenPlace = evenPlace *2;								// double each even-place digit number
			evenPlace = getDigit(evenPlace);						// make single-digit-number
			sumEven = sumEven + evenPlace;							// calculate total for sumEven
		}	
		return sumEven;
	}		
	
	
	//*****************************************************************
	// getPrefix: returns first k number of digits
	//*****************************************************************
	public static long getPrefix(long number, int k) {
		// declare local variable
		long prefixLong;
		
		int postfix = getSize(number) - k;							// gets length of 1st k digits for returning
		long div = (long)Math.pow(10, postfix);						// gets value of the divider
		
		// gets number with the length of k-digits
		prefixLong = number / div;									// e.g: number.length = 16, k = 14, prefixLong.length = 14
		
		return prefixLong;
	}
	
	
	//*****************************************************************
	// getDigit: returns single-digit-number for the doubles even-place digits
	//*****************************************************************
	public static int getDigit(int number) {
		// declare variable
		int digit1, digit2;
		
		// check for 2-digit number
		if(number>=10) {
			digit1 = number/10;
			digit2 = number %10;
			number = digit1 + digit2;
		}
		return number;
	}
		
	
	// end of CredieCard class scope
}
