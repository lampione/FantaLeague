package persistence.dao;

import java.sql.Date;
import java.util.List;

import model.User;
import utilities.Invite;
import utilities.Pair;
import utilities.UserTeamName;

public interface UserDAO {

	public void create(User user, String password) throws Exception;

	public User findByEmail(String email) throws Exception;

	public void setPassword(String email, String password) throws Exception;

	public User validate(String email, String password) throws Exception;

	public List<Pair> findLeagues(String email, String status, Long dayId) throws Exception;

	public List<Invite> findInvites(String email) throws Exception;

	public List<UserTeamName> findUserTeamName(Long leagueId) throws Exception;

	public void createRecoveryPass(String id, String user_email) throws Exception;

	public boolean existIdRecover(String id) throws Exception;

	public String findUserEmailByRequestId(String id) throws Exception;

	public void deleteFromRequest(String id) throws Exception;

	public boolean userNotExist(String email) throws Exception;

	public String rCodeMatch(String code) throws Exception;

	public void updateUserInvite(String email, String firstName, String lastName, Date bornField, String password) throws Exception;

	public boolean passCodeMatch(String id) throws Exception;

	public boolean isStatusUserSession(String status, String email, Long leagueId) throws Exception;

	public void updateFirstName(String email, String firstName) throws Exception;

	public void updateLastName(String email, String lastName) throws Exception;

	public void updateBorn(String email, Date born) throws Exception;

	public boolean isUserInLeague(String email, Long leagueId) throws Exception;

}
