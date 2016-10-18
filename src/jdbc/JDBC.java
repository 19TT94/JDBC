/*
 * Taylor Tobin and Henry Tran
 * CECS 323 - Database Fundamentals
 * Professor Dave Brown
 * JDBC Project
 */

package jdbc;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Given code
 * @author Mimi Opkins with some tweaking from Dave Brown
 * @author More tweaking from Taylor Tobin and Henry Tran
 */


public class JDBC {
    //  Database credentials
    static String DBNAME;
    //This is the specification for the printout that I'm doing:
    //each % denotes the start of a new field.
    //The - denotes left justification.
    //The number indicates how wide to make the field.
    //The "s" denotes that it's a string.  All of our output in this test are 
    //strings, but that won't always be the case.
    static final String displayFormatCol1="%-15s\n";
    static final String displayFormatCol2="%-15s%-15s\n";
    static final String displayFormatCol3="%-15s%-15s%-15s\n";
    static final String displayFormatCol4="%-15s%-15s%-15s%-15s\n";
    static final String displayFormatCol5="%-15s%-15s%-15s%-15s%-15s\n";
    // JDBC driver name and database URL
    static final String JDBC_DRIVER = "org.apache.derby.jdbc.ClientDriver";
    static String DB_URL = "jdbc:derby://localhost:1527/";
//            + "testdb;user=";
/**
 * Takes the input string and outputs "N/A" if the string is empty or null.
 * @param input The string to be mapped.
 * @return  Either the input string or "N/A" as appropriate.
 */
    public static String dispNull (String input) {
        //because of short circuiting, if it's null, it never checks the length.
        if (input == null || input.length() == 0)
            return "N/A";
        else
            return input;
    }
    
