<template>
  <div class="register-form">
    <h2>회원가입</h2>
    <form @submit.prevent="register">
      <div class="form-group">
        <div class="input-group">
          <input v-model="id" id="id" type="text" required @input="idChecked = false" placeholder="ID" @keydown.enter="checkIdDuplicate" />
          <button type="button" @click="checkIdDuplicate" class="btn-secondary">중복확인</button>
        </div>
        <p v-if="!isIdAvailable && idChecked" class="error">이미 사용 중인 아이디입니다.</p>
      </div>

      <div class="form-group">
        <input v-model="password" id="password" type="password" placeholder="PASSWORD" required />
        <p v-if="!isPasswordValid && password" class="error">비밀번호는 영문, 숫자, 특수문자를 포함 8자리 이상이어야 합니다.</p>
      </div>
      
      <div class="form-group">
        <input v-model="confirmPassword" id="confirmPassword" type="password" placeholder="PASSWORD 확인" required />
        <p v-if="passwordMismatch" class="error">비밀번호가 일치하지 않습니다.</p>
      </div>
      
      <div class="form-group">
        <input v-model="username" id="username" type="text" placeholder="이름" required />
      </div>
      
      <div class="form-group">
        <input v-model="nickName" id="nickName" type="text" placeholder="닉네임" required />
      </div>
      
      <div class="form-group">
        <label for="phone">전화번호</label>
        <div class="input-group">
          <input v-model="phonePart1" id="phonePart1" type="text" maxlength="3" placeholder="000" required @input="filterPhoneInput('phonePart1')" />
          <span>-</span>
          <input v-model="phonePart2" id="phonePart2" type="text" maxlength="4" placeholder="0000" required @input="filterPhoneInput('phonePart2')" />
          <span>-</span>
          <input v-model="phonePart3" id="phonePart3" type="text" maxlength="4" placeholder="0000" required @input="filterPhoneInput('phonePart3')" />
        </div>
      </div>
      
      <div class="form-group">
        <label for="birthdate">생년월일</label>
        <div class="input-group">
          <select v-model="selectedYear" @change="updateBirthdate">
            <option value="">연도 선택</option>
            <option v-for="year in years" :key="year" :value="year">{{ year }}년</option>
          </select>
          <select v-model="selectedMonth" @change="updateBirthdate">
            <option value="">월 선택</option>
            <option v-for="month in months" :key="month" :value="month">{{ month }}월</option>
          </select>
          <select v-model="selectedDay" @change="updateBirthdate">
            <option value="">일 선택</option>
            <option v-for="day in days" :key="day" :value="day">{{ day }}일</option>
          </select>
        </div>
      </div>
      
      <div class="form-group">
        <label for="email">이메일</label>
        <div class="input-group">
          <input v-model="emailPrefix" id="emailPrefix" type="text" placeholder="이메일 앞부분" @input="validateEmail" />
          <span>@</span>
          <input v-model="emailDomain" id="emailDomain" type="text" placeholder="이메일 도메인" @input="validateEmail" v-if="selectedDomain === '직접입력'" />
          <select v-model="selectedDomain" @change="updateEmailDomain">
            <option value="직접입력">직접입력</option>
            <option value="gmail.com">gmail.com</option>
            <option value="naver.com">naver.com</option>
            <option value="daum.net">daum.net</option>
            <option value="hotmail.com">hotmail.com</option>
          </select>
          <button type="button" @click="sendVerificationCode" class="btn-secondary" >이메일인증</button>
        </div>
        <p v-if="!isEmailValid && emailPrefix" class="error">올바른 이메일 형식이 아닙니다.</p>
    </div>
    <div class="form-group">
        <label for="verificationCode">인증번호</label>
        <div class="input-group">
          <input v-model="verificationCode" id="verificationCode" type="text" placeholder="인증번호" required />
          <button type="button" @click="checkVerificationCode" class="btn-secondary">확인</button>
        </div>
    </div>
      <div class="form-group address-group">
        <label>주소</label>
        <div class="input-group">
          <input v-model="postcode" type="text" id="sample4_postcode" placeholder="우편번호" readonly />
          <button type="button" @click="execDaumPostcode" class="btn-secondary">우편번호 찾기</button>
        </div>
        <input type="text" id="sample4_roadAddress" v-model="roadAddress" placeholder="도로명주소" readonly />
        <input type="text" id="sample4_jibunAddress" v-model="jibunAddress" placeholder="지번주소" readonly />
        <div class="input-group">
          <input type="text" id="sample4_detailAddress" v-model="detailAddress" placeholder="상세주소" />
          <input type="text" id="sample4_extraAddress" v-model="extraAddress" placeholder="참고항목" />
        </div>
      </div>

      <button type="submit" :disabled="!canRegister" class="btn-primary">회원가입</button>

      <div v-if="!canRegister" class="feedback-message">
        <p>회원가입을 완료하려면 다음 사항을 확인해주세요:</p>
        <ul>
          <li v-if="!id || !isIdAvailable || !idChecked">아이디를 입력하고 중복확인을 해주세요.</li>
          <li v-if="!isPasswordValid">올바른 형식의 비밀번호를 입력해주세요.</li>
          <li v-if="passwordMismatch">비밀번호 확인이 일치하지 않습니다.</li>
          <li v-if="!username">이름을 입력해주세요.</li>
          <li v-if="!nickName">닉네임을 입력해주세요.</li>
          <li v-if="!phonePart1 || !phonePart2 || !phonePart3">전화번호를 입력해주세요.</li>
          <li v-if="!birthdate">생년월일을 입력해주세요.</li>
          <li v-if="!roadAddress">주소를 입력해주세요.</li>
          <li v-if="emailPrefix && !isEmailValid">올바른 이메일 형식을 입력해주세요.</li>
          <li v-if="!isVerificationCodeValid">인증번호를 확인해주세요.</li>
        </ul>
      </div>
    </form>
  </div>
