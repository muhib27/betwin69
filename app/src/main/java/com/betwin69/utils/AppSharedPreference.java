package com.betwin69.utils;

import android.content.SharedPreferences;

import com.betwin69.MyApplication;


/**
 * Created by RR on 13-Nov-18.
 */

public class AppSharedPreference {
    public static final String keyModelTestPrefs = "modelTestPrefs";

    private static final String keyIsFirstTime = "isFirstTime";
    private static final String keyUserName = "username";
    private static final String keyUserPassword = "userpassword";
    private static final String keyApiKey = "api_key";
    private static final String keyRememberMe = "rememberMe";
    private static final String keyUDID = "udid";
    private static final String keyFCMId = "fcm_id";

    private static SharedPreferences getSharedPreferences() {
        return MyApplication.getContext().getSharedPreferences(keyModelTestPrefs, 0);
    }

    public static boolean getUsingFirstTime() {
        final SharedPreferences pref = getSharedPreferences();
        return pref.getBoolean(keyIsFirstTime, true);
    }

    public static void setUsingFirstTime(boolean isFirstTime) {
        final SharedPreferences pref = getSharedPreferences();
        final SharedPreferences.Editor editor = pref.edit();

        editor.putBoolean(keyIsFirstTime, isFirstTime);
        editor.apply();
    }

