package persistence.jdbc;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import org.apache.tomcat.dbcp.dbcp2.BasicDataSource;

import model.User;
import persistence.dao.UuidDAO;
import utilities.Scripts;
import utilities.Utils;

public class UuidJDBC implements UuidDAO {

    private BasicDataSource dataSource;

    public UuidJDBC(BasicDataSource dataSource) {
	this.dataSource = dataSource;
    }

    @Override
    public User find(String uuid) throws Exception {

	Connection connection = null;
	PreparedStatement statement = null;
	ResultSet resultSet = null;

	try {

	    connection = dataSource.getConnection();

	    Scripts scripts = (Scripts) Utils.getJsonFile(Scripts.class, Utils.SCRIPT_PATH);

	    statement = connection.prepareStatement(scripts.findByUuid);
	    statement.setString(1, uuid);
	    resultSet = statement.executeQuery();

	    User user = null;

	    while (resultSet.next()) {

		if (user == null) {
		    user = new User();
		}

		user.setFirstName(resultSet.getString("first_name"));
		user.setLastName(resultSet.getString("last_name"));
		user.setEmail(resultSet.getString("email"));

		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
		Long time = dateFormat.parse(resultSet.getString("born")).getTime();
		Date born = new Date(time);

		user.setBorn(born);
	    }

	    return user;

	} catch (SQLException e) {
	    throw e;
	} catch (ParseException e) {
	    throw e;
	} finally {
	    resultSet.close();
	    statement.close();
	    connection.close();
	}

    }

    @Override
    public void createUpdate(String uuid, String email) throws Exception {

	Connection conn = null;
	PreparedStatement statement = null;

	try {
	    conn = dataSource.getConnection();

	    Scripts scripts = (Scripts) Utils.getJsonFile(Scripts.class, Utils.SCRIPT_PATH);
	    statement = conn.prepareStatement(scripts.insertUpdateUuid);
	    statement.setString(1, uuid);
	    statement.setString(2, email);
	    statement.setString(3, uuid);
	    statement.executeUpdate();

	} catch (SQLException e) {
	    throw e;
	} finally {
	    statement.close();
	    conn.close();
	}

    }

    @Override
    public void delete(String email) throws Exception {

	Connection conn = null;
	PreparedStatement statement = null;

	try {
	    conn = dataSource.getConnection();

	    Scripts scripts = (Scripts) Utils.getJsonFile(Scripts.class, Utils.SCRIPT_PATH);
	    statement = conn.prepareStatement(scripts.deleteUuid);
	    statement.setString(1, email);
	    statement.executeUpdate();

	} catch (SQLException e) {
	    throw e;
	} finally {
	    statement.close();
	    conn.close();
	}

    }

}
