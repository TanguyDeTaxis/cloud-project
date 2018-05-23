package entity;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

@Entity
public class Account {
	@Id Long id;
	@Index private String firstName;
    private String lastName;
    private int amount;
	private boolean risk;
  
    public Account() {
    	
    }
    
	 public Account(String firstName, String lastName) {
		 setFirstName(firstName);
		 setLastName(lastName);
	}
	 
	 public Account(String firstName, String lastName, int amount, boolean risk) {
		 setFirstName(firstName);
		 setLastName(lastName);
		 setAmount(amount);
		 setRisk(risk);
	}
	 
	 public String getFirstName() {
			return firstName;
		}


		public void setFirstName(String firstName) {
			this.firstName = firstName;
		}


		public String getLastName() {
			return lastName;
		}


		public void setLastName(String lastName) {
			this.lastName = lastName;
		}


		public int getAmount() {
			return amount;
		}


		public void setAmount(int amount) {
			this.amount = amount;
		}


		public boolean isRisk() {
			return risk;
		}


		public void setRisk(boolean risk) {
			this.risk = risk;
		}
	
}