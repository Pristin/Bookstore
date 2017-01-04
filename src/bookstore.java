import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Scanner;

public class bookstore 
{
	public enum States 
	{
		Exit, ShowAllBooks, SearchForSpecificBook,
		CheckoutCart, Administrate, Mainmenu
	}
	
	
	public static void main(String[] args)
	{
		States state = States.Mainmenu;
		Scanner reader = new Scanner(System.in);
		Book[] cart = new Book[20];
		Book[] results;
		BookHandler bookHandler = new BookHandler();
		int choice = -1, booksInCart = 0;
		
		while(state != state.Exit)
		{
			switch(state)
			{
				case Mainmenu:
					System.out.println(state.ShowAllBooks.ordinal() + ". Show all books avalible\n" + state.SearchForSpecificBook.ordinal() +
							". Search for specific book or author\n" + state.CheckoutCart.ordinal() + ". Your checkout cart\n" + 
							state.Administrate.ordinal() + ". Administrate\n" + state.Exit.ordinal() + ". Exit");
					//Not adding security since this is temporary and for test purposes only
					state = States.values()[reader.nextInt()];
					//Remove the new line from the buffer
					reader.nextLine();
					break;
					
				case SearchForSpecificBook: 
				case ShowAllBooks:
					if(state == state.SearchForSpecificBook)
					{
						System.out.println("Enter the title of the book you wish to search for or the name of the author: ");
						results = bookHandler.list(reader.nextLine());
					}
					else
						results = bookHandler.list("-1");	//-1 equals all books
				
					if(results.length == 0)
						System.out.println("The book was not found");
					else
					{
						int i = 0;
						while(results[i] != null)
						{
							System.out.println((i+1) + ". " + results[i].getTitle() + " Written by: " + results[i].getAuthor() + " costs: " + results[i].getPrice());
							i++;
							//Not stepping outside of the array since while argument involves testing
							if(i >= results.length)
								break;
						}
						if(results[0] == null)
							System.out.println("No match for that search string");
						else
						{
							System.out.println("\nChose the number to add to your cart or enter 0 to back out");
							choice = reader.nextInt();
							while(choice != 0)		//Not backing out
							{
								cart[booksInCart] = results[choice-1];
								booksInCart++;
								//Extend the size of the cart if we add to many books
								if(booksInCart == cart.length)
									cart = Arrays.copyOf(cart, cart.length+5);
								System.out.println("Add another or 0 to back out");
								choice = reader.nextInt();
							}
							state = state.Mainmenu;
						}
					}
					break;
					
				case CheckoutCart:
					BigDecimal priceSum = BigDecimal.ZERO;
					System.out.println("Books in your cart: ");
					for(int i = 0; i < booksInCart; i++)			//Print the books and calculate total cost
					{
						System.out.println((i+1) + ". " + cart[i].getTitle() + " " + cart[i].getPrice());
						priceSum = priceSum.add(cart[i].getPrice());
					}
					System.out.println("Total cost: " + priceSum.doubleValue() + "\n1.Proceed? \n2.Remove a book from cart?\n3.Back to mainmenu");
					choice = reader.nextInt();
					if(choice == 1)	//Checkout
					{
						if(booksInCart == 0)
						{
							System.out.println("No wares in the cart, returning to main menu\n");
							state = state.Mainmenu;
						}
						int checkoutResults[];
						checkoutResults = bookHandler.buy(cart);
						
						for(int i = 0; i < booksInCart; i++)
						{
							if(checkoutResults[i] == 0)
								System.out.println(cart[i].getTitle() + " was purchased");
							if(checkoutResults[i] == 1)
								System.out.println(cart[i].getTitle() + " was not in stock");
							if(checkoutResults[i] == 3)
								System.out.println(cart[i].getTitle() + " does not exist");
						}
						//reset everything concerning the cart add payment if you want to make money
						booksInCart = 0;
						state = state.Mainmenu;
						
					}
					else if(choice == 2)  //Remove book from cart
					{
						int remove = -1;
						Book tmp = null;
						System.out.println("Enter the number of the book you wish to remove:");
						remove = reader.nextInt() - 1;
						
						tmp = cart[remove];					//Need to swap places or they will both be deleted
						cart[remove] = cart[booksInCart - 1];
						cart[booksInCart] = tmp;
						booksInCart--;
					}
					else if(choice == 3)  //Back to MM
						state = state.Mainmenu;
					break;
					
				case Administrate:
					//Add login and more options when needed. If bookstore gets big add removal option and add from file
					System.out.println("1. Add book\n0. Back to mainmenu ");	
					choice = reader.nextInt();
					reader.nextLine();
					if(choice == 1)
					{
						String title = "";
						String author = "";
						BigDecimal price = BigDecimal.ZERO;
						int quantity = 0;
						Book newBook;
						
						System.out.println("Enter the title of the book: ");
						title = reader.nextLine();
						System.out.println("Enter the author of the book: ");
						author = reader.nextLine();
						System.out.println("Enter the price of the book: (For decimals use a , example 100,50");
						price = reader.nextBigDecimal();
						newBook = new Book(title, author, price);
						
						System.out.println("Enter the quantity of the book: ");
						quantity = reader.nextInt();
						bookHandler.add(newBook, quantity);
					}
					else
						state = state.Mainmenu;
					break;
			}
		}
		reader.close();
	}

}
