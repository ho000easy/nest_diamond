$(document).ready(function () {
    let columnDefs = [
        {
            targets: 1,
            render: function (data, type, full, meta) {
                return editColumn(data)
            }
        },
        {
            targets: 4,
            className: 'seq-col', // 让这一列可以换行
            render: function (data, type, row) {
                return linkColumn(data)
            }
        },
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
            targets: 10,  // 状态列索引
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
        },
        {
            targets: 15,
            className: 'text-center',
            render: function (data, type, row) {
                return `
            <div class="d-flex flex-column align-items-center gap-1">
                <a href="/contractInstanceSnapshot?ticketNo=${row.ticketNo}" target="_blank" class="link-primary">合约快照</a>
                <a href="/signatureLog?ticketNo=${row.ticketNo}" target="_blank" class="link-primary">签名记录</a>
            </div>
        `;
            }
        }
    ]
    let table = multiSelectDataTable('ticketTable', '/ticket/search',
        ['id', 'ticketNo', 'name', 'airdropOperationName', 'airdropName',
            null, 'isAllowTransfer', 'isAllowDeployContract', 'isRequireContractCheck', 'isRequireContractFunctionCheck', 'status', 'applicant', 'applyTime', 'createTime', 'modifyTime', null],
        params, null, columnDefs);

    function params() {
        return $("#searchForm").serialize();
    }

    $("#searchBtn").click(() => table.ajax.reload());
    $("#createBtn").click(() => $("#createModal").modal('show'));

    $('#ticketTable tbody').on('click', 'td img.link', function () {
        let airdropId = table.row($(this).closest('tr')).data().airdropId
        window.open(`airdropItem?airdropId=${airdropId}`);
    });

    $('#ticketTable tbody').on('click', 'td img.edit', function () {
        let rowData = table.row($(this).closest('tr')).data()
        showEditModal($("#editModal"), 'editForm', rowData)
    });

    $('#update').click(function () {
        postForm('/ticket/update', $("#editForm").serialize(), function (resp) {
            processResp(resp, '更新成功', function () {
                table.ajax.reload(null, false);
            })
        })

    })

    $("#saveBtn").click(function () {
        postForm('/ticket/create', $("#createForm").serialize(), function (resp) {
            processResp(resp, '创建成功', function () {
                $("#createModal").modal('hide');
                table.ajax.reload();
            });
        });
    });

    $("#approveBtn").click(function () {
        let ids = getSelectedIds(table);
        if (ids.length === 0) return alert("请选择工单");
        postJson('/ticket/approve', JSON.stringify(ids), function (resp) {
            processResp(resp, '审批通过', () => table.ajax.reload());
        });
    });

    $("#rejectBtn").click(function () {
        let ids = getSelectedIds(table);
        if (ids.length === 0) return alert("请选择工单");
        postJson('/ticket/reject', JSON.stringify(ids), function (resp) {
            processResp(resp, '已拒绝', () => table.ajax.reload());
        });
    });

    $('#deleteBtn').click(function () {
        if (!checkSelectedIds(table)) {
            return;
        }
        postJson('/ticket/delete', JSON.stringify(getSelectedIds(table)), function (resp) {
            processResp(resp, '删除成功', function () {
                table.ajax.reload(null, false);
            })
        })

    })
});