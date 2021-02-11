package ua.mainacademy;



import ua.mainacademy.service.ItemPageParsingService;

public class ApplicationRunner
{
    public static void main( String[] args ) {
        String url = "https://www.amazon.com/HP-Gaming-Laptop-Memory-GeForce/dp/B08JVNZJWT";

        System.out.println(ItemPageParsingService.getItemFromPage(url));
    }

    }

