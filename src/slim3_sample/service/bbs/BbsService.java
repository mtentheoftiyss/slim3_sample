package slim3_sample.service.bbs;

import java.util.ArrayList;
import java.util.List;

import org.slim3.datastore.Datastore;

import slim3_sample.meta.bbs.BodyMeta;
import slim3_sample.meta.bbs.CommentMeta;
import slim3_sample.meta.bbs.HeadMeta;
import slim3_sample.model.bbs.Body;
import slim3_sample.model.bbs.Comment;
import slim3_sample.model.bbs.Head;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Transaction;

/**
 * �f���X�V�p�T�[�r�X
 * @author 10257
 *
 */
public class BbsService {
    
    // �L���ꗗ�̎擾
    public List<Head> getAll() {
        HeadMeta m = HeadMeta.get();
        return Datastore.query(m).sort(m.postDate.desc).asList();
    }
    
    // �L���̓o�^
    public void insert(Head head, Body body) throws Exception {
        head.setKey(Datastore.allocateId(HeadMeta.get()));
        body.setKey(Datastore.allocateId(head.getKey(), BodyMeta.get()));
        head.getBodyRef().setModel(body);
        
        Transaction tx = Datastore.beginTransaction();
        try {
            Datastore.put(tx, head, body);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw e;
        }
    }
    
    // �R�����g�̓o�^
    public void insert(Head head, Comment comment) throws Exception {
        long newCommentId = head.getLastCommentId() + 1L;
        
        Key commentKey = Datastore.createKey(head.getKey(), CommentMeta.get(), newCommentId);
        comment.setKey(commentKey);
        comment.getHeadRef().setModel(head);
        
        head.setLastCommentId(newCommentId);
        head.setLastCommentDate(comment.getPostDate());
        
        Transaction tx = Datastore.beginTransaction();
        try {
            Datastore.get(tx, HeadMeta.get(), head.getKey(), head.getVersion());
            Datastore.put(tx, head, comment);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw e;
        }
        
    }
    
    // �L���̎擾
    public Head get(Key headKey) {
        return Datastore.getOrNull(HeadMeta.get(), headKey);
    }
    
    // �L���̍X�V
    public void update(Head head, Body body) throws Exception {
        Transaction tx = Datastore.beginTransaction();
        try {
            Datastore.get(tx, HeadMeta.get(), head.getKey(), head.getVersion());
            Datastore.put(tx, head, body);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw e;
        }
    }
    
    // �L���̍폜
    public void delete(Key headKey) throws Exception {
        Transaction tx = Datastore.beginTransaction();
        try {
            Head head = Datastore.get(tx, HeadMeta.get(), headKey);
            Key bodyKey = head.getBodyRef().getKey();
            List<Comment> commentList = head.getCommentRef().getModelList();
            List<Key> commentKeys= new ArrayList<Key>();
            for (Comment cmnt: commentList) {
                commentKeys.add(cmnt.getKey());
            }
            Datastore.delete(tx, headKey, bodyKey);
            Datastore.delete(tx, commentKeys);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw e;
        }
    }
    
}
