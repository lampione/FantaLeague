package persistence.dao;

import java.util.List;
import java.util.Map;

import model.Player;
import model.Team;
import utilities.PlayerMatch;
import utilities.PlayerVote;
import utilities.TeamScore;

public interface TeamDAO {

	public void saveTeam(Long teamId, List<Player> players) throws Exception;

	public List<Player> findPlayers(Long teamId) throws Exception;

	public int replacePlayer(Long oldId, Long newId, Long teamId, Long credits) throws Exception;

	public Team getTeam(Long leagueId, String email) throws Exception;

	public void saveTeamMatch(Long teamId, Long dayId, List<Player> playerStarters, List<Player> playerBench) throws Exception;

	public List<PlayerMatch> findPlayersMatch(Long teamId, Long dayId) throws Exception;

	public Map<Long, List<PlayerVote>> findTeamPlayerVote(Long dayId, List<TeamScore> teamScores) throws Exception;

	public void updateTName(Long tId, String tName) throws Exception;

}
