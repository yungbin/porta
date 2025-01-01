<template>
  <section class="notice-detail">
    <div class="content-container">
      <h2>{{ notice.TITLE }}</h2>
      <div class="meta-info">
        <div class="author-info">
          <i class="fas fa-user-circle"></i>
          <span>{{ notice.WRITER }}</span>
        </div>
        <div class="date-info">
          <i class="fas fa-calendar-alt"></i>
          <span>{{ formatDate(notice.CREATEDATE) }}</span>
        </div>
      </div>
      <div class="notice-content">
        <p>{{ notice.CONTENT }}</p>
      </div>
    </div>
    <div class="action-buttons">
      <router-link to="/noticeList" class="btn btn-secondary">목록</router-link>
      <div v-if="canEditOrDelete" class="edit-delete-buttons">
        <button class="btn btn-primary" @click="goToEditPage">수정</button>
        <button class="btn btn-danger" @click="deleteNotice">삭제</button>
      </div>
    </div>
  </section>
</template>

<script>
import axios from 'axios';
import { computed } from 'vue';
import { useStore } from 'vuex';

export default {
  name: 'NoticeDetail',
  data() {
    return {
      notice: {},
      comments: [],
      newComment: '',
      replyForm: {},
      replyText: {}
    };
  },
  computed: {
    canEditOrDelete() {
      return this.role === 'ADMIN';
    }
  },
  created() {
    this.fetchNoticeDetail();
  },
  setup() {
    const store = useStore();
    const user = computed(() => store.getters.user);
    const role = computed(() => store.getters.role);

    return {
      user,
      role,
    };
  },
  methods: {
    fetchNoticeDetail() {
      const noticeId = this.$route.params.id;
      axios.get(`/api/notice/detail/${noticeId}`)
        .then(response => {
          this.notice = response.data;
        })
        .catch(error => {
          console.error('공지 세부정보를 가져오는 중 오류 발생:', error);
        });
    },
    formatDate(dateString) {
      const options = { year: 'numeric', month: 'long', day: 'numeric' };
      return new Date(dateString).toLocaleDateString(undefined, options);
    },
    goToEditPage() {
      this.$router.push({ name: 'NoticeEdit', params: { id: this.$route.params.id } });
    },
    deleteNotice() {
      const noticeId = this.$route.params.id;
      if (confirm('정말로 이 공지를 삭제하시겠습니까?')) {
        axios.delete(`/api/notice/delete/${noticeId}`)
          .then(() => {
            alert('공지가 삭제되었습니다.');
            this.$router.push({ name: 'NoticeList' });           })
          .catch(error => {
            console.error('공지 삭제하는 중 오류 발생:', error);
          });
      }
    }
  },
};
</script>

<style scoped>
.notice-detail {
  max-width: 800px;
  margin: 2rem auto;
  background-color: #ffffff;
  border-radius: 12px;
  box-shadow: 0 5px 20px rgba(0, 0, 0, 0.1);
  overflow: hidden;
}

.content-container {
  padding: 2rem;
}

h2 {
  font-size: 2.2rem;
  color: #2c3e50;
  margin-bottom: 1rem;
  font-weight: 700;
}

.meta-info {
  display: flex;
  justify-content: space-between;
  margin-bottom: 1.5rem;
  font-size: 0.9rem;
  color: #7f8c8d;
}

.author-info, .date-info {
  display: flex;
  align-items: center;
}

.author-info i, .date-info i {
  margin-right: 0.5rem;
  font-size: 1.2rem;
}

.notice-content {
  font-size: 1.1rem;
  color: #34495e;
  line-height: 1.6;
  margin-bottom: 2rem;
}

.interaction-stats {
  display: flex;
  justify-content: flex-start;
  gap: 2rem;
  margin-bottom: 1.5rem;
}

.stat-item {
  display: flex;
  align-items: center;
  font-size: 1rem;
  color: #7f8c8d;
}

.stat-item i {
  margin-right: 0.5rem;
  font-size: 1.2rem;
}

.action-buttons {
  display: flex;
  justify-content: space-between;
  padding: 1rem 2rem;
  background-color: #f8f9fa;
  border-top: 1px solid #e9ecef;
}

.btn {
  padding: 0.6rem 1.2rem;
  border: none;
  border-radius: 5px;
  font-size: 1rem;
  cursor: pointer;
  transition: all 0.3s ease;
}

.btn-secondary {
  background-color: #6c757d;
  color: #ffffff;
  text-decoration: none;
}

.btn-primary {
  background-color: #007bff;
  color: #ffffff;
}

.btn-danger {
  background-color: #dc3545;
  color: #ffffff;
}

.btn:hover {
  opacity: 0.9;
  transform: translateY(-2px);
}

.edit-delete-buttons {
  display: flex;
  gap: 1rem;
}

@media (max-width: 600px) {
  .notice-detail {
    margin: 1rem;
  }

  .content-container {
    padding: 1.5rem;
  }

  h2 {
    font-size: 1.8rem;
  }

  .interaction-stats {
    flex-wrap: wrap;
    gap: 1rem;
  }

  .action-buttons {
    flex-direction: column;
    gap: 1rem;
  }

  .btn {
    width: 100%;
  }
}
</style>