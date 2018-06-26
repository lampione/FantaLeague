package persistence.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.tomcat.dbcp.dbcp2.BasicDataSource;

import model.Player;
import model.Team;
import persistence.IdBroker;
import persistence.PersistenceException;
import persistence.dao.TeamDAO;
import utilities.PlayerMatch;
import utilities.PlayerVote;
import utilities.Scripts;
import utilities.TeamScore;
import utilities.Utils;

public class TeamJDBC implements TeamDAO {
	
	private static HashMap<String, Integer> roleMap;
	
	static {
		roleMap = new HashMap<>();
		roleMap.put("g", 0);
		roleMap.put("d", 1);
		roleMap.put("m", 2);
		roleMap.put("f", 3);
	}

	private BasicDataSource dataSource;

	public TeamJDBC(BasicDataSource dataSource) {
		this.dataSource = dataSource;
	}

	@Override
	public void saveTeam(Long teamId, List<Player> players) throws Exception {

		Connection conn = null;
		PreparedStatement statement = null;

		boolean rollback = false;

		try {
			conn = dataSource.getConnection();
			conn.setAutoCommit(false);

			Scripts scripts = (Scripts) Utils.getJsonFile(Scripts.class, Utils.SCRIPT_PATH);

			statement = conn.prepareStatement(scripts.saveTeam);

			for (Player p : players) {

				Long id = IdBroker.getId(conn);
				statement.setLong(1, id);
				statement.setLong(2, teamId);
				statement.setLong(3, p.getId());
				statement.setLong(4, p.getInitQuote());

				int result = statement.executeUpdate();

				if (result == 0) {
					conn.rollback();
					rollback = true;
					throw new PersistenceException(10002L);
				}

			}

			if (!rollback) {
				conn.commit();
			}

		}
		catch (SQLException e) {
			try {
				conn.rollback();
				throw e;
			}
			catch (SQLException e1) {
				throw e1;
			}
			
		}
		finally {
				statement.close();
				conn.setAutoCommit(true);
				conn.close();
		}

	}

	@Override
	public List<Player> findPlayers(Long teamId) throws Exception{

		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;

		try {

			connection = dataSource.getConnection();

			Scripts scripts = (Scripts) Utils.getJsonFile(Scripts.class, Utils.SCRIPT_PATH);

			statement = connection.prepareStatement(scripts.findPlayersInTeam);
			statement.setLong(1, teamId);
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
		}
		catch (SQLException e) {
			throw e;
		}
		finally {
				resultSet.close();
				statement.close();
				connection.close();
		}

	}

	@Override
	public int replacePlayer(Long oldId, Long newId, Long teamId, Long credits) throws Exception {
		
		Connection conn = null;
		PreparedStatement statement = null;

		try {
			conn = dataSource.getConnection();

			Scripts scripts = (Scripts) Utils.getJsonFile(Scripts.class, Utils.SCRIPT_PATH);

			statement = conn.prepareStatement(scripts.replacePlayer);
			
			statement.setLong(1, newId);
			statement.setLong(2, credits);
			statement.setLong(3, teamId);
			statement.setLong(4, oldId);

			return statement.executeUpdate();
			
		} catch (SQLException e) {
			throw e;
		} finally {
				statement.close();
				conn.close();
		}

	}

	@Override
	public Team getTeam(Long leagueId, String email) throws Exception {

		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;

		try {

			connection = dataSource.getConnection();

			Scripts scripts = (Scripts) Utils.getJsonFile(Scripts.class, Utils.SCRIPT_PATH);

			statement = connection.prepareStatement(scripts.getTeam);
			statement.setLong(1, leagueId);
			statement.setString(2, email);			
			resultSet = statement.executeQuery();

			Team team = null;

			if (resultSet.next()) {

				team = new Team();
				team.setId(resultSet.getLong("id"));
				team.setName(resultSet.getString("name"));

			}

			return team;

		} catch (SQLException e) {
			throw e;
		} finally {
				resultSet.close();
				statement.close();
				connection.close();
		}
		
	}

