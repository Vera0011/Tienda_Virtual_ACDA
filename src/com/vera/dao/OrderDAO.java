package com.vera.dao;

import com.vera.dto.OrderDTO;
import com.vera.dto.UserDTO;
import com.vera.init.DatabaseManager;

import java.util.HashMap;

public class OrderDAO
{
    public static OrderDTO insertIntoDatabase(HashMap<String, HashMap<String, String>> mapArgs)
    {
        return (OrderDTO) DatabaseManager.insertValues("Order", mapArgs);
    }

    public static OrderDTO getByID(HashMap<String, HashMap<String, String>> mapArgs)
    {
        return (OrderDTO) DatabaseManager.selectValues("Order", mapArgs);
    }
}
