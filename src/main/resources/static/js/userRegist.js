let duplicateId = true;

/**
 * id 중복검사 함수
 * duplicateId == false일때 회원가입 작동
 */
function duplicateCheck() {
    if (!/^[a-zA-Z0-9]{4,20}$/.test($('#userId').val())) {
        alert("ID는 영문/숫자로 4자 ~ 20자 이내로만 작성가능합니다.")
        return;
    }

    let userId = $('#userId').val();
    axios.get('/api/user/account/check-id/' + userId)
        .then(function (response) {
            if (response.data !== 0) {
                alert("중복된 id 입니다");
                duplicateId = true;
            } else {
                alert("사용가능한 id 입니다");
                duplicateId = false;
            }
        })
        .catch(function (error) {
            console.log(error);
        })
}

/**
 * input 빈값 체크용 함수
 */
function nullCheck() {
    const $fields = $('#user-container input[type="text"]');

    for (let field of $fields) {
        const $field = $(field);
        if (!$field.val().trim()) {
            console.log($field.data('name'))
            alert(`${$field.data('name')}을(를) 입력해주세요.`);
            return false;
        }
    }

    const gender = $('input[name="optionGender"]:checked').val();
    if (!gender) {
        alert("성별을 선택해주세요.");
        return false;
    }
    return true;
}

/**
 * input 정규식 검사
 * @returns {boolean}
 */
function validateCheck() {
    if (!/^[a-zA-Z0-9]{8,20}$/.test($('#userPw').val())) {
        alert("패스워드는 영문/숫자로 8자 ~ 20자 이내로만 작성가능합니다.")
        return false;
    }
    if (!/^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/.test($('#userEmail').val())) {
        alert("이메일 형식을 준수해주세요");
        return false;
    }
    if (!/^0\d{2}-\d{4}-\d{4}$/.test($('#userPhone').val())) {
        alert("전화번호는 0XX-XXXX-XXXX 형식을 준수해주세요");
        return false;
    }
    return true;
}

/**
 * 중복검사, 비밀번호 일치, 빈값체크, 유효성 검사
 * 통합 검사 함수
 * @returns {boolean}
 */
function totalCheck() {
    if (!nullCheck()) {
        return false
    }

    if (duplicateId) {
        alert("중복검사를 먼저 마쳐주세요!")
        return false;
    }

    if ($("#userPw").val() !== $("#userPwCheck").val()) {
        alert("비밀번호가 불일치합니다.");
        return false;
    }

    if (!validateCheck()) {
        return false;
    }

    return true;
}

/**
 * 회원등록api 함수
 */
function regist() {

    if (!totalCheck()) {
        return;
    }

    axios.post("/api/user/account/regist", {
        userId: $("#userId").val(),
        userPw: $("#userPw").val(),
        userName: $("#userName").val(),
        userPhone: $("#userPhone").val(),
        userEmail: $("#userEmail").val(),
        userGender: $("input[name='optionGender']:checked").val(),
        userBirth: $("#userBirth").val()
    })
        .then(function (response) {
            if (response.data) {
                alert("회원등록 성공 로그인으로 이동합니다.")
                location.href = "/user/account/login";
            } else {
                alert("로그인 오류 발생");
            }
        })
        .catch(function (error) {
            console.log(error);
        })
}