package com.vera.init;

import com.vera.dto.OrderDTO;
import com.vera.dto.ProductDTO;
import com.vera.dto.UserDTO;
import org.apache.ibatis.jdbc.RuntimeSqlException;
import org.apache.ibatis.jdbc.ScriptRunner;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.sql.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class DatabaseManager {
    protected static final String database_name = "virtual_store_acda";
    protected static Connection sqlConnection;
    protected static String static_username;
    protected static String static_password;

    /**
     * Creates a connection to the database
     *
     * @param username Username of the user
     * @param password Password of the user
     */
    public DatabaseManager(String username, String password) {
        if (init(database_name, username, password)) {
            static_username = username;
            static_password = password;
        }
    }

    /**
     * Creates tables and inserts default users
     */
    private static boolean execute_creation() {
        if (init(database_name)) {
            try {
                ScriptRunner sr = new ScriptRunner(sqlConnection);
                sr.setStopOnError(true);

                Reader reader = new BufferedReader(new FileReader("src/com/vera/init/src/Init_SQL.sql"));
                Reader reader2 = new BufferedReader(new FileReader("src/com/vera/init/src/Inserts_SQL.sql"));

                sr.runScript(reader);
                sr.runScript(reader2);

                return true;
            } catch (FileNotFoundException e) {
                System.out.println("No se encontró el archivo SQL: " + e.getMessage());
            } catch (RuntimeSqlException error) {
                System.out.println("No se pudo cargar el archivo SQL: " + error.getMessage());
            } finally {
                close();
            }
        }

        return false;
    }

    /**
     * Closes the database
     */
    public static boolean close() {
        if (sqlConnection != null) {
            try {
                sqlConnection.close();
                sqlConnection = null;
            } catch (SQLException e) {
                System.out.println("Error cerrando la base de datos: " + e);
                return false;
            }

            return true;
        }

        return false;
    }

    /**
     * Overcharged function: Initiates database with already stored credentials
     *
     * @param database Database used
     */
    private static boolean init(String database) {
        return init(database, static_username, static_password);
    }

    /**
     * Initiates database. If database is not created, creates it
     *
     * @param database Database where we want the connection
     * @param username Username used in the connection
     * @param password Password used in the connection
     */
    private static boolean init(String database, String username, String password) {
        try {
            DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver());
            sqlConnection = DriverManager.getConnection("jdbc:mysql://localhost/" + database, username, password);

            return true;
        } catch (NullPointerException e) {
            System.out.println("Error cargando el driver: " + e);
            sqlConnection = null;
        } catch (SQLException error) {
            if (error.getClass().getSimpleName().equals("SQLSyntaxErrorException")) {
                try {
                    sqlConnection = DriverManager.getConnection("jdbc:mysql://localhost/", username, password);

                    PreparedStatement st = sqlConnection.prepareStatement("CREATE DATABASE " + database);
                    st.executeUpdate();

                    static_username = username;
                    static_password = password;

                    init(database, username, password);
                    execute_creation();

                    return true;
                } catch (SQLException err) {
                    System.out.println("Error al conectar a la base de datos: " + err);
                    sqlConnection = null;
                } finally {
                    close();
                }
            } else {
                System.out.println("Error al conectar a la base de datos: " + error);
                sqlConnection = null;
            }
        }

        return false;
    }

    public static Object insertValues(String type, HashMap<String, HashMap<String, String>> mapArgs) {
        if (init(database_name)) {
            try {
                String query = switch (type)
                {
                    case "Product" -> prepareQuery(mapArgs, "INSERT INTO Products(", "", "INSERT");
                    case "Order" -> prepareQuery(mapArgs, "INSERT INTO Orders(", "", "INSERT");
                    case "User" -> prepareQuery(mapArgs, "INSERT INTO Users_(", "", "INSERT");
                    default -> "";
                };

                PreparedStatement st = addAtributesStatement(sqlConnection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS), mapArgs);

                st.executeUpdate();
                ResultSet data = st.getGeneratedKeys();
                data.next();

                /* Retrieve SELECT to return object DTO*/
                HashMap<String, HashMap<String, String>> columnNames = new HashMap<>();
                HashMap<String, String> dataID = new HashMap<>();
                dataID.put("Integer", data.getInt(1) + "");
                columnNames.put("id", dataID);

                return switch (type)
                {
                    case "Product" -> selectValues("Product", columnNames);
                    case "Order" -> selectValues("Order", columnNames);
                    case "User" -> selectValues("User", columnNames);
                    default -> null;
                };

            }
            catch (SQLException err)
            {
                System.out.println("Error al conectar a la base de datos: " + err);
                sqlConnection = null;
            }
        }

        return null;
    }

    /* Prepare query depending arguments (INSERT AND SELECT)*/
    private static String prepareQuery(HashMap<String, HashMap<String, String>> mapArgs, String starting, String final_query, String typeQuery) {
        Iterator<Map.Entry<String, HashMap<String, String>>> iterator = mapArgs.entrySet().iterator();
        StringBuilder query = new StringBuilder();
        int counter = 0;
        int i = 0;

        query.append(starting);

        if (typeQuery.equals("INSERT")) {
            while (iterator.hasNext()) {
                Map.Entry<String, HashMap<String, String>> columns = iterator.next();
                query.append(columns.getKey());

                if (iterator.hasNext()) query.append(",");
                else query.append(")");

                counter++;
            }

            query.append(" VALUES(?");

            while (i < counter - 1) {
                query.append(", ?");
                i++;
            }

            query.append(");");

        }
        else if(typeQuery.equals("SELECT"))
        {
            while (iterator.hasNext())
            {
                Map.Entry<String, HashMap<String, String>> columns = iterator.next();
                query.append(columns.getKey()).append(" = ?");

                if (iterator.hasNext()) query.append(" AND ");
            }
        }

        query.append(final_query);

        System.out.println(query.toString());
        return query.toString();
    }

    /* Add attributes to Statement */
    private static PreparedStatement addAtributesStatement(PreparedStatement st, HashMap<String, HashMap<String, String>> mapArgs) throws SQLException {
        Iterator<Map.Entry<String, HashMap<String, String>>> iterator = mapArgs.entrySet().iterator();
        int counter = 1;

        while (iterator.hasNext()) {
            // Column name
            Map.Entry<String, HashMap<String, String>> columns = iterator.next();
            // Data (type and value) of the column
            Iterator<Map.Entry<String, String>> data = columns.getValue().entrySet().iterator();

            while (data.hasNext())
            {
                Map.Entry<String, String> value = data.next();

                if (value.getKey().equals("Integer")) st.setInt(counter, Integer.parseInt(value.getValue()));
                else if (value.getKey().equals("Double"))
                    st.setDouble(counter, Double.parseDouble(value.getValue()));
                else st.setString(counter, value.getValue());

                counter++;
            }
        }

        return st;
    }

    public static Object selectValues(String type, HashMap<String, HashMap<String, String>> mapArgs) {
        if (init(database_name))
        {
            try
            {
                String query = switch (type)
                {
                    case "Product" -> prepareQuery(mapArgs, "SELECT * FROM Products WHERE ", "", "SELECT");
                    case "Order" -> prepareQuery(mapArgs, "SELECT * FROM Orders WHERE ", "", "SELECT");
                    case "User" -> prepareQuery(mapArgs, "SELECT * FROM Users_ WHERE ", "", "SELECT");
                    default -> "";
                };

                PreparedStatement st = addAtributesStatement(sqlConnection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS), mapArgs);

                ResultSet data = st.executeQuery();
                data.next();

                if (type.equals("Product")) return new ProductDTO(data.getInt("id"), data.getString("name_"), data.getDouble("value_"), data.getInt("userID"));
                else if (type.equals("Order")) return new OrderDTO(data.getInt("id"), data.getInt("objectID"), data.getInt("userC"), data.getInt("userV"), data.getString("date_order"));
                else if (type.equals("User")) return new UserDTO(data.getInt("id"), data.getString("full_name"), data.getString("user_"), data.getString("email"), data.getString("password_"), data.getString("creation_date"), data.getString("modification_date"));

                return null;
            } catch (SQLException err) {
                System.out.println("Error al conectar a la base de datos: " + err);
                sqlConnection = null;
            }
        }

        return null;
    }

    public static void removeDatabase()
    {
        try
        {
            PreparedStatement st = sqlConnection.prepareStatement("DROP DATABASE IF EXISTS " + database_name);
            st.executeUpdate();
        }
        catch (SQLException e)
        {
            System.out.println("Error al conectar a la base de datos: " + e);
        }
        finally
        {
            close();
        }
    }

    public static Connection getSqlConnection()
    {
        return sqlConnection;
    }
}
