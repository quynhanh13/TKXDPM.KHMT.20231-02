package entity.order;

import entity.db.AIMSDB;
import entity.media.Media;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class OrderMedia {

    private Media media;
    private int price;
    private int quantity;

    public OrderMedia(Media media, int quantity, int price) {
        this.media = media;
        this.quantity = quantity;
        this.price = price;
    }

    @Override
    public String toString() {
        return "{" +
                "  media='" + media + "'" +
                ", quantity='" + quantity + "'" +
                ", price='" + price + "'" +
                "}";
    }

    public Media getMedia() {
        return this.media;
    }

    public void setMedia(Media media) {
        this.media = media;
    }

    public int getQuantity() {
        return this.quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getPrice() {
        return this.price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void saveOrderMedia(int orderID) throws SQLException {
        Connection connection = AIMSDB.getConnection();
        PreparedStatement preparedStatement = null;
        try {
            String sql = "INSERT INTO `OrderMedia` (orderID, price, quantity, mediaID) VALUES (?, ?, ?, ?)";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, orderID);
            preparedStatement.setInt(2,this.getPrice());
            preparedStatement.setInt(3,this.getQuantity());
            preparedStatement.setInt(4,media.getId());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
        }
    }
}
