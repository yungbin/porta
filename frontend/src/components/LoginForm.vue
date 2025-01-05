<template>
  <div class="login-form">
    <h2>Login</h2>
    <input v-model="id" type="text" placeholder="Id" />
    <input v-model="password" type="password" placeholder="Password" @keydown.enter="handleEnter" />
    <div class="input-group">
      <input type="checkbox" id="rememberMe" v-model="rememberMe" />
      <label for="rememberMe">로그인 유지하기</label>
    </div>
    <button ref="loginButton" @click="login">Login</button>
    <button @click="goToRegister" class="sign-up-button">Sign Up</button>
  </div>
</template>


<script>
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import axios from 'axios';
import { useStore } from 'vuex';

export default {
  setup() {
    const router = useRouter();
    const store = useStore();
    const id = ref('');
    const password = ref('');
    const rememberMe = ref(false); // 로그인 유지하기 체크박스

    const login = async () => {
      if (id.value && password.value) {
        try {
          const response = await axios.post('/api/member/login', {
            id: id.value,
            password: password.value,
          });
          if (response.data.status === 'success') {
            const user = {
              id: id.value,
              username: response.data.username,
              nickname: response.data.nickname,
              email: response.data.email,
            };
            const role = response.data.role;

            // 사용자가 로그인 유지하기를 선택한 경우에만 localStorage에 저장
            if (rememberMe.value) {
              localStorage.setItem('user', JSON.stringify(user));
              localStorage.setItem('role', role);
            }

            // Vuex에 로그인 상태 저장
            store.dispatch('login', { user, role });

            // 로그인 성공 시 홈 페이지로 이동
            router.push('/');
          } else {
            alert('Login failed: ' + response.data.message);
          }
        } catch (error) {
          console.error('Error during login:', error);
          alert('An error occurred during login. Please try again.');
        }
      } else {
        alert('Please enter id and password');
      }
    };

    const goToRegister = () => {
      router.push('/register');
    };

    const handleEnter = (event) => {
      if (event.key === 'Enter') {
        login();
      }
    };

    return {
      id,
      password,
      rememberMe,
      login,
      goToRegister,
      handleEnter,
    };
  },
};
</script>


<style>
.login-form {
  display: flex;
  flex-direction: column;
  width: 300px;
  margin: 20px auto;
  padding: 20px;
  border: 1px solid #ccc;
  border-radius: 5px;
  background-color: #f9f9f9;
}

.login-form input {
  margin-bottom: 10px;
  padding: 10px;
  border: 1px solid #ccc;
  border-radius: 5px;
}

.login-form button {
  padding: 10px;
  background-color: #333;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  margin-bottom: 10px;
}

.sign-up-button {
  background-color: #007bff;
}

.sign-up-button:hover {
  background-color: #0056b3;
}

.input-group {
  gap: 10px;
  margin-bottom: 10px;
}
</style>
