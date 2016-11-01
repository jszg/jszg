$(document).ready(function() {
    initWebUploader(); // 初始化照片上传控件
    initializeDatePicker(); // 初始化时间选择器
    handleNextAndPreviousEvents(); // 处理下一步，上一步的事件
    handleChangeProvincesEvent(); // 处理切换省时加载城市的事件
    handleChangeProvincesForCollegeEvent(); // 处理切换省时加载最高学历毕业学校的事件

    handleRecognizeOrgsDialog(); // 第四步的认定机构
    handleRegisterSubjectsDialog(); // 第四步的任教学科
    handleTeachSubjectsDialog(); // 第四步的现任教学科
    handleRequestRegisterOrgs(); // 第四步的注册机构
    handleSearchLocalSets(); // 第五步的搜索确认点
    handleGraduationCollegesDialog(); // 第七步的最高学历毕业学校
    handleMajorsDialog(); // 第七步的最高学历所学专业
    handleTechnicalJobsDialog(); // 第七步的教师职务（职称）

    requestDicts();

    // StepUtils.toStep(7); // 到第 N 步，测试使用
    // requestLocalSets(21);

    // 点击取消按钮关闭弹出对话框
    $('.pop-dialog .cancel-button').click(function(event) {
        $("#lean_overlay").click();
    });

    $('tr:last', $('table')).css('border-bottom', 'none'); // 删除最后一行的 border-bottom
});

function initWebUploader() {
    var uploader = WebUploader.create({
        auto: true,                 // 自动上传
        swf: Urls.WEB_UPLOADER_SWF, // swf 文件路径
        server: Urls.URI_UPLOAD_ENROLL_IMAGE, // 文件接收服务端
        pick: '#filePicker',       // 选择文件的按钮，内部根据当前运行时创建，可能是 input 元素，也可能是 flash.
        resize: true,              // 不压缩 image, 默认如果是 jpeg，文件上传前会压缩一把再上传！
        accept: { // 只允许上传图片
            title: 'Images',
            extensions: 'gif,jpg,jpeg,bmp,png',
            mimeTypes: 'image/*'
        },
        compress: { // 对上传的图片进行裁剪处理
            width: 114,
            height: 156,
            allowMagnify: false,
            crop: false // false 为等比缩放
        }
    });

    // 上传成功
    // response 为服务器返回来的数据
    uploader.onUploadSuccess = function(file, response) {
        UiUtils.setFormData('photo', -1, response.data);
        uploader.removeFile(file, true); // 启用多次上传
    };

    // 上传成功，例如抛异常
    // response 为服务器返回来的数据
    uploader.onUploadError = function(file, response) {
        // console.log(response);
    };

    // 上传进度 [0.0, 1.0]
    // fileQueued 时创建进度条，uploadProgress 更新进度条
    // 可以使用 file.id 来确定是哪个文件的上传进度
    uploader.onUploadProgress = function(file, percentage) {
        // console.log(percentage);
        // console.log('uploadProgress:' + file.id);
    };

    // 当有文件添加进来的时候
    uploader.onFileQueued = function(file) {
        // 创建缩略图，如果为非图片文件，可以不用调用此方法，src 是 base64 格式的图片
        uploader.makeThumb(file, function(error, src) {
            if (error) {
                return;
            }

            $('#photo img').attr('src', src);
        }, 114, 156); // 100 * 100 为缩略图多大小
    };
}

function handleNextAndPreviousEvents() {
    ////////////////////////////////////////////////////////////////////////
    ///                                下一步                              //
    ////////////////////////////////////////////////////////////////////////
    // 第一步的下一步
    $('#box-1-next').click(function() {
        UiUtils.reloadPageWhenNoAction();

        $('#box-1').hide();
        $('#box-2').show();
        $('.bz2').addClass('active');
    });

    // 第二步的下一步
    $('#box-2-next').click(function() {
        UiUtils.reloadPageWhenNoAction();

        if (!$('#agree-checkbox').get(0).checked) {
            alert('请先阅读网上申报协议并同意后才可以申报！');
            return;
        }

        $('#box-2').hide();
        $('#box-3').show();
        $('.bz3').addClass('active');
    });

    // 第三步的下一步
    $('#box-3-next').click(function() {
        UiUtils.reloadPageWhenNoAction();

        if (StepValidator.validate3thStep()) {
            // 验证通过，进入第四步
            $('#box-3').hide();
            $('#box-4').show();
            $('.bz4').addClass('active');
        }
    });

    // 第四步的下一步
    $('#box-4-next').click(function() {
        UiUtils.reloadPageWhenNoAction();

        if (StepValidator.validate4thStep()) {
            $('#box-4').hide();
            $('#box-5').show();
            $('.bz5').addClass('active');
        }
    });

    // 第五步的下一步
    $('#box-5-next').click(function() {
        UiUtils.reloadPageWhenNoAction();

        if (StepValidator.validate5thStep()) {
            $('#box-5').hide();
            $('#box-6').show();
            $('.bz6').addClass('active');
        }
    });

    // 第六步的下一步
    $('#box-6-next').click(function() {
        UiUtils.reloadPageWhenNoAction();

        $('#box-6').hide();
        $('#box-7').show();
        $('.bz7').addClass('active');
    });

    // 第七步的下一步
    $('#box-7-submit').click(function() {
        UiUtils.reloadPageWhenNoAction();

        if (StepValidator.validate7thStep()) {
            $('#box-7').hide();
            $('#box-8').show();
            $('.bz8').addClass('active');
        }
    });

    // 第七步的退出
    $('#box-7-exit').click(function() {
        if(confirm('您确定要 "退出" 吗？退出后所有信息都不会保存。\n点击 "确定" 直接退出，点击 "取消" 返回编辑界面')) {
            // UiUtils.closeWindow();
            location.reload(); // 刷新当前页
        }
    });

    ////////////////////////////////////////////////////////////////////////
    ///                                上一步                              //
    ////////////////////////////////////////////////////////////////////////
    $('#box-2-previous, #box-3-previous, #box-4-previous, #box-5-previous, #box-6-previous, #box-7-previous').click(function() {
        UiUtils.reloadPageWhenNoAction();

        var step = parseInt($(this).attr('data-step'));
        StepUtils.toPreviousStep(step);
    });
}

