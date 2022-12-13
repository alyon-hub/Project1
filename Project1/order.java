/*
 * Name: Avriel Lyon
 * Course: CNT 4714 - Fall 2022
 * Assignment title: Project 1 - Event-driven Enterprise Simulation
 * Date: Friday September 15, 2022
 */

public class order {
		private String itemID;
		private String price;
		private String description;
		private String inStock;
		private String discount;
		private String totalPrice;
		private String quantity;
		
		//item ID
		public String getItemID() {
			return itemID;
		}
		public void setItemID(String itemID) {
			this.itemID = itemID;
		}
		
		//quantity
		public String getQuantity() {
			return quantity;
		}
		public void setQuantity(String quantity) {
			this.quantity = quantity;
		}
		
		//is the item in stock
		public String getStock() {
			return inStock;
		}
		public void setStock(String inStock) {
			this.inStock = inStock;
		}
		
		//description of item
		public String getDescription() {
			return description;
		}
		public void setDescription(String description) {
			this.description = description;
		}
		
		//price value
		public String getPrice() {
			return price;
		}
		public void setPrice(String price) {
			this.price = price;
		}
		
		//discount value
		public String getDiscount() {
			return discount;
		}
		
		/*
		 * Name: Avriel Lyon
		 * Course: CNT 4714 - Fall 2022
		 * Assignment title: Project 1 - Event-driven Enterprise Simulation
		 * Date: Friday September 15, 2022
		 */
		
		public void setDiscount(String discount) {
			this.discount = discount;
		}
		
		//total price of the item(s)
		public String getTotalPrice() {
			return totalPrice;
		}
		
		public void setTotalPrice(String totalPrice) {
			this.totalPrice = totalPrice;
		}
}
