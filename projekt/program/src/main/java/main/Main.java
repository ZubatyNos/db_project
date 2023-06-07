package main;

import ui.MainMenu;


import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import org.postgresql.ds.PGSimpleDataSource;

/**
 *
 * @author Rastislav Urbanek
 */
public class Main {

    public static void main(String[] args) throws SQLException, IOException {

        PGSimpleDataSource dataSource = new PGSimpleDataSource();

        dataSource.setServerName("localhost");
        dataSource.setPortNumber(5432);
        dataSource.setDatabaseName("postgres");
        dataSource.setUser("postgres");
        dataSource.setPassword("mojeHeslo");

        try (Connection connection = dataSource.getConnection()) {
            DbContext.setConnection(connection);

            MainMenu mainMenu = new MainMenu();
            mainMenu.run();

        } finally {
            DbContext.clear();
        }
    }
}
