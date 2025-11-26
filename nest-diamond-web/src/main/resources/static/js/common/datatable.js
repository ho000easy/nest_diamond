function multiSelectDataTable(tableId, url, columns, paramsFunc,
                              rowCallBackFunc, columnDefs, footerCallbackFuc,
                              isJson, extraButtons , disableOrdering = false) {
    let innerColumnDefs = [{
        'targets': 0,
        'searchable': false,
        'orderable': true,
        'className': 'dt-body-center',
        'render': function (data, type, full, meta) {
            return '<input type="checkbox" name="id[]" value="' + $('<div/>').text(data).html() + '">' + data;
        }
    }]
    if(columnDefs){
        innerColumnDefs = innerColumnDefs.concat(columnDefs)
    }
    let ajax = {
        url: url,
        type: 'post',
        data: paramsFunc,
        dataSrc: function (json) {
            if (json.message) {
                $.toast(failToast(json.message, 3000))
                return [];
            } else {
                // 正常返回数据数组
                return json.data; // 假设正常响应为 { data: [...] }
            }
        },
        error: function (xhr, error, thrown) {
            $.toast(failToast(thrown))
        }
    }
    if (isJson) {
        ajax['contentType'] = "application/json; charset=utf-8"
    }
    let baseButtons = [
        {
            text: '⬜️ 选中当前页',
            className: 'btn btn-outline-primary btn-sm btn-toggle-current',
            action: function (e, dt, node) {
                toggleCurrentPage(dt);
                updateToggleBtn(dt, $(this.node()));
            },
            init: function (dt, node) {
                updateToggleBtn(dt, $(this.node()));
            }
        }
    ];

    // ★ 核心：把额外按钮拼接到默认按钮后面
    const allButtons = extraButtons && Array.isArray(extraButtons)
        ? baseButtons.concat(extraButtons)
        : baseButtons;
    let table = $(`#${tableId}`).DataTable({
        ajax: ajax,
        searching: false,
        processing: true,
        ordering: !disableOrdering,   // ✅ 动态控制排序开关
        // order: [],
        columnDefs: innerColumnDefs,
        rowCallback: rowCallBackFunc,
        footerCallback: footerCallbackFuc,
        columns: convertColumns(columns),
        dom: '<"top d-flex align-items-center gap-2"lBf>rt<"bottom"ip><"clear">',
        buttons: allButtons
    });


    function toggleCurrentPage(dt) {
        const rows = dt.rows({ page: 'current' }).nodes();
        const $cbs = $('input[type="checkbox"]', rows);
        const allChecked = $cbs.length > 0 && $cbs.filter(':checked').length === $cbs.length;
        $cbs.prop('checked', !allChecked);
    }

    // ★ 这里接收“按钮的 jQuery 对象”，不再用 buttonApi.node()
    function updateToggleBtn(dt, $btn) {
        const rows = dt.rows({ page: 'current' }).nodes();
        const $cbs = $('input[type="checkbox"]', rows);
        const any = $cbs.length > 0;
        const checked = any && $cbs.filter(':checked').length === $cbs.length;

        if (checked) {
            $btn.removeClass('btn-outline-primary').addClass('btn-primary')
                .html('✅ 取消当前页');
        } else {
            $btn.removeClass('btn-primary').addClass('btn-outline-primary')
                .html('⬜️ 选中当前页');
        }
    }

    // 翻页/筛选/排序后刷新按钮状态
    table.on('draw.dt', function () {
        const $btn = $(table.buttons().container()).find('.btn-toggle-current'); // ★ 用类名找按钮
        updateToggleBtn(table, $btn);
    });

    // 头部复选框
    $('#headerSelectAll').on('click', function () {
        const rows = table.rows({ page: 'current' }).nodes();
        $('input[type="checkbox"]', rows).prop('checked', this.checked);
        const $btn = $(table.buttons().container()).find('.btn-toggle-current');
        updateToggleBtn(table, $btn);
    });

    // 行内复选框改变时同步按钮
    table.on('change', 'tbody input[type="checkbox"]', function () {
        const $btn = $(table.buttons().container()).find('.btn-toggle-current');
        updateToggleBtn(table, $btn);
    });

    table.on('draw', function() {
        $('.copy-btn').each(function() {
            let copyBtn = $(this);
            // 获取按钮所在的当前单元格的文本内容
            let cellText = copyBtn.closest('td').text().trim();  // 获取当前按钮所在的单元格的内容

            // 设置data-clipboard-text为当前单元格的文本
            copyBtn.attr('data-clipboard-text', cellText);
        })
    })

    table.on('draw.dt', function () {
        var info = table.page.info();
        var rowsOnPage = table.rows({ page: 'current' }).count();

        if (info.pages > 0 && rowsOnPage === 0 && info.page > 0) {
            table.page(info.page - 1).draw(false); // 回上一页但不重置分页
        }
    });


    $('#' + tableId + ' .example-select-all').on('click', function () {
        let rows = table.rows({'search': 'applied'}).nodes();
        $('input[type="checkbox"]', rows).prop('checked', this.checked);
    })

    $(`#${tableId} tbody`).on('change', 'input[type="checkbox"]', function () {
        if (!this.checked) {
            let el = $('#example-select-all').get(0);
            if (el && el.checked && ('indeterminate' in el)) {
                el.indeterminate = true;
            }
        }
    })

    $(`#${tableId} tbody`).on('dblclick', 'td', function() {
        let data = $(this).text().trim();
        copyTextToClipboardFallback(data)
        $.toast(successToast('复制成功'))
    })

    return table;
}

