package ru.pravvich.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.pravvich.model.Item;

import java.sql.*;

/**
 * Author : Pavel Ravvich.
 * Created : 10.08.17.
 * <p>
 * JDBCStorage
 */
@Component
public class DAOJdbc implements DAO {
    /**
     * For authentication database.
     */
    private final PropertiesLoader properties;
    /**
     * Connection to database.
     */
    private Connection connection;

    @Autowired
    public DAOJdbc(final PropertiesLoader properties) {

        this.properties = properties;
        properties.load("db-auth.properties");

        try {

            createConnection();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private void createConnection() throws SQLException {

        connection = DriverManager.getConnection(
                properties.get("url"),
                properties.get("username"),
                properties.get("password")
        );
    }


    @Override
    public void add(final Item item) {

        try (final PreparedStatement statement = connection.prepareStatement(

                "INSERT INTO items (id, description) VALUES (DEFAULT , (?))")
        ) {


            statement.setString(1, item.getDescription());

            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Item getItem(int id) {

        Item result = new Item();

        try (final PreparedStatement statement = connection.prepareStatement(

                "SELECT * FROM items WHERE id = (?)")
        ) {


            statement.setInt(1, id);

            final ResultSet set = statement.executeQuery();

            if (set.next()) {

                final String description = set.getString("description");

                result.setDescription(description);

                result.setId(id);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }
}
