$(document).ready(function () {
    $('select').selectpicker();
    exchangeSelected();
    $('#exchangeId').change(exchangeSelected)
    let accountListTable = multiSelectDataTable('accountList', '/account/search',
        ['id', 'seedPrefix', 'address', 'hdIndex',
            'createTime', 'modifyTime'], accountParams)

    function accountParams() {
        return $('#adsForm').serialize()
    }

    $("#search").click(function (event) {
        accountListTable.ajax.reload(null, false);
    })

    $('#exchangeId').change(function () {
        let exchangeId = $('#exchangeId').val()
        postForm('/exchange/account/userDistinctQuery', {exchangeId:exchangeId},function (resp) {
            selectWithNullChange($('#exchangeUserName'), resp.data, 'exchangeUserName', 'exchangeUserName')
        })
    })

    $("#adsSet").click(function (event) {
        postForm('/account/ads/set', $('#adsForm').serialize(), function (resp) {
            processResp(resp, '设置成功', function () {
                accountListTable.ajax.reload(null, false);
            })
        })
    })

    $("#attachExchange").click(function (event) {
        postForm('/account/exchange/attach', $('#adsForm').serialize(), function (resp) {
            processResp(resp, '关联交易所成功', function () {
                accountListTable.ajax.reload(null, false);
            })
        })
    })

    $('#resetAds').click(function () {
        if (!checkSelectedIds(accountListTable)) {
            return;
        }
        postJson('/account/resetAds', JSON.stringify(getSelectedIds(accountListTable)), function (resp) {
            processResp(resp, '重置成功', function () {
                accountListTable.ajax.reload(null, false);
            })
        })

    })

    $('#resetExchange').click(function () {
        if (!checkSelectedIds(accountListTable)) {
            return;
        }
        postJson('/account/resetExchange', JSON.stringify(getSelectedIds(accountListTable)), function (resp) {
            processResp(resp, '重置成功', function () {
                accountListTable.ajax.reload(null, false);
            })
        })

    })

    function exchangeSelected() {
        let exchangeId = $('#exchangeId').val()
        postForm('/exchangeUser/search', {exchangeId: exchangeId,isMain:true}, function (resp) {
            if (resp.isSuccess) {
                selectWithNullChange($('#exchangeUserId'), resp.data, 'name', 'id')
            }

        })
    }
})
