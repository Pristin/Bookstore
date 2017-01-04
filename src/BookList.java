import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.util.Arrays;
import java.util.Scanner;

public interface BookList 
{
	public Book[] list(String searchString);
	public boolean add(Book book, int quantity);
	public int[] buy(Book... books);
}

class BookHandler implements BookList
{
	private Book[] allBooks;
	private int[] quantityBooks;
	private int nrOfBooks = 0;
	
	BookHandler()
	{
		String readLine = "";
		String[] splitLine = {""};
		String title = "";
		String author = "";
		int quantity = 0;
		BigDecimal price = BigDecimal.ZERO;
		URL url = null;
		Scanner scan = null;
		
		allBooks = new Book[10];
		quantityBooks = new int[10];
		
		try 
		{
			 url = new URL("http://www.contribe.se/bookstoredata/bookstoredata.txt");
			 scan = new Scanner(url.openStream(), "UTF-8");
		} 
		catch (IOException e) 
		{
			System.out.println("File not found in URL");
			e.printStackTrace();
		}
		
		while(scan.hasNext())	//Add the books to our array
		{
			readLine = scan.nextLine();
			readLine = readLine.replaceAll(",", "");
			splitLine = readLine.split(";");
			title = splitLine[0];
			author = splitLine[1];
			price = BigDecimal.valueOf(Double.parseDouble(splitLine[2]));
			quantity = Integer.parseInt(splitLine[3]);
			Book temp = new Book(title, author, price);
			
			add(temp, quantity);
		}

	}
	
	public Book[] list(String searchString)
	{
		int nrOfResults = 0;
		Book[] result = new Book[nrOfBooks];
		if(searchString.equals("-1"))
		{
			for(int i = 0; i < nrOfBooks; i++)
			{
				result[i] = allBooks[i];
			}
		}
				
		else
		{
			for(int i = 0; i < nrOfBooks; i++)		//Search for a match in the title and author
			{
				if(allBooks[i].getTitle().toLowerCase().contains(searchString.toLowerCase()) || allBooks[i].getAuthor().toLowerCase().contains(searchString.toLowerCase()))
				{
					result[nrOfResults] = allBooks[i];
					nrOfResults++;
				}
			}
		}
		
		return result;
	}
	
	public boolean add(Book book, int quantity) 
	{
		boolean outcome = true;
		
		if(nrOfBooks == allBooks.length)	//Make the array bigger if needed
		{
			allBooks = Arrays.copyOf(allBooks, allBooks.length+10);
		}
		//If bookstore gets big add check for duplicates
		allBooks[nrOfBooks] = book;
		quantityBooks[nrOfBooks] = quantity;
		nrOfBooks++;
		
		return outcome;
	}

	public int[] buy(Book... books) 
	{
		int c = 0;
		int result[] = new int[books.length];
		
		while(books[c] != null)
		{
			for(int i = 0; i < nrOfBooks; i++)
			{
				if(books[c].equals(allBooks[i]))
				{
					if(quantityBooks[c] > 0)		//OK
					{
						quantityBooks[c]--;
						result[c] = 0;
						break;
					}
					else	//NOT_IN_STOCK
					{
						result[c] = 1;
						break;
					}
				}
				else if(i == nrOfBooks - 1)			//DOES_NOT_EXIST
					result[c] = 2;
			}
			c++;
		}
		return result;
	}
}
