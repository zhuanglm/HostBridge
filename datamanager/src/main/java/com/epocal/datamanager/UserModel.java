package com.epocal.datamanager;

import com.epocal.common.realmentities.TestRecord;
import com.epocal.common.realmentities.User;
import com.epocal.common.types.AccountStatus;
import com.epocal.datamanager.realmrepository.PrimaryKeyFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by bmate on 3/24/2017.
 * Processing CRUD operations on User class
 * applying business rules
 */

public class UserModel {

    Realm mRealm = null;

    public void openRealmInstance() {
        if (mRealm == null)
            mRealm = Realm.getDefaultInstance();
    }

    public void closeRealmInstance() {
        if (mRealm != null) {
            mRealm.close();
            mRealm = null;
        }
    }

    public boolean isUserExpired(String userID) {
        boolean retval = false;
        Realm realm = (mRealm == null) ? Realm.getDefaultInstance() : mRealm;
        User u = realm.where(User.class)
                .equalTo("UserId", userID)
                .equalTo("IsActive", true).findFirst();
        retval = isUserExpired(u);

        if (mRealm == null)
            realm.close();

        return retval;
    }

    private boolean isUserExpired(User u) {
        if (u != null) {
            if (u.getSpecial()) {
                return false;
            }
            if (u.getStartDate() != null && (new Date()).before(u.getStartDate())) {
                return true;
            }
            if (u.getEndDate() != null && (new Date()).after(u.getEndDate())) {

                return true;
            }
        }
        return false;
    }

    public AccountStatus getUserAccountStatus(String userID) {
        Realm realm = (mRealm == null) ? Realm.getDefaultInstance() : mRealm;
        User u = realm.where(User.class).equalTo("UserId", userID).findFirst();
        AccountStatus retval = getUserAccountStatus(u);

        if (mRealm == null)
            realm.close();

        return retval;
    }

    private AccountStatus getUserAccountStatus(User u) {
        AccountStatus retval = AccountStatus.Unknown;
        if (u != null) {
            // anonymous account
            if (u.getId() == -1) {
                return AccountStatus.Invalid;
            }
            if (!u.getEnabled()) {
                return AccountStatus.Disabled;
            }
            if (isUserExpired(u)) {
                return AccountStatus.Expired;
            }

            int invalidLoginAttempts = u.getInvalidLoginAttempts();
            Date lastLogin = u.getLastInvalidLogin();
            boolean earlyLoginAttempt = earlyLoginAttempt(lastLogin);

            if (invalidLoginAttempts >= 3 && lastLogin != null && earlyLoginAttempt) {
                return AccountStatus.LockedOut;
            }
            return AccountStatus.Enabled;
        }
        return retval;
    }