	@Override
	public void saveTeamMatch(Long teamId, Long dayId, List<Player> playerStarters, List<Player> playerBench) throws Exception {

		Connection conn = null;
		PreparedStatement statement = null;
		PreparedStatement deleteTeamMatch = null;
		boolean rollback = false;

		try {
			conn = dataSource.getConnection();
			conn.setAutoCommit(false);

			Scripts scripts = (Scripts) Utils.getJsonFile(Scripts.class, Utils.SCRIPT_PATH);

			statement = conn.prepareStatement(scripts.saveTeamMatch);
			deleteTeamMatch = conn.prepareStatement(scripts.deleteTeamMatch);
			
			deleteTeamMatch.setLong(1, dayId);
			deleteTeamMatch.setLong(2, teamId);
			
			deleteTeamMatch.executeUpdate();

			for(Player p : playerStarters) {

				Long id = IdBroker.getId(conn);
				statement.setLong(1, id);
				statement.setLong(2, dayId);
				statement.setLong(3, teamId);
				statement.setLong(4, p.getId());
				statement.setString(5, "S");
				int result = statement.executeUpdate();
				
				if( result != 1 ) {
					conn.rollback();
					rollback = true;
					throw new PersistenceException(10003L);
				}
				
			}

			if( !rollback ) {

				for(Player p : playerBench) {

					Long id = IdBroker.getId(conn);
					statement.setLong(1, id);
					statement.setLong(2, dayId);
					statement.setLong(3, teamId);
					statement.setLong(4, p.getId());
					statement.setString(5, "B");
					int result = statement.executeUpdate();
					
					if( result != 1 ) {
						conn.rollback();
						rollback = true;
						throw new PersistenceException(10003L);
					}
					
				}
				
			}
			else {
				throw new PersistenceException(10003L);
			}

			if( !rollback ) {
				conn.commit();
			}
			else {
				 throw new PersistenceException(10003L);
			}

		}
		catch (SQLException e) {
			try {
				conn.rollback();
				throw e;
			} catch (SQLException e1) {
				throw e;
			}
		}
		finally {
				statement.close();
				conn.setAutoCommit(true);
				conn.close();
		}
	}

	@Override
	public List<PlayerMatch> findPlayersMatch(Long teamId, Long dayId) throws Exception {
	
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;

		try {

			connection = dataSource.getConnection();

			Scripts scripts = (Scripts) Utils.getJsonFile(Scripts.class, Utils.SCRIPT_PATH);

			statement = connection.prepareStatement(scripts.findPlayersInMatch);
			statement.setLong(1, teamId);
			statement.setLong(2, dayId);
			resultSet = statement.executeQuery();

			List<PlayerMatch> players = new ArrayList<>();

			while (resultSet.next()) {

				PlayerMatch playerMatch = new PlayerMatch();
				Player player = new Player();

				player.setId(resultSet.getLong("id"));
				player.setRole(resultSet.getString("role"));
				player.setName(resultSet.getString("name"));
				player.setEquipe(resultSet.getString("equipe"));
				player.setInitQuote(resultSet.getLong("initial_quote"));

				String status = resultSet.getString("status");

				playerMatch.setPlayer(player);
				playerMatch.setStatus(status);
				players.add(playerMatch);
			}

			return players;
		}
		catch (SQLException e) {
			throw e;
		}
		finally {
				resultSet.close();
				statement.close();
				connection.close();
		}
		
	}

	@Override
	public Map<Long, List<PlayerVote>> findTeamPlayerVote(Long dayId, List<TeamScore> teamScores) throws Exception{
		
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;

		try {
			connection = dataSource.getConnection();

			Scripts scripts = (Scripts) Utils.getJsonFile(Scripts.class, Utils.SCRIPT_PATH);

			statement = connection.prepareStatement(scripts.findTeamPlayerVote);
			
			HashMap<Long, List<PlayerVote>> teamPlayerVote = new HashMap<>();
			
			for (TeamScore ts : teamScores) {
				statement.setLong(1, dayId);
				statement.setLong(2, dayId);
				statement.setLong(3, ts.getTeamId());

				resultSet = statement.executeQuery();

				List<PlayerVote> votes = new ArrayList<>();
				
				while (resultSet.next()) {
					PlayerVote vote = new PlayerVote();
					vote.setPlayerId(resultSet.getLong("id"));
					vote.setRole(resultSet.getString("role"));
					vote.setName(resultSet.getString("name"));
					vote.setEquipe(resultSet.getString("equipe"));
					vote.setDayId(dayId);
					vote.setVote(resultSet.getDouble("vote"));
					vote.setRedCard(resultSet.getInt("red_card"));
					vote.setYellowCard(resultSet.getInt("yellow_card"));
					vote.setTaken(resultSet.getInt("taken"));
					vote.setDone(resultSet.getInt("done"));
					vote.setAssist(resultSet.getInt("assist"));
					vote.setFinalVote(resultSet.getDouble("final_vote"));

					votes.add(vote);
				}

				Collections.sort(votes, (v1, v2) -> {
					return Integer.compare(roleMap.get(v1.getRole()), roleMap.get(v2.getRole()));
				});
				
				teamPlayerVote.put(ts.getTeamId(), votes);
				
			}

			return teamPlayerVote;
		}
		catch (SQLException e) {
			throw e;
		}
		finally {
				if( resultSet != null ) {
					resultSet.close();
				}
				if( statement != null ){					
					statement.close();
				}
				if( connection != null ) {
					connection.close();
				}
		}
		
	}

	@Override
	public void updateTName(Long tId, String tName) throws Exception {

		Connection connection = null;
		PreparedStatement statement = null;

		try {

			connection = dataSource.getConnection();

			Scripts scripts = (Scripts) Utils.getJsonFile(Scripts.class, Utils.SCRIPT_PATH);

			statement = connection.prepareStatement(scripts.updateTName);
			statement.setString(1, tName);
			statement.setLong(2, tId);
			statement.executeUpdate();

		} catch (SQLException e) {
			throw e;
		} finally {
				statement.close();
				connection.close();
		}
	}

}
