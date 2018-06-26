package persistence.jdbc;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.dbutils.DbUtils;
import org.apache.tomcat.dbcp.dbcp2.BasicDataSource;

import model.League;
import model.User;
import persistence.IdBroker;
import persistence.PersistenceException;
import persistence.dao.LeagueDAO;
import utilities.RegistrationUserInvite;
import utilities.Scripts;
import utilities.TeamScore;
import utilities.Utils;

public class LeagueJDBC implements LeagueDAO {

    BasicDataSource dataSource;

    public LeagueJDBC(BasicDataSource dataSource) {
	this.dataSource = dataSource;
    }

    @Override
    public void create(League league, String adminEmail, String teamName) throws Exception {

	Connection conn = null;
	PreparedStatement statementLeague = null;
	PreparedStatement statementSignedUp = null;
	PreparedStatement statementTeam = null;

	try {
	    conn = dataSource.getConnection();
	    conn.setAutoCommit(false);

	    Scripts scripts = (Scripts) Utils.getJsonFile(Scripts.class, Utils.SCRIPT_PATH);

	    statementLeague = conn.prepareStatement(scripts.insertLeague); // 11 parameters

	    Long id = IdBroker.getId(conn);
	    league.setId(id);

	    statementLeague.setLong(1, id);
	    statementLeague.setString(2, league.getName());
	    statementLeague.setDate(3, league.getFoundation());
	    statementLeague.setInt(4, league.getCredits());

	    statementLeague.setBoolean(5, league.isDuplicatePlayers());
	    statementLeague.setInt(6, league.getGoalkeepers());
	    statementLeague.setInt(7, league.getDefenders());
	    statementLeague.setInt(8, league.getMidfielders());
	    statementLeague.setInt(9, league.getForwards());
	    statementLeague.setInt(10, league.getMaxTimeToLineup());

	    statementLeague.setString(11, league.getModules());

	    statementLeague.executeUpdate();

	    // create team
	    statementTeam = conn.prepareStatement(scripts.createTeam);
	    Long idTeam = createTeam(conn, statementTeam, teamName);

	    // necessary for persistence
	    statementSignedUp = conn.prepareStatement(scripts.insertSignedUp);

	    Date date = new Date(System.currentTimeMillis());
	    insertSignedUp(id, idTeam, adminEmail, "A", date, conn, statementSignedUp);
	    conn.commit();

	} catch (SQLException e) {
	    if (conn != null) {
		try {
		    league.setId(null);
		    conn.rollback();
		    throw e;
		} catch (SQLException e1) {
		    throw e1;
		}
	    } else {
		throw e;
	    }
	} finally {
	    statementLeague.close();
	    statementTeam.close();
	    statementSignedUp.close();
	    conn.setAutoCommit(true);
	    conn.close();
	}

    }

    private void insertSignedUp(Long leagueId, Long teamId, String email, String status, Date date, Connection conn,
	    PreparedStatement statement) throws Exception {

	Long signedId = IdBroker.getId(conn);
	statement.setLong(1, signedId);
	statement.setLong(2, teamId);
	statement.setString(3, email);
	statement.setLong(4, leagueId);
	statement.setDate(5, date);
	statement.setString(6, status);
	statement.executeUpdate();

    }

    private Long createTeam(Connection conn, PreparedStatement statement, String teamName) throws Exception {

	Long id = IdBroker.getId(conn);

	statement.setLong(1, id);
	statement.setString(2, teamName);

	statement.executeUpdate();
	return id;

    }

    @Override
    public void updateSignedUp(Long leagueId, String email, String teamName, String status) throws Exception {

	Connection conn = null;
	PreparedStatement statementSignedUp = null;
	PreparedStatement statementTeam = null;

	try {

	    conn = dataSource.getConnection();
	    conn.setAutoCommit(false);

	    Scripts scripts = (Scripts) Utils.getJsonFile(Scripts.class, Utils.SCRIPT_PATH);

	    statementSignedUp = conn.prepareStatement(scripts.updateSignedUp);
	    statementTeam = conn.prepareStatement(scripts.updateTeamName);

	    // update where league_id=?, user_id=? set status=? and date=?
	    statementSignedUp.setString(1, status);
	    Date date = new Date(System.currentTimeMillis());
	    statementSignedUp.setDate(2, date);
	    statementSignedUp.setLong(3, leagueId);
	    statementSignedUp.setString(4, email);
	    int first = statementSignedUp.executeUpdate();

	    if (first != 0) {

		// update team set name=? from (select id from team as t, user_team_league as
		// utl where utl.team_id = t.id and utl.user_id=? and utl.league_id=?) as sub
		// where sub.id = team.id;
		statementTeam.setString(1, teamName);
		statementTeam.setString(2, email);
		statementTeam.setLong(3, leagueId);
		int second = statementTeam.executeUpdate();

		if (second != 0) {
		    conn.commit();
		} else {
		    conn.rollback();
		    throw new PersistenceException(10000L);
		}

	    } else {
		conn.rollback();
		throw new PersistenceException(10000L);
	    }

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
	    conn.setAutoCommit(true);
	    statementSignedUp.close();
	    statementTeam.close();
	    conn.close();
	}

    }

