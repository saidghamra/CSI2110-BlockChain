import java.util.*;
import java.io.*;
import java.sql.*;

/**
 * Class BlockChain represents a blockchain object. A blockchain is used 
 * to store all the blocks. It also allows reading a blockchain from a 
 * text file, checking whether its valid, adding transactions and writing
 * a blockchain to a file. 
 * 
 * @author Said Ghamra
 * @version 1.0
 */
public class BlockChain {

	// Instance Variables
	private ArrayList<Block> blocks;             // ArrayList containing all the blocks of the blockchain.
	private ArrayList<String> givenHashes;		// ArrayList containing all the hashes included in the textfile

	/**
	 * Constructor method for the class BlockChain. Initializes
	 * the ArrayLists blocks and givenHashes.
	 */
	public BlockChain () {

		this.blocks = new ArrayList<Block>();
		this.givenHashes = new ArrayList<String>();
	}

	/**
	 * Method to add a block to the BlockChain.
	 * 
	 * @param block Block object to be added.
	 */
	public void add(Block block) {

		blocks.add(block);
	}	

	/**
	 * Getter method for a block object at a certain index in 
	 * the BlockChain.
	 * 
	 * @param  index int containing the index number of the block
	 * wanted in the BlockChain.
	 * @return       a block object corresponding to the index.
	 */
	public Block getBlock(int index){

		return blocks.get(index);
	}

	/**
	 * Getter method for the hash of a certain block given in 
	 * the text file provided. These hashes are stored in the 
	 * ArrayList givenHashes.
	 * 
	 * @param  index int containing the index of the block the hash 
	 * is needed for.
	 * @return       a String containing the given hash of a certain block.
	 */
	public String getGivenHash(int index) {

		return givenHashes.get(index);
	}

	/**
	 * Getter method for the number of blocks in the BlockChain.
	 * 
	 * @return an int containing the number of blocks stored
	 * in the ArrayList blocks.
	 */
	public int getBlockNumbers() {

		return blocks.size();
	}

	/**
	 * The method fromFile reads the content of a textfile, creates blocks
	 * from the information provided, then adds them to a blockchain and 
	 * returns it.
	 * 
	 * @param  filename a String containing the name of the file to be read.
	 * @return          blockchain object containing all the information from the file opened.
	 */
	public static BlockChain fromFile (String filename) {

		System.out.println("\nCreating BlockChain...\n");

		// Variable info is an ArrayList that stores all the info extracted from the textfile
		ArrayList<String> info = new ArrayList<String>(); 

		// Variable blockchain is a BlockChain object that stores all the blocks being generated
		BlockChain blockchain = new BlockChain();

		try {

			// Adding all the information from the text file to the ArrayList info
			Scanner scanner = new Scanner(new File(filename));
			
			while (scanner.hasNext()) {	

			info.add(scanner.nextLine());
			}	
		}

		catch (Exception e) {
			
			// do nothing since this will never throw an exception. It is checked beforehand in the main method
			System.out.println("Whoops! Looks like there's something wrong with the text file!");
		}
		
		for (int i=0;i<=info.size()-7;i+=7) {

			// Storing the index of the block in an int variable called index
			int index = Integer.parseInt(info.get(i));

			// Adding the hashes found in the text file to the ArrayList givenHashes. To be used for validation later on.
			blockchain.givenHashes.add(info.get(i+6));
			
			// If the program is adding the first block to the blockchain, the previousHash of that block has to be set to 00000
			if (index==0) {

				// Creating the block and adding it to the blockchain
				blockchain.add(new Block(index, new Timestamp(Long.parseLong(info.get(i+1))), 
				new Transaction(info.get(i+2),info.get(i+3),Integer.parseInt(info.get(i+4))),info.get(i+5),"00000"));
			}
			
			else {
				// Creating the block and adding it to the blockchain
				blockchain.add(new Block(index, new Timestamp(Long.parseLong(info.get(i+1))), 
				new Transaction(info.get(i+2),info.get(i+3),Integer.parseInt(info.get(i+4))),info.get(i+5), blockchain.getBlock(blockchain.getBlockNumbers()-1).getHash()));
			}
		}

		return blockchain;
	}

	/**
	 * Method print prints out the blocks stored in the blockchain
	 * and the number of blocks stored in the blockcahin. This method 
	 * was used for testing purposes to make sure that the right 
	 * blocks are being created.
	 */
	public void print() {
		
		System.out.println("size: " + getBlockNumbers() + " blocks");

		for (int i=0; i<getBlockNumbers(); i++) {
			
			System.out.println(getBlock(i));
		}
	}