/**
 * 使用同步的方式加载确认点
 *
 * @param  {int} orgId 注册机构的 id
 */
function requestLocalSets(orgId) {
    $('#local-sets-table tr:gt(0)').remove(); // 先删除所有的确认点

    if (-1 == orgId) {
        return;
    }

    $.rest.get({url: Urls.REST_LOCALSETS, urlParams: {orgId: orgId}, async: false, success: function(result) {
        $('#local-sets-div').hide();
        $('#no-local-sets-info').hide();

        if (result.data.length === 0) {
            $('#no-local-sets-info').show();
        } else {
            $('#local-sets-div').show();
            $('#local-sets-table').append(template('localSetsTemplate', {localSets: result.data}));
        }
    }});
}

/**
 * 第五步的搜索
 */
function handleSearchLocalSets() {
    $('#local-sets-div .search-button').click(searchLocalSets);
    $('#local-sets-div .search-input').keyup(function(event) {
        if (event.keyCode==13) {
            searchLocalSets();
        }
    });
    // 列出全部
    $('#local-sets-div .show-all-button').click(function(event) {
        $('#local-sets-table tr').show();
    });

    function searchLocalSets() {
        $('#local-sets-table input:radio').removeAttr('checked'); // 取消选中
        var text = $.trim($('#local-sets-div .search-input').val()); // 输入的 text

        // [1] 如果输入内容为空白，显示所有的确认点
        // [2] 如果确认点的名字包含输入的 text 则显示，否则隐藏
        $('#local-sets-table tr:gt(0)').each(function(index, el) {
            var name = $('td[name="name"]', this).text();

            if (-1 != name.indexOf(text)) {
                $(this).show();
            } else {
                $(this).hide();
            }
        });
    }
}

/**
 * 请求字典数据，然后添加到 DOM 里
 */
function requestDicts() {
    $.rest.get({url: Urls.REST_DICTS, success: function(result) {
        var data = result.data;
        UiUtils.insertOptions('certTypes', data.certTypes);          // 资格种类
        UiUtils.insertOptions('provinces', data.provinces, {templateId: 'provinceOptionTemplate'});   // 省
        UiUtils.insertOptions('provinces-for-college', data.provinces, {templateId: 'provinceOptionTemplate'}); // 省
        UiUtils.insertOptions('id-types', data.idType, {remainFirstOption: false}); // 身份证
        UiUtils.insertOptions('nations', data.nation);               // 民族
        UiUtils.insertOptions('teach-grades', data.teachGrade);      // 现任教学段
        UiUtils.insertOptions('politicals', data.political);         // 政治面貌
        UiUtils.insertOptions('edu-levels', data.eduLevel, {onlyEnabledItems: false}); // 最高学历
        UiUtils.insertOptions('degrees', data.degree);               // 最高学位
        UiUtils.insertOptions('school-types', data.schoolType);      // 最高毕业学校的学校类型
        UiUtils.insertOptions('pth-levels', data.pthLevel);          // 普通话水平
        UiUtils.insertOptions('school-quales', data.schoolQuale);    // 现任教学校性质
        UiUtils.insertOptions('work-unit-types', data.workUnitType); // 任教学校所在地
        UiUtils.insertOptions('learn-types', data.learnType);        // 学习形式
        UiUtils.insertOptions('normal-majors', data.normalMajor);    // 最高学历专业类别
        UiUtils.insertOptions('post-quales', data.postQuale);        // 岗位性质
    }});
}

/**
 * 处理切换省时加载城市的事件
 * 1. 如果是直辖市，则它的城市为自己且没有 '请选择' 选项
 * 2. 如果不是直辖市，则加载省下的城市，且有 '请选择' 选项
 */
function handleChangeProvincesEvent() {
    $('#provinces').change(function() {
        UiUtils.onlyPleaseSelectOption('cities');
        var $province = $('#provinces option:selected');
        var provinceId = parseInt($province.val());
        var isProvinceCity = ('true' === $province.attr('data-province-city')); // 是否直辖市

        if (isProvinceCity) {
            // 直辖市的市为它自己
            var cities = [{id: provinceId, name: $province.text(), provinceCity: true}];
            UiUtils.insertOptions('cities', cities, {templateId: 'provinceOptionTemplate'});
        } else if (-1 != provinceId) {
            // provinceId 为 -1 表示选择了 "请选择"，则不加载省的城市
            $.rest.get({url: Urls.REST_CITIES_BY_PROVINCE, urlParams: {provinceId: provinceId}, success: function(result) {
                UiUtils.insertOptions('cities', result.data);
            }});
        }
    });
}

/**
 * 处理切换省时加载最高学历毕业学校的事件
 */
function handleChangeProvincesForCollegeEvent() {
    $('#provinces-for-college').change(function() {
        $('#graduation-colleges-holder').empty();
        var provinceId = parseInt($('#provinces-for-college option:selected').val());

        if (-1 != provinceId) {
            $.rest.get({url: Urls.REST_COLLEGES_BY_PROVINCE, urlParams: {provinceId: provinceId}, success: function(result) {
                $('#graduation-colleges-holder').append(template('graduationCollegesId', {colleges: result.data}));
            }});
        }
    });
}

/**
 * 当现任教学段和市变化时加载注册机构
 * 使用 teachGradeId(现任教学段) 和 cityId(市) 加载注册机构
 */
