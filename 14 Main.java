/*
* Description
* The task is to implement a bidding system for real estate. The program is supposed to handle information about different real estate objects, as 
* well as the bidding process. The program should have the necessary error handling (ways of validating the input, such as checking that the price is * a number, etc.). The program should under no circumstances crash when receiving incorrect input. It is allowed to store additional information, 
* other than provided in the assignment description.
*
* Menu
* At the start, the program should print a menu of options that can be selected by the user. It should be possible to exit the program by pressing q.
*
* Registering an object
* An object is registered by providing its address, type, as well as its asking price. In the system, it is identified by using a unique randomized ID * (in range 1000-9999). Make sure that multiple objects do not end up with the same ID number.
*
* Bidding registration
* Once an object is registered, it should be possible to bid on the property. An object is referenced by the ID number which was generated in previous * step. A bid should be higher than the current highest bid, but it can be lower than the asking price. Bidder’s name should be registered by the 
* system. Note that the system should provide an error message if there is no object with provided ID.
*
* Ending of bidding process
* Once the seller is satisfied with the highest bid, it should be possible to end the bidding process. In order to be able to end the bidding process, * there should be at least one (1) bid present for the object. Note: it should be possible to abort the ending of the bidding process by typing ”No”.
*
* Printing of bidding history
* It should be possible to print bidding history of an object. If the object is sold, the accepted bid should be highlighted in some way.
*
* Printing all unsold objects
* It should be possible to print all unsold objects.
* 
* Printing all sold objects (sorted by highest price)
* Finally, there should be an option to print all sold objects. The output should be sorted by the selling price (lowest to highest).
*/


import java.util.Scanner;

class Main {
  // Global scanner object
  public static Scanner userInput = new Scanner(System.in);

  // Constants
  public static final int EXIT_CODE = -1;
  public static final int INVALID_INPUT = 7;

  // Constants for property objects
  public static final int MAX_OBJECTS = 10;
  public static final int NUM_OF_OBJECT_COLUMNS = 6;
  public static final int OBJECT_ID = 0;
  public static final int OBJECT_ADDRESS = 1;
  public static final int OBJECT_TYPE = 2;
  public static final int OBJECT_PRICE = 3;
  public static final int OBJECT_STATUS = 4;
  public static final int OBJECT_SOLD_PRICE = 5;

  // Constants for bid objects
  public static final int MAX_BIDS = 1000;
  public static final int NUM_OF_BIDS_COLUMNS = 3;
  public static final int BID_OBJECT_ID = 0;
  public static final int BID_BIDDER = 1;
  public static final int BID_PRICE = 2;


  public static final String[] OBJECT_TYPES = {"Apartment", "House", "Commercial"};
  public static final String[] OBJECT_STATUSES = {"Unsold", "Sold"};

  public static void main(String[] args) {

    String[][] objects = new String[MAX_OBJECTS][NUM_OF_OBJECT_COLUMNS];
    String[][] bids = new String[MAX_BIDS][NUM_OF_BIDS_COLUMNS];

    int numOfObjects = 0;
    int numOfBids = 0;

    while (true) {
      switch (menu()) {
        case -1: // q entered by user
          System.exit(0);
          break;
        case 1:
          System.out.println("Register new object");
          numOfObjects = registerNewObject(objects, numOfObjects);
          break;
        case 2:
          System.out.println("Register bid");
          numOfBids = registerBid(objects, bids, numOfObjects, numOfBids);
          break;
        case 3:
          System.out.println("End bidding process");
          endBiddingProcess(objects, bids, numOfObjects, numOfBids);
          break;
        case 4:
          System.out.println("Print bidding history for the object");
          printBidHistory(objects, bids, numOfObjects, numOfBids);
          break;
        case 5:
          System.out.println("Print all unsold objects");
          printAllUnsoldObjects(objects, numOfObjects, bids, numOfBids);
          break;
        case 6:
          System.out.println("Print all sold objects (by price)");
          printAllSoldObjectsBySoldPrice(objects, numOfObjects);
          break;
        default:
          System.out.println("You must choose between 1-6 or press q to quit");   
      }
    }
  }

