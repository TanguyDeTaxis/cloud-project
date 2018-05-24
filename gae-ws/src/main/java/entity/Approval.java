package entity;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

@Entity
public class Approval {
	@Id private Long id;
	@Index private String lastName;
    private boolean accepted;
  
    public Approval() {
    	
    }
    
	 public Approval(String lastName, boolean accepted) {
		 setLastName(lastName);
		 setAccepted(accepted);
	}
	 
		public void setId(Long id) {
			this.id = id;
		}
		
		 public Long getId() {
			 return id;
		 }

		public String getLastName() {
			return lastName;
		}


		public void setLastName(String lastName) {
			this.lastName = lastName;
		}

		public boolean isAccepted() {
			return accepted;
		}

		public void setAccepted(boolean accepted) {
			this.accepted = accepted;
		}

}
