// TCP Server2 class that
// receives response and sends data

import java.io.*;
import java.net.*;
import java.util.Hashtable;

class Server2 {

	// Get Method Condition Check
	public static String[] Get(String str,int invalid){
		String key = "";
		int flag = 0;
		for(int i = 3; i < str.length(); i++){
			if(str.charAt(i) == ' ' && flag == 0){
				continue;
			}
			else if(Character.isDigit(str.charAt(i))){
				key += str.charAt(i); 
				flag = 1;
			}
			else if(flag == 1 && !Character.isDigit(str.charAt(i))){
				invalid = 1;
				break;
			}else if(flag == 0 && !Character.isDigit(str.charAt(i))){
				invalid = 1;
				break;
			}
		}
		String p[] = new String[2];
		p[0] = key;
		p[1] = Integer.toString(invalid);

		return p;
	}

	// Put Method Condition Check
	public static String[] put(String str,int invalid){
		String key = "";
		int flag = 0;
		int index = 0;
		for(int i = 3; i < str.length(); i++){
			if(str.charAt(i) == ' ' && flag == 0){
				continue;
			}
			else if(Character.isDigit(str.charAt(i))){
				key += str.charAt(i); 
				flag = 1;
			}
			else if(flag == 1 && str.charAt(i) == ' '){
				index = i;
				break;
			}else{
				invalid = 1;
				break;
			}
		}
		if(flag == 0){
			invalid = 1;
		}
		String p[] = new String[4];
		p[0] = key;
		p[1] = Integer.toString(invalid);
		p[2] = Integer.toString(flag);
		p[3] = Integer.toString(index);

		return p;
	}	

	// Delete Method Condition Check
	public static String[] Delete(String str,int invalid){
		String key = "";
		int flag = 0;
		for(int i = 6; i < str.length(); i++){
			if(str.charAt(i) == ' ' && flag == 0){
				continue;
			}
			else if(Character.isDigit(str.charAt(i))){
				key += str.charAt(i); 
				flag = 1;
			}
			else if(flag == 1 && !Character.isDigit(str.charAt(i))){
				invalid = 1;
				break;
			}else if(flag == 0 && !Character.isDigit(str.charAt(i))){
				invalid = 1;
				break;
			}
		}
		String p[] = new String[2];
		p[0] = key;
		p[1] = Integer.toString(invalid);

		return p;
	}

	// Main function //
	public static void main(String args[])
		throws Exception
	{

		// Create server Socket
		ServerSocket ss = new ServerSocket(23); // 23 -> Port

		System.out.println("waiting for request....");
		
		// connect it to client socket
		Socket s = ss.accept();
		System.out.println("Connection established");



		// to send data to the client
		PrintWriter ps
			= new PrintWriter(s.getOutputStream(), true);
		

		// to read data coming from the client
		BufferedReader br
			= new BufferedReader(
				new InputStreamReader(
					s.getInputStream()));

		
		// Hashtable Implementation
		Hashtable<Integer, String> test = new Hashtable<>();
			
			test.put(1, "apple");
			test.put(2, "orange");
			test.put(3, "mango");


		// server executes continuously
		while (true) {
			
			// str -> stores Client message
			String str; 

			// repeat as long as the client
			// does not send a null string
			// read from client
			while ((str = br.readLine()) != null) {
				System.out.println("Client Request-> " + str);

			// exit condition
			if(str.equals("exit")){
				break;
			}
			// //

			// invalid variable
			int invalid = 0;

					// GET Method
			if(str.length() > 3 && str.substring(0, 3).toUpperCase().equals("GET")){
		
				String key = "";
				String temp[] = Get(str,invalid);
				key = temp[0];
				invalid = Integer.parseInt(temp[1]);
				if(invalid != 1){	
				String ret = test.get(Integer.parseInt(key));
				ps.println("Server-> " + ret); // sends to client
				}
			} 		//  PUT Method
			else if(str.length() > 3 && str.substring(0, 3).toUpperCase().equals("PUT")){
				String key = "";
				int flag = 0;
				int index = 0;
				String temp[] = put(str,invalid);
				key = temp[0];
				invalid = Integer.parseInt(temp[1]);
				flag = Integer.parseInt(temp[2]);
				index = Integer.parseInt(temp[3]);
				if(invalid == 0){
				  if(index == 0){
					    flag = 0;
				  }else{
					for(int j = index; j < str.length(); j++){
						if(str.charAt(j) != ' '){
							flag = 1;
							test.put(Integer.parseInt(key), str.substring(j));
							break;
						}
					}
				  }
				}
				if(flag == 0){
					invalid = 1;
				}
				if(invalid != 1){
					ps.println("Server-> " + "PUT Successful"); // sends to client
				}
			}		// Delete Method
			else if(str.length() > 6 && str.substring(0, 6).toUpperCase().equals("DELETE")){
				String key = "";
				String temp[] =Delete(str,invalid);
				key = temp[0];
				invalid = Integer.parseInt(temp[1]);
				if(invalid != 1){
					test.remove(Integer.parseInt(key));
					ps.println("Server-> " + "DELETE Successful"); // sends to client
				}	
			}// Else always Invalid Output // sends to client
			else{
				ps.println("Server-> " + "Invalid Command, Try again: User can perform following commands : get <key> , put <key> <value> , delete <key>, exit");
			}
			// Invalid Output // sends to client
			if(invalid == 1){
				ps.println("Server-> " + "Invalid Command, Try again: User can perform following commands: get <key> , put <key> <value> , delete <key>");
			}
			//


			}
			

			// close connection
			ps.close();
			br.close();
			ss.close();
			s.close();

			// terminate application
			System.exit(0);

		} // end of while
	}
}
