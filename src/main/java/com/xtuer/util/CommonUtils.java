package com.xtuer.util;

public class CommonUtils {

    /**
     * 验证密码的强度
     *     密码不少于 8 位，必须包含数字、字母和特殊字符，特殊字符需从 “#、%、*、-、_、!、@、$、&” 中选择
     * @param password 密码
     * @return 如果密码的强度够返回 true，否则返回 false
     */
    public static boolean passwordHasEnoughStrength(String password) {
        return  password.length() >= 8 &&
                password.matches("^.*[0-9]+.*$") &&
                password.matches("^.*[a-zA-Z]+.*$") &&
                password.matches("^.*[#%_!@&\\-\\*\\$]+.*$");
    }

}
