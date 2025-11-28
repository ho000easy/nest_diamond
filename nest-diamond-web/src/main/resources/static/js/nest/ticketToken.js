$(document).ready(function () {

    $('select').selectpicker();

    // 表格列定义
    const columns = [
        'id',           // 0: checkbox
        'id',           // 1
        'ticketNo',     // 2
        'token',        // 3
        'status',       // 4
        'expireTime',   // 5
        'useTime',      // 6
        'createTime',    // 7
        'modifyTime'    // 7
    ];

    const columnDefs = [
        {
            targets: 4, // 状态列
            className: 'text-center',
            render: function (data, type, row) {
                let color = 'secondary';
                let text = data;
                if (data === 'UNUSED') { color = 'success'; text = '未使用'; }
                else if (data === 'USED') { color = 'primary'; text = '已使用'; }
                else if (data === 'EXPIRED') { color = 'danger'; text = '已失效'; }
                return `<span class="badge bg-${color}">${text}</span>`;
            }
        }
    ];

    // 初始化表格
    let tokenTable = multiSelectDataTable('tokenTable', '/ticketToken/search', columns, params, null, columnDefs);

    function params() {
        return $("#searchForm").serialize();
    }

    // 按钮绑定
    $("#searchBtn").click(() => tokenTable.ajax.reload(null, false));

    $("#createBtn").click(() => {
        $("#createForm")[0].reset();
        $('.selectpicker').selectpicker('refresh');
        $("#createModal").modal('show');
    });

    // 确认生成
    $("#confirmGenerateBtn").click(function () {
        postForm('/ticketToken/generate', $("#createForm").serialize(), function (resp) {
            processResp(resp, 'Token 生成成功', function () {
                $("#createModal").modal('hide');
                tokenTable.ajax.reload();

                // 自动复制到剪贴板（可选优化）
                if(resp.data) {
                    navigator.clipboard.writeText(resp.data);
                    toastr.info("Token 已复制到剪贴板: " + resp.data);
                }
            });
        });
    });

    // 批量作废
    $("#invalidateBtn").click(function () {
        if (!checkSelectedIds(tokenTable)) return;
        const ids = getSelectedIds(tokenTable);

        if(confirm("确定要作废选中的 Token 吗？")) {
            postJson('/ticketToken/invalidate', JSON.stringify(ids), function (resp) {
                processResp(resp, '操作成功', () => tokenTable.ajax.reload(null, false));
            });
        }
    });

    $("#deleteBtn").click(function () {
        postSelectedIds('/ticketToken/delete', '删除成功', tokenTable)
    });


});