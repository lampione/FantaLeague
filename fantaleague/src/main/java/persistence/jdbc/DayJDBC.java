package persistence.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.tomcat.dbcp.dbcp2.BasicDataSource;

import model.Day;
import model.SerieAMatch;
import persistence.dao.DayDAO;
import utilities.Scripts;
import utilities.Utils;

public class DayJDBC implements DayDAO {

    BasicDataSource dataSource;

    public DayJDBC(BasicDataSource dataSource) {
	this.dataSource = dataSource;
    }

    @Override
    public Day getCurrentDay() throws Exception {

	Connection connection = null;
	PreparedStatement statement = null;
	ResultSet resultSet = null;

	try {

	    connection = dataSource.getConnection();

	    Scripts scripts = (Scripts) Utils.getJsonFile(Scripts.class, Utils.SCRIPT_PATH);

	    statement = connection.prepareStatement(scripts.findCurrentDay);
	    resultSet = statement.executeQuery();

	    Day day = null;

	    if (resultSet.next()) {

		day = new Day();

		day.setId(resultSet.getLong("id"));
		day.setStart(resultSet.getString("initial_"));
		day.setEnd(resultSet.getString("final_"));

	    }

	    return day;

	} finally {
	    resultSet.close();
	    statement.close();
	    connection.close();
	}
    }

    @Override
    public List<SerieAMatch> getSerieAMatches(Long dayId) throws Exception {

	Connection connection = null;
	PreparedStatement statement = null;
	ResultSet resultSet = null;

	try {

	    connection = dataSource.getConnection();

	    Scripts scripts = (Scripts) Utils.getJsonFile(Scripts.class, Utils.SCRIPT_PATH);

	    statement = connection.prepareStatement(scripts.findSerieAMatch);
	    statement.setLong(1, dayId);
	    resultSet = statement.executeQuery();

	    List<SerieAMatch> matches = new ArrayList<>();

	    while (resultSet.next()) {

		SerieAMatch match = new SerieAMatch();

		match.setId(resultSet.getLong("id"));
		match.setDayId(resultSet.getLong("day_id"));
		match.setTeam1(resultSet.getString("team1"));
		match.setTeam2(resultSet.getString("team2"));
		match.setTime(resultSet.getString("time"));
		match.setLocation(resultSet.getString("location"));

		matches.add(match);

	    }

	    return matches;

	} finally {
	    resultSet.close();
	    statement.close();
	    connection.close();
	}

    }


    @Override
	public boolean dayVoteExist(Long dayParam) throws Exception {
		Connection conn = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;

		try {
			conn = dataSource.getConnection();

			Scripts scripts = (Scripts) Utils.getJsonFile(Scripts.class, Utils.SCRIPT_PATH);

			statement = conn.prepareStatement(scripts.dayVoteExist);
			statement.setLong(1, dayParam);

			resultSet = statement.executeQuery();

			if (resultSet.next()) {
				return true;
			}

			return false;
		} catch (SQLException e) {
			throw e;
		} finally {
			resultSet.close();
			statement.close();
			conn.close();
		}
	}

}
