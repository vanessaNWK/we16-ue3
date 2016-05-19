package at.ac.tuwien.big.we16.ue3.model;


/**
 * Created by vanessa on 10.05.16.
 */
public class ErrorBean {
    private String firstnameError = "";
    private String lastnameError = "";
    private String emailError = "";
    private String passwordError = "";
    private String dateError = "";

    public ErrorBean() {

    }


    public ErrorBean(final String firstnameError, final String lastnameError, final String emailError, final String passwordError, final String dateError) {
        this.firstnameError = firstnameError;
        this.lastnameError = lastnameError;
        this.emailError = emailError;
        this.passwordError = passwordError;
        this.dateError = dateError;
    }

    public String getFirstnameError() {
        return firstnameError;
    }

    public void setFirstnameError(final String firstnameError) {
        this.firstnameError = firstnameError;
    }

    public String getLastnameError() {
        return lastnameError;
    }

    public ErrorBean setLastnameError(final String lastnameError) {
        this.lastnameError = lastnameError;
        return this;
    }

    public String getEmailError() {
        return emailError;
    }

    public void setEmailError(final String emailError) {
        this.emailError = emailError;
    }

    public String getPasswordError() {
        return passwordError;
    }

    public void setPasswordError(final String passwordError) {
        this.passwordError = passwordError;
    }

    public String getDateError() {
        return dateError;
    }

    public void setDateError(final String dateError) {
        this.dateError = dateError;
    }

}
