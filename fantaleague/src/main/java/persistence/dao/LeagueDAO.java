package persistence.dao;

import java.util.ArrayList;
import java.util.List;

import model.League;
import model.User;
import utilities.RegistrationUserInvite;
import utilities.TeamScore;

public interface LeagueDAO {

	public void create(League league, String adminEmail, String teamName) throws Exception;

	public League findById(Long id) throws Exception;

	public List<User> findInLeague(Long leagueId) throws Exception;

	public void invite(Long leagueId, String[] email, ArrayList<RegistrationUserInvite> emailUserNotExist) throws Exception;

	public boolean alreadyUserSignedUp(String email, Long leagueId) throws Exception;

	public void updateSignedUp(Long leagueId, String email, String teamName, String status) throws Exception;

	public void deleteSignedUp(Long teamId) throws Exception;

	public List<TeamScore> leagueTable(Long leagueId) throws Exception;

	public void updateLName(String lName, Long lId) throws Exception;

	public void deleteLeague(Long leagueId) throws Exception;

}