function handleRequestRegisterOrgs() {
    $('#teach-grades, #cities').change(function(event) {
        var teachGradeId = UiUtils.getSelectedOption('#teach-grades').id;
        var cityId = UiUtils.getSelectedOption('#cities').id;
        var provinceCity = UiUtils.getSelectedOption('#cities').option.attr('data-province-city') === 'true';

        if (-1 === teachGradeId || -1 === cityId) {
            return;
        }

        $.rest.get({url: Urls.REST_ORGS_REG, urlParams: {teachGradeId: teachGradeId, cityId: cityId, provinceCity: provinceCity},
            success: function(result) {
                console.log(result.data);
                UiUtils.insertOptions('register-orgs', result.data);
        }});
    });
}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//                                                           验证                                                //
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

function StepValidator () {}

/**
 * 验证第三步数据，验证通过后并填充在其他步骤上对应的数据
 *      1. 身份证有效
 *      2. 证书号码格式有效
 *      3. 查询证书历史数据
 *          3.1 如果同时有历史记录和注册记录，提示证书数据有误，返回 false
 *          3.2 如果只有历史记录或者注册记录，显示他们，返回 true
 *      4. 证书没有历史数据，则显示输入的信息，返回 true
 *
 * @return {bool} 验证通过返回 true，否则返回 false
 */
StepValidator.validate3thStep = function() {
    // 有可能重复验证，清空以前的数据
    UiUtils.setFormData('inHistory',       -1, false);
    UiUtils.setFormData('inRegistration',  -1, false);
    UiUtils.setFormData('enrollNumber',    -1, 0);
    UiUtils.setFormData('registerId',      -1, -1);
    UiUtils.setFormData('idType',          -1, '');
    UiUtils.setFormData('idNo',            -1, '');
    UiUtils.setFormData('certNo',          -1, '');
    UiUtils.setFormData('certAssignDate',  -1, '');
    UiUtils.setFormData('certType',        -1, '');
    UiUtils.setFormData('recognizeOrg',    -1, '');
    UiUtils.setFormData('registerSubject', -1, '');
    UiUtils.setFormData('name',            -1, '');
    UiUtils.setFormData('gender',          -1, '');
    UiUtils.setFormData('birthday',        -1, '');
    UiUtils.setFormData('nation',          -1, '');

    var idType = UiUtils.getSelectedOption('#id-types'); // 证件类型
    var idNo = $.trim($('#idNo').val()); // 身份证件号码
    var certNo = $.trim($('#certNo').val()); // 教师资格证书号码

    if (-1 == idType.id) {
        alert('请选择 "证件类型"');
        return false;
    }

    if (idType.id == 36 && !IdCard.validate(idNo)) {//证件类型为身份证时校验
        alert('请输入有效的身份证号码');
        return false;
    }

    if (!CertNo.validate(certNo)) {
        return false;
    }

    var enrollNumber = -1; // 教师资格注册的次数，如果为 -1 表示以前没有注册过
    var historyData; // 证书的历史数据
    var certificationValid = true; // 证书数据有误
    var invalid = false;
    var invalidMessage = '';

    // 查询历史记录，如果有
    $.rest.get({url: Urls.REST_ENROLL_STEP3, urlParams: {idNo: idNo, certNo: certNo}, async: false, success: function(result) {
        if (!result.success) {
            invalid = true;
            invalidMessage = result.message;
            return;
        }
        var enrollment = result.data.enrollment;
        enrollNumber = enrollment.enrollNum;

        // [0] 如果同时在在历史记录和注册记录里，提示证书有问题
        if (enrollment.inHistory && enrollment.inRegistration) {
            certificationValid = false;
        }

        // [1] 在历史记录里
        if (enrollment.inHistory) {
            historyData = result.data.historyValid;
            UiUtils.setFormData('inHistory', -1, true);
        }

        // [2] 在注册记录里
        if (enrollment.inRegistration) {
            historyData = result.data.registration;
            UiUtils.setFormData('inRegistration', -1, true);
        }

        // provinceCode 为省的标记，如果为 45 则为广西
        if (result.data.provinceCode) {
            UiUtils.setFormData('provinceCode', -1, result.data.provinceCode);
        }
    }});

    if (invalid) {
        alert(invalidMessage);
        return false;
    }

    // 证书数据有误，提示
    if (!certificationValid) {
        alert('证书数据存在异常，请联系网站工作人员');
        return false;
    }

    // 显示需要填写的信息，隐藏注册的信息
    $('#box-4 .registered').hide();
    $('#box-4 .unregistered').show();

    // 如果有证书的历史数据，显示他们，然后返回
    if (historyData) {
        UiUtils.setFormData('enrollNumber',    -1, enrollNumber);
        UiUtils.setFormData('registerId',      historyData.id, historyData.id);
        UiUtils.setFormData('idType',          -1, historyData.idTypeName);
        UiUtils.setFormData('idNo',            -1, historyData.idNo);
        UiUtils.setFormData('certNo',          -1, historyData.certNo);
        UiUtils.setFormData('certAssignDate',  -1, historyData.certAssign.substring(0, 10));
        UiUtils.setFormData('certType',        -1, historyData.certType);
        UiUtils.setFormData('recognizeOrg',    -1, historyData.orgName);
        UiUtils.setFormData('registerSubject', -1, historyData.subjectName);
        UiUtils.setFormData('name',            -1, historyData.name);
        UiUtils.setFormData('gender',          -1, historyData.sexName);
        UiUtils.setFormData('birthday',        -1, historyData.birthday.substring(0, 10));
        UiUtils.setFormData('nation',          -1, historyData.nationName);

        // 显示注册的信息，隐藏需要填写的信息
        $('#box-4 .unregistered').hide();
        $('#box-4 .registered').show();
        return true;
    }

    UiUtils.setFormData('idType', idType.id, idType.name);
    UiUtils.setFormData('certNo', -1, certNo);

    // 显示身份证上的信息
    var idCard = new IdCard('', idNo);
    UiUtils.setFormData('idNo', -1, idCard.idNo);
    UiUtils.setFormData('birthday', -1, idCard.birthdayString);
    UiUtils.setFormData('gender', (idCard.gender === '男') ? 1 : 2, idCard.gender);

    return true;
};

