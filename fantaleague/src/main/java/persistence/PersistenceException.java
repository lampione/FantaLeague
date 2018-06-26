package persistence;

public class PersistenceException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private Long code;

    // 10000 -> non è stato possibile effettuare la registrazione alla lega
    // 10002 -> non abbiamo potuto salvare il team
    // 10003 -> non abbiamo potuto salvare la formazione per la giornata
    // 10004 -> psswd sbagliata
    // 10005 -> email sbagliata
    // 10006 -> Email esistente

    public PersistenceException(Long code) {
	this.code = code;
    }

    public Long getCode() {
	return code;
    }

}
