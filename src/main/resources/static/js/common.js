$(function(){
    initAsideSlide();
    initLogin();
});

// 왼쪽 메뉴 show/hide
const initAsideSlide = () => {
    const $body = $('body');
    $('#menuBtn').on('click', (e) => {
        if ($body.hasClass('menu-show')) {
            $body.removeClass('menu-show');
        } else {
            $body.addClass('menu-show');
        }
    });
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

        $.ajax({
            url:`/api/user/my-info`,
            type:'get',
            success: function(res) {
                console.log('login-info call')
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