/**
 * 第四步的验证
 *
 * @return {bool} 验证通过返回 true，否则返回 false
 */
StepValidator.validate4thStep = function() {
    var enrollNumber = parseInt(UiUtils.getFormData('#box-4', 'enrollNumber').name); // 已经注册的次数

    // 没有注册过则需要验证用户输入，如果注册过了则不需要
    if (0 === enrollNumber) {
        var provinceCode    = parseInt(UiUtils.getFormData('#box-4', 'provinceCode').name);    // 认定机构
        var certAssignDate  = $.trim($('#cert-assign-date').val());             // 证书签发日期
        var certType        = UiUtils.getSelectedOption('#certTypes');          // 资格种类
        var recognizeOrg    = UiUtils.getFormData('#box-4', 'recognizeOrg');    // 认定机构
        var registerSubject = UiUtils.getFormData('#box-4', 'registerSubject'); // 任教学科
        var name            = $.trim($('#name').val());                         // 姓名
        var nation          = UiUtils.getSelectedOption('#nations');            // 民族

        // 如果 provinceCode == 45 则为广西的，证书签发日期必须小于 2012-01-01 之前
        // 如果 provinceCode != 45 则为非广西的，证书签发日期必须小于 2008-08-01 之前
        if (!certAssignDate)          { alert('请选择 "证书签发日期"'); return false; }
        if (certAssignDate < '1996-01-01' || certAssignDate >= '2012-01-01') { alert('请重新检查证书号码或修改证书签发日期'); return false; }
        if (45 != provinceCode && certAssignDate > '2008-08-01') { alert('请仔细检查证书号码或证书签发日期是否有误'); return false; }
        if (-1 == certType.id)        { alert('请选择 "资格种类"');    return false; }
        if (-1 == recognizeOrg.id)    { alert('请选择 "认定机构"');    return false; }
        if (-1 == registerSubject.id) { alert('请选择 "任教学科"');    return false; }
        if (!name)                    { alert('请输入 "姓名"');       return false; }
        if (-1 == nation.id)          { alert('请选择 "民族"');       return false; }

        UiUtils.setFormData('certAssignDate', -1, certAssignDate);
        UiUtils.setFormData('name', -1, name);
        UiUtils.setFormData('nation', nation.id, nation.name);
        UiUtils.setFormData('certType', certType.id, certType.name);
        UiUtils.setFormData('recognizeOrg', recognizeOrg.id, recognizeOrg.name);
        UiUtils.setFormData('registerSubject', registerSubject.id, registerSubject.name);
    }

    // 下面的信息不管有没有注册过都需要验证
    var teachGrade   = UiUtils.getSelectedOption('#teach-grades');    // 现任教学段
    var province     = UiUtils.getSelectedOption('#provinces');       // 所在省
    var city         = UiUtils.getSelectedOption('#cities');          // 所在市
    var registerOrg  = UiUtils.getSelectedOption('#register-orgs');   // 注册机构
    var teachSubject = UiUtils.getFormData('#box-4', 'teachSubject'); // 现任教学科

    if (-1 == teachGrade.id)      { alert('请选择 "现任教学段"');  return false; }
    if (-1 == province.id)        { alert('请选择 "省"');         return false; }
    if (-1 == city.id)            { alert('请选择 "市"');         return false; }
    if (-1 == registerOrg.id)     { alert('请选择 "注册机构"');    return false; }
    if (-1 == teachSubject.id)    { alert('请选择 "现任教学科"');  return false; }

    // 验证注册机构，如果无效，则返回 false，不让继续第五步
    var valid = true;
    $('#register-org-error').text('');
    $.rest.get({url: Urls.REST_ENROLL_ORG_VALIDATION, urlParams: {orgId: registerOrg.id}, async: false, success: function(result) {
        if (!result.success) {
            $('#register-org-error').text(result.message);
            valid = false;
        } else {
            UiUtils.setFormData('certBatchId', -1, result.data.certBatchId);
        }
    }});

    if (!valid) {
        return false;
    }

    UiUtils.setFormData('teachGrade', teachGrade.id, teachGrade.name);
    UiUtils.setFormData('province', province.id, province.name);
    UiUtils.setFormData('registerOrg', registerOrg.id, registerOrg.name);
    UiUtils.setFormData('teachSubject', teachSubject.id, teachSubject.name);

    requestLocalSets(registerOrg.id); // 请求确认点

    return true;
};

/**
 * 验证第五步数据，选中一个确认点
 *
 * @return {bool} 验证通过返回 true，否则返回 false
 */
StepValidator.validate5thStep = function() {
    var $localSet = $('#local-sets-table input:radio:checked');

    if (0 === $localSet.length) {
        alert('请选择 "确认点"');
        return;
    }

    // 查找确认点的信息，显示在第六步的注意事项下
    var localeId = parseInt($localSet.attr('data-locale-id'));
    var localSetId = parseInt($localSet.val());
    $.rest.get({url: Urls.REST_LOCALSET_INFO, urlParams: {localSetId: localSetId}, async: false, success: function(result) {
        if (result.data && result.data.info) {
            $('#local-set-info').html(result.data.info);
        }
    }});

    UiUtils.setFormData('localeId', -1, localeId);
    UiUtils.setFormData('localSetId',-1, localSetId);

    return true;
};

/**
 * 验证第七步数据
 *
 * @return {bool} 验证通过返回 true，否则返回 false
 */