    public void changeUserAccountStatus(AccountStatus newStatus, String userID) {
        Realm realm = (mRealm == null) ? Realm.getDefaultInstance() : mRealm;
        final User u = realm.where(User.class)
                .equalTo("UserId", userID)
                .equalTo("IsActive", true).findFirst();
        if (u != null) {
            final AccountStatus existingStatus = getUserAccountStatus(u);
            if (existingStatus != newStatus) {
                if (newStatus == AccountStatus.Disabled) {
                    realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            u.setEnabled(false);

                        }
                    });
                } else if (newStatus == AccountStatus.Enabled) {
                    realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            if (existingStatus == AccountStatus.LockedOut) {
                                u.setInvalidLoginAttempts(0);
                                u.setLastInvalidLogin(null);
                            }
                            u.setEnabled(true);

                        }
                    });
                }
            }
        }

        if (mRealm == null)
            realm.close();
    }

    private boolean earlyLoginAttempt(Date attempt) {
        Date now = new Date();

        if (attempt == null) {
            attempt = now;
        }

        Long diff = now.getTime() - attempt.getTime();
        Long diffinMinutes = TimeUnit.MILLISECONDS.toMinutes(diff);
        return diffinMinutes < 5;
    }

    public ArrayList<User> getAllUsers() {
        ArrayList<User> allUsers = new ArrayList<>();

        Realm realm = (mRealm == null) ? Realm.getDefaultInstance() : mRealm;

        try {
            RealmResults<User> allUsersInRealm = realm.where(User.class).findAll();
            allUsers.addAll(allUsersInRealm);
        } catch (Exception ex) {
            // todo handle exception
        }

        if (mRealm == null)
            realm.close();

        return allUsers;
    }

    public void updateUserLastLoginTime(final String userID, final Date lastLogin) {
        Realm realm = (mRealm == null) ? Realm.getDefaultInstance() : mRealm;
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                User user = realm.where(User.class).equalTo("UserId", userID).equalTo("IsActive", true).findFirst();
                user.setLastLogin(lastLogin);
                user.setInvalidLoginAttempts(0);
            }
        });

        if (mRealm == null)
            realm.close();
    }

    /**
     * Get the logged in user (unmanaged)
     *
     * @return the user object
     */
    public User getLoggedInUser() {
        Realm realm = (mRealm == null) ? Realm.getDefaultInstance() : mRealm;
        User retval = null;
        User managedUser = realm.where(User.class).equalTo("IsLoggedIn", true).findFirst();
        if (managedUser != null) {
            retval = realm.copyFromRealm(managedUser);
        } else {
            retval = realm.copyFromRealm(realm.where(User.class).equalTo("UserId", "anonymous").findFirst());
        }

        if (mRealm == null)
            realm.close();

        return retval;
    }

    /**
     * Get all users who are marked as Loggedin  (i.e: IsLoggedIn is set to true)
     *
     * @return list of unmanaged user object who are marked as Loggedin.
     */
    public ArrayList<User> getAllLoggedInUser() {
        ArrayList<User> allUsers = new ArrayList<>();
        Realm realm = (mRealm == null) ? Realm.getDefaultInstance() : mRealm;

        try {
            RealmResults<User> allUsersInRealm = realm.where(User.class).equalTo("IsLoggedIn", true).findAll();
            List<User> retval = realm.copyFromRealm(new ArrayList<>(allUsersInRealm)); // convert user to unmanaged copy
            allUsers.addAll(retval);
        } catch (Exception ex) {
            // todo handle exception
        }

        if (mRealm == null)
            realm.close();

        return allUsers;
    }

    public User getAnonymousUser() {
        Realm realm = (mRealm == null) ? Realm.getDefaultInstance() : mRealm;
        User anonymousUser = realm.copyFromRealm(realm.where(User.class).equalTo("UserId", "anonymous").findFirst());

        if (mRealm == null)
            realm.close();

        return anonymousUser;
    }

    public User getUser(String userID) {
        Realm realm = (mRealm == null) ? Realm.getDefaultInstance() : mRealm;
        User user = realm.copyFromRealm(realm.where(User.class).equalTo("UserId", userID).findFirst());

        if (mRealm == null)
            realm.close();

        return user;
    }

    // returns a user if authentication passes or the user is locked out, or the anonymous user if authentication failed
    public User authenticateUser(String userID, String password) {
        // using fluent interface to construct multi-clause queries
        Realm realm = (mRealm == null) ? Realm.getDefaultInstance() : mRealm;

        final User user = realm.where(User.class)
                .equalTo("IsActive", true)
                .equalTo("UserId", userID)
                .findFirst();

        // conditions: userid or (userid and password)required
        // authentication success
        if (user != null && ((password == null || user.getPassword().equals(password)) || (getUserAccountStatus(user) == AccountStatus.LockedOut))) {

            if (mRealm == null)
                realm.close();

            return user;
        }

        // password authentication failed.
        if (user != null) {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    if (user.getLastInvalidLogin() != null) {
                        // if more than 5 min from last invalid login, then set invalid login attempts to 0
                        if (!earlyLoginAttempt(user.getLastInvalidLogin())) {
                            user.setInvalidLoginAttempts(0);
                        }
                    }
                    user.setLastInvalidLogin(new Date());
                    user.setInvalidLoginAttempts(user.getInvalidLoginAttempts() + 1);
                }
            });
        }

        if (mRealm == null)
            realm.close();

        // if here, return anonymous user
        return realm.where(User.class).equalTo("UserId", "anonymous").findFirst();
    }

    // returns active and inactive users
    public User findUser(String userid) {
        Realm realm = (mRealm == null) ? Realm.getDefaultInstance() : mRealm;

        User user = realm.where(User.class)
                .equalTo("UserId", userid)
                .findFirst();

        if (mRealm == null)
            realm.close();

        return user;
    }

    public User findActiveUser(String userid) {
        Realm realm = (mRealm == null) ? Realm.getDefaultInstance() : mRealm;

        User user =  realm.where(User.class)
                .equalTo("IsActive", true)
                .equalTo("UserId", userid)
                .findFirst();

        if (mRealm == null)
            realm.close();

        return user;
    }

    public User findUserById(int userIdNum) {
        Realm realm = (mRealm == null) ? Realm.getDefaultInstance() : mRealm;

        User user =  realm.where(User.class)
                .equalTo("id", userIdNum)
                .findFirst();

        if (mRealm == null)
            realm.close();

        return user;
    }

    public void saveUser(final User unmanagedUser) {
        if (unmanagedUser.getId() == 0) {
            createNewUser(unmanagedUser);
        } else if (unmanagedUser.getId() > 2) {
            updateUser(unmanagedUser);
        }
    }


    public void setLoggedInByUserID(final String userID, final boolean loggedIn) {
        Realm realm = (mRealm == null) ? Realm.getDefaultInstance() : mRealm;
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                User user = realm.where(User.class).equalTo("UserId", userID).equalTo("IsActive", true).findFirst();
                user.setLoggedIn(loggedIn);
                user.setInvalidLoginAttempts(0);
            }
        });

        if (mRealm == null)
            realm.close();
    }

    public void setLastLoginByUserID(final String userID, final Date lastLogin) {
        Realm realm = (mRealm == null) ? Realm.getDefaultInstance() : mRealm;
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                User user = realm.where(User.class).equalTo("UserId", userID).equalTo("IsActive", true).findFirst();
                user.setLastLogin(lastLogin);
                user.setInvalidLoginAttempts(0);
            }
        });

        if (mRealm == null)
            realm.close();
    }


    /**
     * Logout the user
     *
     * @param unmanagedUser the unmanaged user
     */
    public void logoutUser(User unmanagedUser) {
        unmanagedUser.setLoggedIn(false);
        updateUser(unmanagedUser);
    }

    private void createNewUser(final User unmanagedUser) {
        // here we got an unmanaged User with all fields validated
        Realm realm = (mRealm == null) ? Realm.getDefaultInstance() : mRealm;
        //create by administrator
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                User u2 = realm.createObject(User.class, PrimaryKeyFactory.getInstance().nextKey(User.class));
                u2.setLoggedIn(false);
                u2.setUserId(unmanagedUser.getUserId());

                String password = unmanagedUser.getPassword();
                if (password == null) {
                    password = "";
                }
                u2.setPassword(password);

                u2.setUserName(unmanagedUser.getUserName());
                u2.setStartDate(unmanagedUser.getStartDate());
                u2.setEndDate(unmanagedUser.getEndDate());
                u2.setCreated(new Date());
                u2.setActive(unmanagedUser.getActive());
                u2.setSpecial(false);
                u2.setPermission(unmanagedUser.getPermission());
                u2.setEnabled(unmanagedUser.getEnabled());
            }
        });

        if (mRealm == null)
            realm.close();
    }


    // deletes a user if no tests associated
    public boolean deleteUser(final long userDbId) {
        // exit condition
        if (userDbId < 1) return false;

        boolean deletedUser = false;
        Realm realm = (mRealm == null) ? Realm.getDefaultInstance() : mRealm;
        // user still associated with existing test
        if (realm.where(TestRecord.class).equalTo("user.id", userDbId).findFirst() == null) {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    RealmResults<User> results = realm.where(User.class).equalTo("id", userDbId).findAll();
                    results.deleteAllFromRealm();
                }
            });

            deletedUser = true;
        }

        if (mRealm == null)
            realm.close();

        return deletedUser;
    }

    public void forceDeleteUser(final String userID) {
        // exit condition
        if (userID.isEmpty()) return;
        Realm realm = (mRealm == null) ? Realm.getDefaultInstance() : mRealm;
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<User> results = realm.where(User.class).equalTo("UserId", userID).findAll();
                results.deleteAllFromRealm();
            }
        });

        if (mRealm == null)
            realm.close();
    }

    // TODO: implement new version mechanism
    private void updateUser(final User unmanagedUser) {
        // here we got an unmanaged User with all fields validated
        Realm realm = (mRealm == null) ? Realm.getDefaultInstance() : mRealm;
        //create administrator
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                User u2 = realm.where(User.class)
                        .equalTo("id", unmanagedUser.getId())
                        .equalTo("IsActive", true).findFirst();
                if (u2 != null) {
                    u2.setLoggedIn(unmanagedUser.getLoggedIn());
                    u2.setUserId(unmanagedUser.getUserId());

                    String updatedPassword = unmanagedUser.getPassword();
                    if (updatedPassword != null && !updatedPassword.isEmpty()) {
                        u2.setPassword(updatedPassword);
                    }

                    u2.setUserName(unmanagedUser.getUserName());
                    u2.setStartDate(unmanagedUser.getStartDate());
                    u2.setEndDate(unmanagedUser.getEndDate());
//                    u2.setCreated(unmanagedUser.getCreated());        // Do not update a user's creation date
                    u2.setActive(unmanagedUser.getActive());
                    u2.setSpecial(unmanagedUser.getSpecial());
//                    u2.setUpdated(new Date());                        // TODO: Should this be updated every change?
                    u2.setPermission(unmanagedUser.getPermission());
                    u2.setEnabled(unmanagedUser.getEnabled());
                    u2.setLastInvalidLogin(unmanagedUser.getLastInvalidLogin());
                    u2.setInvalidLoginAttempts(unmanagedUser.getInvalidLoginAttempts());
                }
            }
        });

        if (mRealm == null)
            realm.close();
    }

    public void setLoggedInUserName(final String userName) {
        Realm realm = (mRealm == null) ? Realm.getDefaultInstance() : mRealm;

        User user = new UserModel().getLoggedInUser();
        if (user == null)
            user = new UserModel().getAnonymousUser();

        final User finalUser = user;

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                User u2 = realm.where(User.class)
                        .equalTo("id", finalUser.getId())
                        .equalTo("IsActive", true).findFirst();
                if (u2 != null)
                    u2.setUserName(userName);
            }
        });

        if (mRealm == null)
            realm.close();
    }

    public void setLoggedInUserPassword(final String userPassword) {
        Realm realm = (mRealm == null) ? Realm.getDefaultInstance() : mRealm;

        User user = new UserModel().getLoggedInUser();
        if (user == null)
            user = new UserModel().getAnonymousUser();

        final User finalUser = user;

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                User u2 = realm.where(User.class)
                        .equalTo("id", finalUser.getId())
                        .equalTo("IsActive", true).findFirst();
                if (u2 != null)
                    u2.setPassword(userPassword);
            }
        });

        if (mRealm == null)
            realm.close();
    }
}
