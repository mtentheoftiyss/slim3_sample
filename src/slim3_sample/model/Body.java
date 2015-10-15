package slim3_sample.model;

import java.io.Serializable;

import org.slim3.datastore.Attribute;
import org.slim3.datastore.InverseModelRef;
import org.slim3.datastore.Model;

import slim3_sample.meta.HeadMeta;

import com.google.appengine.api.datastore.Key;

@Model(schemaVersion = 1)
public class Body implements Serializable {

    private static final long serialVersionUID = 1L;

    @Attribute(primaryKey = true)
    private Key key;

    @Attribute(version = true)
    private Long version;

    // 記事本文
    @Attribute(lob=true)
    private String text;
    
    // headへの1対1の関連
    @Attribute(persistent=false)
    private InverseModelRef<Head, Body> headRef = 
        new InverseModelRef<Head, Body>(Head.class, HeadMeta.get().bodyRef, this);

    /**
     * Returns the key.
     *
     * @return the key
     */
    public Key getKey() {
        return key;
    }

    /**
     * Sets the key.
     *
     * @param key
     *            the key
     */
    public void setKey(Key key) {
        this.key = key;
    }

    /**
     * Returns the version.
     *
     * @return the version
     */
    public Long getVersion() {
        return version;
    }

    /**
     * Sets the version.
     *
     * @param version
     *            the version
     */
    public void setVersion(Long version) {
        this.version = version;
    }

    /**
     * @return text
     */
    public String getText() {
        return text;
    }

    /**
     * @param text セットする text
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * @return headRef
     */
    public InverseModelRef<Head, Body> getHeadRef() {
        return headRef;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((key == null) ? 0 : key.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Body other = (Body) obj;
        if (key == null) {
            if (other.key != null) {
                return false;
            }
        } else if (!key.equals(other.key)) {
            return false;
        }
        return true;
    }
}