StepValidator.validate7thStep = function() {
    var $localSet = $('#local-sets-table input:radio:checked');
    var localSet = {id: $localSet.val(), name: $localSet.attr('data-name')}; // 确认点

    var box7 = '#box-7';
    var certAssignDate    = UiUtils.getFormData(box7, 'certAssignDate').name; // 证书签发日期
    var name              = UiUtils.getFormData(box7, 'name').name;           // 姓名
    var genderId          = UiUtils.getFormData(box7, 'gender').id;           // 性别
    var idTypeId          = UiUtils.getFormData(box7, 'idType').id;           // 证件类型
    var idNo              = UiUtils.getFormData(box7, 'idNo').name;           // 身份证号码
    var certNo            = UiUtils.getFormData(box7, 'certNo').name;         // 教师资格证书号码
    var birthday          = UiUtils.getFormData(box7, 'birthday').name;       // 出生日期
    var nationId          = UiUtils.getFormData(box7, 'nation').id;           // 民族
    var certTypeId        = UiUtils.getFormData(box7, 'certType').id;         // 申请资格种类
    var registerId        = UiUtils.getFormData(box7, 'registerId').id;       // 认定 id: regId
    var registerSubjectId = UiUtils.getFormData(box7, 'registerSubject').id;  // 任教学科
    var registerOrgId     = UiUtils.getFormData(box7, 'registerOrg').id;      // 注册机构
    var recognizeOrgId    = UiUtils.getFormData(box7, 'recognizeOrg').id;     // 认定机构
    var recognizeOrgName  = UiUtils.getFormData(box7, 'recognizeOrg').name;   // 认定机构
    var teachGradeId      = UiUtils.getFormData(box7, 'teachGrade').id;       // 现任教学段
    var provinceId        = UiUtils.getFormData(box7, 'province').id;         // 所在省
    var teachSubjectId    = UiUtils.getFormData(box7, 'teachSubject').id;     // 现任教学科
    var enrollNumber      = UiUtils.getFormData(box7, 'enrollNumber').name;   // 注册次数
    var inHistory         = UiUtils.getFormData(box7, 'inHistory').name;      // 证书是否在认定历史库
    var inRegistration    = UiUtils.getFormData(box7, 'inRegistration').name; // 证书是否在认定正式表
    var certBatchId       = UiUtils.getFormData(box7, 'certBatchId').name;    // 注册批次
    var localeId          = UiUtils.getFormData(box7, 'localeId').name;       // 确认点
    var localSetId        = UiUtils.getFormData(box7, 'localSetId').name;     // 确认点安排
    ////////////////////////// 以上数据都不需要验证，前面步骤已经验证过了 //////////////////////////

    var password1 = $('#password1').val(); // 系统登录密码
    var password2 = $('#password2').val(); // 密码确认
    var email = $.trim($('#email').val()); // 密码找回邮箱

    var emailResult = Validator.validateEmail(email);
    var passwordResult = Validator.validatePassword(password1);
    if (!passwordResult.success) { alert(passwordResult.message); return false; }
    if (password1 != password2)  { alert('两次输入的密码不一致');    return false; }
    if (!email)                  { alert('请输入 "密码找回邮箱"');   return false; }
    if (!emailResult.success)    { alert(emailResult.message);    return false; }

    var degreeId            = UiUtils.getSelectedOption('#degrees').id;          // 最高学位
    var eduLevelId          = UiUtils.getSelectedOption('#edu-levels').id;       // 最高学历
    var learnTypeId         = UiUtils.getSelectedOption('#learn-types').id;      // 最高学历学习形式
    var normalMajorId       = UiUtils.getSelectedOption('#normal-majors').id;    // 最高学历专业类别
    var graduationCollegeId = UiUtils.getFormData(box7, 'graduationCollege').id; // 最高学历毕业学校
    var graduationCollegeName = UiUtils.getFormData(box7, 'graduationCollege').name; // 最高学历毕业学校
    var majorId             = UiUtils.getFormData(box7, 'major').id;             // 最高学历所学专业
    var graduationTime      = $.trim($('#graduation-date').val());               // 最高学历毕业时间
    var politicalId         = UiUtils.getSelectedOption('#politicals').id;       // 政治面貌
    var workUnitTypeId      = UiUtils.getSelectedOption('#work-unit-types').id;  // 任教学校所在地
    var schoolQualeId       = UiUtils.getSelectedOption('#school-quales').id;    // 现任教学校性质
    var workUnit            = $.trim($('#work-unit').val());                     // 现任教学校
    var workDate            = $.trim($('#work-date').val());                     // 现任教学校聘用起始日期
    var postQualeId         = UiUtils.getSelectedOption('#post-quales').id;      // 岗位性质
    var pthLevelId          = UiUtils.getSelectedOption('#pth-levels').id;       // 普通话水平
    var beginWorkYear       = $.trim($('#begin-work-year').val());               // 开始参加工作时间
    var technicalJobId      = UiUtils.getFormData(box7, 'technicalJob').id;      // 教师职务（职称）
    var birthPlace          = $.trim($('#birth-place').val());         // 出生地
    var residence           = $.trim($('#residence').val());           // 户籍所在地
    var address             = $.trim($('#address').val());             // 通讯地址
    var zipCode             = $.trim($('#zip-code').val());            // 通讯地的邮编
    var phone               = $.trim($('#phone').val());               // 联系电话
    var cellphone           = $.trim($('#cellphone').val());           // 手机
    var photo               = UiUtils.getFormData(box7, 'photo').name; // 照片

    if (-1 === degreeId)            { alert('请选择 "最高学位"');        return false; }
    if (-1 === eduLevelId)          { alert('请选择 "最高学历"');        return false; }
    if (-1 === learnTypeId)         { alert('请选择 "最高学历学习形式"'); return false; }
    if (-1 === normalMajorId)       { alert('请选择 "最高学历专业类别"'); return false; }
    if (-1 === graduationCollegeId) { alert('请选择 "最高学历毕业学校"'); return false; }
    if (-1 === majorId)             { alert('请选择 "最高学历所学专业"'); return false; }
    if (!graduationTime)            { alert('请输入 "最高学历毕业时间"'); return false; }
    if (-1 === politicalId)         { alert('请选择 "政治面貌"');        return false; }
    if (-1 === workUnitTypeId)      { alert('请选择 "任教学校所在地"');   return false; }
    if (-1 === schoolQualeId)       { alert('请选择 "现任教学校性质"');   return false; }
    if (!workUnit)                  { alert('请输入 "现任教学校"');       return false; }
    if (!workDate)                  { alert('请输入 "现任教学校聘用起始日期"'); return false; }
    if (-1 === postQualeId)         { alert('请选择 "岗位性质"');        return false; }
    if (-1 === pthLevelId)          { alert('请选择 "普通话水平"');      return false; }
    if (!beginWorkYear)             { alert('请输入 "开始参加工作时间"'); return false; }
    if (-1 === technicalJobId)      { alert('请选择 "教师职务（职称）"'); return false; }
    if (!birthPlace)                { alert('请输入 "出生地"');          return false; }
    if (!residence)                 { alert('请输入 "户籍所在地"');      return false; }
    if (!address)                   { alert('请输入 "通讯地址"');        return false; }
    if (!zipCode)                   { alert('请输入 "通讯地的邮编"');     return false; }
    if (!phone)                     { alert('请输入 "联系电话"');        return false; }
    if (!cellphone)                 { alert('请输入 "手机"');           return false; }
    if (!photo)                     { alert('请上传 "照片"');           return false; }

    if (workDate < beginWorkYear)    { alert('"现任教学校聘用起始日期" 不能在 "开始参加工作时间" 之前'); return false; }
    if (!(/^[1-9][0-9]{5}$/.test(zipCode))) { alert('通讯地的邮编: 请输入 6 个数字的 "通讯地的邮编"');      return false; }
    if (!(/^\d{11}$/.test(cellphone)))      { alert('手机号码: 请输入 11 个数字的 "手机号码"');         return false; }

    // 通过验证

    var params = {
        name: name,
        idTypeId: idTypeId,
        idNo: idNo,
        certNo: certNo,
        certAssignDate: certAssignDate,
        inHistory: inHistory,
        inRegistration: inRegistration,
        certTypeId: certTypeId,
        enrollBatchId: certBatchId,
        certBatchId: certBatchId,
        registerId: registerId,
        registerSubjectId: registerSubjectId,
        recognizeOrgId: recognizeOrgId,
        recognizeOrgName: recognizeOrgName,
        nationId: nationId,
        genderId: genderId,
        birthday: birthday,
        degreeId: degreeId,
        eduLevelId: eduLevelId,
        learnTypeId: learnTypeId,
        normalMajorId: normalMajorId,
        graduationCollegeId: graduationCollegeId,
        graduationCollegeName: graduationCollegeName,
        majorId: majorId,
        graduationTime: graduationTime,
        beginWorkYear: beginWorkYear,
        workUnitTypeId: workUnitTypeId,
        workUnit: workUnit,
        workDate: workDate,
        schoolQualeId: schoolQualeId,
        postQualeId: postQualeId,
        pthLevelId: pthLevelId,
        technicalJobId: technicalJobId,
        provinceId: provinceId,
        orgId: registerOrgId,
        teachGradeId: teachGradeId,
        localeId: localeId,
        localSetId: localSetId,
        teachSubjectId: teachSubjectId,
        politicalId: politicalId,
        enrollNumber: enrollNumber,
        birthPlace: birthPlace,
        residence: residence,
        address: address,
        zipCode: zipCode,
        phone: phone,
        cellphone: cellphone,
        tmpPhoto: photo,
        email: email,
        password: password1
    };

    var passed = false;

    $.rest.create({url: Urls.URI_ENROLL_SUBMIT, data: params, async: false, success: function(result) {
        if (!result.success) {
            alert(result.message); // 弹出错误消息
        } else {
            passed = true;
        }
    }, error: function(error) {
        alert(error);
    }});

    if (!passed) { return false; } // 表单提交不成功，不进入第八步
    UiUtils.setFormData('successRegName', -1, name);
    UiUtils.setFormData('email', -1, email); // 显示邮箱在第八步上要使用
    return true;
};

