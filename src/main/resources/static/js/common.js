$(function(){
    initAsideSlide();
    initLogin();
    initAsideMenu();
});

// 왼쪽 영역 show/hide
const initAsideSlide = () => {
    const $body = $('body');
    // PC는 기본값이 노출(모바일은 비노출)
    if (window.screen.width > 767) {
        $body.addClass('menu-show').addClass('menu-show-ani');
    }

    $('#menuBtn').on('click', (e) => {
        $body.removeClass('menu-show');

        if ($body.hasClass('menu-show-ani')) {
            $body.removeClass('menu-show-ani');
        } else {
            $body.addClass('menu-show-ani');
        }
    });
};

const initAsideMenu = () => {
    const path = window.location.pathname;
    if (!path || path === '/' || path.startsWith('/humors')) {
        $(`aside a[href="/humors"]`).addClass('on');
    } else if (path.startsWith('/accidents')) {
        $(`aside a[href="/accidents"]`).addClass('on');
    }
};

// 로그인
const initLogin = () => {
    $(document).on('click', '#kakaoLoginBtn, #naverLoginBtn', function(){
        const provider = $(this).data('provider');

        $.ajax({
            url:`/api/user/login/${provider}/url`,
            type:'get',
            success: function(res) {
                sessionStorage.setItem('isLogin', 'inProgress');
                location.href = res.data;
            }
        });
    });

    $(document).on('click', '#logout', function() {
        $.ajax({
            url:`/api/user/logout`,
            type:'get',
            success: function(res) {
                location.reload();
                sessionStorage.setItem('isLogin', 'false');
            }
        });
    });

    const isLogIn = () => {
        return sessionStorage.getItem('isLogin') === 'true';
    };

    const isLogOut = () => {
        return sessionStorage.getItem('isLogin') === 'false';
    };

    const login = () => {
        $('.login-form').hide();
        $('.aside-profile').hide();

        if (isLogIn()) {
            $('.aside-profile').show();
            $('.aside-profile .login-by > span > span').text(sessionStorage.getItem('nickname'));
            $('.aside-profile .profile-img img').attr('src', sessionStorage.getItem('profileThumbnailUrl'));
            return;
        }

        if (isLogOut()) {
            $('.login-form').show();
            return;
        }

        $('.aside-menu-list').hide(); // 깜빡임 방지
        $.ajax({
            url:`/api/user/my-info`,
            type:'get',
            success: function(res) {
                $('.aside-menu-list').show(); // 깜빡임 방지
                if (!res.data) {
                    $('.login-form').show();
                    $('.aside-profile').hide();
                    return;
                }

                const nickname = res.data.nickname;
                const profileThumbnailUrl = res.data.profileThumbnailUrl;
                if (!res.data.nickname) {
                    $('.login-form').show();
                    return;
                }

                $('.aside-profile .login-by > span > span').text(nickname);
                $('.aside-profile .profile-img img').attr('src', profileThumbnailUrl);
                $('.aside-profile').show();

                saveUserSessionStorage(nickname, profileThumbnailUrl);
            }
        });
    };

    const saveUserSessionStorage = (nickname, profileThumbnailUrl) => {
        sessionStorage.setItem('isLogin', 'true');
        sessionStorage.setItem('nickname', nickname);
        sessionStorage.setItem('profileThumbnailUrl', profileThumbnailUrl);
    };

    login();
};

// 쿠키
const getCookie = (key) => {
    if (!document.cookie) {
        return '';
    }

    const cookies = document.cookie.split(';');
    for (const cookie of cookies) {
        const key_ = cookie.split('=')[0].trim();
        const value_ = cookie.split('=')[1].trim();
        if (key_ === key) {
            return value_;
        }
    }

    return '';
};

// 로딩
const showLoading = () => {
    $('#dimmed').show();
    $('#loading').show();
}

const hideLoading = () => {
    $('#dimmed').hide();
    $('#loading').hide();
}
