package sevikdetaxis;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Approval {
	private Long id;
	private String lastName;
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