function dataTable(tableId, url, columns, queryParams, rowCallBackFunc, columnDefs) {
    let table = $(`#${tableId}`).DataTable({
        ajax: {
            url: url,
            type: 'post',
            data: queryParams,
            dataSrc: function (json) {
                if (json.message) {
                    $.toast(failToast(json.message, 3000))
                    return [];
                } else {
                    // 正常返回数据数组
                    return json.data; // 假设正常响应为 { data: [...] }
                }
            },
            error: function (xhr, error, thrown) {
                $.toast(failToast(thrown))
            }
        },
        columnDefs: columnDefs,
        searching: false,
        "processing": true,
        // info: false,
        rowCallback: rowCallBackFunc,
        columns: convertColumns(columns),
    });

    $(`#${tableId} tbody`).on('dblclick', 'td', function() {
        let data = $(this).text();
        copyTextToClipboardFallback(data)
        $.toast(successToast('复制成功'))
    });
    return table;
}

function convertColumns(columns) {
    let columnsDef = []
    for (const column of columns) {
        columnsDef.push({data: column})
    }
    return columnsDef;
}

function getSelectedIds(table) {
    let ids = []
    table.$('input:checked').each(function () {
        ids.push(this.value)
    });
    return ids;
}

function checkSelectedIds(table, msg) {
    msg = !msg ? "请选择条目" : msg
    if (getSelectedIds(table).length == 0) {
        $.alert({
            title: '警告!',
            content: msg,
        });
        return false;
    }
    return true;
}

function notReadyRowCallBack(row, data, index) {
    if (!data.isReady) {
        $(row).css('background-color', '#e2e1e1')
        return;
    }
}

function sumColumn(api, columnIndex) {
    let sum = api
        .column(columnIndex, {page: 'all'})
        .data()
        .reduce(function (a, b) {
            let x = parseFloat(a) || 0;
            let y = parseFloat(b) || 0;
            return x + y;
        }, 0);
    return sum;
}

function editColumn(data){
    return multiIconColumn(data, ['edit'])
}

function linkColumn(data){
    return multiIconColumn(data, ['link'])
}

function codeColumn(data){
    return multiIconColumn(data, ['code'])
}

function multiIconColumn(data, styles){
    const hasCode = styles && styles.includes('code');

    // 如果 data 为空 且 没有 code 样式，返回空
    if ((data == null || data === '') && !hasCode) {
        return '';
    }

    let prefix = `<div class="name-cell">${data || ''}`;  // data 为空时用空字符串

    for (let i = 0; i < styles.length; i++) {
        let style = styles[i];
        if(style === 'edit'){
            prefix += `&nbsp;&nbsp;<img class="edit" src="image/editing_blue.png" alt="Edit" width="15" height="15">`;
        }
        if(style === 'link'){
            prefix += `&nbsp;&nbsp;<img class="link" src="image/external-link-blue.png" alt="Link" width="13" height="13">`;
        }
        if(style === 'google_auth'){
            prefix += `&nbsp;&nbsp;<img class="google_auth" src="image/google_auth.png" alt="Google Auth" width="13" height="13">`;
        }
        if(style === 'code'){
            prefix += `&nbsp;&nbsp;<img class="code" src="image/code.png" alt="Code" width="13" height="13">`;
        }
    }

    return prefix + `</div>`;
}

