package vip.com.vipmeetings.models;

import androidx.annotation.NonNull;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Created by Srinath on 30/05/17.
 */

@Root(strict = false)
public class Contact implements Comparable<Contact> {

    @Element(name = "Name", required = false)
    private String Name;
    @Element(name = "Email", required = false)
    private String Email;

    @Element(name = "E-mail", required = false)
    private String Email1;

    @Element(name = "Mobile", required = false)
    private String Mobile;
    @Element(name = "Company", required = false)
    private String Company;


    @Element(name = "FirstName", required = false)
    private String FirstName;

    @Element(name = "FamilyName", required = false)
    private String FamilyName;

    @Element(name = "Country", required = false)
    private String Country;
    @Element(name = "City", required = false)
    private String City;

    @Element(name = "last", required = false)
    private String lastName;

    public String getEmail1() {
        return Email1;
    }

    public void setEmail1(String email1) {
        Email1 = email1;
    }


    @Override
    public String toString() {
        if (getMobile() != null)
            return getMobile();
        else if (getName() != null) {
            return getName();
        } else if (getFirstName() != null) {
            return getFirstName();
        }

        return super.toString();
    }


    @Override
    public boolean equals(Object obj) {

        if (obj instanceof Contact) {

            if (getMobile() != null && ((Contact) obj).getMobile() != null
                    && ((Contact) obj).getMobile().equalsIgnoreCase(getMobile())) {

                return true;
            }
            else if (getFirstName() != null && ((Contact) obj).getFirstName() != null
                    && ((Contact) obj).getFirstName().equalsIgnoreCase(getFirstName())) {

                return true;
            } else if (getName() != null && ((Contact) obj).getName() != null
                    && ((Contact) obj).getName().equalsIgnoreCase(getName())) {

                return true;
            } else if (getEmail() != null && ((Contact) obj).getEmail() != null
                    && ((Contact) obj).getEmail().equalsIgnoreCase(getEmail())) {

                return true;
            } else if (getEmail1() != null && ((Contact) obj).getEmail1() != null
                    && ((Contact) obj).getEmail1().equalsIgnoreCase(getEmail1())) {

                return true;
            }else if (getEmail1() != null && ((Contact) obj).getEmail1() != null
                    && ((Contact) obj).getEmail1().equalsIgnoreCase(getEmail1())) {

                return true;
            }else if (getFirstName() != null && ((Contact) obj).getFirstName() != null
                    && ((Contact) obj).getFirstName().equalsIgnoreCase(getFirstName())) {

                return true;
            }
        }

        return false;
    }

    @Override
    public int hashCode() {

        if (getMobile() != null)
            return getMobile().hashCode();
        else if (getName() != null) {

            return getName().hashCode();
        } else if (getEmail1() != null) {
            return getEmail1().hashCode();
        } else if (getEmail() != null) {
            return getEmail().hashCode();
        }

        return super.hashCode();
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFamilyName() {
        return FamilyName;
    }

    public void setFamilyName(String familyName) {
        FamilyName = familyName;
    }

    @Element(required = false)
    private String Type;

    public String getType() {
        return Type;
    }

    public void setType(String Type) {
        this.Type = Type;
    }


    @Element(name = "select", required = false)
    private boolean isSelect;

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String Email) {
        this.Email = Email;
    }

    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String Mobile) {
        this.Mobile = Mobile;
    }

    public String getCompany() {
        return Company;
    }

    public void setCompany(String Company) {
        this.Company = Company;
    }

    public String getCountry() {
        return Country;
    }

    public void setCountry(String Country) {
        this.Country = Country;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String City) {
        this.City = City;
    }

    @Override
    public int compareTo(@NonNull Contact o) {
        return getName().compareTo(o.getName());
    }
}