	/**
	 * The toFile method extracts all the information from the blocks
	 * stored in the blockchain and writes them to a text file of a 
	 * specific name. The format of the text file is the same as the
	 * bitcoinBank.txt file provided. 
	 * 
	 * @param filename a String containing the name of the file to be written.
	 */
	public void toFile (String filename) {

		try {

			BufferedWriter writer = new BufferedWriter(new FileWriter(filename));

			//For loop used to access every block stored in the blockchain
			for (int i=0; i<blocks.size(); i++) {
			
			// Storing the block in a Block object
			Block b = blocks.get(i);

			//Writing the index of the block to the text file
			writer.write(Integer.toString(b.getIndex()));
			writer.newLine();
			//Writing the timestamp of the block to the text file
			writer.write(Long.toString(b.getTimeStamp().getTime()));
			writer.newLine();
			//Writing the sender of the transaction stored in the block to the text file
			writer.write(b.getTransaction().getSender());
			writer.newLine();
			//Writing the receiver of the transaction stored in the block to the text file
			writer.write(b.getTransaction().getReceiver());
			writer.newLine();
			//Writing the amount of bitcoin sent from the transaction stored in the block to the text file
			writer.write(Integer.toString(b.getTransaction().getAmount()));
			writer.newLine();
			//Writing the nonce of the block to the text file
			writer.write(b.getNonce());
			writer.newLine();
			//Writing the hash of the block to the text file
			writer.write(b.getHash());
			writer.newLine();
			}

			// Closing the writer
			writer.close();
		}

		// Catching any exception that might occur 
		catch (Exception e) {

			System.out.println("Whoops! Something went wrong while i was trying to write to the text file!");
		}

		System.out.println("\n" + filename + " created!");
	}

	/**
	 * The method validateBlockChain accesses all the blocks stored
	 * in the blockchain and checks their index, hash, and previous hash.
	 * This method also accesses all the transactions and checks whether
	 * every transaction is valid, i.e. the sender has enough money to
	 * proceed with the transaction.
	 * 
	 * 
	 * @return true if the blockchain is valid, false otherwise
	 */
	public boolean validateBlockChain() {

		System.out.println("Validating BlockChain...\n");

		// boolean result stores the result of validating the blockchain
		boolean result = true;

		// For loop used to cycle through all the blocks stored in the blockchain
		for (int i=0; i<getBlockNumbers(); i++) {
			
			// Checking the hashes of all the blocks
			if (!(getBlock(i).getHash().equals(getGivenHash(i)))) {
				
				result = false;
				break;	
			}

			// Checking previous hash of the first block
			if (i==0) {
				
				if (!(getBlock(i).getPreviousHash().equals("00000"))) {
					
					result=false;
					break;
				}
			}
			
			else {
				
				// Checking the previous hashes of the all other blocks besides the first block
				if ((!(getBlock(i).getPreviousHash().equals(getBlock(i-1).getHash())))) {
					
					result=false;
					break;
				}

				// Checking indexes of all the blocks
				if (getBlock(i).getIndex()!=(getBlock(i-1).getIndex()+1)) {
				
					result = false;
					break;
				}	
			}
		}

		// ArrayList users contains a list of all the users in the blockchain
		ArrayList<String> users = new ArrayList<String>();

		// For loop used to populate the ArrayList users with all the users in the blockchain
		for (int i=0; i<getBlockNumbers(); i++) {
			
			// Storing every transaction of every block in a Transaction object
			Transaction trans = getBlock(i).getTransaction();

			// If the ArrayList users doesn't contain the sender, add it to the ArrayList users
			if (!(users.contains(trans.getSender()))) {
				
				users.add(trans.getSender());
			}

			// If the ArrayList users doesn't contain the receiver, add it to the ArrayList users
			if (!(users.contains(trans.getReceiver()))) {
				
				users.add(trans.getReceiver());
			}
		}

		// For loop used to validate all the transactions stored in the blocks of the blockchain
		for (int i=0; i<users.size(); i++) {
			
			// If the user is bitcoin do nothing since bitcoins balance is always negative (since that determines the amount of bitcoin we have in circulation)
			if (users.get(i).equals("bitcoin")) {
				
			}

			// If the user is not bitcoin, call the method getBalance and check if their balance is negative
			else if (getBalance(users.get(i))<0) {
				
				result = false;
				break;
			}
		}

		return result;
	}

	/**
	 * The method getBalance calculates the balance of a specific user in the 
	 * blockchain. The method cycles through all the blocks stored in the 
	 * blockchain and accumulates all the bitcoins assigned to the user minus
	 * the ones that have been spent by the user.
	 * 
	 * @param  username String cointaining the name of the user whose balance is to be checked
	 * @return          int value of the balance of the user
	 */
	public int getBalance(String username) {

		// int variable balance used to calculate the balance of the user
		int balance=0;

		// For loop used to cycle through all the blocks stored in the blockchain
		for (int i=0; i<getBlockNumbers(); i++) {
			
			// Storing the transaction object of every block in the blockchain in a transation object trans
			Transaction trans = getBlock(i).getTransaction();
			
			// If the user is sending bitcoins, subtract the amount
			if (trans.getSender().equals(username)) {
				
				balance-=trans.getAmount();
			}

			// If the user is receiving money, add the amount
			if (trans.getReceiver().equals(username)) {
				
				balance+=trans.getAmount();
			}
		}

		return balance;
	}

