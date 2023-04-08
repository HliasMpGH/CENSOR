// database
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.PreparedStatement;

// https
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

// regex
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import java.util.Scanner;

public class Censor {

    static Connection conn = null;

    static Scanner input = new Scanner(System.in);
    public static void main(String[] args) {

        try {

            connect();


            String bad = httpReq();

            Pattern p = Pattern.compile("wtf");

            Matcher m = p.matcher(bad);
            System.out.println(m.find());

            int num = m.end() - m.start(); // length of the word
            var sb = new StringBuilder();

            for (int i = 0; i < num; i++) {
                sb.append("*");
            }
            System.out.println(m.replaceAll(sb.toString()));
             
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
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    // used to establish the connection to the DB
    public static void connect() {
        try {
            // db parameters
            String url = "jdbc:sqlite:C:/sqlite/db/newDB.db";
            // create a connection to the database
            conn = DriverManager.getConnection(url);
            
            System.out.println("Connection to SQLite has been established.");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static String httpReq() {

        var content = new StringBuilder();

        try {
            URL url = new URL("http://www.bannedwordlist.com/lists/swearWords.txt");
            var connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            var in = new BufferedReader(
                new InputStreamReader(connection.getInputStream()));
       
            String inputLine;

            
            
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine + "\n");
            }
            in.close();
            
           
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return content.toString();
    }

}
