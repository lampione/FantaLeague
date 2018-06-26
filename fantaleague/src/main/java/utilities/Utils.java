package utilities;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import persistence.PersistenceException;

public class Utils {

    public static final String SCRIPT_PATH = "/script.json";
    public static final String CONFIG_PATH = "/config.json";
    public static final String HTML_EXISTS_PATH = "/invite_exists.html";
    public static final String HTML_NOT_EXISTS_PATH = "/invite_not_exists.html";
    public static final String HTML_FORGOT_PASS_PATH = "/forgot_pass.html";

    public static final int COOKIE_AGE = 60 * 60 * 24 * 30;

    public static Error catchException(Exception exception) {

	Error error = new Error();

	if (exception instanceof PersistenceException) {

	    Long code = ((PersistenceException) exception).getCode();

	    error.setCode(code);

	    if (code == 10005L) {
		error.setMessage("Wrong Email");
	    } else if (code == 10004L) {
		error.setMessage("Wrong Password");
	    } else if (code == 10003L) {
		error.setMessage("Could not save your line-up");
	    } else if (code == 10002L) {
		error.setMessage("Could not save this team");
	    } else if (code == 10000L) {
		error.setMessage("Could not register on league");
	    } else if ((code == 10006L)) {
		error.setMessage("Email already exists");
	    }

	} else if (exception instanceof SQLException) {
	    // 10100 -> connection fail
	    SQLException sqlE = ((SQLException) exception);

	    Integer sqlState = Integer.valueOf(sqlE.getSQLState());
	    Integer sqlStateMin = Integer.valueOf("08000");
	    Integer sqlStateMax = Integer.valueOf("08007");

	    if (sqlState >= sqlStateMin && sqlState <= sqlStateMax) {

		error.setCode(10100L);
		error.setMessage("Connection Failure");

	    } else if (sqlE.getSQLState().equals("23503")) {

		error.setCode(10101L);
		error.setMessage("Reference to requested data not found on our server");

	    } else if (sqlE.getSQLState().equals("23505")) {

		error.setCode(10102L);
		error.setMessage("Data already exists on our server");

	    } else {

		error.setCode(10103L);
		error.setMessage("Internal Database error");

	    }

	} else {
	    error.setCode(10009L);
	    error.setMessage("Internal error");
	}

	return error;
    }

    @SuppressWarnings("unchecked")
    public static Object getJsonFile(@SuppressWarnings("rawtypes") Class type, String path) {
	InputStream stream = Utils.class.getResourceAsStream(path);
	BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
	return new Gson().fromJson(reader, type);
    }

    public static String getStringFromFile(String path) {
	try {
	    InputStream stream = Utils.class.getResourceAsStream(path);
	    BufferedReader reader = new BufferedReader(new InputStreamReader(stream));

	    StringBuilder builder = new StringBuilder();
	    String line = "";

	    while ((line = reader.readLine()) != null) {
		builder.append(line);
	    }

	    return builder.toString();

	} catch (Exception e) {
	    e.printStackTrace();
	}
	return "";
    }

    public static String getCookieValue(HttpServletRequest request, String name) {
	Cookie[] cookies = request.getCookies();
	if (cookies != null) {
	    for (Cookie cookie : cookies) {
		if (name.equals(cookie.getName())) {
		    return cookie.getValue();
		}
	    }
	}
	return null;
    }

    public static void addCookie(HttpServletResponse response, String name, String value, int maxAge) {
	Cookie cookie = new Cookie(name, value);
	cookie.setPath("/");
	cookie.setMaxAge(maxAge);
	response.addCookie(cookie);
    }

    public static void removeCookie(HttpServletResponse response, String name) {
	addCookie(response, name, null, 0);
    }

    public static String getPassCode(String emailUser) {
	String CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
	String chars = CHARS.toLowerCase();
	String string;
	StringBuilder salt = new StringBuilder();
	Random rnd = new Random();
	int indexEmail = 0;
	while (salt.length() < 64) {

	    Random r = new Random();
	    boolean choice = r.nextBoolean();
	    string = chars;
	    if (choice)
		string = CHARS;

	    int index = (int) (rnd.nextFloat() * string.length());
	    salt.append(string.charAt(index));
	    if (indexEmail < emailUser.length()) {
		salt.append(emailUser.charAt(indexEmail));
		indexEmail++;
	    }
	}
	return salt.toString();
    }

    public static String getRegCode() {
	String CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
	String chars = CHARS.toLowerCase();
	String string;
	StringBuilder salt = new StringBuilder();
	Random rnd = new Random();

	while (salt.length() < 64) {

	    Random r = new Random();
	    boolean choice = r.nextBoolean();
	    string = chars;
	    if (choice)
		string = CHARS;

	    int index = (int) (rnd.nextFloat() * string.length());
	    salt.append(string.charAt(index));
	}
	return salt.toString();
    }

    public static int contains(String email, ArrayList<RegistrationUserInvite> emailUserNotExist) {

	for (int i = 0; i < emailUserNotExist.size(); i++) {
	    if (emailUserNotExist.get(i).getUser().equals(email)) {
		return i;
	    }
	}

	return -1;
    }
}
