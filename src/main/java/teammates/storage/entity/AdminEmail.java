package teammates.storage.entity;

import java.util.Date;
import java.util.List;

import javax.jdo.annotations.Extension;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Text;

/**
 * Represents emails composed by Admin 
 */

@PersistenceCapable
public class AdminEmail {
    
    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    @Extension(vendorName="datanucleus", key="gae.encoded-pk", value="true")
    private String emailId;
    
    @Persistent
    private List<String> addressReceiver;
    
    @Persistent
    private List<String> groupReceiver;
    
    @Persistent
    private String subject;
    
    @Persistent
    private Date sendDate;
    
    @Persistent
    @Extension(vendorName = "datanucleus", key = "gae.unindexed", value = "true")
    private Text content;
    
    /**
     * Instantiates a new AdminEmail
     * @param receiver
     *          a string list contaning all receiver (could be email address or characteristics{eg. joined date range} )
     * @param subject
     *          email subject
     * @param content
     *          html email content
     */
    public AdminEmail(List<String> addressReceiver, List<String> groupReceiver, String subject, Text content){
        this.emailId = null;
        this.addressReceiver = addressReceiver;
        this.groupReceiver = groupReceiver;
        this.subject = subject;
        this.content = content;
        this.sendDate = new Date();
    }
    
    public void setAddressReceiver(List<String> receiver){
        this.addressReceiver = receiver;
    }
    
    public void setGroupReceiver(List<String> receiver){
        this.groupReceiver = receiver;
    }
    
    public void setSubject(String subject){
        this.subject = subject;
    }
    
    public void setContent(Text content){
        this.content = content;
    }
    
    public String getEmailId(){
        return this.emailId;
    }
    
    public List<String> getAddressReceiver(){
        return this.addressReceiver;
    }
    
    public List<String> getGroupReceiver(){
        return this.groupReceiver;
    }
     
    public String getSubject(){
        return this.subject;
    }
    
    public Date getSendDate(){
        return this.sendDate;
    }
    
    public Text getContent(){
        return this.content;
    }
}
