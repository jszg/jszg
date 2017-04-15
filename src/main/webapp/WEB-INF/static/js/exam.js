$(document).ready(function() {
    initWebUploader(); // 初始化照片上传控件
    initializeDatePicker(); // 初始化时间选择器
    handleNextAndPreviousEvents(); // 处理下一步，上一步的事件
    handleChangeProvincesEvent(); // 处理切换省时加载城市的事件
    handleRequestOrgs();//处理第三步认定机构
    handleRequestSubjectsDialog(); // 第三步的任教学科
    handleSearchLocalSets(); // 第四步的搜索确认点
    handleChangeProvincesForCollegeEvent();//第七步毕业学校
    handleMajorsDialog(); // 第七步的所学专业
    handleTechnicalJobsDialog();//第七步职业技术职务
    handleChangeEduLevelForDegreeEvent(); //第七步根据最高学历选择最高学位

    handleGraduationCollegesDialog(); // 第七步的最高学历毕业学校
    //StepUtils.toStep(7); // 到第 N 步，测试使用

    requestDicts(); // 请求字典数据，初始化省，政治面貌等

     // 点击取消按钮关闭弹出对话框
    $('.pop-dialog .cancel-button').click(function(event) {
        $("#lean_overlay").click();
    });

    $('tr:last', $('table')).css('border-bottom', 'none'); // 删除最后一行的 border-bottom
});

/**
 * 请求任教学科
 */
