package com.broking.dynamicproperties.approach;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapKeyColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "CUSTOMER", uniqueConstraints = { @UniqueConstraint(name = "UK_CUST_ID", columnNames = { "ID" }) })
public class Customer implements Serializable, Cloneable {

    /**
     * 
     */
    private static final long serialVersionUID = 7608717243972552810L;

    /** The id. */
    @Id
    @Column(name = "ID", length = 60)
    private String id;

    /** The properties. */
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "CUSTOMER_PROPERTIES", joinColumns = @JoinColumn(name = "CUST_ID"))
    @MapKeyColumn(name = "NAME")
    @Column(name = "VALUE")
    private Map<String, String> properties;

    /**
     * @return all attributes of Customer will be returned as hash map
     */
    public Map<String, String> getProperties() {
        return properties;
    }

    /**
     * @param properties all attributes of Customer will be put in this map
     */
    public void setProperties(Map<String, String> properties) {
        this.properties = properties;
    }

    /**
     * @return Customer id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id Customer id to be set
     */
    public void setId(String id) {
        this.id = id;
    }

    @OneToMany(mappedBy = "customer")
    private List<Query> queries = new ArrayList<Query>();

    public List<Query> getQueries() {
        return queries;
    }

    /** The name. */
    @Column(name = "NAME", length = 255)
    private String name;

    /** The deployable. */
    @Column(name = "DOB")
    private Date dob;

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    /**
     * @return Customer name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name Customer name to be set
     */
    public void setName(String name) {
        this.name = name;
    }

    /** The createts. */
    @Column(name = "CREATETS", updatable = false)
    private Timestamp createts;

    /** The updatets. */
    @Column(name = "UPDATETS", insertable = false)
    private Timestamp updatets;

    /**
     * @return create time stamp
     */
    public Timestamp getCreatets() {
        return createts;
    }

    /**
     * @param createts create time stamp will be set
     */
    public void setCreatets(Timestamp createts) {
        this.createts = createts;
    }

    /**
     * @return update time stamp
     */
    public Timestamp getUpdatets() {
        return updatets;
    }

    /**
     * @param updatets update time stamp will be set
     */
    public void setUpdatets(Timestamp updatets) {
        this.updatets = updatets;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuffer strbuf = new StringBuffer();
        Class c = this.getClass();
        Field[] decFields = c.getDeclaredFields();
        strbuf.append(c.getName()).append(System.getProperty("line.separator"));
        for (int i = 0; i < decFields.length; i++) {
            try {
                if (!(decFields[i].getName().startsWith("methodField")
                    || decFields[i].getName().startsWith("woven_proxy")
                    || decFields[i].getName().startsWith("org_apache") || decFields[i].getName().startsWith("pc"))
                    || decFields[i].getName().startsWith("class")) {
                    strbuf.append(decFields[i].getName()).append('=').append(decFields[i].get(this))
                        .append(System.getProperty("line.separator"));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return strbuf.toString();
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if ((getClass() != obj.getClass())
            || ((this.getId() != null) && !this.getId().equals(((Customer) obj).getId()))) {
            return false;
        }
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#clone()
     */
    @Override
    public Object clone() throws CloneNotSupportedException {

        Customer cp = new Customer();

        cp.setId(id);
        cp.setName(name);
        cp.getQueries().addAll(queries);

        if (properties != null) {
            Map<String, String> props = new HashMap<String, String>();
            props.putAll(properties);
            cp.setProperties(props);
        }

        if (createts != null) {
            cp.setCreatets((Timestamp) createts.clone());
        }
        if (updatets != null) {
            cp.setUpdatets((Timestamp) updatets.clone());
        }

        return cp;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int hashCode = 1;
        hashCode = prime * hashCode + ((id == null) ? 0 : id.hashCode());
        hashCode = prime * hashCode + ((name == null) ? 0 : name.hashCode());
        hashCode = prime * hashCode + ((queries == null) ? 0 : queries.hashCode());
        hashCode = prime * hashCode + ((properties == null) ? 0 : properties.hashCode());
        hashCode = prime * hashCode + ((createts == null) ? 0 : createts.hashCode());
        hashCode = prime * hashCode + ((updatets == null) ? 0 : updatets.hashCode());
        return hashCode;
    }

}
