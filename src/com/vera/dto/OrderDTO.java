package com.vera.dto;

import com.vera.dao.OrderDAO;
import com.vera.dao.UserDAO;

import java.util.HashMap;
import java.util.Objects;

public class OrderDTO
{
    private int id;
    private int objectID;
    private int userC;
    private int userV;
    private String date_order;

    public OrderDTO(int id, int objectID, int userC, int userV, String date_order) {
        this.id = id;
        this.objectID = objectID;
        this.userC = userC;
        this.userV = userV;
        this.date_order = date_order;
    }

    public OrderDTO(int objectID, int userC, int userV) {
        this.objectID = objectID;
        this.userC = userC;
        this.userV = userV;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getObjectID() {
        return objectID;
    }

    public void setObjectID(int objectID) {
        this.objectID = objectID;
    }

    public int getUserC() {
        return userC;
    }

    public void setUserC(int userC) {
        this.userC = userC;
    }

    public int getUserV() {
        return userV;
    }

    public void setUserV(int userV) {
        this.userV = userV;
    }

    public String getDate_order() {
        return date_order;
    }

    public void setDate_order(String date_order) {
        this.date_order = date_order;
    }

    public static OrderDTO getOrderByID(int id)
    {
        HashMap<String, HashMap<String, String>> columnNames = new HashMap<>();
        HashMap<String, String> dataID = new HashMap<>();

        dataID.put("Integer", id + "");
        columnNames.put("id", dataID);

        return OrderDAO.getByID(columnNames);
    }

    public OrderDTO insertIntoDatabase()
    {
        HashMap<String, HashMap<String, String>> columnNames = new HashMap<>();
        HashMap<String, String> objectID = new HashMap<>();
        HashMap<String, String> userBuyer = new HashMap<>();
        HashMap<String, String> userSeller = new HashMap<>();

        objectID.put("Integer", this.objectID + "");
        userBuyer.put("Integer", this.userC + "");
        userSeller.put("Integer", this.userV + "");

        columnNames.put("objectID", objectID);
        columnNames.put("userC", userBuyer);
        columnNames.put("userV", userSeller);

        return OrderDAO.insertIntoDatabase(columnNames);
    }

    @Override
    public String toString() {
        return "OrderDTO{" +
                "id=" + id +
                ", objectID=" + objectID +
                ", userC=" + userC +
                ", userV=" + userV +
                ", date_order='" + date_order + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderDTO orderDTO = (OrderDTO) o;
        return id == orderDTO.id && objectID == orderDTO.objectID && userC == orderDTO.userC && userV == orderDTO.userV && Objects.equals(date_order, orderDTO.date_order);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, objectID, userC, userV, date_order);
    }
}
