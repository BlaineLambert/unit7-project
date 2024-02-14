public class Person {
    private String firstname;
    private String lastname;
    private String phonenumber;

    public Person(String firstname, String lastname) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.phonenumber = phonenumber;
    }

    public String getFirstName() {
        return firstname;
    }

    public String getLastName() {
        return lastname;
    }

    public String getPhoneNumber() {
        return phonenumber;
    }


    public void setFirstName(String firstname) {
        this.firstname = firstname;
    }

    public void setLastName(String lastname) {
        this.lastname = lastname;
    }

    public void setPhoneNumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }



    @Override
    public String toString() {
        return "Person{" +
                "firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", phoneNumber='" + phonenumber + '\'' +
                '}';
    }
}
