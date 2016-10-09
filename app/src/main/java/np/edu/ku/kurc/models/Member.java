package np.edu.ku.kurc.models;

public class Member {

    /**
     * Id of member.
     */
    public int id;

    /**
     * Name of member.
     */
    public String name;

    /**
     * Email of member.
     */
    public String email;

    /**
     * Phone number of member.
     */
    public String phone;

    /**
     * Department of member.
     */
    public String dept;

    /**
     * Constructs a member object.
     *
     * @param id    Id of member.
     * @param name  Name of member.
     * @param dept  Department of member.
     * @param phone Phone number of member.
     * @param email Email of member.
     */
    public Member(int id, String name, String dept, String phone, String email) {
        this.id = id;
        this.name = name;
        this.dept = dept;
        this.phone = phone;
        this.email = email;
    }

    /**
     * Returns a guest member.
     *
     * @return  Guest member.
     */
    public static Member getGuestMember() {
        return new Member(0,"Guest","","","");
    }
}