	/**
	 * The main method of the class BlockChain prompts the user for the filename 
	 * of a text file containing all the information of a blockchain. The method 
	 * validates the file name and makes sure it exists in the projects main directory.
	 * The method then reads the texts file and generates a blockchain based on the
	 * information in the text file. Then, it checks whether the blockchain is valid.
	 * If the blockchain is not valid, it will print so and the program terminates.
	 * If not, the method asks the user whether he/she wants to add new transactions.
	 * If yes, the program makes sure the transaction is valid and adds it to the 
	 * blockchain. The method will keep prompting the user whether they would like to 
	 * add a transaction until the user inputs no. The blockchain is then exported to a 
	 * text file with a specific file name and format.
	 * 
	 */
	public static void main(String[] args) {

		System.out.println("****************** CSI2110 A2 BlockChain - Said Ghamra 300008217 ******************");	

		boolean test = true;                            // boolean test is used to validate the filename of the text file from which the blockchain is generated
		boolean moreTransactions = true;               // boolean moreTransactions is used to allow the users to make more transactions
		BlockChain blockChain = new BlockChain();     // BlockChain object blockChain used to store the blockchain generated from reading the text file
		
		// While loop used to validate that file whose name the users enters is valid or not
		while (test) {
			
			// Try catch used to validate whether the file entered is valid or not. (The file is valid if a scanner can be created)
			try {

				System.out.println("\nPlease enter the name of the file you would like to open: ");

				// Getting the name of the file from the user
				String fileName = new Scanner(System.in).nextLine();

				// Used to test whether the file entered is valid or invalid. If its invalid, it'll throw an error that will be caught using the try catch
				Scanner scanner = new Scanner(new File(fileName)); 
				
				// If a scanner is built successfully, we set the variable test to false so it doesn't prompt the user for a new file name
				test = false;

				// Creating the blockchain from the information specified in the text file
				blockChain = BlockChain.fromFile(fileName);

				// Checking whether the blockchain is valid or not
				boolean valid = blockChain.validateBlockChain();

				// If the blockchain is valid, print so
				if (valid) {

					System.out.println("BlockChain is valid!");
				}
				
				// If the blockchain is invalid, print so
				else if (!valid) {
			
					System.out.println("BlockChain is not valid!");

					// Cant add new transactions to a blockchain thats not valid
					moreTransactions=false;
				}

				// While loop used to add more transactions to the blockchain
				while(moreTransactions) {

					System.out.println("\nWould you like to add a transaction to the BlockChain? Please enter y for yes or n for no: ");

					// Determining whether the user wants to add new transactions or not
					String input = new Scanner(System.in).nextLine();
					
					// If the user wants to add more transactions
					if (input.equals("y")) {
					
						// Getting the senders name
						System.out.println("\nPlease enter the senders name: ");
						String sender = new Scanner(System.in).nextLine();

						// Getting the receivers name
						System.out.println("\nPlease enter the receivers name: ");
						String receiver = new Scanner(System.in).nextLine();

						// Getting the amount of bitcoin to be transferred
						System.out.println("\nPlease enter the amount: ");
						int amount = new Scanner(System.in).nextInt();

						// If the sender has enough balance, create a new block and add it to blockchain
						if (blockChain.getBalance(sender)>=amount) {
							
							System.out.println("\nThis transaction is valid! Adding the transaction to the BlockChain!");

							blockChain.add(new Block(blockChain.getBlockNumbers(), new Timestamp(System.currentTimeMillis()), new Transaction(sender, receiver, amount), "", blockChain.getBlock(blockChain.getBlockNumbers()-1).getHash()));

							System.out.println("\nAdded the transaction to the BlockChain!");
						}

						// If the transaction is invalid, it will print so then promps the user if he/she wants to add a new transaction again
						else {

							System.out.println("\nThe transaction you just entered is invalid! " + sender +" doesn't have " + amount + " bitcoins!");
						}
					}

					// If the user doesn't want to add new transactions
					else if (input.equals("n")) {

						System.out.println("\nNo extra transactions for you!");
						moreTransactions=false;
					}

					// If the user inputs an invalid answer, print so and reprompt the user
					else {

						System.out.println("\nYour input is invalid! Please enter y for yes or n for no!");
					}
				}

				// Printing the blockchain to a text file of a specific name in the projects directory
				blockChain.toFile(fileName.substring(0,fileName.length()-4)+"_sgham022.txt");
			}

			// If the file whose name the user inputted, the program prints so and prompts the user for a new file name
			catch (FileNotFoundException e) { 

				System.out.println("\nOops! Looks like the file you entered doesn't exist. Please make sure the file is in the projects main directory and is in the format 'name.txt'!");
			}
		}
	}
}