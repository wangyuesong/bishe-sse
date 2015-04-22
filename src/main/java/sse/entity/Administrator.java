package sse.entity;

import java.util.List;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
@DiscriminatorValue("Administrator")
public class Administrator extends User {

    /**
	 * 
	 */
    private static final long serialVersionUID = -1793927958183962063L;

    // bi-directional many-to-one association to User

    public Administrator() {

    }

    public Administrator(int id, String account, String name, String password)
    {
        this.setId(id);
        this.setAccount(account);
        this.setName(name);
        this.setPassword(password);
    }
}
