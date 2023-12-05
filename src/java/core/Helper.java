package core;

import java.sql.*;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import kong.unirest.core.HttpResponse;
import kong.unirest.core.JsonNode;
import kong.unirest.core.Unirest;
import kong.unirest.core.json.JSONObject;

public class Helper {

    public Helper() {}
    
    public double getCustomerBalance(String accountNo) {
        try(Connection conn = getDBConnection()){
            String query = "SELECT * FROM account WHERE accountNumber = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, accountNo); 
            ResultSet rs = stmt.executeQuery();
            if(rs.next()){
                return rs.getDouble("balance");
            }
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(Helper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }
    
    public boolean debitAccount(String accountNumber, String amount){
        try(Connection conn = getDBConnection()){
            String query = "UPDATE account SET balance = balance - ? "
                    + "WHERE accountNumber = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setDouble(1, Double.parseDouble(amount));
            stmt.setString(2, accountNumber); 
            stmt.execute();
            
            return true;
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(Helper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
    public boolean creditAccount(String accountNumber, String amount){
        try(Connection conn = getDBConnection()){
            String query = "UPDATE account SET balance = balance + ? "
                    + "WHERE accountNumber = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setDouble(1, Double.parseDouble(amount));
            stmt.setString(2, accountNumber); 
            stmt.execute();
            
            return true;
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(Helper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
    //<editor-fold defaultstate="collapsed" desc="DB Connection">
    public Connection getDBConnection() throws ClassNotFoundException,
            SQLException{
        String username = "root";
        String password = "password";
        String url = "jdbc:mysql://localhost:3306/web_class?useSSL=false";
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection(url, username, password);
    }//</editor-fold>

    JSONObject topup(String phone, String amount) {
        Unirest.config().verifySsl(false);
        Unirest.config().connectTimeout(30000);
        HttpResponse<JsonNode> res = Unirest.get("https://vtu.ng/wp-json/api/v1/airtime")
                .queryString("username", "")
                .queryString("password", "")
                .queryString("phone", phone)
                .queryString("network_id", "mtn")
                .queryString("amount", amount)
                .asJson();
        JSONObject e_res = res.getBody().getObject();
        return e_res;
    }

    String createAccount(String phone, String name) {
        String accountNumber = genAN();
        try(Connection conn = getDBConnection()){
            String query = "INSERT INTO customer(accountNumber,phone,name) "
                    + "VALUES(?,?,?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, accountNumber);
            stmt.setString(2, phone);
            stmt.setString(3, name);
            stmt.execute();
            
            query = "INSERT INTO account(accountNumber) VALUES(?)";
            stmt = conn.prepareStatement(query);
            stmt.setString(1, accountNumber);
            stmt.execute();
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(Helper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return accountNumber;
    }
    
        public boolean sendFund(String senderAccount, String recieverAccount, String amount){
        try(Connection conn = getDBConnection()){
            String query = "SELECT balance FROM account WHERE accountNumber = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, senderAccount); 
            ResultSet rs = stmt.executeQuery();
            double amt = Double.parseDouble(amount);
            double fee = amt<5000?10:25;
            if(rs.next() && rs.getDouble("balance")>=(amt+fee)){
                debitAccount(senderAccount, amount);
                debitAccount(senderAccount, String.valueOf(fee));
                creditAccount(recieverAccount, amount);
                return true;
            }else{
                return false;
            }
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(Helper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
        
         
          public boolean Loan(String accountNumber, String loanAmount, String income, String duration, String purpose){
        try(Connection conn = getDBConnection()){
            String query = "INSERT INTO loan (accountNumber, loanAmount, income, duration, purpose) "
                    + "VALUES(? , ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, accountNumber); 
            stmt.setDouble(2, Double.parseDouble(loanAmount));
            stmt.setString(3, income); 
            stmt.setString(4, duration); 
            stmt.setString(5, purpose); 
            stmt.execute();
            
            return true;
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(Helper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }


    private String genAN() {
        Random gen = new Random();
        String nos = String.valueOf(gen.nextLong(999999999));
        int len = nos.length();
        while(len<10){
            nos = "0"+nos;
            len++;
        }
        return nos;
    }
    
    
}
