/**
 * 通用确认弹窗
 * 用法：
 *   showConfirm({
 *     title: '上传确认',
 *     message: '确认上传所选账户吗？',
 *     okText: '确认上传',
 *     okClass: 'btn-danger', // 按钮样式，可选
 *     onConfirm: function () { ... }
 *   });
 *
 * 或简单写：
 *   showConfirm('确认删除吗？', function () { ... });
 */
function showConfirm(options, onConfirm) {
    // 支持 showConfirm('msg', fn) 的简写
    if (typeof options === 'string') {
        options = {
            message: options,
            onConfirm: onConfirm
        };
    }

    let defaults = {
        title: '操作确认',
        message: '确认要执行该操作吗？',
        okText: '确定',
        cancelText: '取消',
        okClass: 'btn-primary',
        onConfirm: function () {}
    };

    let opts = $.extend({}, defaults, options || {});

    let $modal = $('#confirmModal');
    let $title = $('#confirmModalTitle');
    let $body = $('#confirmModalBody');
    let $okBtn = $('#confirmModalOkBtn');
    let $cancelBtn = $modal.find('.btn-cancel');

    $title.text(opts.title);
    $body.text(opts.message);
    $okBtn.text(opts.okText);
    $cancelBtn.text(opts.cancelText);

    // 重置 OK 按钮 class（只保留 btn 和自定义样式）
    $okBtn.attr('class', 'btn ' + opts.okClass);

    // 清除旧事件，绑定新事件
    $okBtn.off('click').on('click', function () {
        try {
            opts.onConfirm && opts.onConfirm();
        } finally {
            let bsModal = bootstrap.Modal.getInstance($modal[0]);
            bsModal && bsModal.hide();
        }
    });

    // 显示弹窗
    let bsModal = new bootstrap.Modal($modal[0]);
    bsModal.show();
}

function showDeleteConfirm(options) {
    // 允许 showDeleteConfirm('确认删除吗？', fn) 这种简写
    if (typeof options === 'string') {
        options = {
            message: options
        };
    }

    let defaults = {
        title: '删除确认',
        message: '确认要删除该数据吗？',
        // 如果设置了 requireText，则启用“输入确认文本”模式
        requireText: null,          // 比如 'DELETE'
        requireTextLabel: null,     // 输入框左侧文案
        hintText: null,             // 输入框下方提示
        onConfirm: function () {}
    };

    let opts = $.extend({}, defaults, options || {});

    let $modal = $('#deleteConfirmModal');
    if ($modal.length === 0) {
        console.error('deleteConfirmModal 元素不存在，请检查页面是否包含 id="deleteConfirmModal" 的 modal HTML');
        return;
    }

    let $title = $('#deleteConfirmModalTitle');
    let $message = $('#deleteConfirmModalMessage');
    let $inputWrapper = $('#deleteConfirmInputWrapper');
    let $input = $('#deleteConfirmInput');
    let $inputLabel = $('#deleteConfirmInputLabel');
    let $hint = $('#deleteConfirmHint');
    let $okBtn = $('#deleteConfirmModalOkBtn');

    $title.text(opts.title);
    $message.text(opts.message);

    $okBtn.off('click');
    $input.off('input');

    if (opts.requireText) {
        // 模式二：需要输入确认文本
        $inputWrapper.removeClass('d-none');

        let label = opts.requireTextLabel || '确认文本';
        let hint = opts.hintText || ('为确认删除，请在上方输入：' + opts.requireText);

        $inputLabel.text(label);
        $hint.text(hint);

        $input.val('');
        $okBtn.prop('disabled', true);

        $input.on('input', function () {
            let val = $(this).val().trim();
            $okBtn.prop('disabled', val !== opts.requireText);
        });

        $okBtn.on('click', function () {
            let val = $input.val().trim();
            if (val !== opts.requireText) {
                return;
            }
            try {
                opts.onConfirm && opts.onConfirm();
            } finally {
                let instance = bootstrap.Modal.getInstance($modal[0]);
                instance && instance.hide();
            }
        });
    } else {
        // 模式一：简单确认删除
        $inputWrapper.addClass('d-none');
        $input.val('');
        $okBtn.prop('disabled', false);

        $okBtn.on('click', function () {
            try {
                opts.onConfirm && opts.onConfirm();
            } finally {
                let instance = bootstrap.Modal.getInstance($modal[0]);
                instance && instance.hide();
            }
        });
    }

    let instance = new bootstrap.Modal($modal[0]);
    instance.show();
}

function confirmDelete(message, onConfirm) {
    showDeleteConfirm({
        message: message || '确认要删除选中数据吗？',
        onConfirm: onConfirm
    });
}

function confirmDangerDelete(message, onConfirm, requireText) {
    showDeleteConfirm({
        message: message || '这是危险操作，删除后不可恢复！',
        requireText: requireText || 'DELETE', // 默认必须输入 DELETE
        onConfirm: onConfirm
    });
}


