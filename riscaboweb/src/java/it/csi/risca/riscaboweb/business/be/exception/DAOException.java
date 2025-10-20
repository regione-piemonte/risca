package it.csi.risca.riscaboweb.business.be.exception;

public class DAOException extends GenericException {

    private static final long serialVersionUID = 1L;

    public DAOException(String msg) {
        super(msg);
    }

    public DAOException(Throwable arg0) {
        super(arg0);

    }

}