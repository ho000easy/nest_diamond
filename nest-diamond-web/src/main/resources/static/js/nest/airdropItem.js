
$(document).ready(function () {
    $('select').selectpicker();
    let columnDefs = [{
        'targets': 13,
        'render': function (data, type, full, meta) {
            if(full.ipProxyHost){
                return `${full.ipProxyHost}:${full.ipProxyPort}:${full.ipProxyUserName}:${full.ipProxyPassword}`
            }
            return ''
        }
    },{
        'targets': 13,
        'className': 'link-primary proxy-link',
        "render": function (data, type, row) {
            return data
        }
    }];

    let airdropItemListTable = dataTable('airdropItemList', '/airdropItem/search',
        ['id', 'accountAddress', 'sequence', 'seedPrefix', 'hdIndex',
            'exchangeName', 'exchangeUserName', 'exchangeAccountSequence', 'chainName', 'tokenName', 'depositAddress',
            'ipProxyVendor', 'ipProxyOrder', null,'emailCatalogName','emailAlias', 'emailOrder', 'adsUserName', 'adsBrowserSequence', 'cairoVersion',
            'discordName', 'discordSequence', 'twitterName', 'twitterSequence', 'cloudServerIp', 'exchangeMetaName','exchangeMetaPwd','galaxyId','createTime','modifyTime'], params, null, columnDefs)

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
