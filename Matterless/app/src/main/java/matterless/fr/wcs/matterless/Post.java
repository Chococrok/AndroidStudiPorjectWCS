package matterless.fr.wcs.matterless;

/**
 * Created by apprenti on 31/03/17.
 */

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Post {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("create_at")
    @Expose
    private Integer createAt;
    @SerializedName("update_at")
    @Expose
    private Integer updateAt;
    @SerializedName("delete_at")
    @Expose
    private Integer deleteAt;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("channel_id")
    @Expose
    private String channelId;
    @SerializedName("root_id")
    @Expose
    private String rootId;
    @SerializedName("parent_id")
    @Expose
    private String parentId;
    @SerializedName("original_id")
    @Expose
    private String originalId;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("props")
    @Expose
    private Props props;
    @SerializedName("hashtag")
    @Expose
    private String hashtag;
    @SerializedName("filenames")
    @Expose
    private List<String> filenames = null;
    @SerializedName("pending_post_id")
    @Expose
    private String pendingPostId;
    @SerializedName("is_pinned")
    @Expose
    private Integer isPinned;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Integer createAt) {
        this.createAt = createAt;
    }

    public Integer getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(Integer updateAt) {
        this.updateAt = updateAt;
    }

    public Integer getDeleteAt() {
        return deleteAt;
    }

    public void setDeleteAt(Integer deleteAt) {
        this.deleteAt = deleteAt;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getRootId() {
        return rootId;
    }

    public void setRootId(String rootId) {
        this.rootId = rootId;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getOriginalId() {
        return originalId;
    }

    public void setOriginalId(String originalId) {
        this.originalId = originalId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Props getProps() {
        return props;
    }

    public void setProps(Props props) {
        this.props = props;
    }

    public String getHashtag() {
        return hashtag;
    }

    public void setHashtag(String hashtag) {
        this.hashtag = hashtag;
    }

    public List<String> getFilenames() {
        return filenames;
    }

    public void setFilenames(List<String> filenames) {
        this.filenames = filenames;
    }

    public String getPendingPostId() {
        return pendingPostId;
    }

    public void setPendingPostId(String pendingPostId) {
        this.pendingPostId = pendingPostId;
    }

    public Integer getIsPinned() {
        return isPinned;
    }

    public void setIsPinned(Integer isPinned) {
        this.isPinned = isPinned;
    }


    public class Props {


    }
}

