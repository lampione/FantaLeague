package persistence.dao;

import model.User;

public interface UuidDAO {

	public User find(String uuid) throws Exception;
	public void createUpdate(String uuid, String email) throws Exception;
	public void delete(String email) throws Exception;

}
