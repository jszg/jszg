/**
 * 使用到的 URL，放在 utils.js 里为了减少 js 文件的请求
 */
Urls = {
    REST_CERT_TYPE: '/rest/signUp/certTypes',
    REST_PROVINCES: '/rest/signUp/provinces',
    REST_CITIES_BY_PROVINCE: '/rest/signUp/provinces/{provinceId}/cities',
    REST_SUBJECTS_ROOT: '/rest/signUp/provinces/{provinceId}/certTypes/{certTypeId}/subjects/root',
    REST_ORGS_BY_CITY_AND_CERT_TYPE: '/rest/signUp/cities/{cityId}/certTypes/{certTypeId}/orgs',
    REST_SUBJECTS_CHILDREN: '/rest/signUp/provinces/{provinceId}/{parentId}/subjects/children',
    WEB_UPLOADER_SWF: 'https://cdn.staticfile.org/webuploader/0.1.5/Uploader.swf'
};

/**
 * 工具类
 */
function Utils() {

}

/**
 * 扩展了 String 类型，给其添加格式化的功能，替换字符串中 {placeholder} 或者 {0}, {1} 等模式部分为参数中传入的字符串
 * 使用方法:
 *     'I can speak {language} since I was {age}'.format({language: 'Javascript', age: 10})
 *     'I can speak {0} since I was {1}'.format('Javascript', 10)
 * 输出都为:
 *     I can speak Javascript since I was 10
 *
 * @param replacements 用来替换 placeholder 的 JSON 对象或者数组
 * @return 格式化后的字符串
 */
String.prototype.format = function(replacements) {
    replacements = (typeof replacements === 'object') ? replacements : Array.prototype.slice.call(arguments, 0);
    return formatString(this, replacements);
};

/**
 * 替换字符串中 {placeholder} 或者 {0}, {1} 等模式部分为参数中传入的字符串
 * 使用方法:
 *     formatString('I can speak {language} since I was {age}', {language: 'Javascript', age: 10})
 *     formatString('I can speak {0} since I was {1}', 'Javascript', 10)
 * 输出都为:
 *     I can speak Javascript since I was 10
 *
 * @param str 带有 placeholder 的字符串
 * @param replacements 用来替换 placeholder 的 JSON 对象或者数组
 * @return 格式化后的字符串
 */
var formatString = function (str, replacements) {
    replacements = (typeof replacements === 'object') ? replacements : Array.prototype.slice.call(arguments, 1);

    return str.replace(/\{\{|\}\}|\{(\w+)\}/g, function(m, n) {
        if (m == '{{') { return '{'; }
        if (m == '}}') { return '}'; }
        return replacements[n];
    });
};