const DtRenderUtil = {
    /**
     * 渲染缩略内容 + 点击弹窗 (支持 JSON 美化)
     * @param {String} data 原始数据
     * @param {String} type DataTables 渲染类型
     * @param {Object} options 可选配置
     * @param {Boolean} options.isJson 是否尝试按 JSON 格式化 (默认 false)
     * @param {String} options.label 表格里显示的替代文本 (可选，默认显示原始值的截断)
     */
    renderCode: function (data, type, options = {}) {
        if (!data) return '-';
        if (type !== 'display') return data; // 排序时使用原始值

        const isJson = options.isJson || false;
        let finalContent = '';
        let cssClass = '';

        // 1. 处理内容：JSON 格式化 或 普通文本
        if (isJson) {
            try {
                // 尝试解析并美化 JSON (缩进 2 空格)
                const obj = typeof data === 'string' ? JSON.parse(data) : data;
                finalContent = JSON.stringify(obj, null, 2);
                cssClass = 'dt-view-json'; // 应用深色代码样式
            } catch (e) {
                // 解析失败，回退到普通文本
                finalContent = String(data);
                cssClass = 'dt-view-text';
            }
        } else {
            finalContent = String(data);
            cssClass = 'dt-view-text';
        }

        // 2. HTML 转义 (防止 XSS 和 HTML 结构破坏)
        const safeContent = this.escapeHtml(finalContent);
        const safePreview = options.label || this.escapeHtml(String(data));

        // 3. 组装 Popover 内容
        // 包裹在一个 div 里，利用 CSS 类控制样式
        const popoverBody = `<pre class='${cssClass}'>${safeContent}</pre>`;

        // 4. 返回表格单元格 HTML
        return `
            <span class="dt-cell-short"
                  tabindex="0"
                  role="button"
                  data-bs-toggle="popover"
                  data-bs-trigger="click"  data-bs-placement="left" data-bs-container="body" data-bs-custom-class="dt-code-popover"
                  data-bs-html="true"
                  data-bs-content="${this.escapeQuote(popoverBody)}">
                ${safePreview}
            </span>
        `;
    },

    // 辅助：转义 HTML 实体 (用于显示)
    escapeHtml: function (str) {
        if (!str) return '';
        return String(str)
            .replace(/&/g, "&amp;")
            .replace(/</g, "&lt;")
            .replace(/>/g, "&gt;");
    },

    // 辅助：转义双引号 (用于放入 data-bs-content 属性中)
    escapeQuote: function (str) {
        if (!str) return '';
        return String(str).replace(/"/g, '&quot;');
    }
};

$(document).ready(function() {

    // 1. DataTables 重绘监听 (保持不变，确保翻页后生效)
    $(document).on('draw.dt', function () {
        const popoverTriggerList = document.querySelectorAll('[data-bs-toggle="popover"]');
        [...popoverTriggerList].map(popoverTriggerEl => {
            // 销毁旧实例
            const instance = bootstrap.Popover.getInstance(popoverTriggerEl);
            if(instance) instance.dispose();

            // 初始化新实例 (使用 click 触发)
            new bootstrap.Popover(popoverTriggerEl, {
                trigger: 'click',
                html: true,
                sanitize: false // 防止代码被过滤
            });
        });
    });

    // 2. 🔴 核心修复：全局点击监听，实现“点击空白处关闭，但点击弹窗内部不关闭”
    $('body').on('click', function (e) {
        // 遍历所有激活的 Popover
        $('[data-bs-toggle="popover"]').each(function () {
            // 目标元素不是“触发按钮”本身 && 目标元素不是“弹窗内容”内部
            if (!$(this).is(e.target) &&
                $(this).has(e.target).length === 0 &&
                $('.popover').has(e.target).length === 0) {

                // 只有当它是显示状态时才关闭
                const popoverInstance = bootstrap.Popover.getInstance(this);
                if (popoverInstance && $(this).attr('aria-describedby')) { // aria-describedby 存在说明它是展开的
                    popoverInstance.hide();
                }
            }
        });
    });
});
