package persistence.jdbc;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.tomcat.dbcp.dbcp2.BasicDataSource;

import model.User;
import persistence.PersistenceException;
import persistence.dao.UserDAO;
import utilities.Invite;
import utilities.Pair;
import utilities.Scripts;
import utilities.UserTeamName;
import utilities.Utils;

public class UserJDBC implements UserDAO {

	private BasicDataSource dataSource;

	public UserJDBC(BasicDataSource dataSource) {
		this.dataSource = dataSource;
	}

	@Override
	public void create(User user, String password) throws Exception {

		Connection conn = null;
		PreparedStatement statement = null;

		try {
			conn = dataSource.getConnection();

			Scripts scripts = (Scripts) Utils.getJsonFile(Scripts.class, Utils.SCRIPT_PATH);

			statement = conn.prepareStatement(scripts.insertUser);
			statement.setString(1, user.getEmail());
			statement.setString(2, user.getFirstName());
			statement.setString(3, user.getLastName());
			statement.setString(4, password);
			statement.setDate(5, user.getBorn());

			statement.executeUpdate();

		} catch (SQLException e) {

			if (e.getSQLState().equals("23505")) {
				throw new PersistenceException(10006L);
			} else {
				throw e;
			}

		} finally {
			try {
				statement.close();
				conn.close();
			} catch (SQLException e) {
				throw e;
			}
		}

	}

