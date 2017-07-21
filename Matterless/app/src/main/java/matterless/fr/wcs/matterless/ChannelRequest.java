
package matterless.fr.wcs.matterless;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ChannelRequest {

    @SerializedName("channels")
    @Expose
    private List<Channel> channels = null;
    @SerializedName("members")
    @Expose
    private Members members;

    public List<Channel> getChannels() {
        return channels;
    }

    public void setChannels(List<Channel> channels) {
        this.channels = channels;
    }

    public Members getMembers() {
        return members;
    }

    public void setMembers(Members members) {
        this.members = members;
    }

    public ArrayList<Channel> getPublicChannel () {

        ArrayList<Channel> publicChan = new ArrayList<>();
        for (int i = 0; i < channels.size(); i ++) {

            if (channels.get(i).getDisplayName() != "") {

                publicChan.add(channels.get(i));

            }
        }
        return publicChan;
    }

    public java.lang.String[] getChannelNames(ArrayList<Channel> channels) {

        java.lang.String[] strings = new java.lang.String[getPublicChannel().size()];

        for (int i = 0; i < getPublicChannel().size(); i++) {
            if (getPublicChannel().get(i).getDisplayName().length() != 0 ) {
                strings[i] = (getPublicChannel().get(i).getDisplayName());
            }
        }
        return strings;
    }

    public class Channel {

        @SerializedName("id")
        @Expose
        private java.lang.String id;
        @SerializedName("create_at")
        @Expose
        private java.lang.String createAt;
        @SerializedName("update_at")
        @Expose
        private java.lang.String updateAt;
        @SerializedName("delete_at")
        @Expose
        private java.lang.String deleteAt;
        @SerializedName("team_id")
        @Expose
        private java.lang.String teamId;
        @SerializedName("type")
        @Expose
        private java.lang.String type;
        @SerializedName("display_name")
        @Expose
        private java.lang.String displayName;
        @SerializedName("name")
        @Expose
        private java.lang.String name;
        @SerializedName("header")
        @Expose
        private java.lang.String header;
        @SerializedName("purpose")
        @Expose
        private java.lang.String purpose;
        @SerializedName("last_post_at")
        @Expose
        private java.lang.String lastPostAt;
        @SerializedName("total_msg_count")
        @Expose
        private Integer totalMsgCount;
        @SerializedName("extra_update_at")
        @Expose
        private java.lang.String extraUpdateAt;
        @SerializedName("creator_id")
        @Expose
        private java.lang.String creatorId;

        public java.lang.String getId() {
            return id;
        }

        public void setId(java.lang.String id) {
            this.id = id;
        }

        public java.lang.String getCreateAt() {
            return createAt;
        }

        public void setCreateAt(java.lang.String createAt) {
            this.createAt = createAt;
        }

        public java.lang.String getUpdateAt() {
            return updateAt;
        }

        public void setUpdateAt(java.lang.String updateAt) {
            this.updateAt = updateAt;
        }

        public java.lang.String getDeleteAt() {
            return deleteAt;
        }

        public void setDeleteAt(java.lang.String deleteAt) {
            this.deleteAt = deleteAt;
        }

        public java.lang.String getTeamId() {
            return teamId;
        }

        public void setTeamId(java.lang.String teamId) {
            this.teamId = teamId;
        }

        public java.lang.String getType() {
            return type;
        }

        public void setType(java.lang.String type) {
            this.type = type;
        }

        public java.lang.String getDisplayName() {
            return displayName;
        }

        public void setDisplayName(java.lang.String displayName) {
            this.displayName = displayName;
        }

        public java.lang.String getName() {
            return name;
        }

        public void setName(java.lang.String name) {
            this.name = name;
        }

        public java.lang.String getHeader() {
            return header;
        }

        public void setHeader(java.lang.String header) {
            this.header = header;
        }

        public java.lang.String getPurpose() {
            return purpose;
        }

        public void setPurpose(java.lang.String purpose) {
            this.purpose = purpose;
        }

        public java.lang.String getLastPostAt() {
            return lastPostAt;
        }

        public void setLastPostAt(java.lang.String lastPostAt) {
            this.lastPostAt = lastPostAt;
        }

        public Integer getTotalMsgCount() {
            return totalMsgCount;
        }

        public void setTotalMsgCount(Integer totalMsgCount) {
            this.totalMsgCount = totalMsgCount;
        }

        public java.lang.String getExtraUpdateAt() {
            return extraUpdateAt;
        }

        public void setExtraUpdateAt(java.lang.String extraUpdateAt) {
            this.extraUpdateAt = extraUpdateAt;
        }

        public java.lang.String getCreatorId() {
            return creatorId;
        }

        public void setCreatorId(java.lang.String creatorId) {
            this.creatorId = creatorId;
        }

    }



    public class Members {

        @SerializedName("string")
        @Expose
        private String string;

        public String getString() {
            return string;
        }

        public void setString(String string) {
            this.string = string;
        }

    }


    public class NotifyProps {


    }


    public class String {

        @SerializedName("channel_id")
        @Expose
        private java.lang.String channelId;
        @SerializedName("user_id")
        @Expose
        private java.lang.String userId;
        @SerializedName("roles")
        @Expose
        private java.lang.String roles;
        @SerializedName("last_viewed_at")
        @Expose
        private Integer lastViewedAt;
        @SerializedName("msg_count")
        @Expose
        private Integer msgCount;
        @SerializedName("mention_count")
        @Expose
        private Integer mentionCount;
        @SerializedName("notify_props")
        @Expose
        private NotifyProps notifyProps;
        @SerializedName("last_update_at")
        @Expose
        private Integer lastUpdateAt;

        public java.lang.String getChannelId() {
            return channelId;
        }

        public void setChannelId(java.lang.String channelId) {
            this.channelId = channelId;
        }

        public java.lang.String getUserId() {
            return userId;
        }

        public void setUserId(java.lang.String userId) {
            this.userId = userId;
        }

        public java.lang.String getRoles() {
            return roles;
        }

        public void setRoles(java.lang.String roles) {
            this.roles = roles;
        }

        public Integer getLastViewedAt() {
            return lastViewedAt;
        }

        public void setLastViewedAt(Integer lastViewedAt) {
            this.lastViewedAt = lastViewedAt;
        }

        public Integer getMsgCount() {
            return msgCount;
        }

        public void setMsgCount(Integer msgCount) {
            this.msgCount = msgCount;
        }

        public Integer getMentionCount() {
            return mentionCount;
        }

        public void setMentionCount(Integer mentionCount) {
            this.mentionCount = mentionCount;
        }

        public NotifyProps getNotifyProps() {
            return notifyProps;
        }

        public void setNotifyProps(NotifyProps notifyProps) {
            this.notifyProps = notifyProps;
        }

        public Integer getLastUpdateAt() {
            return lastUpdateAt;
        }

        public void setLastUpdateAt(Integer lastUpdateAt) {
            this.lastUpdateAt = lastUpdateAt;
        }

    }
}
