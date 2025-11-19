
let blockExplorer = {
    42161: 'https://arbiscan.io/',
    137: 'https://polygonscan.com/',
    10: 'https://optimistic.etherscan.io/',
    1: 'https://etherscan.io/',
    56: 'https://bscscan.com/',
    5: 'https://goerli.etherscan.io/',
    280: 'https://zksync2-testnet.zkscan.io/',
    8888: 'https://starkscan.co/',
    97: 'https://testnet.bscscan.com/',
    324: 'https://era.zksync.network/',
    59140: 'https://goerli.lineascan.build/',
    59144: 'https://lineascan.build/',
    42766: 'https://scan.zkfair.io/',
    43114: 'https://snowtrace.io/',
    112358: 'https://xterscan.io/',
    1100: 'https://dym.fyi/',
    210000: 'https://solscan.io/',
    8453: 'https://basescan.org/',
    660279: 'https://xaiscan.io/',
    50341: 'https://reddio-devnet.l2scan.co/',
    11155111: 'https://sepolia.etherscan.io/',
    1868: 'https://soneium.blockscout.com/',
    10143: 'https://testnet.monadexplorer.com/',
    6342: 'https://www.oklink.com/zh-hans/megaeth-testnet/',
    196: 'https://www.oklink.com/zh-hans/x-layer/',
    9745: 'https://plasmascan.to/',
}
function isEmpty(str){
    return str === null || str === undefined || str === '';
}

function warn(message){
    $.alert({
        title: '警告!',
        content: message,
    });
}

function selectChange(selector, data, showFields, valueField){
    // selector.children().remove().end()
    // for (let item of data) {
    //     let show_ = item, value_ = item
    //     if(showField){
    //         show_ = item[showField]
    //         value_ = item[valueField]
    //     }
    //     let option = `<option data-tokens="${show_}"
    //                             text="${show_}"
    //                             value="${value_}">${show_}</option>`
    //     selector.append(option)
    //
    // }
    // selector.selectpicker('refresh')
    // selector.change()
    __selectWithNullChange(selector, data, showFields, valueField, false)
}

function selectWithNullChange(selector, data, showFields, valueField){
    __selectWithNullChange(selector, data, showFields, valueField, true)
}

function __selectWithNullChange(selector, data, showFields, valueField, includeNull){
    selector.children().remove().end()
    if(includeNull){
        selector.append(`<option data-tokens="请选择" text="请选择" value="">请选择</option>`)
    }

    for (let item of data) {
        let show_ = '', value_ = ''
        if(Array.isArray(showFields)){
            let showStrings = []
            for(let showField of showFields){
                showStrings.push(item[showField])
            }
            show_ = showStrings.join('---')
        }else{
            if(showFields){
                show_ = item[showFields]
            }else{
                show_ = item
            }
        }
        if(valueField){
            value_ = item[valueField]
        }else{
            value_ = item
        }

        let option = `<option data-tokens="${show_}" attr_id="${item['id']}"
                                text="${show_}"
                                value="${value_}">${show_}</option>`
        selector.append(option)
    }
    selector.selectpicker('refresh')
}


function showEditModal(modalSelector, formId, data){
    let fields = Object.getOwnPropertyNames(data)
    for (let field of fields) {
        let ele = $(`#${formId} input[name=${field}],#${formId} select[name=${field}],#${formId} textarea[name=${field}]`)

        let value = data[field]
        if(typeof value === 'boolean'){
            value = value + ''
        }

        ele.val(value)
        if(ele.is('select')){
            ele.change()
            ele.selectpicker('refresh')
        }
    }
    modalSelector.modal('show')
}

function showEditModal_v2(modalSelector, formId, data){
    const $form = $('#' + formId);

    Object.keys(data).forEach(function(field){
        // 注意 name 需要加引号，避免特殊字符时选择失败
        const $ele = $form.find('input[name="' + field + '"], select[name="' + field + '"], textarea[name="' + field + '"]');
        if (!$ele.length) return;

        let value = data[field];
        // 后端布尔/数字统一成字符串，和 <option value="..."> 对齐
        if (typeof value === 'boolean') value = String(value);
        if (value == null) value = '';

        if ($ele.is('select')) {
            if ($ele.hasClass('selectpicker')) {
                // 若返回的值当前选项里没有（比如 true/false、1/0 差异），临时补一条，避免“Nothing selected”
                if (value !== '' && !$ele.find('option[value="' + value + '"]').length) {
                    $ele.append('<option value="'+ value +'">'+ value +'</option>');
                }
                $ele.selectpicker('val', value).selectpicker('refresh').selectpicker('render');
            } else {
                $ele.val(value).trigger('change');
            }
        } else {
            $ele.val(value);
        }
    });

    modalSelector.modal('show');
}

