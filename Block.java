import java.sql.Timestamp;
import java.util.*;

/**
 * Class Block represents a block object. A block is used to store the
 * index of the block, timestamp, transaction object, nonce, previous 
 * hash, and the hash of the block. 
 * 
 * @author Said Ghamra
 * @version 1.0
 */
public class Block {
	
	// Instance Variables
	private int index; 							// The index of the block in the list
	private java.sql.Timestamp timestamp;      // Time at which the transaction has been processed
	private Transaction transaction;          // The transaction object
	private String nonce;                    // Random string for proof of work
	private String previousHash;            // Previous hash
	private String hash;                   // Hash of the block

	/**
	 * Constructor method for the class Block.
	 * 
	 * @param  index        Int containing the index of the block in the list.
	 * @param  timestamp    Timestamp object containing the time the transaction was made.
	 * @param  transaction  Transaction object containing the transaction details.
	 * @param  nonce        String containing the nonce of the block.
	 * @param  previousHash String containing the previous hash of the block.
	 */
	public Block (int index, java.sql.Timestamp timestamp,  Transaction transaction, String nonce, String previousHash) {

		if (index<0) {

			throw new IllegalArgumentException("Index can't be negative!!");
		}
		
		else {
			
			if (nonce.equals(null)|| previousHash.equals(null) || transaction.equals(null)) {
				throw new NullPointerException("Block inputs can't be null!!");
			}

			this.index=index;
			this.timestamp= timestamp;
			this.nonce=nonce;
			this.previousHash=previousHash;
			this.transaction=transaction;
			this.hash=generateHash();
		}
	}

	/**
	 * Method that enables printing out an instance of the class Block.
	 * 
	 * @return a string containing the timestamp, transaction, nonce, 
	 * previousHash.
	 */
	public String toString() {

		return timestamp.toString() + ":" + transaction.toString() + "." + nonce + previousHash;
	}

	/**
	 * This method generates the hash of a block. The hash of a block 
	 * should contain 00000 in the beginning. It sends the string representation 
	 * of the block using the Class Blocks toString() method to the Class 
	 * Sha1 that encrypts it. The Nonce is a randomly generated String that can range
	 * from 1 to 20 characters in length containing ASCII charachers in the integer 
	 * range of [33,126].
	 * 
	 * @return a string containing the hash of a Block.
	 */
	public String generateHash() {

		String returnHash="";   // Variable returnHash used to hold the nonce being generated in the beginning then holds the hash to be returned
		int numberTrials=0;    // Variable numberTrials used to track how many trial runs does it take to generate a nonce that results in a hash beginning with 5 zeros

		try {
			
			// Ensures the algorithm keeps running until the hash of the block contains five zeros in the beginning
        	while(!(Sha1.hash(toString()).substring(0,5).equals("00000"))) {
        		
        		Random r = new Random();

        		// This for loop controls the length of the nonce being generated randomly		
        		for (int x=1; x<=20; x++) {
        			
        			// Increment the numberTrials variable everytime a trial is made
        			numberTrials++;

        			// Resetting the value of returnHash after every run
        			returnHash="";

        			// This for loop adds x many characters to the variable returnHash which holds the temporary nonce 
        			for (int i=0; i<x; i++) {
					
						returnHash+=((char)(r.nextInt(94)+33));        			
        			}

        			// Setting the nonce of this block to the string that was just generated
        			nonce=returnHash;

        			// If the hash contains five zeros in the beginning, stop the for loop
        			if ((Sha1.hash(toString()).substring(0,5).equals("00000"))) {

        				break;
        			}
        		}
        	}

        	// Setting the value of the variable returnHash to the hash that is going to be returned
        	returnHash=Sha1.hash(toString());
		}

		catch (Exception e) {
				
			System.out.println("Whoops!");
		}

		// Used to print out the number of trials for generating a nonce
		//System.out.println("\nNumber of trials: " + numberTrials + "\n");
		
		return returnHash;	
	}

	/**
	 * Getter method for the index of the block.
	 * 
	 * @return an int contain9ing the index of the block.
	 */
	public int getIndex() {

		return index;
	}	

	/**
	 * Getter method for the timestamp of the block.
	 * 
	 * @return a timestamp object containing the time a 
	 * block was created.
	 */
	public java.sql.Timestamp getTimeStamp() {

		return timestamp;
	}

	/**
	 * Getter method for the transaction of the block.
	 * 
	 * @return a transaction object containing the transaction 
	 * details of this block.
	 */
	public Transaction getTransaction() {

		return transaction;
	}	

	/**
	 * Getter method for the nonce of the block.
	 * 
	 * @return a String containing the nonce of the block.
	 */
	public String getNonce() {

		return nonce;
	}

	/**
	 * Getter method for the previous hash of the block.
	 * 
	 * @return  a String containing the previous hash of 
	 * the block.
	 */
	public String getPreviousHash() {

		return previousHash;
	}

	/**
	 * Getter method for the hash of the block.
	 * 
	 * @return a String containing the hash of the block.
	 */
	public String getHash () {
		return hash;
	}
}