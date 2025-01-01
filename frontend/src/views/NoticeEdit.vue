<template>
  <section class="notice-edit">
    <h2>공지사항 수정하기</h2>
    <form @submit.prevent="updateNotice">
      <div class="form-group">
        <label for="title">제목</label>
        <input
          type="text"
          id="title"
          v-model="notice.TITLE"
          required
          placeholder="공지사항 제목을 입력하세요"
        />
      </div>
      <div class="form-group">
        <label for="content">내용</label>
        <textarea
          id="content"
          v-model="notice.CONTENT"
          required
          placeholder="공지사항 내용을 입력하세요"
        ></textarea>
      </div>
      <div class="form-actions">
        <button type="submit" class="btn-submit">수정 완료</button>
        <button type="button" @click="cancelEdit" class="btn-cancel">취소</button>
      </div>
    </form>
  </section>
</template>


<script>
import axios from 'axios';

export default {
  name: 'NoticeEdit',
  data() {
    return {
      notice: {
        TITLE: '',
        CONTENT: ''
      }
    };
  },
  created() {
    this.fetchNoticeDetail();
  },
  methods: {
    fetchNoticeDetail() {
      const noticeId = this.$route.params.id;
      axios.get(`/api/notice/detail/${noticeId}`)
        .then(response => {
          this.notice = response.data;
        })
        .catch(error => {
          console.error('공지사항 세부 사항을 가져오는 중 오류 발생:', error);
        });
    },
    updateNotice() {
      const noticeId = this.$route.params.id;
      axios.put(`/api/notice/update/${noticeId}`, this.notice)
        .then(() => {
          this.$router.push({ name: 'NoticeDetail', params: { id: noticeId } });
        })
        .catch(error => {
          console.error('공지사항 업데이트 중 오류 발생:', error);
        });
    },
    cancelEdit() {
      const noticeId = this.$route.params.id;
      this.$router.push({ name: 'NoticeDetail', params: { id: noticeId } });
    }
  }
};
</script>


<style scoped>
.notice-edit {
  max-width: 800px;
  margin: 0 auto;
  padding: 2rem;
}

h2 {
  font-size: 2rem;
  color: #333;
  margin-bottom: 1rem;
  text-align: center;
}

.form-group {
  margin-bottom: 1.5rem;
}

label {
  display: block;
  font-size: 1rem;
  color: #333;
  margin-bottom: 0.5rem;
}

input[type="text"], textarea {
  width: 100%;
  padding: 0.5rem;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 1rem;
}

textarea {
  height: 150px;
  resize: vertical;
}

.form-actions {
  display: flex;
  justify-content: space-between;
  gap: 1rem;
  margin-top: 1.5rem;
}

button {
  border: none;
  border-radius: 4px;
  padding: 0.5rem 1rem;
  font-size: 1rem;
  cursor: pointer;
  transition: background-color 0.3s;
}

.btn-submit {
  background-color: #3498db;
  color: #fff;
}

.btn-submit:hover {
  background-color: #2980b9;
}

.btn-cancel {
  background-color: #e74c3c;
  color: #fff;
}

.btn-cancel:hover {
  background-color: #c0392b;
}
</style>
