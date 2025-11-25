package model;

public class Client {
	
	
	    private int client_id;
	    private int user_id; // links to User
	    private String name;
	    private String email;
	    private String phone;
	    private String licenseNumber;
	    private String address;
	    
	    
	    //Constructor 
	    public Client(int client_id, int user_id, String name, String email, String phone, String licenseNumber, String address) {
	        this.client_id = client_id;
	        this.user_id = user_id; 
	        this.name = name;
	        this.email = email;
	        this.phone = phone;
	        this.licenseNumber = licenseNumber;
	        this.address = address;
	    }


		public int getClient_id() {
			return client_id;
		}


		public void setClient_id(int client_id) {
			this.client_id = client_id;
		}


		public int getUser_id() {
			return user_id;
		}


		public void setUser_id(int user_id) {
			this.user_id = user_id;
		}


		public String getName() {
			return name;
		}


		public void setName(String name) {
			this.name = name;
		}


		public String getEmail() {
			return email;
		}


		public void setEmail(String email) {
			this.email = email;
		}


		public String getPhone() {
			return phone;
		}


		public void setPhone(String phone) {
			this.phone = phone;
		}


		public String getLicenseNumber() {
			return licenseNumber;
		}


		public void setLicenseNumber(String licenseNumber) {
			this.licenseNumber = licenseNumber;
		}


		public String getAddress() {
			return address;
		}


		public void setAddress(String address) {
			this.address = address;
		}


		@Override
		public String toString() {
			return "Client [client_id=" + client_id + ", user_id=" + user_id + ", name=" + name + ", email=" + email
					+ ", phone=" + phone + ", licenseNumber=" + licenseNumber + ", address=" + address + "]";
		}



		
		
		
		
	    
		
		
		
	    
	    
	    
	
	

}