/**
 * 初始化时间选择器
 */
function initializeDatePicker() {
    // 证书签发日期
    var certAssignDatePicker = {
        elem: '#cert-assign-date',
        format: 'YYYY-MM-DD',
        istoday: true
    };

    // 最高学历毕业时间
    var graduationDatePicker = {
        elem: '#graduation-date',
        format: 'YYYY-MM-DD',
        istoday: true
    };

    // 开始参加工作时间
    var startWorkDatePicker = {
        elem: '#begin-work-year',
        format: 'YYYY-MM-DD',
        istoday: true
    };

    // 现任教学校聘用起始日期
    var currentWorkStartTime = {
        elem: '#work-date',
        format: 'YYYY-MM-DD',
        istoday: true
    };

    laydate(certAssignDatePicker);
    laydate(startWorkDatePicker);
    laydate(graduationDatePicker);
    laydate(currentWorkStartTime);
}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//                                                        第四步的对话框                                           //
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/**
 * 第四步的认定机构
 * 使用证书签发日期进行过滤
 */
function handleRecognizeOrgsDialog() {
    // 初始化 LeanModal 对话框
    $('#recognize-orgs-dialog-trigger').leanModal({top: 50, overlay : 0.4});

    // 点击 '认定机构' 按钮
    // 如果 '证书签发日期' 为空，则提示输入
    $('#select-recognize-org-button').click(function(event) {
        var certAssignDate = $.trim($('#cert-assign-date').val());

        if (!certAssignDate) {
            alert('请先输入 "证书签发日期"');
            return;
        }

        $('#recognize-orgs-dialog-trigger').click(); // 显示对话框

        // 加载任教学科
        UiUtils.requestDataAndShowInTree($('#recognize-orgs-dialog .ztree'), function(treeId, treeNode) {
            if(!treeNode) {
                return Urls.REST_ORGS_BY_ORGTYPE.format({orgType: 4}) + '?date=' + certAssignDate;
            } else {
                return Urls.REST_ORGS_BY_PARENT.format({parentId: treeNode.id}) + '?date=' + certAssignDate;
            }
        });
    });

    // 点击确定按钮，设置选中的学科，并隐藏对话框
    $('#recognize-orgs-dialog .ok-button').click(function(event) {
        var orgNode = window.subjectsTree.getSelectedNodes()[0];
        if (orgNode) {
            UiUtils.setFormData('recognizeOrg', orgNode.id, orgNode.name);
            $("#lean_overlay").click();
        } else {
            alert('没有选中认定机构');
        }
    });
}

