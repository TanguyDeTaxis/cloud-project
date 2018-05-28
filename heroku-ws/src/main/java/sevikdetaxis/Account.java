package sevikdetaxis;

public class Account {

	private Long id;
	private String firstName;
    private String lastName;
    private int amount;
	private boolean risk;
	
	public int getAmount() {
		return amount;
	}
	
	public String getFirstName() {
		return firstName;
	}
	
	public Long getId() {
		return id;
	}
	
	public String getLastName() {
		return lastName;
	}
	
	public boolean getRisk(){
		return this.risk;
	}
	
	public void setAmount(int amount) {
		this.amount = amount;
	}
	
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	public void setRisk(boolean risk) {
		this.risk = risk;
	}
}
