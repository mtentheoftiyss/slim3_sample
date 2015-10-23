package slim3_sample.model.bbs;

import java.io.Serializable;
import java.util.Date;

import org.slim3.datastore.Attribute;
import org.slim3.datastore.InverseModelListRef;
import org.slim3.datastore.Model;
import org.slim3.datastore.ModelRef;

import slim3_sample.meta.bbs.CommentMeta;

import com.google.appengine.api.datastore.Key;

/**
 * �L���̌��o���p�̃��f��
 * @author 10257
 *
 */
@Model(schemaVersion = 1)
public class Head implements Serializable {

    private static final long serialVersionUID = 1L;

    @Attribute(primaryKey = true)
    private Key key;

    @Attribute(version = true)
    private Long version;

    // �L�����o��
    private String subject;
    // ���e����
    private Date postDate;
    // ���e�Җ�
    private String userName;
    // ���e�҃��[���A�h���X
    private String mailAddress;
    // �ҏW�p�p�X���[�h
    private String password;
    // �ŐV�R�����gID(���R�����g��)
    private Long lastCommentId = 0L;
    // �ŐV�R�����g����
    private Date lastCommentDate;

    // Body�ւ�1��1�̊֘A
    private ModelRef<Body> bodyRef = new ModelRef<Body>(Body.class);
    // Comment�ւ�1�Α��̊֘A
    @Attribute(persistent = false)
    private InverseModelListRef<Comment, Head> commentRef =
        new InverseModelListRef<Comment, Head>(Comment.class, CommentMeta.get().headRef, this);

    /**
     * @return subject
     */
    public String getSubject() {
        return subject;
    }

    /**
     * @param subject �Z�b�g���� subject
     */
    public void setSubject(String subject) {
        this.subject = subject;
    }

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
     * @return postData
     */
    public Date getPostDate() {
        return postDate;
    }

    /**
     * @param postData �Z�b�g���� postData
     */
    public void setPostDate(Date postData) {
        this.postDate = postData;
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
     * @return password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password �Z�b�g���� password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return lastCommentId
     */
    public Long getLastCommentId() {
        return lastCommentId;
    }

    /**
     * @param lastCommentId �Z�b�g���� lastCommentId
     */
    public void setLastCommentId(Long lastCommentId) {
        this.lastCommentId = lastCommentId;
    }

    /**
     * @return lastCommentDate
     */
    public Date getLastCommentDate() {
        return lastCommentDate;
    }

    /**
     * @param lastCommentDate �Z�b�g���� lastCommentDate
     */
    public void setLastCommentDate(Date lastCommentDate) {
        this.lastCommentDate = lastCommentDate;
    }

    /**
     * @return bodyRef
     */
    public ModelRef<Body> getBodyRef() {
        return bodyRef;
    }

    /**
     * @return commentRef
     */
    public InverseModelListRef<Comment, Head> getCommentRef() {
        return commentRef;
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
        Head other = (Head) obj;
        if (key == null) {
            if (other.key != null) {
                return false;
            }
        } else if (!key.equals(other.key)) {
            return false;
        }
        return true;
    }

    public String getMailAddress() {
        return mailAddress;
    }

    public void setMailAddress(String mailAddress) {
        this.mailAddress = mailAddress;
    }
}
