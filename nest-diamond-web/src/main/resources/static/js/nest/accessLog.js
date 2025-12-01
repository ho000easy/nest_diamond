$(document).ready(function () {
    // 虽然页面没有 select，但保留初始化以防未来添加
    $('select').selectpicker();

    // 对应 AccessLog 实体类的字段名
    let columns = [
        'id',
        'module',
        'methodName',
        'ipAddress',
        'executionTime',
        'requestParams',
        'responseData',
        'createTime',
        'modifyTime',
    ];

    let columnDef = [
        {
            targets: 5, // 耗时列
            render: function (data, type, row) {
                return data + ' ms';
            }
        },
        {
            targets: 6, // 请求参数
            render: function (data, type, row) {
                if (!data) return '-';
                // 使用 DtRenderUtil 渲染 JSON 代码块样式，或者使用 Tooltip
                // 这里参考你的 signatureLog 里的 tooltip 样式，适合长文本
                const safeData = String(data).replace(/"/g, '&quot;');
                return `
                <span class="dt-text-truncate" 
                      style="max-width: 200px; display: inline-block; white-space: nowrap; overflow: hidden; text-overflow: ellipsis;"
                      data-bs-toggle="tooltip" 
                      data-bs-placement="top" 
                      title="${safeData}">
                    ${data}
                </span>
                `;
            }
        },
        {
            targets: 7, // 返回结果
            render: function (data, type, row) {
                if (!data) return '-';
                const safeData = String(data).replace(/"/g, '&quot;');
                return `
                <span class="dt-text-truncate" 
                      style="max-width: 200px; display: inline-block; white-space: nowrap; overflow: hidden; text-overflow: ellipsis;"
                      data-bs-toggle="tooltip" 
                      data-bs-placement="top" 
                      title="${safeData}">
                    ${data}
                </span>
                `;
            }
        }
    ];

    // 初始化 DataTable
    // 假设后端接口地址为 /sys/log/list (对应之前 AccessLogController)
    let accessLogListTable = multiSelectDataTable('accessLogList', '/accessLog/search',
        columns, params, null, columnDef);

    function params() {
        return $("#addForm").serialize();
    }

    $("#search").click(() => accessLogListTable.ajax.reload());

    // 如果你需要删除功能，取消注释以下代码
    /*
    $("#delete").click(function () {
        if (!checkSelectedIds(accessLogListTable)) return;
        postJson('/sys/log/delete', JSON.stringify(getSelectedIds(accessLogListTable)), function (resp) {
            processResp(resp, '删除成功', () => accessLogListTable.ajax.reload());
        });
    });
    */

    // 初始化 Tooltip (Bootstrap 5 需要手动初始化)
    $('body').tooltip({
        selector: '[data-bs-toggle="tooltip"]'
    });
});