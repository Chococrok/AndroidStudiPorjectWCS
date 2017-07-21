package matterless.fr.wcs.matterless;

/**
 * Created by apprenti on 20/03/17.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserProfile {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("create_at")
    @Expose
    private transient Integer createAt;
    @SerializedName("update_at")
    @Expose
    private transient Integer updateAt;
    @SerializedName("delete_at")
    @Expose
    private Integer deleteAt;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("first_name")
    @Expose
    private String firstName;
    @SerializedName("last_name")
    @Expose
    private String lastName;
    @SerializedName("nickname")
    @Expose
    private String nickname;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("email_verified")
    @Expose
    private Boolean emailVerified;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("auth_data")
    @Expose
    private String authData;
    @SerializedName("auth_service")
    @Expose
    private String authService;
    @SerializedName("roles")
    @Expose
    private String roles;
    @SerializedName("locale")
    @Expose
    private String locale;
    @SerializedName("notify_props")
    @Expose
    private NotifyProps notifyProps;
    @SerializedName("props")
    @Expose
    private Props props;
    @SerializedName("last_password_update")
    @Expose
    private transient Integer lastPasswordUpdate;
    @SerializedName("last_picture_update")
    @Expose
    private String lastPictureUpdate;
    @SerializedName("failed_attempts")
    @Expose
    private Integer failedAttempts;
    @SerializedName("mfa_active")
    @Expose
    private Boolean mfaActive;
    @SerializedName("mfa_secret")
    @Expose
    private String mfaSecret;

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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getEmailVerified() {
        return emailVerified;
    }

    public void setEmailVerified(Boolean emailVerified) {
        this.emailVerified = emailVerified;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAuthData() {
        return authData;
    }

    public void setAuthData(String authData) {
        this.authData = authData;
    }

    public String getAuthService() {
        return authService;
    }

    public void setAuthService(String authService) {
        this.authService = authService;
    }

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public NotifyProps getNotifyProps() {
        return notifyProps;
    }

    public void setNotifyProps(NotifyProps notifyProps) {
        this.notifyProps = notifyProps;
    }

    public Props getProps() {
        return props;
    }

    public void setProps(Props props) {
        this.props = props;
    }

    public Integer getLastPasswordUpdate() {
        return lastPasswordUpdate;
    }

    public void setLastPasswordUpdate(Integer lastPasswordUpdate) {
        this.lastPasswordUpdate = lastPasswordUpdate;
    }

    public String getLastPictureUpdate() {
        return lastPictureUpdate;
    }

    public void setLastPictureUpdate(String lastPictureUpdate) {
        this.lastPictureUpdate = lastPictureUpdate;
    }

    public Integer getFailedAttempts() {
        return failedAttempts;
    }

    public void setFailedAttempts(Integer failedAttempts) {
        this.failedAttempts = failedAttempts;
    }

    public Boolean getMfaActive() {
        return mfaActive;
    }

    public void setMfaActive(Boolean mfaActive) {
        this.mfaActive = mfaActive;
    }

    public String getMfaSecret() {
        return mfaSecret;
    }

    public void setMfaSecret(String mfaSecret) {
        this.mfaSecret = mfaSecret;
    }

    public class NotifyProps {


    }


    public class Props {


    }

}

