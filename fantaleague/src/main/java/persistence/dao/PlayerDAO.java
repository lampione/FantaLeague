package persistence.dao;

import java.util.List;

import model.Player;
import utilities.PlayerVote;

public interface PlayerDAO {

	public List<PlayerVote> findPlayerById(Long playerId) throws Exception;

	public List<Player> findPlayersByRole(String role) throws Exception;

	public List<Player> findPlayersByRoleNoDuplicate(String role, Long leagueId, Long teamId) throws Exception;

	public List<Player> findPlayerByQuery(String clean) throws Exception;

	public boolean playerExsist(Long pId) throws Exception;

}
