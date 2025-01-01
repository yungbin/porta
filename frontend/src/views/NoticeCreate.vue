<template>
  <div class="notice-submission-container">
      <div class="notice-submission">
          <h1>공지사항</h1>
          <form @submit.prevent="submitNotice">
              <div class="form-group">
                  <input v-model="notice.title" id="title" type="text" required placeholder="공지사항 제목">
                  <label for="title">공지사항을 간단히 표현해주세요</label>
              </div>
              <div class="form-group">
                  <textarea v-model="notice.content" id="content" rows="6" required placeholder="공지사항 설명"></textarea>
                  <label for="content">공지사항 내용을 자세히 설명해주세요</label>
              </div>
              <button type="submit" class="btn-submit">
                  <span class="material-icons">send</span> 공지 발행하기
              </button>
          </form>
      </div>
  </div>
</template>

<script>
import { mapGetters } from 'vuex';
import axios from 'axios';

export default {
  data() {
      return {
          notice: {
              title: '',
              content: '',
          },
          tagInput: '',
          categorys: [] // Correctly named categorys to match template
      };
  },
  computed: {
        ...mapGetters(['user']) // Vuex에서 사용자 정보 가져오기
  },
  created() {
  },
  methods: {
      submitNotice() {
          const noticeData = { ...this.notice, userId: this.user.id };
          console.log(noticeData);
          axios.post('/api/notice/submit', noticeData)
              .then(response => {
                  console.log('공지사항 제출 성공:', response.data);
                  // 폼 초기화
                  this.notice = { title: '', content: ''};
                  this.$router.push('/noticeList');
              })
              .catch(error => {
                  console.error('공지사항 제출 실패:', error);
              });
      },
  }
};
</script>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600&display=swap');
@import url('https://fonts.googleapis.com/icon?family=Material+Icons');

body {
  font-family: 'Poppins', sans-serif;
  background-color: #f0f4f8;
  color: #2d3748;
  line-height: 1.6;
}

.notice-submission-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  padding: 2rem;
}

.notice-submission {
  background-color: #ffffff;
  border-radius: 20px;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.1);
  padding: 3rem;
  width: 100%;
  max-width: 700px;
}

h1 {
  font-size: 2.5rem;
  font-weight: 600;
  margin-bottom: 2rem;
  text-align: center;
  color: #3a86ff;
}

.form-group {
  margin-bottom: 2rem;
  position: relative;
}

input[type="text"],
textarea,
select {
  width: 100%;
  padding: 1rem;
  border: 2px solid #e2e8f0;
  border-radius: 10px;
  font-size: 1rem;
  transition: all 0.3s ease;
  background-color: #f8fafc;
}

input[type="text"]:focus,
textarea:focus,
select:focus {
  outline: none;
  border-color: #3a86ff;
  box-shadow: 0 0 0 3px rgba(58, 134, 255, 0.1);
}

label {
  position: absolute;
  left: 1rem;
  top: -0.5rem;
  background-color: #ffffff;
  padding: 0 0.5rem;
  font-size: 0.875rem;
  color: #718096;
}

.btn-submit {
  width: 100%;
  padding: 1rem;
  background-color: #3a86ff;
  color: #ffffff;
  border: none;
  border-radius: 10px;
  font-size: 1rem;
  font-weight: 500;
  cursor: pointer;
  transition: background-color 0.3s ease;
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 0.5rem;
}

.btn-submit:hover {
  background-color: #2563eb;
}

.material-icons {
  font-size: 1.25rem;
}

@media (max-width: 768px) {
  .notice-submission {
      padding: 2rem;
  }

  h1 {
      font-size: 2rem;
  }
}
</style>
