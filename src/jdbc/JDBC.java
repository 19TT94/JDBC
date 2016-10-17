/*
 * Taylor Tobin and Henry Tran
 * CECS 323 - Database Fundamentals
 * Professor Dave Brown
 * JDBC Project
 */

package jdbc;

import java.sql.*;
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
                select = validInt(in.nextLine());
                          
                // Queries for each selection
                if(select == 1) {
                    //STEP 4: Execute a query
                    System.out.println("\nCreating statement...\n");
                    stmt = conn.createStatement();
                    sql = "SELECT groupname FROM writinggroup";
                    rs = stmt.executeQuery(sql);

                    //STEP 5: Extract data from result set
                    System.out.printf(displayFormatCol1, "Group Name");
                    while (rs.next()) {
                        //Retrieve by column name
                        String name = rs.getString("groupname");

                        //Display values
                        System.out.printf(displayFormatCol1, dispNull(name));
                    }
                    System.out.println("");
                }

                if(select == 2) {
                    //STEP 4: Execute a query
                    System.out.println("\nCreating statement...\n");
                    stmt = conn.createStatement();
                    sql = "SELECT publishername FROM publishers";
                    rs = stmt.executeQuery(sql);

                    //STEP 5: Extract data from result set
                    System.out.printf(displayFormatCol1, "Publisher");
                    while (rs.next()) {
                        //Retrieve by column name
                        String name = rs.getString("publishername");

                        //Display values
                        System.out.printf(displayFormatCol1, dispNull(name));
                    }
                    System.out.println("");
                }

                if(select == 3) {
                     //STEP 4: Execute a query
                    System.out.println("\nCreating statement...\n");
                    stmt = conn.createStatement();
                    sql = "SELECT booktitle FROM books";
                    rs = stmt.executeQuery(sql);

                    //STEP 5: Extract data from result set
                    System.out.printf(displayFormatCol1,"Books");
                    while (rs.next()) {
                        //Retrieve by column name
                        String name = rs.getString("booktitle");

                        //Display values
                        System.out.printf(displayFormatCol1, dispNull(name));
                    }
                    System.out.println("");
                }

                if(select == 4) {
                    //STEP 4: Execute a query
                    System.out.println("Creating statement...");
                    stmt = conn.createStatement();
                    sql = "SELECT au_id, au_fname, au_lname, phone FROM Authors";
                    rs = stmt.executeQuery(sql);

                    //STEP 5: Extract data from result set
                    System.out.printf(displayFormatCol4, "ID", "First Name", "Last Name", "Phone #");
                    while (rs.next()) {
                        //Retrieve by column name
                        String id = rs.getString("au_id");
                        String phone = rs.getString("phone");
                        String first = rs.getString("au_fname");
                        String last = rs.getString("au_lname");

                        //Display values
                        System.out.printf(displayFormatCol4, 
                                dispNull(id), dispNull(first), dispNull(last), dispNull(phone));
                    }
                    System.out.println("");
                }
                System.out.println("Hit 'enter' for another query or 'exit' to end program");
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
        int value = Integer.parseInt(entry);
        if(value > 0 && value < 7) {
            return value;
        }
        else
            return  InvalidIntException("Integer selection must be less than 7 and non-negative, please re-enter");
    }

    private static int InvalidIntException(String message) {
        throw new UnsupportedOperationException("message");
    }
    
}