</template>

<script>
import { ref, computed } from 'vue';
import { useRouter } from 'vue-router';
import axios from 'axios';

export default {
  name: 'Register',
  setup() {
    const router = useRouter();
    const id = ref('');
    const password = ref('');
    const confirmPassword = ref('');
    const username = ref('');
    const nickName = ref('');
    const phonePart1 = ref('010');
    const phonePart2 = ref('');
    const phonePart3 = ref('');
    const emailPrefix = ref('');
    const emailDomain = ref('');
    const postcode = ref('');
    const roadAddress = ref('');
    const jibunAddress = ref('');
    const detailAddress = ref('');
    const extraAddress = ref('');
    const isIdAvailable = ref(false);
    const isEmailValid = ref(true);
    const idChecked = ref(false);
    const selectedYear = ref('');
    const selectedMonth = ref('');
    const selectedDay = ref('');
    const birthdate = ref('');
    const selectedDomain = ref('직접입력');
    const verificationCode = ref('');
    const sentVerificationCode = ref(''); // 서버에서 보낸 인증번호
    const isVerificationCodeValid = ref(false);

    const filterPhoneInput = (part) => {
      if (part === 'phonePart1') {
        phonePart1.value = phonePart1.value.replace(/\D/g, '');
      } else if (part === 'phonePart2') {
        phonePart2.value = phonePart2.value.replace(/\D/g, '');
      } else if (part === 'phonePart3') {
        phonePart3.value = phonePart3.value.replace(/\D/g, '');
      }
    };

    const updateEmailDomain = () => {
      if (selectedDomain.value !== '직접입력') {
        emailDomain.value = selectedDomain.value;
      } else {
        emailDomain.value = '';
      }
      validateEmail();
    };

    const passwordMismatch = computed(() => password.value !== confirmPassword.value);
    const isPasswordValid = computed(() => {
      const regex = /^(?=.*[A-Za-z])(?=.*\d)(?=.*[@$!%*#?&])[A-Za-z\d@$!%*#?&]{8,}$/;
      return regex.test(password.value);
    });

    const isIdValid = computed(() => {
      const regex = /^[a-zA-Z0-9-_]{6,12}$/;
      return regex.test(id.value);
    });

    const years = Array.from({ length: 100 }, (_, i) => new Date().getFullYear() - i);
    const months = Array.from({ length: 12 }, (_, i) => String(i + 1).padStart(2, '0'));
    const days = computed(() => {
      const month = parseInt(selectedMonth.value, 10);
      if (!month) return [];
      const isLeapYear = (year) => (year % 4 === 0 && year % 100 !== 0) || (year % 400 === 0);
      const daysInMonth = [31, isLeapYear(selectedYear.value) ? 29 : 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31];
      return Array.from({ length: daysInMonth[month - 1] }, (_, i) => String(i + 1).padStart(2, '0'));
    });

    const canRegister = computed(() =>
      id.value && isIdAvailable.value && isIdValid.value && idChecked.value &&
      password.value && confirmPassword.value && username.value && nickName.value &&
      phonePart1.value && phonePart2.value && phonePart3.value &&
      birthdate.value && !passwordMismatch.value && isPasswordValid.value &&
      roadAddress.value && (emailPrefix.value === '' || isEmailValid.value)
    );

    const updateBirthdate = () => {
      const month = String(selectedMonth.value).padStart(2, '0');
      const day = String(selectedDay.value).padStart(2, '0');
      birthdate.value = `${selectedYear.value}-${month}-${day}`;
    };

    const checkIdDuplicate = async () => {
      if (id.value) {
        if (!isIdValid.value) {
          alert('아이디는 6-12자의 영문, 숫자, 기호(-, _)만 사용할 수 있습니다.');
          id.value = '';
          return;
        }
        try {
          const checkId = id.value.toUpperCase();
          const response = await axios.post('/api/member/check-id', { id: checkId });
          isIdAvailable.value = response.data.available;
          idChecked.value = true;
          alert(isIdAvailable.value ? '사용 가능한 아이디입니다.' : '이미 사용 중인 아이디입니다.');
        } catch (error) {
          console.error('ID 중복 확인 중 오류 발생:', error);
        }
      } else {
        alert('아이디를 입력해주세요.');
      }
    };
    const sendVerificationCode = async () => {
      if (!isEmailValid.value) {
        alert('올바른 이메일 형식이 아닙니다.');
        return;
      }
      
      if (emailPrefix.value && (!emailDomain.value && emailDomain.value !== '')) {
        alert('이메일을 확인해주세요');
        return;
      }
      
      if(!idChecked.value){
        alert('아이디 중복확인 후 가능합니다');
        return;
      }
      try {
        const data = {email : `${emailPrefix.value}@${emailDomain.value}`, userId : id.value};
        console.log("data : ", data);
        const response = await axios.post('/api/mail/send', data);
        alert('인증번호가 전송되었습니다.');
        sentVerificationCode.value = response.data.verificationCode; // 서버에서 받은 인증번호
      } catch (error) {
        console.error('인증번호 전송 중 오류 발생:', error);
        alert('인증번호 전송에 실패했습니다.');
      }
    };

    const checkVerificationCode = async () => {
      try {
        const data = {email : `${emailPrefix.value}@${emailDomain.value}`, userId : id.value, token: verificationCode.value};
        const response = await axios.post('/api/mail/verify', data);

        if (response.data.succes) {
          isVerificationCodeValid.value = true;
          alert('인증번호가 확인되었습니다.');
        } else {
          isVerificationCodeValid.value = false;
          alert('잘못된 인증번호입니다. 다시 확인해주세요.');
        }
      } catch (error) {
        console.error('인증번호 확인 중 오류 발생:', error);
        alert('인증번호 확인에 실패했습니다.');
      }
    };


    const filterInput = (part) => {
      const phoneRegex = /^[0-9]*$/;
      if (part === 'phonePart1' && phonePart1.value.length <= 3) {
        phonePart1.value = phonePart1.value.replace(phoneRegex, '');
      } else if (part === 'phonePart2' && phonePart2.value.length <= 4) {
        phonePart2.value = phonePart2.value.replace(phoneRegex, '');
      } else if (part === 'phonePart3' && phonePart3.value.length <= 4) {
        phonePart3.value = phonePart3.value.replace(phoneRegex, '');
      }
    };

    const validateEmail = () => {
      const regex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
      isEmailValid.value = regex.test(`${emailPrefix.value}@${emailDomain.value}`) || emailPrefix.value === '';
    };

    const execDaumPostcode = () => {
      new window.daum.Postcode({
        oncomplete: function (data) {
          postcode.value = data.zonecode;
          roadAddress.value = data.roadAddress;
          jibunAddress.value = data.jibunAddress;

          if (data.userSelectedType === 'R') {
            extraAddress.value = data.bname && /[동|로|가]$/g.test(data.bname) ? `(${data.bname})` : '';
            extraAddress.value += data.buildingName && data.apartment === 'Y' ? (extraAddress.value ? `, ${data.buildingName}` : `(${data.buildingName})`) : '';
          } else {
            extraAddress.value = '';
          }
        }
      }).open();
    };

    const register = async () => {
      if (canRegister.value) {
        const formData = new FormData();
        formData.append('id', id.value);
        formData.append('password', password.value);
        formData.append('username', username.value);
        formData.append('nickName', nickName.value);
        formData.append('phone', `${phonePart1.value}-${phonePart2.value}-${phonePart3.value}`);
        formData.append('birthdate', birthdate.value);
        formData.append('email', `${emailPrefix.value}@${emailDomain.value}`.toLowerCase());
        formData.append('address', `${roadAddress.value} ${detailAddress.value} ${extraAddress.value}`.trim());

        for (const [key, value] of formData.entries()) {
          console.log(`${key}: ${value}`);
        }
        try {
          const response = await axios.post('/api/member/register', formData, {
            headers: {
              'Content-Type': 'multipart/form-data',
            },
          });
          if (response.data.status === 'registered') {
            alert('회원가입 성공!');
            router.push('/loginForm');
          } else {
            alert('회원가입 실패!');
          }
        } catch (error) {
          console.error('회원가입 중 오류 발생:', error);
          alert('회원가입 중 오류가 발생했습니다.');
        }
      } else {
        alert('모든 필수 항목을 올바르게 입력해주세요.');
      }
    };

    return {
      id,
      password,
      confirmPassword,
      username,
      nickName,
      emailPrefix,
      emailDomain,
      postcode,
      roadAddress,
      jibunAddress,
      detailAddress,
      extraAddress,
      phonePart1,
      phonePart2,
      phonePart3,
      selectedYear,
      selectedMonth,
      selectedDay,
      birthdate,
      years,
      months,
      days,
      register,
      checkIdDuplicate,
      passwordMismatch,
      isPasswordValid,
      isIdValid,
      execDaumPostcode,
      canRegister,
      validateEmail,
      isEmailValid,
      isIdAvailable,
      idChecked,
      updateBirthdate,
      filterInput,
      selectedDomain,
      filterPhoneInput,
      updateEmailDomain,
      verificationCode,
      isVerificationCodeValid,
      sendVerificationCode,
      checkVerificationCode,
    };
  },
  mounted() {
    const script = document.createElement('script');
    script.src = '//t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js';
    document.head.appendChild(script);
  }
};
</script>

<style scoped>
.register-form {
  max-width: 700px;
  margin: 40px auto;
  padding: 20px;
  background-color: #f9f9f9;
  border-radius: 8px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
}

h2 {
  text-align: center;
  color: #333;
  margin-bottom: 20px;
}

.form-group {
  margin-bottom: 20px;
}

label {
  display: block;
  margin-bottom: 5px;
  font-weight: bold;
  color: #555;
}

input[type="text"],
input[type="password"],
input[type="email"],
select {
  width: 100%;
  padding: 10px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 16px;
  box-sizing: border-box;
}

.input-group {
  display: flex;
  gap: 10px;
  margin-bottom: 10px;
}

.input-group input {
  flex-grow: 1;
}

.btn-secondary {
  padding: 10px 15px;
  background-color: #6c757d;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 14px;
  white-space: nowrap; /* 글씨가 두 줄로 나오지 않도록 설정 */
}

.btn-primary {
  width: 100%;
  padding: 12px;
  background-color: #007bff;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 16px;
  transition: background-color 0.3s;
}

.btn-primary:hover {
  background-color: #0056b3;
}

.btn-primary:disabled {
  background-color: #cccccc;
  cursor: not-allowed;
}

.error {
  color: #dc3545;
  font-size: 14px;
  margin-top: 5px;
}

input[readonly] {
  background-color: #e9ecef;
}

.feedback-message {
  margin-top: 20px;
  padding: 10px;
  background-color: #f8d7da;
  border: 1px solid #f5c6cb;
  border-radius: 4px;
  color: #721c24;
}

.feedback-message ul {
  padding-left: 20px;
  margin-top: 10px;
}

.address-group input {
  margin-bottom: 10px;
}

.address-group .input-group:last-child input {
  width: 50%;
}

.input-group select {
  flex-grow: 1;
  padding: 10px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 16px;
  background-color: white;
}


</style>
