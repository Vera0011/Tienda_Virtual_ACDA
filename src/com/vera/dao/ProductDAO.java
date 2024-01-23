package com.vera.dao;

import com.vera.dto.ProductDTO;
import com.vera.init.DatabaseManager;

import java.util.HashMap;

public class ProductDAO
{
    public static ProductDTO insertIntoDatabase(HashMap<String, HashMap<String, String>> mapArgs)
    {
        return (ProductDTO) DatabaseManager.insertValues("Product", mapArgs);
    }

    public static ProductDTO getByID(HashMap<String, HashMap<String, String>> mapArgs)
    {
        return (ProductDTO) DatabaseManager.selectValues("Product", mapArgs);
    }
}
