package server.database.model;


import com.google.gson.annotations.SerializedName;

/** Represents a User row in the users table. */
public class User extends BaseModel {

    private String username;
    private String password; // TODO: should be a hash when set???
    private String email;
    private String firstName;
    private String lastName;
    private String gender;
    private String personID;

    public User() {

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPersonID() {
        return personID;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }

    @Override
    public User clone() {
        try {
            return (User) super.clone();
        } catch (CloneNotSupportedException e) {
            User user = new User();
            user.setUsername(getUsername());
            user.setPassword(getPassword());
            user.setEmail(getEmail());
            user.setFirstName(getFirstName());
            user.setLastName(getLastName());
            user.setGender(getGender());
            user.setPersonID(getPersonID());
            return user;
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("\nUser(");
        sb.append(toStringHelper("username"));
        sb.append(toStringHelper("password"));
        sb.append(toStringHelper("email"));
        sb.append(toStringHelper("firstName"));
        sb.append(toStringHelper("lastName"));
        sb.append(toStringHelper("gender"));
        sb.append(toStringHelper("personID"));
        sb.append("\n)");
        String format = sb.toString();
        return String.format(
            format,
            getUsername(),
            getPassword(),
            getEmail(),
            getFirstName(),
            getLastName(),
            getGender(),
            getPersonID()
        );
    }
}