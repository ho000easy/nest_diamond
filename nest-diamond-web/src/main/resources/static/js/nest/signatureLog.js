// static/js/nest/signatureLog.js
$(document).ready(function () {
    $('select').selectpicker();

    let columns = [
        'id', 'ticketNo', 'airdropOperationName', 'signAddress', 'bizOrderNo', 'rawData', 'signedData',
        'contractName', 'chainName',
        'signType', 'signTime', 'createTime'
    ];
    let columnDef = [
        {
            targets: 5, // 假设是第4列
            render: function (data, type, row) {
                return DtRenderUtil.renderCode(data, type, { isJson: true });
            }
        },
        {
            targets: 6, // 假设是第4列
            render: function (data, type, row) {
                const safeData = String(data).replace(/"/g, '&quot;');
                return `
                <span class="dt-text-truncate" 
                      data-bs-toggle="tooltip" 
                      data-bs-placement="top" 
                      title="${safeData}">
                    ${data}
                </span>
                `
            }
        }
    ]

    let signatureLogListTable = multiSelectDataTable('signatureLogList', '/signatureLog/search',
        columns, params, null, columnDef);

    function params() {
        return $("#addForm").serialize();
    }

    $("#search").click(() => signatureLogListTable.ajax.reload());

    $("#delete").click(function () {
        if (!checkSelectedIds(signatureLogListTable)) return;
        postJson('/signatureLog/delete', JSON.stringify(getSelectedIds(signatureLogListTable)), function (resp) {
            processResp(resp, '删除成功', () => signatureLogListTable.ajax.reload());
        });
    });
});