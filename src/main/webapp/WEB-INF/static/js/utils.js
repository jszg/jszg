///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//                                                   URL                                                         //
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/**
 * 使用到的 URL 都定义到 Urls 里，方便统一管理
 */
Urls = {
    REST_DICTS:     '/new-cert/rest/signUp/dicts',
    REST_CERT_TYPE: '/new-cert/rest/signUp/certTypes',
    REST_PROVINCES: '/new-cert/rest/signUp/provinces',
    REST_CITIES_BY_PROVINCE:         '/new-cert/rest/signUp/provinces/{provinceId}/cities',
    REST_ORGS_BY_CITY_AND_CERT_TYPE: '/new-cert/rest/signUp/cities/{cityId}/certTypes/{certTypeId}/orgs',
    REST_COLLEGES_BY_PROVINCE:       '/new-cert/rest/signUp/provinces/{provinceId}/colleges',
    REST_ORGS_REG:        '/new-cert/rest/signUp/reg/orgs?teachGrade={teachGradeId}&cityId={cityId}&provinceCity={provinceCity}', // 注册机构
    REST_LOCALSETS:       '/new-cert/rest/signUp/orgs/{orgId}/localSets',
    REST_LOCALSET_INFO:   '/new-cert/rest/signUp/localSets/{localSetId}',
    REST_ORGS_BY_ORGTYPE: '/new-cert/rest/signUp/orgtypes/{orgType}/orgs', // 注册的认定机构，ortType 为 4
    REST_ORGS_BY_PARENT:  '/new-cert/rest/signUp/{parentId}/orgs',

    REST_SUBJECTS_ROOT:          '/new-cert/rest/signUp/provinces/{provinceId}/certTypes/{certTypeId}/subjects/root',
    REST_SUBJECTS_CHILDREN:      '/new-cert/rest/signUp/provinces/{provinceId}/{parentId}/subjects/children',
    REST_SUBJECTS_BY_CERT_TYPE:  '/new-cert/rest/signUp/certTypes/{certTypeId}/subjects', // 注册的任教学科
    REST_SUBJECTS_BY_PARENT:     '/new-cert/rest/signUp/{parentId}/subjects', // 注册的任教学科
    REST_SUBJECTS_TEASUBJECT:    '/new-cert/rest/signUp/provinces/{provinceId}/teachGrades/{teachGradeId}/subjects', // 注册的现任教学科

    REST_ZHUCE_MAJOR_PARENT:     '/new-cert/rest/signUp/majors/root', // 注册的最高学历所学专业根节点
    REST_MAJOR_CHILDREN:         '/new-cert/rest/signUp/{parentId}/majors/children', // 认定或注册的最高学历所学专业子节点

    REST_TECHNICAL_JOB_ROOT:     '/new-cert/rest/signUp/technicaljobs/root',
    REST_TECHNICAL_JOB_CHILDREN: '/new-cert/rest/signUp/{parentId}/technicaljobs/children',

    REST_ENROLL_STEP3:           '/new-cert/rest/signUp/enroll/step3?idNo={idNo}&certNo={certNo}', // 第三步验证
    REST_ENROLL_ORG_VALIDATION:  '/new-cert/rest/signUp/enroll/orgs/{orgId}/validation', // 验证注册的注册机构

    URI_UPLOAD_PERSON_IMAGE: '/new-cert/upload-person-image',
    WEB_UPLOADER_SWF: 'https://cdn.staticfile.org/webuploader/0.1.5/Uploader.swf'
};

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//                                                       UiUtils                                                 //
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
function UiUtils() {}

/**
 * 取得选中的 option 的数据
 *
 * @param  {string} selectSelector select 的选择器
 * @return {json}                  返回 option 的 id, name, option 自己组成的对象
 */
