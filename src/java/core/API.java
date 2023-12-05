package core;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import org.json.JSONObject;

/**
 * REST Web Service
 *
 * @author louis
 */
@Path("start")
public class API {
  
    JSONObject json;
    Helper helper;
    
    public API() {
        helper = new Helper();
    }

    //<editor-fold defaultstate="collapse" desc="Test Endpoint">
    @Path("test_access")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String test() {
        json = new JSONObject();
        json.put("status", "200");
        return json.toString();
    }//</editor-fold>

    //<editor-fold defaultstate="collapse" desc="Account Balance">
    @Path("account_balance")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public String getBalance(@FormParam("accountNumber") 
            String accountNo) {
        json = new JSONObject();
        json.put("status", "200");
        json.put("balance", helper.getCustomerBalance(accountNo));
        return json.toString();
    }//</editor-fold>
    
    //<editor-fold defaultstate="collapse" desc="Debit">
    @Path("debit")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public String debit(@FormParam("accountNumber") String accountNumber,
            @FormParam("amount") String amount) {
        json = new JSONObject();
        json.put("success", helper.debitAccount(accountNumber,amount));
        return json.toString();
    }//</editor-fold>
    
    //<editor-fold defaultstate="collapse" desc="Credit">
    @Path("credit")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public String credit(@FormParam("accountNumber") String accountNumber,
            @FormParam("amount") String amount) {
        json = new JSONObject();
        json.put("success", helper.creditAccount(accountNumber,amount));
        return json.toString();
    }//</editor-fold>
    
    //<editor-fold defaultstate="collapse" desc="Airtime Topup">
    @Path("airtime")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public String airtime(@FormParam("phone") String phone, 
            @FormParam("amount") String amount) {
        return helper.topup(phone,amount).toString();
    }//</editor-fold>

    //<editor-fold defaultstate="collapse" desc="Internal Transfer">
    @Path("transfer")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public String transfer(@FormParam("senderAccount") String senderAccount,
            @FormParam("recieverAccount") String recieverAccount,
            @FormParam("amount") String amount) {
        json = new JSONObject();
        json.put("success", helper.sendFund(senderAccount, recieverAccount, amount));
        return json.toString();
    }//</editor-fold>
    
    //<editor-fold defaultstate="collapse" desc="Open Account">
    @Path("loan")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public String loan(@FormParam("accountNumber") String accountNumber,
            @FormParam("loanAmount") String loanAmount,
            @FormParam("income") String income,
            @FormParam("duration") String duration,
            @FormParam("purpose") String purpose) {
        json = new JSONObject();
        json.put("accountNumber", 
                helper.Loan(accountNumber, loanAmount, income, duration, purpose));
        return json.toString();
    }//</editor-fold>
    
    //<editor-fold defaultstate="collapse" desc="Template">
    @Path("tpl")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public String methodName(String content) {
        json = new JSONObject();
        
        return json.toString();
    }//</editor-fold>
}