  /* 
  * The method prints all the sold objects sorted by sold price
  */
  private static void printAllSoldObjectsBySoldPrice(String[][] objects, int numOfObjects) {
    System.out.printf("%-10s %-20s %-10s %-15s %-15s\n", "ID", "Address", "Type", "Asking Price", "Sold Price");

    // Calculate the number of sold objects
    int numOfSoldObjects = 0;
    for (int i = 0; i < numOfObjects; i++) {
      if (objects[i][OBJECT_STATUS].equalsIgnoreCase(OBJECT_STATUSES[1])) {
        numOfSoldObjects++;
      }
    }

    // Create a copy of the objects array of the size of the sold objects
    String[][] objectsCopy = new String[numOfSoldObjects][NUM_OF_OBJECT_COLUMNS];
    int tempCopyIndex = 0;
    for (int i = 0; i < numOfObjects; i++) {
      if (objects[i][OBJECT_STATUS].equalsIgnoreCase(OBJECT_STATUSES[1])) {
        objectsCopy[tempCopyIndex] = objects[i];
        tempCopyIndex++;
      }
    }

    // Sort the copy of the objects array by sold price
    for (int i = 0; i < numOfSoldObjects; i++) {
      for (int j = 0; j < numOfSoldObjects - 1; j++) {
        if (Integer.parseInt(objectsCopy[j][OBJECT_SOLD_PRICE]) > Integer.parseInt(objectsCopy[j + 1][OBJECT_SOLD_PRICE])) {
          String[] temp = objectsCopy[j];
          objectsCopy[j] = objectsCopy[j + 1];
          objectsCopy[j + 1] = temp;
        }
      }
    }

    // Print the sorted copy of the objects array
    for (int i = 0; i < numOfSoldObjects; i++) {
      System.out.printf("%-10s %-20s %-10s %-15s %-15s\n", objectsCopy[i][OBJECT_ID], objectsCopy[i][OBJECT_ADDRESS], objectsCopy[i][OBJECT_TYPE], objectsCopy[i][OBJECT_PRICE], objectsCopy[i][OBJECT_SOLD_PRICE]);
    }
  }

  /**
  * The method ends the bidding process for a specific object
  *
  * @param objects 2D array of objects
  * @param bids 2D array of bids
  * @param numOfObjects number of objects
  * @param numOfBids number of bids
  *
  * @return void
  */
  private static void endBiddingProcess(String[][] objects, String[][] bids, int numOfObjects, int numOfBids) {
    System.out.println("Enter object ID: ");
    String objectID = userInput.nextLine();

    int objectIndex = findObjectIndex(objects, numOfObjects, objectID);
    if (objectIndex == -1) {
      System.out.println("Object not found");
      return;
    }

    int highestBidIndex = getHighestBidIndex(bids, numOfBids, objectID);
    if (highestBidIndex == -1) {
      System.out.println("No bids found for object, cannot end bidding process");
      return;
    }
    System.out.printf("Accept bid by %s (%s)? (yes/no): ", bids[highestBidIndex][BID_BIDDER], bids[highestBidIndex][BID_PRICE]);
    String answer = userInput.nextLine();
    if (answer.equalsIgnoreCase("yes")) {
      objects[objectIndex][OBJECT_STATUS] = OBJECT_STATUSES[1];
      System.out.println("Bid Accepted, object is sold");
      objects[objectIndex][OBJECT_SOLD_PRICE] = bids[highestBidIndex][BID_PRICE];
    } else {
      System.out.println("Bid not accepted, object is still unsold");
    }
  }

  /*
  * This method prints the bid history for a given object
  * @param objects 2D array of objects
  * @param bids 2D array of bids
  * @param numOfObjects number of objects
  * @param numOfBids number of bids
  *
  * @return void
  */

  private static void printBidHistory(String[][] objects, String[][] bids, int numOfObjects, int numOfBids) {
    System.out.println("Enter object ID: ");
    String objectID = userInput.nextLine();

    int objectIndex = findObjectIndex(objects, numOfObjects, objectID);
    if (objectIndex == -1) {
      System.out.println("Object not found");
      return;
    }

    System.out.printf("%-20s %-20s %-20s\n", "Bidder", "Price", "Accepted");
    for (int i = 0; i < numOfBids; i++) {
      if (bids[i][BID_OBJECT_ID].equals(objectID)) {
        if(objects[objectIndex][OBJECT_STATUS].equals(OBJECT_STATUSES[1]) && bids[i][BID_PRICE].equals(objects[objectIndex][OBJECT_SOLD_PRICE])) {
          System.out.printf("%-20s %-20s %-20s\n", bids[i][BID_BIDDER], bids[i][BID_PRICE], "Yes");
        } else {
        System.out.printf("%-20s %-20s %-20s\n", bids[i][BID_BIDDER], bids[i][BID_PRICE], "");
        }
      }
    }
  }