    @Override
    public List<User> findInLeague(Long leagueId) throws Exception {

	Connection connection = null;
	PreparedStatement statement = null;
	ResultSet resultSet = null;

	try {

	    connection = dataSource.getConnection();

	    Scripts scripts = (Scripts) Utils.getJsonFile(Scripts.class, Utils.SCRIPT_PATH);

	    statement = connection.prepareStatement(scripts.findLeagueUsers);
	    statement.setLong(1, leagueId);
	    resultSet = statement.executeQuery();

	    List<User> users = new ArrayList<>();

	    while (resultSet.next()) {

		User user = new User();
		user.setBorn(resultSet.getDate("born"));
		user.setEmail(resultSet.getString("email"));
		user.setFirstName(resultSet.getString("first_name"));
		user.setLastName(resultSet.getString("last_name"));

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

    // -1 if wrong 1 else
    @Override
    public void invite(Long leagueId, String[] emails, ArrayList<RegistrationUserInvite> emailUserNotExist)
	    throws Exception {

	Connection conn = null;
	PreparedStatement statement = null;
	PreparedStatement createTeam = null;
	PreparedStatement createUserNotExist = null;
	PreparedStatement createRegistrationCheck = null;

	try {

	    conn = dataSource.getConnection();
	    conn.setAutoCommit(false);
	    Scripts scripts = (Scripts) Utils.getJsonFile(Scripts.class, Utils.SCRIPT_PATH);

	    for (String email : emails) {

		int i = Utils.contains(email, emailUserNotExist);
		if (i != -1) {

		    createUserNotExist = conn.prepareStatement(scripts.createUsersNotExist);
		    createUserNotExist.setString(1, email);
		    createUserNotExist.executeUpdate();

		    createRegistrationCheck = conn.prepareStatement(scripts.createRegistrationCheck);
		    String code = emailUserNotExist.get(i).getCode();
		    Date date = new Date(System.currentTimeMillis());

		    createRegistrationCheck.setString(1, code);
		    createRegistrationCheck.setString(2, email);
		    createRegistrationCheck.setDate(3, date);
		    createRegistrationCheck.setString(4, code);
		    createRegistrationCheck.setDate(5, date);

		    createRegistrationCheck.executeUpdate();
		}

		createTeam = conn.prepareStatement(scripts.createTeam);
		Long teamId = createTeam(conn, createTeam, null);

		statement = conn.prepareStatement(scripts.insertSignedUp);
		Date date = new Date(System.currentTimeMillis());
		insertSignedUp(leagueId, teamId, email, "I", date, conn, statement);

	    }

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
	    conn.setAutoCommit(true);
	    createTeam.close();
	    statement.close();
	    conn.close();
	}

    }

    @Override
    public League findById(Long id) throws Exception {

	Connection connection = null;
	PreparedStatement statement = null;
	ResultSet resultSet = null;

	try {

	    connection = dataSource.getConnection();

	    Scripts scripts = (Scripts) Utils.getJsonFile(Scripts.class, Utils.SCRIPT_PATH);

	    statement = connection.prepareStatement(scripts.findLeagueById);
	    statement.setLong(1, id);
	    resultSet = statement.executeQuery();

	    League league = null;

	    if (resultSet.next()) {

		league = new League();

		league.setId(resultSet.getLong("id"));

		league.setName(resultSet.getString("name"));
		league.setFoundation(resultSet.getDate("foundation"));
		league.setCredits(resultSet.getInt("credits"));
		league.setDuplicatePlayers(resultSet.getBoolean("duplicateplayers"));
		league.setGoalkeepers(resultSet.getInt("goalkeepers"));
		league.setDefenders(resultSet.getInt("defenders"));
		league.setMidfielders(resultSet.getInt("midfielders"));
		league.setForwards(resultSet.getInt("forwards"));
		league.setMaxTimeToLineup(resultSet.getInt("maxtimetolineup"));
		league.setModules(resultSet.getString("modules"));
	    }

	    return league;

	} catch (SQLException e) {
	    throw e;
	} finally {
	    DbUtils.closeQuietly(connection, statement, resultSet);
	}
    }

    @Override
    public boolean alreadyUserSignedUp(String email, Long leagueId) throws Exception {

	Connection conn = null;
	PreparedStatement statement = null;
	ResultSet resultSet = null;

	try {
	    conn = dataSource.getConnection();

	    Scripts scripts = (Scripts) Utils.getJsonFile(Scripts.class, Utils.SCRIPT_PATH);

	    statement = conn.prepareStatement(scripts.alreadyUserSignedUp);
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

    @Override
    public void deleteSignedUp(Long teamId) throws Exception {

	Connection conn = null;
	PreparedStatement statement = null;

	try {
	    conn = dataSource.getConnection();

	    Scripts scripts = (Scripts) Utils.getJsonFile(Scripts.class, Utils.SCRIPT_PATH);

	    statement = conn.prepareStatement(scripts.deleteSignedUp);
	    statement.setLong(1, teamId);

	    statement.executeUpdate();

	} catch (SQLException e) {
	    throw e;
	} finally {
	    statement.close();
	    conn.close();

	}

    }

    @Override
    public List<TeamScore> leagueTable(Long leagueId) throws Exception {

	Connection connection = null;
	PreparedStatement statement = null;
	ResultSet resultSet = null;

	try {

	    connection = dataSource.getConnection();

	    Scripts scripts = (Scripts) Utils.getJsonFile(Scripts.class, Utils.SCRIPT_PATH);

	    statement = connection.prepareStatement(scripts.leagueTable);
	    statement.setLong(1, leagueId);
	    resultSet = statement.executeQuery();

	    List<TeamScore> teamScores = new ArrayList<>();

	    while (resultSet.next()) {

		TeamScore teamScore = new TeamScore();
		teamScore.setScore(resultSet.getDouble("sum"));
		teamScore.setTeamId(resultSet.getLong("id"));
		teamScore.setTeamName(resultSet.getString("name"));

		teamScores.add(teamScore);
	    }

	    return teamScores;
	} catch (SQLException e) {
	    throw e;
	} finally {
	    resultSet.close();
	    statement.close();
	    connection.close();
	}
    }

    @Override
    public void updateLName(String lName, Long lId) throws Exception {

	Connection connection = null;
	PreparedStatement statement = null;

	try {

	    connection = dataSource.getConnection();

	    Scripts scripts = (Scripts) Utils.getJsonFile(Scripts.class, Utils.SCRIPT_PATH);

	    statement = connection.prepareStatement(scripts.updateLName);
	    statement.setString(1, lName);
	    statement.setLong(2, lId);
	    statement.executeUpdate();

	} catch (SQLException e) {
	    throw e;
	} finally {
	    statement.close();
	    connection.close();
	}

    }

    @Override
    public void deleteLeague(Long leagueId) throws Exception {
	Connection connection = null;
	PreparedStatement statementFindTeams = null;
	PreparedStatement statementDeleteTeams = null;
	PreparedStatement statementDeleteLeague = null;
	ResultSet resultSetFindTeams = null;

	try {

	    connection = dataSource.getConnection();
	    connection.setAutoCommit(false);

	    Scripts scripts = (Scripts) Utils.getJsonFile(Scripts.class, Utils.SCRIPT_PATH);

	    statementFindTeams = connection.prepareStatement(scripts.findTeams);
	    statementDeleteTeams = connection.prepareStatement(scripts.deleteSignedUp);
	    statementDeleteLeague = connection.prepareStatement(scripts.deleteLeague);

	    statementFindTeams.setLong(1, leagueId);
	    resultSetFindTeams = statementFindTeams.executeQuery();

	    List<Long> teams = new ArrayList<>();

	    while (resultSetFindTeams.next()) {
		teams.add(resultSetFindTeams.getLong("team_id"));
	    }

	    for (Long l : teams) {
		statementDeleteTeams.setLong(1, l);
		statementDeleteTeams.executeUpdate();
	    }

	    statementDeleteLeague.setLong(1, leagueId);
	    statementDeleteLeague.executeUpdate();

	    connection.commit();
	} catch (SQLException e) {
	    connection.rollback();
	    throw e;
	} finally {
	    connection.setAutoCommit(true);
	    DbUtils.closeQuietly(statementFindTeams);
	    DbUtils.closeQuietly(statementDeleteTeams);
	    DbUtils.closeQuietly(statementDeleteLeague);
	    DbUtils.closeQuietly(resultSetFindTeams);
	    DbUtils.closeQuietly(connection);
	}
    }

}
