package persistence.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.tomcat.dbcp.dbcp2.BasicDataSource;

import model.Player;
import persistence.dao.PlayerDAO;
import utilities.PlayerVote;
import utilities.Scripts;
import utilities.Utils;

public class PlayerJDBC implements PlayerDAO {

    BasicDataSource dataSource;

    public PlayerJDBC(BasicDataSource dataSource) {
	this.dataSource = dataSource;
    }

    @Override
    public List<Player> findPlayersByRole(String role) throws Exception {

	Connection connection = null;
	PreparedStatement statement = null;
	ResultSet resultSet = null;

	try {

	    connection = dataSource.getConnection();

	    Scripts scripts = (Scripts) Utils.getJsonFile(Scripts.class, Utils.SCRIPT_PATH);

	    statement = connection.prepareStatement(scripts.findPlayersByRole);
	    statement.setString(1, role);
	    resultSet = statement.executeQuery();

	    List<Player> players = new ArrayList<>();

	    while (resultSet.next()) {

		Player player = new Player();

		player.setId(resultSet.getLong("id"));
		player.setRole(resultSet.getString("role"));
		player.setName(resultSet.getString("name"));
		player.setEquipe(resultSet.getString("equipe"));
		player.setInitQuote(resultSet.getLong("initial_quote"));

		players.add(player);

	    }

	    return players;

	} catch (SQLException e) {
	    throw e;
	} finally {
	    resultSet.close();
	    statement.close();
	    connection.close();
	}

    }

    @Override
    public List<Player> findPlayersByRoleNoDuplicate(String role, Long leagueId, Long teamId) throws Exception {

	Connection connection = null;
	PreparedStatement statement = null;
	ResultSet resultSet = null;

	try {

	    connection = dataSource.getConnection();

	    Scripts scripts = (Scripts) Utils.getJsonFile(Scripts.class, Utils.SCRIPT_PATH);

	    statement = connection.prepareStatement(scripts.findPlayersByRoleNoDuplicate);
	    statement.setString(1, role);
	    statement.setLong(2, teamId);
	    statement.setLong(3, leagueId);
	    resultSet = statement.executeQuery();

	    List<Player> players = new ArrayList<>();

	    while (resultSet.next()) {

		Player player = new Player();

		player.setId(resultSet.getLong("id"));
		player.setRole(resultSet.getString("role"));
		player.setName(resultSet.getString("name"));
		player.setEquipe(resultSet.getString("equipe"));
		player.setInitQuote(resultSet.getLong("initial_quote"));

		players.add(player);

	    }

	    return players;

	} catch (SQLException e) {
	    throw e;
	} finally {
	    resultSet.close();
	    statement.close();
	    connection.close();
	}
    }

    @Override
    public List<PlayerVote> findPlayerById(Long playerId) throws Exception {

	Connection connection = null;
	PreparedStatement statement = null;
	ResultSet resultSet = null;

	try {

	    connection = dataSource.getConnection();

	    Scripts scripts = (Scripts) Utils.getJsonFile(Scripts.class, Utils.SCRIPT_PATH);

	    statement = connection.prepareStatement(scripts.findPlayerById);
	    statement.setLong(1, playerId);
	    resultSet = statement.executeQuery();

	    List<PlayerVote> votes = new ArrayList<>();

	    while (resultSet.next()) {
		PlayerVote vote = new PlayerVote();
		vote.setPlayerId(resultSet.getLong("id"));
		vote.setDayId(resultSet.getLong("day_id"));
		vote.setRole(resultSet.getString("role"));
		vote.setName(resultSet.getString("name"));
		vote.setEquipe(resultSet.getString("equipe"));
		vote.setVote(resultSet.getDouble("vote"));
		vote.setRedCard(resultSet.getInt("red_card"));
		vote.setYellowCard(resultSet.getInt("yellow_card"));
		vote.setTaken(resultSet.getInt("taken"));
		vote.setDone(resultSet.getInt("done"));
		vote.setAssist(resultSet.getInt("assist"));
		vote.setFinalVote(resultSet.getDouble("final_vote"));

		votes.add(vote);
	    }

	    return votes;

	} catch (SQLException e) {
	    throw e;
	} finally {
	    resultSet.close();
	    statement.close();
	    connection.close();
	}
    }

    @Override
    public List<Player> findPlayerByQuery(String query) throws Exception {

	Connection connection = null;
	PreparedStatement statement = null;
	ResultSet resultSet = null;

	try {

	    connection = dataSource.getConnection();

	    Scripts scripts = (Scripts) Utils.getJsonFile(Scripts.class, Utils.SCRIPT_PATH);

	    statement = connection.prepareStatement(scripts.findPlayerByQuery);
	    statement.setString(1, query);
	    resultSet = statement.executeQuery();

	    List<Player> players = new ArrayList<>();

	    while (resultSet.next()) {
		Player player = new Player();
		player.setId(resultSet.getLong("id"));
		player.setRole(resultSet.getString("role"));
		player.setName(resultSet.getString("name"));
		player.setEquipe(resultSet.getString("equipe"));
		player.setInitQuote(resultSet.getLong("initial_quote"));
		players.add(player);
	    }

	    return players;

	} catch (SQLException e) {
	    e.printStackTrace();
	    throw e;
	} finally {

	    resultSet.close();
	    statement.close();
	    connection.close();

	}

    }

    @Override
    public boolean playerExsist(Long pId) throws Exception {

	Connection conn = null;
	PreparedStatement statement = null;
	ResultSet resultSet = null;

	try {
	    conn = dataSource.getConnection();

	    Scripts scripts = (Scripts) Utils.getJsonFile(Scripts.class, Utils.SCRIPT_PATH);

	    statement = conn.prepareStatement(scripts.playerExsist);
	    statement.setLong(1, pId);

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
