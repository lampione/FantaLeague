package persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import utilities.Scripts;
import utilities.Utils;

public class IdBroker {

	public static Long getId(Connection connection) throws Exception {
		
		Long id = null;
		PreparedStatement statement = null;
		ResultSet result = null;
		
		try {

			Scripts scripts = (Scripts) Utils.getJsonFile(Scripts.class, Utils.SCRIPT_PATH);
			statement = connection.prepareStatement(scripts.nextId);
			result = statement.executeQuery();
			result.next();
			id = result.getLong("id");

			return id;
		} catch (SQLException e) {
			throw e;
		} finally {
				statement.close();
				result.close();
		}
	}

}
