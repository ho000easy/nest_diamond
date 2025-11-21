// static/js/nest/workOrder.js
$(document).ready(function () {
    let columnDefs = [

        {
            targets: 5,
            className: 'seq-col', // 让这一列可以换行
            render: function (data, type, row) {
                if (!row.startTime || !row.endTime) return '-';
                let start = row.startTime.substring(0, 10);
                let end = row.endTime.substring(0, 10);
                let days = Math.ceil((new Date(end) - new Date(start)) / 86400000) + 1;
                return start + ' 至 ' + end + '（共' + days + '天）';
            }
        },
        {
            targets: 8,  // 状态列索引
            className: 'text-center',
            render: function (data, type, row) {
                let badge = '';
                switch (row.status) {
                    case 'PENDING':
                        badge = '<span class="badge bg-warning text-dark">待审批</span>';
                        break;
                    case 'APPROVED':
                        badge = '<span class="badge bg-success">通过</span>';
                        break;
                    case 'REJECTED':
                        badge = '<span class="badge bg-danger">拒绝</span>';
                        break;
                    default:
                        badge = '<span class="badge bg-secondary">' + row.status + '</span>';
                }
                return badge;
            }
        }
    ]
    let table = multiSelectDataTable('workOrderTable', '/workOrder/search',
        ['id', 'workOrderNo', 'name', 'airdropOperationName', 'airdropName',
            null, 'isAllowTransfer', 'isAllowDeployContract', 'status', 'applicant', 'applyTime', 'createTime', 'modifyTime'],
        params, null, columnDefs);

    function params() {
        return $("#searchForm").serialize();
    }

    $("#searchBtn").click(() => table.ajax.reload());
    $("#createBtn").click(() => $("#createModal").modal('show'));

    $("#saveBtn").click(function () {
        postForm('/workOrder/create', $("#createForm").serialize(), function (resp) {
            processResp(resp, '创建成功', function () {
                $("#createModal").modal('hide');
                table.ajax.reload();
            });
        });
    });

    $("#approveBtn").click(function () {
        let ids = getSelectedIds(table);
        if (ids.length === 0) return alert("请选择工单");
        postJson('/workOrder/approve', JSON.stringify(ids), function (resp) {
            processResp(resp, '审批通过', () => table.ajax.reload());
        });
    });

    $("#rejectBtn").click(function () {
        let ids = getSelectedIds(table);
        if (ids.length === 0) return alert("请选择工单");
        postJson('/workOrder/reject', JSON.stringify(ids), function (resp) {
            processResp(resp, '已拒绝', () => table.ajax.reload());
        });
    });
});