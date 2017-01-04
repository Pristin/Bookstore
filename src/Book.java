import java.math.BigDecimal;

public class Book 
{
	private String title;
	private String author;
	private BigDecimal price;
	private int quantity;
	
	Book(String title, String author, BigDecimal price)
	{
		this.title = title;
		this.author = author;
		this.price = price;
	}
	
	String getTitle()
	{
		return title;
	}
	
	String getAuthor()
	{
		return author;
	}
	
	BigDecimal getPrice()
	{
		return price;
	}
	
	int getQuantity()
	{
		return quantity;
	}
	
	void setPrice(BigDecimal price)
	{
		this.price = price;
	}
}
