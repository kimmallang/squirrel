$(function(){
    const $body = $('body');

    // 왼쪽 메뉴 show/hide
    $('#menuBtn').on('click', (e) => {
        if ($body.hasClass('menu-show')) {
            $body.removeClass('menu-show');
        } else {
            $body.addClass('menu-show');
        }
    });
});