package entity.invoice;

import entity.db.AIMSDB;
import entity.order.Order;

import java.sql.*;
import java.util.ArrayList;

public class Invoice {

    private int Id;
    private Order order;
    private int amount;
    private String paypalId;
    private String status = "CREATED";

    public Invoice(){

    }

    public Invoice(Order order){
        this.order = order;
    }

    public Invoice(Order order, String id){
        this.order = order;
        this.paypalId = id;
        this.amount = order.getAmount() + order.getShippingFees();
    }

    public Order getOrder() {
        return order;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void setId(int id){this.Id = id;}
    public int getId(){return this.Id;}

    public int getAmount() {
        return amount;
    }

    public String getPaypalId(){ return this.paypalId;}

    public void setPaypalId(String paypalId){ this.paypalId = paypalId;}

    public String getStatus(){return status;}

    public void setStatus(String status){this.status=status;}

    public void updateStatus(String status){
        this.status = status;
        Connection connection = AIMSDB.getConnection();
        PreparedStatement preparedStatement = null;
        try{
            String sql = "UPDATE invoice SET status = ? WHERE paypalId = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,status);
            preparedStatement.setString(2, this.getPaypalId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void saveInvoice() throws SQLException {
        Connection connection = AIMSDB.getConnection();
        PreparedStatement preparedStatement = null;
        try {
            String sql = "INSERT INTO `invoice` (orderId, amount, paypalId, status) VALUES (?, ?, ?, ?)";
            preparedStatement = connection.prepareStatement(sql);

            // Giả sử getAmount() và getUrlPayOrder() trả về giá trị thích hợp
            preparedStatement.setInt(1, order.getID()); // orderId
            preparedStatement.setDouble(2, this.getAmount()); // amount
            preparedStatement.setString(3, this.getPaypalId()); // paypalId
            preparedStatement.setString(4,this.getStatus()); // status
            preparedStatement.executeUpdate();

            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            int generatedId = generatedKeys.getInt(1);
            this.setId(generatedId);
        } finally {
            // Đóng PreparedStatement để tránh rò rỉ nguồn và giải phóng tài nguyên
            if (preparedStatement != null) {
                preparedStatement.close();
            }
        }
    }

    public static ArrayList<Invoice> getListInvoice() throws SQLException {
        Statement stm = AIMSDB.getConnection().createStatement();
        ResultSet res = stm.executeQuery("SELECT * from Invoice");
        ArrayList<Invoice> invoices = new ArrayList<>();
        while (res.next()) {
            Invoice invoice = new Invoice();
            invoice.setId(res.getInt("id"));
            invoice.setAmount(res.getInt("amount"));
            invoice.setPaypalId(res.getString("paypalId"));
            invoice.setStatus(res.getString("status"));
            invoices.add(invoice);
        }
        return invoices;
    }

}
