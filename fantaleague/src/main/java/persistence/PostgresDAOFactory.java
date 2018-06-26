package persistence;

import org.apache.tomcat.dbcp.dbcp2.BasicDataSource;

import persistence.dao.DayDAO;
import persistence.dao.LeagueDAO;
import persistence.dao.PlayerDAO;
import persistence.dao.TeamDAO;
import persistence.dao.UserDAO;
import persistence.dao.UuidDAO;
import persistence.jdbc.DayJDBC;
import persistence.jdbc.LeagueJDBC;
import persistence.jdbc.PlayerJDBC;
import persistence.jdbc.TeamJDBC;
import persistence.jdbc.UserJDBC;
import persistence.jdbc.UuidJDBC;
import utilities.Configuration;
import utilities.Utils;

public class PostgresDAOFactory extends DAOFactory {
	
	private static BasicDataSource dataSource;

	static {

			Configuration config = (Configuration) Utils.getJsonFile(Configuration.class, Utils.CONFIG_PATH);

			String url = String.format("jdbc:%s://%s:%s/%s", config.jdbc, config.host, config.port, config.database);

			String username = config.username;
			String password = config.password;
			String driver = config.driver;

			dataSource = new BasicDataSource();
			dataSource.setDriverClassName(driver);
			dataSource.setUrl(url);
			dataSource.setUsername(username);
			dataSource.setPassword(password);
			dataSource.setMaxIdle(0);

	}

	@Override
	public UserDAO getUserDAO() {
		return new UserJDBC(dataSource);
	}

	@Override
	public UuidDAO getUuidDAO() {
		return new UuidJDBC(dataSource);
	}
	
	@Override
	public LeagueDAO getLeagueDAO() {
		return new LeagueJDBC(dataSource);
	}
	
	@Override
	public PlayerDAO getPlayerDAO() {
		return new PlayerJDBC(dataSource);
	}
	
	@Override
	public TeamDAO getTeamDAO() {
		return new TeamJDBC(dataSource);
	}

	@Override
	public DayDAO getDayDAO() {
		return new DayJDBC(dataSource);
	}
	
	
}
