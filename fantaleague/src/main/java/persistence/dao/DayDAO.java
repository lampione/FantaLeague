package persistence.dao;

import java.util.List;

import model.Day;
import model.SerieAMatch;

public interface DayDAO {

	public Day getCurrentDay() throws Exception;

	public List<SerieAMatch> getSerieAMatches(Long dayId) throws Exception;

	public boolean dayVoteExist(Long dayParam) throws Exception;

}