    public static void savePrefBoolean(String key, boolean value) {
        final SharedPreferences pref = getSharedPreferences();
        final SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public static boolean getPrefBoolean(String key) {
        final SharedPreferences pref = getSharedPreferences();
        return pref.getBoolean(key, false);
    }

    public static void setUserNameAndPassword(String userId, String password, String api_key, boolean rememberMe) {
        final SharedPreferences pref = getSharedPreferences();
        final SharedPreferences.Editor editor = pref.edit();

        editor.putString(keyUserName, userId);
        editor.putString(keyUserPassword, password);
        editor.putString(keyApiKey, api_key);
        editor.putBoolean(keyRememberMe, rememberMe);
        editor.apply();
    }

//    public static void setUserBasicInfo(User user) {
//        final SharedPreferences pref = getSharedPreferences();
//        final SharedPreferences.Editor editor = pref.edit();
//
//        if (user.getId() != null)
//            editor.putString(AppConstant.USER_NAME, user.getName());
//        editor.putString(AppConstant.USER_EMAIL, user.getEmail());
//        editor.putString(AppConstant.USER_ID, user.getId());
//        editor.putString(AppConstant.USER_IMAGE, user.getImage());
//        editor.apply();
//    }
//
//    public static User getUserBasicInfo() {
//        final SharedPreferences pref = getSharedPreferences();
//        User user = new User();
//
//        user.setName(pref.getString(AppConstant.USER_NAME, ""));
//        user.setEmail(pref.getString(AppConstant.USER_EMAIL, ""));
//        user.setId(pref.getString(AppConstant.USER_ID, ""));
//        user.setImage(pref.getString(AppConstant.USER_IMAGE, ""));
//        return user;
//    }

    public static void setUDID(String udid) {
        final SharedPreferences pref = getSharedPreferences();
        final SharedPreferences.Editor editor = pref.edit();

        editor.putString(keyUDID, udid);
        editor.apply();
    }

    public static String getUDID() {
        final SharedPreferences pref = getSharedPreferences();
        return pref.getString(keyUDID, "");
    }

    public static void setFcm(String fcm_id) {
        final SharedPreferences pref = getSharedPreferences();
        final SharedPreferences.Editor editor = pref.edit();

        editor.putString(keyFCMId, fcm_id);
        editor.apply();
    }

    public static String getFcm() {
        final SharedPreferences pref = getSharedPreferences();
        return pref.getString(keyFCMId, "");
    }

    public static String getUserName() {
        final SharedPreferences pref = getSharedPreferences();
        return pref.getString(keyUserName, "");
    }
    public static boolean getRememberMe() {
        final SharedPreferences pref = getSharedPreferences();
        return pref.getBoolean(keyRememberMe, false);
    }

    public static String getUserPassword() {
        final SharedPreferences pref = getSharedPreferences();
        return pref.getString(keyUserPassword, "");
    }

    public static String getApiKey() {
        final SharedPreferences pref = getSharedPreferences();
        return pref.getString(keyApiKey, "");
    }
    //eSWBaw0MaMM:APA91bFhVluppQU8GIpUuMUEF2gCXuWE4ZXiV6Nv9Wsm9ywYe7m4fDx6aK6DakJgCqvu4Iv7_L91AfNxrfXQICVL-pjSTI1b_00MsA5RNqZ_MOy7QQLJqJLslyEQavUSKn13Rc3tWYxy

//    public static void setUserPermission(UserPermission userPermission) {
//        final SharedPreferences pref = getSharedPreferences();
//        final SharedPreferences.Editor editor = pref.edit();
//
//
//        editor.putBoolean(AppConstant.USER_TASKS_LIST, userPermission.isTasksList());
//        editor.putBoolean(AppConstant.USER_TASKS_ADD, userPermission.isTasksAdd());
//        editor.putBoolean(AppConstant.USER_TASKS_EDIT, userPermission.isTasksEdit());
//        editor.putBoolean(AppConstant.USER_TASKS_DELETE, userPermission.isTasksDelete());
//        editor.putBoolean(AppConstant.USER_TASKS_VIEW, userPermission.isTasksView());
//        editor.putBoolean(AppConstant.USER_TASKS_SUBMIT_TASK, userPermission.isUserTasksSubmitTask());
//        editor.putBoolean(AppConstant.USER_TASKS_SUBMITTED_LIST, userPermission.isTasksSubmittedList());
//        editor.putBoolean(AppConstant.USER_TASKS_SINGLE_VIEW, userPermission.isTasksSingleView());
//        editor.putBoolean(AppConstant.USER_ADD, userPermission.isUserAdd());
//        editor.putBoolean(AppConstant.USER_EDIT, userPermission.isUserEdit());
//        editor.putBoolean(AppConstant.USER_DELETE, userPermission.isUserDelete());
//        editor.apply();
//    }
//    public static UserPermission getUserPermission() {
//        final SharedPreferences pref = getSharedPreferences();
//        UserPermission userPermission = new UserPermission();
//
//        userPermission.setTasksList(pref.getBoolean(AppConstant.USER_TASKS_LIST, false));
//        userPermission.setTasksAdd(pref.getBoolean(AppConstant.USER_TASKS_ADD, false));
//        userPermission.setTasksEdit(pref.getBoolean(AppConstant.USER_TASKS_EDIT, false));
//        userPermission.setTasksDelete(pref.getBoolean(AppConstant.USER_TASKS_DELETE, false));
//        userPermission.setUserTasksSubmitTask(pref.getBoolean(AppConstant.USER_TASKS_SUBMIT_TASK, false));
//        userPermission.setTasksView(pref.getBoolean(AppConstant.USER_TASKS_VIEW, false));
//        userPermission.setTasksSubmittedList(pref.getBoolean(AppConstant.USER_TASKS_SUBMITTED_LIST, false));
//        userPermission.setTasksSingleView(pref.getBoolean(AppConstant.USER_TASKS_SINGLE_VIEW, false));
//        userPermission.setUserAdd(pref.getBoolean(AppConstant.USER_ADD, false));
//        userPermission.setUserEdit(pref.getBoolean(AppConstant.USER_EDIT, false));
//        userPermission.setUserDelete(pref.getBoolean(AppConstant.USER_DELETE, false));
//        return userPermission;
//    }
//
//    public static void setUserMenu(UserMenu userMenu) {
//        final SharedPreferences pref = getSharedPreferences();
//        final SharedPreferences.Editor editor = pref.edit();
//
//        editor.putBoolean(AppConstant.MENU_TASKS, userMenu.isTasks());
//        editor.putBoolean(AppConstant.MENU_EVENTS, userMenu.isEvents());
//        editor.putBoolean(AppConstant.MENU_ROUTINE, userMenu.isRoutine());
//        editor.putBoolean(AppConstant.MENU_RESEARCH, userMenu.isResearch());
//        editor.putBoolean(AppConstant.MENU_RESEARCH_WING, userMenu.isResearch_wing());
//        editor.putBoolean(AppConstant.MENU_NOTICE, userMenu.isNotice());
//        editor.putBoolean(AppConstant.MENU_READING_LIST, userMenu.isReading_list());
//        editor.apply();
//    }
//    public static UserMenu getUserMenu() {
//        final SharedPreferences pref = getSharedPreferences();
//        UserMenu userMenu = new UserMenu();
//
//        userMenu.setTasks(pref.getBoolean(AppConstant.MENU_TASKS, false));
//        userMenu.setEvents(pref.getBoolean(AppConstant.MENU_EVENTS, false));
//        userMenu.setRoutine(pref.getBoolean(AppConstant.MENU_ROUTINE, false));
//        userMenu.setResearch(pref.getBoolean(AppConstant.MENU_RESEARCH, false));
//        userMenu.setResearch_wing(pref.getBoolean(AppConstant.MENU_RESEARCH_WING, false));
//        userMenu.setNotice(pref.getBoolean(AppConstant.MENU_NOTICE, false));
//        userMenu.setReading_list(pref.getBoolean(AppConstant.MENU_READING_LIST, false));
//
//        return userMenu;
//    }
//
//
//
//    public static void setUserCourse(UserCourses userCourse) {
//        final SharedPreferences pref = getSharedPreferences();
//        final SharedPreferences.Editor editor = pref.edit();
//
//        editor.putBoolean(AppConstant.NDC, userCourse.isNdc());
//        editor.putBoolean(AppConstant.AFWC, userCourse.isAfwc());
//        editor.putBoolean(AppConstant.CAPSTON, userCourse.isCapston());
//
//        editor.apply();
//    }
//    public static UserCourses getUserCourse() {
//        final SharedPreferences pref = getSharedPreferences();
//        UserCourses userCourses = new UserCourses();
//
//        userCourses.setNdc(pref.getBoolean(AppConstant.NDC, false));
//        userCourses.setAfwc(pref.getBoolean(AppConstant.AFWC, false));
//        userCourses.setCapston(pref.getBoolean(AppConstant.CAPSTON, false));
//        return userCourses;
//    }

}
