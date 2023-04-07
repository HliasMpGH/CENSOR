import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.PreparedStatement;

import java.util.Scanner;

public class Censor {

    static Scanner input = new Scanner(System.in);
    public static void main(String[] args) {
        Connection conn = null;
        try {
            // db parameters
            String url = "jdbc:sqlite:C:/sqlite/db/newDB.db";
            // create a connection to the database
            conn = DriverManager.getConnection(url);
            
            System.out.println("Connection to SQLite has been established.");


            while (true) {
                System.out.print("enter some words: ");
                String words = input.nextLine();
    
                String insQuery = "insert into messages(text, time) " +
                                  "values (?, datetime('now', 'localtime'))" ;
                PreparedStatement stmt = conn.prepareStatement(insQuery);
                stmt.setString(1, words);
                stmt.executeUpdate();   
                
                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery("select * from messages");
    
                System.out.println("ID \t TEXT \t TIME");
                while (rs.next()) {
    
                    System.out.print(rs.getString("id") + "\t");
                    System.out.print(rs.getString("text") + "\t");
                    System.out.println(rs.getString("time"));
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }
}