  /* 
  * This method registers a new bid
  *
  * @param objects 2D array of objects
  * @param bids 2D array of bids
  * @param numOfObjects number of objects
  * @param numOfBids number of bids
  *
  * @return int number of bids
  */
  private static int registerBid(String[][] objects, String[][] bids, int numOfObjects, int numOfBids) {
    System.out.println("Enter object ID: ");
    String objectID = userInput.nextLine();

    int objectIndex = findObjectIndex(objects, numOfObjects, objectID);

    if (objectIndex == -1) {
      System.out.println("Object not found");
      return numOfBids;
    }

    System.out.println("Enter bidder name: ");
    String bidderName = userInput.nextLine();

    System.out.println("Enter bid price: ");
    String bidPrice = userInput.nextLine();

    // Get the highest bid for this object
    int highestBidIndex = getHighestBidIndex(bids, numOfBids, objectID);

    if(highestBidIndex != -1) {
      int highestBid = Integer.parseInt(bids[highestBidIndex][BID_PRICE]);
      if(Integer.parseInt(bidPrice) <= highestBid) {
        System.out.printf("There is a higher bid present (%s by %s)! Could not register the bid.%n", bids[highestBidIndex][BID_PRICE], bids[highestBidIndex][BID_BIDDER]);
        return numOfBids;
      }
    }
    else {
      // Check if the bid is higher than the object price
      int objectPrice = Integer.parseInt(objects[objectIndex][OBJECT_PRICE]);
      if(Integer.parseInt(bidPrice) <= objectPrice) {
        System.out.printf("The bid is lower than the object price (%s)! Could not register the bid.%n", objects[objectIndex][OBJECT_PRICE]);
        return numOfBids;
      }
    }


    bids[numOfBids][BID_OBJECT_ID] = objectID;
    bids[numOfBids][BID_BIDDER] = bidderName;
    bids[numOfBids][BID_PRICE] = bidPrice;

    numOfBids++;

    return numOfBids;
  }

  /*
  * This method returns the index of highest bid for an object
  *
  * @param String[][] bids the array of bids
  * @param int numOfBids the number of bids
  * @param String objectID the ID of the object to find
  *
  * @return int index of the highest bid for the object or -1 if no bid is found
  */ 
  private static int getHighestBidIndex(String[][] bids, int numOfBids, String objectID) {
    int highestBidIndex = -1;
    int highestBid = 0;
    for(int i = 0; i < numOfBids; i++) {
      if(bids[i][BID_OBJECT_ID].equals(objectID)) {
        if(Integer.parseInt(bids[i][BID_PRICE]) > highestBid) {
          highestBid = Integer.parseInt(bids[i][BID_PRICE]);
          highestBidIndex = i;
        }
      }
    }
    return highestBidIndex;
  }

  /* 
  * This method returns the object index in the objects array
  *
  * @param String[][] objects the array of objects
  * @param int numOfObjects the number of objects
  * @param String objectID the ID of the object to find
  *
  * @return int the index of the object in the objects array or -1 if not found
  */
  private static int findObjectIndex(String[][] objects, int numOfObjects, String objectID) {
    for (int i = 0; i < numOfObjects; i++) {
      if (objects[i][OBJECT_ID].equalsIgnoreCase(objectID)) {
        return i;
      }
    }
    return -1;
  }