	@Override
	public User findByEmail(String email) throws Exception {

		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;

		try {

			connection = dataSource.getConnection();

			Scripts scripts = (Scripts) Utils.getJsonFile(Scripts.class, Utils.SCRIPT_PATH);

			statement = connection.prepareStatement(scripts.findByEmail);
			statement.setString(1, email);
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
	public void setPassword(String email, String password) throws Exception {

		Connection connection = null;
		PreparedStatement statement = null;

		try {

			connection = dataSource.getConnection();

			Scripts scripts = (Scripts) Utils.getJsonFile(Scripts.class, Utils.SCRIPT_PATH);

			statement = connection.prepareStatement(scripts.updateUserPass);
			statement.setString(1, password);
			statement.setString(2, email);
			statement.executeUpdate();

		} catch (SQLException e) {
			throw e;
		} finally {
			statement.close();
			connection.close();
		}
	}

	@Override
	public User validate(String email, String password) throws Exception {

		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;

		try {

			connection = dataSource.getConnection();

			Scripts scripts = (Scripts) Utils.getJsonFile(Scripts.class, Utils.SCRIPT_PATH);
			statement = connection.prepareStatement(scripts.validateUser);
			statement.setString(1, password);
			statement.setString(2, email);
			resultSet = statement.executeQuery();

			User user = null;

			if (resultSet.next()) {

				if (resultSet.getString("pswmatch").equals("t")) {

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

				else {
					throw new PersistenceException(10004L);
				}

			} else {
				throw new PersistenceException(10005L);
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
	public List<Pair> findLeagues(String email, String status, Long dayId) throws Exception {

		Connection connection = null;
		PreparedStatement statement = null;
		PreparedStatement statementTotal = null;
		PreparedStatement statementLast = null;
		ResultSet resultSet = null;
		ResultSet resultSetTotal = null;
		ResultSet resultSetLast = null;

		try {

			connection = dataSource.getConnection();

			Scripts scripts = (Scripts) Utils.getJsonFile(Scripts.class, Utils.SCRIPT_PATH);

			statement = connection.prepareStatement(scripts.findLeagues);
			statementTotal = connection.prepareStatement(scripts.totalScore);
			statementLast = connection.prepareStatement(scripts.lastScore);

			statement.setString(1, email);
			statement.setString(2, status);
			resultSet = statement.executeQuery();

			List<Pair> pairs = new ArrayList<>();

			while (resultSet.next()) {

				Pair p = new Pair();

				p.setLeagueId(resultSet.getLong("id"));
				p.setLeagueName(resultSet.getString("name"));
				p.setTeamName(resultSet.getString("team_name"));
				p.setTeamId(resultSet.getLong("team_id"));

				Long currentDay = dayId - 1;
				statementLast.setLong(1, p.getLeagueId());
				statementLast.setLong(2, p.getTeamId());
				statementLast.setLong(3, currentDay);

				resultSetLast = statementLast.executeQuery();

				if (resultSetLast.next()) {
					p.setLastScore(resultSetLast.getDouble("last_score"));

				}

				statementTotal.setLong(1, p.getLeagueId());
				statementTotal.setLong(2, p.getTeamId());

				resultSetTotal = statementTotal.executeQuery();
				if (resultSetTotal.next()) {
					p.setTotalScore(resultSetTotal.getDouble("total_score"));
				}

				pairs.add(p);
			}

			return pairs;

		} catch (SQLException e) {
			throw e;
		} finally {
			if (resultSetTotal != null) {
				resultSetTotal.close();
			}
			if (resultSetLast != null) {
				resultSetLast.close();
			}
			if (resultSet != null) {
				resultSet.close();
			}
			if (statement != null) {
				statement.close();
			}
			if (statementLast != null) {
				statementLast.close();
			}
			if (statementTotal != null) {
				statementTotal.close();
			}
			if (connection != null) {
				connection.close();
			}
		}

	}

	@Override
	public List<Invite> findInvites(String email) throws Exception {

		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;

		try {

			connection = dataSource.getConnection();

			Scripts scripts = (Scripts) Utils.getJsonFile(Scripts.class, Utils.SCRIPT_PATH);

			statement = connection.prepareStatement(scripts.findUserInvites);
			statement.setString(1, email);
			resultSet = statement.executeQuery();

			List<Invite> invites = new ArrayList<>();

			while (resultSet.next()) {
				Invite invite = new Invite();
				invite.setId(resultSet.getString("id"));
				invite.setName(resultSet.getString("name"));
				invite.setTeamId(resultSet.getLong("team_id"));
				invites.add(invite);
			}

			return invites;

		} catch (SQLException e) {
			throw e;
		} finally {
			connection.close();
			statement.close();
			resultSet.close();
		}

	}

	@Override
	public void createRecoveryPass(String id, String user_email) throws Exception {
		Connection conn = null;
		PreparedStatement statement = null;

		try {
			conn = dataSource.getConnection();

			Scripts scripts = (Scripts) Utils.getJsonFile(Scripts.class, Utils.SCRIPT_PATH);

			statement = conn.prepareStatement(scripts.insertRecoveryPass);
			statement.setString(1, id);
			statement.setString(2, user_email);
			Date date = new Date(System.currentTimeMillis());
			statement.setDate(3, date);
			statement.setString(4, id);
			statement.setDate(5, date);

			statement.executeUpdate();

		} catch (SQLException e) {
			throw e;
		} finally {
			statement.close();
			conn.close();
		}

	}

	@Override
	public boolean existIdRecover(String id) throws Exception {

		Connection conn = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;

		try {
			conn = dataSource.getConnection();

			Scripts scripts = (Scripts) Utils.getJsonFile(Scripts.class, Utils.SCRIPT_PATH);

			statement = conn.prepareStatement(scripts.existRecoveryPass);
			statement.setString(1, id);

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

	@Override
	public String findUserEmailByRequestId(String id) throws Exception {

		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;

		try {

			connection = dataSource.getConnection();

			Scripts scripts = (Scripts) Utils.getJsonFile(Scripts.class, Utils.SCRIPT_PATH);

			statement = connection.prepareStatement(scripts.findUserEmailByRequestId);
			statement.setString(1, id);
			resultSet = statement.executeQuery();

			String email = " ";

			if (resultSet.next()) {
				email = resultSet.getString("email");
			}

			return email;

		} catch (SQLException e) {
			throw e;
		} finally {
			connection.close();
			statement.close();
			resultSet.close();
		}
	}

	@Override
	public void deleteFromRequest(String id) throws Exception {

		Connection conn = null;
		PreparedStatement statement = null;

		try {
			conn = dataSource.getConnection();

			Scripts scripts = (Scripts) Utils.getJsonFile(Scripts.class, Utils.SCRIPT_PATH);

			statement = conn.prepareStatement(scripts.deleteFromRequest);
			statement.setString(1, id);

			statement.executeUpdate();
		} catch (SQLException e) {
			throw e;
		} finally {
			statement.close();
			conn.close();
		}

	}

	@Override
	public List<UserTeamName> findUserTeamName(Long leagueId) throws Exception {

		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;

		try {

			connection = dataSource.getConnection();

			Scripts scripts = (Scripts) Utils.getJsonFile(Scripts.class, Utils.SCRIPT_PATH);

			statement = connection.prepareStatement(scripts.findUserTeamName);
			statement.setLong(1, leagueId);
			resultSet = statement.executeQuery();

			List<UserTeamName> users = new ArrayList<>();

			while (resultSet.next()) {

				UserTeamName user = new UserTeamName();
				user.setUserEmail(resultSet.getString("user_id"));
				user.setTeamId(resultSet.getLong("id"));
				user.setTeamName(resultSet.getString("name"));

				users.add(user);

			}

			return users;

		} catch (SQLException e) {
			throw e;
		} finally {
			resultSet.close();
			statement.close();
			connection.close();
		}

	}

	@Override
	public boolean userNotExist(String email) throws Exception {

		Connection conn = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;

		try {
			conn = dataSource.getConnection();

			Scripts scripts = (Scripts) Utils.getJsonFile(Scripts.class, Utils.SCRIPT_PATH);

			statement = conn.prepareStatement(scripts.userNotExist);
			statement.setString(1, email);

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

	@Override
	public String rCodeMatch(String code) throws Exception {

		Connection conn = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;

		try {
			conn = dataSource.getConnection();

			Scripts scripts = (Scripts) Utils.getJsonFile(Scripts.class, Utils.SCRIPT_PATH);

			statement = conn.prepareStatement(scripts.rCodeMatch);
			statement.setString(1, code);

			resultSet = statement.executeQuery();

			if (resultSet.next()) {
				return resultSet.getString("user_email");
			}

			return "null";

		} catch (SQLException e) {
			throw e;
		} finally {
			resultSet.close();
			statement.close();
			conn.close();
		}
	}

	@Override
	public void updateUserInvite(String email, String firstName, String lastName, Date bornField, String password)
			throws Exception {

		Connection conn = null;
		PreparedStatement statementUpdate = null;
		PreparedStatement statementDelete = null;

		try {
			conn = dataSource.getConnection();
			conn.setAutoCommit(false);

			Scripts scripts = (Scripts) Utils.getJsonFile(Scripts.class, Utils.SCRIPT_PATH);

			statementUpdate = conn.prepareStatement(scripts.updateUserInvite);
			statementUpdate.setString(1, firstName);
			statementUpdate.setString(2, lastName);
			statementUpdate.setString(3, password);
			statementUpdate.setDate(4, bornField);
			statementUpdate.setString(5, email);

			statementUpdate.executeUpdate();

			statementDelete = conn.prepareStatement(scripts.deleteRegistrationCode);
			statementDelete.setString(1, email);

			statementDelete.executeUpdate();

			conn.commit();

		} catch (SQLException e) {
			if (conn != null) {
				try {
					conn.rollback();
					throw e;
				} catch (SQLException e1) {
					throw e1;
				}
			} else {
				throw e;
			}
		} finally {
			statementUpdate.close();
			statementDelete.close();
			conn.close();
		}
	}

	@Override
	public boolean passCodeMatch(String id) throws Exception {

		Connection conn = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;

		try {
			conn = dataSource.getConnection();

			Scripts scripts = (Scripts) Utils.getJsonFile(Scripts.class, Utils.SCRIPT_PATH);

			statement = conn.prepareStatement(scripts.passCodeMatch);
			statement.setString(1, id);

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

	@Override
	public boolean isStatusUserSession(String status, String email, Long leagueId) throws Exception {

		Connection conn = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;

		try {
			conn = dataSource.getConnection();

			Scripts scripts = (Scripts) Utils.getJsonFile(Scripts.class, Utils.SCRIPT_PATH);

			statement = conn.prepareStatement(scripts.isStatusUserSession);
			statement.setString(1, email);
			statement.setString(2, status);
			statement.setLong(3, leagueId);

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

	@Override
	public void updateFirstName(String email, String firstName) throws Exception {

		Connection connection = null;
		PreparedStatement statement = null;

		try {

			connection = dataSource.getConnection();

			Scripts scripts = (Scripts) Utils.getJsonFile(Scripts.class, Utils.SCRIPT_PATH);

			statement = connection.prepareStatement(scripts.updateUserFirstName);
			statement.setString(1, firstName);
			statement.setString(2, email);
			statement.executeUpdate();

		} catch (SQLException e) {
			throw e;
		} finally {
			statement.close();
			connection.close();
		}
	}

	@Override
	public void updateLastName(String email, String lastName) throws Exception {

		Connection connection = null;
		PreparedStatement statement = null;

		try {

			connection = dataSource.getConnection();

			Scripts scripts = (Scripts) Utils.getJsonFile(Scripts.class, Utils.SCRIPT_PATH);

			statement = connection.prepareStatement(scripts.updateUserLastName);
			statement.setString(1, lastName);
			statement.setString(2, email);
			statement.executeUpdate();

		} catch (SQLException e) {
			throw e;
		} finally {
			statement.close();
			connection.close();
		}
	}

	@Override
	public void updateBorn(String email, Date born) throws Exception {

		Connection connection = null;
		PreparedStatement statement = null;

		try {

			connection = dataSource.getConnection();

			Scripts scripts = (Scripts) Utils.getJsonFile(Scripts.class, Utils.SCRIPT_PATH);

			statement = connection.prepareStatement(scripts.updateUserBorn);
			statement.setDate(1, born);
			statement.setString(2, email);
			statement.executeUpdate();

		} catch (SQLException e) {
			throw e;
		} finally {
			statement.close();
			connection.close();
		}
	}


	@Override
	public boolean isUserInLeague(String email, Long leagueId) throws Exception {
		Connection conn = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;

		try {
			conn = dataSource.getConnection();

			Scripts scripts = (Scripts) Utils.getJsonFile(Scripts.class, Utils.SCRIPT_PATH);

			statement = conn.prepareStatement(scripts.isUserInLeague);
			statement.setString(1, email);
			statement.setLong(2, leagueId);

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