UiUtils.getSelectedOption = function(selectSelector) {
    var $selectedOption = $(selectSelector).find('option:selected');

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
 * @param {int}    id
 * @param {string} name
 */
UiUtils.setFormData = function(formDataName, id, name) {
    var $dataSpan = $('span.form-data[name="' + formDataName + '"]');
    $dataSpan.attr('data-id', id);
    $dataSpan.attr('data-name', name);
    $dataSpan.text(name);
};

/**
 * 选择 containerSelector 下面 span.form-data 属性 name 为 formDataName 的 span 中的属性 data-id 和 data-text
 *
 * @param  {string} containerSelector form-data 所在的容器的选择器
 * @param  {string} formDataName      保存 form-data 的属性名
 * @return {json}                     格式为 {id: 1, name: 'foo'}，只有 id 和 name 2 个属性
 */
UiUtils.getFormData = function(containerSelector, formDataName) {
    var $dataSpan = $('span.form-data[name="' + formDataName + '"]', $(containerSelector));
    var id = parseInt($dataSpan.attr('data-id'));
    id = (!id) ? -1 : id; // 如果 id 不存在，则为 -1

    return {
        id: id,
        name: $dataSpan.attr('data-name')
    };
};

/**
 * 删除 select 中的所有 options，只留下 '请选择' 一个 option
 *
 * @param  {string} selectId select 的 id
 */
UiUtils.onlyPleaseSelectOption = function(selectId) {
    $('#'+selectId+' option').remove();
    $('#'+selectId).append('<option selected="selected" value="-1">请选择</option>');
};

/**
 * 使用 Ajax 加载数据，并显示到树中
 *
 * @param  {[type]} $tree       树
 * @param  {function} urlMethod 确定加载数据 URL 的函数，其参数为 treeId, treeNode，返回值为 URL
 */
UiUtils.requestDataAndShowInTree = function($tree, urlMethod) {
    var settings = {
        async: {
            enable: true,
            url: urlMethod,
            type: 'GET',
            dataFilter: filter
        },
        view: {
            showIcon: false
        }
    };

    function filter(treeId, parentNode, result) {
        if (!result) return null;

        var childNodes = result.data;

        for (var i = 0, l = childNodes.length; i < l; i++) {
            childNodes[i].isParent = true;
        }

        return childNodes;
    }

    $.fn.zTree.destroy();
    window.subjectsTree = $.fn.zTree.init($tree, settings);
};

/**
 * 向 select 中插入 options
 *
 * 默认保留第一个选项，模版名为 optionTemplate，名字的过滤为空
 *
 * @param  {string} selectId   select 的 id
 * @param  {array} optionsData ptions 的数据，为 {id: 12, name: 'Foo', status: true} 等
 * @param  {json}  config     可选参数, 查看 defaults
 */
UiUtils.insertOptions = function(selectId, optionsData, config) {
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
        templateId: 'optionTemplate', // 默认模版的 id
        remainFirstOption: true, // 留下第一个选项
        onlyEnabledItems: true // 是否显示 status 为 false 的项
    };
    var settings = $.extend({}, defaults, config);

    // 如果 remainFirstOption 为 true 则留下第一个选项 "请选择"，否则删除所有的选项
    $select.find('option').remove();

    if (settings.remainFirstOption) {
        $select.append('<option selected="selected" value="-1">请选择</option>');
    }

    for (var i = 0; i < optionsData.length; ++i) {
        // filters 为空，或者不为空时 name 在 filters 中才显示
        if (0 === settings.filters.length || -1 != $.inArray(optionsData[i].name, settings.filters)) {
            // 如果有 status 属性，并且 status 为 false，则不显示
            if (settings.onlyEnabledItems && optionsData[i].hasOwnProperty('status') && !optionsData[i].status) {
                continue;
            }
            $select.append(template(settings.templateId, optionsData[i]));
        }
    }
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
 * @param  {string} idNo 身份证号码
 * @return {bool}        身份证号码返回 true，否则返回 false
 */
IdCard.validate = function(idNo) {
    var regex = /^[1-9]\d{5}[1-9]\d{3}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])\d{3}([0-9]|X|x)$/;
    return regex.test(idNo);
};

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//                                                  教师资格证书号码                                                //
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
function CertNo() {}

/**
 * 验证教师资格证书号码是否有效
 *
 *      1. 必须是数字
 *      2. 必须是 15 位或者 17 为的
 *      3. 如果是 15 位的年份不能小于 1996
 *      4. 如果是 17 位的年份不能小雨 1996 且不能大于当前年
 *
 * @param  {string} certNo 教师资格证书号码
 * @return {bool}          教师资格证书号码有效返回 true，否则返回 false
 */
CertNo.validate = function(certNo) {
    var certNoFormat=/^(\d{15}|\d{17})$/;
    if(!certNoFormat.test(certNo)){
    	alert('教师资格证书号码不规范，请检查您的证书号码，或者去发证机关规范教师资格证书后再报名');
    	return false;
    }

    var certNoYear;
    if(certNo.length == 15){
    	certNoYear = '19' + certNo.substring(0, 2);
    	if(certNoYear < '1996'){
    		alert("教师资格证书号码不规范，请检查您的证书号码，或者去发证机关规范教师资格证书后再报名");
    		return false;
    	}
    }

    if(certNo.length == 17){
    	certNoYear = certNo.substring(0, 4);
    	var nowYear = new Date().getFullYear();
    	if(certNoYear < '1996' || certNoYear > nowYear){
    		alert("教师资格证书号码不规范，请检查您的证书号码，或者去发证机关规范教师资格证书后再报名");
    		return false;
    	}
    }

    return true;
};

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//                                                       格式校验类                                                //
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
function Validator() {}

/**
 * 验证邮件地址是否有效
 *
 * @param  {string} email 邮件地址
 * @return {bool}         邮件地址有效返回 true，否则返回 false
 */
Validator.validateEmail = function(email) {
    var result = {success: false, message: ''};

    if (!(/^.+@.+\..+$/.test(email))) {
        result.message = '邮箱地址不正确';
        return result;
    }

    result.success = true;
    return result;
};

/**
 * 验证密码是否有效
 * 密码的验证规则
 *     1. 不少于 8 位
 *     2. 必须包含数字
 *     3. 必须包含字母
 *     4. 必须包含特殊字符，特殊字符需从 “#、%、*、-、_、!、@、$、&” 中选
 *
 * @param  {string} password 密码
 * @return {json}            密码有效返回对象的 success 属性为 true，否则为 false
 */
Validator.validatePassword = function(password) {
    var result = {success: false, message: ''};

    if (password.length < 8) {
        result.message = '密码不少于 8 位';
        return result;
    }

    if (!(/^.*[0-9]+.*$/.test(password))) {
        result.message = '密码必须包含数字';
        return result;
    }

    if (!(/^.*[a-zA-Z]+.*$/.test(password))) {
        result.message = '密码必须包含字母';
        return result;
    }

    if (!(/^.*[#%_!@&\-\*\$]+.*$/.test(password))) {
        result.message = '密码必须包含特殊字符';
        return result;
    }

    result.success = true;

    return result;
};
