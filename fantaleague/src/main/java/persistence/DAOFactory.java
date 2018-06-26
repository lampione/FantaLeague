package persistence;

import persistence.dao.DayDAO;
import persistence.dao.LeagueDAO;
import persistence.dao.PlayerDAO;
import persistence.dao.TeamDAO;
import persistence.dao.UserDAO;
import persistence.dao.UuidDAO;

public abstract class DAOFactory {

	public static final int HSQLDB = 1;

	public static final int POSTGRESQL = 2;

	public static DAOFactory getDAOFactory(int which) {

		switch (which) {

			case HSQLDB:
				return null;
			case POSTGRESQL:
				return new PostgresDAOFactory();
			default:
				return null;
		}
	}

	public abstract UserDAO getUserDAO();

	public abstract UuidDAO getUuidDAO();

	public abstract LeagueDAO getLeagueDAO();

	public abstract PlayerDAO getPlayerDAO();

	public abstract TeamDAO getTeamDAO();

	public abstract DayDAO getDayDAO();
}