/**
 * 处理选择任教学科的相关事件
 *
 * 1. 点击 '任教学科' 按钮
 *    如果 certTypeId 不为 -1，则加载任教学科并显示在对话框中
 *    如果 certTypeId 为 -1，则提示选择资格种类
 * 2. 点击取消按钮隐藏对话匡
 * 3. 点击确定按钮，设置选中的学科，并隐藏对话框
 */
function handleRegisterSubjectsDialog() {
    // 初始化 LeanModal 对话框
    $('#register-subjects-dialog-trigger').leanModal({top: 50, overlay : 0.4});

    // 点击 '任教学科' 按钮
    // 如果 certTypeId 不为 -1，则请求任教学科
    // 如果 certTypeId 为 -1，则提示选择资格种类
    $('#select-register-subject-button').click(function(event) {
        var certTypeId = UiUtils.getSelectedOption('#certTypes').id;

        if (-1 === certTypeId) {
            alert('请先选择 "资格种类"，然后才能选择 "任教学科"');
            return;
        }

        $('#register-subjects-dialog-trigger').click(); // 显示对话框

        // 加载任教学科
        UiUtils.requestDataAndShowInTree($('#register-subjects-dialog .ztree'), function(treeId, treeNode) {
            if(!treeNode) {
                return Urls.REST_SUBJECTS_BY_CERT_TYPE.format({certTypeId: certTypeId});
            } else {
                return Urls.REST_SUBJECTS_BY_PARENT.format({parentId: treeNode.id});
            }
        });
    });

    // 点击确定按钮，设置选中的学科，并隐藏对话框
    $('#register-subjects-dialog .ok-button').click(function(event) {
        var subjectNode = window.subjectsTree.getSelectedNodes()[0];
        if (subjectNode) {
            UiUtils.setFormData('registerSubject', subjectNode.id, subjectNode.name);
            $("#lean_overlay").click();
        } else {
            alert('没有选中任教学科');
        }
    });
}

/**
 * 第四步的现任教学科
 */
function handleTeachSubjectsDialog() {
    // 初始化 LeanModal 对话框
    $('#teach-subjects-dialog-trigger').leanModal({top: 50, overlay : 0.4});

    $('#select-teach-subject-button').click(function(event) {
        var provinceId = UiUtils.getSelectedOption('#provinces').id;
        var teachGradeId = UiUtils.getSelectedOption('#teach-grades').id;

        if (-1 === teachGradeId) {
            alert('请先选择 "现任教学段"，然后才能选择 "现任教学科"');
            return;
        }

        if (-1 === provinceId) {
            alert('请先选择 "省"，然后才能选择 "现任教学科"');
            return;
        }

        $('#teach-subjects-dialog-trigger').click(); // 显示对话框

        // 加载现任教学科
        UiUtils.requestDataAndShowInTree($('#teach-subjects-dialog .ztree'), function(treeId, treeNode) {
            if(!treeNode) {
                return Urls.REST_SUBJECTS_TEASUBJECT.format({provinceId: provinceId, teachGradeId: teachGradeId});
            } else {
                return Urls.REST_SUBJECTS_CHILDREN.format({provinceId: provinceId, parentId: treeNode.id});
            }
        });
    });

    // 点击确定按钮，设置选中的学科，并隐藏对话框
    $('#teach-subjects-dialog .ok-button').click(function(event) {
        var subjectNode = window.subjectsTree.getSelectedNodes()[0];
        if (subjectNode) {
            UiUtils.setFormData('teachSubject', subjectNode.id, subjectNode.name);
            $("#lean_overlay").click();
        } else {
            alert('没有选中现任教学科');
        }
    });
}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//                                                        第七步的对话框                                           //
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/**
 * 最高学历毕业学校
 */
