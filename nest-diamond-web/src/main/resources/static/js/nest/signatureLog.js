// static/js/nest/signatureLog.js
$(document).ready(function () {
    $('select').selectpicker();

    let columns = [
        'id', 'address', 'bizOrderNo', 'airdropOperationName',
        'contractAddress', 'chainName',
        'signType', 'signTime', 'createTime'
    ];

    let signatureLogListTable = multiSelectDataTable('signatureLogList', '/signatureLog/search',
        columns, params, null, null);

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