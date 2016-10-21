///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//                                                   URL                                                         //
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/**
 * 使用到的 URL 都定义到 Urls 里，方便统一管理
 */
Urls = {
    REST_CERT_TYPE: '/rest/signUp/certTypes',
    REST_PROVINCES: '/rest/signUp/provinces',
    REST_CITIES_BY_PROVINCE: '/rest/signUp/provinces/{provinceId}/cities',
    REST_SUBJECTS_ROOT: '/rest/signUp/provinces/{provinceId}/certTypes/{certTypeId}/subjects/root',
    REST_ORGS_BY_CITY_AND_CERT_TYPE: '/rest/signUp/cities/{cityId}/certTypes/{certTypeId}/orgs',
    REST_SUBJECTS_CHILDREN: '/rest/signUp/provinces/{provinceId}/{parentId}/subjects/children',
    WEB_UPLOADER_SWF: 'https://cdn.staticfile.org/webuploader/0.1.5/Uploader.swf',
    REST_DICTS: '/rest/signUp/dicts',
    URI_UPLOAD_PERSON_IMAGE: '/upload-person-image',
    REST_COLLEGES_BY_PROVINCE: '/rest/signUp/provinces/{provinceId}/colleges'
};

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//                                     格式化字符串，给字符串加上 format 函数                                         //
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
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

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//                                              报名注册的导航工具类                                                //
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/**
 * 报名注册的导航工具类
 */
function StepUtils() {
}

/**
 * 跳到第 step 步
 *
 * @param  {int} step 报名的步骤
 */
StepUtils.toStep = function(step) {
    if (!window.toStepInitialized) {
        window.toStepInitialized = true;

        $('.declare li').click(function() {
            var step = $(this).attr('data-step');
            StepUtils.toStep(step);
        });
    }

    var i = 0;

    // 删除所有 bz 的 class active，然后 bz-[1-step] 添加 class active
    $('.bz').removeClass('active');
    for (i = 1; i <= step; ++i) {
        $('.bz'+i).addClass('active');
    }

    // 隐藏所有 box，然后显示 box-step
    for (i = 1; i <= 8; ++i) {
        $('#box-'+i).hide();
    }
    $('#box-'+step).show();
};

/**
 * 跳到当前步骤的上一步
 *
 * @param  {int} step 当前步数
 */
StepUtils.toPreviousStep = function(step) {
    // 例如第五步的上一步
    // $('#box-5-previous').click(function(){
    //     $('#box-5').hide();
    //     $('#box-4').show();
    //     $('.bz5').removeClass('active');
    // });

    $('#box-'+step).hide();
    $('#box-'+(step-1)).show();
    $('.bz'+step).removeClass('active');
};


///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//                                                      字典工具类                                                //
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

function DictUtils() {
}

/**
 * 向 select 中插入 options
 *
 * @param  {string} selectId  select 的 id
 * @param  {array} options    options 的数据，为 {id: 12, name: 'Foo', status: true}
 */
DictUtils.insertOptions = function(selectId, options) {
    // 例如插入资格种类的 options
    // $('#certTypes option:gt(0)').remove();
    // var certTypes = data.certTypes;
    // var $certTypes = $('#certTypes');
    // for (i = 0; i < certTypes.length; ++i) {
    //     $certTypes.append(template('optionTemplate', certTypes[i]));
    // }

    var $select = $('#'+selectId);
    $select.find('option:gt(0)').remove(); // 留下第一个选项 "请选择"

    for (var i = 0; i < options.length; ++i) {
        $select.append(template('optionTemplate', options[i]));
    }
};

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//                                                     身份证解析类                                                //
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/**
 * 身份证解析类，用于解析身份证号码上不同数字代表的不同含义
 * 例子: var id = new IdCard('女性的', '110102198611267047');
 *
 * 身份证号码位数的含意
 *      前 1、2 位数字表示：所在省份的代码
 *      第 3、4 位数字表示：所在城市的代码
 *      第 5、6 位数字表示：所在区县的代码
 *      第 7~14 位数字表示：出生年、月、日
 *      第 15、16 位数字表示：所在地的派出所的代码
 *      第 17 位数字表示性别：奇数表示男性，偶数表示女性
 *      第 18 位数字是校检码：也有的说是个人信息码，用来检验身份证的正确性。校检码可以是 0~9 的数字，
 *          有时也用 x 表示(尾号是10，那么就得用 x 来代替)，一般是随计算机的随机产生
 *
 * @param {string} name 名字
 * @param {string} idNo 身份证号码
 */
function IdCard(name, idNo) {
    this.name          = name;
    this.birthday      = idNo.substring(6, 15);
    this.birthdayYear  = this.birthday.substring(0, 4);
    this.birthdayMonth = this.birthday.substring(4, 6);
    this.birthdayDay   = this.birthday.substring(6, 8);
    this.gender        = (parseInt(idNo.substring(16, 17)) % 2 === 0) ? '女' : '男';
}
