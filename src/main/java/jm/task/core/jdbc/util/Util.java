package jm.task.core.jdbc.util;

import jm.task.core.jdbc.model.User;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.service.ServiceRegistry;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;

public class Util {
    // реализуйте настройку соеденения с БД
    private static Connection connection;
    private static SessionFactory sessionFactory;
    private String database = "jdbc:mysql://localhost:3306/testDB";
    private String userName = "root";
    private String password = "5986";
    private Util(String ConnectionType) {
        if (ConnectionType.equals("JDBC")) {
            connection = buildConnection();
        } else if (ConnectionType.equals("Hibernate")) {
            sessionFactory = buildSessionFactory();
        }

    }
    private Connection buildConnection() {
        try {
            connection = DriverManager.getConnection(database, userName, password);
        } catch (SQLException exception) {
            throw new RuntimeException("Проблема с подключением к базе данных JDBC.");
        }
        return connection;
    }
    private SessionFactory buildSessionFactory() {
        try {
            java.util.logging.Logger.getLogger("org.hibernate").setLevel(Level.OFF);
            Configuration configuration = new Configuration();
            // Hibernate settings equivalent to hibernate.cfg.xml's properties
            Properties settings = new Properties();
            settings.put(Environment.DRIVER, "com.mysql.cj.jdbc.Driver");
            settings.put(Environment.URL, database + "?useSSL=false");
            settings.put(Environment.USER, userName);
            settings.put(Environment.PASS, password);
            settings.put(Environment.DIALECT, "org.hibernate.dialect.MySQL5Dialect");

            settings.put(Environment.SHOW_SQL, "true");

            settings.put(Environment.CURRENT_SESSION_CONTEXT_CLASS, "thread");

            configuration.setProperties(settings);

            configuration.addAnnotatedClass(User.class);

            ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                    .applySettings(configuration.getProperties()).build();

            sessionFactory = configuration.buildSessionFactory(serviceRegistry);
        } catch (Exception e) {
            throw new RuntimeException("Проблема с подключением к базе данных Hibernate.");
        }
        return sessionFactory;
    }
    public static Connection getConnection() {
        if (connection == null) {
            new Util("JDBC");
        }
        return connection;
    }
    public static void closeConnection() {
        try {
            connection.close();
        } catch (SQLException exception) {
            throw new RuntimeException("Не существует подключения для закрытия");
        }
    }
    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            new Util("Hibernate");
        }
        return sessionFactory;
    }
    public static void closeSessionFactory() {
        sessionFactory.close();
        sessionFactory = null;
    }
}
