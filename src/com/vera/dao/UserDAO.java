package com.vera.dao;

import com.vera.dto.ProductDTO;
import com.vera.dto.UserDTO;
import com.vera.init.DatabaseManager;

import java.util.HashMap;

public class UserDAO
{
    public static UserDTO insertIntoDatabase(HashMap<String, HashMap<String, String>> mapArgs)
    {
        return (UserDTO) DatabaseManager.insertValues("User", mapArgs);
    }

    public static UserDTO getByID(HashMap<String, HashMap<String, String>> mapArgs)
    {
        return (UserDTO) DatabaseManager.selectValues("User", mapArgs);
    }
}
