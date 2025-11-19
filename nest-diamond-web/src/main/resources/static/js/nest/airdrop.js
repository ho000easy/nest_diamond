
$(document).ready(function () {
    $('select').selectpicker();

    let columnDefs = [{
        'targets': 1,
        'render': function (data, type, full, meta) {
            return multiIconColumn(data, ['edit', 'link'])
        }},
        {
            'targets': 2,
            'render': function (data, type, full, meta) {
                return data;
            }
        }
    ];

    let airdropTable = multiSelectDataTable('airdropList', '/airdrop/search',
        ['id', 'name', 'remark', 'createTime'], params, null, columnDefs)


    function params() {
        return {
            "name": $("#airdropName").val()
        }
    }

    $('#airdropList tbody').on('click', 'td img.edit', function () {
        let rowData = airdropTable.row($(this).closest('tr')).data()
        showEditModal($("#editModal"), 'editForm', rowData)
    });

    $('#airdropList tbody').on('click', 'td img.link', function () {
        let airdropId = airdropTable.row($(this).closest('tr')).data()['id']
        window.open(`airdropItem?airdropId=${airdropId}`);
    });

    $("#search").click(function (event) {
        airdropTable.ajax.reload(null, false);
    });

    $('#add').click(function () {
        postJson('/airdrop/add', $("#addForm").serializeJSON(), function (resp) {
            processResp(resp, '添加成功', function () {
                airdropTable.ajax.reload(null, false);
            })
        })
    })

    $('#update').click(function () {
        postJson('/airdrop/update', $("#editForm").serializeJSON(), function (resp) {
            processResp(resp, '更新成功', function () {
                airdropTable.ajax.reload(null, false);
            })
        })
    })

    $('#append').click(function () {
        postJson('/airdrop/append', $("#addForm").serializeJSON(), function (resp) {
            processResp(resp, '追加成功', function () {
                airdropTable.ajax.reload(null, false);
            })
        })

    })

    $('#delete').click(function () {
        if (!checkSelectedIds(airdropTable)) {
            return;
        }
        let deleteModal = $('#deleteModal')
        let airdropIds = getSelectedIds(airdropTable)
        deleteModal.data("airdropIds", airdropIds)
        deleteModal.modal('show')


    })

    $('#confirmDeleteBtn').click(function () {
        let deleteModal = $('#deleteModal')

        let airdropIds = deleteModal.data("airdropIds")
        postJson('/airdrop/delete', JSON.stringify(airdropIds), function (resp) {
            processResp(resp, '删除成功', function () {
                airdropTable.ajax.reload(null, false);
            })
        })
        deleteModal.modal('hide')
    })


})