function requestSubjects() {
    var setting = {
        async: {
            enable: true,
            url: loadSubjectsUrl,
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

    function loadSubjectsUrl(treeId, treeNode) {
        var provinceId = 12911;
        var certTypeId = 4;

        if(!treeNode) {
            return Urls.REST_SUBJECTS_ROOT.format({provinceId: provinceId, certTypeId: certTypeId});
        } else {
            return Urls.REST_SUBJECTS_CHILDREN.format({provinceId: provinceId, parentId: treeNode.id});
        }
    }

    $.fn.zTree.destroy();
    window.subjectsTree = $.fn.zTree.init($("#subjects"), setting);
    $('#subjects-dialog-trigger').click(); // 显示对话框
}

function initWebUploader() {
    var uploader = WebUploader.create({
        auto: true,                 // 自动上传
        swf: Urls.WEB_UPLOADER_SWF, // swf 文件路径
        server: Urls.URI_UPLOAD_ENROLL_IMAGE, // 文件接收服务端
        pick: '#filePicker',       // 选择文件的按钮，内部根据当前运行时创建，可能是 input 元素，也可能是 flash.
        resize: true,              // 不压缩 image, 默认如果是 jpeg，文件上传前会压缩一把再上传！
        accept: { // 只允许上传图片
            title: 'Images',
            extensions: 'jpg,jpeg',
            // mimeTypes: 'image/*'
            mimeTypes: 'image/jpg,image/jpeg'
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

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//                                                           验证                                                //
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////


function StepValidator () {}


//验证第3步数据
StepValidator.validate3thStep = function(){
    var $name = $.trim($('#name').val());
    var $idNo = $.trim($('#idNo').val());
    var $scoreCertNo = $.trim($('#scoreCertNo').val());
    if(!$name){
        alert('姓名不能为空!');
        return false;
    }

    if (!IdCard.validate($idNo)) {//证件类型为身份证时校验
        alert('请输入有效的身份证号码');
        return false;
    }

    if(!$idNo){
        alert('身份证件号码不能为空!');
        return false;
    }else{
          //判断是否输入有全角
         for (var i = $idNo.length-1; i >= 0; i--){
    　　　　var unicode=$idNo.charCodeAt(i);
    　　    if (unicode>65280 && unicode<65375){
    　　　　　　alert("输入全角字符'"+$idNo[i]+"',身份证件号码不能输入'全角字符'!");
                return false;
    　　　　}
    　　}
    }

     if($idNo.length != 18){
        alert('身份证件号码必须为18位!');
        return false;
     }
     var par=/^[0-9]*$/;
     if(!par.test($idNo.substring(0,17))){
         alert('身份证件号码前17位必须为数字!');
         return false;
     }
     if(!$scoreCertNo){
        alert('请输入统考合格证编号!');
        return false;
     }
     var invalidMessage = '';
     var invalid = false;
     // 查询历史记录，如果有
     $.rest.get({url: Urls.REST_EXAM_STEP3, urlParams: {name: encodeURI(encodeURI($name)), idNo: $idNo,scoreCertNo: $scoreCertNo}, async: false, success: function(result) {
         if (!result.success) {
             invalid = true;
             invalidMessage = result.message;
             alert(invalidMessage);
             return;
         }else{
            var score = result.data.score;
            var certTypeId = score.certType;
            UiUtils.setFormData('name', score.name, score.name);
            UiUtils.setFormData('idType', score.idType, score.idTypeName);
            UiUtils.setFormData('idNo', -1, score.idNo);
            UiUtils.setFormData('scoreCertNo', score.scoreCertNo, score.scoreCertNo);
            UiUtils.setFormData('certType', score.certType, score.certTypeName);
            UiUtils.setFormData('subject', score.subject, score.subjectName);
            UiUtils.setFormData('adminLevel', score.adminLevel, score.adminLevel);
            UiUtils.setFormData('scoreId', score.id, score.id);
         }
     }});

     if (invalid) {
         return false;
     }
     var idCard = new IdCard('', $.trim($idNo));
     var genderId          = idCard.gender;           // 性别
     var birthday          = idCard.birthdayString;           // 出生日期
     UiUtils.setFormData('gender', idCard.genderValue, genderId);
     //UiUtils.setFormData('gender', parseInt($.trim($idNo).substring(16, 17)), genderId);
     UiUtils.setFormData('birthday', birthday, birthday);
    return true;
};


/**
 * 第四步校验:
 *      省       id 不能为 -1
 *      市       id 不能为 -1
 *      认定机构 id 不能为 -1
 *      任教学科 id 不能为 -1
 * @return {bool} true 为验证通过，false 为验证不通过
 */
StepValidator.validate4thStep = function() {
    var subjectId  = UiUtils.getFormData('#box-4', 'subject').id; // 任教学科
    var certTypeId = UiUtils.getFormData('#box-4', 'certType').id; // 申请资格种类

    var provinceId = parseInt($('#provinces option:selected').val());
    var cityId     = parseInt($('#cities option:selected').val());
    var orgId      = parseInt($('#request-orgs option:selected').val());

    if (-1 == provinceId) {
        alert('请选择: 省');
        return false;
    }

    if (-1 == cityId) {
        alert('请选择: 市');
        return false;
    }

    if (-1 == orgId) {
        alert('请选择: 认定机构');
        return false;
    }

    // 验证非统考认定机构，如果无效，则返回 false，不让继续第四步
    var valid = true;
    $('#exam-org-error').text('');
    $.rest.get({url: Urls.REST_REQUEST_ORG_VALIDATION, urlParams: {orgId:orgId,certTypeId:certTypeId}, async: false, success: function(result) {
        if (!result.success) {
            $('#exam-org-error').addClass('error');
            $('#exam-org-error').text(result.message);
            valid = false;
        } else {
            UiUtils.setFormData('certBatchId', result.data.certBatchId, result.data.certBatchId);
        }
    }});

    if (!valid) {
        return false;
    }else{
         $('#exam-org-error').removeClass('error');
    }

    //根据选择的资格种类,给第七步的最高学历赋值
    $.rest.get({url: Urls.REST_EDU_LEVELS, urlParams: {certTypeId:certTypeId}, async: false, success: function(result) {
       if (!result.success) {
           valid = false;
       } else {
           UiUtils.insertOptions('edu-levels', result.data); // 最高学历
       }
    }});

    var requestOrg  = UiUtils.getSelectedOption('#request-orgs');   // 统考第三步认定机构
    requestLocalSets(requestOrg.id);//请求确认点
    var registerOrg  = UiUtils.getSelectedOption('#request-orgs');   // 认定机构
    UiUtils.setFormData('recognizeOrg', registerOrg.id, registerOrg.name);
    return true;
}

/**
 * 验证第5步数据，选中一个确认点
 *
 * @return {bool} 验证通过返回 true，否则返回 false
 */
StepValidator.validate5thStep = function() {
    var $localSet = $('#local-sets-table input:radio:checked');

    if (0 === $localSet.length) {
        alert('请选择 "确认点"');
        return;
    }

    // 查找确认点的信息，显示在第五步的注意事项下
    var localeId = parseInt($localSet.attr('data-locale-id'));
    var localSetId = parseInt($localSet.val());
    $.rest.get({url: Urls.REST_LOCALSET_INFO, urlParams: {localSetId: localSetId}, async: false, success: function(result) {
        if (result.data && result.data.info) {
            $('#local-set-info').html(result.data.info);
        }
    }});

    UiUtils.setFormData('box-7-locale-Set', localSetId, $localSet.attr('data-name'));
    UiUtils.setFormData('localeId', localeId, localeId);
    UiUtils.setFormData('localSetId',localSetId, localSetId);

    return true;
};

//验证第7步数据
StepValidator.validate7thStep = function(){
    var $localSet = $('#local-sets-table input:radio:checked');
    var localSet = {id: $localSet.val(), name: $localSet.attr('data-name')}; // 确认点
    var box7 = '#box-7';
    var name       = UiUtils.getFormData(box7, 'name').name; // 姓名
    var idTypeId   = UiUtils.getFormData(box7, 'idType').id; // 证件类型
    var idNo       = UiUtils.getFormData(box7, 'idNo').name; // 身份证号码
    var certTypeId = UiUtils.getFormData(box7, 'certType').id; // 申请资格种类
    var subjectId  = UiUtils.getFormData(box7, 'subject').id; // 任教学科
    var org        = UiUtils.getFormData(box7, 'recognizeOrg').id; // 认定机构
    var orgName    = UiUtils.getFormData(box7, 'recognizeOrg').name; // 认定机构名称
    var provinceId        = UiUtils.getSelectedOption('#provinces').id;       // 所在省
    var localeId          = UiUtils.getFormData(box7, 'localeId').id;       // 确认点
    var localeSetId        = UiUtils.getFormData(box7, 'localSetId').id;       // 确认点安排
    var certBatchId       = UiUtils.getFormData(box7, 'certBatchId').id;    // 注册批次
    var birthday          = UiUtils.getFormData(box7, 'birthday').name;       // 出生日期
    var genderId          = UiUtils.getFormData(box7, 'gender').id;           // 性别
    var scoreCertNo       = UiUtils.getFormData(box7, 'scoreCertNo').id;           // 统考编号
    var scoreId           = UiUtils.getFormData(box7, 'scoreId').id;           // 统考合格名单id
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

    var nationId            = UiUtils.getSelectedOption('#nations').id;          // 民族
    var politicalId         = UiUtils.getSelectedOption('#politicals').id;       // 政治面貌
    var pthLevelId          = UiUtils.getSelectedOption('#pth-levels').id;       // 普通话水平
    var pthCertNo           = $.trim($('#pth_cert_no').val());                    // 普通话证书编号
    var pthOrg              = $.trim($('#pth_org').val());                       // 普通话发证单位
    var graduateId          = $('#graduateId').val();                           // 是否在校生
    var graduationDate      = $.trim($('#graduation-date').val());               // 毕业时间
    var degreeId            = UiUtils.getSelectedOption('#degrees').id;          // 最高学位
    var eduLevelId          = UiUtils.getSelectedOption('#edu-levels').id;       // 最高学历
    var graduationCollegeId = UiUtils.getFormData(box7, 'graduationCollege').id; // 毕业学校
    var graduationCollegeName = UiUtils.getFormData(box7, 'graduationCollege').name; // 毕业学校
    var majorId             = UiUtils.getFormData(box7, 'major').id;             // 所学专业
    var normalMajorId       = UiUtils.getSelectedOption('#normal-majors').id;    // 专业类别
    var learnTypeId         = UiUtils.getSelectedOption('#learn-types').id;      // 最高学历学习形式
    var workUnit            = $.trim($('#work-unit').val());                     // 工作单位
    var occupations         = UiUtils.getSelectedOption('#occupations').id;      // 现从事职业
    var technicalJobId      = UiUtils.getFormData(box7, 'technicalJob').id;      // 职业技术职务
    var residence           = $.trim($('#residence').val());                    // 户籍所在地
    var birthPlace          = $.trim($('#birth-place').val());                  // 出生地
    var address             = $.trim($('#address').val());                      // 通讯地址
    var zipCode             = $.trim($('#zip-code').val());                     // 通讯地的邮编
    var phone               = $.trim($('#phone').val());                        // 联系电话
    var cellphone           = $.trim($('#cellphone').val());                    // 手机
    var photo               = UiUtils.getFormData(box7, 'photo').name;          // 照片

    if (-1 === nationId)            { alert('请选择 "民族"');            return false; }
    if (-1 === politicalId)         { alert('请选择 "政治面貌"');        return false; }
    if (-1 === pthLevelId)          { alert('请选择 "普通话水平"');      return false; }
    if (!pthCertNo)                 { alert('请输入 "普通话证书编号"');  return false; }
    if (!pthOrg)                    { alert('请输入 "普通话发证单位"');  return false; }
    if (-1 === graduateId)          { alert('请选择 "是否在校生"');      return false; }
    if (!graduationDate)            { alert('请输入 "毕业时间"');        return false; }
    if (-1 === degreeId)            { alert('请选择 "最高学位"');        return false; }
    if (-1 === eduLevelId)          { alert('请选择 "最高学历"');        return false; }
    if (-1 === graduationCollegeId) { alert('请选择 "毕业学校"');        return false; }
    if (-1 === majorId)             { alert('请选择 "所学专业"');        return false; }
    if (-1 === normalMajorId)       { alert('请选择 "专业类别"');        return false; }
    if (-1 === learnTypeId)         { alert('请选择 "学习形式"');        return false; }
    if (!workUnit)                  { alert('请输入 "工作单位"');        return false; }
    if (-1 === occupations)         { alert('请选择 "现从事职业"');      return false; }
    if (-1 === technicalJobId)      { alert('请选择 "专业技术职务"');    return false; }
    if (!residence)                 { alert('请输入 "户籍所在地"');      return false; }
    if (!birthPlace)                { alert('请输入 "出生地"');          return false; }
    if (!address)                   { alert('请输入 "通讯地址"');        return false; }
    if (!zipCode)                   { alert('请输入 "通讯地的邮编"');    return false; }
    if (!phone)                     { alert('请输入 "联系电话"');        return false; }
    if (!cellphone)                 { alert('请输入 "手机"');            return false; }
    if (!photo)                     { alert('请上传 "照片"');            return false; }

    if (!(/^\d{6}$/.test(zipCode))) { alert('通讯地的邮编: 请输入 6 个数字的 "通讯地的邮编"');  return false; }
    if (!(/^\d{11}$/.test(cellphone)))      { alert('手机号码: 请输入 11 个数字的 "手机号码"');         return false; }

   //判断简历信息是否完整
   var resumIndex = 0;
   var resumInfo = '';
   var through = true;
   $('#resumForm tr:gt(0)').each(function(index, el) {
       var $inputs = $('input', this); // 找到每一行的所有输入框 input
       // 分别取得每一个 input 的值
       var startDate = $inputs.eq(0).val();
       var endDate   = $inputs.eq(1).val();
       var workUnit  = $inputs.eq(2).val();
       var job       = $inputs.eq(3).val();
       var certifier = $inputs.eq(4).val();
       if(startDate.length > 0 && endDate.length > 0 && workUnit.length > 0 && job.length > 0 && certifier.length > 0){
           resumIndex ++;
           resumInfo += startDate + '-'+endDate+'-'+workUnit + '-'+job + '-'+certifier+',';
       }else if(startDate .length == 0 && endDate .length == 0 && workUnit .length == 0 && job .length == 0 && certifier .length == 0){

       }else{
           alert('第'+(index+1)+'条简历保存失败！开始时间、结束时间、单位、职务、证明人都不能为空！');
           through = false;
           return false;
       }
   });
    if(through == false){
        return false;
    }
   if(resumIndex < 2){
       alert('简历请填写至少两条！');
       return false;
   }
   if(resumInfo.length > 0){
       resumInfo = resumInfo.substring(0,resumInfo.length-1);
   }

    // 通过验证
    var params = {
        provinceId:provinceId,
        certType: certTypeId,
        orgId: org,
        orgName: orgName,
        localeId: localeId,
        localeSet: localeSetId,
        subjectId: subjectId,
        certBatchId:certBatchId,
        idNo: idNo,
        name: name,
        idType: idTypeId,
        email:email,
        password: password1,
        sex:genderId,
        birthday: birthday,
        eduLevelId: eduLevelId,
        degreeId: degreeId,
        nation:nationId,
        majorId: majorId,
        occupation: occupations,
        techniqueJobId: technicalJobId,
        political:politicalId,
        pthevelId: pthLevelId,
        graduateSchool: graduationCollegeId,
        graduateSchoolName: graduationCollegeName,
        learnType: learnTypeId,
        graduaTime: graduationDate,
        graduateId: graduateId,
        residence: residence,
        birthPlace: birthPlace,
        address: address,
        zipCode: zipCode,
        phone: phone,
        cellphone: cellphone,
        workUnits: workUnit,
        normalMajor: normalMajorId,
        pthCertNo: pthCertNo,
        pthOrg: pthOrg,
        genderId: genderId,
        tmpPhoto: photo,
        resumInfo: resumInfo,
        scoreCertNo: scoreCertNo,
        scoreId: scoreId
    };

    var passed = false;
    $.rest.create({url: Urls.URI_EXAM_SUBMIT, data: params, urlParams:{token: $('#token').val()}, async: false, success: function(result) {
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
    UiUtils.setFormData('sucessEmail', -1, email); // 显示邮箱在第八步上要使用
    UiUtils.setFormData('sucessIdNo', -1, idNo); // 显示邮箱在第八步上要使用

    return true;
};


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

        if (!$('#agree-checkbox').get(0).checked){
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
    $('#box-7-next').click(function() {
        UiUtils.reloadPageWhenNoAction();

        if(StepValidator.validate7thStep()){
            $('#box-7').hide();
            $('#box-8').show();
            $('.bz8').addClass('active');
        }
    });

    // 第七步的退出
    $('#box-7-exit').click(function() {
        if(confirm('您确定要 "退出" 吗？退出后所有信息都不会保存。\n点击 "确定" 直接退出，点击 "取消" 返回编辑界面')) {
            // UiUtils.closeWindow();
            location.href='http://www.jszg.edu.cn/portal/request/exit'; // 跳转到认定首页
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
 * 初始化时间选择器
 */
function initializeDatePicker() {
    // 毕业时间
    var graduationDatePicker = {
        elem: '#graduation-date',
        format: 'YYYY-MM-DD',
        istoday: true
    };
    laydate(graduationDatePicker);
}


/**
 * 请求字典数据，然后添加到 DOM 里
 */
function requestDicts() {
    // 请求字典
    $.rest.get({url: Urls.REST_DICTS, success: function(result) {
        var i = 0;
        var data = result.data;

        UiUtils.insertOptions('certTypes', data.certTypes,{templateId:'certTypeOptionTemplate'});       // 资格种类
        UiUtils.insertOptions('provinces', data.provinces, {templateId: 'provinceOptionTemplate'});   // 省
        UiUtils.insertOptions('provinces-for-college', data.provinces, {templateId: 'provinceOptionTemplate'});   // 省
        UiUtils.insertOptions('id-types', data.idType, {remainFirstOption: false, filters: ['身份证']}); // 身份证
        //UiUtils.insertOptions('id-types', data.idType, {name: '身份证'}); // 身份证
        UiUtils.insertOptions('nations', data.nation);            // 民族
        UiUtils.insertOptions('politicals', data.political);      // 政治面貌
        //UiUtils.insertOptions('edu-levels', data.eduLevel);       // 最高学位
        //UiUtils.insertOptions('degrees', data.degree);            // 最高学历
        UiUtils.insertOptions('pth-levels', data.pthLevel);       // 普通话水平
        UiUtils.insertOptions('learn-types', data.learnType);     // 学习形式
        UiUtils.insertOptions('school-types', data.schoolType);      // 最高毕业学校的学校类型
        UiUtils.insertOptions('occupations', data.occupation);    // 现从事职业
        UiUtils.insertOptions('normal-majors', data.normalMajor); // 专业类别
    }});
}

/**
 * 加载省下的毕业院校
 *
 * @param  {int} provinceId 省的 id
 */
function requestGraduationColleges(provinceId) {
    $('#graduation-colleges option:gt(0)').remove();

    if (-1 != provinceId) {
        $.rest.get({url: Urls.REST_COLLEGES_BY_PROVINCE, urlParams: {provinceId: provinceId}, success: function(result) {
            UiUtils.insertOptions('graduation-colleges', result.data);
        }});
    }
}


/**
 * 处理切换省时加载城市的事件
 * 1. 如果是直辖市，则它的城市为自己且没有 '请选择' 选项
 * 2. 如果不是直辖市，如果资格种类是高校,则显示"无需选择"实际id为省的id
 * 3. 如果所选资格种类的行政级别大于3 则显示"无需选择"实际id为省的id
 * 3. 其他加载省下的城市，且有 '请选择' 选项
 */
function handleChangeProvincesEvent() {
    $('#certTypes,#provinces').change(function() {
        UiUtils.onlyPleaseSelectOption('cities');
        var $province = $('#provinces option:selected');
        var $certType = $('#certTypes option:selected');
        var provinceId = parseInt($province.val());
        var certTypeId = parseInt($certType.val());
        var isProvinceCity = ('true' === $province.attr('data-province-city')); // 是否直辖市
        if (isProvinceCity) {
            // 直辖市的市为它自己
            var cities = [{id: provinceId, name: $province.text(), provinceCity: true}];
            //UiUtils.insertOptions('cities', cities, {templateId: 'provinceOptionTemplate'});
            UiUtils.insertOptions('cities', cities, {remainFirstOption: false});
        }else if (-1 != provinceId) {
            //如果是高校或者资格种类的行政级别大于市(3)
            if(7 == certTypeId || $certType.attr('data-admin-level') > 3){
                var cities = [{id: provinceId, name: $province.text(), provinceCity: true}];
                //UiUtils.insertOptions('cities', cities, {templateId: 'provinceOptionTemplate'});
                UiUtils.insertOptions('cities', cities, {remainFirstOption: false});
            }else{
                // provinceId 为 -1 表示选择了 "请选择"，则不加载省的城市
                $.rest.get({url: Urls.REST_CITIES_BY_PROVINCE, urlParams: {provinceId: provinceId}, success: function(result) {
                    UiUtils.insertOptions('cities', result.data);
                }});
            }

        }
    });
}

/**
 * 当资格种类,所在省,所在市变化时加载认定机构
 * 使用 certType(现任教学段)provinceId(省) 和 cityId(市) 加载注册机构
 */
function handleRequestOrgs() {
    $('#provinces, #cities').change(function(event) {
        var certTypeId = UiUtils.getFormData('#box-4', 'certType').id; // 申请资格种类
        var adminLevel = UiUtils.getFormData('#box-4', 'adminLevel').id; // 申请资格种类行政级别
        var provinceId = UiUtils.getSelectedOption('#provinces').id;
        var cityId = UiUtils.getSelectedOption('#cities').id;

        if ( -1 == provinceId || -1 === cityId) {
            return;
        }
        $.rest.get({url: Urls.REST_ORGS_REQUEST_BY_CERT_TYPE_PROVINCE_CITY, urlParams: {certTypeId: certTypeId,adminLevel: adminLevel, provinceId: provinceId,cityId: cityId},
            success: function(result) {
                console.log(result.data);
                UiUtils.insertOptions('request-orgs', result.data);
        }});
    });
}

/**
 * 第三步的任教学科
 */
function handleRequestSubjectsDialog() {
    // 初始化 LeanModal 对话框
    $('#request-subjects-dialog-trigger').leanModal({top: 50, overlay : 0.4});

     //tab切换
     $(".request_subject_tab_content").hide(); //Hide all content
     $("ul.request_subject_tabs li:first").addClass("active").show(); //Activate first tab
     $(".request_subject_tab_content:first").show(); //Show first tab content
     $("ul.request_subject_tabs li").click(function() {
         $("ul.request_subject_tabs li").removeClass("active"); //Remove any "active" class
         $(this).addClass("active"); //Add "active" class to selected tab
         $(".request_subject_tab_content").hide(); //Hide all tab content
         var activeTab = $(this).find("a").attr("href"); //Find the rel attribute value to identify the active tab + content
         $(activeTab).fadeIn(); //Fade in the active content
         return false;
     });

    // 点击搜索按钮，显示搜索的结果
    $('#request-subjects-dialog .search-button').click(function(event) {
        var searchValue = $.trim($('#request-subject-search-name').val());
        if(!searchValue){
            alert('请输入搜索内容!');
            return false;
        }
        searchValue = encodeURI(encodeURI(searchValue));
        var $certType = $('#certTypes option:selected');
        var teachGrade = $certType.attr('data-teach-grade');
        var provinceId = UiUtils.getSelectedOption('#provinces').id;
        $('#search-request-subject-result tr:gt(0)').empty();
        $.rest.get({url: Urls.REST_REQUEST_SUBJECT_BY_NAME, urlParams: {teachGrade:teachGrade, provinceId:provinceId, name: searchValue}, success: function(result) {
            $('#search-request-subject-result').append(template('requestSubjectTemplate', {requestSubjects: result.data}));
        }});
    });

    $('#select-request-subject-button').click(function(event) {
        var orgId = UiUtils.getSelectedOption('#request-orgs').id;
        var certTypeId = UiUtils.getSelectedOption('#certTypes').id;
        var $certType = $('#certTypes option:selected');
        var teachGrade = $certType.attr('data-teach-grade');
        var provinceId = UiUtils.getSelectedOption('#provinces').id;
        var cityId = UiUtils.getSelectedOption('#cities').id;

        if (-1 === certTypeId) {
            alert('请先选择 "资格种类"，然后才能选择 "任教学科"');
            return;
        }

        if (-1 === orgId) {
            alert('请先选择 "认定机构"，然后才能选择 "任教学科"');
            return;
        }

        $('#request-subjects-dialog-trigger').click(); // 显示对话框

        //默认选中所学专业的第一个tab页
        $("ul.request_subject_tabs li:first").addClass("active").show();
        $(".request_subject_tab_content:first").show();
        $(".request_subject_tab_content:last").hide();
        $('#request-subject-search-name').val('');
        $('#search-request-subject-result tr:gt(0)').empty();

        // 加载现任教学科
        UiUtils.requestDataAndShowInTree($('#request-subjects-dialog .ztree'), function(treeId, treeNode) {
            if(!treeNode) {
                return Urls.REST_REQUEST_SUBJECTS.format({provinceId: provinceId, teachGrade: teachGrade});
            } else {
                return Urls.REST_REQUEST_SUBJECTS_CHILDREN.format({provinceId: provinceId, parentId: treeNode.id});
            }
        });
    });

    // 点击确定按钮，设置选中的学科，并隐藏对话框
    $('#request-subjects-dialog .ok-button').click(function(event) {
        if( $("ul.request_subject_tabs li:first").hasClass('active')){
            var subjectNode = window.subjectsTree.getSelectedNodes()[0];
            if (subjectNode) {
                UiUtils.setFormData('request-subject-text', subjectNode.id, subjectNode.name);
                $("#lean_overlay").click();
            } else {
                alert('没有选中现任教学科');
            }
        }else{
            var $requestSubject = $('#search-request-subject-result input:radio:checked');
            if (0 === $requestSubject.length) {
                alert('请选择 "现任教学科"');
                return;
            }else{
                UiUtils.setFormData('request-subject-text', $requestSubject.val(), $requestSubject.attr('data-name'));
                $("#lean_overlay").click();
            }
        }
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

    $.rest.get({url: Urls.REST_LOCAL_SETS, urlParams: {orgId: orgId, type : 1}, async: false, success: function(result) {
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
 * 第四步的搜索
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
 * 处理切换最高学历时加载最高学位事件
 */
function handleChangeEduLevelForDegreeEvent() {
    $('#edu-levels').change(function() {
        $('#degrees').empty();
        var box7 = '#box-7';
        var eduLevelId = parseInt($('#edu-levels option:selected').val());
        var certTypeId = UiUtils.getFormData(box7, 'certType').id;
        if (-1 != eduLevelId && -1 !=certTypeId) {
            //根据选择的资格种类,第七步的最高学历给最高学位赋值
            $.rest.get({url: Urls.REST_DEGREE_BY_CERT_TYPE_AND_EDU_LEVEL, urlParams: {certTypeId:certTypeId,eduLevelId:eduLevelId}, async: false, success: function(result) {
                if (result.success) {
                    UiUtils.insertOptions('degrees', result.data); // 最高学位
                }
            }});
        }
    });
}

/**
 * 最高学历毕业学校9
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

        var validateCollege = false;
        $.rest.get({url: Urls.REST_COLLEGE_BY_NAME, urlParams: {name: encodeURI(encodeURI(collegeName))}, async: false, success: function(result) {
            if (result.data.length != 0) {
                 validateCollege=true;
            }
        }});
        if(validateCollege){
            alert('学校名称已经存在!');
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
 * 所学专业
 *
 */
function handleMajorsDialog() {
    // 初始化 LeanModal 对话框
    $('#majors-dialog-trigger').leanModal({top: 50, overlay : 0.4});

    //tab切换
     $(".major_tab_content").hide(); //Hide all content
     $("ul.major_tabs li:first").addClass("active").show(); //Activate first tab
     $(".major_tab_content:first").show(); //Show first tab content
     $("ul.major_tabs li").click(function() {
         $("ul.major_tabs li").removeClass("active"); //Remove any "active" class
         $(this).addClass("active"); //Add "active" class to selected tab
         $(".major_tab_content").hide(); //Hide all tab content
         var activeTab = $(this).find("a").attr("href"); //Find the rel attribute value to identify the active tab + content
         $(activeTab).fadeIn(); //Fade in the active content
         return false;
     });

    // 点击搜索按钮，显示搜索的结果
    $('#majors-dialog .search-button').click(function(event) {
        var searchValue = $.trim($('#major-search-name').val());
        var provinceId = parseInt($('#provinces option:selected').val());
        if(!searchValue){
            alert('请输入搜索内容!');
            return false;
        }
        searchValue = encodeURI(encodeURI(searchValue));
        var box7 = '#box-7';
        var eduLevelId = parseInt($('#edu-levels option:selected').val());
        var certTypeId = UiUtils.getFormData(box7, 'certType').id;
        $('#search-major-result tr:gt(0)').empty();
        $.rest.get({url: Urls.REST_MAJOR_SEARCH_BY_NAME_REQUEST, urlParams: {provinceId:provinceId,name: searchValue,certTypeId:certTypeId,eduLevelId:eduLevelId}, success: function(result) {
            $('#search-major-result').append(template('majorsTemplate', {majors: result.data}));
        }});
    });

    // 点击确定按钮，设置选中的学科，并隐藏对话框
    $('#majors-dialog .ok-button').click(function(event) {
        if( $("ul.major_tabs li:first").hasClass('active')){
            var subjectNode = window.subjectsTree.getSelectedNodes()[0];
            if (subjectNode) {
                UiUtils.setFormData('major', subjectNode.id, subjectNode.name);
                $("#lean_overlay").click();
            } else {
                alert('没有选中任教学科');
            }
        }else{
            var $major = $('#search-major-result input:radio:checked');
            if (0 === $major.length) {
                alert('请选择 "所学专业"');
                return;
            }else{
                UiUtils.setFormData('major', $major.val(), $major.attr('data-name'));
                $("#lean_overlay").click();
            }
        }
    });

    $('#select-major-button').click(function(event) {
        var eduLevelId = UiUtils.getSelectedOption('#edu-levels').id;

        //默认选中所学专业的第一个tab页
        $("ul.major_tabs li:first").addClass("active").show();
        $(".major_tab_content:first").show();
        $(".major_tab_content:last").hide();
        $('#major-search-name').val('');
        $('#search-major-result tr:gt(0)').empty();

        var provinceId = parseInt($('#provinces option:selected').val());
        if(-1 == provinceId){
            alert('请先选择省份!');
            return false;
        }

        $('#majors-dialog-trigger').click(); // 显示对话框
        var box7 = '#box-7';
        var eduLevelId = parseInt($('#edu-levels option:selected').val());
        var certTypeId = UiUtils.getFormData(box7, 'certType').id;
        // 加载最高学历所学专业
        UiUtils.requestDataAndShowInTree($('#majors-dialog .ztree'), function(treeId, treeNode) {
            if(!treeNode) {
                return Urls.REST_REQUEST_MAJOR_PARENT.format({provinceId:provinceId,certTypeId:certTypeId,eduLevelId:eduLevelId});
            } else {
                return Urls.REST_REQUEST_MAJOR_CHILDREN.format({provinceId:provinceId,parentId: treeNode.id});
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

        //tab切换
         $(".technical_jobs_tab_content").hide(); //Hide all content
         $("ul.technical-jobs_tabs li:first").addClass("active").show(); //Activate first tab
         $(".technical_jobs_tab_content:first").show(); //Show first tab content
         $("ul.technical-jobs_tabs li").click(function() {
             $("ul.technical-jobs_tabs li").removeClass("active"); //Remove any "active" class
             $(this).addClass("active"); //Add "active" class to selected tab
             $(".technical_jobs_tab_content").hide(); //Hide all tab content
             var activeTab = $(this).find("a").attr("href"); //Find the rel attribute value to identify the active tab + content
             $(activeTab).fadeIn(); //Fade in the active content
             return false;
         });

        // 点击搜索按钮，显示搜索的结果
        $('#technical-jobs-dialog .search-button').click(function(event) {
            var searchValue = $.trim($('#technical-jobs-search-name').val());
            if(!searchValue){
                alert('请输入搜索内容!');
                return false;
            }
            searchValue = encodeURI(encodeURI(searchValue));
            $('#search-technical-jobs-result tr:gt(0)').empty();
            $.rest.get({url: Urls.REST_TECHNICAL_JOB_BY_NAME, urlParams: {name: searchValue}, success: function(result) {
                $('#search-technical-jobs-result').append(template('technicalJobsTemplate', {technicalJobs: result.data}));
            }});
        });

    // 点击确定按钮，设置选中的学科，并隐藏对话框
    $('#technical-jobs-dialog .ok-button').click(function(event) {
        if( $("ul.technical-jobs_tabs li:first").hasClass('active')){
            var technicalJobNode = window.subjectsTree.getSelectedNodes()[0];
                if (technicalJobNode) {
                    if (0 === technicalJobNode.level && '00' != technicalJobNode.code) {
                        alert('双击职务名称或单击前面的 “+” 图标可以展开更具体的职务！');
                        return;
                    }

                    UiUtils.setFormData('technicalJob', technicalJobNode.id, technicalJobNode.name);
                    $("#lean_overlay").click();
                } else {
                    alert('请选择具体的教师职务');
                }
        }else{
            var $technicalJob = $('#search-technical-jobs-result input:radio:checked');
            if (0 === $technicalJob.length) {
                alert('请选择 "最高学历所学专业"');
                return;
            }else{
                UiUtils.setFormData('technicalJob', $technicalJob.val(), $technicalJob.attr('data-name'));
                $("#lean_overlay").click();
            }
        }

    });

    $('#select-technical-job-button').click(function(event) {
        $('#technical-jobs-dialog-trigger').click(); // 显示对话框

         //默认选中所学专业的第一个tab页
        $("ul.technical-jobs_tabs li:first").addClass("active").show();
        $(".technical_jobs_tab_content:first").show();
        $(".technical_jobs_tab_content:last").hide();
        $('#technical-jobs-search-name').val('');
        $('#search-technical-jobs-result tr:gt(0)').empty();

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
