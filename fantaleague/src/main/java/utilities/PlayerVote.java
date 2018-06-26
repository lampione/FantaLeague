package utilities;

public class PlayerVote {

	Long playerId;
	String role;
	String name;
	String equipe;
	String initQuote;
	Long dayId;

	Double vote;
	Integer redCard;
	Integer yellowCard;
	Integer taken;
	Integer done;
	Integer assist;
	Double finalVote;

	public Long getPlayerId() {
		return playerId;
	}

	public void setPlayerId(Long playerId) {
		this.playerId = playerId;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEquipe() {
		return equipe;
	}

	public void setEquipe(String equipe) {
		this.equipe = equipe;
	}

	public String getInitQuote() {
		return initQuote;
	}

	public void setInitQuote(String initQuote) {
		this.initQuote = initQuote;
	}

	public Long getDayId() {
		return dayId;
	}

	public void setDayId(Long dayId) {
		this.dayId = dayId;
	}

	public Double getVote() {
		return vote;
	}

	public void setVote(Double vote) {
		this.vote = vote;
	}

	public Integer getRedCard() {
		return redCard;
	}

	public void setRedCard(Integer redCard) {
		this.redCard = redCard;
	}

	public Integer getYellowCard() {
		return yellowCard;
	}

	public void setYellowCard(Integer yellowCard) {
		this.yellowCard = yellowCard;
	}

	public Integer getTaken() {
		return taken;
	}

	public void setTaken(Integer taken) {
		this.taken = taken;
	}

	public Integer getDone() {
		return done;
	}

	public void setDone(Integer done) {
		this.done = done;
	}

	public Integer getAssist() {
		return assist;
	}

	public void setAssist(Integer assist) {
		this.assist = assist;
	}

	public Double getFinalVote() {
		return finalVote;
	}

	public void setFinalVote(Double finalVote) {
		this.finalVote = finalVote;
	}

}
