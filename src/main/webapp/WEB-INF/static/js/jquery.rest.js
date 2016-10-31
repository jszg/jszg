'use strict';

(function($) {
    /**
     * 执行 REST 请求的 jQuery 插件:
     *      Get    请求调用 $.rest.get()
     *      Create 请求调用 $.rest.create()
     *      Update 请求调用 $.rest.update()
     *      Delete 请求调用 $.rest.remove()
     *
     * 调用例子:
     *      $.rest.get({url: '/rest', data: {name: 'Alice'}, success: function(result) {
     *          console.log(result);
     *      }}, error: function(errorResponse) {});
     */
    $.rest = {
        /**
         * 使用 Ajax 的方式执行 REST 的 GET 请求(服务器响应的数据根据 REST 的规范，应该是 Json 对象).
         * 以下几个 REST 的函数 $.rest.create(), $.rest.update(), $.rest.remove() 只是请求的 HTTP 方法和 data 处理不一样，其他的都是相同的.
         *
         * @param {json} options 有以下几个选项:
         *        {string}   url       请求的 URL(必选)
         *        {json}     urlParams URL 中的变量(可选)，例如 /rest/users/{id}，其中 {id} 为要被 urlParams.id 替换的部分
         *        {json}     data      请求的参数(可选)
         *        {boolean}  async      默认为异步方式(可选)
         *        {function} success   请求成功时的回调函数(可选)
         *        {function} error     请求失败时的回调函数(可选)
         *        {function} complete  请求完成后的回调函数(可选)
         * @return 没有返回值
         */
        get: function(options) {
            options.httpMethod = 'GET';
            this.sendRequest(options);
        },
        create: function(options) {
            options.httpMethod = 'POST';
            options.data = options.data ? JSON.stringify(options.data) : {};
            this.sendRequest(options);
        },
        update: function(options) {
            options.httpMethod = 'PUT';
            options.data = options.data ? JSON.stringify(options.data) : {};
            this.sendRequest(options);
        },
        remove: function(options) {
            options.httpMethod = 'DELETE';
            options.data = options.data ? JSON.stringify(options.data) : {};
            this.sendRequest(options);
        },
        /**
         * 执行 Ajax 请求，不推荐直接调用这个方法.
         * 因为发送给 SpringMVC 的 Ajax 请求中必须满足下面的条件才能执行成功:
         *     1. dataType: 'json'
         *     2. contentType: 'application/json'
         *     3. data: GET 时为 json 对象，POST, PUT, DELETE 时必须为 JSON.stringify(data)
         *
         * @param {json} options 有以下几个选项:
         *        {string}   url        请求的 URL(必选)
         *        {string}   httpMethod 请求的方式，有 GET, PUT, POST, DELETE(必选)
         *        {json}     urlParams  URL 中的变量(可选)
         *        {json}     data       请求的参数(可选)
         *        {boolean}  async      默认为异步方式(可选)
         *        {function} success    请求成功时的回调函数(可选)
         *        {function} error      请求失败时的回调函数(可选)
         *        {function} complete   请求完成后的回调函数(可选)
         */
        sendRequest: function(options) {
            var defaults = {
                data: {},
                async: true,
                success: function() {},
                error: function() {},
                complete: function() {}
            };

            var settings = $.extend({}, defaults, options);

            // 替换路径中的变量，例如 /rest/users/{id}, 其中 {id} 为要被 settings.urlParams.id 替换的部分
            if (settings.urlParams) {
                settings.url = settings.url.replace(/\{\{|\}\}|\{(\w+)\}/g, function(m, n) {
                    if (m == '{{') { return '{'; }
                    if (m == '}}') { return '}'; }
                    return settings.urlParams[n];
                });
            }

            $.ajax({
                url:  settings.url,
                data: settings.data,
                async: settings.async,
                type: settings.httpMethod,
                dataType: 'json',
                contentType: 'application/json;charset=utf-8'
            })
            .done(function(data, textStatus, jqXHR) {
                settings.success(data, textStatus, jqXHR);
            })
            .fail(function(jqXHR, textStatus, errorThrown) {
                // data|jqXHR, textStatus, jqXHR|errorThrown
                settings.error(jqXHR, textStatus, errorThrown);
            })
            .always(function() {
                settings.complete();
            });
        }
    };
})(jQuery);