    public static void main(String[] args) {
        //Initial app log
        System.out.println("Writing Groups Database Connection\n\n");
        
        //Prompt the user for the database name, and the credentials.
        //If your database has no credentials, you can update this code to 
        //remove that from the connection string.
        Scanner in = new Scanner(System.in);
        System.out.print("Name of the database (not the user account): ");
        DBNAME = in.nextLine();
      
        //Constructing the database URL connection string
        DB_URL = DB_URL + DBNAME;
        Connection conn = null; //initialize the connection
        Statement stmt = null;  //initialize the statement that we're using
        try {
            //STEP 2: Register JDBC driver
            Class.forName("org.apache.derby.jdbc.ClientDriver");

            //STEP 3: Open a connection
            System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(DB_URL);
            
            System.out.println("\nNo Errors Connected\n");
            
            System.out.println("Hit 'enter' to begin queries (type 'exit' then hit 'enter' to quit at any time)");
            
            String sql = null;
            ResultSet rs = null;
            
            while(!(in.nextLine().equals("exit"))) {
                System.out.println("Information for queries listed below:");
                System.out.println("(enter number for the desired table listing or action)");
                System.out.println("Tables:\n1). Writing Groups\n2). Publishers\n3). Book Titles\n");
                System.out.println("\nUpdate Database:\n4). Add a Publisher\n5). Add a new Book\n6). Remove a book\n");

                int select = 0;
                System.out.println("Select corresponding number for query, then hit 'enter':");
                select = checkSetRange(validInt(in.nextLine()), 0, 7);
                          
                // Queries for each selection
                if(select == 1) {
                    System.out.println("In one");
                    //STEP 4: Execute a query
                    System.out.println("\nCreating statement...\n");
                    stmt = conn.createStatement();
                    sql = "SELECT groupname FROM writinggroup";
                    rs = stmt.executeQuery(sql);

                    //STEP 5: Extract data from result set
                    System.out.printf(displayFormatCol2, "Writing Groups", "Group Name");
                    Integer count = 0;
                    ArrayList arr = new ArrayList();
                    while (rs.next()) {
                        count++;
                        //Retrieve by column name
                        String name = rs.getString("groupname");
                        arr.add(name);
                        
                        //Display values
                        System.out.printf(displayFormatCol2, dispNull(count.toString()), dispNull(name));
                    }
                    System.out.println("");
                    
                    System.out.println("Would You like to know more about a specific Writing group? (y/n)");
                    char answer = validBool(in.nextLine());
                    
                    if(answer == 'y') {
                        System.out.println("Enter corresponding number for more data on specific group");
                        int select2 = checkSetRange(validInt(in.nextLine()), 0, (count + 1));
                        
                        if(select2 == 0)
                            continue;
                        
                        System.out.println("\nCreating Statement...\n");
                        sql = "SELECT groupname, headwriter, yearformed, subject FROM writinggroup WHERE groupname = " + "'" +arr.get(select2-1) + "'";
                        rs = stmt.executeQuery(sql);

                        //STEP 5: Extract data from result set
                        System.out.printf(displayFormatCol4, "Group Name", "Head Writer", "Year Formed", "Subject");
                        while (rs.next()) {
                            //Retrieve by column name
                            String name = rs.getString("groupname");
                            String head = rs.getString("headwriter");
                            String year = rs.getString("yearformed");
                            String subject = rs.getString("subject");

                            //Display values
                            System.out.printf(displayFormatCol4, dispNull(name), dispNull(head), dispNull(year), dispNull(subject));
                        }
                        System.out.println("");
                    }
                                        
                }

                if(select == 2) {
                    //STEP 4: Execute a query
                    System.out.println("\nCreating statement...\n");
                    stmt = conn.createStatement();
                    sql = "SELECT publishername FROM publishers";
                    rs = stmt.executeQuery(sql);

                    //STEP 5: Extract data from result set
                    System.out.printf(displayFormatCol2, "Publishers", "Publisher Name");
                    Integer count = 0;
                    ArrayList arr = new ArrayList();
                    while (rs.next()) {
                        count++;
                        //Retrieve by column name
                        String name = rs.getString("publishername");
                        arr.add(name);
                        
                        //Display values
                        System.out.printf(displayFormatCol2, dispNull(count.toString()), dispNull(name));
                    }
                    System.out.println("");
                    
                    System.out.println("Would You like to know more about a specific Publisher? (y/n)");
                    char answer = validBool(in.nextLine());
                    
                    if(answer == 'y') {
                        System.out.println("Enter corresponding number for more data on specific group");
                        int select2 = checkSetRange(validInt(in.nextLine()), 0, (count + 1));
                        
                        if(select2 == 0)
                            continue;
                        
                        System.out.println("\nCreating Statement...\n");
                        sql = "SELECT * FROM publishers WHERE publishername = " + "'" +arr.get(select2-1) + "'";
                        rs = stmt.executeQuery(sql);

                        //STEP 5: Extract data from result set
                        System.out.printf(displayFormatCol4, "Publisher Name", "Publisher Address", "Publisher Phone", "Publisher Email");
                        while (rs.next()) {
                            //Retrieve by column name
                            String name = rs.getString("publishername");
                            String add = rs.getString("publisheraddress");
                            String phone = rs.getString("publisherphone");
                            String email = rs.getString("publisheremail");

                            //Display values
                            System.out.printf(displayFormatCol4, dispNull(name), dispNull(add), dispNull(phone), dispNull(email));
                        }
                        System.out.println("");
                    }
                    
                }

                if(select == 3) {
                     //STEP 4: Execute a query
                    System.out.println("\nCreating statement...\n");
                    stmt = conn.createStatement();
                    sql = "SELECT booktitle FROM books";
                    rs = stmt.executeQuery(sql);
                    
                    Integer count = 0;
                    ArrayList arr = new ArrayList();
                    //STEP 5: Extract data from result set
                    System.out.printf(displayFormatCol2, "Books", "Book Titles");
                    while (rs.next()) {
                        count++;
                        //Retrieve by column name
                        String name = rs.getString("booktitle");
                        arr.add(name);
                        
                        //Display values
                        System.out.printf(displayFormatCol2, dispNull(count.toString()), dispNull(name));
                    }
                    System.out.println("");
                    
                    System.out.println("Would You like to know more about a specific Publisher? (y/n)");
                    char answer = validBool(in.nextLine());
                    
                    if(answer == 'y') {
                        System.out.println("Enter corresponding number for more data on specific group");
                        int select2 = checkSetRange(validInt(in.nextLine()), 0, (count + 1));
                        
                        if(select2 == 0)
                            continue;
                        
                        System.out.println("\nCreating Statement...\n");
                        sql = "SELECT * FROM books WHERE booktitle = " + "'" +arr.get(select2-1) + "'";
                        rs = stmt.executeQuery(sql);

                        //STEP 5: Extract data from result set
                        System.out.printf(displayFormatCol5, "Group Name", "Book Title", "Publisher Name", "Year Published", "Number of Pages");
                        while (rs.next()) {
                            //Retrieve by column name
                            String name = rs.getString("groupname");
                            String title = rs.getString("booktitle");
                            String pub = rs.getString("publishername");
                            String year = rs.getString("yearpublished");
                            String pages = rs.getString("numberpages");

                            //Display values
                            System.out.printf(displayFormatCol5, dispNull(name), dispNull(title), dispNull(pub), dispNull(year), dispNull(pages));
                        }
                        System.out.println("");
                    }
                    
                }

                if(select == 4) {
                    System.out.println("What is the name of the publisher?");
                    String name = in.nextLine();
                    System.out.println("What is the publisher's address?");
                    String address = in.nextLine();
                    System.out.println("What is the publisher's phone?");
                    String phone = in.nextLine();
                    System.out.println("What is the publisher's email?");
                    String email = in.nextLine();
                    
                    //STEP 4: Execute a query
                    System.out.println("Creating statement...");
                    stmt = conn.createStatement();
                    
                    sql = "INSERT INTO publishers " + "VALUES ('" + name + "', '" + address + "', '" + phone + "','" + email + "')";
                    System.out.println(sql);
                    stmt.executeUpdate(sql);

                    System.out.println("...Table Updated");
                    System.out.println("To see results hit enter and choose books table");
                    System.out.println("");
                }
                System.out.println("Hit 'enter' for another query or 'exit' to end program");
          
                if(select == 5) {
                    System.out.println("Which writing group authored the book?");
                    String group = in.nextLine();
                    System.out.println("What is the title of the book?");
                    String title = in.nextLine();
                    System.out.println("What is the name of the publisher?");
                    String pub = in.nextLine();
                    System.out.println("What year was it published? (YYYY-MM-DD)");
                    String year;
                    String date = in.nextLine();
                    if(isValidDate(date) == true) {
                       year = date;
                    }
                    else {
                        System.out.println("Invalid date entry, try again");
                        continue;
                    }
                    System.out.println("How many pages are in the book?");
                    String pages = in.nextLine();
                    
                    //STEP 4: Execute a query
                    System.out.println("Creating statement...");
                    stmt = conn.createStatement();
                    
                    sql = "INSERT INTO books " + "VALUES ('" + group + "', '" + title + "', '" + pub + "','" + year + "', " + pages + ")";
                    System.out.println(sql);
                    stmt.executeUpdate(sql);

                    System.out.println("...Table Updated");
                    System.out.println("To see results hit enter and choose books table");
                    System.out.println("");
                }
                
                if(select == 6) {
                    stmt = conn.createStatement();
                    sql = "SELECT booktitle FROM books";
                    rs = stmt.executeQuery(sql);
                    
                    Integer count = 0;
                    ArrayList arr = new ArrayList();
                    //STEP 5: Extract data from result set
                    System.out.printf(displayFormatCol2, "Books", "Book Titles");
                    while (rs.next()) {
                        count++;
                        //Retrieve by column name
                        String name = rs.getString("booktitle");
                        arr.add(name);
                        
                        //Display values
                        System.out.printf(displayFormatCol2, dispNull(count.toString()), dispNull(name));
                    }
                    
                    System.out.println("Enter corresponding number of title to be deleted");
                    int select2 = checkSetRange(validInt(in.nextLine()), 0, (count + 1));
                        
                    if(select2 == 0)
                        continue;
                    
                    //STEP 4: Execute a query
                    System.out.println("Creating statement...");
                    stmt = conn.createStatement();
                    
                    sql = "Delete From Books where booktitle = '" + arr.get(select2-1) + "'";
                    System.out.println(sql);
                    stmt.executeUpdate(sql);

                    System.out.println("...Table Updated");
                    System.out.println("To see results hit enter and choose books table");
                    System.out.println("");
                }
                
            }

             //STEP 6: Clean-up environment
            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException se) {
            //Handle errors for JDBC
            se.printStackTrace();
        } catch (Exception e) {
            //Handle errors for Class.forName
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException se2) {
            }
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }
    
    public static int validInt(String entry) {
        if(entry.equals("exit")) {
            System.exit(0);
        }
        
        //make sure value entered is an integer
        int value = 0;
        try {
            value = Integer.parseInt(entry); 
        } catch(NumberFormatException e) {
            System.out.println("Not a valid Integer please re-try");
        }
        
        return value;
    }
    
    public static int checkSetRange(int entry, int low, int high) {
        if(entry > low && entry < high)
            return entry;
        else
            System.out.println("Invalid entry, must be in range");
            return 0;
    }
    
    public static char validBool(String entry) {
        if(entry.equals("exit")) {
            System.exit(0);
        }
        
        char valid = entry.charAt(0);
        if(valid == 'y' || valid == 'n') {
            return valid;
        }
        else {
            System.out.println("Invalid entry, must be 'y' or 'n'");
            return 'e';
        }
 
    }
    
    public static boolean isValidDate(String inDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);
        try {
          dateFormat.parse(inDate.trim());
        } catch (ParseException pe) {
          return false;
        }
        return true;
    }

}


