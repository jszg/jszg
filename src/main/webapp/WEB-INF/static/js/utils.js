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
//                                                       UiUtils                                                 //
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
function UiUtils() {}

/**
 * 取得选中的 option 的数据
 *
 * @param  {string} selectId select 的 id
 * @return {json}            返回 option 的 id, name, option 自己组成的对象
 */
UiUtils.getSelectedOption = function(selectId) {
    var $selectedOption = $('#'+selectId).find('option:selected');

    return {
        id: parseInt($selectedOption.val()),
        name: $selectedOption.text(),
        option: $selectedOption
    };
};

/**
 * 设置 form-data 的 span 的属性
 * <span class="form-data" name="idNo" data-id="" data-name=""></span>
 *
 * @param {string} formDataName 表示 formData 的名字
 * @param {int} id
 * @param {string} name
 */
UiUtils.setFormData = function(formDataName, id, text) {
    var $dataSpan = $('span.form-data[name="' + formDataName + '"]');
    $dataSpan.attr('data-id', id);
    $dataSpan.attr('data-text', text);
    $dataSpan.text(text);
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
 * 默认保留第一个选项，模版名为 optionTemplate，名字的过滤为空
 *
 * @param  {string} selectId  select 的 id
 * @param  {array} options    options 的数据，为 {id: 12, name: 'Foo', status: true} 等
 * @param  {json}  config     可选参数, 查看 defaults
 */
DictUtils.insertOptions = function(selectId, options, config) {
    // 例如插入资格种类的 options
    // $('#certTypes option:gt(0)').remove();
    // var certTypes = data.certTypes;
    // var $certTypes = $('#certTypes');
    // for (i = 0; i < certTypes.length; ++i) {
    //     $certTypes.append(template('optionTemplate', certTypes[i]));
    // }
    var $select = $('#'+selectId);
    var defaults = {
        filters: [], // name 的 filter
        templateId: 'optionTemplate',
        remainFirstOption: true
    };
    var fc = $.extend({}, defaults, config); // final config

    // 如果 remainFirstOption 为 true 则留下第一个选项 "请选择"，否则删除所有的选项
    $select.find('option').remove();

    if (fc.remainFirstOption) {
        $select.append('<option selected="selected" value="-1">请选择</option>');
    }

    for (var i = 0; i < options.length; ++i) {
        // filters 为空，或者不为空时 name 在 filters 中才显示
        if (fc.filters.length === 0 || fc.filters.includes(options[i].name)) {
            // 如果有 status 属性，并且 status 为 false，则不显示
            if (options[i].hasOwnProperty('status') && !options[i].status) {
                continue;
            }
            $select.append(template(fc.templateId, options[i]));
        }
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
    this.name           = name;
    this.idNo           = idNo.toUpperCase();
    this.birthday       = idNo.substring(6, 15);
    this.birthdayYear   = this.birthday.substring(0, 4);
    this.birthdayMonth  = this.birthday.substring(4, 6);
    this.birthdayDay    = this.birthday.substring(6, 8);
    this.gender         = (parseInt(idNo.substring(16, 17)) % 2 === 0) ? '女' : '男';
    this.birthdayString = this.birthdayYear + '-' + this.birthdayMonth + '-' + this.birthdayDay;
}

/**
 * 验证身份证号码是否有效
 *
 * @param  {[type]} idNo [description]
 * @return {[type]}      [description]
 */
IdCard.validate = function(idNo) {
    var regex = /^[1-9]\d{5}[1-9]\d{3}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])\d{3}([0-9]|X|x)$/;
    return regex.test(idNo);
};
