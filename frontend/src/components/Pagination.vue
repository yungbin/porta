<template>
  <div class="pagination">
    <button @click="changePage(currentPage - 1)" :disabled="currentPage === 1">Previous</button>

    <span v-for="page in computedPages" :key="page" class="page-item">
      <button
        v-if="page !== '...'"
        @click="changePage(page)"
        :class="{ 'active': page === currentPage }"
      >
        {{ page }}
      </button>
      <span v-else>{{ page }}</span>
    </span>

    <button @click="changePage(currentPage + 1)" :disabled="currentPage === totalPages">Next</button>
  </div>
</template>

<script>
export default {
  name: 'Pagination',
  props: {
    currentPage: {
      type: Number,
      required: true,
    },
    totalPages: {
      type: Number,
      required: true,
    },
  },
  computed: {
    computedPages() {
      const total = this.totalPages;
      const current = this.currentPage;
      const pages = [];

      if (total <= 5) {
        for (let i = 1; i <= total; i++) {
          pages.push(i);
        }
      } else {
        if (current <= 3) {
          pages.push(1, 2, 3, 4, 5, '...', total);
        } else if (current >= total - 2) {
          pages.push(1, '...', total - 4, total - 3, total - 2, total - 1, total);
        } else {
          pages.push(1, '...', current - 2, current - 1, current, current + 1, current + 2, '...', total);
        }
      }

      return pages;
    },
  },
  methods: {
    changePage(page) {
      if (typeof page === 'number' && page >= 1 && page <= this.totalPages) {
        this.$emit('page-changed', page);  // 부모 컴포넌트에 페이지 변경 이벤트 전달
      }
    },
  },
};
</script>

<style scoped>
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

.pagination span {
  font-size: 1.2rem;
  padding: 0.5rem;
}
</style>