  /* This method prints all unsold objects
  * It prints the ID, address, type, asking price and highest bid (if any) for all unsold objects
  *
  * @param String[][] objects the array of objects
  * @param int numOfObjects the number of objects
  * @param String[][] bids the array of bids
  * @param int numOfBids the number of bids
  *
  * @return void
  */
  private static void printAllUnsoldObjects(String[][] objects, int numOfObjects, String[][] bids, int numOfBids) {
    System.out.printf("%-10s %-20s %-15s %-15s %-15s\n", "ID", "Address", "Type", "Asking Price", "Highest Price");

    for (int i = 0; i < numOfObjects; i++) {
      if (objects[i][OBJECT_STATUS].equalsIgnoreCase("Unsold")) {
        int highestBidIndex = getHighestBidIndex(bids, numOfBids, objects[i][OBJECT_ID]);
        if (highestBidIndex == -1) {
          System.out.printf("%-10s %-20s %-15s %-15s %-15s\n", objects[i][OBJECT_ID], objects[i][OBJECT_ADDRESS], objects[i][OBJECT_TYPE], objects[i][OBJECT_PRICE], "N/A");
        }
        else{
        System.out.printf("%-10s %-20s %-15s %-15s %-15s\n", objects[i][OBJECT_ID], objects[i][OBJECT_ADDRESS], objects[i][OBJECT_TYPE], objects[i][OBJECT_PRICE], bids[highestBidIndex][BID_PRICE]);
        }
      }
    }
  }

  /* 
  * This method registers a new object
  * An object is registered by providing its address, type, as well as its asking price.
  * In the system, it is identified by using a unique randomized ID (in range 1000-9999).
  * Make sure that multiple objects do not end up with the same ID number
  *
  * @param String[][] objects the array of objects
  * @param int numOfObjects the number of objects
  * @return int the number of objects
  */
  private static int registerNewObject(String[][] objects, int numOfObjects) {
    if (numOfObjects < MAX_OBJECTS) {
      System.out.print("Enter property's address: ");
      String address = userInput.nextLine();

      // Get valid type
      String type = "";
      do {
        System.out.print("Enter property's type (Apartment, House or Commercial): ");
        type = userInput.nextLine();
      } while (!type.equalsIgnoreCase(OBJECT_TYPES[0]) && !type.equalsIgnoreCase(OBJECT_TYPES[1]) && !type.equalsIgnoreCase(OBJECT_TYPES[2]));

      String priceString = "";
      int price = 0;
      do {
        System.out.print("Enter property's price: ");
        priceString = userInput.nextLine();
        try {
          price = Integer.parseInt(priceString);
        } catch (NumberFormatException e) {
          System.out.println("Invalid input");
        }
      } while (price <= 0);

      // Generate random ID
      int id = (int) (Math.random() * 8999) + 1000;

      // Check if ID already exists and generate id again, it it exists
      for (int i = 0; i < numOfObjects; i++) {
        if (Integer.parseInt(objects[i][OBJECT_ID]) == id) {
          id = (int) (Math.random() * 8999) + 1000;
            i = 0; // reset the index so that new id can now be compared against all others
        }
      }

      // Add object to array
      objects[numOfObjects][OBJECT_ID] = Integer.toString(id);
      objects[numOfObjects][OBJECT_ADDRESS] = address;
      objects[numOfObjects][OBJECT_TYPE] = type;
      objects[numOfObjects][OBJECT_PRICE] = priceString;
      objects[numOfObjects][OBJECT_STATUS] = OBJECT_STATUSES[0];

      numOfObjects++;

      System.out.println("Object ID: " + id);

      } else {
        System.out.println("Maximum number of objects reached");
      }

      return numOfObjects;
      }

  /*
  * This method prints the menu and asks the user to choose an option
  *
  * @return int the option chosen by the user
  */
  public static int menu() {
    System.out.print("--------------------------------\n LTU REAL ESTATE\n--------------------------------\n");
    System.out.println("1. Register new object");
    System.out.println("2. Register bid");
    System.out.println("3. End bidding process");
    System.out.println("4. Print bidding history for object");
    System.out.println("5. Print all unsold objects");
    System.out.println("6. Print all sold objects (by price)");
    System.out.println("q. End program");
    System.out.println("Enter your option: ");

    return input();

  }

  /*
  * This method asks the user to input a number
  *
  * @return int the number input by the user
  */
  public static int input() {
    int number = 0;

    while (true) {
      if (userInput.hasNextInt()) {
        String temp = userInput.nextLine();
        number = Math.abs(Integer.parseInt(temp));
        if (number > 0) {
          break;
        }
      } else if (userInput.hasNext()) {
        String inString = userInput.nextLine();
        //Q and q
        if (inString.equalsIgnoreCase("q")) {
          number = EXIT_CODE;
          break;
        }
        else {
          number = INVALID_INPUT;
          break;
        }
      }
    }
    return number;
  }
}

