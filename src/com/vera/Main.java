package com.vera;

import com.vera.dto.OrderDTO;
import com.vera.dto.ProductDTO;
import com.vera.dto.UserDTO;
import com.vera.init.DatabaseManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.Assert.*;

public class Main
{
    private static DatabaseManager manager;

    @BeforeEach
    public void init()
    {
        if(manager != null) DatabaseManager.removeDatabase();
        manager = new DatabaseManager("root", "root");
    }

    @Test
    public void testAddingOrder()
    {
        ProductDTO product = new ProductDTO("testing_adding_order", 20.20).insertIntoDatabase();
        UserDTO userBuyer = new UserDTO("testing_adding_order_buyer", "testing_adding_order_buyer_2", "testing_adding_order_buyer@gmail.com", "testing_adding_order_buyer").insertIntoDatabase();
        UserDTO userSeller = new UserDTO("testing_adding_order_seller", "testing_adding_order_seller", "testing_adding_order_seller@gmail.com", "testing_adding_order_seller").insertIntoDatabase();

        //User do not exist (buyer)
        OrderDTO orderFailed = new OrderDTO(product.getId(), userBuyer.getId(), 500).insertIntoDatabase();
        assertNull(orderFailed);

        // User do not exist (seller)
        OrderDTO orderFailed2 = new OrderDTO(product.getId(), 500, userSeller.getId()).insertIntoDatabase();
        assertNull(orderFailed2);

        // User do not exist (buyer and seller)
        OrderDTO orderFailed3 = new OrderDTO(product.getId(), 500, 500).insertIntoDatabase();
        assertNull(orderFailed3);

        // Product do not exist
        OrderDTO orderFailed4 = new OrderDTO(500, userBuyer.getId(), userSeller.getId()).insertIntoDatabase();
        assertNull(orderFailed4);

        // Order correct
        OrderDTO orderCorrect = new OrderDTO(product.getId(), userBuyer.getId(), userSeller.getId()).insertIntoDatabase();
        assertNotNull(orderCorrect);
    }

    @Test
    public void testAddingUser()
    {
        // User correct
        UserDTO user_not_duplicated = new UserDTO("user_not_duplicated", "user_not_duplicated", "user_not_duplicated@gmail.com", "user_not_duplicated").insertIntoDatabase();
        assertNotNull(user_not_duplicated);

        // User duplicated
        UserDTO user_duplicated = user_not_duplicated.insertIntoDatabase();
        assertNull(user_duplicated);
    }

    @Test
    public void testAddingProduct()
    {
        // Product correct
        ProductDTO product = new ProductDTO("testing_adding_order", 20.20).insertIntoDatabase();
        assertNotNull(product);
    }
}