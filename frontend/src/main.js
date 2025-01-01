import { createApp } from 'vue';
import App from './App.vue';
import router from '../src/script/router';
import store from '@/store';
import 'bootstrap-icons/font/bootstrap-icons.css';

function parseJSON(value) {
  try {
    return JSON.parse(value);
  } catch (e) {
    return null;
  }
}

const storedUser = parseJSON(localStorage.getItem('user'));

const app = createApp(App);

app.use(router);
app.use(store);

// Vuex에서 사용자 정보를 설정
if (storedUser) {
  store.dispatch('login', { user: storedUser, role: sessionStorage.getItem('role') || '' }); // 사용자 로그인 상태로 설정
}

app.mount('#app');
