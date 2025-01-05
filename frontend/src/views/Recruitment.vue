<template>
  <section class="notice-list">
    <h2>파티원 모집</h2>
    <div v-if="canEditOrDelete">
      <div class="create-notice-container">
        <router-link to="noticeCreate" class="btn-create-notice">공지 등록</router-link>
      </div>
    </div>
    <div class="notice-grid">
      <router-link 
        v-for="notice in paginatedNotices" 
        :key="notice.ID" 
        :to="{ name: 'NoticeDetail', params: { id: notice.ID } }"
        class="notice-card-link"
      >
        <article class="notice-card">
          <h3 class="notice-title">{{ notice.TITLE }}</h3>
          <p class="notice-content">{{ notice.CONTENT }}</p>
          <div class="notice-meta">
            <div class="notice-author">
              <i class="fas fa-user"></i>
              <span>{{ notice.WRITER }}</span>
            </div>
          </div>
          <div class="notice-date">
              <i class="fas fa-calendar-alt"></i>
              <span>{{ formatDate(notice.CREATEDATE) }}</span>
            </div>
        </article>
      </router-link>
    </div>

    <div class="pagination">
      <button @click="changePage(currentPage - 1)" :disabled="currentPage === 1">Previous</button>
      
      <button v-for="page in pageNumbers" 
              :key="page" 
              @click="changePage(page)"
              :class="{ 'active': page === currentPage }">
        {{ page }}
      </button>
      
      <button @click="changePage(currentPage + 1)" :disabled="currentPage === totalPages">Next</button>
    </div>
  </section>
</template>

<script>
import axios from 'axios';
import { computed } from 'vue';
import { useStore } from 'vuex';

export default {
  name: 'Recruitment',
  data() {
    return {
      notices: [],        // 전체 공지 리스트
      currentPage: 1,     // 현재 페이지
      pageSize: 5         // 페이지당 공지 개수
    };
  },
  computed: {
    totalPages() {
      return Math.ceil(this.notices.length / this.pageSize);
    },
    paginatedNotices() {
      const start = (this.currentPage - 1) * this.pageSize;
      const end = start + this.pageSize;
      return this.notices.slice(start, end);
    },
    pageNumbers() {
      const pages = [];
      for (let i = 1; i <= this.totalPages; i++) {
        pages.push(i);
      }
      return pages;
    },
    canEditOrDelete() {
      return this.role === 'ADMIN';
    }
  },
  created() {
    this.fetchNoticeList();
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
    fetchNoticeList() {
      axios.get('/api/notice/list') // 경로를 절대 경로로 변경
        .then(response => {
          this.notices = response.data;
        })
        .catch(error => {
          console.error('Error fetching notice list:', error);
        });
    },
    formatDate(dateString) {
      const options = { year: 'numeric', month: 'long', day: 'numeric' };
      return new Date(dateString).toLocaleDateString(undefined, options);
    },
    changePage(page) {
      if (page >= 1 && page <= this.totalPages) {
        this.currentPage = page;
      }
    }
  }
};
</script>

<style scoped>
.notice-list {
  max-width: 800px; /* 너비를 줄여서 한 줄에 하나씩 보이게 */
  margin: 0 auto;
  padding: 2rem;
}

h2 {
  font-size: 2rem; /* 제목 크기 조정 */
  color: #333;
  margin-bottom: 1.5rem;
  text-align: center;
}

/* 공지 등록 버튼 스타일 */
.create-notice-container {
  display: flex;
  justify-content: flex-end;
  margin-bottom: 1.5rem;
}

.btn-create-notice {
  background-color: #3a86ff;
  color: #fff;
  padding: 0.5rem 1rem;
  border-radius: 5px;
  text-decoration: none;
  display: inline-flex;
  align-items: center;
  gap: 0.5rem;
  transition: background-color 0.3s ease;
}

.btn-create-notice:hover {
  background-color: #2563eb;
}

.notice-grid {
  display: flex;
  flex-direction: column; /* 세로로 나열 */
  gap: 2rem; /* 카드 간의 간격 */
}

.notice-card-link {
  text-decoration: none;
}

.notice-card {
  background-color: #fff;
  border-radius: 12px;
  padding: 1.5rem;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.3);
  transition: all 0.3s ease;
}

.notice-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 6px 12px rgba(0, 0, 0, 0.6);
}

.notice-title {
  font-size: 1.6rem; /* 제목 크기 조정 */
  color: #2c3e50;
  margin-bottom: 0.5rem; /* 제목과 내용 간격 조정 */
}

.notice-content {
  font-size: 1.1rem; /* 내용 크기 조정 */
  color: #34495e;
  margin-bottom: 1rem;
  line-height: 1.6;
}

.notice-meta {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
  font-size: 0.9rem;
  color: #7f8c8d;
}

.notice-author {
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

.notice-date {
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

i {
  font-size: 1rem;
  color: #3498db;
}

/* Pagination styles */
.pagination {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 0.5rem;
  margin-top: 2rem;
}

.pagination button {
  background-color: #3498db;
  color: #fff;
  border: none;
  border-radius: 4px;
  padding: 0.5rem 1rem;
  cursor: pointer;
  transition: background-color 0.3s;
}

.pagination button:disabled {
  background-color: #bdc3c7;
  cursor: not-allowed;
}

.pagination button:hover:not(:disabled) {
  background-color: #2980b9;
}

.pagination button.active {
  background-color: #2980b9;
  font-weight: bold;
}
</style>
