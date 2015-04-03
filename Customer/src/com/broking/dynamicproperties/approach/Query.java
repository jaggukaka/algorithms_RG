package com.broking.dynamicproperties.approach;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapKeyColumn;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "Query", uniqueConstraints = { @UniqueConstraint(name = "UK_QRY_ID", columnNames = { "ID" }) })
public class Query implements Serializable, Cloneable {

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
    @CollectionTable(name = "Query_PROP", joinColumns = @JoinColumn(name = "QRY_ID"))
    @MapKeyColumn(name = "NAME")
    @Column(name = "VALUE")
    private Map<String, String> properties;

    /**
     * @return Query id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id Query id to be set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return all attributes of Query will be returned as hash map
     */
    public Map<String, String> getProperties() {
        return properties;
    }

    /**
     * @param properties all attributes of Query will be put in this map
     */
    public void setProperties(Map<String, String> properties) {
        this.properties = properties;
    }

    @ManyToOne
    protected Customer customer;

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    /** The Description. */
    @Column(name = "Description", length = 255)
    protected String description;

    /** The createts. */
    @Column(name = "CREATE_TS", updatable = false)
    protected Timestamp createts;

    /** The updatets. */
    @Column(name = "UPDATE_TS", insertable = false)
    protected Timestamp updatets;

    protected enum QueryType {
        Accountstatement, Changeofaddress, Changeofpassword, General
    }

    @Enumerated(EnumType.STRING)
    protected QueryType queryType;

    public QueryType getQueryType() {
        return queryType;
    }

    public void setQueryType(QueryType queryType) {
        this.queryType = queryType;
    }

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
            || ((this.getId() != null) && !this.getId().equals(((Query) obj).getId()))) {
            return false;
        }
        return true;
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
     * @see java.lang.Object#clone()
     */
    @Override
    public Object clone() throws CloneNotSupportedException {

        Query cp = new Query();

        cp.setId(id);
        cp.setDescription(description);
        cp.setCustomer(customer);
        cp.setQueryType(queryType);

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
        hashCode = prime * hashCode + ((queryType == null) ? 0 : queryType.toString().hashCode());
        hashCode = prime * hashCode + ((customer == null) ? 0 : customer.hashCode());
        hashCode = prime * hashCode + ((description == null) ? 0 : description.hashCode());
        hashCode = prime * hashCode + ((properties == null) ? 0 : properties.hashCode());
        hashCode = prime * hashCode + ((createts == null) ? 0 : createts.hashCode());
        hashCode = prime * hashCode + ((updatets == null) ? 0 : updatets.hashCode());
        return hashCode;
    }

}
