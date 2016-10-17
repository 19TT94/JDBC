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
    static final String displayFormat="%-15s%-15s%-15s%-15s\n";
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
             
            System.out.println("Information for queries listed below:");
            System.out.println("(enter number for the desired table listing or action)");
            System.out.println("Tables:\n1). Writing Groups\n2). Publishers\n3). Book Titles\n");
            System.out.println("\nUpdate Database:\n4). Add a Publisher\n5). Add a new Book\n6). Remove a book\n");

            int select = 0;
            System.out.println("Select corresponding number for query, then hit 'enter':");
            select = in.nextInt();
            validInt(select);
            
            String sql = null;
            ResultSet rs = null;
            // Queries for each selection
            if(select == 1) {
                //STEP 4: Execute a query
                System.out.println("\nCreating statement...\n");
                stmt = conn.createStatement();
                sql = "Select * From WritingGroup";
                rs = stmt.executeQuery(sql);

                //STEP 5: Extract data from result set
                System.out.printf(displayFormat, "Group Name", "Head Writer", "Year Formed", "Subject");
                while (rs.next()) {
                    //Retrieve by column name
                    String id = rs.getString("groupname");
                    String phone = rs.getString("headwriter");
                    String first = rs.getString("yearformed");
                    String last = rs.getString("subject");

                    //Display values
                    System.out.printf(displayFormat, 
                            dispNull(id), dispNull(first), dispNull(last), dispNull(phone));
                }
            }
            
            if(select == 2) {
                //STEP 4: Execute a query
                System.out.println("Creating statement...");
                stmt = conn.createStatement();
                sql = "SELECT groupname FROM writinggroup";
                rs = stmt.executeQuery(sql);

                //STEP 5: Extract data from result set
                System.out.printf(displayFormat, "Group Name", "Head Writer", "Year Formed", "Subject");
                while (rs.next()) {
                    //Retrieve by column name
                    String name = rs.getString("groupname");
                    String writer = rs.getString("headwriter");
                    String year = rs.getString("yearformed");
                    String sub = rs.getString("subject");

                    //Display values
                    System.out.printf(displayFormat, 
                            dispNull(name), dispNull(writer), dispNull(year), dispNull(sub));
                }
            }
            
            if(select == 3) {
                //STEP 4: Execute a query
                System.out.println("Creating statement...");
                stmt = conn.createStatement();
                sql = "SELECT au_id, au_fname, au_lname, phone FROM Authors";
                rs = stmt.executeQuery(sql);

                //STEP 5: Extract data from result set
                System.out.printf(displayFormat, "ID", "First Name", "Last Name", "Phone #");
                while (rs.next()) {
                    //Retrieve by column name
                    String id = rs.getString("au_id");
                    String phone = rs.getString("phone");
                    String first = rs.getString("au_fname");
                    String last = rs.getString("au_lname");

                    //Display values
                    System.out.printf(displayFormat, 
                            dispNull(id), dispNull(first), dispNull(last), dispNull(phone));
                }
                //STEP 6: Clean-up environment
            }
            
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
    
    public static void validInt(int entry) {
        if(entry < 0 || entry > 7) {
            try {
                throw new InvalidIntException("Integer selection must be less than 7 and non-negative, please re-enter");
            } catch (InvalidIntException ex) {
                Logger.getLogger(JDBC.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InputMismatchException e) {
                Logger.getLogger(JDBC.class.getName()).log(Level.SEVERE, null, e);
            }
        }
    }
}
