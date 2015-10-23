package slim3_sample.model.bbs;

import java.io.Serializable;
import java.util.Date;

import org.slim3.datastore.Attribute;
import org.slim3.datastore.Model;
import org.slim3.datastore.ModelRef;

import com.google.appengine.api.datastore.Key;

/**
 * �L���̃R�����g�p�̃��f��
 * @author 10257
 *
 */
@Model(schemaVersion = 1)
public class Comment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Attribute(primaryKey = true)
    private Key key;

    @Attribute(version = true)
    private Long version;

    // ���e�Җ�
    private String userName;
    // �R�����g�{��
    @Attribute(lob = true)
    private String comment;
    // ���e����
    private Date postDate;
    
    // Head�ւ̑���1�̊֘A
    private ModelRef<Head> headRef = new ModelRef<Head>(Head.class);

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
     * @return userName
     */
    public String getUserName() {
        return userName;
    }

    /**
     * @param userName �Z�b�g���� userName
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * @return comment
     */
    public String getComment() {
        return comment;
    }

    /**
     * @param comment �Z�b�g���� comment
     */
    public void setComment(String comment) {
        this.comment = comment;
    }

    /**
     * @return postDate
     */
    public Date getPostDate() {
        return postDate;
    }

    /**
     * @param postDate �Z�b�g���� postDate
     */
    public void setPostDate(Date postDate) {
        this.postDate = postDate;
    }

    /**
     * @return headRef
     */
    public ModelRef<Head> getHeadRef() {
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
        Comment other = (Comment) obj;
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