function handleGraduationCollegesDialog() {
    // 初始化 LeanModal 对话框
    $('#graduation-colleges-dialog-trigger').leanModal({top: 50, overlay : 0.4});

    $('#select-graduation-college-button').click(function(event) {
        $('#graduation-colleges-holder').show(); // 显示学校选择组件
        $('#school-input-holder').hide(); // 隐藏输入学校的组件
        $('#graduation-colleges-dialog-trigger').click(); // 显示对话框
    });

    // 点击确定按钮，设置选中的学科，并隐藏对话框
    $('#graduation-colleges-dialog .ok-button').click(function(event) {
        var $college = $('#graduation-colleges li.active');

        if ($college.length > 0) {
            var id = $college.attr('data-id');
            var name = $college.attr('data-name');
            UiUtils.setFormData('graduationCollege', id, name);
            $("#lean_overlay").click();
        } else {
            alert('没有选中毕业学校');
        }
    });

    // 选中学校
    $(document).on('click', '#graduation-colleges li', function() {
        $(this).siblings().removeClass('active');
        $(this).addClass('active');
    });

    // 点击搜索学校按钮或者输入后按下回车，搜索学校
    var $dlg = $('#graduation-colleges-dialog');
    $('.toolbar .search-button', $dlg).click(searchCollege);
    $('.toolbar .search-input', $dlg).keyup(function(event) {
        if (event.keyCode==13) {
            searchCollege();
        }
    });

    // 点击 "添加" 按钮，隐藏学校的树，显示添加的组件
    $('.toolbar .add-button', $dlg).click(function(event) {
        $('#graduation-colleges-holder').hide(); // 显示学校选择组件
        $('#school-input-holder').show(); // 隐藏输入学校的组件
    });

    $('#school-input-holder .cancel-school-button').click(function(event) {
        $('#school-input-holder').hide(); // 隐藏输入学校的组件
        $('#graduation-colleges-holder').show(); // 显示学校选择组件
    });

    $('#school-input-holder .add-school-button').click(function(event) {
        var collegeType = UiUtils.getSelectedOption('#school-types');
        var collegeName = $.trim($('#school-input-holder input[name="collegeName"]').val());

        if (-1 === collegeType.id) {
            alert('没有选择 "学校类型"');
            return;
        }

        if (!collegeName) {
            alert('没有输入 "学校名称""');
            return;
        }

        UiUtils.setFormData('graduationCollege', -collegeType.id, collegeName);
        $("#lean_overlay").click();

    });

    // 收缩当前省的学校
    function searchCollege() {
        $('#graduation-colleges li').removeClass('active'); // 删除被选中状态
        var provinceId = UiUtils.getSelectedOption('#provinces-for-college').id;

        if (-1 == provinceId) {
            alert('请选择省，然后再进行搜索');
            return;
        }

        var text = $.trim($('.toolbar .search-input', $dlg).val());
        var $colleges = $('#graduation-colleges li');

        // [1] 如果输入内容为空白，显示所有的学校
        // [2] 如果学校的名字包含输入的 text 则显示，否则隐藏
        $colleges.each(function() {
            var schoolName = $(this).text();
            if (-1 != schoolName.indexOf(text)) {
                $(this).show();
            } else {
                $(this).hide();
            }
        });
    }
}

/**
 * 最高学历所学专业
 * 需要注意: 如果最高学历为高中毕业及一下，则不弹出选择对话框，设定最高学历所学专业为无，其 id 固定为 4001
 */
function handleMajorsDialog() {
    // 初始化 LeanModal 对话框
    $('#majors-dialog-trigger').leanModal({top: 50, overlay : 0.4});

    // 点击确定按钮，设置选中的学科，并隐藏对话框
    $('#majors-dialog .ok-button').click(function(event) {
        var subjectNode = window.subjectsTree.getSelectedNodes()[0];
        if (subjectNode) {
            if (0 === subjectNode.level) {
                alert('请选择具体的所学专业');
                return;
            }
            UiUtils.setFormData('major', subjectNode.id, subjectNode.name);
            $("#lean_overlay").click();
        } else {
            alert('没有选中任教学科');
        }
    });

    $('#select-major-button').click(function(event) {
        var eduLevelId = UiUtils.getSelectedOption('#edu-levels').id;

        if (-1 == eduLevelId) {
            alert('请先选择 "最高学历"，然后才能选择 "最高学历所学专业"');
            return;
        }

        // 如果最高学历为高中毕业及以下，则不弹出选择对话框，设定最高学历所学专业为无，其 id 固定为 4001
        if (204 === eduLevelId) {
            UiUtils.setFormData('major', 4001, '无');
            alert('"最高学历" 为 "高中毕业及以下" 时 "最高学历所学专业" 为 "无"，不需要自己选择');
            return;
        }

        $('#majors-dialog-trigger').click(); // 显示对话框

        // 加载最高学历所学专业
        UiUtils.requestDataAndShowInTree($('#majors-dialog .ztree'), function(treeId, treeNode) {
            if(!treeNode) {
                return Urls.REST_ZHUCE_MAJOR_PARENT;
            } else {
                return Urls.REST_MAJOR_CHILDREN.format({parentId: treeNode.id});
            }
        });
    });
}

/**
 * 教师职务（职称）
 * 注意: 如果是顶级节点，且 code 为 '00'，则提示不可使用
 */
function handleTechnicalJobsDialog() {
    // 初始化 LeanModal 对话框
    $('#technical-jobs-dialog-trigger').leanModal({top: 50, overlay : 0.4});

    // 点击确定按钮，设置选中的学科，并隐藏对话框
    $('#technical-jobs-dialog .ok-button').click(function(event) {
        var technicalJobNode = window.subjectsTree.getSelectedNodes()[0];
        if (technicalJobNode) {
            if (0 === technicalJobNode.level && '00' != technicalJobNode.code) {
                alert('请选择具体的教师职务！');
                return;
            }

            UiUtils.setFormData('technicalJob', technicalJobNode.id, technicalJobNode.name);
            $("#lean_overlay").click();
        } else {
            alert('请选择具体的教师职务');
        }
    });

    $('#select-technical-job-button').click(function(event) {
        $('#technical-jobs-dialog-trigger').click(); // 显示对话框

        // 加载最高学历所学专业
        UiUtils.requestDataAndShowInTree($('#technical-jobs-dialog .ztree'), function(treeId, treeNode) {
            if(!treeNode) {
                return Urls.REST_TECHNICAL_JOB_ROOT;
            } else {
                return Urls.REST_TECHNICAL_JOB_CHILDREN.format({parentId: treeNode.id});
            }
        });
    });
}
