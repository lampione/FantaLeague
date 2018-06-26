package model;

public class Player {

	private Long id;
	private String role;
	private String name;
	private String equipe;
	private Long initQuote;
	
	public Player() {}

	public Player(Long id, String role, String name, String equipe, Long initQuote) {
		super();
		this.id = id;
		this.role = role;
		this.name = name;
		this.equipe = equipe;
		this.initQuote = initQuote;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public Long getInitQuote() {
		return initQuote;
	}

	public void setInitQuote(Long initQuote) {
		this.initQuote = initQuote;
	}

}
