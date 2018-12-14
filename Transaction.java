/**
 * Class Transaction represents a transaction object.
 * A transaction is used to document the sender, receiver,
 * and the amount sent. 
 * 
 * @author Said Ghamra
 * @version 1.0
 */
public class Transaction {

	// Instance Variables
	private String sender;          // Sender of the bitcoin
	private String receiver;	   // Receiver of the bitcoin
	private int amount;           // Amount of bitcoin sent

	/**
	 * Constructor method for the class Transaction. Takes in 
	 * three variables as input.
	 * 
	 * @param  sender   String containing the name of the sender.
	 * @param  receiver String containing the name of the receiver.
	 * @param  amount   Int containing the amount of Bitcoin sent.
	 */
	public Transaction (String sender, String receiver, int amount) {
		
		// Checking whether amount is negative or equal to zero
		if (amount<=0) {
			
			throw new IllegalArgumentException("Amount can't be negative or zero!!");
		}

		else {

			// Checking whether sender or receiver are null
			if (sender.equals(null) || receiver.equals(null)) {
				
				throw new NullPointerException("Transaction inputs can't be null!!");
			}
			
			this.sender=sender;
			this.receiver=receiver;
			this.amount=amount;
		}
	}

	/**
	  * Getter method for the variable sender.
	  * 
	  * @return the name of the sender as a String.
	  */
	public String getSender () {

		return sender;
	}

	/**
	 * Getter method for the variable receiver.
	 * 
	 * @return the name of the receiver as a String.
	 */
	public String getReceiver() {

		return receiver;
	}

	/**
	 * Getter method for the variable amount representing 
	 * the amount of Bitcoin beint sent.
	 * 
	 * @return the amount of bitcoin being sent as an int.
	 */
	public int getAmount() {

		return amount;
	}

	/**
	 * Method that enables printing out an instance of the class Transaction.
	 * 
	 * @return a string containing the sender, receiver, and the amount of 
	 * the transaction.
	 */
	public String toString() {

		return sender + ":" + receiver + "=" + amount;
	}
}