function delay(time) {
    return new Promise(resolve => setTimeout(resolve, time));
}

function sleep(delay) {
    let start = new Date().getTime();
    while (new Date().getTime() < start + delay)
        ;
}

function copyTextToClipboardFallback(text) {
    if (navigator.clipboard && window.isSecureContext) {
        return navigator.clipboard.writeText(text)
            .then(() => console.log('Text copied successfully!'))
            .catch(err => console.error('Failed to copy text: ', err));
    }

    // 回退方案
    const textarea = document.createElement('textarea');
    textarea.value = text;
    textarea.setAttribute('readonly', '');
    textarea.style.position = 'absolute';
    textarea.style.left = '-9999px';
    document.body.appendChild(textarea);

    try {
        textarea.select();
        textarea.setSelectionRange(0, textarea.value.length); // 动态选择文本长度
        const success = document.execCommand('copy');
        if (success) {
            console.log('Text copied to clipboard');
        } else {
            console.error('Failed to copy: execCommand returned false');
        }
    } catch (err) {
        console.error('Failed to copy: ', err);
    } finally {
        document.body.removeChild(textarea);
    }
}


function formatDateLite(date) {
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const day = String(date.getDate()).padStart(2, '0');
    const hours = String(date.getHours()).padStart(2, '0');
    const minutes = String(date.getMinutes()).padStart(2, '0');
    const seconds = String(date.getSeconds()).padStart(2, '0');

    // return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
    return `${month}-${day} ${hours}:${minutes}`;
}

function formatDate(date) {
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const day = String(date.getDate()).padStart(2, '0');
    const hours = String(date.getHours()).padStart(2, '0');
    const minutes = String(date.getMinutes()).padStart(2, '0');
    const seconds = String(date.getSeconds()).padStart(2, '0');

    return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
}

/**
 * 从 DataTables 提取 sequence 连续段并返回多行文本，每行带个数
 * 例： "5-9 (5个)\n12 (1个)\n20-22 (3个)"
 * @param {DataTable} dt  $('#table').DataTable() 实例
 * @param {Object} opts  可选：{ applied: false, field: 'sequence', singleAsRange: false }
 *  - applied: true 时仅使用当前筛选后的可见行
 *  - field:   sequence字段名（默认 'sequence'）
 *  - singleAsRange: 单个也显示成 "n-n (1个)"（默认 false）
 * @returns {string}
 */
function sequenceRangesWithCount(dt, opts = {}) {
    let { applied = false, field = 'sequence', singleAsRange = false } = opts;

    // 1) 拿数据
    let rowsApi = applied ? dt.rows({ search: 'applied' }) : dt.rows();
    let rows = rowsApi.data().toArray();

    // 2) 提取整数 sequence，去重+排序
    let seqs = [...new Set(
        rows
            .map(r => {
                if (r && typeof r === 'object') return Number(r[field] ?? r.sequence);
                return Number(r); // 兜底
            })
            .filter(n => Number.isInteger(n))
    )].sort((a, b) => a - b);

    if (seqs.length === 0) return '';

    // 3) 扫描连续区间
    let lines = [];
    for (let i = 0; i < seqs.length; i++) {
        let start = seqs[i];
        let end = start;
        while (i + 1 < seqs.length && seqs[i + 1] === end + 1) {
            end = seqs[++i];
        }
        let count = end - start + 1;
        if (start === end && !singleAsRange) {
            lines.push(`${start} (${count}个)`);
        } else {
            lines.push(`${start}-${end} (${count}个)`);
        }
    }

    // 4) 用换行拼接
    return lines.join('\n');
}

// === 使用示例 ===
let dt = $('#yourTable').DataTable();

// 全部行
let textAll = sequenceRangesWithCount(dt);
// 仅筛选后的可见行
let textApplied = sequenceRangesWithCount(dt, { applied: true });

// 显示
$('#sequenceRanges').text(textAll);

