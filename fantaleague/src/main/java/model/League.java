package model;

import java.sql.Date;

import com.google.gson.Gson;

public class League {

    private Long id;
    private String name;
    private Date foundation;

    // relative to every participants
    private int credits;
    private boolean duplicatePlayers;

    // Squad
    private int goalkeepers;
    private int defenders;
    private int midfielders;
    private int forwards;

    // formations
    private int maxTimeToLineup;
    private String modules; // split by commas

    public League() {

    }

    public League(String name, Date foundation, int credits, boolean duplicatePlayers, int goalkeepers, int defenders,
	    int midfielders, int forwards, int maxTimeToLineup, String modules) {
	this.name = name;
	this.foundation = foundation;
	this.credits = credits;
	this.duplicatePlayers = duplicatePlayers;
	this.goalkeepers = goalkeepers;
	this.defenders = defenders;
	this.midfielders = midfielders;
	this.forwards = forwards;
	this.maxTimeToLineup = maxTimeToLineup;
	this.modules = modules;
    }

    public Long getId() {
	return id;
    }

    public void setId(Long id) {
	this.id = id;
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public Date getFoundation() {
	return foundation;
    }

    public void setFoundation(Date foundation) {
	this.foundation = foundation;
    }

    public int getCredits() {
	return credits;
    }

    public void setCredits(int credits) {
	this.credits = credits;
    }

    public boolean isDuplicatePlayers() {
	return duplicatePlayers;
    }

    public void setDuplicatePlayers(boolean duplicatePlayers) {
	this.duplicatePlayers = duplicatePlayers;
    }

    public int getGoalkeepers() {
	return goalkeepers;
    }

    public void setGoalkeepers(int goalkeepers) {
	this.goalkeepers = goalkeepers;
    }

    public int getDefenders() {
	return defenders;
    }

    public void setDefenders(int defenders) {
	this.defenders = defenders;
    }

    public int getMidfielders() {
	return midfielders;
    }

    public void setMidfielders(int midfielders) {
	this.midfielders = midfielders;
    }

    public int getForwards() {
	return forwards;
    }

    public void setForwards(int forwards) {
	this.forwards = forwards;
    }

    public int getMaxTimeToLineup() {
	return maxTimeToLineup;
    }

    public void setMaxTimeToLineup(int maxTimeToLineup) {
	this.maxTimeToLineup = maxTimeToLineup;
    }

    public String getModules() {
	return modules;
    }

    public void setModules(String modules) {
	this.modules = modules;
    }

    public String toGsonString() {
	return new Gson().toJson(this);
    }

}
