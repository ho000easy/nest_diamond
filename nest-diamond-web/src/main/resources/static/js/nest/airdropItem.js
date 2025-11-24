
$(document).ready(function () {
    $('select').selectpicker();
    let columnDefs = [];

    let airdropItemListTable = dataTable('airdropItemList', '/airdropItem/search',
        ['id', 'accountAddress', 'sequence', 'seedPrefix', 'hdIndex', 'createTime','modifyTime'], params, null, columnDefs)

    function params() {
        return $('#searchForm').serialize()
    }

    $('#airdropItemList tbody').on('click', 'tr .link-primary.proxy-link', function () {
        let ipProxyId = airdropItemListTable.row(this).data()['ipProxyId']
        window.open(`ipProxy?idListStr=${ipProxyId}`);
    });

    $("#search").click(function (event) {
        airdropItemListTable.ajax.reload(null, false);
        fillTextField('/airdropItem/search', 'ipProxyConnectText');
        twitterFillTextField('/airdropItem/search', 'twitterConnectText');
    });

    function fillTextField(url, id) {
        postForm(url, $("#searchForm").serialize(), function (resp) {
            let valueList = '';
            resp.data.forEach(
                function (element, index, array) {
                    valueList = `${valueList}\r\n${element.ipProxyHost}`
                }
            )
            $(`#${id}`).text(valueList.substring(2))
        })
    }

    function twitterFillTextField(url, id) {
        postForm(url, $("#searchForm").serialize(), function (resp) {
            let valueList = '';
            resp.data.forEach(
                function (element, index, array) {
                    valueList = `${valueList}\r\n${element.accountAddress}`
                }
            )
            $(`#${id}`).text(valueList.substring(2))
        })
    }

})
