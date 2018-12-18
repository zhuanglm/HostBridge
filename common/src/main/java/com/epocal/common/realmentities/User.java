package com.epocal.common.realmentities;

import com.epocal.common.types.DBActionOnUser;
import com.epocal.common.types.Permissions;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;

/**
 * Created by bmate on 3/17/2017.
 */

public class User extends RealmObject {

    //persisted

    // TODO: All these need default values

    @PrimaryKey
    private long id;
    private Boolean IsLoggedIn;
    private Boolean IsActive;
    private Boolean IsEnabled;
    private Boolean IsSpecial;
    private String UserId;
    private String Password;
    private String UserName;
    private Date StartDate;
    private Date EndDate;
    private Date Created;
    private Date Updated;
    private Date LastLogin;
    private Date LastInvalidLogin;
    private int InvalidLoginAttempts;

    // enum mapping starts here
    private String UserPermission;

    // the enum api field
    @Ignore
    private Permissions Permission;

    public User() {}

    public User(User src) {
        if (src != null) {
            id = src.getId();
            IsLoggedIn = src.getLoggedIn();
            IsActive = src.getActive();
            IsEnabled = src.getEnabled();
            IsSpecial = src.getSpecial();
            UserId = src.getUserId();
            Password = src.getPassword();
            UserName = src.getUserName();
            StartDate = src.getStartDate();
            EndDate = src.getEndDate();
            Created = src.getCreated();
            Updated = src.getUpdated();
            LastLogin = src.getLastLogin();
            LastInvalidLogin = src.getLastInvalidLogin();
            InvalidLoginAttempts = src.getInvalidLoginAttempts();
            UserPermission = src.getUserPermission();
            Permission = src.getPermission();
        }
    }

    public String getUserPermission() {
        return UserPermission;
    }

    public void setUserPermission(String userPermission) {
        UserPermission = userPermission;
    }

    public Permissions getPermission() {
        String permissions = getUserPermission();

        if (permissions == null)
            permissions = "NONE";

        return Permissions.valueOf(permissions);
    }

    public void setPermission(Permissions permission) {
        setUserPermission(permission.toString());
    }
    // enum mapping ends here

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Boolean getLoggedIn() {
        if (IsLoggedIn == null) {
            return false;
        }
        return IsLoggedIn;
    }

    public void setLoggedIn(Boolean loggedIn) {
        IsLoggedIn = loggedIn;
    }

    public Boolean getSpecial() {
        return IsSpecial;
    }

    public void setSpecial(Boolean special) {
        IsSpecial = special;
    }

    public Boolean getActive() {
        return IsActive;
    }

    public void setActive(Boolean active) {
        IsActive = active;
    }

    public Boolean getEnabled() {
        return IsEnabled;
    }

    public void setEnabled(Boolean enabled) {
        IsEnabled = enabled;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public Date getStartDate() {
        return StartDate;
    }

    public void setStartDate(Date startDate) {
        StartDate = startDate;
    }

    public Date getEndDate() {
        return EndDate;
    }

    public void setEndDate(Date endDate) {
        EndDate = endDate;

    }

    public Date getCreated() {
        return Created;
    }

    public void setCreated(Date created) {
        Created = created;
    }

    public Date getUpdated() {
        return Updated;
    }

    public void setUpdated(Date updated) {
        Updated = updated;
    }

    public Date getLastLogin() {
        return LastLogin;
    }

    public void setLastLogin(Date lastLogin) {
        LastLogin = lastLogin;
    }

    public Date getLastInvalidLogin() {
        return LastInvalidLogin;
    }

    public void setLastInvalidLogin(Date lastInvalidLogin) {
        LastInvalidLogin = lastInvalidLogin;
    }

    public int getInvalidLoginAttempts() {

        return InvalidLoginAttempts;
    }

    public void setInvalidLoginAttempts(int invalidLoginAttempts) {
        InvalidLoginAttempts = invalidLoginAttempts;
    }

    public Date Expires() {
        if (IsSpecial == null || IsSpecial == true) return null;
        return EndDate;
    }

    public DBActionOnUser getDbAction() {
        return dbAction;
    }

    public void setDbAction(DBActionOnUser dbAction) {
        this.dbAction = dbAction;
    }

    @Ignore
    private DBActionOnUser dbAction;

}
